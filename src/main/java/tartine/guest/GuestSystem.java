package tartine.guest;

import java.util.Set;

/**
 * Guest-facing {@code System.getProperty} facade.
 *
 * <p>The translator routes {@code System.getProperty(String)} and
 * {@code System.getProperty(String, String)} here. Only host-neutral,
 * read-only keys are answered; everything else returns the caller's default
 * (or {@code null}). This is deliberately a value allowlist, not a denial
 * list: host paths ({@code user.home}, {@code java.io.tmpdir},
 * {@code java.class.path}), identity, and arbitrary configuration keys never
 * leak into the guest.
 *
 * <p>{@code System.setProperty}, {@code clearProperty}, {@code getenv},
 * {@code getProperties}, and output/process controls stay denied at the
 * translation pass.
 */
public final class GuestSystem {
    private GuestSystem() {
    }

    /** Host properties safe to answer: runtime identity and separators. */
    private static final Set<String> ALLOWED = Set.of(
            "java.version",
            "java.vendor",
            "java.vm.name",
            "java.vm.version",
            "java.vm.vendor",
            "java.specification.version",
            "java.specification.vendor",
            "java.specification.name",
            "java.class.version",
            "os.name",
            "os.arch",
            "os.version",
            "file.separator",
            "path.separator",
            "line.separator",
            "file.encoding",
            "native.encoding",
            "sun.jnu.encoding",
            "user.language",
            "user.country",
            "user.variant",
            "user.timezone");

    public static String getProperty(String key) {
        return getProperty(key, null);
    }

    public static String getProperty(String key, String def) {
        if (key == null) {
            throw new NullPointerException("key");
        }
        if (!ALLOWED.contains(key)) {
            return def;
        }
        String value = System.getProperty(key);
        return value == null ? def : value;
    }
}
