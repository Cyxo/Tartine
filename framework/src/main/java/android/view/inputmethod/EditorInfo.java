package android.view.inputmethod;

    public class EditorInfo implements android.text.InputType, android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;
    public static final int IME_ACTION_DONE = 6;
    public static final int IME_ACTION_GO = 2;
    public static final int IME_ACTION_NEXT = 5;
    public static final int IME_ACTION_NONE = 1;
    public static final int IME_ACTION_PREVIOUS = 7;
    public static final int IME_ACTION_SEARCH = 3;
    public static final int IME_ACTION_SEND = 4;
    public static final int IME_ACTION_UNSPECIFIED = 0;
    public static final int IME_FLAG_FORCE_ASCII = -2147483648;
    public static final int IME_FLAG_NAVIGATE_NEXT = 134217728;
    public static final int IME_FLAG_NAVIGATE_PREVIOUS = 67108864;
    public static final int IME_FLAG_NO_ACCESSORY_ACTION = 536870912;
    public static final int IME_FLAG_NO_ENTER_ACTION = 1073741824;
    public static final int IME_FLAG_NO_EXTRACT_UI = 268435456;
    public static final int IME_FLAG_NO_FULLSCREEN = 33554432;
    public static final int IME_FLAG_NO_PERSONALIZED_LEARNING = 16777216;
    public static final int IME_MASK_ACTION = 255;
    public static final int IME_NULL = 0;
    public int actionId;
    public java.lang.CharSequence actionLabel;
    public java.lang.String[] contentMimeTypes;
    public android.os.Bundle extras;
    public int fieldId;
    public java.lang.String fieldName;
    public android.os.LocaleList hintLocales;
    public java.lang.CharSequence hintText;
    public int imeOptions;
    public int initialCapsMode;
    public int initialSelEnd;
    public int initialSelStart;
    public int inputType;
    public java.lang.CharSequence label;
    public java.lang.String packageName;
    public java.lang.String privateImeOptions;

    public EditorInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;-><init>()V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->describeContents()I");
    }

    public void dump(android.util.Printer arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->dump(Landroid/util/Printer;Ljava/lang/String;)V");
    }

    public android.view.autofill.AutofillId getAutofillId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->getAutofillId()Landroid/view/autofill/AutofillId;");
    }

    public java.lang.CharSequence getInitialSelectedText(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->getInitialSelectedText(I)Ljava/lang/CharSequence;");
    }

    public android.view.inputmethod.SurroundingText getInitialSurroundingText(int arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->getInitialSurroundingText(III)Landroid/view/inputmethod/SurroundingText;");
    }

    public java.lang.CharSequence getInitialTextAfterCursor(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->getInitialTextAfterCursor(II)Ljava/lang/CharSequence;");
    }

    public java.lang.CharSequence getInitialTextBeforeCursor(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->getInitialTextBeforeCursor(II)Ljava/lang/CharSequence;");
    }

    public int getInitialToolType() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->getInitialToolType()I");
    }

    public java.util.Set<java.lang.Class<? extends android.view.inputmethod.PreviewableHandwritingGesture>> getSupportedHandwritingGesturePreviews() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->getSupportedHandwritingGesturePreviews()Ljava/util/Set;");
    }

    public java.util.List<java.lang.Class<? extends android.view.inputmethod.HandwritingGesture>> getSupportedHandwritingGestures() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->getSupportedHandwritingGestures()Ljava/util/List;");
    }

    public boolean isStylusHandwritingEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->isStylusHandwritingEnabled()Z");
    }

    public boolean isWritingToolsEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->isWritingToolsEnabled()Z");
    }

    public final void makeCompatible(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->makeCompatible(I)V");
    }

    public void setAutofillId(android.view.autofill.AutofillId arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->setAutofillId(Landroid/view/autofill/AutofillId;)V");
    }

    public void setInitialSurroundingSubText(java.lang.CharSequence arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->setInitialSurroundingSubText(Ljava/lang/CharSequence;I)V");
    }

    public void setInitialSurroundingText(java.lang.CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->setInitialSurroundingText(Ljava/lang/CharSequence;)V");
    }

    public void setInitialToolType(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->setInitialToolType(I)V");
    }

    public void setStylusHandwritingEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->setStylusHandwritingEnabled(Z)V");
    }

    public void setSupportedHandwritingGesturePreviews(java.util.Set<java.lang.Class<? extends android.view.inputmethod.PreviewableHandwritingGesture>> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->setSupportedHandwritingGesturePreviews(Ljava/util/Set;)V");
    }

    public void setSupportedHandwritingGestures(java.util.List<java.lang.Class<? extends android.view.inputmethod.HandwritingGesture>> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->setSupportedHandwritingGestures(Ljava/util/List;)V");
    }

    public void setWritingToolsEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->setWritingToolsEnabled(Z)V");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
