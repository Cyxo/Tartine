package tartine.tx;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
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
        for (DexClass cls : all) {
            Set<String> keys = new LinkedHashSet<>();
            for (DexClass.DexField field : cls.fields) {
                keys.add(field.name + ":" + field.type);
            }
            declared.put(cls.type, keys);
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
                    out.computeIfAbsent(owner, k -> new LinkedHashMap<>())
                            .putIfAbsent(key, new DexClass.DexField(
                                    rest.substring(0, colon),
                                    rest.substring(colon + 1),
                                    ACC_PUBLIC | ACC_VOLATILE
                                            | (stat ? ACC_STATIC : 0)));
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
    static final int ACC_ABSTRACT = 0x400;
    static final int ACC_STRICT = 0x800;
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
        return translate(cls, Map.of(), java.util.Set.of());
    }

    public static byte[] translate(DexClass cls,
            Map<String, List<DexClass.DexField>> synthsByOwner) {
        return translate(cls, synthsByOwner, java.util.Set.of());
    }

    /** {@code guestInterfaces}: dex-owned interface descriptors, consulted
     *  when an invoke-virtual names an interface owner (DEX lets it; the JVM
     *  needs invokeinterface). */
    public static byte[] translate(DexClass cls,
            Map<String, List<DexClass.DexField>> synthsByOwner,
            java.util.Set<String> guestInterfaces) {
        // Class-level flags come from the dex class_def_item (interfaces are
        // emitted ACC_INTERFACE: implementers fail at defineClass time
        // otherwise). ACC_SUPER only applies to class files.
        int access = asmAccess(cls.accessFlags);
        if ((access & ACC_INTERFACE) == 0) {
            access |= ACC_SUPER;
        }
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        String name = TxDescriptors.internal(cls.type);
        String parent = cls.superclass == null ? null : TxDescriptors.internal(cls.superclass);
        String[] ifaces = cls.interfaces.stream().map(TxDescriptors::internal)
                .toArray(String[]::new);
        cw.visit(V1_5, access, name, null, parent, ifaces);

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
        for (DexClass.DexField field : cls.fields) {
            cw.visitField(asmAccess(field.accessFlags), field.name,
                    field.type, null, null).visitEnd();
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
            // Two-pass: probe the method on a throwaway writer so a crash
            // never half-visits the real one (ClassWriter cannot abandon an
            // opened MethodVisitor — the stub would duplicate the method).
            // emit() is deterministic, so a successful probe is safe to
            // re-run on the real writer.
            boolean ok = false;
            String msg = "unknown failure";
            try {
                new TxMethodWriter(new ClassWriter(0), method,
                        guestInterfaces).emit();
                ok = true;
            } catch (RuntimeException x) {
                msg = x instanceof TxTranslator.TxGap
                        ? x.getMessage()
                        : "writer crash: " + x;
            }
            if (ok) {
                new TxMethodWriter(cw, method, guestInterfaces).emit();
            } else {
                emitGapStub(cw, method, base, msg);
            }
        }
        cw.visitEnd();
        return cw.toByteArray();
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
        System.err.println("tx: degraded " + sig + ": " + msg);
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
