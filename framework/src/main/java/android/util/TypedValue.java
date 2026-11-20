package android.util;

/**
 * Tartine: container for a dynamically typed data value, mirroring AOSP
 * {@code android.util.TypedValue} (frameworks/base core/java/android/util/
 * TypedValue.java). The dimension/fraction converters and
 * {@link #applyDimension} are ported directly; a value read from the app's
 * resource table lands here with its {@code Res_value} type and data word.
 */
public class TypedValue {

    public static final int COMPLEX_MANTISSA_MASK = 16777215;
    public static final int COMPLEX_MANTISSA_SHIFT = 8;
    public static final int COMPLEX_RADIX_0p23 = 3;
    public static final int COMPLEX_RADIX_16p7 = 1;
    public static final int COMPLEX_RADIX_23p0 = 0;
    public static final int COMPLEX_RADIX_8p15 = 2;
    public static final int COMPLEX_RADIX_MASK = 3;
    public static final int COMPLEX_RADIX_SHIFT = 4;
    public static final int COMPLEX_UNIT_DIP = 1;
    public static final int COMPLEX_UNIT_FRACTION = 0;
    public static final int COMPLEX_UNIT_FRACTION_PARENT = 1;
    public static final int COMPLEX_UNIT_IN = 4;
    public static final int COMPLEX_UNIT_MASK = 15;
    public static final int COMPLEX_UNIT_MM = 5;
    public static final int COMPLEX_UNIT_PT = 3;
    public static final int COMPLEX_UNIT_PX = 0;
    public static final int COMPLEX_UNIT_SHIFT = 0;
    public static final int COMPLEX_UNIT_SP = 2;
    public static final int DATA_NULL_EMPTY = 1;
    public static final int DATA_NULL_UNDEFINED = 0;
    public static final int DENSITY_DEFAULT = 0;
    public static final int DENSITY_NONE = 65535;
    public static final int TYPE_ATTRIBUTE = 2;
    public static final int TYPE_DIMENSION = 5;
    public static final int TYPE_FIRST_COLOR_INT = 28;
    public static final int TYPE_FIRST_INT = 16;
    public static final int TYPE_FLOAT = 4;
    public static final int TYPE_FRACTION = 6;
    public static final int TYPE_INT_BOOLEAN = 18;
    public static final int TYPE_INT_COLOR_ARGB4 = 30;
    public static final int TYPE_INT_COLOR_ARGB8 = 28;
    public static final int TYPE_INT_COLOR_RGB4 = 31;
    public static final int TYPE_INT_COLOR_RGB8 = 29;
    public static final int TYPE_INT_DEC = 16;
    public static final int TYPE_INT_HEX = 17;
    public static final int TYPE_LAST_COLOR_INT = 31;
    public static final int TYPE_LAST_INT = 31;
    public static final int TYPE_NULL = 0;
    public static final int TYPE_REFERENCE = 1;
    public static final int TYPE_STRING = 3;
    public int assetCookie;
    public int changingConfigurations;
    public int data;
    public int density;
    public int resourceId;
    public int sourceResourceId;
    public CharSequence string;
    public int type;

    /**
     * AOSP's complex-value radix scales. {@link #complexToFloat} keeps the
     * mantissa in place ({@code complex & 0xFFFFFF00}), so the radix-0 entry
     * folds in the 8-bit mantissa shift: 23p0 (an integer) scales by
     * 1/2^8, not by 1 as a pre-shifted reading would suggest.
     */
    private static final float MANTISSA_MULT =
            1.0f / (1 << COMPLEX_MANTISSA_SHIFT);
    private static final float[] RADIX_MULTS = {
        1.0f * MANTISSA_MULT,
        1.0f / (1 << 7) * MANTISSA_MULT,
        1.0f / (1 << 15) * MANTISSA_MULT,
        1.0f / (1 << 23) * MANTISSA_MULT
    };

    private static final String[] DIMENSION_UNIT_STRS = {
        "px", "dip", "sp", "pt", "in", "mm"
    };

    private static final String[] FRACTION_UNIT_STRS = {
        "%", "%p"
    };

    public TypedValue() {
    }

    /**
     * AOSP TypedValue.applyDimension (lines 1218-1242): scales a complex value
     * by the unit's device multiplier.
     */
    public static float applyDimension(int unit, float value, DisplayMetrics metrics) {
        switch (unit) {
            case COMPLEX_UNIT_PX:
                return value;
            case COMPLEX_UNIT_DIP:
                return value * metrics.density;
            case COMPLEX_UNIT_SP:
                return value * metrics.scaledDensity;
            case COMPLEX_UNIT_PT:
                return value * metrics.xdpi * (1.0f / 72);
            case COMPLEX_UNIT_IN:
                return value * metrics.xdpi;
            case COMPLEX_UNIT_MM:
                return value * metrics.xdpi * (1.0f / 25.4f);
            default:
                return 0;
        }
    }

    public final CharSequence coerceToString() {
        return coerceToString(type, data);
    }

