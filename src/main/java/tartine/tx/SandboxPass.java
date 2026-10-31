package tartine.tx;

import java.util.function.Predicate;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ConstantDynamic;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.RecordComponentVisitor;
import org.objectweb.asm.Type;
import tartine.guest.SandboxDenied;

/**
 * Translation-time sandbox boundary and {@code java.io.File} rewrite.
 *
 * <p>Sits between {@link TxTranslator.TxMethodWriter} and the frame-computing
 * {@code ClassWriter}, so the rewritten instructions (guest {@code File} to
 * {@link tartine.guest.GuestFile} and {@code GuestFiles} calls) are what the
 * verifier frame computation sees. Class metadata (superclass, interfaces,
 * field descriptors) is checked once; denied instructions raise
 * {@link SandboxDenied}, which {@code TxTranslator} turns into the method's
 * named gap stub.
 */
final class SandboxPass extends ClassVisitor {
    private final Predicate<String> isGuest;

    SandboxPass(ClassVisitor delegate, Predicate<String> isGuest) {
        super(Opcodes.ASM9, delegate);
        this.isGuest = isGuest;
    }

    private String typeDenialFor(String type) {
        if (type.startsWith("[")) {
            return SandboxPolicy.typeDenial(type, isGuest);
        }
        return SandboxPolicy.classDenial(type, isGuest);
    }

