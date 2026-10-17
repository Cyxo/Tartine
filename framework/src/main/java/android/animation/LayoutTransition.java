package android.animation;

    public class LayoutTransition {

    public static final int APPEARING = 2;
    public static final int CHANGE_APPEARING = 0;
    public static final int CHANGE_DISAPPEARING = 1;
    public static final int CHANGING = 4;
    public static final int DISAPPEARING = 3;

    public LayoutTransition() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/LayoutTransition;-><init>()V");
    }

    public void addChild(android.view.ViewGroup arg0, android.view.View arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/LayoutTransition;->addChild(Landroid/view/ViewGroup;Landroid/view/View;)V");
    }

    public void addTransitionListener(android.animation.LayoutTransition.TransitionListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/LayoutTransition;->addTransitionListener(Landroid/animation/LayoutTransition$TransitionListener;)V");
    }

    public void disableTransitionType(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/LayoutTransition;->disableTransitionType(I)V");
    }

    public void enableTransitionType(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/LayoutTransition;->enableTransitionType(I)V");
    }

    public android.animation.Animator getAnimator(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/LayoutTransition;->getAnimator(I)Landroid/animation/Animator;");
    }

    public long getDuration(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/LayoutTransition;->getDuration(I)J");
    }

    public android.animation.TimeInterpolator getInterpolator(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/LayoutTransition;->getInterpolator(I)Landroid/animation/TimeInterpolator;");
    }

    public long getStagger(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/LayoutTransition;->getStagger(I)J");
    }

    public long getStartDelay(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/LayoutTransition;->getStartDelay(I)J");
    }

    public java.util.List<android.animation.LayoutTransition.TransitionListener> getTransitionListeners() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/LayoutTransition;->getTransitionListeners()Ljava/util/List;");
    }

    public void hideChild(android.view.ViewGroup arg0, android.view.View arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/LayoutTransition;->hideChild(Landroid/view/ViewGroup;Landroid/view/View;)V");
    }

    public void hideChild(android.view.ViewGroup arg0, android.view.View arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/LayoutTransition;->hideChild(Landroid/view/ViewGroup;Landroid/view/View;I)V");
    }

    public boolean isChangingLayout() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/LayoutTransition;->isChangingLayout()Z");
    }

    public boolean isRunning() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/LayoutTransition;->isRunning()Z");
    }

    public boolean isTransitionTypeEnabled(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/LayoutTransition;->isTransitionTypeEnabled(I)Z");
    }

    public void removeChild(android.view.ViewGroup arg0, android.view.View arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/LayoutTransition;->removeChild(Landroid/view/ViewGroup;Landroid/view/View;)V");
    }

    public void removeTransitionListener(android.animation.LayoutTransition.TransitionListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/LayoutTransition;->removeTransitionListener(Landroid/animation/LayoutTransition$TransitionListener;)V");
    }

    public void setAnimateParentHierarchy(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/LayoutTransition;->setAnimateParentHierarchy(Z)V");
    }

    public void setAnimator(int arg0, android.animation.Animator arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/LayoutTransition;->setAnimator(ILandroid/animation/Animator;)V");
    }

    public void setDuration(int arg0, long arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/LayoutTransition;->setDuration(IJ)V");
    }

    public void setDuration(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/LayoutTransition;->setDuration(J)V");
    }

    public void setInterpolator(int arg0, android.animation.TimeInterpolator arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/LayoutTransition;->setInterpolator(ILandroid/animation/TimeInterpolator;)V");
    }

    public void setStagger(int arg0, long arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/LayoutTransition;->setStagger(IJ)V");
    }

    public void setStartDelay(int arg0, long arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/LayoutTransition;->setStartDelay(IJ)V");
    }

    public void showChild(android.view.ViewGroup arg0, android.view.View arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/LayoutTransition;->showChild(Landroid/view/ViewGroup;Landroid/view/View;)V");
    }

    public void showChild(android.view.ViewGroup arg0, android.view.View arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/animation/LayoutTransition;->showChild(Landroid/view/ViewGroup;Landroid/view/View;I)V");
    }
        public static interface TransitionListener {

        public abstract void endTransition(android.animation.LayoutTransition arg0, android.view.ViewGroup arg1, android.view.View arg2, int arg3);

        public abstract void startTransition(android.animation.LayoutTransition arg0, android.view.ViewGroup arg1, android.view.View arg2, int arg3);
        }
    }
