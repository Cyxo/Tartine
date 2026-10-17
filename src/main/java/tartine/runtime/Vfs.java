package tartine.runtime;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Maps Android guest filesystem paths onto host paths under a per-user
 * Tartine root. This is the routing seam the future java.io/java.nio.file
 * guest bridge goes through (audit 2026-10: "virtual filesystem" High
 * finding) — guest-visible paths must never touch host disk unmediated.
 *
 * {@link #target} returns {@code null} for anything it would not map.
 * Callers must treat {@code null} as a sandbox denial (path does not
 * exist for the guest), never fall back to the host working directory.
 *
 * Root location (app data, not config: installed APKs, translated code
 * caches, per-package data dirs):
 * <ul>
 *   <li>Windows: {@code %LOCALAPPDATA%\Tartine}</li>
 *   <li>macOS: {@code ~/Library/Application Support/Tartine}</li>
 *   <li>Linux XDG: {@code $XDG_DATA_HOME/tartine}, default
 *       {@code ~/.local/share/tartine}</li>
 *   <li>Overrides: {@code TARTINE_HOME} env, then {@code tartine.home}
 *       system property.</li>
 * </ul>
 *
 * Guest mounts (longest prefix wins; {@code /data/user/0} is the
 * multi-user spelling of {@code /data/data}):
 * <ul>
 *   <li>{@code /data/data/<pkg>} and {@code /data/user/0/<pkg>},
 *       including the {@code files|cache|databases|shared_prefs}
 *       subdirs — {@code <root>/data/<pkg>}</li>
 *   <li>{@code /data/app/<pkg>} — {@code <root>/apps/<pkg>} (where the
 *       installed base.apk lives; guest-visible sourceDir)</li>
 *   <li>{@code /sdcard} and {@code /storage/emulated/0} — the host
 *       user's real home directory (mounted read-write, Wine-style)</li>
 *   <li>{@code /data/local/tmp} — {@code <root>/tmp}</li>
 * </ul>
 *
 * Device-only trees ({@code /system}, {@code /proc}, {@code /dev} ...)
 * stay unmapped: reading them is not yet used by any guest, and the
 * bridge must fail closed when one asks.
 */
public final class Vfs {
    private Vfs() {
    }

    /** One guest prefix mapped onto a host directory. */
    record Mount(String guestPrefix, Path hostRoot) {
    }

    /** Root of the on-disk Tartine state (override, then per-OS default). */
    public static Path root() {
        String home = System.getenv("TARTINE_HOME");
        if (home != null && !home.isBlank()) {
            return Paths.get(home);
        }
        String prop = System.getProperty("tartine.home");
        if (prop != null && !prop.isBlank()) {
            return Paths.get(prop);
        }
        return defaultRoot();
    }

    static Path defaultRoot() {
        String os = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        if (os.contains("win")) {
            return Paths.get(System.getenv("LOCALAPPDATA"), "Tartine");
        }
        if (os.contains("mac")) {
            return Paths.get(System.getProperty("user.home"), "Library",
                    "Application Support", "Tartine");
        }
        String xdg = System.getenv("XDG_DATA_HOME");
        if (xdg != null && !xdg.isBlank()) {
            return Paths.get(xdg, "tartine");
        }
        return Paths.get(System.getProperty("user.home"), ".local", "share", "tartine");
    }

    /** Mount table for a given root/home pair (test seam). */
    static List<Mount> mounts(Path root, Path home) {
        return List.of(
                new Mount("/data/user/0", root.resolve("data")),
                new Mount("/data/data", root.resolve("data")),
                new Mount("/data/app", root.resolve("apps")),
                new Mount("/storage/emulated/0", home),
                new Mount("/sdcard", home),
                new Mount("/data/local/tmp", root.resolve("tmp")));
    }

    /** Handed to guests for {@code getExternalStorageDirectory}-style paths. */
    static Path home() {
        return Paths.get(System.getProperty("user.home"));
    }

    /**
     * Host path for a guest absolute path under the default root and home,
     * or {@code null} when unmapped or unsafe.
     */
    public static Path target(String guestPath) {
        return target(guestPath, root(), home());
    }

    /**
     * Host path for {@code guestPath}, or {@code null} when the path does
     * not map into the sandbox. No existence checks happen here; callers
     * decide read/write behavior and create directories on demand.
     */
    static Path target(String guestPath, Path root, Path home) {
        String canon = guestPath.replace('\\', '/');
        if (!canon.startsWith("/")) {
            return null; // guest-relative paths resolve against mapped dirs
        }
        List<Mount> table = mounts(root, home);
        Mount best = null;
        for (Mount m : table) {
            if (best != null && m.guestPrefix.length() <= best.guestPrefix.length()) {
                continue;
            }
            if (canon.equals(m.guestPrefix) || canon.startsWith(m.guestPrefix + "/")) {
                best = m;
            }
        }
        if (best == null) {
            return null;
        }
        String rel = canon.substring(best.guestPrefix.length());
        Path result = best.hostRoot();
        for (String seg : rel.split("/")) {
            if (seg.isEmpty() || seg.equals(".")) {
                continue;
            }
            if (!legalSegment(seg)) {
                return null;
            }
            result = result.resolve(seg);
        }
        return result;
    }

    /**
     * Reject traversal ({@code ..}), drive letters and Windows reserved
     * device names so a "legal" guest name never escapes the mapped root
     * or breaks NT path syntax. {@code .} was already skipped.
     */
    private static boolean legalSegment(String seg) {
        if (seg.contains(":")) {
            return false;
        }
        if (seg.contains("/")) {
            return false;
        }
        String lower = seg.toLowerCase(Locale.ROOT);
        int dot = lower.indexOf('.');
        String stem = dot < 0 ? lower : lower.substring(0, dot);
        if (stem.equals("..") || stem.isEmpty()) {
            return false;
        }
        return !Arrays.asList("con", "prn", "aux", "nul", "com1", "com2", "com3",
                "com4", "com5", "com6", "com7", "com8", "com9", "lpt1", "lpt2",
                "lpt3", "lpt4", "lpt5", "lpt6", "lpt7", "lpt8", "lpt9")
                .contains(stem);
    }
}
