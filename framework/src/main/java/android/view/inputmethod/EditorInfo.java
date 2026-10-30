package android.view.inputmethod;

import android.os.Bundle;
import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.InputType;
import android.util.Printer;
import android.view.autofill.AutofillId;
import java.util.List;
import java.util.Set;

    public class EditorInfo implements InputType, Parcelable {

    public static Parcelable.Creator CREATOR;
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
    public CharSequence actionLabel;
    public String[] contentMimeTypes;
    public Bundle extras;
    public int fieldId;
    public String fieldName;
    public LocaleList hintLocales;
    public CharSequence hintText;
    public int imeOptions;
    public int initialCapsMode;
    public int initialSelEnd;
    public int initialSelStart;
    public int inputType;
    public CharSequence label;
    public String packageName;
    public String privateImeOptions;

    public EditorInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;-><init>()V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->describeContents()I");
    }

    public void dump(Printer arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->dump(Landroid/util/Printer;Ljava/lang/String;)V");
    }

    public AutofillId getAutofillId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->getAutofillId()Landroid/view/autofill/AutofillId;");
    }

    public CharSequence getInitialSelectedText(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->getInitialSelectedText(I)Ljava/lang/CharSequence;");
    }

    public SurroundingText getInitialSurroundingText(int arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->getInitialSurroundingText(III)Landroid/view/inputmethod/SurroundingText;");
    }

    public CharSequence getInitialTextAfterCursor(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->getInitialTextAfterCursor(II)Ljava/lang/CharSequence;");
    }

    public CharSequence getInitialTextBeforeCursor(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->getInitialTextBeforeCursor(II)Ljava/lang/CharSequence;");
    }

    public int getInitialToolType() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->getInitialToolType()I");
    }

    public Set<Class<? extends PreviewableHandwritingGesture>> getSupportedHandwritingGesturePreviews() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->getSupportedHandwritingGesturePreviews()Ljava/util/Set;");
    }

    public List<Class<? extends HandwritingGesture>> getSupportedHandwritingGestures() {
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

    public void setAutofillId(AutofillId arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->setAutofillId(Landroid/view/autofill/AutofillId;)V");
    }

    public void setInitialSurroundingSubText(CharSequence arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->setInitialSurroundingSubText(Ljava/lang/CharSequence;I)V");
    }

    public void setInitialSurroundingText(CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->setInitialSurroundingText(Ljava/lang/CharSequence;)V");
    }

    public void setInitialToolType(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->setInitialToolType(I)V");
    }

    public void setStylusHandwritingEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->setStylusHandwritingEnabled(Z)V");
    }

    public void setSupportedHandwritingGesturePreviews(Set<Class<? extends PreviewableHandwritingGesture>> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->setSupportedHandwritingGesturePreviews(Ljava/util/Set;)V");
    }

    public void setSupportedHandwritingGestures(List<Class<? extends HandwritingGesture>> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->setSupportedHandwritingGestures(Ljava/util/List;)V");
    }

    public void setWritingToolsEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->setWritingToolsEnabled(Z)V");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/EditorInfo;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
