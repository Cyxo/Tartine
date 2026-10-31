package tartine.tx;

import java.util.Set;
import java.util.function.Predicate;
import org.objectweb.asm.Type;
import tartine.dex.DexClass;

/**
 * One source of truth for the guest sandbox boundary.
 *
 * <p>The loader applies {@link #classDenial(String)} when a host class is
 * requested; the translation pass applies the same policy to every
 * instruction and to class metadata. Keeping both on this table means a
 * class the loader refuses can never be reached through a translated call,
 * and a call the translator refuses cannot be smuggled past the loader.
 *
 * <p>Denials are described, never silently dropped: callers turn the
 * description into a named gap (method) or a named
 * {@code ClassNotFoundException} (class).
 */
final class SandboxPolicy {
    private SandboxPolicy() {
    }

    /** Java I/O types that remain available; everything else is denied. */
    private static final Set<String> JAVA_IO_ALLOWED = Set.of(
            "java.io.BufferedInputStream",
            "java.io.BufferedOutputStream",
            "java.io.BufferedReader",
            "java.io.BufferedWriter",
            "java.io.ByteArrayInputStream",
            "java.io.ByteArrayOutputStream",
            "java.io.CharArrayReader",
            "java.io.CharArrayWriter",
            "java.io.CharConversionException",
            "java.io.Closeable",
            "java.io.DataInput",
            "java.io.DataInputStream",
            "java.io.DataOutput",
            "java.io.DataOutputStream",
            "java.io.EOFException",
            "java.io.Externalizable",
            "java.io.File",
            "java.io.FileFilter",
            "java.io.FileNotFoundException",
            "java.io.FilenameFilter",
            "java.io.FilterInputStream",
            "java.io.FilterOutputStream",
            "java.io.FilterReader",
            "java.io.FilterWriter",
            "java.io.Flushable",
            "java.io.IOError",
            "java.io.IOException",
            "java.io.InputStream",
            "java.io.InputStreamReader",
            "java.io.InterruptedIOException",
            "java.io.InvalidClassException",
            "java.io.InvalidObjectException",
            "java.io.LineNumberInputStream",
            "java.io.LineNumberReader",
            "java.io.NotActiveException",
            "java.io.NotSerializableException",
            "java.io.ObjectInput",
            "java.io.ObjectInputFilter",
            "java.io.ObjectInputStream",
            "java.io.ObjectInputValidation",
            "java.io.ObjectOutput",
            "java.io.ObjectOutputStream",
            "java.io.ObjectStreamConstants",
            "java.io.ObjectStreamException",
            "java.io.OptionalDataException",
            "java.io.OutputStream",
            "java.io.OutputStreamWriter",
            "java.io.PrintStream",
            "java.io.PrintWriter",
            "java.io.PushbackInputStream",
            "java.io.PushbackReader",
            "java.io.Reader",
            "java.io.SequenceInputStream",
            "java.io.Serializable",
            "java.io.StreamCorruptedException",
            "java.io.StringReader",
            "java.io.StringWriter",
            "java.io.SyncFailedException",
            "java.io.UncheckedIOException",
            "java.io.UnsupportedEncodingException",
            "java.io.UTFDataFormatException",
            "java.io.WriteAbortedException",
            "java.io.Writer");

    /** {@code java.lang} classes that must never be reachable. */
    private static final Set<String> JAVA_LANG_DENIED = Set.of(
            "java.lang.ClassLoader",
            "java.lang.Compiler",
            "java.lang.Module",
            "java.lang.ModuleLayer",
            "java.lang.Process",
            "java.lang.ProcessBuilder",
            "java.lang.ProcessHandle",
            "java.lang.Runtime",
            "java.lang.SecurityManager",
            "java.lang.StackWalker");

    /** {@code java.lang.Class} operations that expose host reflection. */
    private static final Set<String> CLASS_DENIED = Set.of(
            "getClasses",
            "getClassLoader",
            "getConstructor",
            "getConstructors",
            "getDeclaredClasses",
            "getDeclaredConstructor",
            "getDeclaredConstructors",
            "getDeclaredField",
            "getDeclaredFields",
            "getDeclaredMethod",
            "getDeclaredMethods",
            "getEnclosingClass",
            "getEnclosingConstructor",
            "getEnclosingMethod",
            "getField",
            "getFields",
            "getMethod",
            "getMethods",
            "getModule",
            "getNestHost",
            "getNestMembers",
            "getPermittedSubclasses",
            "getProtectionDomain",
            "getRecordComponents",
            "getResource",
            "getResourceAsStream",
            "getResourceBundle",
            "getSigners",
            "newInstance");

