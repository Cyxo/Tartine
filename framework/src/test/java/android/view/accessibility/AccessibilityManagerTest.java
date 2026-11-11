package android.view.accessibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Desktop parity pins for the accessibility paths Compose reads on pointer
 * input setup: the service list is empty, no services-state listener binds,
 * and the recommended timeout (used by hover/touch-slop bookkeeping) passes
 * the caller's value through because extended timeouts are never requested
 * (ADR-0014).
 */
class AccessibilityManagerTest {

    @Test
    void desktopReportsTheDisabledState() {
        AccessibilityManager manager = new AccessibilityManager();

        assertFalse(manager.isEnabled());
        assertFalse(manager.isTouchExplorationEnabled());
        assertFalse(manager.isRequestFromAccessibilityTool());
        assertTrue(manager.getEnabledAccessibilityServiceList(0).isEmpty());
        assertTrue(manager.getInstalledAccessibilityServiceList().isEmpty());
    }

    @Test
    void recommendedTimeoutPassesThroughUnchanged() {
        AccessibilityManager manager = new AccessibilityManager();

        assertEquals(1234, manager.getRecommendedTimeoutMillis(1234, 0));
        assertEquals(0, manager.getRecommendedTimeoutMillis(0, 0));
    }

    @Test
    void servicesStateChangeListenerRegistrationIsInert() {
        AccessibilityManager manager = new AccessibilityManager();
        AccessibilityManager.AccessibilityServicesStateChangeListener listener =
                registeredManager -> { };

        manager.addAccessibilityServicesStateChangeListener(listener);
        assertFalse(manager.removeAccessibilityServicesStateChangeListener(listener));
    }
}
