package aine.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aine.dex.Dex;
import aine.dex.DexClass;
import aine.dex.DexMethod;
import aine.dex.Insn;

/**
 * Tree-walking DEX interpreter (spike) over our IR.
 *
 * <p>Value model: registers are object slots. Integers (and bytes, shorts,
 * chars, booleans) are normalized to {@link Integer}, {@code long} to
 * {@link Long}, {@code float} to {@link Float}, {@code double} to
 * {@link Double}; object references (including strings) are stored directly
 * and {@code null} means null. Wide values occupy one slot; the nominal
 * second slot is never read or written.
 *
 * <p>Same-dex static/direct/virtual calls recurse with fresh frames.
 * Anything else — unknown opcodes, framework methods, missing classes —
 * throws {@link VmException} naming the gap.
 */
public final class Interpreter {
    private static final int ACC_STATIC = 0x0008;

    private final Dex dex;

    public Interpreter(Dex dex) {
        this.dex = dex;
    }

    /**
     * Calls a method by name, matched on argument count (ambiguous or
     * missing targets throw).
     */
    public Object call(String type, String methodName, Object... args) {
        DexClass cls = dex.findClass(type);
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
        return execute(cls, target, null, args);
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
        Object pending = null;
        boolean hasPending = false;
        int pc = 0;
        while (true) {
            Insn insn = code.get(pc);
            String op = insn.opcode;
            List<Integer> g = insn.registers;
            boolean consumesPending = op.startsWith("move-result");
            boolean producesPending = op.startsWith("invoke-");
            if (!consumesPending && !producesPending) {
                hasPending = false;
            }
            switch (op) {
                case "const-4":
                case "const-16":
                case "const":
                case "const-wide-16":
                case "const-wide-32":
                case "const-wide":
                    regs[g.get(0)] = insn.hasLiteral ? insn.literal : 0L;
                    // Narrow consts arrive as ints; normalize through the value model.
                    if (!op.startsWith("const-wide")) {
                        regs[g.get(0)] = (int) (long) regs[g.get(0)];
                    }
                    pc++;
                    break;
                case "const-string":
                    regs[g.get(0)] = insn.reference;
                    pc++;
                    break;
                case "move":
                case "move-from16":
                case "move-16":
                case "move-object":
                case "move-object-from16":
                case "move-object-16":
                    regs[g.get(0)] = regs[g.get(1)];
                    pc++;
                    break;
                case "move-wide":
                case "move-wide-from16":
                case "move-wide-16":
                    regs[g.get(0)] = regs[g.get(1)];
                    pc++;
                    break;
                case "move-result":
                case "move-result-wide":
                case "move-result-object":
                    if (!hasPending) {
                        throw new VmException("move-result without invoke");
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
                case "invoke-static":
                case "invoke-static-range":
                case "invoke-direct":
                case "invoke-direct-range":
                case "invoke-virtual":
                case "invoke-virtual-range": {
                    MethodRef ref = MethodRef.parse(insn.reference);
                    if (ref.definingClass.equals("Ljava/lang/Object;")
                            && ref.name.equals("<init>")) {
                        pending = null;
                        hasPending = false;
                        pc++;
                        break;
                    }
                    DexClass target = dex.findClass(ref.definingClass);
                    if (target == null) {
                        throw new VmException("unimplemented: " + insn.reference);
                    }
                    DexMethod callee = null;
                    for (DexMethod candidate : target.methods) {
                        if (candidate.name.equals(ref.name)
                                && candidate.parameterTypes.equals(ref.parameters)
                                && candidate.returnType.equals(ref.returnType)) {
                            callee = candidate;
                            break;
                        }
                    }
                    if (callee == null) {
                        throw new VmException("unimplemented: " + insn.reference);
                    }
                    boolean calleeStatic = (callee.accessFlags & ACC_STATIC) != 0;
                    boolean wantStatic = op.startsWith("invoke-static");
                    if (wantStatic == !calleeStatic && !op.startsWith("invoke-direct")) {
                        throw new VmException("static mismatch: " + insn.reference);
                    }
                    List<Integer> slots = g;
                    int slot = 0;
                    Object receiver = null;
                    if (!calleeStatic) {
                        receiver = regs[slots.get(slot++)];
                    }
                    Object[] callArgs = new Object[callee.parameterTypes.size()];
                    for (int i = 0; i < callArgs.length; i++) {
                        callArgs[i] = regs[slots.get(slot)];
                        slot += isWide(callee.parameterTypes.get(i)) ? 2 : 1;
                    }
                    Object result = execute(target, callee, receiver, callArgs);
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

    private static boolean isWide(String type) {
        return type.equals("J") || type.equals("D");
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
