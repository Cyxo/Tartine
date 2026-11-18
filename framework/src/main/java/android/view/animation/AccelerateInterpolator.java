package android.view.animation;

import android.content.Context;
import android.util.AttributeSet;

/**
 * AOSP {@code android.view.animation.AccelerateInterpolator}: starts slow and
 * speeds up.
 *
 * <p>Deviation: like {@link DecelerateInterpolator}, the XML constructor
 * ignores the {@code factor} attribute and keeps the default 1.0.
 */
public class AccelerateInterpolator extends BaseInterpolator {

    private final float mFactor;

    public AccelerateInterpolator() {
        mFactor = 1.0f;
    }

    public AccelerateInterpolator(Context context, AttributeSet attrs) {
        this();
    }

    public AccelerateInterpolator(float factor) {
        mFactor = factor;
    }

    @Override
    public float getInterpolation(float input) {
        if (mFactor == 1.0f) {
            return input * input;
        }
        return (float) Math.pow(input, 2.0f * mFactor);
    }
}
