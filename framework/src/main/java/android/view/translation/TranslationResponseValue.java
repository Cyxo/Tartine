package android.view.translation;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

    public class TranslationResponseValue implements Parcelable {

    public static Parcelable.Creator CREATOR;
    public static final String EXTRA_DEFINITIONS = "android.view.translation.extra.DEFINITIONS";
    public static final int STATUS_ERROR = 1;
    public static final int STATUS_SUCCESS = 0;

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/translation/TranslationResponseValue;->describeContents()I");
    }

    public boolean equals(Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/translation/TranslationResponseValue;->equals(Ljava/lang/Object;)Z");
    }

    public static TranslationResponseValue forError() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/translation/TranslationResponseValue;->forError()Landroid/view/translation/TranslationResponseValue;");
    }

    public Bundle getExtras() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/translation/TranslationResponseValue;->getExtras()Landroid/os/Bundle;");
    }

    public int getStatusCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/translation/TranslationResponseValue;->getStatusCode()I");
    }

    public CharSequence getText() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/translation/TranslationResponseValue;->getText()Ljava/lang/CharSequence;");
    }

    public CharSequence getTransliteration() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/translation/TranslationResponseValue;->getTransliteration()Ljava/lang/CharSequence;");
    }

    public int hashCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/translation/TranslationResponseValue;->hashCode()I");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/translation/TranslationResponseValue;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/translation/TranslationResponseValue;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
