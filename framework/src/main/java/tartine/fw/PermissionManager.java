package tartine.fw;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Guest permission table and consent coordinator.
 *
 * <p>Android permission state for one installed app: the manifest's
 * requested permissions, the user's persisted decisions, and the runtime
 * API surface behind {@code Context.checkSelfPermission},
 * {@code Activity.requestPermissions}, and {@code PackageManager}. Init
 * happens in the host before the activity launches; with no app configured
 * the manager answers granted, which is the framework-test and bare-library
 * default.
 *
 * <p>Consent is not enforcement (ADR-0012): the prompts and the store make
 * cooperative apps see the Android experience, while a crafted app can still
 * reach host APIs directly. Dangerous permissions prompt through the
 * {@link PermissionPrompt} seam on first use or on an explicit request;
 * headless runs leave it unset and answer denied. Declared normal permissions
 * (and app-defined permissions the catalog does not know) are granted at
 * install time, and signature permissions stay denied. Apps targeting API 22
 * or older get their declared dangerous permissions at install time, matching
 * the pre-runtime-permission model.
 *
 * <p>The shared-storage gate rides on the same table: reads under
 * {@code /sdcard} and {@code /storage/emulated/0} need a granted storage
 * permission, writes need a write or all-files grant, and the app's own
 * {@code Android/data}, {@code Android/media}, and {@code Android/obb}
 * directories stay free, as on a device.
 */
public final class PermissionManager {
    private static final Logger log =
            Logger.getLogger(PermissionManager.class.getName());

    private static final Object LOCK = new Object();

    private static boolean configured;
    private static String packageName = "";
    private static Set<String> declared = Collections.emptySet();
    private static int targetSdk = 1;
    private static Path storePath;
    /** Permission name -> true (granted) / false (asked and denied). */
    private static final Map<String, Boolean> decisions = new HashMap<>();
    private static PermissionPrompt prompt;

    private static final String[] STORAGE_PREFIXES = {
        "/sdcard", "/storage/emulated/0"
    };

    private PermissionManager() {
    }

    /**
     * Installs the app's permission state. The grant store is the host path
     * of the per-package decisions file; null keeps them in memory.
     */
    public static void configure(String appPackage,
            Collection<String> requested, int appTargetSdk, Path store) {
        synchronized (LOCK) {
            configured = true;
            packageName = appPackage == null ? "" : appPackage;
            declared = Collections.unmodifiableSet(
                    new LinkedHashSet<>(requested == null
                            ? Collections.<String>emptyList() : requested));
            targetSdk = Math.max(1, appTargetSdk);
            storePath = store;
            decisions.clear();
            loadLocked();
        }
    }

    /** Test seam: forget the app and every in-memory decision. */
    public static void reset() {
        synchronized (LOCK) {
            configured = false;
            packageName = "";
            declared = Collections.emptySet();
            targetSdk = 1;
            storePath = null;
            decisions.clear();
            prompt = null;
        }
    }

    /** Installs or clears the host consent seam. */
    public static void setPrompt(PermissionPrompt consentPrompt) {
        synchronized (LOCK) {
            prompt = consentPrompt;
        }
    }

    /** The configured package, or the empty string before launch. */
    public static String packageName() {
        synchronized (LOCK) {
            return packageName;
        }
    }

    /** Whether an app has been configured for this run. */
    public static boolean isConfigured() {
        synchronized (LOCK) {
            return configured;
        }
    }

    /** The manifest's requested permissions, in document order. */
    public static List<String> declaredPermissions() {
        synchronized (LOCK) {
            return List.copyOf(declared);
        }
    }

    /**
     * State check without any prompting: granted for normal/declared
     * permissions, the persisted decision for dangerous ones, denied for
     * undeclared and signature permissions.
     */
    public static int check(String permission) {
        synchronized (LOCK) {
            return checkLocked(permission);
        }
    }

    /**
     * State check that asks the host on a dangerous permission whose user
     * decision is still unknown. Unavailable prompts leave the decision
     * unrecorded so a later GUI run can ask.
     */
    public static int ensure(String permission) {
        synchronized (LOCK) {
            return ensureLocked(permission);
        }
    }

