package android.window;

/**
 * Tartine: the predictive-back animation callback, mirroring AOSP
 * {@code android.window.OnBackAnimationCallback}. Registered through
 * {@code OnBackInvokedDispatcher} when a guest asks for animation frames;
 * the desktop shell has no system back gesture, so nothing invokes it.
 */
public interface OnBackAnimationCallback extends OnBackInvokedCallback {

    void onBackStarted(BackEvent backEvent);

    void onBackProgressed(BackEvent backEvent);

    void onBackCancelled();
}