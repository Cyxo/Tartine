package tartine.tx;

import java.util.Map;
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
    /** File-backed constructors rewritten to their GuestFile* subclass. */
    private static final Map<String, String> FILE_STREAM_OWNERS = Map.of(
            "java/io/FileInputStream", "tartine/guest/GuestFileInputStream",
            "java/io/FileOutputStream", "tartine/guest/GuestFileOutputStream",
            "java/io/FileReader", "tartine/guest/GuestFileReader",
            "java/io/FileWriter", "tartine/guest/GuestFileWriter",
            "java/io/PrintStream", "tartine/guest/GuestPrintStream",
            "java/io/PrintWriter", "tartine/guest/GuestPrintWriter");

    private final Predicate<String> isGuest;
    /** Internal name of the class being visited, for caller injection. */
    private String className;

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
        this.className = name;
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
                if (opcode == Opcodes.NEW
                        && FILE_STREAM_OWNERS.containsKey(type)) {
                    // NEW goes to the GuestFile* subclass; the matching
                    // constructor call is rewritten below, so the object
                    // still carries the host stream type for the verifier.
                    super.visitTypeInsn(opcode, FILE_STREAM_OWNERS.get(type));
                    return;
                }
                if (type.equals("java/lang/Runtime")) {
                    if (opcode == Opcodes.NEW) {
                        throw denied("Runtime: host runtime access is not"
                                + " available");
                    }
                    super.visitTypeInsn(opcode, "tartine/guest/GuestRuntime");
                    return;
                }
                if (type.equals("java/lang/ClassLoader")) {
                    if (opcode == Opcodes.NEW) {
                        throw denied("ClassLoader: host class loader access"
                                + " is not available");
                    }
                    super.visitTypeInsn(opcode,
                            "tartine/guest/GuestClassLoader");
                    return;
                }
                if (type.equals("java/util/ServiceLoader")
                        || type.startsWith("java/util/ServiceLoader$")) {
                    if (opcode == Opcodes.NEW) {
                        throw denied("ServiceLoader: service loading is not"
                                + " available");
                    }
                    super.visitTypeInsn(opcode,
                            "tartine/guest/GuestServiceLoader");
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
                if (name.equals("<init>")
                        && FILE_STREAM_OWNERS.containsKey(owner)) {
                    if (!SandboxPolicy.streamCtorSupported(owner, descriptor)
                            && !SandboxPolicy.streamCtorPassthrough(owner,
                                    descriptor)) {
                        throw denied("file-backed "
                                + owner.replace('/', '.')
                                + " construction is not available");
                    }
                    delegate.visitMethodInsn(Opcodes.INVOKESPECIAL,
                            FILE_STREAM_OWNERS.get(owner), name, descriptor,
                            false);
                    return;
                }
                if (owner.equals("java/io/File")) {
                    fileCall(delegate, name, descriptor);
                    return;
                }
                if (owner.equals("java/lang/Class")) {
                    if (name.equals("getClassLoader")
                            && descriptor.equals("()Ljava/lang/ClassLoader;")) {
                        staticCall(delegate, "tartine/guest/GuestReflect",
                                "getClassLoader",
                                "(Ljava/lang/Class;)Ltartine/guest/GuestClassLoader;");
                        return;
                    }
                    if (name.equals("forName")
                            && descriptor.equals(
                                    "(Ljava/lang/String;)Ljava/lang/Class;")) {
                        // Caller injection keeps Class.forName off the host
                        // loader: the lookup is gated by the guest loader.
                        delegate.visitLdcInsn(Type.getObjectType(className));
                        staticCall(delegate, "tartine/guest/GuestSandbox",
                                "forName",
                                "(Ljava/lang/String;Ljava/lang/Class;)"
                                + "Ljava/lang/Class;");
                        return;
                    }
                    if (name.equals("forName")
                            && descriptor.equals(
                                    "(Ljava/lang/String;Z)Ljava/lang/Class;")) {
                        delegate.visitLdcInsn(Type.getObjectType(className));
                        staticCall(delegate, "tartine/guest/GuestSandbox",
                                "forName",
                                "(Ljava/lang/String;ZLjava/lang/Class;)"
                                + "Ljava/lang/Class;");
                        return;
                    }
                    if (name.equals("forName") && descriptor.equals(
                            "(Ljava/lang/String;ZLjava/lang/ClassLoader;)"
                            + "Ljava/lang/Class;")) {
                        delegate.visitLdcInsn(Type.getObjectType(className));
                        staticCall(delegate, "tartine/guest/GuestSandbox",
                                "forName",
                                "(Ljava/lang/String;ZLjava/lang/Object;"
                                + "Ljava/lang/Class;)Ljava/lang/Class;");
                        return;
                    }
                }
                if (owner.equals("java/util/ServiceLoader")) {
                    serviceLoaderCall(delegate, name, descriptor);
                    return;
                }
                if (owner.equals("java/lang/ClassLoader")) {
                    classLoaderCall(delegate, name, descriptor);
                    return;
                }
                if (owner.equals("java/lang/Runtime")) {
                    runtimeCall(delegate, name, descriptor);
                    return;
                }
                if (owner.equals("java/lang/System")
                        && name.equals("getProperty")) {
                    systemPropertyCall(delegate, name, descriptor);
                    return;
                }
                if (owner.equals("java/lang/Class")
                        && SandboxPolicy.CLASS_REFLECT_LOOKUPS.contains(name)) {
                    classLookupCall(delegate, name, descriptor);
                    return;
                }
                if (owner.equals("java/lang/reflect/Method")) {
                    if (name.equals("invoke")) {
                        methodInvokeCall(delegate, descriptor);
                        return;
                    }
                    if (name.equals("setAccessible")
                            || name.equals("trySetAccessible")) {
                        accessibleCall(delegate, name, descriptor);
                        return;
                    }
                }
                if (owner.equals("java/lang/reflect/Constructor")) {
                    if (name.equals("newInstance")) {
                        constructorNewInstanceCall(delegate, descriptor);
                        return;
                    }
                    if (name.equals("setAccessible")
                            || name.equals("trySetAccessible")) {
                        accessibleCall(delegate, name, descriptor);
                        return;
                    }
                }
                if (owner.equals("java/lang/reflect/Field")) {
                    if (SandboxPolicy.FIELD_REFLECT_ACCESS.contains(name)) {
                        fieldAccessCall(delegate, name, descriptor);
                        return;
                    }
                    if (name.equals("setAccessible")
                            || name.equals("trySetAccessible")) {
                        accessibleCall(delegate, name, descriptor);
                        return;
                    }
                }
                if (owner.equals("java/lang/reflect/AccessibleObject")
                        && (name.equals("setAccessible")
                            || name.equals("trySetAccessible"))) {
                    accessibleCall(delegate, name, descriptor);
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

    private static void staticCall(MethodVisitor mv, String owner, String name,
            String descriptor) {
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, owner, name, descriptor, false);
    }

    /**
     * Rewrites a {@code java.util.ServiceLoader} chain to the empty guest
     * facade: service discovery never instantiates host or absent providers,
     * so libraries see the documented "no providers" result.
     */
    private void serviceLoaderCall(MethodVisitor mv, String name,
            String descriptor) {
        if (!SandboxPolicy.SERVICE_LOADER_ALLOWED.contains(name)) {
            throw denied("ServiceLoader." + name + descriptor
                    + ": service loading is not available");
        }
        if (name.equals("load")
                && descriptor.equals(
                        "(Ljava/lang/Class;)Ljava/util/ServiceLoader;")) {
            staticCall(mv, "tartine/guest/GuestServiceLoader", "load",
                    "(Ljava/lang/Class;)Ltartine/guest/GuestServiceLoader;");
            return;
        }
        if (name.equals("load")
                && descriptor.equals("(Ljava/lang/Class;Ljava/lang/ClassLoader;)"
                        + "Ljava/util/ServiceLoader;")) {
            staticCall(mv, "tartine/guest/GuestServiceLoader", "load",
                    "(Ljava/lang/Class;Ljava/lang/Object;)"
                    + "Ltartine/guest/GuestServiceLoader;");
            return;
        }
        if (name.equals("loadInstalled")
                && descriptor.equals(
                        "(Ljava/lang/Class;)Ljava/util/ServiceLoader;")) {
            staticCall(mv, "tartine/guest/GuestServiceLoader", "loadInstalled",
                    "(Ljava/lang/Class;)Ltartine/guest/GuestServiceLoader;");
            return;
        }
        if (name.equals("iterator")
                && descriptor.equals("()Ljava/util/Iterator;")) {
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                    "tartine/guest/GuestServiceLoader", "iterator",
                    "()Ljava/util/Iterator;", false);
            return;
        }
        if (name.equals("stream")
                && descriptor.equals("()Ljava/util/stream/Stream;")) {
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                    "tartine/guest/GuestServiceLoader", "stream",
                    "()Ljava/util/stream/Stream;", false);
            return;
        }
        if ((name.equals("findFirst") || name.equals("findNext"))
                && descriptor.equals("()Ljava/util/Optional;")) {
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                    "tartine/guest/GuestServiceLoader", name,
                    "()Ljava/util/Optional;", false);
            return;
        }
        if (name.equals("reload") && descriptor.equals("()V")) {
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                    "tartine/guest/GuestServiceLoader", "reload", "()V",
                    false);
            return;
        }
        throw denied("ServiceLoader." + name + descriptor
                + ": service loading is not available");
    }

    /**
     * Rewrites the safe {@code ClassLoader} surface to
     * {@code GuestClassLoader}; everything else stays a named denial.
     */
    private void classLoaderCall(MethodVisitor mv, String name,
            String descriptor) {
        if (!SandboxPolicy.CLASS_LOADER_ALLOWED.contains(name)) {
            throw denied("ClassLoader." + name + descriptor
                    + ": host class loader access is not available");
        }
        if (name.equals("loadClass")
                && descriptor.equals("(Ljava/lang/String;)Ljava/lang/Class;")) {
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                    "tartine/guest/GuestClassLoader", name, descriptor, false);
            return;
        }
        if (name.equals("getResources") && descriptor.equals(
                "(Ljava/lang/String;)Ljava/util/Enumeration;")) {
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                    "tartine/guest/GuestClassLoader", name, descriptor, false);
            return;
        }
        if (name.equals("getParent")
                && descriptor.equals("()Ljava/lang/ClassLoader;")) {
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                    "tartine/guest/GuestClassLoader", name, descriptor, false);
            return;
        }
        throw denied("ClassLoader." + name + descriptor
                + ": host class loader access is not available");
    }

    /**
     * Rewrites a {@code java.lang.Runtime} call: {@code getRuntime()} returns
     * the {@code GuestRuntime} singleton and the allowlisted queries become
     * static calls on it. Everything else is a named denial.
     */
    private void runtimeCall(MethodVisitor mv, String name, String descriptor) {
        if (name.equals("getRuntime")
                && descriptor.equals("()Ljava/lang/Runtime;")) {
            staticCall(mv, "tartine/guest/GuestRuntime", "getRuntime",
                    "()Ltartine/guest/GuestRuntime;");
            return;
        }
        if (SandboxPolicy.RUNTIME_ALLOWED.contains(name)
                && descriptor.startsWith("()")) {
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                    "tartine/guest/GuestRuntime", name, descriptor, false);
            return;
        }
        throw denied("Runtime." + name + descriptor
                + ": host runtime access is not available");
    }

    /**
     * Rewrites {@code System.getProperty} to the value allowlist facade. The
     * descriptor is unchanged because the receiver was already a static call;
     * {@code GuestSystem} takes the key as its first argument.
     */
    private void systemPropertyCall(MethodVisitor mv, String name,
            String descriptor) {
        if (descriptor.equals("(Ljava/lang/String;)Ljava/lang/String;")
                || descriptor.equals(
                        "(Ljava/lang/String;Ljava/lang/String;)"
                        + "Ljava/lang/String;")) {
            staticCall(mv, "tartine/guest/GuestSystem", "getProperty",
                    descriptor);
            return;
        }
        throw denied("System." + name + descriptor
                + ": host property access is not available");
    }

    /** Rewrites a {@code Class} reflection lookup with the receiver first. */
    private void classLookupCall(MethodVisitor mv, String name,
            String descriptor) {
        staticCall(mv, "tartine/guest/GuestReflect", name,
                "(Ljava/lang/Class;" + descriptor.substring(1));
    }

    /** Rewrites {@code Method.invoke} to the declaring-class-checked bridge. */
    private void methodInvokeCall(MethodVisitor mv, String descriptor) {
        staticCall(mv, "tartine/guest/GuestReflect", "invoke",
                "(Ljava/lang/reflect/Method;" + descriptor.substring(1));
    }

    /** Rewrites {@code Constructor.newInstance} to the checked bridge. */
    private void constructorNewInstanceCall(MethodVisitor mv,
            String descriptor) {
        staticCall(mv, "tartine/guest/GuestReflect", "newInstance",
                "(Ljava/lang/reflect/Constructor;" + descriptor.substring(1));
    }

    /** Rewrites one {@code Field} getter/setter to the checked bridge. */
    private void fieldAccessCall(MethodVisitor mv, String name,
            String descriptor) {
        staticCall(mv, "tartine/guest/GuestReflect", name,
                "(Ljava/lang/reflect/Field;" + descriptor.substring(1));
    }

    /**
     * Rewrites {@code setAccessible}/{@code trySetAccessible} to the bridge,
     * which checks the member's declaring class before granting access.
     */
    private void accessibleCall(MethodVisitor mv, String name,
            String descriptor) {
        staticCall(mv, "tartine/guest/GuestReflect", name,
                "(Ljava/lang/reflect/AccessibleObject;"
                + descriptor.substring(1));
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
