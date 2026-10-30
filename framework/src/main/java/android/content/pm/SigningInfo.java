package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import java.security.PublicKey;
import java.util.Collection;

    public class SigningInfo implements Parcelable {

    public static Parcelable.Creator CREATOR;
    public static final int VERSION_JAR = 1;
    public static final int VERSION_SIGNING_BLOCK_V2 = 2;
    public static final int VERSION_SIGNING_BLOCK_V3 = 3;
    public static final int VERSION_SIGNING_BLOCK_V4 = 4;

    public SigningInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/SigningInfo;-><init>()V");
    }

    public SigningInfo(SigningInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/SigningInfo;-><init>(Landroid/content/pm/SigningInfo;)V");
    }

    public SigningInfo(int arg0, Collection arg1, Collection arg2, Collection arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/SigningInfo;-><init>(ILjava/util/Collection;Ljava/util/Collection;Ljava/util/Collection;)V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/SigningInfo;->describeContents()I");
    }

    public Signature[] getApkContentsSigners() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/SigningInfo;->getApkContentsSigners()[Landroid/content/pm/Signature;");
    }

    public Collection<PublicKey> getPublicKeys() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/SigningInfo;->getPublicKeys()Ljava/util/Collection;");
    }

    public int getSchemeVersion() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/SigningInfo;->getSchemeVersion()I");
    }

    public Signature[] getSigningCertificateHistory() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/SigningInfo;->getSigningCertificateHistory()[Landroid/content/pm/Signature;");
    }

    public boolean hasMultipleSigners() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/SigningInfo;->hasMultipleSigners()Z");
    }

    public boolean hasPastSigningCertificates() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/SigningInfo;->hasPastSigningCertificates()Z");
    }

    public boolean signersMatchExactly(SigningInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/SigningInfo;->signersMatchExactly(Landroid/content/pm/SigningInfo;)Z");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/SigningInfo;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
