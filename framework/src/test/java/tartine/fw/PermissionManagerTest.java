package tartine.fw;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Consent, persistence, PackageManager, and storage-gate pins. */
class PermissionManagerTest {
    private static final String PKG = "com.example.app";
    private static final String CAMERA = "android.permission.CAMERA";
    private static final String INTERNET = "android.permission.INTERNET";
    private static final String READ_STORAGE =
            "android.permission.READ_EXTERNAL_STORAGE";
    private static final String WRITE_STORAGE =
            "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final String MANAGE_STORAGE =
            "android.permission.MANAGE_EXTERNAL_STORAGE";

    @TempDir
    Path tempDir;

    @AfterEach
    void resetManager() {
        PermissionManager.reset();
    }

    private static PermissionPrompt answer(PermissionPrompt.Decision decision,
            AtomicInteger calls) {
        return (packageName, permission, label) -> {
            calls.incrementAndGet();
            return decision;
        };
    }

    @Test
    void unconfiguredManagerAnswersGranted() {
        assertFalse(PermissionManager.isConfigured());
        assertEquals(PackageManager.PERMISSION_GRANTED,
                PermissionManager.check(CAMERA));
        assertEquals(PackageManager.PERMISSION_GRANTED,
                PermissionManager.ensure(CAMERA));
        assertTrue(PermissionManager.storageAllowed("/sdcard/Download/a", false));
    }

    @Test
    void declaredNormalAndAppDefinedPermissionsAreGranted() {
        PermissionManager.configure(PKG, List.of(INTERNET, "com.example.CUSTOM"),
                36, null);

        assertEquals(PackageManager.PERMISSION_GRANTED,
                PermissionManager.check(INTERNET));
        assertEquals(PackageManager.PERMISSION_GRANTED,
                PermissionManager.check("com.example.CUSTOM"));
        assertEquals(PackageManager.PERMISSION_GRANTED,
                PermissionManager.ensure("com.example.CUSTOM"));
    }

    @Test
    void undeclaredAndSignaturePermissionsStayDenied() {
        PermissionManager.configure(PKG, List.of("android.permission.SYSTEM_ALERT_WINDOW"),
                36, null);
        AtomicInteger calls = new AtomicInteger();
        PermissionManager.setPrompt(answer(PermissionPrompt.Decision.GRANTED, calls));

        assertEquals(PackageManager.PERMISSION_DENIED,
                PermissionManager.check(CAMERA));
        assertEquals(PackageManager.PERMISSION_DENIED,
                PermissionManager.ensure(CAMERA));
        assertEquals(PackageManager.PERMISSION_DENIED,
                PermissionManager.check("android.permission.SYSTEM_ALERT_WINDOW"));
        assertEquals(PackageManager.PERMISSION_DENIED,
                PermissionManager.ensure("android.permission.SYSTEM_ALERT_WINDOW"));
        assertEquals(0, calls.get());
    }

    @Test
    void dangerousGrantIsPersistedAndReloaded() throws Exception {
        Path store = tempDir.resolve("permissions").resolve(PKG + ".properties");
        PermissionManager.configure(PKG, List.of(CAMERA), 36, store);
        AtomicInteger calls = new AtomicInteger();
        PermissionManager.setPrompt(answer(PermissionPrompt.Decision.GRANTED, calls));

        assertEquals(PackageManager.PERMISSION_DENIED,
                PermissionManager.check(CAMERA));
        assertEquals(PackageManager.PERMISSION_GRANTED,
                PermissionManager.ensure(CAMERA));
        assertEquals(1, calls.get());
        assertEquals(PackageManager.PERMISSION_GRANTED,
                PermissionManager.check(CAMERA));

        Properties stored = new Properties();
        try (InputStream in = Files.newInputStream(store)) {
            stored.load(in);
        }
        assertEquals("granted", stored.getProperty(CAMERA));

        // A later process (fresh reset) loads the grant without prompting.
        PermissionManager.reset();
        PermissionManager.configure(PKG, List.of(CAMERA), 36, store);
        PermissionManager.setPrompt((packageName, permission, label) -> {
            throw new AssertionError("persisted grant must not prompt");
        });
        assertEquals(PackageManager.PERMISSION_GRANTED,
                PermissionManager.ensure(CAMERA));
    }

