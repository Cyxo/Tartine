package tartine.tx;

import java.util.function.Predicate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Direct pins for the sandbox policy table the loader and pass share. */
class SandboxPolicyTest {
    @Test
    void hostAllowlist() {
        assertNull(SandboxPolicy.classDenial("java/io/File"));
        assertNull(SandboxPolicy.classDenial("java/lang/String"));
        assertNull(SandboxPolicy.classDenial("java/lang/reflect/Method"));
        assertNull(SandboxPolicy.classDenial("java/util/ArrayList"));
        assertNull(SandboxPolicy.classDenial("javax/crypto/Cipher"));
        assertNull(SandboxPolicy.classDenial("javax/microedition/khronos/opengles/GL10"));
        assertNull(SandboxPolicy.classDenial("android/graphics/Paint"));
        assertNull(SandboxPolicy.classDenial("org/xmlpull/v1/XmlPullParser"));
        assertNull(SandboxPolicy.classDenial("tartine/guest/GuestFile"));
        assertNull(SandboxPolicy.classDenial("tartine/guest/GuestFiles"));
    }

    @Test
    void hostDenies() {
        assertNotNull(SandboxPolicy.classDenial("java/net/Socket"));
        assertNotNull(SandboxPolicy.classDenial("java/io/FileInputStream"));
        assertNotNull(SandboxPolicy.classDenial("java/io/RandomAccessFile"));
        assertNotNull(SandboxPolicy.classDenial("java/io/FileDescriptor"));
        assertNotNull(SandboxPolicy.classDenial("java/nio/file/Path"));
        assertNotNull(SandboxPolicy.classDenial("java/nio/channels/FileChannel"));
        assertNotNull(SandboxPolicy.classDenial("java/lang/Runtime"));
        assertNotNull(SandboxPolicy.classDenial("java/lang/ProcessBuilder"));
        assertNotNull(SandboxPolicy.classDenial("java/lang/ProcessHandle"));
        assertNotNull(SandboxPolicy.classDenial("java/lang/ClassLoader"));
        assertNotNull(SandboxPolicy.classDenial("java/lang/invoke/MethodHandles"));
        assertNotNull(SandboxPolicy.classDenial("java/lang/Module"));
        assertNotNull(SandboxPolicy.classDenial("java/lang/StackWalker"));
        assertNotNull(SandboxPolicy.classDenial("java/lang/reflect/Proxy"));
        assertNotNull(SandboxPolicy.classDenial("java/util/ServiceLoader"));
        assertNotNull(SandboxPolicy.classDenial("java/util/ResourceBundle"));
        assertNotNull(SandboxPolicy.classDenial("java/util/logging/Logger"));
        assertNotNull(SandboxPolicy.classDenial("java/util/prefs/Preferences"));
        assertNotNull(SandboxPolicy.classDenial("java/util/zip/ZipFile"));
        assertNotNull(SandboxPolicy.classDenial("java/util/jar/JarFile"));
        assertNotNull(SandboxPolicy.classDenial("java/rmi/Remote"));
        assertNotNull(SandboxPolicy.classDenial("java/sql/DriverManager"));
        assertNotNull(SandboxPolicy.classDenial("java/awt/Window"));
        assertNotNull(SandboxPolicy.classDenial("javax/xml/parsers/DocumentBuilderFactory"));
        assertNotNull(SandboxPolicy.classDenial("javax/naming/Context"));
        assertNotNull(SandboxPolicy.classDenial("org/objectweb/asm/ClassWriter"));
        assertNotNull(SandboxPolicy.classDenial("jdk/internal/misc/Unsafe"));
        assertNotNull(SandboxPolicy.classDenial("sun/misc/Unsafe"));
        assertNotNull(SandboxPolicy.classDenial("tartine/Main"));
        assertNotNull(SandboxPolicy.classDenial("tartine/tx/TxTranslator"));
        assertNotNull(SandboxPolicy.classDenial("dalvik/system/DexClassLoader"));
        assertNotNull(SandboxPolicy.classDenial("libcore/io/IoUtils"));
        assertNotNull(SandboxPolicy.classDenial("kotlin/io/FilesKt"));
    }