    /**
     * Grants when any of {@code permissions} is granted, otherwise prompts
     * for the first declared, undecided dangerous one. Used by the
     * shared-storage gate, where a legacy read grant and the scoped media
     * grants are alternatives.
     */
    public static int ensureAny(String... permissions) {
        synchronized (LOCK) {
            return ensureAnyLocked(permissions);
        }
    }

    /** Checks a permission for {@code appPackage} (PackageManager API). */
    public static int checkForPackage(String appPackage, String permission) {
        synchronized (LOCK) {
            if (appPackage != null && !appPackage.isEmpty()
                    && !appPackage.equals(packageName)) {
                return PackageManager.PERMISSION_DENIED;
            }
            return checkLocked(permission);
        }
    }

    /**
     * Implements {@code Activity.requestPermissions}: resolve every entry,
     * then deliver the Android-shaped result on the same call (the desktop
     * Handler delivers inline).
     */
    public static void request(Activity activity, String[] permissions,
            int requestCode) {
        if (activity == null) {
            throw new NullPointerException("activity");
        }
        String[] requested = permissions == null
                ? new String[0] : permissions.clone();
        int[] results = new int[requested.length];
        synchronized (LOCK) {
            for (int i = 0; i < requested.length; i++) {
                results[i] = ensureLocked(requested[i]);
            }
        }
        activity.onRequestPermissionsResult(requestCode, requested, results);
    }

    /**
     * Whether a guest path may be accessed under the shared-storage model.
     * App-private paths and unmapped/other mounts answer true; the Vfs
     * bridge owns the path mapping. A denied shared path is a guest-visible
     * permission failure, never a host fallback.
     */
    public static boolean storageAllowed(String guestPath, boolean write) {
        synchronized (LOCK) {
            if (!configured || guestPath == null
                    || !isSharedStorageLocked(guestPath)) {
                return true;
            }
            if (write) {
                return ensureAnyLocked("android.permission.WRITE_EXTERNAL_STORAGE",
                        "android.permission.MANAGE_EXTERNAL_STORAGE")
                        == PackageManager.PERMISSION_GRANTED;
            }
            return ensureAnyLocked("android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.READ_MEDIA_IMAGES",
                    "android.permission.READ_MEDIA_VIDEO",
                    "android.permission.READ_MEDIA_AUDIO",
                    "android.permission.MANAGE_EXTERNAL_STORAGE")
                    == PackageManager.PERMISSION_GRANTED;
        }
    }

    /** Whether the user has already declined {@code permission}. */
    public static boolean wasDenied(String permission) {
        synchronized (LOCK) {
            return Boolean.FALSE.equals(decisions.get(permission));
        }
    }

    /** Human-readable capability name for a prompt. */
    public static String describe(String permission) {
        PermissionCatalog.Entry entry = PermissionCatalog.find(permission);
        if (entry != null) {
            return entry.label();
        }
        String name = permission == null ? "a permission" : permission;
        int dot = name.lastIndexOf('.');
        String shortName = dot < 0 ? name : name.substring(dot + 1);
        return shortName.toLowerCase(Locale.ROOT).replace('_', ' ');
    }

    private static String groupLabel(String group) {
        int dot = group.lastIndexOf('.');
        String shortName = dot < 0 ? group : group.substring(dot + 1);
        switch (shortName) {
            case "STORAGE":
                return "Storage";
            case "CAMERA":
                return "Camera";
            case "MICROPHONE":
                return "Microphone";
            case "LOCATION":
                return "Location";
            case "CONTACTS":
                return "Contacts";
            case "CALENDAR":
                return "Calendar";
            case "CALL_LOG":
                return "Call log";
            case "PHONE":
                return "Phone";
            case "SENSORS":
                return "Sensors";
            case "SMS":
                return "SMS";
            case "NEARBY_DEVICES":
                return "Nearby devices";
            case "NOTIFICATIONS":
                return "Notifications";
            case "ACTIVITY_RECOGNITION":
                return "Physical activity";
            case "NETWORK":
                return "Network";
            default:
                return "Other";
        }
    }

