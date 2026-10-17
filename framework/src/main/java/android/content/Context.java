package android.content;

    public abstract class Context {

    public static final java.lang.String ACCESSIBILITY_SERVICE = "accessibility";
    public static final java.lang.String ACCOUNT_SERVICE = "account";
    public static final java.lang.String ACTIVITY_SERVICE = "activity";
    public static final java.lang.String ADVANCED_PROTECTION_SERVICE = "advanced_protection";
    public static final java.lang.String ALARM_SERVICE = "alarm";
    public static final java.lang.String APPWIDGET_SERVICE = "appwidget";
    public static final java.lang.String APP_FUNCTION_SERVICE = "app_function";
    public static final java.lang.String APP_OPS_SERVICE = "appops";
    public static final java.lang.String APP_SEARCH_SERVICE = "app_search";
    public static final java.lang.String AUDIO_SERVICE = "audio";
    public static final java.lang.String BATTERY_SERVICE = "batterymanager";
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
    public static final java.lang.String BIOMETRIC_SERVICE = "biometric";
    public static final java.lang.String BLOB_STORE_SERVICE = "blob_store";
    public static final java.lang.String BLUETOOTH_SERVICE = "bluetooth";
    public static final java.lang.String BUGREPORT_SERVICE = "bugreport";
    public static final java.lang.String CAMERA_SERVICE = "camera";
    public static final java.lang.String CAPTIONING_SERVICE = "captioning";
    public static final java.lang.String CARRIER_CONFIG_SERVICE = "carrier_config";
    public static final java.lang.String CLIPBOARD_SERVICE = "clipboard";
    public static final java.lang.String COMPANION_DEVICE_SERVICE = "companiondevice";
    public static final java.lang.String CONNECTIVITY_DIAGNOSTICS_SERVICE = "connectivity_diagnostics";
    public static final java.lang.String CONNECTIVITY_SERVICE = "connectivity";
    public static final java.lang.String CONSUMER_IR_SERVICE = "consumer_ir";
    public static final java.lang.String CONTACT_KEYS_SERVICE = "contact_keys";
    public static final int CONTEXT_IGNORE_SECURITY = 2;
    public static final int CONTEXT_INCLUDE_CODE = 1;
    public static final int CONTEXT_RESTRICTED = 4;
    public static final java.lang.String CREDENTIAL_SERVICE = "credential";
    public static final java.lang.String CROSS_PROFILE_APPS_SERVICE = "crossprofileapps";
    public static final int DEVICE_ID_DEFAULT = 0;
    public static final int DEVICE_ID_INVALID = -1;
    public static final java.lang.String DEVICE_LOCK_SERVICE = "device_lock";
    public static final java.lang.String DEVICE_POLICY_SERVICE = "device_policy";
    public static final java.lang.String DISPLAY_HASH_SERVICE = "display_hash";
    public static final java.lang.String DISPLAY_SERVICE = "display";
    public static final java.lang.String DOMAIN_VERIFICATION_SERVICE = "domain_verification";
    public static final java.lang.String DOWNLOAD_SERVICE = "download";
    public static final java.lang.String DROPBOX_SERVICE = "dropbox";
    public static final java.lang.String EUICC_SERVICE = "euicc";
    public static final java.lang.String FILE_INTEGRITY_SERVICE = "file_integrity";
    public static final java.lang.String FINGERPRINT_SERVICE = "fingerprint";
    public static final java.lang.String GAME_SERVICE = "game";
    public static final java.lang.String GRAMMATICAL_INFLECTION_SERVICE = "grammatical_inflection";
    public static final java.lang.String HARDWARE_PROPERTIES_SERVICE = "hardware_properties";
    public static final java.lang.String HEALTHCONNECT_SERVICE = "healthconnect";
    public static final java.lang.String INPUT_METHOD_SERVICE = "input_method";
    public static final java.lang.String INPUT_SERVICE = "input";
    public static final java.lang.String IPSEC_SERVICE = "ipsec";
    public static final java.lang.String JOB_SCHEDULER_SERVICE = "jobscheduler";
    public static final java.lang.String KEYGUARD_SERVICE = "keyguard";
    public static final java.lang.String KEYSTORE_SERVICE = "keystore";
    public static final java.lang.String LAUNCHER_APPS_SERVICE = "launcherapps";
    public static final java.lang.String LAYOUT_INFLATER_SERVICE = "layout_inflater";
    public static final java.lang.String LOCALE_SERVICE = "locale";
    public static final java.lang.String LOCATION_SERVICE = "location";
    public static final java.lang.String MEDIA_COMMUNICATION_SERVICE = "media_communication";
    public static final java.lang.String MEDIA_METRICS_SERVICE = "media_metrics";
    public static final java.lang.String MEDIA_PROJECTION_SERVICE = "media_projection";
    public static final java.lang.String MEDIA_QUALITY_SERVICE = "media_quality";
    public static final java.lang.String MEDIA_ROUTER_SERVICE = "media_router";
    public static final java.lang.String MEDIA_SESSION_SERVICE = "media_session";
    public static final java.lang.String MIDI_SERVICE = "midi";
    public static final int MODE_APPEND = 32768;
    public static final int MODE_ENABLE_WRITE_AHEAD_LOGGING = 8;
    public static final int MODE_MULTI_PROCESS = 4;
    public static final int MODE_NO_LOCALIZED_COLLATORS = 16;
    public static final int MODE_PRIVATE = 0;
    public static final int MODE_WORLD_READABLE = 1;
    public static final int MODE_WORLD_WRITEABLE = 2;
    public static final java.lang.String NETWORK_STATS_SERVICE = "netstats";
    public static final java.lang.String NFC_SERVICE = "nfc";
    public static final java.lang.String NOTIFICATION_SERVICE = "notification";
    public static final java.lang.String NSD_SERVICE = "servicediscovery";
    public static final java.lang.String OVERLAY_SERVICE = "overlay";
    public static final java.lang.String PEOPLE_SERVICE = "people";
    public static final java.lang.String PERFORMANCE_HINT_SERVICE = "performance_hint";
    public static final java.lang.String PERSISTENT_DATA_BLOCK_SERVICE = "persistent_data_block";
    public static final java.lang.String POWER_SERVICE = "power";
    public static final java.lang.String PRINT_SERVICE = "print";
    public static final java.lang.String PROFILING_SERVICE = "profiling";
    public static final int RECEIVER_EXPORTED = 2;
    public static final int RECEIVER_NOT_EXPORTED = 4;
    public static final int RECEIVER_VISIBLE_TO_INSTANT_APPS = 1;
    public static final java.lang.String RESTRICTIONS_SERVICE = "restrictions";
    public static final java.lang.String ROLE_SERVICE = "role";
    public static final java.lang.String SATELLITE_SERVICE = "satellite";
    public static final java.lang.String SEARCH_SERVICE = "search";
    public static final java.lang.String SECURITY_STATE_SERVICE = "security_state";
    public static final java.lang.String SENSOR_SERVICE = "sensor";
    public static final java.lang.String SHORTCUT_SERVICE = "shortcut";
    public static final java.lang.String STATUS_BAR_SERVICE = "statusbar";
    public static final java.lang.String STORAGE_SERVICE = "storage";
    public static final java.lang.String STORAGE_STATS_SERVICE = "storagestats";
    public static final java.lang.String SYSTEM_HEALTH_SERVICE = "systemhealth";
    public static final java.lang.String TELECOM_SERVICE = "telecom";
    public static final java.lang.String TELEPHONY_IMS_SERVICE = "telephony_ims";
    public static final java.lang.String TELEPHONY_SERVICE = "phone";
    public static final java.lang.String TELEPHONY_SUBSCRIPTION_SERVICE = "telephony_subscription_service";
    public static final java.lang.String TETHERING_SERVICE = "tethering";
    public static final java.lang.String TEXT_CLASSIFICATION_SERVICE = "textclassification";
    public static final java.lang.String TEXT_SERVICES_MANAGER_SERVICE = "textservices";
    public static final java.lang.String TV_AD_SERVICE = "tv_ad";
    public static final java.lang.String TV_INPUT_SERVICE = "tv_input";
    public static final java.lang.String TV_INTERACTIVE_APP_SERVICE = "tv_interactive_app";
    public static final java.lang.String UI_MODE_SERVICE = "uimode";
    public static final java.lang.String USAGE_STATS_SERVICE = "usagestats";
    public static final java.lang.String USB_SERVICE = "usb";
    public static final java.lang.String USER_SERVICE = "user";
    public static final java.lang.String VIBRATOR_MANAGER_SERVICE = "vibrator_manager";
    public static final java.lang.String VIBRATOR_SERVICE = "vibrator";
    public static final java.lang.String VIRTUAL_DEVICE_SERVICE = "virtualdevice";
    public static final java.lang.String VPN_MANAGEMENT_SERVICE = "vpn_management";
    public static final java.lang.String WALLPAPER_SERVICE = "wallpaper";
    public static final java.lang.String WIFI_AWARE_SERVICE = "wifiaware";
    public static final java.lang.String WIFI_P2P_SERVICE = "wifip2p";
    public static final java.lang.String WIFI_RTT_RANGING_SERVICE = "wifirtt";
    public static final java.lang.String WIFI_SERVICE = "wifi";
    public static final java.lang.String WINDOW_SERVICE = "window";

    public Context() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;-><init>()V");
    }

    public boolean bindIsolatedService(android.content.Intent arg0, android.content.Context.BindServiceFlags arg1, java.lang.String arg2, java.util.concurrent.Executor arg3, android.content.ServiceConnection arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->bindIsolatedService(Landroid/content/Intent;Landroid/content/Context$BindServiceFlags;Ljava/lang/String;Ljava/util/concurrent/Executor;Landroid/content/ServiceConnection;)Z");
    }

    public boolean bindIsolatedService(android.content.Intent arg0, int arg1, java.lang.String arg2, java.util.concurrent.Executor arg3, android.content.ServiceConnection arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->bindIsolatedService(Landroid/content/Intent;ILjava/lang/String;Ljava/util/concurrent/Executor;Landroid/content/ServiceConnection;)Z");
    }

    public boolean bindService(android.content.Intent arg0, android.content.Context.BindServiceFlags arg1, java.util.concurrent.Executor arg2, android.content.ServiceConnection arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->bindService(Landroid/content/Intent;Landroid/content/Context$BindServiceFlags;Ljava/util/concurrent/Executor;Landroid/content/ServiceConnection;)Z");
    }

    public boolean bindService(android.content.Intent arg0, android.content.ServiceConnection arg1, android.content.Context.BindServiceFlags arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->bindService(Landroid/content/Intent;Landroid/content/ServiceConnection;Landroid/content/Context$BindServiceFlags;)Z");
    }

    public abstract boolean bindService(android.content.Intent arg0, android.content.ServiceConnection arg1, int arg2);

    public boolean bindService(android.content.Intent arg0, int arg1, java.util.concurrent.Executor arg2, android.content.ServiceConnection arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->bindService(Landroid/content/Intent;ILjava/util/concurrent/Executor;Landroid/content/ServiceConnection;)Z");
    }

    public boolean bindServiceAsUser(android.content.Intent arg0, android.content.ServiceConnection arg1, android.content.Context.BindServiceFlags arg2, android.os.UserHandle arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->bindServiceAsUser(Landroid/content/Intent;Landroid/content/ServiceConnection;Landroid/content/Context$BindServiceFlags;Landroid/os/UserHandle;)Z");
    }

    public boolean bindServiceAsUser(android.content.Intent arg0, android.content.ServiceConnection arg1, int arg2, android.os.UserHandle arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->bindServiceAsUser(Landroid/content/Intent;Landroid/content/ServiceConnection;ILandroid/os/UserHandle;)Z");
    }

    public abstract int checkCallingOrSelfPermission(java.lang.String arg0);

    public abstract int checkCallingOrSelfUriPermission(android.net.Uri arg0, int arg1);

    public int[] checkCallingOrSelfUriPermissions(java.util.List<android.net.Uri> arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->checkCallingOrSelfUriPermissions(Ljava/util/List;I)[I");
    }

    public abstract int checkCallingPermission(java.lang.String arg0);

    public abstract int checkCallingUriPermission(android.net.Uri arg0, int arg1);

    public int[] checkCallingUriPermissions(java.util.List<android.net.Uri> arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->checkCallingUriPermissions(Ljava/util/List;I)[I");
    }

    public int checkContentUriPermissionFull(android.net.Uri arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->checkContentUriPermissionFull(Landroid/net/Uri;III)I");
    }

    public abstract int checkPermission(java.lang.String arg0, int arg1, int arg2);

    public abstract int checkSelfPermission(java.lang.String arg0);

    public abstract int checkUriPermission(android.net.Uri arg0, int arg1, int arg2, int arg3);

    public abstract int checkUriPermission(android.net.Uri arg0, java.lang.String arg1, java.lang.String arg2, int arg3, int arg4, int arg5);

    public int[] checkUriPermissions(java.util.List<android.net.Uri> arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->checkUriPermissions(Ljava/util/List;III)[I");
    }

    public abstract void clearWallpaper();

    public android.content.Context createAttributionContext(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->createAttributionContext(Ljava/lang/String;)Landroid/content/Context;");
    }

    public abstract android.content.Context createConfigurationContext(android.content.res.Configuration arg0);

    public android.content.Context createContext(android.content.ContextParams arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->createContext(Landroid/content/ContextParams;)Landroid/content/Context;");
    }

    public abstract android.content.Context createContextForSplit(java.lang.String arg0);

    public android.content.Context createDeviceContext(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->createDeviceContext(I)Landroid/content/Context;");
    }

    public abstract android.content.Context createDeviceProtectedStorageContext();

    public abstract android.content.Context createDisplayContext(android.view.Display arg0);

    public abstract android.content.Context createPackageContext(java.lang.String arg0, int arg1);

    public android.content.Context createWindowContext(android.view.Display arg0, int arg1, android.os.Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->createWindowContext(Landroid/view/Display;ILandroid/os/Bundle;)Landroid/content/Context;");
    }

    public android.content.Context createWindowContext(int arg0, android.os.Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->createWindowContext(ILandroid/os/Bundle;)Landroid/content/Context;");
    }

    public abstract java.lang.String[] databaseList();

    public abstract boolean deleteDatabase(java.lang.String arg0);

    public abstract boolean deleteFile(java.lang.String arg0);

    public abstract boolean deleteSharedPreferences(java.lang.String arg0);

    public abstract void enforceCallingOrSelfPermission(java.lang.String arg0, java.lang.String arg1);

    public abstract void enforceCallingOrSelfUriPermission(android.net.Uri arg0, int arg1, java.lang.String arg2);

    public abstract void enforceCallingPermission(java.lang.String arg0, java.lang.String arg1);

    public abstract void enforceCallingUriPermission(android.net.Uri arg0, int arg1, java.lang.String arg2);

    public abstract void enforcePermission(java.lang.String arg0, int arg1, int arg2, java.lang.String arg3);

    public abstract void enforceUriPermission(android.net.Uri arg0, int arg1, int arg2, int arg3, java.lang.String arg4);

    public abstract void enforceUriPermission(android.net.Uri arg0, java.lang.String arg1, java.lang.String arg2, int arg3, int arg4, int arg5, java.lang.String arg6);

    public abstract java.lang.String[] fileList();

    public abstract android.content.Context getApplicationContext();

    public abstract android.content.pm.ApplicationInfo getApplicationInfo();

    public abstract android.content.res.AssetManager getAssets();

    public android.content.AttributionSource getAttributionSource() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getAttributionSource()Landroid/content/AttributionSource;");
    }

    public java.lang.String getAttributionTag() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getAttributionTag()Ljava/lang/String;");
    }

    public abstract java.io.File getCacheDir();

    public abstract java.lang.ClassLoader getClassLoader();

    public abstract java.io.File getCodeCacheDir();

    public final int getColor(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getColor(I)I");
    }

    public final android.content.res.ColorStateList getColorStateList(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getColorStateList(I)Landroid/content/res/ColorStateList;");
    }

    public abstract android.content.ContentResolver getContentResolver();

    public abstract java.io.File getDataDir();

    public abstract java.io.File getDatabasePath(java.lang.String arg0);

    public int getDeviceId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getDeviceId()I");
    }

    public abstract java.io.File getDir(java.lang.String arg0, int arg1);

    public android.view.Display getDisplay() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getDisplay()Landroid/view/Display;");
    }

    public final android.graphics.drawable.Drawable getDrawable(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getDrawable(I)Landroid/graphics/drawable/Drawable;");
    }

    public abstract java.io.File getExternalCacheDir();

    public abstract java.io.File[] getExternalCacheDirs();

    public abstract java.io.File getExternalFilesDir(java.lang.String arg0);

    public abstract java.io.File[] getExternalFilesDirs(java.lang.String arg0);

    public abstract java.io.File[] getExternalMediaDirs();

    public abstract java.io.File getFileStreamPath(java.lang.String arg0);

    public abstract java.io.File getFilesDir();

    public java.util.concurrent.Executor getMainExecutor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getMainExecutor()Ljava/util/concurrent/Executor;");
    }

    public abstract android.os.Looper getMainLooper();

    public abstract java.io.File getNoBackupFilesDir();

    public abstract java.io.File getObbDir();

    public abstract java.io.File[] getObbDirs();

    public java.lang.String getOpPackageName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getOpPackageName()Ljava/lang/String;");
    }

    public abstract java.lang.String getPackageCodePath();

    public abstract android.content.pm.PackageManager getPackageManager();

    public abstract java.lang.String getPackageName();

    public abstract java.lang.String getPackageResourcePath();

    public android.content.ContextParams getParams() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getParams()Landroid/content/ContextParams;");
    }

    public abstract android.content.res.Resources getResources();

    public abstract android.content.SharedPreferences getSharedPreferences(java.lang.String arg0, int arg1);

    public final java.lang.String getString(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getString(I)Ljava/lang/String;");
    }

    public final java.lang.String getString(int arg0, java.lang.Object[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getString(I[Ljava/lang/Object;)Ljava/lang/String;");
    }

    public final <T> T getSystemService(java.lang.Class<T> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getSystemService(Ljava/lang/Class;)Ljava/lang/Object;");
    }

    public abstract java.lang.Object getSystemService(java.lang.String arg0);

    public abstract java.lang.String getSystemServiceName(java.lang.Class<?> arg0);

    public final java.lang.CharSequence getText(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->getText(I)Ljava/lang/CharSequence;");
    }

    public abstract android.content.res.Resources.Theme getTheme();

    public abstract android.graphics.drawable.Drawable getWallpaper();

    public abstract int getWallpaperDesiredMinimumHeight();

    public abstract int getWallpaperDesiredMinimumWidth();

    public abstract void grantUriPermission(java.lang.String arg0, android.net.Uri arg1, int arg2);

    public abstract boolean isDeviceProtectedStorage();

    public boolean isRestricted() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->isRestricted()Z");
    }

    public boolean isUiContext() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->isUiContext()Z");
    }

    public abstract boolean moveDatabaseFrom(android.content.Context arg0, java.lang.String arg1);

    public abstract boolean moveSharedPreferencesFrom(android.content.Context arg0, java.lang.String arg1);

    public final android.content.res.TypedArray obtainStyledAttributes(android.util.AttributeSet arg0, int[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->obtainStyledAttributes(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;");
    }

    public final android.content.res.TypedArray obtainStyledAttributes(android.util.AttributeSet arg0, int[] arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->obtainStyledAttributes(Landroid/util/AttributeSet;[III)Landroid/content/res/TypedArray;");
    }

    public final android.content.res.TypedArray obtainStyledAttributes(int arg0, int[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->obtainStyledAttributes(I[I)Landroid/content/res/TypedArray;");
    }

    public final android.content.res.TypedArray obtainStyledAttributes(int[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->obtainStyledAttributes([I)Landroid/content/res/TypedArray;");
    }

    public abstract java.io.FileInputStream openFileInput(java.lang.String arg0);

    public abstract java.io.FileOutputStream openFileOutput(java.lang.String arg0, int arg1);

    public abstract android.database.sqlite.SQLiteDatabase openOrCreateDatabase(java.lang.String arg0, int arg1, android.database.sqlite.SQLiteDatabase.CursorFactory arg2);

    public abstract android.database.sqlite.SQLiteDatabase openOrCreateDatabase(java.lang.String arg0, int arg1, android.database.sqlite.SQLiteDatabase.CursorFactory arg2, android.database.DatabaseErrorHandler arg3);

    public abstract android.graphics.drawable.Drawable peekWallpaper();

    public void registerComponentCallbacks(android.content.ComponentCallbacks arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->registerComponentCallbacks(Landroid/content/ComponentCallbacks;)V");
    }

    public void registerDeviceIdChangeListener(java.util.concurrent.Executor arg0, java.util.function.IntConsumer arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->registerDeviceIdChangeListener(Ljava/util/concurrent/Executor;Ljava/util/function/IntConsumer;)V");
    }

    public abstract android.content.Intent registerReceiver(android.content.BroadcastReceiver arg0, android.content.IntentFilter arg1);

    public abstract android.content.Intent registerReceiver(android.content.BroadcastReceiver arg0, android.content.IntentFilter arg1, int arg2);

    public abstract android.content.Intent registerReceiver(android.content.BroadcastReceiver arg0, android.content.IntentFilter arg1, java.lang.String arg2, android.os.Handler arg3);

    public abstract android.content.Intent registerReceiver(android.content.BroadcastReceiver arg0, android.content.IntentFilter arg1, java.lang.String arg2, android.os.Handler arg3, int arg4);

    public abstract void removeStickyBroadcast(android.content.Intent arg0);

    public abstract void removeStickyBroadcastAsUser(android.content.Intent arg0, android.os.UserHandle arg1);

    public void revokeSelfPermissionOnKill(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->revokeSelfPermissionOnKill(Ljava/lang/String;)V");
    }

    public void revokeSelfPermissionsOnKill(java.util.Collection<java.lang.String> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->revokeSelfPermissionsOnKill(Ljava/util/Collection;)V");
    }

    public abstract void revokeUriPermission(android.net.Uri arg0, int arg1);

    public abstract void revokeUriPermission(java.lang.String arg0, android.net.Uri arg1, int arg2);

    public abstract void sendBroadcast(android.content.Intent arg0);

    public abstract void sendBroadcast(android.content.Intent arg0, java.lang.String arg1);

    public void sendBroadcast(android.content.Intent arg0, java.lang.String arg1, android.os.Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->sendBroadcast(Landroid/content/Intent;Ljava/lang/String;Landroid/os/Bundle;)V");
    }

    public abstract void sendBroadcastAsUser(android.content.Intent arg0, android.os.UserHandle arg1);

    public abstract void sendBroadcastAsUser(android.content.Intent arg0, android.os.UserHandle arg1, java.lang.String arg2);

    public void sendBroadcastWithMultiplePermissions(android.content.Intent arg0, java.lang.String[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->sendBroadcastWithMultiplePermissions(Landroid/content/Intent;[Ljava/lang/String;)V");
    }

    public abstract void sendOrderedBroadcast(android.content.Intent arg0, java.lang.String arg1);

    public abstract void sendOrderedBroadcast(android.content.Intent arg0, java.lang.String arg1, android.content.BroadcastReceiver arg2, android.os.Handler arg3, int arg4, java.lang.String arg5, android.os.Bundle arg6);

    public void sendOrderedBroadcast(android.content.Intent arg0, java.lang.String arg1, android.os.Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->sendOrderedBroadcast(Landroid/content/Intent;Ljava/lang/String;Landroid/os/Bundle;)V");
    }

    public void sendOrderedBroadcast(android.content.Intent arg0, java.lang.String arg1, android.os.Bundle arg2, android.content.BroadcastReceiver arg3, android.os.Handler arg4, int arg5, java.lang.String arg6, android.os.Bundle arg7) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->sendOrderedBroadcast(Landroid/content/Intent;Ljava/lang/String;Landroid/os/Bundle;Landroid/content/BroadcastReceiver;Landroid/os/Handler;ILjava/lang/String;Landroid/os/Bundle;)V");
    }

    public void sendOrderedBroadcast(android.content.Intent arg0, java.lang.String arg1, java.lang.String arg2, android.content.BroadcastReceiver arg3, android.os.Handler arg4, int arg5, java.lang.String arg6, android.os.Bundle arg7) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->sendOrderedBroadcast(Landroid/content/Intent;Ljava/lang/String;Ljava/lang/String;Landroid/content/BroadcastReceiver;Landroid/os/Handler;ILjava/lang/String;Landroid/os/Bundle;)V");
    }

    public abstract void sendOrderedBroadcastAsUser(android.content.Intent arg0, android.os.UserHandle arg1, java.lang.String arg2, android.content.BroadcastReceiver arg3, android.os.Handler arg4, int arg5, java.lang.String arg6, android.os.Bundle arg7);

    public abstract void sendStickyBroadcast(android.content.Intent arg0);

    public void sendStickyBroadcast(android.content.Intent arg0, android.os.Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->sendStickyBroadcast(Landroid/content/Intent;Landroid/os/Bundle;)V");
    }

    public abstract void sendStickyBroadcastAsUser(android.content.Intent arg0, android.os.UserHandle arg1);

    public abstract void sendStickyOrderedBroadcast(android.content.Intent arg0, android.content.BroadcastReceiver arg1, android.os.Handler arg2, int arg3, java.lang.String arg4, android.os.Bundle arg5);

    public abstract void sendStickyOrderedBroadcastAsUser(android.content.Intent arg0, android.os.UserHandle arg1, android.content.BroadcastReceiver arg2, android.os.Handler arg3, int arg4, java.lang.String arg5, android.os.Bundle arg6);

    public abstract void setTheme(int arg0);

    public abstract void setWallpaper(android.graphics.Bitmap arg0);

    public abstract void setWallpaper(java.io.InputStream arg0);

    public abstract void startActivities(android.content.Intent[] arg0);

    public abstract void startActivities(android.content.Intent[] arg0, android.os.Bundle arg1);

    public abstract void startActivity(android.content.Intent arg0);

    public abstract void startActivity(android.content.Intent arg0, android.os.Bundle arg1);

    public abstract android.content.ComponentName startForegroundService(android.content.Intent arg0);

    public abstract boolean startInstrumentation(android.content.ComponentName arg0, java.lang.String arg1, android.os.Bundle arg2);

    public abstract void startIntentSender(android.content.IntentSender arg0, android.content.Intent arg1, int arg2, int arg3, int arg4);

    public abstract void startIntentSender(android.content.IntentSender arg0, android.content.Intent arg1, int arg2, int arg3, int arg4, android.os.Bundle arg5);

    public abstract android.content.ComponentName startService(android.content.Intent arg0);

    public abstract boolean stopService(android.content.Intent arg0);

    public abstract void unbindService(android.content.ServiceConnection arg0);

    public void unregisterComponentCallbacks(android.content.ComponentCallbacks arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->unregisterComponentCallbacks(Landroid/content/ComponentCallbacks;)V");
    }

    public void unregisterDeviceIdChangeListener(java.util.function.IntConsumer arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->unregisterDeviceIdChangeListener(Ljava/util/function/IntConsumer;)V");
    }

    public abstract void unregisterReceiver(android.content.BroadcastReceiver arg0);

    public void updateServiceGroup(android.content.ServiceConnection arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context;->updateServiceGroup(Landroid/content/ServiceConnection;II)V");
    }
        public static class BindServiceFlags {

        public static android.content.Context.BindServiceFlags of(long arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Context$BindServiceFlags;->of(J)Landroid/content/Context$BindServiceFlags;");
        }
        }
    }