    /** {@code java.lang.System} members that leak or alter the host. */
    private static final Set<String> SYSTEM_DENIED = Set.of(
            "clearProperty",
            "console",
            "exit",
            "getLogger",
            "getProperties",
            "getSecurityManager",
            "getenv",
            "getProperty",
            "inheritedChannel",
            "load",
            "loadLibrary",
            "runFinalization",
            "setErr",
            "setIn",
            "setOut",
            "setProperty",
            "setSecurityManager");

    /** {@code java.lang.Thread} members that expose host loaders/stacks. */
    private static final Set<String> THREAD_DENIED = Set.of(
            "getAllStackTraces",
            "getContextClassLoader",
            "getStackTrace",
            "resume",
            "setContextClassLoader",
            "stop",
            "suspend");

    /** {@code java.lang.reflect.Field} members that read/write values. */
    private static final Set<String> FIELD_DENIED = Set.of(
            "get",
            "getBoolean",
            "getByte",
            "getChar",
            "getDouble",
            "getFloat",
            "getInt",
            "getLong",
            "getShort",
            "set",
            "setBoolean",
            "setByte",
            "setChar",
            "setDouble",
            "setFloat",
            "setInt",
            "setLong",
            "setShort",
            "setAccessible",
            "trySetAccessible");

    /** Host loading policy for a class the dex set does not define. */
    static String classDenial(String internalName) {
        if (internalName == null) {
            return null;
        }
        if (internalName.startsWith("[")) {
            return typeDenial(internalName);
        }
        String name = internalName.replace('/', '.');
        if (name.startsWith("android.")) {
            return null; // the framework module is the guest's Android API
        }
        if (name.startsWith("tartine.guest.")) {
            return null; // the File bridge and Class.forName helper
        }
        if (name.startsWith("tartine.")) {
            return "host internals are not available: " + name;
        }
        if (name.startsWith("org.xmlpull.")) {
            return null; // framework XML pull interfaces
        }
        if (name.startsWith("org.")) {
            return "host library is not available: " + name;
        }
        if (name.startsWith("java.")) {
            return javaDenial(name);
        }
        if (name.startsWith("javax.")) {
            return javaxDenial(name);
        }
        return "host class is not available: " + name;
    }

    /** Host loading policy, skipping classes the guest dex set defines. */
    static String classDenial(String internalName, Predicate<String> isGuest) {
        if (internalName == null || isGuest.test(internalName)) {
            return null;
        }
        return classDenial(internalName);
    }

    private static String javaDenial(String name) {
        if (name.startsWith("java.net.")) {
            return "network access is not available: " + name;
        }
        if (name.startsWith("java.nio.file.")) {
            return "host paths are not available: " + name;
        }
        if (name.startsWith("java.nio.channels.")) {
            return "channels are not available: " + name;
        }
        if (name.startsWith("java.lang.invoke.")
                || name.startsWith("java.lang.management.")
                || name.startsWith("java.lang.instrument.")
                || name.startsWith("java.lang.foreign.")
                || name.startsWith("java.lang.constant.")) {
            return "host runtime internals are not available: " + name;
        }
        if (name.startsWith("java.rmi.")
                || name.startsWith("java.sql.")
                || name.startsWith("java.awt.")
                || name.startsWith("java.applet.")
                || name.startsWith("java.beans.")) {
            return "host desktop/database access is not available: " + name;
        }
        if (name.startsWith("java.util.logging.")
                || name.startsWith("java.util.prefs.")
                || name.startsWith("java.util.spi.")) {
            return "host configuration/logging is not available: " + name;
        }
        if (name.equals("java.util.ServiceLoader")
                || name.startsWith("java.util.ServiceLoader$")) {
            return "service loading is not available: " + name;
        }
        if (name.equals("java.util.ResourceBundle")
                || name.startsWith("java.util.ResourceBundle$")) {
            return "host resource bundles are not available: " + name;
        }
        if (name.equals("java.util.zip.ZipFile")
                || name.startsWith("java.util.zip.ZipFile$")) {
            return "host archive paths are not available: " + name;
        }
        if (name.equals("java.util.jar.JarFile")
                || name.startsWith("java.util.jar.JarFile$")) {
            return "host archive paths are not available: " + name;
        }
        if (name.equals("java.security.SecureClassLoader")) {
            return "class loading is not available: " + name;
        }
        if (name.startsWith("java.io.")) {
            if (JAVA_IO_ALLOWED.contains(name)) {
                return null;
            }
            return "raw file/stream access is not available: " + name;
        }
        if (name.startsWith("java.lang.")) {
            if (name.equals("java.lang.reflect.Proxy")
                    || name.startsWith("java.lang.reflect.Proxy$")) {
                return "dynamic proxies are not available: " + name;
            }
            for (String denied : JAVA_LANG_DENIED) {
                if (name.equals(denied) || name.startsWith(denied + "$")) {
                    return "host runtime access is not available: " + name;
                }
            }
            if (name.startsWith("java.lang.Process")) {
                return "process access is not available: " + name;
            }
        }
        return null;
    }

