package tartine.tx;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicVerifier;
import tartine.dex.DexClass;
import tartine.dex.DexMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Spikes the translation one class at a time: DEX model -> JVM bytecode.
 *
 * <p>Classfile version 49 (Java 5): no StackMapTable, so the old
 * type-inferencing verifier covers our register-machine shapes; {@code
 * COMPUTE_MAXS} suffices because there are no frames to compute. All DEX
 * types keep their descriptors ({@code Lpkg/Cls;} etc.) verbatim.
 */
public final class TxTranslator implements Opcodes {

    /**
     * Whole-dex scan: fields referenced but not declared by their owner. The
     * interpreter stores such fields in placeholder maps (the fixture
     * references fields its classes never declare; real dx only emits
     * declared members), so synthesized self-owned members keep getstatic/
     * putstatic resolvable on the JVM. Keyed by the owner type descriptor.
     */
    public static Map<String, java.util.List<DexClass.DexField>> fieldSynths(
            java.util.List<DexClass> all) {
        Map<String, Set<String>> declared = new HashMap<>();
        Map<String, String> superOf = new HashMap<>();
        for (DexClass cls : all) {
            Set<String> keys = new LinkedHashSet<>();
            for (DexClass.DexField field : cls.fields) {
                keys.add(field.name + ":" + field.type);
            }
            declared.put(cls.type, keys);
            if (cls.superclass != null && !cls.superclass.isEmpty()) {
                superOf.put(cls.type, cls.superclass);
            }
        }
        Map<String, LinkedHashMap<String, DexClass.DexField>> out = new HashMap<>();
        for (DexClass cls : all) {
            for (DexMethod method : cls.methods) {
                if (method.instructions == null) {
                    continue;
                }
                for (tartine.dex.Insn in : method.instructions) {
                    boolean stat = in.opcode.startsWith("sget")
                            || in.opcode.startsWith("sput");
                    boolean inst = in.opcode.startsWith("iget")
                            || in.opcode.startsWith("iput");
                    if (!stat && !inst) {
                        continue;
                    }
                    int arrow = in.reference.indexOf("->");
                    if (arrow <= 0) {
                        continue;
                    }
                    String owner = in.reference.substring(0, arrow);
                    if (!declared.containsKey(owner)) {
                        continue; // not a guest type; resolves elsewhere
                    }
                    String rest = in.reference.substring(arrow + 2);
                    int colon = rest.indexOf(':');
                    String key = rest.substring(0, colon) + ":"
                            + rest.substring(colon + 1);
                    if (declared.get(owner).contains(key)) {
                        continue;
                    }
                    // Ancestor-declared fields must not be synthesized: the
                    // JVM would add a shadow field to the subclass
                    // (MutableIntList;->content:[I resolves to IntList's
                    // field; announcing it on MutableIntList too split the
                    // value in two — super's <init> put the array into the
                    // declared one, and ensureCapacity's subclass-owned
                    // iget read the null shadow). Walk the guest chain to
                    // the declaring class; the emit side retargets the
                    // field instruction there (see TxMethodWriter).
                    boolean ancestorDeclares = false;
                    String anc = superOf.get(owner);
                    while (anc != null) {
                        Set<String> ancKeys = declared.get(anc);
                        if (ancKeys != null && ancKeys.contains(key)) {
                            ancestorDeclares = true;
                            break;
                        }
                        anc = superOf.get(anc);
                    }
                    if (ancestorDeclares) {
                        continue;
                    }
                    out.computeIfAbsent(owner, k -> new LinkedHashMap<>())
                            .putIfAbsent(key, new DexClass.DexField(
                                    rest.substring(0, colon),
                                    rest.substring(colon + 1),
                                    ACC_PUBLIC | ACC_VOLATILE
                                            | (stat ? ACC_STATIC : 0),
                                    null)); // synthesized: no dex initial value
                }
            }
        }
        Map<String, java.util.List<DexClass.DexField>> byOwner = new HashMap<>();
        for (Map.Entry<String, LinkedHashMap<String, DexClass.DexField>> e
                : out.entrySet()) {
            byOwner.put(e.getKey(), new ArrayList<>(e.getValue().values()));
        }
        return byOwner;
    }
    /** DEX access flag bits (dex spec), shared by classes/methods/fields. */
    static final int ACC_PUBLIC = 0x1;
    static final int ACC_PRIVATE = 0x2;
    static final int ACC_PROTECTED = 0x4;
    static final int ACC_STATIC = 0x8;
    static final int ACC_FINAL = 0x10;
    static final int ACC_SYNCHRONIZED = 0x20;
    static final int ACC_VOLATILE = 0x40;
    static final int ACC_TRANSIENT = 0x80;
    static final int ACC_NATIVE = 0x100;
    /** Same value in dex and JVM flag spaces. */
    static final int ACC_INTERFACE = 0x200;
    static final int ACC_ABSTRACT = 0x400;
    static final int ACC_STRICT = 0x800;
    static final int ACC_SYNTHETIC = 0x1000;
    static final int ACC_ANNOTATION = 0x2000;
    static final int ACC_ENUM = 0x4000;
    static final int ACC_CONSTRUCTOR = 0x10000;

