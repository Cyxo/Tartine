package tartine.guest;

import java.lang.reflect.Method;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tartine.dex.Dex;
import tartine.dex.DexSet;
import tartine.dex.FixtureDex;
import tartine.tx.TranslatedLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Direct pins for the guest facades the translator routes host calls to. */
class GuestFacadesTest {
    @TempDir
    Path temp;

    private TranslatedLoader loader;
    private Class<?> hello;

    @BeforeEach
    void loadFixture() throws Exception {
        Path dexFile = temp.resolve("fixture.dex");
        FixtureDex.writeHello(dexFile);
        DexSet dex = DexSet.of(Dex.load(dexFile.toFile()));
        loader = new TranslatedLoader(getClass().getClassLoader(), dex);
        hello = loader.loadClass("test.Hello");
    }

    @Test
    void runtimeExposesOnlyReadOnlyQueries() {
        GuestRuntime runtime = GuestRuntime.getRuntime();
        assertSame(runtime, GuestRuntime.getRuntime());
        assertTrue(runtime.availableProcessors() > 0);
        assertTrue(runtime.totalMemory() > 0L);
        assertTrue(runtime.freeMemory() > 0L);
        assertTrue(runtime.maxMemory() > 0L);
        runtime.gc(); // advisory no-op
    }

    @Test
    void systemPropertyAllowlistReturnsDefaultsForEverythingElse() {
        assertNotNull(GuestSystem.getProperty("java.version"));
        assertNotNull(GuestSystem.getProperty("file.separator"));
        assertNotNull(GuestSystem.getProperty("user.language"));
        assertNull(GuestSystem.getProperty("user.home"));
        assertNull(GuestSystem.getProperty("java.class.path"));
        assertEquals("fallback",
                GuestSystem.getProperty("user.home", "fallback"));
        assertEquals("fallback",
                GuestSystem.getProperty("tartine.missing", "fallback"));
        assertThrows(NullPointerException.class,
                () -> GuestSystem.getProperty(null));
    }

    @Test
    void reflectionAllowsGuestDeclaredMembersOnly() throws Exception {
        Class<?> base = loader.loadClass("test.Base");
        Method who = GuestReflect.getMethod(base, "who");
        Object instance = GuestReflect.newInstance(base);
        assertEquals("base", GuestReflect.invoke(who, instance));

        // Inherited guest member: the declaring class is guest-loaded.
        Method inherited = GuestReflect.getMethod(loader.loadClass("test.Sub"),
                "who");
        assertEquals("base", GuestReflect.invoke(inherited, instance));

        // Host-declared members are denied at lookup...
        assertThrows(SandboxDenied.class,
                () -> GuestReflect.getDeclaredMethod(Object.class, "toString"));
        assertThrows(SandboxDenied.class,
                () -> GuestReflect.getMethod(Object.class, "toString"));
        // ...and again when a host member is reached through a guest class.
        assertThrows(SandboxDenied.class,
                () -> GuestReflect.getMethod(loader.loadClass("test.Sub"),
                        "toString"));
    }

    @Test
    void serviceLoaderIsEmptyAndIterable() {
        GuestServiceLoader services = GuestServiceLoader.load(Object.class);
        assertTrue(services instanceof Iterable);
        assertFalse(services.iterator().hasNext());
        assertFalse(GuestServiceLoader.load(Object.class, null)
                .stream().findAny().isPresent());
        assertFalse(services.findFirst().isPresent());
        assertFalse(services.findNext().isPresent());
        services.reload();
    }

    @Test
    void classLoaderFacadeStaysInsideTheGuest() throws Exception {
        GuestClassLoader guest = GuestReflect.getClassLoader(hello);
        assertNotNull(guest);
        assertEquals("test.Counter",
                guest.loadClass("test.Counter").getName());
        assertNull(guest.getResource("anything"));
        assertNull(guest.getResourceAsStream("anything"));
        assertFalse(guest.getResources("anything").hasMoreElements());
        assertNull(guest.getParent());

        ClassNotFoundException denied = assertThrows(
                ClassNotFoundException.class,
                () -> guest.loadClass("java.lang.Runtime"));
        assertTrue(denied.getMessage().contains("sandbox denied"),
                denied.getMessage());

        assertSame(hello, GuestSandbox.forName("test.Hello", false, guest));
        assertThrows(ClassNotFoundException.class,
                () -> GuestSandbox.forName("java.lang.Runtime", false, guest));
        // A host loader is refused rather than used.
        assertThrows(ClassNotFoundException.class,
                () -> GuestSandbox.forName("java.lang.String", false,
                        String.class));
        // The translator entry maps a null loader to the caller's guest
        // loader, so guest classes resolve and denied host classes do not.
        assertSame(hello, GuestSandbox.forName("test.Hello", false, null,
                hello));
        assertThrows(ClassNotFoundException.class,
                () -> GuestSandbox.forName("java.lang.Runtime", false, null,
                        hello));
    }
}
