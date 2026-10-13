package tartine.vm;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tartine.dex.Descriptors;
import tartine.dex.DexClass;
import tartine.dex.DexMethod;
import tartine.dex.DexSet;
import tartine.dex.Insn;
import tartine.fw.FwIndex;

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
    private FwIndex framework;
    private long steps = 0;
    private long maxSteps = Long.MAX_VALUE;

    public Interpreter(DexSet dexes) {
        this.dexes = dexes;
        registerBuiltins();
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
                        regs[g.get(0)] = asFloat(regs[g.get(1)]) + asFloat(regs[g.get(2)]);
                        pc++;
                        break;
                    case "sub-float":
                        regs[g.get(0)] = asFloat(regs[g.get(1)]) - asFloat(regs[g.get(2)]);
                        pc++;
                        break;
                    case "mul-float":
                        regs[g.get(0)] = asFloat(regs[g.get(1)]) * asFloat(regs[g.get(2)]);
                        pc++;
                        break;
                    case "div-float":
                        regs[g.get(0)] = asFloat(regs[g.get(1)]) / asFloat(regs[g.get(2)]);
                        pc++;
                        break;
                    case "rem-float":
                        regs[g.get(0)] = asFloat(regs[g.get(1)]) % asFloat(regs[g.get(2)]);
                        pc++;
                        break;
                    case "add-float-2addr":
                        regs[g.get(0)] = asFloat(regs[g.get(0)]) + asFloat(regs[g.get(1)]);
                        pc++;
                        break;
                    case "sub-float-2addr":
                        regs[g.get(0)] = asFloat(regs[g.get(0)]) - asFloat(regs[g.get(1)]);
                        pc++;
                        break;
                    case "mul-float-2addr":
                        regs[g.get(0)] = asFloat(regs[g.get(0)]) * asFloat(regs[g.get(1)]);
                        pc++;
                        break;
                    case "div-float-2addr":
                        regs[g.get(0)] = asFloat(regs[g.get(0)]) / asFloat(regs[g.get(1)]);
                        pc++;
                        break;
                    case "rem-float-2addr":
                        regs[g.get(0)] = asFloat(regs[g.get(0)]) % asFloat(regs[g.get(1)]);
                        pc++;
                        break;
                    case "add-double":
                        regs[g.get(0)] = asDouble(regs[g.get(1)]) + asDouble(regs[g.get(2)]);
                        pc++;
                        break;
                    case "sub-double":
                        regs[g.get(0)] = asDouble(regs[g.get(1)]) - asDouble(regs[g.get(2)]);
                        pc++;
                        break;
                    case "mul-double":
                        regs[g.get(0)] = asDouble(regs[g.get(1)]) * asDouble(regs[g.get(2)]);
                        pc++;
                        break;
                    case "div-double":
                        regs[g.get(0)] = asDouble(regs[g.get(1)]) / asDouble(regs[g.get(2)]);
                        pc++;
                        break;
                    case "rem-double":
                        regs[g.get(0)] = asDouble(regs[g.get(1)]) % asDouble(regs[g.get(2)]);
                        pc++;
                        break;
                    case "add-double-2addr":
                        regs[g.get(0)] = asDouble(regs[g.get(0)]) + asDouble(regs[g.get(1)]);
                        pc++;
                        break;
                    case "sub-double-2addr":
                        regs[g.get(0)] = asDouble(regs[g.get(0)]) - asDouble(regs[g.get(1)]);
                        pc++;
                        break;
                    case "mul-double-2addr":
                        regs[g.get(0)] = asDouble(regs[g.get(0)]) * asDouble(regs[g.get(1)]);
                        pc++;
                        break;
                    case "div-double-2addr":
                        regs[g.get(0)] = asDouble(regs[g.get(0)]) / asDouble(regs[g.get(1)]);
                        pc++;
                        break;
                    case "rem-double-2addr":
                        regs[g.get(0)] = asDouble(regs[g.get(0)]) % asDouble(regs[g.get(1)]);
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
                        regs[g.get(0)] = -asFloat(regs[g.get(1)]);
                        pc++;
                        break;
                    case "neg-double":
                        regs[g.get(0)] = -asDouble(regs[g.get(1)]);
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
                        regs[g.get(0)] = (int) asFloat(regs[g.get(1)]);
                        pc++;
                        break;
                    case "float-to-long":
                        regs[g.get(0)] = (long) asFloat(regs[g.get(1)]);
                        pc++;
                        break;
                    case "float-to-double":
                        regs[g.get(0)] = (double) asFloat(regs[g.get(1)]);
                        pc++;
                        break;
                    case "double-to-int":
                        regs[g.get(0)] = (int) asDouble(regs[g.get(1)]);
                        pc++;
                        break;
                    case "double-to-long":
                        regs[g.get(0)] = (long) asDouble(regs[g.get(1)]);
                        pc++;
                        break;
                    case "double-to-float":
                        regs[g.get(0)] = (float) asDouble(regs[g.get(1)]);
                        pc++;
                        break;
                    case "cmp-long":
                        regs[g.get(0)] = Long.compare(asLong(regs[g.get(1)]), asLong(regs[g.get(2)]));
                        pc++;
                        break;
                    case "cmpl-float":
                        regs[g.get(0)] = compareFloat(asFloat(regs[g.get(1)]), asFloat(regs[g.get(2)]), -1);
                        pc++;
                        break;
                    case "cmpg-float":
                        regs[g.get(0)] = compareFloat(asFloat(regs[g.get(1)]), asFloat(regs[g.get(2)]), 1);
                        pc++;
                        break;
                    case "cmpl-double":
                        regs[g.get(0)] = compareDouble(asDouble(regs[g.get(1)]), asDouble(regs[g.get(2)]), -1);
                        pc++;
                        break;
                    case "cmpg-double":
                        regs[g.get(0)] = compareDouble(asDouble(regs[g.get(1)]), asDouble(regs[g.get(2)]), 1);
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
                        if (value != null && !isInstance(value, insn.reference)) {
                            throw new ClassCastException(insn.reference);
                        }
                        pc++;
                        break;
                    }                    case "instance-of":
                        regs[g.get(0)] = toInt(isInstance(regs[g.get(1)], insn.reference));
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
                            array.set(i, regs[g.get(i)]);
                        }
                        pending = array;
                        hasPending = true;
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
                        at(regs[g.get(1)], regs[g.get(2)], regs[g.get(0)]);
                        pc++;
                        break;
                    case "aput-wide":
                        at(regs[g.get(1)], regs[g.get(2)], asLong(regs[g.get(0)]));
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
                    case "iget-boolean":
                    case "iget-byte":
                    case "iget-char":
                    case "iget-short":
                        regs[g.get(0)] = fieldInt(regs[g.get(1)], insn.reference);
                        pc++;
                        break;
                    case "iget-wide":
                        regs[g.get(0)] = fieldLong(regs[g.get(1)], insn.reference);
                        pc++;
                        break;
                    case "iget-object":
                        regs[g.get(0)] = field(regs[g.get(1)], insn.reference);
                        pc++;
                        break;
                    case "iput":
                        field(regs[g.get(1)], insn.reference, asInt(regs[g.get(0)]));
                        pc++;
                        break;
                    case "iput-boolean":
                        field(regs[g.get(1)], insn.reference, asInt(regs[g.get(0)]) & 1);
                        pc++;
                        break;
                    case "iput-wide":
                        field(regs[g.get(1)], insn.reference, asLong(regs[g.get(0)]));
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
                        staticPut(insn.reference, asInt(regs[g.get(0)]));
                        pc++;
                        break;
                    case "sput-boolean":
                        staticPut(insn.reference, asInt(regs[g.get(0)]) & 1);
                        pc++;
                        break;
                    case "sput-wide":
                        staticPut(insn.reference, asLong(regs[g.get(0)]));
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
                        if (value == null) {
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
                        DexClass target = dexes.findClass(ref.definingClass);
                        DexMethod callee = target == null ? null : findMethod(target, ref);
                        if (callee != null) {
                            if ((callee.accessFlags & ACC_STATIC) == 0) {
                                throw new VmException("static mismatch: " + insn.reference);
                            }
                            ensureInit(target);
                            Object[] callArgs = readArgs(callee, g, 0, regs);
                            Object result = execute(target, callee, null, callArgs);
                            if (callee.returnType.equals("V")) {
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
                        HostMethod builtin = builtins.get(builtinKey(ref));
                        if (builtin != null) {
                            Object result = builtin.invoke(null, readRefArgs(ref, g, 0, regs));
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
                        throw frameworkGap(ref.definingClass, ref, insn.reference);
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
                        if (receiver == null) {
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
                        DexMethod callee = null;
                        DexClass home = null;
                        if (op.startsWith("invoke-direct")) {
                            home = dexes.findClass(ref.definingClass);
                            if (home != null) {
                                callee = findMethod(home, ref);
                            }
                        } else if (op.startsWith("invoke-super")) {
                            home = superOf(lexical);
                            if (home != null) {
                                callee = lookupUp(home, ref);
                            }
                        } else {
                            DexClass runtime = runtimeClass(receiver);
                            home = runtime;
                            if (runtime != null) {
                                callee = lookupUp(runtime, ref);
                            }
                        }
                        if (callee != null) {
                            if ((callee.accessFlags & ACC_STATIC) != 0) {
                                throw new VmException("static mismatch: " + insn.reference);
                            }
                            Object[] callArgs = readArgs(callee, g, 1, regs);
                            Object result = execute(home, callee, receiver, callArgs);
                            if (callee.returnType.equals("V")) {
                                pending = null;
                                hasPending = false;
                            } else {
                                pending = result;
                                hasPending = true;
                            }
                            pc++;
                            break;
                        }
                        HostMethod builtin = builtins.get(builtinKey(ref));
                        if (builtin != null) {
                            Object[] callArgs = readRefArgs(ref, g, 1, regs);
                            Object result = builtin.invoke(receiver, callArgs);
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
                        throw frameworkGap(ref.definingClass, ref, insn.reference);
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
        if (descriptor.startsWith("Ljava/lang/")) {
            Class<?> clazz = hostThrowable(descriptor);
            if (clazz != null) {
                String superDesc = "Ljava/lang/Throwable;";
                if (framework != null) {
                    FwIndex.FwClass fw = framework.find(descriptor);
                    if (fw != null && fw.superclass != null) {
                        superDesc = fw.superclass;
                    }
                }
                VmObject placeholder = new VmObject(new DexClass(descriptor, superDesc,
                        new ArrayList<String>(), new ArrayList<DexMethod>()));
                throwables.put(placeholder, new PendingThrowable(clazz));
                return placeholder;
            }
        }
        if (framework != null) {
            FwIndex.FwClass fw = framework.find(descriptor);
            if (fw != null) {
                // Placeholder: modeled for casts/dispatch, never initialized.
                // Framework <init> bodies run as no-ops (see runSpecial).
                return new VmObject(new DexClass(descriptor, fw.superclass,
                        fw.interfaces, new ArrayList<DexMethod>()));
            }
        }
        return null;
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
            throw new VmException("unimplemented: " + fieldReference);
        }
        // Framework class: no <clinit> to run; constants served without init.
    }

    private Object staticInt(String fieldReference) {
        ensureInitFor(fieldReference);
        Object value = statics.get(staticKey(fieldReference));
        if (value != null) {
            return asInt(value);
        }
        Object constant = frameworkConstant(fieldReference);
        if (constant instanceof Number) {
            return ((Number) constant).intValue();
        }
        if (constant != null || !isDexClass(fieldReference)) {
            throw new VmException("unimplemented: " + fieldReference);
        }
        return 0;
    }

    private Object staticLong(String fieldReference) {
        ensureInitFor(fieldReference);
        Object value = statics.get(staticKey(fieldReference));
        if (value != null) {
            return asLong(value);
        }
        Object constant = frameworkConstant(fieldReference);
        if (constant instanceof Number) {
            return ((Number) constant).longValue();
        }
        if (constant != null || !isDexClass(fieldReference)) {
            throw new VmException("unimplemented: " + fieldReference);
        }
        return 0L;
    }

    private Object staticRef(String fieldReference) {
        ensureInitFor(fieldReference);
        String key = staticKey(fieldReference);
        if (statics.containsKey(key)) {
            return statics.get(key);
        }
        Object constant = frameworkConstant(fieldReference);
        if (constant != null) {
            return constant;
        }
        if (isDexClass(fieldReference)) {
            return null;
        }
        throw new VmException("unimplemented: " + fieldReference);
    }

    private void staticPut(String fieldReference, Object value) {
        ensureInitFor(fieldReference);
        if (!isDexClass(fieldReference)) {
            throw new VmException("unimplemented: " + fieldReference);
        }
        statics.put(staticKey(fieldReference), value);
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

    private Object field(Object target, String fieldReference) {
        return vmObject(target, fieldReference).getField(fieldKey(fieldReference));
    }

    private void field(Object target, String fieldReference, Object value) {
        vmObject(target, fieldReference).putField(fieldKey(fieldReference), value);
    }

    private Object fieldInt(Object target, String fieldReference) {
        Object value = field(target, fieldReference);
        return value == null ? 0 : asInt(value);
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

    /** Null is never an instance; otherwise BFS over supers and interfaces. */
    private boolean isInstance(Object value, String target) {
        if (value == null) {
            return false;
        }
        if (target.equals("Ljava/lang/Object;")) {
            return true;
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
    private DexMethod lookupUp(DexClass start, MethodRef ref) {
        DexClass current = start;
        while (current != null) {
            DexMethod found = findMethod(current, ref);
            if (found != null) {
                return found;
            }
            current = superOf(current);
        }
        return null;
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

    private static String builtinKey(MethodRef ref) {        StringBuilder key = new StringBuilder(ref.definingClass).append("->")
                .append(ref.name).append('(');
        for (String param : ref.parameters) {
            key.append(param);
        }
        return key.append(')').append(ref.returnType).toString();
    }

    private VmException frameworkGap(String definingClass, MethodRef ref, String reference) {
        FwIndex.FwClass fw = framework == null ? null : framework.find(definingClass);
        if (fw != null && fw.hasMethod(ref.name, ref.parameters, ref.returnType)) {
            return new VmException("unimplemented: " + reference);
        }
        if (dexes.findClass(definingClass) != null || fw != null) {
            return new VmException("no such method: " + reference);
        }
        return new VmException("unimplemented: " + reference);
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
        return ((Number) args[i]).floatValue();
    }

    private static double argDouble(Object[] args, int i) {
        return ((Number) args[i]).doubleValue();
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
            // Framework constructors have no DEX body: no-op. Applies to
            // placeholder receivers and to in-dex receivers calling a
            // framework super constructor (e.g. Activity.<init>).
            boolean frameworkCtor = dexes.findClass(ref.definingClass) == null
                    && framework != null && framework.find(ref.definingClass) != null;
            if (isPlaceholder(receiver) || frameworkCtor) {
                PendingThrowable pending = throwables.get(receiver);
                if (pending != null && ref.parameters.size() == 1
                        && ref.parameters.get(0).equals("Ljava/lang/String;")) {
                    pending.message = argString(args, 0);
                    return Outcome.empty();
                }
                // Framework construction initializes nothing; fields default.
                return Outcome.empty();
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
                (receiver, args) -> Integer.valueOf(argInt(args, 0)));
        builtin("Ljava/lang/Integer;->intValue()I",
                (receiver, args) -> ((Integer) receiver).intValue());
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
                (receiver, args) -> argInt(args, 0) != 0 ? 1 : 0);
        builtin("Ljava/lang/Boolean;->booleanValue()Z",
                (receiver, args) -> argInt(new Object[]{receiver}, 0));
        builtin("Ljava/lang/Boolean;->parseBoolean(Ljava/lang/String;)Z",
                (receiver, args) -> bool(Boolean.parseBoolean(argString(args, 0))));
        builtin("Ljava/lang/Boolean;->toString(Z)Ljava/lang/String;",
                (receiver, args) -> Boolean.toString(argInt(args, 0) != 0));

        builtin("Ljava/lang/Character;->valueOf(C)Ljava/lang/Character;",
                (receiver, args) -> argInt(args, 0));
        builtin("Ljava/lang/Character;->charValue()C",
                (receiver, args) -> argInt(new Object[]{receiver}, 0));
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
                (receiver, args) -> argInt(args, 0));
        builtin("Ljava/lang/Byte;->byteValue()B",
                (receiver, args) -> argInt(new Object[]{receiver}, 0));
        builtin("Ljava/lang/Byte;->parseByte(Ljava/lang/String;)B",
                (receiver, args) -> (int) Byte.parseByte(argString(args, 0)));
        builtin("Ljava/lang/Byte;->parseByte(Ljava/lang/String;I)B",
                (receiver, args) -> (int) Byte.parseByte(argString(args, 0), argInt(args, 1)));
        builtin("Ljava/lang/Short;->valueOf(S)Ljava/lang/Short;",
                (receiver, args) -> argInt(args, 0));
        builtin("Ljava/lang/Short;->shortValue()S",
                (receiver, args) -> argInt(new Object[]{receiver}, 0));
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
        builtin("Landroid/os/SystemClock;->uptimeMillis()J",
                (receiver, args) -> System.nanoTime() / 1000000L);
        builtin("Landroid/os/SystemClock;->elapsedRealtime()J",
                (receiver, args) -> System.nanoTime() / 1000000L);
        builtin("Landroid/os/SystemClock;->elapsedRealtimeNanos()J",
                (receiver, args) -> System.nanoTime());

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

        builtin("Ljava/lang/Class;->getName()Ljava/lang/String;",
                (receiver, args) -> classNameOf(((VmClass) receiver).descriptor));
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
                (receiver, args) -> {
                    String binary = argString(args, 0);
                    String descriptor = "L" + binary.replace('.', '/') + ";";
                    if (dexes.findClass(descriptor) != null
                            || framework != null && framework.find(descriptor) != null) {
                        return new VmClass(descriptor);
                    }
                    throw new VmThrow(new ClassNotFoundException(binary));
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

    private static boolean updaterCas(FieldUpdater updater, VmObject target,
            Object expect, Object update) {
        Object current = target.getField(updater.key());
        if (current == expect) {
            target.putField(updater.key(), update);
            return true;
        }
        return false;
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

    /** Reference equality, with value equality for boxed primitives and strings. */
    private static boolean eq(Object left, Object right) {
        return left == right || (left != null && left.equals(right));
    }

    private static boolean isZero(Object value) {
        return value == null || (value instanceof Number && ((Number) value).intValue() == 0);
    }

    private static int asInt(Object value) {
        return ((Number) value).intValue();
    }

    private static long asLong(Object value) {
        return ((Number) value).longValue();
    }

    private static float asFloat(Object value) {
        return ((Number) value).floatValue();
    }

    private static double asDouble(Object value) {
        return ((Number) value).doubleValue();
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
