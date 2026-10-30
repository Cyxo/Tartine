package android.view;

import android.os.CancellationSignal;
import android.view.animation.Interpolator;

/**
 * Tartine: minimal shell insets controller standing in for the AOSP
 * {@code android.view.InsetsController} (frameworks/base
 * core/java/android/view/InsetsController.java — the device concrete that
 * implements the WindowInsetsController interface behind PhoneWindow).
 * The shell has no display-side insets controller to host the state, so
 * the flags live on this instance instead of an InsetsState host:
 * <ul>
 *   <li>{@code setSystemBarsAppearance/getSystemBarsAppearance} — the
 *       device merge semantics from AOSP
 *       InsetsController.setSystemBarsAppearanceFromResource (lines
 *       2143-2148: {@code (m & ~mask) | (appearance & mask)} discussing the
 *       mask merge; the real controlled set carries through
 *       mHost.setSystemBarsAppearance — the shell keeps the state local
 *       until the display controller lands).</li>
 *   <li>{@code show/hide/controlWindowInsetsAnimation} and the controllable
 *       listener plumbing — inert: nothing bar-or-keyboard-shaped presents
 *       on the shell display yet; each call the guest makes names the next
 *       gap.</li>
 * </ul>
 */
public class InsetsController implements WindowInsetsController {

    private int mSystemBarsAppearance;
    private int mSystemBarsBehavior;

    /** AOSP InsetsController.setSystemBarsAppearance (mask merge, 2137-2148). */
    @Override
    public void setSystemBarsAppearance(int appearance, int mask) {
        mSystemBarsAppearance = (mSystemBarsAppearance & ~mask) | (appearance & mask);
    }

    /** AOSP InsetsController.getSystemBarsAppearance (lines 2151-2156). */
    @Override
    public int getSystemBarsAppearance() {
        return mSystemBarsAppearance;
    }

    /** AOSP InsetsController.setSystemBarsBehavior — inert store. */
    @Override
    public void setSystemBarsBehavior(int behavior) {
        mSystemBarsBehavior = behavior;
    }

    /** AOSP InsetsController.getSystemBarsBehavior — inert read. */
    @Override
    public int getSystemBarsBehavior() {
        return mSystemBarsBehavior;
    }

    /** AOSP InsetsController.show — inert (no bars to reveal on the shell). */
    @Override
    public void show(int arg0) {
    }

    /** AOSP InsetsController.hide — inert (no bars to tuck on the shell). */
    @Override
    public void hide(int arg0) {
    }

    /** AOSP InsetsController.controlWindowInsetsAnimation — inert (no
     * animation session machinery on the shell yet). */
    @Override
    public void controlWindowInsetsAnimation(int arg0, long arg1,
            Interpolator arg2, CancellationSignal arg3,
            WindowInsetsAnimationControlListener arg4) {
    }

    /** AOSP InsetsController.addOnControllableInsetsChangedListener — inert
     * (controllable state never changes on the shell, so no event fires). */
    @Override
    public void addOnControllableInsetsChangedListener(
            OnControllableInsetsChangedListener arg0) {
    }

    /** AOSP InsetsController.removeOnControllableInsetsChangedListener —
     * inert counterpart of the no-op add above. */
    @Override
    public void removeOnControllableInsetsChangedListener(
            OnControllableInsetsChangedListener arg0) {
    }
}
