package tartine.fw;

/**
 * Host window metrics seam. The desktop host resolves the window's
 * framebuffer size and content scale after creating it, then writes them
 * here before guest code launches; {@code Resources} reads them when
 * building the guest {@code Configuration} and {@code DisplayMetrics}.
 *
 * <p>The default is the historical 480x960 px mdpi surface (density 1.0,
 * 160 dpi), so framework-only runs and tests keep their behavior. Density
 * is derived from the host content scale: a 2x window reports 320 dpi and
 * 2.0 density, leaving the dp size unchanged when the framebuffer doubles.
 *
 * <p>Resizes update the pixel viewport ({@link #setSize}); the guest's
 * existing {@code Resources} snapshot keeps its density and configuration
 * until it is rebuilt, matching how a real device delivers a configuration
 * change rather than mutating live metrics in place.
 */
public final class DisplayConfig {

    public static final int DEFAULT_WIDTH_PIXELS = 480;
    public static final int DEFAULT_HEIGHT_PIXELS = 960;
    public static final float DEFAULT_CONTENT_SCALE = 1.0f;

    private static volatile int widthPixels = DEFAULT_WIDTH_PIXELS;
    private static volatile int heightPixels = DEFAULT_HEIGHT_PIXELS;
    private static volatile int densityDpi =
            Math.round(DEFAULT_CONTENT_SCALE * 160f);

    private DisplayConfig() {
    }

    /**
     * Sets the initial window metrics. {@code contentScale} is the desktop
     * scale factor (1.0 = mdpi), rounded to the nearest density bucket.
     */
    public static void set(int widthPixels, int heightPixels,
            float contentScale) {
        if (widthPixels <= 0 || heightPixels <= 0) {
            throw new IllegalArgumentException(
                    "display size must be positive: "
                            + widthPixels + "x" + heightPixels);
        }
        if (!(contentScale > 0) || !Float.isFinite(contentScale)) {
            throw new IllegalArgumentException(
                    "content scale must be positive and finite: "
                            + contentScale);
        }
        DisplayConfig.widthPixels = widthPixels;
        DisplayConfig.heightPixels = heightPixels;
        DisplayConfig.densityDpi = Math.max(1,
                Math.round(contentScale * 160f));
    }

    /** Updates the pixel viewport after a window resize; density is kept. */
    public static void setSize(int widthPixels, int heightPixels) {
        if (widthPixels <= 0 || heightPixels <= 0) {
            throw new IllegalArgumentException(
                    "display size must be positive: "
                            + widthPixels + "x" + heightPixels);
        }
        DisplayConfig.widthPixels = widthPixels;
        DisplayConfig.heightPixels = heightPixels;
    }

    public static int widthPixels() {
        return widthPixels;
    }

    public static int heightPixels() {
        return heightPixels;
    }

    public static int densityDpi() {
        return densityDpi;
    }

    /** Pixels per dp, derived from the rounded {@link #densityDpi()}. */
    public static float density() {
        return densityDpi / 160f;
    }

    public static int screenWidthDp() {
        return Math.round(widthPixels / density());
    }

    public static int screenHeightDp() {
        return Math.round(heightPixels / density());
    }

    public static int smallestScreenWidthDp() {
        return Math.min(screenWidthDp(), screenHeightDp());
    }

    /** Test seam: restores the 480x960 mdpi default. */
    public static void reset() {
        widthPixels = DEFAULT_WIDTH_PIXELS;
        heightPixels = DEFAULT_HEIGHT_PIXELS;
        densityDpi = Math.round(DEFAULT_CONTENT_SCALE * 160f);
    }
}
