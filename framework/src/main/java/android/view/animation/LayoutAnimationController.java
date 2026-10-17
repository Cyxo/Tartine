package android.view.animation;

    public class LayoutAnimationController {

    public static final int ORDER_NORMAL = 0;
    public static final int ORDER_RANDOM = 2;
    public static final int ORDER_REVERSE = 1;
    protected android.view.animation.Animation mAnimation;
    protected android.view.animation.Interpolator mInterpolator;
    protected java.util.Random mRandomizer;

    public LayoutAnimationController(android.content.Context arg0, android.util.AttributeSet arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;-><init>(Landroid/content/Context;Landroid/util/AttributeSet;)V");
    }

    public LayoutAnimationController(android.view.animation.Animation arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;-><init>(Landroid/view/animation/Animation;)V");
    }

    public LayoutAnimationController(android.view.animation.Animation arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;-><init>(Landroid/view/animation/Animation;F)V");
    }

    public android.view.animation.Animation getAnimation() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;->getAnimation()Landroid/view/animation/Animation;");
    }

    public final android.view.animation.Animation getAnimationForView(android.view.View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;->getAnimationForView(Landroid/view/View;)Landroid/view/animation/Animation;");
    }

    public float getDelay() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;->getDelay()F");
    }

    protected long getDelayForView(android.view.View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;->getDelayForView(Landroid/view/View;)J");
    }

    public android.view.animation.Interpolator getInterpolator() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;->getInterpolator()Landroid/view/animation/Interpolator;");
    }

    public int getOrder() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;->getOrder()I");
    }

    protected int getTransformedIndex(android.view.animation.LayoutAnimationController.AnimationParameters arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;->getTransformedIndex(Landroid/view/animation/LayoutAnimationController$AnimationParameters;)I");
    }

    public boolean isDone() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;->isDone()Z");
    }

    public void setAnimation(android.content.Context arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;->setAnimation(Landroid/content/Context;I)V");
    }

    public void setAnimation(android.view.animation.Animation arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;->setAnimation(Landroid/view/animation/Animation;)V");
    }

    public void setDelay(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;->setDelay(F)V");
    }

    public void setInterpolator(android.content.Context arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;->setInterpolator(Landroid/content/Context;I)V");
    }

    public void setInterpolator(android.view.animation.Interpolator arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;->setInterpolator(Landroid/view/animation/Interpolator;)V");
    }

    public void setOrder(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;->setOrder(I)V");
    }

    public void start() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;->start()V");
    }

    public boolean willOverlap() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;->willOverlap()Z");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public LayoutAnimationController() {
        }
        public static class AnimationParameters {

        public int count;
        public int index;

        public AnimationParameters() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController$AnimationParameters;-><init>()V");
        }
        }
    }
