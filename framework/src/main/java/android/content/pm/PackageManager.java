package android.content.pm;

    public abstract class PackageManager {

    public static final int CERT_INPUT_RAW_X509 = 0;
    public static final int CERT_INPUT_SHA256 = 1;
    public static final int COMPONENT_ENABLED_STATE_DEFAULT = 0;
    public static final int COMPONENT_ENABLED_STATE_DISABLED = 2;
    public static final int COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED = 4;
    public static final int COMPONENT_ENABLED_STATE_DISABLED_USER = 3;
    public static final int COMPONENT_ENABLED_STATE_ENABLED = 1;
    public static final int DELETE_ARCHIVE = 16;
    public static final int DONT_KILL_APP = 1;
    public static final java.lang.String EXTRA_VERIFICATION_ID = "android.content.pm.extra.VERIFICATION_ID";
    public static final java.lang.String EXTRA_VERIFICATION_RESULT = "android.content.pm.extra.VERIFICATION_RESULT";
    public static final java.lang.String FEATURE_ACTIVITIES_ON_SECONDARY_DISPLAYS = "android.software.activities_on_secondary_displays";
    public static final java.lang.String FEATURE_APP_WIDGETS = "android.software.app_widgets";
    public static final java.lang.String FEATURE_AUDIO_LOW_LATENCY = "android.hardware.audio.low_latency";
    public static final java.lang.String FEATURE_AUDIO_OUTPUT = "android.hardware.audio.output";
    public static final java.lang.String FEATURE_AUDIO_PRO = "android.hardware.audio.pro";
    public static final java.lang.String FEATURE_AUDIO_SPATIAL_HEADTRACKING_LOW_LATENCY = "android.hardware.audio.spatial.headtracking.low_latency";
    public static final java.lang.String FEATURE_AUTOFILL = "android.software.autofill";
    public static final java.lang.String FEATURE_AUTOMOTIVE = "android.hardware.type.automotive";
    public static final java.lang.String FEATURE_BACKUP = "android.software.backup";
    public static final java.lang.String FEATURE_BLUETOOTH = "android.hardware.bluetooth";
    public static final java.lang.String FEATURE_BLUETOOTH_LE = "android.hardware.bluetooth_le";
    public static final java.lang.String FEATURE_BLUETOOTH_LE_CHANNEL_SOUNDING = "android.hardware.bluetooth_le.channel_sounding";
    public static final java.lang.String FEATURE_CAMERA = "android.hardware.camera";
    public static final java.lang.String FEATURE_CAMERA_ANY = "android.hardware.camera.any";
    public static final java.lang.String FEATURE_CAMERA_AR = "android.hardware.camera.ar";
    public static final java.lang.String FEATURE_CAMERA_AUTOFOCUS = "android.hardware.camera.autofocus";
    public static final java.lang.String FEATURE_CAMERA_CAPABILITY_MANUAL_POST_PROCESSING = "android.hardware.camera.capability.manual_post_processing";
    public static final java.lang.String FEATURE_CAMERA_CAPABILITY_MANUAL_SENSOR = "android.hardware.camera.capability.manual_sensor";
    public static final java.lang.String FEATURE_CAMERA_CAPABILITY_RAW = "android.hardware.camera.capability.raw";
    public static final java.lang.String FEATURE_CAMERA_CONCURRENT = "android.hardware.camera.concurrent";
    public static final java.lang.String FEATURE_CAMERA_EXTERNAL = "android.hardware.camera.external";
    public static final java.lang.String FEATURE_CAMERA_FLASH = "android.hardware.camera.flash";
    public static final java.lang.String FEATURE_CAMERA_FRONT = "android.hardware.camera.front";
    public static final java.lang.String FEATURE_CAMERA_LEVEL_FULL = "android.hardware.camera.level.full";
    public static final java.lang.String FEATURE_CANT_SAVE_STATE = "android.software.cant_save_state";
    public static final java.lang.String FEATURE_COMPANION_DEVICE_SETUP = "android.software.companion_device_setup";
    public static final java.lang.String FEATURE_CONNECTION_SERVICE = "android.software.connectionservice";
    public static final java.lang.String FEATURE_CONSUMER_IR = "android.hardware.consumerir";
    public static final java.lang.String FEATURE_CONTROLS = "android.software.controls";
    public static final java.lang.String FEATURE_CREDENTIALS = "android.software.credentials";
    public static final java.lang.String FEATURE_DEVICE_ADMIN = "android.software.device_admin";
    public static final java.lang.String FEATURE_DEVICE_LOCK = "android.software.device_lock";
    public static final java.lang.String FEATURE_EMBEDDED = "android.hardware.type.embedded";
    public static final java.lang.String FEATURE_ETHERNET = "android.hardware.ethernet";
    public static final java.lang.String FEATURE_EXPANDED_PICTURE_IN_PICTURE = "android.software.expanded_picture_in_picture";
    public static final java.lang.String FEATURE_FACE = "android.hardware.biometrics.face";
    public static final java.lang.String FEATURE_FAKETOUCH = "android.hardware.faketouch";
    public static final java.lang.String FEATURE_FAKETOUCH_MULTITOUCH_DISTINCT = "android.hardware.faketouch.multitouch.distinct";
    public static final java.lang.String FEATURE_FAKETOUCH_MULTITOUCH_JAZZHAND = "android.hardware.faketouch.multitouch.jazzhand";
    public static final java.lang.String FEATURE_FINGERPRINT = "android.hardware.fingerprint";
    public static final java.lang.String FEATURE_FREEFORM_WINDOW_MANAGEMENT = "android.software.freeform_window_management";
    public static final java.lang.String FEATURE_GAMEPAD = "android.hardware.gamepad";
    public static final java.lang.String FEATURE_HARDWARE_KEYSTORE = "android.hardware.hardware_keystore";
    public static final java.lang.String FEATURE_HIFI_SENSORS = "android.hardware.sensor.hifi_sensors";
    public static final java.lang.String FEATURE_HOME_SCREEN = "android.software.home_screen";
    public static final java.lang.String FEATURE_IDENTITY_CREDENTIAL_HARDWARE = "android.hardware.identity_credential";
    public static final java.lang.String FEATURE_IDENTITY_CREDENTIAL_HARDWARE_DIRECT_ACCESS = "android.hardware.identity_credential_direct_access";
    public static final java.lang.String FEATURE_INPUT_METHODS = "android.software.input_methods";
    public static final java.lang.String FEATURE_IPSEC_TUNNELS = "android.software.ipsec_tunnels";
    public static final java.lang.String FEATURE_IPSEC_TUNNEL_MIGRATION = "android.software.ipsec_tunnel_migration";
    public static final java.lang.String FEATURE_IRIS = "android.hardware.biometrics.iris";
    public static final java.lang.String FEATURE_KEYSTORE_APP_ATTEST_KEY = "android.hardware.keystore.app_attest_key";
    public static final java.lang.String FEATURE_KEYSTORE_LIMITED_USE_KEY = "android.hardware.keystore.limited_use_key";
    public static final java.lang.String FEATURE_KEYSTORE_SINGLE_USE_KEY = "android.hardware.keystore.single_use_key";
    public static final java.lang.String FEATURE_LEANBACK = "android.software.leanback";
    public static final java.lang.String FEATURE_LEANBACK_ONLY = "android.software.leanback_only";
    public static final java.lang.String FEATURE_LIVE_TV = "android.software.live_tv";
    public static final java.lang.String FEATURE_LIVE_WALLPAPER = "android.software.live_wallpaper";
    public static final java.lang.String FEATURE_LOCATION = "android.hardware.location";
    public static final java.lang.String FEATURE_LOCATION_GPS = "android.hardware.location.gps";
    public static final java.lang.String FEATURE_LOCATION_NETWORK = "android.hardware.location.network";
    public static final java.lang.String FEATURE_MANAGED_USERS = "android.software.managed_users";
    public static final java.lang.String FEATURE_MICROPHONE = "android.hardware.microphone";
    public static final java.lang.String FEATURE_MIDI = "android.software.midi";
    public static final java.lang.String FEATURE_NFC = "android.hardware.nfc";
    public static final java.lang.String FEATURE_NFC_BEAM = "android.sofware.nfc.beam";
    public static final java.lang.String FEATURE_NFC_HOST_CARD_EMULATION = "android.hardware.nfc.hce";
    public static final java.lang.String FEATURE_NFC_HOST_CARD_EMULATION_NFCF = "android.hardware.nfc.hcef";
    public static final java.lang.String FEATURE_NFC_OFF_HOST_CARD_EMULATION_ESE = "android.hardware.nfc.ese";
    public static final java.lang.String FEATURE_NFC_OFF_HOST_CARD_EMULATION_UICC = "android.hardware.nfc.uicc";
    public static final java.lang.String FEATURE_OPENGLES_DEQP_LEVEL = "android.software.opengles.deqp.level";
    public static final java.lang.String FEATURE_OPENGLES_EXTENSION_PACK = "android.hardware.opengles.aep";
    public static final java.lang.String FEATURE_PC = "android.hardware.type.pc";
    public static final java.lang.String FEATURE_PICTURE_IN_PICTURE = "android.software.picture_in_picture";
    public static final java.lang.String FEATURE_PRINTING = "android.software.print";
    public static final java.lang.String FEATURE_RAM_LOW = "android.hardware.ram.low";
    public static final java.lang.String FEATURE_RAM_NORMAL = "android.hardware.ram.normal";
    public static final java.lang.String FEATURE_SCREEN_LANDSCAPE = "android.hardware.screen.landscape";
    public static final java.lang.String FEATURE_SCREEN_PORTRAIT = "android.hardware.screen.portrait";
    public static final java.lang.String FEATURE_SECURELY_REMOVES_USERS = "android.software.securely_removes_users";
    public static final java.lang.String FEATURE_SECURE_LOCK_SCREEN = "android.software.secure_lock_screen";
    public static final java.lang.String FEATURE_SECURITY_MODEL_COMPATIBLE = "android.hardware.security.model.compatible";
    public static final java.lang.String FEATURE_SENSOR_ACCELEROMETER = "android.hardware.sensor.accelerometer";
    public static final java.lang.String FEATURE_SENSOR_ACCELEROMETER_LIMITED_AXES = "android.hardware.sensor.accelerometer_limited_axes";
    public static final java.lang.String FEATURE_SENSOR_ACCELEROMETER_LIMITED_AXES_UNCALIBRATED = "android.hardware.sensor.accelerometer_limited_axes_uncalibrated";
    public static final java.lang.String FEATURE_SENSOR_AMBIENT_TEMPERATURE = "android.hardware.sensor.ambient_temperature";
    public static final java.lang.String FEATURE_SENSOR_BAROMETER = "android.hardware.sensor.barometer";
    public static final java.lang.String FEATURE_SENSOR_COMPASS = "android.hardware.sensor.compass";
    public static final java.lang.String FEATURE_SENSOR_DYNAMIC_HEAD_TRACKER = "android.hardware.sensor.dynamic.head_tracker";
    public static final java.lang.String FEATURE_SENSOR_GYROSCOPE = "android.hardware.sensor.gyroscope";
    public static final java.lang.String FEATURE_SENSOR_GYROSCOPE_LIMITED_AXES = "android.hardware.sensor.gyroscope_limited_axes";
    public static final java.lang.String FEATURE_SENSOR_GYROSCOPE_LIMITED_AXES_UNCALIBRATED = "android.hardware.sensor.gyroscope_limited_axes_uncalibrated";
    public static final java.lang.String FEATURE_SENSOR_HEADING = "android.hardware.sensor.heading";
    public static final java.lang.String FEATURE_SENSOR_HEART_RATE = "android.hardware.sensor.heartrate";
    public static final java.lang.String FEATURE_SENSOR_HEART_RATE_ECG = "android.hardware.sensor.heartrate.ecg";
    public static final java.lang.String FEATURE_SENSOR_HINGE_ANGLE = "android.hardware.sensor.hinge_angle";
    public static final java.lang.String FEATURE_SENSOR_LIGHT = "android.hardware.sensor.light";
    public static final java.lang.String FEATURE_SENSOR_PROXIMITY = "android.hardware.sensor.proximity";
    public static final java.lang.String FEATURE_SENSOR_RELATIVE_HUMIDITY = "android.hardware.sensor.relative_humidity";
    public static final java.lang.String FEATURE_SENSOR_STEP_COUNTER = "android.hardware.sensor.stepcounter";
    public static final java.lang.String FEATURE_SENSOR_STEP_DETECTOR = "android.hardware.sensor.stepdetector";
    public static final java.lang.String FEATURE_SE_OMAPI_ESE = "android.hardware.se.omapi.ese";
    public static final java.lang.String FEATURE_SE_OMAPI_SD = "android.hardware.se.omapi.sd";
    public static final java.lang.String FEATURE_SE_OMAPI_UICC = "android.hardware.se.omapi.uicc";
    public static final java.lang.String FEATURE_SIP = "android.software.sip";
    public static final java.lang.String FEATURE_SIP_VOIP = "android.software.sip.voip";
    public static final java.lang.String FEATURE_STRONGBOX_KEYSTORE = "android.hardware.strongbox_keystore";
    public static final java.lang.String FEATURE_TELECOM = "android.software.telecom";
    public static final java.lang.String FEATURE_TELEPHONY = "android.hardware.telephony";
    public static final java.lang.String FEATURE_TELEPHONY_CALLING = "android.hardware.telephony.calling";
    public static final java.lang.String FEATURE_TELEPHONY_CDMA = "android.hardware.telephony.cdma";
    public static final java.lang.String FEATURE_TELEPHONY_DATA = "android.hardware.telephony.data";
    public static final java.lang.String FEATURE_TELEPHONY_EUICC = "android.hardware.telephony.euicc";
    public static final java.lang.String FEATURE_TELEPHONY_EUICC_MEP = "android.hardware.telephony.euicc.mep";
    public static final java.lang.String FEATURE_TELEPHONY_GSM = "android.hardware.telephony.gsm";
    public static final java.lang.String FEATURE_TELEPHONY_IMS = "android.hardware.telephony.ims";
    public static final java.lang.String FEATURE_TELEPHONY_MBMS = "android.hardware.telephony.mbms";
    public static final java.lang.String FEATURE_TELEPHONY_MESSAGING = "android.hardware.telephony.messaging";
    public static final java.lang.String FEATURE_TELEPHONY_RADIO_ACCESS = "android.hardware.telephony.radio.access";
    public static final java.lang.String FEATURE_TELEPHONY_SUBSCRIPTION = "android.hardware.telephony.subscription";
    public static final java.lang.String FEATURE_TELEVISION = "android.hardware.type.television";
    public static final java.lang.String FEATURE_THREAD_NETWORK = "android.hardware.thread_network";
    public static final java.lang.String FEATURE_TOUCHSCREEN = "android.hardware.touchscreen";
    public static final java.lang.String FEATURE_TOUCHSCREEN_MULTITOUCH = "android.hardware.touchscreen.multitouch";
    public static final java.lang.String FEATURE_TOUCHSCREEN_MULTITOUCH_DISTINCT = "android.hardware.touchscreen.multitouch.distinct";
    public static final java.lang.String FEATURE_TOUCHSCREEN_MULTITOUCH_JAZZHAND = "android.hardware.touchscreen.multitouch.jazzhand";
    public static final java.lang.String FEATURE_USB_ACCESSORY = "android.hardware.usb.accessory";
    public static final java.lang.String FEATURE_USB_HOST = "android.hardware.usb.host";
    public static final java.lang.String FEATURE_UWB = "android.hardware.uwb";
    public static final java.lang.String FEATURE_VERIFIED_BOOT = "android.software.verified_boot";
    public static final java.lang.String FEATURE_VR_HEADTRACKING = "android.hardware.vr.headtracking";
    public static final java.lang.String FEATURE_VR_MODE = "android.software.vr.mode";
    public static final java.lang.String FEATURE_VR_MODE_HIGH_PERFORMANCE = "android.hardware.vr.high_performance";
    public static final java.lang.String FEATURE_VULKAN_DEQP_LEVEL = "android.software.vulkan.deqp.level";
    public static final java.lang.String FEATURE_VULKAN_HARDWARE_COMPUTE = "android.hardware.vulkan.compute";
    public static final java.lang.String FEATURE_VULKAN_HARDWARE_LEVEL = "android.hardware.vulkan.level";
    public static final java.lang.String FEATURE_VULKAN_HARDWARE_VERSION = "android.hardware.vulkan.version";
    public static final java.lang.String FEATURE_WALLET_LOCATION_BASED_SUGGESTIONS = "android.software.wallet_location_based_suggestions";
    public static final java.lang.String FEATURE_WATCH = "android.hardware.type.watch";
    public static final java.lang.String FEATURE_WEBVIEW = "android.software.webview";
    public static final java.lang.String FEATURE_WIFI = "android.hardware.wifi";
    public static final java.lang.String FEATURE_WIFI_AWARE = "android.hardware.wifi.aware";
    public static final java.lang.String FEATURE_WIFI_DIRECT = "android.hardware.wifi.direct";
    public static final java.lang.String FEATURE_WIFI_PASSPOINT = "android.hardware.wifi.passpoint";
    public static final java.lang.String FEATURE_WIFI_RTT = "android.hardware.wifi.rtt";
    public static final java.lang.String FEATURE_WINDOW_MAGNIFICATION = "android.software.window_magnification";
    public static final int FLAG_PERMISSION_WHITELIST_INSTALLER = 2;
    public static final int FLAG_PERMISSION_WHITELIST_SYSTEM = 1;
    public static final int FLAG_PERMISSION_WHITELIST_UPGRADE = 4;
    public static final int GET_ACTIVITIES = 1;
    public static final int GET_ATTRIBUTIONS = -2147483648;
    public static final long GET_ATTRIBUTIONS_LONG = 2147483648L;
    public static final int GET_CONFIGURATIONS = 16384;
    public static final int GET_DISABLED_COMPONENTS = 512;
    public static final int GET_DISABLED_UNTIL_USED_COMPONENTS = 32768;
    public static final int GET_GIDS = 256;
    public static final int GET_INSTRUMENTATION = 16;
    public static final int GET_INTENT_FILTERS = 32;
    public static final int GET_META_DATA = 128;
    public static final int GET_PERMISSIONS = 4096;
    public static final int GET_PROVIDERS = 8;
    public static final int GET_RECEIVERS = 2;
    public static final int GET_RESOLVED_FILTER = 64;
    public static final int GET_SERVICES = 4;
    public static final int GET_SHARED_LIBRARY_FILES = 1024;
    public static final int GET_SIGNATURES = 64;
    public static final int GET_SIGNING_CERTIFICATES = 134217728;
    public static final int GET_UNINSTALLED_PACKAGES = 8192;
    public static final int GET_URI_PERMISSION_PATTERNS = 2048;
    public static final int INSTALL_REASON_DEVICE_RESTORE = 2;
    public static final int INSTALL_REASON_DEVICE_SETUP = 3;
    public static final int INSTALL_REASON_POLICY = 1;
    public static final int INSTALL_REASON_UNKNOWN = 0;
    public static final int INSTALL_REASON_USER = 4;
    public static final int INSTALL_SCENARIO_BULK = 2;
    public static final int INSTALL_SCENARIO_BULK_SECONDARY = 3;
    public static final int INSTALL_SCENARIO_DEFAULT = 0;
    public static final int INSTALL_SCENARIO_FAST = 1;
    public static final int MATCH_ALL = 131072;
    public static final int MATCH_APEX = 1073741824;
    public static final long MATCH_ARCHIVED_PACKAGES = 4294967296L;
    public static final int MATCH_DEFAULT_ONLY = 65536;
    public static final int MATCH_DIRECT_BOOT_AUTO = 268435456;
    public static final int MATCH_DIRECT_BOOT_AWARE = 524288;
    public static final int MATCH_DIRECT_BOOT_UNAWARE = 262144;
    public static final int MATCH_DISABLED_COMPONENTS = 512;
    public static final int MATCH_DISABLED_UNTIL_USED_COMPONENTS = 32768;
    public static final int MATCH_SYSTEM_ONLY = 1048576;
    public static final int MATCH_UNINSTALLED_PACKAGES = 8192;
    public static final long MAXIMUM_VERIFICATION_TIMEOUT = 3600000L;
    public static final int PERMISSION_DENIED = -1;
    public static final int PERMISSION_GRANTED = 0;
    public static final java.lang.String PROPERTY_COMPAT_OVERRIDE_LANDSCAPE_TO_PORTRAIT = "android.camera.PROPERTY_COMPAT_OVERRIDE_LANDSCAPE_TO_PORTRAIT";
    public static final java.lang.String PROPERTY_MEDIA_CAPABILITIES = "android.media.PROPERTY_MEDIA_CAPABILITIES";
    public static final java.lang.String PROPERTY_SELF_CERTIFIED_NETWORK_CAPABILITIES = "android.net.PROPERTY_SELF_CERTIFIED_NETWORK_CAPABILITIES";
    public static final java.lang.String PROPERTY_SPECIAL_USE_FGS_SUBTYPE = "android.app.PROPERTY_SPECIAL_USE_FGS_SUBTYPE";
    public static final java.lang.String PROPERTY_USE_RESTRICTED_BACKUP_MODE = "android.app.backup.PROPERTY_USE_RESTRICTED_BACKUP_MODE";
    public static final int SIGNATURE_FIRST_NOT_SIGNED = -1;
    public static final int SIGNATURE_MATCH = 0;
    public static final int SIGNATURE_NEITHER_SIGNED = 1;
    public static final int SIGNATURE_NO_MATCH = -3;
    public static final int SIGNATURE_SECOND_NOT_SIGNED = -2;
    public static final int SIGNATURE_UNKNOWN_PACKAGE = -4;
    public static final int SYNCHRONOUS = 2;
    public static java.util.List TRUST_ALL;
    public static java.util.List TRUST_NONE;
    public static final int VERIFICATION_ALLOW = 1;
    public static final int VERIFICATION_REJECT = -1;
    public static final int VERSION_CODE_HIGHEST = -1;

    public PackageManager() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;-><init>()V");
    }

    public abstract void addPackageToPreferred(java.lang.String arg0);

    public abstract boolean addPermission(android.content.pm.PermissionInfo arg0);

    public abstract boolean addPermissionAsync(android.content.pm.PermissionInfo arg0);

    public abstract void addPreferredActivity(android.content.IntentFilter arg0, int arg1, android.content.ComponentName[] arg2, android.content.ComponentName arg3);

    public boolean addWhitelistedRestrictedPermission(java.lang.String arg0, java.lang.String arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->addWhitelistedRestrictedPermission(Ljava/lang/String;Ljava/lang/String;I)Z");
    }

    public boolean canPackageQuery(java.lang.String arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->canPackageQuery(Ljava/lang/String;Ljava/lang/String;)Z");
    }

    public boolean[] canPackageQuery(java.lang.String arg0, java.lang.String[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->canPackageQuery(Ljava/lang/String;[Ljava/lang/String;)[Z");
    }

    public abstract boolean canRequestPackageInstalls();

    public abstract java.lang.String[] canonicalToCurrentPackageNames(java.lang.String[] arg0);

    public abstract int checkPermission(java.lang.String arg0, java.lang.String arg1);

    public abstract int checkSignatures(int arg0, int arg1);

    public abstract int checkSignatures(java.lang.String arg0, java.lang.String arg1);

    public abstract void clearInstantAppCookie();

    public abstract void clearPackagePreferredActivities(java.lang.String arg0);

    public abstract java.lang.String[] currentToCanonicalPackageNames(java.lang.String[] arg0);

    public abstract void extendVerificationTimeout(int arg0, int arg1, long arg2);

    public abstract android.graphics.drawable.Drawable getActivityBanner(android.content.ComponentName arg0);

    public abstract android.graphics.drawable.Drawable getActivityBanner(android.content.Intent arg0);

    public abstract android.graphics.drawable.Drawable getActivityIcon(android.content.ComponentName arg0);

    public abstract android.graphics.drawable.Drawable getActivityIcon(android.content.Intent arg0);

    public android.content.pm.ActivityInfo getActivityInfo(android.content.ComponentName arg0, android.content.pm.PackageManager.ComponentInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getActivityInfo(Landroid/content/ComponentName;Landroid/content/pm/PackageManager$ComponentInfoFlags;)Landroid/content/pm/ActivityInfo;");
    }

    public abstract android.content.pm.ActivityInfo getActivityInfo(android.content.ComponentName arg0, int arg1);

    public abstract android.graphics.drawable.Drawable getActivityLogo(android.content.ComponentName arg0);

    public abstract android.graphics.drawable.Drawable getActivityLogo(android.content.Intent arg0);

    public abstract java.util.List<android.content.pm.PermissionGroupInfo> getAllPermissionGroups(int arg0);

    public abstract android.graphics.drawable.Drawable getApplicationBanner(android.content.pm.ApplicationInfo arg0);

    public abstract android.graphics.drawable.Drawable getApplicationBanner(java.lang.String arg0);

    public abstract int getApplicationEnabledSetting(java.lang.String arg0);

    public abstract android.graphics.drawable.Drawable getApplicationIcon(android.content.pm.ApplicationInfo arg0);

    public abstract android.graphics.drawable.Drawable getApplicationIcon(java.lang.String arg0);

    public android.content.pm.ApplicationInfo getApplicationInfo(java.lang.String arg0, android.content.pm.PackageManager.ApplicationInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getApplicationInfo(Ljava/lang/String;Landroid/content/pm/PackageManager$ApplicationInfoFlags;)Landroid/content/pm/ApplicationInfo;");
    }

    public abstract android.content.pm.ApplicationInfo getApplicationInfo(java.lang.String arg0, int arg1);

    public abstract java.lang.CharSequence getApplicationLabel(android.content.pm.ApplicationInfo arg0);

    public abstract android.graphics.drawable.Drawable getApplicationLogo(android.content.pm.ApplicationInfo arg0);

    public abstract android.graphics.drawable.Drawable getApplicationLogo(java.lang.String arg0);

    public android.content.pm.ArchivedPackageInfo getArchivedPackage(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getArchivedPackage(Ljava/lang/String;)Landroid/content/pm/ArchivedPackageInfo;");
    }

    public java.lang.CharSequence getBackgroundPermissionOptionLabel() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getBackgroundPermissionOptionLabel()Ljava/lang/CharSequence;");
    }

    public abstract android.content.pm.ChangedPackages getChangedPackages(int arg0);

    public abstract int getComponentEnabledSetting(android.content.ComponentName arg0);

    public abstract android.graphics.drawable.Drawable getDefaultActivityIcon();

    public abstract android.graphics.drawable.Drawable getDrawable(java.lang.String arg0, int arg1, android.content.pm.ApplicationInfo arg2);

    public void getGroupOfPlatformPermission(java.lang.String arg0, java.util.concurrent.Executor arg1, java.util.function.Consumer<java.lang.String> arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getGroupOfPlatformPermission(Ljava/lang/String;Ljava/util/concurrent/Executor;Ljava/util/function/Consumer;)V");
    }

    public android.content.pm.InstallSourceInfo getInstallSourceInfo(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getInstallSourceInfo(Ljava/lang/String;)Landroid/content/pm/InstallSourceInfo;");
    }

    public java.util.List<android.content.pm.ApplicationInfo> getInstalledApplications(android.content.pm.PackageManager.ApplicationInfoFlags arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getInstalledApplications(Landroid/content/pm/PackageManager$ApplicationInfoFlags;)Ljava/util/List;");
    }

    public abstract java.util.List<android.content.pm.ApplicationInfo> getInstalledApplications(int arg0);

    public java.util.List<android.content.pm.ModuleInfo> getInstalledModules(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getInstalledModules(I)Ljava/util/List;");
    }

    public java.util.List<android.content.pm.PackageInfo> getInstalledPackages(android.content.pm.PackageManager.PackageInfoFlags arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getInstalledPackages(Landroid/content/pm/PackageManager$PackageInfoFlags;)Ljava/util/List;");
    }

    public abstract java.util.List<android.content.pm.PackageInfo> getInstalledPackages(int arg0);

    public abstract java.lang.String getInstallerPackageName(java.lang.String arg0);

    public abstract byte[] getInstantAppCookie();

    public abstract int getInstantAppCookieMaxBytes();

    public abstract android.content.pm.InstrumentationInfo getInstrumentationInfo(android.content.ComponentName arg0, int arg1);

    public abstract android.content.Intent getLaunchIntentForPackage(java.lang.String arg0);

    public android.content.IntentSender getLaunchIntentSenderForPackage(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getLaunchIntentSenderForPackage(Ljava/lang/String;)Landroid/content/IntentSender;");
    }

    public abstract android.content.Intent getLeanbackLaunchIntentForPackage(java.lang.String arg0);

    public java.util.Set<java.lang.String> getMimeGroup(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getMimeGroup(Ljava/lang/String;)Ljava/util/Set;");
    }

    public android.content.pm.ModuleInfo getModuleInfo(java.lang.String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getModuleInfo(Ljava/lang/String;I)Landroid/content/pm/ModuleInfo;");
    }

    public abstract java.lang.String getNameForUid(int arg0);

    public android.content.pm.PackageInfo getPackageArchiveInfo(java.lang.String arg0, android.content.pm.PackageManager.PackageInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getPackageArchiveInfo(Ljava/lang/String;Landroid/content/pm/PackageManager$PackageInfoFlags;)Landroid/content/pm/PackageInfo;");
    }

    public android.content.pm.PackageInfo getPackageArchiveInfo(java.lang.String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getPackageArchiveInfo(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
    }

    public abstract int[] getPackageGids(java.lang.String arg0);

    public int[] getPackageGids(java.lang.String arg0, android.content.pm.PackageManager.PackageInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getPackageGids(Ljava/lang/String;Landroid/content/pm/PackageManager$PackageInfoFlags;)[I");
    }

    public abstract int[] getPackageGids(java.lang.String arg0, int arg1);

    public android.content.pm.PackageInfo getPackageInfo(android.content.pm.VersionedPackage arg0, android.content.pm.PackageManager.PackageInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getPackageInfo(Landroid/content/pm/VersionedPackage;Landroid/content/pm/PackageManager$PackageInfoFlags;)Landroid/content/pm/PackageInfo;");
    }

    public abstract android.content.pm.PackageInfo getPackageInfo(android.content.pm.VersionedPackage arg0, int arg1);

    public android.content.pm.PackageInfo getPackageInfo(java.lang.String arg0, android.content.pm.PackageManager.PackageInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getPackageInfo(Ljava/lang/String;Landroid/content/pm/PackageManager$PackageInfoFlags;)Landroid/content/pm/PackageInfo;");
    }

    public abstract android.content.pm.PackageInfo getPackageInfo(java.lang.String arg0, int arg1);

    public abstract android.content.pm.PackageInstaller getPackageInstaller();

    public int getPackageUid(java.lang.String arg0, android.content.pm.PackageManager.PackageInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getPackageUid(Ljava/lang/String;Landroid/content/pm/PackageManager$PackageInfoFlags;)I");
    }

    public abstract int getPackageUid(java.lang.String arg0, int arg1);

    public abstract java.lang.String[] getPackagesForUid(int arg0);

    public java.util.List<android.content.pm.PackageInfo> getPackagesHoldingPermissions(java.lang.String[] arg0, android.content.pm.PackageManager.PackageInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getPackagesHoldingPermissions([Ljava/lang/String;Landroid/content/pm/PackageManager$PackageInfoFlags;)Ljava/util/List;");
    }

    public abstract java.util.List<android.content.pm.PackageInfo> getPackagesHoldingPermissions(java.lang.String[] arg0, int arg1);

    public abstract android.content.pm.PermissionGroupInfo getPermissionGroupInfo(java.lang.String arg0, int arg1);

    public abstract android.content.pm.PermissionInfo getPermissionInfo(java.lang.String arg0, int arg1);

    public void getPlatformPermissionsForGroup(java.lang.String arg0, java.util.concurrent.Executor arg1, java.util.function.Consumer<java.util.List<java.lang.String>> arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getPlatformPermissionsForGroup(Ljava/lang/String;Ljava/util/concurrent/Executor;Ljava/util/function/Consumer;)V");
    }

    public abstract int getPreferredActivities(java.util.List<android.content.IntentFilter> arg0, java.util.List<android.content.ComponentName> arg1, java.lang.String arg2);

    public abstract java.util.List<android.content.pm.PackageInfo> getPreferredPackages(int arg0);

    public android.content.pm.PackageManager.Property getProperty(java.lang.String arg0, android.content.ComponentName arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getProperty(Ljava/lang/String;Landroid/content/ComponentName;)Landroid/content/pm/PackageManager$Property;");
    }

    public android.content.pm.PackageManager.Property getProperty(java.lang.String arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getProperty(Ljava/lang/String;Ljava/lang/String;)Landroid/content/pm/PackageManager$Property;");
    }

    public android.content.pm.ProviderInfo getProviderInfo(android.content.ComponentName arg0, android.content.pm.PackageManager.ComponentInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getProviderInfo(Landroid/content/ComponentName;Landroid/content/pm/PackageManager$ComponentInfoFlags;)Landroid/content/pm/ProviderInfo;");
    }

    public abstract android.content.pm.ProviderInfo getProviderInfo(android.content.ComponentName arg0, int arg1);

    public android.content.pm.ActivityInfo getReceiverInfo(android.content.ComponentName arg0, android.content.pm.PackageManager.ComponentInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getReceiverInfo(Landroid/content/ComponentName;Landroid/content/pm/PackageManager$ComponentInfoFlags;)Landroid/content/pm/ActivityInfo;");
    }

    public abstract android.content.pm.ActivityInfo getReceiverInfo(android.content.ComponentName arg0, int arg1);

    public abstract android.content.res.Resources getResourcesForActivity(android.content.ComponentName arg0);

    public abstract android.content.res.Resources getResourcesForApplication(android.content.pm.ApplicationInfo arg0);

    public android.content.res.Resources getResourcesForApplication(android.content.pm.ApplicationInfo arg0, android.content.res.Configuration arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getResourcesForApplication(Landroid/content/pm/ApplicationInfo;Landroid/content/res/Configuration;)Landroid/content/res/Resources;");
    }

    public abstract android.content.res.Resources getResourcesForApplication(java.lang.String arg0);

    public android.content.pm.ServiceInfo getServiceInfo(android.content.ComponentName arg0, android.content.pm.PackageManager.ComponentInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getServiceInfo(Landroid/content/ComponentName;Landroid/content/pm/PackageManager$ComponentInfoFlags;)Landroid/content/pm/ServiceInfo;");
    }

    public abstract android.content.pm.ServiceInfo getServiceInfo(android.content.ComponentName arg0, int arg1);

    public java.util.List<android.content.pm.SharedLibraryInfo> getSharedLibraries(android.content.pm.PackageManager.PackageInfoFlags arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getSharedLibraries(Landroid/content/pm/PackageManager$PackageInfoFlags;)Ljava/util/List;");
    }

    public abstract java.util.List<android.content.pm.SharedLibraryInfo> getSharedLibraries(int arg0);

    public android.os.Bundle getSuspendedPackageAppExtras() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getSuspendedPackageAppExtras()Landroid/os/Bundle;");
    }

    public boolean getSyntheticAppDetailsActivityEnabled(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getSyntheticAppDetailsActivityEnabled(Ljava/lang/String;)Z");
    }

    public abstract android.content.pm.FeatureInfo[] getSystemAvailableFeatures();

    public abstract java.lang.String[] getSystemSharedLibraryNames();

    public int getTargetSdkVersion(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getTargetSdkVersion(Ljava/lang/String;)I");
    }

    public abstract java.lang.CharSequence getText(java.lang.String arg0, int arg1, android.content.pm.ApplicationInfo arg2);

    public abstract android.graphics.drawable.Drawable getUserBadgedDrawableForDensity(android.graphics.drawable.Drawable arg0, android.os.UserHandle arg1, android.graphics.Rect arg2, int arg3);

    public abstract android.graphics.drawable.Drawable getUserBadgedIcon(android.graphics.drawable.Drawable arg0, android.os.UserHandle arg1);

    public abstract java.lang.CharSequence getUserBadgedLabel(java.lang.CharSequence arg0, android.os.UserHandle arg1);

    public static android.content.pm.SigningInfo getVerifiedSigningInfo(java.lang.String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getVerifiedSigningInfo(Ljava/lang/String;I)Landroid/content/pm/SigningInfo;");
    }

    public java.util.Set<java.lang.String> getWhitelistedRestrictedPermissions(java.lang.String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getWhitelistedRestrictedPermissions(Ljava/lang/String;I)Ljava/util/Set;");
    }

    public abstract android.content.res.XmlResourceParser getXml(java.lang.String arg0, int arg1, android.content.pm.ApplicationInfo arg2);

    public boolean hasSigningCertificate(int arg0, byte[] arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->hasSigningCertificate(I[BI)Z");
    }

    public boolean hasSigningCertificate(java.lang.String arg0, byte[] arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->hasSigningCertificate(Ljava/lang/String;[BI)Z");
    }

    public abstract boolean hasSystemFeature(java.lang.String arg0);

    public abstract boolean hasSystemFeature(java.lang.String arg0, int arg1);

    public boolean isAppArchivable(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->isAppArchivable(Ljava/lang/String;)Z");
    }

    public boolean isAutoRevokeWhitelisted() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->isAutoRevokeWhitelisted()Z");
    }

    public boolean isAutoRevokeWhitelisted(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->isAutoRevokeWhitelisted(Ljava/lang/String;)Z");
    }

    public boolean isDefaultApplicationIcon(android.graphics.drawable.Drawable arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->isDefaultApplicationIcon(Landroid/graphics/drawable/Drawable;)Z");
    }

    public boolean isDeviceUpgrading() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->isDeviceUpgrading()Z");
    }

    public abstract boolean isInstantApp();

    public abstract boolean isInstantApp(java.lang.String arg0);

    public boolean isPackageStopped(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->isPackageStopped(Ljava/lang/String;)Z");
    }

    public boolean isPackageSuspended() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->isPackageSuspended()Z");
    }

    public boolean isPackageSuspended(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->isPackageSuspended(Ljava/lang/String;)Z");
    }

    public abstract boolean isPermissionRevokedByPolicy(java.lang.String arg0, java.lang.String arg1);

    public abstract boolean isSafeMode();

    public <T> T parseAndroidManifest(android.os.ParcelFileDescriptor arg0, java.util.function.Function<android.content.res.XmlResourceParser, T> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->parseAndroidManifest(Landroid/os/ParcelFileDescriptor;Ljava/util/function/Function;)Ljava/lang/Object;");
    }

    public <T> T parseAndroidManifest(java.io.File arg0, java.util.function.Function<android.content.res.XmlResourceParser, T> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->parseAndroidManifest(Ljava/io/File;Ljava/util/function/Function;)Ljava/lang/Object;");
    }

    public java.util.List<android.content.pm.PackageManager.Property> queryActivityProperty(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->queryActivityProperty(Ljava/lang/String;)Ljava/util/List;");
    }

    public java.util.List<android.content.pm.PackageManager.Property> queryApplicationProperty(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->queryApplicationProperty(Ljava/lang/String;)Ljava/util/List;");
    }

    public java.util.List<android.content.pm.ResolveInfo> queryBroadcastReceivers(android.content.Intent arg0, android.content.pm.PackageManager.ResolveInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->queryBroadcastReceivers(Landroid/content/Intent;Landroid/content/pm/PackageManager$ResolveInfoFlags;)Ljava/util/List;");
    }

    public abstract java.util.List<android.content.pm.ResolveInfo> queryBroadcastReceivers(android.content.Intent arg0, int arg1);

    public java.util.List<android.content.pm.ProviderInfo> queryContentProviders(java.lang.String arg0, int arg1, android.content.pm.PackageManager.ComponentInfoFlags arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->queryContentProviders(Ljava/lang/String;ILandroid/content/pm/PackageManager$ComponentInfoFlags;)Ljava/util/List;");
    }

    public abstract java.util.List<android.content.pm.ProviderInfo> queryContentProviders(java.lang.String arg0, int arg1, int arg2);

    public abstract java.util.List<android.content.pm.InstrumentationInfo> queryInstrumentation(java.lang.String arg0, int arg1);

    public java.util.List<android.content.pm.ResolveInfo> queryIntentActivities(android.content.Intent arg0, android.content.pm.PackageManager.ResolveInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->queryIntentActivities(Landroid/content/Intent;Landroid/content/pm/PackageManager$ResolveInfoFlags;)Ljava/util/List;");
    }

    public abstract java.util.List<android.content.pm.ResolveInfo> queryIntentActivities(android.content.Intent arg0, int arg1);

    public abstract java.util.List<android.content.pm.ResolveInfo> queryIntentActivityOptions(android.content.ComponentName arg0, android.content.Intent[] arg1, android.content.Intent arg2, int arg3);

    public java.util.List<android.content.pm.ResolveInfo> queryIntentActivityOptions(android.content.ComponentName arg0, java.util.List<android.content.Intent> arg1, android.content.Intent arg2, android.content.pm.PackageManager.ResolveInfoFlags arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->queryIntentActivityOptions(Landroid/content/ComponentName;Ljava/util/List;Landroid/content/Intent;Landroid/content/pm/PackageManager$ResolveInfoFlags;)Ljava/util/List;");
    }

    public java.util.List<android.content.pm.ResolveInfo> queryIntentContentProviders(android.content.Intent arg0, android.content.pm.PackageManager.ResolveInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->queryIntentContentProviders(Landroid/content/Intent;Landroid/content/pm/PackageManager$ResolveInfoFlags;)Ljava/util/List;");
    }

    public abstract java.util.List<android.content.pm.ResolveInfo> queryIntentContentProviders(android.content.Intent arg0, int arg1);

    public java.util.List<android.content.pm.ResolveInfo> queryIntentServices(android.content.Intent arg0, android.content.pm.PackageManager.ResolveInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->queryIntentServices(Landroid/content/Intent;Landroid/content/pm/PackageManager$ResolveInfoFlags;)Ljava/util/List;");
    }

    public abstract java.util.List<android.content.pm.ResolveInfo> queryIntentServices(android.content.Intent arg0, int arg1);

    public abstract java.util.List<android.content.pm.PermissionInfo> queryPermissionsByGroup(java.lang.String arg0, int arg1);

    public java.util.List<android.content.pm.PackageManager.Property> queryProviderProperty(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->queryProviderProperty(Ljava/lang/String;)Ljava/util/List;");
    }

    public java.util.List<android.content.pm.PackageManager.Property> queryReceiverProperty(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->queryReceiverProperty(Ljava/lang/String;)Ljava/util/List;");
    }

    public java.util.List<android.content.pm.PackageManager.Property> queryServiceProperty(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->queryServiceProperty(Ljava/lang/String;)Ljava/util/List;");
    }

    public void relinquishUpdateOwnership(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->relinquishUpdateOwnership(Ljava/lang/String;)V");
    }

    public abstract void removePackageFromPreferred(java.lang.String arg0);

    public abstract void removePermission(java.lang.String arg0);

    public boolean removeWhitelistedRestrictedPermission(java.lang.String arg0, java.lang.String arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->removeWhitelistedRestrictedPermission(Ljava/lang/String;Ljava/lang/String;I)Z");
    }

    public void requestChecksums(java.lang.String arg0, boolean arg1, int arg2, java.util.List<java.security.cert.Certificate> arg3, android.content.pm.PackageManager.OnChecksumsReadyListener arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->requestChecksums(Ljava/lang/String;ZILjava/util/List;Landroid/content/pm/PackageManager$OnChecksumsReadyListener;)V");
    }

    public android.content.pm.ResolveInfo resolveActivity(android.content.Intent arg0, android.content.pm.PackageManager.ResolveInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->resolveActivity(Landroid/content/Intent;Landroid/content/pm/PackageManager$ResolveInfoFlags;)Landroid/content/pm/ResolveInfo;");
    }

    public abstract android.content.pm.ResolveInfo resolveActivity(android.content.Intent arg0, int arg1);

    public android.content.pm.ProviderInfo resolveContentProvider(java.lang.String arg0, android.content.pm.PackageManager.ComponentInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->resolveContentProvider(Ljava/lang/String;Landroid/content/pm/PackageManager$ComponentInfoFlags;)Landroid/content/pm/ProviderInfo;");
    }

    public abstract android.content.pm.ProviderInfo resolveContentProvider(java.lang.String arg0, int arg1);

    public android.content.pm.ResolveInfo resolveService(android.content.Intent arg0, android.content.pm.PackageManager.ResolveInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->resolveService(Landroid/content/Intent;Landroid/content/pm/PackageManager$ResolveInfoFlags;)Landroid/content/pm/ResolveInfo;");
    }

    public abstract android.content.pm.ResolveInfo resolveService(android.content.Intent arg0, int arg1);

    public abstract void setApplicationCategoryHint(java.lang.String arg0, int arg1);

    public abstract void setApplicationEnabledSetting(java.lang.String arg0, int arg1, int arg2);

    public boolean setAutoRevokeWhitelisted(java.lang.String arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->setAutoRevokeWhitelisted(Ljava/lang/String;Z)Z");
    }

    public abstract void setComponentEnabledSetting(android.content.ComponentName arg0, int arg1, int arg2);

    public void setComponentEnabledSettings(java.util.List<android.content.pm.PackageManager.ComponentEnabledSetting> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->setComponentEnabledSettings(Ljava/util/List;)V");
    }

    public abstract void setInstallerPackageName(java.lang.String arg0, java.lang.String arg1);

    public void setMimeGroup(java.lang.String arg0, java.util.Set<java.lang.String> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->setMimeGroup(Ljava/lang/String;Ljava/util/Set;)V");
    }

    public abstract void updateInstantAppCookie(byte[] arg0);

    public abstract void verifyPendingInstall(int arg0, int arg1);
        public static class ApplicationInfoFlags {

        public long getValue() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$ApplicationInfoFlags;->getValue()J");
        }

        public static android.content.pm.PackageManager.ApplicationInfoFlags of(long arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$ApplicationInfoFlags;->of(J)Landroid/content/pm/PackageManager$ApplicationInfoFlags;");
        }
        }
        public static class ComponentEnabledSetting implements android.os.Parcelable {

        public static android.os.Parcelable.Creator CREATOR;

        public ComponentEnabledSetting(android.content.ComponentName arg0, int arg1, int arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$ComponentEnabledSetting;-><init>(Landroid/content/ComponentName;II)V");
        }

        public int describeContents() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$ComponentEnabledSetting;->describeContents()I");
        }

        public android.content.ComponentName getComponentName() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$ComponentEnabledSetting;->getComponentName()Landroid/content/ComponentName;");
        }

        public int getEnabledFlags() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$ComponentEnabledSetting;->getEnabledFlags()I");
        }

        public int getEnabledState() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$ComponentEnabledSetting;->getEnabledState()I");
        }

        public void writeToParcel(android.os.Parcel arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$ComponentEnabledSetting;->writeToParcel(Landroid/os/Parcel;I)V");
        }

            /** Scratch mirror constructor (SkeletonGen). */
            public ComponentEnabledSetting() {
            }
        }
        public static class ComponentInfoFlags {

        public long getValue() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$ComponentInfoFlags;->getValue()J");
        }

        public static android.content.pm.PackageManager.ComponentInfoFlags of(long arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$ComponentInfoFlags;->of(J)Landroid/content/pm/PackageManager$ComponentInfoFlags;");
        }
        }
        public static interface OnChecksumsReadyListener {

        public abstract void onChecksumsReady(java.util.List<android.content.pm.ApkChecksum> arg0);
        }
        public static class PackageInfoFlags {

        public long getValue() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$PackageInfoFlags;->getValue()J");
        }

        public static android.content.pm.PackageManager.PackageInfoFlags of(long arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$PackageInfoFlags;->of(J)Landroid/content/pm/PackageManager$PackageInfoFlags;");
        }
        }
        public static class Property implements android.os.Parcelable {

        public static android.os.Parcelable.Creator CREATOR;

        public int describeContents() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$Property;->describeContents()I");
        }

        public boolean equals(java.lang.Object arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$Property;->equals(Ljava/lang/Object;)Z");
        }

        public boolean getBoolean() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$Property;->getBoolean()Z");
        }

        public java.lang.String getClassName() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$Property;->getClassName()Ljava/lang/String;");
        }

        public float getFloat() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$Property;->getFloat()F");
        }

        public int getInteger() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$Property;->getInteger()I");
        }

        public java.lang.String getName() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$Property;->getName()Ljava/lang/String;");
        }

        public java.lang.String getPackageName() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$Property;->getPackageName()Ljava/lang/String;");
        }

        public int getResourceId() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$Property;->getResourceId()I");
        }

        public java.lang.String getString() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$Property;->getString()Ljava/lang/String;");
        }

        public int hashCode() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$Property;->hashCode()I");
        }

        public boolean isBoolean() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$Property;->isBoolean()Z");
        }

        public boolean isFloat() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$Property;->isFloat()Z");
        }

        public boolean isInteger() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$Property;->isInteger()Z");
        }

        public boolean isResourceId() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$Property;->isResourceId()Z");
        }

        public boolean isString() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$Property;->isString()Z");
        }

        public void writeToParcel(android.os.Parcel arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$Property;->writeToParcel(Landroid/os/Parcel;I)V");
        }
        }
        public static class ResolveInfoFlags {

        public long getValue() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$ResolveInfoFlags;->getValue()J");
        }

        public static android.content.pm.PackageManager.ResolveInfoFlags of(long arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$ResolveInfoFlags;->of(J)Landroid/content/pm/PackageManager$ResolveInfoFlags;");
        }
        }
    }
