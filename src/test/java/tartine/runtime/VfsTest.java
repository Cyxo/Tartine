package tartine.runtime;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Behavioral pins for the guest-path seam (tartine.runtime.Vfs). */
class VfsTest {
    @TempDir
    Path root;

    private Vfs.Sdcard card() {
        // Default-tier shape for tests: the card lives inside the temp root.
        return new Vfs.Sdcard(root.resolve("sdcard"), false);
    }

    private Path t(String guest) {
        return Vfs.target(guest, root, card());
    }

    @Test
    void sdcardMapsToTheVirtualCard() {
        Path card = root.resolve("sdcard");
        assertEquals(card, t("/sdcard"));
        assertEquals(card.resolve("Documents").resolve("a.txt"),
                t("/sdcard/Documents/a.txt"));
        assertFalse(card().readOnly());
    }

    @Test
    void emulatedStorageAliasesSdcard() {
        assertEquals(root.resolve("sdcard").resolve("Music"), t("/storage/emulated/0/Music"));
    }

    @Test
    void explicitMountReplacesTheCard() {
        Path external = root.resolve("shared");
        Vfs.Sdcard override = new Vfs.Sdcard(external, false);
        assertEquals(external.resolve("Download"), Vfs.target("/sdcard/Download",
                root, override));
        assertEquals(external.resolve("x"), Vfs.target("/storage/emulated/0/x",
                root, override));
        // App-private mounts are unaffected by the card choice.
        assertEquals(root.resolve("data").resolve("com.example").resolve("files"),
                Vfs.target("/data/data/com.example/files", root, override));
    }

    @Test
    void overrideRootsAnswerReadOnlyPerConfig() {
        Vfs.Policy policy = new Vfs.Policy(root, new Vfs.Sdcard(root.resolve("shared"), true));
        assertTrue(policy.readOnlyHost(root.resolve("shared").resolve("Download").resolve("d")));
        assertFalse(policy.readOnlyHost(root.resolve("data").resolve("com.example")));
        Vfs.Policy rw = new Vfs.Policy(root, new Vfs.Sdcard(root.resolve("shared"), false));
        assertFalse(rw.readOnlyHost(root.resolve("shared").resolve("Download").resolve("d")));
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
        // Deep prefixes must never be shadowed by shorter ones.
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
        assertEquals(root.resolve("sdcard").resolve("a").resolve("b"), t("/sdcard//a//b/"));
        assertEquals(root.resolve("sdcard").resolve("x"),
                Vfs.target("/sdcard/./x", root, card()));
    }

    @Test
    void exactMountRootResolvesToItsDirectory() {
        assertEquals(root.resolve("data").resolve("com.example"), t("/data/data/com.example"));
        assertEquals(root.resolve("sdcard"), t("/storage/emulated/0"));
    }

    @Test
    void acceptOverrideRefusesBadMounts() {
        // Missing, relative, root, and root-enclosing mounts are refused
        // (warning path), the static only kept once valid.
        assertNull(Vfs.acceptOverride(""));
        assertNull(Vfs.acceptOverride(null));
        assertNull(Vfs.acceptOverride(root.resolve("not-there").toString()));
        assertNull(Vfs.acceptOverride("C:\\"));
        assertNull(Vfs.acceptOverride("/"));
        // A valid existing directory outside the root is accepted.
        Path shared = root.resolve("shared");
        try {
            Files.createDirectories(shared);
            assertEquals(shared.toRealPath().normalize(), Vfs.acceptOverride(shared.toString()));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
