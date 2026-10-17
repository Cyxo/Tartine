package android.content.pm;

    public class PackageInfo implements android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;
    public static final int INSTALL_LOCATION_AUTO = 0;
    public static final int INSTALL_LOCATION_INTERNAL_ONLY = 1;
    public static final int INSTALL_LOCATION_PREFER_EXTERNAL = 2;
    public static final int REQUESTED_PERMISSION_GRANTED = 2;
    public static final int REQUESTED_PERMISSION_IMPLICIT = 4;
    public static final int REQUESTED_PERMISSION_NEVER_FOR_LOCATION = 65536;
    public android.content.pm.ActivityInfo[] activities;
    public android.content.pm.ApplicationInfo applicationInfo;
    public android.content.pm.Attribution[] attributions;
    public int baseRevisionCode;
    public android.content.pm.ConfigurationInfo[] configPreferences;
    public android.content.pm.FeatureGroupInfo[] featureGroups;
    public long firstInstallTime;
    public int[] gids;
    public int installLocation;
    public android.content.pm.InstrumentationInfo[] instrumentation;
    public boolean isApex;
    public long lastUpdateTime;
    public java.lang.String packageName;
    public android.content.pm.PermissionInfo[] permissions;
    public android.content.pm.ProviderInfo[] providers;
    public android.content.pm.ActivityInfo[] receivers;
    public android.content.pm.FeatureInfo[] reqFeatures;
    public java.lang.String[] requestedPermissions;
    public int[] requestedPermissionsFlags;
    public android.content.pm.ServiceInfo[] services;
    public java.lang.String sharedUserId;
    public int sharedUserLabel;
    public android.content.pm.Signature[] signatures;
    public android.content.pm.SigningInfo signingInfo;
    public java.lang.String[] splitNames;
    public int[] splitRevisionCodes;
    public int versionCode;
    public java.lang.String versionName;

    public PackageInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInfo;-><init>()V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInfo;->describeContents()I");
    }

    public java.lang.String getApexPackageName() {
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

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInfo;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInfo;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
