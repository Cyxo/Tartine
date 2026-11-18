package android.view.animation;

import android.content.Context;
import android.util.AttributeSet;

/**
 * AOSP {@code android.view.animation.LinearInterpolator}: the identity curve.
 */
public class LinearInterpolator extends BaseInterpolator {

    public LinearInterpolator() {
    }

    public LinearInterpolator(Context context, AttributeSet attrs) {
    }

    @Override
    public float getInterpolation(float input) {
        return input;
    }
}
