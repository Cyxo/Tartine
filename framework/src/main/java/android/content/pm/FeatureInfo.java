package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;

    public class FeatureInfo implements Parcelable {

    public static Parcelable.Creator CREATOR;
    public static final int FLAG_REQUIRED = 1;
    public static final int GL_ES_VERSION_UNDEFINED = 0;
    public int flags;
    public String name;
    public int reqGlEsVersion;
    public int version;

    public FeatureInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/FeatureInfo;-><init>()V");
    }

    public FeatureInfo(FeatureInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/FeatureInfo;-><init>(Landroid/content/pm/FeatureInfo;)V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/FeatureInfo;->describeContents()I");
    }

    public String getGlEsVersion() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/FeatureInfo;->getGlEsVersion()Ljava/lang/String;");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/FeatureInfo;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/FeatureInfo;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
