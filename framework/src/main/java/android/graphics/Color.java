package android.graphics;

import java.util.Locale;

    public class Color {

    public static final int BLACK = -16777216;
    public static final int BLUE = -16776961;
    public static final int CYAN = -16711681;
    public static final int DKGRAY = -12303292;
    public static final int GRAY = -7829368;
    public static final int GREEN = -16711936;
    public static final int LTGRAY = -3355444;
    public static final int MAGENTA = -65281;
    public static final int RED = -65536;
    public static final int TRANSPARENT = 0;
    public static final int WHITE = -1;
    public static final int YELLOW = -256;

    public Color() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;-><init>()V");
    }

    public static int HSVToColor(float[] arg0) {
        return HSVToColor(0xFF, arg0);
    }

    public static int HSVToColor(int arg0, float[] arg1) {
        if (arg1 == null || arg1.length < 3) {
            throw new RuntimeException("3 components required for hsv");
        }
        // AOSP delegates to Skia's SkHSVToColor. Java2D's HSB is the same
        // model (hue in [0,360), saturation and value in [0,1]).
        int rgb = java.awt.Color.HSBtoRGB(arg1[0] / 360.0f, arg1[1], arg1[2]);
        return (arg0 << 24) | (rgb & 0x00FFFFFF);
    }

    public static void RGBToHSV(int arg0, int arg1, int arg2, float[] arg3) {
        if (arg3 == null || arg3.length < 3) {
            throw new RuntimeException("3 components required for hsv");
        }
        float[] hsb = java.awt.Color.RGBtoHSB(arg0, arg1, arg2, null);
        arg3[0] = hsb[0] * 360.0f;
        arg3[1] = hsb[1];
        arg3[2] = hsb[2];
    }

    public float alpha() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->alpha()F");
    }

    public static int alpha(int arg0) {
        // AOSP Color.alpha(int): the high byte.
        return arg0 >>> 24;
    }

    public static float alpha(long arg0) {
        // AOSP Color.alpha(long): a color long in sRGB carries 8-bit
        // channels (Color.java:782); other color spaces use a 10-bit
        // float alpha. Both branches are pure bit math, no color space
        // lookup needed.
        if ((arg0 & 0x3fL) == 0L) {
            return ((arg0 >> 56) & 0xFF) / 255.0f;
        }
        return ((arg0 >> 6) & 0x3FF) / 1023.0f;
    }

    public static int argb(float arg0, float arg1, float arg2, float arg3) {
        // AOSP Color.argb(float,float,float,float): 8-bit components.
        return (to8bit(arg0) << 24) | (to8bit(arg1) << 16)
                | (to8bit(arg2) << 8) | to8bit(arg3);
    }

    /** AOSP graphics/java/android/graphics/Color.java line 1326
     *  {@code (alpha << 24) | (red << 16) | (green << 8) | blue}. */
    public static int argb(int arg0, int arg1, int arg2, int arg3) {
        return (arg0 << 24) | (arg1 << 16) | (arg2 << 8) | arg3;
    }

    public float blue() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->blue()F");
    }

    public static int blue(int arg0) {
        // AOSP Color.blue(int): low byte.
        return arg0 & 0xFF;
    }

    public static float blue(long arg0) {
        // AOSP Color.blue(long): sRGB channel byte, else half float.
        if ((arg0 & 0x3fL) == 0L) {
            return ((arg0 >> 32) & 0xFF) / 255.0f;
        }
        return Float.float16ToFloat((short) ((arg0 >> 16) & 0xFFFF));
    }

    public static ColorSpace colorSpace(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->colorSpace(J)Landroid/graphics/ColorSpace;");
    }

    public static void colorToHSV(int arg0, float[] arg1) {
        RGBToHSV((arg0 >> 16) & 0xFF, (arg0 >> 8) & 0xFF, arg0 & 0xFF, arg1);
    }

    public Color convert(ColorSpace arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->convert(Landroid/graphics/ColorSpace;)Landroid/graphics/Color;");
    }

    public static long convert(float arg0, float arg1, float arg2, float arg3, ColorSpace arg4, ColorSpace arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->convert(FFFFLandroid/graphics/ColorSpace;Landroid/graphics/ColorSpace;)J");
    }

    public static long convert(float arg0, float arg1, float arg2, float arg3, ColorSpace.Connector arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->convert(FFFFLandroid/graphics/ColorSpace$Connector;)J");
    }

    public static long convert(int arg0, ColorSpace arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->convert(ILandroid/graphics/ColorSpace;)J");
    }

    public static long convert(long arg0, ColorSpace arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->convert(JLandroid/graphics/ColorSpace;)J");
    }

    public static long convert(long arg0, ColorSpace.Connector arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->convert(JLandroid/graphics/ColorSpace$Connector;)J");
    }

    public boolean equals(Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->equals(Ljava/lang/Object;)Z");
    }

    public ColorSpace getColorSpace() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->getColorSpace()Landroid/graphics/ColorSpace;");
    }

    public float getComponent(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->getComponent(I)F");
    }

    public int getComponentCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->getComponentCount()I");
    }

    public float[] getComponents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->getComponents()[F");
    }

    public float[] getComponents(float[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->getComponents([F)[F");
    }

    public ColorSpace.Model getModel() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->getModel()Landroid/graphics/ColorSpace$Model;");
    }

    public float green() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->green()F");
    }

    public static int green(int arg0) {
        // AOSP Color.green(int): second byte.
        return (arg0 >> 8) & 0xFF;
    }

    public static float green(long arg0) {
        // AOSP Color.green(long): sRGB channel byte, else half float.
        if ((arg0 & 0x3fL) == 0L) {
            return ((arg0 >> 40) & 0xFF) / 255.0f;
        }
        return Float.float16ToFloat((short) ((arg0 >> 32) & 0xFFFF));
    }

    public int hashCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->hashCode()I");
    }

    public static boolean isInColorSpace(long arg0, ColorSpace arg1) {
        // AOSP Color.java:828: the low 6 bits carry the color space id.
        return (int) (arg0 & 0x3fL) == arg1.getId();
    }

    public boolean isSrgb() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->isSrgb()Z");
    }

    public static boolean isSrgb(long arg0) {
        // sRGB is id 0, so an sRGB long leaves the low 6 bits clear
        // (Color.java:842).
        return (arg0 & 0x3fL) == 0L;
    }

    public boolean isWideGamut() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->isWideGamut()Z");
    }

    public static boolean isWideGamut(long arg0) {
        // AOSP asks the encoded color space; only sRGB longs exist until
        // the ColorSpace port.
        if ((arg0 & 0x3fL) == 0L) {
            return false;
        }
        throw new UnsupportedOperationException(
                "Tartine: not implemented: wide-gamut color space lookup");
    }

    public float luminance() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->luminance()F");
    }

    public static float luminance(int arg0) {
        // AOSP Color.luminance(int): relative luminance of the sRGB color
        // (WCAG coefficients over linear-light components).
        return (float) (0.2126 * linearize(red(arg0))
                + 0.7152 * linearize(green(arg0))
                + 0.0722 * linearize(blue(arg0)));
    }

    public static float luminance(long arg0) {
        // An sRGB long is the ARGB word in its high half; wider spaces need
        // the ColorSpace EOTF and wait on that port.
        if ((arg0 & 0x3fL) == 0L) {
            return luminance((int) (arg0 >> 32));
        }
        throw new UnsupportedOperationException(
                "Tartine: not implemented: wide-gamut luminance");
    }

    public long pack() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->pack()J");
    }

    public static long pack(float arg0, float arg1, float arg2) {
        return pack(argb(1.0f, arg0, arg1, arg2));
    }

    public static long pack(float arg0, float arg1, float arg2, float arg3) {
        // AOSP pack(FFFF) routes through the sRGB color space, which only
        // re-quantizes to 8-bit channels and shifts them up (Color.java:
        // 1044-1050).
        return pack(argb(arg3, arg0, arg1, arg2));
    }

    public static long pack(float arg0, float arg1, float arg2, float arg3, ColorSpace arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->pack(FFFFLandroid/graphics/ColorSpace;)J");
    }

    public static long pack(int arg0) {
        // AOSP Color.pack(int) (Color.java:984): the ARGB word as the high
        // 32 bits of an sRGB color long.
        return (arg0 & 0xFFFFFFFFL) << 32;
    }

    public static int parseColor(String arg0) {
        // AOSP Color.parseColor: "#RRGGBB", "#AARRGGBB", or a named color.
        if (arg0 == null || arg0.isEmpty()) {
            throw new IllegalArgumentException("Unknown color");
        }
        if (arg0.charAt(0) == '#') {
            long color = Long.parseLong(arg0.substring(1), 16);
            if (arg0.length() == 7) {
                color |= 0x00000000FF000000L;
            } else if (arg0.length() != 9) {
                throw new IllegalArgumentException("Unknown color");
            }
            return (int) color;
        }
        switch (arg0.toLowerCase(Locale.ROOT)) {
            case "black": return BLACK;
            case "darkgray": case "darkgrey": return DKGRAY;
            case "gray": case "grey": return GRAY;
            case "lightgray": case "lightgrey": return LTGRAY;
            case "white": return WHITE;
            case "red": return RED;
            case "green": return GREEN;
            case "blue": return BLUE;
            case "yellow": return YELLOW;
            case "cyan": case "aqua": return CYAN;
            case "magenta": case "fuchsia": return MAGENTA;
            case "transparent": return TRANSPARENT;
            default:
                throw new IllegalArgumentException("Unknown color");
        }
    }

    public float red() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->red()F");
    }

    public static int red(int arg0) {
        // AOSP Color.red(int): second-highest byte.
        return (arg0 >> 16) & 0xFF;
    }

    public static float red(long arg0) {
        // AOSP Color.red(long): sRGB channel byte, else half float.
        if ((arg0 & 0x3fL) == 0L) {
            return ((arg0 >> 48) & 0xFF) / 255.0f;
        }
        return Float.float16ToFloat((short) ((arg0 >> 48) & 0xFFFF));
    }

    public static int rgb(float arg0, float arg1, float arg2) {
        return 0xFF000000 | (to8bit(arg0) << 16) | (to8bit(arg1) << 8)
                | to8bit(arg2);
    }

    public static int rgb(int arg0, int arg1, int arg2) {
        return 0xFF000000 | (arg0 << 16) | (arg1 << 8) | arg2;
    }

    public int toArgb() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->toArgb()I");
    }

    public static int toArgb(long arg0) {
        // AOSP Color.toArgb(long) (Color.java:841): an sRGB long carries
        // the ARGB word outright; anything else first needs a color space
        // conversion, which waits on the ColorSpace port.
        if ((arg0 & 0x3fL) == 0L) {
            return (int) (arg0 >> 32);
        }
        throw new UnsupportedOperationException(
                "Tartine: not implemented: wide-gamut color conversion");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->toString()Ljava/lang/String;");
    }

    public static Color valueOf(float arg0, float arg1, float arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->valueOf(FFF)Landroid/graphics/Color;");
    }

    public static Color valueOf(float arg0, float arg1, float arg2, float arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->valueOf(FFFF)Landroid/graphics/Color;");
    }

    public static Color valueOf(float arg0, float arg1, float arg2, float arg3, ColorSpace arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->valueOf(FFFFLandroid/graphics/ColorSpace;)Landroid/graphics/Color;");
    }

    public static Color valueOf(float[] arg0, ColorSpace arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->valueOf([FLandroid/graphics/ColorSpace;)Landroid/graphics/Color;");
    }

    public static Color valueOf(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->valueOf(I)Landroid/graphics/Color;");
    }

    public static Color valueOf(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->valueOf(J)Landroid/graphics/Color;");
    }

    /** AOSP Color.to8bit: a [0..1] component to an 8-bit channel. */
    private static int to8bit(float value) {
        return (int) (value * 255.0f + 0.5f);
    }

    /** sRGB 8-bit channel to relative linear light (WCAG definition). */
    private static double linearize(int channel) {
        double value = channel / 255.0;
        return value <= 0.04045
                ? value / 12.92
                : Math.pow((value + 0.055) / 1.055, 2.4);
    }
    }
