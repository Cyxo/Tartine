package android.content.pm;

    public class PermissionGroupInfo extends android.content.pm.PackageItemInfo implements android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;
    public static final int FLAG_PERSONAL_INFO = 1;
    public int descriptionRes;
    public int flags;
    public java.lang.CharSequence nonLocalizedDescription;
    public int priority;

    public PermissionGroupInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PermissionGroupInfo;-><init>()V");
    }

    public PermissionGroupInfo(android.content.pm.PermissionGroupInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PermissionGroupInfo;-><init>(Landroid/content/pm/PermissionGroupInfo;)V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PermissionGroupInfo;->describeContents()I");
    }

    public java.lang.CharSequence loadDescription(android.content.pm.PackageManager arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PermissionGroupInfo;->loadDescription(Landroid/content/pm/PackageManager;)Ljava/lang/CharSequence;");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PermissionGroupInfo;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PermissionGroupInfo;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
