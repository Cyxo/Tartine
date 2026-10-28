package tartine.text;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The live device configuration Compose derives its Density from:
 * {@code Density(context)} reads exactly {@code displayMetrics.density} and
 * {@code configuration.fontScale}, and an {@code sp} value converts to
 * {@code value * fontScale * density} px.
 */
class TextMetricsTest {

    @Test
    void resourcesServeLiveDeviceConfiguration() {
        Resources resources = new Resources();
        Configuration config = resources.getConfiguration();
        // Compose's Density(context) reads exactly these two values; a zero
        // fontScale collapsed every sp text size to 0 px.
        assertEquals(1.0f, config.fontScale);
        assertEquals(160, config.densityDpi);
        assertEquals(480, config.screenWidthDp);
        assertEquals(960, config.screenHeightDp);
        assertEquals(480, config.smallestScreenWidthDp);
        assertEquals(Configuration.ORIENTATION_PORTRAIT, config.orientation);
        assertEquals(Configuration.UI_MODE_TYPE_NORMAL
                | Configuration.UI_MODE_NIGHT_NO, config.uiMode);

        DisplayMetrics metrics = resources.getDisplayMetrics();
        assertEquals(480, metrics.widthPixels);
        assertEquals(960, metrics.heightPixels);
        assertEquals(1.0f, metrics.density);
        assertEquals(metrics.density * config.fontScale, metrics.scaledDensity);
    }

    @Test
    void configurationSetToDefaultsMatchesAosp() {
        Configuration config = new Configuration();
        // AOSP's no-arg ctor runs unset(), which forces fontScale back to 0.
        assertEquals(0.0f, config.fontScale);
        config.setToDefaults();
        assertEquals(1.0f, config.fontScale);
        assertEquals(Configuration.DENSITY_DPI_UNDEFINED, config.densityDpi);
        assertEquals(Configuration.FONT_WEIGHT_ADJUSTMENT_UNDEFINED,
                config.fontWeightAdjustment);
    }
}
