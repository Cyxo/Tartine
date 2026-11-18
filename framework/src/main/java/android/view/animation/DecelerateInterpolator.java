package android.view.animation;

import android.content.Context;
import android.util.AttributeSet;

/**
 * AOSP {@code android.view.animation.DecelerateInterpolator}: starts fast and
 * eases out.
 *
 * <p>Deviation: AOSP reads the {@code factor} attribute from the XML
 * constructor; the shell ignores the attribute set and uses the default
 * factor (1.0), which is what every framework and Material animation passes.
 */
public class DecelerateInterpolator extends BaseInterpolator {

    private final float mFactor;

    public DecelerateInterpolator() {
        mFactor = 1.0f;
    }

    public DecelerateInterpolator(Context context, AttributeSet attrs) {
        this();
    }

    public DecelerateInterpolator(float factor) {
        mFactor = factor;
    }

    @Override
    public float getInterpolation(float input) {
        if (mFactor == 1.0f) {
            return 1.0f - (1.0f - input) * (1.0f - input);
        }
        return (float) (1.0f - Math.pow(1.0f - input, 2.0f * mFactor));
    }
}
