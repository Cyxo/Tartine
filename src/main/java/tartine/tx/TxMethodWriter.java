package tartine.tx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import tartine.dex.DexMethod;
import tartine.dex.Insn;

/**
 * One DEX method -> one JVM method body (translation spike).
 *
 * <p>Wiring: each DEX register {@code r} maps to JVM local {@code 2*r}; wide
 * pairs therefore occupy {@code 2*r}/{@code 2*r+1} without colliding (the
 * nominal tail register {@code r+1} sits at {@code 2*r+2}). DEX register
 * typing is verifiable-in-DEX per path, so the JVM v49 type-inferencing
 * verifier infers matching slot types from our load/store choices.
 *
 * <p>Bit-pattern ambiguity (a const/move carrying an int, float or reference
 * 32-bit pattern) is resolved by scanning forward to the register's first
 * consumer and storing with its demanded kind; unresolved falls back to int.
 * Load-side kind mismatches throw {@code TxGap} instead of emitting
 * silently-invalid bytecode.
 */
final class TxMethodWriter implements Opcodes {
    /** Kind chars: narrow-int-family, float, reference, long, double. */
    private static final char INT = 'I', FLOAT = 'F', OBJ = 'A', LONG = 'J', DOUBLE = 'D';
    private static final char UNKNOWN = 0;

    private static final int ACC_STATIC = 0x8;

    private final DexMethod method;
    private final List<Insn> insns;
    private final MethodVisitor mv;

    private final Map<Integer, Label> labels = new HashMap<>();
    private final Set<Integer> labelsVisited = new HashSet<>();

    private final char[] regKind;
    private final String[] regArray;
    /** Slot created by new-instance whose <init> has not run yet: the JVM
     *  verifier only permits <init> and astore on such refs, so a virtual
     *  call on one is a loud gap (fixture-only shape; real dx never emits). */
    private final boolean[] regUninit;
    /** Fixture shim: move-result slots whose value was already stored. */
    private final Set<Integer> skipInsn = new HashSet<>();
    /** Register currently holds the literal zero (DEX zero-type: usable as
     *  both an int and the null reference at each use site). */
    private final boolean[] regZero;

    /** Result of the previous instruction still on the stack: descriptor. */
    private String pendingResult;
    /** Array descriptor for the pending result (array clone), else null. */
    private String pendingArray;
    /** Instruction currently emitted (context for load-side gap messages). */
    private Insn currentInsn;
    /** DEX-owned interface type descriptors, for virtual-on-interface refs. */
    private final java.util.Set<String> guestInterfaces;

    TxMethodWriter(ClassWriter cw, DexMethod method,
            java.util.Set<String> guestInterfaces) {
        this.method = method;
        this.guestInterfaces = guestInterfaces;
        this.insns = method.instructions;
        int regCount = Math.max(method.registerCount, paramRegs());
        this.regKind = new char[regCount + 1];
        this.regArray = new String[regCount + 1];
        this.regZero = new boolean[regCount + 1];
        this.regUninit = new boolean[regCount + 1];
        this.mv = cw.visitMethod(asmAccess(), method.name, TxTranslator.asmDesc(method),
                null, null);
    }

    /** Receiver plus one register per parameter (wide values take two). */
    private int paramRegs() {
        int params = 0;
        for (String p : method.parameterTypes) {
            params += TxDescriptors.isWide(p) ? 2 : 1;
        }
        return params + (((method.accessFlags & ACC_STATIC) == 0) ? 1 : 0);
    }

    void emit() {
        // Interpreter semantics: reading a register never written in a path
        // yields zero/null. The JVM verifier forbids reads of unassigned
        // locals, so pre-initialize every non-parameter slot to null (the
        // copyParams window immediately after overwrites its own initial
        // slots). REF demands then read null; INT demands on untouched
        // slots remain a loud wall.
        int paramSlots = paramRegs();
        for (int slot = paramSlots; slot < slots(); slot++) {
            mv.visitInsn(ACONST_NULL);
            mv.visitVarInsn(ASTORE, slot);
        }
        visitTries();
        copyParams();
        for (int i = 0; i < insns.size(); i++) {
            if (skipInsn.contains(i)) {
                continue;
            }
            int off = insns.get(i).codeOffset;
            if (labels.containsKey(off)) {
                visitLabel(off);
            }
            if (isPayloadOp(insns.get(i))) {
                // Payload bytes are DATA; real control flow never falls in.
                if (i + 1 < insns.size()) {
                    mv.visitJumpInsn(GOTO, labelAt(insns.get(i + 1).codeOffset));
                }
                continue;
            }
            emitInsn(i);
        }
        // Boundary labels past the last instruction (exclusive try ends).
        flushLabels();
        mv.visitMaxs(0, slots());
        mv.visitEnd();
    }

    private int slots() {
        return 2 * Math.max(method.registerCount, paramRegs()) + 2;
    }

    private boolean isPayloadOp(Insn in) {
        return in.opcode.contains("payload");
    }

    // ---------------------------------------------------------------------
    // Labels / tries / entry copy
    // ---------------------------------------------------------------------

    private Label labelAt(int off) {
        return labels.computeIfAbsent(off, k -> new Label());
    }

    private void visitLabel(int off) {
        labelsVisited.add(off);
        mv.visitLabel(labels.get(off));
    }

    private void flushLabels() {
        for (Map.Entry<Integer, Label> e : labels.entrySet()) {
            if (!labelsVisited.contains(e.getKey())) {
                visitLabel(e.getKey());
            }
        }
    }

    private void visitTries() {
        for (DexMethod.Try t : method.tries) {
            Label start = labelAt(t.start);
            Label end = labelAt(t.end);
            for (DexMethod.Handler h : t.handlers) {
                mv.visitTryCatchBlock(start, end, labelAt(h.address),
                        h.type == null ? null : TxDescriptors.internal(h.type));
            }
        }
    }

    private void copyParams() {
        boolean instance = (method.accessFlags & ACC_STATIC) == 0;
        int first = method.registerCount - paramRegs();
        int paramBase = first;
        if (instance) {
            mv.visitVarInsn(ALOAD, 0);
            store(first, OBJ);
            regKind[first] = OBJ;
            // In a constructor the receiver parameter IS the
            // uninitialized-this: the verifier admits exactly one <init>
            // call on it, which the uninit-tracking then clears.
            regUninit[first] = method.name.equals("<init>");
            // Parameters start after the receiver's register.
            paramBase = first + 1;
        }
        // Compute each parameter's JVM source slot first (compact JVM
        // layout), then copy HIGH-to-LOW: the strided register slots
        // (2*reg) can overlap the compact JVM parameter slots, and a copy
        // into an overlapping slot must not fire before the not-yet-read
        // JVM value is lifted out (enum <init>(String,I): copying the
        // name into slot 2 would wipe the ordinal that slot 2 still
        // carries).
        int count = method.parameterTypes.size();
        int[] jvmSlots = new int[count];
        int slot = instance ? 1 : 0;
        for (int p = 0; p < count; p++) {
            jvmSlots[p] = slot;
            slot += TxDescriptors.isWide(method.parameterTypes.get(p)) ? 2 : 1;
        }
        for (int p = count - 1; p >= 0; p--) {
            String type = method.parameterTypes.get(p);
            int reg = paramBase + p;
            loadFromJvmSlot(jvmSlots[p], type);
            store(reg, kindOf(type));
            regKind[reg] = kindOf(type);
        }
    }

    private void loadFromJvmSlot(int slot, String type) {
        if (type.equals("J")) {
            mv.visitVarInsn(LLOAD, slot);
        } else if (type.equals("D")) {
            mv.visitVarInsn(DLOAD, slot);
        } else if (type.equals("F")) {
            mv.visitVarInsn(FLOAD, slot);
        } else if (type.startsWith("L") || type.startsWith("[")) {
            mv.visitVarInsn(ALOAD, slot);
        } else {
            // B/C/S/Z/I all arrive as INTEGER-typed locals.
            mv.visitVarInsn(ILOAD, slot);
        }
    }

