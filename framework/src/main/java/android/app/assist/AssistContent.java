package android.app.assist;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

    public class AssistContent implements Parcelable {

    public static Parcelable.Creator CREATOR;
    public static final String EXTRA_APP_FUNCTION_DATA = "android.app.assist.extra.APP_FUNCTION_DATA";

    public AssistContent() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/assist/AssistContent;-><init>()V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/assist/AssistContent;->describeContents()I");
    }

    public ClipData getClipData() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/assist/AssistContent;->getClipData()Landroid/content/ClipData;");
    }

    public Bundle getExtras() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/assist/AssistContent;->getExtras()Landroid/os/Bundle;");
    }

    public Intent getIntent() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/assist/AssistContent;->getIntent()Landroid/content/Intent;");
    }

    public Uri getSessionTransferUri() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/assist/AssistContent;->getSessionTransferUri()Landroid/net/Uri;");
    }

    public String getStructuredData() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/assist/AssistContent;->getStructuredData()Ljava/lang/String;");
    }

    public Uri getWebUri() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/assist/AssistContent;->getWebUri()Landroid/net/Uri;");
    }

    public boolean isAppProvidedIntent() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/assist/AssistContent;->isAppProvidedIntent()Z");
    }

    public boolean isAppProvidedWebUri() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/assist/AssistContent;->isAppProvidedWebUri()Z");
    }

    public void setClipData(ClipData arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/assist/AssistContent;->setClipData(Landroid/content/ClipData;)V");
    }

    public void setIntent(Intent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/assist/AssistContent;->setIntent(Landroid/content/Intent;)V");
    }

    public void setSessionTransferUri(Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/assist/AssistContent;->setSessionTransferUri(Landroid/net/Uri;)V");
    }

    public void setStructuredData(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/assist/AssistContent;->setStructuredData(Ljava/lang/String;)V");
    }

    public void setWebUri(Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/assist/AssistContent;->setWebUri(Landroid/net/Uri;)V");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/assist/AssistContent;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
