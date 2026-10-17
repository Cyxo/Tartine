package tartine.runtime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/** Behavioral pins for the guest-path seam (tartine.runtime.Vfs). */
class VfsTest {
    @TempDir
    Path root;

    @TempDir
    Path home;

    private Path t(String guest) {
        return Vfs.target(guest, root, home);
    }

    @Test
    void sdcardMapsToHostHome() {
        assertEquals(home, t("/sdcard"));
        assertEquals(home.resolve("Documents").resolve("a.txt"), t("/sdcard/Documents/a.txt"));
    }

    @Test
    void emulatedStorageAliasesSdcard() {
        assertEquals(home.resolve("Music"), t("/storage/emulated/0/Music"));
    }

    @Test
    void appDataDirsMapUnderRootData() {
        assertEquals(root.resolve("data").resolve("com.example").resolve("files").resolve("f.txt"),
                t("/data/data/com.example/files/f.txt"));
    }

    @Test
    void multiUserDataSpellingMapsTheSame() {
        assertEquals(root.resolve("data").resolve("com.example").resolve("cache").resolve("c"),
                t("/data/user/0/com.example/cache/c"));
    }

    @Test
    void installedAppApkIsGuestVisible() {
        assertEquals(root.resolve("apps").resolve("com.example").resolve("base.apk"),
                t("/data/app/com.example/base.apk"));
    }

    @Test
    void localTmpMapsToRootTmp() {
        assertEquals(root.resolve("tmp").resolve("x"), t("/data/local/tmp/x"));
    }

    @Test
    void longestPrefixWins() {
        // /data/data/com.example vs a (non-mounted) /data/data/com.example/files/extra tie-break:
        // deeper mount prefixes must never be shadowed by shorter ones.
        assertEquals(root.resolve("data").resolve("com.example").resolve("databases").resolve("x.db"),
                t("/data/data/com.example/databases/x.db"));
        assertEquals(root.resolve("tmp").resolve("y"), t("/data/local/tmp/y"));
    }

    @Test
    void deviceTreesStayUnmapped() {
        assertNull(t("/system/lib/libc.so"));
        assertNull(t("/proc/self/maps"));
        assertNull(t("/dev/urandom"));
        assertNull(t("/etc/passwd"));
        assertNull(t("/data/private"));
    }

    @Test
    void pathTraversalEscapesAreDenied() {
        assertNull(t("/sdcard/../../etc/passwd"));
        assertNull(t("/data/data/com.example/../../hostfile"));
        assertNull(t("/data/data/com.example/../otherpkg/file"));
    }

    @Test
    void windowsUnsafeSegmentsAreDenied() {
        assertNull(t("/sdcard/a:b"));
        assertNull(t("/sdcard/CON"));
        assertNull(t("/sdcard/report.txt:NtfsStream"));
        assertNull(t("/sdcard/aux.c"));
        assertNull(t("/sdcard/LPT9"));
    }

    @Test
    void relativeAndEmptyGuestPathsDoNotResolve() {
        assertNull(t("relative/path"));
        assertNull(t(""));
    }

    @Test
    void doubleAndTrailingSlashesCleanUp() {
        assertEquals(home.resolve("a").resolve("b"), t("/sdcard//a//b/"));
        assertEquals(home.resolve("x"),
                Vfs.target("/sdcard/./x", root, home));
    }

    @Test
    void exactMountRootResolvesToItsDirectory() {
        assertEquals(root.resolve("data").resolve("com.example"), t("/data/data/com.example"));
        assertEquals(home, Vfs.target("/storage/emulated/0", root, home));
    }
}
