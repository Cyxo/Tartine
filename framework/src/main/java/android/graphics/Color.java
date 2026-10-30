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
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->HSVToColor([F)I");
    }

    public static int HSVToColor(int arg0, float[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->HSVToColor(I[F)I");
    }

    public static void RGBToHSV(int arg0, int arg1, int arg2, float[] arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->RGBToHSV(III[F)V");
    }

    public float alpha() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->alpha()F");
    }

    public static int alpha(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->alpha(I)I");
    }

    public static float alpha(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->alpha(J)F");
    }

    public static int argb(float arg0, float arg1, float arg2, float arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->argb(FFFF)I");
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
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->blue(I)I");
    }

    public static float blue(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->blue(J)F");
    }

    public static ColorSpace colorSpace(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->colorSpace(J)Landroid/graphics/ColorSpace;");
    }

    public static void colorToHSV(int arg0, float[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->colorToHSV(I[F)V");
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
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->green(I)I");
    }

    public static float green(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->green(J)F");
    }

    public int hashCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->hashCode()I");
    }

    public static boolean isInColorSpace(long arg0, ColorSpace arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->isInColorSpace(JLandroid/graphics/ColorSpace;)Z");
    }

    public boolean isSrgb() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->isSrgb()Z");
    }

    public static boolean isSrgb(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->isSrgb(J)Z");
    }

    public boolean isWideGamut() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->isWideGamut()Z");
    }

    public static boolean isWideGamut(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->isWideGamut(J)Z");
    }

    public float luminance() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->luminance()F");
    }

    public static float luminance(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->luminance(I)F");
    }

    public static float luminance(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->luminance(J)F");
    }

    public long pack() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->pack()J");
    }

    public static long pack(float arg0, float arg1, float arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->pack(FFF)J");
    }

    public static long pack(float arg0, float arg1, float arg2, float arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->pack(FFFF)J");
    }

    public static long pack(float arg0, float arg1, float arg2, float arg3, ColorSpace arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->pack(FFFFLandroid/graphics/ColorSpace;)J");
    }

    public static long pack(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->pack(I)J");
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
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->red(I)I");
    }

    public static float red(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->red(J)F");
    }

    public static int rgb(float arg0, float arg1, float arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->rgb(FFF)I");
    }

    public static int rgb(int arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->rgb(III)I");
    }

    public int toArgb() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->toArgb()I");
    }

    public static int toArgb(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Color;->toArgb(J)I");
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
    }
