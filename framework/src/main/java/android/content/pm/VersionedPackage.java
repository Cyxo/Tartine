package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;

    public class VersionedPackage implements Parcelable {

    public static Parcelable.Creator CREATOR;

    private final String packageName;
    private final long versionCode;

    public VersionedPackage(String arg0, int arg1) {
        this(arg0, (long) arg1);
    }

    public VersionedPackage(String arg0, long arg1) {
        packageName = arg0;
        versionCode = arg1;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object arg0) {
        if (this == arg0) {
            return true;
        }
        if (!(arg0 instanceof VersionedPackage other)) {
            return false;
        }
        return versionCode == other.versionCode
                && java.util.Objects.equals(packageName, other.packageName);
    }

    public long getLongVersionCode() {
        return versionCode;
    }

    public String getPackageName() {
        return packageName;
    }

    public int getVersionCode() {
        return (int) versionCode;
    }

    public int hashCode() {
        return java.util.Objects.hash(packageName, versionCode);
    }

    public String toString() {
        return "VersionedPackage{" + packageName + ":" + versionCode + "}";
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/VersionedPackage;->writeToParcel(Landroid/os/Parcel;I)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public VersionedPackage() {
            this(null, 0L);
        }
    }
