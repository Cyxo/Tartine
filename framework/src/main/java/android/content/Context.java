package android.content;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.util.AttributeSet;
import android.view.Display;
import android.view.accessibility.AccessibilityManager;
import android.view.autofill.AutofillManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.IntConsumer;

    public class Context {

    public static final String ACCESSIBILITY_SERVICE = "accessibility";
    public static final String ACCOUNT_SERVICE = "account";
    public static final String ACTIVITY_SERVICE = "activity";
    public static final String ADVANCED_PROTECTION_SERVICE = "advanced_protection";
    public static final String ALARM_SERVICE = "alarm";
    public static final String APPWIDGET_SERVICE = "appwidget";
    public static final String APP_FUNCTION_SERVICE = "app_function";
    public static final String APP_OPS_SERVICE = "appops";
    public static final String APP_SEARCH_SERVICE = "app_search";
    public static final String AUDIO_SERVICE = "audio";
    public static final String BATTERY_SERVICE = "batterymanager";
    public static final int BIND_ABOVE_CLIENT = 8;
    public static final int BIND_ADJUST_WITH_ACTIVITY = 128;
    public static final int BIND_ALLOW_ACTIVITY_STARTS = 512;
    public static final int BIND_ALLOW_OOM_MANAGEMENT = 16;
    public static final int BIND_AUTO_CREATE = 1;
    public static final int BIND_DEBUG_UNBIND = 2;
    public static final int BIND_EXTERNAL_SERVICE = -2147483648;
    public static final long BIND_EXTERNAL_SERVICE_LONG = 4611686018427387904L;
    public static final int BIND_IMPORTANT = 64;
    public static final int BIND_INCLUDE_CAPABILITIES = 4096;
    public static final int BIND_NOT_FOREGROUND = 4;
    public static final int BIND_NOT_PERCEPTIBLE = 256;
    public static final int BIND_PACKAGE_ISOLATED_PROCESS = 16384;
    public static final int BIND_SHARED_ISOLATED_PROCESS = 8192;
    public static final int BIND_WAIVE_PRIORITY = 32;
    public static final String BIOMETRIC_SERVICE = "biometric";
    public static final String BLOB_STORE_SERVICE = "blob_store";
    public static final String BLUETOOTH_SERVICE = "bluetooth";
    public static final String BUGREPORT_SERVICE = "bugreport";
    public static final String CAMERA_SERVICE = "camera";
    public static final String CAPTIONING_SERVICE = "captioning";
    public static final String CARRIER_CONFIG_SERVICE = "carrier_config";
    public static final String CLIPBOARD_SERVICE = "clipboard";
    public static final String COMPANION_DEVICE_SERVICE = "companiondevice";
    public static final String CONNECTIVITY_DIAGNOSTICS_SERVICE = "connectivity_diagnostics";
    public static final String CONNECTIVITY_SERVICE = "connectivity";
    public static final String CONSUMER_IR_SERVICE = "consumer_ir";
    public static final String CONTACT_KEYS_SERVICE = "contact_keys";
    public static final int CONTEXT_IGNORE_SECURITY = 2;
    public static final int CONTEXT_INCLUDE_CODE = 1;
    public static final int CONTEXT_RESTRICTED = 4;
    public static final String CREDENTIAL_SERVICE = "credential";
    public static final String CROSS_PROFILE_APPS_SERVICE = "crossprofileapps";
    public static final int DEVICE_ID_DEFAULT = 0;
    public static final int DEVICE_ID_INVALID = -1;
    public static final String DEVICE_LOCK_SERVICE = "device_lock";
    public static final String DEVICE_POLICY_SERVICE = "device_policy";
    public static final String DISPLAY_HASH_SERVICE = "display_hash";
    public static final String DISPLAY_SERVICE = "display";
    public static final String DOMAIN_VERIFICATION_SERVICE = "domain_verification";
    public static final String DOWNLOAD_SERVICE = "download";
    public static final String DROPBOX_SERVICE = "dropbox";
    public static final String EUICC_SERVICE = "euicc";
    public static final String FILE_INTEGRITY_SERVICE = "file_integrity";
    public static final String FINGERPRINT_SERVICE = "fingerprint";
    public static final String GAME_SERVICE = "game";
    public static final String GRAMMATICAL_INFLECTION_SERVICE = "grammatical_inflection";
    public static final String HARDWARE_PROPERTIES_SERVICE = "hardware_properties";
    public static final String HEALTHCONNECT_SERVICE = "healthconnect";
    public static final String INPUT_METHOD_SERVICE = "input_method";
    public static final String INPUT_SERVICE = "input";
    public static final String IPSEC_SERVICE = "ipsec";
    public static final String JOB_SCHEDULER_SERVICE = "jobscheduler";
    public static final String KEYGUARD_SERVICE = "keyguard";
    public static final String KEYSTORE_SERVICE = "keystore";
    public static final String LAUNCHER_APPS_SERVICE = "launcherapps";
    public static final String LAYOUT_INFLATER_SERVICE = "layout_inflater";
    public static final String LOCALE_SERVICE = "locale";
    public static final String LOCATION_SERVICE = "location";
    public static final String MEDIA_COMMUNICATION_SERVICE = "media_communication";
    public static final String MEDIA_METRICS_SERVICE = "media_metrics";
    public static final String MEDIA_PROJECTION_SERVICE = "media_projection";
    public static final String MEDIA_QUALITY_SERVICE = "media_quality";
    public static final String MEDIA_ROUTER_SERVICE = "media_router";
    public static final String MEDIA_SESSION_SERVICE = "media_session";
    public static final String MIDI_SERVICE = "midi";
    public static final int MODE_APPEND = 32768;
    public static final int MODE_ENABLE_WRITE_AHEAD_LOGGING = 8;
    public static final int MODE_MULTI_PROCESS = 4;
    public static final int MODE_NO_LOCALIZED_COLLATORS = 16;
    public static final int MODE_PRIVATE = 0;
    public static final int MODE_WORLD_READABLE = 1;
    public static final int MODE_WORLD_WRITEABLE = 2;
    public static final String NETWORK_STATS_SERVICE = "netstats";
    public static final String NFC_SERVICE = "nfc";
    public static final String NOTIFICATION_SERVICE = "notification";
    public static final String NSD_SERVICE = "servicediscovery";
    public static final String OVERLAY_SERVICE = "overlay";
    public static final String PEOPLE_SERVICE = "people";
    public static final String PERFORMANCE_HINT_SERVICE = "performance_hint";
    public static final String PERSISTENT_DATA_BLOCK_SERVICE = "persistent_data_block";
    public static final String POWER_SERVICE = "power";
    public static final String PRINT_SERVICE = "print";
    public static final String PROFILING_SERVICE = "profiling";
    public static final int RECEIVER_EXPORTED = 2;
    public static final int RECEIVER_NOT_EXPORTED = 4;
    public static final int RECEIVER_VISIBLE_TO_INSTANT_APPS = 1;
    public static final String RESTRICTIONS_SERVICE = "restrictions";
    public static final String ROLE_SERVICE = "role";
    public static final String SATELLITE_SERVICE = "satellite";
    public static final String SEARCH_SERVICE = "search";
    public static final String SECURITY_STATE_SERVICE = "security_state";
    public static final String SENSOR_SERVICE = "sensor";
    public static final String SHORTCUT_SERVICE = "shortcut";
    public static final String STATUS_BAR_SERVICE = "statusbar";
    public static final String STORAGE_SERVICE = "storage";
    public static final String STORAGE_STATS_SERVICE = "storagestats";
    public static final String SYSTEM_HEALTH_SERVICE = "systemhealth";
    public static final String TELECOM_SERVICE = "telecom";
    public static final String TELEPHONY_IMS_SERVICE = "telephony_ims";
    public static final String TELEPHONY_SERVICE = "phone";
    public static final String TELEPHONY_SUBSCRIPTION_SERVICE = "telephony_subscription_service";
    public static final String TETHERING_SERVICE = "tethering";
    public static final String TEXT_CLASSIFICATION_SERVICE = "textclassification";
    public static final String TEXT_SERVICES_MANAGER_SERVICE = "textservices";
    public static final String TV_AD_SERVICE = "tv_ad";
    public static final String TV_INPUT_SERVICE = "tv_input";
    public static final String TV_INTERACTIVE_APP_SERVICE = "tv_interactive_app";
    public static final String UI_MODE_SERVICE = "uimode";
    public static final String USAGE_STATS_SERVICE = "usagestats";
    public static final String USB_SERVICE = "usb";
    public static final String USER_SERVICE = "user";
    public static final String VIBRATOR_MANAGER_SERVICE = "vibrator_manager";
    public static final String VIBRATOR_SERVICE = "vibrator";
    public static final String VIRTUAL_DEVICE_SERVICE = "virtualdevice";
    public static final String VPN_MANAGEMENT_SERVICE = "vpn_management";
    public static final String WALLPAPER_SERVICE = "wallpaper";
    public static final String WIFI_AWARE_SERVICE = "wifiaware";
    public static final String WIFI_P2P_SERVICE = "wifip2p";
    public static final String WIFI_RTT_RANGING_SERVICE = "wifirtt";
    public static final String WIFI_SERVICE = "wifi";
    public static final String WINDOW_SERVICE = "window";

    // Host-side placeholder allocation: guest activities bottom out here
    // through Activity's no-op <init>; real android Context state (resource
    // refs, base context) arrives with the Framework-in-Java migration.
    public Context() {
    }

    public boolean bindIsolatedService(Intent arg0, Context.BindServiceFlags arg1, String arg2, Executor arg3, ServiceConnection arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->bindIsolatedService(Landroid/content/Intent;Landroid/content/Context$BindServiceFlags;Ljava/lang/String;Ljava/util/concurrent/Executor;Landroid/content/ServiceConnection;)Z");
    }

    public boolean bindIsolatedService(Intent arg0, int arg1, String arg2, Executor arg3, ServiceConnection arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->bindIsolatedService(Landroid/content/Intent;ILjava/lang/String;Ljava/util/concurrent/Executor;Landroid/content/ServiceConnection;)Z");
    }

    public boolean bindService(Intent arg0, Context.BindServiceFlags arg1, Executor arg2, ServiceConnection arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->bindService(Landroid/content/Intent;Landroid/content/Context$BindServiceFlags;Ljava/util/concurrent/Executor;Landroid/content/ServiceConnection;)Z");
    }

    public boolean bindService(Intent arg0, ServiceConnection arg1, Context.BindServiceFlags arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->bindService(Landroid/content/Intent;Landroid/content/ServiceConnection;Landroid/content/Context$BindServiceFlags;)Z");
    }

    public boolean bindService(Intent arg0, ServiceConnection arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->bindService(Landroid/content/Intent;Landroid/content/ServiceConnection;I)Z");
    }

    public boolean bindService(Intent arg0, int arg1, Executor arg2, ServiceConnection arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->bindService(Landroid/content/Intent;ILjava/util/concurrent/Executor;Landroid/content/ServiceConnection;)Z");
    }

    public boolean bindServiceAsUser(Intent arg0, ServiceConnection arg1, Context.BindServiceFlags arg2, UserHandle arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->bindServiceAsUser(Landroid/content/Intent;Landroid/content/ServiceConnection;Landroid/content/Context$BindServiceFlags;Landroid/os/UserHandle;)Z");
    }

    public boolean bindServiceAsUser(Intent arg0, ServiceConnection arg1, int arg2, UserHandle arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->bindServiceAsUser(Landroid/content/Intent;Landroid/content/ServiceConnection;ILandroid/os/UserHandle;)Z");
    }

    public int checkCallingOrSelfPermission(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->checkCallingOrSelfPermission(Ljava/lang/String;)I");
    }

    public int checkCallingOrSelfUriPermission(Uri arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->checkCallingOrSelfUriPermission(Landroid/net/Uri;I)I");
    }

    public int[] checkCallingOrSelfUriPermissions(List<Uri> arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->checkCallingOrSelfUriPermissions(Ljava/util/List;I)[I");
    }

    public int checkCallingPermission(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->checkCallingPermission(Ljava/lang/String;)I");
    }

    public int checkCallingUriPermission(Uri arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->checkCallingUriPermission(Landroid/net/Uri;I)I");
    }

    public int[] checkCallingUriPermissions(List<Uri> arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->checkCallingUriPermissions(Ljava/util/List;I)[I");
    }

    public int checkContentUriPermissionFull(Uri arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->checkContentUriPermissionFull(Landroid/net/Uri;III)I");
    }

    public int checkPermission(String arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->checkPermission(Ljava/lang/String;II)I");
    }

    public int checkSelfPermission(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->checkSelfPermission(Ljava/lang/String;)I");
    }

    public int checkUriPermission(Uri arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->checkUriPermission(Landroid/net/Uri;III)I");
    }

    public int checkUriPermission(Uri arg0, String arg1, String arg2, int arg3, int arg4, int arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->checkUriPermission(Landroid/net/Uri;Ljava/lang/String;Ljava/lang/String;III)I");
    }

    public int[] checkUriPermissions(List<Uri> arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->checkUriPermissions(Ljava/util/List;III)[I");
    }

    public void clearWallpaper() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->clearWallpaper()V");
    }

    public Context createAttributionContext(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->createAttributionContext(Ljava/lang/String;)Landroid/content/Context;");
    }

    public Context createConfigurationContext(Configuration arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->createConfigurationContext(Landroid/content/res/Configuration;)Landroid/content/Context;");
    }

    public Context createContext(ContextParams arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->createContext(Landroid/content/ContextParams;)Landroid/content/Context;");
    }

    public Context createContextForSplit(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->createContextForSplit(Ljava/lang/String;)Landroid/content/Context;");
    }

    public Context createDeviceContext(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->createDeviceContext(I)Landroid/content/Context;");
    }

    public Context createDeviceProtectedStorageContext() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->createDeviceProtectedStorageContext()Landroid/content/Context;");
    }

    public Context createDisplayContext(Display arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->createDisplayContext(Landroid/view/Display;)Landroid/content/Context;");
    }

    public Context createPackageContext(String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->createPackageContext(Ljava/lang/String;I)Landroid/content/Context;");
    }

    public Context createWindowContext(Display arg0, int arg1, Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->createWindowContext(Landroid/view/Display;ILandroid/os/Bundle;)Landroid/content/Context;");
    }

    public Context createWindowContext(int arg0, Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->createWindowContext(ILandroid/os/Bundle;)Landroid/content/Context;");
    }

    public String[] databaseList() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->databaseList()[Ljava/lang/String;");
    }

    public boolean deleteDatabase(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->deleteDatabase(Ljava/lang/String;)Z");
    }

    public boolean deleteFile(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->deleteFile(Ljava/lang/String;)Z");
    }

    public boolean deleteSharedPreferences(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->deleteSharedPreferences(Ljava/lang/String;)Z");
    }

    public void enforceCallingOrSelfPermission(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->enforceCallingOrSelfPermission(Ljava/lang/String;Ljava/lang/String;)V");
    }

    public void enforceCallingOrSelfUriPermission(Uri arg0, int arg1, String arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->enforceCallingOrSelfUriPermission(Landroid/net/Uri;ILjava/lang/String;)V");
    }

    public void enforceCallingPermission(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->enforceCallingPermission(Ljava/lang/String;Ljava/lang/String;)V");
    }

    public void enforceCallingUriPermission(Uri arg0, int arg1, String arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->enforceCallingUriPermission(Landroid/net/Uri;ILjava/lang/String;)V");
    }

    public void enforcePermission(String arg0, int arg1, int arg2, String arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->enforcePermission(Ljava/lang/String;IILjava/lang/String;)V");
    }

    public void enforceUriPermission(Uri arg0, int arg1, int arg2, int arg3, String arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->enforceUriPermission(Landroid/net/Uri;IIILjava/lang/String;)V");
    }

    public void enforceUriPermission(Uri arg0, String arg1, String arg2, int arg3, int arg4, int arg5, String arg6) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->enforceUriPermission(Landroid/net/Uri;Ljava/lang/String;Ljava/lang/String;IIILjava/lang/String;)V");
    }

    public String[] fileList() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->fileList()[Ljava/lang/String;");
    }

    public Context getApplicationContext() {
        // AOSP frameworks-base core/java/android/content/Context.java:
        // getApplicationContext is the base's own context; the shell
        // context is its own application root.
        return this;
    }

    public ApplicationInfo getApplicationInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getApplicationInfo()Landroid/content/pm/ApplicationInfo;");
    }

    public AssetManager getAssets() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getAssets()Landroid/content/res/AssetManager;");
    }

    public AttributionSource getAttributionSource() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getAttributionSource()Landroid/content/AttributionSource;");
    }

    public String getAttributionTag() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getAttributionTag()Ljava/lang/String;");
    }

    public File getCacheDir() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getCacheDir()Ljava/io/File;");
    }

    public ClassLoader getClassLoader() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getClassLoader()Ljava/lang/ClassLoader;");
    }

    public File getCodeCacheDir() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getCodeCacheDir()Ljava/io/File;");
    }

    public final int getColor(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getColor(I)I");
    }

    public final ColorStateList getColorStateList(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getColorStateList(I)Landroid/content/res/ColorStateList;");
    }

    public ContentResolver getContentResolver() {
        return new ContentResolver(this);
    }

    public File getDataDir() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getDataDir()Ljava/io/File;");
    }

    public File getDatabasePath(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getDatabasePath(Ljava/lang/String;)Ljava/io/File;");
    }

    public int getDeviceId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getDeviceId()I");
    }

    public File getDir(String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getDir(Ljava/lang/String;I)Ljava/io/File;");
    }

    public Display getDisplay() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getDisplay()Landroid/view/Display;");
    }

    public final Drawable getDrawable(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getDrawable(I)Landroid/graphics/drawable/Drawable;");
    }

    public File getExternalCacheDir() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getExternalCacheDir()Ljava/io/File;");
    }

    public File[] getExternalCacheDirs() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getExternalCacheDirs()[Ljava/io/File;");
    }

    public File getExternalFilesDir(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getExternalFilesDir(Ljava/lang/String;)Ljava/io/File;");
    }

    public File[] getExternalFilesDirs(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getExternalFilesDirs(Ljava/lang/String;)[Ljava/io/File;");
    }

    public File[] getExternalMediaDirs() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getExternalMediaDirs()[Ljava/io/File;");
    }

    public File getFileStreamPath(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getFileStreamPath(Ljava/lang/String;)Ljava/io/File;");
    }

    public File getFilesDir() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getFilesDir()Ljava/io/File;");
    }

    public Executor getMainExecutor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getMainExecutor()Ljava/util/concurrent/Executor;");
    }

    public Looper getMainLooper() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getMainLooper()Landroid/os/Looper;");
    }

    public File getNoBackupFilesDir() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getNoBackupFilesDir()Ljava/io/File;");
    }

    public File getObbDir() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getObbDir()Ljava/io/File;");
    }

    public File[] getObbDirs() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getObbDirs()[Ljava/io/File;");
    }

    public String getOpPackageName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getOpPackageName()Ljava/lang/String;");
    }

    public String getPackageCodePath() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getPackageCodePath()Ljava/lang/String;");
    }

    public PackageManager getPackageManager() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getPackageManager()Landroid/content/pm/PackageManager;");
    }

    public String getPackageName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getPackageName()Ljava/lang/String;");
    }

    public String getPackageResourcePath() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getPackageResourcePath()Ljava/lang/String;");
    }

    public ContextParams getParams() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getParams()Landroid/content/ContextParams;");
    }

    /**
     * The device answers the per-process Resources held by ContextImpl
     * (seeded through ResourcesManager#getResources); the shell lazily owns
     * one Resources per context, carrying the configuration surface only —
     * assets/dimensions land with the resource pipeline.
     */
    private Resources mResources;

    public Resources getResources() {
        if (mResources == null) {
            mResources = new Resources();
        }
        return mResources;
    }

    public SharedPreferences getSharedPreferences(String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getSharedPreferences(Ljava/lang/String;I)Landroid/content/SharedPreferences;");
    }

    public final String getString(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getString(I)Ljava/lang/String;");
    }

    public final String getString(int arg0, Object[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getString(I[Ljava/lang/Object;)Ljava/lang/String;");
    }

    public final <T> T getSystemService(Class<T> arg0) {
        // AOSP frameworks-base core/java/android/content/Context.java
        // getSystemService(Class): map the class to its service name, then
        // route through the string-based lookup so subclass overrides win.
        String serviceName = getSystemServiceName(arg0);
        @SuppressWarnings("unchecked")
        T service = serviceName != null
                ? (T) getSystemService(serviceName)
                : null;
        return service;
    }

    /**
     * The shell answers a small fixed service table: accessibility,
     * autofill, and clipboard are live objects, everything else misses to
     * null and the consumer's next wall names the gap. A real per-process
     * service registry is future work once the shell needs more services.
     */
    public Object getSystemService(String name) {
        if ("accessibility".equals(name)) {
            return new
                    AccessibilityManager();
        }
        if ("autofill".equals(name)) {
            return new AutofillManager();
        }
        if ("clipboard".equals(name)) {
            return new ClipboardManager(this);
        }
        return null;
    }

    /**
     * AOSP frameworks-base core/java/android/content/Context.java#
     * getSystemServiceName delegates to SystemServiceRegistry's name
     * table; the shell carries only the subset the fixture requests, so
     * unknown classes answer null (AOSP returns null for unregistered
     * classes too).
     */
    public String getSystemServiceName(Class<?> serviceClass) {
        if (serviceClass == null) {
            return null;
        }
        if (serviceClass.getName().equals(
                "android.view.autofill.AutofillManager")) {
            return "autofill";
        }
        if (serviceClass.getName().equals(
                "android.view.accessibility.AccessibilityManager")) {
            return "accessibility";
        }
        return null;
    }

    public final CharSequence getText(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getText(I)Ljava/lang/CharSequence;");
    }

    public Resources.Theme getTheme() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getTheme()Landroid/content/res/Resources$Theme;");
    }

    public Drawable getWallpaper() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getWallpaper()Landroid/graphics/drawable/Drawable;");
    }

    public int getWallpaperDesiredMinimumHeight() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getWallpaperDesiredMinimumHeight()I");
    }

    public int getWallpaperDesiredMinimumWidth() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getWallpaperDesiredMinimumWidth()I");
    }

    public void grantUriPermission(String arg0, Uri arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->grantUriPermission(Ljava/lang/String;Landroid/net/Uri;I)V");
    }

    public boolean isDeviceProtectedStorage() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->isDeviceProtectedStorage()Z");
    }

    public boolean isRestricted() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->isRestricted()Z");
    }

    public boolean isUiContext() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->isUiContext()Z");
    }

    public boolean moveDatabaseFrom(Context arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->moveDatabaseFrom(Landroid/content/Context;Ljava/lang/String;)Z");
    }

    public boolean moveSharedPreferencesFrom(Context arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->moveSharedPreferencesFrom(Landroid/content/Context;Ljava/lang/String;)Z");
    }

    public final TypedArray obtainStyledAttributes(AttributeSet arg0, int[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->obtainStyledAttributes(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;");
    }

    public final TypedArray obtainStyledAttributes(AttributeSet arg0, int[] arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->obtainStyledAttributes(Landroid/util/AttributeSet;[III)Landroid/content/res/TypedArray;");
    }

    public final TypedArray obtainStyledAttributes(int arg0, int[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->obtainStyledAttributes(I[I)Landroid/content/res/TypedArray;");
    }

    public final TypedArray obtainStyledAttributes(int[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->obtainStyledAttributes([I)Landroid/content/res/TypedArray;");
    }

    public FileInputStream openFileInput(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->openFileInput(Ljava/lang/String;)Ljava/io/FileInputStream;");
    }

    public FileOutputStream openFileOutput(String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->openFileOutput(Ljava/lang/String;I)Ljava/io/FileOutputStream;");
    }

    public SQLiteDatabase openOrCreateDatabase(String arg0, int arg1, SQLiteDatabase.CursorFactory arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->openOrCreateDatabase(Ljava/lang/String;ILandroid/database/sqlite/SQLiteDatabase$CursorFactory;)Landroid/database/sqlite/SQLiteDatabase;");
    }

    public SQLiteDatabase openOrCreateDatabase(String arg0, int arg1, SQLiteDatabase.CursorFactory arg2, DatabaseErrorHandler arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->openOrCreateDatabase(Ljava/lang/String;ILandroid/database/sqlite/SQLiteDatabase$CursorFactory;Landroid/database/DatabaseErrorHandler;)Landroid/database/sqlite/SQLiteDatabase;");
    }

    public Drawable peekWallpaper() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->peekWallpaper()Landroid/graphics/drawable/Drawable;");
    }

    public void registerComponentCallbacks(ComponentCallbacks arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->registerComponentCallbacks(Landroid/content/ComponentCallbacks;)V");
    }

    public void registerDeviceIdChangeListener(Executor arg0, IntConsumer arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->registerDeviceIdChangeListener(Ljava/util/concurrent/Executor;Ljava/util/function/IntConsumer;)V");
    }

    public Intent registerReceiver(BroadcastReceiver arg0, IntentFilter arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->registerReceiver(Landroid/content/BroadcastReceiver;Landroid/content/IntentFilter;)Landroid/content/Intent;");
    }

    public Intent registerReceiver(BroadcastReceiver arg0, IntentFilter arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->registerReceiver(Landroid/content/BroadcastReceiver;Landroid/content/IntentFilter;I)Landroid/content/Intent;");
    }

    public Intent registerReceiver(BroadcastReceiver arg0, IntentFilter arg1, String arg2, Handler arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->registerReceiver(Landroid/content/BroadcastReceiver;Landroid/content/IntentFilter;Ljava/lang/String;Landroid/os/Handler;)Landroid/content/Intent;");
    }

    public Intent registerReceiver(BroadcastReceiver arg0, IntentFilter arg1, String arg2, Handler arg3, int arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->registerReceiver(Landroid/content/BroadcastReceiver;Landroid/content/IntentFilter;Ljava/lang/String;Landroid/os/Handler;I)Landroid/content/Intent;");
    }

    public void removeStickyBroadcast(Intent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->removeStickyBroadcast(Landroid/content/Intent;)V");
    }

    public void removeStickyBroadcastAsUser(Intent arg0, UserHandle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->removeStickyBroadcastAsUser(Landroid/content/Intent;Landroid/os/UserHandle;)V");
    }

    public void revokeSelfPermissionOnKill(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->revokeSelfPermissionOnKill(Ljava/lang/String;)V");
    }

    public void revokeSelfPermissionsOnKill(Collection<String> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->revokeSelfPermissionsOnKill(Ljava/util/Collection;)V");
    }

    public void revokeUriPermission(Uri arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->revokeUriPermission(Landroid/net/Uri;I)V");
    }

    public void revokeUriPermission(String arg0, Uri arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->revokeUriPermission(Ljava/lang/String;Landroid/net/Uri;I)V");
    }

    public void sendBroadcast(Intent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->sendBroadcast(Landroid/content/Intent;)V");
    }

    public void sendBroadcast(Intent arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->sendBroadcast(Landroid/content/Intent;Ljava/lang/String;)V");
    }

    public void sendBroadcast(Intent arg0, String arg1, Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->sendBroadcast(Landroid/content/Intent;Ljava/lang/String;Landroid/os/Bundle;)V");
    }

    public void sendBroadcastAsUser(Intent arg0, UserHandle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->sendBroadcastAsUser(Landroid/content/Intent;Landroid/os/UserHandle;)V");
    }

    public void sendBroadcastAsUser(Intent arg0, UserHandle arg1, String arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->sendBroadcastAsUser(Landroid/content/Intent;Landroid/os/UserHandle;Ljava/lang/String;)V");
    }

    public void sendBroadcastWithMultiplePermissions(Intent arg0, String[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->sendBroadcastWithMultiplePermissions(Landroid/content/Intent;[Ljava/lang/String;)V");
    }

    public void sendOrderedBroadcast(Intent arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->sendOrderedBroadcast(Landroid/content/Intent;Ljava/lang/String;)V");
    }

    public void sendOrderedBroadcast(Intent arg0, String arg1, BroadcastReceiver arg2, Handler arg3, int arg4, String arg5, Bundle arg6) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->sendOrderedBroadcast(Landroid/content/Intent;Ljava/lang/String;Landroid/content/BroadcastReceiver;Landroid/os/Handler;ILjava/lang/String;Landroid/os/Bundle;)V");
    }

    public void sendOrderedBroadcast(Intent arg0, String arg1, Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->sendOrderedBroadcast(Landroid/content/Intent;Ljava/lang/String;Landroid/os/Bundle;)V");
    }

    public void sendOrderedBroadcast(Intent arg0, String arg1, Bundle arg2, BroadcastReceiver arg3, Handler arg4, int arg5, String arg6, Bundle arg7) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->sendOrderedBroadcast(Landroid/content/Intent;Ljava/lang/String;Landroid/os/Bundle;Landroid/content/BroadcastReceiver;Landroid/os/Handler;ILjava/lang/String;Landroid/os/Bundle;)V");
    }

    public void sendOrderedBroadcast(Intent arg0, String arg1, String arg2, BroadcastReceiver arg3, Handler arg4, int arg5, String arg6, Bundle arg7) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->sendOrderedBroadcast(Landroid/content/Intent;Ljava/lang/String;Ljava/lang/String;Landroid/content/BroadcastReceiver;Landroid/os/Handler;ILjava/lang/String;Landroid/os/Bundle;)V");
    }

    public void sendOrderedBroadcastAsUser(Intent arg0, UserHandle arg1, String arg2, BroadcastReceiver arg3, Handler arg4, int arg5, String arg6, Bundle arg7) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->sendOrderedBroadcastAsUser(Landroid/content/Intent;Landroid/os/UserHandle;Ljava/lang/String;Landroid/content/BroadcastReceiver;Landroid/os/Handler;ILjava/lang/String;Landroid/os/Bundle;)V");
    }

    public void sendStickyBroadcast(Intent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->sendStickyBroadcast(Landroid/content/Intent;)V");
    }

    public void sendStickyBroadcast(Intent arg0, Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->sendStickyBroadcast(Landroid/content/Intent;Landroid/os/Bundle;)V");
    }

    public void sendStickyBroadcastAsUser(Intent arg0, UserHandle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->sendStickyBroadcastAsUser(Landroid/content/Intent;Landroid/os/UserHandle;)V");
    }

    public void sendStickyOrderedBroadcast(Intent arg0, BroadcastReceiver arg1, Handler arg2, int arg3, String arg4, Bundle arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->sendStickyOrderedBroadcast(Landroid/content/Intent;Landroid/content/BroadcastReceiver;Landroid/os/Handler;ILjava/lang/String;Landroid/os/Bundle;)V");
    }

    public void sendStickyOrderedBroadcastAsUser(Intent arg0, UserHandle arg1, BroadcastReceiver arg2, Handler arg3, int arg4, String arg5, Bundle arg6) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->sendStickyOrderedBroadcastAsUser(Landroid/content/Intent;Landroid/os/UserHandle;Landroid/content/BroadcastReceiver;Landroid/os/Handler;ILjava/lang/String;Landroid/os/Bundle;)V");
    }

    public void setTheme(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->setTheme(I)V");
    }

    public void setWallpaper(Bitmap arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->setWallpaper(Landroid/graphics/Bitmap;)V");
    }

    public void setWallpaper(InputStream arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->setWallpaper(Ljava/io/InputStream;)V");
    }

    public void startActivities(Intent[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->startActivities([Landroid/content/Intent;)V");
    }

    public void startActivities(Intent[] arg0, Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->startActivities([Landroid/content/Intent;Landroid/os/Bundle;)V");
    }

    public void startActivity(Intent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->startActivity(Landroid/content/Intent;)V");
    }

    public void startActivity(Intent arg0, Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->startActivity(Landroid/content/Intent;Landroid/os/Bundle;)V");
    }

    public ComponentName startForegroundService(Intent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->startForegroundService(Landroid/content/Intent;)Landroid/content/ComponentName;");
    }

    public boolean startInstrumentation(ComponentName arg0, String arg1, Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->startInstrumentation(Landroid/content/ComponentName;Ljava/lang/String;Landroid/os/Bundle;)Z");
    }

    public void startIntentSender(IntentSender arg0, Intent arg1, int arg2, int arg3, int arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->startIntentSender(Landroid/content/IntentSender;Landroid/content/Intent;III)V");
    }

    public void startIntentSender(IntentSender arg0, Intent arg1, int arg2, int arg3, int arg4, Bundle arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->startIntentSender(Landroid/content/IntentSender;Landroid/content/Intent;IIILandroid/os/Bundle;)V");
    }

    public ComponentName startService(Intent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->startService(Landroid/content/Intent;)Landroid/content/ComponentName;");
    }

    public boolean stopService(Intent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->stopService(Landroid/content/Intent;)Z");
    }

    public void unbindService(ServiceConnection arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->unbindService(Landroid/content/ServiceConnection;)V");
    }

    public void unregisterComponentCallbacks(ComponentCallbacks arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->unregisterComponentCallbacks(Landroid/content/ComponentCallbacks;)V");
    }

    public void unregisterDeviceIdChangeListener(IntConsumer arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->unregisterDeviceIdChangeListener(Ljava/util/function/IntConsumer;)V");
    }

    public void unregisterReceiver(BroadcastReceiver arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->unregisterReceiver(Landroid/content/BroadcastReceiver;)V");
    }

    public void updateServiceGroup(ServiceConnection arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->updateServiceGroup(Landroid/content/ServiceConnection;II)V");
    }
        public static class BindServiceFlags {

        public static Context.BindServiceFlags of(long arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context$BindServiceFlags;->of(J)Landroid/content/Context$BindServiceFlags;");
        }
        }
    }
