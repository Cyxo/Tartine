package tartine.vm;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tartine.dex.Descriptors;
import tartine.dex.DexClass;
import tartine.dex.DexMethod;
import tartine.dex.DexSet;
import tartine.fw.VirtualCall;
import tartine.dex.Insn;
import tartine.fw.FwIndex;
import tartine.runtime.Vfs;

/**
 * Tree-walking DEX interpreter over our IR.
 *
 * <p>Value model: registers are object slots. Integers (and bytes, shorts,
 * chars, booleans) are normalized to {@link Integer}, {@code long} to
 * {@link Long}, {@code float} to {@link Float}, {@code double} to
 * {@link Double}; heap values are {@link VmObject}/{@link VmArray}, strings
 * are host strings, {@code null} is null. Wide values occupy one slot; the
 * nominal second slot is never read or written.
 *
 * <p>Same-dex calls recurse with fresh frames. Static state lives here and
 * {@code <clinit>} runs on first active use. Anything else — unknown
 * opcodes, missing classes, framework members — throws {@link VmException}
 * naming the gap. DEX {@code throw} unwinds via {@link VmThrow} until a
 * typed handler or catch-all claims it; host failures stay uncaught by DEX
 * handlers for now. Monitor ops are no-ops (single thread).
 */
public final class Interpreter {
    private static final int ACC_STATIC = 0x0008;

    private final DexSet dexes;
    private final Map<String, Object> statics = new HashMap<>();
    private final Set<String> initialized = new HashSet<>();
    private final Set<String> initializing = new HashSet<>();
    private final Map<String, HostMethod> builtins = new HashMap<>();
    private final Map<VmObject, PendingThrowable> throwables = new IdentityHashMap<>();
    private final Map<Object, List<Object>> suppressed = new IdentityHashMap<>();
    private FwIndex framework;
    private long steps = 0;
    private long maxSteps = Long.MAX_VALUE;
    private VmLooper mainLooper;
    private VmQueue mainQueue;
    private VmObject choreographer;
    private VmObject defaultLocales;
    // Reported Android API level. Defaults to the modeled framework
    // (android-36); Main overrides it from the APK manifest per app.
    private int sdkInt = 36;
    private final String[] trace = new String[16384];
    private int traceAt = 0;
    /**
     * Filesystem seam for the {@code java.io.File} bridge (Vfs mapping +
     * sdcard write policy). Snapshotted at construction; package-visible
     * so tests can point it at temp directories.
     */
    Vfs.Policy sdcardPolicy;

    public Interpreter(DexSet dexes) {
        this.dexes = dexes;
        // Capture the filesystem seam once (HostConfig applies any config
        // or TARTINE_SDCARD override before constructors run; tests may
        // re-seat the field for temp-root behavior).
        this.sdcardPolicy = Vfs.policy();
        registerBuiltins();
        // Reverse bridge for framework mirror virtual dispatch (see
        // guestVirtualCall): mirror call sites reach guest overrides here.
        VirtualCall.install(this::guestVirtualCall);
    }

    /** Framework type universe (hierarchy, signatures, constants). Optional. */
    public void setFramework(FwIndex framework) {
        this.framework = framework;
    }

    /** Host-implemented method: framework behavior in plain Java. */
    public interface HostMethod {
        Object invoke(Object receiver, Object[] args);
    }

    /** Throwable allocated before its message is known (seen at {@code <init>}). */
    static final class PendingThrowable {
        final Class<?> clazz;
        String message;

        PendingThrowable(Class<?> clazz) {
            this.clazz = clazz;
        }
    }

    /** Instructions executed by the last top-level {@link #call} or {@link #invoke}. */
    public long steps() {
        return steps;
    }

    /** Aborts execution past this many steps (infinite-loop guard for probes). */
    public void setMaxSteps(long maxSteps) {
        this.maxSteps = maxSteps;
    }

    /** Last executed instructions, oldest first (ring for gap diagnosis). */
    public List<String> lastTrace() {
        List<String> out = new ArrayList<>();
        int count = Math.min(traceAt, trace.length);
        for (int i = 0; i < count; i++) {
            out.add(trace[(traceAt - count + i) & (trace.length - 1)]);
        }
        return out;
    }

    /**
     * Calls a static method by name, matched on argument count (ambiguous or
     * missing targets throw).
     */
    public Object call(String type, String methodName, Object... args) {
        DexClass cls = dexes.findClass(type);
        if (cls == null) {
            throw new VmException("no such class: " + type);
        }
        DexMethod target = null;
        for (DexMethod candidate : cls.methods) {
            if (candidate.name.equals(methodName)
                    && candidate.parameterTypes.size() == args.length) {
                if (target != null) {
                    throw new VmException("ambiguous method: " + methodName);
                }
                target = candidate;
            }
        }
        if (target == null) {
            throw new VmException("no such method: " + type + "->" + methodName);
        }
        steps = 0;
        return execute(cls, target, null, args);
    }

    /** Invokes a resolved method with an explicit receiver (null for static).
     * Steps accumulate across calls; {@link #call} resets the counter. */
    public Object invoke(DexClass cls, DexMethod method, Object receiver, Object... args) {
        return execute(cls, method, receiver, args);
    }

    private Object execute(DexClass cls, DexMethod method, Object thisRef, Object[] args) {
        if (!method.hasImplementation) {
            throw new VmException("no code: " + method.signature());
        }
        boolean isStatic = (method.accessFlags & ACC_STATIC) != 0;
        if (!isStatic && thisRef == null) {
            throw new VmException("no receiver: " + method.signature());
        }
        if (args.length != method.parameterTypes.size()) {
            throw new VmException("arity mismatch: " + method.signature());
        }
        int words = isStatic ? 0 : 1;
        for (String param : method.parameterTypes) {
            words += Descriptors.isWide(param) ? 2 : 1;
        }
        Object[] regs = new Object[method.registerCount];
        int reg = regs.length - words;
        if (reg < 0) {
            throw new VmException("register overflow: " + method.signature());
        }
        if (!isStatic) {
            regs[reg++] = thisRef;
        }
        for (int i = 0; i < args.length; i++) {
            regs[reg] = args[i];
            reg += Descriptors.isWide(method.parameterTypes.get(i)) ? 2 : 1;
        }

        List<Insn> code = method.instructions;
        Map<Integer, Integer> addresses = new HashMap<>();
        for (int i = 0; i < code.size(); i++) {
            addresses.put(code.get(i).codeOffset, i);
        }
        // Lexical home for invoke-super: the method's owner when known.
        DexClass lexical = cls;
        if (method.owner != null) {
            DexClass owned = dexes.findClass(method.owner);
            if (owned != null) {
                lexical = owned;
            }
        }
        Object pending = null;
        boolean hasPending = false;
        int pc = 0;
        while (true) {
            int here = pc;
            try {
                Insn insn = code.get(pc);
                steps++;
                if (steps > maxSteps) {
                    throw new VmException("step budget exceeded");
                }
                trace[traceAt & (trace.length - 1)] = (method.owner == null ? "?"
                        : method.owner) + "." + method.signature()
                        + "@" + insn.codeOffset + ":" + insn.opcode;
                traceAt++;
                String op = insn.opcode;
                List<Integer> g = insn.registers;
                boolean consumesPending = op.startsWith("move-result") || op.equals("move-exception");
                boolean producesPending = op.startsWith("invoke-")
                        || op.equals("new-instance")
                        || op.startsWith("filled-new-array");
                if (!consumesPending && !producesPending) {
                    hasPending = false;
                }
                switch (op) {
                    // A real nop is one code unit. Payload tables arrive as
                    // distinct opcodes (packed-switch-payload, ...) and never
                    // execute: switches read them as data, a later step.
                    case "nop":
                        pc++;
                        break;                    case "const-4":
                    case "const-16":
                    case "const":
                        regs[g.get(0)] = (int) insn.literal;
                        pc++;
                        break;
                    // Hat literals arrive pre-shifted from dexlib2
                    // (narrow << 16, wide << 48: verified via javap).
                    case "const-high16":
                        regs[g.get(0)] = (int) insn.literal;
                        pc++;
                        break;
                    case "const-wide-high16":
                        regs[g.get(0)] = insn.literal;
                        pc++;
                        break;
                    case "const-wide-16":
                    case "const-wide-32":
                    case "const-wide":
                        regs[g.get(0)] = insn.literal;
                        pc++;
                        break;
                    case "const-string":
                    case "const-string-jumbo":
                        regs[g.get(0)] = insn.reference;
                        pc++;
                        break;
                    case "const-class":
                        regs[g.get(0)] = new VmClass(insn.reference);
                        pc++;
                        break;
                    case "move":
                    case "move-from16":
                    case "move-16":
                    case "move-object":
                    case "move-object-from16":
                    case "move-object-16":
                    case "move-wide":
                    case "move-wide-from16":
                    case "move-wide-16":
                        regs[g.get(0)] = regs[g.get(1)];
                        pc++;
                        break;
                    case "move-result":
                    case "move-result-wide":
                    case "move-result-object":
                    case "move-exception":
                        if (!hasPending) {
                            throw new VmException(op + " without value");
                        }
                        regs[g.get(0)] = pending;
                        hasPending = false;
                        pc++;
                        break;
                    case "return-void":
                        return null;
                    case "return":
                    case "return-wide":
                    case "return-object":
                        return regs[g.get(0)];
                    case "add-int":
                        regs[g.get(0)] = asInt(regs[g.get(1)]) + asInt(regs[g.get(2)]);
                        pc++;
                        break;
                    case "sub-int":
                        regs[g.get(0)] = asInt(regs[g.get(1)]) - asInt(regs[g.get(2)]);
                        pc++;
                        break;
                    case "mul-int":
                        regs[g.get(0)] = asInt(regs[g.get(1)]) * asInt(regs[g.get(2)]);
                        pc++;
                        break;
                    case "div-int":
                        regs[g.get(0)] = asInt(regs[g.get(1)]) / asInt(regs[g.get(2)]);
                        pc++;
                        break;
                    case "rem-int":
                        regs[g.get(0)] = asInt(regs[g.get(1)]) % asInt(regs[g.get(2)]);
                        pc++;
                        break;
                    case "and-int":
                        regs[g.get(0)] = asInt(regs[g.get(1)]) & asInt(regs[g.get(2)]);
                        pc++;
                        break;
                    case "or-int":
                        regs[g.get(0)] = asInt(regs[g.get(1)]) | asInt(regs[g.get(2)]);
                        pc++;
                        break;
                    case "xor-int":
                        regs[g.get(0)] = asInt(regs[g.get(1)]) ^ asInt(regs[g.get(2)]);
                        pc++;
                        break;
                    case "shl-int":
                        regs[g.get(0)] = asInt(regs[g.get(1)]) << asInt(regs[g.get(2)]);
                        pc++;
                        break;
                    case "shr-int":
                        regs[g.get(0)] = asInt(regs[g.get(1)]) >> asInt(regs[g.get(2)]);
                        pc++;
                        break;
                    case "ushr-int":
                        regs[g.get(0)] = asInt(regs[g.get(1)]) >>> asInt(regs[g.get(2)]);
                        pc++;
                        break;
                    case "add-int-2addr":
                        regs[g.get(0)] = asInt(regs[g.get(0)]) + asInt(regs[g.get(1)]);
                        pc++;
                        break;
                    case "sub-int-2addr":
                        regs[g.get(0)] = asInt(regs[g.get(0)]) - asInt(regs[g.get(1)]);
                        pc++;
                        break;
                    case "mul-int-2addr":
                        regs[g.get(0)] = asInt(regs[g.get(0)]) * asInt(regs[g.get(1)]);
                        pc++;
                        break;
                    case "div-int-2addr":
                        regs[g.get(0)] = asInt(regs[g.get(0)]) / asInt(regs[g.get(1)]);
                        pc++;
                        break;
                    case "rem-int-2addr":
                        regs[g.get(0)] = asInt(regs[g.get(0)]) % asInt(regs[g.get(1)]);
                        pc++;
                        break;
                    case "and-int-2addr":
                        regs[g.get(0)] = asInt(regs[g.get(0)]) & asInt(regs[g.get(1)]);
                        pc++;
                        break;
                    case "or-int-2addr":
                        regs[g.get(0)] = asInt(regs[g.get(0)]) | asInt(regs[g.get(1)]);
                        pc++;
                        break;
                    case "xor-int-2addr":
                        regs[g.get(0)] = asInt(regs[g.get(0)]) ^ asInt(regs[g.get(1)]);
                        pc++;
                        break;
                    case "shl-int-2addr":
                        regs[g.get(0)] = asInt(regs[g.get(0)]) << asInt(regs[g.get(1)]);
                        pc++;
                        break;
                    case "shr-int-2addr":
                        regs[g.get(0)] = asInt(regs[g.get(0)]) >> asInt(regs[g.get(1)]);
                        pc++;
                        break;
                    case "ushr-int-2addr":
                        regs[g.get(0)] = asInt(regs[g.get(0)]) >>> asInt(regs[g.get(1)]);
                        pc++;
                        break;
                    case "add-long":
                        regs[g.get(0)] = asLong(regs[g.get(1)]) + asLong(regs[g.get(2)]);
                        pc++;
                        break;
                    case "sub-long":
                        regs[g.get(0)] = asLong(regs[g.get(1)]) - asLong(regs[g.get(2)]);
                        pc++;
                        break;
                    case "mul-long":
                        regs[g.get(0)] = asLong(regs[g.get(1)]) * asLong(regs[g.get(2)]);
                        pc++;
                        break;
                    case "div-long":
                        regs[g.get(0)] = asLong(regs[g.get(1)]) / asLong(regs[g.get(2)]);
                        pc++;
                        break;
                    case "rem-long":
                        regs[g.get(0)] = asLong(regs[g.get(1)]) % asLong(regs[g.get(2)]);
                        pc++;
                        break;
                    case "and-long":
                        regs[g.get(0)] = asLong(regs[g.get(1)]) & asLong(regs[g.get(2)]);
                        pc++;
                        break;
                    case "or-long":
                        regs[g.get(0)] = asLong(regs[g.get(1)]) | asLong(regs[g.get(2)]);
                        pc++;
                        break;
                    case "xor-long":
                        regs[g.get(0)] = asLong(regs[g.get(1)]) ^ asLong(regs[g.get(2)]);
                        pc++;
                        break;
                    case "shl-long":
                        regs[g.get(0)] = asLong(regs[g.get(1)]) << asInt(regs[g.get(2)]);
                        pc++;
                        break;
                    case "shr-long":
                        regs[g.get(0)] = asLong(regs[g.get(1)]) >> asInt(regs[g.get(2)]);
                        pc++;
                        break;
                    case "ushr-long":
                        regs[g.get(0)] = asLong(regs[g.get(1)]) >>> asInt(regs[g.get(2)]);
                        pc++;
                        break;
                    case "add-long-2addr":
                        regs[g.get(0)] = asLong(regs[g.get(0)]) + asLong(regs[g.get(1)]);
                        pc++;
                        break;
                    case "sub-long-2addr":
                        regs[g.get(0)] = asLong(regs[g.get(0)]) - asLong(regs[g.get(1)]);
                        pc++;
                        break;
                    case "mul-long-2addr":
                        regs[g.get(0)] = asLong(regs[g.get(0)]) * asLong(regs[g.get(1)]);
                        pc++;
                        break;
                    case "div-long-2addr":
                        regs[g.get(0)] = asLong(regs[g.get(0)]) / asLong(regs[g.get(1)]);
                        pc++;
                        break;
                    case "rem-long-2addr":
                        regs[g.get(0)] = asLong(regs[g.get(0)]) % asLong(regs[g.get(1)]);
                        pc++;
                        break;
                    case "and-long-2addr":
                        regs[g.get(0)] = asLong(regs[g.get(0)]) & asLong(regs[g.get(1)]);
                        pc++;
                        break;
                    case "or-long-2addr":
                        regs[g.get(0)] = asLong(regs[g.get(0)]) | asLong(regs[g.get(1)]);
                        pc++;
                        break;
                    case "xor-long-2addr":
                        regs[g.get(0)] = asLong(regs[g.get(0)]) ^ asLong(regs[g.get(1)]);
                        pc++;
                        break;
                    case "shl-long-2addr":
                        regs[g.get(0)] = asLong(regs[g.get(0)]) << asInt(regs[g.get(1)]);
                        pc++;
                        break;
                    case "shr-long-2addr":
                        regs[g.get(0)] = asLong(regs[g.get(0)]) >> asInt(regs[g.get(1)]);
                        pc++;
                        break;
                    case "ushr-long-2addr":
                        regs[g.get(0)] = asLong(regs[g.get(0)]) >>> asInt(regs[g.get(1)]);
                        pc++;
                        break;
                    case "add-float":
                        regs[g.get(0)] = asFloatBits(regs[g.get(1)]) + asFloatBits(regs[g.get(2)]);
                        pc++;
                        break;
                    case "sub-float":
                        regs[g.get(0)] = asFloatBits(regs[g.get(1)]) - asFloatBits(regs[g.get(2)]);
                        pc++;
                        break;
                    case "mul-float":
                        regs[g.get(0)] = asFloatBits(regs[g.get(1)]) * asFloatBits(regs[g.get(2)]);
                        pc++;
                        break;
                    case "div-float":
                        regs[g.get(0)] = asFloatBits(regs[g.get(1)]) / asFloatBits(regs[g.get(2)]);
                        pc++;
                        break;
                    case "rem-float":
                        regs[g.get(0)] = asFloatBits(regs[g.get(1)]) % asFloatBits(regs[g.get(2)]);
                        pc++;
                        break;
                    case "add-float-2addr":
                        regs[g.get(0)] = asFloatBits(regs[g.get(0)]) + asFloatBits(regs[g.get(1)]);
                        pc++;
                        break;
                    case "sub-float-2addr":
                        regs[g.get(0)] = asFloatBits(regs[g.get(0)]) - asFloatBits(regs[g.get(1)]);
                        pc++;
                        break;
                    case "mul-float-2addr":
                        regs[g.get(0)] = asFloatBits(regs[g.get(0)]) * asFloatBits(regs[g.get(1)]);
                        pc++;
                        break;
                    case "div-float-2addr":
                        regs[g.get(0)] = asFloatBits(regs[g.get(0)]) / asFloatBits(regs[g.get(1)]);
                        pc++;
                        break;
                    case "rem-float-2addr":
                        regs[g.get(0)] = asFloatBits(regs[g.get(0)]) % asFloatBits(regs[g.get(1)]);
                        pc++;
                        break;
                    case "add-double":
                        regs[g.get(0)] = asDoubleBits(regs[g.get(1)]) + asDoubleBits(regs[g.get(2)]);
                        pc++;
                        break;
                    case "sub-double":
                        regs[g.get(0)] = asDoubleBits(regs[g.get(1)]) - asDoubleBits(regs[g.get(2)]);
                        pc++;
                        break;
                    case "mul-double":
                        regs[g.get(0)] = asDoubleBits(regs[g.get(1)]) * asDoubleBits(regs[g.get(2)]);
                        pc++;
                        break;
                    case "div-double":
                        regs[g.get(0)] = asDoubleBits(regs[g.get(1)]) / asDoubleBits(regs[g.get(2)]);
                        pc++;
                        break;
                    case "rem-double":
                        regs[g.get(0)] = asDoubleBits(regs[g.get(1)]) % asDoubleBits(regs[g.get(2)]);
                        pc++;
                        break;
                    case "add-double-2addr":
                        regs[g.get(0)] = asDoubleBits(regs[g.get(0)]) + asDoubleBits(regs[g.get(1)]);
                        pc++;
                        break;
                    case "sub-double-2addr":
                        regs[g.get(0)] = asDoubleBits(regs[g.get(0)]) - asDoubleBits(regs[g.get(1)]);
                        pc++;
                        break;
                    case "mul-double-2addr":
                        regs[g.get(0)] = asDoubleBits(regs[g.get(0)]) * asDoubleBits(regs[g.get(1)]);
                        pc++;
                        break;
                    case "div-double-2addr":
                        regs[g.get(0)] = asDoubleBits(regs[g.get(0)]) / asDoubleBits(regs[g.get(1)]);
                        pc++;
                        break;
                    case "rem-double-2addr":
                        regs[g.get(0)] = asDoubleBits(regs[g.get(0)]) % asDoubleBits(regs[g.get(1)]);
                        pc++;
                        break;
                    case "neg-long":
                        regs[g.get(0)] = -asLong(regs[g.get(1)]);
                        pc++;
                        break;
                    case "not-long":
                        regs[g.get(0)] = ~asLong(regs[g.get(1)]);
                        pc++;
                        break;
                    case "neg-float":
                        regs[g.get(0)] = -asFloatBits(regs[g.get(1)]);
                        pc++;
                        break;
                    case "neg-double":
                        regs[g.get(0)] = -asDoubleBits(regs[g.get(1)]);
                        pc++;
                        break;
                    case "add-int-lit8":
                    case "add-int-lit16":
                        regs[g.get(0)] = asInt(regs[g.get(1)]) + (int) insn.literal;
                        pc++;
                        break;
                    case "rsub-int":
                    case "rsub-int-lit8":
                        regs[g.get(0)] = (int) insn.literal - asInt(regs[g.get(1)]);
                        pc++;
                        break;
                    case "sub-int-lit8":
                    case "sub-int-lit16":
                        regs[g.get(0)] = asInt(regs[g.get(1)]) - (int) insn.literal;
                        pc++;
                        break;
                    case "mul-int-lit8":
                    case "mul-int-lit16":
                        regs[g.get(0)] = asInt(regs[g.get(1)]) * (int) insn.literal;
                        pc++;
                        break;
                    case "div-int-lit8":
                    case "div-int-lit16":
                        regs[g.get(0)] = asInt(regs[g.get(1)]) / (int) insn.literal;
                        pc++;
                        break;
                    case "rem-int-lit8":
                    case "rem-int-lit16":
                        regs[g.get(0)] = asInt(regs[g.get(1)]) % (int) insn.literal;
                        pc++;
                        break;
                    case "and-int-lit8":
                    case "and-int-lit16":
                        regs[g.get(0)] = asInt(regs[g.get(1)]) & (int) insn.literal;
                        pc++;
                        break;
                    case "or-int-lit8":
                    case "or-int-lit16":
                        regs[g.get(0)] = asInt(regs[g.get(1)]) | (int) insn.literal;
                        pc++;
                        break;
                    case "xor-int-lit8":
                    case "xor-int-lit16":
                        regs[g.get(0)] = asInt(regs[g.get(1)]) ^ (int) insn.literal;
                        pc++;
                        break;
                    case "shl-int-lit8":
                        regs[g.get(0)] = asInt(regs[g.get(1)]) << (int) insn.literal;
                        pc++;
                        break;
                    case "shr-int-lit8":
                        regs[g.get(0)] = asInt(regs[g.get(1)]) >> (int) insn.literal;
                        pc++;
                        break;
                    case "ushr-int-lit8":
                        regs[g.get(0)] = asInt(regs[g.get(1)]) >>> (int) insn.literal;
                        pc++;
                        break;
                    case "neg-int":
                        regs[g.get(0)] = -asInt(regs[g.get(1)]);
                        pc++;
                        break;
                    case "not-int":
                        regs[g.get(0)] = ~asInt(regs[g.get(1)]);
                        pc++;
                        break;
                    case "int-to-long":
                        regs[g.get(0)] = (long) asInt(regs[g.get(1)]);
                        pc++;
                        break;
                    case "int-to-float":
                        regs[g.get(0)] = (float) asInt(regs[g.get(1)]);
                        pc++;
                        break;
                    case "int-to-double":
                        regs[g.get(0)] = (double) asInt(regs[g.get(1)]);
                        pc++;
                        break;
                    case "int-to-byte":
                        regs[g.get(0)] = (int) (byte) asInt(regs[g.get(1)]);
                        pc++;
                        break;
                    case "int-to-char":
                        regs[g.get(0)] = (int) (char) asInt(regs[g.get(1)]);
                        pc++;
                        break;
                    case "int-to-short":
                        regs[g.get(0)] = (int) (short) asInt(regs[g.get(1)]);
                        pc++;
                        break;
                    case "long-to-int":
                        regs[g.get(0)] = (int) asLong(regs[g.get(1)]);
                        pc++;
                        break;
                    case "long-to-float":
                        regs[g.get(0)] = (float) asLong(regs[g.get(1)]);
                        pc++;
                        break;
                    case "long-to-double":
                        regs[g.get(0)] = (double) asLong(regs[g.get(1)]);
                        pc++;
                        break;
                    case "float-to-int":
                        regs[g.get(0)] = (int) asFloatBits(regs[g.get(1)]);
                        pc++;
                        break;
                    case "float-to-long":
                        regs[g.get(0)] = (long) asFloatBits(regs[g.get(1)]);
                        pc++;
                        break;
                    case "float-to-double":
                        regs[g.get(0)] = (double) asFloatBits(regs[g.get(1)]);
                        pc++;
                        break;
                    case "double-to-int":
                        regs[g.get(0)] = (int) asDoubleBits(regs[g.get(1)]);
                        pc++;
                        break;
                    case "double-to-long":
                        regs[g.get(0)] = (long) asDoubleBits(regs[g.get(1)]);
                        pc++;
                        break;
                    case "double-to-float":
                        regs[g.get(0)] = (float) asDoubleBits(regs[g.get(1)]);
                        pc++;
                        break;
                    case "cmp-long":
                        regs[g.get(0)] = Long.compare(asLong(regs[g.get(1)]), asLong(regs[g.get(2)]));
                        pc++;
                        break;
                    case "cmpl-float":
                        regs[g.get(0)] = compareFloat(asFloatBits(regs[g.get(1)]), asFloatBits(regs[g.get(2)]), -1);
                        pc++;
                        break;
                    case "cmpg-float":
                        regs[g.get(0)] = compareFloat(asFloatBits(regs[g.get(1)]), asFloatBits(regs[g.get(2)]), 1);
                        pc++;
                        break;
                    case "cmpl-double":
                        regs[g.get(0)] = compareDouble(asDoubleBits(regs[g.get(1)]), asDoubleBits(regs[g.get(2)]), -1);
                        pc++;
                        break;
                    case "cmpg-double":
                        regs[g.get(0)] = compareDouble(asDoubleBits(regs[g.get(1)]), asDoubleBits(regs[g.get(2)]), 1);
                        pc++;
                        break;
                    case "goto":
                    case "goto-16":
                    case "goto-32":
                        pc = jump(addresses, insn);
                        break;
                    case "if-eq":
                        pc = check(eq(regs[g.get(0)], regs[g.get(1)]), addresses, insn, pc);
                        break;
                    case "if-ne":
                        pc = check(!eq(regs[g.get(0)], regs[g.get(1)]), addresses, insn, pc);
                        break;
                    case "if-lt":
                        pc = check(asInt(regs[g.get(0)]) < asInt(regs[g.get(1)]), addresses, insn, pc);
                        break;
                    case "if-ge":
                        pc = check(asInt(regs[g.get(0)]) >= asInt(regs[g.get(1)]), addresses, insn, pc);
                        break;
                    case "if-gt":
                        pc = check(asInt(regs[g.get(0)]) > asInt(regs[g.get(1)]), addresses, insn, pc);
                        break;
                    case "if-le":
                        pc = check(asInt(regs[g.get(0)]) <= asInt(regs[g.get(1)]), addresses, insn, pc);
                        break;
                    case "if-eqz":
                        pc = check(isZero(regs[g.get(0)]), addresses, insn, pc);
                        break;
                    case "if-nez":
                        pc = check(!isZero(regs[g.get(0)]), addresses, insn, pc);
                        break;
                    case "if-ltz":
                        pc = check(asInt(regs[g.get(0)]) < 0, addresses, insn, pc);
                        break;
                    case "if-gez":
                        pc = check(asInt(regs[g.get(0)]) >= 0, addresses, insn, pc);
                        break;
                    case "if-gtz":
                        pc = check(asInt(regs[g.get(0)]) > 0, addresses, insn, pc);
                        break;
                    case "if-lez":
                        pc = check(asInt(regs[g.get(0)]) <= 0, addresses, insn, pc);
                        break;
                    case "packed-switch":
                    case "sparse-switch": {
                        int test = asInt(regs[g.get(0)]);
                        Integer dest = addresses.get(insn.codeOffset + insn.branchOffset);
                        if (dest == null) {
                            throw new VmException("switch without payload");
                        }
                        Insn payload = code.get(dest);
                        String want = op.equals("packed-switch") ? "packed-switch-payload"
                                : "sparse-switch-payload";
                        if (!payload.opcode.equals(want) || payload.payloadKeys == null
                                || payload.payloadTargets == null) {
                            throw new VmException("bad switch payload");
                        }
                        Integer go = null;
                        for (int i = 0; i < payload.payloadKeys.size(); i++) {
                            if (payload.payloadKeys.get(i) == test) {
                                go = insn.codeOffset + payload.payloadTargets.get(i);
                                break;
                            }
                        }
                        if (go == null) {
                            pc++;
                            break;
                        }
                        Integer at = addresses.get(go);
                        if (at == null) {
                            throw new VmException("bad switch target");
                        }
                        pc = at;
                        break;
                    }
                    case "new-instance": {
                        Object created = createInstance(insn.reference);
                        if (created == null) {
                            DexClass target = dexes.findClass(insn.reference);
                            if (target == null) {
                                throw new VmException("unimplemented: " + insn.reference);
                            }
                            ensureInit(target);
                            created = new VmObject(target);
                        }
                        regs[g.get(0)] = created;
                        pending = created;
                        hasPending = true;
                        pc++;
                        break;
                    }
                    case "check-cast": {
                        Object value = regs[g.get(0)];
                        if (!isNullish(value) && !isInstance(value, insn.reference)) {
                            throw new ClassCastException(
                                    insn.reference + " <- " + valueType(value));
                        }
                        pc++;
                        break;
                    }
                    case "instance-of":
                        regs[g.get(0)] = toInt(instanceOf(regs[g.get(1)], insn.reference));
                        pc++;
                        break;
                    case "array-length": {
                        Object value = regs[g.get(1)];
                        if (value == null) {
                            throw new NullPointerException();
                        }
                        if (!(value instanceof VmArray)) {
                            throw new VmException("not an array");
                        }
                        regs[g.get(0)] = ((VmArray) value).length();
                        pc++;
                        break;
                    }
                    case "new-array": {
                        String element = arrayElement(insn.reference);
                        int size = asInt(regs[g.get(1)]);
                        if (size < 0) {
                            throw new NegativeArraySizeException();
                        }
                        regs[g.get(0)] = new VmArray(element, size);
                        pc++;
                        break;
                    }
                    case "filled-new-array":
                    case "filled-new-array-range": {
                        String element = arrayElement(insn.reference);
                        if (Descriptors.isWide(element)) {
                            throw new VmException("unsupported opcode: " + op + " wide");
                        }
                        VmArray array = new VmArray(element, g.size());
                        for (int i = 0; i < g.size(); i++) {
                            array.set(i, aputValue(array, regs[g.get(i)]));
                        }
                        pending = array;
                        hasPending = true;
                        pc++;
                        break;
                    }
                    case "fill-array-data": {
                        VmArray array = checkedArray(regs[g.get(0)]);
                        Integer dest = addresses.get(insn.codeOffset + insn.branchOffset);
                        if (dest == null) {
                            throw new VmException("fill without payload");
                        }
                        Insn payload = code.get(dest);
                        if (!payload.opcode.equals("array-payload")
                                || payload.payloadData == null) {
                            throw new VmException("bad fill payload");
                        }
                        if (payload.payloadData.size() != array.length()) {
                            throw new VmException("fill size mismatch: "
                                    + payload.payloadData.size() + " into "
                                    + array.length());
                        }
                        if (payload.elementWidth != fillWidth(array.elementType)) {
                            throw new VmException("fill width mismatch: "
                                    + payload.elementWidth + " into " + array.elementType);
                        }
                        for (int i = 0; i < array.length(); i++) {
                            long raw = payload.payloadData.get(i);
                            switch (array.elementType) {
                                case "Z":
                                    array.set(i, (int) raw & 1);
                                    break;
                                case "B":
                                    array.set(i, (int) (byte) raw);
                                    break;
                                case "S":
                                    array.set(i, (int) (short) raw);
                                    break;
                                case "C":
                                    array.set(i, (int) (char) raw);
                                    break;
                                case "I":
                                    array.set(i, (int) raw);
                                    break;
                                case "J":
                                    array.set(i, raw);
                                    break;
                                case "F":
                                    array.set(i, (int) raw);
                                    break;
                                case "D":
                                    array.set(i, raw);
                                    break;
                                default:
                                    throw new VmException(
                                            "fill into " + array.elementType);
                            }
                        }
                        pc++;
                        break;
                    }
                    case "aget":
                        regs[g.get(0)] = asInt(at(regs[g.get(1)], regs[g.get(2)]));
                        pc++;
                        break;
                    case "aget-wide":
                        regs[g.get(0)] = asLong(at(regs[g.get(1)], regs[g.get(2)]));
                        pc++;
                        break;
                    case "aget-object":
                        regs[g.get(0)] = at(regs[g.get(1)], regs[g.get(2)]);
                        pc++;
                        break;
                    case "aget-boolean":
                    case "aget-byte":
                    case "aget-char":
                    case "aget-short":
                        regs[g.get(0)] = asInt(at(regs[g.get(1)], regs[g.get(2)]));
                        pc++;
                        break;
                    case "aput":
                        at(regs[g.get(1)], regs[g.get(2)],
                                aputValue(regs[g.get(1)], regs[g.get(0)]));
                        pc++;
                        break;
                    case "aput-wide":
                        at(regs[g.get(1)], regs[g.get(2)],
                                aputValue(regs[g.get(1)], regs[g.get(0)]));
                        pc++;
                        break;
                    case "aput-object":
                        at(regs[g.get(1)], regs[g.get(2)], regs[g.get(0)]);
                        pc++;
                        break;
                    case "aput-boolean":
                        at(regs[g.get(1)], regs[g.get(2)], asInt(regs[g.get(0)]) & 1);
                        pc++;
                        break;
                    case "aput-byte":
                        at(regs[g.get(1)], regs[g.get(2)], (int) (byte) asInt(regs[g.get(0)]));
                        pc++;
                        break;
                    case "aput-char":
                        at(regs[g.get(1)], regs[g.get(2)], (int) (char) asInt(regs[g.get(0)]));
                        pc++;
                        break;
                    case "aput-short":
                        at(regs[g.get(1)], regs[g.get(2)], (int) (short) asInt(regs[g.get(0)]));
                        pc++;
                        break;
                    case "iget":
                        regs[g.get(0)] = fieldExact(regs[g.get(1)], insn.reference);
                        pc++;
                        break;
                    case "iget-boolean":
                    case "iget-byte":
                    case "iget-char":
                    case "iget-short":
                        regs[g.get(0)] = fieldInt(regs[g.get(1)], insn.reference);
                        pc++;
                        break;
                    case "iget-wide":
                        regs[g.get(0)] = fieldWide(regs[g.get(1)], insn.reference);
                        pc++;
                        break;
                    case "iget-object":
                        regs[g.get(0)] = field(regs[g.get(1)], insn.reference);
                        pc++;
                        break;
                    case "iput":
                        field(regs[g.get(1)], insn.reference, regs[g.get(0)]);
                        pc++;
                        break;
                    case "iput-boolean":
                        field(regs[g.get(1)], insn.reference, asInt(regs[g.get(0)]) & 1);
                        pc++;
                        break;
                    case "iput-wide":
                        field(regs[g.get(1)], insn.reference, regs[g.get(0)]);
                        pc++;
                        break;
                    case "iput-object":
                        field(regs[g.get(1)], insn.reference, regs[g.get(0)]);
                        pc++;
                        break;
                    case "iput-byte":
                        field(regs[g.get(1)], insn.reference, (int) (byte) asInt(regs[g.get(0)]));
                        pc++;
                        break;
                    case "iput-char":
                        field(regs[g.get(1)], insn.reference, (int) (char) asInt(regs[g.get(0)]));
                        pc++;
                        break;
                    case "iput-short":
                        field(regs[g.get(1)], insn.reference, (int) (short) asInt(regs[g.get(0)]));
                        pc++;
                        break;
                    case "sget":
                    case "sget-boolean":
                    case "sget-byte":
                    case "sget-char":
                    case "sget-short":
                        regs[g.get(0)] = staticInt(insn.reference);
                        pc++;
                        break;
                    case "sget-wide":
                        regs[g.get(0)] = staticLong(insn.reference);
                        pc++;
                        break;
                    case "sget-object":
                        regs[g.get(0)] = staticRef(insn.reference);
                        pc++;
                        break;
                    case "sput":
                        staticPut(insn.reference, regs[g.get(0)]);
                        pc++;
                        break;
                    case "sput-boolean":
                        staticPut(insn.reference, asInt(regs[g.get(0)]) & 1);
                        pc++;
                        break;
                    case "sput-wide":
                        staticPut(insn.reference, regs[g.get(0)]);
                        pc++;
                        break;
                    case "sput-object":
                        staticPut(insn.reference, regs[g.get(0)]);
                        pc++;
                        break;
                    case "sput-byte":
                        staticPut(insn.reference, (int) (byte) asInt(regs[g.get(0)]));
                        pc++;
                        break;
                    case "sput-char":
                        staticPut(insn.reference, (int) (char) asInt(regs[g.get(0)]));
                        pc++;
                        break;
                    case "sput-short":
                        staticPut(insn.reference, (int) (short) asInt(regs[g.get(0)]));
                        pc++;
                        break;
                    case "throw": {
                        Object value = regs[g.get(0)];
                        if (isNullish(value)) {
                            throw new NullPointerException("throw null");
                        }
                        throw new VmThrow(value);
                    }
                    case "monitor-enter":
                    case "monitor-exit":
                        pc++;
                        break;
                    case "invoke-static":
                    case "invoke-static-range": {
                        MethodRef ref = MethodRef.parse(insn.reference);
                        Resolved resolved = resolve(null, ref.definingClass, ref, true);
                        if (resolved.dexMethod != null) {
                            if ((resolved.dexMethod.accessFlags & ACC_STATIC) == 0) {
                                throw new VmException("static mismatch: " + insn.reference);
                            }
                            ensureInit(resolved.dexHome);
                            Object[] callArgs = readArgs(resolved.dexMethod, g, 0, regs);
                            Object result = execute(resolved.dexHome, resolved.dexMethod, null,
                                    callArgs);
                            if (resolved.dexMethod.returnType.equals("V")) {
                                pending = null;
                                hasPending = false;
                            } else {
                                pending = result;
                                hasPending = true;
                            }
                            pc++;
                            break;
                        }
                        if (isKotlinLazy(ref)) {
                            Object[] callArgs = readRefArgs(ref, g, 0, regs);
                            Object function = callArgs[callArgs.length - 1];
                            if (function == null) {
                                throw new NullPointerException("null initializer");
                            }
                            if (!(function instanceof VmObject)) {
                                throw new VmException("not a function: " + insn.reference);
                            }
                            pending = new LazyHolder((VmObject) function);
                            hasPending = true;
                            pc++;
                            break;
                        }
                        if (resolved.builtin != null) {
                            Object result;
                            try {
                                result = resolved.builtin.invoke(null,
                                        readRefArgs(ref, g, 0, regs));
                            } catch (ClassCastException e) {
                                throw new VmException("bridge mismatch: " + insn.reference, e);
                            }
                            if (ref.returnType.equals("V")) {
                                pending = null;
                                hasPending = false;
                            } else {
                                pending = result;
                                hasPending = true;
                            }
                            pc++;
                            break;
                        }
                        Outcome special = runSpecial(null, ref,
                                readRefArgs(ref, g, 0, regs));
                        if (special.handled) {
                            pending = special.hasValue ? special.value : null;
                            hasPending = special.hasValue;
                            pc++;
                            break;
                        }
                        ReflectResult reflected = reflectCall(null, ref,
                                readRefArgs(ref, g, 0, regs), true);
                        if (reflected.hit) {
                            if (ref.returnType.equals("V")) {
                                pending = null;
                                hasPending = false;
                            } else {
                                pending = reflected.value;
                                hasPending = true;
                            }
                            pc++;
                            break;
                        }
                        throw gapMessage(ref.definingClass, ref, insn.reference,
                                resolved, null);
                    }
                    case "invoke-direct":
                    case "invoke-direct-range":
                    case "invoke-virtual":
                    case "invoke-virtual-range":
                    case "invoke-super":
                    case "invoke-super-range":
                    case "invoke-interface":
                    case "invoke-interface-range": {
                        MethodRef ref = MethodRef.parse(insn.reference);
                        if (ref.definingClass.equals("Ljava/lang/Object;")
                                && ref.name.equals("<init>")) {
                            pending = null;
                            hasPending = false;
                            pc++;
                            break;
                        }
                        Object receiver = regs[g.get(0)];
                        if (receiver == null
                                || (isNullish(receiver) && !isWrapperCall(ref))) {
                            throw new NullPointerException(
                                    "null receiver: " + insn.reference);
                        }
                        if (receiver instanceof LazyHolder && runLazy(ref)) {
                            LazyHolder holder = (LazyHolder) receiver;
                            if (ref.name.equals("getValue")) {
                                if (!holder.initialized) {
                                    DexClass fnClass = holder.function.type;
                                    DexMethod invoke = null;
                                    for (DexMethod candidate : fnClass.methods) {
                                        if (candidate.name.equals("invoke")
                                                && candidate.parameterTypes.isEmpty()) {
                                            invoke = candidate;
                                            break;
                                        }
                                    }
                                    if (invoke == null) {
                                        throw new VmException(
                                                "unimplemented: Function0.invoke");
                                    }
                                    holder.value = execute(fnClass, invoke, holder.function,
                                            new Object[0]);
                                    holder.initialized = true;
                                }
                                pending = holder.value;
                                hasPending = true;
                                pc++;
                                break;
                            }
                            pending = holder.initialized ? 1 : 0;
                            hasPending = true;
                            pc++;
                            break;
                        }
                        // Dex subclasses of concrete host classes with no
                        // instance state (e.g. Compose's TreeSet over
                        // java.util.TreeSet) construct as the host
                        // superclass, so constructor args configure the
                        // live object. Misses run the dex body as usual.
                        if (ref.name.equals("<init>") && receiver instanceof VmObject) {
                            Object[] initArgs = readRefArgs(ref, g, 1, regs);
                            ReflectResult hostSuper = hostSuperConstruct(
                                    (VmObject) receiver, ref, initArgs);
                            if (hostSuper.hit) {
                                regs[g.get(0)] = hostSuper.value;
                                pending = null;
                                hasPending = false;
                                pc++;
                                break;
                            }
                        }
                        Resolved resolved;
                        if (op.startsWith("invoke-direct")) {
                            resolved = resolve(null, ref.definingClass, ref, false);
                        } else if (op.startsWith("invoke-super")) {
                            resolved = resolve(superOf(lexical), ref.definingClass, ref, true);
                        } else {
                            resolved = resolve(runtimeClass(receiver), ref.definingClass, ref,
                                    true);
                        }
                        if (resolved.dexMethod != null) {
                            if ((resolved.dexMethod.accessFlags & ACC_STATIC) != 0) {
                                throw new VmException("static mismatch: " + insn.reference);
                            }
                            Object[] callArgs = readArgs(resolved.dexMethod, g, 1, regs);
                            Object result = execute(resolved.dexHome, resolved.dexMethod,
                                    receiver, callArgs);
                            if (resolved.dexMethod.returnType.equals("V")) {
                                pending = null;
                                hasPending = false;
                            } else {
                                pending = result;
                                hasPending = true;
                            }
                            pc++;
                            break;
                        }
                        if (resolved.builtin != null) {
                            Object[] callArgs = readRefArgs(ref, g, 1, regs);
                            Object result;
                            try {
                                result = resolved.builtin.invoke(receiver, callArgs);
                            } catch (ClassCastException e) {
                                throw new VmException("bridge mismatch: " + insn.reference, e);
                            }
                            if (ref.returnType.equals("V")) {
                                pending = null;
                                hasPending = false;
                            } else {
                                pending = result;
                                hasPending = true;
                            }
                            pc++;
                            break;
                        }
                        Outcome special = runSpecial(receiver, ref, readRefArgs(ref, g, 1, regs));
                        if (special.handled) {
                            pending = special.hasValue ? special.value : null;
                            hasPending = special.hasValue;
                            pc++;
                            break;
                        }
                        if (ref.name.equals("<init>")) {
                            ReflectResult constructed = reflectConstruct(ref,
                                    readRefArgs(ref, g, 1, regs));
                            if (constructed.hit) {
                                regs[g.get(0)] = constructed.value;
                                pending = null;
                                hasPending = false;
                                pc++;
                                break;
                            }
                        }
                        Object[] refArgs = readRefArgs(ref, g, 1, regs);
                        ReflectResult reflected = reflectCall(receiver, ref,
                                refArgs, false);
                        if (reflected.hit) {
                            if (ref.returnType.equals("V")) {
                                pending = null;
                                hasPending = false;
                            } else {
                                pending = reflected.value;
                                hasPending = true;
                            }
                            pc++;
                            break;
                        }
                        Outcome fw = frameworkDispatch(receiver, ref, refArgs);
                        if (fw.handled) {
                            pending = fw.hasValue ? fw.value : null;
                            hasPending = fw.hasValue;
                            pc++;
                            break;
                        }
                        throw gapMessage(ref.definingClass, ref, insn.reference,
                                resolved, receiver);
                    }
                    default:
                        throw new VmException("unsupported opcode: " + op);
                }
            } catch (VmThrow thrown) {
                DexMethod.Handler match = null;
                int fault = code.get(here).codeOffset;
                for (DexMethod.Try candidate : method.tries) {
                    if (fault >= candidate.start && fault < candidate.end) {
                        for (DexMethod.Handler handler : candidate.handlers) {
                            if (handler.type == null || catches(handler.type, thrown.value)) {
                                match = handler;
                                break;
                            }
                        }
                        if (match != null) {
                            break;
                        }
                    }
                }
                if (match == null) {
                    throw thrown;
                }
                Integer index = addresses.get(match.address);
                if (index == null) {
                    throw new VmException("bad handler address");
                }
                pending = thrown.value;
                hasPending = true;
                pc = index;
            }
        }
    }

    /**
     * Host-allocates framework objects with value semantics. Returns null
     * when the class needs a DEX body (or a gap message) instead.
     */
    private Object createInstance(String descriptor) {
        if (descriptor.equals("Ljava/lang/StringBuilder;")) {
            return new StringBuilder();
        }
        if (descriptor.equals("Ljava/lang/String;")) {
            return "";
        }
        if (descriptor.equals("Ljava/util/HashMap;")) {
            return new java.util.HashMap<Object, Object>();
        }
        if (descriptor.equals("Ljava/util/ArrayList;")) {
            return new java.util.ArrayList<Object>();
        }
        if (descriptor.equals("Ljava/util/HashSet;")) {
            return new java.util.HashSet<Object>();
        }
        // File is guest-managed, never a bare host object: the <init>
        // builtins below remap the guest path through the Vfs seam and
        // store the mapping on the placeholder (fail-closed sandbox).
        if (descriptor.equals("Ljava/io/File;")) {
            return newPlaceholder(descriptor);
        }
        // Bundles are string-keyed value maps; a host map carries the
        // semantics (casts against Bundle stay approximate, as with
        // other host-allocated model types).
        if (descriptor.equals("Landroid/os/Bundle;")) {
            return new java.util.HashMap<Object, Object>();
        }
        if (descriptor.startsWith("Ljava/")) {
            Class<?> host = hostClass(descriptor);
            if (host != null && !host.isInterface() && !host.isArray() && !host.isEnum()
                    && !Modifier.isAbstract(host.getModifiers())) {
                try {
                    return host.getConstructor().newInstance();
                } catch (ReflectiveOperationException | LinkageError | RuntimeException e) {
                    // fall through to placeholder
                }
            }
        }
        FwIndex.FwClass fw = framework == null ? null : framework.find(descriptor);
        Class<?> throwable = descriptor.startsWith("Ljava/lang/")
                ? hostThrowable(descriptor) : null;
        if (fw == null && throwable == null) {
            return null;
        }
        VmObject placeholder = newPlaceholder(descriptor);
        if (throwable != null) {
            throwables.put(placeholder, new PendingThrowable(throwable));
        }
        return placeholder;
    }

    private VmObject newPlaceholder(String descriptor) {
        String superDesc = "Ljava/lang/Object;";
        List<String> ifaces = new ArrayList<>();
        if (framework != null) {
            FwIndex.FwClass fw = framework.find(descriptor);
            if (fw != null) {
                if (fw.superclass != null) {
                    superDesc = fw.superclass;
                }
                ifaces = fw.interfaces;
            }
        }
        return new VmObject(
                new DexClass(descriptor, 0, superDesc, ifaces, new ArrayList<DexMethod>(),
                        new ArrayList<DexClass.DexField>()));
    }

    // java.io.File bridge state (keys live on the File placeholder;
    // guest code has no access to these — file ops all route via the
    // builtins above) plus the path helpers they share.

    /** Key on the placeholder carrying the guest-form path. */
    private static final String FILE_GUEST = "Ljava/io/File;.path";
    /** Key carrying the mapped host path (never shown to the guest). */
    private static final String FILE_HOST = "Ljava/io/File;.host";
    /** Key carrying the effective read-only verdict of the mount. */
    private static final String FILE_RO = "Ljava/io/File;.ro";

    /**
     * Binds a File placeholder: the guest path is kept verbatim (cleaned),
     * the host remap comes from the Vfs seam, and unmapped paths (denied)
     * bind a sentinel that never exists — subsequent ops fail closed.
     */
    void installFileFields(VmObject file, String guestPath) {
        Vfs.Policy policy = this.sdcardPolicy;
        String clean = cleanGuestPath(guestPath);
        file.putField(FILE_GUEST, clean);
        Path host = policy.target(clean);
        if (host == null) {
            file.putField(FILE_HOST,
                    policy.root().resolve("denied-file-access").toString());
            file.putField(FILE_RO, Boolean.TRUE);
            return;
        }
        file.putField(FILE_HOST, host.toString());
        file.putField(FILE_RO, policy.readOnlyHost(host));
    }

    private static String fileGuestPath(VmObject file) {
        return (String) file.getField(FILE_GUEST);
    }

    private static Path fileHostPath(VmObject file) {
        return Paths.get((String) file.getField(FILE_HOST));
    }

    private static boolean fileReadOnly(VmObject file) {
        return Boolean.TRUE.equals(file.getField(FILE_RO));
    }

    /** A fresh File placeholder bound to the same guest path. */
    private VmObject cloneFileFields(VmObject source) {
        VmObject out = newPlaceholder("Ljava/io/File;");
        installFileFields(out, fileGuestPath(source));
        return out;
    }

    /**
     * Collapses repeated separators and drops a trailing one (the root
     * itself stays {@code "/"}); Android paths use POSIX spelling.
     */
    private static String cleanGuestPath(String path) {
        String canon = path.replace('\\', '/');
        StringBuilder out = new StringBuilder(canon.length());
        boolean onSlash = false;
        for (int i = 0; i < canon.length(); i++) {
            char c = canon.charAt(i);
            if (c == '/') {
                if (!onSlash) {
                    out.append('/');
                    onSlash = true;
                }
                continue;
            }
            out.append(c);
            onSlash = false;
        }
        String text = out.toString();
        if (!text.equals("/") && text.endsWith("/")) {
            return text.substring(0, text.length() - 1);
        }
        return text;
    }

    /** POSIX parent of a guest path, or {@code null} when none. */
    private static String parentOfPath(String path) {
        String canon = path.replace('\\', '/');
        int cut = canon.lastIndexOf('/');
        if (cut <= 0) {
            return null;
        }
        return canon.substring(0, cut);
    }

    /** Last segment of a guest path ({@code ""} for the empty path). */
    private static String nameOfPath(String path) {
        String canon = path.replace('\\', '/');
        int cut = canon.lastIndexOf('/');
        return cut < 0 ? canon : canon.substring(cut + 1);
    }

    /**
     * Joins parent + child the way the two-argument File constructors do:
     * a null/empty parent degrades to the child, separators are inserted.
     */
    private static String joinGuestPath(String parent, String child) {
        if (child == null) {
            throw new NullPointerException("child");
        }
        if (parent == null || parent.isEmpty()) {
            return child;
        }
        String canon = parent.replace('\\', '/');
        if (canon.endsWith("/")) {
            return canon + child;
        }
        return canon + "/" + child;
    }

    /**
     * Dev-driver bridge: binds a host-built framework mirror to a fresh
     * placeholder of the given descriptor. Without this, the first guest
     * dispatch on such an object would lazily build a bare second mirror and
     * lose any renderer-attached state (the draw chain's surface canvas).
     * Registering the mirror also makes identity unbridging work both ways.
     */
    public VmObject hostObjectFor(String descriptor, Object mirror) {
        VmObject placeholder = newPlaceholder(descriptor);
        placeholder.host = mirror;
        mirrorOwners.put(mirror, placeholder);
        return placeholder;
    }

    /** Host {@link Throwable} subclass for a framework descriptor, else null. */
    private static Class<?> hostThrowable(String descriptor) {
        if (!descriptor.startsWith("Ljava/lang/") || !descriptor.endsWith(";")) {
            return null;
        }
        String simple = descriptor.substring("Ljava/lang/".length(), descriptor.length() - 1);
        if (simple.isEmpty() || simple.indexOf('/') >= 0 || simple.indexOf('[') >= 0) {
            return null;
        }
        String binary = "java.lang." + simple;
        try {
            Class<?> clazz = Class.forName(binary);
            if (java.lang.Throwable.class.isAssignableFrom(clazz)) {
                return clazz;
            }
            return null;
        } catch (ClassNotFoundException | LinkageError e) {
            return null;
        }
    }

    private void ensureInit(DexClass cls) {
        if (initialized.contains(cls.type) || initializing.contains(cls.type)) {
            return;
        }
        initializing.add(cls.type);
        try {
            for (DexMethod method : cls.methods) {
                if (method.name.equals("<clinit>") && method.parameterTypes.isEmpty()) {
                    execute(cls, method, null, new Object[0]);
                    break;
                }
            }
        } finally {
            initializing.remove(cls.type);
            initialized.add(cls.type);
        }
    }

    private void ensureInitFor(String fieldReference) {
        FieldRef ref = FieldRef.parse(fieldReference);
        DexClass cls = dexes.findClass(ref.definingClass);
        if (cls != null) {
            ensureInit(cls);
            return;
        }
        if (framework == null || framework.find(ref.definingClass) == null) {
            if (hostClass(ref.definingClass) == null) {
                throw new VmException("unimplemented: " + fieldReference);
            }
            // Host class: <clinit> runs implicitly on reflective access.
            return;
        }
        // Framework class: no <clinit> to run; constants served without init.
    }

    private Object staticInt(String fieldReference) {
        ensureInitFor(fieldReference);
        FieldRef ref = FieldRef.parse(fieldReference);
        if (ref.type.equals("F")) {
            Object value = staticRead(ref.definingClass, ref.name, ref.type);
            if (value == null) {
                return 0.0f;
            }
            return asFloatBits(value);
        }
        Object value = staticRead(ref.definingClass, ref.name, ref.type);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof Character) {
            return (int) (char) (Character) value;
        }
        if (value == null && isDexClass(fieldReference)) {
            return 0;
        }
        ReflectResult reflected = reflectStaticField(ref.definingClass, ref.name);
        if (reflected.hit && reflected.value instanceof Number) {
            return ((Number) reflected.value).intValue();
        }
        throw new VmException("unimplemented: " + fieldReference);
    }

    private Object staticLong(String fieldReference) {
        ensureInitFor(fieldReference);
        FieldRef ref = FieldRef.parse(fieldReference);
        if (ref.type.equals("D")) {
            Object value = staticRead(ref.definingClass, ref.name, ref.type);
            if (value == null) {
                return 0.0d;
            }
            return asDoubleBits(value);
        }
        Object value = staticRead(ref.definingClass, ref.name, ref.type);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value == null && isDexClass(fieldReference)) {
            return 0L;
        }
        ReflectResult reflected = reflectStaticField(ref.definingClass, ref.name);
        if (reflected.hit && reflected.value instanceof Number) {
            return ((Number) reflected.value).longValue();
        }
        throw new VmException("unimplemented: " + fieldReference);
    }

    private Object staticRef(String fieldReference) {
        ensureInitFor(fieldReference);
        FieldRef ref = FieldRef.parse(fieldReference);
        Object value = staticRead(ref.definingClass, ref.name, ref.type);
        if (value != null) {
            return value;
        }
        if (isDexClass(fieldReference)) {
            return null;
        }
        VmObject enumValue = frameworkEnumValue(ref.definingClass, ref.name);
        if (enumValue != null) {
            return enumValue;
        }
        VmObject singleton = frameworkSingleton(ref.definingClass, ref.name);
        if (singleton != null) {
            return singleton;
        }
        ReflectResult reflected = reflectStaticField(ref.definingClass, ref.name);
        if (reflected.hit) {
            return reflected.value;
        }
        throw new VmException("unimplemented: " + fieldReference);
    }

    /**
     * Materializes a framework enum constant (e.g. Paint.Style.STROKE) that
     * has no jar value: placeholder with Enum name and declaration ordinal,
     * cached for singleton identity. Null when the class is not a framework
     * enum or the name is not one of its constants.
     */
    private VmObject frameworkEnumValue(String descriptor, String name) {
        if (framework == null) {
            return null;
        }
        // Host-owned java.* enums carry real behavior (TimeUnit.toNanos
        // etc.): reflection serves them. Android module enums stay
        // placeholders until stub-jar constant order (alpha, not device
        // declaration order) is repaired, so serving judges by the
        // registry until then.
        if (hostClass(descriptor) != null
                && descriptor.startsWith("Ljava/")) {
            return null;
        }
        FwIndex.FwClass fw = framework.find(descriptor);
        if (fw == null || !"Ljava/lang/Enum;".equals(fw.superclass)) {
            return null;
        }
        int ordinal = 0;
        boolean found = false;
        for (FwIndex.FwField field : fw.fields) {
            if (!descriptor.equals(field.type)) {
                continue;
            }
            if (field.name.equals(name)) {
                found = true;
                break;
            }
            ordinal++;
        }
        if (!found) {
            return null;
        }
        VmObject value = newPlaceholder(descriptor);
        value.putField("Ljava/lang/Enum;.name", name);
        value.putField("Ljava/lang/Enum;.ordinal", ordinal);
        statics.put(descriptor + "." + name, value);
        return value;
    }

    /**
     * Every constant of a framework enum in declaration order, mirroring
     * the device values() array. Null when not a framework enum.
     */
    private List<VmObject> frameworkEnumValues(String descriptor) {
        if (framework == null) {
            return null;
        }
        // Host-owned java.* enums reflect (same rule as above); android
        // module enums stay placeholders until ordinals are faithful.
        if (hostClass(descriptor) != null
                && descriptor.startsWith("Ljava/")) {
            return null;
        }
        FwIndex.FwClass fw = framework.find(descriptor);
        if (fw == null || !"Ljava/lang/Enum;".equals(fw.superclass)) {
            return null;
        }
        List<VmObject> out = new ArrayList<>();
        for (FwIndex.FwField field : fw.fields) {
            if (!descriptor.equals(field.type)) {
                continue;
            }
            Object cached = statics.get(descriptor + "." + field.name);
            if (cached instanceof VmObject) {
                out.add((VmObject) cached);
                continue;
            }
            VmObject constant = frameworkEnumValue(descriptor, field.name);
            if (constant == null) {
                return null;
            }
            out.add(constant);
        }
        return out;
    }

    /**
     * Materializes opaque framework singletons (Typeface families,
     * TextDirectionHeuristics) that have no jar value: cached placeholder
     * recording the constant name for later matching (font lookup,
     * bidi). Singleton fields may be interface-typed, so matching is by
     * declaring class and name; the class allowlist is the guard.
     * Null for anything else.
     */
    private VmObject frameworkSingleton(String descriptor, String name) {
        String label;
        if (descriptor.equals("Landroid/graphics/Typeface;")) {
            label = "familyName";
        } else if (descriptor.equals("Landroid/text/TextDirectionHeuristics;")) {
            label = "heuristicName";
        } else {
            return null;
        }
        if (framework == null) {
            return null;
        }
        FwIndex.FwClass fw = framework.find(descriptor);
        if (fw == null) {
            return null;
        }
        boolean found = false;
        for (FwIndex.FwField field : fw.fields) {
            if (field.name.equals(name)) {
                found = true;
                break;
            }
        }
        if (!found) {
            return null;
        }
        VmObject value = newPlaceholder(descriptor);
        value.putField(descriptor + "." + label, name);
        statics.put(descriptor + "." + name, value);
        return value;
    }

    private void staticPut(String fieldReference, Object value) {
        ensureInitFor(fieldReference);
        if (isDexClass(fieldReference)) {
            statics.put(staticKey(fieldReference), value);
            return;
        }
        FieldRef ref = FieldRef.parse(fieldReference);
        Class<?> host = hostClass(ref.definingClass);
        if (host != null) {
            try {
                java.lang.reflect.Field field = host.getField(ref.name);
                if (!Modifier.isFinal(field.getModifiers())) {
                    field.set(null, toHost(field.getType(), value));
                    return;
                }
            } catch (NoSuchFieldException | IllegalAccessException | LinkageError e) {
                // fall through to gap
            }
        }
        throw new VmException("unimplemented: " + fieldReference);
    }

    private boolean isDexClass(String fieldReference) {
        return dexes.findClass(FieldRef.parse(fieldReference).definingClass) != null;
    }

    private Object frameworkConstant(String fieldReference) {
        if (framework == null) {
            return null;
        }
        FieldRef ref = FieldRef.parse(fieldReference);
        FwIndex.FwClass fw = framework.find(ref.definingClass);
        if (fw == null) {
            return null;
        }
        return fw.constants.get(ref.name);
    }

    private static String staticKey(String fieldReference) {
        FieldRef ref = FieldRef.parse(fieldReference);
        return ref.definingClass + "." + ref.name;
    }

    /** Superclass descriptor, or null when unknown (lenient for field walks). */
    private String superDesc(String descriptor) {
        DexClass dex = dexes.findClass(descriptor);
        if (dex != null) {
            return dex.superclass;
        }
        if (framework != null) {
            FwIndex.FwClass fw = framework.find(descriptor);
            if (fw != null) {
                return fw.superclass;
            }
        }
        return null;
    }

    /**
     * Instance field read through the receiver hierarchy. Compilers may emit
     * a subclass in the reference for an inherited field, so the first hit
     * walking up wins (device resolves to the single declaration the same
     * way). Writes stay reference-keyed.
     */
    private Object instanceField(VmObject object, String definingClass, String name) {
        String current = object.type.type;
        while (current != null) {
            Object have = object.getField(current + "." + name);
            if (have != null) {
                return have;
            }
            current = superDesc(current);
        }
        return null;
    }

    /**
     * Static read through the reference hierarchy (same subclass-reference
     * rule as instance fields), then jar constants.
     */
    private Object staticRead(String definingClass, String name, String type) {
        if (definingClass.equals("Ljava/io/File;")) {
            Object fileStatic = fileStatic(name);
            if (fileStatic != null) {
                return fileStatic;
            }
        }
        String current = definingClass;
        while (current != null) {
            String key = current + "." + name;
            if (statics.containsKey(key)) {
                return statics.get(key);
            }
            if (type != null) {
                Object constant = frameworkConstant(current + "->" + name + ":" + type);
                if (constant != null) {
                    return constant;
                }
            }
            current = superDesc(current);
        }
        return null;
    }

    /**
     * {@code java.io.File} class constants (POSIX values, as on device).
     * The class is deny-listed for generic reflection, so its approved
     * statics answer inline instead. {@code null} keeps the general walk.
     */
    private static Object fileStatic(String name) {
        switch (name) {
            case "separator":
            case "pathSeparator":
                return "/";
            case "separatorChar":
            case "pathSeparatorChar":
                return '/';
            default:
                return null;
        }
    }

    private Object field(Object target, String fieldReference) {
        if (target == null) {
            throw new NullPointerException("null target: " + fieldReference);
        }
        if (!(target instanceof VmObject)) {
            return hostField(target, FieldRef.parse(fieldReference), true, null);
        }
        VmObject object = (VmObject) target;
        FieldRef ref = FieldRef.parse(fieldReference);
        return instanceField(object, ref.definingClass, ref.name);
    }

    private void field(Object target, String fieldReference, Object value) {
        if (target == null) {
            throw new NullPointerException("null target: " + fieldReference);
        }
        if (!(target instanceof VmObject)) {
            hostField(target, FieldRef.parse(fieldReference), false, value);
            return;
        }
        vmObject(target, fieldReference).putField(fieldKey(fieldReference), value);
    }

    private Object fieldInt(Object target, String fieldReference) {
        Object value = field(target, fieldReference);
        return value == null ? 0 : asInt(value);
    }

    /**
     * Narrow instance read honoring float fields ({@code iget} serves int
     * and float alike; the reference type disambiguates).
     */
    private Object fieldExact(Object target, String fieldReference) {
        FieldRef ref = FieldRef.parse(fieldReference);
        if (ref.type.equals("F")) {
            Object value = field(target, fieldReference);
            if (value == null) {
                return 0.0f;
            }
            return asFloatBits(value);
        }
        return fieldInt(target, fieldReference);
    }

    /**
     * Wide instance read honoring double fields ({@code iget-wide} serves
     * long and double alike).
     */
    private Object fieldWide(Object target, String fieldReference) {
        FieldRef ref = FieldRef.parse(fieldReference);
        if (ref.type.equals("D")) {
            Object value = field(target, fieldReference);
            if (value == null) {
                return 0.0d;
            }
            return asDoubleBits(value);
        }
        return fieldLong(target, fieldReference);
    }

    private Object fieldLong(Object target, String fieldReference) {
        Object value = field(target, fieldReference);
        return value == null ? 0L : asLong(value);
    }

    private static VmObject vmObject(Object target, String fieldReference) {
        if (target == null) {
            throw new NullPointerException("null target: " + fieldReference);
        }
        if (!(target instanceof VmObject)) {
            throw new VmException("not an object: " + fieldReference);
        }
        return (VmObject) target;
    }

    private static String fieldKey(String fieldReference) {
        FieldRef ref = FieldRef.parse(fieldReference);
        return ref.definingClass + "." + ref.name;
    }

    private Object at(Object array, Object index) {
        return checkedArray(array).get(asInt(index));
    }

    private void at(Object array, Object index, Object value) {
        checkedArray(array).set(asInt(index), value);
    }

    private static VmArray checkedArray(Object array) {
        if (array == null) {
            throw new NullPointerException("null array");
        }
        if (!(array instanceof VmArray)) {
            throw new VmException("not an array");
        }
        return (VmArray) array;
    }

    private static String arrayElement(String reference) {
        if (reference.length() < 2 || reference.charAt(0) != '[') {
            throw new VmException("not an array type: " + reference);
        }
        return reference.substring(1);
    }

    /** Payload width a primitive array element expects, or -1. */
    private static int fillWidth(String element) {
        switch (element) {
            case "Z":
            case "B":
                return 1;
            case "S":
            case "C":
                return 2;
            case "I":
            case "F":
                return 4;
            case "J":
            case "D":
                return 8;
            default:
                return -1;
        }
    }

    /**
     * Normalizes a stored array element: float/double arrays hold raw bit
     * patterns (like Dalvik memory), so {@link Float}/{@link Double} values
     * are converted to bits while {@link Integer}/{@link Long} patterns pass
     * through untouched. Everything else stores as-is.
     */
    private static Object aputValue(Object array, Object value) {
        if (array instanceof VmArray) {
            String element = ((VmArray) array).elementType;
            if (element.equals("F") && value instanceof Float) {
                return Float.floatToRawIntBits((Float) value);
            }
            if (element.equals("D") && value instanceof Double) {
                return Double.doubleToRawLongBits((Double) value);
            }
        }
        return value;
    }

    /**
     * {@code instanceof} with the zero-bits heuristic: null (and zero-bits)
     * is never an instance, except that zero-bits tests true against its own
     * wrapper (the overwhelmingly common real-value case).
     */
    private boolean instanceOf(Object value, String target) {
        if (value == null) {
            return false;
        }
        if (isNullish(value)) {
            return target.equals("Ljava/lang/Integer;")
                    || target.equals("Ljava/lang/Object;");
        }
        return isInstance(value, target);
    }
    private boolean isInstance(Object value, String target) {
        if (value == null) {
            return false;
        }
        if (target.equals("Ljava/lang/Object;")) {
            return true;
        }
        if (value instanceof BoxedZero) {
            return target.equals("Ljava/lang/Integer;")
                    || target.equals("Ljava/lang/Number;")
                    || target.equals("Ljava/lang/Object;");
        }
        if (value instanceof VmArray) {
            if (!target.startsWith("[")) {
                return false;
            }
            return arrayComponentAssignable(target.substring(1),
                    ((VmArray) value).elementType);
        }
        if (!(value instanceof VmObject)) {
            if (value instanceof VmClass) {
                return target.equals("Ljava/lang/Class;");
            }
            if (value instanceof LazyHolder) {
                return target.equals("Lkotlin/Lazy;");
            }
            if (value instanceof VmLooper) {
                return target.equals("Landroid/os/Looper;");
            }
            if (value instanceof VmQueue) {
                return target.equals("Landroid/os/MessageQueue;");
            }
            return hostAssignable(target, value.getClass());
        }
        return typeAssignable(target, ((VmObject) value).type.type);
    }

    /** Array covariance: identical, Object destination, or hierarchy walk. */
    private boolean arrayComponentAssignable(String want, String have) {
        if (want.equals(have)) {
            return true;
        }
        if (want.equals("Ljava/lang/Object;")) {
            return isRefType(have);
        }
        if (!isRefType(want) || !isRefType(have)) {
            return false;
        }
        if (have.startsWith("[")) {
            if (!want.startsWith("[")) {
                return false;
            }
            return arrayComponentAssignable(want.substring(1), have.substring(1));
        }
        return typeAssignable(want, have);
    }

    /** Hierarchy walk over both universes; unknown classes fail loudly. */
    private boolean typeAssignable(String target, String haveType) {
        Deque<String> queue = new ArrayDeque<>();
        Set<String> seen = new HashSet<>();
        queue.add(haveType);
        while (!queue.isEmpty()) {
            String current = queue.removeFirst();
            if (current.equals(target)) {
                return true;
            }
            if (!seen.add(current)) {
                continue;
            }
            Supers supers = supersOf(current);
            if (supers.superclass != null
                    && !supers.superclass.equals("Ljava/lang/Object;")) {
                queue.add(supers.superclass);
            }
            queue.addAll(supers.interfaces);
        }
        return false;
    }

    private static final class Supers {
        final String superclass;
        final List<String> interfaces;

        Supers(String superclass, List<String> interfaces) {
            this.superclass = superclass;
            this.interfaces = interfaces;
        }
    }

    private Supers supersOf(String descriptor) {
        DexClass dex = dexes.findClass(descriptor);
        if (dex != null) {
            return new Supers(dex.superclass, dex.interfaces);
        }
        if (framework != null) {
            FwIndex.FwClass fw = framework.find(descriptor);
            if (fw != null) {
                return new Supers(fw.superclass, fw.interfaces);
            }
        }
        throw new VmException("unimplemented class: " + descriptor);
    }

    private static boolean hostAssignable(String target, Class<?> start) {
        Deque<Class<?>> queue = new ArrayDeque<>();
        Set<Class<?>> seen = new HashSet<>();
        queue.add(start);
        while (!queue.isEmpty()) {
            Class<?> current = queue.removeFirst();
            if (!seen.add(current)) {
                continue;
            }
            if (hostDescriptor(current).equals(target)) {
                return true;
            }
            if (current.getSuperclass() != null) {
                queue.add(current.getSuperclass());
            }
            Collections.addAll(queue, current.getInterfaces());
        }
        return false;
    }

    static String hostDescriptor(Class<?> type) {
        if (type.isArray()) {
            return type.getName().replace('.', '/');
        }
        if (type == boolean.class) {
            return "Z";
        }
        if (type == byte.class) {
            return "B";
        }
        if (type == short.class) {
            return "S";
        }
        if (type == char.class) {
            return "C";
        }
        if (type == int.class) {
            return "I";
        }
        if (type == long.class) {
            return "J";
        }
        if (type == float.class) {
            return "F";
        }
        if (type == double.class) {
            return "D";
        }
        if (type == void.class) {
            return "V";
        }
        return "L" + type.getName().replace('.', '/') + ";";
    }

    private static boolean isRefType(String type) {
        return type.startsWith("L") || type.startsWith("[");
    }

    private static String valueType(Object value) {
        if (value instanceof VmObject) {
            return ((VmObject) value).type.type;
        }
        if (value instanceof VmArray) {
            return "[" + ((VmArray) value).elementType;
        }
        return String.valueOf(value.getClass());
    }

    private static int toInt(boolean value) {
        return value ? 1 : 0;
    }

    private DexClass superOf(DexClass cls) {
        if (cls.superclass == null) {
            return null;
        }
        return dexes.findClass(cls.superclass);
    }

    private DexClass runtimeClass(Object receiver) {
        if (receiver instanceof VmObject) {
            return ((VmObject) receiver).type;
        }
        return null;
    }

    /** Exact signature match, walking superclasses for virtual dispatch. */
    /** Ordered resolution across the dex/framework boundary. */
    private static final class Resolved {
        DexMethod dexMethod;
        DexClass dexHome;
        HostMethod builtin;
        boolean exists;
        String unknownSuper;
    }

    /**
     * Resolution in precedence order: dex bodies anywhere beat host
     * builtins anywhere, which beat mere existence. Unknown supers stop
     * their branch. `hierarchy` false means exact (direct calls) only.
     */
    private Resolved resolve(DexClass hint, String defineDesc, MethodRef ref,
            boolean hierarchy) {
        Resolved out = new Resolved();
        // Phase 1: dex bodies, most-derived first.
        Deque<String> queue = new ArrayDeque<>();
        Set<String> seen = new HashSet<>();
        queue.add(hint == null ? defineDesc : hint.type);
        while (!queue.isEmpty()) {
            String current = queue.removeFirst();
            if (!seen.add(current)) {
                continue;
            }
            DexClass dex = dexes.findClass(current);
            if (dex == null) {
                continue;
            }
            DexMethod found = findMethod(dex, ref);
            if (found != null) {
                out.dexMethod = found;
                out.dexHome = dex;
                return out;
            }
            if (!hierarchy) {
                return out;
            }
            // Object terminates every walk; it is never a gap.
            if (dex.superclass != null
                    && !dex.superclass.equals("Ljava/lang/Object;")) {
                queue.add(dex.superclass);
            }
            queue.addAll(dex.interfaces);
        }
        if (!hierarchy) {
            HostMethod builtin = builtins.get(keyFor(ref, defineDesc));
            if (builtin != null) {
                out.builtin = builtin;
            }
            return out;
        }
        // Phase 2: host builtins, most-derived first (exact table included).
        queue.clear();
        seen.clear();
        queue.add(defineDesc);
        while (!queue.isEmpty()) {
            String current = queue.removeFirst();
            if (!seen.add(current)) {
                continue;
            }
            HostMethod builtin = builtins.get(keyFor(ref, current));
            if (builtin != null) {
                out.builtin = builtin;
                return out;
            }
            DexClass dex = dexes.findClass(current);
            if (dex != null) {
                if (dex.superclass != null
                        && !dex.superclass.equals("Ljava/lang/Object;")) {
                    queue.add(dex.superclass);
                }
                queue.addAll(dex.interfaces);
                continue;
            }
            if (framework != null) {
                FwIndex.FwClass fw = framework.find(current);
                if (fw != null) {
                    if (fw.hasMethod(ref.name, ref.parameters, ref.returnType)) {
                        out.exists = true;
                    }
                    if (fw.superclass != null
                            && !fw.superclass.equals("Ljava/lang/Object;")) {
                        queue.add(fw.superclass);
                    }
                    queue.addAll(fw.interfaces);
                    continue;
                }
            }
            if (out.unknownSuper == null) {
                out.unknownSuper = current;
            }
        }
        // Phase 2b: builtins keyed on the receiver's runtime hierarchy.
        // Interface calls into concrete builtins (e.g. CharSequence.length
        // on our SpannableString store) miss the lexical walk above;
        // lexical matches still win since they returned first.
        if (hint != null) {
            queue.clear();
            seen.clear();
            queue.add(hint.type);
            while (!queue.isEmpty()) {
                String current = queue.removeFirst();
                if (!seen.add(current)) {
                    continue;
                }
                HostMethod builtin = builtins.get(keyFor(ref, current));
                if (builtin != null) {
                    out.builtin = builtin;
                    return out;
                }
                DexClass dex = dexes.findClass(current);
                if (dex != null) {
                    if (dex.superclass != null
                            && !dex.superclass.equals("Ljava/lang/Object;")) {
                        queue.add(dex.superclass);
                    }
                    queue.addAll(dex.interfaces);
                    continue;
                }
                if (framework != null) {
                    FwIndex.FwClass fw = framework.find(current);
                    if (fw != null) {
                        if (fw.superclass != null
                                && !fw.superclass.equals("Ljava/lang/Object;")) {
                            queue.add(fw.superclass);
                        }
                        queue.addAll(fw.interfaces);
                    }
                }
            }
        }
        return out;
    }

    private static String keyFor(MethodRef ref, String definingClass) {
        StringBuilder key = new StringBuilder(definingClass).append("->")
                .append(ref.name).append('(');
        for (String param : ref.parameters) {
            key.append(param);
        }
        return key.append(')').append(ref.returnType).toString();
    }

    // Reflective bridge: fallback for java.* calls the table does not cover.
    // Precedence everywhere is dex, table, special rules, reflection, gaps.
    // Only host objects (or none, for static) cross; model objects never do.

    /** Classes never touched by reflection (process/thread control, loaders). */
    private static final Set<String> REFLECT_DENY;
    static {
        Set<String> deny = new HashSet<>();
        deny.add("Ljava/lang/Runtime;");
        deny.add("Ljava/lang/ProcessBuilder;");
        deny.add("Ljava/lang/Process;");
        deny.add("Ljava/lang/ProcessHandle;");
        deny.add("Ljava/lang/System;");
        deny.add("Ljava/lang/Thread;");
        deny.add("Ljava/lang/ThreadGroup;");
        deny.add("Ljava/lang/ClassLoader;");
        deny.add("Ljava/lang/Compiler;");
        deny.add("Ljava/lang/SecurityManager;");
        REFLECT_DENY = Collections.unmodifiableSet(deny);
    }

    /**
     * Vetted {@code java.*} modules the guest may cross onto the live
     * JDK reflectively (a prefix match, with {@link #REFLECT_DENY}
     * overriding inside them). Everything else — notably {@code java.io},
     * {@code java.nio} and {@code java.net} — fails closed; the File
     * bridge below is the only guest filesystem path.
     */
    private static final List<String> REFLECT_ALLOW_PREFIXES = List.of(
            "Ljava/lang/",
            "Ljava/lang/reflect/",
            "Ljava/lang/annotation/",
            "Ljava/util/",
            "Ljava/util/concurrent/",
            "Ljava/util/function/",
            "Ljava/util/stream/",
            "Ljava/util/regex/",
            "Ljava/text/",
            "Ljava/math/");

    private final Map<String, Class<?>> hostClasses = new HashMap<>();
    private final Map<String, Method> hostMethods = new HashMap<>();
    /**
     * Mirror-owner registry: framework-dispatch tracks which model object a
     * host mirror belongs to, so values that hosts return (children lists,
     * child lookups) convert back to the model object the guest allocated,
     * keeping reference identity stable across the adapter boundary.
     */
    private final Map<Object, VmObject> mirrorOwners = new java.util.IdentityHashMap<>();

    /**
     * Host-runtime classes a guest may reach reflectively.
     * Deny-by-default (follow-up audit 2026-10-19): the allowlist names
     * the vetted {@code java.*} modules that cross onto the live JDK
     * (value types, collections, primitives boxing, reflection machinery,
     * text/regex), {@code android.*} resolves against the framework
     * module (skeletons ported there). {@code java.io}, {@code java.nio}
     * and {@code java.net} are NOT on the list — guest filesystem and
     * network reach does not cross reflection; filesystem access goes
     * through the dedicated {@code File} bridge builtins, which remap
     * every path via {@code tartine.runtime.Vfs} and fail closed.
     * {@code REFLECT_DENY} overrides the prefixes: process/thread
     * control, loaders and the security surface never cross, exactly the
     * classes the earlier blocklist named. Presence of a listed class is
     * still confirmed by {@link Class#forName}, so unported android
     * classes simply resolve to nothing.
     */
    private static boolean isReflectable(String descriptor) {
        if (descriptor.startsWith("Landroid/")) {
            return true;
        }
        for (String prefix : REFLECT_ALLOW_PREFIXES) {
            if (descriptor.startsWith(prefix)) {
                return !REFLECT_DENY.contains(descriptor);
            }
        }
        return false;
    }

    /** Descriptor form of a host reference type (verbose arrays kept). */
    private static String descriptorOf(Class<?> type) {
        if (type.isArray()) {
            return "[" + descriptorOf(type.getComponentType());
        }
        String name = type.getName().replace('.', '/');
        return "L" + name + ";";
    }

    /**
     * Framework-module dispatch for placeholder receivers: the adapter's
     * reflective host step (AGENTS.md framework-in-Java (2)). Finds the
     * receiver's most-derived present framework class, lazily builds a
     * mirror instance, copies the receiver's mapped state into it
     * (definingClass-keyed fields), invokes reflectively (guest overrides
     * already dispatched above; builtins stay ahead), then copies the
     * mirror's instance fields back so the mapped state stays canonical.
     * Misses fall through to the gap path unchanged.
     */
    private Outcome frameworkDispatch(Object receiver, MethodRef ref,
            Object[] args) {
        if (!(receiver instanceof VmObject) || ref.name.equals("<init>")) {
            return Outcome.MISS;
        }
        VmObject object = (VmObject) receiver;
        Class<?> home = mirrorHome(object);
        if (home == null) {
            return Outcome.MISS;
        }
        Object mirror = hostMirror(object, home);
        if (mirror == null) {
            return Outcome.MISS;
        }
        // Guest refs bridge to their own mirrors; real args stay put.
        List<VmObject> bridged = new ArrayList<>();
        Object[] hostArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            hostArgs[i] = bridgeMirrorArg(args[i], bridged);
        }
        syncFields(object, mirror, true);
        ReflectResult result = mirrorInvoke(mirror, ref, hostArgs);
        if (!result.hit) {
            return Outcome.MISS;
        }
        syncFields(object, mirror, false);
        for (VmObject guest : bridged) {
            Object guestMirror = guest.host;
            if (guestMirror != null) {
                syncFields(guest, guestMirror, false);
            }
        }
        if (ref.returnType.equals("V")) {
            return Outcome.empty();
        }
        return Outcome.value(unbridgeMirrorValue(result.value, ref));
    }

    /**
     * Virtual-call seam handler (tartine.fw.VirtualCall). Framework mirror
     * bodies must reach guest overrides (View.draw's onDraw/dispatchDraw/
     * onDrawForeground legs, ViewGroup.drawChild's child.draw). Host Java
     * cannot virtual-dispatch into guest DEX, so mirror call sites route
     * through here: a receiver that mirrors a guest model whose DEX class
     * chain overrides the method runs the guest body (args converted
     * mirror-ward to model-ward, result bridged back); anything else returns
     * NOT_GUEST and the call site runs its framework default inline. Step
     * accounting stays with the single interpreter probe budget.
     */
    private Object guestVirtualCall(Object receiver, String name,
            String descriptor, Object[] args) {
        VmObject model = receiver == null ? null : mirrorOwners.get(receiver);
        if (model == null) {
            return VirtualCall.NOT_GUEST;
        }
        int arity = descriptorArity(descriptor);
        DexClass home = model.type;
        DexMethod target = null;
        DexClass targetHome = null;
        while (home != null && target == null) {
            for (DexMethod m : home.methods) {
                if (m.name.equals(name) && m.parameterTypes.size() == arity
                        && (m.accessFlags & ACC_STATIC) == 0) {
                    target = m;
                    targetHome = home;
                    break;
                }
            }
            if (target == null) {
                home = home.superclass == null ? null
                        : dexes.findClass(home.superclass);
            }
        }
        if (target == null) {
            return VirtualCall.NOT_GUEST;
        }
        Object[] guestArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            guestArgs[i] = mirrorOwners.containsKey(args[i])
                    ? mirrorOwners.get(args[i]) : args[i];
        }
        Object result = invoke(targetHome, target, model, guestArgs);
        if (result != null && mirrorOwners.containsKey(result)) {
            return mirrorOwners.get(result);
        }
        return result instanceof VmObject
                ? bridgeMirrorArg(result, new ArrayList<>()) : result;
    }

    /** Parameter count of a DEX-style descriptor, for seam lookups. */
    private static int descriptorArity(String descriptor) {
        int count = 0;
        int i = 1; // past '('
        while (descriptor.charAt(i) != ')') {
            char c = descriptor.charAt(i);
            if (c == 'L') {
                i = descriptor.indexOf(';', i) + 1;
            } else if (c == '[') {
                i++;
                continue;
            } else {
                i++;
            }
            count++;
        }
        return count;
    }

    /**
     * Mirror dispatch selector. Protected lifecycle members
     * (addViewInLayout, onLayout, onMeasure...) are the framework's real
     * surface and invisible to getMethods, so this walks declared methods
     * along the mirror's class chain (mirrors are always framework-module
     * instances; setAccessible is safe here). Same two-phase pick as
     * {@link #selectMethod}: exact guest descriptor first, guest-assignable
     * signature second. Virtual invoke keepsOverrides dispatch correct even
     * when the cache crosses mirror types with identical descriptors.
     */
    private ReflectResult mirrorInvoke(Object mirror, MethodRef ref,
            Object[] args) {
        Method selected = mirroredMethod(mirror, ref, args);
        if (selected == null) {
            return ReflectResult.MISS;
        }
        Class<?>[] params = selected.getParameterTypes();
        Object[] hostArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            hostArgs[i] = bridgeArg(params[i], args[i]);
        }
        try {
            selected.setAccessible(true);
            Object returned = selected.invoke(mirror, hostArgs);
            for (int i = 0; i < args.length; i++) {
                copyBack(hostArgs[i], args[i]);
            }
            return new ReflectResult(true, boxReturn(returned));
        } catch (IllegalAccessException e) {
            return ReflectResult.MISS;
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw new VmThrow(cause);
        }
    }

    private Method mirroredMethod(Object mirror, MethodRef ref, Object[] args) {
        StringBuilder key = new StringBuilder("M").append(ref.definingClass)
                .append('#').append(ref.name).append('#')
                .append(ref.parameters.size());
        for (Object arg : args) {
            key.append('#').append(argKey(arg));
        }
        String cacheKey = key.toString();
        if (hostMethods.containsKey(cacheKey)) {
            return hostMethods.get(cacheKey);
        }
        List<Method> candidates = new ArrayList<>();
        for (Class<?> c = mirror.getClass(); c != null
                && c != Object.class; c = c.getSuperclass()) {
            for (Method m : c.getDeclaredMethods()) {
                if (m.isBridge() || m.isSynthetic()
                        || Modifier.isStatic(m.getModifiers())
                        || !m.getName().equals(ref.name)
                        || m.getParameterCount() != ref.parameters.size()) {
                    continue;
                }
                candidates.add(m);
            }
        }
        Method selected = null;
        String wanted = dexDescriptor(ref);
        for (Method candidate : candidates) {
            if (methodDescriptor(candidate).equals(wanted)) {
                selected = candidate;
                break;
            }
        }
        if (selected == null) {
            candidates.sort((left, right) ->
                    methodDescriptor(left).compareTo(methodDescriptor(right)));
            for (Method candidate : candidates) {
                Class<?>[] params = candidate.getParameterTypes();
                boolean ok = true;
                for (int i = 0; i < params.length; i++) {
                    if (!argAssignable(params[i], args[i])) {
                        ok = false;
                        break;
                    }
                }
                if (ok) {
                    selected = candidate;
                    break;
                }
            }
        }
        hostMethods.put(cacheKey, selected);
        return selected;
    }

    /**
     * Lazily built (and identity-registered) mirror for a model object.
     * Reused across calls so state keeps accumulating in one place.
     */    private Object hostMirror(VmObject object, Class<?> home) {
        Object mirror = object.host;
        if (mirror == null || mirror.getClass() != home) {
            try {
                mirror = home.getDeclaredConstructor().newInstance();
            } catch (ReflectiveOperationException | LinkageError
                    | RuntimeException e) {
                // Generated stub ctors (SDK-native no-arg signatures like
                // Outline(), Rect()) throw from their own bodies and defeat
                // plain newInstance; allocate without running any ctor so
                // mirror creation never masks the real gap. Reflected
                // on purpose: sun.misc.Unsafe is JDK-internal.
                mirror = allocateInstance(home);
                if (mirror == null) {
                    return null;
                }
            }
            object.host = mirror;
            mirrorOwners.put(mirror, object);
        }
        return mirror;
    }

    /** Unsafe.allocateInstance via reflection; null when unavailable. */
    private static Object allocateInstance(Class<?> home) {
        try {
            Class<?> unsafe = Class.forName("sun.misc.Unsafe");
            java.lang.reflect.Field field = unsafe.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            java.lang.reflect.Method alloc =
                    unsafe.getMethod("allocateInstance", Class.class);
            return alloc.invoke(field.get(null), (Object) home);
        } catch (ReflectiveOperationException | LinkageError
                | RuntimeException e) {
            return null;
        }
    }

    /**
     * Bridges one call argument toward the host mirror call. Model objects
     * (VmObject/VmArray holders) whose type resolves to a framework mirror
     * swap over to that mirror so ported framework bodies see real types;
     * everything else (primitive boxes, real hosts) passes unchanged.
     */
    private Object bridgeMirrorArg(Object arg, List<VmObject> bridged) {
        if (arg instanceof VmObject) {
            VmObject object = (VmObject) arg;
            Class<?> home = mirrorHome(object);
            if (home == null) {
                return arg;
            }
            Object mirror = hostMirror(object, home);
            if (mirror == null) {
                return arg;
            }
            syncFields(object, mirror, true);
            bridged.add(object);
            return mirror;
        }
        return arg;
    }

    /**
     * Converts a host call result back toward the model world: registry
     * mirrors unbridge to their owning model object (identity), everything
     * else keeps the reflective conversion. Framework-typed returns are the
     * remaining case: a module instance the body minted itself (RenderNode
     * beginRecording's RecordingCanvas) has no registration yet, so it gets
     * one and becomes directly callable from guest code. Unbounded growth of
     * the registry is accepted at staging pace; per-frame replacements are
     * garbage only on the host side while the model keeps the last entry.
     */
    private Object unbridgeMirrorValue(Object value, MethodRef ref) {
        if (value != null && mirrorOwners.containsKey(value)) {
            return mirrorOwners.get(value);
        }
        Object boxed = boxReturn(value);
        if (boxed != null && boxed instanceof Object && !(boxed instanceof VmObject)
                && !boxed.getClass().isArray()) {
            String type = ref.returnType;
            if (type != null && type.startsWith("Landroid/")) {
                FwIndex.FwClass fw = framework == null ? null : framework.find(type);
                if (fw != null) {
                    // Register the fresh mirror (identity for later calls,
                    // hostObjectFor semantics) but return the host value
                    // itself: guest iget on non-VmObject targets reads host
                    // fields reflectively (LayoutParams.width round-trip),
                    // while a returning placeholder would leave the iget on
                    // an empty field map reading zero-bits.
                    VmObject placeholder = newPlaceholder(type);
                    placeholder.host = boxed;
                    mirrorOwners.put(boxed, placeholder);
                    return boxed;
                }
            }
        }
        return boxed;
    }

    /**
     * Most-derived concrete framework class on the receiver's super chain:
     * dex supers name framework classes that only the module indexes know,
     * so the walk crosses into FwIndex territory and stops at the first
     * class the runtime classpath can actually instantiate.
     */
    private Class<?> mirrorHome(VmObject object) {
        String current = object.type.type;
        int guard = 0;
        while (current != null && !"Ljava/lang/Object;".equals(current)
                && guard++ < 64) {
            DexClass dex = dexes.findClass(current);
            if (dex != null) {
                current = dex.superclass;
                continue;
            }
            FwIndex.FwClass fw = framework == null ? null : framework.find(current);
            if (fw == null) {
                return null;
            }
            Class<?> home = hostClass(current);
            if (home != null && !home.isInterface() && !home.isEnum()
                    && !Modifier.isAbstract(home.getModifiers())) {
                return home;
            }
            current = fw.superclass;
        }
        return null;
    }

    /**
     * Copies instance fields between the mapped receiver (state keys are
     * {@code L<declaringClass>;.<name>}) and the mirror. {@code forward}
     * runs before the call; without it the mutated mirror state would
     * vanish. Reflection failure stays silent: that field is not passable
     * state and both sides simply won't track it.
     */
    private void syncFields(VmObject object, Object mirror, boolean forward) {
        Class<?> c = mirror.getClass();
        while (c != null && c != Object.class) {
            for (java.lang.reflect.Field f : c.getDeclaredFields()) {
                int mods = f.getModifiers();
                if (Modifier.isStatic(mods)) {
                    continue;
                }
                String key = "L" + c.getName().replace('.', '/') + ";."
                        + f.getName();
                try {
                    f.setAccessible(true);
                    if (forward) {
                        Object value = object.getField(key);
                        if (value != null) {
                            f.set(mirror, bridgeValue(value));
                        }
                    } else {
                        Object value = f.get(mirror);
                        if (value != null && !isNullish(value)) {
                            object.putField(key, unbridgeValue(value));
                        }
                    }
                } catch (IllegalAccessException | IllegalArgumentException ignored) {
                    // Not passable state; skip the field.
                }
            }
            c = c.getSuperclass();
        }
    }

    /**
     * Converts a model value toward the mirror world: list elements that
     * already carry a registered mirror swap to it, so ported framework
     * code iterating {@code List<View>} sees hosts instead of placeholders
     * (the declared {@code View} return of getChildAt would otherwise cast
     * a VmObject and crash). Lookup-only by design: element field state is
     * kept coherent by the element's own arg-bridge crossings.
     */
    private Object bridgeValue(Object value) {
        if (!(value instanceof List)) {
            return value;
        }
        List<Object> bridged = new ArrayList<>();
        for (Object element : (List<?>) value) {
            if (element instanceof VmObject && ((VmObject) element).host != null) {
                bridged.add(((VmObject) element).host);
            } else {
                bridged.add(element);
            }
        }
        return bridged;
    }

    /**
     * Converts a mirror-world value toward the canonical model keys:
     * registry mirrors become their owning model objects (identity: the
     * child link points at the same object the guest allocated, not at a
     * host mirror), lists convert element-wise so model readers like the
     * attach-propagation builtin keep seeing placeholders.
     */
    private Object unbridgeValue(Object value) {
        if (mirrorOwners.containsKey(value)) {
            return mirrorOwners.get(value);
        }
        if (value instanceof List) {
            List<Object> unbridged = new ArrayList<>();
            for (Object element : (List<?>) value) {
                if (element != null && mirrorOwners.containsKey(element)) {
                    unbridged.add(mirrorOwners.get(element));
                } else {
                    unbridged.add(element);
                }
            }
            return unbridged;
        }
        return value;
    }

    /**
     * Reflective instance-field access against host objects (host values
     * of java.* or ported framework types): iget/iput on a non-mapped
     * receiver resolves the declared owner and reads/writes the real
     * field. Guest placeholders never reach here.
     */
    private Object hostField(Object target, FieldRef ref, boolean read,
            Object value) {
        Class<?> owner = hostClass(ref.definingClass);
        java.lang.reflect.Field field = null;
        if (owner == null || !owner.isInstance(target)) {
            throw new VmException("no host field: " + ref.definingClass
                    + "." + ref.name + " (" + target.getClass().getTypeName()
                    + ")");
        }
        for (Class<?> c = owner; c != null && c != Object.class
                && field == null; c = c.getSuperclass()) {
            try {
                field = c.getDeclaredField(ref.name);
            } catch (NoSuchFieldException e) {
                // keep walking up the chain
            }
        }
        if (field == null) {
            throw new VmException("no host field: " + ref.definingClass
                    + "." + ref.name);
        }
        try {
            field.setAccessible(true);
            if (read) {
                return field.get(target);
            }
            if (value == null && field.getType().isPrimitive()) {
                value = zeroValue(field.getType());
            }
            field.set(target, value);
            return null;
        } catch (IllegalAccessException e) {
            throw new VmException("no host field: " + ref.definingClass
                    + "." + ref.name, e);
        }
    }

    private static Object zeroValue(Class<?> type) {
        if (type == long.class) {
            return 0L;
        }
        if (type == boolean.class) {
            return false;
        }
        if (type == char.class) {
            return '\0';
        }
        if (type == double.class) {
            return 0.0d;
        }
        if (type == float.class) {
            return 0.0f;
        }
        return 0;
    }

    private Class<?> hostClass(String descriptor) {
        if (hostClasses.containsKey(descriptor)) {
            return hostClasses.get(descriptor);
        }
        Class<?> resolved = null;
        if (isReflectable(descriptor)) {
            String binary = descriptor.substring(1, descriptor.length() - 1).replace('/', '.');
            try {
                resolved = Class.forName(binary);
            } catch (ClassNotFoundException | LinkageError e) {
                resolved = null;
            }
        }
        hostClasses.put(descriptor, resolved);
        return resolved;
    }

    /** Miss-or-value carrier for reflective calls. */
    private static final class ReflectResult {
        static final ReflectResult MISS = new ReflectResult(false, null);
        final boolean hit;
        final Object value;

        ReflectResult(boolean hit, Object value) {
            this.hit = hit;
            this.value = value;
        }
    }

    /**
     * A resolved {@code java.lang.reflect.Method}: exactly one target kind.
     * Invocation reuses the normal dispatch for that kind, so model objects
     * never cross into host reflection and denied classes stay denied
     * (they resolve to nothing and fail lookup with NoSuchMethodException).
     */
    private static final class MethodTarget {
        DexMethod dexMethod;
        DexClass dexHome;
        HostMethod builtin;
        List<String> parameters = new ArrayList<>();
        Method hostMethod;
        String label;
    }

    /**
     * A resolved {@code java.lang.reflect.Constructor}. Allocation mirrors
     * {@code new-instance} (host values, placeholders, dex bodies), then the
     * recorded target runs as the constructor body.
     */
    private static final class ConstructorTarget {
        String classDesc;
        DexMethod dexMethod;
        DexClass dexHome;
        HostMethod builtin;
        List<String> parameters = new ArrayList<>();
        java.lang.reflect.Constructor<?> hostConstructor;
        String label;
    }

    /**
     * A resolved {@code java.lang.reflect.Field}. Values flow through the
     * same stores as everything else (interpreter statics, VmObject fields,
     * jar constants, host reflection), so reflective reads observe the same
     * state direct access would.
     */
    private static final class FieldTarget {
        String definingClass;
        String name;
        String type;
        java.lang.reflect.Field hostField;
    }

    private static final String FIELD_TARGET = "Ljava/lang/reflect/Field;.target";
    private static final String METHOD_TARGET = "Ljava/lang/reflect/Method;.target";
    private static final String CTOR_TARGET = "Ljava/lang/reflect/Constructor;.target";

    /** Dex class lookup for host-side dispatch helpers (see VmTask). */
    public DexClass findClass(String type) {
        return dexes.findClass(type);
    }

    /** Overrides the reported API level (manifest targetSdk). */
    public void setSdkInt(int sdkInt) {
        this.sdkInt = sdkInt;
    }

    /**
     * Reads a static field exactly the way {@code sget-object} would
     * (class init, interpreter statics, framework constants and
     * materializers, host reflection). Lets the driver fetch enum values
     * and constants without a dex round-trip.
     */
    public Object staticField(String classDesc, String name, String type) {
        return staticRef(classDesc + "->" + name + ":" + type);
    }

    /**
     * Frame callbacks posted but never fired headless. The driver pumps
     * them as the VSync equivalent; callbacks may re-post, so callers
     * bound the pump.
     */
    public List<Object> frameCallbacks() {
        if (choreographer == null) {
            return Collections.emptyList();
        }
        Object held = choreographer
                .getField("Landroid/view/Choreographer;.frameCallbacks");
        if (!(held instanceof List)) {
            return Collections.emptyList();
        }
        return new ArrayList<>((List<?>) held);
    }

    private static String argKey(Object arg) {
        if (arg == null) {
            return "null";
        }
        if (arg instanceof VmObject) {
            return "dex:" + ((VmObject) arg).type.type;
        }
        if (arg instanceof VmArray) {
            return "dex:[" + ((VmArray) arg).elementType;
        }
        return "host:" + arg.getClass().getName();
    }

    private static boolean argAssignable(Class<?> host, Object arg) {
        if (arg == null) {
            return !host.isPrimitive();
        }
        if (host.isPrimitive()) {
            if (host == boolean.class) {
                return arg instanceof Boolean || arg instanceof Integer;
            }
            if (host == char.class) {
                return arg instanceof Character || arg instanceof Integer;
            }
            if (host == byte.class) {
                return arg instanceof Byte || arg instanceof Integer;
            }
            if (host == short.class) {
                return arg instanceof Short || arg instanceof Integer;
            }
            if (host == int.class) {
                return arg instanceof Integer;
            }
            if (host == long.class) {
                return arg instanceof Long;
            }
            if (host == float.class) {
                return arg instanceof Float;
            }
            if (host == double.class) {
                return arg instanceof Double;
            }
            return false;
        }
        if (host.isArray()) {
            return arg instanceof VmArray || arg.getClass().isArray();
        }
        if (arg instanceof VmObject || arg instanceof VmArray || arg instanceof LazyHolder
                || arg instanceof VmClass || arg instanceof FieldUpdater) {
            if (host == Object.class) {
                return true;
            }
            // Model comparators cross via bridging proxy (see VmComparator);
            // every other model object stays put.
            return host.isInterface()
                    && host.isAssignableFrom(java.util.Comparator.class);
        }
        return host.isInstance(arg);
    }

    private static Object toHost(Class<?> host, Object arg) {
        if (arg == null) {
            return null;
        }
        if (!host.isPrimitive()) {
            // Zero-bits are null in reference positions (Dalvik cannot
            // tell them apart); only the designated heap zero survives
            // as an object. Without this, host identity checks (like
            // AtomicReferenceArray CAS against null) can never match.
            if (isNullish(arg) && !(arg instanceof BoxedZero)) {
                return null;
            }
            if (host.isArray() && arg instanceof VmArray) {
                return toHostArray(host.getComponentType(), (VmArray) arg);
            }
            // Model collections crossing to host collection interfaces
            // convert element-wise (storage only; model elements stay put).
            // Concrete host types (including Object) keep the value as-is.
            if (host.isInterface() && arg instanceof VmArray) {
                VmArray array = (VmArray) arg;
                if (host.isAssignableFrom(java.util.ArrayList.class)) {
                    List<Object> list = new ArrayList<>(array.length());
                    for (int i = 0; i < array.length(); i++) {
                        list.add(boxReturn(array.get(i)));
                    }
                    return list;
                }
                if (host.isAssignableFrom(java.util.LinkedHashSet.class)) {
                    Set<Object> set = new LinkedHashSet<>();
                    for (int i = 0; i < array.length(); i++) {
                        set.add(boxReturn(array.get(i)));
                    }
                    return set;
                }
            }
            if (host.isInterface() && arg instanceof VmObject) {
                String descriptor = ((VmObject) arg).type.type;
                if (descriptor.equals("Lkotlin/collections/EmptyList;")
                        && host.isAssignableFrom(java.util.List.class)) {
                    return java.util.Collections.emptyList();
                }
                if (descriptor.equals("Lkotlin/collections/EmptySet;")
                        && host.isAssignableFrom(java.util.Set.class)) {
                    return java.util.Collections.emptySet();
                }
                if (descriptor.equals("Lkotlin/collections/EmptyMap;")
                        && host.isAssignableFrom(java.util.Map.class)) {
                    return java.util.Collections.emptyMap();
                }
            }
            return arg;
        }
        if (host == boolean.class) {
            return arg instanceof Boolean ? arg : asInt(arg) != 0;
        }
        if (host == char.class) {
            return (char) (arg instanceof Character ? ((Character) arg).charValue()
                    : asInt(arg));
        }
        if (host == byte.class) {
            return (byte) (arg instanceof Byte ? ((Byte) arg).byteValue() : asInt(arg));
        }
        if (host == short.class) {
            return (short) (arg instanceof Short ? ((Short) arg).shortValue() : asInt(arg));
        }
        if (host == int.class) {
            return asInt(arg);
        }
        if (host == long.class) {
            return asLong(arg);
        }
        if (host == float.class) {
            return asFloatBits(arg);
        }
        return asDoubleBits(arg);
    }

    private static Object toHostArray(Class<?> component, VmArray array) {
        Object out = Array.newInstance(component, array.length());
        for (int i = 0; i < array.length(); i++) {
            Array.set(out, i, toHost(component, array.get(i)));
        }
        return out;
    }

    /**
     * Host argument conversion with bridging: model comparators become host
     * proxies that call back into dex (see VmComparator), everything else
     * goes through plain toHost.
     */
    private Object bridgeArg(Class<?> host, Object arg) {
        if (arg instanceof VmObject && host.isInterface()
                && host.isAssignableFrom(java.util.Comparator.class)) {
            return new VmComparator(this, (VmObject) arg);
        }
        if (arg instanceof VmObject && host.isEnum()) {
            Object bridged = hostEnumConstant(host, (VmObject) arg);
            if (bridged != null) {
                return bridged;
            }
        }
        return toHost(host, arg);
    }

    /**
     * Framework enum constants cross the adapter boundary as placeholders
     * (frameworkEnumValue); mirror methods whose parameters take the host
     * enum resolve back to the module constant by descriptor + ordinal.
     * Ordinal parity holds because the skeleton enum was generated from the
     * same android.jar the APK compiled against (see frameworkEnumValue).
     */
    private Object hostEnumConstant(Class<?> host, VmObject model) {
        String descriptor = model.type == null ? null : model.type.type;
        if (descriptor == null) {
            return null;
        }
        String wanted = "L" + host.getName().replace('.', '/') + ";";
        if (!wanted.equals(descriptor) || framework == null
                || framework.find(descriptor) == null) {
            return null;
        }
        Object ordinal = model.getField("Ljava/lang/Enum;.ordinal");
        Object[] constants = host.getEnumConstants();
        int index = ordinal instanceof Integer ? (Integer) ordinal : -1;
        if (index < 0 || index >= constants.length) {
            throw new VmException("enum ordinal out of range: " + descriptor
                    + " ordinal " + index);
        }
        return constants[index];
    }

    /**
     * Host-side proxy for a model comparator. Comparisons re-enter the
     * interpreter (same thread, inline), so host-sorted structures work
     * with dex ordering. Mirrors VmTask for runnables.
     */
    private static final class VmComparator implements java.util.Comparator<Object> {
        private final Interpreter vm;
        private final VmObject target;

        VmComparator(Interpreter vm, VmObject target) {
            this.vm = vm;
            this.target = target;
        }

        @Override
        public int compare(Object left, Object right) {
            return vm.compareObjects(target, left, right);
        }
    }

    private static Object boxReturn(Object value) {
        if (value == null) {
            return null;
        }        if (value instanceof Boolean) {
            return ((Boolean) value) ? 1 : 0;
        }
        if (value instanceof Character) {
            return (int) ((Character) value).charValue();
        }
        if (value instanceof Byte || value instanceof Short || value instanceof Integer) {
            return ((Number) value).intValue();
        }
        if (value.getClass().isArray()) {
            int length = Array.getLength(value);
            VmArray out = new VmArray(
                    hostDescriptor(value.getClass().getComponentType()), length);
            for (int i = 0; i < length; i++) {
                out.set(i, aputValue(out, boxReturn(Array.get(value, i))));
            }
            return out;
        }
        return value;
    }

    /**
     * Copies host-side array mutations back into the model array. Host calls
     * receive converted copies (a Dalvik array is not a host array), so
     * without this, in-place host operations like {@code Arrays.fill} or
     * {@code Arrays.sort} silently vanish while the model keeps defaults.
     */
    private static void copyBack(Object hostValue, Object modelValue) {
        if (hostValue == null || !hostValue.getClass().isArray()
                || !(modelValue instanceof VmArray)) {
            return;
        }
        VmArray model = (VmArray) modelValue;
        int length = Math.min(Array.getLength(hostValue), model.length());
        for (int i = 0; i < length; i++) {
            Object hostElement = Array.get(hostValue, i);
            Object modelElement = model.get(i);
            if (hostElement != null && hostElement.getClass().isArray()
                    && modelElement instanceof VmArray) {
                copyBack(hostElement, modelElement);
            } else {
                model.set(i, aputValue(model, boxReturn(hostElement)));
            }
        }
    }

    private static String methodDescriptor(Method method) {
        StringBuilder out = new StringBuilder("(");
        for (Class<?> param : method.getParameterTypes()) {
            out.append(hostDescriptor(param));
        }
        return out.append(')').append(hostDescriptor(method.getReturnType())).toString();
    }

    private static String dexDescriptor(MethodRef ref) {
        StringBuilder out = new StringBuilder("(");
        for (String param : ref.parameters) {
            out.append(param);
        }
        return out.append(')').append(ref.returnType).toString();
    }

    private Method selectMethod(Class<?> host, MethodRef ref, Object[] args,
            boolean wantStatic) {
        StringBuilder key = new StringBuilder(host.getName()).append('#').append(ref.name)
                .append('#').append(ref.parameters.size()).append(wantStatic ? 'S' : 'I');
        for (Object arg : args) {
            key.append('#').append(argKey(arg));
        }
        String cacheKey = key.toString();
        if (hostMethods.containsKey(cacheKey)) {
            return hostMethods.get(cacheKey);
        }
        List<Method> candidates = new ArrayList<>();
        for (Method candidate : host.getMethods()) {
            if (!candidate.getName().equals(ref.name)
                    || Modifier.isStatic(candidate.getModifiers()) != wantStatic
                    || candidate.isBridge() || candidate.isSynthetic()
                    || candidate.getParameterCount() != ref.parameters.size()) {
                continue;
            }
            candidates.add(candidate);
        }
        Method selected = null;
        String wanted = dexDescriptor(ref);
        for (Method candidate : candidates) {
            if (methodDescriptor(candidate).equals(wanted)) {
                selected = candidate;
                break;
            }
        }
        if (selected == null) {
            candidates.sort((left, right) ->
                    methodDescriptor(left).compareTo(methodDescriptor(right)));
            for (Method candidate : candidates) {
                Class<?>[] params = candidate.getParameterTypes();
                boolean ok = true;
                for (int i = 0; i < params.length; i++) {
                    if (!argAssignable(params[i], args[i])) {
                        ok = false;
                        break;
                    }
                }
                if (ok) {
                    selected = candidate;
                    break;
                }
            }
        }
        hostMethods.put(cacheKey, selected);
        return selected;
    }

    /**
     * Reflective fallback for {@code java.*} calls the table does not cover.
     * Host receivers (or none, for static) only; model objects never cross.
     * Deny-by-default gates (follow-up audit 2026-10-19): static calls
     * resolve only through an allowlisted defining class, and an instance
     * receiver's runtime class must be allowlisted too — a host object of
     * a non-listed type can never become a reflective receiver, whatever
     * the declared claim (dex subclasses delegate on guest declaring
     * classes, so the instance gate keys the runtime class).
     */
    private ReflectResult reflectCall(Object receiver, MethodRef ref, Object[] args,
            boolean wantStatic) {
        Class<?> host;
        if (receiver == null) {
            if (!isReflectable(ref.definingClass)) {
                return ReflectResult.MISS;
            }
            host = hostClass(ref.definingClass);
            if (host == null) {
                return ReflectResult.MISS;
            }
        } else {
            if (receiver instanceof VmObject || receiver instanceof VmArray
                    || receiver instanceof LazyHolder || receiver instanceof VmClass
                    || receiver instanceof FieldUpdater) {
                return ReflectResult.MISS;
            }
            host = receiver.getClass();
            // Deny java.* receivers the allowlist does not name (a leaked
            // Runtime/File/ClassLoader object could never become a
            // reflective receiver). Interpreter-internal models (BoxedZero
            // extends Number) stay legitimate receivers; the declared-class
            // check below keeps their dispatch honest.
            String runtimeDesc = descriptorOf(host);
            if (runtimeDesc.startsWith("Ljava/")
                    && !isReflectable(runtimeDesc)) {
                return ReflectResult.MISS;
            }
            Class<?> declared = hostClass(ref.definingClass);
            if (declared != null && !declared.isInstance(receiver)) {
                return ReflectResult.MISS;
            }
        }
        Method method = selectMethod(host, ref, args, wantStatic);
        if (method == null) {
            return ReflectResult.MISS;
        }
        Class<?>[] params = method.getParameterTypes();
        Object[] hostArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            hostArgs[i] = bridgeArg(params[i], args[i]);
        }
        try {
            Object returned = method.invoke(receiver, hostArgs);
            for (int i = 0; i < args.length; i++) {
                copyBack(hostArgs[i], args[i]);
            }
            return new ReflectResult(true, boxReturn(returned));
        } catch (IllegalAccessException e) {
            return ReflectResult.MISS;
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw new VmThrow(cause);
        }
    }

    private ReflectResult reflectStaticField(String definingClass, String name) {
        Class<?> host = hostClass(definingClass);
        if (host == null) {
            return ReflectResult.MISS;
        }
        try {
            return new ReflectResult(true, boxReturn(host.getField(name).get(null)));
        } catch (NoSuchFieldException | IllegalAccessException | LinkageError e) {
            return ReflectResult.MISS;
        }
    }

    private ReflectResult reflectConstruct(MethodRef ref, Object[] args) {
        Class<?> host = hostClass(ref.definingClass);
        if (host == null || host.isInterface() || host.isArray() || host.isEnum()
                || Modifier.isAbstract(host.getModifiers())) {
            return ReflectResult.MISS;
        }
        List<java.lang.reflect.Constructor<?>> candidates = new ArrayList<>();
        for (java.lang.reflect.Constructor<?> candidate : host.getConstructors()) {
            if (candidate.getParameterCount() == ref.parameters.size()
                    && !candidate.isSynthetic()) {
                candidates.add(candidate);
            }
        }
        candidates.sort((left, right) -> {
            StringBuilder l = new StringBuilder("(");
            for (Class<?> p : left.getParameterTypes()) {
                l.append(hostDescriptor(p));
            }
            StringBuilder r = new StringBuilder("(");
            for (Class<?> p : right.getParameterTypes()) {
                r.append(hostDescriptor(p));
            }
            return l.toString().compareTo(r.toString());
        });
        for (java.lang.reflect.Constructor<?> candidate : candidates) {
            Class<?>[] params = candidate.getParameterTypes();
            boolean ok = true;
            for (int i = 0; i < params.length; i++) {
                if (!argAssignable(params[i], args[i])) {
                    ok = false;
                    break;
                }
            }
            if (!ok) {
                continue;
            }
            Object[] hostArgs = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                hostArgs[i] = bridgeArg(params[i], args[i]);
            }
            try {
                return new ReflectResult(true, candidate.newInstance(hostArgs));
            } catch (InstantiationException | IllegalAccessException e) {
                return ReflectResult.MISS;
            } catch (InvocationTargetException e) {
                Throwable cause = e.getCause();
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException) cause;
                }
                if (cause instanceof Error) {
                    throw (Error) cause;
                }
                throw new VmThrow(cause);
            }
        }
        return ReflectResult.MISS;
    }

    /**
     * Dex subclasses of concrete host classes with no instance state of
     * their own (e.g. Compose's {@code TreeSet} over
     * {@code java.util.TreeSet}) store everything in the host superclass:
     * construction re-targets to its constructor so args (like
     * comparators) configure the live object instead of a discarded
     * super-call copy. {@code Object} (the universal base),
     * non-reflectable hosts (threads, loaders), and Throwables (own
     * exception model) are excluded; misses keep normal dex execution.
     * Skips the dex body on hit, so bodies doing more than delegating
     * (beyond statics) would need their effects replayed.
     */
    private ReflectResult hostSuperConstruct(VmObject receiver, MethodRef ref,
            Object[] args) {
        DexClass dex = dexes.findClass(ref.definingClass);
        if (dex == null || dex.superclass == null
                || dex.superclass.equals("Ljava/lang/Object;")
                || dexes.findClass(dex.superclass) != null
                || dex.hasInstanceFields()
                // Java superclasses only: guest android.* subclasses stay
                // placeholders, and framework dispatch mirrors from the
                // ported module instead of re-targeting construction.
                || !dex.superclass.startsWith("Ljava/")
                || !isReflectable(dex.superclass)) {
            return ReflectResult.MISS;
        }
        Class<?> host = hostClass(dex.superclass);
        if (host == null || Throwable.class.isAssignableFrom(host)) {
            return ReflectResult.MISS;
        }
        return reflectConstruct(
                new MethodRef(dex.superclass, ref.name, ref.parameters, ref.returnType),
                args);
    }

    /** Resolves a declared method to exactly one dispatch target. */
    private MethodTarget declaredMethod(String classDesc, String name,
            List<String> parameters) {
        MethodTarget target = new MethodTarget();
        target.label = classDesc + "." + name;
        target.parameters.addAll(parameters);
        DexClass dex = dexes.findClass(classDesc);
        if (dex != null) {
            for (DexMethod candidate : dex.methods) {
                if (candidate.name.equals(name)
                        && candidate.parameterTypes.equals(parameters)) {
                    target.dexMethod = candidate;
                    target.dexHome = dex;
                    return target;
                }
            }
        }
        return finishMethodTarget(target, classDesc, name, parameters, false);
    }

    /**
     * Public method lookup walks the dex hierarchy (most-derived first),
     * then shares the builtin/host tail with declared lookup (host uses
     * the inheriting getMethod instead of getDeclaredMethod).
     */
    private MethodTarget publicMethod(String classDesc, String name,
            List<String> parameters) {
        MethodTarget target = new MethodTarget();
        target.label = classDesc + "." + name;
        target.parameters.addAll(parameters);
        Deque<String> queue = new ArrayDeque<>();
        Set<String> seen = new HashSet<>();
        queue.add(classDesc);
        while (!queue.isEmpty()) {
            String current = queue.removeFirst();
            if (!seen.add(current)) {
                continue;
            }
            DexClass dex = dexes.findClass(current);
            if (dex == null) {
                continue;
            }
            for (DexMethod candidate : dex.methods) {
                if (candidate.name.equals(name)
                        && candidate.parameterTypes.equals(parameters)) {
                    target.dexMethod = candidate;
                    target.dexHome = dex;
                    return target;
                }
            }
            if (dex.superclass != null
                    && !dex.superclass.equals("Ljava/lang/Object;")) {
                queue.add(dex.superclass);
            }
            queue.addAll(dex.interfaces);
        }
        return finishMethodTarget(target, classDesc, name, parameters, true);
    }

    /** Builtin-table/host tail shared by declared and public lookup. */
    private MethodTarget finishMethodTarget(MethodTarget target, String classDesc, String name,
            List<String> parameters, boolean publics) {
        StringBuilder prefix = new StringBuilder(classDesc).append("->")
                .append(name).append('(');
        for (String param : parameters) {
            prefix.append(param);
        }
        prefix.append(')');
        for (Map.Entry<String, HostMethod> entry : builtins.entrySet()) {
            if (entry.getKey().startsWith(prefix.toString())) {
                target.builtin = entry.getValue();
                return target;
            }
        }
        Class<?> host = isReflectable(classDesc) ? hostClass(classDesc) : null;
        if (host != null) {
            Class<?>[] params = new Class<?>[parameters.size()];
            for (int i = 0; i < params.length; i++) {
                try {
                    params[i] = hostParamClass(parameters.get(i));
                } catch (ClassNotFoundException | LinkageError e) {
                    throw new VmThrow(new NoSuchMethodException(target.label));
                }
            }
            try {
                target.hostMethod = publics ? host.getMethod(name, params)
                        : host.getDeclaredMethod(name, params);
                return target;
            } catch (NoSuchMethodException | SecurityException e) {
                throw new VmThrow(new NoSuchMethodException(target.label));
            }
        }
        throw new VmThrow(new NoSuchMethodException(target.label));
    }

    /** VmArray of VmClass to descriptors; null stays empty (no-arg). */
    private static List<String> classArray(Object value) {
        List<String> out = new ArrayList<>();
        if (value instanceof VmArray) {
            VmArray array = (VmArray) value;
            for (int i = 0; i < array.length(); i++) {
                Object element = array.get(i);
                if (!(element instanceof VmClass)) {
                    throw new VmException("not a class");
                }
                out.add(((VmClass) element).descriptor);
            }
        }
        return out;
    }

    /** VmArray to length-exact values; null stays empty (no-arg). */
    private static Object[] objectArray(Object value) {
        if (!(value instanceof VmArray)) {
            return new Object[0];
        }
        VmArray array = (VmArray) value;
        Object[] out = new Object[array.length()];
        for (int i = 0; i < out.length; i++) {
            out[i] = array.get(i);
        }
        return out;
    }

    /** Single-universe class lookup shared by forName and loadClass. */
    private VmClass lookupClass(String binary) {
        String descriptor = "L" + binary.replace('.', '/') + ";";
        if (dexes.findClass(descriptor) != null
                || framework != null && framework.find(descriptor) != null) {
            return new VmClass(descriptor);
        }
        throw new VmThrow(new ClassNotFoundException(binary));
    }

    /**
     * Approximate text width from stored paint state. Average glyphs run
     * half the text size; callers should route through here so real font
     * measurement replaces one seam.
     */
    /**
     * Text of any CharSequence: host strings directly, our spannable
     * store, dex toString (AnnotatedString returns its text), host
     * fallback otherwise.
     */
    private String charSequenceText(Object value) {
        if (value == null || value instanceof String) {
            return (String) value;
        }
        if (value instanceof VmObject) {
            Object stored = ((VmObject) value)
                    .getField("Landroid/text/SpannableString;.mText");
            if (stored instanceof String) {
                return (String) stored;
            }
            try {
                Object text = dispatchDex(value, "toString",
                        Collections.<String>emptyList(), "Ljava/lang/String;",
                        new Object[0]);
                if (text instanceof String) {
                    return (String) text;
                }
            } catch (VmException e) {
                // fall through to host rendering below
            }
        }
        return String.valueOf(value);
    }

    /**
     * Approximate text width from stored paint state. Average glyphs run
     * half the text size; callers should route through here so real font
     * measurement replaces one seam.
     */
    private static float measureText(Object paint, String text, int start, int end) {
        float size = 0.0f;
        float scaleX = 1.0f;
        float spacing = 0.0f;
        if (paint instanceof VmObject) {
            VmObject metrics = (VmObject) paint;
            Object textSize = metrics.getField("Landroid/graphics/Paint;.mTextSize");
            if (textSize != null) {
                size = asFloatBits(textSize);
            }
            Object scale = metrics.getField("Landroid/graphics/Paint;.mTextScaleX");
            if (scale != null) {
                scaleX = asFloatBits(scale);
            }
            Object letter = metrics.getField("Landroid/graphics/Paint;.mLetterSpacing");
            if (letter != null) {
                spacing = asFloatBits(letter);
            }
        }
        int length = text == null ? 0 : text.length();
        int from = Math.min(Math.max(start, 0), length);
        int to = Math.min(Math.max(end, from), length);
        return (to - from) * (size * 0.5f * scaleX + spacing);
    }

    /**
     * Greedy line breaking for StaticLayout.build: longest space-bounded
     * run fitting the outer width per line (overlong first words
     * overflow, explicit newlines force breaks), line height from text
     * size until font metrics arrive.
     */
    private VmObject buildStaticLayout(VmObject builder) {
        Object rawText = builder.getField("Landroid/text/StaticLayout$Builder;.mText");
        String text = rawText instanceof String ? (String) rawText : null;
        Object paint = builder.getField("Landroid/text/StaticLayout$Builder;.mPaint");
        Object startValue = builder.getField("Landroid/text/StaticLayout$Builder;.mStart");
        Object endValue = builder.getField("Landroid/text/StaticLayout$Builder;.mEnd");
        Object widthValue = builder.getField("Landroid/text/StaticLayout$Builder;.mWidth");
        Object multValue =
                builder.getField("Landroid/text/StaticLayout$Builder;.mSpacingMult");
        Object addValue = builder.getField("Landroid/text/StaticLayout$Builder;.mSpacingAdd");
        Object maxValue = builder.getField("Landroid/text/StaticLayout$Builder;.mMaxLines");
        int length = text == null ? 0 : text.length();
        int start = startValue == null ? 0
                : Math.min(Math.max(asInt(startValue), 0), length);
        int end = endValue == null ? length
                : Math.min(Math.max(asInt(endValue), start), length);
        int outerWidth = widthValue == null ? 0 : Math.max(asInt(widthValue), 0);
        float mult = multValue == null ? 1.0f : asFloatBits(multValue);
        float add = addValue == null ? 0.0f : asFloatBits(addValue);
        int maxLines = maxValue == null ? Integer.MAX_VALUE : Math.max(asInt(maxValue), 0);
        List<Integer> starts = new ArrayList<>();
        List<Integer> ends = new ArrayList<>();
        List<Float> widths = new ArrayList<>();
        if (start < end && outerWidth > 0) {
            int i = start;
            while (i < end && starts.size() < maxLines) {
                if (starts.size() + 1 >= maxLines) {
                    starts.add(i);
                    ends.add(end);
                    widths.add(measureText(paint, text, i, end));
                    break;
                }
                int j = i;
                int lastFit = -1;
                while (j < end) {
                    int k = j;
                    while (k < end && text.charAt(k) != ' ' && text.charAt(k) != '\n') {
                        k++;
                    }
                    boolean newline = k < end && text.charAt(k) == '\n';
                    float wordWidth = measureText(paint, text, i, k);
                    if (newline) {
                        lastFit = k;
                        break;
                    }
                    if (wordWidth <= outerWidth || j == i) {
                        lastFit = k;
                        j = k + 1;
                        if (j >= end) {
                            lastFit = end;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (lastFit < 0) {
                    lastFit = Math.min(i + 1, end);
                }
                starts.add(i);
                ends.add(lastFit);
                widths.add(measureText(paint, text, i, lastFit));
                i = lastFit;
                while (i < end && (text.charAt(i) == ' ' || text.charAt(i) == '\n')) {
                    i++;
                }
            }
        } else if (start < end) {
            starts.add(start);
            ends.add(end);
            widths.add(measureText(paint, text, start, end));
        }
        float textSize = 0.0f;
        if (paint instanceof VmObject) {
            Object sizeValue =
                    ((VmObject) paint).getField("Landroid/graphics/Paint;.mTextSize");
            if (sizeValue != null) {
                textSize = asFloatBits(sizeValue);
            }
        }
        float lineHeight = textSize * 1.2f * mult + add;
        VmObject layout = newPlaceholder("Landroid/text/StaticLayout;");
        layout.putField("Landroid/text/StaticLayout;.mLineCount", starts.size());
        layout.putField("Landroid/text/StaticLayout;.mWidth", outerWidth);
        layout.putField("Landroid/text/StaticLayout;.mHeight",
                (int) Math.ceil(lineHeight * starts.size()));
        layout.putField("Landroid/text/StaticLayout;.mText", text);
        layout.putField("Landroid/text/StaticLayout;.mPaint", paint);
        Object fallback = builder.getField(
                "Landroid/text/StaticLayout$Builder;.mFallbackLineSpacing");
        layout.putField("Landroid/text/StaticLayout;.mFallbackLineSpacing",
                fallback == null ? 0 : asInt(fallback));
        VmArray lineStarts = new VmArray("I", starts.size());
        VmArray lineEnds = new VmArray("I", starts.size());
        VmArray lineWidths = new VmArray("F", widths.size());
        VmArray lineTops = new VmArray("I", starts.size());
        for (int i = 0; i < starts.size(); i++) {
            lineStarts.set(i, starts.get(i));
            lineWidths.set(i, widths.get(i));
            lineTops.set(i, Math.round(lineHeight * i));
        }
        // Line ends are tracked during breaking (trailing spaces belong
        // to no line); tops are uniform from the line height.
        for (int i = 0; i < starts.size(); i++) {
            lineEnds.set(i, ends.get(i));
        }
        layout.putField("Landroid/text/StaticLayout;.mLineStarts", lineStarts);
        layout.putField("Landroid/text/StaticLayout;.mLineEnds", lineEnds);
        layout.putField("Landroid/text/StaticLayout;.mLineWidths", lineWidths);
        layout.putField("Landroid/text/StaticLayout;.mLineTops", lineTops);
        return layout;
    }

    /** VmArray store read, or null when absent. */
    private static VmArray layoutFloats(Object receiver, String key) {
        Object value = ((VmObject) receiver).getField(key);
        return value instanceof VmArray ? (VmArray) value : null;
    }

    /**
     * Draws the layout's lines onto the bridged canvas (Layout.draw staging
     * port; see the builtin above for the AOSP derivation). Reads the same
     * StaticLayout state keys the per-line accessors serve, then dispatches
     * {@code Canvas.drawText} through the interpreter (guest-first virtual
     * dispatch, invokeModelVirtual) so guest canvas wrappers like compose's
     * TextAndroidCanvas forward through their own overrides to the real
     * canvas; a direct host-mirror call would bypass them and draw nothing.
     */
    private void layoutDraw(VmObject layout, Object canvasArg) {
        Object text = layout.getField("Landroid/text/StaticLayout;.mText");
        if (!(text instanceof String) || !(canvasArg instanceof VmObject)) {
            return;
        }
        Object paintValue = layout.getField("Landroid/text/StaticLayout;.mPaint");
        if (!(paintValue instanceof VmObject)) {
            return;
        }
        Object countValue = layout.getField("Landroid/text/StaticLayout;.mLineCount");
        int lineCount = countValue == null ? 0 : asInt(countValue);
        if (lineCount <= 0) {
            return;
        }
        Object heightValue = layout.getField("Landroid/text/StaticLayout;.mHeight");
        int height = heightValue == null ? 0 : asInt(heightValue);
        Object sizeValue = ((VmObject) paintValue)
                .getField("Landroid/graphics/Paint;.mTextSize");
        float descent = sizeValue == null ? 0.0f : asFloatBits(sizeValue) * 0.2f;
        MethodRef drawText = MethodRef.parse(
                "Landroid/graphics/Canvas;->drawText"
                        + "(Ljava/lang/String;IIFFLandroid/graphics/Paint;)V");
        for (int line = 0; line < lineCount; line++) {
            int start = layoutLineInt(layout, "Landroid/text/StaticLayout;.mLineStarts", line);
            int end = layoutLineInt(layout, "Landroid/text/StaticLayout;.mLineEnds", line);
            int bottom = line + 1 < lineCount
                    ? layoutLineInt(layout, "Landroid/text/StaticLayout;.mLineTops", line + 1)
                    : height;
            int baseline = bottom - Math.round(descent);
            invokeModelVirtual(canvasArg, drawText, new Object[]{text, start,
                    end, 0.0f, (float) baseline, paintValue});
        }
    }

    /**
     * Guest-first virtual dispatch for host drawing callers: resolves the
     * receiver's runtime class exactly like the interpreter's invoke-virtual
     * arm, runs the DEX override when the guest class declares one (guest
     * canvas wrappers such as compose's TextAndroidCanvas forward through
     * their overrides), then legacy builtins, then framework mirror
     * dispatch (args stay model values; frameworkDispatch bridges them).
     */
    private void invokeModelVirtual(Object receiver, MethodRef ref, Object[] args) {
        Resolved resolved = resolve(runtimeClass(receiver), ref.definingClass, ref, true);
        if (resolved.dexMethod != null) {
            execute(resolved.dexHome, resolved.dexMethod, receiver, args);
            return;
        }
        if (resolved.builtin != null) {
            resolved.builtin.invoke(receiver, args);
            return;
        }
        Outcome outcome = frameworkDispatch(receiver, ref, args);
        if (!outcome.handled) {
            throw new VmException("no such method: " + ref.definingClass
                    + "->" + ref.name + " (host virtual dispatch)");
        }
    }

    /** Approximated ascent (0.8 of text size) backing baseline/ascent reads. */
    private static float layoutAscent(VmObject layout) {
        Object paint = layout.getField("Landroid/text/StaticLayout;.mPaint");
        if (!(paint instanceof VmObject)) {
            return 0.0f;
        }
        Object textSize = ((VmObject) paint).getField("Landroid/graphics/Paint;.mTextSize");
        return textSize == null ? 0.0f : asFloatBits(textSize) * 0.8f;
    }

    /** Indexed int read with lenient out-of-range default 0. */
    private static int layoutLineInt(Object receiver, String key, int line) {
        VmArray array = layoutFloats(receiver, key);
        return array == null || line < 0 || line >= array.length() ? 0
                : asInt(array.get(line));
    }

    /** Descriptor to host class for reflective parameter matching. */
    private static Class<?> hostParamClass(String descriptor) throws ClassNotFoundException {
        switch (descriptor) {
            case "Z":
                return boolean.class;
            case "B":
                return byte.class;
            case "S":
                return short.class;
            case "C":
                return char.class;
            case "I":
                return int.class;
            case "J":
                return long.class;
            case "F":
                return float.class;
            case "D":
                return double.class;
            case "V":
                return void.class;
            default:
                return Class.forName(
                        descriptor.substring(1, descriptor.length() - 1).replace('/', '.'));
        }
    }

    /** Invokes a resolved Method target through its own dispatch. */
    private Object invokeTarget(MethodTarget target, Object receiver, Object[] callArgs) {
        if (target.dexMethod != null) {
            boolean isStatic = (target.dexMethod.accessFlags & ACC_STATIC) != 0;
            Object thisRef = receiver;
            if (isStatic) {
                thisRef = null;
                ensureInit(target.dexHome);
            } else if (isNullish(receiver)) {
                throw new NullPointerException("null receiver: " + target.label);
            }
            return execute(target.dexHome, target.dexMethod, thisRef, callArgs);
        }
        if (target.builtin != null) {
            if (callArgs.length != target.parameters.size()) {
                throw new IllegalArgumentException("arity: " + target.label);
            }
            try {
                return target.builtin.invoke(isNullish(receiver) ? null : receiver,
                        callArgs);
            } catch (ClassCastException e) {
                throw new VmException("bridge mismatch: " + target.label, e);
            }
        }
        if (receiver instanceof VmObject || receiver instanceof VmArray
                || receiver instanceof VmClass || receiver instanceof LazyHolder
                || receiver instanceof FieldUpdater) {
            throw new IllegalArgumentException("not a host receiver");
        }
        Method method = target.hostMethod;
        Class<?>[] params = method.getParameterTypes();
        if (callArgs.length != params.length) {
            throw new IllegalArgumentException("arity: " + target.label);
        }
        Object[] hostArgs = new Object[callArgs.length];
        for (int i = 0; i < callArgs.length; i++) {
            hostArgs[i] = bridgeArg(params[i], callArgs[i]);
        }
        try {
            Object returned = method.invoke(isNullish(receiver) ? null : receiver,
                    hostArgs);
            for (int i = 0; i < callArgs.length; i++) {
                copyBack(hostArgs[i], callArgs[i]);
            }
            return boxReturn(returned);
        } catch (IllegalAccessException e) {
            throw new VmThrow(e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw new VmThrow(cause);
        }
    }

    /** Resolves a declared constructor to exactly one dispatch target. */
    private ConstructorTarget declaredConstructor(String classDesc,
            List<String> parameters) {
        ConstructorTarget target = new ConstructorTarget();
        target.classDesc = classDesc;
        target.label = classDesc + ".<init>";
        target.parameters.addAll(parameters);
        DexClass dex = dexes.findClass(classDesc);
        if (dex != null) {
            for (DexMethod candidate : dex.methods) {
                if (candidate.name.equals("<init>")
                        && candidate.parameterTypes.equals(parameters)) {
                    target.dexMethod = candidate;
                    target.dexHome = dex;
                    return target;
                }
            }
        }
        StringBuilder prefix = new StringBuilder(classDesc).append("-><init>(");
        for (String param : parameters) {
            prefix.append(param);
        }
        prefix.append(')');
        for (Map.Entry<String, HostMethod> entry : builtins.entrySet()) {
            if (entry.getKey().startsWith(prefix.toString())) {
                target.builtin = entry.getValue();
                return target;
            }
        }
        Class<?> host = isReflectable(classDesc) ? hostClass(classDesc) : null;
        if (host != null && !host.isInterface() && !host.isArray() && !host.isEnum()
                && !Modifier.isAbstract(host.getModifiers())) {
            Class<?>[] params = new Class<?>[parameters.size()];
            for (int i = 0; i < params.length; i++) {
                try {
                    params[i] = hostParamClass(parameters.get(i));
                } catch (ClassNotFoundException | LinkageError e) {
                    throw new VmThrow(new NoSuchMethodException(target.label));
                }
            }
            try {
                target.hostConstructor = host.getDeclaredConstructor(params);
                return target;
            } catch (NoSuchMethodException | SecurityException e) {
                throw new VmThrow(new NoSuchMethodException(target.label));
            }
        }
        throw new VmThrow(new NoSuchMethodException(target.label));
    }

    /** Allocates like {@code new-instance}, then runs the constructor body. */
    private Object newInstanceTarget(ConstructorTarget target, Object[] callArgs) {
        if (target.dexMethod != null) {
            ensureInit(target.dexHome);
            VmObject instance = new VmObject(target.dexHome);
            ReflectResult hostSuper = hostSuperConstruct(instance,
                    new MethodRef(target.classDesc, "<init>", target.parameters, "V"),
                    callArgs);
            if (hostSuper.hit) {
                return hostSuper.value;
            }
            execute(target.dexHome, target.dexMethod, instance, callArgs);
            return instance;
        }
        Object created = createInstance(target.classDesc);
        if (created == null) {
            DexClass dex = dexes.findClass(target.classDesc);
            if (dex == null) {
                throw new VmException("unimplemented: " + target.classDesc);
            }
            ensureInit(dex);
            created = new VmObject(dex);
        }
        if (target.builtin != null) {
            if (callArgs.length != target.parameters.size()) {
                throw new IllegalArgumentException("arity: " + target.label);
            }
            try {
                target.builtin.invoke(created, callArgs);
            } catch (ClassCastException e) {
                throw new VmException("bridge mismatch: " + target.label, e);
            }
            return created;
        }
        try {
            java.lang.reflect.Constructor<?> ctor = target.hostConstructor;
            Class<?>[] params = ctor.getParameterTypes();
            if (callArgs.length != params.length) {
                throw new IllegalArgumentException("arity: " + target.label);
            }
            Object[] hostArgs = new Object[callArgs.length];
            for (int i = 0; i < callArgs.length; i++) {
                hostArgs[i] = bridgeArg(params[i], callArgs[i]);
            }
            Object instance = ctor.newInstance(hostArgs);
            for (int i = 0; i < callArgs.length; i++) {
                copyBack(hostArgs[i], callArgs[i]);
            }
            return boxReturn(instance);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new VmThrow(e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw new VmThrow(cause);
        }
    }

    /** Resolves a field to exactly one read path. */
    private FieldTarget declaredField(String classDesc, String name) {
        FieldTarget target = new FieldTarget();
        target.definingClass = classDesc;
        target.name = name;
        if (dexes.findClass(classDesc) != null) {
            return target;
        }
        if (framework != null) {
            String current = classDesc;
            while (current != null) {
                FwIndex.FwClass fw = framework.find(current);
                if (fw == null) {
                    break;
                }
                for (FwIndex.FwField field : fw.fields) {
                    if (field.name.equals(name)) {
                        target.type = field.type;
                        return target;
                    }
                }
                current = fw.superclass;
            }
        }
        Class<?> host = isReflectable(classDesc) ? hostClass(classDesc) : null;
        if (host != null) {
            try {
                target.hostField = host.getField(name);
                return target;
            } catch (NoSuchFieldException | SecurityException e) {
                throw new VmThrow(new NoSuchFieldException(classDesc + "." + name));
            }
        }
        throw new VmThrow(new NoSuchFieldException(classDesc + "." + name));
    }

    private static FieldTarget targetOf(Object receiver) {
        if (!(receiver instanceof VmObject)) {
            throw new VmException("not a field");
        }
        Object target = ((VmObject) receiver).getField(FIELD_TARGET);
        if (!(target instanceof FieldTarget)) {
            throw new VmException("not a field");
        }
        return (FieldTarget) target;
    }

    /** Reads through a resolved Field target. */
    private Object fieldGet(FieldTarget target, Object receiver) {
        if (target.hostField != null) {
            Object hostRecv = null;
            if (receiver != null && !isNullish(receiver)) {
                if (receiver instanceof VmObject || receiver instanceof VmArray
                        || receiver instanceof VmClass || receiver instanceof LazyHolder
                        || receiver instanceof FieldUpdater) {
                    throw new IllegalArgumentException("not a host receiver");
                }
                hostRecv = receiver;
            }
            try {
                return boxReturn(target.hostField.get(hostRecv));
            } catch (IllegalAccessException e) {
                throw new VmThrow(e);
            }
        }
        if (receiver != null && !isNullish(receiver)) {
            if (!(receiver instanceof VmObject)) {
                throw new IllegalArgumentException("not an instance");
            }
            return instanceField((VmObject) receiver, target.definingClass, target.name);
        }
        Object found = staticRead(target.definingClass, target.name, target.type);
        if (found != null) {
            return found;
        }
        if (target.definingClass.equals("Landroid/os/Build$VERSION;")
                && target.name.equals("SDK_INT")) {
            return sdkInt;
        }
        return null;
    }

    private VmException gapMessage(String defineDesc, MethodRef ref, String reference,
            Resolved resolved, Object receiver) {
        // Naming the receiver type pinpoints which placeholder/host shape the
        // gap actually bit on (identical interface holes differ by dispatch).
        String suffix = "";
        if (receiver instanceof VmObject vm) {
            suffix = " (on " + vm.type.type + ")";
        } else if (receiver != null) {
            suffix = " (on " + receiver.getClass().getTypeName() + ")";
        }
        if (resolved.exists) {
            return new VmException("unimplemented: " + reference + suffix);
        }
        if (resolved.unknownSuper != null && !resolved.unknownSuper.equals(defineDesc)) {
            return new VmException("unimplemented class: " + resolved.unknownSuper + suffix);
        }
        if (dexes.findClass(defineDesc) != null
                || framework != null && framework.find(defineDesc) != null) {
            return new VmException("no such method: " + reference + suffix);
        }
        return new VmException("unimplemented: " + reference + suffix);
    }

    private static DexMethod findMethod(DexClass cls, MethodRef ref) {
        for (DexMethod candidate : cls.methods) {
            if (candidate.name.equals(ref.name)
                    && candidate.parameterTypes.equals(ref.parameters)
                    && candidate.returnType.equals(ref.returnType)) {
                return candidate;
            }
        }
        return null;
    }

    /** Kotlin {@code LazyKt.lazy(...)} (R8 may strip the real body). */
    private static boolean isKotlinLazy(MethodRef ref) {
        if (!ref.definingClass.equals("Lkotlin/LazyKt;") || !ref.name.equals("lazy")
                || !ref.returnType.equals("Lkotlin/Lazy;")) {
            return false;
        }
        int params = ref.parameters.size();
        return params == 1 || params == 2;
    }

    /** Kotlin {@code Lazy.getValue/isInitialized} on a holder. */
    private static boolean runLazy(MethodRef ref) {
        if (ref.name.equals("getValue") && ref.parameters.size() == 2) {
            return true;
        }
        return ref.name.equals("isInitialized") && ref.parameters.isEmpty();
    }

    private static Object[] readArgs(DexMethod callee, List<Integer> slots, int first,
            Object[] regs) {
        Object[] callArgs = new Object[callee.parameterTypes.size()];
        int slot = first;
        for (int i = 0; i < callArgs.length; i++) {
            callArgs[i] = regs[slots.get(slot)];
            slot += Descriptors.isWide(callee.parameterTypes.get(i)) ? 2 : 1;
        }
        return callArgs;
    }

    private static Object[] readRefArgs(MethodRef ref, List<Integer> slots, int first,
            Object[] regs) {
        Object[] callArgs = new Object[ref.parameters.size()];
        int slot = first;
        for (int i = 0; i < callArgs.length; i++) {
            callArgs[i] = regs[slots.get(slot)];
            slot += Descriptors.isWide(ref.parameters.get(i)) ? 2 : 1;
        }
        return callArgs;
    }

    /**
     * Runs a method on a DEX-implemented object without builtins or
     * defaults (for emulating interface default methods). Framework
     * bodies cannot run, so the walk stops at the dex boundary.
     */
    private Object dispatchDex(Object receiver, String name, List<String> params, String ret,
            Object[] args) {
        VmObject object = (VmObject) receiver;
        String current = object.type.type;
        Set<String> seen = new HashSet<>();
        while (current != null && seen.add(current)
                && !current.equals("Ljava/lang/Object;")) {
            DexClass dex = dexes.findClass(current);
            if (dex == null) {
                break;
            }
            for (DexMethod candidate : dex.methods) {
                if (candidate.name.equals(name) && candidate.parameterTypes.equals(params)
                        && candidate.returnType.equals(ret)) {
                    return execute(dex, candidate, receiver, args);
                }
            }
            current = dex.superclass;
        }
        throw new VmException("unimplemented: " + object.type.type + "->" + name);
    }

    private boolean catches(String handlerType, Object value) {
        if (value instanceof java.lang.Throwable) {
            return hostAssignable(handlerType, value.getClass());
        }
        if (!(value instanceof VmObject)) {
            return false;
        }
        try {
            return isInstance(value, handlerType);
        } catch (VmException unknown) {
            return false;
        }
    }

    private void builtin(String key, HostMethod impl) {
        builtins.put(key, impl);
    }

    private static String argString(Object[] args, int i) {
        return (String) args[i];
    }

    private static int argInt(Object[] args, int i) {
        return ((Number) args[i]).intValue();
    }

    private static long argLong(Object[] args, int i) {
        return ((Number) args[i]).longValue();
    }

    private static float argFloat(Object[] args, int i) {
        return asFloatBits(args[i]);
    }

    private static double argDouble(Object[] args, int i) {
        return asDoubleBits(args[i]);
    }

    private static int bool(boolean value) {
        return value ? 1 : 0;
    }

    private static VmArray stringArray(String[] values) {
        VmArray array = new VmArray("Ljava/lang/String;", values.length);
        for (int i = 0; i < values.length; i++) {
            array.set(i, values[i]);
        }
        return array;
    }

    private static VmArray byteArray(byte[] values) {
        VmArray array = new VmArray("B", values.length);
        for (int i = 0; i < values.length; i++) {
            array.set(i, (int) values[i]);
        }
        return array;
    }

    private static VmArray charArray(char[] values) {
        VmArray array = new VmArray("C", values.length);
        for (int i = 0; i < values.length; i++) {
            array.set(i, (int) values[i]);
        }
        return array;
    }

    private static VmArray objectArray(Object[] values) {
        VmArray array = new VmArray("Ljava/lang/Object;", values.length);
        for (int i = 0; i < values.length; i++) {
            array.set(i, values[i]);
        }
        return array;
    }

    /** Outcome of a name-based default rule (see {@link #runSpecial}). */
    static final class Outcome {
        static final Outcome MISS = new Outcome(false, null, false);
        final boolean handled;
        final Object value;
        final boolean hasValue;

        Outcome(boolean handled, Object value, boolean hasValue) {
            this.handled = handled;
            this.value = value;
            this.hasValue = hasValue;
        }

        static Outcome value(Object result) {
            return new Outcome(true, result, true);
        }

        static Outcome empty() {
            return new Outcome(true, null, false);
        }
    }

    /**
     * Default implementations keyed by method name (not declaring class):
     * constructors on host/placeholder objects and the
     * {@code Object}/{@code Throwable} protocol.
     */
    private Outcome runSpecial(Object receiver, MethodRef ref, Object[] args) {
        boolean noArgs = ref.parameters.isEmpty();
        if (ref.name.equals("<init>")) {
            if (receiver instanceof StringBuilder) {
                StringBuilder builder = (StringBuilder) receiver;
                if (noArgs) {
                    return Outcome.empty();
                }
                if (ref.parameters.size() == 1
                        && ref.parameters.get(0).equals("Ljava/lang/String;")) {
                    builder.append(argString(args, 0));
                    return Outcome.empty();
                }
                if (ref.parameters.size() == 1 && ref.parameters.get(0).equals("I")) {
                    return Outcome.empty();
                }
                return Outcome.MISS;
            }
            if (receiver instanceof String && noArgs) {
                return Outcome.empty();
            }
            // Enum construction records name and ordinal for compareTo,
            // name, valueOf and toString.
            if (ref.definingClass.equals("Ljava/lang/Enum;")
                    && ref.parameters.size() == 2
                    && receiver instanceof VmObject) {
                VmObject object = (VmObject) receiver;
                object.putField("Ljava/lang/Enum;.name", args[0]);
                object.putField("Ljava/lang/Enum;.ordinal", args[1]);
                return Outcome.empty();
            }
            // Framework constructors have no DEX body: argless ones are
            // no-ops (fields default). Argument-carrying ones fall through
            // to reflective construction or a gap, so values are not
            // silently dropped.
            boolean frameworkCtor = dexes.findClass(ref.definingClass) == null
                    && framework != null && framework.find(ref.definingClass) != null;
            if (isPlaceholder(receiver) || frameworkCtor) {
                PendingThrowable pending = throwables.get(receiver);
                if (pending != null && ref.parameters.size() == 1
                        && ref.parameters.get(0).equals("Ljava/lang/String;")) {
                    pending.message = argString(args, 0);
                    return Outcome.empty();
                }
                if (noArgs) {
                    return Outcome.empty();
                }
                return Outcome.MISS;
            }
            return Outcome.MISS;
        }
        // Framework enum values(): materialize every constant in declaration
        // order, mirroring the device array.
        if (ref.name.equals("values") && noArgs && receiver == null
                && ref.returnType.equals("[" + ref.definingClass)) {
            List<VmObject> constants = frameworkEnumValues(ref.definingClass);
            if (constants != null) {
                VmArray out = new VmArray(ref.definingClass, constants.size());
                for (int i = 0; i < constants.size(); i++) {
                    out.set(i, constants.get(i));
                }
                return Outcome.value(out);
            }
            return Outcome.MISS;
        }
        if (ref.name.equals("toString") && noArgs
                && ref.returnType.equals("Ljava/lang/String;")) {
            if (receiver instanceof VmObject) {
                VmObject object = (VmObject) receiver;
                PendingThrowable pending = throwables.get(object);
                String name = dots(object.type.type);
                if (pending != null) {
                    return Outcome.value(pending.message == null ? name
                            : name + ": " + pending.message);
                }
                Object enumName = object.getField("Ljava/lang/Enum;.name");
                if (enumName != null) {
                    return Outcome.value(enumName);
                }
                return Outcome.value(defaultToString(object));
            }
            return Outcome.value(String.valueOf(receiver));
        }
        if (ref.name.equals("getMessage") && noArgs
                && ref.returnType.equals("Ljava/lang/String;")) {
            if (receiver instanceof VmObject) {
                PendingThrowable pending = throwables.get(receiver);
                return Outcome.value(pending == null ? null : pending.message);
            }
            if (receiver instanceof java.lang.Throwable) {
                return Outcome.value(((java.lang.Throwable) receiver).getMessage());
            }
            return Outcome.MISS;
        }
        if (ref.name.equals("equals") && ref.parameters.size() == 1
                && ref.returnType.equals("Z")) {
            if (receiver instanceof VmObject) {
                return Outcome.value(bool(receiver == args[0]));
            }
            return Outcome.value(bool(receiver.equals(args[0])));
        }
        if (ref.name.equals("hashCode") && noArgs && ref.returnType.equals("I")) {
            if (receiver instanceof VmObject) {
                return Outcome.value(System.identityHashCode(receiver));
            }
            return Outcome.value(receiver.hashCode());
        }
        if (ref.name.equals("clone") && noArgs
                && ref.returnType.equals("Ljava/lang/Object;")) {
            if (receiver instanceof VmArray) {
                VmArray source = (VmArray) receiver;
                VmArray copy = new VmArray(source.elementType, source.length());
                for (int i = 0; i < copy.length(); i++) {
                    copy.set(i, source.get(i));
                }
                return Outcome.value(copy);
            }
            return Outcome.MISS;
        }
        if (ref.name.equals("printStackTrace") && noArgs
                && ref.returnType.equals("V")) {
            if (receiver instanceof java.lang.Throwable) {
                ((java.lang.Throwable) receiver).printStackTrace();
                return Outcome.empty();
            }
            return Outcome.MISS;
        }
        if (ref.name.equals("fillInStackTrace") && noArgs
                && ref.returnType.equals("Ljava/lang/Object;")) {
            if (receiver instanceof java.lang.Throwable) {
                return Outcome.value(receiver);
            }
            return Outcome.MISS;
        }
        return Outcome.MISS;
    }

    private boolean isPlaceholder(Object receiver) {
        return receiver instanceof VmObject
                && dexes.findClass(((VmObject) receiver).type.type) == null;
    }

    private static String dots(String descriptor) {
        return descriptor.substring(1, descriptor.length() - 1).replace('/', '.');
    }

    private static String defaultToString(VmObject object) {
        return dots(object.type.type) + "@"
                + Integer.toHexString(System.identityHashCode(object));
    }

    private void registerBuiltins() {
        builtin("Ljava/lang/String;->length()I",
                (receiver, args) -> ((String) receiver).length());
        builtin("Ljava/lang/String;->isEmpty()Z",
                (receiver, args) -> bool(((String) receiver).isEmpty()));
        builtin("Ljava/lang/String;->charAt(I)C",
                (receiver, args) -> (int) ((String) receiver).charAt(argInt(args, 0)));
        builtin("Ljava/lang/String;->equals(Ljava/lang/Object;)Z",
                (receiver, args) -> bool(receiver.equals(args[0])));
        builtin("Ljava/lang/String;->equalsIgnoreCase(Ljava/lang/String;)Z",
                (receiver, args) -> bool(((String) receiver).equalsIgnoreCase(argString(args, 0))));
        builtin("Ljava/lang/String;->compareTo(Ljava/lang/String;)I",
                (receiver, args) -> ((String) receiver).compareTo(argString(args, 0)));
        builtin("Ljava/lang/String;->compareToIgnoreCase(Ljava/lang/String;)I",
                (receiver, args) -> ((String) receiver).compareToIgnoreCase(argString(args, 0)));
        builtin("Ljava/lang/String;->indexOf(I)I",
                (receiver, args) -> ((String) receiver).indexOf(argInt(args, 0)));
        builtin("Ljava/lang/String;->indexOf(Ljava/lang/String;)I",
                (receiver, args) -> ((String) receiver).indexOf(argString(args, 0)));
        builtin("Ljava/lang/String;->indexOf(Ljava/lang/String;I)I",
                (receiver, args) -> ((String) receiver).indexOf(argString(args, 0),
                        argInt(args, 1)));
        builtin("Ljava/lang/String;->indexOf(II)I",
                (receiver, args) -> ((String) receiver).indexOf(argInt(args, 0),
                        argInt(args, 1)));
        builtin("Ljava/lang/String;->substring(I)Ljava/lang/String;",
                (receiver, args) -> ((String) receiver).substring(argInt(args, 0)));
        builtin("Ljava/lang/String;->substring(II)Ljava/lang/String;",
                (receiver, args) -> ((String) receiver).substring(argInt(args, 0),
                        argInt(args, 1)));
        builtin("Ljava/lang/String;->startsWith(Ljava/lang/String;)Z",
                (receiver, args) -> bool(((String) receiver).startsWith(argString(args, 0))));
        builtin("Ljava/lang/String;->startsWith(Ljava/lang/String;I)Z",
                (receiver, args) -> bool(((String) receiver).startsWith(argString(args, 0),
                        argInt(args, 1))));
        builtin("Ljava/lang/String;->endsWith(Ljava/lang/String;)Z",
                (receiver, args) -> bool(((String) receiver).endsWith(argString(args, 0))));
        builtin("Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z",
                (receiver, args) -> bool(((String) receiver).contains((CharSequence) args[0])));
        builtin("Ljava/lang/String;->toLowerCase()Ljava/lang/String;",
                (receiver, args) -> ((String) receiver).toLowerCase());
        builtin("Ljava/lang/String;->toUpperCase()Ljava/lang/String;",
                (receiver, args) -> ((String) receiver).toUpperCase());
        builtin("Ljava/lang/String;->trim()Ljava/lang/String;",
                (receiver, args) -> ((String) receiver).trim());
        builtin("Ljava/lang/String;->concat(Ljava/lang/String;)Ljava/lang/String;",
                (receiver, args) -> ((String) receiver).concat(argString(args, 0)));
        builtin("Ljava/lang/String;->replace(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)"
                + "Ljava/lang/String;",
                (receiver, args) -> ((String) receiver).replace((CharSequence) args[0],
                        (CharSequence) args[1]));
        builtin("Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;",
                (receiver, args) -> stringArray(((String) receiver).split(argString(args, 0))));
        builtin("Ljava/lang/String;->getBytes()[B",
                (receiver, args) -> byteArray(((String) receiver).getBytes()));
        builtin("Ljava/lang/String;->toCharArray()[C",
                (receiver, args) -> charArray(((String) receiver).toCharArray()));
        builtin("Ljava/lang/String;->matches(Ljava/lang/String;)Z",
                (receiver, args) -> bool(((String) receiver).matches(argString(args, 0))));
        builtin("Ljava/lang/String;->replaceAll(Ljava/lang/String;Ljava/lang/String;)"
                + "Ljava/lang/String;",
                (receiver, args) -> ((String) receiver).replaceAll(argString(args, 0),
                        argString(args, 1)));
        builtin("Ljava/lang/String;->replaceFirst(Ljava/lang/String;Ljava/lang/String;)"
                + "Ljava/lang/String;",
                (receiver, args) -> ((String) receiver).replaceFirst(argString(args, 0),
                        argString(args, 1)));
        builtin("Ljava/lang/String;->valueOf(Z)Ljava/lang/String;",
                (receiver, args) -> String.valueOf(argInt(args, 0) != 0));
        builtin("Ljava/lang/String;->valueOf(C)Ljava/lang/String;",
                (receiver, args) -> String.valueOf((char) argInt(args, 0)));
        builtin("Ljava/lang/String;->valueOf(I)Ljava/lang/String;",
                (receiver, args) -> String.valueOf(argInt(args, 0)));
        builtin("Ljava/lang/String;->valueOf(J)Ljava/lang/String;",
                (receiver, args) -> String.valueOf(argLong(args, 0)));
        builtin("Ljava/lang/String;->valueOf(F)Ljava/lang/String;",
                (receiver, args) -> String.valueOf(argFloat(args, 0)));
        builtin("Ljava/lang/String;->valueOf(D)Ljava/lang/String;",
                (receiver, args) -> String.valueOf(argDouble(args, 0)));
        builtin("Ljava/lang/String;->valueOf(Ljava/lang/Object;)Ljava/lang/String;",
                (receiver, args) -> String.valueOf(args[0]));
        builtin("Ljava/lang/String;->toString()Ljava/lang/String;",
                (receiver, args) -> receiver);

        builtin("Ljava/lang/StringBuilder;->append(Ljava/lang/String;)"
                + "Ljava/lang/StringBuilder;",
                (receiver, args) -> ((StringBuilder) receiver).append(argString(args, 0)));
        builtin("Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;",
                (receiver, args) -> ((StringBuilder) receiver).append(argInt(args, 0) != 0));
        builtin("Ljava/lang/StringBuilder;->append(C)Ljava/lang/StringBuilder;",
                (receiver, args) -> ((StringBuilder) receiver).append((char) argInt(args, 0)));
        builtin("Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;",
                (receiver, args) -> ((StringBuilder) receiver).append(argInt(args, 0)));
        builtin("Ljava/lang/StringBuilder;->append(J)Ljava/lang/StringBuilder;",
                (receiver, args) -> ((StringBuilder) receiver).append(argLong(args, 0)));
        builtin("Ljava/lang/StringBuilder;->append(F)Ljava/lang/StringBuilder;",
                (receiver, args) -> ((StringBuilder) receiver).append(argFloat(args, 0)));
        builtin("Ljava/lang/StringBuilder;->append(D)Ljava/lang/StringBuilder;",
                (receiver, args) -> ((StringBuilder) receiver).append(argDouble(args, 0)));
        builtin("Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)"
                + "Ljava/lang/StringBuilder;",
                (receiver, args) -> ((StringBuilder) receiver).append(args[0]));
        builtin("Ljava/lang/StringBuilder;->toString()Ljava/lang/String;",
                (receiver, args) -> receiver.toString());
        builtin("Ljava/lang/StringBuilder;->length()I",
                (receiver, args) -> ((StringBuilder) receiver).length());
        builtin("Ljava/lang/StringBuilder;->setLength(I)V",
                (receiver, args) -> {
                    ((StringBuilder) receiver).setLength(argInt(args, 0));
                    return null;
                });

        builtin("Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;",
                (receiver, args) -> boxInt(argInt(args, 0)));
        builtin("Ljava/lang/Integer;->intValue()I",
                (receiver, args) -> asInt(receiver));
        builtin("Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I",
                (receiver, args) -> Integer.parseInt(argString(args, 0)));
        builtin("Ljava/lang/Integer;->parseInt(Ljava/lang/String;I)I",
                (receiver, args) -> Integer.parseInt(argString(args, 0), argInt(args, 1)));
        builtin("Ljava/lang/Integer;->toString(I)Ljava/lang/String;",
                (receiver, args) -> Integer.toString(argInt(args, 0)));
        builtin("Ljava/lang/Integer;->toString(II)Ljava/lang/String;",
                (receiver, args) -> Integer.toString(argInt(args, 0), argInt(args, 1)));
        builtin("Ljava/lang/Integer;->toHexString(I)Ljava/lang/String;",
                (receiver, args) -> Integer.toHexString(argInt(args, 0)));
        builtin("Ljava/lang/Integer;->toOctalString(I)Ljava/lang/String;",
                (receiver, args) -> Integer.toOctalString(argInt(args, 0)));
        builtin("Ljava/lang/Integer;->toBinaryString(I)Ljava/lang/String;",
                (receiver, args) -> Integer.toBinaryString(argInt(args, 0)));
        builtin("Ljava/lang/Integer;->compare(II)I",
                (receiver, args) -> Integer.compare(argInt(args, 0), argInt(args, 1)));

        builtin("Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;",
                (receiver, args) -> Long.valueOf(argLong(args, 0)));
        builtin("Ljava/lang/Long;->longValue()J",
                (receiver, args) -> ((Long) receiver).longValue());
        builtin("Ljava/lang/Long;->parseLong(Ljava/lang/String;)J",
                (receiver, args) -> Long.parseLong(argString(args, 0)));
        builtin("Ljava/lang/Long;->parseLong(Ljava/lang/String;I)J",
                (receiver, args) -> Long.parseLong(argString(args, 0), argInt(args, 1)));
        builtin("Ljava/lang/Long;->toString(J)Ljava/lang/String;",
                (receiver, args) -> Long.toString(argLong(args, 0)));
        builtin("Ljava/lang/Long;->toHexString(J)Ljava/lang/String;",
                (receiver, args) -> Long.toHexString(argLong(args, 0)));
        builtin("Ljava/lang/Long;->compare(JJ)I",
                (receiver, args) -> Long.compare(argLong(args, 0), argLong(args, 1)));

        builtin("Ljava/lang/Float;->valueOf(F)Ljava/lang/Float;",
                (receiver, args) -> Float.valueOf(argFloat(args, 0)));
        builtin("Ljava/lang/Float;->floatValue()F",
                (receiver, args) -> ((Float) receiver).floatValue());
        builtin("Ljava/lang/Float;->parseFloat(Ljava/lang/String;)F",
                (receiver, args) -> Float.parseFloat(argString(args, 0)));
        builtin("Ljava/lang/Float;->toString(F)Ljava/lang/String;",
                (receiver, args) -> Float.toString(argFloat(args, 0)));
        builtin("Ljava/lang/Float;->isNaN()Z",
                (receiver, args) -> bool(Float.isNaN(argFloat(args, 0))));
        builtin("Ljava/lang/Float;->isNaN(F)Z",
                (receiver, args) -> bool(Float.isNaN(argFloat(args, 0))));
        builtin("Ljava/lang/Float;->isInfinite()Z",
                (receiver, args) -> bool(Float.isInfinite(argFloat(args, 0))));
        builtin("Ljava/lang/Float;->isInfinite(F)Z",
                (receiver, args) -> bool(Float.isInfinite(argFloat(args, 0))));
        builtin("Ljava/lang/Float;->compare(FF)I",
                (receiver, args) -> Float.compare(argFloat(args, 0), argFloat(args, 1)));

        builtin("Ljava/lang/Double;->valueOf(D)Ljava/lang/Double;",
                (receiver, args) -> Double.valueOf(argDouble(args, 0)));
        builtin("Ljava/lang/Double;->doubleValue()D",
                (receiver, args) -> ((Double) receiver).doubleValue());
        builtin("Ljava/lang/Double;->parseDouble(Ljava/lang/String;)D",
                (receiver, args) -> Double.parseDouble(argString(args, 0)));
        builtin("Ljava/lang/Double;->toString(D)Ljava/lang/String;",
                (receiver, args) -> Double.toString(argDouble(args, 0)));
        builtin("Ljava/lang/Double;->isNaN()Z",
                (receiver, args) -> bool(Double.isNaN(argDouble(args, 0))));
        builtin("Ljava/lang/Double;->isNaN(D)Z",
                (receiver, args) -> bool(Double.isNaN(argDouble(args, 0))));
        builtin("Ljava/lang/Double;->isInfinite()Z",
                (receiver, args) -> bool(Double.isInfinite(argDouble(args, 0))));
        builtin("Ljava/lang/Double;->isInfinite(D)Z",
                (receiver, args) -> bool(Double.isInfinite(argDouble(args, 0))));
        builtin("Ljava/lang/Double;->compare(DD)I",
                (receiver, args) -> Double.compare(argDouble(args, 0), argDouble(args, 1)));

        builtin("Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;",
                (receiver, args) -> Boolean.valueOf(argInt(args, 0) != 0));
        builtin("Ljava/lang/Boolean;->booleanValue()Z",
                (receiver, args) -> receiver instanceof Boolean
                        ? (((Boolean) receiver) ? 1 : 0) : argInt(new Object[]{receiver}, 0));
        builtin("Ljava/lang/Boolean;->parseBoolean(Ljava/lang/String;)Z",
                (receiver, args) -> bool(Boolean.parseBoolean(argString(args, 0))));
        builtin("Ljava/lang/Boolean;->toString(Z)Ljava/lang/String;",
                (receiver, args) -> Boolean.toString(argInt(args, 0) != 0));

        builtin("Ljava/lang/Character;->valueOf(C)Ljava/lang/Character;",
                (receiver, args) -> Character.valueOf((char) argInt(args, 0)));
        builtin("Ljava/lang/Character;->charValue()C",
                (receiver, args) -> receiver instanceof Character
                        ? (int) ((Character) receiver).charValue()
                        : argInt(new Object[]{receiver}, 0));
        builtin("Ljava/lang/Character;->toString()Ljava/lang/String;",
                (receiver, args) -> receiver.toString());
        builtin("Ljava/lang/Character;->isDigit(C)Z",
                (receiver, args) -> bool(Character.isDigit((char) argInt(args, 0))));
        builtin("Ljava/lang/Character;->isLetter(C)Z",
                (receiver, args) -> bool(Character.isLetter((char) argInt(args, 0))));
        builtin("Ljava/lang/Character;->isWhitespace(C)Z",
                (receiver, args) -> bool(Character.isWhitespace((char) argInt(args, 0))));
        builtin("Ljava/lang/Character;->isLowerCase(C)Z",
                (receiver, args) -> bool(Character.isLowerCase((char) argInt(args, 0))));
        builtin("Ljava/lang/Character;->isUpperCase(C)Z",
                (receiver, args) -> bool(Character.isUpperCase((char) argInt(args, 0))));
        builtin("Ljava/lang/Character;->toLowerCase(C)C",
                (receiver, args) -> (int) Character.toLowerCase((char) argInt(args, 0)));
        builtin("Ljava/lang/Character;->toUpperCase(C)C",
                (receiver, args) -> (int) Character.toUpperCase((char) argInt(args, 0)));
        builtin("Ljava/lang/Character;->digit(CI)I",
                (receiver, args) -> Character.digit((char) argInt(args, 0), argInt(args, 1)));
        builtin("Ljava/lang/Character;->getNumericValue(C)I",
                (receiver, args) -> Character.getNumericValue((char) argInt(args, 0)));

        builtin("Ljava/lang/Byte;->valueOf(B)Ljava/lang/Byte;",
                (receiver, args) -> Byte.valueOf((byte) argInt(args, 0)));
        builtin("Ljava/lang/Byte;->byteValue()B",
                (receiver, args) -> receiver instanceof Byte
                        ? (int) ((Byte) receiver).byteValue()
                        : argInt(new Object[]{receiver}, 0));
        builtin("Ljava/lang/Byte;->parseByte(Ljava/lang/String;)B",
                (receiver, args) -> (int) Byte.parseByte(argString(args, 0)));
        builtin("Ljava/lang/Byte;->parseByte(Ljava/lang/String;I)B",
                (receiver, args) -> (int) Byte.parseByte(argString(args, 0), argInt(args, 1)));
        builtin("Ljava/lang/Short;->valueOf(S)Ljava/lang/Short;",
                (receiver, args) -> Short.valueOf((short) argInt(args, 0)));
        builtin("Ljava/lang/Short;->shortValue()S",
                (receiver, args) -> receiver instanceof Short
                        ? (int) ((Short) receiver).shortValue()
                        : argInt(new Object[]{receiver}, 0));
        builtin("Ljava/lang/Short;->parseShort(Ljava/lang/String;)S",
                (receiver, args) -> (int) Short.parseShort(argString(args, 0)));
        builtin("Ljava/lang/Short;->parseShort(Ljava/lang/String;I)S",
                (receiver, args) -> (int) Short.parseShort(argString(args, 0), argInt(args, 1)));

        builtin("Ljava/lang/Math;->abs(I)I",
                (receiver, args) -> Math.abs(argInt(args, 0)));
        builtin("Ljava/lang/Math;->abs(J)J",
                (receiver, args) -> Math.abs(argLong(args, 0)));
        builtin("Ljava/lang/Math;->abs(F)F",
                (receiver, args) -> Math.abs(argFloat(args, 0)));
        builtin("Ljava/lang/Math;->abs(D)D",
                (receiver, args) -> Math.abs(argDouble(args, 0)));
        builtin("Ljava/lang/Math;->max(II)I",
                (receiver, args) -> Math.max(argInt(args, 0), argInt(args, 1)));
        builtin("Ljava/lang/Math;->max(JJ)J",
                (receiver, args) -> Math.max(argLong(args, 0), argLong(args, 1)));
        builtin("Ljava/lang/Math;->max(FF)F",
                (receiver, args) -> Math.max(argFloat(args, 0), argFloat(args, 1)));
        builtin("Ljava/lang/Math;->max(DD)D",
                (receiver, args) -> Math.max(argDouble(args, 0), argDouble(args, 1)));
        builtin("Ljava/lang/Math;->min(II)I",
                (receiver, args) -> Math.min(argInt(args, 0), argInt(args, 1)));
        builtin("Ljava/lang/Math;->min(JJ)J",
                (receiver, args) -> Math.min(argLong(args, 0), argLong(args, 1)));
        builtin("Ljava/lang/Math;->min(FF)F",
                (receiver, args) -> Math.min(argFloat(args, 0), argFloat(args, 1)));
        builtin("Ljava/lang/Math;->min(DD)D",
                (receiver, args) -> Math.min(argDouble(args, 0), argDouble(args, 1)));
        builtin("Ljava/lang/Math;->sqrt(D)D",
                (receiver, args) -> Math.sqrt(argDouble(args, 0)));
        builtin("Ljava/lang/Math;->pow(DD)D",
                (receiver, args) -> Math.pow(argDouble(args, 0), argDouble(args, 1)));
        builtin("Ljava/lang/Math;->ceil(D)D",
                (receiver, args) -> Math.ceil(argDouble(args, 0)));
        builtin("Ljava/lang/Math;->floor(D)D",
                (receiver, args) -> Math.floor(argDouble(args, 0)));
        builtin("Ljava/lang/Math;->rint(D)D",
                (receiver, args) -> Math.rint(argDouble(args, 0)));
        builtin("Ljava/lang/Math;->round(F)I",
                (receiver, args) -> Math.round(argFloat(args, 0)));
        builtin("Ljava/lang/Math;->round(D)J",
                (receiver, args) -> Math.round(argDouble(args, 0)));
        builtin("Ljava/lang/Math;->sin(D)D",
                (receiver, args) -> Math.sin(argDouble(args, 0)));
        builtin("Ljava/lang/Math;->cos(D)D",
                (receiver, args) -> Math.cos(argDouble(args, 0)));
        builtin("Ljava/lang/Math;->tan(D)D",
                (receiver, args) -> Math.tan(argDouble(args, 0)));
        builtin("Ljava/lang/Math;->asin(D)D",
                (receiver, args) -> Math.asin(argDouble(args, 0)));
        builtin("Ljava/lang/Math;->acos(D)D",
                (receiver, args) -> Math.acos(argDouble(args, 0)));
        builtin("Ljava/lang/Math;->atan(D)D",
                (receiver, args) -> Math.atan(argDouble(args, 0)));
        builtin("Ljava/lang/Math;->atan2(DD)D",
                (receiver, args) -> Math.atan2(argDouble(args, 0), argDouble(args, 1)));
        builtin("Ljava/lang/Math;->log(D)D",
                (receiver, args) -> Math.log(argDouble(args, 0)));
        builtin("Ljava/lang/Math;->log10(D)D",
                (receiver, args) -> Math.log10(argDouble(args, 0)));
        builtin("Ljava/lang/Math;->exp(D)D",
                (receiver, args) -> Math.exp(argDouble(args, 0)));
        builtin("Ljava/lang/Math;->cbrt(D)D",
                (receiver, args) -> Math.cbrt(argDouble(args, 0)));
        builtin("Ljava/lang/Math;->hypot(DD)D",
                (receiver, args) -> Math.hypot(argDouble(args, 0), argDouble(args, 1)));
        builtin("Ljava/lang/Math;->toDegrees(D)D",
                (receiver, args) -> Math.toDegrees(argDouble(args, 0)));
        builtin("Ljava/lang/Math;->toRadians(D)D",
                (receiver, args) -> Math.toRadians(argDouble(args, 0)));

        builtin("Ljava/lang/System;->currentTimeMillis()J",
                (receiver, args) -> System.currentTimeMillis());
        builtin("Ljava/lang/System;->nanoTime()J",
                (receiver, args) -> System.nanoTime());
        builtin("Ljava/lang/System;->identityHashCode(Ljava/lang/Object;)I",
                (receiver, args) -> System.identityHashCode(args[0]));
        builtin("Ljava/lang/System;->arraycopy(Ljava/lang/Object;ILjava/lang/Object;II)V",
                (receiver, args) -> {
                    arrayCopy(args);
                    return null;
                });
        // Runtime stays denied to reflection (exec/exit must not reach the
        // host), but pure resource queries are safe numbers. getRuntime
        // yields a placeholder; the queries below read the host JVM.
        builtin("Ljava/lang/Runtime;->getRuntime()Ljava/lang/Runtime;",
                (receiver, args) -> newPlaceholder("Ljava/lang/Runtime;"));
        builtin("Ljava/lang/Runtime;->maxMemory()J",
                (receiver, args) -> Runtime.getRuntime().maxMemory());
        builtin("Ljava/lang/Runtime;->totalMemory()J",
                (receiver, args) -> Runtime.getRuntime().totalMemory());
        builtin("Ljava/lang/Runtime;->freeMemory()J",
                (receiver, args) -> Runtime.getRuntime().freeMemory());
        builtin("Ljava/lang/Runtime;->availableProcessors()I",
                (receiver, args) -> Runtime.getRuntime().availableProcessors());
        // System properties are unset headless (same as an Android runtime
        // without them); callers null-check, and host values must not leak.
        builtin("Ljava/lang/System;->getProperty(Ljava/lang/String;)Ljava/lang/String;",
                (receiver, args) -> null);

        // java.io.File bridge (audit 2026-10 security batch): the ONLY
        // guest filesystem path. Constructors remap the guest path through
        // the Vfs seam; unmapped paths bind a fail-closed sentinel that
        // ops answer as nonexistent, and read-only mounts deny writes.
        // Getters report the guest-form path, never the mapped host
        // location. java.nio.file/java.io raw access stays off the reflect
        // allowlist, so this bridge has no second route.
        builtin("Ljava/io/File;-><init>(Ljava/lang/String;)V",
                (receiver, args) -> {
                    if (args[0] == null) {
                        throw new NullPointerException("pathname");
                    }
                    installFileFields((VmObject) receiver, argString(args, 0));
                    return null;
                });
        builtin("Ljava/io/File;-><init>(Ljava/lang/String;Ljava/lang/String;)V",
                (receiver, args) -> {
                    if (args[1] == null) {
                        throw new NullPointerException("child");
                    }
                    installFileFields((VmObject) receiver,
                            joinGuestPath(args[0] == null ? null : argString(args, 0),
                                    argString(args, 1)));
                    return null;
                });
        builtin("Ljava/io/File;-><init>(Ljava/io/File;Ljava/lang/String;)V",
                (receiver, args) -> {
                    if (args[1] == null) {
                        throw new NullPointerException("child");
                    }
                    String parentPath = args[0] == null ? null
                            : fileGuestPath((VmObject) args[0]);
                    installFileFields((VmObject) receiver,
                            joinGuestPath(parentPath, argString(args, 1)));
                    return null;
                });
        // Path getters answer the guest form; the mapped host path stays
        // internal (no host layout leak to guest code).
        builtin("Ljava/io/File;->getPath()Ljava/lang/String;",
                (receiver, args) -> fileGuestPath((VmObject) receiver));
        builtin("Ljava/io/File;->getAbsolutePath()Ljava/lang/String;",
                (receiver, args) -> fileGuestPath((VmObject) receiver));
        builtin("Ljava/io/File;->getCanonicalPath()Ljava/lang/String;",
                (receiver, args) -> fileGuestPath((VmObject) receiver));
        builtin("Ljava/io/File;->toString()Ljava/lang/String;",
                (receiver, args) -> fileGuestPath((VmObject) receiver));
        builtin("Ljava/io/File;->getAbsoluteFile()Ljava/io/File;",
                (receiver, args) -> cloneFileFields((VmObject) receiver));
        builtin("Ljava/io/File;->getCanonicalFile()Ljava/io/File;",
                (receiver, args) -> cloneFileFields((VmObject) receiver));
        builtin("Ljava/io/File;->getParent()Ljava/lang/String;",
                (receiver, args) -> parentOfPath(fileGuestPath((VmObject) receiver)));
        builtin("Ljava/io/File;->getParentFile()Ljava/io/File;",
                (receiver, args) -> {
                    String parent = parentOfPath(fileGuestPath((VmObject) receiver));
                    if (parent == null) {
                        return null;
                    }
                    VmObject out = newPlaceholder("Ljava/io/File;");
                    installFileFields(out, parent);
                    return out;
                });
        builtin("Ljava/io/File;->getName()Ljava/lang/String;",
                (receiver, args) -> nameOfPath(fileGuestPath((VmObject) receiver)));
        builtin("Ljava/io/File;->exists()Z",
                (receiver, args) -> bool(Files.exists(fileHostPath((VmObject) receiver))));
        builtin("Ljava/io/File;->isDirectory()Z",
                (receiver, args) -> bool(Files.isDirectory(
                        fileHostPath((VmObject) receiver))));
        builtin("Ljava/io/File;->isFile()Z",
                (receiver, args) -> bool(Files.isRegularFile(
                        fileHostPath((VmObject) receiver))));
        builtin("Ljava/io/File;->canRead()Z",
                (receiver, args) -> bool(Files.isReadable(
                        fileHostPath((VmObject) receiver))));
        builtin("Ljava/io/File;->canWrite()Z",
                (receiver, args) -> bool(!fileReadOnly((VmObject) receiver)
                        && Files.isWritable(fileHostPath((VmObject) receiver))));
        builtin("Ljava/io/File;->length()J",
                (receiver, args) -> {
                    try {
                        return Files.size(fileHostPath((VmObject) receiver));
                    } catch (IOException e) {
                        return 0L;
                    }
                });
        builtin("Ljava/io/File;->lastModified()J",
                (receiver, args) -> {
                    try {
                        return Files.getLastModifiedTime(
                                fileHostPath((VmObject) receiver)).toMillis();
                    } catch (IOException e) {
                        return 0L;
                    }
                });
        // Write-shaped ops consult the read-only flag and short-circuit
        // before touching the host (the denied sentinel inherits TRUE).
        builtin("Ljava/io/File;->mkdir()Z",
                (receiver, args) -> {
                    VmObject file = (VmObject) receiver;
                    if (fileReadOnly(file)) {
                        return bool(false);
                    }
                    try {
                        Files.createDirectory(fileHostPath(file));
                        return bool(true);
                    } catch (IOException e) {
                        return bool(false);
                    }
                });
        builtin("Ljava/io/File;->mkdirs()Z",
                (receiver, args) -> {
                    VmObject file = (VmObject) receiver;
                    if (fileReadOnly(file)) {
                        return bool(false);
                    }
                    try {
                        Files.createDirectories(fileHostPath(file));
                        return bool(true);
                    } catch (IOException e) {
                        return bool(false);
                    }
                });
        builtin("Ljava/io/File;->createNewFile()Z",
                (receiver, args) -> {
                    VmObject file = (VmObject) receiver;
                    if (fileReadOnly(file)) {
                        return bool(false);
                    }
                    try {
                        Files.createFile(fileHostPath(file));
                        return bool(true);
                    } catch (IOException e) {
                        return bool(false); // exists, or parent missing
                    }
                });
        builtin("Ljava/io/File;->delete()Z",
                (receiver, args) -> {
                    VmObject file = (VmObject) receiver;
                    if (fileReadOnly(file)) {
                        return bool(false);
                    }
                    try {
                        return bool(Files.deleteIfExists(fileHostPath(file)));
                    } catch (IOException e) {
                        return bool(false);
                    }
                });
        builtin("Ljava/io/File;->list()[Ljava/lang/String;",
                (receiver, args) -> {
                    VmObject file = (VmObject) receiver;
                    try {
                        List<String> names = new ArrayList<>();
                        try (DirectoryStream<Path> stream = Files.newDirectoryStream(
                                fileHostPath(file))) {
                            for (Path entry : stream) {
                                names.add(entry.getFileName().toString());
                            }
                        }
                        VmArray out = new VmArray("Ljava/lang/String;", names.size());
                        for (int i = 0; i < names.size(); i++) {
                            out.set(i, names.get(i));
                        }
                        return out;
                    } catch (IOException e) {
                        return null;
                    }
                });
        builtin("Ljava/io/File;->listFiles()[Ljava/io/File;",
                (receiver, args) -> {
                    VmObject file = (VmObject) receiver;
                    try {
                        List<Path> entries = new ArrayList<>();
                        try (DirectoryStream<Path> stream = Files.newDirectoryStream(
                                fileHostPath(file))) {
                            for (Path entry : stream) {
                                entries.add(entry);
                            }
                        }
                        VmArray out = new VmArray("Ljava/io/File;", entries.size());
                        for (int i = 0; i < entries.size(); i++) {
                            VmObject child = newPlaceholder("Ljava/io/File;");
                            installFileFields(child,
                                    joinGuestPath(fileGuestPath(file),
                                            entries.get(i).getFileName().toString()));
                            out.set(i, child);
                        }
                        return out;
                    } catch (IOException e) {
                        return null;
                    }
                });

        builtin("Ljava/util/Objects;->equals(Ljava/lang/Object;Ljava/lang/Object;)Z",
                (receiver, args) -> bool(java.util.Objects.equals(args[0], args[1])));
        builtin("Ljava/util/Objects;->hashCode(Ljava/lang/Object;)I",
                (receiver, args) -> java.util.Objects.hashCode(args[0]));
        builtin("Ljava/util/Objects;->hash([Ljava/lang/Object;)I",
                (receiver, args) -> {
                    if (!(args[0] instanceof VmArray)) {
                        throw new VmException("not an array");
                    }
                    VmArray array = (VmArray) args[0];
                    Object[] values = new Object[array.length()];
                    for (int i = 0; i < values.length; i++) {
                        values[i] = array.get(i);
                    }
                    return java.util.Objects.hash(values);
                });
        builtin("Ljava/util/Objects;->requireNonNull(Ljava/lang/Object;)Ljava/lang/Object;",
                (receiver, args) -> java.util.Objects.requireNonNull(args[0]));
        // copyOf/copyOfRange on reference arrays preserve the runtime
        // component type (newArray semantics); the host-reflected generic
        // overloads would erase it to Object[] and break check-cast.
        builtin("Ljava/util/Arrays;->copyOf([Ljava/lang/Object;I)[Ljava/lang/Object;",
                (receiver, args) -> {
                    VmArray source = checkedArray(args[0]);
                    int length = asInt(args[1]);
                    VmArray out = new VmArray(source.elementType, length);
                    int copy = Math.min(length, source.length());
                    for (int i = 0; i < copy; i++) {
                        out.set(i, source.get(i));
                    }
                    return out;
                });
        builtin("Ljava/util/Arrays;->copyOfRange([Ljava/lang/Object;II)[Ljava/lang/Object;",
                (receiver, args) -> {
                    VmArray source = checkedArray(args[0]);
                    int from = asInt(args[1]);
                    int to = asInt(args[2]);
                    if (from < 0 || from > source.length()) {
                        throw new ArrayIndexOutOfBoundsException(from);
                    }
                    VmArray out = new VmArray(source.elementType, to - from);
                    int copy = Math.min(to, source.length()) - from;
                    for (int i = 0; i < copy; i++) {
                        out.set(i, source.get(from + i));
                    }
                    return out;
                });
        // Sorting with a model comparator stays in-model: the host call
        // would need the comparator across the bridge. Insertion sort with
        // dex callbacks; bounds mirror the host behavior.
        builtin("Ljava/util/Arrays;->sort([Ljava/lang/Object;Ljava/util/Comparator;)V",
                (receiver, args) -> {
                    sortRange(args[0], 0, checkedArray(args[0]).length(), args[1]);
                    return null;
                });
        builtin("Ljava/util/Arrays;->sort([Ljava/lang/Object;IILjava/util/Comparator;)V",
                (receiver, args) -> {
                    sortRange(args[0], asInt(args[1]), asInt(args[2]), args[3]);
                    return null;
                });
        builtin("Ljava/util/Objects;->requireNonNull(Ljava/lang/Object;Ljava/lang/String;)"
                + "Ljava/lang/Object;",
                (receiver, args) -> java.util.Objects.requireNonNull(args[0],
                        argString(args, 1)));
        builtin("Ljava/util/Objects;->toString(Ljava/lang/Object;)Ljava/lang/String;",
                (receiver, args) -> java.util.Objects.toString(args[0]));
        builtin("Ljava/util/Objects;->toString(Ljava/lang/Object;Ljava/lang/String;)"
                + "Ljava/lang/String;",
                (receiver, args) -> java.util.Objects.toString(args[0], argString(args, 1)));

        // PROBE STUBS: framework behavior deferred; each no-op is repaid later.
        builtin("Landroid/app/Activity;->onCreate(Landroid/os/Bundle;)V",
                (receiver, args) -> null);
        builtin("Landroid/app/Activity;->getWindow()Landroid/view/Window;",
                (receiver, args) -> cachedPlaceholder((VmObject) receiver,
                        "Landroid/app/Activity;.mWindow", "Landroid/view/Window;"));
        builtin("Landroid/view/Window;->getDecorView()Landroid/view/View;",
                (receiver, args) -> cachedPlaceholder((VmObject) receiver,
                        "Landroid/view/Window;.mDecor", "Landroid/view/View;"));
        builtin("Landroid/view/Window;->findViewById(I)Landroid/view/View;",
                (receiver, args) -> findContent((VmObject) receiver, args));
        builtin("Landroid/app/Activity;->findViewById(I)Landroid/view/View;",
                (receiver, args) -> findContent((VmObject) receiver, args));
        // Nothing is retained headless: no config change ever happened.
        builtin("Landroidx/activity/ComponentActivity;->getLastNonConfigurationInstance()"
                + "Ljava/lang/Object;",
                (receiver, args) -> null);
        // One application per activity, like the device singleton.
        builtin("Landroidx/activity/ComponentActivity;->getApplication()"
                + "Landroid/app/Application;",
                (receiver, args) -> cachedPlaceholder((VmObject) receiver,
                        "Landroidx/activity/ComponentActivity;.mApplication",
                        "Landroid/app/Application;"));
        // Headless launches carry no action, data, or extras: the Intent
        // is stable per activity but empty.
        builtin("Landroidx/activity/ComponentActivity;->getIntent()Landroid/content/Intent;",
                (receiver, args) -> cachedPlaceholder((VmObject) receiver,
                        "Landroidx/activity/ComponentActivity;.mIntent",
                        "Landroid/content/Intent;"));
        // Empty Intents carry no extras bundle.
        builtin("Landroid/content/Intent;->getExtras()Landroid/os/Bundle;",
                (receiver, args) -> {
                    Object extras = ((VmObject) receiver)
                            .getField("Landroid/content/Intent;.mExtras");
                    return extras instanceof java.util.Map ? extras : null;
                });
        builtin("Landroid/view/View;->findViewById(I)Landroid/view/View;",
                (receiver, args) -> findContent((VmObject) receiver, args));
        // ViewGroup's child surface (addView/child reads/removeAll) lives in
        // the framework module; framework-dispatch serves it through the
        // mirror and the model keys stay synced (unbridgeValue).
        // View construction bottoms out here: keep the context (getContext
        // is the reader half); attrs/defStyle have no headless consumer yet.
        // All four overloads behave the same (attrs are always null headless,
        // styles unresolved), so one shape covers View and ViewGroup alike.
        for (String params : new String[]{"(Landroid/content/Context;)V",
                "(Landroid/content/Context;Landroid/util/AttributeSet;)V",
                "(Landroid/content/Context;Landroid/util/AttributeSet;I)V",
                "(Landroid/content/Context;Landroid/util/AttributeSet;II)V"}) {
            final String signature = params;
            builtin("Landroid/view/ViewGroup;-><init>" + signature,
                    (receiver, args) -> {
                        ((VmObject) receiver).putField("Landroid/view/View;.mContext", args[0]);
                        return null;
                    });
            builtin("Landroid/view/View;-><init>" + signature,
                    (receiver, args) -> {
                        ((VmObject) receiver).putField("Landroid/view/View;.mContext", args[0]);
                        return null;
                    });
        }
        builtin("Landroid/view/View;->getContext()Landroid/content/Context;",
                (receiver, args) -> ((VmObject) receiver)
                        .getField("Landroid/view/View;.mContext"));
        builtin("Landroid/view/View;->setClipChildren(Z)V",
                (receiver, args) -> null);
        builtin("Landroid/view/View;->setClipToPadding(Z)V",
                (receiver, args) -> null);
        // Autofill mode has no headless effect; the manager path stays live.
        builtin("Landroid/view/View;->setImportantForAutofill(I)V",
                (receiver, args) -> null);
        // Content capture has no headless effect either.
        builtin("Landroid/view/View;->setImportantForContentCapture(I)V",
                (receiver, args) -> null);
        // No capture session exists headless (as when capture is disabled).
        builtin("Landroid/view/View;->getContentCaptureSession()"
                + "Landroid/view/contentcapture/ContentCaptureSession;",
                (receiver, args) -> null);
        // No autofill service runs headless; callbacks never fire.
        builtin("Landroid/view/autofill/AutofillManager;->registerCallback("
                + "Landroid/view/autofill/AutofillManager$AutofillCallback;)V",
                (receiver, args) -> null);
        // View configuration holds device constants (touch slop etc.);
        // per-context singleton like the framework cache.
        builtin("Landroid/view/ViewConfiguration;->get(Landroid/content/Context;)"
                + "Landroid/view/ViewConfiguration;",
                (receiver, args) -> {
                    if (!(args[0] instanceof VmObject)) {
                        throw new NullPointerException("null context");
                    }
                    return cachedPlaceholder((VmObject) args[0],
                            "Landroid/view/ViewConfiguration;.mInstance",
                            "Landroid/view/ViewConfiguration;");
                });
        // Detached until setContentView joins the view to a window (which is
        // also what re-triggers composition on device).
        builtin("Landroid/view/View;->isAttachedToWindow()Z",
                (receiver, args) -> {
                    Object marked = ((VmObject) receiver)
                            .getField("Landroid/view/View;.mAttached");
                    return marked == null ? 0 : asInt(marked);
                });
        // Attach marks the view; groups also mark recorded children.
        // Child bodies do not run here (shallow dispatch): bodies run
        // when the driver or interpreter reaches them.
        builtin("Landroid/view/View;->onAttachedToWindow()V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/view/View;.mAttached", 1);
                    return null;
                });
        builtin("Landroid/view/ViewGroup;->onAttachedToWindow()V",
                (receiver, args) -> {
                    VmObject group = (VmObject) receiver;
                    group.putField("Landroid/view/View;.mAttached", 1);
                    Object children = group.getField("Landroid/view/ViewGroup;.mChildren");
                    if (children instanceof List) {
                        for (Object child : (List<?>) children) {
                            if (child instanceof VmObject) {
                                ((VmObject) child)
                                        .putField("Landroid/view/View;.mAttached", 1);
                            }
                        }
                    }
                    return null;
                });
        // Headless runs foreground: the window holds focus.
        builtin("Landroid/view/View;->hasWindowFocus()Z",
                (receiver, args) -> 1);
        // No GPU runs headless; revisit when the GL backend lands.
        builtin("Landroid/view/View;->isHardwareAccelerated()Z",
                (receiver, args) -> 0);
        // Nothing takes focus headless (no input, focus setters no-op).
        builtin("Landroid/view/View;->isFocused()Z",
                (receiver, args) -> 0);
        // ... so no subtree holds focus either.
        builtin("Landroid/view/View;->hasFocus()Z",
                (receiver, args) -> 0);
        // No input occurs headless, so touch mode is never entered.
        builtin("Landroid/view/View;->isInTouchMode()Z",
                (receiver, args) -> 0);
        // No draw dispatch exists headless; the flag is recorded nowhere.
        builtin("Landroid/view/View;->setWillNotDraw(Z)V",
                (receiver, args) -> null);
        // Focus has no headless effect without input or windows.
        builtin("Landroid/view/View;->setFocusable(Z)V",
                (receiver, args) -> null);
        builtin("Landroid/view/View;->setFocusable(I)V",
                (receiver, args) -> null);
        builtin("Landroid/view/View;->setDefaultFocusHighlightEnabled(Z)V",
                (receiver, args) -> null);
        builtin("Landroid/view/View;->setFocusableInTouchMode(Z)V",
                (receiver, args) -> null);
        // One Choreographer per interpreter (single display, single thread).
        // Frame callbacks never fire headless; posting records interest for
        // the future VSync pump, unposting drops it (both silent otherwise).
        builtin("Landroid/view/Choreographer;->getInstance()Landroid/view/Choreographer;",
                (receiver, args) -> {
                    if (choreographer == null) {
                        choreographer = newPlaceholder("Landroid/view/Choreographer;");
                    }
                    return choreographer;
                });
        builtin("Landroid/view/Choreographer;->postFrameCallback("
                + "Landroid/view/Choreographer$FrameCallback;)V",
                (receiver, args) -> {
                    VmObject choreo = (VmObject) receiver;
                    Object held = choreo
                            .getField("Landroid/view/Choreographer;.frameCallbacks");
                    List<Object> callbacks;
                    if (held instanceof List) {
                        callbacks = (List<Object>) held;
                    } else {
                        callbacks = new ArrayList<>();
                        choreo.putField("Landroid/view/Choreographer;.frameCallbacks",
                                callbacks);
                    }
                    callbacks.add(args[0]);
                    return null;
                });
        builtin("Landroid/view/Choreographer;->removeFrameCallback("
                + "Landroid/view/Choreographer$FrameCallback;)V",
                (receiver, args) -> {
                    Object held = ((VmObject) receiver)
                            .getField("Landroid/view/Choreographer;.frameCallbacks");
                    if (held instanceof List) {
                        ((List<?>) held).remove(args[0]);
                    }
                    return null;
                });
        // Parentless until setContentView links the content frame.
        builtin("Landroid/view/View;->getParent()Landroid/view/ViewParent;",
                (receiver, args) -> ((VmObject) receiver)
                        .getField("Landroid/view/View;.mParent"));
        // ViewParent receivers are always views here; same linkage.
        builtin("Landroid/view/ViewParent;->getParent()Landroid/view/ViewParent;",
                (receiver, args) -> ((VmObject) receiver)
                        .getField("Landroid/view/View;.mParent"));
        // Headless views are never inflated nor setId'd: NO_ID (-1), except
        // the content frame, which carries android.R.id.content like the
        // real PhoneWindow frame (ViewTree walks match on it).
        builtin("Landroid/view/View;->getId()I",
                (receiver, args) -> {
                    Object id = ((VmObject) receiver).getField("Landroid/view/View;.mId");
                    return id == null ? -1 : asInt(id);
                });
        // Attached views post to the main looper (inline, like Handler.post);
        // detached views have no handler, as on device.
        builtin("Landroid/view/View;->getHandler()Landroid/os/Handler;",
                (receiver, args) -> {
                    VmObject view = (VmObject) receiver;
                    Object marked = view.getField("Landroid/view/View;.mAttached");
                    if (marked == null || asInt(marked) == 0) {
                        return null;
                    }
                    Object handler = view.getField("Landroid/view/View;.mHandler");
                    if (!(handler instanceof VmObject)) {
                        VmObject created = newPlaceholder("Landroid/os/Handler;");
                        initHandler(created, mainLooper(), null);
                        view.putField("Landroid/view/View;.mHandler", created);
                        return created;
                    }
                    return handler;
                });
        builtin("Landroid/view/View;->getViewTreeObserver()Landroid/view/ViewTreeObserver;",
                (receiver, args) -> newPlaceholder("Landroid/view/ViewTreeObserver;"));
        // Observer callbacks never fire headless; registration is a no-op.
        builtin("Landroid/view/ViewTreeObserver;->addOnDrawListener("
                + "Landroid/view/ViewTreeObserver$OnDrawListener;)V",
                (receiver, args) -> null);
        builtin("Landroid/view/ViewTreeObserver;->addOnGlobalLayoutListener("
                + "Landroid/view/ViewTreeObserver$OnGlobalLayoutListener;)V",
                (receiver, args) -> null);
        builtin("Landroid/view/ViewTreeObserver;->addOnScrollChangedListener("
                + "Landroid/view/ViewTreeObserver$OnScrollChangedListener;)V",
                (receiver, args) -> null);
        builtin("Landroid/view/ViewTreeObserver;->addOnTouchModeChangeListener("
                + "Landroid/view/ViewTreeObserver$OnTouchModeChangeListener;)V",
                (receiver, args) -> null);
        builtin("Landroid/view/ViewTreeObserver;->addOnPreDrawListener("
                + "Landroid/view/ViewTreeObserver$OnPreDrawListener;)V",
                (receiver, args) -> null);
        builtin("Landroid/view/ViewTreeObserver;->addOnWindowFocusChangeListener("
                + "Landroid/view/ViewTreeObserver$OnWindowFocusChangeListener;)V",
                (receiver, args) -> null);
        builtin("Landroid/view/ViewTreeObserver;->addOnWindowAttachListener("
                + "Landroid/view/ViewTreeObserver$OnWindowAttachListener;)V",
                (receiver, args) -> null);
        builtin("Landroid/view/ViewTreeObserver;->addOnComputeInternalInsetsListener("
                + "Landroid/view/ViewTreeObserver$OnComputeInternalInsetsListener;)V",
                (receiver, args) -> null);
        builtin("Landroid/view/ViewTreeObserver;->removeOnGlobalLayoutListener("
                + "Landroid/view/ViewTreeObserver$OnGlobalLayoutListener;)V",
                (receiver, args) -> null);
        builtin("Landroid/view/ViewTreeObserver;->removeOnScrollChangedListener("
                + "Landroid/view/ViewTreeObserver$OnScrollChangedListener;)V",
                (receiver, args) -> null);
        builtin("Landroid/view/ViewTreeObserver;->removeOnPreDrawListener("
                + "Landroid/view/ViewTreeObserver$OnPreDrawListener;)V",
                (receiver, args) -> null);
        builtin("Landroid/view/ViewTreeObserver;->removeOnTouchModeChangeListener("
                + "Landroid/view/ViewTreeObserver$OnTouchModeChangeListener;)V",
                (receiver, args) -> null);
        // Installing the content view joins the window hierarchy: the view
        // gains the content frame as parent (so ViewTree walks reach the
        // decor tags), becomes the frame's first child, and attaches — the
        // retained handle lets the probe inspect the composed content.
        builtin("Landroid/app/Activity;->setContentView(Landroid/view/View;)V",
                (receiver, args) -> {
                    joinWindow((VmObject) receiver, (VmObject) args[0]);
                    return null;
                });
        builtin("Landroid/app/Activity;->setContentView(Landroid/view/View;"
                + "Landroid/view/ViewGroup$LayoutParams;)V",
                (receiver, args) -> {
                    joinWindow((VmObject) receiver, (VmObject) args[0]);
                    return null;
                });
        // Attach listeners never fire headless; registration is a no-op.
        builtin("Landroid/view/View;->addOnAttachStateChangeListener("
                + "Landroid/view/View$OnAttachStateChangeListener;)V",
                (receiver, args) -> null);
        // ViewTree owners key lifecycle objects off integer tags.
        builtin("Landroid/view/View;->getTag(I)Ljava/lang/Object;",
                (receiver, args) -> {
                    Object tags = ((VmObject) receiver)
                            .getField("Landroid/view/View;.tags");
                    if (!(tags instanceof Map)) {
                        return null;
                    }
                    return ((Map<?, ?>) tags).get(asInt(args[0]));
                });
        builtin("Landroid/view/View;->setTag(ILjava/lang/Object;)V",
                (receiver, args) -> {
                    VmObject view = (VmObject) receiver;
                    Object tags = view.getField("Landroid/view/View;.tags");
                    Map<Integer, Object> map;
                    if (tags instanceof Map) {
                        map = (Map<Integer, Object>) tags;
                    } else {
                        map = new HashMap<>();
                        view.putField("Landroid/view/View;.tags", map);
                    }
                    map.put(asInt(args[0]), args[1]);
                    return null;
                });
        builtin("Landroid/view/View;->getSystemUiVisibility()I",
                (receiver, args) -> {
                    Object value = ((VmObject) receiver)
                            .getField("Landroid/view/View;.mSystemUiVisibility");
                    return value == null ? 0 : asInt(value);
                });
        builtin("Landroid/view/View;->setSystemUiVisibility(I)V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/view/View;.mSystemUiVisibility",
                            asInt(args[0]));
                    return null;
                });
        builtin("Landroid/view/View;->getVisibility()I",
                (receiver, args) -> {
                    Object value = ((VmObject) receiver)
                            .getField("Landroid/view/View;.mVisibility");
                    return value == null ? 0 : asInt(value);
                });
        builtin("Landroid/view/View;->setVisibility(I)V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/view/View;.mVisibility",
                            asInt(args[0]));
                    return null;
                });
        builtin("Landroid/view/Window;->getWindowManager()Landroid/view/WindowManager;",
                (receiver, args) -> newPlaceholder("Landroid/view/WindowManager;"));
        builtin("Landroid/view/View;->getResources()Landroid/content/res/Resources;",
                (receiver, args) -> newPlaceholder("Landroid/content/res/Resources;"));
        builtin("Landroid/content/Context;->getResources()Landroid/content/res/Resources;",
                (receiver, args) -> newPlaceholder("Landroid/content/res/Resources;"));
        // The application outlives activities; one stable placeholder each.
        builtin("Landroid/content/Context;->getApplicationContext()Landroid/content/Context;",
                (receiver, args) -> cachedPlaceholder((VmObject) receiver,
                        "Landroid/content/Context;.mApplication",
                        "Landroid/app/Application;"));
        // No content providers run headless; observers never fire.
        builtin("Landroid/database/ContentObserver;-><init>(Landroid/os/Handler;)V",
                (receiver, args) -> null);
        builtin("Landroid/content/ContentResolver;->registerContentObserver("
                + "Landroid/net/Uri;ZLandroid/database/ContentObserver;)V",
                (receiver, args) -> null);
        builtin("Landroid/content/ContentResolver;->unregisterContentObserver("
                + "Landroid/database/ContentObserver;)V",
                (receiver, args) -> null);
        // No content providers run headless; the resolver is a stable
        // placeholder whose queries stay future gaps.
        builtin("Landroid/content/Context;->getContentResolver()"
                + "Landroid/content/ContentResolver;",
                (receiver, args) -> cachedPlaceholder((VmObject) receiver,
                        "Landroid/content/Context;.mContentResolver",
                        "Landroid/content/ContentResolver;"));
        // One theme per context; styled attributes stay future gaps.
        builtin("Landroid/content/Context;->getTheme()Landroid/content/res/Resources$Theme;",
                (receiver, args) -> cachedPlaceholder((VmObject) receiver,
                        "Landroid/content/Context;.mTheme",
                        "Landroid/content/res/Resources$Theme;"));
        // No configuration changes, memory pressure, or trimming occurs
        // headless; component callbacks never fire.
        builtin("Landroid/content/Context;->registerComponentCallbacks("
                + "Landroid/content/ComponentCallbacks;)V",
                (receiver, args) -> null);
        builtin("Landroid/content/Context;->unregisterComponentCallbacks("
                + "Landroid/content/ComponentCallbacks;)V",
                (receiver, args) -> null);
        // Resources.getColor is real Java in the framework module now (the
        // android-package system palette baseline; app-package ids gap).
        // Settings URIs are pure names; values stay unread headless.
        builtin("Landroid/provider/Settings$Global;->getUriFor(Ljava/lang/String;)"
                + "Landroid/net/Uri;",
                (receiver, args) -> newPlaceholder("Landroid/net/Uri;"));
        // No settings provider runs headless: reads fall back to the
        // caller-supplied default.
        builtin("Landroid/provider/Settings$Global;->getFloat("
                + "Landroid/content/ContentResolver;Ljava/lang/String;F)F",
                (receiver, args) -> asFloatBits(args[2]));
        // System services dispatch by name; unknown names stay gaps so new
        // service needs surface as tasks instead of silent placeholders.
        builtin("Landroid/content/Context;->getSystemService(Ljava/lang/String;)"
                + "Ljava/lang/Object;",
                (receiver, args) -> systemService(argString(args, 0)));
        // Class-typed lookup resolves the service class, same table.
        builtin("Landroid/content/Context;->getSystemService(Ljava/lang/Class;)"
                + "Ljava/lang/Object;",
                (receiver, args) -> {
                    if (!(args[0] instanceof VmClass)) {
                        throw new NullPointerException("null service class");
                    }
                    return systemServiceClass(((VmClass) args[0]).descriptor);
                });
        // No accessibility services run headless: default AUTO (0), stored
        // when set so get observes it.
        builtin("Landroid/view/View;->getImportantForAccessibility()I",
                (receiver, args) -> {
                    Object value = ((VmObject) receiver)
                            .getField("Landroid/view/View;.mImportantForAccessibility");
                    return value == null ? 0 : asInt(value);
                });
        builtin("Landroid/view/View;->setImportantForAccessibility(I)V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField(
                            "Landroid/view/View;.mImportantForAccessibility",
                            asInt(args[0]));
                    return null;
                });
        // No accessibility services run headless; delegates never fire.
        builtin("Landroid/view/View;->setAccessibilityDelegate("
                + "Landroid/view/View$AccessibilityDelegate;)V",
                (receiver, args) -> null);
        // No drag input occurs headless; listeners never fire.
        builtin("Landroid/view/View;->setOnDragListener(Landroid/view/View$OnDragListener;)V",
                (receiver, args) -> null);
        // Layout never runs headless: requestLayout records the request
        // (dispatch is a no-op), isLayoutRequested observes it.
        builtin("Landroid/view/View;->requestLayout()V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/view/View;.mLayoutRequested", 1);
                    return null;
                });
        builtin("Landroid/view/View;->isLayoutRequested()Z",
                (receiver, args) -> {
                    Object marked = ((VmObject) receiver)
                            .getField("Landroid/view/View;.mLayoutRequested");
                    return marked == null ? 0 : asInt(marked);
                });
        // Window insets never change headless; dispatch is a no-op.
        builtin("Landroid/view/View;->requestApplyInsets()V",
                (receiver, args) -> null);
        // Measured dimensions store for later reads (no parent to report).
        builtin("Landroid/view/View;->setMeasuredDimension(II)V",
                (receiver, args) -> {
                    VmObject view = (VmObject) receiver;
                    view.putField("Landroid/view/View;.mMeasuredWidth", asInt(args[0]));
                    view.putField("Landroid/view/View;.mMeasuredHeight", asInt(args[1]));
                    return null;
                });
        builtin("Landroid/view/View;->getMeasuredWidth()I",
                (receiver, args) -> {
                    Object value = ((VmObject) receiver)
                            .getField("Landroid/view/View;.mMeasuredWidth");
                    return value == null ? 0 : asInt(value);
                });
        builtin("Landroid/view/View;->getMeasuredHeight()I",
                (receiver, args) -> {
                    Object value = ((VmObject) receiver)
                            .getField("Landroid/view/View;.mMeasuredHeight");
                    return value == null ? 0 : asInt(value);
                });
        // One default locale list; a single US entry, like a fresh
        // device: an empty list cannot exist on device and would crash
        // blind get(0) callers with a false IndexOutOfBounds.
        builtin("Landroid/os/LocaleList;->getDefault()Landroid/os/LocaleList;",
                (receiver, args) -> {
                    if (defaultLocales == null) {
                        defaultLocales = newPlaceholder("Landroid/os/LocaleList;");
                    }
                    return defaultLocales;
                });
        builtin("Landroid/os/LocaleList;->size()I",
                (receiver, args) -> 1);
        builtin("Landroid/os/LocaleList;->isEmpty()Z",
                (receiver, args) -> 0);
        builtin("Landroid/os/LocaleList;->get(I)Ljava/util/Locale;",
                (receiver, args) -> asInt(args[0]) == 0 ? java.util.Locale.US : null);
        // MeasureSpecs are packed ints; the packing is pure arithmetic.
        builtin("Landroid/view/View$MeasureSpec;->makeMeasureSpec(II)I",
                (receiver, args) -> (asInt(args[0]) & ~(0x3 << 30))
                        | (asInt(args[1]) & (0x3 << 30)));
        builtin("Landroid/view/View$MeasureSpec;->getMode(I)I",
                (receiver, args) -> asInt(args[0]) & (0x3 << 30));
        builtin("Landroid/view/View$MeasureSpec;->getSize(I)I",
                (receiver, args) -> asInt(args[0]) & ~(0x3 << 30));
        // Inset animations never run headless; callbacks never fire.
        builtin("Landroid/view/WindowInsetsAnimation$Callback;-><init>(I)V",
                (receiver, args) -> null);
        builtin("Landroid/view/View;->setWindowInsetsAnimationCallback("
                + "Landroid/view/WindowInsetsAnimation$Callback;)V",
                (receiver, args) -> null);
        // Force-dark is a display feature with no headless effect.
        builtin("Landroid/view/View;->setForceDarkAllowed(Z)V",
                (receiver, args) -> null);
        // Content capture/translation has no headless effect.
        builtin("Landroid/view/View;->setViewTranslationCallback("
                + "Landroid/view/translation/ViewTranslationCallback;)V",
                (receiver, args) -> null);
        // No accessibility services run headless: empty but mutable, like
        // the device list when nothing is enabled.
        builtin("Landroid/view/accessibility/AccessibilityManager;->"
                + "getEnabledAccessibilityServiceList(I)Ljava/util/List;",
                (receiver, args) -> new java.util.ArrayList<>());
        // No accessibility services run headless, so the manager is disabled.
        builtin("Landroid/view/accessibility/AccessibilityManager;->isEnabled()Z",
                (receiver, args) -> 0);
        builtin("Landroid/content/res/Resources;->getConfiguration()"
                + "Landroid/content/res/Configuration;",
                (receiver, args) -> {
                    VmObject configuration =
                            newPlaceholder("Landroid/content/res/Configuration;");
                    configuration.putField(
                            "Landroid/content/res/Configuration;.fontScale", 1.0f);
                    configuration.putField(
                            "Landroid/content/res/Configuration;.densityDpi", 160);
                    return configuration;
                });
        // Baseline mdpi phone metrics, coherent with the desktop window size
        // (480x960 at density 1.0); fields are read directly by density code.
        builtin("Landroid/content/res/Resources;->getDisplayMetrics()"
                + "Landroid/util/DisplayMetrics;",
                (receiver, args) -> {
                    VmObject metrics = newPlaceholder("Landroid/util/DisplayMetrics;");
                    metrics.putField("Landroid/util/DisplayMetrics;.widthPixels", 480);
                    metrics.putField("Landroid/util/DisplayMetrics;.heightPixels", 960);
                    metrics.putField("Landroid/util/DisplayMetrics;.density", 1.0f);
                    metrics.putField("Landroid/util/DisplayMetrics;.densityDpi", 160);
                    metrics.putField("Landroid/util/DisplayMetrics;.scaledDensity", 1.0f);
                    metrics.putField("Landroid/util/DisplayMetrics;.xdpi", 160.0f);
                    metrics.putField("Landroid/util/DisplayMetrics;.ydpi", 160.0f);
                    return metrics;
                });
        // Default locale is left-to-right; no RTL headless.
        builtin("Landroid/content/res/Configuration;->getLayoutDirection()I",
                (receiver, args) -> 0);
        // Copy construction clones the stored fields (density, font scale).
        builtin("Landroid/content/res/Configuration;-><init>("
                + "Landroid/content/res/Configuration;)V",
                (receiver, args) -> {
                    if (args[0] == null) {
                        throw new NullPointerException("config copy of null");
                    }
                    ((VmObject) receiver).copyFieldsFrom((VmObject) args[0]);
                    return null;
                });
        // setTo overwrites with another configuration's stored fields.
        builtin("Landroid/content/res/Configuration;->setTo("
                + "Landroid/content/res/Configuration;)V",
                (receiver, args) -> {
                    if (args[0] == null) {
                        throw new NullPointerException("config setTo null");
                    }
                    ((VmObject) receiver).copyFieldsFrom((VmObject) args[0]);
                    return null;
                });
        builtin("Landroid/view/Window;->setDecorFitsSystemWindows(Z)V",
                (receiver, args) -> null);
        builtin("Landroid/view/Window;->setStatusBarColor(I)V",
                (receiver, args) -> null);
        builtin("Landroid/view/Window;->setNavigationBarColor(I)V",
                (receiver, args) -> null);
        builtin("Landroid/view/Window;->setStatusBarContrastEnforced(Z)V",
                (receiver, args) -> null);
        builtin("Landroid/view/Window;->setNavigationBarContrastEnforced(Z)V",
                (receiver, args) -> null);
        builtin("Landroid/view/Window;->getInsetsController()"
                + "Landroid/view/WindowInsetsController;",
                (receiver, args) -> newPlaceholder("Landroid/view/WindowInsetsController;"));
        builtin("Landroid/view/Window;->getAttributes()"
                + "Landroid/view/WindowManager$LayoutParams;",
                (receiver, args) -> newPlaceholder("Landroid/view/WindowManager$LayoutParams;"));
        builtin("Landroid/view/Window;->setAttributes("
                + "Landroid/view/WindowManager$LayoutParams;)V",
                (receiver, args) -> null);
        builtin("Landroid/view/WindowInsetsController;->setSystemBarsAppearance(II)V",
                (receiver, args) -> null);
        builtin("Landroid/app/Activity;->registerActivityLifecycleCallbacks("
                + "Landroid/app/Application$ActivityLifecycleCallbacks;)V",
                (receiver, args) -> null);
        builtin("Landroid/app/Activity;->getFragmentManager()Landroid/app/FragmentManager;",
                (receiver, args) -> newPlaceholder("Landroid/app/FragmentManager;"));
        builtin("Landroid/app/FragmentManager;->findFragmentByTag(Ljava/lang/String;)"
                + "Landroid/app/Fragment;",
                (receiver, args) -> null);
        builtin("Landroid/app/FragmentManager;->beginTransaction()"
                + "Landroid/app/FragmentTransaction;",
                (receiver, args) -> newPlaceholder("Landroid/app/FragmentTransaction;"));
        builtin("Landroid/app/FragmentTransaction;->add(ILandroid/app/Fragment;)"
                + "Landroid/app/FragmentTransaction;",
                (receiver, args) -> receiver);
        builtin("Landroid/app/FragmentTransaction;->add(Landroid/app/Fragment;"
                + "Ljava/lang/String;)Landroid/app/FragmentTransaction;",
                (receiver, args) -> receiver);
        builtin("Landroid/app/FragmentTransaction;->replace(ILandroid/app/Fragment;)"
                + "Landroid/app/FragmentTransaction;",
                (receiver, args) -> receiver);
        builtin("Landroid/app/FragmentTransaction;->replace(ILandroid/app/Fragment;"
                + "Ljava/lang/String;)Landroid/app/FragmentTransaction;",
                (receiver, args) -> receiver);
        builtin("Landroid/app/FragmentTransaction;->remove(Landroid/app/Fragment;)"
                + "Landroid/app/FragmentTransaction;",
                (receiver, args) -> receiver);
        builtin("Landroid/app/FragmentTransaction;->hide(Landroid/app/Fragment;)"
                + "Landroid/app/FragmentTransaction;",
                (receiver, args) -> receiver);
        builtin("Landroid/app/FragmentTransaction;->show(Landroid/app/Fragment;)"
                + "Landroid/app/FragmentTransaction;",
                (receiver, args) -> receiver);
        builtin("Landroid/app/FragmentTransaction;->detach(Landroid/app/Fragment;)"
                + "Landroid/app/FragmentTransaction;",
                (receiver, args) -> receiver);
        builtin("Landroid/app/FragmentTransaction;->attach(Landroid/app/Fragment;)"
                + "Landroid/app/FragmentTransaction;",
                (receiver, args) -> receiver);
        builtin("Landroid/app/FragmentTransaction;->addToBackStack(Ljava/lang/String;)"
                + "Landroid/app/FragmentTransaction;",
                (receiver, args) -> receiver);
        builtin("Landroid/app/FragmentTransaction;->setCustomAnimations(II)"
                + "Landroid/app/FragmentTransaction;",
                (receiver, args) -> receiver);
        builtin("Landroid/app/FragmentTransaction;->setCustomAnimations(IIII)"
                + "Landroid/app/FragmentTransaction;",
                (receiver, args) -> receiver);
        builtin("Landroid/app/FragmentTransaction;->setTransition(I)"
                + "Landroid/app/FragmentTransaction;",
                (receiver, args) -> receiver);
        builtin("Landroid/app/FragmentTransaction;->setTransitionStyle(I)"
                + "Landroid/app/FragmentTransaction;",
                (receiver, args) -> receiver);
        builtin("Landroid/app/FragmentTransaction;->commit()I",
                (receiver, args) -> 0);
        builtin("Landroid/app/FragmentTransaction;->commitAllowingStateLoss()I",
                (receiver, args) -> 0);
        builtin("Landroid/app/FragmentManager;->executePendingTransactions()Z",
                (receiver, args) -> 0);
        // Color math is plain bit packing (no host counterpart exists).
        builtin("Landroid/graphics/Color;->argb(IIII)I",
                (receiver, args) -> (argInt(args, 0) << 24) | (argInt(args, 1) << 16)
                        | (argInt(args, 2) << 8) | argInt(args, 3));
        builtin("Landroid/graphics/Color;->rgb(III)I",
                (receiver, args) -> 0xFF000000 | (argInt(args, 0) << 16)
                        | (argInt(args, 1) << 8) | argInt(args, 2));
        builtin("Landroid/graphics/Color;->alpha(I)I",
                (receiver, args) -> argInt(args, 0) >>> 24);
        builtin("Landroid/graphics/Color;->red(I)I",
                (receiver, args) -> (argInt(args, 0) >> 16) & 0xFF);
        builtin("Landroid/graphics/Color;->green(I)I",
                (receiver, args) -> (argInt(args, 0) >> 8) & 0xFF);
        builtin("Landroid/graphics/Color;->blue(I)I",
                (receiver, args) -> argInt(args, 0) & 0xFF);
        builtin("Landroid/graphics/Color;->parseColor(Ljava/lang/String;)I",
                (receiver, args) -> parseColor(argString(args, 0)));
        // Paint carries creation flags (anti-alias etc.) read back by text
        // measurement; values round-trip, rendering stays deferred.
        builtin("Landroid/graphics/Paint;-><init>(I)V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/graphics/Paint;.mFlags",
                            asInt(args[0]));
                    return null;
                });
        // TextPaint shares Paint's flag store (inherited getters observe it).
        builtin("Landroid/text/TextPaint;-><init>(I)V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/graphics/Paint;.mFlags",
                            asInt(args[0]));
                    return null;
                });
        // SpannableStrings wrap source text with an overridable span list;
        // reads delegate to the source, spans filter by class and overlap.
        builtin("Landroid/text/SpannableString;-><init>(Ljava/lang/CharSequence;)V",
                (receiver, args) -> {
                    VmObject spannable = (VmObject) receiver;
                    spannable.putField("Landroid/text/SpannableString;.mText",
                            charSequenceText(args[0]));
                    spannable.putField("Landroid/text/SpannableString;.mSpans",
                            new ArrayList<Object>());
                    return null;
                });
        builtin("Landroid/text/SpannableString;->length()I",
                (receiver, args) -> {
                    Object text = ((VmObject) receiver)
                            .getField("Landroid/text/SpannableString;.mText");
                    return text == null ? 0 : ((String) text).length();
                });
        builtin("Landroid/text/SpannableString;->charAt(I)C",
                (receiver, args) -> {
                    Object text = ((VmObject) receiver)
                            .getField("Landroid/text/SpannableString;.mText");
                    return text == null ? 0 : (int) ((String) text).charAt(asInt(args[0]));
                });
        builtin("Landroid/text/SpannableString;->setSpan(Ljava/lang/Object;III)V",
                (receiver, args) -> {
                    Object spans = ((VmObject) receiver)
                            .getField("Landroid/text/SpannableString;.mSpans");
                    if (spans instanceof List) {
                        ((List<Object>) spans).add(new Object[]{args[0], args[1], args[2],
                                args[3]});
                    }
                    return null;
                });
        builtin("Landroid/text/SpannableString;->getSpans(IILjava/lang/Class;)"
                + "[Ljava/lang/Object;",
                (receiver, args) -> {
                    int queryStart = asInt(args[0]);
                    int queryEnd = asInt(args[1]);
                    String query = ((VmClass) args[2]).descriptor;
                    Object spans = ((VmObject) receiver)
                            .getField("Landroid/text/SpannableString;.mSpans");
                    List<Object> matches = new ArrayList<>();
                    if (spans instanceof List) {
                        for (Object record : (List<?>) spans) {
                            Object[] span = (Object[]) record;
                            int spanStart = asInt(span[1]);
                            int spanEnd = asInt(span[2]);
                            if (spanEnd <= queryStart || spanStart >= queryEnd) {
                                continue;
                            }
                            if (isInstance(span[0], query)) {
                                matches.add(span[0]);
                            }
                        }
                    }
                    VmArray out = new VmArray(query, matches.size());
                    for (int i = 0; i < matches.size(); i++) {
                        out.set(i, matches.get(i));
                    }
                    return out;
                });
        // First offset past start where a matching span starts or ends
        // (capped at limit); spans never transition headless beyond this.
        builtin("Landroid/text/Spanned;->nextSpanTransition(II"
                + "Ljava/lang/Class;)I",
                (receiver, args) -> {
                    int start = asInt(args[0]);
                    int limit = asInt(args[1]);
                    Object query = args[2];
                    String filter = query instanceof VmClass
                            ? ((VmClass) query).descriptor : null;
                    Object spans = ((VmObject) receiver)
                            .getField("Landroid/text/SpannableString;.mSpans");
                    int next = limit;
                    if (spans instanceof List) {
                        for (Object record : (List<?>) spans) {
                            Object[] span = (Object[]) record;
                            if (filter != null && !isInstance(span[0], filter)) {
                                continue;
                            }
                            int spanStart = asInt(span[1]);
                            int spanEnd = asInt(span[2]);
                            if (spanStart > start && spanStart < next) {
                                next = spanStart;
                            }
                            if (spanEnd > start && spanEnd < next) {
                                next = spanEnd;
                            }
                        }
                    }
                    return next;
                });
        builtin("Landroid/graphics/Paint;->getFlags()I",
                (receiver, args) -> {
                    Object value = ((VmObject) receiver)
                            .getField("Landroid/graphics/Paint;.mFlags");
                    return value == null ? 0 : asInt(value);
                });
        builtin("Landroid/graphics/Paint;->setFlags(I)V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/graphics/Paint;.mFlags",
                            asInt(args[0]));
                    return null;
                });
        // Text-measurement state round-trips for later measure/draw calls.
        builtin("Landroid/graphics/Paint;->setHinting(I)V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/graphics/Paint;.mHinting",
                            asInt(args[0]));
                    return null;
                });
        builtin("Landroid/graphics/Paint;->getHinting()I",
                (receiver, args) -> {
                    Object value = ((VmObject) receiver)
                            .getField("Landroid/graphics/Paint;.mHinting");
                    return value == null ? 0 : asInt(value);
                });
        builtin("Landroid/graphics/Paint;->setTextSize(F)V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/graphics/Paint;.mTextSize",
                            asFloatBits(args[0]));
                    return null;
                });
        builtin("Landroid/graphics/Paint;->getTextSize()F",
                (receiver, args) -> {
                    Object value = ((VmObject) receiver)
                            .getField("Landroid/graphics/Paint;.mTextSize");
                    return value == null ? 0.0f : asFloatBits(value);
                });
        builtin("Landroid/graphics/Paint;->setTypeface(Landroid/graphics/Typeface;)"
                + "Landroid/graphics/Typeface;",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/graphics/Paint;.mTypeface",
                            args[0]);
                    return args[0];
                });
        builtin("Landroid/graphics/Paint;->getTypeface()Landroid/graphics/Typeface;",
                (receiver, args) -> ((VmObject) receiver)
                        .getField("Landroid/graphics/Paint;.mTypeface"));
        // Boringness can't be proven headless (no bidi analysis): null
        // routes text through the full layout path, which measure needs.
        builtin("Landroid/text/BoringLayout;->isBoring(Ljava/lang/CharSequence;"
                + "Landroid/text/TextPaint;Landroid/text/TextDirectionHeuristic;Z"
                + "Landroid/text/BoringLayout$Metrics;)Landroid/text/BoringLayout$Metrics;",
                (receiver, args) -> null);
        // StaticLayout builders record their inputs; line breaking in
        // build() arrives with the first layout that needs it.
        builtin("Landroid/text/StaticLayout$Builder;->obtain(Ljava/lang/CharSequence;II"
                + "Landroid/text/TextPaint;I)Landroid/text/StaticLayout$Builder;",
                (receiver, args) -> {
                    VmObject builder =
                            newPlaceholder("Landroid/text/StaticLayout$Builder;");
                    builder.putField("Landroid/text/StaticLayout$Builder;.mText",
                            charSequenceText(args[0]));
                    builder.putField("Landroid/text/StaticLayout$Builder;.mStart",
                            asInt(args[1]));
                    builder.putField("Landroid/text/StaticLayout$Builder;.mEnd",
                            asInt(args[2]));
                    builder.putField("Landroid/text/StaticLayout$Builder;.mPaint", args[3]);
                    builder.putField("Landroid/text/StaticLayout$Builder;.mWidth",
                            asInt(args[4]));
                    return builder;
                });
        // Builder options record and chain; build() consumes them.
        builtin("Landroid/text/StaticLayout$Builder;->setTextDirection("
                + "Landroid/text/TextDirectionHeuristic;)"
                + "Landroid/text/StaticLayout$Builder;",
                (receiver, args) -> {
                    ((VmObject) receiver).putField(
                            "Landroid/text/StaticLayout$Builder;.mTextDir", args[0]);
                    return receiver;
                });
        builtin("Landroid/text/StaticLayout$Builder;->setLineBreakConfig("
                + "Landroid/graphics/text/LineBreakConfig;)"
                + "Landroid/text/StaticLayout$Builder;",
                (receiver, args) -> {
                    ((VmObject) receiver).putField(
                            "Landroid/text/StaticLayout$Builder;.mLineBreakConfig", args[0]);
                    return receiver;
                });
        builtin("Landroid/text/StaticLayout$Builder;->setAlignment("
                + "Landroid/text/Layout$Alignment;)Landroid/text/StaticLayout$Builder;",
                (receiver, args) -> {
                    ((VmObject) receiver).putField(
                            "Landroid/text/StaticLayout$Builder;.mAlignment", args[0]);
                    return receiver;
                });
        builtin("Landroid/text/StaticLayout$Builder;->setLineSpacing(FF)"
                + "Landroid/text/StaticLayout$Builder;",
                (receiver, args) -> {
                    VmObject builder = (VmObject) receiver;
                    builder.putField("Landroid/text/StaticLayout$Builder;.mSpacingAdd",
                            asFloatBits(args[0]));
                    builder.putField("Landroid/text/StaticLayout$Builder;.mSpacingMult",
                            asFloatBits(args[1]));
                    return receiver;
                });
        builtin("Landroid/text/StaticLayout$Builder;->setIncludePad(Z)"
                + "Landroid/text/StaticLayout$Builder;",
                (receiver, args) -> {
                    ((VmObject) receiver).putField(
                            "Landroid/text/StaticLayout$Builder;.mIncludePad", asInt(args[0]));
                    return receiver;
                });
        builtin("Landroid/text/StaticLayout$Builder;->setEllipsize("
                + "Landroid/text/TextUtils$TruncateAt;)Landroid/text/StaticLayout$Builder;",
                (receiver, args) -> {
                    ((VmObject) receiver).putField(
                            "Landroid/text/StaticLayout$Builder;.mEllipsize", args[0]);
                    return receiver;
                });
        builtin("Landroid/text/StaticLayout$Builder;->setEllipsizedWidth(I)"
                + "Landroid/text/StaticLayout$Builder;",
                (receiver, args) -> {
                    ((VmObject) receiver).putField(
                            "Landroid/text/StaticLayout$Builder;.mEllipsizedWidth",
                            asInt(args[0]));
                    return receiver;
                });
        builtin("Landroid/text/StaticLayout$Builder;->setMaxLines(I)"
                + "Landroid/text/StaticLayout$Builder;",
                (receiver, args) -> {
                    ((VmObject) receiver).putField(
                            "Landroid/text/StaticLayout$Builder;.mMaxLines", asInt(args[0]));
                    return receiver;
                });
        builtin("Landroid/text/StaticLayout$Builder;->setBreakStrategy(I)"
                + "Landroid/text/StaticLayout$Builder;",
                (receiver, args) -> {
                    ((VmObject) receiver).putField(
                            "Landroid/text/StaticLayout$Builder;.mBreakStrategy",
                            asInt(args[0]));
                    return receiver;
                });
        builtin("Landroid/text/StaticLayout$Builder;->setHyphenationFrequency(I)"
                + "Landroid/text/StaticLayout$Builder;",
                (receiver, args) -> {
                    ((VmObject) receiver).putField(
                            "Landroid/text/StaticLayout$Builder;.mHyphenation",
                            asInt(args[0]));
                    return receiver;
                });
        builtin("Landroid/text/StaticLayout$Builder;->setJustificationMode(I)"
                + "Landroid/text/StaticLayout$Builder;",
                (receiver, args) -> {
                    ((VmObject) receiver).putField(
                            "Landroid/text/StaticLayout$Builder;.mJustification",
                            asInt(args[0]));
                    return receiver;
                });
        builtin("Landroid/text/StaticLayout$Builder;->setIndents([I[I)"
                + "Landroid/text/StaticLayout$Builder;",
                (receiver, args) -> {
                    VmObject builder = (VmObject) receiver;
                    builder.putField("Landroid/text/StaticLayout$Builder;.mIndentsLeft",
                            args[0]);
                    builder.putField("Landroid/text/StaticLayout$Builder;.mIndentsRight",
                            args[1]);
                    return receiver;
                });
        builtin("Landroid/text/StaticLayout$Builder;->setUseLineSpacingFromFallbacks(Z)"
                + "Landroid/text/StaticLayout$Builder;",
                (receiver, args) -> {
                    ((VmObject) receiver).putField(
                            "Landroid/text/StaticLayout$Builder;.mFallbackLineSpacing",
                            asInt(args[0]));
                    return receiver;
                });
        // build() breaks lines greedily over the width approximation;
        // single-line text stays one line, the rest wraps on spaces.
        builtin("Landroid/text/StaticLayout$Builder;->build()Landroid/text/StaticLayout;",
                (receiver, args) -> buildStaticLayout((VmObject) receiver));
        // Layout getters read the broken lines; out-of-range lines read
        // 0 (device throws, headless stays lenient).
        builtin("Landroid/text/Layout;->getLineCount()I",
                (receiver, args) -> {
                    Object value = ((VmObject) receiver)
                            .getField("Landroid/text/StaticLayout;.mLineCount");
                    return value == null ? 0 : asInt(value);
                });
        builtin("Landroid/text/Layout;->getHeight()I",
                (receiver, args) -> {
                    Object value = ((VmObject) receiver)
                            .getField("Landroid/text/StaticLayout;.mHeight");
                    return value == null ? 0 : asInt(value);
                });
        builtin("Landroid/text/Layout;->getWidth()I",
                (receiver, args) -> {
                    Object value = ((VmObject) receiver)
                            .getField("Landroid/text/StaticLayout;.mWidth");
                    return value == null ? 0 : asInt(value);
                });
        builtin("Landroid/text/Layout;->getLineWidth(I)F",
                (receiver, args) -> {
                    VmArray widths = layoutFloats(receiver,
                            "Landroid/text/StaticLayout;.mLineWidths");
                    int line = asInt(args[0]);
                    return widths == null || line < 0 || line >= widths.length() ? 0.0f
                            : asFloatBits(widths.get(line));
                });
        builtin("Landroid/text/Layout;->getLineTop(I)I",
                (receiver, args) -> layoutLineInt(receiver,
                        "Landroid/text/StaticLayout;.mLineTops", asInt(args[0])));
        builtin("Landroid/text/Layout;->getLineBottom(I)I",
                (receiver, args) -> {
                    VmObject layout = (VmObject) receiver;
                    Object heightValue =
                            layout.getField("Landroid/text/StaticLayout;.mHeight");
                    int line = asInt(args[0]);
                    VmArray tops = layoutFloats(receiver,
                            "Landroid/text/StaticLayout;.mLineTops");
                    if (tops == null || line < 0 || line >= tops.length()) {
                        return 0;
                    }
                    if (line + 1 < tops.length()) {
                        return asInt(tops.get(line + 1));
                    }
                    return heightValue == null ? 0 : asInt(heightValue);
                });
        builtin("Landroid/text/Layout;->getLineStart(I)I",
                (receiver, args) -> layoutLineInt(receiver,
                        "Landroid/text/StaticLayout;.mLineStarts", asInt(args[0])));
        builtin("Landroid/text/Layout;->getLineEnd(I)I",
                (receiver, args) -> layoutLineInt(receiver,
                        "Landroid/text/StaticLayout;.mLineEnds", asInt(args[0])));
        builtin("Landroid/text/Layout;->getLineLeft(I)F",
                (receiver, args) -> 0.0f);
        builtin("Landroid/text/Layout;->getLineRight(I)F",
                (receiver, args) -> {
                    VmArray widths = layoutFloats(receiver,
                            "Landroid/text/StaticLayout;.mLineWidths");
                    int line = asInt(args[0]);
                    return widths == null || line < 0 || line >= widths.length() ? 0.0f
                            : asFloatBits(widths.get(line));
                });
        builtin("Landroid/text/Layout;->getEllipsisCount(I)I",
                (receiver, args) -> 0);
        builtin("Landroid/text/Layout;->getParagraphDirection(I)I",
                (receiver, args) -> 1);
        builtin("Landroid/text/Layout;->getLineBaseline(I)I",
                (receiver, args) -> {
                    VmObject layout = (VmObject) receiver;
                    VmArray tops = layoutFloats(layout,
                            "Landroid/text/StaticLayout;.mLineTops");
                    int line = asInt(args[0]);
                    if (tops == null || line < 0 || line >= tops.length()) {
                        return 0;
                    }
                    return asInt(tops.get(line)) + Math.round(layoutAscent(layout));
                });
        builtin("Landroid/text/Layout;->getLineAscent(I)I",
                (receiver, args) -> Math.round(
                        layoutAscent((VmObject) receiver)));
        builtin("Landroid/text/Layout;->getLineDescent(I)I",
                (receiver, args) -> {
                    Object paint = ((VmObject) receiver)
                            .getField("Landroid/text/StaticLayout;.mPaint");
                    float size = 0.0f;
                    if (paint instanceof VmObject) {
                        Object textSize = ((VmObject) paint)
                                .getField("Landroid/graphics/Paint;.mTextSize");
                        if (textSize != null) {
                            size = asFloatBits(textSize);
                        }
                    }
                    return Math.round(size * 0.2f);
                });
        builtin("Landroid/text/Layout;->getText()Ljava/lang/CharSequence;",
                (receiver, args) -> ((VmObject) receiver)
                        .getField("Landroid/text/StaticLayout;.mText"));
        builtin("Landroid/text/Layout;->getPaint()Landroid/text/TextPaint;",
                (receiver, args) -> ((VmObject) receiver)
                        .getField("Landroid/text/StaticLayout;.mPaint"));
        builtin("Landroid/text/Layout;->draw(Landroid/graphics/Canvas;)V",
                (receiver, args) -> {
                    // AOSP Layout.java draw(Canvas) → draw(c, (Path) null,
                    // (Paint) null, 0) (mainline main, fetched 2026-10-19);
                    // no selection/quals there. Line geometry per drawText
                    // (Canvas, int firstLine, int lastLine): per line,
                    // lbaseline = lbottom - getLineDescent(line) with x=0
                    // (ALIGN_NORMAL + DIR_LEFT_TO_RIGHT + no indent). Plain
                    // unspanned text takes AOSP's own direct-drawText leg
                    // (directions LTR && !spanned && !hasTab && !justify);
                    // line ends already exclude trailing whitespace, so
                    // start/end stand in for getLineVisibleEnd. Globals set
                    // aside: no TextLine run machinery, no bidi, no spans
                    // (staging grimace while the run engine stays unported).
                    layoutDraw((VmObject) receiver, args[0]);
                    return null;
                });
        builtin("Landroid/text/StaticLayout;->isFallbackLineSpacingEnabled()Z",
                (receiver, args) -> {
                    Object value = ((VmObject) receiver)
                            .getField("Landroid/text/StaticLayout;.mFallbackLineSpacing");
                    return value == null ? 0 : asInt(value);
                });
        // Line-break configs record and chain; consumed by text layout.
        builtin("Landroid/graphics/text/LineBreakConfig$Builder;->setLineBreakStyle(I)"
                + "Landroid/graphics/text/LineBreakConfig$Builder;",
                (receiver, args) -> {
                    ((VmObject) receiver).putField(
                            "Landroid/graphics/text/LineBreakConfig$Builder;.mStyle",
                            asInt(args[0]));
                    return receiver;
                });
        builtin("Landroid/graphics/text/LineBreakConfig$Builder;->setLineBreakWordStyle(I)"
                + "Landroid/graphics/text/LineBreakConfig$Builder;",
                (receiver, args) -> {
                    ((VmObject) receiver).putField(
                            "Landroid/graphics/text/LineBreakConfig$Builder;.mWordStyle",
                            asInt(args[0]));
                    return receiver;
                });
        builtin("Landroid/graphics/text/LineBreakConfig$Builder;->setHyphenation(I)"
                + "Landroid/graphics/text/LineBreakConfig$Builder;",
                (receiver, args) -> {
                    ((VmObject) receiver).putField(
                            "Landroid/graphics/text/LineBreakConfig$Builder;.mHyphenation",
                            asInt(args[0]));
                    return receiver;
                });
        // build() snapshots the recorded options into an immutable config.
        builtin("Landroid/graphics/text/LineBreakConfig$Builder;->build()"
                + "Landroid/graphics/text/LineBreakConfig;",
                (receiver, args) -> {
                    VmObject config =
                            newPlaceholder("Landroid/graphics/text/LineBreakConfig;");
                    config.copyFieldsFrom((VmObject) receiver);
                    return config;
                });
        // Desired width approximates from stored paint state (average
        // glyph half the text size, modulated by scale and spacing).
        // Real font measurement plugs into measureText when draw needs it.
        builtin("Landroid/text/Layout;->getDesiredWidth(Ljava/lang/CharSequence;II"
                + "Landroid/text/TextPaint;)F",
                (receiver, args) -> measureText(args[3], charSequenceText(args[0]),
                        asInt(args[1]), asInt(args[2])));
        // Shaders back Brush fills; stored for later draw calls to consume.
        builtin("Landroid/graphics/Paint;->setShader(Landroid/graphics/Shader;)"
                + "Landroid/graphics/Shader;",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/graphics/Paint;.mShader",
                            args[0]);
                    return args[0];
                });
        builtin("Landroid/graphics/Paint;->getShader()Landroid/graphics/Shader;",
                (receiver, args) -> ((VmObject) receiver)
                        .getField("Landroid/graphics/Paint;.mShader"));
        builtin("Landroid/graphics/Paint;->setTextScaleX(F)V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/graphics/Paint;.mTextScaleX",
                            asFloatBits(args[0]));
                    return null;
                });
        builtin("Landroid/graphics/Paint;->getTextScaleX()F",
                (receiver, args) -> {
                    Object value = ((VmObject) receiver)
                            .getField("Landroid/graphics/Paint;.mTextScaleX");
                    return value == null ? 1.0f : asFloatBits(value);
                });
        // Remaining text-state round-trips (spacing, skew, flags, locale,
        // features, shadow, filter, blend, alpha) for measure/draw to read.
        builtin("Landroid/graphics/Paint;->setLetterSpacing(F)V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/graphics/Paint;.mLetterSpacing",
                            asFloatBits(args[0]));
                    return null;
                });
        builtin("Landroid/graphics/Paint;->getLetterSpacing()F",
                (receiver, args) -> {
                    Object value = ((VmObject) receiver)
                            .getField("Landroid/graphics/Paint;.mLetterSpacing");
                    return value == null ? 0.0f : asFloatBits(value);
                });
        builtin("Landroid/graphics/Paint;->setWordSpacing(F)V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/graphics/Paint;.mWordSpacing",
                            asFloatBits(args[0]));
                    return null;
                });
        builtin("Landroid/graphics/Paint;->getWordSpacing()F",
                (receiver, args) -> {
                    Object value = ((VmObject) receiver)
                            .getField("Landroid/graphics/Paint;.mWordSpacing");
                    return value == null ? 0.0f : asFloatBits(value);
                });
        builtin("Landroid/graphics/Paint;->setTextSkewX(F)V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/graphics/Paint;.mTextSkewX",
                            asFloatBits(args[0]));
                    return null;
                });
        builtin("Landroid/graphics/Paint;->getTextSkewX()F",
                (receiver, args) -> {
                    Object value = ((VmObject) receiver)
                            .getField("Landroid/graphics/Paint;.mTextSkewX");
                    return value == null ? 0.0f : asFloatBits(value);
                });
        builtin("Landroid/graphics/Paint;->setAntiAlias(Z)V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/graphics/Paint;.mAntiAlias",
                            asInt(args[0]));
                    return null;
                });
        builtin("Landroid/graphics/Paint;->isAntiAlias()Z",
                (receiver, args) -> {
                    Object value = ((VmObject) receiver)
                            .getField("Landroid/graphics/Paint;.mAntiAlias");
                    return value == null ? 0 : asInt(value);
                });
        builtin("Landroid/graphics/Paint;->setFilterBitmap(Z)V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/graphics/Paint;.mFilterBitmap",
                            asInt(args[0]));
                    return null;
                });
        builtin("Landroid/graphics/Paint;->isFilterBitmap()Z",
                (receiver, args) -> {
                    Object value = ((VmObject) receiver)
                            .getField("Landroid/graphics/Paint;.mFilterBitmap");
                    return value == null ? 0 : asInt(value);
                });
        builtin("Landroid/graphics/Paint;->setDither(Z)V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/graphics/Paint;.mDither",
                            asInt(args[0]));
                    return null;
                });
        builtin("Landroid/graphics/Paint;->isDither()Z",
                (receiver, args) -> {
                    Object value = ((VmObject) receiver)
                            .getField("Landroid/graphics/Paint;.mDither");
                    return value == null ? 0 : asInt(value);
                });
        builtin("Landroid/graphics/Paint;->setStrikeThruText(Z)V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/graphics/Paint;.mStrikeThru",
                            asInt(args[0]));
                    return null;
                });
        builtin("Landroid/graphics/Paint;->isStrikeThruText()Z",
                (receiver, args) -> {
                    Object value = ((VmObject) receiver)
                            .getField("Landroid/graphics/Paint;.mStrikeThru");
                    return value == null ? 0 : asInt(value);
                });
        builtin("Landroid/graphics/Paint;->setUnderlineText(Z)V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/graphics/Paint;.mUnderline",
                            asInt(args[0]));
                    return null;
                });
        builtin("Landroid/graphics/Paint;->isUnderlineText()Z",
                (receiver, args) -> {
                    Object value = ((VmObject) receiver)
                            .getField("Landroid/graphics/Paint;.mUnderline");
                    return value == null ? 0 : asInt(value);
                });
        builtin("Landroid/graphics/Paint;->setFakeBoldText(Z)V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/graphics/Paint;.mFakeBold",
                            asInt(args[0]));
                    return null;
                });
        builtin("Landroid/graphics/Paint;->isFakeBoldText()Z",
                (receiver, args) -> {
                    Object value = ((VmObject) receiver)
                            .getField("Landroid/graphics/Paint;.mFakeBold");
                    return value == null ? 0 : asInt(value);
                });
        builtin("Landroid/graphics/Paint;->setElegantTextHeight(Z)V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/graphics/Paint;.mElegant",
                            asInt(args[0]));
                    return null;
                });
        builtin("Landroid/graphics/Paint;->isElegantTextHeight()Z",
                (receiver, args) -> {
                    Object value = ((VmObject) receiver)
                            .getField("Landroid/graphics/Paint;.mElegant");
                    return value == null ? 0 : asInt(value);
                });
        builtin("Landroid/graphics/Paint;->setTextLocale(Ljava/util/Locale;)V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/graphics/Paint;.mTextLocale",
                            args[0]);
                    return null;
                });
        builtin("Landroid/graphics/Paint;->getTextLocale()Ljava/util/Locale;",
                (receiver, args) -> ((VmObject) receiver)
                        .getField("Landroid/graphics/Paint;.mTextLocale"));
        builtin("Landroid/graphics/Paint;->setTextLocales(Landroid/os/LocaleList;)V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/graphics/Paint;.mTextLocales",
                            args[0]);
                    return null;
                });
        builtin("Landroid/graphics/Paint;->getTextLocales()Landroid/os/LocaleList;",
                (receiver, args) -> ((VmObject) receiver)
                        .getField("Landroid/graphics/Paint;.mTextLocales"));
        builtin("Landroid/graphics/Paint;->setFontFeatureSettings(Ljava/lang/String;)V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField(
                            "Landroid/graphics/Paint;.mFontFeature", argString(args, 0));
                    return null;
                });
        builtin("Landroid/graphics/Paint;->getFontFeatureSettings()Ljava/lang/String;",
                (receiver, args) -> ((VmObject) receiver)
                        .getField("Landroid/graphics/Paint;.mFontFeature"));
        builtin("Landroid/graphics/Paint;->setColorFilter(Landroid/graphics/ColorFilter;)"
                + "Landroid/graphics/ColorFilter;",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/graphics/Paint;.mColorFilter",
                            args[0]);
                    return args[0];
                });
        builtin("Landroid/graphics/Paint;->getColorFilter()Landroid/graphics/ColorFilter;",
                (receiver, args) -> ((VmObject) receiver)
                        .getField("Landroid/graphics/Paint;.mColorFilter"));
        builtin("Landroid/graphics/Paint;->clearColorFilter()V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/graphics/Paint;.mColorFilter",
                            null);
                    return null;
                });
        builtin("Landroid/graphics/Paint;->setBlendMode(Landroid/graphics/BlendMode;)V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/graphics/Paint;.mBlendMode",
                            args[0]);
                    return null;
                });
        builtin("Landroid/graphics/Paint;->getBlendMode()Landroid/graphics/BlendMode;",
                (receiver, args) -> ((VmObject) receiver)
                        .getField("Landroid/graphics/Paint;.mBlendMode"));
        builtin("Landroid/graphics/Paint;->setAlpha(I)V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/graphics/Paint;.mAlpha",
                            asInt(args[0]));
                    return null;
                });
        builtin("Landroid/graphics/Paint;->getAlpha()I",
                (receiver, args) -> {
                    Object value = ((VmObject) receiver)
                            .getField("Landroid/graphics/Paint;.mAlpha");
                    return value == null ? 255 : asInt(value);
                });
        builtin("Landroid/graphics/Paint;->setShadowLayer(FFFI)V",
                (receiver, args) -> {
                    VmObject paint = (VmObject) receiver;
                    paint.putField("Landroid/graphics/Paint;.mShadowRadius",
                            asFloatBits(args[0]));
                    paint.putField("Landroid/graphics/Paint;.mShadowDx",
                            asFloatBits(args[1]));
                    paint.putField("Landroid/graphics/Paint;.mShadowDy",
                            asFloatBits(args[2]));
                    paint.putField("Landroid/graphics/Paint;.mShadowColor", asInt(args[3]));
                    paint.putField("Landroid/graphics/Paint;.mHasShadow", 1);
                    return null;
                });
        builtin("Landroid/graphics/Paint;->setShadowLayer(FFFJ)V",
                (receiver, args) -> {
                    VmObject paint = (VmObject) receiver;
                    paint.putField("Landroid/graphics/Paint;.mShadowRadius",
                            asFloatBits(args[0]));
                    paint.putField("Landroid/graphics/Paint;.mShadowDx",
                            asFloatBits(args[1]));
                    paint.putField("Landroid/graphics/Paint;.mShadowDy",
                            asFloatBits(args[2]));
                    paint.putField("Landroid/graphics/Paint;.mShadowColor", args[3]);
                    paint.putField("Landroid/graphics/Paint;.mHasShadow", 1);
                    return null;
                });
        builtin("Landroid/graphics/Paint;->clearShadowLayer()V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/graphics/Paint;.mHasShadow", 0);
                    return null;
                });
        builtin("Landroid/graphics/Paint;->hasShadowLayer()Z",
                (receiver, args) -> {
                    Object value = ((VmObject) receiver)
                            .getField("Landroid/graphics/Paint;.mHasShadow");
                    return value == null ? 0 : asInt(value);
                });
        // Color is stored intact for later draw calls to consume.
        builtin("Landroid/graphics/Paint;->setColor(I)V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/graphics/Paint;.mColor",
                            asInt(args[0]));
                    return null;
                });
        builtin("Landroid/graphics/Paint;->getColor()I",
                (receiver, args) -> {
                    Object value = ((VmObject) receiver)
                            .getField("Landroid/graphics/Paint;.mColor");
                    return value == null ? 0 : asInt(value);
                });
        // Stroke width is a float field read back by stroking ops.
        builtin("Landroid/graphics/Paint;->setStrokeWidth(F)V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/graphics/Paint;.mStrokeWidth",
                            args[0]);
                    return null;
                });
        builtin("Landroid/graphics/Paint;->getStrokeWidth()F",
                (receiver, args) -> {
                    Object value = ((VmObject) receiver)
                            .getField("Landroid/graphics/Paint;.mStrokeWidth");
                    return value == null ? 0.0f : asFloatBits(value);
                });
        // Style is an enum placeholder round-tripped for draw dispatch.
        builtin("Landroid/graphics/Paint;->setStyle(Landroid/graphics/Paint$Style;)V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField("Landroid/graphics/Paint;.mStyle",
                            args[0]);
                    return null;
                });
        builtin("Landroid/graphics/Paint;->getStyle()Landroid/graphics/Paint$Style;",
                (receiver, args) -> ((VmObject) receiver)
                        .getField("Landroid/graphics/Paint;.mStyle"));
        builtin("Landroid/os/SystemClock;->uptimeMillis()J",
                (receiver, args) -> System.nanoTime() / 1000000L);
        builtin("Landroid/os/SystemClock;->elapsedRealtime()J",
                (receiver, args) -> System.nanoTime() / 1000000L);
        builtin("Landroid/os/SystemClock;->elapsedRealtimeNanos()J",
                (receiver, args) -> System.nanoTime());

        builtin("Landroid/os/Looper;->getMainLooper()Landroid/os/Looper;",
                (receiver, args) -> mainLooper());
        builtin("Landroid/os/Looper;->myLooper()Landroid/os/Looper;",
                (receiver, args) -> mainLooper());
        builtin("Landroid/os/Looper;->myQueue()Landroid/os/MessageQueue;",
                (receiver, args) -> mainQueue());
        builtin("Landroid/os/Looper;->prepare()V",
                (receiver, args) -> {
                    mainLooper();
                    return null;
                });
        builtin("Landroid/os/Looper;->prepareMainLooper()V",
                (receiver, args) -> {
                    mainLooper();
                    return null;
                });
        // Deviation: the real loop() blocks forever pumping; ours drains
        // nothing (posts run inline) and returns so probes terminate.
        builtin("Landroid/os/Looper;->loop()V",
                (receiver, args) -> null);
        builtin("Landroid/os/Looper;->getQueue()Landroid/os/MessageQueue;",
                (receiver, args) -> mainQueue());
        builtin("Landroid/os/Looper;->getThread()Ljava/lang/Thread;",
                (receiver, args) -> Thread.currentThread());
        builtin("Landroid/os/Looper;->quit()V",
                (receiver, args) -> null);
        builtin("Landroid/os/Looper;->quitSafely()V",
                (receiver, args) -> null);

        builtin("Landroid/os/Handler;-><init>()V",
                (receiver, args) -> {
                    initHandler((VmObject) receiver, mainLooper(), null);
                    return null;
                });
        builtin("Landroid/os/Handler;-><init>(Landroid/os/Looper;)V",
                (receiver, args) -> {
                    initHandler((VmObject) receiver, args[0], null);
                    return null;
                });
        builtin("Landroid/os/Handler;-><init>(Landroid/os/Handler$Callback;)V",
                (receiver, args) -> {
                    initHandler((VmObject) receiver, mainLooper(), args[0]);
                    return null;
                });
        builtin("Landroid/os/Handler;-><init>(Landroid/os/Looper;"
                + "Landroid/os/Handler$Callback;)V",
                (receiver, args) -> {
                    initHandler((VmObject) receiver, args[0], args[1]);
                    return null;
                });
        // createAsync is new Handler(looper[, callback]) with the async flag,
        // which is unobservable while posts run inline.
        builtin("Landroid/os/Handler;->createAsync(Landroid/os/Looper;)Landroid/os/Handler;",
                (receiver, args) -> {
                    VmObject handler = newPlaceholder("Landroid/os/Handler;");
                    initHandler(handler, args[0], null);
                    return handler;
                });
        builtin("Landroid/os/Handler;->createAsync(Landroid/os/Looper;"
                + "Landroid/os/Handler$Callback;)Landroid/os/Handler;",
                (receiver, args) -> {
                    VmObject handler = newPlaceholder("Landroid/os/Handler;");
                    initHandler(handler, args[0], args[1]);
                    return handler;
                });
        builtin("Landroid/os/Handler;->post(Ljava/lang/Runnable;)Z",
                (receiver, args) -> {
                    VmTask.runnable(this, args[0], "run").call();
                    return 1;
                });
        builtin("Landroid/os/Handler;->postDelayed(Ljava/lang/Runnable;J)Z",
                (receiver, args) -> {
                    VmTask.runnable(this, args[0], "run").call();
                    return 1;
                });
        builtin("Landroid/os/Handler;->postAtTime(Ljava/lang/Runnable;J)Z",
                (receiver, args) -> {
                    VmTask.runnable(this, args[0], "run").call();
                    return 1;
                });
        builtin("Landroid/os/Handler;->postAtFrontOfQueue(Ljava/lang/Runnable;)Z",
                (receiver, args) -> {
                    VmTask.runnable(this, args[0], "run").call();
                    return 1;
                });
        builtin("Landroid/os/Handler;->sendMessage(Landroid/os/Message;)Z",
                (receiver, args) -> {
                    dispatchMessage((VmObject) receiver, args[0]);
                    return 1;
                });
        builtin("Landroid/os/Handler;->sendEmptyMessage(I)Z",
                (receiver, args) -> {
                    dispatchMessage((VmObject) receiver, message(argInt(args, 0)));
                    return 1;
                });
        builtin("Landroid/os/Handler;->sendMessageDelayed(Landroid/os/Message;J)Z",
                (receiver, args) -> {
                    dispatchMessage((VmObject) receiver, args[0]);
                    return 1;
                });
        builtin("Landroid/os/Handler;->sendEmptyMessageDelayed(IJ)Z",
                (receiver, args) -> {
                    dispatchMessage((VmObject) receiver, message(argInt(args, 0)));
                    return 1;
                });
        builtin("Landroid/os/Handler;->obtainMessage()Landroid/os/Message;",
                (receiver, args) -> messageTarget(newPlaceholder("Landroid/os/Message;"),
                        receiver));
        builtin("Landroid/os/Handler;->obtainMessage(I)Landroid/os/Message;",
                (receiver, args) -> {
                    VmObject message = messageTarget(newPlaceholder("Landroid/os/Message;"),
                            receiver);
                    message.putField("Landroid/os/Message;.what", argInt(args, 0));
                    return message;
                });
        builtin("Landroid/os/Handler;->getLooper()Landroid/os/Looper;",
                (receiver, args) -> {
                    Object looper = ((VmObject) receiver)
                            .getField("Landroid/os/Handler;.mLooper");
                    return looper == null ? mainLooper() : looper;
                });
        builtin("Landroid/os/Handler;->hasMessages(I)Z",
                (receiver, args) -> 0);
        builtin("Landroid/os/Handler;->removeMessages(I)V",
                (receiver, args) -> null);
        builtin("Landroid/os/Handler;->removeCallbacks(Ljava/lang/Runnable;)V",
                (receiver, args) -> null);
        builtin("Landroid/os/Message;->obtain()Landroid/os/Message;",
                (receiver, args) -> newPlaceholder("Landroid/os/Message;"));

        builtin("Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;->newUpdater("
                + "Ljava/lang/Class;Ljava/lang/Class;Ljava/lang/String;)"
                + "Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;",
                (receiver, args) -> new FieldUpdater(
                        ((VmClass) args[0]).descriptor, argString(args, 2)));
        builtin("Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;->compareAndSet("
                + "Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Z",
                (receiver, args) -> bool(updaterCas((FieldUpdater) receiver,
                        FieldUpdater.checkedTarget(args[0]), args[1], args[2])));
        builtin("Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;->weakCompareAndSet("
                + "Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Z",
                (receiver, args) -> bool(updaterCas((FieldUpdater) receiver,
                        FieldUpdater.checkedTarget(args[0]), args[1], args[2])));
        builtin("Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;->set("
                + "Ljava/lang/Object;Ljava/lang/Object;)V",
                (receiver, args) -> {
                    FieldUpdater.checkedTarget(args[0])
                            .putField(((FieldUpdater) receiver).key(), args[1]);
                    return null;
                });
        builtin("Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;->get("
                + "Ljava/lang/Object;)Ljava/lang/Object;",
                (receiver, args) -> FieldUpdater.checkedTarget(args[0])
                        .getField(((FieldUpdater) receiver).key()));
        builtin("Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;->getAndSet("
                + "Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
                (receiver, args) -> {
                    VmObject target = FieldUpdater.checkedTarget(args[0]);
                    Object old = target.getField(((FieldUpdater) receiver).key());
                    target.putField(((FieldUpdater) receiver).key(), args[1]);
                    return old;
                });
        builtin("Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;->lazySet("
                + "Ljava/lang/Object;Ljava/lang/Object;)V",
                (receiver, args) -> {
                    FieldUpdater.checkedTarget(args[0])
                            .putField(((FieldUpdater) receiver).key(), args[1]);
                    return null;
                });
        // Long/Integer updaters share the model: plain field reads/writes
        // (single thread, no volatility), longs compared by value.
        builtin("Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;->newUpdater("
                + "Ljava/lang/Class;Ljava/lang/String;)"
                + "Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;",
                (receiver, args) -> new FieldUpdater(
                        ((VmClass) args[0]).descriptor, argString(args, 1)));
        builtin("Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;->compareAndSet("
                + "Ljava/lang/Object;JJ)Z",
                (receiver, args) -> bool(longCas((FieldUpdater) receiver,
                        FieldUpdater.checkedTarget(args[0]), argLong(args, 1),
                        argLong(args, 2))));
        builtin("Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;->weakCompareAndSet("
                + "Ljava/lang/Object;JJ)Z",
                (receiver, args) -> bool(longCas((FieldUpdater) receiver,
                        FieldUpdater.checkedTarget(args[0]), argLong(args, 1),
                        argLong(args, 2))));
        builtin("Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;->set("
                + "Ljava/lang/Object;J)V",
                (receiver, args) -> {
                    FieldUpdater.checkedTarget(args[0])
                            .putField(((FieldUpdater) receiver).key(), args[1]);
                    return null;
                });
        builtin("Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;->lazySet("
                + "Ljava/lang/Object;J)V",
                (receiver, args) -> {
                    FieldUpdater.checkedTarget(args[0])
                            .putField(((FieldUpdater) receiver).key(), args[1]);
                    return null;
                });
        builtin("Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;->get("
                + "Ljava/lang/Object;)J",
                (receiver, args) -> {
                    Object have = FieldUpdater.checkedTarget(args[0])
                            .getField(((FieldUpdater) receiver).key());
                    return have == null ? 0L : asLong(have);
                });
        builtin("Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;->getAndSet("
                + "Ljava/lang/Object;J)J",
                (receiver, args) -> {
                    VmObject target = FieldUpdater.checkedTarget(args[0]);
                    Object have = target.getField(((FieldUpdater) receiver).key());
                    long old = have == null ? 0L : asLong(have);
                    target.putField(((FieldUpdater) receiver).key(), args[1]);
                    return old;
                });
        builtin("Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;->getAndAdd("
                + "Ljava/lang/Object;J)J",
                (receiver, args) -> longAdd((FieldUpdater) receiver,
                        FieldUpdater.checkedTarget(args[0]), argLong(args, 1), false));
        builtin("Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;->addAndGet("
                + "Ljava/lang/Object;J)J",
                (receiver, args) -> longAdd((FieldUpdater) receiver,
                        FieldUpdater.checkedTarget(args[0]), argLong(args, 1), true));
        builtin("Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;->getAndIncrement("
                + "Ljava/lang/Object;)J",
                (receiver, args) -> longAdd((FieldUpdater) receiver,
                        FieldUpdater.checkedTarget(args[0]), 1L, false));
        builtin("Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;->incrementAndGet("
                + "Ljava/lang/Object;)J",
                (receiver, args) -> longAdd((FieldUpdater) receiver,
                        FieldUpdater.checkedTarget(args[0]), 1L, true));
        builtin("Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;->getAndDecrement("
                + "Ljava/lang/Object;)J",
                (receiver, args) -> longAdd((FieldUpdater) receiver,
                        FieldUpdater.checkedTarget(args[0]), -1L, false));
        builtin("Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;->decrementAndGet("
                + "Ljava/lang/Object;)J",
                (receiver, args) -> longAdd((FieldUpdater) receiver,
                        FieldUpdater.checkedTarget(args[0]), -1L, true));
        builtin("Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;->newUpdater("
                + "Ljava/lang/Class;Ljava/lang/String;)"
                + "Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;",
                (receiver, args) -> new FieldUpdater(
                        ((VmClass) args[0]).descriptor, argString(args, 1)));
        builtin("Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;->compareAndSet("
                + "Ljava/lang/Object;II)Z",
                (receiver, args) -> bool(intCas((FieldUpdater) receiver,
                        FieldUpdater.checkedTarget(args[0]), argInt(args, 1),
                        argInt(args, 2))));
        builtin("Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;->weakCompareAndSet("
                + "Ljava/lang/Object;II)Z",
                (receiver, args) -> bool(intCas((FieldUpdater) receiver,
                        FieldUpdater.checkedTarget(args[0]), argInt(args, 1),
                        argInt(args, 2))));
        builtin("Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;->set("
                + "Ljava/lang/Object;I)V",
                (receiver, args) -> {
                    FieldUpdater.checkedTarget(args[0])
                            .putField(((FieldUpdater) receiver).key(), args[1]);
                    return null;
                });
        builtin("Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;->lazySet("
                + "Ljava/lang/Object;I)V",
                (receiver, args) -> {
                    FieldUpdater.checkedTarget(args[0])
                            .putField(((FieldUpdater) receiver).key(), args[1]);
                    return null;
                });
        builtin("Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;->get("
                + "Ljava/lang/Object;)I",
                (receiver, args) -> {
                    Object have = FieldUpdater.checkedTarget(args[0])
                            .getField(((FieldUpdater) receiver).key());
                    return have == null ? 0 : asInt(have);
                });
        builtin("Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;->getAndSet("
                + "Ljava/lang/Object;I)I",
                (receiver, args) -> {
                    VmObject target = FieldUpdater.checkedTarget(args[0]);
                    Object have = target.getField(((FieldUpdater) receiver).key());
                    int old = have == null ? 0 : asInt(have);
                    target.putField(((FieldUpdater) receiver).key(), args[1]);
                    return old;
                });
        builtin("Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;->getAndAdd("
                + "Ljava/lang/Object;I)I",
                (receiver, args) -> intAdd((FieldUpdater) receiver,
                        FieldUpdater.checkedTarget(args[0]), argInt(args, 1), false));
        builtin("Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;->addAndGet("
                + "Ljava/lang/Object;I)I",
                (receiver, args) -> intAdd((FieldUpdater) receiver,
                        FieldUpdater.checkedTarget(args[0]), argInt(args, 1), true));
        builtin("Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;->getAndIncrement("
                + "Ljava/lang/Object;)I",
                (receiver, args) -> intAdd((FieldUpdater) receiver,
                        FieldUpdater.checkedTarget(args[0]), 1, false));
        builtin("Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;->incrementAndGet("
                + "Ljava/lang/Object;)I",
                (receiver, args) -> intAdd((FieldUpdater) receiver,
                        FieldUpdater.checkedTarget(args[0]), 1, true));
        builtin("Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;->getAndDecrement("
                + "Ljava/lang/Object;)I",
                (receiver, args) -> intAdd((FieldUpdater) receiver,
                        FieldUpdater.checkedTarget(args[0]), -1, false));
        builtin("Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;->decrementAndGet("
                + "Ljava/lang/Object;)I",
                (receiver, args) -> intAdd((FieldUpdater) receiver,
                        FieldUpdater.checkedTarget(args[0]), -1, true));

        builtin("Ljava/lang/Class;->getName()Ljava/lang/String;",
                (receiver, args) -> classNameOf(((VmClass) receiver).descriptor));
        // Primitive descriptors are the single-letter codes.
        builtin("Ljava/lang/Class;->isPrimitive()Z",
                (receiver, args) -> {
                    String descriptor = ((VmClass) receiver).descriptor;
                    return descriptor.length() == 1
                            && "ZBSCIJFDV".indexOf(descriptor.charAt(0)) >= 0 ? 1 : 0;
                });
        // getClass observes the model type: dex objects report their class,
        // arrays their array descriptor, host values their host descriptor.
        builtin("Ljava/lang/Object;->getClass()Ljava/lang/Class;",
                (receiver, args) -> {
                    if (receiver == null) {
                        throw new NullPointerException("null.getClass()");
                    }
                    if (receiver instanceof VmObject) {
                        return new VmClass(((VmObject) receiver).type.type);
                    }
                    if (receiver instanceof VmArray) {
                        return new VmClass("[" + ((VmArray) receiver).elementType);
                    }
                    if (receiver instanceof VmClass) {
                        return new VmClass("Ljava/lang/Class;");
                    }
                    return new VmClass(hostDescriptor(receiver.getClass()));
                });
        builtin("Ljava/lang/Class;->toString()Ljava/lang/String;",
                (receiver, args) -> "class " + classNameOf(((VmClass) receiver).descriptor));
        builtin("Ljava/lang/Class;->getSimpleName()Ljava/lang/String;",
                (receiver, args) -> {
                    String name = classNameOf(((VmClass) receiver).descriptor);
                    int dot = name.lastIndexOf('.');
                    if (dot >= 0) {
                        name = name.substring(dot + 1);
                    }
                    int inner = name.lastIndexOf('$');
                    if (inner >= 0) {
                        name = name.substring(inner + 1);
                    }
                    return name;
                });
        builtin("Ljava/lang/Class;->isInstance(Ljava/lang/Object;)Z",
                (receiver, args) -> bool(isInstance(args[0],
                        ((VmClass) receiver).descriptor)));
        builtin("Ljava/lang/Class;->cast(Ljava/lang/Object;)Ljava/lang/Object;",
                (receiver, args) -> {
                    String descriptor = ((VmClass) receiver).descriptor;
                    if (!isInstance(args[0], descriptor)) {
                        throw new ClassCastException(descriptor);
                    }
                    return args[0];
                });
        builtin("Ljava/lang/Class;->getSuperclass()Ljava/lang/Class;",
                (receiver, args) -> {
                    Supers supers = supersOf(((VmClass) receiver).descriptor);
                    return supers.superclass == null ? null : new VmClass(supers.superclass);
                });
        builtin("Ljava/lang/Class;->isAssignableFrom(Ljava/lang/Class;)Z",
                (receiver, args) -> bool(typeAssignable(((VmClass) receiver).descriptor,
                        ((VmClass) args[0]).descriptor)));
        builtin("Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;",
                (receiver, args) -> lookupClass(argString(args, 0)));
        // There is one class universe (dex + framework + host), so its loader
        // is a singleton placeholder; services resolve to nothing (no
        // META-INF/services providers exist in that universe).
        builtin("Ljava/lang/Class;->getClassLoader()Ljava/lang/ClassLoader;",
                (receiver, args) -> newPlaceholder("Ljava/lang/ClassLoader;"));
        builtin("Ljava/util/ServiceLoader;->load(Ljava/lang/Class;)Ljava/util/ServiceLoader;",
                (receiver, args) -> newPlaceholder("Ljava/util/ServiceLoader;"));
        builtin("Ljava/util/ServiceLoader;->load(Ljava/lang/Class;Ljava/lang/ClassLoader;)"
                + "Ljava/util/ServiceLoader;",
                (receiver, args) -> newPlaceholder("Ljava/util/ServiceLoader;"));
        builtin("Ljava/util/ServiceLoader;->iterator()Ljava/util/Iterator;",
                (receiver, args) -> java.util.Collections.emptyIterator());
        // Reflective factories (e.g. coroutines' Handler.createAsync
        // lookup) resolve through the model: the Method placeholder records
        // its target, and invoke reuses the matching dispatch. Host
        // reflection stays behind the deny list; missing methods fail lookup
        // with NoSuchMethodException, as on device.
        builtin("Ljava/lang/Class;->getDeclaredMethod(Ljava/lang/String;[Ljava/lang/Class;)"
                + "Ljava/lang/reflect/Method;",
                (receiver, args) -> {
                    if (!(receiver instanceof VmClass)) {
                        throw new VmException("not a class");
                    }
                    String classDesc = ((VmClass) receiver).descriptor;
                    MethodTarget target = declaredMethod(classDesc, argString(args, 0),
                            classArray(args[1]));
                    VmObject method = newPlaceholder("Ljava/lang/reflect/Method;");
                    method.putField(METHOD_TARGET, target);
                    return method;
                });
        // Our model retains no annotations; host methods report their own.
        builtin("Ljava/lang/reflect/Method;->getAnnotations()"
                + "[Ljava/lang/annotation/Annotation;",
                (receiver, args) -> {
                    if (receiver instanceof java.lang.reflect.Method) {
                        return boxReturn(
                                ((java.lang.reflect.Method) receiver).getAnnotations());
                    }
                    return new VmArray("Ljava/lang/annotation/Annotation;", 0);
                });
        // getMethod inherits: same placeholder, hierarchy-walking lookup.
        builtin("Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)"
                + "Ljava/lang/reflect/Method;",
                (receiver, args) -> {
                    if (!(receiver instanceof VmClass)) {
                        throw new VmException("not a class");
                    }
                    String classDesc = ((VmClass) receiver).descriptor;
                    MethodTarget target = publicMethod(classDesc, argString(args, 0),
                            classArray(args[1]));
                    VmObject method = newPlaceholder("Ljava/lang/reflect/Method;");
                    method.putField(METHOD_TARGET, target);
                    return method;
                });
        builtin("Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)"
                + "Ljava/lang/Object;",
                (receiver, args) -> {
                    if (!(receiver instanceof VmObject)) {
                        throw new VmException("not a method");
                    }
                    Object target = ((VmObject) receiver).getField(METHOD_TARGET);
                    if (!(target instanceof MethodTarget)) {
                        throw new VmException("not a method");
                    }
                    return invokeTarget((MethodTarget) target, args[0],
                            objectArray(args[1]));
                });
        // Class.forName with loader takes the same single-universe lookup;
        // loader and initialize flags have no headless meaning.
        builtin("Ljava/lang/Class;->forName(Ljava/lang/String;ZLjava/lang/ClassLoader;)"
                + "Ljava/lang/Class;",
                (receiver, args) -> lookupClass(argString(args, 0)));
        // Loading is the same lookup through whichever loader instance
        // dex code holds; the deny list still bars reflective access to
        // loaders themselves.
        builtin("Ljava/lang/ClassLoader;->loadClass(Ljava/lang/String;)Ljava/lang/Class;",
                (receiver, args) -> lookupClass(argString(args, 0)));
        // Reflective construction mirrors Method handling: the Constructor
        // placeholder records its target, and newInstance allocates like
        // new-instance before running the body.
        builtin("Ljava/lang/Class;->getDeclaredConstructor([Ljava/lang/Class;)"
                + "Ljava/lang/reflect/Constructor;",
                (receiver, args) -> {
                    if (!(receiver instanceof VmClass)) {
                        throw new VmException("not a class");
                    }
                    String classDesc = ((VmClass) receiver).descriptor;
                    ConstructorTarget target = declaredConstructor(classDesc,
                            classArray(args[0]));
                    VmObject ctor = newPlaceholder("Ljava/lang/reflect/Constructor;");
                    ctor.putField(CTOR_TARGET, target);
                    return ctor;
                });
        builtin("Ljava/lang/reflect/Constructor;->newInstance([Ljava/lang/Object;)"
                + "Ljava/lang/Object;",
                (receiver, args) -> {
                    if (!(receiver instanceof VmObject)) {
                        throw new VmException("not a constructor");
                    }
                    Object target = ((VmObject) receiver).getField(CTOR_TARGET);
                    if (!(target instanceof ConstructorTarget)) {
                        throw new VmException("not a constructor");
                    }
                    return newInstanceTarget((ConstructorTarget) target,
                            objectArray(args[0]));
                });
        // Field reads observe the same stores as direct access (interpreter
        // statics, object fields, jar constants, and the reported SDK_INT);
        // missing fields fail lookup, as on device.
        builtin("Ljava/lang/Class;->getField(Ljava/lang/String;)Ljava/lang/reflect/Field;",
                (receiver, args) -> {
                    if (!(receiver instanceof VmClass)) {
                        throw new VmException("not a class");
                    }
                    FieldTarget target = declaredField(
                            ((VmClass) receiver).descriptor, argString(args, 0));
                    VmObject field = newPlaceholder("Ljava/lang/reflect/Field;");
                    field.putField(FIELD_TARGET, target);
                    return field;
                });
        builtin("Ljava/lang/reflect/Field;->get(Ljava/lang/Object;)Ljava/lang/Object;",
                (receiver, args) -> fieldGet(targetOf(receiver), args[0]));
        builtin("Ljava/lang/reflect/Field;->getInt(Ljava/lang/Object;)I",
                (receiver, args) -> {
                    Object value = fieldGet(targetOf(receiver), args[0]);
                    return value == null ? 0 : asInt(value);
                });
        // Suppressed exceptions record per receiver. Host pairs delegate so
        // stack traces stay complete; mixed pairs land in the side table
        // (model objects cannot cross into host calls).
        builtin("Ljava/lang/Throwable;->addSuppressed(Ljava/lang/Throwable;)V",
                (receiver, args) -> {
                    if (args[0] == null || isNullish(args[0])) {
                        throw new NullPointerException("null suppressed");
                    }
                    if (receiver instanceof java.lang.Throwable
                            && args[0] instanceof java.lang.Throwable) {
                        ((java.lang.Throwable) receiver)
                                .addSuppressed((java.lang.Throwable) args[0]);
                        return null;
                    }
                    List<Object> list = suppressed.get(receiver);
                    if (list == null) {
                        list = new ArrayList<>();
                        suppressed.put(receiver, list);
                    }
                    list.add(args[0]);
                    return null;
                });
        builtin("Ljava/lang/Throwable;->getSuppressed()[Ljava/lang/Throwable;",
                (receiver, args) -> {
                    List<Object> extra = suppressed.get(receiver);
                    if (receiver instanceof java.lang.Throwable) {
                        Object[] host =
                                ((java.lang.Throwable) receiver).getSuppressed();
                        if (extra == null || extra.isEmpty()) {
                            return boxReturn(host);
                        }
                        VmArray out = new VmArray("Ljava/lang/Throwable;",
                                host.length + extra.size());
                        for (int i = 0; i < host.length; i++) {
                            out.set(i, host[i]);
                        }
                        for (int i = 0; i < extra.size(); i++) {
                            out.set(host.length + i, extra.get(i));
                        }
                        return out;
                    }
                    if (extra == null) {
                        return new VmArray("Ljava/lang/Throwable;", 0);
                    }
                    VmArray out = new VmArray("Ljava/lang/Throwable;", extra.size());
                    for (int i = 0; i < extra.size(); i++) {
                        out.set(i, extra.get(i));
                    }
                    return out;
                });
        // Enum entry points serve both placeholder model objects and real
        // java.lang.Enum instances: ported module enums allocate real
        // constants on the host, while guest-only enums stay placeholders.
        builtin("Ljava/lang/Enum;->name()Ljava/lang/String;",
                (receiver, args) -> {
                    if (receiver instanceof java.lang.Enum) {
                        return ((java.lang.Enum<?>) receiver).name();
                    }
                    Object name = ((VmObject) receiver).getField("Ljava/lang/Enum;.name");
                    if (name == null) {
                        throw new VmException("enum without name");
                    }
                    return name;
                });
        builtin("Ljava/lang/Enum;->compareTo(Ljava/lang/Enum;)I",
                (receiver, args) -> {
                    if (receiver instanceof java.lang.Enum
                            && args[0] instanceof java.lang.Enum) {
                        return Integer.compare(((java.lang.Enum<?>) receiver).ordinal(),
                                ((java.lang.Enum<?>) args[0]).ordinal());
                    }
                    Object left = ((VmObject) receiver).getField("Ljava/lang/Enum;.ordinal");
                    Object right = ((VmObject) args[0]).getField("Ljava/lang/Enum;.ordinal");
                    if (left == null || right == null) {
                        throw new VmException("enum without ordinal");
                    }
                    return Integer.compare(asInt(left), asInt(right));
                });
        builtin("Ljava/lang/Enum;->ordinal()I",
                (receiver, args) -> {
                    if (receiver instanceof java.lang.Enum) {
                        return ((java.lang.Enum<?>) receiver).ordinal();
                    }
                    Object ordinal = ((VmObject) receiver)
                            .getField("Ljava/lang/Enum;.ordinal");
                    if (ordinal == null) {
                        throw new VmException("enum without ordinal");
                    }
                    return asInt(ordinal);
                });
        builtin("Ljava/lang/Enum;->valueOf(Ljava/lang/Class;Ljava/lang/String;)"
                + "Ljava/lang/Object;",
                (receiver, args) -> {
                    VmClass cls = (VmClass) args[0];
                    String name = argString(args, 1);
                    DexClass dc = dexes.findClass(cls.descriptor);
                    if (dc != null) {
                        ensureInit(dc);
                    }
                    Object value = statics.get(cls.descriptor + "." + name);
                    if (value == null) {
                        throw new IllegalArgumentException(name);
                    }
                    return value;
                });

        builtin("Ljava/util/concurrent/Executors;->defaultThreadFactory()"
                + "Ljava/util/concurrent/ThreadFactory;",
                (receiver, args) -> java.util.concurrent.Executors.defaultThreadFactory());
        builtin("Ljava/util/concurrent/Executors;->newFixedThreadPool(I)"
                + "Ljava/util/concurrent/ExecutorService;",
                (receiver, args) -> new VmTask.DirectExecutorService());
        builtin("Ljava/util/concurrent/Executors;->newFixedThreadPool(ILjava/util/concurrent/"
                + "ThreadFactory;)Ljava/util/concurrent/ExecutorService;",
                (receiver, args) -> new VmTask.DirectExecutorService());
        builtin("Ljava/util/concurrent/Executor;->execute(Ljava/lang/Runnable;)V",
                (receiver, args) -> {
                    directPool(receiver).execute(wrapTask(args[0], "run"));
                    return null;
                });
        builtin("Ljava/util/concurrent/ExecutorService;->submit(Ljava/util/concurrent/Callable;)"
                + "Ljava/util/concurrent/Future;",
                (receiver, args) -> {
                    VmTask.DirectExecutorService pool = directPool(receiver);
                    if (args[0] instanceof VmObject) {
                        VmTask job = VmTask.runnable(Interpreter.this, args[0], "call");
                        Object value = null;
                        Throwable failure = null;
                        try {
                            value = job.call();
                        } catch (RuntimeException | Error e) {
                            failure = e;
                        }
                        return new VmTask.DoneFuture(value, failure);
                    }
                    return pool.submit((java.util.concurrent.Callable<Object>) args[0]);
                });
        builtin("Ljava/util/concurrent/ExecutorService;->submit(Ljava/lang/Runnable;)"
                + "Ljava/util/concurrent/Future;",
                (receiver, args) -> submitInline(receiver, args[0], null));
        builtin("Ljava/util/concurrent/ExecutorService;->submit(Ljava/lang/Runnable;"
                + "Ljava/lang/Object;)Ljava/util/concurrent/Future;",
                (receiver, args) -> submitInline(receiver, args[0], args[1]));
        builtin("Ljava/util/concurrent/Future;->get()Ljava/lang/Object;",
                (receiver, args) -> {
                    try {
                        return ((java.util.concurrent.Future<Object>) receiver).get();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new VmException("interrupted");
                    } catch (java.util.concurrent.ExecutionException e) {
                        Throwable cause = e.getCause();
                        if (cause instanceof RuntimeException) {
                            throw (RuntimeException) cause;
                        }
                        throw new VmException("task failed: " + cause);
                    }
                });
        builtin("Ljava/util/concurrent/Future;->get(JLjava/util/concurrent/TimeUnit;)"
                + "Ljava/lang/Object;",
                (receiver, args) -> {
                    try {
                        return ((java.util.concurrent.Future<Object>) receiver).get();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new VmException("interrupted");
                    } catch (java.util.concurrent.ExecutionException e) {
                        Throwable cause = e.getCause();
                        if (cause instanceof RuntimeException) {
                            throw (RuntimeException) cause;
                        }
                        throw new VmException("task failed: " + cause);
                    }
                });
        builtin("Ljava/util/concurrent/Future;->isDone()Z",
                (receiver, args) -> bool(
                        ((java.util.concurrent.Future<Object>) receiver).isDone()));
        builtin("Ljava/util/concurrent/Future;->isCancelled()Z",
                (receiver, args) -> bool(
                        ((java.util.concurrent.Future<Object>) receiver).isCancelled()));
        builtin("Ljava/util/concurrent/Future;->cancel(Z)Z",
                (receiver, args) -> bool(
                        ((java.util.concurrent.Future<Object>) receiver)
                                .cancel(argInt(args, 0) != 0)));

        builtin("Ljava/lang/Thread;->currentThread()Ljava/lang/Thread;",
                (receiver, args) -> Thread.currentThread());
        builtin("Ljava/lang/Thread;->getName()Ljava/lang/String;",
                (receiver, args) -> ((Thread) receiver).getName());
        builtin("Ljava/lang/Thread;->setName(Ljava/lang/String;)V",
                (receiver, args) -> {
                    ((Thread) receiver).setName(argString(args, 0));
                    return null;
                });
        builtin("Ljava/lang/Thread;->getId()J",
                (receiver, args) -> ((Thread) receiver).getId());
        builtin("Ljava/lang/Thread;->isAlive()Z",
                (receiver, args) -> bool(((Thread) receiver).isAlive()));
        builtin("Ljava/lang/Thread;->isInterrupted()Z",
                (receiver, args) -> bool(((Thread) receiver).isInterrupted()));
        builtin("Ljava/lang/Thread;->interrupted()Z",
                (receiver, args) -> bool(Thread.interrupted()));
        builtin("Ljava/lang/Thread;->getPriority()I",
                (receiver, args) -> ((Thread) receiver).getPriority());
        builtin("Ljava/lang/Thread;->setPriority(I)V",
                (receiver, args) -> {
                    ((Thread) receiver).setPriority(argInt(args, 0));
                    return null;
                });
        // No systrace runs headless; sections are no-ops, tracing off.
        builtin("Landroid/os/Trace;->beginSection(Ljava/lang/String;)V",
                (receiver, args) -> null);
        builtin("Landroid/os/Trace;->endSection()V",
                (receiver, args) -> null);
        builtin("Landroid/os/Trace;->isEnabled()Z",
                (receiver, args) -> 0);
        // The host stack carries no DEX frames, so Kotlin's null-check
        // message builder (which scans the stack for its own frames)
        // would walk off the end. Synthesize the frames it needs from
        // the trace ring — the Intrinsics hop plus the newest DEX
        // frames — so the message names the true dex culprit instead
        // of crashing.
        builtin("Ljava/lang/Thread;->getStackTrace()[Ljava/lang/StackTraceElement;",
                (receiver, args) -> {
                    List<StackTraceElement> frames = new ArrayList<>();
                    frames.add(new StackTraceElement("kotlin.jvm.internal.Intrinsics",
                            "checkNotNullParameter", "Intrinsics.java", -1));
                    List<String> ring = lastTrace();
                    for (int i = ring.size() - 1; i >= 0 && frames.size() < 32; i--) {
                        String line = ring.get(i);
                        int dot = line.indexOf('.');
                        int paren = line.indexOf('(', dot);
                        if (dot < 1 || paren < 0) {
                            continue;
                        }
                        String owner = line.substring(0, dot);
                        if (!owner.startsWith("L") || !owner.endsWith(";")) {
                            continue;
                        }
                        frames.add(new StackTraceElement(dots(owner),
                                line.substring(dot + 1, paren), null, -1));
                    }
                    VmArray out = new VmArray("Ljava/lang/StackTraceElement;",
                            frames.size());
                    for (int i = 0; i < frames.size(); i++) {
                        out.set(i, frames.get(i));
                    }
                    return out;
                });
        // Reference referents live under one shared key: subclasses share
        // the base implementation without redeclaring the field.
        builtin("Ljava/lang/ref/Reference;->get()Ljava/lang/Object;",
                (receiver, args) -> ((VmObject) receiver)
                        .getField("Ljava/lang/ref/Reference;.referent"));
        builtin("Ljava/lang/ref/Reference;->clear()V",
                (receiver, args) -> {
                    ((VmObject) receiver)
                            .putField("Ljava/lang/ref/Reference;.referent", null);
                    return null;
                });
        builtin("Ljava/lang/ref/Reference;->isEnqueued()Z",
                (receiver, args) -> 0);
        builtin("Ljava/lang/ref/ReferenceQueue;->poll()Ljava/lang/ref/Reference;",
                (receiver, args) -> null);
        builtin("Ljava/lang/ref/WeakReference;-><init>(Ljava/lang/Object;)V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField(
                            "Ljava/lang/ref/Reference;.referent", args[0]);
                    return null;
                });
        builtin("Ljava/lang/ref/SoftReference;-><init>(Ljava/lang/Object;)V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField(
                            "Ljava/lang/ref/Reference;.referent", args[0]);
                    return null;
                });
        builtin("Ljava/lang/ref/PhantomReference;-><init>(Ljava/lang/Object;"
                + "Ljava/lang/ref/ReferenceQueue;)V",
                (receiver, args) -> {
                    ((VmObject) receiver).putField(
                            "Ljava/lang/ref/Reference;.referent", args[0]);
                    return null;
                });

        builtin("Ljava/util/HashMap;-><init>()V",
                (receiver, args) -> null);
        builtin("Ljava/util/HashMap;-><init>(I)V",
                (receiver, args) -> null);
        builtin("Ljava/util/HashMap;-><init>(IF)V",
                (receiver, args) -> null);
        builtin("Ljava/util/HashMap;-><init>(Ljava/util/Map;)V",
                (receiver, args) -> {
                    ((java.util.HashMap<Object, Object>) receiver)
                            .putAll((java.util.Map<Object, Object>) args[0]);
                    return null;
                });
        builtin("Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)"
                + "Ljava/lang/Object;",
                (receiver, args) -> ((java.util.HashMap<Object, Object>) receiver)
                        .put(args[0], args[1]));
        builtin("Ljava/util/HashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;",
                (receiver, args) -> ((java.util.HashMap<Object, Object>) receiver)
                        .get(args[0]));
        builtin("Ljava/util/HashMap;->remove(Ljava/lang/Object;)Ljava/lang/Object;",
                (receiver, args) -> ((java.util.HashMap<Object, Object>) receiver)
                        .remove(args[0]));
        builtin("Ljava/util/HashMap;->containsKey(Ljava/lang/Object;)Z",
                (receiver, args) -> bool(
                        ((java.util.HashMap<Object, Object>) receiver).containsKey(args[0])));
        builtin("Ljava/util/HashMap;->containsValue(Ljava/lang/Object;)Z",
                (receiver, args) -> bool(
                        ((java.util.HashMap<Object, Object>) receiver).containsValue(args[0])));
        builtin("Ljava/util/HashMap;->size()I",
                (receiver, args) -> ((java.util.HashMap<Object, Object>) receiver).size());
        builtin("Ljava/util/HashMap;->isEmpty()Z",
                (receiver, args) -> bool(
                        ((java.util.HashMap<Object, Object>) receiver).isEmpty()));
        builtin("Ljava/util/HashMap;->clear()V",
                (receiver, args) -> {
                    ((java.util.HashMap<Object, Object>) receiver).clear();
                    return null;
                });
        builtin("Ljava/util/HashMap;->keySet()Ljava/util/Set;",
                (receiver, args) -> ((java.util.HashMap<Object, Object>) receiver).keySet());
        builtin("Ljava/util/HashMap;->values()Ljava/util/Collection;",
                (receiver, args) -> ((java.util.HashMap<Object, Object>) receiver).values());
        builtin("Ljava/util/HashMap;->entrySet()Ljava/util/Set;",
                (receiver, args) -> ((java.util.HashMap<Object, Object>) receiver).entrySet());
        builtin("Ljava/util/HashMap;->putAll(Ljava/util/Map;)V",
                (receiver, args) -> {
                    ((java.util.HashMap<Object, Object>) receiver)
                            .putAll((java.util.Map<Object, Object>) args[0]);
                    return null;
                });
        builtin("Ljava/util/HashMap;->getOrDefault(Ljava/lang/Object;Ljava/lang/Object;)"
                + "Ljava/lang/Object;",
                (receiver, args) -> ((java.util.HashMap<Object, Object>) receiver)
                        .getOrDefault(args[0], args[1]));
        builtin("Ljava/util/HashMap;->putIfAbsent(Ljava/lang/Object;Ljava/lang/Object;)"
                + "Ljava/lang/Object;",
                (receiver, args) -> ((java.util.HashMap<Object, Object>) receiver)
                        .putIfAbsent(args[0], args[1]));
        builtin("Ljava/util/HashMap;->clone()Ljava/lang/Object;",
                (receiver, args) -> ((java.util.HashMap<Object, Object>) receiver).clone());
        // Bundles are string-keyed maps backed by a host HashMap (see
        // createInstance); typed accessors default like the device.
        builtin("Landroid/os/Bundle;-><init>()V",
                (receiver, args) -> null);
        builtin("Landroid/os/Bundle;-><init>(I)V",
                (receiver, args) -> null);
        builtin("Landroid/os/Bundle;-><init>(Landroid/os/Bundle;)V",
                (receiver, args) -> {
                    if (args[0] instanceof java.util.Map) {
                        ((java.util.HashMap<Object, Object>) receiver)
                                .putAll((java.util.Map<?, ?>) args[0]);
                    }
                    return null;
                });
        builtin("Landroid/os/Bundle;->putString(Ljava/lang/String;Ljava/lang/String;)V",
                (receiver, args) -> {
                    ((java.util.HashMap<Object, Object>) receiver)
                            .put(argString(args, 0), argString(args, 1));
                    return null;
                });
        builtin("Landroid/os/Bundle;->getString(Ljava/lang/String;)Ljava/lang/String;",
                (receiver, args) -> {
                    Object value = ((java.util.HashMap<Object, Object>) receiver)
                            .get(argString(args, 0));
                    return value == null ? null : (String) value;
                });
        builtin("Landroid/os/Bundle;->putInt(Ljava/lang/String;I)V",
                (receiver, args) -> {
                    ((java.util.HashMap<Object, Object>) receiver)
                            .put(argString(args, 0), asInt(args[1]));
                    return null;
                });
        builtin("Landroid/os/Bundle;->getInt(Ljava/lang/String;)I",
                (receiver, args) -> {
                    Object value = ((java.util.HashMap<Object, Object>) receiver)
                            .get(argString(args, 0));
                    return value == null ? 0 : ((Number) value).intValue();
                });
        builtin("Landroid/os/Bundle;->getInt(Ljava/lang/String;I)I",
                (receiver, args) -> {
                    Object value = ((java.util.HashMap<Object, Object>) receiver)
                            .get(argString(args, 0));
                    return value == null ? asInt(args[1]) : ((Number) value).intValue();
                });
        builtin("Landroid/os/Bundle;->putBoolean(Ljava/lang/String;Z)V",
                (receiver, args) -> {
                    ((java.util.HashMap<Object, Object>) receiver)
                            .put(argString(args, 0), asInt(args[1]) != 0);
                    return null;
                });
        builtin("Landroid/os/Bundle;->getBoolean(Ljava/lang/String;)Z",
                (receiver, args) -> {
                    Object value = ((java.util.HashMap<Object, Object>) receiver)
                            .get(argString(args, 0));
                    return value == null ? 0 : bool(Boolean.TRUE.equals(value));
                });
        builtin("Landroid/os/Bundle;->putBundle(Ljava/lang/String;Landroid/os/Bundle;)V",
                (receiver, args) -> {
                    ((java.util.HashMap<Object, Object>) receiver)
                            .put(argString(args, 0), args[1]);
                    return null;
                });
        builtin("Landroid/os/Bundle;->getBundle(Ljava/lang/String;)Landroid/os/Bundle;",
                (receiver, args) -> ((java.util.HashMap<Object, Object>) receiver)
                        .get(argString(args, 0)));
        builtin("Landroid/os/Bundle;->containsKey(Ljava/lang/String;)Z",
                (receiver, args) -> bool(((java.util.HashMap<Object, Object>) receiver)
                        .containsKey(argString(args, 0))));
        builtin("Landroid/os/Bundle;->remove(Ljava/lang/String;)V",
                (receiver, args) -> {
                    ((java.util.HashMap<Object, Object>) receiver)
                            .remove(argString(args, 0));
                    return null;
                });

        builtin("Ljava/util/Map;->get(Ljava/lang/Object;)Ljava/lang/Object;",
                (receiver, args) -> ((java.util.Map<Object, Object>) receiver).get(args[0]));
        builtin("Ljava/util/Map;->put(Ljava/lang/Object;Ljava/lang/Object;)"
                + "Ljava/lang/Object;",
                (receiver, args) -> ((java.util.Map<Object, Object>) receiver)
                        .put(args[0], args[1]));
        builtin("Ljava/util/Map;->remove(Ljava/lang/Object;)Ljava/lang/Object;",
                (receiver, args) -> ((java.util.Map<Object, Object>) receiver).remove(args[0]));
        builtin("Ljava/util/Map;->containsKey(Ljava/lang/Object;)Z",
                (receiver, args) -> bool(
                        ((java.util.Map<Object, Object>) receiver).containsKey(args[0])));
        builtin("Ljava/util/Map;->containsValue(Ljava/lang/Object;)Z",
                (receiver, args) -> bool(
                        ((java.util.Map<Object, Object>) receiver).containsValue(args[0])));
        builtin("Ljava/util/Map;->size()I",
                (receiver, args) -> ((java.util.Map<Object, Object>) receiver).size());
        builtin("Ljava/util/Map;->isEmpty()Z",
                (receiver, args) -> bool(((java.util.Map<Object, Object>) receiver).isEmpty()));
        builtin("Ljava/util/Map;->clear()V",
                (receiver, args) -> {
                    ((java.util.Map<Object, Object>) receiver).clear();
                    return null;
                });
        builtin("Ljava/util/Map;->keySet()Ljava/util/Set;",
                (receiver, args) -> ((java.util.Map<Object, Object>) receiver).keySet());
        builtin("Ljava/util/Map;->values()Ljava/util/Collection;",
                (receiver, args) -> ((java.util.Map<Object, Object>) receiver).values());
        builtin("Ljava/util/Map;->entrySet()Ljava/util/Set;",
                (receiver, args) -> ((java.util.Map<Object, Object>) receiver).entrySet());
        // Entries live in package-private impls (HashMap$Node) that
        // reflective invoke cannot reach; the table calls through the
        // public interface instead, and model entries run their dex body.
        builtin("Ljava/util/Map$Entry;->getKey()Ljava/lang/Object;",
                (receiver, args) -> receiver instanceof VmObject
                        ? dispatchDex(receiver, "getKey",
                                Collections.<String>emptyList(), "Ljava/lang/Object;",
                                new Object[0])
                        : ((java.util.Map.Entry<Object, Object>) receiver).getKey());
        builtin("Ljava/util/Map$Entry;->getValue()Ljava/lang/Object;",
                (receiver, args) -> receiver instanceof VmObject
                        ? dispatchDex(receiver, "getValue",
                                Collections.<String>emptyList(), "Ljava/lang/Object;",
                                new Object[0])
                        : ((java.util.Map.Entry<Object, Object>) receiver).getValue());
        builtin("Ljava/util/Map$Entry;->setValue(Ljava/lang/Object;)Ljava/lang/Object;",
                (receiver, args) -> receiver instanceof VmObject
                        ? dispatchDex(receiver, "setValue",
                                Collections.singletonList("Ljava/lang/Object;"),
                                "Ljava/lang/Object;", new Object[]{args[0]})
                        : ((java.util.Map.Entry<Object, Object>) receiver)
                                .setValue(args[0]));
        builtin("Ljava/util/Map;->putAll(Ljava/util/Map;)V",
                (receiver, args) -> {
                    ((java.util.Map<Object, Object>) receiver)
                            .putAll((java.util.Map<Object, Object>) args[0]);
                    return null;
                });
        builtin("Ljava/util/Map;->getOrDefault(Ljava/lang/Object;Ljava/lang/Object;)"
                + "Ljava/lang/Object;",
                (receiver, args) -> {
                    if (receiver instanceof VmObject) {
                        Object hit = dispatchDex(receiver, "containsKey",
                                Collections.singletonList("Ljava/lang/Object;"), "Z",
                                new Object[]{args[0]});
                        if (asInt(hit) != 0) {
                            return dispatchDex(receiver, "get",
                                    Collections.singletonList("Ljava/lang/Object;"),
                                    "Ljava/lang/Object;", new Object[]{args[0]});
                        }
                        return args[1];
                    }
                    return ((java.util.Map<Object, Object>) receiver)
                            .getOrDefault(args[0], args[1]);
                });
        builtin("Ljava/util/Map;->putIfAbsent(Ljava/lang/Object;Ljava/lang/Object;)"
                + "Ljava/lang/Object;",
                (receiver, args) -> {
                    if (receiver instanceof VmObject) {
                        Object current = dispatchDex(receiver, "get",
                                Collections.singletonList("Ljava/lang/Object;"),
                                "Ljava/lang/Object;", new Object[]{args[0]});
                        if (current != null) {
                            return current;
                        }
                        return dispatchDex(receiver, "put",
                                Arrays.asList("Ljava/lang/Object;", "Ljava/lang/Object;"),
                                "Ljava/lang/Object;", new Object[]{args[0], args[1]});
                    }
                    return ((java.util.Map<Object, Object>) receiver)
                            .putIfAbsent(args[0], args[1]);
                });

        builtin("Ljava/util/ArrayList;-><init>()V",
                (receiver, args) -> null);
        builtin("Ljava/util/ArrayList;-><init>(I)V",
                (receiver, args) -> null);
        builtin("Ljava/util/ArrayList;-><init>(Ljava/util/Collection;)V",
                (receiver, args) -> {
                    ((java.util.ArrayList<Object>) receiver)
                            .addAll((java.util.Collection<Object>) args[0]);
                    return null;
                });
        builtin("Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z",
                (receiver, args) -> bool(
                        ((java.util.ArrayList<Object>) receiver).add(args[0])));
        builtin("Ljava/util/ArrayList;->add(ILjava/lang/Object;)V",
                (receiver, args) -> {
                    ((java.util.ArrayList<Object>) receiver).add(argInt(args, 0), args[1]);
                    return null;
                });
        builtin("Ljava/util/ArrayList;->get(I)Ljava/lang/Object;",
                (receiver, args) -> ((java.util.ArrayList<Object>) receiver).get(argInt(args, 0)));
        builtin("Ljava/util/ArrayList;->set(ILjava/lang/Object;)Ljava/lang/Object;",
                (receiver, args) -> ((java.util.ArrayList<Object>) receiver)
                        .set(argInt(args, 0), args[1]));
        builtin("Ljava/util/ArrayList;->remove(I)Ljava/lang/Object;",
                (receiver, args) -> ((java.util.ArrayList<Object>) receiver)
                        .remove(argInt(args, 0)));
        builtin("Ljava/util/ArrayList;->remove(Ljava/lang/Object;)Z",
                (receiver, args) -> bool(
                        ((java.util.ArrayList<Object>) receiver).remove(args[0])));
        builtin("Ljava/util/ArrayList;->size()I",
                (receiver, args) -> ((java.util.ArrayList<Object>) receiver).size());
        builtin("Ljava/util/ArrayList;->isEmpty()Z",
                (receiver, args) -> bool(
                        ((java.util.ArrayList<Object>) receiver).isEmpty()));
        builtin("Ljava/util/ArrayList;->clear()V",
                (receiver, args) -> {
                    ((java.util.ArrayList<Object>) receiver).clear();
                    return null;
                });
        builtin("Ljava/util/ArrayList;->contains(Ljava/lang/Object;)Z",
                (receiver, args) -> bool(
                        ((java.util.ArrayList<Object>) receiver).contains(args[0])));
        builtin("Ljava/util/ArrayList;->indexOf(Ljava/lang/Object;)I",
                (receiver, args) -> ((java.util.ArrayList<Object>) receiver).indexOf(args[0]));
        builtin("Ljava/util/ArrayList;->lastIndexOf(Ljava/lang/Object;)I",
                (receiver, args) -> ((java.util.ArrayList<Object>) receiver)
                        .lastIndexOf(args[0]));
        builtin("Ljava/util/ArrayList;->toArray()[Ljava/lang/Object;",
                (receiver, args) -> objectArray(
                        ((java.util.ArrayList<Object>) receiver).toArray()));
        builtin("Ljava/util/ArrayList;->iterator()Ljava/util/Iterator;",
                (receiver, args) -> ((java.util.ArrayList<Object>) receiver).iterator());
        builtin("Ljava/util/ArrayList;->listIterator()Ljava/util/ListIterator;",
                (receiver, args) -> ((java.util.ArrayList<Object>) receiver).listIterator());
        builtin("Ljava/util/ArrayList;->subList(II)Ljava/util/List;",
                (receiver, args) -> ((java.util.ArrayList<Object>) receiver)
                        .subList(argInt(args, 0), argInt(args, 1)));
        builtin("Ljava/util/ArrayList;->trimToSize()V",
                (receiver, args) -> {
                    ((java.util.ArrayList<Object>) receiver).trimToSize();
                    return null;
                });
        builtin("Ljava/util/ArrayList;->ensureCapacity(I)V",
                (receiver, args) -> {
                    ((java.util.ArrayList<Object>) receiver).ensureCapacity(argInt(args, 0));
                    return null;
                });

        builtin("Ljava/util/List;->add(Ljava/lang/Object;)Z",
                (receiver, args) -> bool(((java.util.List<Object>) receiver).add(args[0])));
        builtin("Ljava/util/List;->add(ILjava/lang/Object;)V",
                (receiver, args) -> {
                    ((java.util.List<Object>) receiver).add(argInt(args, 0), args[1]);
                    return null;
                });
        builtin("Ljava/util/List;->get(I)Ljava/lang/Object;",
                (receiver, args) -> ((java.util.List<Object>) receiver).get(argInt(args, 0)));
        builtin("Ljava/util/List;->set(ILjava/lang/Object;)Ljava/lang/Object;",
                (receiver, args) -> ((java.util.List<Object>) receiver)
                        .set(argInt(args, 0), args[1]));
        builtin("Ljava/util/List;->remove(I)Ljava/lang/Object;",
                (receiver, args) -> ((java.util.List<Object>) receiver).remove(argInt(args, 0)));
        builtin("Ljava/util/List;->remove(Ljava/lang/Object;)Z",
                (receiver, args) -> bool(((java.util.List<Object>) receiver).remove(args[0])));
        builtin("Ljava/util/List;->size()I",
                (receiver, args) -> ((java.util.List<Object>) receiver).size());
        builtin("Ljava/util/List;->isEmpty()Z",
                (receiver, args) -> bool(((java.util.List<Object>) receiver).isEmpty()));
        builtin("Ljava/util/List;->clear()V",
                (receiver, args) -> {
                    ((java.util.List<Object>) receiver).clear();
                    return null;
                });
        builtin("Ljava/util/List;->contains(Ljava/lang/Object;)Z",
                (receiver, args) -> bool(((java.util.List<Object>) receiver).contains(args[0])));
        builtin("Ljava/util/List;->indexOf(Ljava/lang/Object;)I",
                (receiver, args) -> ((java.util.List<Object>) receiver).indexOf(args[0]));
        builtin("Ljava/util/List;->lastIndexOf(Ljava/lang/Object;)I",
                (receiver, args) -> ((java.util.List<Object>) receiver).lastIndexOf(args[0]));
        builtin("Ljava/util/List;->toArray()[Ljava/lang/Object;",
                (receiver, args) -> objectArray(((java.util.List<Object>) receiver).toArray()));
        builtin("Ljava/util/List;->iterator()Ljava/util/Iterator;",
                (receiver, args) -> ((java.util.List<Object>) receiver).iterator());
        builtin("Ljava/util/List;->listIterator()Ljava/util/ListIterator;",
                (receiver, args) -> ((java.util.List<Object>) receiver).listIterator());
        builtin("Ljava/util/List;->subList(II)Ljava/util/List;",
                (receiver, args) -> ((java.util.List<Object>) receiver)
                        .subList(argInt(args, 0), argInt(args, 1)));

        builtin("Ljava/util/HashSet;-><init>()V",
                (receiver, args) -> null);
        builtin("Ljava/util/HashSet;-><init>(I)V",
                (receiver, args) -> null);
        builtin("Ljava/util/HashSet;-><init>(IF)V",
                (receiver, args) -> null);
        builtin("Ljava/util/HashSet;-><init>(Ljava/util/Collection;)V",
                (receiver, args) -> {
                    ((java.util.HashSet<Object>) receiver)
                            .addAll((java.util.Collection<Object>) args[0]);
                    return null;
                });
        builtin("Ljava/util/HashSet;->add(Ljava/lang/Object;)Z",
                (receiver, args) -> bool(((java.util.HashSet<Object>) receiver).add(args[0])));
        builtin("Ljava/util/HashSet;->remove(Ljava/lang/Object;)Z",
                (receiver, args) -> bool(((java.util.HashSet<Object>) receiver).remove(args[0])));
        builtin("Ljava/util/HashSet;->contains(Ljava/lang/Object;)Z",
                (receiver, args) -> bool(
                        ((java.util.HashSet<Object>) receiver).contains(args[0])));
        builtin("Ljava/util/HashSet;->size()I",
                (receiver, args) -> ((java.util.HashSet<Object>) receiver).size());
        builtin("Ljava/util/HashSet;->isEmpty()Z",
                (receiver, args) -> bool(((java.util.HashSet<Object>) receiver).isEmpty()));
        builtin("Ljava/util/HashSet;->clear()V",
                (receiver, args) -> {
                    ((java.util.HashSet<Object>) receiver).clear();
                    return null;
                });
        builtin("Ljava/util/HashSet;->iterator()Ljava/util/Iterator;",
                (receiver, args) -> ((java.util.HashSet<Object>) receiver).iterator());

        builtin("Ljava/util/Set;->add(Ljava/lang/Object;)Z",
                (receiver, args) -> bool(((java.util.Set<Object>) receiver).add(args[0])));
        builtin("Ljava/util/Set;->remove(Ljava/lang/Object;)Z",
                (receiver, args) -> bool(((java.util.Set<Object>) receiver).remove(args[0])));
        builtin("Ljava/util/Set;->contains(Ljava/lang/Object;)Z",
                (receiver, args) -> bool(((java.util.Set<Object>) receiver).contains(args[0])));
        builtin("Ljava/util/Set;->size()I",
                (receiver, args) -> ((java.util.Set<Object>) receiver).size());
        builtin("Ljava/util/Set;->isEmpty()Z",
                (receiver, args) -> bool(((java.util.Set<Object>) receiver).isEmpty()));
        builtin("Ljava/util/Set;->clear()V",
                (receiver, args) -> {
                    ((java.util.Set<Object>) receiver).clear();
                    return null;
                });
        builtin("Ljava/util/Set;->iterator()Ljava/util/Iterator;",
                (receiver, args) -> ((java.util.Set<Object>) receiver).iterator());

        builtin("Ljava/util/Collection;->iterator()Ljava/util/Iterator;",
                (receiver, args) -> ((java.util.Collection<Object>) receiver).iterator());
        // Kotlin/stdlib code often types host lists as bare Iterable.
        builtin("Ljava/lang/Iterable;->iterator()Ljava/util/Iterator;",
                (receiver, args) -> ((java.lang.Iterable<Object>) receiver).iterator());
        builtin("Ljava/util/Collection;->size()I",
                (receiver, args) -> ((java.util.Collection<Object>) receiver).size());
        builtin("Ljava/util/Collection;->isEmpty()Z",
                (receiver, args) -> bool(((java.util.Collection<Object>) receiver).isEmpty()));
        builtin("Ljava/util/Collection;->clear()V",
                (receiver, args) -> {
                    ((java.util.Collection<Object>) receiver).clear();
                    return null;
                });
        builtin("Ljava/util/Collection;->contains(Ljava/lang/Object;)Z",
                (receiver, args) -> bool(
                        ((java.util.Collection<Object>) receiver).contains(args[0])));
        builtin("Ljava/util/Collection;->toArray()[Ljava/lang/Object;",
                (receiver, args) -> objectArray(
                        ((java.util.Collection<Object>) receiver).toArray()));

        builtin("Ljava/util/Iterator;->hasNext()Z",
                (receiver, args) -> bool(((java.util.Iterator<Object>) receiver).hasNext()));
        builtin("Ljava/util/Iterator;->next()Ljava/lang/Object;",
                (receiver, args) -> ((java.util.Iterator<Object>) receiver).next());
        builtin("Ljava/util/Iterator;->remove()V",
                (receiver, args) -> {
                    ((java.util.Iterator<Object>) receiver).remove();
                    return null;
                });
        builtin("Ljava/util/ListIterator;->hasNext()Z",
                (receiver, args) -> bool(((java.util.ListIterator<Object>) receiver).hasNext()));
        builtin("Ljava/util/ListIterator;->next()Ljava/lang/Object;",
                (receiver, args) -> ((java.util.ListIterator<Object>) receiver).next());
        builtin("Ljava/util/ListIterator;->remove()V",
                (receiver, args) -> {
                    ((java.util.ListIterator<Object>) receiver).remove();
                    return null;
                });
        builtin("Ljava/util/ListIterator;->hasPrevious()Z",
                (receiver, args) -> bool(
                        ((java.util.ListIterator<Object>) receiver).hasPrevious()));
        builtin("Ljava/util/ListIterator;->previous()Ljava/lang/Object;",
                (receiver, args) -> ((java.util.ListIterator<Object>) receiver).previous());
        builtin("Ljava/util/ListIterator;->previousIndex()I",
                (receiver, args) -> ((java.util.ListIterator<Object>) receiver).previousIndex());
        builtin("Ljava/util/ListIterator;->nextIndex()I",
                (receiver, args) -> ((java.util.ListIterator<Object>) receiver).nextIndex());

        builtin("Ljava/util/Collections;->emptyList()Ljava/util/List;",
                (receiver, args) -> java.util.Collections.emptyList());
        builtin("Ljava/util/Collections;->emptySet()Ljava/util/Set;",
                (receiver, args) -> java.util.Collections.emptySet());
        builtin("Ljava/util/Collections;->emptyMap()Ljava/util/Map;",
                (receiver, args) -> java.util.Collections.emptyMap());
        builtin("Ljava/util/Collections;->singleton(Ljava/lang/Object;)Ljava/util/Set;",
                (receiver, args) -> java.util.Collections.singleton(args[0]));
        builtin("Ljava/util/Collections;->singletonList(Ljava/lang/Object;)Ljava/util/List;",
                (receiver, args) -> java.util.Collections.singletonList(args[0]));
        builtin("Ljava/util/Collections;->singletonMap(Ljava/lang/Object;Ljava/lang/Object;)"
                + "Ljava/util/Map;",
                (receiver, args) -> java.util.Collections.singletonMap(args[0], args[1]));
        builtin("Ljava/util/Collections;->unmodifiableList(Ljava/util/List;)Ljava/util/List;",
                (receiver, args) -> java.util.Collections
                        .unmodifiableList((java.util.List<Object>) args[0]));
        builtin("Ljava/util/Collections;->unmodifiableSet(Ljava/util/Set;)Ljava/util/Set;",
                (receiver, args) -> java.util.Collections
                        .unmodifiableSet((java.util.Set<Object>) args[0]));
        builtin("Ljava/util/Collections;->unmodifiableMap(Ljava/util/Map;)Ljava/util/Map;",
                (receiver, args) -> java.util.Collections
                        .unmodifiableMap((java.util.Map<Object, Object>) args[0]));
        builtin("Ljava/util/Collections;->synchronizedList(Ljava/util/List;)Ljava/util/List;",
                (receiver, args) -> java.util.Collections
                        .synchronizedList((java.util.List<Object>) args[0]));
        builtin("Ljava/util/Collections;->synchronizedSet(Ljava/util/Set;)Ljava/util/Set;",
                (receiver, args) -> java.util.Collections
                        .synchronizedSet((java.util.Set<Object>) args[0]));
        builtin("Ljava/util/Collections;->synchronizedMap(Ljava/util/Map;)Ljava/util/Map;",
                (receiver, args) -> java.util.Collections
                        .synchronizedMap((java.util.Map<Object, Object>) args[0]));
        builtin("Ljava/util/Collections;->sort(Ljava/util/List;)V",
                (receiver, args) -> {
                    java.util.Collections.sort((java.util.List<Comparable<Object>>) args[0]);
                    return null;
                });
        builtin("Ljava/util/Collections;->reverse(Ljava/util/List;)V",
                (receiver, args) -> {
                    java.util.Collections.reverse((java.util.List<Object>) args[0]);
                    return null;
                });
        builtin("Ljava/util/Collections;->shuffle(Ljava/util/List;)V",
                (receiver, args) -> {
                    java.util.Collections.shuffle((java.util.List<Object>) args[0]);
                    return null;
                });
        builtin("Ljava/util/Collections;->binarySearch(Ljava/util/List;Ljava/lang/Object;)I",
                (receiver, args) -> java.util.Collections.binarySearch(
                        (java.util.List<Comparable<Object>>) args[0], args[1]));
        builtin("Ljava/util/Collections;->fill(Ljava/util/List;Ljava/lang/Object;)V",
                (receiver, args) -> {
                    java.util.Collections.fill((java.util.List<Object>) args[0], args[1]);
                    return null;
                });
        builtin("Ljava/util/Collections;->copy(Ljava/util/List;Ljava/util/List;)V",
                (receiver, args) -> {
                    java.util.Collections.copy((java.util.List<Object>) args[0],
                            (java.util.List<Object>) args[1]);
                    return null;
                });
        builtin("Ljava/util/Collections;->swap(Ljava/util/List;II)V",
                (receiver, args) -> {
                    java.util.Collections.swap((java.util.List<Object>) args[0],
                            argInt(args, 1), argInt(args, 2));
                    return null;
                });
        builtin("Ljava/util/Collections;->nCopies(ILjava/lang/Object;)Ljava/util/List;",
                (receiver, args) -> java.util.Collections.nCopies(argInt(args, 0), args[1]));
        builtin("Ljava/util/Collections;->min(Ljava/util/Collection;)Ljava/lang/Object;",
                (receiver, args) -> java.util.Collections
                        .min((java.util.Collection<Comparable<Object>>) args[0]));
        builtin("Ljava/util/Collections;->max(Ljava/util/Collection;)Ljava/lang/Object;",
                (receiver, args) -> java.util.Collections
                        .max((java.util.Collection<Comparable<Object>>) args[0]));

        builtin("Ljava/util/Arrays;->asList([Ljava/lang/Object;)Ljava/util/List;",
                (receiver, args) -> {
                    Object array = args[0];
                    if (array instanceof VmArray) {
                        VmArray vm = (VmArray) array;
                        Object[] values = new Object[vm.length()];
                        for (int i = 0; i < values.length; i++) {
                            values[i] = vm.get(i);
                        }
                        return java.util.Arrays.asList(values);
                    }
                    return java.util.Arrays.asList((Object[]) array);
                });
    }

    private Runnable wrapTask(Object task, String method) {
        if (task instanceof Runnable) {
            return (Runnable) task;
        }
        return VmTask.runnable(this, task, method);
    }

    private VmTask.DirectExecutorService directPool(Object receiver) {
        if (!(receiver instanceof VmTask.DirectExecutorService)) {
            throw new VmException("threaded pools unsupported");
        }
        return (VmTask.DirectExecutorService) receiver;
    }

    private Object submitInline(Object receiver, Object task, Object preset) {
        VmTask.DirectExecutorService pool = directPool(receiver);
        if (task instanceof VmObject) {
            VmTask job = VmTask.runnable(this, task, "run");
            try {
                job.call();
                return new VmTask.DoneFuture(preset, null);
            } catch (RuntimeException | Error e) {
                return new VmTask.DoneFuture(null, e);
            }
        }
        if (preset == null) {
            return pool.submit((Runnable) task);
        }
        return pool.submit((Runnable) task, preset);
    }

    private static String classNameOf(String descriptor) {
        if (descriptor.startsWith("L") && descriptor.endsWith(";")) {
            return dots(descriptor);
        }
        if (descriptor.startsWith("[")) {
            return "[" + classNameOf(descriptor.substring(1));
        }
        return descriptor;
    }

    private static void arrayCopy(Object[] args) {
        if (!(args[0] instanceof VmArray) || !(args[2] instanceof VmArray)) {
            throw new VmException("arraycopy of non-array");
        }
        VmArray src = (VmArray) args[0];
        VmArray dst = (VmArray) args[2];
        int srcPos = ((Number) args[1]).intValue();
        int dstPos = ((Number) args[3]).intValue();
        int length = ((Number) args[4]).intValue();
        if (srcPos < 0 || dstPos < 0 || length < 0
                || srcPos + length > src.length() || dstPos + length > dst.length()) {
            throw new ArrayIndexOutOfBoundsException(length);
        }
        if (src == dst && srcPos < dstPos) {
            for (int i = length - 1; i >= 0; i--) {
                dst.set(dstPos + i, src.get(srcPos + i));
            }
        } else {
            for (int i = 0; i < length; i++) {
                dst.set(dstPos + i, src.get(srcPos + i));
            }
        }
    }

    private void sortRange(Object arrayValue, int from, int to, Object comparator) {
        VmArray array = checkedArray(arrayValue);
        if (from > to) {
            throw new IllegalArgumentException(from + " > " + to);
        }
        if (from < 0 || to > array.length()) {
            throw new ArrayIndexOutOfBoundsException(
                    from < 0 ? from : to);
        }
        for (int i = from + 1; i < to; i++) {
            Object current = array.get(i);
            int j = i - 1;
            while (j >= from && compareObjects(comparator, array.get(j), current) > 0) {
                array.set(j + 1, array.get(j));
                j--;
            }
            array.set(j + 1, current);
        }
    }

    @SuppressWarnings("unchecked")
    private int compareObjects(Object comparator, Object left, Object right) {
        if (comparator == null) {
            throw new NullPointerException("null comparator");
        }
        if (comparator instanceof VmObject) {
            VmObject object = (VmObject) comparator;
            MethodRef ref = MethodRef.parse("Ljava/util/Comparator;->compare("
                    + "Ljava/lang/Object;Ljava/lang/Object;)I");
            Resolved resolved = resolve(object.type, "Ljava/util/Comparator;", ref, true);
            if (resolved.dexMethod != null) {
                return asInt(invoke(resolved.dexHome, resolved.dexMethod, object,
                        left, right));
            }
            if (resolved.builtin != null) {
                return asInt(resolved.builtin.invoke(object,
                        new Object[]{left, right}));
            }
            throw new VmException("unimplemented comparator: " + object.type.type);
        }
        if (comparator instanceof java.util.Comparator) {
            return ((java.util.Comparator<Object>) comparator).compare(left, right);
        }
        throw new VmException("not a comparator");
    }

    private VmLooper mainLooper() {
        if (mainLooper == null) {
            mainLooper = new VmLooper();
        }
        return mainLooper;
    }

    private VmQueue mainQueue() {
        if (mainQueue == null) {
            mainQueue = new VmQueue();
        }
        return mainQueue;
    }

    private void initHandler(VmObject handler, Object looper, Object callback) {
        handler.putField("Landroid/os/Handler;.mLooper",
                looper == null ? mainLooper() : looper);
        handler.putField("Landroid/os/Handler;.mCallback", callback);
    }

    private VmObject message(int what) {
        VmObject message = newPlaceholder("Landroid/os/Message;");
        message.putField("Landroid/os/Message;.what", what);
        return message;
    }

    private static VmObject messageTarget(VmObject message, Object target) {
        message.putField("Landroid/os/Message;.target", target);
        return message;
    }

    /** Runs {@code handleMessage} when the receiver defines it (else no-op). */
    private void dispatchMessage(VmObject handler, Object message) {
        for (DexMethod candidate : handler.type.methods) {
            if (candidate.name.equals("handleMessage")
                    && candidate.parameterTypes.size() == 1) {
                execute(handler.type, candidate, handler, new Object[]{message});
                return;
            }
        }
    }

    private static boolean updaterCas(FieldUpdater updater, VmObject target,
            Object expect, Object update) {
        Object current = target.getField(updater.key());
        if (current == expect) {
            target.putField(updater.key(), update);
            return true;
        }
        return false;
    }

    private static boolean longCas(FieldUpdater updater, VmObject target, long expect,
            long update) {
        Object have = target.getField(updater.key());
        long current = have == null ? 0L : asLong(have);
        if (current != expect) {
            return false;
        }
        target.putField(updater.key(), update);
        return true;
    }

    private static long longAdd(FieldUpdater updater, VmObject target, long delta,
            boolean retNew) {
        Object have = target.getField(updater.key());
        long old = have == null ? 0L : asLong(have);
        target.putField(updater.key(), old + delta);
        return retNew ? old + delta : old;
    }

    private static boolean intCas(FieldUpdater updater, VmObject target, int expect,
            int update) {
        Object have = target.getField(updater.key());
        int current = have == null ? 0 : asInt(have);
        if (current != expect) {
            return false;
        }
        target.putField(updater.key(), update);
        return true;
    }

    private static int intAdd(FieldUpdater updater, VmObject target, int delta,
            boolean retNew) {
        Object have = target.getField(updater.key());
        int old = have == null ? 0 : asInt(have);
        target.putField(updater.key(), old + delta);
        return retNew ? old + delta : old;
    }

    private static int parseColor(String text) {
        if (text.startsWith("#")) {
            String hex = text.substring(1);
            switch (hex.length()) {
                case 3:
                    return 0xFF000000 | duplicate(hex.charAt(0)) << 16
                            | duplicate(hex.charAt(1)) << 8 | duplicate(hex.charAt(2));
                case 4:
                    return duplicate(hex.charAt(0)) << 24 | duplicate(hex.charAt(1)) << 16
                            | duplicate(hex.charAt(2)) << 8 | duplicate(hex.charAt(3));
                case 6:
                    return (int) (0xFF000000L | Long.parseLong(hex, 16));
                case 8:
                    return (int) Long.parseLong(hex, 16);
                default:
                    throw new IllegalArgumentException("unknown color: " + text);
            }
        }
        switch (text.toLowerCase(java.util.Locale.ROOT)) {
            case "black":
                return 0xFF000000;
            case "blue":
                return 0xFF0000FF;
            case "cyan":
                return 0xFF00FFFF;
            case "dkgray":
            case "darkgray":
            case "darkgrey":
                return 0xFF444444;
            case "gray":
            case "grey":
                return 0xFF888888;
            case "green":
                return 0xFF00FF00;
            case "lightgray":
            case "lightgrey":
            case "ltgray":
                return 0xFFCCCCCC;
            case "magenta":
                return 0xFFFF00FF;
            case "red":
                return 0xFFFF0000;
            case "transparent":
                return 0;
            case "white":
                return 0xFFFFFFFF;
            case "yellow":
                return 0xFFFFFF00;
            default:
                throw new IllegalArgumentException("unknown color: " + text);
        }
    }

    private static int duplicate(char hex) {
        int nibble = Character.digit(hex, 16);
        if (nibble < 0) {
            throw new IllegalArgumentException("bad hex: " + hex);
        }
        return (nibble << 4) | nibble;
    }

    /**
     * PhoneWindow always installs the content frame ({@code android.R.id.content}),
     * so that id resolves even headless; every other id misses (no inflated
     * hierarchy). The frame is cached per view and parented to it, so
     * ViewTree walks climb toward the decor tags. Returned as a ViewGroup so
     * the setContent check-cast holds.
     */
    private Object findContent(VmObject view, Object[] args) {
        if (args.length == 0 || !(args[0] instanceof Number)
                || ((Number) args[0]).intValue() != 0x1020002) {
            return null;
        }
        Object have = view.getField("Landroid/view/View;.mContentFrame");
        if (have instanceof VmObject) {
            return have;
        }
        VmObject frame = newPlaceholder("Landroid/view/ViewGroup;");
        frame.putField("Landroid/view/View;.mParent", view);
        frame.putField("Landroid/view/View;.mId", 0x1020002);
        view.putField("Landroid/view/View;.mContentFrame", frame);
        return frame;
    }

    /**
     * System service by name. Each known service resolves to a placeholder
     * whose own methods name the next tasks; anything else throws the usual
     * gap so the need stays visible.
     */
    private Object systemService(String name) {
        if ("accessibility".equals(name)) {
            return newPlaceholder("Landroid/view/accessibility/AccessibilityManager;");
        }
        if ("clipboard".equals(name)) {
            return newPlaceholder("Landroid/content/ClipboardManager;");
        }
        throw new VmException("unimplemented system service: " + name);
    }

    private Object systemServiceClass(String descriptor) {
        if ("Landroid/view/autofill/AutofillManager;".equals(descriptor)) {
            return newPlaceholder("Landroid/view/autofill/AutofillManager;");
        }
        throw new VmException("unimplemented system service: " + descriptor);
    }    /** Read-or-create a cached placeholder child (stable framework identity). */
    private VmObject cachedPlaceholder(VmObject owner, String key, String type) {        Object have = owner.getField(key);
        if (have instanceof VmObject) {
            return (VmObject) have;
        }
        VmObject created = newPlaceholder(type);
        owner.putField(key, created);
        return created;
    }

    /** The activity window's content frame, created on demand. */
    private VmObject contentFrame(VmObject activity) {
        VmObject window = cachedPlaceholder(activity, "Landroid/app/Activity;.mWindow",
                "Landroid/view/Window;");
        VmObject decor = cachedPlaceholder(window, "Landroid/view/Window;.mDecor",
                "Landroid/view/View;");
        Object have = decor.getField("Landroid/view/View;.mContentFrame");
        if (have instanceof VmObject) {
            return (VmObject) have;
        }
        VmObject frame = newPlaceholder("Landroid/view/ViewGroup;");
        frame.putField("Landroid/view/View;.mParent", decor);
        frame.putField("Landroid/view/View;.mId", 0x1020002);
        decor.putField("Landroid/view/View;.mContentFrame", frame);
        return frame;
    }

    /**
     * Joins the view to the window hierarchy: parented under the content
     * frame (so ViewTree walks reach the decor tags), recorded among the
     * frame's children (so a later setContent reuses it, as on device),
     * marked attached, and retained for the probe. Mirrors what
     * Window.setContentView + addView achieve on device, minus drawing.
     */
    private void joinWindow(VmObject activity, VmObject view) {
        activity.putField("Landroid/app/Activity;.mContentView", view);
        VmObject frame = contentFrame(activity);
        view.putField("Landroid/view/View;.mParent", frame);
        childrenOf(frame).add(view);
        view.putField("Landroid/view/View;.mAttached", 1);
    }

    /**
     * Ordered children of a group, created on demand.
     */
    private List<Object> childrenOf(VmObject group) {
        Object have = group.getField("Landroid/view/ViewGroup;.mChildren");
        if (have instanceof List) {
            return (List<Object>) have;
        }
        List<Object> children = new ArrayList<>();
        group.putField("Landroid/view/ViewGroup;.mChildren", children);
        return children;
    }

    private static int jump(Map<Integer, Integer> addresses, Insn insn) {
        Integer index = addresses.get(insn.codeOffset + insn.branchOffset);
        if (index == null) {
            throw new VmException("bad branch target");
        }
        return index;
    }

    private static int check(boolean taken, Map<Integer, Integer> addresses, Insn insn, int pc) {
        return taken ? jump(addresses, insn) : pc + 1;
    }

    /**
     * A boxed (heap) integer zero. Dalvik registers hold bits (plain
     * {@link Integer}, where 0-bits double as null), but boxing sites
     * ({@code Integer.valueOf}) produce heap objects, and reference checks
     * ({@code if-eqz} on an object, {@code checkNotNull}) must see an
     * object, not null-bits. Only zero needs the split: every other int is
     * unambiguous in all positions. Instance checks cover the types real
     * code tests for (Integer/Number/Object); rarer interfaces stay gaps.
     */
    private static final class BoxedZero extends Number {
        @Override
        public int intValue() {
            return 0;
        }

        @Override
        public long longValue() {
            return 0L;
        }

        @Override
        public float floatValue() {
            return 0.0f;
        }

        @Override
        public double doubleValue() {
            return 0.0d;
        }

        @Override
        public boolean equals(Object other) {
            return other == this
                    || (other instanceof Integer && (Integer) other == 0);
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public String toString() {
            return "0";
        }
    }

    private static final BoxedZero BOXED_ZERO = new BoxedZero();

    /** Boxes an int from a boxing site (heap object, never null-bits). */
    private static Number boxInt(int value) {
        return value == 0 ? BOXED_ZERO : Integer.valueOf(value);
    }

    /**
     * Dalvik {@code if-eq/if-ne}: value compare for the int family (all
     * normalized to {@link Integer}); reference identity otherwise, with
     * null and zero-bits equivalent (both are null at runtime).
     */
    static boolean eq(Object left, Object right) {
        if (left instanceof Integer && right instanceof Integer) {
            return left.equals(right);
        }
        return left == right || (isNullish(left) && isNullish(right));
    }

    /** True for null and for zero-bits (Dalvik cannot tell them apart). */
    static boolean isNullish(Object value) {
        return value == null || Integer.valueOf(0).equals(value);
    }

    private static boolean isZero(Object value) {
        return value == null || (value instanceof Number && !(value instanceof BoxedZero)
                && ((Number) value).intValue() == 0);
    }

    private static int asInt(Object value) {
        return ((Number) value).intValue();
    }

    private static long asLong(Object value) {
        return ((Number) value).longValue();
    }

    /**
     * Float value of a slot holding float bits ({@link Integer}) or a
     * {@link Float} value. Dalvik registers are untyped patterns; the use
     * site decides the reading, so float arithmetic reinterprets integers
     * instead of converting them numerically.
     */
    private static float asFloatBits(Object value) {
        if (value instanceof Float) {
            return (Float) value;
        }
        return Float.intBitsToFloat(asInt(value));
    }

    /**
     * Double value of a slot holding double bits ({@link Long}) or a
     * {@link Double} value. Same bit-model as {@link #asFloatBits}.
     */
    private static double asDoubleBits(Object value) {
        if (value instanceof Double) {
            return (Double) value;
        }
        return Double.longBitsToDouble(asLong(value));
    }

    private static int compareFloat(float left, float right, int nanBias) {
        if (Float.isNaN(left) || Float.isNaN(right)) {
            return nanBias;
        }
        return Float.compare(left, right);
    }

    private static int compareDouble(double left, double right, int nanBias) {
        if (Double.isNaN(left) || Double.isNaN(right)) {
            return nanBias;
        }
        return Double.compare(left, right);
    }

    /** Smali-like field reference: {@code Lpkg/Cls;->name:Type}. */
    static final class FieldRef {
        final String definingClass;
        final String name;
        final String type;

        FieldRef(String definingClass, String name, String type) {
            this.definingClass = definingClass;
            this.name = name;
            this.type = type;
        }

        static FieldRef parse(String reference) {
            int arrow = reference.indexOf("->");
            int colon = reference.indexOf(':', arrow);
            if (arrow < 0 || colon < 0) {
                throw new VmException("bad field reference: " + reference);
            }
            return new FieldRef(reference.substring(0, arrow),
                    reference.substring(arrow + 2, colon), reference.substring(colon + 1));
        }
    }

    /** Primitive-wrapper calls accept zero-boxed receivers as values. */
    private static boolean isWrapperCall(MethodRef ref) {
        String def = ref.definingClass;
        return def.equals("Ljava/lang/Integer;") || def.equals("Ljava/lang/Long;")
                || def.equals("Ljava/lang/Float;") || def.equals("Ljava/lang/Double;")
                || def.equals("Ljava/lang/Boolean;") || def.equals("Ljava/lang/Character;")
                || def.equals("Ljava/lang/Byte;") || def.equals("Ljava/lang/Short;");
    }

    /** Smali-like method reference: {@code Lpkg/Cls;->name(params)return}. */
    static final class MethodRef {
        final String definingClass;
        final String name;
        final List<String> parameters;
        final String returnType;

        MethodRef(String definingClass, String name, List<String> parameters, String returnType) {
            this.definingClass = definingClass;
            this.name = name;
            this.parameters = parameters;
            this.returnType = returnType;
        }

        static MethodRef parse(String reference) {
            int arrow = reference.indexOf("->");
            int open = reference.indexOf('(', arrow);
            int close = reference.indexOf(')', open);
            if (arrow < 0 || open < 0 || close < 0) {
                throw new VmException("bad method reference: " + reference);
            }
            return new MethodRef(reference.substring(0, arrow), reference.substring(arrow + 2, open),
                    Descriptors.splitTypes(reference.substring(open + 1, close)),
                    reference.substring(close + 1));
        }
    }
}
