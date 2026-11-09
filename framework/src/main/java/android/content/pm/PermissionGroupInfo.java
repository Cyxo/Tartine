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
        super();
    }

    public PermissionGroupInfo(PermissionGroupInfo arg0) {
        super(arg0);
        descriptionRes = arg0.descriptionRes;
        flags = arg0.flags;
        nonLocalizedDescription = arg0.nonLocalizedDescription;
        priority = arg0.priority;
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PermissionGroupInfo;->describeContents()I");
    }

    public CharSequence loadDescription(PackageManager arg0) {
        return nonLocalizedDescription;
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PermissionGroupInfo;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PermissionGroupInfo;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