    /** Stack -> slot store for the given kind. */
    private void store(int reg, char k) {
        regZero[reg] = false;
        int slot = slotOf(reg);
        switch (k) {
            case LONG: mv.visitVarInsn(LSTORE, slot); break;
            case DOUBLE: mv.visitVarInsn(DSTORE, slot); break;
            case FLOAT: mv.visitVarInsn(FSTORE, slot); break;
            case OBJ: mv.visitVarInsn(ASTORE, slot); break;
            default: mv.visitVarInsn(ISTORE, slot);
        }
    }

    private int slotOf(int reg) {
        return reg * 2;
    }

    private static char kindOf(String desc) {
        switch (desc) {
            case "V": throw new TxTranslator.TxGap("kind of void value");
            case "J": return LONG;
            case "D": return DOUBLE;
            case "F": return FLOAT;
            case "I": case "B": case "C": case "S": case "Z": return INT;
            default: return OBJ;
        }
    }

    private int asmAccess() {
        int flags = method.accessFlags;
        int out = 0;
        if ((flags & 0x1) != 0) out |= ACC_PUBLIC;
        if ((flags & 0x2) != 0) out |= ACC_PRIVATE;
        if ((flags & 0x4) != 0) out |= ACC_PROTECTED;
        if ((flags & ACC_STATIC) != 0) out |= ACC_STATIC;
        if ((flags & 0x10) != 0) out |= ACC_FINAL;
        if ((flags & 0x20) != 0) out |= ACC_SYNCHRONIZED;
        if ((flags & 0x400) != 0) out |= ACC_ABSTRACT;
        return out;
    }

    // ---------------------------------------------------------------------
    // Pending-result plumbing (invoke -> move-result*)
    // ---------------------------------------------------------------------

    private boolean isMoveResult(Insn in) {
        return in.opcode.equals("move-result") || in.opcode.equals("move-result-wide")
                || in.opcode.equals("move-result-object");
    }

    private void consumePending(int i) {
        Insn in = insns.get(i);
        if (pendingResult == null) {
            if (isMoveResult(in)) {
                throw gap("move-result without pending result", in);
            }
            return;
        }
        if (isMoveResult(in)) {
            char k = kindOf(pendingResult);
            boolean wantObj = in.opcode.equals("move-result-object");
            boolean wantWide = in.opcode.equals("move-result-wide");
            if (wantWide && (k == INT || k == FLOAT || k == OBJ)) {
                throw gap("move-result-wide for " + pendingResult, in);
            }
            if (wantObj && k != OBJ) {
                throw gap("move-result-object for " + pendingResult, in);
            }
            if (!wantWide && !wantObj && (k == LONG || k == DOUBLE)) {
                throw gap("move-result for wide " + pendingResult, in);
            }
            int dst = in.registers.get(0);
            store(dst, k);
            regKind[dst] = k;
            // Track arrays from any invoke returning an array (e.g.
            // Arrays.copyOf) so aget/aput/array-length stay verifiable.
            regArray[dst] = pendingArray != null ? pendingArray
                    : (k == OBJ && pendingResult.startsWith("[") ? pendingResult
                            : null);
            pendingResult = null;
            pendingArray = null;
            return;
        }
        dropPending();
    }

    private void dropPending() {
        char k = kindOf(pendingResult);
        mv.visitInsn(k == LONG || k == DOUBLE ? POP2 : POP);
        pendingResult = null;
        pendingArray = null;
    }

    /** Advertises the result descriptor left on the stack by this op. */
    private void pended(String desc, int index) {
        pended(desc, null, index);
    }

    /** Variant that also remembers an array descriptor for aget/aput use. */
    private void pended(String desc, String arrDesc, int index) {
        if (desc.equals("V")) {
            return; // nothing on the stack
        }
        boolean consumed = index + 1 < insns.size() && isMoveResult(insns.get(index + 1));
        if (consumed) {
            pendingResult = desc;
            pendingArray = arrDesc;
            return;
        }
        char k = kindOf(desc);
        mv.visitInsn(k == LONG || k == DOUBLE ? POP2 : POP);
    }

    // ---------------------------------------------------------------------
    // Producer-kind scans (const/move ambiguity resolution)
    // ---------------------------------------------------------------------

    private char narrowScan(int producerIdx, int reg) {
        return scanUse(producerIdx, reg, false, new HashSet<>());
    }

    private char wideScan(int producerIdx, int reg) {
        return scanUse(producerIdx, reg, true, new HashSet<>());
    }

    /**
     * First consumer decides: what kind does that instruction demand of the
     * scanned register? Follows move-chains; wide callers resolve J vs D,
     * narrow callers I vs F vs A.
     */
    private char scanUse(int producerIdx, int reg, boolean wide, Set<Long> guard) {
        long token = producerIdx * 100000L + reg + (wide ? 90000000L : 0);
        if (!guard.add(token)) {
            return wide ? LONG : INT;
        }
        for (int i = producerIdx + 1; i < insns.size(); i++) {
            Insn in = insns.get(i);
            List<Integer> rs = in.registers;
            int pos = rs.indexOf(reg);
            if (pos < 0) {
                continue;
            }
            String op = in.opcode;
            if (isMoveResult(in)) {
                // A move-result WRITES rather than reads; only the producer
                // of that slot matters; skip chasing it here.
                continue;
            }
            if (op.equals("move") || op.equals("move-from16") || op.equals("move-16")) {
                int dst = rs.get(0);
                if (dst != reg) {
                    return scanUse(i, dst, wide, guard);
                }
                continue; // dst == reg: overwrite, next consumer decides
            }
            if (op.startsWith("move-wide")) {
                int dst = rs.get(0);
                if (dst != reg) {
                    return scanUse(i, dst, true, guard);
                }
                continue;
            }
            if (op.startsWith("move-object")) {
                return OBJ;
            }
            if (op.startsWith("invoke")) {
                if (!isInvokeStatic(in) && pos == 0) {
                    return OBJ; // receiver
                }
                int order = isInvokeStatic(in) ? pos : pos - 1;
                return kindOfArg(in, order, wide);
            }
            if (op.startsWith("return") && !op.equals("return-void")) {
                String rt = method.returnType;
                char k = kindOf(rt);
                if (wide) {
                    return (k == LONG || k == DOUBLE) ? k : LONG;
                }
                return (k == LONG || k == DOUBLE) ? INT : k;
            }
            if (op.startsWith("if")) {
                return wide ? LONG : INT; // zero-test or compare; int default
            }
            if (op.equals("monitor-enter") || op.equals("monitor-exit")
                    || op.equals("throw") || op.equals("check-cast")
                    || op.equals("fill-array-data")) {
                return OBJ; // single-reference operand (check-cast re-uses dst)
            }
            if (op.equals("instance-of") || op.equals("array-length")) {
                // [dst, ref]: the ref operand consumes OBJ; the dst slot is
                // overwritten here.
                if (pos == 1) {
                    return OBJ;
                }
                continue;
            }
            if (op.startsWith("aget") || op.startsWith("aput")) {
                // Position-aware: aget [dst, arr, idx], aput [value, arr,
                // idx]. The index is INT and the array a plain reference in
                // every form; only element slots follow the component kind.
                if (pos == 2) {
                    return INT;
                }
                if (pos == 1) {
                    return OBJ;
                }
                if (op.startsWith("aget")) {
                    continue; // dst slot overwritten here; next read decides
                }
                if (op.endsWith("-wide")) {
                    return sideWide(in, wide);
                }
                if (op.equals("aput-object")) {
                    return OBJ;
                }
                return wide ? LONG : INT;
            }
            if (op.startsWith("iget") || op.startsWith("sget")) {
                // sget [dst]; iget [dst, objref]: the object-ref slot
                // consumes OBJ; the dst slot is overwritten here. Only the
                // dst read resolves the field type.
                if (pos == 1) {
                    return OBJ;
                }
                continue; // dst: overwritten by this get; next read decides
            }
            if (op.startsWith("iput") || op.startsWith("sput")) {
                // sput [src]; iput [src, objref]: the object ref consumes
                // OBJ; the stored value resolves the field kind.
                if (pos == 1) {
                    return OBJ;
                }
                char k = kindOf(fieldRefType(in.reference));
                if (wide) {
                    return (k == LONG || k == DOUBLE) ? k : LONG;
                }
                return (k == LONG || k == DOUBLE) ? INT : k;
            }
            // Conversions consume by from-kind (int-to-double reads int);
            // neg/not carry their kind in the suffix handled below.
            int cut = op.indexOf("-to-");
            if (cut > 0) {
                switch (op.substring(0, cut)) {
                    case "int": return wide ? LONG : INT;
                    case "long": return LONG;
                    case "float": return FLOAT;
                    case "double": return DOUBLE;
                }
            }
            if (op.contains("-long") && !op.startsWith("const")) {
                return LONG;
            }
            if (op.contains("-double") && !op.startsWith("const")) {
                return DOUBLE;
            }
            if (op.contains("-float") && !op.startsWith("const")) {
                return FLOAT;
            }
            return wide ? LONG : INT;
        }
        return wide ? LONG : INT;
    }

