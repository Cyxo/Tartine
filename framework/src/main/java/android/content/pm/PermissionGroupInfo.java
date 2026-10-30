package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;

    public class PermissionGroupInfo extends PackageItemInfo implements Parcelable {

    public static Parcelable.Creator CREATOR;
    public static final int FLAG_PERSONAL_INFO = 1;
    public int descriptionRes;
    public int flags;
    public CharSequence nonLocalizedDescription;
    public int priority;

    public PermissionGroupInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PermissionGroupInfo;-><init>()V");
    }

    public PermissionGroupInfo(PermissionGroupInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PermissionGroupInfo;-><init>(Landroid/content/pm/PermissionGroupInfo;)V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PermissionGroupInfo;->describeContents()I");
    }

    public CharSequence loadDescription(PackageManager arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PermissionGroupInfo;->loadDescription(Landroid/content/pm/PackageManager;)Ljava/lang/CharSequence;");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PermissionGroupInfo;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PermissionGroupInfo;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
