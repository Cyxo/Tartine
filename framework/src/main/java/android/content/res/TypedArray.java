package android.content.res;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import tartine.fw.ResValue;

/**
 * Tartine: a view of styled attribute values, mirroring AOSP
 * {@code android.content.res.TypedArray}. Built by
 * {@link Resources.Theme#obtainStyledAttributes} from either an element's
 * attributes or a style/theme, one entry per requested attribute id, so
 * {@code getIndex} is positional and {@code hasValue} reports whether the
 * attribute was defined anywhere in the lookup chain.
 */
public class TypedArray implements AutoCloseable {

    private final Resources mResources;
    private final Resources.Theme mTheme;
    private final int[] mAttrIds;
    private final ResValue[] mValues;

    TypedArray(Resources resources, Resources.Theme theme, int[] attrIds,
            ResValue[] values) {
        mResources = resources;
        mTheme = theme;
        mAttrIds = attrIds;
        mValues = values;
    }

    private ResValue valueAt(int index) {
        if (index < 0 || index >= mValues.length) {
            throw new IndexOutOfBoundsException("index " + index);
        }
        return mValues[index];
    }

    public void close() {
        // values are plain host records; nothing to release
    }

    public void recycle() {
        // values are plain host records; nothing to release
    }

    public int length() {
        return mValues.length;
    }

    public int getIndex(int at) {
        return at;
    }

    public int getIndexCount() {
        return mValues.length;
    }

    public boolean hasValue(int at) {
        return valueAt(at) != null;
    }

    public boolean hasValueOrEmpty(int at) {
        return valueAt(at) != null;
    }

    public int getType(int at) {
        ResValue value = valueAt(at);
        return value == null ? TypedValue.TYPE_NULL : value.type;
    }

    public TypedValue peekValue(int at) {
        ResValue value = valueAt(at);
        return value == null ? null : toTypedValue(value);
    }

    public boolean getValue(int at, TypedValue out) {
        ResValue value = valueAt(at);
        if (value == null) {
            return false;
        }
        out.setTo(toTypedValue(value));
        return true;
    }

    private static TypedValue toTypedValue(ResValue value) {
        TypedValue out = new TypedValue();
        out.type = value.type;
        out.data = value.data;
        out.string = value.string;
        out.resourceId = value.sourceId;
        return out;
    }

    public boolean getBoolean(int at, boolean defaultValue) {
        ResValue value = valueAt(at);
        if (value == null) {
            return defaultValue;
        }
        if (value.type == TypedValue.TYPE_INT_BOOLEAN) {
            return value.data != 0;
        }
        return coerce(value, defaultValue ? "true" : "false").equals("true");
    }

