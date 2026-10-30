package android.view.translation;

import android.os.Parcel;
import android.os.Parcelable;

    public class TranslationCapability implements Parcelable {

    public static Parcelable.Creator CREATOR;
    public static final int STATE_AVAILABLE_TO_DOWNLOAD = 1;
    public static final int STATE_DOWNLOADING = 2;
    public static final int STATE_NOT_AVAILABLE = 4;
    public static final int STATE_ON_DEVICE = 3;

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/translation/TranslationCapability;->describeContents()I");
    }

    public TranslationSpec getSourceSpec() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/translation/TranslationCapability;->getSourceSpec()Landroid/view/translation/TranslationSpec;");
    }

    public int getState() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/translation/TranslationCapability;->getState()I");
    }

    public int getSupportedTranslationFlags() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/translation/TranslationCapability;->getSupportedTranslationFlags()I");
    }

    public TranslationSpec getTargetSpec() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/translation/TranslationCapability;->getTargetSpec()Landroid/view/translation/TranslationSpec;");
    }

    public boolean isUiTranslationEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/translation/TranslationCapability;->isUiTranslationEnabled()Z");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/translation/TranslationCapability;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/translation/TranslationCapability;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
