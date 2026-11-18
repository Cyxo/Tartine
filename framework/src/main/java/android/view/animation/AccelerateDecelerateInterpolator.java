package android.view.animation;

import android.content.Context;
import android.util.AttributeSet;

/**
 * AOSP {@code android.view.animation.AccelerateDecelerateInterpolator}: slow
 * at both ends, fastest in the middle.
 */
public class AccelerateDecelerateInterpolator extends BaseInterpolator {

    public AccelerateDecelerateInterpolator() {
    }

    public AccelerateDecelerateInterpolator(Context context, AttributeSet attrs) {
    }

    @Override
    public float getInterpolation(float input) {
        return (float) (Math.cos((input + 1.0f) * Math.PI) / 2.0f) + 0.5f;
    }
}
