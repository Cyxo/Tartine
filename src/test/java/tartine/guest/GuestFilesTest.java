package tartine.guest;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tartine.TestLinks;
import tartine.runtime.Vfs;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Behavioral pins for the File bridge: guest paths map through the VFS,
 * reads on unmapped paths answer nonexistent, writes fail closed with a
 * named denial, and path getters never leak the host layout.
 */
class GuestFilesTest {
    @TempDir
    Path root;

    private String home;

    @BeforeEach
    void installHome() {
        home = System.getProperty("tartine.home");
        System.setProperty("tartine.home", root.toString());
        Vfs.resetCard();
    }

    @AfterEach
    void restoreHome() {
        if (home == null) {
            System.clearProperty("tartine.home");
        } else {
            System.setProperty("tartine.home", home);
        }
        Vfs.resetCard();
    }

    @Test
    void appDataFilesRoundTripThroughTheVfs() throws Exception {
        GuestFile file = new GuestFile("/data/data/com.example/files/hello.txt");
        assertFalse(file.exists());
        assertTrue(file.getParentFile().mkdirs());
        assertTrue(file.createNewFile());
        Path host = root.resolve("data").resolve("com.example")
                .resolve("files").resolve("hello.txt");
        assertTrue(Files.exists(host));
        assertEquals("/data/data/com.example/files/hello.txt", file.getPath());
        assertEquals("/data/data/com.example/files/hello.txt", file.getAbsolutePath());
        assertEquals("/data/data/com.example/files", file.getParent());
        assertEquals("hello.txt", file.getName());
        assertFalse(file.getAbsolutePath().contains(root.toString()));
        assertTrue(file.canRead());
        assertTrue(file.canWrite());
        assertEquals(0L, file.length());
        assertTrue(file.delete());
        assertFalse(Files.exists(host));
    }

    @Test
    void sdcardAndParentConstructorsStayGuestSpelled() throws Exception {
        GuestFile dir = new GuestFile("/sdcard/Documents");
        assertTrue(dir.mkdirs());
        GuestFile note = new GuestFile(dir, "note.txt");
        assertTrue(note.createNewFile());
        assertTrue(Files.exists(root.resolve("sdcard").resolve("Documents")
                .resolve("note.txt")));
        assertEquals("/sdcard/Documents/note.txt", note.getPath());
        assertEquals("/sdcard/Documents/note.txt", note.getAbsolutePath());
        assertEquals("/sdcard/Documents", note.getParent());
    }

    @Test
    void unmappedPathsReadAsNonexistent() {
        GuestFile proc = new GuestFile("/proc/self/maps");
        assertFalse(proc.exists());
        assertFalse(proc.isFile());
        assertFalse(proc.isDirectory());
        assertFalse(proc.canRead());
        assertFalse(proc.canWrite());
        assertEquals(0L, proc.length());
        assertEquals(0L, proc.lastModified());
        assertNull(proc.list());
        assertNull(proc.listFiles());
    }

    @Test
    void unmappedWritesAreDeniedByName() {
        GuestFile proc = new GuestFile("/proc/self/maps");
        assertThrows(SandboxDenied.class, proc::createNewFile);
        assertThrows(SandboxDenied.class, proc::mkdir);
        assertThrows(SandboxDenied.class, proc::mkdirs);
        assertThrows(SandboxDenied.class, proc::delete);
        assertThrows(SandboxDenied.class, proc::deleteOnExit);
        assertThrows(SandboxDenied.class, () -> proc.setLastModified(1L));
        assertThrows(SandboxDenied.class, proc::setReadOnly);
        assertThrows(SandboxDenied.class,
                () -> proc.renameTo(new GuestFile("/sdcard/x")));
        assertThrows(SandboxDenied.class, proc::toPath);
        assertThrows(SandboxDenied.class, proc::toURI);
        assertThrows(SandboxDenied.class, proc::toURL);
        // Static File surfaces are hidden on GuestFile too, so a crafted
        // guest naming the facade directly cannot create host temp files.
        assertThrows(SandboxDenied.class,
                () -> GuestFile.createTempFile("t", ".tmp"));
        assertThrows(SandboxDenied.class, GuestFile::listRoots);
    }

    @Test
    void traversalIsDeniedEvenWhenTheParentMaps() {
        GuestFile escape = new GuestFile("/sdcard/../../etc/passwd");
        assertFalse(escape.exists());
        assertThrows(SandboxDenied.class, escape::mkdirs);
        GuestFile dotdot = new GuestFile("/sdcard/..");
        assertFalse(dotdot.exists());
        assertThrows(SandboxDenied.class, dotdot::createNewFile);
    }

    @Test
    void relativePathsNeverTouchTheHostCwd() {
        GuestFile relative = new GuestFile("relative.txt");
        assertFalse(relative.exists());
        assertFalse(relative.isAbsolute());
        assertEquals("relative.txt", relative.getPath());
        assertEquals("/relative.txt", relative.getAbsolutePath());
        assertFalse(relative.getAbsolutePath().contains(root.toString()));
        assertThrows(SandboxDenied.class, relative::createNewFile);
    }

