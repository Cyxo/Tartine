package aine.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aine.dex.DexClass;
import aine.dex.DexMethod;
import aine.dex.DexSet;
import aine.dex.Insn;

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
    private long steps = 0;

    public Interpreter(DexSet dexes) {
        this.dexes = dexes;
    }

    /** Instructions executed by the last top-level {@link #call} or {@link #invoke}. */
    public long steps() {
        return steps;
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

    /** Invokes a resolved method with an explicit receiver (null for static). */
    public Object invoke(DexClass cls, DexMethod method, Object receiver, Object... args) {
        steps = 0;
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
            words += isWide(param) ? 2 : 1;
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
            reg += isWide(method.parameterTypes.get(i)) ? 2 : 1;
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
                    case "const-4":
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
                        DexClass target = dexes.findClass(insn.reference);
                        if (target == null) {
                            throw new VmException("unimplemented: " + insn.reference);
                        }
                        ensureInit(target);
                        VmObject object = new VmObject(target);
                        regs[g.get(0)] = object;
                        pending = object;
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
                        if (isWide(element)) {
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
                        if (target == null) {
                            throw new VmException("unimplemented: " + insn.reference);
                        }
                        DexMethod callee = findMethod(target, ref);
                        if (callee == null) {
                            throw new VmException("unimplemented: " + insn.reference);
                        }
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
                        DexMethod callee;
                        DexClass home;
                        if (op.startsWith("invoke-direct")) {
                            home = dexes.findClass(ref.definingClass);
                            if (home == null) {
                                throw new VmException("unimplemented: " + insn.reference);
                            }
                            callee = findMethod(home, ref);
                        } else if (op.startsWith("invoke-super")) {
                            home = superOf(lexical);
                            callee = home == null ? null : lookupUp(home, ref);
                        } else {
                            home = runtimeClass(receiver);
                            callee = home == null ? null : lookupUp(home, ref);
                        }
                        if (callee == null || home == null) {
                            throw new VmException("unimplemented: " + insn.reference);
                        }
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
        if (cls == null) {
            throw new VmException("unimplemented: " + fieldReference);
        }
        ensureInit(cls);
    }

    private Object staticInt(String fieldReference) {
        ensureInitFor(fieldReference);
        Object value = statics.get(staticKey(fieldReference));
        return value == null ? 0 : asInt(value);
    }

    private Object staticLong(String fieldReference) {
        ensureInitFor(fieldReference);
        Object value = statics.get(staticKey(fieldReference));
        return value == null ? 0L : asLong(value);
    }

    private Object staticRef(String fieldReference) {
        ensureInitFor(fieldReference);
        return statics.get(staticKey(fieldReference));
    }

    private void staticPut(String fieldReference, Object value) {
        ensureInitFor(fieldReference);
        statics.put(staticKey(fieldReference), value);
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

    /** Null passes check-cast; instance-of null is false. Otherwise walks
     * the same-dex superclass chain (arrays: exact component or Object). */
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
            String want = target.substring(1);
            String have = ((VmArray) value).elementType;
            return want.equals(have)
                    || (want.equals("Ljava/lang/Object;") && isRefType(have));
        }
        if (!(value instanceof VmObject)) {
            return false;
        }
        String current = ((VmObject) value).type.type;
        while (true) {
            if (current.equals(target)) {
                return true;
            }
            if (current.equals("Ljava/lang/Object;")) {
                return false;
            }
            DexClass cls = dexes.findClass(current);
            if (cls == null || cls.superclass == null) {
                throw new VmException("unimplemented class: " + current);
            }
            current = cls.superclass;
        }
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

    private static Object[] readArgs(DexMethod callee, List<Integer> slots, int first,
            Object[] regs) {
        Object[] callArgs = new Object[callee.parameterTypes.size()];
        int slot = first;
        for (int i = 0; i < callArgs.length; i++) {
            callArgs[i] = regs[slots.get(slot)];
            slot += isWide(callee.parameterTypes.get(i)) ? 2 : 1;
        }
        return callArgs;
    }

    private boolean catches(String handlerType, Object value) {
        if (!(value instanceof VmObject)) {
            return false;
        }
        try {
            return isInstance(value, handlerType);
        } catch (VmException unknown) {
            return false;
        }
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

    private static boolean isWide(String type) {
        return type.equals("J") || type.equals("D");
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
                    splitTypes(reference.substring(open + 1, close)),
                    reference.substring(close + 1));
        }

        static List<String> splitTypes(String descriptors) {
            List<String> types = new ArrayList<>();
            int i = 0;
            while (i < descriptors.length()) {
                char c = descriptors.charAt(i);
                if (c == 'L') {
                    int end = descriptors.indexOf(';', i);
                    if (end < 0) {
                        throw new VmException("bad type: " + descriptors);
                    }
                    types.add(descriptors.substring(i, end + 1));
                    i = end + 1;
                } else if (c == '[') {
                    int dims = 0;
                    while (descriptors.charAt(i) == '[') {
                        dims++;
                        i++;
                    }
                    if (descriptors.charAt(i) == 'L') {
                        int end = descriptors.indexOf(';', i);
                        types.add(descriptors.substring(i - dims, end + 1));
                        i = end + 1;
                    } else {
                        types.add(descriptors.substring(i - dims, i + 1));
                        i++;
                    }
                } else {
                    types.add(String.valueOf(c));
                    i++;
                }
            }
            return types;
        }
    }
}
