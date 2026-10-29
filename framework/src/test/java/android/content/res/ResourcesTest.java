package android.content.res;

import android.util.DisplayMetrics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The desktop device Resources mapping: a 480x960 mdpi surface whose
 * Configuration is the live device configuration (not AOSP's "completely
 * undefined" no-arg Configuration). Source:
 * aosp-mirror/platform_frameworks_base
 * core/java/android/content/res/Resources.java (getConfiguration,
 * getDisplayMetrics, getColor) plus Configuration.setToDefaults().
 */
class ResourcesTest {

    @Test
    void servesLiveDeviceConfiguration() {
        Resources resources = new Resources();
        Configuration config = resources.getConfiguration();

        assertEquals(1.0f, config.fontScale);
        assertEquals(160, config.densityDpi);
        assertEquals(480, config.screenWidthDp);
        assertEquals(960, config.screenHeightDp);
        assertEquals(480, config.smallestScreenWidthDp);
        assertEquals(Configuration.ORIENTATION_PORTRAIT, config.orientation);
        assertEquals(Configuration.UI_MODE_TYPE_NORMAL
                | Configuration.UI_MODE_NIGHT_NO, config.uiMode);

        assertEquals(Configuration.TOUCHSCREEN_FINGER, config.touchscreen);
        assertEquals(Configuration.KEYBOARD_NOKEYS, config.keyboard);
        assertEquals(Configuration.KEYBOARDHIDDEN_YES, config.keyboardHidden);
        assertEquals(Configuration.HARDKEYBOARDHIDDEN_YES,
                config.hardKeyboardHidden);
        assertEquals(Configuration.NAVIGATION_NONAV, config.navigation);
        assertEquals(Configuration.NAVIGATIONHIDDEN_YES,
                config.navigationHidden);

        // The Resources owns one stable configuration per instance.
        assertSame(config, resources.getConfiguration());
    }

    @Test
    void displayMetricsAgreeWithTheConfiguration() {
        Resources resources = new Resources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();

        assertEquals(480, metrics.widthPixels);
        assertEquals(960, metrics.heightPixels);
        assertEquals(1.0f, metrics.density);
        assertEquals(160, metrics.densityDpi);
        assertEquals(160.0f, metrics.xdpi);
        assertEquals(160.0f, metrics.ydpi);
        assertEquals(metrics.density * config.fontScale,
                metrics.scaledDensity);
        assertSame(metrics, resources.getDisplayMetrics());
    }

    @Test
    void knownSystemColorResolvesThroughTheBaselineTable() {
        Resources resources = new Resources();
        // android.R.color.white (0x0106001D = 17170461) from the
        // generated SystemColors baseline.
        assertEquals(0xFFFFFFFF, resources.getColor(17170461));
        assertEquals(0xFFFFFFFF, resources.getColor(17170461, null));
    }

    @Test
    void unknownColorThrowsTheNamedGap() {
        Resources resources = new Resources();
        UnsupportedOperationException e = assertThrows(
                UnsupportedOperationException.class,
                () -> resources.getColor(0x7F010000));
        assertTrue(e.getMessage().contains("Resources;->getColor(I)I"));
        assertTrue(e.getMessage().contains("id=7f010000"));
    }
}