    @Test
    void readOnlyCardAllowsReadsAndRefusesWrites() throws Exception {
        Path card = root.resolve("card");
        Files.createDirectories(card);
        Files.writeString(card.resolve("keep.txt"), "hello");
        Vfs.configure(new Vfs.Sdcard(card, true));

        GuestFile keep = new GuestFile("/sdcard/keep.txt");
        assertTrue(keep.exists());
        assertTrue(keep.isFile());
        assertEquals(5L, keep.length());
        assertThrows(SandboxDenied.class, keep::createNewFile);
        assertThrows(SandboxDenied.class, keep::delete);
        assertThrows(SandboxDenied.class, keep::setReadOnly);
        assertThrows(SandboxDenied.class, () -> keep.setLastModified(1L));
        assertThrows(SandboxDenied.class,
                () -> new GuestFile("/sdcard/new.txt").mkdirs());

        // App-private mounts stay writable while the card is read-only.
        GuestFile data = new GuestFile("/data/data/com.example/files/x.txt");
        assertTrue(data.getParentFile().mkdirs());
        assertTrue(data.createNewFile());
    }

    @Test
    void symlinkedEscapesStayInvisible() throws Exception {
        Path card = root.resolve("sdcard");
        Files.createDirectories(card);
        Path outside = root.resolve("outside");
        Files.createDirectories(outside);
        Files.writeString(outside.resolve("secret.txt"), "secret");
        assumeTrue(TestLinks.create(card.resolve("escape"), outside),
                "symlink/junction creation unavailable");

        GuestFile secret = new GuestFile("/sdcard/escape/secret.txt");
        assertFalse(secret.exists());
        assertFalse(secret.isFile());
        assertEquals(0L, secret.length());
        assertFalse(secret.canRead());
        assertNull(new GuestFile("/sdcard/escape").list());
        assertThrows(SandboxDenied.class,
                () -> new GuestFile("/sdcard/escape/new.txt").createNewFile());
        assertFalse(Files.exists(outside.resolve("new.txt")));
    }

    @Test
    void hostFilesReverseMapForFrameworkReturns() throws Exception {
        Files.createDirectories(root.resolve("data").resolve("pkg"));
        File host = root.resolve("data").resolve("pkg").resolve("f.bin").toFile();
        assertTrue(host.createNewFile());
        assertEquals("/data/data/pkg/f.bin", GuestFiles.guestPath(host));
        assertTrue(GuestFiles.exists(host));
        assertTrue(new GuestFile("/data/data/pkg/f.bin").equals(host));

        File outside = root.resolve("outside.txt").toFile();
        assertTrue(outside.createNewFile());
        assertThrows(SandboxDenied.class, () -> GuestFiles.guestPath(outside));
        assertFalse(GuestFiles.exists(outside));
        assertFalse(GuestFiles.equals(new GuestFile("/data/data/pkg/f.bin"),
                outside));
        assertEquals("<outside guest filesystem>", GuestFiles.toString(outside));
        assertFalse(GuestFiles.toString(outside).contains(root.toString()));
    }

    @Test
    void listingStaysGuestSpelled() throws Exception {
        Path dir = root.resolve("sdcard").resolve("dir");
        Files.createDirectories(dir);
        Files.writeString(dir.resolve("a.txt"), "a");
        Files.writeString(dir.resolve("b.bin"), "b");

        GuestFile guest = new GuestFile("/sdcard/dir");
        String[] names = guest.list();
        Arrays.sort(names);
        assertArrayEquals(new String[] {"a.txt", "b.bin"}, names);

        String[] text = guest.list((d, n) -> n.endsWith(".txt"));
        assertArrayEquals(new String[] {"a.txt"}, text);

        File[] files = guest.listFiles();
        assertEquals(2, files.length);
        for (File file : files) {
            assertTrue(file instanceof GuestFile);
            assertTrue(((GuestFile) file).getPath().startsWith("/sdcard/dir/"));
        }

        File[] filtered = guest.listFiles(
                (FileFilter) f -> f.getName().endsWith(".txt"));
        assertEquals(1, filtered.length);
        assertEquals("/sdcard/dir/a.txt", filtered[0].getPath());
    }

    @Test
    void canonicalAndAbsoluteFormsStayGuest() {
        GuestFile messy = new GuestFile("/sdcard//Documents/./notes.txt");
        assertEquals("/sdcard/Documents/notes.txt", messy.getCanonicalPath());
        assertEquals("/sdcard/Documents/notes.txt",
                messy.getCanonicalFile().getPath());
        assertEquals("/sdcard//Documents/./notes.txt", messy.getPath());
        assertEquals("/sdcard//Documents/./notes.txt", messy.getAbsolutePath());
        assertFalse(messy.getCanonicalPath().contains(root.toString()));
    }

    @Test
    void equalityAndOrderingUseTheGuestSpelling() {
        GuestFile a = new GuestFile("/sdcard/a");
        GuestFile b = new GuestFile("/sdcard/b");
        assertEquals(a, new GuestFile("/sdcard/a"));
        assertEquals(a.hashCode(), new GuestFile("/sdcard/a").hashCode());
        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
        assertNotEquals(a, b);
        assertFalse(a.equals("not a file"));
    }
}
