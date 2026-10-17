package android.content.pm;

    public class ApplicationInfo extends android.content.pm.PackageItemInfo implements android.os.Parcelable {

    public static final int CATEGORY_ACCESSIBILITY = 8;
    public static final int CATEGORY_AUDIO = 1;
    public static final int CATEGORY_GAME = 0;
    public static final int CATEGORY_IMAGE = 3;
    public static final int CATEGORY_MAPS = 6;
    public static final int CATEGORY_NEWS = 5;
    public static final int CATEGORY_PRODUCTIVITY = 7;
    public static final int CATEGORY_SOCIAL = 4;
    public static final int CATEGORY_UNDEFINED = -1;
    public static final int CATEGORY_VIDEO = 2;
    public static android.os.Parcelable.Creator CREATOR;
    public static final int FLAG_ALLOW_BACKUP = 32768;
    public static final int FLAG_ALLOW_CLEAR_USER_DATA = 64;
    public static final int FLAG_ALLOW_TASK_REPARENTING = 32;
    public static final int FLAG_DEBUGGABLE = 2;
    public static final int FLAG_EXTERNAL_STORAGE = 262144;
    public static final int FLAG_EXTRACT_NATIVE_LIBS = 268435456;
    public static final int FLAG_FACTORY_TEST = 16;
    public static final int FLAG_FULL_BACKUP_ONLY = 67108864;
    public static final int FLAG_HARDWARE_ACCELERATED = 536870912;
    public static final int FLAG_HAS_CODE = 4;
    public static final int FLAG_INSTALLED = 8388608;
    public static final int FLAG_IS_DATA_ONLY = 16777216;
    public static final int FLAG_IS_GAME = 33554432;
    public static final int FLAG_KILL_AFTER_RESTORE = 65536;
    public static final int FLAG_LARGE_HEAP = 1048576;
    public static final int FLAG_MULTIARCH = -2147483648;
    public static final int FLAG_PERSISTENT = 8;
    public static final int FLAG_RESIZEABLE_FOR_SCREENS = 4096;
    public static final int FLAG_RESTORE_ANY_VERSION = 131072;
    public static final int FLAG_STOPPED = 2097152;
    public static final int FLAG_SUPPORTS_LARGE_SCREENS = 2048;
    public static final int FLAG_SUPPORTS_NORMAL_SCREENS = 1024;
    public static final int FLAG_SUPPORTS_RTL = 4194304;
    public static final int FLAG_SUPPORTS_SCREEN_DENSITIES = 8192;
    public static final int FLAG_SUPPORTS_SMALL_SCREENS = 512;
    public static final int FLAG_SUPPORTS_XLARGE_SCREENS = 524288;
    public static final int FLAG_SUSPENDED = 1073741824;
    public static final int FLAG_SYSTEM = 1;
    public static final int FLAG_TEST_ONLY = 256;
    public static final int FLAG_UPDATED_SYSTEM_APP = 128;
    public static final int FLAG_USES_CLEARTEXT_TRAFFIC = 134217728;
    public static final int FLAG_VM_SAFE_MODE = 16384;
    public static final int GWP_ASAN_ALWAYS = 1;
    public static final int GWP_ASAN_DEFAULT = -1;
    public static final int GWP_ASAN_NEVER = 0;
    public static final int MEMTAG_ASYNC = 1;
    public static final int MEMTAG_DEFAULT = -1;
    public static final int MEMTAG_OFF = 0;
    public static final int MEMTAG_SYNC = 2;
    public static final int RAW_EXTERNAL_STORAGE_ACCESS_DEFAULT = 0;
    public static final int RAW_EXTERNAL_STORAGE_ACCESS_NOT_REQUESTED = 2;
    public static final int RAW_EXTERNAL_STORAGE_ACCESS_REQUESTED = 1;
    public static final int ZEROINIT_DEFAULT = -1;
    public static final int ZEROINIT_DISABLED = 0;
    public static final int ZEROINIT_ENABLED = 1;
    public java.lang.String appComponentFactory;
    public java.lang.String backupAgentName;
    public int category;
    public java.lang.String className;
    public int compatibleWidthLimitDp;
    public int compileSdkVersion;
    public java.lang.String compileSdkVersionCodename;
    public java.lang.String dataDir;
    public int descriptionRes;
    public java.lang.String deviceProtectedDataDir;
    public boolean enabled;
    public int flags;
    public int largestWidthLimitDp;
    public java.lang.String manageSpaceActivityName;
    public int minSdkVersion;
    public java.lang.String nativeLibraryDir;
    public java.lang.String permission;
    public java.lang.String processName;
    public java.lang.String publicSourceDir;
    public int requiresSmallestWidthDp;
    public java.lang.String[] sharedLibraryFiles;
    public java.lang.String sourceDir;
    public java.lang.String[] splitNames;
    public java.lang.String[] splitPublicSourceDirs;
    public java.lang.String[] splitSourceDirs;
    public java.util.UUID storageUuid;
    public int targetSdkVersion;
    public java.lang.String taskAffinity;
    public int theme;
    public int uiOptions;
    public int uid;

    public ApplicationInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ApplicationInfo;-><init>()V");
    }

    public ApplicationInfo(android.content.pm.ApplicationInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ApplicationInfo;-><init>(Landroid/content/pm/ApplicationInfo;)V");
    }

    public boolean areAttributionsUserVisible() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ApplicationInfo;->areAttributionsUserVisible()Z");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ApplicationInfo;->describeContents()I");
    }

    public void dump(android.util.Printer arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ApplicationInfo;->dump(Landroid/util/Printer;Ljava/lang/String;)V");
    }

    public static java.lang.CharSequence getCategoryTitle(android.content.Context arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ApplicationInfo;->getCategoryTitle(Landroid/content/Context;I)Ljava/lang/CharSequence;");
    }

    public int getGwpAsanMode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ApplicationInfo;->getGwpAsanMode()I");
    }

    public java.util.Set<java.lang.String> getKnownActivityEmbeddingCerts() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ApplicationInfo;->getKnownActivityEmbeddingCerts()Ljava/util/Set;");
    }

    public int getMemtagMode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ApplicationInfo;->getMemtagMode()I");
    }

    public int getNativeHeapZeroInitialized() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ApplicationInfo;->getNativeHeapZeroInitialized()I");
    }

    public int getRequestRawExternalStorageAccess() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ApplicationInfo;->getRequestRawExternalStorageAccess()I");
    }

    public boolean isProfileable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ApplicationInfo;->isProfileable()Z");
    }

    public boolean isProfileableByShell() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ApplicationInfo;->isProfileableByShell()Z");
    }

    public boolean isResourceOverlay() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ApplicationInfo;->isResourceOverlay()Z");
    }

    public boolean isVirtualPreload() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ApplicationInfo;->isVirtualPreload()Z");
    }

    public java.lang.CharSequence loadDescription(android.content.pm.PackageManager arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ApplicationInfo;->loadDescription(Landroid/content/pm/PackageManager;)Ljava/lang/CharSequence;");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ApplicationInfo;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ApplicationInfo;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