    /** Exceptions who cannot be translated surface as this named failure. */
    public static final class TxGap extends RuntimeException {
        public TxGap(String message) {
            super(message);
        }
    }

    private final DexClass cls;

    private TxTranslator(DexClass cls) {
        this.cls = cls;
    }

    public static byte[] translate(DexClass cls) {
        return translate(cls, Map.of(), java.util.Set.of(), null, null);
    }

    public static byte[] translate(DexClass cls,
            Map<String, List<DexClass.DexField>> synthsByOwner) {
        return translate(cls, synthsByOwner, java.util.Set.of(), null, null);
    }

    public static byte[] translate(DexClass cls,
            Map<String, List<DexClass.DexField>> synthsByOwner,
            java.util.Set<String> guestInterfaces) {
        return translate(cls, synthsByOwner, guestInterfaces, null, null);
    }

    /** {@code guestInterfaces}: dex-owned interface descriptors, consulted
     *  when an invoke-virtual names an interface owner (DEX lets it; the JVM
     *  needs invokeinterface). {@code dexLookup}/{@code hostLoader} resolve
     *  the merge hierarchy for computed frames (guest types never reflect
     *  onto the host loader). Classfile v1_8: the v1_5 inference path lost
     *  its JVM failover, and default interface methods (androidx) do not
     *  encode before v1_8 — StackMapTable frames on, computed by ASM. */
    public static byte[] translate(DexClass cls,
            Map<String, List<DexClass.DexField>> synthsByOwner,
            java.util.Set<String> guestInterfaces,
            java.util.function.Function<String, DexClass> dexLookup,
            ClassLoader hostLoader) {
        // Class-level flags come from the dex class_def_item (interfaces are
        // emitted ACC_INTERFACE: implementers fail at defineClass time
        // otherwise). ACC_SUPER only applies to class files.
        int access = asmAccess(cls.accessFlags);
        if ((access & ACC_INTERFACE) == 0) {
            access |= ACC_SUPER;
        }
        String name = TxDescriptors.internal(cls.type);
        String parent = cls.superclass == null ? null
                : TxDescriptors.internal(cls.superclass);
        String[] ifaces = cls.interfaces.stream().map(TxDescriptors::internal)
                .toArray(String[]::new);
        FrameWriter cw = new FrameWriter(dexLookup, hostLoader);
        cw.visit(Opcodes.V1_8, access, name, null, parent, ifaces);

        // Synthesized members for undeclared referenced fields (see
        // fieldSynths). Declared fields always win.
        Set<String> declared = new LinkedHashSet<>();
        for (DexClass.DexField field : cls.fields) {
            declared.add(field.name + ":" + field.type);
        }
        List<DexClass.DexField> extra = synthsByOwner
                .getOrDefault(cls.type, List.of());
        for (DexClass.DexField field : extra) {
            String key = field.name + ":" + field.type;
            if (declared.contains(key)) {
                continue;
            }
            declared.add(key); // a ref scanned twice with different flags
            cw.visitField(asmAccess(field.accessFlags), field.name,
                    field.type, null, null).visitEnd();
        }
        List<DexClass.DexField> staticSeeds = new ArrayList<>();
        boolean sawClinit = false;
        for (DexClass.DexField field : cls.fields) {
            if ((field.accessFlags & ACC_STATIC) != 0
                    && field.initialValue != null
                    && (field.accessFlags & ACC_FINAL) == 0) {
                // Non-final statics cannot carry a JVM ConstantValue (JVMS
                // §4.7.3 is static-final-only) yet the dex seeds them via
                // static_values before <clinit> runs — survive through a
                // head-of-<clinit> assignment (TxMethodWriter).
                staticSeeds.add(field);
            }
        }
        for (DexClass.DexField field : cls.fields) {
            // JVM ConstantValue applies to static finals only (JVMS §4.7.3)
            // and Java primitive/String types; the dex initial value is
            // pre-boxed into exactly those (DexClass#javaInitialValue), so
            // R.id-style ints survive translation. Non-final/instance
            // fields keep null — the guest <clinit> keeps setting those.
            boolean staticFinal = ((field.accessFlags
                    & (ACC_STATIC | ACC_FINAL)) == (ACC_STATIC | ACC_FINAL));
            cw.visitField(asmAccess(field.accessFlags), field.name,
                    field.type, null,
                    staticFinal ? field.initialValue : null).visitEnd();
        }
        for (DexMethod method : cls.methods) {
            int flags = method.accessFlags;
            int base = asmAccess(flags);
            if (!method.hasImplementation) {
                // Abstract: signature only, no code.
                cw.visitMethod(base | ACC_ABSTRACT, method.name,
                        asmDesc(method), null, null).visitEnd();
                continue;
            }
            if ((flags & ACC_NATIVE) != 0) {
                // Native DEX bodies have nothing to translate; keep the gap loud.
                cw.visitMethod(base | ACC_NATIVE, method.name,
                        asmDesc(method), null, null).visitEnd();
                continue;
            }
            // Two-pass: probe the method on a throwaway one-method class so
            // a crash never half-visits the real one (ClassWriter cannot
            // abandon an opened MethodVisitor — the stub would duplicate
            // the method). Frame computation happens at toByteArray, so the
            // probe class forces it: frames are load-bearing at defineClass
            // now (the v1_5 inference verifier has no failover on current
            // JVMs).
            boolean ok = false;
            String msg = "unknown failure";
            if (method.name.equals("<clinit>")) {
                sawClinit = true;
            }
            try {
                FrameWriter probe = new FrameWriter(dexLookup, hostLoader);
                probe.visit(Opcodes.V1_8, access, name, null, parent, ifaces);
                new TxMethodWriter(probe, method, guestInterfaces,
                        staticSeeds, dexLookup).emit();
                byte[] probeBytes = probe.toByteArray();
                // COMPUTE_FRAMES writes per-path frames and does not reject
                // an int/reference conflict at a merge (d8 reuses registers
                // polymorphically); the JVM fails the whole class instead.
                // Replaying the probe through ASM's BasicVerifier (kind
                // lattice, no hierarchy needed) degrades such methods to
                // their named stub.
                verifyKindwise(probeBytes, method.name, asmDesc(method),
                        method.signature());
                ok = true;
            } catch (RuntimeException | LinkageError x) {
                msg = x instanceof TxTranslator.TxGap
                        ? x.getMessage()
                        : "writer crash: " + x;
            }
            if (ok) {
                new TxMethodWriter(cw, method, guestInterfaces,
                        staticSeeds, dexLookup).emit();
            } else {
                emitGapStub(cw, method, base, msg);
            }
        }
        if (!staticSeeds.isEmpty() && !sawClinit) {
            // The class seeds non-final statics but carries no <clinit>:
            // synthesize one that assigns the static_values (dex static field
            // initial values apply to any static field before <clinit>; the
            // JVM has no ConstantValue lane for non-final statics).
            MethodVisitor mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V",
                    null, null);
            mv.visitCode();
            for (DexClass.DexField f : staticSeeds) {
                mv.visitLdcInsn(f.initialValue);
                mv.visitFieldInsn(PUTSTATIC, name, f.name, f.type);
            }
            mv.visitInsn(RETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        cw.visitEnd();
        return cw.toByteArray();
    }

    /**
     * ClassWriter computing StackMapTable frames (v1_8 requires them):
     * merge hierarchy resolves through the dex model first, then host
     * reflection on the class name only — guest types are never defined
     * onto the host loader.
     */
    private static final class FrameWriter extends ClassWriter {
        private final java.util.function.Function<String, DexClass> dexLookup;
        private final ClassLoader hostLoader;

        FrameWriter(java.util.function.Function<String, DexClass> dexLookup,
                ClassLoader hostLoader) {
            super(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            this.dexLookup = dexLookup;
            this.hostLoader = hostLoader;
        }

        @Override
        protected String getCommonSuperClass(String type1, String type2) {
            if (type1.equals(type2)) {
                return type1;
            }
            boolean array1 = !type1.isEmpty() && type1.charAt(0) == '[';
            boolean array2 = !type2.isEmpty() && type2.charAt(0) == '[';
            if (array1 != array2) {
                return "java/lang/Object";
            }
            if (array1) {
                // Both arrays: merge the components; differing depths or an
                // Object-degraded merge fall back to Object (ASM's default
                // for anything nontrivial).
                String c1 = commonComponent(type1);
                String c2 = commonComponent(type2);
                String merged = getCommonSuperClass(c1, c2);
                int depth = commonDepth(type1, type2);
                if (depth < 0 || merged.charAt(0) == '[') {
                    return "java/lang/Object";
                }
                return "[".repeat(depth) + "L" + merged + ";";
            }
            java.util.LinkedHashSet<String> first = ancestors(type1);
            java.util.LinkedHashSet<String> second = ancestors(type2);
            for (String candidate : first) {
                if (second.contains(candidate)) {
                    return candidate;
                }
            }
            return "java/lang/Object";
        }

        /**
         * Types of the lineage of {@code type}, most specific first (BFS:
         * superclass before interfaces), including interfaces — the JVM's
         * frame merge legitimately yields a common interface.
         */
        private java.util.LinkedHashSet<String> ancestors(String type) {
            java.util.LinkedHashSet<String> set = new java.util.LinkedHashSet<>();
            java.util.ArrayDeque<String> queue = new java.util.ArrayDeque<>();
            set.add(type);
            queue.add(type);
            while (!queue.isEmpty()) {
                for (String sup : supers(queue.poll())) {
                    if (set.add(sup)) {
                        queue.add(sup);
                    }
                }
            }
            return set;
        }

        /** Superclass then interfaces of {@code type} (internal name). */
        private java.util.List<String> supers(String type) {
            if (type.equals("java/lang/Object")) {
                return java.util.List.of();
            }
            java.util.ArrayList<String> list = new java.util.ArrayList<>();
            DexClass dex = dexLookup == null ? null
                    : dexLookup.apply("L" + type + ";");
            if (dex != null) {
                if (dex.superclass != null) {
                    list.add(TxDescriptors.internal(dex.superclass));
                }
                for (String iface : dex.interfaces) {
                    list.add(TxDescriptors.internal(iface));
                }
                return list;
            }
            try {
                Class<?> host = Class.forName(type.replace('/', '.'), false,
                        hostLoader != null ? hostLoader
                                : FrameWriter.class.getClassLoader());
                if (host.getSuperclass() != null) {
                    list.add(host.getSuperclass().getName().replace('.', '/'));
                }
                for (Class<?> iface : host.getInterfaces()) {
                    list.add(iface.getName().replace('.', '/'));
                }
            } catch (ClassNotFoundException | LinkageError e) {
                // Type unknown everywhere: this lineage stops here and the
                // merge falls back to Object (a later verification error
                // names the real gap if the frame depended on it).
            }
            return list;
        }

        private static String commonComponent(String arrayType) {
            String body = arrayType.substring(1);
            while (body.charAt(0) == '[') {
                body = body.substring(1);
            }
            if (body.charAt(0) == 'L' && body.endsWith(";")) {
                return body.substring(1, body.length() - 1);
            }
            return "java/lang/Object";
        }

        private static int commonDepth(String type1, String type2) {
            int depth = 0;
            while (depth < type1.length() && depth < type2.length()
                    && type1.charAt(depth) == '[' && type2.charAt(depth) == '[') {
                depth++;
            }
            return depth;
        }
    }

    /**
     * TEMP probe: emits one method with frames and without verifier gating
     * (for {@code txdump} forensics of a degraded method).
     */
    public static byte[] emitProbeSingle(DexClass cls,
            Map<String, List<DexClass.DexField>> synthsByOwner,
            java.util.Set<String> guestInterfaces,
            java.util.function.Function<String, DexClass> dexLookup,
            ClassLoader hostLoader, String methodFilter) {
        int access = asmAccess(cls.accessFlags);
        if ((access & ACC_INTERFACE) == 0) {
            access |= ACC_SUPER;
        }
        String name = TxDescriptors.internal(cls.type);
        String parent = cls.superclass == null ? null
                : TxDescriptors.internal(cls.superclass);
        String[] ifaces = cls.interfaces.stream().map(TxDescriptors::internal)
                .toArray(String[]::new);
        FrameWriter probe = new FrameWriter(dexLookup, hostLoader);
        probe.visit(Opcodes.V1_8, access, name, null, parent, ifaces);
        for (DexMethod method : cls.methods) {
            String want = methodFilter;
            String descFilter = null;
            int paren = want.indexOf('(');
            if (paren >= 0) {
                descFilter = want.substring(paren);
                want = want.substring(0, paren);
            }
            if (!method.name.equals(want)
                    || (descFilter != null
                        && !TxTranslator.asmDesc(method).equals(descFilter))) {
                continue;
            }
            new TxMethodWriter(probe, method, guestInterfaces,
                    List.of(), dexLookup).emit();
        }
        probe.visitEnd();
        return probe.toByteArray();
    }

    /**
     * Kind-lattice replay of the probe bytes: ASM's Analyzer with
     * {@code BasicVerifier} merge-conflicts an int/reference local the same
     * way the JVM's frame verifier would, before the whole class proves
     * und definable. Throws {@link TxGap} naming the method.
     */
    private static void verifyKindwise(byte[] bytes, String name, String desc,
            String sig) {
        ClassNode node = new ClassNode();
        new ClassReader(bytes).accept(node, 0);
        for (MethodNode mn : node.methods) {
            if (mn.name.equals(name) && mn.desc.equals(desc)) {
                Analyzer<org.objectweb.asm.tree.analysis.BasicValue>
                        verifier = new Analyzer<>(new BasicVerifier());
                try {
                    verifier.analyze(node.name, mn);
                } catch (AnalyzerException e) {
                    throw new TxGap(sig + " @frame: kind conflict: "
                            + e.getMessage() + " @ " + brief(e.node));
                }
                return;
            }
        }
    }

    /** Probe-only: print locals/stack frames around a verifier conflict so
     *  the guest dex shapes behind it can be matched quickly. */
    /** One-line description of a failing ASM instruction for the census. */
    private static String brief(org.objectweb.asm.tree.AbstractInsnNode n) {
        if (n == null) {
            return "?";
        }
        String head = n.getClass().getSimpleName();
        if (n instanceof org.objectweb.asm.tree.FieldInsnNode f) {
            return head + " " + f.owner + "." + f.name + " " + f.desc;
        }
        if (n instanceof org.objectweb.asm.tree.MethodInsnNode m) {
            return head + " " + m.owner + "." + m.name + m.desc;
        }
        if (n instanceof org.objectweb.asm.tree.VarInsnNode v) {
            return head + " var=" + v.var;
        }
        if (n instanceof org.objectweb.asm.tree.IntInsnNode ii) {
            return head + " " + ii.operand;
        }
        if (n instanceof org.objectweb.asm.tree.TypeInsnNode t) {
            return head + " " + t.desc;
        }
        if (n instanceof org.objectweb.asm.tree.LdcInsnNode ld) {
            return head + " " + ld.cst;
        }
        return head + " op=" + n.getOpcode();
    }

    /**
     * Degrades a method that hit a named gap to a throwing stub: the class
     * stays loadable (translation is class-granular, one broken body would
     * otherwise kill every sibling method) and the gap surfaces exactly at
     * first call, naming itself — the interpreter's VmException loop,
     * transposed onto translation.
     */
    private static void emitGapStub(ClassWriter cw, DexMethod method, int base, String msg) {
        String sig = method.owner + "->" + method.signature();
        MethodVisitor mv = cw.visitMethod(base, method.name, asmDesc(method), null, null);
        mv.visitCode();
        mv.visitTypeInsn(NEW, "java/lang/UnsupportedOperationException");
        mv.visitInsn(DUP);
        mv.visitLdcInsn("Tartine: not implemented: " + sig + ": " + msg);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/UnsupportedOperationException",
                "<init>", "(Ljava/lang/String;)V", false);
        mv.visitInsn(ATHROW);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private static int asmAccess(int dexFlags) {
        int out = 0;
        if ((dexFlags & ACC_PUBLIC) != 0) {
            out |= ACC_PUBLIC;
        }
        if ((dexFlags & ACC_PRIVATE) != 0) {
            out |= ACC_PRIVATE;
        }
        if ((dexFlags & ACC_PROTECTED) != 0) {
            out |= ACC_PROTECTED;
        }
        if ((dexFlags & ACC_STATIC) != 0) {
            out |= ACC_STATIC;
        }
        if ((dexFlags & ACC_FINAL) != 0) {
            out |= ACC_FINAL;
        }
        if ((dexFlags & ACC_SYNCHRONIZED) != 0) {
            out |= ACC_SYNCHRONIZED;
        }
        if ((dexFlags & ACC_VOLATILE) != 0) {
            out |= ACC_VOLATILE;
        }
        if ((dexFlags & ACC_TRANSIENT) != 0) {
            out |= ACC_TRANSIENT;
        }
        // Class-shape bits sharing values across dex and JVM flag spaces
        // (interface 0x200, synthetic, annotation, enum); ASM validates the
        // interface bit on every `implements` referencing these classes.
        if ((dexFlags & ACC_INTERFACE) != 0) {
            out |= ACC_INTERFACE;
        }
        if ((dexFlags & ACC_SYNTHETIC) != 0) {
            out |= ACC_SYNTHETIC;
        }
        if ((dexFlags & ACC_ANNOTATION) != 0) {
            out |= ACC_ANNOTATION;
        }
        if ((dexFlags & ACC_ENUM) != 0) {
            out |= ACC_ENUM;
        }
        if ((dexFlags & ACC_ABSTRACT) != 0) {
            out |= ACC_ABSTRACT;
        }
        if ((dexFlags & ACC_STRICT) != 0) {
            out |= ACC_STRICT;
        }
        return out;
    }

    /** {@code (II)I}-style JVM descriptor from the DEX parameter list. */
    static String asmDesc(DexMethod method) {
        StringBuilder out = new StringBuilder("(");
        for (String param : method.parameterTypes) {
            out.append(param);
        }
        return out.append(')').append(method.returnType).toString();
    }
}
