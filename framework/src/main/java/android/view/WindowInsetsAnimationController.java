package android.view;

import android.graphics.Insets;

    public interface WindowInsetsAnimationController {

    public abstract void finish(boolean arg0);

    public abstract float getCurrentAlpha();

    public abstract float getCurrentFraction();

    public abstract Insets getCurrentInsets();

    public abstract Insets getHiddenStateInsets();

    public abstract Insets getShownStateInsets();

    public abstract int getTypes();

    public abstract boolean isCancelled();

    public abstract boolean isFinished();

    public default boolean isReady() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowInsetsAnimationController;->isReady()Z");
    }

    public abstract void setInsetsAndAlpha(Insets arg0, float arg1, float arg2);
    }
