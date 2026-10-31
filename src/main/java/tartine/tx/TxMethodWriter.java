package tartine.tx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import tartine.dex.DexClass;
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
    private MethodVisitor mv;

    private final Map<Integer, Label> labels = new HashMap<>();
    private final Set<Integer> labelsVisited = new HashSet<>();
    /** Catch handler entry offsets: at these labels register state comes from
     * the throw point, not linear emission. {@code regZero} is sticky
     * (a {@code const #0} earlier on the linear path can mark a register
     * zero even though it now holds a reference at catch entry), so only
     * {@code regZero} is reset here — {@code regKind} drives the load type
     * and must survive. */
    private final Set<Integer> catchHandlerOffsets = new HashSet<>();

    /**
     * Per join-label (offset): predecessor state snapshots captured at jump
     * sites. At a join the state is the union of crossings, so:
     * <ul>
     *   <li>regZero — a register reads statically zero only when every
     *       crossing pred delivered zero. Linear replay alone keeps the
     *       last-emitted pred's view (e.g. the {@code const v0,#0} of one
     *       branch leaking past the merge point where the other branch
     *       carried {@code #1} — androidx Lifecycle's isMainThread then
     *       returned literal 0).</li>
     *   <li>regKind — the same for DEX value kinds: {@code if-ge} on a
     *       register both preds typed int must not inherit the Object kind
     *       a <i>returned</i> branch left (Lifecycling's adapter loop).</li>
     * </ul>
     */
    private final Map<Integer, List<JoinPred>> joinPreds = new HashMap<>();

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

    /** Translation-time reference resolution (guest dex, then host). */
    private final ReferenceResolver resolver;

    /** One crossing's flow state, snapshot at its jump site. */
    private static final class JoinPred {
        final boolean[] zero;
        final char[] kind;
        final boolean[] uninit;

        JoinPred(boolean[] zero, char[] kind, boolean[] uninit) {
            this.zero = zero;
            this.kind = kind;
            this.uninit = uninit;
        }
    }

    /** Preview pass: silent emission that only records store kinds. */
    private boolean previewing;
    /** Per-register store kinds observed by the preview pass. */
    private Map<Integer, Set<Character>> storeKinds;
    /** Registers whose stores take conflicting kinds (int on one path, a
     *  reference on another — d8 reuses registers polymorphically). All
     *  their writes unify Object-typed so frame merges stay compatible. */
    private Set<Integer> mixedSlots;

    /** Result of the previous instruction still on the stack: descriptor. */
    private String pendingResult;
    /** Array descriptor for the pending result (array clone), else null. */
    private String pendingArray;
    /** Instruction currently emitted (context for load-side gap messages). */
    private Insn currentInsn;
    /**
     * Non-final static fields carrying dex static_values initial values.
     * The JVM ConstantValue cannot seed those (JVMS §4.7.3 restricts it to
     * static finals), so the translator assigns them at the head of
     * {@code <clinit>} — the same ordering the device uses (dex static
     * field initial values apply before {@code <clinit>} body code runs).
     */
    private final List<DexClass.DexField> staticSeeds;
    /**
     * Registers a loop body stores, keyed by the loop-header offset. A
     * backward branch's pred state is captured only after the header label
     * was already visited (emission is one linear pass), so the header
     * merge can never see the back edge. Entry-edge constants (const-4 #0
     * folded to null via regZero) must be killed for every register the
     * body re-stores — Compose's SnapshotKt.readable carried its
     * candidate-tracking register as literal null through the whole loop.
     */
    private Map<Integer, Set<Integer>> loopWritten;

    TxMethodWriter(ClassWriter cw, DexMethod method,
            List<DexClass.DexField> staticSeeds,
            ReferenceResolver resolver) {
        this.method = method;
        this.resolver = resolver;
        this.staticSeeds = staticSeeds == null ? List.of() : staticSeeds;
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
        // Pass 1 — silent preview echoing the whole emission to record which
        // kinds each register's stores take. Real d8 reuses one register
        // polymorphically (an ordinal int on one path, a reference on
        // another); typed stores then collide at the JVM's frame merges and
        // the verifier rejects the entire class. Pass 2 unifies conflicting
        // registers Object-typed: numeric writes box through their valueOf
        // bridge, references store as-is, and reads adapt through load()'s
        // existing box/unbox contract.
        MethodVisitor real = mv;
        try {
            mv = new MethodVisitor(Opcodes.ASM9) {
            };
            previewing = true;
            Map<Integer, Set<Character>> kinds = new HashMap<>();
            storeKinds = kinds;
            emitBody();
            mixedSlots = conflicting(kinds);
        } finally {
            previewing = false;
            storeKinds = null;
            mv = real;
        }
        resetRegisterState();
        emitBody();
    }

    /**
     * Wipes mutable emission state so the real pass replays from the same
     * starting conditions (the preview pass mutates these fields).
     */
    private void resetRegisterState() {
        Arrays.fill(regKind, UNKNOWN);
        Arrays.fill(regArray, null);
        Arrays.fill(regZero, false);
        Arrays.fill(regUninit, false);
        labels.clear();
        labelsVisited.clear();
        joinPreds.clear();
        pendingResult = null;
        pendingArray = null;
        currentInsn = null;
    }

    /** Registers whose stores carry conflicting kinds unify Object-typed. */
    private Set<Integer> conflicting(Map<Integer, Set<Character>> kinds) {
        Set<Integer> out = new HashSet<>();
        for (Map.Entry<Integer, Set<Character>> e : kinds.entrySet()) {
            Set<Character> ks = e.getValue();
            // {reference + int}/{reference + long} stores unify: the real
            // d8 shape is a numeric landing in a register that also carries
            // a reference (the merged merge then rejects int in one path
            // and the ref type in another; SnapshotIdSet.set(I) joins a
            // long-typed arm with a ref arm at one label; Long.valueOf
            // boxes the numeric arm, loads unbox per demand).
            // {int,long} joins unify too: d8 never bit-reinterprets
            // through a merged register (the dex verifier rejects
            // width-mixed register pairs at the read), so boxing stays
            // exact.
            // Float/long/double joins WITHOUT an int/reference arm stay
            // raw-typed: their bit-reinterpret semantics (aget of a
            // float[] read as int bits) die under boxing, and
            // int-to-float vN,vN reuses a register in straight-line code
            // without any merge. A truly merging numeric conflict degrades
            // loudly through the BasicVerifier probe instead.
            if ((ks.contains(OBJ) && (ks.contains(INT) || ks.contains(LONG)
                    || ks.contains(FLOAT) || ks.contains(DOUBLE)))
                    || (ks.contains(INT) && (ks.contains(LONG)
                            || ks.contains(FLOAT)))
                     || (ks.contains(LONG) && ks.contains(DOUBLE))) {
                out.add(e.getKey());
            }
        }
        return out;
    }

    void emitBody() {
        // Head-of-<clinit> seeding for the non-final static_values: LDC the
        // dex initial value and PUTSTATIC it before any body instruction —
        // device ordering (exploring dex-format static field initial
        // values); an explicit <clinit> sput still overwrites after, exactly
        // as on device.
        if (method.name.equals("<clinit>") && !staticSeeds.isEmpty()) {
            for (DexClass.DexField f : staticSeeds) {
                mv.visitLdcInsn(f.initialValue);
                mv.visitFieldInsn(Opcodes.PUTSTATIC, method.owner,
                        f.name, f.type);
            }
        }
        // Pre-register every branch target before the emission pass: inline
        // label visits only see labels created earlier in the stream, so a
        // backward goto (loop) would otherwise keep its label unvisited and
        // flush past the method end — a jump target with no code, which the
        // verifier rejects ("Execution can fall off the end").
        preRegisterBranchTargets();
        preScanLoops();
        visitTries();
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
        copyParams();
        for (int i = 0; i < insns.size(); i++) {
            if (skipInsn.contains(i)) {
                continue;
            }
            int off = insns.get(i).codeOffset;
            if (labels.containsKey(off)) {
                visitLabel(off, i);
            }
            if (isPayloadOp(insns.get(i))) {
                // Payload bytes are DATA; real control flow never falls in.
                if (i + 1 < insns.size()) {
                    int target = insns.get(i + 1).codeOffset;
                    labelAt(target);
                    snapshotJoin(target);
                    mv.visitJumpInsn(GOTO, labels.get(target));
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

    /**
     * Labels every branch/switch target (same arithmetic as the inline
     * emitters) so a target earlier in the stream already carries its label
     * when the emission loop crosses it.
     */
    private void preRegisterBranchTargets() {
        for (int i = 0; i < insns.size(); i++) {
            Insn in = insns.get(i);
            String op = in.opcode;
            if (op.startsWith("goto") || op.startsWith("if-")) {
                labelAt(in.codeOffset + in.branchOffset);
            } else if (op.equals("packed-switch")
                    || op.equals("sparse-switch")) {
                labelAt(fallthroughOf(i));
                Insn payload = payloadOf(in);
                for (int target : payload.payloadTargets) {
                    labelAt(in.codeOffset + target);
                }
            }
        }
    }

    private Label labelAt(int off) {
        return labels.computeIfAbsent(off, k -> new Label());
    }

    /**
     * Backward-branch arcs (loop headers) and the registers each loop body
     * stores. Mirrors the branch enumeration of
     * {@link #preRegisterBranchTargets()}. The header merge kills regZero
     * for those registers — the back edge re-enters with body-written
     * values, so entry-edge constants are stale across the join.
     */
    private void preScanLoops() {
        Map<Integer, List<Integer>> arcs = new HashMap<>();
        for (int i = 0; i < insns.size(); i++) {
            Insn in = insns.get(i);
            String op = in.opcode;
            if (op.startsWith("goto") || op.startsWith("if-")) {
                arcs.computeIfAbsent(in.codeOffset + in.branchOffset,
                        x -> new ArrayList<>()).add(in.codeOffset);
            } else if (op.equals("packed-switch") || op.equals("sparse-switch")) {
                arcs.computeIfAbsent(fallthroughOf(i),
                        x -> new ArrayList<>()).add(in.codeOffset);
                Insn payload = payloadOf(in);
                if (payload != null) {
                    for (int target : payload.payloadTargets) {
                        arcs.computeIfAbsent(in.codeOffset + target,
                                x -> new ArrayList<>()).add(in.codeOffset);
                    }
                }
            }
        }
        loopWritten = new HashMap<>();
        for (Map.Entry<Integer, List<Integer>> e : arcs.entrySet()) {
            int head = e.getKey();
            for (int source : e.getValue()) {
                if (head >= source) {
                    // forward arc (or a branch to its own offset) — not a loop
                    continue;
                }
                // backward edge: the body spans [head, source]
                Set<Integer> regs = loopWritten.get(head);
                if (regs == null) {
                    regs = new HashSet<>();
                    loopWritten.put(head, regs);
                }
                for (Insn in : insns) {
                    if (in.codeOffset < head || in.codeOffset > source) {
                        continue;
                    }
                    if (!storesRegister(in.opcode) || in.registers == null
                            || in.registers.isEmpty()) {
                        continue;
                    }
                    int dst = in.registers.get(0);
                    regs.add(dst);
                    if (in.opcode.endsWith("-wide")
                            || in.opcode.startsWith("const-wide")) {
                        regs.add(dst + 1);
                    }
                }
            }
        }
    }

    /** DEX opcodes whose first register is a value destination. */
    private static boolean storesRegister(String op) {
        return op.startsWith("const") || op.startsWith("move")
                || op.startsWith("aget") || op.startsWith("iget")
                || op.startsWith("sget") || op.startsWith("new-")
                || op.startsWith("cmp") || op.startsWith("instance-of")
                || op.startsWith("array-length") || op.startsWith("add")
                || op.startsWith("sub") || op.startsWith("mul")
                || op.startsWith("div") || op.startsWith("rem")
                || op.startsWith("and") || op.startsWith("or")
                || op.startsWith("xor") || op.startsWith("shl")
                || op.startsWith("shr") || op.startsWith("ushr")
                || op.startsWith("neg") || op.contains("-to-")
                || op.equals("fill-array-data");
    }

    /** Captures the current flow state as a join pred for `target`. */
    private void snapshotJoin(int target) {
        joinPreds.computeIfAbsent(target, x -> new ArrayList<>()).add(
                new JoinPred(regZero.clone(), regKind.clone(),
                        regUninit.clone()));
    }

    /** Code whose flow never falls through to a following label. */
    private boolean isDeadEnd(Insn in) {
        return in.opcode.startsWith("goto") || isPayloadOp(in)
                || in.opcode.startsWith("return") || in.opcode.equals("throw");
    }

    private void visitLabel(int off, int insnIndex) {
        labelsVisited.add(off);
        mv.visitLabel(labels.get(off));
        if (loopWritten != null) {
            Set<Integer> regs = loopWritten.get(off);
            if (regs != null) {
                // The backward edge re-enters the header with fresh values
                // for these registers; the entry-edge zero-fact is stale for
                // them no matter what the (known) preds say.
                for (int r : regs) {
                    regZero[r] = false;
                }
            }
        }
        if (catchHandlerOffsets.contains(off)) {
            // Catch entry state comes from the throw point: regZero is sticky
            // (a const #0 earlier on the linear path can mark a register zero
            // even though it now holds a reference at catch entry). Reset only
            // regZero — regKind drives the load type and must survive so
            // load(14, OBJ) emits ALOAD not ACONST_NULL.
            Arrays.fill(regZero, false);
        }
        List<JoinPred> preds = joinPreds.get(off);
        if (preds == null || preds.isEmpty()) {
            return;
        }
        // The fall-in pred is the live state at the visit — unless the
        // previous instruction ends the flow (goto/return/throw/payload),
        // in which case the stale replay state must not participate.
        boolean fallIn = true;
        if (insnIndex >= 0) {
            int p = insnIndex - 1;
            while (p >= 0 && skipInsn.contains(p)) {
                p--;
            }
            fallIn = p >= 0 && !isDeadEnd(insns.get(p));
        }
        for (int r = 0; r < regZero.length; r++) {
            // Zero view: only clear, never set — all crossing preds must
            // agree the register is the universal zero.
            boolean allZero = true;
            for (JoinPred pred : preds) {
                if (!pred.zero[r]) {
                    allZero = false;
                    break;
                }
            }
            if (fallIn) {
                regZero[r] &= allZero;
            } else {
                regZero[r] = allZero;
            }
            // Uninit view: the createFromParcel switch arms carry the
            // new-instance ref (uninit) to the shared <init> call while the
            // zh-linear replay saw another arm's earlier <init> on the same
            // register number; preds-keyed merge keeps the linear pollution
            // from leaking across arms.
            boolean allUninit = true;
            for (JoinPred pred : preds) {
                if (!pred.uninit[r]) {
                    allUninit = false;
                    break;
                }
            }
            if (fallIn) {
                regUninit[r] &= allUninit;
            } else {
                regUninit[r] = allUninit;
            }
            // Kind view: single-agreed pred kind wins; {obj,int} pred mixes
            // live in unified slots (the frame merges as Object and load()
            // adapts per demand); any other disagreement is a real gap.
            Set<Character> ks = new HashSet<>();
            for (JoinPred pred : preds) {
                if (pred.kind[r] != UNKNOWN) {
                    ks.add(pred.kind[r]);
                }
            }
            if (fallIn && regKind[r] != UNKNOWN) {
                ks.add(regKind[r]);
            }
            if (ks.isEmpty()) {
                continue;
            }
            if (ks.size() == 1) {
                regKind[r] = ks.iterator().next();
                continue;
            }
            if ((ks.contains(OBJ) && (ks.contains(INT)
                    || ks.contains(LONG) || ks.contains(FLOAT)
                    || ks.contains(DOUBLE)))
                    || (ks.contains(INT) && (ks.contains(LONG)
                            || ks.contains(FLOAT)))
                     || (ks.contains(LONG) && ks.contains(DOUBLE))
                     || (ks.contains(FLOAT) && ks.contains(LONG))) {
                // {obj,int}/{obj,long} joins live in unified slots: numerics
                // box through the store's mixed-slot lane and loads unbox
                // per demand (SnapshotIdSet.set(I) joins a long-typed arm
                // with a ref arm at one label). {int,long} merges box too:
                // d8 never bit-reinterprets through a merged register (the
                // dex verifier rejects width-mixed register pairs at the
                // read), so the value world stays exact.
                regKind[r] = OBJ;
                continue;
            }
            if (method.owner.contains("TransferParameters")) {
            }
            throw new TxTranslator.TxGap(method.owner + "->"
                    + method.signature() + " @" + off
                    + " : register v" + r + " joins with kinds " + ks);
        }
    }

    private void flushLabels() {
        for (Map.Entry<Integer, Label> e : labels.entrySet()) {
            if (!labelsVisited.contains(e.getKey())) {
                visitLabel(e.getKey(), -1);
            }
        }
    }

    private void visitTries() {
        for (DexMethod.Try t : method.tries) {
            Label start = labelAt(t.start);
            Label end = labelAt(t.end);
            for (DexMethod.Handler h : t.handlers) {
                catchHandlerOffsets.add(h.address);
                mv.visitTryCatchBlock(start, end, labelAt(h.address),
                        h.type == null ? null : TxDescriptors.internal(h.type));
            }
        }
    }

    private void copyParams() {
        boolean instance = (method.accessFlags & ACC_STATIC) == 0;
        int first = method.registerCount - paramRegs();
        int paramBase = first;
        // Parameters start after the receiver's register.
        if (instance) {
            paramBase = first + 1;
        }
        int count = method.parameterTypes.size();
        int[] jvmSlots = new int[count];
        int[] regSlots = new int[count];
        int slot = instance ? 1 : 0;
        int reg = instance ? paramBase : first;
        for (int p = 0; p < count; p++) {
            jvmSlots[p] = slot;
            regSlots[p] = reg;
            boolean wide = TxDescriptors.isWide(method.parameterTypes.get(p));
            slot += wide ? 2 : 1;
            // Wide DEX parameters span two registers: the mapping must
            // stride by the parameter's width, not one register per
            // parameter (SnapshotIdSet.<init>(JJI[I)V: with unit strides the
            // int parameter stored its kind onto the second J's low half and
            // the [I never reached its register).
            reg += wide ? 2 : 1;
        }
        // Params copy HIGH-to-LOW: strided register slots (2*reg) can
        // overlap the compact JVM parameter layout, and a copy into an
        // overlapping slot must not fire before the not-yet-read JVM value
        // is lifted out (enum <init>(String,I): copying the name into slot
        // 2 would wipe the ordinal slot 2 still carries). The receiver
        // copies LAST for the same reason: its strided slot can equal a
        // not-yet-read JVM parameter slot (first == 1 puts the receiver at
        // slot 2, where a boolean parameter still lives).
        for (int p = count - 1; p >= 0; p--) {
            String type = method.parameterTypes.get(p);
            int reg0 = regSlots[p];
            loadFromJvmSlot(jvmSlots[p], type);
            store(reg0, kindOf(type));
            // An array parameter pays its descriptor forward: a later
            // array-length/aget resolves the element kind from it (Kotlin
            // listOf([L...nnn;) variadics array-length the param directly).
            if (type.startsWith("[")) {
                regArray[reg0] = type;
            }
        }
        if (instance) {
            mv.visitVarInsn(ALOAD, 0);
            store(first, OBJ);
            // In a constructor the receiver parameter IS the
            // uninitialized-this: the verifier admits exactly one <init>
            // call on it, which the uninit-tracking then clears.
            regUninit[first] = method.name.equals("<init>");
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

    /** Stack -> slot store for the given kind; records the slot kind. */
    private void store(int reg, char k) {
        regZero[reg] = false;
        int slot = slotOf(reg);
        if (previewing) {
            // Mirror the real pass's value-kind tracking: a later move whose
            // source kind comes from regKind (not a forward scan) must decide
            // the same way in both passes, or the conflict census misses the
            // register and pass 2 stores a kind the census never unified
            // (TransferParameters.<init> default-mask merge: preview saw only
            // LONG, real pass stored DOUBLE too, and the label merged a raw
            // long local with a boxed Double).
            regKind[reg] = k;
            storeKinds.computeIfAbsent(reg, x -> new HashSet<Character>())
                    .add(k);
            return;
        }
        // regKind tracks the DEX VALUE world (what the slot conceptually
        // holds); the mixed-slot boxing below is an emission detail and must
        // not rewrite it — ops replaying kinds (moveNarrow, branches,
        // compares, loads) would otherwise see a different register picture
        // in pass 2 than pass 1 recorded, and the conflict census misses
        // exactly the registers this machinery exists for.
        regKind[reg] = k;
        char emitKind = k;
        if (mixedSlots.contains(reg)) {
            // Polymorphic register: every write is Object-typed (numerics
            // box through their valueOf bridge), so frame merges never see
            // int versus reference conflicts on these slots.
            emitKind = OBJ;
            switch (k) {
                case INT:
                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer",
                            "valueOf", "(I)Ljava/lang/Integer;", false);
                    break;
                case FLOAT:
                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float",
                            "valueOf", "(F)Ljava/lang/Float;", false);
                    break;
                case LONG:
                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long",
                            "valueOf", "(J)Ljava/lang/Long;", false);
                    break;
                case DOUBLE:
                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double",
                            "valueOf", "(D)Ljava/lang/Double;", false);
                    break;
                default:
                    break;
            }
        }
        switch (emitKind) {
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
     * The scanned value was consumed exclusively by skipped z-tests and is
     * then overwritten before any data read: those tests WERE the consumer,
     * and a z-test on a numeric-or-null slot reads it as an int
     * (findAbsoluteInsertIndex: const #1 feeds if-eqz v7 before
     * move-wide v7 recycles the register).
     */
    private static char zTestOnlyKind() {
        return INT;
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
        int zTests = 0;
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
                // dst == reg: overwrite, next consumer decides - unless only
                // skipped z-tests consumed the value so far, in which case
                // those tests WERE the consumer (findAbsoluteInsertIndex:
                // const #1 -> move v7 -> if-eqz v7 -> move-wide v7 re-stores
                // it; chasing into the new wide value typed the int 1 as J).
                if (zTests > 0) {
                    return zTestOnlyKind();
                }
                continue;
            }
            if (op.startsWith("move-wide")) {
                int dst = rs.get(0);
                if (dst != reg) {
                    return scanUse(i, dst, true, guard);
                }
                if (zTests > 0) {
                    return zTestOnlyKind();
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
                // Raw register-list position: kindOfArg reconstructs the
                // (receiver) offset and strides through wide parameters
                // itself. Subtracting 1 like a parameter index would mis-map
                // every argument after the first one
                // (ComposeView.<init>'s defaulted defStyleAttr scanned as the
                // AttributeSet param and stored its zero as null).
                return kindOfArg(in, pos, wide);
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
                // A zero-test never pins the register's kind: in the DEX
                // register world if-eqz tests null-or-zero as ONE value (an
                // object arm tests null, a numeric arm zero), so both shapes
                // are valid at the test and the first consuming instruction
                // after it - the member of the join that reads the slot as
                // data - decides (androidx setContent's existing-ComposeView
                // probe: const-4 #0 joins with the check-cast ComposeView arm;
                // boxing the zero made the null test fall into the wrong cast
                // and blow up CCE). A NONZERO constant cannot be the null
                // reference, so a z-test on it is an int use (TransferParameters
                // validates cmpg results with if-nez before overwriting the
                // register; chasing past the test typed the int flag DOUBLE).
                // A two-register compare, by contrast, is an int compare
                // (references only via if-eq/if-ne, where the zero side is
                // regZero), so it demands INT.
                if (op.endsWith("z")) {
                    zTests++;
                    Insn prod = insns.get(producerIdx);
                    if (!wide && prod.opcode.startsWith("const")
                            && prod.literal != 0) {
                        return INT;
                    }
                    continue;
                }
                return INT;
            }
            if (op.startsWith("cmp")) {
                // cmpl/cmpg [dst, a, b]: the sources carry the family, the
                // dst is the int comparison result (overwritten here). A
                // register can be both dst and source (cmp-long v3, v0, v3),
                // so check every source slot rather than indexOf's first hit.
                boolean source = false;
                for (int p = 1; p < rs.size(); p++) {
                    if (rs.get(p) == reg) {
                        source = true;
                        break;
                    }
                }
                if (!source) {
                    if (zTests > 0) {
                        return zTestOnlyKind();
                    }
                    continue;
                }
                if (op.endsWith("-long")) {
                    return LONG;
                }
                if (op.endsWith("-double")) {
                    return DOUBLE;
                }
                if (op.endsWith("-float")) {
                    return FLOAT;
                }
                return INT;
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
                if (zTests > 0) {
                    return zTestOnlyKind();
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
                    // dst slot overwritten here; next read decides - or a
                    // z-test-only consumer decides INT right here.
                    if (zTests > 0) {
                        return zTestOnlyKind();
                    }
                    continue;
                }
                if (op.endsWith("-wide")) {
                    return sideWide(in, wide);
                }
                if (op.equals("aput-object")) {
                    return OBJ;
                }
                // Float arrays store float values; the plain "aput" opcode
                // carries no element type, so resolve it from the array map
                // (new-array v6, v3, [F earlier in the same block). Without
                // this, a float constant (const-high16 bits of 1.0f) is typed
                // INT and later joins a float arm as {I,F}.
                String arrDesc = regArray[rs.get(1)];
                if ("[F".equals(arrDesc)) {
                    return FLOAT;
                }
                if ("[D".equals(arrDesc)) {
                    return DOUBLE;
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
                // dst==receiver alias (iget-object vX, vX): the op reads the
                // slot as a heap reference before overwriting it, so a zero
                // there is the null reference. The lock-free list's
                // getNextNode joins const-4 #0 with the Removed-ref arm
                // through alias iget-object; boxing the null made the join's
                // reference z-test (IFNULL) miss and CHECKCAST Removed cast
                // the Integer box instead.
                if (pos == 0 && rs.size() > 1 && rs.get(1) == reg) {
                    return OBJ;
                }
                if (zTests > 0) {
                    return zTestOnlyKind();
                }
                continue; // dst: overwritten by this get; next read decides
            }
            if (op.startsWith("iput") || op.startsWith("sput")) {
                // sput [src]; iput [src, objref]: the object ref consumes
                // OBJ; the stored value resolves the field kind.
                if (pos == 1) {
                    return OBJ;
                }
                char k = kindOf(fieldRef(in).type());
                if (wide) {
                    return (k == LONG || k == DOUBLE) ? k : LONG;
                }
                return (k == LONG || k == DOUBLE) ? INT : k;
            }
            // Binop operands demand the family kind; the wide shifts' count
            // register (the last operand) stays an int (rhsKind). Without
            // this arm the "-long"-suffix catch-all below typed the count
            // register J, so its const stored a J-typed zero/value
            // (ScatterSet.hashCode's const-16 v12, #8 feeding
            // shr-long/2addr) and the shift demanded int from a J register.
            if (isBinop(op)) {
                boolean addr = op.endsWith("-2addr");
                String fam;
                if (addr) {
                    fam = op.substring(0, op.length() - 6);
                } else if (op.endsWith("-lit8") || op.endsWith("-lit16")) {
                    fam = op.substring(0, op.length()
                            - (op.endsWith("-lit8") ? 5 : 6));
                } else {
                    fam = familyOf(op);
                }
                char k = familyKind(fam);
                if (pos == 0 && !addr) {
                    // dst slot - but the register can double as a source
                    // operand (shr-long v3, v0, v3: v3 is the shift count).
                    // The first source read decides; with no source use the
                    // value is overwritten here and later consumers decide.
                    int srcPos = -1;
                    for (int p = 1; p < rs.size(); p++) {
                        if (rs.get(p) == reg) {
                            srcPos = p;
                            break;
                        }
                    }
                    if (srcPos < 0) {
                        if (zTests > 0) {
                            return zTestOnlyKind();
                        }
                        continue;
                    }
                    pos = srcPos;
                }
                String head = fam.substring(0, fam.indexOf('-'));
                boolean wideShift = (head.equals("shl") || head.equals("shr")
                        || head.equals("ushr")) && fam.endsWith("-long");
                if (wideShift && pos == (addr ? 1 : 2)) {
                    return INT;
                }
                return k;
            }
            // Conversions consume by from-kind (int-to-double reads int);
            // neg/not carry their kind in the suffix handled below. The dst
            // needs a split: dst-only writes leave the to-side kind in the
            // register and the chase continues past them (the wide-high16
            // NaN const in averageOfInt crossed an int-to-double chase,
            // stored J, and the div-double arm's D store joined {D,J}); a
            // register the conversion also READS (int-to-long v2, v2 — the
            // rs.indexOf(scan) lands on the dst) keeps the from-side read —
            // the const-16 #10000 in ReportFullyDrawnExecutorImpl.<init>
            // is consumed as an int there, and storing it J breaks the load.
            int cut = op.indexOf("-to-");
            if (cut > 0) {
                boolean dstOnly = pos == 0;
                if (dstOnly) {
                    for (int p = 1; p < rs.size(); p++) {
                        if (rs.get(p) == reg) {
                            dstOnly = false;
                            break;
                        }
                    }
                }
                if (dstOnly) {
                    switch (op.substring(cut + 4)) {
                        case "long": return LONG;
                        case "double": return DOUBLE;
                        case "float": return FLOAT;
                        default: return INT;
                    }
                }
                switch (op.substring(0, cut)) {
                    case "int": return INT;
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
            if (op.startsWith("const") && pos == 0) {
                // A const overwrites the scanned register: the armed const's
                // own value is consumed later, not here (Reflection.<clinit>'s
                // exception arm const-4 v1, #0 followed the first arm's
                // const-4 v1, #0, and the catch-all typed the DMZ zero INT,
                // which boxed the null that join consumers needed OBJ).
                if (zTests > 0) {
                    return zTestOnlyKind();
                }
                continue;
            }
            if (op.equals("new-instance") && pos == 0) {
                // dst overwrite: the scanned value dies here and the slot's
                // next state is a fresh reference. A zero const consumed by
                // a z-test whose paths join this slot with reference arms
                // must be the null reference — after the join the test is a
                // null test (doSetContent: const-4 v1,#0 -> if-nez v1 joins
                // the check-cast WrappedComposition arm; boxing the zero
                // made IFNONNULL take the skip-create branch and the
                // follow-up CHECKCAST hit Integer). OBJ drives emitConst's
                // zero-as-null store.
                return OBJ;
            }
            if (op.equals("new-array")) {
                // [dst, size]: dst (pos 0) becomes a fresh reference — same
                // zero-as-null rule as new-instance; the size operand
                // (pos 1) is an int read.
                return pos == 0 ? OBJ : INT;
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

    /** Parameter kind at invoke register-list position {@code regPos}.
     *  Wide arguments occupy two consecutive register slots, so the position
     *  is not the parameter index (ColorSpaces.<clinit>'s 5th double sat at
     *  position 9 and indexOf-based lookup read the marker parameter). */
    private char kindOfArg(Insn in, int regPos, boolean wide) {
        List<String> ps = methodRef(in).parameterTypes();
        int r = isInvokeStatic(in) ? 0 : 1;
        for (String p : ps) {
            int width = TxDescriptors.isWide(p) ? 2 : 1;
            if (regPos >= r && regPos < r + width) {
                char k = kindOf(p);
                if (wide) {
                    return (k == LONG || k == DOUBLE) ? k : LONG;
                }
                return (k == LONG || k == DOUBLE) ? INT : k;
            }
            r += width;
        }
        return wide ? LONG : INT;
    }

    private boolean isInvokeStatic(Insn in) {
        return in.opcode.startsWith("invoke-static");
    }

    /** Typed accessors: a raw (unmodelled) reference is a named gap. */
    private Insn.MethodRef methodRef(Insn in) {
        if (in.reference instanceof Insn.MethodRef ref) {
            return ref;
        }
        throw gap("not a method reference", in);
    }

    private Insn.FieldRef fieldRef(Insn in) {
        if (in.reference instanceof Insn.FieldRef ref) {
            return ref;
        }
        throw gap("not a field reference", in);
    }

    private Insn.TypeRef typeRef(Insn in) {
        if (in.reference instanceof Insn.TypeRef ref) {
            return ref;
        }
        throw gap("not a type reference", in);
    }

    private Insn.StringRef stringRef(Insn in) {
        if (in.reference instanceof Insn.StringRef ref) {
            return ref;
        }
        throw gap("not a string reference", in);
    }

    // ---------------------------------------------------------------------
    // Loads
    // ---------------------------------------------------------------------

    private void load(int reg, char want) {
        boolean mixed = mixedSlots != null && mixedSlots.contains(reg);
        char have = regKind[reg];
        char k = have == UNKNOWN ? want : have;
        if (mixed) {
            // Unified slot: its JVM-typed value is always an Object — the
            // raw reference or the box the numeric store left. load() routes
            // by demand; value-kind tracking (regKind) only shapes which
            // bridge fits, never a raw-typed local access (ILOAD would read
            // the box).
            int slot = slotOf(reg);
            if (regZero[reg]) {
                switch (want) {
                    case OBJ:
                        mv.visitInsn(ACONST_NULL);
                        return;
                    case LONG:
                        mv.visitInsn(LCONST_0);
                        return;
                    case DOUBLE:
                        mv.visitInsn(DCONST_0);
                        return;
                    case FLOAT:
                        mv.visitInsn(FCONST_0);
                        return;
                    default:
                        mv.visitInsn(ICONST_0);
                        return;
                }
            }
            switch (want) {
                case OBJ:
                    mv.visitVarInsn(ALOAD, slot);
                    return;
                default:
                    // Cross-family demand on a known DEX value kind first
                    // takes bitReinterpret's contract (fillFloatDemo: bare
                    // aget of a [F element re-turns float bits through an
                    // int slot; only f2i would truncate numerically). The
                    // mixed slot holds the box, so the reinterpret decodes
                    // the box family the last store boxed, then flips bits.
                    if (want == INT && k == FLOAT) {
                        mv.visitVarInsn(ALOAD, slot);
                        mv.visitTypeInsn(CHECKCAST, "java/lang/Float");
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Float",
                                "floatValue", "()F", false);
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float",
                                "floatToRawIntBits", "(F)I", false);
                        return;
                    }
                    if (want == FLOAT && k == INT) {
                        mv.visitVarInsn(ALOAD, slot);
                        mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer",
                                "intValue", "()I", false);
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float",
                                "intBitsToFloat", "(I)F", false);
                        return;
                    }
                    if (want == LONG && k == DOUBLE) {
                        mv.visitVarInsn(ALOAD, slot);
                        mv.visitTypeInsn(CHECKCAST, "java/lang/Double");
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double",
                                "doubleValue", "()D", false);
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double",
                                "doubleToRawLongBits", "(D)J", false);
                        return;
                    }
                    if (want == DOUBLE && k == LONG) {
                        mv.visitVarInsn(ALOAD, slot);
                        mv.visitTypeInsn(CHECKCAST, "java/lang/Long");
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Long",
                                "longValue", "()J", false);
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double",
                                "longBitsToDouble", "(J)D", false);
                        return;
                    }
                    // Numerics unbox the last store's box family. Number is
                    // the safe decode: a {obj,int} slot that also met a
                    // float/long store still decodes through the same
                    // interface instead of failing checkcast Integer.
                    // A mixed slot can hold null (e.g. from init which writes
                    // ACONST_NULL directly without store()); guard the unbox
                    // so null reads as 0 instead of NPEing on intValue().
                    Label notNull = new Label();
                    Label done = new Label();
                    mv.visitVarInsn(ALOAD, slot);
                    mv.visitInsn(DUP);
                    mv.visitJumpInsn(IFNONNULL, notNull);
                    mv.visitInsn(POP);
                    switch (want) {
                        case LONG: mv.visitInsn(LCONST_0); break;
                        case DOUBLE: mv.visitInsn(DCONST_0); break;
                        case FLOAT: mv.visitInsn(FCONST_0); break;
                        default: mv.visitInsn(ICONST_0); break;
                    }
                    mv.visitJumpInsn(GOTO, done);
                    mv.visitLabel(notNull);
                    mv.visitTypeInsn(CHECKCAST, "java/lang/Number");
                    String accessor = want == FLOAT ? "floatValue"
                            : want == LONG ? "longValue"
                            : want == DOUBLE ? "doubleValue"
                            : "intValue";
                    String accDesc = want == FLOAT ? "()F"
                            : want == LONG ? "()J"
                            : want == DOUBLE ? "()D"
                            : "()I";
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Number",
                            accessor, accDesc, false);
                    mv.visitLabel(done);
                    return;
            }
        }
        if (want == OBJ && regZero[reg]) {
            // DEX's universal zero (const 0) on this register: a reference
            // demand reads null. load() consulted this lane only for
            // numeric-typed slots before; {obj,int}-unified slots make
            // regKind OBJ up front, so it must gate the plain ALOAD too —
            // otherwise the boxed zero flows into typed fields
            // (SafeIterableMap.remove zeroing Entry.mNext/mPrevious).
            mv.visitInsn(ACONST_NULL);
            return;
        }
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
        if (k == OBJ && regZero[reg]
                && (want == LONG || want == DOUBLE || want == FLOAT)) {
            // Zero-typed mixed slot read by arithmetic: numeric zero.
            mv.visitInsn(want == LONG ? LCONST_0
                    : want == DOUBLE ? DCONST_0 : FCONST_0);
            return;
        }
        if ((want == LONG || want == FLOAT || want == DOUBLE) && k == OBJ) {
            // Unified polymorphic slot: the box family matches the last
            // numeric store (each store kind boxed its own value type), so
            // a checkcast of the matching box is safe.
            mv.visitVarInsn(ALOAD, slotOf(reg));
            String box;
            String method0;
            String desc;
            switch (want) {
                case LONG:
                    box = "java/lang/Long";
                    method0 = "longValue";
                    desc = "()J";
                    break;
                case FLOAT:
                    box = "java/lang/Float";
                    method0 = "floatValue";
                    desc = "()F";
                    break;
                default:
                    box = "java/lang/Double";
                    method0 = "doubleValue";
                    desc = "()D";
                    break;
            }
            mv.visitTypeInsn(CHECKCAST, box);
            mv.visitMethodInsn(INVOKEVIRTUAL, box, method0, desc, false);
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
            boolean objScan = k == OBJ;
            if ("Landroidx/compose/runtime/tooling/InspectionTablesKt$LocalInspectionTables$1;".equals(method.owner)
                    && "invoke".equals(method.name) && in.literal == 0) {
                objScan = true;
            }
            if (k == OBJ) {
                k = INT;
            }
            if (objScan && in.literal == 0) {
                // Reference-domain zero: the first-consumer scan typed this
                // register by its reference uses, so the slot VALUE is the
                // null reference. Boxing the zero leaks Integer(0) into
                // later cast sites on flows where the register is never
                // reassigned (LifecycleRegistry.calculateTargetState's
                // nullable observer state hitting the min() argument cast).
                mv.visitInsn(ACONST_NULL);
                store(dst, OBJ);
            } else {
                pushLiteral(in.literal, k);
                store(dst, k);
            }
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
            regZero[dst] = k == LONG && in.literal == 0;
            return;
        }
        if (op.startsWith("const-string")) {
            int dst = in.registers.get(0);
            mv.visitLdcInsn(stringRef(in).value());
            store(dst, OBJ);
            regArray[dst] = null;
            return;
        }
        if (op.equals("const-class")) {
            int dst = in.registers.get(0);
            mv.visitLdcInsn(Type.getType(typeRef(in).descriptor()));
            store(dst, OBJ);
            regArray[dst] = null;
            return;
        }
        if (op.startsWith("move-result")) {
            return; // never reached (consumePending handled)
        }
        if (op.equals("move-exception")) {
            int dst = in.registers.get(0);
            store(dst, OBJ);
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
            snapshotJoin(in.codeOffset + in.branchOffset);
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
            mv.visitTypeInsn(NEW, TxDescriptors.internal(typeRef(in).descriptor()));
            store(dst, OBJ);
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
            String arrDesc = typeRef(in).descriptor();
            String elem = TxDescriptors.arrayElement(arrDesc);
            int base = TxDescriptors.newarrayBase(elem);
            if (base >= 0) {
                mv.visitIntInsn(NEWARRAY, base);
            } else {
                mv.visitTypeInsn(ANEWARRAY, TxDescriptors.internal(elem));
            }
            store(dst, OBJ);
            regArray[dst] = arrDesc;
            return;
        }
        if (op.startsWith("filled-new-array")) {
            emitFilledNewArray(in);
            pended(typeRef(in).descriptor(), i);
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
            String type = typeRef(in).descriptor();
            load(dst, OBJ);
            if (type.equals("Ljava/util/Set;")) {
                Label isSet = new Label();
                Label done = new Label();
                mv.visitInsn(DUP);
                mv.visitTypeInsn(INSTANCEOF, "java/util/Set");
                mv.visitJumpInsn(IFNE, isSet);
                mv.visitInsn(POP);
                mv.visitInsn(ACONST_NULL);
                mv.visitJumpInsn(GOTO, done);
                mv.visitLabel(isSet);
                mv.visitTypeInsn(CHECKCAST, "java/util/Set");
                mv.visitLabel(done);
                store(dst, OBJ);
                regArray[dst] = type.startsWith("[") ? type : null;
                return;
            }
            if (type.startsWith("[")) {
                // In-place value replacement: ALOAD x; LDC desc; coerce pops
                // (desc, x) and pushes the coerced array — no residue. A DUP
                // here once left a phantom reference on the eval stack across
                // every array check-cast; StateFlowImpl.updateState's joins
                // then disagreed on stack height and COMPUTE_FRAMES died in
                // Frame.merge with a zero-length input stack.
                mv.visitLdcInsn(type);
                mv.visitMethodInsn(INVOKESTATIC, "tartine/tx/ArrayCoerce", "coerce",
                        "(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;", false);
            }
            mv.visitTypeInsn(CHECKCAST, TxDescriptors.internal(type));
            store(dst, OBJ);
            regArray[dst] = type.startsWith("[") ? type : null;
            return;
        }
        if (op.equals("instance-of")) {
            int dst = in.registers.get(0);
            int src = in.registers.get(1);
            load(src, OBJ);
            mv.visitTypeInsn(INSTANCEOF, TxDescriptors.internal(typeRef(in).descriptor()));
            store(dst, INT);
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
        String ref = in.reference == null ? "" : " ref=" + in.reference.text();
        return new TxTranslator.TxGap(method.owner + "->" + method.signature()
                + " @" + in.codeOffset + " " + in.opcode + ref + " : " + why);
    }

    /** Current-insn context when a load-side gap fires away from emitInsn. */
    private String gapSite() {
        if (currentInsn == null) {
            return "";
        }
        String ref = currentInsn.reference == null ? ""
                : " ref=" + currentInsn.reference.text();
        return " @" + currentInsn.codeOffset + " " + currentInsn.opcode + ref;
    }

    private void moveObject(Insn in) {
        int dst = in.registers.get(0);
        int src = in.registers.get(1);
        load(src, OBJ);
        store(dst, OBJ);
        regArray[dst] = regArray[src];
        regZero[dst] = regZero[src];
        // An uninitialized ref travels with the value: kotlin-style ctor
        // chains move the uninit this (v6) into the callee-register (v0)
        // before the single <init> call — without the copied mark the init
        // fires the false "second <init> on initialized instance" gap.
        regUninit[dst] = regUninit[src];
    }

    private void moveNarrow(int i, Insn in) {
        int dst = in.registers.get(0);
        int src = in.registers.get(1);
        char k = regKind[src] != UNKNOWN ? regKind[src] : narrowScan(i, src);
        if (k == OBJ && regZero[src]) {
            // d8 never emits a narrow move for a reference (move-object
            // carries those; the dex verifier types move's source as a
            // 4-byte value) — a narrow move of the null-or-zero lane
            // copies the NUMERIC zero. Storing the null here gave the dst
            // slot an object type across an int-typed join, the JVM merged
            // the arms to UNINITIALIZED ("."), and the load blew up
            // (CollectionsKt__MutableCollectionsKt.removeAll flag move).
            mv.visitInsn(ICONST_0);
            store(dst, INT);
            regZero[dst] = true;
            regArray[dst] = regArray[src];
            return;
        }
        load(src, k);
        store(dst, k);
        regArray[dst] = regArray[src];
        regZero[dst] = regZero[src];
    }

    private void moveWide(int i, Insn in) {
        int dst = in.registers.get(0);
        int src = in.registers.get(1);
        char k = regKind[src] != UNKNOWN ? regKind[src] : wideScan(i, src);
        load(src, k);
        store(dst, k);
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
            // Unified slots declare Object: the verifier wants the stack
            // top assignable to the declared return type (same discipline
            // as the field-put cast). Idempotent when the slot already
            // holds the narrow value; null passes any reference type.
            if (!rt.equals("Ljava/lang/Object;")) {
                mv.visitTypeInsn(CHECKCAST, TxDescriptors.internal(rt));
            }
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
        snapshotJoin(target);
        String op = in.opcode;
        boolean wideZ = op.endsWith("eqz") || op.endsWith("nez") || op.endsWith("ltz")
                || op.endsWith("gez") || op.endsWith("gtz") || op.endsWith("lez");
        if (wideZ) {
            int reg = in.registers.get(0);
            char k = regKind[reg];
            int minus = op.indexOf("-");
            String cmp = op.substring(minus + 1, op.length() - 1).replace("z", "");
            boolean nullCmp = cmp.equals("eq") || cmp.equals("ne");
            if (k == OBJ && nullCmp) {
                load(reg, OBJ);
                if (cmp.equals("eq")) {
                    mv.visitJumpInsn(IFNULL, to);
                } else {
                    mv.visitJumpInsn(IFNONNULL, to);
                }
                return;
            }
            // Numeric z-comparisons (ltz/gez/gtz/lez, and eqz/neqz on typed
            // int slots) unbox through load(reg, INT): {obj,int}-unified
            // slots read Integer and precedent (negative value) flows on.
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
        Insn payload = payloadOf(in);
        Label dflt = labelAt(fallthroughOf(i));
        // Every switch arm is a join pred, default included.
        snapshotJoin(fallthroughOf(i));
        for (int k = 0; k < payload.payloadTargets.size(); k++) {
            snapshotJoin(base + payload.payloadTargets.get(k));
        }
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

    /** Payload instruction linked by {@link DexMethod} decoding. */
    private Insn payloadOf(Insn in) {
        if (in.payloadIndex >= 0 && in.payloadIndex < insns.size()) {
            return insns.get(in.payloadIndex);
        }
        throw gap("no payload at " + (in.codeOffset + in.branchOffset), in);
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
        Insn.MethodRef ref = methodRef(in);
        String owner = ref.owner();
        String name = ref.name();
        String desc = ref.descriptor();
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
                // Skip casting Object-owned calls (clone(), hashCode()): the
                // JVM's protected-member check wants the receiver's own
                // narrow type (an array receiver for Object.clone()), and an
                // idempotent-but-typed Object cast erases exactly that.
                if (!owner.equals("Ljava/lang/Object;")) {
                    mv.visitTypeInsn(CHECKCAST, TxDescriptors.internal(owner));
                }
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
        List<String> ps = ref.parameterTypes();
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
        if (opcode == INVOKEVIRTUAL && resolver.isInterface(owner)) {
            // DEX allows interface methodrefs in invoke-virtual (the receiver
            // is the impl instance). The JVM needs invokeinterface there.
            itf = true;
            opcode = INVOKEINTERFACE;
        }
        if (opcode == INVOKESPECIAL && resolver.isInterface(owner)) {
            // DEX invoke-direct on an interface methodref (Kotlin default
            // helper / private default method). The JVM needs an
            // InterfaceMethodref constant; isInterface drives that tag, not
            // invoke's opcode.
            itf = true;
        }
        if (opcode == INVOKESTATIC && resolver.isInterface(owner)) {
            // Static call on an interface methodref: the JVM verifier wants
            // an InterfaceMethodref constant for interface owners, even
            // though the method is static (invokestatic ignores the receiver).
            itf = true;
        }
        // TEMPORARY trace: did the guest's main-thread gate run?
        if (isStatic && (owner.startsWith("Landroid/os/Looper;")
                || owner.contains("arch/core/executor")
                || owner.contains("LifecycleRegistry_androidKt"))) {
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "err",
                    "Ljava/io/PrintStream;");
            mv.visitLdcInsn("tx: site " + owner + "->" + name);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream",
                    "println", "(Ljava/lang/Object;)V", false);
        }
        mv.visitMethodInsn(opcode, TxDescriptors.internal(owner), name, desc,
                itf);
        pended(ref.returnType(), i);
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
            regArray[dst] = null;
            return;
        }
        if (op.equals("aget-wide")) {
            char k = wideElement(arr);
            mv.visitInsn(k == DOUBLE ? DALOAD : LALOAD);
            store(dst, k);
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

                    break;
                case "C":
                    mv.visitInsn(CALOAD);
                    store(dst, INT);

                    break;
                case "S":
                    mv.visitInsn(SALOAD);
                    store(dst, INT);

                    break;
                case "I":
                    mv.visitInsn(IALOAD);
                    store(dst, INT);

                    break;
                case "F":
                    mv.visitInsn(FALOAD);
                    store(dst, FLOAT);

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
        String arrDesc = typeRef(in).descriptor(); // e.g. [I or [Ljava/lang/String;
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
        Insn payload = payloadOf(in);
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
        Insn.FieldRef ref = fieldRef(in);
        String owner = ref.owner();
        String name = ref.name();
        String ftype = ref.type();
        char k = kindOf(ftype);
        int dst = in.registers.get(0);
        String desc = ftype;
        String declaring = resolver.fieldDeclaring(ref);
        String internal = TxDescriptors.internal(declaring);
        // The receiver cast follows the REF's owner, not the resolved
        // declaring class: protected-field verification (JVMS §4.10.1.9)
        // wants the receiver assignable to the current class when resolving
        // up into a host ancestor (kotlin.jvm.internal.CallableReference's
        // protected receiver; casting to the ancestor would reject the
        // verifier's own subclass check), and the guest-chain shadow fix
        // only changed WHERE the value lives, not who may read it.
        if (isStatic) {
            mv.visitFieldInsn(GETSTATIC, internal, name, desc);
        } else {
            int objReg = in.registers.get(1);
            load(objReg, OBJ);
            // Register types widen through CFG merges (the Removed/DEX-zero
            // ternary in LockFreeLinkedListNode.getNextNode merges to
            // Object), while GETFIELD requires the receiver to be assignable
            // to the declaring class. Narrow like putfield values; the cast
            // is idempotent and the JIT elides it. Skip it for an
            // uninitialized this: checkcast is illegal on uninitializedThis
            // (ReportFullyDrawnExecutorImpl.<init> touches its own field
            // before super()).
            if (!regUninit[objReg]) {
                mv.visitTypeInsn(CHECKCAST, TxDescriptors.internal(owner));
            }
            mv.visitFieldInsn(GETFIELD, internal, name, desc);
        }
        store(dst, k);
        // A read of an array-typed field carries its descriptor: a later
        // aget/aput resolves the element kind from it (SnapshotIdSet.belowBound
        // is [I and lowest(I) immediately agets it).
        regArray[dst] = ftype.startsWith("[") ? ftype : null;
    }

    private void emitPutField(Insn in, boolean isStatic) {
        Insn.FieldRef ref = fieldRef(in);
        String owner = ref.owner();
        String name = ref.name();
        String ftype = ref.type();
        char k = kindOf(ftype);
        int valueReg = in.registers.get(0);
        // Slot types widen through CFG merges ({obj,int}-unified slots
        // declare Object); the JVM wants the field's own type on putfield.
        // Same narrowing as invoke args; idempotent, JIT elides. The cast
        // applies to the stack top (the value), after both loads.
        boolean refField = ftype.startsWith("L") || ftype.startsWith("[");
        String declaring = resolver.fieldDeclaring(ref);
        String internal = TxDescriptors.internal(declaring);
        // Same receiver-cast rule as getfield: cast to the ref's owner
        // (protected access), walk only the FIELD instruction to the
        // declaring class (shadow fields).
        if (isStatic) {
            load(valueReg, k == OBJ || k == INT || k == FLOAT || k == LONG || k == DOUBLE
                    ? valueKindFor(ftype) : k);
            if (refField) {
                mv.visitTypeInsn(CHECKCAST, TxDescriptors.internal(ftype));
            }
            mv.visitFieldInsn(PUTSTATIC, internal, name, ftype);
        } else {
            int objReg = in.registers.get(1);
            load(objReg, OBJ);
            // Same widening fix as getfield: the receiver register can merge
            // to Object across CFG arms while putfield requires the declaring
            // class (AndroidComposeView.handleMotionEvent's $changed arm).
            // Uninitialized this must skip it too (checkcast rejects
            // uninitializedThis).
            if (!regUninit[objReg]) {
                mv.visitTypeInsn(CHECKCAST, TxDescriptors.internal(owner));
            }
            load(valueReg, valueKindFor(ftype));
            if (refField) {
                mv.visitTypeInsn(CHECKCAST, TxDescriptors.internal(ftype));
            }
            mv.visitFieldInsn(PUTFIELD, internal, name, ftype);
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
        if (method.owner.contains("BufferedChannel")
                && method.name.equals("isRendezvousOrUnlimited")) {
        }
        load(a, kind);
        load(b, kind);
        mv.visitInsn(opc);
        store(dst, INT);

        regArray[dst] = null;
    }

    private boolean isBinop(String op) {
        return op.startsWith("add-") || op.startsWith("sub-") || op.startsWith("mul-")
                || op.startsWith("div-") || op.startsWith("rem-")
                || op.startsWith("and-") || op.startsWith("or-") || op.startsWith("xor-")
                || op.startsWith("shl-") || op.startsWith("shr-") || op.startsWith("ushr-")
                || op.startsWith("rsub-");
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
            regArray[a] = null;
            return;
        }
        if (lit) {
            // lit16/lit8 binops exist for the int family only.
            int src = in.registers.get(1);
            if (family.startsWith("rsub")) {
                // dalvik rsub-int (lit8): vAA = -vBB + CC, i.e. lit - src —
                // the only subtract with the literal as the LEFT operand
                // (source.android.com dalvik-bytecode rsub-int). The JVM has
                // no reversed-sub opcode; push the literal first.
                pushInt((int) in.literal);
                load(src, k);
                mv.visitInsn(ISUB);
                store(a, k);
                regArray[a] = null;
                return;
            }
            load(src, k);
            pushInt((int) in.literal);
            mv.visitInsn(binOpcode(family, k));
            store(a, k);
            regArray[a] = null;
            return;
        }
        if (addr) {
            load(a, k);
            load(in.registers.get(1), rhsKind(family));
            // JVM opcode keys on the LHS kind: shifts carry an int rhs with
            // a wide lhs (shl-long/2addr is LSHL, not ISHL).
            mv.visitInsn(binOpcode(family, k));
            store(a, k);
            regArray[a] = null;
            return;
        }
        int b2 = in.registers.get(1);
        int c = in.registers.get(2);
        load(b2, k);
        load(c, rhsKind(family));
        // Same LHS-kind key for the 3-arg form.
        mv.visitInsn(binOpcode(family, k));
        store(a, k);
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