    @Test
    void guestClassesAreAlwaysAvailable() {
        Predicate<String> isGuest = name -> name.equals("com/example/App");
        assertNull(SandboxPolicy.classDenial("com/example/App", isGuest));
        assertNull(SandboxPolicy.typeDenial("Lcom/example/App;", isGuest));
        assertNull(SandboxPolicy.memberDenial("com/example/App", "run", "()V",
                true, isGuest));
        assertNull(SandboxPolicy.typeDenial("[Lcom/example/App;", isGuest));
        assertNotNull(SandboxPolicy.typeDenial("[Ljava/net/Socket;", isGuest));
    }

    @Test
    void memberTable() {
        assertNotNull(SandboxPolicy.memberDenial("java/io/File", "toPath",
                "()Ljava/nio/file/Path;", true));
        assertNotNull(SandboxPolicy.memberDenial("java/io/File", "toURI",
                "()Ljava/net/URI;", true));
        assertNotNull(SandboxPolicy.memberDenial("java/io/File", "deleteOnExit",
                "()V", true));
        assertNotNull(SandboxPolicy.memberDenial("java/io/File", "createTempFile",
                "(Ljava/lang/String;Ljava/lang/String;)Ljava/io/File;", true));
        assertNotNull(SandboxPolicy.memberDenial("java/io/File", "listRoots",
                "()[Ljava/io/File;", true));
        assertNotNull(SandboxPolicy.memberDenial("java/io/File", "<init>",
                "(Ljava/net/URI;)V", true));
        assertNull(SandboxPolicy.memberDenial("java/io/File", "exists",
                "()Z", true));
        assertNull(SandboxPolicy.memberDenial("java/io/File", "<init>",
                "(Ljava/lang/String;)V", true));
        assertNull(SandboxPolicy.memberDenial("java/io/File", "<init>",
                "(Ljava/lang/String;Ljava/lang/String;)V", true));
        assertNotNull(SandboxPolicy.memberDenial("java/io/PrintStream", "<init>",
                "(Ljava/io/File;)V", true));
        assertNotNull(SandboxPolicy.memberDenial("java/io/PrintWriter", "<init>",
                "(Ljava/lang/String;)V", true));
        assertNull(SandboxPolicy.memberDenial("java/io/PrintStream", "println",
                "(Ljava/lang/Object;)V", true));
        assertNotNull(SandboxPolicy.memberDenial("java/util/Scanner", "<init>",
                "(Ljava/io/File;)V", true));
        assertNotNull(SandboxPolicy.memberDenial("java/util/Scanner", "<init>",
                "(Ljava/nio/file/Path;)V", true));
        assertNull(SandboxPolicy.memberDenial("java/util/Scanner", "<init>",
                "(Ljava/lang/String;)V", true));
        assertNotNull(SandboxPolicy.memberDenial("java/util/Formatter", "<init>",
                "(Ljava/lang/String;)V", true));
        assertNotNull(SandboxPolicy.memberDenial("java/lang/System",
                "getProperty", "(Ljava/lang/String;)Ljava/lang/String;", true));
        assertNotNull(SandboxPolicy.memberDenial("java/lang/System", "exit",
                "(I)V", true));
        assertNotNull(SandboxPolicy.memberDenial("java/lang/System",
                "setProperty",
                "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;",
                true));
        assertNotNull(SandboxPolicy.memberDenial("java/lang/System", "getenv",
                "(Ljava/lang/String;)Ljava/lang/String;", true));
        assertNotNull(SandboxPolicy.memberDenial("java/lang/Runtime",
                "getRuntime", "()Ljava/lang/Runtime;", true));
        assertNotNull(SandboxPolicy.memberDenial("java/lang/Runtime", "exec",
                "(Ljava/lang/String;)Ljava/lang/Process;", true));
        assertNotNull(SandboxPolicy.memberDenial("java/lang/Runtime", "exit",
                "(I)V", true));
        assertNotNull(SandboxPolicy.memberDenial("java/lang/Runtime", "halt",
                "(I)V", true));
        assertNotNull(SandboxPolicy.memberDenial("java/lang/Runtime",
                "addShutdownHook", "(Ljava/lang/Thread;)V", true));
        assertNotNull(SandboxPolicy.memberDenial("java/lang/Runtime", "load",
                "(Ljava/lang/String;)V", true));
        assertNotNull(SandboxPolicy.memberDenial("java/lang/Runtime",
                "loadLibrary", "(Ljava/lang/String;)V", true));
        assertNotNull(SandboxPolicy.memberDenial("java/lang/System", "in",
                "Ljava/io/InputStream;", false));
        assertNull(SandboxPolicy.memberDenial("java/lang/System", "out",
                "Ljava/io/PrintStream;", false));
        assertNull(SandboxPolicy.memberDenial("java/lang/System", "err",
                "Ljava/io/PrintStream;", false));
        assertNotNull(SandboxPolicy.memberDenial("java/lang/Class",
                "getDeclaredMethod",
                "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;",
                true));
        assertNotNull(SandboxPolicy.memberDenial("java/lang/Class", "getField",
                "(Ljava/lang/String;)Ljava/lang/reflect/Field;", true));
        assertNotNull(SandboxPolicy.memberDenial("java/lang/Class", "forName",
                "(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;",
                true));
        assertNotNull(SandboxPolicy.memberDenial("java/lang/Class",
                "getClassLoader", "()Ljava/lang/ClassLoader;", true));
        assertNotNull(SandboxPolicy.memberDenial("java/lang/reflect/Method",
                "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;",
                true));
        assertNotNull(SandboxPolicy.memberDenial("java/lang/reflect/Field",
                "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true));
        assertNotNull(SandboxPolicy.memberDenial("java/lang/reflect/Constructor",
                "newInstance", "([Ljava/lang/Object;)Ljava/lang/Object;", true));
        assertNotNull(SandboxPolicy.memberDenial("java/lang/reflect/AccessibleObject",
                "setAccessible", "(Z)V", true));
        assertNotNull(SandboxPolicy.memberDenial("java/lang/Thread",
                "getStackTrace", "()[Ljava/lang/StackTraceElement;", true));
        assertNotNull(SandboxPolicy.memberDenial("java/lang/Thread",
                "getContextClassLoader", "()Ljava/lang/ClassLoader;", true));
        assertNull(SandboxPolicy.memberDenial("java/lang/Thread", "currentThread",
                "()Ljava/lang/Thread;", true));
        assertNull(SandboxPolicy.memberDenial("java/lang/Thread", "sleep",
                "(J)V", true));
    }

    @Test
    void facadeAllowlistsAreNarrow() {
        assertTrue(SandboxPolicy.RUNTIME_ALLOWED.contains("maxMemory"));
        assertFalse(SandboxPolicy.RUNTIME_ALLOWED.contains("exec"));
        assertFalse(SandboxPolicy.RUNTIME_ALLOWED.contains("exit"));
        assertFalse(SandboxPolicy.RUNTIME_ALLOWED.contains("halt"));
        assertTrue(SandboxPolicy.CLASS_LOADER_ALLOWED.contains("loadClass"));
        assertFalse(SandboxPolicy.CLASS_LOADER_ALLOWED.contains(
                "getSystemClassLoader"));
        assertTrue(SandboxPolicy.SERVICE_LOADER_ALLOWED.contains("iterator"));
        assertFalse(SandboxPolicy.SERVICE_LOADER_ALLOWED.contains("provider"));
    }

    @Test
    void returnTypesAreCheckedAtCallSites() {
        // A host method handing back a denied type is denied even when the
        // owner and name are otherwise fine.
        assertNotNull(SandboxPolicy.memberDenial("java/util/Objects", "fake",
                "()Ljava/lang/ClassLoader;", true));
        assertNull(SandboxPolicy.memberDenial("java/util/Objects", "fake",
                "()Ljava/lang/String;", true));
        assertNull(SandboxPolicy.memberDenial("java/lang/Math", "max",
                "(II)I", true));
    }

    @Test
    void reservedGuestNames() {
        assertTrue(SandboxPolicy.isReservedGuestName("java.lang.Evil"));
        assertTrue(SandboxPolicy.isReservedGuestName("javax.crypto.Evil"));
        assertTrue(SandboxPolicy.isReservedGuestName("android.util.Evil"));
        assertTrue(SandboxPolicy.isReservedGuestName("tartine.guest.Evil"));
        assertTrue(SandboxPolicy.isReservedGuestName("org.xmlpull.v1.Evil"));
        assertTrue(SandboxPolicy.isReservedGuestName("dalvik.system.Evil"));
        assertTrue(SandboxPolicy.isReservedGuestName("libcore.io.Evil"));
        assertFalse(SandboxPolicy.isReservedGuestName("com.example.App"));
        assertFalse(SandboxPolicy.isReservedGuestName("kotlin.Unit"));
    }
}
