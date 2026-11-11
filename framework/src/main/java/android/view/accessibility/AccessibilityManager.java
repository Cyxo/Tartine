package android.view.accessibility;

import android.view.accessibility.AccessibilityEvent;
import java.util.Collections;
import java.util.List;

/**
 * System service handle for screen-reader state (android.jar stub re-
 * embodied as shell Java). The desktop raises no accessibility services,
 * so the manager answers the disabled state everywhere — the same value a
 * device with no service bound produces.
 */
public final class AccessibilityManager {

    /**
     * AOSP frameworks-base core/java/android/view/accessibility/
     * AccessibilityManager.java#isEnabled: true when at least one
     * accessibility service is bound; the desktop has none.
     */
    public boolean isEnabled() {
        return false;
    }

    /**
     * AOSP .../AccessibilityManager.java#isTouchExplorationEnabled: the
     * service flag only turns on through Settings; the desktop has no
     * service to turn it on.
     */
    public boolean isTouchExplorationEnabled() {
        return false;
    }

    /**
     * AOSP .../AccessibilityManager.java#sendAccessibilityEvent: with the
     * service down the AOSP walk-through returns early and drops the
     * event; the desktop matches (no dispatch target).
     */
    public void sendAccessibilityEvent(AccessibilityEvent event) {
    }

    /**
     * AOSP .../AccessibilityManager.java#addAccessibilityStateChangeListener:
     * registration is bookkeeping around the bound service; nothing binds,
     * so the listener registration fills the supplied slot and never fires.
     */
    public boolean addAccessibilityStateChangeListener(
            AccessibilityStateChangeListener listener) {
        return false;
    }

    public boolean removeAccessibilityStateChangeListener(
            AccessibilityStateChangeListener listener) {
        return false;
    }

    /**
     * AOSP .../AccessibilityManager.java#addTouchExplorationState
     * ChangeListener (the API-34 overload registering against the bound
     * service): registration is bookkeeping; with no service bound the
     * listener is stored and never fires, matching the disabled state.
     */
    public boolean addTouchExplorationStateChangeListener(
            TouchExplorationStateChangeListener listener) {
        return false;
    }

    public boolean removeTouchExplorationStateChangeListener(
            TouchExplorationStateChangeListener listener) {
        return false;
    }

    /**
     * AOSP .../AccessibilityManager.java#getEnabledAccessibilityServiceList:
     * the service == null walk-through returns Collections.emptyList();
     * the desktop has no accessibility service, so the empty list is the
     * dividend of the same shape (getEnabledAccessibilityServiceList's
     * early return branch).
     */
    public List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(
            int feedbackTypeFlags) {
        return Collections.emptyList();
    }

    /**
     * AOSP .../AccessibilityManager.java#getInstalledAccessibilityServiceList:
     * with no service manager the list is empty on the desktop, matching the
     * disabled state (the AOSP body returns an empty list when nothing is
     * installed).
     */
    public List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList() {
        return Collections.emptyList();
    }

    /**
     * AOSP .../AccessibilityManager.java#addAccessibilityServicesState
     * ChangeListener: registration against the bound services; nothing binds,
     * so the call is a no-op that never fires. Compose registers here to
     * invalidate its accessibility configuration on pointer input setup.
     */
    public void addAccessibilityServicesStateChangeListener(
            AccessibilityServicesStateChangeListener listener) {
    }

    /**
     * AOSP .../AccessibilityManager.java#removeAccessibilityServicesState
     * ChangeListener: true when the listener had been registered; the
     * no-service shell never holds one.
     */
    public boolean removeAccessibilityServicesStateChangeListener(
            AccessibilityServicesStateChangeListener listener) {
        return false;
    }

    /**
     * AOSP .../AccessibilityManager.java#getRecommendedTimeoutMillis: when
     * the user has not requested extended timeouts (recommended timeout 0)
     * the AOSP body returns {@code originalTimeout} unchanged; the desktop
     * never sets the extended-timeout preference, so that branch always wins.
     * Compose's hover/touch-slop bookkeeping reads this value directly.
     */
    public int getRecommendedTimeoutMillis(int originalTimeout,
            int uiContentFlags) {
        return originalTimeout;
    }

    /**
     * AOSP .../AccessibilityManager.java#isRequestFromAccessibilityTool: true
     * only when the caller is an accessibility tool; the desktop has none.
     */
    public boolean isRequestFromAccessibilityTool() {
        return false;
    }

    /** Listener contract from android.jar signatures. */
    public interface AccessibilityStateChangeListener {
        void onAccessibilityStateChanged(boolean enabled);
    }

    /** Listener contract from android.jar signatures. */
    public interface AccessibilityServicesStateChangeListener {
        void onAccessibilityServicesStateChanged(AccessibilityManager manager);
    }

    /** Listener contract from android.jar signatures. */
    public interface TouchExplorationStateChangeListener {
        void onTouchExplorationStateChanged(boolean enabled);
    }
}
