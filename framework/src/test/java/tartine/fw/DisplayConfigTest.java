package tartine.fw;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pins the host window metrics seam: framebuffer pixels drive the viewport,
 * the content scale becomes the density, and the default stays the
 * historical 480x960 mdpi surface.
 */
class DisplayConfigTest {

    @BeforeEach
    @AfterEach
    void resetConfig() {
        DisplayConfig.reset();
    }

    @Test
    void defaultsToTheHistoricalMdpiSurface() {
        assertEquals(480, DisplayConfig.widthPixels());
        assertEquals(960, DisplayConfig.heightPixels());
        assertEquals(160, DisplayConfig.densityDpi());
        assertEquals(1.0f, DisplayConfig.density());
        assertEquals(480, DisplayConfig.screenWidthDp());
        assertEquals(960, DisplayConfig.screenHeightDp());
        assertEquals(480, DisplayConfig.smallestScreenWidthDp());
    }

    @Test
    void contentScaleBecomesDensityAndKeepsDpSize() {
        DisplayConfig.set(960, 1920, 2.0f);

        assertEquals(960, DisplayConfig.widthPixels());
        assertEquals(1920, DisplayConfig.heightPixels());
        assertEquals(320, DisplayConfig.densityDpi());
        assertEquals(2.0f, DisplayConfig.density());
        assertEquals(480, DisplayConfig.screenWidthDp());
        assertEquals(960, DisplayConfig.screenHeightDp());
        assertEquals(480, DisplayConfig.smallestScreenWidthDp());
    }

    @Test
    void fractionalScaleRoundsToDensityBucket() {
        DisplayConfig.set(600, 1200, 1.25f);

        assertEquals(200, DisplayConfig.densityDpi());
        assertEquals(1.25f, DisplayConfig.density());
        assertEquals(480, DisplayConfig.screenWidthDp());
        assertEquals(960, DisplayConfig.screenHeightDp());
    }

    @Test
    void resizeKeepsDensityAndRecomputesDpAndSmallestWidth() {
        DisplayConfig.set(600, 1200, 1.25f);

        DisplayConfig.setSize(1200, 600);

        assertEquals(200, DisplayConfig.densityDpi());
        assertEquals(960, DisplayConfig.screenWidthDp());
        assertEquals(480, DisplayConfig.screenHeightDp());
        assertEquals(480, DisplayConfig.smallestScreenWidthDp());
    }

    @Test
    void refreshRateDefaultsAndResetsToSixtyHertz() {
        assertEquals(60.0f, DisplayConfig.refreshHz());

        DisplayConfig.setRefreshHz(90.5f);
        assertEquals(90.5f, DisplayConfig.refreshHz());

        DisplayConfig.reset();
        assertEquals(60.0f, DisplayConfig.refreshHz());
    }

    @Test
    void rejectsInvalidRefreshRates() {
        assertThrows(IllegalArgumentException.class,
                () -> DisplayConfig.setRefreshHz(0.0f));
        assertThrows(IllegalArgumentException.class,
                () -> DisplayConfig.setRefreshHz(-60.0f));
        assertThrows(IllegalArgumentException.class,
                () -> DisplayConfig.setRefreshHz(Float.NaN));
        assertThrows(IllegalArgumentException.class,
                () -> DisplayConfig.setRefreshHz(Float.POSITIVE_INFINITY));
    }

    @Test
    void rejectsInvalidMetrics() {
        assertThrows(IllegalArgumentException.class,
                () -> DisplayConfig.set(0, 960, 1.0f));
        assertThrows(IllegalArgumentException.class,
                () -> DisplayConfig.set(480, 0, 1.0f));
        assertThrows(IllegalArgumentException.class,
                () -> DisplayConfig.set(480, 960, 0.0f));
        assertThrows(IllegalArgumentException.class,
                () -> DisplayConfig.set(480, 960, Float.POSITIVE_INFINITY));
        assertThrows(IllegalArgumentException.class,
                () -> DisplayConfig.setSize(0, 960));
    }
}