    @Test
    void deniedDecisionIsRemembered() {
        PermissionManager.configure(PKG, List.of(CAMERA), 36, null);
        AtomicInteger calls = new AtomicInteger();
        PermissionManager.setPrompt(answer(PermissionPrompt.Decision.DENIED, calls));

        assertEquals(PackageManager.PERMISSION_DENIED,
                PermissionManager.ensure(CAMERA));
        assertEquals(PackageManager.PERMISSION_DENIED,
                PermissionManager.ensure(CAMERA));
        assertEquals(1, calls.get());
        assertTrue(PermissionManager.wasDenied(CAMERA));
    }

    @Test
    void unavailablePromptLeavesTheDecisionOpen() {
        PermissionManager.configure(PKG, List.of(CAMERA), 36, null);
        AtomicInteger calls = new AtomicInteger();
        PermissionManager.setPrompt(answer(
                PermissionPrompt.Decision.UNAVAILABLE, calls));

        assertEquals(PackageManager.PERMISSION_DENIED,
                PermissionManager.ensure(CAMERA));
        assertFalse(PermissionManager.wasDenied(CAMERA));

        PermissionManager.setPrompt(answer(PermissionPrompt.Decision.GRANTED, calls));
        assertEquals(PackageManager.PERMISSION_GRANTED,
                PermissionManager.ensure(CAMERA));
        assertEquals(2, calls.get());
    }

    @Test
    void pre23AppsGetDeclaredDangerousPermissionsAtInstall() {
        PermissionManager.configure(PKG, List.of(CAMERA), 22, null);
        PermissionManager.setPrompt((packageName, permission, label) -> {
            throw new AssertionError("pre-23 install grant must not prompt");
        });

        assertEquals(PackageManager.PERMISSION_GRANTED,
                PermissionManager.check(CAMERA));
        assertEquals(PackageManager.PERMISSION_GRANTED,
                PermissionManager.ensure(CAMERA));
    }

    @Test
    void sharedStorageNeedsTheDeclaredStorageGrant() {
        PermissionManager.configure(PKG, List.of(READ_STORAGE), 36, null);
        AtomicInteger calls = new AtomicInteger();
        PermissionManager.setPrompt(answer(PermissionPrompt.Decision.GRANTED, calls));

        assertTrue(PermissionManager.storageAllowed("/sdcard/Download/a.txt", false));
        assertFalse(PermissionManager.storageAllowed("/sdcard/Download/a.txt", true));
        assertTrue(PermissionManager.storageAllowed(
                "/storage/emulated/0/Pictures/a.png", false));
        assertTrue(PermissionManager.storageAllowed(
                "/data/data/" + PKG + "/files/a.txt", false));
        assertEquals(1, calls.get());
    }

    @Test
    void appSpecificExternalDirectoriesNeedNoStorageGrant() {
        PermissionManager.configure(PKG, List.of(), 36, null);
        PermissionManager.setPrompt((packageName, permission, label) -> {
            throw new AssertionError("app-specific external path must not prompt");
        });

        for (String area : List.of("data", "media", "obb")) {
            String path = "/sdcard/Android/" + area + "/" + PKG + "/file";
            assertTrue(PermissionManager.storageAllowed(path, false));
            assertTrue(PermissionManager.storageAllowed(path, true));
        }
        assertFalse(PermissionManager.storageAllowed(
                "/sdcard/Android/data/other.app/file", false));
    }

    @Test
    void manageExternalStorageGrantsReadAndWrite() {
        PermissionManager.configure(PKG, List.of(MANAGE_STORAGE), 36, null);
        PermissionManager.setPrompt(answer(PermissionPrompt.Decision.GRANTED,
                new AtomicInteger()));

        assertTrue(PermissionManager.storageAllowed("/sdcard/Documents/a", false));
        assertTrue(PermissionManager.storageAllowed("/sdcard/Documents/a", true));
    }

