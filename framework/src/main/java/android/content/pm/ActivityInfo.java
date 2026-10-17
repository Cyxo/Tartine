package android.content.pm;

    public class ActivityInfo extends android.content.pm.ComponentInfo implements android.os.Parcelable {

    public static final int COLOR_MODE_DEFAULT = 0;
    public static final int COLOR_MODE_HDR = 2;
    public static final int COLOR_MODE_WIDE_COLOR_GAMUT = 1;
    public static final int CONFIG_ASSETS_PATHS = -2147483648;
    public static final int CONFIG_COLOR_MODE = 16384;
    public static final int CONFIG_DENSITY = 4096;
    public static final int CONFIG_FONT_SCALE = 1073741824;
    public static final int CONFIG_FONT_WEIGHT_ADJUSTMENT = 268435456;
    public static final int CONFIG_GRAMMATICAL_GENDER = 32768;
    public static final int CONFIG_KEYBOARD = 16;
    public static final int CONFIG_KEYBOARD_HIDDEN = 32;
    public static final int CONFIG_LAYOUT_DIRECTION = 8192;
    public static final int CONFIG_LOCALE = 4;
    public static final int CONFIG_MCC = 1;
    public static final int CONFIG_MNC = 2;
    public static final int CONFIG_NAVIGATION = 64;
    public static final int CONFIG_ORIENTATION = 128;
    public static final int CONFIG_RESOURCES_UNUSED = 134217728;
    public static final int CONFIG_SCREEN_LAYOUT = 256;
    public static final int CONFIG_SCREEN_SIZE = 1024;
    public static final int CONFIG_SMALLEST_SCREEN_SIZE = 2048;
    public static final int CONFIG_TOUCHSCREEN = 8;
    public static final int CONFIG_UI_MODE = 512;
    public static android.os.Parcelable.Creator CREATOR;
    public static final int DOCUMENT_LAUNCH_ALWAYS = 2;
    public static final int DOCUMENT_LAUNCH_INTO_EXISTING = 1;
    public static final int DOCUMENT_LAUNCH_NEVER = 3;
    public static final int DOCUMENT_LAUNCH_NONE = 0;
    public static final int FLAG_ALLOW_TASK_REPARENTING = 64;
    public static final int FLAG_ALLOW_UNTRUSTED_ACTIVITY_EMBEDDING = 268435456;
    public static final int FLAG_ALWAYS_RETAIN_TASK_STATE = 8;
    public static final int FLAG_AUTO_REMOVE_FROM_RECENTS = 8192;
    public static final int FLAG_CLEAR_TASK_ON_LAUNCH = 4;
    public static final int FLAG_ENABLE_VR_MODE = 32768;
    public static final int FLAG_EXCLUDE_FROM_RECENTS = 32;
    public static final int FLAG_FINISH_ON_CLOSE_SYSTEM_DIALOGS = 256;
    public static final int FLAG_FINISH_ON_TASK_LAUNCH = 2;
    public static final int FLAG_HARDWARE_ACCELERATED = 512;
    public static final int FLAG_IMMERSIVE = 2048;
    public static final int FLAG_MULTIPROCESS = 1;
    public static final int FLAG_NO_HISTORY = 128;
    public static final int FLAG_PREFER_MINIMAL_POST_PROCESSING = 33554432;
    public static final int FLAG_RELINQUISH_TASK_IDENTITY = 4096;
    public static final int FLAG_RESUME_WHILE_PAUSING = 16384;
    public static final int FLAG_SINGLE_USER = 1073741824;
    public static final int FLAG_STATE_NOT_NEEDED = 16;
    public static final int LAUNCH_MULTIPLE = 0;
    public static final int LAUNCH_SINGLE_INSTANCE = 3;
    public static final int LAUNCH_SINGLE_INSTANCE_PER_TASK = 4;
    public static final int LAUNCH_SINGLE_TASK = 2;
    public static final int LAUNCH_SINGLE_TOP = 1;
    public static final int PERSIST_ACROSS_REBOOTS = 2;
    public static final int PERSIST_NEVER = 1;
    public static final int PERSIST_ROOT_ONLY = 0;
    public static final int SCREEN_ORIENTATION_BEHIND = 3;
    public static final int SCREEN_ORIENTATION_FULL_SENSOR = 10;
    public static final int SCREEN_ORIENTATION_FULL_USER = 13;
    public static final int SCREEN_ORIENTATION_LANDSCAPE = 0;
    public static final int SCREEN_ORIENTATION_LOCKED = 14;
    public static final int SCREEN_ORIENTATION_NOSENSOR = 5;
    public static final int SCREEN_ORIENTATION_PORTRAIT = 1;
    public static final int SCREEN_ORIENTATION_REVERSE_LANDSCAPE = 8;
    public static final int SCREEN_ORIENTATION_REVERSE_PORTRAIT = 9;
    public static final int SCREEN_ORIENTATION_SENSOR = 4;
    public static final int SCREEN_ORIENTATION_SENSOR_LANDSCAPE = 6;
    public static final int SCREEN_ORIENTATION_SENSOR_PORTRAIT = 7;
    public static final int SCREEN_ORIENTATION_UNSPECIFIED = -1;
    public static final int SCREEN_ORIENTATION_USER = 2;
    public static final int SCREEN_ORIENTATION_USER_LANDSCAPE = 11;
    public static final int SCREEN_ORIENTATION_USER_PORTRAIT = 12;
    public static final int UIOPTION_SPLIT_ACTION_BAR_WHEN_NARROW = 1;
    public int colorMode;
    public int configChanges;
    public int documentLaunchMode;
    public int flags;
    public int launchMode;
    public int maxRecents;
    public java.lang.String parentActivityName;
    public java.lang.String permission;
    public int persistableMode;
    public java.lang.String requiredDisplayCategory;
    public int screenOrientation;
    public int softInputMode;
    public java.lang.String targetActivity;
    public java.lang.String taskAffinity;
    public int theme;
    public int uiOptions;
    public android.content.pm.ActivityInfo.WindowLayout windowLayout;

    public ActivityInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ActivityInfo;-><init>()V");
    }

    public ActivityInfo(android.content.pm.ActivityInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ActivityInfo;-><init>(Landroid/content/pm/ActivityInfo;)V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ActivityInfo;->describeContents()I");
    }

    public void dump(android.util.Printer arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ActivityInfo;->dump(Landroid/util/Printer;Ljava/lang/String;)V");
    }

    public java.util.Set<java.lang.String> getKnownActivityEmbeddingCerts() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ActivityInfo;->getKnownActivityEmbeddingCerts()Ljava/util/Set;");
    }

    public final int getThemeResource() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ActivityInfo;->getThemeResource()I");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ActivityInfo;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ActivityInfo;->writeToParcel(Landroid/os/Parcel;I)V");
    }
        public static class WindowLayout {

        public int gravity;
        public int height;
        public float heightFraction;
        public int minHeight;
        public int minWidth;
        public int width;
        public float widthFraction;

        public WindowLayout(int arg0, float arg1, int arg2, float arg3, int arg4, int arg5, int arg6) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ActivityInfo$WindowLayout;-><init>(IFIFIII)V");
        }

            /** Scratch mirror constructor (SkeletonGen). */
            public WindowLayout() {
            }
        }
    }
