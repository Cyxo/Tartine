package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.PatternMatcher;
import android.util.Printer;

    public class ProviderInfo extends ComponentInfo implements Parcelable {

    public static Parcelable.Creator CREATOR;
    public static final int FLAG_SINGLE_USER = 1073741824;
    public String authority;
    public int flags;
    public boolean forceUriPermissions;
    public boolean grantUriPermissions;
    public int initOrder;
    public boolean isSyncable;
    public boolean multiprocess;
    public PathPermission[] pathPermissions;
    public String readPermission;
    public PatternMatcher[] uriPermissionPatterns;
    public String writePermission;

    public ProviderInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ProviderInfo;-><init>()V");
    }

    public ProviderInfo(ProviderInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ProviderInfo;-><init>(Landroid/content/pm/ProviderInfo;)V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ProviderInfo;->describeContents()I");
    }

    public void dump(Printer arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ProviderInfo;->dump(Landroid/util/Printer;Ljava/lang/String;)V");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ProviderInfo;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ProviderInfo;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
