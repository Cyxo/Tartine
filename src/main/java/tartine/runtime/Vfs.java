package tartine.runtime;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Maps Android guest filesystem paths onto host paths under a per-user
 * Tartine root. This is the routing seam the java.io guest bridge goes
 * through (audit 2026-10: "virtual filesystem" High finding) —
 * guest-visible paths must never touch host disk unmediated.
 *
 * <p>{@linkplain #target} returns {@code null} for anything it would not
 * map; callers (the interpreter's File bridge) must treat {@code null}
 * as a sandbox denial (the path does not exist for the guest) and fail
 * closed, never fall back to the host working directory.
 *
 * <p>Root location (app data, not config: installed APKs, per-package
 * data dirs):
 * <ul>
 *   <li>Windows: {@code %LOCALAPPDATA%\Tartine}</li>
 *   <li>macOS: {@code ~/Library/Application Support/Tartine}</li>
 *   <li>Linux XDG: {@code $XDG_DATA_HOME/tartine}, default
 *       {@code ~/.local/share/tartine}</li>
 *   <li>Overrides: {@code TARTINE_HOME} env, then {@code tartine.home}
 *       system property.</li>
 * </ul>
 *
 * <p>Guest mounts (longest prefix wins; {@code /data/user/0} is the
 * multi-user spelling of {@code /data/data}):
 * <ul>
 *   <li>{@code /data/data/<pkg>} and {@code /data/user/0/<pkg>},
 *       including the {@code files|cache|databases|shared_prefs}
 *       subdirs — {@code <root>/data/<pkg>}</li>
 *   <li>{@code /data/app/<pkg>} — {@code <root>/apps/<pkg>} (where the
 *       installed base.apk lives; guest-visible sourceDir)</li>
 *   <li>{@code /sdcard} and {@code /storage/emulated/0} — the shared
 *       storage card (decided 2026-10-19, three tiers):
 *       default the private virtual card {@code <root>/sdcard}; an
 *       explicit opt-in mount (config {@code sdcard.mount} or env
 *       {@code TARTINE_SDCARD}) maps a user-chosen host folder, which is
 *       <em>read-only by default</em> and must be an existing directory
 *       outside the Tartine root, never a junction/symlink; the
 *       read-only flag clears with {@code sdcard.mount.readonly=false}.</li>
 *   <li>{@code /data/local/tmp} — {@code <root>/tmp}</li>
 * </ul>
 *
 * <p>Device-only trees ({@code /system}, {@code /proc}, {@code /dev}
 * and friends) stay unmapped: reading them is not yet used by any
 * guest, and the bridge must fail closed when one asks.
 *
 * <p>The mount is configuration state, fixed once at startup
 * ({@code HostConfig} reads the file, applies the env fallback, then
 * calls {@link #configure}); {@link #policy()} snapshots it per run so
 * mid-run restructuring of the static does not affect live interpreters.
 */
public final class Vfs {
    private Vfs() {
    }

    /** Effective shared-storage card: host root plus write policy. */
    public record Sdcard(Path hostRoot, boolean readOnly) {
    }

    /**
     * Immutable per-run view combining the Tartine root with the card
     * choice. All routing answers flow through here so the interpreter
     * (and tests) capture {@linkplain #policy() policy} values once.
     */
    public record Policy(Path root, Sdcard sdcard) {

        /** Host path for a guest absolute path, or {@code null} (denied). */
        public Path target(String guestPath) {
            return Vfs.target(guestPath, root, sdcard);
        }

        /**
         * Whether file writes under a mapped host path are denied (the
         * read-only card mount); app-private mounts never answer true.
         */
        public boolean readOnlyHost(Path hostPath) {
            if (!sdcard.readOnly()) {
                return false;
            }
            return hostPath.normalize().startsWith(sdcard.hostRoot().normalize());
        }
    }

    /** One guest prefix mapped onto a host directory. */
    record Mount(String guestPrefix, Path hostRoot) {
    }

    /** Configured card override; {@code null} = the default virtual card. */
    private static volatile Sdcard configuredCard;

    /**
     * Replaces the shared-storage card for this process. Called once at
     * startup by {@code HostConfig} (config file keys and the
     * {@code TARTINE_SDCARD} env fallback); later calls only affect
     * freshly snapshotted {@link #policy()} values, which is exactly the
     * test seam.
     */
    public static void configure(Sdcard override) {
        configuredCard = override;
    }

    /** Test seam: forget any configured card override (defaults resume). */
    public static void resetCard() {
        configuredCard = null;
    }

    /** Snapshot of the effective card choice for a fresh run. */
    public static Policy policy() {
        Path root = root();
        Sdcard card = configuredCard;
        if (card == null) {
            card = new Sdcard(root.resolve("sdcard"), false);
        }
        return new Policy(root, card);
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

    /**
     * Creates the default virtual card with the standard shared-storage
     * folders, once, on first use (idempotent; only the default card
     * seeds — an explicit mount points at an existing directory).
     */
    public static void ensureDefaultCard() {
        if (configuredCard != null) {
            return;
        }
        Path card = policy().sdcard().hostRoot();
        try {
            Files.createDirectories(card);
            for (String folder : Arrays.asList("Download", "Documents", "Pictures",
                    "DCIM", "Music", "Movies")) {
                Files.createDirectories(card.resolve(folder));
            }
        } catch (IOException e) {
            System.err.println("Tartine: sdcard seed failed: " + e);
        }
    }

    /**
     * Validates and accepts a configured sdcard override mount, or
     * explains why it was refused (stderr + null). Rules (AGENTS VFS
     * seam): must be an absolute path naming an existing directory, must
     * not be a filesystem root or a junction/symlink indirection, and
     * must not contain the Tartine root inside it.
     */
    public static Path acceptOverride(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        Path candidate = Paths.get(raw);
        if (!candidate.isAbsolute()) {
            System.err.println("Tartine: sdcard mount refused: '" + raw
                    + "' (relative path; use an absolute directory)");
            return null;
        }
        Path normalized = candidate.normalize();
        if (normalized.getNameCount() == 0) {
            System.err.println("Tartine: sdcard mount refused: '" + raw
                    + "' (never mount a filesystem root)");
            return null;
        }
        if (!Files.isDirectory(normalized)) {
            System.err.println("Tartine: sdcard mount refused: '" + raw
                    + "' (no such directory)");
            return null;
        }
        try {
            Path real = normalized.toRealPath();
            if (!real.equals(normalized)) {
                System.err.println("Tartine: sdcard mount refused: '" + raw
                        + "' (junction/symlink indirection; mount the real"
                        + " location instead)");
                return null;
            }
        } catch (IOException e) {
            System.err.println("Tartine: sdcard mount refused: '" + raw
                    + "' (" + e + ")");
            return null;
        }
        Path root = root().normalize();
        for (Path walk = normalized; walk.getParent() != null; walk = walk.getParent()) {
            if (walk.equals(root)) {
                System.err.println("Tartine: sdcard mount refused: '" + raw
                        + "' (encloses the Tartine root)");
                return null;
            }
        }
        return normalized;
    }

    /** Mount table for a given root/card pair (test seam). */
    static List<Mount> mounts(Path root, Sdcard sdcard) {
        return List.of(
                new Mount("/data/user/0", root.resolve("data")),
                new Mount("/data/data", root.resolve("data")),
                new Mount("/data/app", root.resolve("apps")),
                new Mount("/storage/emulated/0", sdcard.hostRoot()),
                new Mount("/sdcard", sdcard.hostRoot()),
                new Mount("/data/local/tmp", root.resolve("tmp")));
    }

    /**
     * Host path for a guest absolute path under the default root and the
     * configured card, or {@code null} when unmapped or unsafe.
     */
    public static Path target(String guestPath) {
        return policy().target(guestPath);
    }

    /**
     * Host path for {@code guestPath}, or {@code null} when the path does
     * not map into the sandbox. No existence checks happen here; callers
     * decide read/write behavior and create directories on demand.
     */
    static Path target(String guestPath, Path root, Sdcard sdcard) {
        String canon = guestPath.replace('\\', '/');
        if (!canon.startsWith("/")) {
            return null; // guest-relative paths resolve against mapped dirs
        }
        List<Mount> table = mounts(root, sdcard);
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
