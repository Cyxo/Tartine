package android.credentials;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

    public class Credential implements Parcelable {

    public static Parcelable.Creator CREATOR;
    public static final String TYPE_PASSWORD_CREDENTIAL = "android.credentials.TYPE_PASSWORD_CREDENTIAL";

    public Credential(String arg0, Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/credentials/Credential;-><init>(Ljava/lang/String;Landroid/os/Bundle;)V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/credentials/Credential;->describeContents()I");
    }

    public Bundle getData() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/credentials/Credential;->getData()Landroid/os/Bundle;");
    }

    public String getType() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/credentials/Credential;->getType()Ljava/lang/String;");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/credentials/Credential;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/credentials/Credential;->writeToParcel(Landroid/os/Parcel;I)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public Credential() {
        }
    }
