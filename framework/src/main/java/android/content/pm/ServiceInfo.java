package android.content.pm;

    public class ServiceInfo extends android.content.pm.ComponentInfo implements android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;
    public static final int FLAG_ALLOW_SHARED_ISOLATED_PROCESS = 16;
    public static final int FLAG_EXTERNAL_SERVICE = 4;
    public static final int FLAG_ISOLATED_PROCESS = 2;
    public static final int FLAG_SINGLE_USER = 1073741824;
    public static final int FLAG_STOP_WITH_TASK = 1;
    public static final int FLAG_USE_APP_ZYGOTE = 8;
    public static final int FOREGROUND_SERVICE_TYPE_CAMERA = 64;
    public static final int FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE = 16;
    public static final int FOREGROUND_SERVICE_TYPE_DATA_SYNC = 1;
    public static final int FOREGROUND_SERVICE_TYPE_HEALTH = 256;
    public static final int FOREGROUND_SERVICE_TYPE_LOCATION = 8;
    public static final int FOREGROUND_SERVICE_TYPE_MANIFEST = -1;
    public static final int FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK = 2;
    public static final int FOREGROUND_SERVICE_TYPE_MEDIA_PROCESSING = 8192;
    public static final int FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION = 32;
    public static final int FOREGROUND_SERVICE_TYPE_MICROPHONE = 128;
    public static final int FOREGROUND_SERVICE_TYPE_NONE = 0;
    public static final int FOREGROUND_SERVICE_TYPE_PHONE_CALL = 4;
    public static final int FOREGROUND_SERVICE_TYPE_REMOTE_MESSAGING = 512;
    public static final int FOREGROUND_SERVICE_TYPE_SHORT_SERVICE = 2048;
    public static final int FOREGROUND_SERVICE_TYPE_SPECIAL_USE = 1073741824;
    public static final int FOREGROUND_SERVICE_TYPE_SYSTEM_EXEMPTED = 1024;
    public int flags;
    public java.lang.String permission;

    public ServiceInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ServiceInfo;-><init>()V");
    }

    public ServiceInfo(android.content.pm.ServiceInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ServiceInfo;-><init>(Landroid/content/pm/ServiceInfo;)V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ServiceInfo;->describeContents()I");
    }

    public void dump(android.util.Printer arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ServiceInfo;->dump(Landroid/util/Printer;Ljava/lang/String;)V");
    }

    public int getForegroundServiceType() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ServiceInfo;->getForegroundServiceType()I");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ServiceInfo;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ServiceInfo;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
