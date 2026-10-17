package android.animation;

    public abstract class Animator implements java.lang.Cloneable {

    public static final long DURATION_INFINITE = -1L;

    public Animator() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;-><init>()V");
    }

    public void addListener(android.animation.Animator.AnimatorListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->addListener(Landroid/animation/Animator$AnimatorListener;)V");
    }

    public void addPauseListener(android.animation.Animator.AnimatorPauseListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->addPauseListener(Landroid/animation/Animator$AnimatorPauseListener;)V");
    }

    public void cancel() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->cancel()V");
    }

    public android.animation.Animator clone() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->clone()Landroid/animation/Animator;");
    }

    public void end() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->end()V");
    }

    public abstract long getDuration();

    public android.animation.TimeInterpolator getInterpolator() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->getInterpolator()Landroid/animation/TimeInterpolator;");
    }

    public java.util.ArrayList<android.animation.Animator.AnimatorListener> getListeners() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->getListeners()Ljava/util/ArrayList;");
    }

    public abstract long getStartDelay();

    public long getTotalDuration() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->getTotalDuration()J");
    }

    public boolean isPaused() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->isPaused()Z");
    }

    public abstract boolean isRunning();

    public boolean isStarted() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->isStarted()Z");
    }

    public void pause() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->pause()V");
    }

    public void removeAllListeners() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->removeAllListeners()V");
    }

    public void removeListener(android.animation.Animator.AnimatorListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->removeListener(Landroid/animation/Animator$AnimatorListener;)V");
    }

    public void removePauseListener(android.animation.Animator.AnimatorPauseListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->removePauseListener(Landroid/animation/Animator$AnimatorPauseListener;)V");
    }

    public void resume() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->resume()V");
    }

    public abstract android.animation.Animator setDuration(long arg0);

    public abstract void setInterpolator(android.animation.TimeInterpolator arg0);

    public abstract void setStartDelay(long arg0);

    public void setTarget(java.lang.Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->setTarget(Ljava/lang/Object;)V");
    }

    public void setupEndValues() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->setupEndValues()V");
    }

    public void setupStartValues() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->setupStartValues()V");
    }

    public void start() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->start()V");
    }
        public static interface AnimatorListener {

        public abstract void onAnimationCancel(android.animation.Animator arg0);

        public abstract void onAnimationEnd(android.animation.Animator arg0);

        public default void onAnimationEnd(android.animation.Animator arg0, boolean arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator$AnimatorListener;->onAnimationEnd(Landroid/animation/Animator;Z)V");
        }

        public abstract void onAnimationRepeat(android.animation.Animator arg0);

        public abstract void onAnimationStart(android.animation.Animator arg0);

        public default void onAnimationStart(android.animation.Animator arg0, boolean arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator$AnimatorListener;->onAnimationStart(Landroid/animation/Animator;Z)V");
        }
        }
        public static interface AnimatorPauseListener {

        public abstract void onAnimationPause(android.animation.Animator arg0);

        public abstract void onAnimationResume(android.animation.Animator arg0);
        }
    }
