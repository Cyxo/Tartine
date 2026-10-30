package android.view.translation;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.autofill.AutofillId;
import java.util.Set;

    public class ViewTranslationRequest implements Parcelable {

    public static Parcelable.Creator CREATOR;
    public static final String ID_TEXT = "android:text";

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/translation/ViewTranslationRequest;->describeContents()I");
    }

    public boolean equals(Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/translation/ViewTranslationRequest;->equals(Ljava/lang/Object;)Z");
    }

    public AutofillId getAutofillId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/translation/ViewTranslationRequest;->getAutofillId()Landroid/view/autofill/AutofillId;");
    }

    public Set<String> getKeys() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/translation/ViewTranslationRequest;->getKeys()Ljava/util/Set;");
    }

    public TranslationRequestValue getValue(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/translation/ViewTranslationRequest;->getValue(Ljava/lang/String;)Landroid/view/translation/TranslationRequestValue;");
    }

    public int hashCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/translation/ViewTranslationRequest;->hashCode()I");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/translation/ViewTranslationRequest;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/translation/ViewTranslationRequest;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
