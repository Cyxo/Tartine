package android.view;

    public class WindowInsetsAnimation {

    public WindowInsetsAnimation(int arg0, android.view.animation.Interpolator arg1, long arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowInsetsAnimation;-><init>(ILandroid/view/animation/Interpolator;J)V");
    }

    public float getAlpha() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowInsetsAnimation;->getAlpha()F");
    }

    public long getDurationMillis() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowInsetsAnimation;->getDurationMillis()J");
    }

    public float getFraction() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowInsetsAnimation;->getFraction()F");
    }

    public float getInterpolatedFraction() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowInsetsAnimation;->getInterpolatedFraction()F");
    }

    public android.view.animation.Interpolator getInterpolator() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowInsetsAnimation;->getInterpolator()Landroid/view/animation/Interpolator;");
    }

    public int getTypeMask() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowInsetsAnimation;->getTypeMask()I");
    }

    public void setAlpha(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowInsetsAnimation;->setAlpha(F)V");
    }

    public void setFraction(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowInsetsAnimation;->setFraction(F)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public WindowInsetsAnimation() {
        }
        public static class Bounds {

        public Bounds(android.graphics.Insets arg0, android.graphics.Insets arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowInsetsAnimation$Bounds;-><init>(Landroid/graphics/Insets;Landroid/graphics/Insets;)V");
        }

        public android.graphics.Insets getLowerBound() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowInsetsAnimation$Bounds;->getLowerBound()Landroid/graphics/Insets;");
        }

        public android.graphics.Insets getUpperBound() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowInsetsAnimation$Bounds;->getUpperBound()Landroid/graphics/Insets;");
        }

        public android.view.WindowInsetsAnimation.Bounds inset(android.graphics.Insets arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowInsetsAnimation$Bounds;->inset(Landroid/graphics/Insets;)Landroid/view/WindowInsetsAnimation$Bounds;");
        }

        public java.lang.String toString() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowInsetsAnimation$Bounds;->toString()Ljava/lang/String;");
        }

            /** Scratch mirror constructor (SkeletonGen). */
            public Bounds() {
            }
        }
        public static class Callback {

        public static final int DISPATCH_MODE_CONTINUE_ON_SUBTREE = 1;
        public static final int DISPATCH_MODE_STOP = 0;

        public Callback(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowInsetsAnimation$Callback;-><init>(I)V");
        }

        public final int getDispatchMode() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowInsetsAnimation$Callback;->getDispatchMode()I");
        }

        public void onEnd(android.view.WindowInsetsAnimation arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowInsetsAnimation$Callback;->onEnd(Landroid/view/WindowInsetsAnimation;)V");
        }

        public void onPrepare(android.view.WindowInsetsAnimation arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowInsetsAnimation$Callback;->onPrepare(Landroid/view/WindowInsetsAnimation;)V");
        }

        public android.view.WindowInsets onProgress(android.view.WindowInsets arg0, java.util.List<android.view.WindowInsetsAnimation> arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowInsetsAnimation$Callback;->onProgress(Landroid/view/WindowInsets;Ljava/util/List;)Landroid/view/WindowInsets;");
        }

        public android.view.WindowInsetsAnimation.Bounds onStart(android.view.WindowInsetsAnimation arg0, android.view.WindowInsetsAnimation.Bounds arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowInsetsAnimation$Callback;->onStart(Landroid/view/WindowInsetsAnimation;Landroid/view/WindowInsetsAnimation$Bounds;)Landroid/view/WindowInsetsAnimation$Bounds;");
        }

            /** Scratch mirror constructor (SkeletonGen). */
            public Callback() {
            }
        }
    }
