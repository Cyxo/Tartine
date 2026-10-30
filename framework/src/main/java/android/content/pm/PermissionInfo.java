package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;

    public class PermissionInfo extends PackageItemInfo implements Parcelable {

    public static Parcelable.Creator CREATOR;
    public static final int FLAG_COSTS_MONEY = 1;
    public static final int FLAG_HARD_RESTRICTED = 4;
    public static final int FLAG_IMMUTABLY_RESTRICTED = 16;
    public static final int FLAG_INSTALLED = 1073741824;
    public static final int FLAG_SOFT_RESTRICTED = 8;
    public static final int PROTECTION_DANGEROUS = 1;
    public static final int PROTECTION_FLAG_APPOP = 64;
    public static final int PROTECTION_FLAG_DEVELOPMENT = 32;
    public static final int PROTECTION_FLAG_INSTALLER = 256;
    public static final int PROTECTION_FLAG_INSTANT = 4096;
    public static final int PROTECTION_FLAG_PRE23 = 128;
    public static final int PROTECTION_FLAG_PREINSTALLED = 1024;
    public static final int PROTECTION_FLAG_PRIVILEGED = 16;
    public static final int PROTECTION_FLAG_RUNTIME_ONLY = 8192;
    public static final int PROTECTION_FLAG_SETUP = 2048;
    public static final int PROTECTION_FLAG_SYSTEM = 16;
    public static final int PROTECTION_FLAG_VERIFIER = 512;
    public static final int PROTECTION_INTERNAL = 4;
    public static final int PROTECTION_MASK_BASE = 15;
    public static final int PROTECTION_MASK_FLAGS = 65520;
    public static final int PROTECTION_NORMAL = 0;
    public static final int PROTECTION_SIGNATURE = 2;
    public static final int PROTECTION_SIGNATURE_OR_SYSTEM = 3;
    public int descriptionRes;
    public int flags;
    public String group;
    public CharSequence nonLocalizedDescription;
    public int protectionLevel;

    public PermissionInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PermissionInfo;-><init>()V");
    }

    public PermissionInfo(PermissionInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PermissionInfo;-><init>(Landroid/content/pm/PermissionInfo;)V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PermissionInfo;->describeContents()I");
    }

    public int getProtection() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PermissionInfo;->getProtection()I");
    }

    public int getProtectionFlags() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PermissionInfo;->getProtectionFlags()I");
    }

    public CharSequence loadDescription(PackageManager arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PermissionInfo;->loadDescription(Landroid/content/pm/PackageManager;)Ljava/lang/CharSequence;");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PermissionInfo;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PermissionInfo;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
