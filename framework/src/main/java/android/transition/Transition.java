package android.transition;

    public class Transition implements java.lang.Cloneable {

    public static final int MATCH_ID = 3;
    public static final int MATCH_INSTANCE = 1;
    public static final int MATCH_ITEM_ID = 4;
    public static final int MATCH_NAME = 2;

    public Transition() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;-><init>()V");
    }

    public Transition(android.content.Context arg0, android.util.AttributeSet arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;-><init>(Landroid/content/Context;Landroid/util/AttributeSet;)V");
    }

    public android.transition.Transition addListener(android.transition.Transition.TransitionListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->addListener(Landroid/transition/Transition$TransitionListener;)Landroid/transition/Transition;");
    }

    public android.transition.Transition addTarget(android.view.View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->addTarget(Landroid/view/View;)Landroid/transition/Transition;");
    }

    public android.transition.Transition addTarget(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->addTarget(I)Landroid/transition/Transition;");
    }

    public android.transition.Transition addTarget(java.lang.Class arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->addTarget(Ljava/lang/Class;)Landroid/transition/Transition;");
    }

    public android.transition.Transition addTarget(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->addTarget(Ljava/lang/String;)Landroid/transition/Transition;");
    }

    public boolean canRemoveViews() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->canRemoveViews()Z");
    }

    public void captureEndValues(android.transition.TransitionValues arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->captureEndValues(Landroid/transition/TransitionValues;)V");
    }

    public void captureStartValues(android.transition.TransitionValues arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->captureStartValues(Landroid/transition/TransitionValues;)V");
    }

    public android.transition.Transition clone() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->clone()Landroid/transition/Transition;");
    }

    public android.animation.Animator createAnimator(android.view.ViewGroup arg0, android.transition.TransitionValues arg1, android.transition.TransitionValues arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->createAnimator(Landroid/view/ViewGroup;Landroid/transition/TransitionValues;Landroid/transition/TransitionValues;)Landroid/animation/Animator;");
    }

    public android.transition.Transition excludeChildren(android.view.View arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->excludeChildren(Landroid/view/View;Z)Landroid/transition/Transition;");
    }

    public android.transition.Transition excludeChildren(int arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->excludeChildren(IZ)Landroid/transition/Transition;");
    }

    public android.transition.Transition excludeChildren(java.lang.Class arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->excludeChildren(Ljava/lang/Class;Z)Landroid/transition/Transition;");
    }

    public android.transition.Transition excludeTarget(android.view.View arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->excludeTarget(Landroid/view/View;Z)Landroid/transition/Transition;");
    }

    public android.transition.Transition excludeTarget(int arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->excludeTarget(IZ)Landroid/transition/Transition;");
    }

    public android.transition.Transition excludeTarget(java.lang.Class arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->excludeTarget(Ljava/lang/Class;Z)Landroid/transition/Transition;");
    }

    public android.transition.Transition excludeTarget(java.lang.String arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->excludeTarget(Ljava/lang/String;Z)Landroid/transition/Transition;");
    }

    public long getDuration() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->getDuration()J");
    }

    public android.graphics.Rect getEpicenter() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->getEpicenter()Landroid/graphics/Rect;");
    }

    public android.transition.Transition.EpicenterCallback getEpicenterCallback() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->getEpicenterCallback()Landroid/transition/Transition$EpicenterCallback;");
    }

    public android.animation.TimeInterpolator getInterpolator() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->getInterpolator()Landroid/animation/TimeInterpolator;");
    }

    public java.lang.String getName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->getName()Ljava/lang/String;");
    }

    public android.transition.PathMotion getPathMotion() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->getPathMotion()Landroid/transition/PathMotion;");
    }

    public android.transition.TransitionPropagation getPropagation() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->getPropagation()Landroid/transition/TransitionPropagation;");
    }

    public long getStartDelay() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->getStartDelay()J");
    }

    public java.util.List<java.lang.Integer> getTargetIds() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->getTargetIds()Ljava/util/List;");
    }

    public java.util.List<java.lang.String> getTargetNames() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->getTargetNames()Ljava/util/List;");
    }

    public java.util.List<java.lang.Class> getTargetTypes() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->getTargetTypes()Ljava/util/List;");
    }

    public java.util.List<android.view.View> getTargets() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->getTargets()Ljava/util/List;");
    }

    public java.lang.String[] getTransitionProperties() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->getTransitionProperties()[Ljava/lang/String;");
    }

    public android.transition.TransitionValues getTransitionValues(android.view.View arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->getTransitionValues(Landroid/view/View;Z)Landroid/transition/TransitionValues;");
    }

    public boolean isTransitionRequired(android.transition.TransitionValues arg0, android.transition.TransitionValues arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->isTransitionRequired(Landroid/transition/TransitionValues;Landroid/transition/TransitionValues;)Z");
    }

    public android.transition.Transition removeListener(android.transition.Transition.TransitionListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->removeListener(Landroid/transition/Transition$TransitionListener;)Landroid/transition/Transition;");
    }

    public android.transition.Transition removeTarget(android.view.View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->removeTarget(Landroid/view/View;)Landroid/transition/Transition;");
    }

    public android.transition.Transition removeTarget(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->removeTarget(I)Landroid/transition/Transition;");
    }

    public android.transition.Transition removeTarget(java.lang.Class arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->removeTarget(Ljava/lang/Class;)Landroid/transition/Transition;");
    }

    public android.transition.Transition removeTarget(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->removeTarget(Ljava/lang/String;)Landroid/transition/Transition;");
    }

    public android.transition.Transition setDuration(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->setDuration(J)Landroid/transition/Transition;");
    }

    public void setEpicenterCallback(android.transition.Transition.EpicenterCallback arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->setEpicenterCallback(Landroid/transition/Transition$EpicenterCallback;)V");
    }

    public android.transition.Transition setInterpolator(android.animation.TimeInterpolator arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->setInterpolator(Landroid/animation/TimeInterpolator;)Landroid/transition/Transition;");
    }

    public void setMatchOrder(int[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->setMatchOrder([I)V");
    }

    public void setPathMotion(android.transition.PathMotion arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->setPathMotion(Landroid/transition/PathMotion;)V");
    }

    public void setPropagation(android.transition.TransitionPropagation arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->setPropagation(Landroid/transition/TransitionPropagation;)V");
    }

    public android.transition.Transition setStartDelay(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->setStartDelay(J)Landroid/transition/Transition;");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->toString()Ljava/lang/String;");
    }
        public static class EpicenterCallback {

        public EpicenterCallback() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition$EpicenterCallback;-><init>()V");
        }

        public android.graphics.Rect onGetEpicenter(android.transition.Transition arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition$EpicenterCallback;->onGetEpicenter(Landroid/transition/Transition;)Landroid/graphics/Rect;");
        }
        }
        public static interface TransitionListener {

        public abstract void onTransitionCancel(android.transition.Transition arg0);

        public abstract void onTransitionEnd(android.transition.Transition arg0);

        public abstract void onTransitionPause(android.transition.Transition arg0);

        public abstract void onTransitionResume(android.transition.Transition arg0);

        public abstract void onTransitionStart(android.transition.Transition arg0);
        }
    }
