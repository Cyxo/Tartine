package android.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Pins the desktop shell's ViewConfiguration surface: Compose's
 * AndroidViewConfiguration and Clickable read the tap/long-press timings and
 * the scaled slops/fling velocities on the pointer path, so a missing
 * accessor was a hover-time NoSuchMethodError (ADR-0014). Values are the
 * mdpi AOSP defaults the shell reports.
 */
class ViewConfigurationTest {

    @Test
    void getReturnsProcessWideSingleton() {
        assertSame(ViewConfiguration.get(null), ViewConfiguration.get(null));
    }

    @Test
    void staticTimingsReportTheMdpiDefaults() {
        assertEquals(500, ViewConfiguration.getLongPressTimeout());
        assertEquals(100, ViewConfiguration.getTapTimeout());
        assertEquals(300, ViewConfiguration.getDoubleTapTimeout());
        assertEquals(0.015f, ViewConfiguration.getScrollFriction(), 1e-6f);
    }

    @Test
    void scaledSlopsAndFlingVelocitiesArePositive() {
        ViewConfiguration config = ViewConfiguration.get(null);

        assertTrue(config.getScaledTouchSlop() > 0);
        assertTrue(config.getScaledPagingTouchSlop() >= config.getScaledTouchSlop());
        assertTrue(config.getScaledMinimumFlingVelocity() > 0);
        assertTrue(config.getScaledMaximumFlingVelocity()
                >= config.getScaledMinimumFlingVelocity());
        assertTrue(config.getScaledHoverSlop() > 0);
        assertTrue(config.getScaledHandwritingSlop() > 0);
        assertTrue(config.getScaledHandwritingGestureLineMargin() > 0);
        assertTrue(config.getScaledHorizontalScrollFactor() > 0f);
        assertTrue(config.getScaledVerticalScrollFactor() > 0f);
    }

    @Test
    void perAxisOverridesFallBackToTheDeviceDefaults() {
        ViewConfiguration config = ViewConfiguration.get(null);

        assertEquals(config.getScaledMinimumFlingVelocity(),
                config.getScaledMinimumFlingVelocity(0, 0,
                        android.view.MotionEvent.AXIS_X));
        assertEquals(config.getScaledMaximumFlingVelocity(),
                config.getScaledMaximumFlingVelocity(0, 0,
                        android.view.MotionEvent.AXIS_Y));
    }

    @Test
    void desktopHasNoHardwareMenuKeyOrKeyboardShortcuts() {
        ViewConfiguration config = ViewConfiguration.get(null);

        assertFalse(config.hasPermanentMenuKey());
        assertFalse(config.shouldShowMenuShortcutsWhenKeyboardPresent());
    }
}
