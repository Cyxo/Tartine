package android.content.pm;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.UserHandle;
import java.io.File;
import java.security.cert.Certificate;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;

    public class PackageManager {

    public static final int CERT_INPUT_RAW_X509 = 0;
    public static final int CERT_INPUT_SHA256 = 1;
    public static final int COMPONENT_ENABLED_STATE_DEFAULT = 0;
    public static final int COMPONENT_ENABLED_STATE_DISABLED = 2;
    public static final int COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED = 4;
    public static final int COMPONENT_ENABLED_STATE_DISABLED_USER = 3;
    public static final int COMPONENT_ENABLED_STATE_ENABLED = 1;
    public static final int DELETE_ARCHIVE = 16;
    public static final int DONT_KILL_APP = 1;
    public static final String EXTRA_VERIFICATION_ID = "android.content.pm.extra.VERIFICATION_ID";
    public static final String EXTRA_VERIFICATION_RESULT = "android.content.pm.extra.VERIFICATION_RESULT";
    public static final String FEATURE_ACTIVITIES_ON_SECONDARY_DISPLAYS = "android.software.activities_on_secondary_displays";
    public static final String FEATURE_APP_WIDGETS = "android.software.app_widgets";
    public static final String FEATURE_AUDIO_LOW_LATENCY = "android.hardware.audio.low_latency";
    public static final String FEATURE_AUDIO_OUTPUT = "android.hardware.audio.output";
    public static final String FEATURE_AUDIO_PRO = "android.hardware.audio.pro";
    public static final String FEATURE_AUDIO_SPATIAL_HEADTRACKING_LOW_LATENCY = "android.hardware.audio.spatial.headtracking.low_latency";
    public static final String FEATURE_AUTOFILL = "android.software.autofill";
    public static final String FEATURE_AUTOMOTIVE = "android.hardware.type.automotive";
    public static final String FEATURE_BACKUP = "android.software.backup";
    public static final String FEATURE_BLUETOOTH = "android.hardware.bluetooth";
    public static final String FEATURE_BLUETOOTH_LE = "android.hardware.bluetooth_le";
    public static final String FEATURE_BLUETOOTH_LE_CHANNEL_SOUNDING = "android.hardware.bluetooth_le.channel_sounding";
    public static final String FEATURE_CAMERA = "android.hardware.camera";
    public static final String FEATURE_CAMERA_ANY = "android.hardware.camera.any";
    public static final String FEATURE_CAMERA_AR = "android.hardware.camera.ar";
    public static final String FEATURE_CAMERA_AUTOFOCUS = "android.hardware.camera.autofocus";
    public static final String FEATURE_CAMERA_CAPABILITY_MANUAL_POST_PROCESSING = "android.hardware.camera.capability.manual_post_processing";
    public static final String FEATURE_CAMERA_CAPABILITY_MANUAL_SENSOR = "android.hardware.camera.capability.manual_sensor";
    public static final String FEATURE_CAMERA_CAPABILITY_RAW = "android.hardware.camera.capability.raw";
    public static final String FEATURE_CAMERA_CONCURRENT = "android.hardware.camera.concurrent";
    public static final String FEATURE_CAMERA_EXTERNAL = "android.hardware.camera.external";
    public static final String FEATURE_CAMERA_FLASH = "android.hardware.camera.flash";
    public static final String FEATURE_CAMERA_FRONT = "android.hardware.camera.front";
    public static final String FEATURE_CAMERA_LEVEL_FULL = "android.hardware.camera.level.full";
    public static final String FEATURE_CANT_SAVE_STATE = "android.software.cant_save_state";
    public static final String FEATURE_COMPANION_DEVICE_SETUP = "android.software.companion_device_setup";
    public static final String FEATURE_CONNECTION_SERVICE = "android.software.connectionservice";
    public static final String FEATURE_CONSUMER_IR = "android.hardware.consumerir";
    public static final String FEATURE_CONTROLS = "android.software.controls";
    public static final String FEATURE_CREDENTIALS = "android.software.credentials";
    public static final String FEATURE_DEVICE_ADMIN = "android.software.device_admin";
    public static final String FEATURE_DEVICE_LOCK = "android.software.device_lock";
    public static final String FEATURE_EMBEDDED = "android.hardware.type.embedded";
    public static final String FEATURE_ETHERNET = "android.hardware.ethernet";
    public static final String FEATURE_EXPANDED_PICTURE_IN_PICTURE = "android.software.expanded_picture_in_picture";
    public static final String FEATURE_FACE = "android.hardware.biometrics.face";
    public static final String FEATURE_FAKETOUCH = "android.hardware.faketouch";
    public static final String FEATURE_FAKETOUCH_MULTITOUCH_DISTINCT = "android.hardware.faketouch.multitouch.distinct";
    public static final String FEATURE_FAKETOUCH_MULTITOUCH_JAZZHAND = "android.hardware.faketouch.multitouch.jazzhand";
    public static final String FEATURE_FINGERPRINT = "android.hardware.fingerprint";
    public static final String FEATURE_FREEFORM_WINDOW_MANAGEMENT = "android.software.freeform_window_management";
    public static final String FEATURE_GAMEPAD = "android.hardware.gamepad";
    public static final String FEATURE_HARDWARE_KEYSTORE = "android.hardware.hardware_keystore";
    public static final String FEATURE_HIFI_SENSORS = "android.hardware.sensor.hifi_sensors";
    public static final String FEATURE_HOME_SCREEN = "android.software.home_screen";
    public static final String FEATURE_IDENTITY_CREDENTIAL_HARDWARE = "android.hardware.identity_credential";
    public static final String FEATURE_IDENTITY_CREDENTIAL_HARDWARE_DIRECT_ACCESS = "android.hardware.identity_credential_direct_access";
    public static final String FEATURE_INPUT_METHODS = "android.software.input_methods";
    public static final String FEATURE_IPSEC_TUNNELS = "android.software.ipsec_tunnels";
    public static final String FEATURE_IPSEC_TUNNEL_MIGRATION = "android.software.ipsec_tunnel_migration";
    public static final String FEATURE_IRIS = "android.hardware.biometrics.iris";
    public static final String FEATURE_KEYSTORE_APP_ATTEST_KEY = "android.hardware.keystore.app_attest_key";
    public static final String FEATURE_KEYSTORE_LIMITED_USE_KEY = "android.hardware.keystore.limited_use_key";
    public static final String FEATURE_KEYSTORE_SINGLE_USE_KEY = "android.hardware.keystore.single_use_key";
    public static final String FEATURE_LEANBACK = "android.software.leanback";
    public static final String FEATURE_LEANBACK_ONLY = "android.software.leanback_only";
    public static final String FEATURE_LIVE_TV = "android.software.live_tv";
    public static final String FEATURE_LIVE_WALLPAPER = "android.software.live_wallpaper";
    public static final String FEATURE_LOCATION = "android.hardware.location";
    public static final String FEATURE_LOCATION_GPS = "android.hardware.location.gps";
    public static final String FEATURE_LOCATION_NETWORK = "android.hardware.location.network";
    public static final String FEATURE_MANAGED_USERS = "android.software.managed_users";
    public static final String FEATURE_MICROPHONE = "android.hardware.microphone";
    public static final String FEATURE_MIDI = "android.software.midi";
    public static final String FEATURE_NFC = "android.hardware.nfc";
    public static final String FEATURE_NFC_BEAM = "android.sofware.nfc.beam";
    public static final String FEATURE_NFC_HOST_CARD_EMULATION = "android.hardware.nfc.hce";
    public static final String FEATURE_NFC_HOST_CARD_EMULATION_NFCF = "android.hardware.nfc.hcef";
    public static final String FEATURE_NFC_OFF_HOST_CARD_EMULATION_ESE = "android.hardware.nfc.ese";
    public static final String FEATURE_NFC_OFF_HOST_CARD_EMULATION_UICC = "android.hardware.nfc.uicc";
    public static final String FEATURE_OPENGLES_DEQP_LEVEL = "android.software.opengles.deqp.level";
    public static final String FEATURE_OPENGLES_EXTENSION_PACK = "android.hardware.opengles.aep";
    public static final String FEATURE_PC = "android.hardware.type.pc";
    public static final String FEATURE_PICTURE_IN_PICTURE = "android.software.picture_in_picture";
    public static final String FEATURE_PRINTING = "android.software.print";
    public static final String FEATURE_RAM_LOW = "android.hardware.ram.low";
    public static final String FEATURE_RAM_NORMAL = "android.hardware.ram.normal";
    public static final String FEATURE_SCREEN_LANDSCAPE = "android.hardware.screen.landscape";
    public static final String FEATURE_SCREEN_PORTRAIT = "android.hardware.screen.portrait";
    public static final String FEATURE_SECURELY_REMOVES_USERS = "android.software.securely_removes_users";
    public static final String FEATURE_SECURE_LOCK_SCREEN = "android.software.secure_lock_screen";
    public static final String FEATURE_SECURITY_MODEL_COMPATIBLE = "android.hardware.security.model.compatible";
    public static final String FEATURE_SENSOR_ACCELEROMETER = "android.hardware.sensor.accelerometer";
    public static final String FEATURE_SENSOR_ACCELEROMETER_LIMITED_AXES = "android.hardware.sensor.accelerometer_limited_axes";
    public static final String FEATURE_SENSOR_ACCELEROMETER_LIMITED_AXES_UNCALIBRATED = "android.hardware.sensor.accelerometer_limited_axes_uncalibrated";
    public static final String FEATURE_SENSOR_AMBIENT_TEMPERATURE = "android.hardware.sensor.ambient_temperature";
    public static final String FEATURE_SENSOR_BAROMETER = "android.hardware.sensor.barometer";
    public static final String FEATURE_SENSOR_COMPASS = "android.hardware.sensor.compass";
    public static final String FEATURE_SENSOR_DYNAMIC_HEAD_TRACKER = "android.hardware.sensor.dynamic.head_tracker";
    public static final String FEATURE_SENSOR_GYROSCOPE = "android.hardware.sensor.gyroscope";
    public static final String FEATURE_SENSOR_GYROSCOPE_LIMITED_AXES = "android.hardware.sensor.gyroscope_limited_axes";
    public static final String FEATURE_SENSOR_GYROSCOPE_LIMITED_AXES_UNCALIBRATED = "android.hardware.sensor.gyroscope_limited_axes_uncalibrated";
    public static final String FEATURE_SENSOR_HEADING = "android.hardware.sensor.heading";
    public static final String FEATURE_SENSOR_HEART_RATE = "android.hardware.sensor.heartrate";
    public static final String FEATURE_SENSOR_HEART_RATE_ECG = "android.hardware.sensor.heartrate.ecg";
    public static final String FEATURE_SENSOR_HINGE_ANGLE = "android.hardware.sensor.hinge_angle";
    public static final String FEATURE_SENSOR_LIGHT = "android.hardware.sensor.light";
    public static final String FEATURE_SENSOR_PROXIMITY = "android.hardware.sensor.proximity";
    public static final String FEATURE_SENSOR_RELATIVE_HUMIDITY = "android.hardware.sensor.relative_humidity";
    public static final String FEATURE_SENSOR_STEP_COUNTER = "android.hardware.sensor.stepcounter";
    public static final String FEATURE_SENSOR_STEP_DETECTOR = "android.hardware.sensor.stepdetector";
    public static final String FEATURE_SE_OMAPI_ESE = "android.hardware.se.omapi.ese";
    public static final String FEATURE_SE_OMAPI_SD = "android.hardware.se.omapi.sd";
    public static final String FEATURE_SE_OMAPI_UICC = "android.hardware.se.omapi.uicc";
    public static final String FEATURE_SIP = "android.software.sip";
    public static final String FEATURE_SIP_VOIP = "android.software.sip.voip";
    public static final String FEATURE_STRONGBOX_KEYSTORE = "android.hardware.strongbox_keystore";
    public static final String FEATURE_TELECOM = "android.software.telecom";
    public static final String FEATURE_TELEPHONY = "android.hardware.telephony";
    public static final String FEATURE_TELEPHONY_CALLING = "android.hardware.telephony.calling";
    public static final String FEATURE_TELEPHONY_CDMA = "android.hardware.telephony.cdma";
    public static final String FEATURE_TELEPHONY_DATA = "android.hardware.telephony.data";
    public static final String FEATURE_TELEPHONY_EUICC = "android.hardware.telephony.euicc";
    public static final String FEATURE_TELEPHONY_EUICC_MEP = "android.hardware.telephony.euicc.mep";
    public static final String FEATURE_TELEPHONY_GSM = "android.hardware.telephony.gsm";
    public static final String FEATURE_TELEPHONY_IMS = "android.hardware.telephony.ims";
    public static final String FEATURE_TELEPHONY_MBMS = "android.hardware.telephony.mbms";
    public static final String FEATURE_TELEPHONY_MESSAGING = "android.hardware.telephony.messaging";
    public static final String FEATURE_TELEPHONY_RADIO_ACCESS = "android.hardware.telephony.radio.access";
    public static final String FEATURE_TELEPHONY_SUBSCRIPTION = "android.hardware.telephony.subscription";
    public static final String FEATURE_TELEVISION = "android.hardware.type.television";
    public static final String FEATURE_THREAD_NETWORK = "android.hardware.thread_network";
    public static final String FEATURE_TOUCHSCREEN = "android.hardware.touchscreen";
    public static final String FEATURE_TOUCHSCREEN_MULTITOUCH = "android.hardware.touchscreen.multitouch";
    public static final String FEATURE_TOUCHSCREEN_MULTITOUCH_DISTINCT = "android.hardware.touchscreen.multitouch.distinct";
    public static final String FEATURE_TOUCHSCREEN_MULTITOUCH_JAZZHAND = "android.hardware.touchscreen.multitouch.jazzhand";
    public static final String FEATURE_USB_ACCESSORY = "android.hardware.usb.accessory";
    public static final String FEATURE_USB_HOST = "android.hardware.usb.host";
    public static final String FEATURE_UWB = "android.hardware.uwb";
    public static final String FEATURE_VERIFIED_BOOT = "android.software.verified_boot";
    public static final String FEATURE_VR_HEADTRACKING = "android.hardware.vr.headtracking";
    public static final String FEATURE_VR_MODE = "android.software.vr.mode";
    public static final String FEATURE_VR_MODE_HIGH_PERFORMANCE = "android.hardware.vr.high_performance";
    public static final String FEATURE_VULKAN_DEQP_LEVEL = "android.software.vulkan.deqp.level";
    public static final String FEATURE_VULKAN_HARDWARE_COMPUTE = "android.hardware.vulkan.compute";
    public static final String FEATURE_VULKAN_HARDWARE_LEVEL = "android.hardware.vulkan.level";
    public static final String FEATURE_VULKAN_HARDWARE_VERSION = "android.hardware.vulkan.version";
    public static final String FEATURE_WALLET_LOCATION_BASED_SUGGESTIONS = "android.software.wallet_location_based_suggestions";
    public static final String FEATURE_WATCH = "android.hardware.type.watch";
    public static final String FEATURE_WEBVIEW = "android.software.webview";
    public static final String FEATURE_WIFI = "android.hardware.wifi";
    public static final String FEATURE_WIFI_AWARE = "android.hardware.wifi.aware";
    public static final String FEATURE_WIFI_DIRECT = "android.hardware.wifi.direct";
    public static final String FEATURE_WIFI_PASSPOINT = "android.hardware.wifi.passpoint";
    public static final String FEATURE_WIFI_RTT = "android.hardware.wifi.rtt";
    public static final String FEATURE_WINDOW_MAGNIFICATION = "android.software.window_magnification";
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
    public static final String PROPERTY_COMPAT_OVERRIDE_LANDSCAPE_TO_PORTRAIT = "android.camera.PROPERTY_COMPAT_OVERRIDE_LANDSCAPE_TO_PORTRAIT";
    public static final String PROPERTY_MEDIA_CAPABILITIES = "android.media.PROPERTY_MEDIA_CAPABILITIES";
    public static final String PROPERTY_SELF_CERTIFIED_NETWORK_CAPABILITIES = "android.net.PROPERTY_SELF_CERTIFIED_NETWORK_CAPABILITIES";
    public static final String PROPERTY_SPECIAL_USE_FGS_SUBTYPE = "android.app.PROPERTY_SPECIAL_USE_FGS_SUBTYPE";
    public static final String PROPERTY_USE_RESTRICTED_BACKUP_MODE = "android.app.backup.PROPERTY_USE_RESTRICTED_BACKUP_MODE";
    public static final int SIGNATURE_FIRST_NOT_SIGNED = -1;
    public static final int SIGNATURE_MATCH = 0;
    public static final int SIGNATURE_NEITHER_SIGNED = 1;
    public static final int SIGNATURE_NO_MATCH = -3;
    public static final int SIGNATURE_SECOND_NOT_SIGNED = -2;
    public static final int SIGNATURE_UNKNOWN_PACKAGE = -4;
    public static final int SYNCHRONOUS = 2;
    public static List TRUST_ALL;
    public static List TRUST_NONE;
    public static final int VERIFICATION_ALLOW = 1;
    public static final int VERIFICATION_REJECT = -1;
    public static final int VERSION_CODE_HIGHEST = -1;

    public PackageManager() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;-><init>()V");
    }

    public void addPackageToPreferred(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->addPackageToPreferred(Ljava/lang/String;)V");
    }

    public boolean addPermission(PermissionInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->addPermission(Landroid/content/pm/PermissionInfo;)Z");
    }

    public boolean addPermissionAsync(PermissionInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->addPermissionAsync(Landroid/content/pm/PermissionInfo;)Z");
    }

    public void addPreferredActivity(IntentFilter arg0, int arg1, ComponentName[] arg2, ComponentName arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->addPreferredActivity(Landroid/content/IntentFilter;I[Landroid/content/ComponentName;Landroid/content/ComponentName;)V");
    }

    public boolean addWhitelistedRestrictedPermission(String arg0, String arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->addWhitelistedRestrictedPermission(Ljava/lang/String;Ljava/lang/String;I)Z");
    }

    public boolean canPackageQuery(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->canPackageQuery(Ljava/lang/String;Ljava/lang/String;)Z");
    }

    public boolean[] canPackageQuery(String arg0, String[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->canPackageQuery(Ljava/lang/String;[Ljava/lang/String;)[Z");
    }

    public boolean canRequestPackageInstalls() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->canRequestPackageInstalls()Z");
    }

    public String[] canonicalToCurrentPackageNames(String[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->canonicalToCurrentPackageNames([Ljava/lang/String;)[Ljava/lang/String;");
    }

    public int checkPermission(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->checkPermission(Ljava/lang/String;Ljava/lang/String;)I");
    }

    public int checkSignatures(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->checkSignatures(II)I");
    }

    public int checkSignatures(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->checkSignatures(Ljava/lang/String;Ljava/lang/String;)I");
    }

    public void clearInstantAppCookie() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->clearInstantAppCookie()V");
    }

    public void clearPackagePreferredActivities(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->clearPackagePreferredActivities(Ljava/lang/String;)V");
    }

    public String[] currentToCanonicalPackageNames(String[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->currentToCanonicalPackageNames([Ljava/lang/String;)[Ljava/lang/String;");
    }

    public void extendVerificationTimeout(int arg0, int arg1, long arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->extendVerificationTimeout(IIJ)V");
    }

    public Drawable getActivityBanner(ComponentName arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getActivityBanner(Landroid/content/ComponentName;)Landroid/graphics/drawable/Drawable;");
    }

    public Drawable getActivityBanner(Intent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getActivityBanner(Landroid/content/Intent;)Landroid/graphics/drawable/Drawable;");
    }

    public Drawable getActivityIcon(ComponentName arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getActivityIcon(Landroid/content/ComponentName;)Landroid/graphics/drawable/Drawable;");
    }

    public Drawable getActivityIcon(Intent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getActivityIcon(Landroid/content/Intent;)Landroid/graphics/drawable/Drawable;");
    }

    public ActivityInfo getActivityInfo(ComponentName arg0, PackageManager.ComponentInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getActivityInfo(Landroid/content/ComponentName;Landroid/content/pm/PackageManager$ComponentInfoFlags;)Landroid/content/pm/ActivityInfo;");
    }

    public ActivityInfo getActivityInfo(ComponentName arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getActivityInfo(Landroid/content/ComponentName;I)Landroid/content/pm/ActivityInfo;");
    }

    public Drawable getActivityLogo(ComponentName arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getActivityLogo(Landroid/content/ComponentName;)Landroid/graphics/drawable/Drawable;");
    }

    public Drawable getActivityLogo(Intent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getActivityLogo(Landroid/content/Intent;)Landroid/graphics/drawable/Drawable;");
    }

    public List<PermissionGroupInfo> getAllPermissionGroups(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getAllPermissionGroups(I)Ljava/util/List;");
    }

    public Drawable getApplicationBanner(ApplicationInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getApplicationBanner(Landroid/content/pm/ApplicationInfo;)Landroid/graphics/drawable/Drawable;");
    }

    public Drawable getApplicationBanner(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getApplicationBanner(Ljava/lang/String;)Landroid/graphics/drawable/Drawable;");
    }

    public int getApplicationEnabledSetting(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getApplicationEnabledSetting(Ljava/lang/String;)I");
    }

    public Drawable getApplicationIcon(ApplicationInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getApplicationIcon(Landroid/content/pm/ApplicationInfo;)Landroid/graphics/drawable/Drawable;");
    }

    public Drawable getApplicationIcon(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getApplicationIcon(Ljava/lang/String;)Landroid/graphics/drawable/Drawable;");
    }

    public ApplicationInfo getApplicationInfo(String arg0, PackageManager.ApplicationInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getApplicationInfo(Ljava/lang/String;Landroid/content/pm/PackageManager$ApplicationInfoFlags;)Landroid/content/pm/ApplicationInfo;");
    }

    public ApplicationInfo getApplicationInfo(String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getApplicationInfo(Ljava/lang/String;I)Landroid/content/pm/ApplicationInfo;");
    }

    public CharSequence getApplicationLabel(ApplicationInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getApplicationLabel(Landroid/content/pm/ApplicationInfo;)Ljava/lang/CharSequence;");
    }

    public Drawable getApplicationLogo(ApplicationInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getApplicationLogo(Landroid/content/pm/ApplicationInfo;)Landroid/graphics/drawable/Drawable;");
    }

    public Drawable getApplicationLogo(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getApplicationLogo(Ljava/lang/String;)Landroid/graphics/drawable/Drawable;");
    }

    public ArchivedPackageInfo getArchivedPackage(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getArchivedPackage(Ljava/lang/String;)Landroid/content/pm/ArchivedPackageInfo;");
    }

    public CharSequence getBackgroundPermissionOptionLabel() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getBackgroundPermissionOptionLabel()Ljava/lang/CharSequence;");
    }

    public ChangedPackages getChangedPackages(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getChangedPackages(I)Landroid/content/pm/ChangedPackages;");
    }

    public int getComponentEnabledSetting(ComponentName arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getComponentEnabledSetting(Landroid/content/ComponentName;)I");
    }

    public Drawable getDefaultActivityIcon() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getDefaultActivityIcon()Landroid/graphics/drawable/Drawable;");
    }

    public Drawable getDrawable(String arg0, int arg1, ApplicationInfo arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getDrawable(Ljava/lang/String;ILandroid/content/pm/ApplicationInfo;)Landroid/graphics/drawable/Drawable;");
    }

    public void getGroupOfPlatformPermission(String arg0, Executor arg1, Consumer<String> arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getGroupOfPlatformPermission(Ljava/lang/String;Ljava/util/concurrent/Executor;Ljava/util/function/Consumer;)V");
    }

    public InstallSourceInfo getInstallSourceInfo(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getInstallSourceInfo(Ljava/lang/String;)Landroid/content/pm/InstallSourceInfo;");
    }

    public List<ApplicationInfo> getInstalledApplications(PackageManager.ApplicationInfoFlags arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getInstalledApplications(Landroid/content/pm/PackageManager$ApplicationInfoFlags;)Ljava/util/List;");
    }

    public List<ApplicationInfo> getInstalledApplications(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getInstalledApplications(I)Ljava/util/List;");
    }

    public List<ModuleInfo> getInstalledModules(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getInstalledModules(I)Ljava/util/List;");
    }

    public List<PackageInfo> getInstalledPackages(PackageManager.PackageInfoFlags arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getInstalledPackages(Landroid/content/pm/PackageManager$PackageInfoFlags;)Ljava/util/List;");
    }

    public List<PackageInfo> getInstalledPackages(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getInstalledPackages(I)Ljava/util/List;");
    }

    public String getInstallerPackageName(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getInstallerPackageName(Ljava/lang/String;)Ljava/lang/String;");
    }

    public byte[] getInstantAppCookie() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getInstantAppCookie()[B");
    }

    public int getInstantAppCookieMaxBytes() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getInstantAppCookieMaxBytes()I");
    }

    public InstrumentationInfo getInstrumentationInfo(ComponentName arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getInstrumentationInfo(Landroid/content/ComponentName;I)Landroid/content/pm/InstrumentationInfo;");
    }

    public Intent getLaunchIntentForPackage(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getLaunchIntentForPackage(Ljava/lang/String;)Landroid/content/Intent;");
    }

    public IntentSender getLaunchIntentSenderForPackage(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getLaunchIntentSenderForPackage(Ljava/lang/String;)Landroid/content/IntentSender;");
    }

    public Intent getLeanbackLaunchIntentForPackage(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getLeanbackLaunchIntentForPackage(Ljava/lang/String;)Landroid/content/Intent;");
    }

    public Set<String> getMimeGroup(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getMimeGroup(Ljava/lang/String;)Ljava/util/Set;");
    }

    public ModuleInfo getModuleInfo(String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getModuleInfo(Ljava/lang/String;I)Landroid/content/pm/ModuleInfo;");
    }

    public String getNameForUid(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getNameForUid(I)Ljava/lang/String;");
    }

    public PackageInfo getPackageArchiveInfo(String arg0, PackageManager.PackageInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getPackageArchiveInfo(Ljava/lang/String;Landroid/content/pm/PackageManager$PackageInfoFlags;)Landroid/content/pm/PackageInfo;");
    }

    public PackageInfo getPackageArchiveInfo(String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getPackageArchiveInfo(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
    }

    public int[] getPackageGids(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getPackageGids(Ljava/lang/String;)[I");
    }

    public int[] getPackageGids(String arg0, PackageManager.PackageInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getPackageGids(Ljava/lang/String;Landroid/content/pm/PackageManager$PackageInfoFlags;)[I");
    }

    public int[] getPackageGids(String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getPackageGids(Ljava/lang/String;I)[I");
    }

    public PackageInfo getPackageInfo(VersionedPackage arg0, PackageManager.PackageInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getPackageInfo(Landroid/content/pm/VersionedPackage;Landroid/content/pm/PackageManager$PackageInfoFlags;)Landroid/content/pm/PackageInfo;");
    }

    public PackageInfo getPackageInfo(VersionedPackage arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getPackageInfo(Landroid/content/pm/VersionedPackage;I)Landroid/content/pm/PackageInfo;");
    }

    public PackageInfo getPackageInfo(String arg0, PackageManager.PackageInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getPackageInfo(Ljava/lang/String;Landroid/content/pm/PackageManager$PackageInfoFlags;)Landroid/content/pm/PackageInfo;");
    }

    public PackageInfo getPackageInfo(String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getPackageInfo(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
    }

    public PackageInstaller getPackageInstaller() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getPackageInstaller()Landroid/content/pm/PackageInstaller;");
    }

    public int getPackageUid(String arg0, PackageManager.PackageInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getPackageUid(Ljava/lang/String;Landroid/content/pm/PackageManager$PackageInfoFlags;)I");
    }

    public int getPackageUid(String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getPackageUid(Ljava/lang/String;I)I");
    }

    public String[] getPackagesForUid(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getPackagesForUid(I)[Ljava/lang/String;");
    }

    public List<PackageInfo> getPackagesHoldingPermissions(String[] arg0, PackageManager.PackageInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getPackagesHoldingPermissions([Ljava/lang/String;Landroid/content/pm/PackageManager$PackageInfoFlags;)Ljava/util/List;");
    }

    public List<PackageInfo> getPackagesHoldingPermissions(String[] arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getPackagesHoldingPermissions([Ljava/lang/String;I)Ljava/util/List;");
    }

    public PermissionGroupInfo getPermissionGroupInfo(String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getPermissionGroupInfo(Ljava/lang/String;I)Landroid/content/pm/PermissionGroupInfo;");
    }

    public PermissionInfo getPermissionInfo(String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getPermissionInfo(Ljava/lang/String;I)Landroid/content/pm/PermissionInfo;");
    }

    public void getPlatformPermissionsForGroup(String arg0, Executor arg1, Consumer<List<String>> arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getPlatformPermissionsForGroup(Ljava/lang/String;Ljava/util/concurrent/Executor;Ljava/util/function/Consumer;)V");
    }

    public int getPreferredActivities(List<IntentFilter> arg0, List<ComponentName> arg1, String arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getPreferredActivities(Ljava/util/List;Ljava/util/List;Ljava/lang/String;)I");
    }

    public List<PackageInfo> getPreferredPackages(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getPreferredPackages(I)Ljava/util/List;");
    }

    public PackageManager.Property getProperty(String arg0, ComponentName arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getProperty(Ljava/lang/String;Landroid/content/ComponentName;)Landroid/content/pm/PackageManager$Property;");
    }

    public PackageManager.Property getProperty(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getProperty(Ljava/lang/String;Ljava/lang/String;)Landroid/content/pm/PackageManager$Property;");
    }

    public ProviderInfo getProviderInfo(ComponentName arg0, PackageManager.ComponentInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getProviderInfo(Landroid/content/ComponentName;Landroid/content/pm/PackageManager$ComponentInfoFlags;)Landroid/content/pm/ProviderInfo;");
    }

    public ProviderInfo getProviderInfo(ComponentName arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getProviderInfo(Landroid/content/ComponentName;I)Landroid/content/pm/ProviderInfo;");
    }

    public ActivityInfo getReceiverInfo(ComponentName arg0, PackageManager.ComponentInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getReceiverInfo(Landroid/content/ComponentName;Landroid/content/pm/PackageManager$ComponentInfoFlags;)Landroid/content/pm/ActivityInfo;");
    }

    public ActivityInfo getReceiverInfo(ComponentName arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getReceiverInfo(Landroid/content/ComponentName;I)Landroid/content/pm/ActivityInfo;");
    }

    public Resources getResourcesForActivity(ComponentName arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getResourcesForActivity(Landroid/content/ComponentName;)Landroid/content/res/Resources;");
    }

    public Resources getResourcesForApplication(ApplicationInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getResourcesForApplication(Landroid/content/pm/ApplicationInfo;)Landroid/content/res/Resources;");
    }

    public Resources getResourcesForApplication(ApplicationInfo arg0, Configuration arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getResourcesForApplication(Landroid/content/pm/ApplicationInfo;Landroid/content/res/Configuration;)Landroid/content/res/Resources;");
    }

    public Resources getResourcesForApplication(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getResourcesForApplication(Ljava/lang/String;)Landroid/content/res/Resources;");
    }

    public ServiceInfo getServiceInfo(ComponentName arg0, PackageManager.ComponentInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getServiceInfo(Landroid/content/ComponentName;Landroid/content/pm/PackageManager$ComponentInfoFlags;)Landroid/content/pm/ServiceInfo;");
    }

    public ServiceInfo getServiceInfo(ComponentName arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getServiceInfo(Landroid/content/ComponentName;I)Landroid/content/pm/ServiceInfo;");
    }

    public List<SharedLibraryInfo> getSharedLibraries(PackageManager.PackageInfoFlags arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getSharedLibraries(Landroid/content/pm/PackageManager$PackageInfoFlags;)Ljava/util/List;");
    }

    public List<SharedLibraryInfo> getSharedLibraries(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getSharedLibraries(I)Ljava/util/List;");
    }

    public Bundle getSuspendedPackageAppExtras() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getSuspendedPackageAppExtras()Landroid/os/Bundle;");
    }

    public boolean getSyntheticAppDetailsActivityEnabled(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getSyntheticAppDetailsActivityEnabled(Ljava/lang/String;)Z");
    }

    public FeatureInfo[] getSystemAvailableFeatures() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getSystemAvailableFeatures()[Landroid/content/pm/FeatureInfo;");
    }

    public String[] getSystemSharedLibraryNames() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getSystemSharedLibraryNames()[Ljava/lang/String;");
    }

    public int getTargetSdkVersion(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getTargetSdkVersion(Ljava/lang/String;)I");
    }

    public CharSequence getText(String arg0, int arg1, ApplicationInfo arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getText(Ljava/lang/String;ILandroid/content/pm/ApplicationInfo;)Ljava/lang/CharSequence;");
    }

    public Drawable getUserBadgedDrawableForDensity(Drawable arg0, UserHandle arg1, Rect arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getUserBadgedDrawableForDensity(Landroid/graphics/drawable/Drawable;Landroid/os/UserHandle;Landroid/graphics/Rect;I)Landroid/graphics/drawable/Drawable;");
    }

    public Drawable getUserBadgedIcon(Drawable arg0, UserHandle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getUserBadgedIcon(Landroid/graphics/drawable/Drawable;Landroid/os/UserHandle;)Landroid/graphics/drawable/Drawable;");
    }

    public CharSequence getUserBadgedLabel(CharSequence arg0, UserHandle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getUserBadgedLabel(Ljava/lang/CharSequence;Landroid/os/UserHandle;)Ljava/lang/CharSequence;");
    }

    public static SigningInfo getVerifiedSigningInfo(String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getVerifiedSigningInfo(Ljava/lang/String;I)Landroid/content/pm/SigningInfo;");
    }

    public Set<String> getWhitelistedRestrictedPermissions(String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getWhitelistedRestrictedPermissions(Ljava/lang/String;I)Ljava/util/Set;");
    }

    public XmlResourceParser getXml(String arg0, int arg1, ApplicationInfo arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->getXml(Ljava/lang/String;ILandroid/content/pm/ApplicationInfo;)Landroid/content/res/XmlResourceParser;");
    }

    public boolean hasSigningCertificate(int arg0, byte[] arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->hasSigningCertificate(I[BI)Z");
    }

    public boolean hasSigningCertificate(String arg0, byte[] arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->hasSigningCertificate(Ljava/lang/String;[BI)Z");
    }

    public boolean hasSystemFeature(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->hasSystemFeature(Ljava/lang/String;)Z");
    }

    public boolean hasSystemFeature(String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->hasSystemFeature(Ljava/lang/String;I)Z");
    }

    public boolean isAppArchivable(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->isAppArchivable(Ljava/lang/String;)Z");
    }

    public boolean isAutoRevokeWhitelisted() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->isAutoRevokeWhitelisted()Z");
    }

    public boolean isAutoRevokeWhitelisted(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->isAutoRevokeWhitelisted(Ljava/lang/String;)Z");
    }

    public boolean isDefaultApplicationIcon(Drawable arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->isDefaultApplicationIcon(Landroid/graphics/drawable/Drawable;)Z");
    }

    public boolean isDeviceUpgrading() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->isDeviceUpgrading()Z");
    }

    public boolean isInstantApp() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->isInstantApp()Z");
    }

    public boolean isInstantApp(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->isInstantApp(Ljava/lang/String;)Z");
    }

    public boolean isPackageStopped(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->isPackageStopped(Ljava/lang/String;)Z");
    }

    public boolean isPackageSuspended() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->isPackageSuspended()Z");
    }

    public boolean isPackageSuspended(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->isPackageSuspended(Ljava/lang/String;)Z");
    }

    public boolean isPermissionRevokedByPolicy(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->isPermissionRevokedByPolicy(Ljava/lang/String;Ljava/lang/String;)Z");
    }

    public boolean isSafeMode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->isSafeMode()Z");
    }

    public <T> T parseAndroidManifest(ParcelFileDescriptor arg0, Function<XmlResourceParser, T> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->parseAndroidManifest(Landroid/os/ParcelFileDescriptor;Ljava/util/function/Function;)Ljava/lang/Object;");
    }

    public <T> T parseAndroidManifest(File arg0, Function<XmlResourceParser, T> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->parseAndroidManifest(Ljava/io/File;Ljava/util/function/Function;)Ljava/lang/Object;");
    }

    public List<PackageManager.Property> queryActivityProperty(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->queryActivityProperty(Ljava/lang/String;)Ljava/util/List;");
    }

    public List<PackageManager.Property> queryApplicationProperty(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->queryApplicationProperty(Ljava/lang/String;)Ljava/util/List;");
    }

    public List<ResolveInfo> queryBroadcastReceivers(Intent arg0, PackageManager.ResolveInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->queryBroadcastReceivers(Landroid/content/Intent;Landroid/content/pm/PackageManager$ResolveInfoFlags;)Ljava/util/List;");
    }

    public List<ResolveInfo> queryBroadcastReceivers(Intent arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->queryBroadcastReceivers(Landroid/content/Intent;I)Ljava/util/List;");
    }

    public List<ProviderInfo> queryContentProviders(String arg0, int arg1, PackageManager.ComponentInfoFlags arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->queryContentProviders(Ljava/lang/String;ILandroid/content/pm/PackageManager$ComponentInfoFlags;)Ljava/util/List;");
    }

    public List<ProviderInfo> queryContentProviders(String arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->queryContentProviders(Ljava/lang/String;II)Ljava/util/List;");
    }

    public List<InstrumentationInfo> queryInstrumentation(String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->queryInstrumentation(Ljava/lang/String;I)Ljava/util/List;");
    }

    public List<ResolveInfo> queryIntentActivities(Intent arg0, PackageManager.ResolveInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->queryIntentActivities(Landroid/content/Intent;Landroid/content/pm/PackageManager$ResolveInfoFlags;)Ljava/util/List;");
    }

    public List<ResolveInfo> queryIntentActivities(Intent arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->queryIntentActivities(Landroid/content/Intent;I)Ljava/util/List;");
    }

    public List<ResolveInfo> queryIntentActivityOptions(ComponentName arg0, Intent[] arg1, Intent arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->queryIntentActivityOptions(Landroid/content/ComponentName;[Landroid/content/Intent;Landroid/content/Intent;I)Ljava/util/List;");
    }

    public List<ResolveInfo> queryIntentActivityOptions(ComponentName arg0, List<Intent> arg1, Intent arg2, PackageManager.ResolveInfoFlags arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->queryIntentActivityOptions(Landroid/content/ComponentName;Ljava/util/List;Landroid/content/Intent;Landroid/content/pm/PackageManager$ResolveInfoFlags;)Ljava/util/List;");
    }

    public List<ResolveInfo> queryIntentContentProviders(Intent arg0, PackageManager.ResolveInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->queryIntentContentProviders(Landroid/content/Intent;Landroid/content/pm/PackageManager$ResolveInfoFlags;)Ljava/util/List;");
    }

    public List<ResolveInfo> queryIntentContentProviders(Intent arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->queryIntentContentProviders(Landroid/content/Intent;I)Ljava/util/List;");
    }

    public List<ResolveInfo> queryIntentServices(Intent arg0, PackageManager.ResolveInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->queryIntentServices(Landroid/content/Intent;Landroid/content/pm/PackageManager$ResolveInfoFlags;)Ljava/util/List;");
    }

    public List<ResolveInfo> queryIntentServices(Intent arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->queryIntentServices(Landroid/content/Intent;I)Ljava/util/List;");
    }

    public List<PermissionInfo> queryPermissionsByGroup(String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->queryPermissionsByGroup(Ljava/lang/String;I)Ljava/util/List;");
    }

    public List<PackageManager.Property> queryProviderProperty(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->queryProviderProperty(Ljava/lang/String;)Ljava/util/List;");
    }

    public List<PackageManager.Property> queryReceiverProperty(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->queryReceiverProperty(Ljava/lang/String;)Ljava/util/List;");
    }

    public List<PackageManager.Property> queryServiceProperty(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->queryServiceProperty(Ljava/lang/String;)Ljava/util/List;");
    }

    public void relinquishUpdateOwnership(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->relinquishUpdateOwnership(Ljava/lang/String;)V");
    }

    public void removePackageFromPreferred(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->removePackageFromPreferred(Ljava/lang/String;)V");
    }

    public void removePermission(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->removePermission(Ljava/lang/String;)V");
    }

    public boolean removeWhitelistedRestrictedPermission(String arg0, String arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->removeWhitelistedRestrictedPermission(Ljava/lang/String;Ljava/lang/String;I)Z");
    }

    public void requestChecksums(String arg0, boolean arg1, int arg2, List<Certificate> arg3, PackageManager.OnChecksumsReadyListener arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->requestChecksums(Ljava/lang/String;ZILjava/util/List;Landroid/content/pm/PackageManager$OnChecksumsReadyListener;)V");
    }

    public ResolveInfo resolveActivity(Intent arg0, PackageManager.ResolveInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->resolveActivity(Landroid/content/Intent;Landroid/content/pm/PackageManager$ResolveInfoFlags;)Landroid/content/pm/ResolveInfo;");
    }

    public ResolveInfo resolveActivity(Intent arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->resolveActivity(Landroid/content/Intent;I)Landroid/content/pm/ResolveInfo;");
    }

    public ProviderInfo resolveContentProvider(String arg0, PackageManager.ComponentInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->resolveContentProvider(Ljava/lang/String;Landroid/content/pm/PackageManager$ComponentInfoFlags;)Landroid/content/pm/ProviderInfo;");
    }

    public ProviderInfo resolveContentProvider(String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->resolveContentProvider(Ljava/lang/String;I)Landroid/content/pm/ProviderInfo;");
    }

    public ResolveInfo resolveService(Intent arg0, PackageManager.ResolveInfoFlags arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->resolveService(Landroid/content/Intent;Landroid/content/pm/PackageManager$ResolveInfoFlags;)Landroid/content/pm/ResolveInfo;");
    }

    public ResolveInfo resolveService(Intent arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->resolveService(Landroid/content/Intent;I)Landroid/content/pm/ResolveInfo;");
    }

    public void setApplicationCategoryHint(String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->setApplicationCategoryHint(Ljava/lang/String;I)V");
    }

    public void setApplicationEnabledSetting(String arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->setApplicationEnabledSetting(Ljava/lang/String;II)V");
    }

    public boolean setAutoRevokeWhitelisted(String arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->setAutoRevokeWhitelisted(Ljava/lang/String;Z)Z");
    }

    public void setComponentEnabledSetting(ComponentName arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->setComponentEnabledSetting(Landroid/content/ComponentName;II)V");
    }

    public void setComponentEnabledSettings(List<PackageManager.ComponentEnabledSetting> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->setComponentEnabledSettings(Ljava/util/List;)V");
    }

    public void setInstallerPackageName(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->setInstallerPackageName(Ljava/lang/String;Ljava/lang/String;)V");
    }

    public void setMimeGroup(String arg0, Set<String> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->setMimeGroup(Ljava/lang/String;Ljava/util/Set;)V");
    }

    public void updateInstantAppCookie(byte[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->updateInstantAppCookie([B)V");
    }

    public void verifyPendingInstall(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager;->verifyPendingInstall(II)V");
    }
        public static class ApplicationInfoFlags {

        public long getValue() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$ApplicationInfoFlags;->getValue()J");
        }

        public static PackageManager.ApplicationInfoFlags of(long arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$ApplicationInfoFlags;->of(J)Landroid/content/pm/PackageManager$ApplicationInfoFlags;");
        }
        }
        public static class ComponentEnabledSetting implements Parcelable {

        public static Parcelable.Creator CREATOR;

        public ComponentEnabledSetting(ComponentName arg0, int arg1, int arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$ComponentEnabledSetting;-><init>(Landroid/content/ComponentName;II)V");
        }

        public int describeContents() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$ComponentEnabledSetting;->describeContents()I");
        }

        public ComponentName getComponentName() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$ComponentEnabledSetting;->getComponentName()Landroid/content/ComponentName;");
        }

        public int getEnabledFlags() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$ComponentEnabledSetting;->getEnabledFlags()I");
        }

        public int getEnabledState() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$ComponentEnabledSetting;->getEnabledState()I");
        }

        public void writeToParcel(Parcel arg0, int arg1) {
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

        public static PackageManager.ComponentInfoFlags of(long arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$ComponentInfoFlags;->of(J)Landroid/content/pm/PackageManager$ComponentInfoFlags;");
        }
        }
        public static interface OnChecksumsReadyListener {

        public abstract void onChecksumsReady(List<ApkChecksum> arg0);
        }
        public static class PackageInfoFlags {

        public long getValue() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$PackageInfoFlags;->getValue()J");
        }

        public static PackageManager.PackageInfoFlags of(long arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$PackageInfoFlags;->of(J)Landroid/content/pm/PackageManager$PackageInfoFlags;");
        }
        }
        public static class Property implements Parcelable {

        public static Parcelable.Creator CREATOR;

        public int describeContents() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$Property;->describeContents()I");
        }

        public boolean equals(Object arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$Property;->equals(Ljava/lang/Object;)Z");
        }

        public boolean getBoolean() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$Property;->getBoolean()Z");
        }

        public String getClassName() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$Property;->getClassName()Ljava/lang/String;");
        }

        public float getFloat() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$Property;->getFloat()F");
        }

        public int getInteger() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$Property;->getInteger()I");
        }

        public String getName() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$Property;->getName()Ljava/lang/String;");
        }

        public String getPackageName() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$Property;->getPackageName()Ljava/lang/String;");
        }

        public int getResourceId() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$Property;->getResourceId()I");
        }

        public String getString() {
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

        public void writeToParcel(Parcel arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$Property;->writeToParcel(Landroid/os/Parcel;I)V");
        }
        }
        public static class ResolveInfoFlags {

        public long getValue() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$ResolveInfoFlags;->getValue()J");
        }

        public static PackageManager.ResolveInfoFlags of(long arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageManager$ResolveInfoFlags;->of(J)Landroid/content/pm/PackageManager$ResolveInfoFlags;");
        }
        }
    }
