package android.graphics;

/**
 * Tartine: minimal skeleton carrying the pixel-format constants exactly as
 * the device does (AOSP graphics/java/android/graphics/PixelFormat.java,
 * lines 39-67 — the note there that values must match the HIDL/HAL
 * definitions still applies to callers' expectations). No methods ported;
 * grows when a wall needs one.
 */
public class PixelFormat {

    /** AOSP PixelFormat line 39. */
    public static final int UNKNOWN = 0;

    /** AOSP PixelFormat line 42: system chooses a translucency-capable format. */
    public static final int TRANSLUCENT = -3;

    /** AOSP PixelFormat line 48: system chooses a transparency-capable format. */
    public static final int TRANSPARENT = -2;

    /** AOSP PixelFormat line 51: system chooses an opaque format. */
    public static final int OPAQUE = -1;

    /** AOSP PixelFormat lines 53-56. */
    public static final int RGBA_8888 = 1;
    public static final int RGBX_8888 = 2;
    public static final int RGB_888 = 3;
    public static final int RGB_565 = 4;
}
