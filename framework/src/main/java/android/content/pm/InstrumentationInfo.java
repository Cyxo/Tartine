package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;

    public class InstrumentationInfo extends PackageItemInfo implements Parcelable {

    public static Parcelable.Creator CREATOR;
    public String dataDir;
    public boolean functionalTest;
    public boolean handleProfiling;
    public String publicSourceDir;
    public String sourceDir;
    public String[] splitNames;
    public String[] splitPublicSourceDirs;
    public String[] splitSourceDirs;
    public String targetPackage;
    public String targetProcesses;

    public InstrumentationInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/InstrumentationInfo;-><init>()V");
    }

    public InstrumentationInfo(InstrumentationInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/InstrumentationInfo;-><init>(Landroid/content/pm/InstrumentationInfo;)V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/InstrumentationInfo;->describeContents()I");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/InstrumentationInfo;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/InstrumentationInfo;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
