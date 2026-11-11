package android.view.animation;

import android.os.SystemClock;

/**
 * AOSP AnimationUtils, ported wall-by-wall: the guest only needs the
 * animation clock so far. Compose reaches this from its hover path
 * ({@code AndroidComposeView.recalculateWindowPosition}) and androidx from
 * {@code NestedScrollView}/{@code RippleHostView}; before this class existed
 * the first hover event died with a hard {@code NoClassDefFoundError}.
 * Further members (loadAnimation/loadInterpolator/...) attach when a guest
 * actually calls them and degrade to a named gap until then.
 */
    public class AnimationUtils {

    public static long currentAnimationTimeMillis() {
        return SystemClock.uptimeMillis();
    }
}