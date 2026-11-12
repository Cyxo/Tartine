package tartine.fw;

/**
 * Installed-app resource seam holder. The host installs an
 * {@link AppResourceHost} before guest code launches (see {@code tartine.Main});
 * the framework's resource layer reads {@link #host()}, which is
 * {@code null} for framework-only runs and tests (behaviour then falls back
 * to the system baseline, e.g. {@link SystemColors}).
 */
public final class AppResources {

    private static volatile AppResourceHost host;

    private AppResources() {
    }

    /** Installs the host resource table, or {@code null} to clear it. */
    public static void install(AppResourceHost newHost) {
        host = newHost;
    }

    /** The installed host table, or {@code null} when none is installed. */
    public static AppResourceHost host() {
        return host;
    }
}
