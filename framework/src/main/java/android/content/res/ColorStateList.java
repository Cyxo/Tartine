package android.content.res;

import android.os.Parcel;
import android.os.Parcelable;
import org.xmlpull.v1.XmlPullParser;

/**
 * Tartine: a color that varies by view state, mirroring AOSP
 * {@code android.content.res.ColorStateList}. The desktop shell resolves the
 * single flat color the resource table holds; state-specific lists built from
 * {@code <selector>} XML are a future step, so {@code valueOf} answers one
 * default color for every state.
 */
public class ColorStateList implements Parcelable {

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
        throw new UnsupportedOperationException(
                "Tartine: not implemented: ColorStateList.createFromXml");
    }

    public static ColorStateList createFromXml(Resources resources, XmlPullParser parser,
            Resources.Theme theme) {
        throw new UnsupportedOperationException(
                "Tartine: not implemented: ColorStateList.createFromXml");
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