    private static String javaxDenial(String name) {
        if (name.startsWith("javax.crypto.")
                || name.startsWith("javax.security.cert.")
                || name.startsWith("javax.microedition.")
                || name.startsWith("javax.inject.")
                || name.startsWith("javax.annotation.")) {
            return null;
        }
        return "host extension API is not available: " + name;
    }

    /** Reserved names a guest dex class may never define. */
    static boolean isReservedGuestName(String dottedName) {
        return dottedName.startsWith("java.")
                || dottedName.startsWith("javax.")
                || dottedName.startsWith("jdk.")
                || dottedName.startsWith("sun.")
                || dottedName.startsWith("android.")
                || dottedName.startsWith("tartine.")
                || dottedName.startsWith("org.xmlpull.")
                || dottedName.startsWith("dalvik.")
                || dottedName.startsWith("libcore.");
    }

    /** Class-level metadata denial for a dex-defined guest class. */
    static String guestMetadataDenial(DexClass cls, Predicate<String> isGuest) {
        if (cls.superclass != null) {
            if (cls.superclass.equals("Ljava/io/File;")) {
                return "guest subclasses of java.io.File are not available";
            }
            String denial = typeDenial(cls.superclass, isGuest);
            if (denial != null) {
                return "superclass " + denial;
            }
        }
        for (String iface : cls.interfaces) {
            String denial = typeDenial(iface, isGuest);
            if (denial != null) {
                return "interface " + denial;
            }
        }
        for (DexClass.DexField field : cls.fields) {
            String denial = typeDenial(field.type, isGuest);
            if (denial != null) {
                return "field " + field.name + ": " + denial;
            }
        }
        return null;
    }

    /** Denial for a JVM type descriptor, or {@code null} when allowed. */
    static String typeDenial(String descriptor) {
        return typeDenial(descriptor, guestName -> false);
    }

    /** Type denial that treats {@code isGuest} classes as available. */
    static String typeDenial(String descriptor, Predicate<String> isGuest) {
        if (descriptor == null || descriptor.isEmpty()) {
            return null;
        }
        String desc = descriptor;
        while (desc.startsWith("[")) {
            desc = desc.substring(1);
        }
        if (desc.startsWith("L") && desc.endsWith(";")) {
            return classDenial(desc.substring(1, desc.length() - 1), isGuest);
        }
        return null; // primitive or void
    }

    /** Denial for a method/field reference, or {@code null} when allowed. */
    static String memberDenial(String owner, String name, String desc,
            boolean method) {
        return memberDenial(owner, name, desc, method, guestName -> false);
    }

    /** Member denial that treats {@code isGuest} owners as available. */
    static String memberDenial(String owner, String name, String desc,
            boolean method, Predicate<String> isGuest) {
        String ownerDenial = classDenial(owner, isGuest);
        if (ownerDenial != null) {
            return ownerDenial;
        }
        if (isGuest.test(owner)) {
            return null;
        }
        if (!method) {
            return fieldDenial(owner, name, desc, isGuest);
        }
        if (owner.equals("java/io/File")) {
            return fileMethodDenial(name, desc);
        }
        if (owner.equals("java/io/PrintStream")
                || owner.equals("java/io/PrintWriter")) {
            if (name.equals("<init>") && fileConstructor(desc)) {
                return "file-backed " + owner.replace('/', '.')
                        + " construction is not available";
            }
        }
        if (owner.equals("java/util/Scanner") && name.equals("<init>")
                && (desc.startsWith("(Ljava/io/File;")
                    || desc.startsWith("(Ljava/nio/file/Path;"))) {
            return "file-backed Scanner construction is not available";
        }
        if (owner.equals("java/util/Formatter") && name.equals("<init>")
                && (desc.startsWith("(Ljava/io/File;")
                    || desc.startsWith("(Ljava/lang/String;")
                    || desc.startsWith("(Ljava/nio/file/Path;"))) {
            return "file-backed Formatter construction is not available";
        }
        if (owner.equals("java/lang/System")) {
            if (SYSTEM_DENIED.contains(name)) {
                return "System." + name + " is not available";
            }
        }
        if (owner.equals("java/lang/Thread")
                && THREAD_DENIED.contains(name)) {
            return "Thread." + name + " is not available";
        }
        if (owner.equals("java/lang/Class")) {
            if (CLASS_DENIED.contains(name)) {
                return "Class." + name + " is not available";
            }
            if (name.equals("forName") && desc.equals(
                    "(Ljava/lang/String;ZLjava/lang/ClassLoader;)"
                    + "Ljava/lang/Class;")) {
                return "Class.forName with a host loader is not available";
            }
        }
        if (owner.equals("java/lang/reflect/AccessibleObject")
                && (name.equals("setAccessible") || name.equals("trySetAccessible"))) {
            return "reflective accessibility is not available";
        }
        if (owner.equals("java/lang/reflect/Method")
                && (name.equals("invoke") || name.equals("setAccessible")
                    || name.equals("trySetAccessible"))) {
            return "reflective invocation is not available";
        }
        if (owner.equals("java/lang/reflect/Constructor")
                && (name.equals("newInstance") || name.equals("setAccessible")
                    || name.equals("trySetAccessible"))) {
            return "reflective construction is not available";
        }
        if (owner.equals("java/lang/reflect/Field")
                && FIELD_DENIED.contains(name)) {
            return "reflective field access is not available";
        }
        String returnDenial = returnTypeDenial(desc, isGuest);
        if (returnDenial != null) {
            return returnDenial;
        }
        return null;
    }

