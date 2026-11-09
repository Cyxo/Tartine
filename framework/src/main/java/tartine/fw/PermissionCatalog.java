package tartine.fw;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Protection table for the Android permissions the runtime models.
 *
 * <p>Only permissions that need a decision beyond "declared in the manifest"
 * are listed. A declared permission the table does not know is treated as a
 * normal (install-granted) permission, which matches app-defined normal
 * permissions; undeclared permissions stay denied. Dangerous entries prompt
 * through the host consent seam, and signature entries stay denied.
 */
final class PermissionCatalog {
    static final int NORMAL = 0;
    static final int DANGEROUS = 1;
    static final int SIGNATURE = 2;

    /** One catalog row: group, protection level, and prompt label. */
    record Entry(String group, int protection, String label) {
    }

    private static final Map<String, Entry> ENTRIES = build();

    private PermissionCatalog() {
    }

    /** Catalog row for {@code permission}, or null when unlisted. */
    static Entry find(String permission) {
        return permission == null ? null : ENTRIES.get(permission);
    }

    /** Every catalog row, in declaration order (platform permissions). */
    static Map<String, Entry> entries() {
        return ENTRIES;
    }

    private static Map<String, Entry> build() {
        Map<String, Entry> map = new LinkedHashMap<>();
        dangerous(map, "READ_EXTERNAL_STORAGE", "STORAGE", "shared storage");
        dangerous(map, "WRITE_EXTERNAL_STORAGE", "STORAGE", "shared storage");
        dangerous(map, "MANAGE_EXTERNAL_STORAGE", "STORAGE", "all files");
        dangerous(map, "READ_MEDIA_IMAGES", "STORAGE", "photos");
        dangerous(map, "READ_MEDIA_VIDEO", "STORAGE", "videos");
        dangerous(map, "READ_MEDIA_AUDIO", "STORAGE", "music and audio");
        dangerous(map, "READ_MEDIA_VISUAL_USER_SELECTED", "STORAGE",
                "selected photos and videos");
        dangerous(map, "ACCESS_MEDIA_LOCATION", "STORAGE",
                "photo location data");
        dangerous(map, "CAMERA", "CAMERA", "the camera");
        dangerous(map, "RECORD_AUDIO", "MICROPHONE", "the microphone");
        dangerous(map, "ACCESS_FINE_LOCATION", "LOCATION", "precise location");
        dangerous(map, "ACCESS_COARSE_LOCATION", "LOCATION",
                "approximate location");
        dangerous(map, "ACCESS_BACKGROUND_LOCATION", "LOCATION",
                "background location");
        dangerous(map, "READ_CONTACTS", "CONTACTS", "contacts");
        dangerous(map, "WRITE_CONTACTS", "CONTACTS", "contacts");
        dangerous(map, "GET_ACCOUNTS", "CONTACTS", "accounts");
        dangerous(map, "READ_CALENDAR", "CALENDAR", "calendar");
        dangerous(map, "WRITE_CALENDAR", "CALENDAR", "calendar");
        dangerous(map, "READ_CALL_LOG", "CALL_LOG", "the call log");
        dangerous(map, "WRITE_CALL_LOG", "CALL_LOG", "the call log");
        dangerous(map, "READ_PHONE_STATE", "PHONE", "phone state");
        dangerous(map, "READ_PHONE_NUMBERS", "PHONE", "phone numbers");
        dangerous(map, "CALL_PHONE", "PHONE", "phone calls");
        dangerous(map, "ANSWER_PHONE_CALLS", "PHONE", "phone calls");
        dangerous(map, "ADD_VOICEMAIL", "PHONE", "voicemail");
        dangerous(map, "USE_SIP", "PHONE", "SIP calls");
        dangerous(map, "PROCESS_OUTGOING_CALLS", "PHONE",
                "outgoing calls");
        dangerous(map, "BODY_SENSORS", "SENSORS", "body sensors");
        dangerous(map, "BODY_SENSORS_BACKGROUND", "SENSORS",
                "body sensors in the background");
        dangerous(map, "SEND_SMS", "SMS", "SMS messages");
        dangerous(map, "RECEIVE_SMS", "SMS", "SMS messages");
        dangerous(map, "READ_SMS", "SMS", "SMS messages");
        dangerous(map, "RECEIVE_MMS", "SMS", "MMS messages");
        dangerous(map, "RECEIVE_WAP_PUSH", "SMS", "WAP push messages");
        dangerous(map, "BLUETOOTH_SCAN", "NEARBY_DEVICES",
                "nearby devices");
        dangerous(map, "BLUETOOTH_CONNECT", "NEARBY_DEVICES",
                "nearby devices");
        dangerous(map, "BLUETOOTH_ADVERTISE", "NEARBY_DEVICES",
                "nearby devices");
        dangerous(map, "NEARBY_WIFI_DEVICES", "NEARBY_DEVICES",
                "nearby Wi-Fi devices");
        dangerous(map, "UWB_RANGING", "NEARBY_DEVICES", "nearby devices");
        dangerous(map, "POST_NOTIFICATIONS", "NOTIFICATIONS",
                "notifications");
        dangerous(map, "ACTIVITY_RECOGNITION", "ACTIVITY_RECOGNITION",
                "physical activity");

        normal(map, "INTERNET", "NETWORK", "network access");
        normal(map, "ACCESS_NETWORK_STATE", "NETWORK", "network state");
        normal(map, "ACCESS_WIFI_STATE", "NETWORK", "Wi-Fi state");
        normal(map, "CHANGE_WIFI_STATE", "NETWORK", "Wi-Fi state");
        normal(map, "CHANGE_NETWORK_STATE", "NETWORK", "network state");
        normal(map, "VIBRATE", "OTHER", "vibration");
        normal(map, "WAKE_LOCK", "OTHER", "wake lock");
        normal(map, "RECEIVE_BOOT_COMPLETED", "OTHER", "startup");
        normal(map, "FOREGROUND_SERVICE", "OTHER", "foreground service");
        normal(map, "SET_WALLPAPER", "OTHER", "wallpaper");
        normal(map, "EXPAND_STATUS_BAR", "OTHER", "status bar");
        normal(map, "NFC", "OTHER", "NFC");
        normal(map, "MODIFY_AUDIO_SETTINGS", "OTHER", "audio settings");
        normal(map, "DISABLE_KEYGUARD", "OTHER", "keyguard");
        normal(map, "RECEIVE_USER_PRESENT", "OTHER", "user presence");
        normal(map, "READ_SYNC_SETTINGS", "OTHER", "sync settings");
        normal(map, "WRITE_SYNC_SETTINGS", "OTHER", "sync settings");
        normal(map, "READ_SYNC_STATS", "OTHER", "sync statistics");
        normal(map, "SET_ALARM", "OTHER", "alarms");
        normal(map, "INSTALL_SHORTCUT", "OTHER", "shortcuts");
        normal(map, "UNINSTALL_SHORTCUT", "OTHER", "shortcuts");
        normal(map, "BLUETOOTH", "OTHER", "Bluetooth");
        normal(map, "BLUETOOTH_ADMIN", "OTHER", "Bluetooth settings");
        normal(map, "REQUEST_IGNORE_BATTERY_OPTIMIZATIONS", "OTHER",
                "battery optimization");
        normal(map, "REQUEST_DELETE_PACKAGES", "OTHER", "package removal");
        normal(map, "QUERY_ALL_PACKAGES", "OTHER", "installed apps");
        normal(map, "ACCESS_NOTIFICATION_POLICY", "OTHER",
                "notification policy");
        normal(map, "HIGH_SAMPLING_RATE_SENSORS", "SENSORS",
                "high-rate sensor data");
        normal(map, "SCHEDULE_EXACT_ALARM", "OTHER", "exact alarms");
        normal(map, "USE_EXACT_ALARM", "OTHER", "exact alarms");

        signature(map, "SYSTEM_ALERT_WINDOW", "OTHER", "over other apps");
        signature(map, "WRITE_SETTINGS", "OTHER", "system settings");
        signature(map, "PACKAGE_USAGE_STATS", "OTHER", "usage statistics");
        signature(map, "READ_LOGS", "OTHER", "system logs");
        signature(map, "DUMP", "OTHER", "system dumps");
        signature(map, "WRITE_SECURE_SETTINGS", "OTHER",
                "secure settings");
        signature(map, "INSTALL_PACKAGES", "OTHER", "package installs");
        signature(map, "DELETE_PACKAGES", "OTHER", "package removal");
        signature(map, "CHANGE_CONFIGURATION", "OTHER",
                "configuration changes");
        signature(map, "REBOOT", "OTHER", "reboot");
        signature(map, "STATUS_BAR", "OTHER", "the status bar");
        signature(map, "MODIFY_PHONE_STATE", "PHONE", "phone state");
        signature(map, "READ_PRIVILEGED_PHONE_STATE", "PHONE",
                "privileged phone state");
        signature(map, "WRITE_APN_SETTINGS", "OTHER", "APN settings");
        signature(map, "READ_FRAME_BUFFER", "OTHER", "the screen buffer");
        signature(map, "CAPTURE_AUDIO_OUTPUT", "MICROPHONE",
                "audio output");
        signature(map, "CAPTURE_VIDEO_OUTPUT", "CAMERA", "video output");
        return Collections.unmodifiableMap(map);
    }

    private static void dangerous(Map<String, Entry> map, String name,
            String group, String label) {
        put(map, name, group, DANGEROUS, label);
    }

    private static void normal(Map<String, Entry> map, String name,
            String group, String label) {
        put(map, name, group, NORMAL, label);
    }

    private static void signature(Map<String, Entry> map, String name,
            String group, String label) {
        put(map, name, group, SIGNATURE, label);
    }

    private static void put(Map<String, Entry> map, String name, String group,
            int protection, String label) {
        map.put("android.permission." + name,
                new Entry("android.permission-group." + group, protection,
                        label));
    }
}