    @Override
    public void visit(int version, int access, String name, String signature,
            String superName, String[] interfaces) {
        if (superName != null) {
            if (superName.equals("java/io/File")) {
                throw denied("guest subclasses of java.io.File are not available");
            }
            String denial = SandboxPolicy.classDenial(superName, isGuest);
            if (denial != null) {
                throw denied("superclass " + denial);
            }
        }
        if (interfaces != null) {
            for (String iface : interfaces) {
                String denial = SandboxPolicy.classDenial(iface, isGuest);
                if (denial != null) {
                    throw denied("interface " + denial);
                }
            }
        }
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor,
            String signature, Object value) {
        String denial = SandboxPolicy.typeDenial(descriptor, isGuest);
        if (denial != null) {
            throw denied("field " + name + ": " + denial);
        }
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public RecordComponentVisitor visitRecordComponent(String name,
            String descriptor, String signature) {
        String denial = SandboxPolicy.typeDenial(descriptor, isGuest);
        if (denial != null) {
            throw denied("record component " + name + ": " + denial);
        }
        return super.visitRecordComponent(name, descriptor, signature);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor,
            String signature, String[] exceptions) {
        if (exceptions != null) {
            for (String exception : exceptions) {
                String denial = SandboxPolicy.classDenial(exception, isGuest);
                if (denial != null) {
                    throw denied("throws " + denial);
                }
            }
        }
        MethodVisitor delegate = super.visitMethod(access, name, descriptor,
                signature, exceptions);
        if (delegate == null) {
            return null;
        }
        return new MethodVisitor(Opcodes.ASM9, delegate) {
            @Override
            public void visitTypeInsn(int opcode, String type) {
                if (opcode == Opcodes.NEW && type.equals("java/io/File")) {
                    super.visitTypeInsn(opcode, "tartine/guest/GuestFile");
                    return;
                }
                String denial = typeDenialFor(type);
                if (denial != null) {
                    throw denied("type " + type + ": " + denial);
                }
                super.visitTypeInsn(opcode, type);
            }

            @Override
            public void visitMultiANewArrayInsn(String descriptor, int dimensions) {
                String denial = SandboxPolicy.typeDenial(descriptor, isGuest);
                if (denial != null) {
                    throw denied("array " + descriptor + ": " + denial);
                }
                super.visitMultiANewArrayInsn(descriptor, dimensions);
            }

            @Override
            public void visitMethodInsn(int opcode, String owner, String name,
                    String descriptor, boolean isInterface) {
                if (owner.equals("java/io/File")) {
                    fileCall(delegate, name, descriptor);
                    return;
                }
                String denial = SandboxPolicy.memberDenial(owner, name, descriptor,
                        true, isGuest);
                if (denial != null) {
                    throw denied("method " + owner.replace('/', '.') + "." + name
                            + descriptor + ": " + denial);
                }
                super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
            }

            @Override
            public void visitFieldInsn(int opcode, String owner, String name,
                    String descriptor) {
                if (opcode == Opcodes.PUTSTATIC && owner.equals("java/lang/System")
                        && (name.equals("out") || name.equals("err"))) {
                    throw denied("System." + name
                            + ": output redirection is not available");
                }
                String denial = SandboxPolicy.memberDenial(owner, name, descriptor,
                        false, isGuest);
                if (denial != null) {
                    throw denied("field " + owner.replace('/', '.') + "." + name
                            + ": " + denial);
                }
                super.visitFieldInsn(opcode, owner, name, descriptor);
            }

            @Override
            public void visitLdcInsn(Object value) {
                checkConstant(value);
                super.visitLdcInsn(value);
            }

            @Override
            public void visitInvokeDynamicInsn(String name, String descriptor,
                    Handle bootstrap, Object... bootstrapArgs) {
                String denial = SandboxPolicy.methodTypeDenial(descriptor, isGuest);
                if (denial != null) {
                    throw denied("invokedynamic " + name + ": " + denial);
                }
                String bootstrapDenial = SandboxPolicy.bootstrapDenial(
                        bootstrap.getOwner(), bootstrap.getName(),
                        bootstrap.getDesc(), isGuest);
                if (bootstrapDenial != null) {
                    throw denied(bootstrapDenial);
                }
                for (Object argument : bootstrapArgs) {
                    checkConstant(argument);
                }
                super.visitInvokeDynamicInsn(name, descriptor, bootstrap,
                        bootstrapArgs);
            }

            private void checkConstant(Object value) {
                if (value instanceof Type type) {
                    String denial = type.getSort() == Type.METHOD
                            ? SandboxPolicy.methodTypeDenial(
                                    type.getDescriptor(), isGuest)
                            : SandboxPolicy.typeDenial(
                                    type.getDescriptor(), isGuest);
                    if (denial != null) {
                        throw denied("constant " + type.getDescriptor() + ": "
                                + denial);
                    }
                } else if (value instanceof Handle handle) {
                    String denial = SandboxPolicy.handleDenial(handle.getOwner(),
                            handle.getName(), handle.getDesc(), isGuest);
                    if (denial != null) {
                        throw denied(denial);
                    }
                } else if (value instanceof ConstantDynamic) {
                    throw denied("dynamic constants are not available");
                }
            }
        };
    }

    /**
     * Rewrites one {@code java.io.File} call: constructors to
     * {@code GuestFile}, instance calls to the static {@code GuestFiles}
     * bridge, and the host-path/handle surface to a named denial.
     */
    private void fileCall(MethodVisitor mv, String name, String descriptor) {
        if (name.equals("<init>")) {
            if (!descriptor.equals("(Ljava/lang/String;)V")
                    && !descriptor.equals("(Ljava/lang/String;Ljava/lang/String;)V")
                    && !descriptor.equals("(Ljava/io/File;Ljava/lang/String;)V")) {
                throw denied("File.<init>" + descriptor
                        + ": constructor is not available");
            }
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL,
                    "tartine/guest/GuestFile", name, descriptor, false);
            return;
        }
        if (name.equals("createTempFile") || name.equals("listRoots")) {
            throw denied("File." + name + ": static file access is not available");
        }
        if (name.equals("toPath") || name.equals("toURI") || name.equals("toURL")
                || name.equals("deleteOnExit")) {
            throw denied("File." + name
                    + ": host path/handle access is not available");
        }
        if (name.equals("compareTo")
                && descriptor.equals("(Ljava/lang/Object;)I")) {
            throw denied("File.compareTo(Object): bridge call is not available");
        }
        String rewritten = "(Ljava/io/File;" + descriptor.substring(1);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "tartine/guest/GuestFiles",
                name, rewritten, false);
    }

    private static SandboxDenied denied(String reason) {
        return new SandboxDenied(reason);
    }
}
