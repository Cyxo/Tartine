package android.credentials;

import android.content.ComponentName;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Set;

    public class CredentialOption implements Parcelable {

    public static Parcelable.Creator CREATOR;
    public static final String SUPPORTED_ELEMENT_KEYS = "android.credentials.GetCredentialOption.SUPPORTED_ELEMENT_KEYS";

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/credentials/CredentialOption;->describeContents()I");
    }

    public Set<ComponentName> getAllowedProviders() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/credentials/CredentialOption;->getAllowedProviders()Ljava/util/Set;");
    }

    public Bundle getCandidateQueryData() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/credentials/CredentialOption;->getCandidateQueryData()Landroid/os/Bundle;");
    }

    public Bundle getCredentialRetrievalData() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/credentials/CredentialOption;->getCredentialRetrievalData()Landroid/os/Bundle;");
    }

    public String getType() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/credentials/CredentialOption;->getType()Ljava/lang/String;");
    }

    public boolean isSystemProviderRequired() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/credentials/CredentialOption;->isSystemProviderRequired()Z");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/credentials/CredentialOption;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/credentials/CredentialOption;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
