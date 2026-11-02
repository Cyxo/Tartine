package tartine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Locale;

/**
 * Test-only helper for planting a symlink (or, on Windows without the
 * symlink privilege, a directory junction) inside a mount. Tests that need
 * one skip when neither is available.
 */
public final class TestLinks {
    private TestLinks() {
    }

    /** Creates {@code link} pointing at {@code target}; false when unsupported. */
    public static boolean create(Path link, Path target) {
        try {
            Files.createSymbolicLink(link, target);
            return Files.exists(link, LinkOption.NOFOLLOW_LINKS);
        } catch (IOException | UnsupportedOperationException e) {
            return junction(link, target);
        }
    }

    /** Windows directory-junction fallback (no admin/developer mode needed). */
    private static boolean junction(Path link, Path target) {
        if (!Files.isDirectory(target) || !System.getProperty("os.name", "")
                .toLowerCase(Locale.ROOT).contains("win")) {
            return false;
        }
        try {
            Process process = new ProcessBuilder("cmd", "/c", "mklink", "/J",
                    link.toString(), target.toString())
                    .redirectErrorStream(true).start();
            return process.waitFor() == 0
                    && Files.exists(link, LinkOption.NOFOLLOW_LINKS);
        } catch (IOException e) {
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
