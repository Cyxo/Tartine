package android.view.inputmethod;

    public class ExtractedText implements android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;
    public static final int FLAG_SELECTING = 2;
    public static final int FLAG_SINGLE_LINE = 1;
    public int flags;
    public java.lang.CharSequence hint;
    public int partialEndOffset;
    public int partialStartOffset;
    public int selectionEnd;
    public int selectionStart;
    public int startOffset;
    public java.lang.CharSequence text;

    public ExtractedText() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/ExtractedText;-><init>()V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/ExtractedText;->describeContents()I");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/ExtractedText;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
