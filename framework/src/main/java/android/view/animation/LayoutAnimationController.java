package android.view.animation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import java.util.Random;

    public class LayoutAnimationController {

    public static final int ORDER_NORMAL = 0;
    public static final int ORDER_RANDOM = 2;
    public static final int ORDER_REVERSE = 1;
    protected Animation mAnimation;
    protected Interpolator mInterpolator;
    protected Random mRandomizer;

    public LayoutAnimationController(Context arg0, AttributeSet arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;-><init>(Landroid/content/Context;Landroid/util/AttributeSet;)V");
    }

    public LayoutAnimationController(Animation arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;-><init>(Landroid/view/animation/Animation;)V");
    }

    public LayoutAnimationController(Animation arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;-><init>(Landroid/view/animation/Animation;F)V");
    }

    public Animation getAnimation() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;->getAnimation()Landroid/view/animation/Animation;");
    }

    public final Animation getAnimationForView(View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;->getAnimationForView(Landroid/view/View;)Landroid/view/animation/Animation;");
    }

    public float getDelay() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;->getDelay()F");
    }

    protected long getDelayForView(View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;->getDelayForView(Landroid/view/View;)J");
    }

    public Interpolator getInterpolator() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;->getInterpolator()Landroid/view/animation/Interpolator;");
    }

    public int getOrder() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;->getOrder()I");
    }

    protected int getTransformedIndex(LayoutAnimationController.AnimationParameters arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;->getTransformedIndex(Landroid/view/animation/LayoutAnimationController$AnimationParameters;)I");
    }

    public boolean isDone() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;->isDone()Z");
    }

    public void setAnimation(Context arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;->setAnimation(Landroid/content/Context;I)V");
    }

    public void setAnimation(Animation arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;->setAnimation(Landroid/view/animation/Animation;)V");
    }

    public void setDelay(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;->setDelay(F)V");
    }

    public void setInterpolator(Context arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/LayoutAnimationController;->setInterpolator(Landroid/content/Context;I)V");
    }

    public void setInterpolator(Interpolator arg0) {
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
