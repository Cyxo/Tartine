package android.util;

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
    public java.lang.CharSequence string;
    public int type;

    public TypedValue() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/util/TypedValue;-><init>()V");
    }

    public static float applyDimension(int arg0, float arg1, android.util.DisplayMetrics arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/util/TypedValue;->applyDimension(IFLandroid/util/DisplayMetrics;)F");
    }

    public final java.lang.CharSequence coerceToString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/util/TypedValue;->coerceToString()Ljava/lang/CharSequence;");
    }

    public static final java.lang.String coerceToString(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/util/TypedValue;->coerceToString(II)Ljava/lang/String;");
    }

    public static float complexToDimension(int arg0, android.util.DisplayMetrics arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/util/TypedValue;->complexToDimension(ILandroid/util/DisplayMetrics;)F");
    }

    public static int complexToDimensionPixelOffset(int arg0, android.util.DisplayMetrics arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/util/TypedValue;->complexToDimensionPixelOffset(ILandroid/util/DisplayMetrics;)I");
    }

    public static int complexToDimensionPixelSize(int arg0, android.util.DisplayMetrics arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/util/TypedValue;->complexToDimensionPixelSize(ILandroid/util/DisplayMetrics;)I");
    }

    public static float complexToFloat(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/util/TypedValue;->complexToFloat(I)F");
    }

    public static float complexToFraction(int arg0, float arg1, float arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/util/TypedValue;->complexToFraction(IFF)F");
    }

    public static float convertDimensionToPixels(int arg0, float arg1, android.util.DisplayMetrics arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/util/TypedValue;->convertDimensionToPixels(IFLandroid/util/DisplayMetrics;)F");
    }

    public static float convertPixelsToDimension(int arg0, float arg1, android.util.DisplayMetrics arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/util/TypedValue;->convertPixelsToDimension(IFLandroid/util/DisplayMetrics;)F");
    }

    public static float deriveDimension(int arg0, float arg1, android.util.DisplayMetrics arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/util/TypedValue;->deriveDimension(IFLandroid/util/DisplayMetrics;)F");
    }

    public int getComplexUnit() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/util/TypedValue;->getComplexUnit()I");
    }

    public float getDimension(android.util.DisplayMetrics arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/util/TypedValue;->getDimension(Landroid/util/DisplayMetrics;)F");
    }

    public final float getFloat() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/util/TypedValue;->getFloat()F");
    }

    public float getFraction(float arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/util/TypedValue;->getFraction(FF)F");
    }

    public boolean isColorType() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/util/TypedValue;->isColorType()Z");
    }

    public void setTo(android.util.TypedValue arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/util/TypedValue;->setTo(Landroid/util/TypedValue;)V");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/util/TypedValue;->toString()Ljava/lang/String;");
    }
    }