    private static String fieldDenial(String owner, String name, String desc,
            Predicate<String> isGuest) {
        if (owner.equals("java/lang/System")) {
            if (name.equals("out") || name.equals("err")) {
                return null;
            }
            return "System." + name + " is not available";
        }
        if (owner.equals("java/io/File")) {
            if (name.equals("separator") || name.equals("separatorChar")
                    || name.equals("pathSeparator")
                    || name.equals("pathSeparatorChar")) {
                return null;
            }
            return "File." + name + " is not available";
        }
        return typeDenial(desc, isGuest);
    }

    private static String fileMethodDenial(String name, String desc) {
        if (name.equals("<init>")) {
            if (desc.equals("(Ljava/lang/String;)V")
                    || desc.equals("(Ljava/lang/String;Ljava/lang/String;)V")
                    || desc.equals("(Ljava/io/File;Ljava/lang/String;)V")) {
                return null; // rewritten to tartine/guest/GuestFile.<init>
            }
            return "File constructor is not available: " + desc;
        }
        if (name.equals("createTempFile") || name.equals("listRoots")) {
            return "File." + name + " is not available";
        }
        if (name.equals("toPath") || name.equals("toURI")
                || name.equals("toURL") || name.equals("deleteOnExit")) {
            return "File." + name + " is not available";
        }
        return null; // instance calls are rewritten to GuestFiles
    }

    private static boolean fileConstructor(String desc) {
        return desc.startsWith("(Ljava/io/File;")
                || desc.startsWith("(Ljava/lang/String;");
    }

    private static String returnTypeDenial(String desc,
            Predicate<String> isGuest) {
        if (desc == null || !desc.startsWith("(")) {
            return null;
        }
        Type returnType = Type.getReturnType(desc);
        return typeDenial(returnType.getDescriptor(), isGuest);
    }

    /** Denial for a method handle constant, or {@code null} when allowed. */
    static String handleDenial(String owner, String name, String desc) {
        return handleDenial(owner, name, desc, guestName -> false);
    }

    /** Handle denial that treats {@code isGuest} owners as available. */
    static String handleDenial(String owner, String name, String desc,
            Predicate<String> isGuest) {
        if (isGuest.test(owner)) {
            return null;
        }
        if (owner.equals("java/io/File")
                || owner.equals("java/lang/System")
                || owner.equals("java/lang/Class")
                || owner.equals("java/lang/Thread")
                || owner.startsWith("java/lang/reflect/")) {
            return "method handle to " + owner.replace('/', '.')
                    + "." + name + " is not available";
        }
        return memberDenial(owner, name, desc, true, isGuest);
    }

    /** Denial for an invokedynamic bootstrap target. */
    static String bootstrapDenial(String owner, String name, String desc,
            Predicate<String> isGuest) {
        if (isGuest.test(owner)) {
            return null;
        }
        if (owner.equals("java/lang/invoke/LambdaMetafactory")
                || owner.equals("java/lang/invoke/StringConcatFactory")
                || owner.equals("java/lang/runtime/ObjectMethods")) {
            return null;
        }
        if (owner.startsWith("java/lang/invoke/")) {
            return "invokedynamic bootstrap is not available: "
                    + owner.replace('/', '.') + "." + name;
        }
        return memberDenial(owner, name, desc, true, isGuest);
    }

    /** Denial for any class mentioned by a method type descriptor. */
    static String methodTypeDenial(String desc, Predicate<String> isGuest) {
        if (desc == null || !desc.startsWith("(")) {
            return typeDenial(desc, isGuest);
        }
        for (Type argument : Type.getArgumentTypes(desc)) {
            String denial = typeDenial(argument.getDescriptor(), isGuest);
            if (denial != null) {
                return denial;
            }
        }
        return typeDenial(Type.getReturnType(desc).getDescriptor(), isGuest);
    }
}
