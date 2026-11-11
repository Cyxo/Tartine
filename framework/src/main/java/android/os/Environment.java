package android.os;

import java.io.File;

/**
 * Ported subset of {@code android.os.Environment}: the shared/external
 * storage entry points a guest app reads.
 *
 * <p>AOSP resolves {@link #getExternalStorageDirectory()} to
 * {@code sCurrentUser.getExternalDirs()[0]}, the primary emulated volume
 * mounted at {@code /storage/emulated/0} (the legacy {@code /sdcard} is a
 * symlink to it). Tartine's Vfs maps both guest spellings onto the
 * shared-storage card, so this class returns the emulated spelling and the
 * java.io bridge resolves it; returning
 * {@code new File(...)} here is safe because framework code (unlike guest
 * bytecode) is not rewritten to {@code GuestFile}.
 *
 * <p>In Tartine's model the card is always mounted, emulated, and never
 * physically removable, so the state and predicate members answer the
 * matching constants directly rather than consulting a storage service.
 *
 * <p>Only the members a guest links are present; the device-tree roots
 * ({@code /system}, {@code /data}, ...) stay unimplemented gaps because the
 * Vfs does not map them.
 */
public class Environment {

    /** Primary emulated shared storage; Vfs mount {@code /storage/emulated/0}. */
    private static final String EXTERNAL_STORAGE_PATH = "/storage/emulated/0";

    public static String DIRECTORY_MUSIC = "Music";
    public static String DIRECTORY_PODCASTS = "Podcasts";
    public static String DIRECTORY_RINGTONES = "Ringtones";
    public static String DIRECTORY_ALARMS = "Alarms";
    public static String DIRECTORY_NOTIFICATIONS = "Notifications";
    public static String DIRECTORY_PICTURES = "Pictures";
    public static String DIRECTORY_MOVIES = "Movies";
    public static String DIRECTORY_DOWNLOADS = "Download";
    public static String DIRECTORY_DCIM = "DCIM";
    public static String DIRECTORY_DOCUMENTS = "Documents";
    public static String DIRECTORY_SCREENSHOTS = "Screenshots";
    public static String DIRECTORY_AUDIOBOOKS = "Audiobooks";
    public static String DIRECTORY_RECORDINGS = "Recordings";

    public static final String MEDIA_UNKNOWN = "unknown";
    public static final String MEDIA_REMOVED = "removed";
    public static final String MEDIA_UNMOUNTED = "unmounted";
    public static final String MEDIA_CHECKING = "checking";
    public static final String MEDIA_NOFS = "nofs";
    public static final String MEDIA_MOUNTED = "mounted";
    public static final String MEDIA_MOUNTED_READ_ONLY = "mounted_ro";
    public static final String MEDIA_SHARED = "shared";
    public static final String MEDIA_BAD_REMOVAL = "bad_removal";
    public static final String MEDIA_UNMOUNTABLE = "unmountable";
    public static final String MEDIA_EJECTING = "ejecting";

    /** Matches AOSP's implicit public no-op constructor. */
    public Environment() {
    }

    /**
     * The primary shared/external storage directory, {@code /storage/emulated/0}.
     * The guest must hold a storage grant to read or write it through the
     * java.io bridge, exactly as on a device.
     */
    public static File getExternalStorageDirectory() {
        return new File(EXTERNAL_STORAGE_PATH);
    }

    /**
     * A top-level shared directory for {@code type} (one of the
     * {@code DIRECTORY_*} constants); the directory may not exist yet.
     */
    public static File getExternalStoragePublicDirectory(String type) {
        return new File(getExternalStorageDirectory(), type);
    }

    /** State of the primary shared storage; always mounted in Tartine. */
    public static String getExternalStorageState() {
        return getExternalStorageState(getExternalStorageDirectory());
    }

    /** State of the shared storage at {@code path}; always mounted. */
    public static String getExternalStorageState(File path) {
        return MEDIA_MOUNTED;
    }

    /**
     * @deprecated AOSP alias for {@link #getExternalStorageState(File)}.
     */
    @Deprecated
    public static String getStorageState(File path) {
        return getExternalStorageState(path);
    }

    /** Whether the primary shared storage is physically removable (no). */
    public static boolean isExternalStorageRemovable() {
        return isExternalStorageRemovable(getExternalStorageDirectory());
    }

    /** Whether the shared storage at {@code path} is removable (no). */
    public static boolean isExternalStorageRemovable(File path) {
        return false;
    }

    /** Whether the primary shared storage is emulated (yes: the card). */
    public static boolean isExternalStorageEmulated() {
        return isExternalStorageEmulated(getExternalStorageDirectory());
    }

    /** Whether the shared storage at {@code path} is emulated (yes). */
    public static boolean isExternalStorageEmulated(File path) {
        return true;
    }

    /** Whether the primary shared storage runs in legacy mode (no; scoped). */
    public static boolean isExternalStorageLegacy() {
        return isExternalStorageLegacy(getExternalStorageDirectory());
    }

    /**
     * Whether the shared storage at {@code path} runs in legacy mode. Tartine
     * targets API 36, where scoped storage is on for every app.
     */
    public static boolean isExternalStorageLegacy(File path) {
        return false;
    }

    /** Whether the app holds All Files Access (no grant by default). */
    public static boolean isExternalStorageManager() {
        return isExternalStorageManager(getExternalStorageDirectory());
    }

    /** Whether the app holds All Files Access at {@code path} (no). */
    public static boolean isExternalStorageManager(File path) {
        return false;
    }

    public static File getDataDirectory() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Environment;->getDataDirectory()Ljava/io/File;");
    }

    public static File getDownloadCacheDirectory() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Environment;->getDownloadCacheDirectory()Ljava/io/File;");
    }

    public static File getRootDirectory() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Environment;->getRootDirectory()Ljava/io/File;");
    }

    public static File getStorageDirectory() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Environment;->getStorageDirectory()Ljava/io/File;");
    }
}
