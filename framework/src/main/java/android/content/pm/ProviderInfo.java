package android.content.pm;

    public class ProviderInfo extends android.content.pm.ComponentInfo implements android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;
    public static final int FLAG_SINGLE_USER = 1073741824;
    public java.lang.String authority;
    public int flags;
    public boolean forceUriPermissions;
    public boolean grantUriPermissions;
    public int initOrder;
    public boolean isSyncable;
    public boolean multiprocess;
    public android.content.pm.PathPermission[] pathPermissions;
    public java.lang.String readPermission;
    public android.os.PatternMatcher[] uriPermissionPatterns;
    public java.lang.String writePermission;

    public ProviderInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ProviderInfo;-><init>()V");
    }

    public ProviderInfo(android.content.pm.ProviderInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ProviderInfo;-><init>(Landroid/content/pm/ProviderInfo;)V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ProviderInfo;->describeContents()I");
    }

    public void dump(android.util.Printer arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ProviderInfo;->dump(Landroid/util/Printer;Ljava/lang/String;)V");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ProviderInfo;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ProviderInfo;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
