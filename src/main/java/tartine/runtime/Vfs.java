package tartine.runtime;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * Maps Android guest filesystem paths onto host paths under a per-user
 * Tartine root. This is the routing seam the java.io guest bridge goes
 * through (audit 2026-10: "virtual filesystem" High finding) —
 * guest-visible paths must never touch host disk unmediated.
 *
 * <p>{@linkplain #target} returns {@code null} for anything it would not
 * map; guest-reachable access uses {@link Policy#secureTarget} (the same
 * mapping plus the per-access real-path check) and treats {@code null} as a
 * sandbox denial (the path does not exist for the guest), never falling
 * back to the host working directory.
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
    private static final Logger log = Logger.getLogger(Vfs.class.getName());

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

        /**
         * Lexical host path for a guest absolute path, or {@code null}
         * (denied). Guest-reachable access should use
         * {@link #secureTarget} so a symlink cannot escape the mount.
         */
        public Path target(String guestPath) {
            return Vfs.target(guestPath, root, sdcard);
        }

        /**
         * Host path for a guest absolute path with the real-path check:
         * symlinks and junctions inside a mount must resolve inside the
         * mount's real root, so a link cannot escape the sandbox.
         * {@code write} additionally covers a dangling link whose target
         * does not exist yet. Answers {@code null} on any escape.
         */
        public Path secureTarget(String guestPath, boolean write) {
            return Vfs.secureTarget(guestPath, root, sdcard, write);
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

        /**
         * Guest spelling of a host path, or {@code null} when the host path
         * is not inside any mount. Reverse mapping prefers the most specific
         * host root and, on a tie, the shortest guest prefix, so app data
         * answers {@code /data/data/...} rather than {@code /data/user/0/...}
         * and the card answers {@code /sdcard} rather than
         * {@code /storage/emulated/0}.
         */
        public String guestPath(Path hostPath) {
            return Vfs.guestPath(hostPath, root, sdcard);
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
            log.warning("Tartine: sdcard seed failed: " + e);
        }
    }

    /**
     * Validates and accepts a configured sdcard override mount, or
     * explains why it was refused (stderr + null). Rules (ADR-0005): must
     * be an absolute path naming an existing directory, must
     * not be a filesystem root or a junction/symlink indirection, and
     * must not contain the Tartine root inside it.
     */
    public static Path acceptOverride(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        Path candidate = Paths.get(raw);
        if (!candidate.isAbsolute()) {
            log.warning("Tartine: sdcard mount refused: '" + raw
                    + "' (relative path; use an absolute directory)");
            return null;
        }
        Path normalized = candidate.normalize();
        if (normalized.getNameCount() == 0) {
            log.warning("Tartine: sdcard mount refused: '" + raw
                    + "' (never mount a filesystem root)");
            return null;
        }
        if (!Files.isDirectory(normalized)) {
            log.warning("Tartine: sdcard mount refused: '" + raw
                    + "' (no such directory)");
            return null;
        }
        try {
            Path real = normalized.toRealPath();
            if (!real.equals(normalized)) {
                log.warning("Tartine: sdcard mount refused: '" + raw
                        + "' (junction/symlink indirection; mount the real"
                        + " location instead)");
                return null;
            }
        } catch (IOException e) {
            log.warning("Tartine: sdcard mount refused: '" + raw
                    + "' (" + e + ")");
            return null;
        }
        Path root = root().normalize();
        Path walk = normalized;
        while (walk != null) {
            if (walk.equals(root)) {
                log.warning("Tartine: sdcard mount refused: '" + raw
                        + "' (encloses the Tartine root)");
                return null;
            }
            walk = walk.getParent();
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
     * Lexical host path for {@code guestPath}, or {@code null} when the path
     * does not map into the sandbox. No existence or real-path checks happen
     * here; guest-reachable access must use {@link #secureTarget}.
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
     * {@link #target} plus the per-access real-path check: every existing
     * component (including the final one) must resolve inside the mount's
     * real root, so a symlink or junction planted in a mounted folder cannot
     * point the guest outside it. {@code write} additionally resolves a
     * dangling link before the host creates its target. Answers {@code null}
     * when the path escapes or cannot be resolved.
     */
    static Path secureTarget(String guestPath, Path root, Sdcard sdcard,
            boolean write) {
        Path host = target(guestPath, root, sdcard);
        if (host == null || !resolvesInsideMount(host, root, sdcard, write)) {
            return null;
        }
        return host;
    }

    /** Whether {@code host} (mount-relative) resolves inside its mount. */
    private static boolean resolvesInsideMount(Path host, Path root,
            Sdcard sdcard, boolean write) {
        Mount mount = mountFor(host, root, sdcard);
        if (mount == null) {
            return false;
        }
        Path realRoot = realOf(mount.hostRoot());
        if (Files.isSymbolicLink(host)) {
            // exists() answers false for a dangling link, so resolve the
            // link by hand; otherwise a write through it would create its
            // target wherever the host points.
            return (write || Files.exists(host))
                    && resolvesLinkInside(host, realRoot, 0);
        }
        Path existing = host;
        while (existing != null && !Files.exists(existing)) {
            existing = existing.getParent();
        }
        return inside(existing, realRoot);
    }

    /**
     * Whether {@code existing} resolves inside {@code realRoot}. When the
     * mount root itself does not exist yet, the deepest existing ancestor is
     * necessarily above it; that is still safe because the lexical tail
     * starts with the mount root Tartine chose.
     */
    private static boolean inside(Path existing, Path realRoot) {
        if (existing == null) {
            return false;
        }
        try {
            Path realExisting = existing.toRealPath();
            return realExisting.startsWith(realRoot)
                    || realRoot.startsWith(realExisting);
        } catch (IOException | SecurityException e) {
            return false;
        }
    }

    /** Follows a symlink chain (bounded) and checks the final real path. */
    private static boolean resolvesLinkInside(Path link, Path realRoot,
            int depth) {
        if (depth > 8) {
            return false;
        }
        try {
            Path target = Files.readSymbolicLink(link);
            Path parent = link.getParent();
            if (parent == null) {
                // A link at a filesystem root has no mount parent to check.
                return false;
            }
            Path resolved = (target.isAbsolute() ? target
                    : parent.resolve(target)).normalize();
            if (Files.isSymbolicLink(resolved)) {
                return resolvesLinkInside(resolved, realRoot, depth + 1);
            }
            Path existing = resolved;
            while (existing != null && !Files.exists(existing)) {
                existing = existing.getParent();
            }
            // A link target must land inside the mount; unlike a lexical
            // path, "above the mount root" is an escape (the host would
            // create the target there on the next write).
            return strictlyInside(existing, realRoot);
        } catch (IOException | SecurityException e) {
            return false;
        }
    }

    /** Whether {@code existing} resolves strictly inside {@code realRoot}. */
    private static boolean strictlyInside(Path existing, Path realRoot) {
        if (existing == null) {
            return false;
        }
        try {
            return existing.toRealPath().startsWith(realRoot);
        } catch (IOException | SecurityException e) {
            return false;
        }
    }

    /** Mount whose host root is the longest prefix of {@code host}. */
    private static Mount mountFor(Path host, Path root, Sdcard sdcard) {
        Path normalized = host.normalize();
        Mount best = null;
        for (Mount mount : mounts(root, sdcard)) {
            Path mountRoot = mount.hostRoot().normalize();
            if (!normalized.startsWith(mountRoot)) {
                continue;
            }
            if (best == null || mountRoot.getNameCount()
                    > best.hostRoot().normalize().getNameCount()) {
                best = mount;
            }
        }
        return best;
    }

    /**
     * Real path of {@code p}, resolving the deepest existing ancestor and
     * keeping the not-yet-created tail lexical (mount roots and app dirs are
     * created lazily).
     */
    private static Path realOf(Path p) {
        Path existing = p;
        while (existing != null && !Files.exists(existing)) {
            existing = existing.getParent();
        }
        if (existing == null) {
            return p.normalize();
        }
        try {
            return existing.toRealPath().resolve(existing.relativize(p))
                    .normalize();
        } catch (IOException | SecurityException e) {
            return p.normalize();
        }
    }

    /**
     * Guest path for a host path, or {@code null} when it falls outside
     * every mount. The answer round-trips through {@link #target} so a host
     * spelling the forward mapper would reject (traversal, reserved device
     * name, wrong case on a case-sensitive filesystem) never becomes a guest
     * path.
     */
    static String guestPath(Path hostPath, Path root, Sdcard sdcard) {
        if (hostPath == null) {
            return null;
        }
        Path host = hostPath.normalize();
        if (!host.isAbsolute()) {
            return null;
        }
        Mount best = null;
        Path bestRoot = null;
        for (Mount mount : mounts(root, sdcard)) {
            Path mountRoot = mount.hostRoot().normalize();
            if (!host.startsWith(mountRoot)) {
                continue;
            }
            if (best == null
                    || mountRoot.getNameCount() > bestRoot.getNameCount()
                    || (mountRoot.getNameCount() == bestRoot.getNameCount()
                        && mount.guestPrefix().length()
                            < best.guestPrefix().length())) {
                best = mount;
                bestRoot = mountRoot;
            }
        }
        if (best == null) {
            return null;
        }
        StringBuilder guest = new StringBuilder(best.guestPrefix());
        for (Path segment : bestRoot.relativize(host)) {
            String name = segment.toString().replace('\\', '/');
            if (!name.isEmpty()) {
                guest.append('/').append(name);
            }
        }
        String result = guest.toString();
        Path roundTrip = target(result, root, sdcard);
        if (roundTrip == null || !roundTrip.normalize().equals(host)) {
            return null;
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