    /** aget/aput-wide resolves long-vs-double from our emitted array map. */
    private char sideWide(Insn in, boolean wide) {
        if (!wide) {
            return LONG;
        }
        String arrDesc = regArray[in.registers.get(1)];
        if ("[D".equals(arrDesc)) {
            return DOUBLE;
        }
        return LONG;
    }

    /** Parameter kind at argument order {@code order} of an invoke ref. */
    private char kindOfArg(Insn in, int order, boolean wide) {
        List<String> ps = paramsOf(in.reference);
        if (order < 0 || order >= ps.size()) {
            return wide ? LONG : INT;
        }
        char k = kindOf(ps.get(order));
        if (wide) {
            return (k == LONG || k == DOUBLE) ? k : LONG;
        }
        return (k == LONG || k == DOUBLE) ? INT : k;
    }

    private boolean isInvokeStatic(Insn in) {
        return in.opcode.startsWith("invoke-static");
    }

    /** Parameter descriptors of a method reference text (shared helper). */
    private List<String> paramsOf(String ref) {
        int open = ref.indexOf('(');
        int close = ref.indexOf(')', open);
        return tartine.dex.Descriptors.splitTypes(ref.substring(open + 1, close));
    }

    /** Type descriptor of a field reference text. */
    private String fieldRefType(String reference) {
        int colon = reference.indexOf(':', reference.indexOf("->") + 2);
        return reference.substring(colon + 1);
    }

    // ---------------------------------------------------------------------
    // Loads
    // ---------------------------------------------------------------------

