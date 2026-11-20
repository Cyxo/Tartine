package android.content.res;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.util.Xml;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

/**
 * Tartine: a color that varies by view state, mirroring AOSP
 * {@code android.content.res.ColorStateList}. The desktop shell resolves
 * {@code <selector>} resources through {@link #createFromXml}; callers that
 * only need one color (the common case for enabled views) get the selector's
 * default item.
 */
public class ColorStateList implements Parcelable {

    /** {@code android.R.attr.color}. */
    private static final int ATTR_COLOR = 0x010100d0;
    /** {@code android.R.attr.alpha}. */
    private static final int ATTR_ALPHA = 0x0101031f;

    public static Parcelable.Creator CREATOR;

    private final int[][] mStateSpecs;
    private final int[] mColors;
    private final int mDefaultColor;

    /**
     * AOSP ColorStateList(int[][], int[]): one color per state spec; the
     * default is the last entry's color.
     */
    public ColorStateList(int[][] states, int[] colors) {
        mStateSpecs = states;
        mColors = colors;
        mDefaultColor = colors != null && colors.length > 0
                ? colors[colors.length - 1] : 0;
    }

    public static ColorStateList createFromXml(Resources resources, XmlPullParser parser) {
        return createFromXml(resources, parser, null);
    }

    /**
     * AOSP {@code ColorStateList.createFromXml}: reads a {@code <selector>}
     * of {@code <item android:color=... android:alpha=...>} entries. State
     * matching is not implemented yet (the shell keeps the default item, the
     * last one), but the color and alpha resources resolve through the
     * installed app's table instead of leaking a string-pool index as a raw
     * ARGB value.
     */
    public static ColorStateList createFromXml(Resources resources, XmlPullParser parser,
            Resources.Theme theme) {
        int event = parser.getEventType();
        while (event != XmlPullParser.START_TAG
                && event != XmlPullParser.END_DOCUMENT) {
            event = parser.next();
        }
        if (event == XmlPullParser.END_DOCUMENT) {
            return valueOf(0);
        }
        int outerDepth = parser.getDepth();
        List<int[]> states = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();
        while ((event = parser.next()) != XmlPullParser.END_DOCUMENT) {
            if (event == XmlPullParser.END_TAG
                    && parser.getDepth() <= outerDepth) {
                break;
            }
            if (event != XmlPullParser.START_TAG
                    || !"item".equals(parser.getName())) {
                continue;
            }
            int color = resolveColor(resources, parser, theme);
            float alpha = resolveAlpha(resources, parser);
            if (alpha != 1.0f) {
                int channel = Math.max(0, Math.min(255,
                        Math.round(alpha * 255.0f)));
                color = (color & 0x00FFFFFF) | (channel << 24);
            }
            states.add(new int[0]);
            colors.add(color);
        }
        if (colors.isEmpty()) {
            return valueOf(0);
        }
        int[] resolved = new int[colors.size()];
        for (int i = 0; i < resolved.length; i++) {
            resolved[i] = colors.get(i);
        }
        return new ColorStateList(states.toArray(new int[0][]), resolved);
    }

    /** Reads an item's {@code android:color}, following references. */
    private static int resolveColor(Resources resources, XmlPullParser parser,
            Resources.Theme theme) {
        AttributeSet attrs = Xml.asAttributeSet(parser);
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            if (!"color".equals(attrs.getAttributeName(i))) {
                continue;
            }
            int resId = attrs.getAttributeResourceValue(i, 0);
            if (resId != 0) {
                return resources.getColor(resId, theme);
            }
            String text = attrs.getAttributeValue(i);
            if (text != null && text.startsWith("#")) {
                return Resources.parseColorText(text);
            }
        }
        return 0;
    }

    /** Reads an item's {@code android:alpha} (a float or dimen reference). */
    private static float resolveAlpha(Resources resources, XmlPullParser parser) {
        AttributeSet attrs = Xml.asAttributeSet(parser);
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            if (!"alpha".equals(attrs.getAttributeName(i))) {
                continue;
            }
            int resId = attrs.getAttributeResourceValue(i, 0);
            if (resId != 0) {
                return resolveFloat(resources, resId, 1.0f);
            }
            String text = attrs.getAttributeValue(i);
            if (text != null) {
                try {
                    return Float.parseFloat(text);
                } catch (NumberFormatException ignored) {
                    return 1.0f;
                }
            }
        }
        return 1.0f;
    }

    /** Resolves a float-valued resource (type float, int or dimension). */
    private static float resolveFloat(Resources resources, int resId,
            float defaultValue) {
        TypedValue value = new TypedValue();
        try {
            resources.getValue(resId, value, true);
        } catch (RuntimeException ignored) {
            return defaultValue;
        }
        if (value.type == TypedValue.TYPE_FLOAT) {
            return Float.intBitsToFloat(value.data);
        }
        if (value.type == TypedValue.TYPE_DIMENSION
                || value.type == TypedValue.TYPE_FRACTION) {
            return TypedValue.complexToFloat(value.data);
        }
        if (value.type >= TypedValue.TYPE_FIRST_INT
                && value.type <= TypedValue.TYPE_LAST_INT) {
            return value.data;
        }
        return defaultValue;
    }

    public int describeContents() {
        return 0;
    }

    public int getChangingConfigurations() {
        return 0;
    }

    public int getColorForState(int[] stateSet, int defaultColor) {
        if (mColors == null || mColors.length == 0) {
            return defaultColor;
        }
        return mDefaultColor;
    }

    public int getDefaultColor() {
        return mDefaultColor;
    }

    public boolean isOpaque() {
        return (mDefaultColor >>> 24) == 0xFF;
    }

    public boolean isStateful() {
        return mStateSpecs != null && mStateSpecs.length > 1;
    }

    public String toString() {
        return "ColorStateList{color=0x" + Integer.toHexString(mDefaultColor) + "}";
    }

    /** AOSP valueOf(int): a single-color, non-stateful list. */
    public static ColorStateList valueOf(int color) {
        return new ColorStateList(new int[][] {new int[0]}, new int[] {color});
    }

    public ColorStateList withAlpha(int alpha) {
        int color = (mDefaultColor & 0x00FFFFFF) | (alpha << 24);
        return valueOf(color);
    }

    public ColorStateList withLStar(float lStar) {
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mDefaultColor);
    }

    /** Scratch mirror constructor (SkeletonGen). */
    public ColorStateList() {
        this(new int[][] {new int[0]}, new int[] {0});
    }
}
