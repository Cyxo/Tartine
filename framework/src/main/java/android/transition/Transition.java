package android.transition;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

    public class Transition implements Cloneable {

    public static final int MATCH_ID = 3;
    public static final int MATCH_INSTANCE = 1;
    public static final int MATCH_ITEM_ID = 4;
    public static final int MATCH_NAME = 2;

    public Transition() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;-><init>()V");
    }

    public Transition(Context arg0, AttributeSet arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;-><init>(Landroid/content/Context;Landroid/util/AttributeSet;)V");
    }

    public Transition addListener(Transition.TransitionListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->addListener(Landroid/transition/Transition$TransitionListener;)Landroid/transition/Transition;");
    }

    public Transition addTarget(View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->addTarget(Landroid/view/View;)Landroid/transition/Transition;");
    }

    public Transition addTarget(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->addTarget(I)Landroid/transition/Transition;");
    }

    public Transition addTarget(Class arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->addTarget(Ljava/lang/Class;)Landroid/transition/Transition;");
    }

    public Transition addTarget(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->addTarget(Ljava/lang/String;)Landroid/transition/Transition;");
    }

    public boolean canRemoveViews() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->canRemoveViews()Z");
    }

    public void captureEndValues(TransitionValues arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->captureEndValues(Landroid/transition/TransitionValues;)V");
    }

    public void captureStartValues(TransitionValues arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->captureStartValues(Landroid/transition/TransitionValues;)V");
    }

    public Transition clone() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->clone()Landroid/transition/Transition;");
    }

    public Animator createAnimator(ViewGroup arg0, TransitionValues arg1, TransitionValues arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->createAnimator(Landroid/view/ViewGroup;Landroid/transition/TransitionValues;Landroid/transition/TransitionValues;)Landroid/animation/Animator;");
    }

    public Transition excludeChildren(View arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->excludeChildren(Landroid/view/View;Z)Landroid/transition/Transition;");
    }

    public Transition excludeChildren(int arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->excludeChildren(IZ)Landroid/transition/Transition;");
    }

    public Transition excludeChildren(Class arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->excludeChildren(Ljava/lang/Class;Z)Landroid/transition/Transition;");
    }

    public Transition excludeTarget(View arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->excludeTarget(Landroid/view/View;Z)Landroid/transition/Transition;");
    }

    public Transition excludeTarget(int arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->excludeTarget(IZ)Landroid/transition/Transition;");
    }

    public Transition excludeTarget(Class arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->excludeTarget(Ljava/lang/Class;Z)Landroid/transition/Transition;");
    }

    public Transition excludeTarget(String arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->excludeTarget(Ljava/lang/String;Z)Landroid/transition/Transition;");
    }

    public long getDuration() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->getDuration()J");
    }

    public Rect getEpicenter() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->getEpicenter()Landroid/graphics/Rect;");
    }

    public Transition.EpicenterCallback getEpicenterCallback() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->getEpicenterCallback()Landroid/transition/Transition$EpicenterCallback;");
    }

    public TimeInterpolator getInterpolator() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->getInterpolator()Landroid/animation/TimeInterpolator;");
    }

    public String getName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->getName()Ljava/lang/String;");
    }

    public PathMotion getPathMotion() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->getPathMotion()Landroid/transition/PathMotion;");
    }

    public TransitionPropagation getPropagation() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->getPropagation()Landroid/transition/TransitionPropagation;");
    }

    public long getStartDelay() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->getStartDelay()J");
    }

    public List<Integer> getTargetIds() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->getTargetIds()Ljava/util/List;");
    }

    public List<String> getTargetNames() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->getTargetNames()Ljava/util/List;");
    }

    public List<Class> getTargetTypes() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->getTargetTypes()Ljava/util/List;");
    }

    public List<View> getTargets() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->getTargets()Ljava/util/List;");
    }

    public String[] getTransitionProperties() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->getTransitionProperties()[Ljava/lang/String;");
    }

    public TransitionValues getTransitionValues(View arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->getTransitionValues(Landroid/view/View;Z)Landroid/transition/TransitionValues;");
    }

    public boolean isTransitionRequired(TransitionValues arg0, TransitionValues arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->isTransitionRequired(Landroid/transition/TransitionValues;Landroid/transition/TransitionValues;)Z");
    }

    public Transition removeListener(Transition.TransitionListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->removeListener(Landroid/transition/Transition$TransitionListener;)Landroid/transition/Transition;");
    }

    public Transition removeTarget(View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->removeTarget(Landroid/view/View;)Landroid/transition/Transition;");
    }

    public Transition removeTarget(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->removeTarget(I)Landroid/transition/Transition;");
    }

    public Transition removeTarget(Class arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->removeTarget(Ljava/lang/Class;)Landroid/transition/Transition;");
    }

    public Transition removeTarget(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->removeTarget(Ljava/lang/String;)Landroid/transition/Transition;");
    }

    public Transition setDuration(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->setDuration(J)Landroid/transition/Transition;");
    }

    public void setEpicenterCallback(Transition.EpicenterCallback arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->setEpicenterCallback(Landroid/transition/Transition$EpicenterCallback;)V");
    }

    public Transition setInterpolator(TimeInterpolator arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->setInterpolator(Landroid/animation/TimeInterpolator;)Landroid/transition/Transition;");
    }

    public void setMatchOrder(int[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->setMatchOrder([I)V");
    }

    public void setPathMotion(PathMotion arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->setPathMotion(Landroid/transition/PathMotion;)V");
    }

    public void setPropagation(TransitionPropagation arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->setPropagation(Landroid/transition/TransitionPropagation;)V");
    }

    public Transition setStartDelay(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->setStartDelay(J)Landroid/transition/Transition;");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition;->toString()Ljava/lang/String;");
    }
        public static class EpicenterCallback {

        public EpicenterCallback() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition$EpicenterCallback;-><init>()V");
        }

        public Rect onGetEpicenter(Transition arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/transition/Transition$EpicenterCallback;->onGetEpicenter(Landroid/transition/Transition;)Landroid/graphics/Rect;");
        }
        }
        public static interface TransitionListener {

        public abstract void onTransitionCancel(Transition arg0);

        public abstract void onTransitionEnd(Transition arg0);

        public abstract void onTransitionPause(Transition arg0);

        public abstract void onTransitionResume(Transition arg0);

        public abstract void onTransitionStart(Transition arg0);
        }
    }