    private void load(int reg, char want) {
        char have = regKind[reg];
        char k = have == UNKNOWN ? want : have;
        if (want == OBJ && k != OBJ && k != UNKNOWN) {
            // Interpreter-style mixed numeric slot: the value stays numeric
            // in the register and the reference demand autoboxes it (the
            // interpreter's invoke-arg bridge picked this same contract).
            // A zero-typed register is still the null reference.
            if (regZero[reg]) {
                mv.visitInsn(ACONST_NULL);
                return;
            }
            int slot = slotOf(reg);
            switch (k) {
                case INT:
                    mv.visitVarInsn(ILOAD, slot);
                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer",
                            "valueOf", "(I)Ljava/lang/Integer;", false);
                    return;
                case FLOAT:
                    mv.visitVarInsn(FLOAD, slot);
                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float",
                            "valueOf", "(F)Ljava/lang/Float;", false);
                    return;
                case LONG:
                    mv.visitVarInsn(LLOAD, slot);
                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long",
                            "valueOf", "(J)Ljava/lang/Long;", false);
                    return;
                case DOUBLE:
                    mv.visitVarInsn(DLOAD, slot);
                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double",
                            "valueOf", "(D)Ljava/lang/Double;", false);
                    return;
                default:
                    break;
            }
        }
        if (want == FLOAT && k == INT) {
            // Untyped-register bit flow (the fixture does this; real dx never
            // emits a non-zero int slot where a float is demanded).
            bitReinterpret(reg, want, k);
            return;
        }
        if (want == INT && k == OBJ && !regZero[reg]) {
            // Interpreter-shaped boxing: the slot holds an Object whose
            // numeric value flows into arithmetic (real dex unboxes at the
            // source). Match the interpreter's arithmetic bridge and unbox
            // Integer loudly.
            System.err.println("tx: unbox " + method.owner + "->"
                    + method.name + " slot v" + reg + gapSite());
            mv.visitVarInsn(ALOAD, slotOf(reg));
            mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue",
                    "()I", false);
            return;
        }
        if (want == INT && k == OBJ) {
            // A zero-typed object slot read by arithmetic: interpreter
            // semantics put numeric zero there.
            mv.visitInsn(ICONST_0);
            return;
        }
        if (want == INT && k == FLOAT) {
            bitReinterpret(reg, want, k);
            return;
        }
        if (want == LONG && k == DOUBLE) {
            bitReinterpret(reg, want, k);
            return;
        }
        if (want == DOUBLE && k == LONG) {
            bitReinterpret(reg, want, k);
            return;
        }
        if (want != k && k != UNKNOWN && want != 'B' && want != 'C' && want != 'S') {
            throw new TxTranslator.TxGap(method.owner + "->" + method.signature()
                    + gapSite() + ": register v" + reg + " kind " + k
                    + " but " + want + " demanded");
        }
        int slot = slotOf(reg);
        switch (k) {
            case LONG: mv.visitVarInsn(LLOAD, slot); break;
            case DOUBLE: mv.visitVarInsn(DLOAD, slot); break;
            case FLOAT: mv.visitVarInsn(FLOAD, slot); break;
            case OBJ: mv.visitVarInsn(ALOAD, slot); break;
            default: mv.visitVarInsn(ILOAD, slot);
        }
        // Narrow casts when the consumption wants a specific integral shape.
        if (want == 'B') {
            mv.visitInsn(I2B);
        } else if (want == 'C') {
            mv.visitInsn(I2C);
        } else if (want == 'S') {
            mv.visitInsn(I2S);
        }
    }

    /**
     * Bit-preserving reinterpret across primitive kinds. Untyped DEX
     * registers let bit patterns cross type families (the fixture's
     * fillFloatDemo returns float bits through an int slot; real dx never
     * emits this). Logged loudly; the JVM has no reinterpret opcode so we
     * bridge through the host's raw-bits accessors.
     */
    private void bitReinterpret(int reg, char want, char have) {
        int slot = slotOf(reg);
        System.err.println("tx: bit reinterpret " + have + "->" + want + " v" + reg
                + " in " + method.owner + "->" + method.signature());
        if (want == INT && have == FLOAT) {
            mv.visitVarInsn(FLOAD, slot);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "floatToRawIntBits",
                    "(F)I", false);
            return;
        }
        if (want == FLOAT && have == INT) {
            mv.visitVarInsn(ILOAD, slot);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "intBitsToFloat",
                    "(I)F", false);
            return;
        }
        if (want == LONG && have == DOUBLE) {
            mv.visitVarInsn(DLOAD, slot);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "doubleToRawLongBits",
                    "(D)J", false);
            return;
        }
        if (want == DOUBLE && have == LONG) {
            mv.visitVarInsn(LLOAD, slot);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "longBitsToDouble",
                    "(J)D", false);
            return;
        }
        throw new TxTranslator.TxGap("bit reinterpret " + have + "->" + want);
    }

    // ---------------------------------------------------------------------
    // Instruction dispatch
    // ---------------------------------------------------------------------

    private void emitInsn(int i) {
        Insn in = insns.get(i);
        currentInsn = in;
        consumePending(i);
        String op = in.opcode;
        if (isMoveResult(in)) {
            return; // consumed above
        }
        if (op.equals("nop")) {
            return;
        }
        if (op.equals("const") || op.equals("const-4") || op.equals("const-16")
                || op.equals("const-high16") || op.equals("const-jumbo")) {
            int dst = in.registers.get(0);
            char k = narrowScan(i, dst);
            // The const value is an int; consumers decide its view. A scan
            // saying OBJ (relay into an Object-typed invoke arg) never turns
            // the slot into a null: real d8 route such constants through
            // Integer.valueOf (whose int param scans INT), and the
            // interpreter kept the value with autoboxing at the bridge.
            // Zero remains zero-typed: load() pushes ACONST_NULL there.
            if (k == OBJ) {
                k = INT;
            }
            pushLiteral(in.literal, k);
            store(dst, k);
            regKind[dst] = k;
            regArray[dst] = null;
            regZero[dst] = k == INT && in.literal == 0;
            return;
        }
        if (op.startsWith("const-wide")) {
            int dst = in.registers.get(0);
            char k = wideScan(i, dst);
            // Wide constants are numeric values; the OBJ scan verdict (an
            // Object-typed invoke arg) must not null the slot. load() boxes
            // the value at the reference demand.
            if (k == OBJ) {
                k = LONG;
            }
            pushLiteral(in.literal, k);
            store(dst, k);
            regKind[dst] = k;
            regZero[dst] = k == LONG && in.literal == 0;
            return;
        }
        if (op.startsWith("const-string")) {
            int dst = in.registers.get(0);
            mv.visitLdcInsn(stripStringRef(in.reference));
            store(dst, OBJ);
            regKind[dst] = OBJ;
            regArray[dst] = null;
            return;
        }
        if (op.equals("const-class")) {
            int dst = in.registers.get(0);
            mv.visitLdcInsn(org.objectweb.asm.Type.getType(in.reference));
            store(dst, OBJ);
            regKind[dst] = OBJ;
            regArray[dst] = null;
            return;
        }
        if (op.startsWith("move-result")) {
            return; // never reached (consumePending handled)
        }
        if (op.equals("move-exception")) {
            int dst = in.registers.get(0);
            mv.visitVarInsn(ASTORE, slotOf(dst));
            regKind[dst] = OBJ;
            regArray[dst] = null;
            regZero[dst] = false;
            return;
        }
        if (op.startsWith("move-object")) {
            moveObject(in);
            return;
        }
        if (op.startsWith("move-wide")) {
            moveWide(i, in);
            return;
        }
        if (op.startsWith("move")) {
            moveNarrow(i, in);
            return;
        }
        if (op.startsWith("return")) {
            emitReturn(in);
            return;
        }
        if (op.startsWith("goto")) {
            mv.visitJumpInsn(GOTO, labelAt(in.codeOffset + in.branchOffset));
            return;
        }
        if (op.startsWith("if-")) {
            emitBranch(in);
            return;
        }
        if (op.equals("packed-switch") || op.equals("sparse-switch")) {
            emitSwitch(i, in);
            return;
        }
        if (op.startsWith("invoke")) {
            emitInvoke(i, in);
            return;
        }
        if (op.equals("new-instance")) {
            int dst = in.registers.get(0);
            mv.visitTypeInsn(NEW, TxDescriptors.internal(in.reference));
            store(dst, OBJ);
            regKind[dst] = OBJ;
            regArray[dst] = null;
            regUninit[dst] = true;
            // The fixture also emits move-result-object right after
            // new-instance (real dx never does). The value is already in the
            // slot, so the copy is a no-op: skip the next move-result-object.
            if (i + 1 < insns.size()
                    && insns.get(i + 1).opcode.equals("move-result-object")) {
                skipInsn.add(i + 1);
            }
            return;
        }
        if (op.equals("new-array")) {
            int dst = in.registers.get(0);
            int size = in.registers.get(1);
            load(size, INT);
            String elem = TxDescriptors.arrayElement(in.reference);
            int base = TxDescriptors.newarrayBase(elem);
            if (base >= 0) {
                mv.visitIntInsn(NEWARRAY, base);
            } else {
                mv.visitTypeInsn(ANEWARRAY, TxDescriptors.internal(elem));
            }
            store(dst, OBJ);
            regKind[dst] = OBJ;
            regArray[dst] = in.reference;
            return;
        }
        if (op.startsWith("filled-new-array")) {
            emitFilledNewArray(in);
            pended(in.reference, i);
            return;
        }
        if (op.equals("fill-array-data")) {
            emitFillArray(in);
            return;
        }
        if (op.startsWith("iget")) {
            emitGetField(in, false);
            return;
        }
        if (op.startsWith("iput")) {
            emitPutField(in, false);
            return;
        }
        if (op.startsWith("sget")) {
            emitGetField(in, true);
            return;
        }
        if (op.startsWith("sput")) {
            emitPutField(in, true);
            return;
        }
        if (op.startsWith("aget")) {
            emitArrayGet(in);
            return;
        }
        if (op.startsWith("aput")) {
            emitArrayPut(in);
            return;
        }
        if (op.equals("check-cast")) {
            int dst = in.registers.get(0);
            load(dst, OBJ);
            mv.visitTypeInsn(CHECKCAST, TxDescriptors.internal(in.reference));
            store(dst, OBJ);
            regKind[dst] = OBJ;
            regArray[dst] = in.reference.startsWith("[") ? in.reference : null;
            return;
        }
        if (op.equals("instance-of")) {
            int dst = in.registers.get(0);
            int src = in.registers.get(1);
            load(src, OBJ);
            mv.visitTypeInsn(INSTANCEOF, TxDescriptors.internal(in.reference));
            store(dst, INT);
            regKind[dst] = INT;
            regArray[dst] = null;
            return;
        }
        if (op.equals("array-length")) {
            int dst = in.registers.get(0);
            int src = in.registers.get(1);
            String arrDesc = regArray[src];
            if (arrDesc == null) {
                // Without a tracked array the slot likely holds a non-array
                // object: the verifier would reject the whole class at load.
                // Degrade the method loudly instead.
                throw gap("array-length on register without a known array type", in);
            }
            load(src, OBJ);
            mv.visitTypeInsn(CHECKCAST, TxDescriptors.internal(arrDesc));
            mv.visitInsn(ARRAYLENGTH);
            store(dst, INT);
            regKind[dst] = INT;
            regArray[dst] = null;
            return;
        }
        if (op.equals("monitor-enter")) {
            load(in.registers.get(0), OBJ);
            mv.visitInsn(MONITORENTER);
            return;
        }
        if (op.equals("monitor-exit")) {
            load(in.registers.get(0), OBJ);
            mv.visitInsn(MONITOREXIT);
            return;
        }
        if (op.equals("throw")) {
            load(in.registers.get(0), OBJ);
            // Slot types widen through merges; ATHROW must verify as an
            // object-of-Throwable. Idempotent for real throwables.
            mv.visitTypeInsn(CHECKCAST, "java/lang/Throwable");
            mv.visitInsn(ATHROW);
            return;
        }
        if (op.startsWith("cmp")) {
            emitCompare(in);
            return;
        }
        if (isUnop(op)) {
            emitUnop(in);
            return;
        }
        if (isBinop(op)) {
            emitBinop(i, in);
            return;
        }
        throw gap("opcode " + op, in);
    }

    private TxTranslator.TxGap gap(String why, Insn in) {
        String ref = in.reference == null ? "" : " ref=" + in.reference;
        return new TxTranslator.TxGap(method.owner + "->" + method.signature()
                + " @" + in.codeOffset + " " + in.opcode + ref + " : " + why);
    }

    /** Current-insn context when a load-side gap fires away from emitInsn. */
    private String gapSite() {
        if (currentInsn == null) {
            return "";
        }
        String ref = currentInsn.reference == null ? ""
                : " ref=" + currentInsn.reference;
        return " @" + currentInsn.codeOffset + " " + currentInsn.opcode + ref;
    }

    /** Const-string reference text carries quotes: `Ljava/lang/String; ...`. */
    private String stripStringRef(String reference) {
        // const-string reference text is the raw string contents.
        return reference;
    }

    private void moveObject(Insn in) {
        int dst = in.registers.get(0);
        int src = in.registers.get(1);
        load(src, OBJ);
        store(dst, OBJ);
        regKind[dst] = OBJ;
        regArray[dst] = regArray[src];
        regZero[dst] = regZero[src];
    }

    private void moveNarrow(int i, Insn in) {
        int dst = in.registers.get(0);
        int src = in.registers.get(1);
        char k = regKind[src] != UNKNOWN ? regKind[src] : narrowScan(i, src);
        load(src, k);
        store(dst, k);
        regKind[dst] = k;
        regArray[dst] = regArray[src];
        regZero[dst] = regZero[src];
    }

    private void moveWide(int i, Insn in) {
        int dst = in.registers.get(0);
        int src = in.registers.get(1);
        char k = regKind[src] != UNKNOWN ? regKind[src] : wideScan(i, src);
        load(src, k);
        store(dst, k);
        regKind[dst] = k;
    }

    private void emitReturn(Insn in) {
        String op = in.opcode;
        if (op.equals("return-void")) {
            mv.visitInsn(RETURN);
            return;
        }
        String rt = method.returnType;
        char k = kindOf(rt);
        if (k == LONG || k == DOUBLE) {
            if (!op.equals("return-wide")) {
                throw gap("return kind " + rt + " needs return-wide", in);
            }
            load(in.registers.get(0), k);
            mv.visitInsn(k == LONG ? LRETURN : DRETURN);
            return;
        }
        if (op.equals("return-wide")) {
            throw gap("return-wide for narrow return " + rt, in);
        }
        if (k == OBJ) {
            if (!op.equals("return-object")) {
                throw gap("return-object needed for " + rt, in);
            }
            load(in.registers.get(0), OBJ);
            mv.visitInsn(ARETURN);
            return;
        }
        load(in.registers.get(0), k);
        mv.visitInsn(k == FLOAT ? FRETURN : IRETURN);
    }

    // ---------------------------------------------------------------------
    // Branches
    // ---------------------------------------------------------------------

    private void emitBranch(Insn in) {
        int target = in.codeOffset + in.branchOffset;
        Label to = labelAt(target);
        String op = in.opcode;
        boolean wideZ = op.endsWith("eqz") || op.endsWith("nez") || op.endsWith("ltz")
                || op.endsWith("gez") || op.endsWith("gtz") || op.endsWith("lez");
        if (wideZ) {
            int reg = in.registers.get(0);
            char k = regKind[reg];
            int minus = op.indexOf("-");
            String cmp = op.substring(minus + 1, op.length() - 1).replace("z", "");
            if (k == OBJ) {
                load(reg, OBJ);
                if (cmp.equals("eq")) {
                    mv.visitJumpInsn(IFNULL, to);
                } else if (cmp.equals("ne")) {
                    mv.visitJumpInsn(IFNONNULL, to);
                } else {
                    throw gap("object branch " + op, in);
                }
                return;
            }
            load(reg, INT); // unknown defaults to int/family shape
            int opc;
            switch (cmp) {
                case "eq": opc = IFEQ; break;
                case "ne": opc = IFNE; break;
                case "lt": opc = IFLT; break;
                case "ge": opc = IFGE; break;
                case "gt": opc = IFGT; break;
                case "le": opc = IFLE; break;
                default: throw gap("z-branch " + op, in);
            }
            mv.visitJumpInsn(opc, to);
            return;
        }
        // Two-register compare.
        int a = in.registers.get(0);
        int b = in.registers.get(1);
        String cmp = op.substring(op.indexOf("-") + 1);
        char ka = regKind[a];
        char kb = regKind[b];
        if (ka == UNKNOWN) {
            ka = INT;
        }
        if (kb == UNKNOWN) {
            kb = INT;
        }
        if ((ka == OBJ || kb == OBJ)) {
            if (!(cmp.equals("eq") || cmp.equals("ne"))) {
                throw gap("ordered compare on references " + op, in);
            }
            // The DEX verifier only admits int/ref mixed compares when the
            // int side is the zero-type (null); anything else is a real gap.
            if (ka != OBJ && !regZero[a]) {
                throw gap("compare mixes reference with non-zero int", in);
            }
            if (kb != OBJ && !regZero[b]) {
                throw gap("compare mixes reference with non-zero int", in);
            }
            if (ka == OBJ) {
                load(a, OBJ);
            } else {
                mv.visitInsn(ACONST_NULL);
            }
            if (kb == OBJ) {
                load(b, OBJ);
            } else {
                mv.visitInsn(ACONST_NULL);
            }
            mv.visitJumpInsn(cmp.equals("eq") ? IF_ACMPEQ : IF_ACMPNE, to);
            return;
        }
        load(a, INT);
        load(b, INT);
        int opc;
        switch (cmp) {
            case "eq": opc = IF_ICMPEQ; break;
            case "ne": opc = IF_ICMPNE; break;
            case "lt": opc = IF_ICMPLT; break;
            case "ge": opc = IF_ICMPGE; break;
            case "gt": opc = IF_ICMPGT; break;
            case "le": opc = IF_ICMPLE; break;
            default: throw gap("branch " + op, in);
        }
        mv.visitJumpInsn(opc, to);
    }

    // ---------------------------------------------------------------------
    // Switches
    // ---------------------------------------------------------------------

    private void emitSwitch(int i, Insn in) {
        int base = in.codeOffset;
        int reg = in.registers.get(0);
        int payloadOff = in.codeOffset + in.branchOffset;
        Insn payload = payloadAt(payloadOff);
        Label dflt = labelAt(fallthroughOf(i));
        if (in.opcode.equals("packed-switch")) {
            int min = payload.payloadKeys.get(0);
            int max = payload.payloadKeys.get(payload.payloadKeys.size() - 1);
            if (max - min + 1 != payload.payloadTargets.size()) {
                throw gap("packed-switch arity " + payload.payloadKeys + " vs "
                        + payload.payloadTargets, in);
            }
            Label[] targets = new Label[payload.payloadTargets.size()];
            for (int k = 0; k < payload.payloadTargets.size(); k++) {
                targets[k] = labelAt(base + payload.payloadTargets.get(k));
            }
            load(reg, INT);
            mv.visitTableSwitchInsn(min, max, dflt, targets);
            return;
        }
        // sparse-switch: lookupswitch
        int[] keys = new int[payload.payloadKeys.size()];
        Label[] targets = new Label[payload.payloadTargets.size()];
        for (int k = 0; k < keys.length; k++) {
            keys[k] = payload.payloadKeys.get(k);
            targets[k] = labelAt(base + payload.payloadTargets.get(k));
        }
        load(reg, INT);
        mv.visitLookupSwitchInsn(dflt, keys, targets);
    }

    /** Finds a payload instruction by its code offset. */
    private Insn payloadAt(int off) {
        for (Insn p : insns) {
            if (p.codeOffset == off && p.opcode.contains("payload")) {
                return p;
            }
        }
        throw new TxTranslator.TxGap("no payload at " + off);
    }

    private int fallthroughOf(int i) {
        return i + 1 < insns.size() ? insns.get(i + 1).codeOffset
                : insns.get(i).codeOffset + 1;
    }

    // ---------------------------------------------------------------------
    // Invokes
    // ---------------------------------------------------------------------

    private void emitInvoke(int i, Insn in) {
        String op = in.opcode;
        String ref = in.reference;
        int arrow = ref.indexOf("->");
        String owner = ref.substring(0, arrow);
        int open = ref.indexOf('(');
        String name = ref.substring(arrow + 2, open);
        int close = ref.indexOf(')', open);
        String desc = ref.substring(open, close + 1) + ref.substring(close + 1);
        boolean isStatic = op.startsWith("invoke-static");
        int opcode;
        switch (op) {
            case "invoke-virtual": case "invoke-virtual-range":
                opcode = INVOKEVIRTUAL; break;
            case "invoke-super": case "invoke-super-range":
                opcode = INVOKESPECIAL; break;
            case "invoke-direct": case "invoke-direct-range":
                opcode = INVOKESPECIAL; break;
            case "invoke-static": case "invoke-static-range":
                opcode = INVOKESTATIC; break;
            case "invoke-interface": case "invoke-interface-range":
                opcode = INVOKEINTERFACE; break;
            default:
                throw gap("invoke kind " + op, in);
        }
        if (!isStatic) {
            int recv = in.registers.get(0);
            load(recv, OBJ);
            if (opcode == INVOKEVIRTUAL || opcode == INVOKEINTERFACE) {
                // Slot types can widen through CFG merges (and the
                // interpreter tolerated verifier-invalid receivers, which
                // real dx never emits); the idempotent cast keeps the call
                // verifier-safe. Skipped for <init> (uninitialized
                // receivers) and invoke-super (the this receiver is always
                // assignable to the owner). The cast is the receiver load,
                // not the stack top: it must precede the argument pushes.
                if (regUninit[recv]) {
                    throw gap("virtual call on uninitialized instance", in);
                }
                mv.visitTypeInsn(CHECKCAST, TxDescriptors.internal(owner));
            } else if (opcode == INVOKESPECIAL && name.equals("<init>")) {
                if (!regUninit[recv]) {
                    // Calls a constructor twice on one new-instance. The
                    // interpreter tolerated it as disjoint builtins; the
                    // JVM verifier cannot (second <init> on an initialized
                    // ref). Degrade loudly; real dx never emits this.
                    throw gap("second <init> on initialized instance", in);
                }
                regUninit[recv] = false; // constructor call initializes the ref
            }
        }
        // Registers list layout: non-static -> [receiver, arg0, arg1, ...];
        // static -> [arg0, arg1, ...]. Wide args occupy two consecutive
        // register slots (DEX paramWidth), so the cursor advances by width,
        // not by parameter index. /range forms were already expanded into
        // this list by the frontend.
        int r = isStatic ? 0 : 1;
        List<String> ps = paramsOf(ref);
        for (int p = 0; p < ps.size(); p++) {
            String type = ps.get(p);
            if (r >= in.registers.size()) {
                // Fixture-only under-arity invokes (real dx supplies one
                // register per parameter): uninitialized-register semantics —
                // null for refs, zero for primitives.
                if (type.startsWith("L") || type.startsWith("[")) {
                    mv.visitInsn(ACONST_NULL);
                } else if (type.equals("J")) {
                    mv.visitInsn(LCONST_0);
                } else if (type.equals("D")) {
                    mv.visitInsn(DCONST_0);
                } else if (type.equals("F")) {
                    mv.visitInsn(FCONST_0);
                } else {
                    mv.visitInsn(ICONST_0);
                }
                r += TxDescriptors.isWide(type) ? 2 : 1;
                continue;
            }
            int reg = in.registers.get(r);
            load(reg, kindOf(type));
            if (type.startsWith("L") || type.startsWith("[")) {
                // Object params verify by the declared type alone: slot types
                // can widen through CFG merges, and the interpreter shipped
                // verifier-invalid shapes to builtins by role, not by class.
                // Idempotent; JIT elides the redundant check.
                mv.visitTypeInsn(CHECKCAST, TxDescriptors.internal(type));
            }
            r += TxDescriptors.isWide(type) ? 2 : 1;
        }
        if (name.equals("clone") && owner.startsWith("[")) {
            // Array clone: the JVM types the result as Object; consumers
            // (aget/aput/array-length) need the concrete array both on the
            // stack and in the slot. Cast the result and remember the type.
            mv.visitTypeInsn(CHECKCAST, owner);
            pended(owner, owner, i);
            return;
        }
        boolean itf = opcode == INVOKEINTERFACE;
        if (opcode == INVOKEVIRTUAL && isInterface(owner)) {
            // DEX allows interface methodrefs in invoke-virtual (the receiver
            // is the impl instance). The JVM needs invokeinterface there.
            itf = true;
            opcode = INVOKEINTERFACE;
        }
        mv.visitMethodInsn(opcode, TxDescriptors.internal(owner), name, desc,
                itf);
        pended(ref.substring(close + 1), i);
    }

    /** Whether the methodref owner is an interface (guest set, then host). */
    private boolean isInterface(String owner) {
        if (guestInterfaces.contains(owner)) {
            return true;
        }
        // Host types (java.util.Map on a LinkedHashMap receiver): ask the
        // running JVM. Bounded: the owner name only, never guest classes.
        try {
            Class<?> c = Class.forName(
                    TxDescriptors.internal(owner).replace('/', '.'), false,
                    TxMethodWriter.class.getClassLoader());
            return c.isInterface();
        } catch (ClassNotFoundException | LinkageError e) {
            return false;
        }
    }

    // ---------------------------------------------------------------------
    // Arrays
    // ---------------------------------------------------------------------

    private void emitArrayGet(Insn in) {
        int dst = in.registers.get(0);
        int arr = in.registers.get(1);
        int idx = in.registers.get(2);
        String op = in.opcode;
        load(arr, OBJ);
        load(idx, INT);
        if (op.equals("aget-object")) {
            mv.visitInsn(AALOAD);
            store(dst, OBJ);
            regKind[dst] = OBJ;
            regArray[dst] = null;
            return;
        }
        if (op.equals("aget-wide")) {
            char k = wideElement(arr);
            mv.visitInsn(k == DOUBLE ? DALOAD : LALOAD);
            store(dst, k);
            regKind[dst] = k;
            return;
        }
        if (op.equals("aget") || op.equals("aget-boolean") || op.equals("aget-byte")
                || op.equals("aget-char") || op.equals("aget-short")) {
            // Bare aget (0x44) types per the array element; suffixed
            // boolean/byte share baload semantics (zero-extend for boolean).
            String elem = elementOf(arr);
            switch (elem) {
                case "B": case "Z":
                    mv.visitInsn(BALOAD);
                    store(dst, INT);
                    regKind[dst] = INT;
                    break;
                case "C":
                    mv.visitInsn(CALOAD);
                    store(dst, INT);
                    regKind[dst] = INT;
                    break;
                case "S":
                    mv.visitInsn(SALOAD);
                    store(dst, INT);
                    regKind[dst] = INT;
                    break;
                case "I":
                    mv.visitInsn(IALOAD);
                    store(dst, INT);
                    regKind[dst] = INT;
                    break;
                case "F":
                    mv.visitInsn(FALOAD);
                    store(dst, FLOAT);
                    regKind[dst] = FLOAT;
                    break;
                default:
                    throw gap("bare aget element " + elem, in);
            }
            regArray[dst] = null;
        } else {
            throw gap("aget kind " + op, in);
        }
    }

    private void emitArrayPut(Insn in) {
        int value = in.registers.get(0);
        int arr = in.registers.get(1);
        int idx = in.registers.get(2);
        String op = in.opcode;
        load(arr, OBJ);
        load(idx, INT);
        if (op.equals("aput-object")) {
            load(value, OBJ);
            mv.visitInsn(AASTORE);
            return;
        }
        if (op.equals("aput-wide")) {
            char k = wideElement(arr);
            load(value, k);
            mv.visitInsn(k == DOUBLE ? DASTORE : LASTORE);
            return;
        }
        if (op.equals("aput-char")) {
            load(value, 'C');
            mv.visitInsn(CASTORE);
            return;
        }
        if (op.equals("aput-short")) {
            load(value, 'S');
            mv.visitInsn(SASTORE);
            return;
        }
        if (op.equals("aput") || op.equals("aput-boolean") || op.equals("aput-byte")
                || op.equals("aput-char") || op.equals("aput-short")) {
            String elem = elementOf(arr);
            switch (elem) {
                case "B": case "Z":
                    load(value, INT);
                    mv.visitInsn(BASTORE);
                    return;
                case "C":
                    load(value, 'C');
                    mv.visitInsn(CASTORE);
                    return;
                case "S":
                    load(value, 'S');
                    mv.visitInsn(SASTORE);
                    return;
                case "I":
                    load(value, INT);
                    mv.visitInsn(IASTORE);
                    return;
                case "F":
                    load(value, FLOAT);
                    mv.visitInsn(FASTORE);
                    return;
                default:
                    throw gap("bare aput element " + elem, in);
            }
        }
        throw gap("aput kind " + op, in);
    }

    /** Long-vs-double element kind from the array descriptor we tracked. */
    private char wideElement(int arrReg) {
        String arr = regArray[arrReg];
        return "[D".equals(arr) ? DOUBLE : LONG;
    }

    /** Element descriptor of a tracked array register. */
    private String elementOf(int arrReg) {
        String arr = regArray[arrReg];
        if (arr == null) {
            throw new TxTranslator.TxGap(method.owner + "->" + method.signature()
                    + ": unknown array type (register v" + arrReg + ")");
        }
        return TxDescriptors.arrayElement(arr);
    }

    private void emitFilledNewArray(Insn in) {
        boolean range = in.opcode.equals("filled-new-array-range");
        String arrDesc = in.reference; // e.g. [I or [Ljava/lang/String;
        String elem = TxDescriptors.arrayElement(arrDesc);
        int base = TxDescriptors.newarrayBase(elem);
        List<Integer> regs = range ? rangeRegs(in) : in.registers;
        pushInt(regs.size());
        if (base >= 0) {
            mv.visitIntInsn(NEWARRAY, base);
        } else {
            mv.visitTypeInsn(ANEWARRAY, TxDescriptors.internal(elem));
        }
        for (int e = 0; e < regs.size(); e++) {
            mv.visitInsn(DUP);
            pushInt(e);
            int reg = regs.get(e);
            load(reg, kindOf(elem));
            if (base >= 0) {
                switch (elem) {
                    case "J": mv.visitInsn(LASTORE); break;
                    case "F": mv.visitInsn(FASTORE); break;
                    case "D": mv.visitInsn(DASTORE); break;
                    case "C":
                        mv.visitInsn(I2C);
                        mv.visitInsn(CASTORE);
                        break;
                    case "S":
                        mv.visitInsn(I2S);
                        mv.visitInsn(SASTORE);
                        break;
                    default: mv.visitInsn(IASTORE);
                }
            } else {
                mv.visitInsn(AASTORE);
            }
        }
    }

    private List<Integer> rangeRegs(Insn in) {
        List<Integer> out = new ArrayList<>();
        int start = in.registers.get(0);
        int count = in.registers.size() >= 2 ? in.registers.get(1) : 1;
        // 3rc counts encoded by the range length; use the opcode's register
        // list, which Insn already expanded for RegisterRangeInstruction.
        for (int r : in.registers) {
            out.add(r);
        }
        return out;
    }

    private void emitFillArray(Insn in) {
        int arr = in.registers.get(0);
        int off = in.codeOffset + in.branchOffset;
        Insn payload = payloadAt(off);
        String arrDesc = regArray[arr];
        char elemKind;
        if (arrDesc != null) {
            String elem = TxDescriptors.arrayElement(arrDesc);
            elemKind = kindOf(elem);
        } else {
            elemKind = switch (payload.elementWidth) {
                case 1 -> INT;
                case 2 -> INT;
                case 4 -> INT;
                case 8 -> LONG;
                default -> INT;
            };
            if (arrDesc == null) {
                gapLog("fill-array-data array type unknown (width "
                        + payload.elementWidth + ")", in);
            }
        }
        load(arr, OBJ);
        for (int e = 0; e < payload.payloadData.size(); e++) {
            pushInt(e);
            long bits = payload.payloadData.get(e);
            if (elemKind == LONG) {
                mv.visitLdcInsn(bits);
                mv.visitInsn(LASTORE);
            } else if (elemKind == FLOAT) {
                mv.visitLdcInsn(Float.intBitsToFloat((int) bits));
                mv.visitInsn(FASTORE);
            } else if (elemKind == DOUBLE) {
                mv.visitLdcInsn(Double.longBitsToDouble(bits));
                mv.visitInsn(DASTORE);
            } else {
                pushInt((int) bits);
                mv.visitInsn(IASTORE);
            }
            load(arr, OBJ); // re-load the array for the next element
        }
    }

    private void gapLog(String message, Insn in) {
        // Spike policy: soft guesses log through TxGap so tests surface them.
        throw gap(message, in);
    }

    // ---------------------------------------------------------------------
    // Fields
    // ---------------------------------------------------------------------

    private void emitGetField(Insn in, boolean isStatic) {
        String ref = in.reference;
        int arrow = ref.indexOf("->");
        String owner = ref.substring(0, arrow);
        int colon = ref.indexOf(':', arrow);
        String name = ref.substring(arrow + 2, colon);
        String ftype = ref.substring(colon + 1);
        char k = kindOf(ftype);
        int dst = in.registers.get(0);
        String desc = ftype;
        if (isStatic) {
            mv.visitFieldInsn(GETSTATIC, TxDescriptors.internal(owner), name, desc);
        } else {
            int objReg = in.registers.get(1);
            load(objReg, OBJ);
            mv.visitFieldInsn(GETFIELD, TxDescriptors.internal(owner), name, desc);
        }
        store(dst, k);
        regKind[dst] = k;
        regArray[dst] = null;
    }

    private void emitPutField(Insn in, boolean isStatic) {
        String ref = in.reference;
        int arrow = ref.indexOf("->");
        String owner = ref.substring(0, arrow);
        int colon = ref.indexOf(':', arrow);
        String name = ref.substring(arrow + 2, colon);
        String ftype = ref.substring(colon + 1);
        char k = kindOf(ftype);
        int valueReg = in.registers.get(0);
        if (isStatic) {
            load(valueReg, k == OBJ || k == INT || k == FLOAT || k == LONG || k == DOUBLE
                    ? valueKindFor(ftype) : k);
            mv.visitFieldInsn(PUTSTATIC, TxDescriptors.internal(owner), name, ftype);
        } else {
            int objReg = in.registers.get(1);
            load(objReg, OBJ);
            load(valueReg, valueKindFor(ftype));
            mv.visitFieldInsn(PUTFIELD, TxDescriptors.internal(owner), name, ftype);
        }
    }

    /** Loads match the JVM's putfield contract: int for B/C/S/Z. */
    private char valueKindFor(String ftype) {
        char k = kindOf(ftype);
        if (k == INT && (ftype.equals("B") || ftype.equals("S") || ftype.equals("Z")
                || ftype.equals("C"))) {
            return INT;
        }
        return k;
    }

    // ---------------------------------------------------------------------
    // Arithmetic
    // ---------------------------------------------------------------------

    private void emitCompare(Insn in) {
        int dst = in.registers.get(0);
        int a = in.registers.get(1);
        int b = in.registers.get(2);
        String op = in.opcode;
        int opc;
        char kind;
        if (op.equals("cmp-long")) {
            opc = LCMP;
            kind = LONG;
        } else if (op.equals("cmpl-float") || op.equals("cmpg-float")) {
            opc = op.equals("cmpl-float") ? FCMPL : FCMPG;
            kind = FLOAT;
        } else if (op.equals("cmpl-double") || op.equals("cmpg-double")) {
            opc = op.equals("cmpl-double") ? DCMPL : DCMPG;
            kind = DOUBLE;
        } else {
            throw gap("compare " + op, in);
        }
        load(a, kind);
        load(b, kind);
        mv.visitInsn(opc);
        store(dst, INT);
        regKind[dst] = INT;
        regArray[dst] = null;
    }

    private boolean isBinop(String op) {
        return op.startsWith("add-") || op.startsWith("sub-") || op.startsWith("mul-")
                || op.startsWith("div-") || op.startsWith("rem-")
                || op.startsWith("and-") || op.startsWith("or-") || op.startsWith("xor-")
                || op.startsWith("shl-") || op.startsWith("shr-") || op.startsWith("ushr-");
    }

    private boolean isUnop(String op) {
        return op.startsWith("neg-") || op.startsWith("not-") || op.startsWith("int-to-")
                || op.startsWith("long-to-") || op.startsWith("float-to-")
                || op.startsWith("double-to-");
    }

    private void emitUnop(Insn in) {
        int dst = in.registers.get(0);
        int src = in.registers.get(1);
        String op = in.opcode;
        char k = unopKind(op);
        load(src, k);
        switch (op) {
            case "neg-int": mv.visitInsn(INEG); break;
            case "neg-long":
                mv.visitInsn(LNEG);
                k = LONG;
                break;
            case "neg-float":
                mv.visitInsn(FNEG);
                k = FLOAT;
                break;
            case "neg-double":
                mv.visitInsn(DNEG);
                k = DOUBLE;
                break;
            case "not-int":
                mv.visitInsn(ICONST_M1);
                mv.visitInsn(IXOR);
                break;
            case "not-long":
                mv.visitLdcInsn(-1L);
                mv.visitInsn(LXOR);
                k = LONG;
                break;
            case "int-to-long":
                mv.visitInsn(I2L);
                k = LONG;
                break;
            case "int-to-float":
                mv.visitInsn(I2F);
                k = FLOAT;
                break;
            case "int-to-double":
                mv.visitInsn(I2D);
                k = DOUBLE;
                break;
            case "int-to-byte": mv.visitInsn(I2B); break;
            case "int-to-char": mv.visitInsn(I2C); break;
            case "int-to-short": mv.visitInsn(I2S); break;
            case "long-to-int":
                mv.visitInsn(L2I);
                k = INT;
                break;
            case "long-to-float":
                mv.visitInsn(L2F);
                k = FLOAT;
                break;
            case "long-to-double":
                mv.visitInsn(L2D);
                k = DOUBLE;
                break;
            case "float-to-int":
                mv.visitInsn(F2I);
                k = INT;
                break;
            case "float-to-long":
                mv.visitInsn(F2L);
                k = LONG;
                break;
            case "float-to-double":
                mv.visitInsn(F2D);
                k = DOUBLE;
                break;
            case "double-to-int":
                mv.visitInsn(D2I);
                k = INT;
                break;
            case "double-to-long":
                mv.visitInsn(D2L);
                k = LONG;
                break;
            case "double-to-float":
                mv.visitInsn(D2F);
                k = FLOAT;
                break;
            default:
                throw gap("unop " + op, in);
        }
        store(dst, k);
        regKind[dst] = k;
        regArray[dst] = null;
    }

    /** Source kind of an unop; its result kind is set per-op above. */
    private char unopKind(String op) {
        if (op.startsWith("int-to-") || op.startsWith("neg-int") || op.startsWith("not-int")) {
            return INT;
        }
        if (op.startsWith("long-to-") || op.startsWith("neg-long") || op.startsWith("not-long")) {
            return LONG;
        }
        if (op.startsWith("float-to-") || op.startsWith("neg-float")) {
            return FLOAT;
        }
        if (op.startsWith("double-to-") || op.startsWith("neg-double")) {
            return DOUBLE;
        }
        throw new TxTranslator.TxGap("unop kind " + op);
    }

    private void emitBinop(int i, Insn in) {
        String op = in.opcode;
        boolean addr = op.endsWith("-2addr");
        boolean lit = op.endsWith("-lit16") || op.endsWith("-lit8");
        String family;
        if (addr) {
            family = op.substring(0, op.length() - 6);
        } else if (lit) {
            family = op.endsWith("-lit8")
                    ? op.substring(0, op.length() - 5)
                    : op.substring(0, op.length() - 6);
        } else {
            family = familyOf(op);
        }
        char k = familyKind(family);
        int a = in.registers.get(0);
        // vA = vB op lit (or high32 of vB*(lit<<16) for mul-int-high16).
        if (op.equals("mul-int-high16")) {
            int src = in.registers.get(1);
            load(src, INT);
            mv.visitInsn(I2L);
            pushInt((int) (in.literal << 16));
            mv.visitInsn(I2L);
            mv.visitInsn(LMUL);
            pushInt(32);
            mv.visitInsn(LSHR);
            mv.visitInsn(L2I);
            store(a, INT);
            regKind[a] = INT;
            regArray[a] = null;
            return;
        }
        if (lit) {
            // lit16/lit8 binops exist for the int family only.
            int src = in.registers.get(1);
            load(src, k);
            pushInt((int) in.literal);
            mv.visitInsn(binOpcode(family, k));
            store(a, k);
            regKind[a] = k;
            regArray[a] = null;
            return;
        }
        if (addr) {
            load(a, k);
            load(in.registers.get(1), rhsKind(family));
            mv.visitInsn(binOpcode(family, rhsKind(family)));
            store(a, k);
            regKind[a] = k;
            regArray[a] = null;
            return;
        }
        int b2 = in.registers.get(1);
        int c = in.registers.get(2);
        load(b2, k);
        load(c, rhsKind(family));
        mv.visitInsn(binOpcode(family, rhsKind(family)));
        store(a, k);
        regKind[a] = k;
        regArray[a] = null;
    }

    /** Right-operand kind: shifts on longs take an int rhs (DEX aligns). */
    private static char rhsKind(String family) {
        String head = family.substring(0, family.indexOf('-'));
        if ((head.equals("shl") || head.equals("shr") || head.equals("ushr"))
                && family.endsWith("-long")) {
            return INT;
        }
        return familyKind(family);
    }

    /** The DEX op family minus suffixes: add-int/lit16 -> add-int. */
    private String familyOf(String op) {
        int slash = op.indexOf('/');
        if (slash >= 0) {
            return op.substring(0, slash);
        }
        return op;
    }

    private static char familyKind(String family) {
        if (family.endsWith("-int")) {
            return INT;
        }
        if (family.endsWith("-long")) {
            return LONG;
        }
        if (family.endsWith("-float")) {
            return FLOAT;
        }
        if (family.endsWith("-double")) {
            return DOUBLE;
        }
        throw new TxTranslator.TxGap("binop family " + family);
    }

    /** JVM opcode for a binop family (head + width). */
    private static int binOpcode(String family, char kind) {
        String head = family.substring(0, family.indexOf('-'));
        switch (kind) {
            case LONG:
                switch (head) {
                    case "add": return LADD;
                    case "sub": return LSUB;
                    case "mul": return LMUL;
                    case "div": return LDIV;
                    case "rem": return LREM;
                    case "and": return LAND;
                    case "or": return LOR;
                    case "xor": return LXOR;
                    case "shl": return LSHL;
                    case "shr": return LSHR;
                    case "ushr": return LUSHR;
                    default: throw new TxTranslator.TxGap("binop head " + head);
                }
            case FLOAT:
                switch (head) {
                    case "add": return FADD;
                    case "sub": return FSUB;
                    case "mul": return FMUL;
                    case "div": return FDIV;
                    case "rem": return FREM;
                    default: throw new TxTranslator.TxGap("binop head " + head);
                }
            case DOUBLE:
                switch (head) {
                    case "add": return DADD;
                    case "sub": return DSUB;
                    case "mul": return DMUL;
                    case "div": return DDIV;
                    case "rem": return DREM;
                    default: throw new TxTranslator.TxGap("binop head " + head);
                }
            default:
                switch (head) {
                    case "add": return IADD;
                    case "sub": return ISUB;
                    case "mul": return IMUL;
                    case "div": return IDIV;
                    case "rem": return IREM;
                    case "and": return IAND;
                    case "or": return IOR;
                    case "xor": return IXOR;
                    case "shl": return ISHL;
                    case "shr": return ISHR;
                    case "ushr": return IUSHR;
                    default: throw new TxTranslator.TxGap("binop head " + head);
                }
        }
    }

    /** Push a literal of the declared kind (bit pattern for float/double). */
    private void pushLiteral(long lit, char k) {
        switch (k) {
            case LONG:
                mv.visitLdcInsn(lit);
                break;
            case DOUBLE:
                mv.visitLdcInsn(Double.longBitsToDouble(lit));
                break;
            case FLOAT:
                mv.visitLdcInsn(Float.intBitsToFloat((int) lit));
                break;
            case OBJ:
                mv.visitInsn(ACONST_NULL);
                break;
            default:
                pushInt((int) lit);
        }
    }

    private void pushInt(int v) {
        if (v >= 0 && v <= 5) {
            mv.visitInsn(ICONST_0 + v);
        } else if (v == -1) {
            mv.visitInsn(ICONST_M1);
        } else if (v >= Byte.MIN_VALUE && v <= Byte.MAX_VALUE) {
            mv.visitIntInsn(BIPUSH, v);
        } else if (v >= Short.MIN_VALUE && v <= Short.MAX_VALUE) {
            mv.visitIntInsn(SIPUSH, v);
        } else {
            mv.visitLdcInsn(v);
        }
    }
}
