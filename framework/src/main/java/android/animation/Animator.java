package android.animation;

import java.util.ArrayList;

    public class Animator implements Cloneable {

    public static final long DURATION_INFINITE = -1L;

    public Animator() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;-><init>()V");
    }

    public void addListener(Animator.AnimatorListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->addListener(Landroid/animation/Animator$AnimatorListener;)V");
    }

    public void addPauseListener(Animator.AnimatorPauseListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->addPauseListener(Landroid/animation/Animator$AnimatorPauseListener;)V");
    }

    public void cancel() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->cancel()V");
    }

    public Animator clone() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->clone()Landroid/animation/Animator;");
    }

    public void end() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->end()V");
    }

    public long getDuration() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->getDuration()J");
    }

    public TimeInterpolator getInterpolator() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->getInterpolator()Landroid/animation/TimeInterpolator;");
    }

    public ArrayList<Animator.AnimatorListener> getListeners() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->getListeners()Ljava/util/ArrayList;");
    }

    public long getStartDelay() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->getStartDelay()J");
    }

    public long getTotalDuration() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->getTotalDuration()J");
    }

    public boolean isPaused() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->isPaused()Z");
    }

    public boolean isRunning() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->isRunning()Z");
    }

    public boolean isStarted() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->isStarted()Z");
    }

    public void pause() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->pause()V");
    }

    public void removeAllListeners() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->removeAllListeners()V");
    }

    public void removeListener(Animator.AnimatorListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->removeListener(Landroid/animation/Animator$AnimatorListener;)V");
    }

    public void removePauseListener(Animator.AnimatorPauseListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->removePauseListener(Landroid/animation/Animator$AnimatorPauseListener;)V");
    }

    public void resume() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->resume()V");
    }

    public Animator setDuration(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->setDuration(J)Landroid/animation/Animator;");
    }

    public void setInterpolator(TimeInterpolator arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->setInterpolator(Landroid/animation/TimeInterpolator;)V");
    }

    public void setStartDelay(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator;->setStartDelay(J)V");
    }

    public void setTarget(Object arg0) {
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

        public abstract void onAnimationCancel(Animator arg0);

        public abstract void onAnimationEnd(Animator arg0);

        public default void onAnimationEnd(Animator arg0, boolean arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator$AnimatorListener;->onAnimationEnd(Landroid/animation/Animator;Z)V");
        }

        public abstract void onAnimationRepeat(Animator arg0);

        public abstract void onAnimationStart(Animator arg0);

        public default void onAnimationStart(Animator arg0, boolean arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/Animator$AnimatorListener;->onAnimationStart(Landroid/animation/Animator;Z)V");
        }
        }
        public static interface AnimatorPauseListener {

        public abstract void onAnimationPause(Animator arg0);

        public abstract void onAnimationResume(Animator arg0);
        }
    }
