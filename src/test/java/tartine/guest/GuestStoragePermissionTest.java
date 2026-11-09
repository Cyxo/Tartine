package tartine.guest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tartine.fw.PermissionManager;
import tartine.fw.PermissionPrompt;
import tartine.runtime.Vfs;

/**
 * Shared-storage permission gate pinned through the real guest file bridge:
 * denied reads look nonexistent, denied writes are SecurityException, and
 * app-specific external directories stay free.
 */
class GuestStoragePermissionTest {
    private static final String PKG = "com.example.app";
    private static final String READ_STORAGE =
            "android.permission.READ_EXTERNAL_STORAGE";
    private static final String MANAGE_STORAGE =
            "android.permission.MANAGE_EXTERNAL_STORAGE";

    @TempDir
    Path root;

    private String home;

    @BeforeEach
    void installHome() {
        home = System.getProperty("tartine.home");
        System.setProperty("tartine.home", root.toString());
        Vfs.resetCard();
        PermissionManager.reset();
    }

    @AfterEach
    void restoreHome() {
        PermissionManager.reset();
        if (home == null) {
            System.clearProperty("tartine.home");
        } else {
            System.setProperty("tartine.home", home);
        }
        Vfs.resetCard();
    }

    private GuestFile cardFile() throws Exception {
        Path dir = root.resolve("sdcard").resolve("Download");
        Files.createDirectories(dir);
        Files.writeString(dir.resolve("note.txt"), "hello");
        return new GuestFile("/sdcard/Download/note.txt");
    }

    private static PermissionPrompt answer(PermissionPrompt.Decision decision,
            AtomicInteger calls) {
        return (packageName, permission, label) -> {
            calls.incrementAndGet();
            return decision;
        };
    }

    @Test
    void unconfiguredManagerKeepsTheCardOpen() throws Exception {
        GuestFile file = cardFile();
        assertTrue(file.exists());
        try (FileInputStream in = new GuestFileInputStream(file)) {
            assertEquals("hello", new String(in.readAllBytes()));
        }
    }

    @Test
    void deniedReadLooksNonexistentAndDeniedWriteThrows() throws Exception {
        PermissionManager.configure(PKG, List.of(), 36, null);
        PermissionManager.setPrompt((packageName, permission, label) -> {
            throw new AssertionError("no storage permission is declared");
        });
        GuestFile file = cardFile();

        assertFalse(file.exists());
        assertFalse(file.canRead());
        assertNull(GuestFiles.list(new GuestFile("/sdcard/Download")));
        try (FileInputStream in = new GuestFileInputStream(file)) {
            in.read();
            fail("a denied read must look nonexistent");
        } catch (FileNotFoundException expected) {
            // denied reads answer as a nonexistent file
        }
        assertThrows(SecurityException.class, () -> {
            try (FileOutputStream out = new GuestFileOutputStream(file)) {
                out.write('x');
            }
        });
    }

    @Test
    void grantedReadAllowsReadingButNotWriting() throws Exception {
        PermissionManager.configure(PKG, List.of(READ_STORAGE), 36, null);
        AtomicInteger calls = new AtomicInteger();
        PermissionManager.setPrompt(answer(PermissionPrompt.Decision.GRANTED,
                calls));
        GuestFile file = cardFile();

        try (FileInputStream in = new GuestFileInputStream(file)) {
            assertEquals("hello", new String(in.readAllBytes()));
        }
        assertTrue(file.canRead());
        assertThrows(SecurityException.class, () -> {
            try (FileOutputStream out = new GuestFileOutputStream(file)) {
                out.write('x');
            }
        });
        assertEquals(1, calls.get());
    }

    @Test
    void allFilesGrantAllowsReadingAndWriting() throws Exception {
        PermissionManager.configure(PKG, List.of(MANAGE_STORAGE), 36, null);
        PermissionManager.setPrompt(answer(PermissionPrompt.Decision.GRANTED,
                new AtomicInteger()));
        GuestFile source = cardFile();

        try (FileInputStream in = new GuestFileInputStream(source)) {
            assertEquals("hello", new String(in.readAllBytes()));
        }
        GuestFile target = new GuestFile("/sdcard/Documents/out.txt");
        assertTrue(new GuestFile("/sdcard/Documents").mkdirs());
        try (FileOutputStream out = new GuestFileOutputStream(target)) {
            out.write('o');
        }
        assertTrue(Files.exists(root.resolve("sdcard").resolve("Documents")
                .resolve("out.txt")));
    }

    @Test
    void appSpecificExternalDirectoriesStayFree() throws Exception {
        PermissionManager.configure(PKG, List.of(), 36, null);
        PermissionManager.setPrompt((packageName, permission, label) -> {
            throw new AssertionError("app-specific external path must not prompt");
        });
        GuestFile file = new GuestFile(
                "/sdcard/Android/data/" + PKG + "/files/private.txt");

        assertTrue(file.getParentFile().mkdirs());
        assertTrue(file.createNewFile());
        assertTrue(file.exists());
        try (FileOutputStream out = new GuestFileOutputStream(file)) {
            out.write('p');
        }
        try (FileInputStream in = new GuestFileInputStream(file)) {
            assertEquals("p", new String(in.readAllBytes()));
        }
    }
}
