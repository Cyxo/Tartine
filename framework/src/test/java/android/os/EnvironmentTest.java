package android.os;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.junit.jupiter.api.Test;

/**
 * Pins the shared-storage surface: AOSP resolves the external directory to
 * the primary emulated volume, {@code /storage/emulated/0}, which the Vfs
 * mounts onto the shared card, and reports it as mounted, emulated, and not
 * physically removable. Paths are compared separator-insensitively because
 * the framework answers a plain host {@link File}, whose spelling Windows
 * normalizes.
 */
class EnvironmentTest {

    private static String path(File file) {
        return file.getPath().replace('\\', '/');
    }

    @Test
    void externalStorageDirectoryIsTheEmulatedVolume() {
        assertEquals("/storage/emulated/0",
                path(Environment.getExternalStorageDirectory()));
    }

    @Test
    void publicDirectoryAppendsTheTypeUnderTheExternalDirectory() {
        assertEquals("/storage/emulated/0/Download",
                path(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS)));
    }

    @Test
    void cardIsMountedEmulatedAndNotRemovable() {
        assertEquals(Environment.MEDIA_MOUNTED,
                Environment.getExternalStorageState());
        assertEquals(Environment.MEDIA_MOUNTED,
                Environment.getExternalStorageState(
                        Environment.getExternalStorageDirectory()));
        assertTrue(Environment.isExternalStorageEmulated());
        assertTrue(Environment.isExternalStorageEmulated(
                Environment.getExternalStorageDirectory()));
        assertFalse(Environment.isExternalStorageRemovable());
        assertFalse(Environment.isExternalStorageLegacy());
        assertFalse(Environment.isExternalStorageManager());
    }

    @Test
    void directoryConstantsMatchAosp() {
        assertEquals("Download", Environment.DIRECTORY_DOWNLOADS);
        assertEquals("DCIM", Environment.DIRECTORY_DCIM);
        assertEquals("Documents", Environment.DIRECTORY_DOCUMENTS);
        assertEquals("Pictures", Environment.DIRECTORY_PICTURES);
    }
}
