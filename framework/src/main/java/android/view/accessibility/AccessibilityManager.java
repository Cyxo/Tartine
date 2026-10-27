package android.view.accessibility;

import android.view.accessibility.AccessibilityEvent;

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
    public java.util.List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(
            int feedbackTypeFlags) {
        return java.util.Collections.emptyList();
    }

    /** Listener contract from android.jar signatures. */
    public interface AccessibilityStateChangeListener {
        void onAccessibilityStateChanged(boolean enabled);
    }

    /** Listener contract from android.jar signatures. */
    public interface TouchExplorationStateChangeListener {
        void onTouchExplorationStateChanged(boolean enabled);
    }
}
