package android.view;

    public class ViewTreeObserver {

    /** AOSP ViewTreeObserver.java:80 — mutated only by kill()/merge(); a
     * floating observer (unmerged, pre-window-attachment) stays alive. */
    private boolean mAlive = true;

    /** AOSP ViewTreeObserver.java:377-380 — package-private ctor. The
     * device seeds sIllegalOnDrawModificationIsFatal from the app's
     * targetSdkVersion; our draw chain has no on-draw modification guard
     * yet, so nothing is tracked here. */
    ViewTreeObserver(android.content.Context arg0) {
    }

    /** AOSP ViewTreeObserver.isAlive (ViewTreeObserver.java:1043-1045). */
    public boolean isAlive() {
        return mAlive;
    }

    /** AOSP ViewTreeObserver.checkIsAlive (ViewTreeObserver.java:1027-1031). */
    private void checkIsAlive() {
        if (!mAlive) {
            throw new IllegalStateException("This ViewTreeObserver is not alive, call "
                    + "getViewTreeObserver() again");
        }
    }

    /** AOSP ViewTreeObserver.java:773-790. The mInDispatchOnDraw guard
     * (780-788) is unreachable here — no dispatch-on-draw exists yet. */
    private java.util.ArrayList<OnDrawListener> mOnDrawListeners;

    public void addOnDrawListener(android.view.ViewTreeObserver.OnDrawListener arg0) {
        checkIsAlive();
        if (mOnDrawListeners == null) {
            mOnDrawListeners = new java.util.ArrayList<OnDrawListener>();
        }
        mOnDrawListeners.add(arg0);
    }

    public void addOnGlobalFocusChangeListener(android.view.ViewTreeObserver.OnGlobalFocusChangeListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewTreeObserver;->addOnGlobalFocusChangeListener(Landroid/view/ViewTreeObserver$OnGlobalFocusChangeListener;)V");
    }

    public void addOnGlobalLayoutListener(android.view.ViewTreeObserver.OnGlobalLayoutListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewTreeObserver;->addOnGlobalLayoutListener(Landroid/view/ViewTreeObserver$OnGlobalLayoutListener;)V");
    }

    public void addOnPreDrawListener(android.view.ViewTreeObserver.OnPreDrawListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewTreeObserver;->addOnPreDrawListener(Landroid/view/ViewTreeObserver$OnPreDrawListener;)V");
    }

    public void addOnScrollChangedListener(android.view.ViewTreeObserver.OnScrollChangedListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewTreeObserver;->addOnScrollChangedListener(Landroid/view/ViewTreeObserver$OnScrollChangedListener;)V");
    }

    public void addOnSystemGestureExclusionRectsChangedListener(java.util.function.Consumer<java.util.List<android.graphics.Rect>> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewTreeObserver;->addOnSystemGestureExclusionRectsChangedListener(Ljava/util/function/Consumer;)V");
    }

    public void addOnTouchModeChangeListener(android.view.ViewTreeObserver.OnTouchModeChangeListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewTreeObserver;->addOnTouchModeChangeListener(Landroid/view/ViewTreeObserver$OnTouchModeChangeListener;)V");
    }

    public void addOnWindowAttachListener(android.view.ViewTreeObserver.OnWindowAttachListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewTreeObserver;->addOnWindowAttachListener(Landroid/view/ViewTreeObserver$OnWindowAttachListener;)V");
    }

    public void addOnWindowFocusChangeListener(android.view.ViewTreeObserver.OnWindowFocusChangeListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewTreeObserver;->addOnWindowFocusChangeListener(Landroid/view/ViewTreeObserver$OnWindowFocusChangeListener;)V");
    }

    public void addOnWindowVisibilityChangeListener(android.view.ViewTreeObserver.OnWindowVisibilityChangeListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewTreeObserver;->addOnWindowVisibilityChangeListener(Landroid/view/ViewTreeObserver$OnWindowVisibilityChangeListener;)V");
    }

    public void dispatchOnDraw() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewTreeObserver;->dispatchOnDraw()V");
    }

    public void dispatchOnGlobalLayout() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewTreeObserver;->dispatchOnGlobalLayout()V");
    }

    public boolean dispatchOnPreDraw() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewTreeObserver;->dispatchOnPreDraw()Z");
    }

    public void registerFrameCommitCallback(java.lang.Runnable arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewTreeObserver;->registerFrameCommitCallback(Ljava/lang/Runnable;)V");
    }

    public void removeGlobalOnLayoutListener(android.view.ViewTreeObserver.OnGlobalLayoutListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewTreeObserver;->removeGlobalOnLayoutListener(Landroid/view/ViewTreeObserver$OnGlobalLayoutListener;)V");
    }

    public void removeOnDrawListener(android.view.ViewTreeObserver.OnDrawListener arg0) {
        checkIsAlive();
        if (mOnDrawListeners == null) {
            return;
        }
        mOnDrawListeners.remove(arg0);
    }

    public void removeOnGlobalFocusChangeListener(android.view.ViewTreeObserver.OnGlobalFocusChangeListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewTreeObserver;->removeOnGlobalFocusChangeListener(Landroid/view/ViewTreeObserver$OnGlobalFocusChangeListener;)V");
    }

    public void removeOnGlobalLayoutListener(android.view.ViewTreeObserver.OnGlobalLayoutListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewTreeObserver;->removeOnGlobalLayoutListener(Landroid/view/ViewTreeObserver$OnGlobalLayoutListener;)V");
    }

    public void removeOnPreDrawListener(android.view.ViewTreeObserver.OnPreDrawListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewTreeObserver;->removeOnPreDrawListener(Landroid/view/ViewTreeObserver$OnPreDrawListener;)V");
    }

    public void removeOnScrollChangedListener(android.view.ViewTreeObserver.OnScrollChangedListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewTreeObserver;->removeOnScrollChangedListener(Landroid/view/ViewTreeObserver$OnScrollChangedListener;)V");
    }

    public void removeOnSystemGestureExclusionRectsChangedListener(java.util.function.Consumer<java.util.List<android.graphics.Rect>> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewTreeObserver;->removeOnSystemGestureExclusionRectsChangedListener(Ljava/util/function/Consumer;)V");
    }

    public void removeOnTouchModeChangeListener(android.view.ViewTreeObserver.OnTouchModeChangeListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewTreeObserver;->removeOnTouchModeChangeListener(Landroid/view/ViewTreeObserver$OnTouchModeChangeListener;)V");
    }

    public void removeOnWindowAttachListener(android.view.ViewTreeObserver.OnWindowAttachListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewTreeObserver;->removeOnWindowAttachListener(Landroid/view/ViewTreeObserver$OnWindowAttachListener;)V");
    }

    public void removeOnWindowFocusChangeListener(android.view.ViewTreeObserver.OnWindowFocusChangeListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewTreeObserver;->removeOnWindowFocusChangeListener(Landroid/view/ViewTreeObserver$OnWindowFocusChangeListener;)V");
    }

    public void removeOnWindowVisibilityChangeListener(android.view.ViewTreeObserver.OnWindowVisibilityChangeListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewTreeObserver;->removeOnWindowVisibilityChangeListener(Landroid/view/ViewTreeObserver$OnWindowVisibilityChangeListener;)V");
    }

    public boolean unregisterFrameCommitCallback(java.lang.Runnable arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewTreeObserver;->unregisterFrameCommitCallback(Ljava/lang/Runnable;)Z");
    }
        public static interface OnDrawListener {

        public abstract void onDraw();
        }
        public static interface OnGlobalFocusChangeListener {

        public abstract void onGlobalFocusChanged(android.view.View arg0, android.view.View arg1);
        }
        public static interface OnGlobalLayoutListener {

        public abstract void onGlobalLayout();
        }
        public static interface OnPreDrawListener {

        public abstract boolean onPreDraw();
        }
        public static interface OnScrollChangedListener {

        public abstract void onScrollChanged();
        }
        public static interface OnTouchModeChangeListener {

        public abstract void onTouchModeChanged(boolean arg0);
        }
        public static interface OnWindowAttachListener {

        public abstract void onWindowAttached();

        public abstract void onWindowDetached();
        }
        public static interface OnWindowFocusChangeListener {

        public abstract void onWindowFocusChanged(boolean arg0);
        }
        public static interface OnWindowVisibilityChangeListener {

        public abstract void onWindowVisibilityChanged(int arg0);
        }
    }