    /**
     * AOSP TypedValue.coerceToString(int, int) (lines 1306-1341): renders a
     * typed value the way resource tooling prints it.
     */
    public static final String coerceToString(int type, int data) {
        switch (type) {
            case TYPE_NULL:
                return null;
            case TYPE_REFERENCE:
                return "@" + data;
            case TYPE_ATTRIBUTE:
                return "?" + data;
            case TYPE_FLOAT:
                return Float.toString(Float.intBitsToFloat(data));
            case TYPE_DIMENSION:
                return Float.toString(complexToFloat(data))
                        + DIMENSION_UNIT_STRS[(data >> COMPLEX_UNIT_SHIFT)
                                & COMPLEX_UNIT_MASK];
            case TYPE_FRACTION:
                return Float.toString(complexToFloat(data) * 100)
                        + FRACTION_UNIT_STRS[(data >> COMPLEX_UNIT_SHIFT)
                                & COMPLEX_UNIT_MASK];
            case TYPE_INT_HEX:
                return "0x" + Integer.toHexString(data);
            case TYPE_INT_BOOLEAN:
                return data != 0 ? "true" : "false";
            default:
                return Integer.toString(data);
        }
    }

    public static float complexToDimension(int data, DisplayMetrics metrics) {
        return applyDimension((data >> COMPLEX_UNIT_SHIFT) & COMPLEX_UNIT_MASK,
                complexToFloat(data), metrics);
    }

    public static int complexToDimensionPixelOffset(int data, DisplayMetrics metrics) {
        return (int) applyDimension((data >> COMPLEX_UNIT_SHIFT) & COMPLEX_UNIT_MASK,
                complexToFloat(data), metrics);
    }

    public static int complexToDimensionPixelSize(int data, DisplayMetrics metrics) {
        final float value = complexToFloat(data);
        final float f = applyDimension((data >> COMPLEX_UNIT_SHIFT) & COMPLEX_UNIT_MASK,
                value, metrics);
        final int res = (int) ((f >= 0) ? (f + 0.5f) : (f - 0.5f));
        if (res != 0) {
            return res;
        }
        if (value == 0) {
            return 0;
        }
        return value > 0 ? 1 : -1;
    }

    public static float complexToFloat(int complex) {
        return (complex & (COMPLEX_MANTISSA_MASK << COMPLEX_MANTISSA_SHIFT))
                * RADIX_MULTS[(complex >> COMPLEX_RADIX_SHIFT) & COMPLEX_RADIX_MASK];
    }

    public static float complexToFraction(int data, float base, float pbase) {
        switch ((data >> COMPLEX_UNIT_SHIFT) & COMPLEX_UNIT_MASK) {
            case COMPLEX_UNIT_FRACTION:
                return complexToFloat(data) * base;
            case COMPLEX_UNIT_FRACTION_PARENT:
                return complexToFloat(data) * pbase;
            default:
                return 0;
        }
    }

    public static float convertDimensionToPixels(int unit, float value,
            DisplayMetrics metrics) {
        return applyDimension(unit, value, metrics);
    }

    public static float convertPixelsToDimension(int unit, float pixelValue,
            DisplayMetrics metrics) {
        return deriveDimension(unit, pixelValue, metrics);
    }

    public static float deriveDimension(int unit, float pixelValue, DisplayMetrics metrics) {
        switch (unit) {
            case COMPLEX_UNIT_PX:
                return pixelValue;
            case COMPLEX_UNIT_DIP:
                return pixelValue / metrics.density;
            case COMPLEX_UNIT_SP:
                return pixelValue / metrics.scaledDensity;
            case COMPLEX_UNIT_PT:
                return pixelValue / (metrics.xdpi * (1.0f / 72));
            case COMPLEX_UNIT_IN:
                return pixelValue / metrics.xdpi;
            case COMPLEX_UNIT_MM:
                return pixelValue / (metrics.xdpi * (1.0f / 25.4f));
            default:
                return 0.0f;
        }
    }

    public int getComplexUnit() {
        return (data >> COMPLEX_UNIT_SHIFT) & COMPLEX_UNIT_MASK;
    }

    public float getDimension(DisplayMetrics metrics) {
        return complexToDimension(data, metrics);
    }

    public final float getFloat() {
        return Float.intBitsToFloat(data);
    }

    public float getFraction(float base, float pbase) {
        return complexToFraction(data, base, pbase);
    }

    public boolean isColorType() {
        return type >= TYPE_FIRST_COLOR_INT && type <= TYPE_LAST_COLOR_INT;
    }

    public void setTo(TypedValue other) {
        type = other.type;
        data = other.data;
        string = other.string;
        resourceId = other.resourceId;
        density = other.density;
        assetCookie = other.assetCookie;
        changingConfigurations = other.changingConfigurations;
        sourceResourceId = other.sourceResourceId;
    }

    public String toString() {
        return "TypedValue{t=0x" + Integer.toHexString(type)
                + "/d=0x" + Integer.toHexString(data) + "}";
    }
}
