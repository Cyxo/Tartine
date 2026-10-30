package android.credentials;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

    public class GetCredentialRequest implements Parcelable {

    public static Parcelable.Creator CREATOR;

    public boolean alwaysSendAppInfoToProvider() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/credentials/GetCredentialRequest;->alwaysSendAppInfoToProvider()Z");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/credentials/GetCredentialRequest;->describeContents()I");
    }

    public List<CredentialOption> getCredentialOptions() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/credentials/GetCredentialRequest;->getCredentialOptions()Ljava/util/List;");
    }

    public Bundle getData() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/credentials/GetCredentialRequest;->getData()Landroid/os/Bundle;");
    }

    public String getOrigin() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/credentials/GetCredentialRequest;->getOrigin()Ljava/lang/String;");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/credentials/GetCredentialRequest;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/credentials/GetCredentialRequest;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
