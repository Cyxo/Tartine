package android.view.autofill;

/**
 * Autofill system-service handle (android.jar stub re-embodied as shell
 * Java). The desktop raises no autofill service, so every state query
 * answers the no-feature state — the same "false" a device without the
 * autofill feature produces.
 */
public final class AutofillManager {

    /**
     * AOSP frameworks-base core/java/android/view/autofill/
     * AutofillManager.java#isEnabled: the no-feature walk-through returns
     * false before touching the bound service, and the desktop binds none.
     */
    public boolean isEnabled() {
        return false;
    }

    /**
     * AOSP .../AutofillManager.java#isAutofillSupported: null service
     * returns false directly (the shell has no IAutofillManagerService).
     */
    public boolean isAutofillSupported() {
        return false;
    }

    /**
     * AOSP .../AutofillManager.java#requestShowFillDialog/commit: send-
     * shaped methods early-return with no bound feature; the desktop has
     * none, so registration requests drop silently.
     */
    public void requestShowFillDialog(android.view.View view, int virtualId,
            boolean force) {
    }

    /**
     * AOSP .../AutofillManager.java:2956 registerCallback: with no bound
     * service the callback list stays empty; the shell drops the
     * registration (no events can ever fire).
     */
    public void registerCallback(AutofillCallback callback) {
    }

    /**
     * AOSP .../AutofillManager.java:2981 unregisterCallback: no-service
     * early return — nothing was registered, nothing to remove.
     */
    public void unregisterCallback(AutofillCallback callback) {
    }

    /**
     * AOSP .../AutofillManager.java:4711+ public abstract static class
     * AutofillCallback: the client-side event hook. The desktop raises no
     * autofill events, so the event constants keep their device values and
     * the callbacks never fire (registerCallback/unregisterCallback are
     * bookkeeping — AOSP .../AutofillManager.java:2956/2981 no-service
     * early returns).
     */
    public abstract static class AutofillCallback {

        public static final int EVENT_INPUT_SHOWN = 1;
        public static final int EVENT_INPUT_HIDDEN = 2;
        public static final int EVENT_INPUT_UNAVAILABLE = 3;

        /**
         * AOSP .../AutofillManager.java:4754 onAutofillEvent(View, int):
         * the event hook; never invoked with no autofill service bound.
         */
        public void onAutofillEvent(android.view.View view, int event) {
        }
    }
}
