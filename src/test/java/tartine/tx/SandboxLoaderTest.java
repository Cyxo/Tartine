package tartine.tx;

import android.graphics.Paint;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tartine.dex.Dex;
import tartine.dex.DexSet;
import tartine.dex.FixtureDex;
import tartine.guest.GuestFile;
import tartine.guest.GuestSandbox;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Loader-side sandbox pins: the allowlist, guest-first resolution, array
 * construction, and the {@code Class.forName} bridge.
 */
class SandboxLoaderTest {
    @TempDir
    Path temp;

    private TranslatedLoader loader;

    @BeforeEach
    void loadFixture() throws Exception {
        Path dexFile = temp.resolve("fixture.dex");
        FixtureDex.writeHello(dexFile);
        DexSet dex = DexSet.of(Dex.load(dexFile.toFile()));
        loader = new TranslatedLoader(getClass().getClassLoader(), dex);
    }

    @Test
    void allowedHostClassesLoadThroughTheParent() throws Exception {
        assertSame(File.class, loader.loadClass("java.io.File"));
        // The file-backed stream types are loadable; construction is
        // rerouted by SandboxPass to the GuestFile* subclasses.
        assertSame(FileInputStream.class,
                loader.loadGuestClass("java.io.FileInputStream"));
        assertSame(FileOutputStream.class,
                loader.loadGuestClass("java.io.FileOutputStream"));
        assertSame(String.class, loader.loadClass("java.lang.String"));
        assertSame(Method.class, loader.loadClass("java.lang.reflect.Method"));
        assertSame(Paint.class, loader.loadClass("android.graphics.Paint"));
        assertSame(GuestFile.class, loader.loadClass("tartine.guest.GuestFile"));
    }

    @Test
    void deniedHostClassesFailClosedWithAName() {
        String[] names = {
            "java.net.Socket",
            "java.nio.file.Path",
            "java.nio.channels.FileChannel",
            "java.lang.Runtime",
            "java.lang.ProcessBuilder",
            "java.lang.ClassLoader",
            "java.lang.invoke.MethodHandles",
            "java.util.ServiceLoader",
            "java.util.logging.Logger",
            "org.objectweb.asm.ClassWriter",
            "tartine.Main",
        };
        for (String name : names) {
            ClassNotFoundException denied = assertThrows(
                    ClassNotFoundException.class,
                    () -> loader.loadGuestClass(name), name);
            assertTrue(denied.getMessage().contains("sandbox denied"),
                    name + ": " + denied.getMessage());
        }
    }

    @Test
    void linkageResolvesDeniedHostTypesWithoutExposingThem() throws Exception {
        // The JVM resolves every descriptor type of a translated class; a
        // framework signature may mention a type the guest can never use.
        assertSame(ClassLoader.class, loader.loadClass("java.lang.ClassLoader"));
        assertSame(Runtime.class, loader.loadClass("java.lang.Runtime"));
        // Guest-reachable lookups still fail closed.
        assertThrows(ClassNotFoundException.class,
                () -> loader.loadGuestClass("java.lang.ClassLoader"));
    }

    @Test
    void guestClassesResolveThroughTheDexSet() throws Exception {
        Class<?> hello = loader.loadClass("test.Hello");
        assertSame(loader, hello.getClassLoader());
        assertEquals("test.Hello", hello.getName());
        Class<?> counter = loader.loadClass("test.Counter");
        assertSame(loader, counter.getClassLoader());
        assertTrue(loader.isGuest("test/Counter"));
        assertFalse(loader.isGuest("java/io/File"));
    }

    @Test
    void arraysResolveThroughTheLoader() throws Exception {
        Class<?> counter = loader.loadClass("test.Counter");
        Class<?> array = loader.loadClass("[Ltest.Counter;");
        assertTrue(array.isArray());
        assertSame(counter, array.getComponentType());
        assertSame(int[].class, loader.loadClass("[I"));
        Class<?> nested = loader.loadClass("[[Ltest.Counter;");
        assertSame(array, nested.getComponentType());
    }

    @Test
    void guestForNameUsesTheGuestLoaderOnly() throws Exception {
        Class<?> hello = loader.loadClass("test.Hello");
        assertSame(String.class, GuestSandbox.forName("java.lang.String", hello));
        assertSame(hello, GuestSandbox.forName("test.Hello", hello));
        assertThrows(ClassNotFoundException.class,
                () -> GuestSandbox.forName("java.lang.Runtime", hello));
        // A host caller is refused rather than falling back to the host loader.
        assertThrows(ClassNotFoundException.class,
                () -> GuestSandbox.forName("java.lang.String", String.class));
        assertThrows(ClassNotFoundException.class,
                () -> GuestSandbox.forName("java.lang.String", null));
    }

    @Test
    void loaderResourcesStayHidden() {
        assertNull(loader.getResource("any.txt"));
        assertNull(loader.getResourceAsStream("any.txt"));
        assertFalse(loader.getResources("any.txt").hasMoreElements());
    }
}
