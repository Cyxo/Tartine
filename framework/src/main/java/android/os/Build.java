package android.os;

/**
 * Ported subset of {@code android.os.Build}: the identity and version fields
 * Tartine models. Tartine targets API 36 (Android 16), so
 * {@link VERSION#SDK_INT} is 36 and an app compiled against API 36 sees the
 * matching level here.
 *
 * <p>Only the fields the runtime or a fixture reads are present; the long
 * tail of {@code VERSION_CODES} and the per-ABI fields land when a wall
 * needs them.
 */
public class Build {

    public static final String BRAND = "Tartine";
    public static final String DEVICE = "tartine";
    public static final String MANUFACTURER = "Tartine";
    public static final String MODEL = "Tartine";
    public static final String PRODUCT = "tartine";

    private Build() {
    }

    /** AOSP {@code Build.VERSION}: the platform API level. */
    public static class VERSION {
        /** API level Tartine implements. */
        public static final int SDK_INT = 36;
        /** User-visible release version (Android 16 == API 36). */
        public static final String RELEASE = "16";
        public static final String CODENAME = "REL";
        public static final String INCREMENTAL = "tartine";
        public static final String SECURITY_PATCH = "2025-01-01";

        private VERSION() {
        }
    }

    /** AOSP {@code Build.VERSION_CODES}: the subset apps commonly compare. */
    public static class VERSION_CODES {
        public static final int BASE = 1;
        public static final int LOLLIPOP = 21;
        public static final int M = 23;
        public static final int N = 24;
        public static final int O = 26;
        public static final int P = 28;
        public static final int Q = 29;
        public static final int R = 30;
        public static final int S = 31;
        public static final int TIRAMISU = 33;
        public static final int UPSIDE_DOWN_CAKE = 34;
        public static final int VANILLA_ICE_CREAM = 35;
        public static final int BAKLAVA = 36;

        private VERSION_CODES() {
        }
    }
}