    public int getInt(int at, int defaultValue) {
        ResValue value = valueAt(at);
        if (value == null) {
            return defaultValue;
        }
        if (value.type >= TypedValue.TYPE_FIRST_INT
                && value.type <= TypedValue.TYPE_LAST_INT) {
            return value.data;
        }
        try {
            return Integer.parseInt(coerce(value, ""));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public int getInteger(int at, int defaultValue) {
        return getInt(at, defaultValue);
    }

    public float getFloat(int at, float defaultValue) {
        ResValue value = valueAt(at);
        if (value == null) {
            return defaultValue;
        }
        if (value.type == TypedValue.TYPE_FLOAT) {
            return Float.intBitsToFloat(value.data);
        }
        if (value.type >= TypedValue.TYPE_FIRST_INT
                && value.type <= TypedValue.TYPE_LAST_INT) {
            return value.data;
        }
        try {
            return Float.parseFloat(coerce(value, ""));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public String getString(int at) {
        ResValue value = valueAt(at);
        if (value == null) {
            return null;
        }
        if (value.type == TypedValue.TYPE_STRING) {
            return value.string;
        }
        return TypedValue.coerceToString(value.type, value.data);
    }

    public CharSequence getText(int at) {
        return getString(at);
    }

    public CharSequence[] getTextArray(int at) {
        return null;
    }

    public String getNonResourceString(int at) {
        ResValue value = valueAt(at);
        return value != null && value.type != TypedValue.TYPE_REFERENCE
                ? getString(at) : null;
    }

    public String getPositionDescription() {
        return "TypedArray";
    }

    public int getResourceId(int at, int defaultValue) {
        ResValue value = valueAt(at);
        if (value != null && value.type == TypedValue.TYPE_REFERENCE) {
            return value.data;
        }
        return defaultValue;
    }

    public int getSourceResourceId(int at, int defaultValue) {
        return defaultValue;
    }

    public Resources getResources() {
        return mResources;
    }

    public int getChangingConfigurations() {
        return 0;
    }

    public int getColor(int at, int defaultValue) {
        ResValue value = valueAt(at);
        if (value == null) {
            return defaultValue;
        }
        if (value.type >= TypedValue.TYPE_FIRST_COLOR_INT
                && value.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            return value.data;
        }
        if (value.type == TypedValue.TYPE_REFERENCE && mResources != null) {
            return mResources.getColor(value.data, mTheme);
        }
        if (value.type == TypedValue.TYPE_NULL) {
            return defaultValue;
        }
        return value.data;
    }

    public ColorStateList getColorStateList(int at) {
        ResValue value = valueAt(at);
        if (value == null) {
            return null;
        }
        if (value.type == TypedValue.TYPE_REFERENCE && mResources != null) {
            return mResources.getColorStateList(value.data, mTheme);
        }
        return ColorStateList.valueOf(value.data);
    }

    public Drawable getDrawable(int at) {
        ResValue value = valueAt(at);
        if (value != null && value.type == TypedValue.TYPE_REFERENCE && mResources != null) {
            return mResources.getDrawable(value.data, mTheme);
        }
        return null;
    }

    public Typeface getFont(int at) {
        return null;
    }

    public float getDimension(int at, float defaultValue) {
        ResValue value = valueAt(at);
        if (value == null) {
            return defaultValue;
        }
        if (value.type == TypedValue.TYPE_DIMENSION) {
            return TypedValue.complexToDimension(value.data, metrics());
        }
        if (value.type >= TypedValue.TYPE_FIRST_INT
                && value.type <= TypedValue.TYPE_LAST_INT) {
            return value.data;
        }
        return defaultValue;
    }

    public int getDimensionPixelOffset(int at, int defaultValue) {
        ResValue value = valueAt(at);
        if (value == null) {
            return defaultValue;
        }
        if (value.type == TypedValue.TYPE_DIMENSION) {
            return TypedValue.complexToDimensionPixelOffset(value.data, metrics());
        }
        return value.type >= TypedValue.TYPE_FIRST_INT
                && value.type <= TypedValue.TYPE_LAST_INT ? value.data : defaultValue;
    }

    public int getDimensionPixelSize(int at, int defaultValue) {
        ResValue value = valueAt(at);
        if (value == null) {
            return defaultValue;
        }
        if (value.type == TypedValue.TYPE_DIMENSION) {
            return TypedValue.complexToDimensionPixelSize(value.data, metrics());
        }
        return value.type >= TypedValue.TYPE_FIRST_INT
                && value.type <= TypedValue.TYPE_LAST_INT ? value.data : defaultValue;
    }

    public int getLayoutDimension(int at, int defaultValue) {
        ResValue value = valueAt(at);
        if (value == null) {
            return defaultValue;
        }
        if (value.type == TypedValue.TYPE_DIMENSION) {
            return TypedValue.complexToDimensionPixelSize(value.data, metrics());
        }
        return value.type >= TypedValue.TYPE_FIRST_INT
                && value.type <= TypedValue.TYPE_LAST_INT ? value.data : defaultValue;
    }

    public int getLayoutDimension(int at, String name) {
        return getLayoutDimension(at, 0);
    }

    public float getFraction(int at, int base, int pbase, float defaultValue) {
        ResValue value = valueAt(at);
        if (value == null) {
            return defaultValue;
        }
        if (value.type == TypedValue.TYPE_FRACTION) {
            return TypedValue.complexToFraction(value.data, base, pbase);
        }
        return defaultValue;
    }

    private android.util.DisplayMetrics metrics() {
        return mResources == null ? new android.util.DisplayMetrics()
                : mResources.getDisplayMetrics();
    }

    private static String coerce(ResValue value, String fallback) {
        if (value.string != null) {
            return value.string;
        }
        String text = TypedValue.coerceToString(value.type, value.data);
        return text == null ? fallback : text;
    }

    public String toString() {
        return "TypedArray{attrs=" + mAttrIds.length + "}";
    }
}
