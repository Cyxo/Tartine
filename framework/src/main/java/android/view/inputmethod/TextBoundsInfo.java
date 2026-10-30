package android.view.inputmethod;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Layout;
import android.text.SegmentFinder;

    public class TextBoundsInfo implements Parcelable {

    public static Parcelable.Creator CREATOR;
    public static final int FLAG_CHARACTER_LINEFEED = 2;
    public static final int FLAG_CHARACTER_PUNCTUATION = 4;
    public static final int FLAG_CHARACTER_WHITESPACE = 1;
    public static final int FLAG_LINE_IS_RTL = 8;

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/TextBoundsInfo;->describeContents()I");
    }

    public int getCharacterBidiLevel(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/TextBoundsInfo;->getCharacterBidiLevel(I)I");
    }

    public void getCharacterBounds(int arg0, RectF arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/TextBoundsInfo;->getCharacterBounds(ILandroid/graphics/RectF;)V");
    }

    public int getCharacterFlags(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/TextBoundsInfo;->getCharacterFlags(I)I");
    }

    public int getEndIndex() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/TextBoundsInfo;->getEndIndex()I");
    }

    public SegmentFinder getGraphemeSegmentFinder() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/TextBoundsInfo;->getGraphemeSegmentFinder()Landroid/text/SegmentFinder;");
    }

    public SegmentFinder getLineSegmentFinder() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/TextBoundsInfo;->getLineSegmentFinder()Landroid/text/SegmentFinder;");
    }

    public void getMatrix(Matrix arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/TextBoundsInfo;->getMatrix(Landroid/graphics/Matrix;)V");
    }

    public int getOffsetForPosition(float arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/TextBoundsInfo;->getOffsetForPosition(FF)I");
    }

    public int[] getRangeForRect(RectF arg0, SegmentFinder arg1, Layout.TextInclusionStrategy arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/TextBoundsInfo;->getRangeForRect(Landroid/graphics/RectF;Landroid/text/SegmentFinder;Landroid/text/Layout$TextInclusionStrategy;)[I");
    }

    public int getStartIndex() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/TextBoundsInfo;->getStartIndex()I");
    }

    public SegmentFinder getWordSegmentFinder() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/TextBoundsInfo;->getWordSegmentFinder()Landroid/text/SegmentFinder;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/TextBoundsInfo;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