    /** Android protection level for {@code permission}. */
    public static int protectionLevel(String permission) {
        PermissionCatalog.Entry entry = PermissionCatalog.find(permission);
        if (entry == null || entry.protection() == PermissionCatalog.NORMAL) {
            return PermissionInfo.PROTECTION_NORMAL;
        }
        if (entry.protection() == PermissionCatalog.DANGEROUS) {
            return PermissionInfo.PROTECTION_DANGEROUS;
        }
        return PermissionInfo.PROTECTION_SIGNATURE;
    }

    /** Android group name for {@code permission}, or null when unlisted. */
    public static String groupOf(String permission) {
        PermissionCatalog.Entry entry = PermissionCatalog.find(permission);
        return entry == null ? null : entry.group();
    }

    /** Every permission the catalog knows, platform names included. */
    public static Set<String> knownPermissions() {
        return PermissionCatalog.entries().keySet();
    }

    /** The app's target SDK level. */
    public static int targetSdk() {
        synchronized (LOCK) {
            return targetSdk;
        }
    }

    /** Catalog groups in declaration order. */
    public static List<String> permissionGroups() {
        Set<String> groups = new LinkedHashSet<>();
        for (PermissionCatalog.Entry entry : PermissionCatalog.entries().values()) {
            groups.add(entry.group());
        }
        return List.copyOf(groups);
    }

    /**
     * A {@link PermissionInfo} for a platform catalog entry or a declared
     * app-defined permission; null when the name is unknown to both.
     */
    public static PermissionInfo permissionInfo(String permission) {
        synchronized (LOCK) {
            if (permission == null) {
                return null;
            }
            PermissionCatalog.Entry entry = PermissionCatalog.find(permission);
            if (entry == null && !declared.contains(permission)) {
                return null;
            }
            PermissionInfo info = new PermissionInfo();
            info.name = permission;
            info.packageName = entry == null ? packageName : "android";
            info.protectionLevel = protectionLevel(permission);
            info.group = entry == null ? null : entry.group();
            info.nonLocalizedDescription = describe(permission);
            return info;
        }
    }

    /** A {@link PermissionGroupInfo} for a catalog group, or null. */
    public static PermissionGroupInfo permissionGroupInfo(String group) {
        if (group == null) {
            return null;
        }
        for (PermissionCatalog.Entry entry : PermissionCatalog.entries().values()) {
            if (group.equals(entry.group())) {
                PermissionGroupInfo info = new PermissionGroupInfo();
                info.name = group;
                info.packageName = "android";
                info.nonLocalizedLabel = groupLabel(group);
                return info;
            }
        }
        return null;
    }

    // -----------------------------------------------------------------
    // Internal state (all callers hold LOCK)
    // -----------------------------------------------------------------

    private static int checkLocked(String permission) {
        if (!configured) {
            return PackageManager.PERMISSION_GRANTED;
        }
        if (permission == null || !declared.contains(permission)) {
            return PackageManager.PERMISSION_DENIED;
        }
        PermissionCatalog.Entry entry = PermissionCatalog.find(permission);
        if (entry == null || entry.protection() == PermissionCatalog.NORMAL) {
            return PackageManager.PERMISSION_GRANTED;
        }
        if (entry.protection() == PermissionCatalog.SIGNATURE) {
            return PackageManager.PERMISSION_DENIED;
        }
        if (targetSdk < 23) {
            return PackageManager.PERMISSION_GRANTED;
        }
        return Boolean.TRUE.equals(decisions.get(permission))
                ? PackageManager.PERMISSION_GRANTED
                : PackageManager.PERMISSION_DENIED;
    }

