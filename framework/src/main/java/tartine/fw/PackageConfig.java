package tartine.fw;

/**
 * Installed-app metadata seam. The host reads the manifest before guest code
 * launches (package name, target SDK, application flags) and installs it here;
 * {@code android.content.Context#getApplicationInfo} materialises the
 * framework's {@code ApplicationInfo} from these values. Tests and
 * framework-only runs see the pre-launch defaults (empty package, API 1, no
 * flags), matching a context that was never attached to an app.
 */
public final class PackageConfig {

    private static volatile String packageName = "";
    private static volatile int targetSdkVersion = 1;
    private static volatile int applicationFlags;

    private PackageConfig() {
    }

    /**
     * Installs the launched app's manifest values. A null package name is
     * treated as empty; the target SDK is clamped to at least API 1.
     */
    public static void install(String appPackage, int appTargetSdk,
            int flags) {
        packageName = appPackage == null ? "" : appPackage;
        targetSdkVersion = Math.max(1, appTargetSdk);
        applicationFlags = flags;
    }

    /** Test seam: return to the unattached-context defaults. */
    public static void reset() {
        packageName = "";
        targetSdkVersion = 1;
        applicationFlags = 0;
    }

    public static String packageName() {
        return packageName;
    }

    public static int targetSdkVersion() {
        return targetSdkVersion;
    }

    public static int applicationFlags() {
        return applicationFlags;
    }
}