    @Test
    void packageManagerAnswersPermissionQueries() throws Exception {
        PermissionManager.configure(PKG, List.of(INTERNET, CAMERA), 36, null);
        PermissionManager.setPrompt(answer(PermissionPrompt.Decision.GRANTED,
                new AtomicInteger()));
        PackageManager pm = new PackageManager();

        assertEquals(PackageManager.PERMISSION_GRANTED,
                pm.checkPermission(INTERNET, PKG));
        assertEquals(PackageManager.PERMISSION_DENIED,
                pm.checkPermission(CAMERA, PKG));
        assertEquals(PackageManager.PERMISSION_DENIED,
                pm.checkPermission(INTERNET, "other.app"));

        PackageInfo info = pm.getPackageInfo(PKG, PackageManager.GET_PERMISSIONS);
        assertEquals(PKG, info.packageName);
        assertEquals(List.of(INTERNET, CAMERA),
                List.of(info.requestedPermissions));
        assertEquals(PackageInfo.REQUESTED_PERMISSION_GRANTED,
                info.requestedPermissionsFlags[0]);
        assertEquals(0, info.requestedPermissionsFlags[1]);

        assertThrows(PackageManager.NameNotFoundException.class,
                () -> pm.getPackageInfo("other.app", 0));
        assertEquals(36, pm.getTargetSdkVersion(PKG));

        PermissionInfo camera = pm.getPermissionInfo(CAMERA, 0);
        assertEquals(PermissionInfo.PROTECTION_DANGEROUS,
                camera.getProtection());
        assertEquals("android.permission-group.CAMERA", camera.group);
        PermissionGroupInfo storage = pm.getPermissionGroupInfo(
                "android.permission-group.STORAGE", 0);
        assertNotNull(storage);
        assertEquals("Storage", storage.nonLocalizedLabel);
        assertFalse(pm.getAllPermissionGroups(0).isEmpty());
        assertTrue(pm.queryPermissionsByGroup(
                "android.permission-group.STORAGE", 0).stream()
                .anyMatch(p -> p.name.equals(READ_STORAGE)));

        assertEquals(1, pm.getPackagesHoldingPermissions(
                new String[] {INTERNET}, 0).size());
        assertTrue(pm.getPackagesHoldingPermissions(
                new String[] {CAMERA}, 0).isEmpty());
        assertFalse(pm.isPermissionRevokedByPolicy(CAMERA, PKG));
    }

    @Test
    void contextExposesTheConfiguredPackageAndChecks() {
        PermissionManager.configure(PKG, List.of(CAMERA), 36, null);
        Activity activity = new Activity();

        assertEquals(PKG, activity.getPackageName());
        assertEquals(PKG, activity.getOpPackageName());
        assertEquals(PackageManager.PERMISSION_DENIED,
                activity.checkSelfPermission(CAMERA));
        assertNotNull(activity.getPackageManager());
        assertEquals(PackageManager.PERMISSION_DENIED,
                activity.checkCallingOrSelfPermission(CAMERA));
        assertThrows(SecurityException.class,
                () -> activity.enforceCallingOrSelfPermission(CAMERA, "denied"));
    }

    @Test
    void requestPermissionsDeliversTheAndroidCallback() {
        PermissionManager.configure(PKG, List.of(CAMERA), 36, null);
        PermissionManager.setPrompt(answer(PermissionPrompt.Decision.GRANTED,
                new AtomicInteger()));
        RecordingActivity activity = new RecordingActivity();

        activity.requestPermissions(new String[] {CAMERA}, 42);

        assertEquals(42, activity.requestCode);
        assertEquals(List.of(CAMERA), List.of(activity.permissions));
        assertEquals(PackageManager.PERMISSION_GRANTED, activity.grantResults[0]);
    }

    @Test
    void catalogDescribesKnownAndDeclaredPermissions() {
        PermissionManager.configure(PKG, List.of("com.example.CUSTOM"), 36, null);

        assertEquals("the camera", PermissionManager.describe(CAMERA));
        assertEquals("custom", PermissionManager.describe("com.example.CUSTOM"));
        assertNull(PermissionManager.groupOf("com.example.CUSTOM"));
        assertEquals("android.permission-group.STORAGE",
                PermissionManager.groupOf(READ_STORAGE));
        assertTrue(PermissionManager.knownPermissions().contains(CAMERA));
        assertTrue(PermissionManager.permissionGroups().contains(
                "android.permission-group.STORAGE"));
    }

    private static final class RecordingActivity extends Activity {
        private int requestCode = -1;
        private String[] permissions;
        private int[] grantResults;

        @Override
        public void onRequestPermissionsResult(int code, String[] requested,
                int[] results) {
            requestCode = code;
            permissions = requested.clone();
            grantResults = results.clone();
        }
    }
}
