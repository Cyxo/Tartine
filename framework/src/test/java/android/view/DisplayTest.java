package android.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import tartine.fw.DisplayConfig;

/**
 * The shell has one window-backed display whose metrics and frame pacing
 * come from {@link DisplayConfig}. These pins cover the surface androidx
 * reads while attaching (size, density, refresh rate and mode).
 */
class DisplayTest {

    @AfterEach
    void resetDisplayConfig() {
        DisplayConfig.reset();
    }

    @Test
    void defaultDisplayMatchesTheHostWindow() {
        Display display = new Display();

        assertEquals(480, display.getWidth());
        assertEquals(960, display.getHeight());
        assertEquals(60.0f, display.getRefreshRate());
        assertEquals(Display.STATE_ON, display.getState());
        assertEquals(Display.DEFAULT_DISPLAY, display.getDisplayId());
        assertEquals(Surface.ROTATION_0, display.getRotation());
        assertTrue(display.isValid());

        Point size = new Point();
        display.getSize(size);
        assertEquals(480, size.x);
        assertEquals(960, size.y);

        Rect rect = new Rect();
        display.getRectSize(rect);
        assertEquals(new Rect(0, 0, 480, 960), rect);

        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        assertEquals(480, metrics.widthPixels);
        assertEquals(960, metrics.heightPixels);
        assertEquals(160, metrics.densityDpi);
        assertEquals(1.0f, metrics.density);
    }

    @Test
    void resizeAndRefreshRateAreReadLive() {
        Display display = new Display();
        DisplayConfig.set(960, 1920, 2.0f);
        DisplayConfig.setRefreshHz(90.0f);

        assertEquals(960, display.getWidth());
        assertEquals(1920, display.getHeight());
        assertEquals(90.0f, display.getRefreshRate());
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        assertEquals(320, metrics.densityDpi);
        assertEquals(2.0f, metrics.density);

        Display.Mode mode = display.getMode();
        assertEquals(960, mode.getPhysicalWidth());
        assertEquals(1920, mode.getPhysicalHeight());
        assertEquals(90.0f, mode.getRefreshRate());
        assertEquals(1, display.getSupportedModes().length);
        assertEquals(90.0f, display.getSupportedRefreshRates()[0]);

        // Frame pacing follows the same rate (one frame per vsync).
        assertEquals(90.0f, display.getSuggestedFrameRate(0));
        assertEquals((long) (1_000_000_000.0 / 90.0), display.getPresentationDeadlineNanos());
    }

    @Test
    void hdrAndCutoutAreAbsentOnTheDesktopWindow() {
        Display display = new Display();

        assertFalse(display.isHdr());
        assertFalse(display.isWideColorGamut());
        assertNotNull(display.getHdrCapabilities());
        assertEquals(0, display.getHdrCapabilities().getSupportedHdrTypes().length);
        assertNull(display.getCutout());
    }
}
