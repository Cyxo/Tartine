package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;

    public class PackageInfo implements Parcelable {

    public static Parcelable.Creator CREATOR;
    public static final int INSTALL_LOCATION_AUTO = 0;
    public static final int INSTALL_LOCATION_INTERNAL_ONLY = 1;
    public static final int INSTALL_LOCATION_PREFER_EXTERNAL = 2;
    public static final int REQUESTED_PERMISSION_GRANTED = 2;
    public static final int REQUESTED_PERMISSION_IMPLICIT = 4;
    public static final int REQUESTED_PERMISSION_NEVER_FOR_LOCATION = 65536;
    public ActivityInfo[] activities;
    public ApplicationInfo applicationInfo;
    public Attribution[] attributions;
    public int baseRevisionCode;
    public ConfigurationInfo[] configPreferences;
    public FeatureGroupInfo[] featureGroups;
    public long firstInstallTime;
    public int[] gids;
    public int installLocation;
    public InstrumentationInfo[] instrumentation;
    public boolean isApex;
    public long lastUpdateTime;
    public String packageName;
    public PermissionInfo[] permissions;
    public ProviderInfo[] providers;
    public ActivityInfo[] receivers;
    public FeatureInfo[] reqFeatures;
    public String[] requestedPermissions;
    public int[] requestedPermissionsFlags;
    public ServiceInfo[] services;
    public String sharedUserId;
    public int sharedUserLabel;
    public Signature[] signatures;
    public SigningInfo signingInfo;
    public String[] splitNames;
    public int[] splitRevisionCodes;
    public int versionCode;
    public String versionName;

    public PackageInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInfo;-><init>()V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInfo;->describeContents()I");
    }

    public String getApexPackageName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInfo;->getApexPackageName()Ljava/lang/String;");
    }

    public long getArchiveTimeMillis() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInfo;->getArchiveTimeMillis()J");
    }

    public long getLongVersionCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInfo;->getLongVersionCode()J");
    }

    public void setLongVersionCode(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInfo;->setLongVersionCode(J)V");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInfo;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInfo;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
