package android.view.animation;

/**
 * AOSP {@code android.view.animation.BaseInterpolator}: the shared base of the
 * platform interpolators. AOSP also carries a changing-configurations word
 * here; the shell keeps the type so framework signatures line up and drops
 * the configuration bookkeeping.
 */
public abstract class BaseInterpolator implements Interpolator {
}
