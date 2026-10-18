package android.view.animation;

    public class Animation implements java.lang.Cloneable {

    public static final int ABSOLUTE = 0;
    public static final int INFINITE = -1;
    public static final int RELATIVE_TO_PARENT = 2;
    public static final int RELATIVE_TO_SELF = 1;
    public static final int RESTART = 1;
    public static final int REVERSE = 2;
    public static final int START_ON_FIRST_FRAME = -1;
    public static final int ZORDER_BOTTOM = -1;
    public static final int ZORDER_NORMAL = 0;
    public static final int ZORDER_TOP = 1;

    public Animation() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;-><init>()V");
    }

    public Animation(android.content.Context arg0, android.util.AttributeSet arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;-><init>(Landroid/content/Context;Landroid/util/AttributeSet;)V");
    }

    protected void applyTransformation(float arg0, android.view.animation.Transformation arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->applyTransformation(FLandroid/view/animation/Transformation;)V");
    }

    public void cancel() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->cancel()V");
    }

    protected android.view.animation.Animation clone() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->clone()Landroid/view/animation/Animation;");
    }

    public long computeDurationHint() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->computeDurationHint()J");
    }

    protected void ensureInterpolator() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->ensureInterpolator()V");
    }

    protected void finalize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->finalize()V");
    }

    public int getBackdropColor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->getBackdropColor()I");
    }

    public int getBackgroundColor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->getBackgroundColor()I");
    }

    public boolean getDetachWallpaper() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->getDetachWallpaper()Z");
    }

    public long getDuration() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->getDuration()J");
    }

    public boolean getFillAfter() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->getFillAfter()Z");
    }

    public boolean getFillBefore() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->getFillBefore()Z");
    }

    public android.view.animation.Interpolator getInterpolator() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->getInterpolator()Landroid/view/animation/Interpolator;");
    }

    public int getRepeatCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->getRepeatCount()I");
    }

    public int getRepeatMode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->getRepeatMode()I");
    }

    protected float getScaleFactor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->getScaleFactor()F");
    }

    public boolean getShowBackdrop() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->getShowBackdrop()Z");
    }

    public long getStartOffset() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->getStartOffset()J");
    }

    public long getStartTime() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->getStartTime()J");
    }

    public boolean getTransformation(long arg0, android.view.animation.Transformation arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->getTransformation(JLandroid/view/animation/Transformation;)Z");
    }

    public boolean getTransformation(long arg0, android.view.animation.Transformation arg1, float arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->getTransformation(JLandroid/view/animation/Transformation;F)Z");
    }

    public int getZAdjustment() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->getZAdjustment()I");
    }

    public boolean hasEnded() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->hasEnded()Z");
    }

    public boolean hasStarted() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->hasStarted()Z");
    }

    public void initialize(int arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->initialize(IIII)V");
    }

    public boolean isFillEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->isFillEnabled()Z");
    }

    public boolean isInitialized() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->isInitialized()Z");
    }

    public void reset() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->reset()V");
    }

    protected float resolveSize(int arg0, float arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->resolveSize(IFII)F");
    }

    public void restrictDuration(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->restrictDuration(J)V");
    }

    public void scaleCurrentDuration(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->scaleCurrentDuration(F)V");
    }

    public void setAnimationListener(android.view.animation.Animation.AnimationListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->setAnimationListener(Landroid/view/animation/Animation$AnimationListener;)V");
    }

    public void setBackdropColor(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->setBackdropColor(I)V");
    }

    public void setBackgroundColor(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->setBackgroundColor(I)V");
    }

    public void setDetachWallpaper(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->setDetachWallpaper(Z)V");
    }

    public void setDuration(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->setDuration(J)V");
    }

    public void setFillAfter(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->setFillAfter(Z)V");
    }

    public void setFillBefore(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->setFillBefore(Z)V");
    }

    public void setFillEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->setFillEnabled(Z)V");
    }

    public void setInterpolator(android.content.Context arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->setInterpolator(Landroid/content/Context;I)V");
    }

    public void setInterpolator(android.view.animation.Interpolator arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->setInterpolator(Landroid/view/animation/Interpolator;)V");
    }

    public void setRepeatCount(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->setRepeatCount(I)V");
    }

    public void setRepeatMode(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->setRepeatMode(I)V");
    }

    public void setShowBackdrop(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->setShowBackdrop(Z)V");
    }

    public void setStartOffset(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->setStartOffset(J)V");
    }

    public void setStartTime(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->setStartTime(J)V");
    }

    public void setZAdjustment(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->setZAdjustment(I)V");
    }

    public void start() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->start()V");
    }

    public void startNow() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->startNow()V");
    }

    public boolean willChangeBounds() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->willChangeBounds()Z");
    }

    public boolean willChangeTransformationMatrix() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/animation/Animation;->willChangeTransformationMatrix()Z");
    }
        public static interface AnimationListener {

        public abstract void onAnimationEnd(android.view.animation.Animation arg0);

        public abstract void onAnimationRepeat(android.view.animation.Animation arg0);

        public abstract void onAnimationStart(android.view.animation.Animation arg0);
        }
    }