    private static int ensureLocked(String permission) {
        int state = checkLocked(permission);
        if (state == PackageManager.PERMISSION_GRANTED) {
            return state;
        }
        if (!configured || permission == null
                || !declared.contains(permission)) {
            return PackageManager.PERMISSION_DENIED;
        }
        PermissionCatalog.Entry entry = PermissionCatalog.find(permission);
        if (entry == null) {
            return PackageManager.PERMISSION_GRANTED; // app-defined normal
        }
        if (entry.protection() != PermissionCatalog.DANGEROUS) {
            return PackageManager.PERMISSION_DENIED; // signature stays denied
        }
        if (targetSdk < 23) {
            return PackageManager.PERMISSION_GRANTED;
        }
        Boolean decided = decisions.get(permission);
        if (decided != null) {
            return decided ? PackageManager.PERMISSION_GRANTED
                    : PackageManager.PERMISSION_DENIED;
        }
        PermissionPrompt consentPrompt = prompt;
        if (consentPrompt == null) {
            return PackageManager.PERMISSION_DENIED;
        }
        PermissionPrompt.Decision decision = consentPrompt.request(packageName,
                permission, describe(permission));
        if (decision == PermissionPrompt.Decision.GRANTED) {
            recordLocked(permission, true);
            return PackageManager.PERMISSION_GRANTED;
        }
        if (decision == PermissionPrompt.Decision.DENIED) {
            recordLocked(permission, false);
        }
        return PackageManager.PERMISSION_DENIED;
    }

    private static int ensureAnyLocked(String... permissions) {
        if (!configured) {
            return PackageManager.PERMISSION_GRANTED;
        }
        for (String permission : permissions) {
            if (checkLocked(permission) == PackageManager.PERMISSION_GRANTED) {
                return PackageManager.PERMISSION_GRANTED;
            }
        }
        for (String permission : permissions) {
            if (!declared.contains(permission)) {
                continue;
            }
            PermissionCatalog.Entry entry = PermissionCatalog.find(permission);
            if (entry == null) {
                return PackageManager.PERMISSION_GRANTED; // app-defined normal
            }
            if (entry.protection() != PermissionCatalog.DANGEROUS) {
                continue;
            }
            if (targetSdk < 23) {
                return PackageManager.PERMISSION_GRANTED;
            }
            if (decisions.containsKey(permission)) {
                continue;
            }
            return ensureLocked(permission);
        }
        return PackageManager.PERMISSION_DENIED;
    }

    private static boolean isSharedStorageLocked(String guestPath) {
        String path = guestPath.replace('\\', '/');
        String prefix = null;
        for (String candidate : STORAGE_PREFIXES) {
            if (path.equals(candidate) || path.startsWith(candidate + "/")) {
                prefix = candidate;
                break;
            }
        }
        if (prefix == null) {
            return false;
        }
        for (String area : new String[] {"data", "media", "obb"}) {
            String base = prefix + "/Android/" + area + "/" + packageName;
            if (path.equals(base) || path.startsWith(base + "/")) {
                return false;
            }
        }
        return true;
    }

    private static void recordLocked(String permission, boolean granted) {
        decisions.put(permission, granted);
        saveLocked();
    }

    private static void loadLocked() {
        if (storePath == null || !Files.exists(storePath)) {
            return;
        }
        Properties stored = new Properties();
        try (InputStream in = Files.newInputStream(storePath)) {
            stored.load(in);
        } catch (IOException | IllegalArgumentException e) {
            log.warning("permission store unreadable (" + storePath + "): "
                    + e);
            return;
        }
        for (String name : stored.stringPropertyNames()) {
            String value = stored.getProperty(name);
            if ("granted".equals(value)) {
                decisions.put(name, Boolean.TRUE);
            } else if ("denied".equals(value)) {
                decisions.put(name, Boolean.FALSE);
            }
        }
    }

    private static void saveLocked() {
        if (storePath == null) {
            return;
        }
        Properties stored = new Properties();
        for (Map.Entry<String, Boolean> decision : decisions.entrySet()) {
            stored.setProperty(decision.getKey(),
                    decision.getValue() ? "granted" : "denied");
        }
        Path parent = storePath.toAbsolutePath().getParent();
        Path temp = storePath.resolveSibling(
                storePath.getFileName().toString() + ".tmp");
        try {
            if (parent != null) {
                Files.createDirectories(parent);
            }
            try (OutputStream out = Files.newOutputStream(temp)) {
                stored.store(out, "Tartine permission decisions");
            }
            try {
                Files.move(temp, storePath,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temp, storePath,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            log.warning("permission store write failed (" + storePath + "): "
                    + e);
        }
    }
}
