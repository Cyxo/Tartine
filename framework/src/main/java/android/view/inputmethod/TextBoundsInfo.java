package android.view.inputmethod;

    public class TextBoundsInfo implements android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;
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

    public void getCharacterBounds(int arg0, android.graphics.RectF arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/TextBoundsInfo;->getCharacterBounds(ILandroid/graphics/RectF;)V");
    }

    public int getCharacterFlags(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/TextBoundsInfo;->getCharacterFlags(I)I");
    }

    public int getEndIndex() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/TextBoundsInfo;->getEndIndex()I");
    }

    public android.text.SegmentFinder getGraphemeSegmentFinder() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/TextBoundsInfo;->getGraphemeSegmentFinder()Landroid/text/SegmentFinder;");
    }

    public android.text.SegmentFinder getLineSegmentFinder() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/TextBoundsInfo;->getLineSegmentFinder()Landroid/text/SegmentFinder;");
    }

    public void getMatrix(android.graphics.Matrix arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/TextBoundsInfo;->getMatrix(Landroid/graphics/Matrix;)V");
    }

    public int getOffsetForPosition(float arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/TextBoundsInfo;->getOffsetForPosition(FF)I");
    }

    public int[] getRangeForRect(android.graphics.RectF arg0, android.text.SegmentFinder arg1, android.text.Layout.TextInclusionStrategy arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/TextBoundsInfo;->getRangeForRect(Landroid/graphics/RectF;Landroid/text/SegmentFinder;Landroid/text/Layout$TextInclusionStrategy;)[I");
    }

    public int getStartIndex() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/TextBoundsInfo;->getStartIndex()I");
    }

    public android.text.SegmentFinder getWordSegmentFinder() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/TextBoundsInfo;->getWordSegmentFinder()Landroid/text/SegmentFinder;");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/TextBoundsInfo;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
