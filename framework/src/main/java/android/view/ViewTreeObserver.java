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

    // -----------------------------------------------------------------
    // Listener registries. The device stores CopyOnWriteArray(-List)s so a
    // listener may mutate its own list mid-dispatch; the shell has no
    // concurrent dispatch yet, so plain ArrayLists carry the same
    // register/remove bookkeeping (each field cites its device owner).
    // -----------------------------------------------------------------

    /** AOSP ViewTreeObserver.java:773-790 addOnDrawListener. The
     * mInDispatchOnDraw guard (780-788) is checked by dispatchOnDraw. */
    private java.util.ArrayList<OnDrawListener> mOnDrawListeners;

    /** AOSP ViewTreeObserver.java:613-623 addOnGlobalFocusChangeListener. */
    private java.util.ArrayList<OnGlobalFocusChangeListener> mOnGlobalFocusListeners;

    /** AOSP ViewTreeObserver.java:648-658 addOnGlobalLayoutListener. */
    private java.util.ArrayList<OnGlobalLayoutListener> mOnGlobalLayoutListeners;

    /** AOSP ViewTreeObserver.java:698-708 addOnPreDrawListener. */
    private java.util.ArrayList<OnPreDrawListener> mOnPreDrawListeners;

    /** AOSP ViewTreeObserver.java:868-878 addOnScrollChangedListener. */
    private java.util.ArrayList<OnScrollChangedListener> mOnScrollChangedListeners;

    /** AOSP ViewTreeObserver.java:902-912 addOnTouchModeChangeListener. */
    private java.util.ArrayList<OnTouchModeChangeListener> mOnTouchModeChangeListeners;

    /** AOSP ViewTreeObserver.java:504-514 addOnWindowAttachListener. */
    private java.util.ArrayList<OnWindowAttachListener> mOnWindowAttachListeners;

    /** AOSP ViewTreeObserver.java:539-549 addOnWindowFocusChangeListener. */
    private java.util.ArrayList<OnWindowFocusChangeListener> mOnWindowFocusListeners;

    /** AOSP ViewTreeObserver.java:574-585 addOnWindowVisibilityChangeListener. */
    private java.util.ArrayList<OnWindowVisibilityChangeListener> mOnWindowVisibilityListeners;

    /** AOSP ViewTreeObserver.java:1004-1013 addOnSystemGestureExclusionRectsChangedListener. */
    private java.util.ArrayList<java.util.function.Consumer<java.util.List<android.graphics.Rect>>> mGestureExclusionListeners;

    /** AOSP ViewTreeObserver.java:831-840 registerFrameCommitCallback. */
    private java.util.ArrayList<Runnable> mOnFrameCommitListeners;

    /** AOSP ViewTreeObserver.java:1230-1232 dispatchOnDraw's re-entrancy
     * flag (device-wide fatal flag never armed on the shell). */
    private boolean mInDispatchOnDraw;

    public void addOnDrawListener(android.view.ViewTreeObserver.OnDrawListener arg0) {
        // AOSP ViewTreeObserver.java:773-790: checkIsAlive, lazy list,
        // in-dispatch guard, add.
        checkIsAlive();
        if (mOnDrawListeners == null) {
            mOnDrawListeners = new java.util.ArrayList<OnDrawListener>();
        }
        if (mInDispatchOnDraw) {
            // AOSP 780-788: the fatal flag (sIllegalOnDrawModificationIsFatal)
            // is only armed for apps targeting the guarded API level; the
            // default is a logged error. The shell logs nothing.
        }
        mOnDrawListeners.add(arg0);
    }

    public void addOnGlobalFocusChangeListener(android.view.ViewTreeObserver.OnGlobalFocusChangeListener arg0) {
        // AOSP ViewTreeObserver.java:613-623: checkIsAlive, lazy list, add.
        checkIsAlive();
        if (mOnGlobalFocusListeners == null) {
            mOnGlobalFocusListeners = new java.util.ArrayList<>();
        }
        mOnGlobalFocusListeners.add(arg0);
    }

    public void addOnGlobalLayoutListener(android.view.ViewTreeObserver.OnGlobalLayoutListener arg0) {
        // AOSP ViewTreeObserver.java:648-658: checkIsAlive, lazy list, add.
        checkIsAlive();
        if (mOnGlobalLayoutListeners == null) {
            mOnGlobalLayoutListeners = new java.util.ArrayList<>();
        }
        mOnGlobalLayoutListeners.add(arg0);
    }

    public void addOnPreDrawListener(android.view.ViewTreeObserver.OnPreDrawListener arg0) {
        // AOSP ViewTreeObserver.java:698-708: checkIsAlive, lazy list, add.
        checkIsAlive();
        if (mOnPreDrawListeners == null) {
            mOnPreDrawListeners = new java.util.ArrayList<>();
        }
        mOnPreDrawListeners.add(arg0);
    }

    public void addOnScrollChangedListener(android.view.ViewTreeObserver.OnScrollChangedListener arg0) {
        // AOSP ViewTreeObserver.java:868-878: checkIsAlive, lazy list, add.
        checkIsAlive();
        if (mOnScrollChangedListeners == null) {
            mOnScrollChangedListeners = new java.util.ArrayList<>();
        }
        mOnScrollChangedListeners.add(arg0);
    }

    public void addOnSystemGestureExclusionRectsChangedListener(java.util.function.Consumer<java.util.List<android.graphics.Rect>> arg0) {
        // AOSP ViewTreeObserver.java:1004-1013: checkIsAlive, lazy list, add.
        checkIsAlive();
        if (mGestureExclusionListeners == null) {
            mGestureExclusionListeners = new java.util.ArrayList<>();
        }
        mGestureExclusionListeners.add(arg0);
    }

    public void addOnTouchModeChangeListener(android.view.ViewTreeObserver.OnTouchModeChangeListener arg0) {
        // AOSP ViewTreeObserver.java:902-912: checkIsAlive, lazy list, add.
        checkIsAlive();
        if (mOnTouchModeChangeListeners == null) {
            mOnTouchModeChangeListeners = new java.util.ArrayList<>();
        }
        mOnTouchModeChangeListeners.add(arg0);
    }

    public void addOnWindowAttachListener(android.view.ViewTreeObserver.OnWindowAttachListener arg0) {
        // AOSP ViewTreeObserver.java:504-514: checkIsAlive, lazy list, add.
        checkIsAlive();
        if (mOnWindowAttachListeners == null) {
            mOnWindowAttachListeners = new java.util.ArrayList<>();
        }
        mOnWindowAttachListeners.add(arg0);
    }

    public void addOnWindowFocusChangeListener(android.view.ViewTreeObserver.OnWindowFocusChangeListener arg0) {
        // AOSP ViewTreeObserver.java:539-549: checkIsAlive, lazy list, add.
        checkIsAlive();
        if (mOnWindowFocusListeners == null) {
            mOnWindowFocusListeners = new java.util.ArrayList<>();
        }
        mOnWindowFocusListeners.add(arg0);
    }

    public void addOnWindowVisibilityChangeListener(android.view.ViewTreeObserver.OnWindowVisibilityChangeListener arg0) {
        // AOSP ViewTreeObserver.java:574-585: checkIsAlive, lazy list, add.
        checkIsAlive();
        if (mOnWindowVisibilityListeners == null) {
            mOnWindowVisibilityListeners = new java.util.ArrayList<>();
        }
        mOnWindowVisibilityListeners.add(arg0);
    }

    public void registerFrameCommitCallback(java.lang.Runnable arg0) {
        // AOSP ViewTreeObserver.java:831-840: checkIsAlive, lazy list, add.
        checkIsAlive();
        if (mOnFrameCommitListeners == null) {
            mOnFrameCommitListeners = new java.util.ArrayList<>();
        }
        mOnFrameCommitListeners.add(arg0);
    }

    // -----------------------------------------------------------------
    // Removals — every body is AOSP's checkIsAlive + null-guarded remove.
    // -----------------------------------------------------------------

    public void removeGlobalOnLayoutListener(android.view.ViewTreeObserver.OnGlobalLayoutListener arg0) {
        // AOSP ViewTreeObserver.java:670-672 removeGlobalOnLayoutListener:
        // deprecated alias delegating to removeOnGlobalLayoutListener.
        removeOnGlobalLayoutListener(arg0);
    }

    public void removeOnDrawListener(android.view.ViewTreeObserver.OnDrawListener arg0) {
        // AOSP ViewTreeObserver.java:797-806: checkIsAlive, null-guarded
        // remove, in-dispatch guard (not armed on the shell).
        checkIsAlive();
        if (mOnDrawListeners == null) {
            return;
        }
        mOnDrawListeners.remove(arg0);
    }

    public void removeOnGlobalFocusChangeListener(android.view.ViewTreeObserver.OnGlobalFocusChangeListener arg0) {
        // AOSP ViewTreeObserver.java:632-640: checkIsAlive, null-guarded
        // remove.
        checkIsAlive();
        if (mOnGlobalFocusListeners == null) {
            return;
        }
        mOnGlobalFocusListeners.remove(arg0);
    }

    public void removeOnGlobalLayoutListener(android.view.ViewTreeObserver.OnGlobalLayoutListener arg0) {
        // AOSP ViewTreeObserver.java:683-692: checkIsAlive, null-guarded
        // remove.
        checkIsAlive();
        if (mOnGlobalLayoutListeners == null) {
            return;
        }
        mOnGlobalLayoutListeners.remove(arg0);
    }

    public void removeOnPreDrawListener(android.view.ViewTreeObserver.OnPreDrawListener arg0) {
        // AOSP ViewTreeObserver.java:717-725: checkIsAlive, null-guarded
        // remove.
        checkIsAlive();
        if (mOnPreDrawListeners == null) {
            return;
        }
        mOnPreDrawListeners.remove(arg0);
    }

    public void removeOnScrollChangedListener(android.view.ViewTreeObserver.OnScrollChangedListener arg0) {
        // AOSP ViewTreeObserver.java:887-895: checkIsAlive, null-guarded
        // remove.
        checkIsAlive();
        if (mOnScrollChangedListeners == null) {
            return;
        }
        mOnScrollChangedListeners.remove(arg0);
    }

    public void removeOnSystemGestureExclusionRectsChangedListener(java.util.function.Consumer<java.util.List<android.graphics.Rect>> arg0) {
        // AOSP ViewTreeObserver.java:1018-1026: checkIsAlive, null-guarded
        // remove.
        checkIsAlive();
        if (mGestureExclusionListeners == null) {
            return;
        }
        mGestureExclusionListeners.remove(arg0);
    }

    public void removeOnTouchModeChangeListener(android.view.ViewTreeObserver.OnTouchModeChangeListener arg0) {
        // AOSP ViewTreeObserver.java:921-929: checkIsAlive, null-guarded
        // remove.
        checkIsAlive();
        if (mOnTouchModeChangeListeners == null) {
            return;
        }
        mOnTouchModeChangeListeners.remove(arg0);
    }

    public void removeOnWindowAttachListener(android.view.ViewTreeObserver.OnWindowAttachListener arg0) {
        // AOSP ViewTreeObserver.java:524-532: checkIsAlive, null-guarded
        // remove.
        checkIsAlive();
        if (mOnWindowAttachListeners == null) {
            return;
        }
        mOnWindowAttachListeners.remove(arg0);
    }

    public void removeOnWindowFocusChangeListener(android.view.ViewTreeObserver.OnWindowFocusChangeListener arg0) {
        // AOSP ViewTreeObserver.java:559-567: checkIsAlive, null-guarded
        // remove.
        checkIsAlive();
        if (mOnWindowFocusListeners == null) {
            return;
        }
        mOnWindowFocusListeners.remove(arg0);
    }

    public void removeOnWindowVisibilityChangeListener(android.view.ViewTreeObserver.OnWindowVisibilityChangeListener arg0) {
        // AOSP ViewTreeObserver.java:596-604: checkIsAlive, null-guarded
        // remove.
        checkIsAlive();
        if (mOnWindowVisibilityListeners == null) {
            return;
        }
        mOnWindowVisibilityListeners.remove(arg0);
    }

    public boolean unregisterFrameCommitCallback(java.lang.Runnable arg0) {
        // AOSP ViewTreeObserver.java:853-861: checkIsAlive, null-guarded
        // remove answering whether the callback was present.
        checkIsAlive();
        if (mOnFrameCommitListeners == null) {
            return false;
        }
        return mOnFrameCommitListeners.remove(arg0);
    }

    // -----------------------------------------------------------------
    // Dispatches — walk the registry calling every listener. The device
    // iterates CopyOnWriteArray snapshots so listeners may mutate their
    // own list mid-dispatch; the shell iterates a defensive copy.
    // -----------------------------------------------------------------

    public void dispatchOnDraw() {
        // AOSP ViewTreeObserver.java:1230-1242 dispatchOnDraw: null-guarded
        // sequential onDraw() walk under the mInDispatchOnDraw flag.
        if (mOnDrawListeners != null) {
            mInDispatchOnDraw = true;
            java.util.ArrayList<OnDrawListener> listeners =
                    new java.util.ArrayList<>(mOnDrawListeners);
            int numListeners = listeners.size();
            for (int i = 0; i < numListeners; ++i) {
                listeners.get(i).onDraw();
            }
            mInDispatchOnDraw = false;
        }
    }

    public void dispatchOnGlobalLayout() {
        // AOSP ViewTreeObserver.java:1131-1150 dispatchOnGlobalLayout:
        // null/empty-guarded onGlobalLayout() walk.
        if (mOnGlobalLayoutListeners != null
                && !mOnGlobalLayoutListeners.isEmpty()) {
            java.util.ArrayList<OnGlobalLayoutListener> listeners =
                    new java.util.ArrayList<>(mOnGlobalLayoutListeners);
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                listeners.get(i).onGlobalLayout();
            }
        }
    }

    public boolean dispatchOnPreDraw() {
        // AOSP ViewTreeObserver.java:1166-1196 dispatchOnPreDraw: walk
        // collecting cancellations; any false return cancels the draw. The
        // mLastDispatchOnPreDrawCanceledReason debug bookkeeping (device)
        // is omitted — the shell has no consumption site for it.
        boolean cancelDraw = false;
        if (mOnPreDrawListeners != null && !mOnPreDrawListeners.isEmpty()) {
            java.util.ArrayList<OnPreDrawListener> listeners =
                    new java.util.ArrayList<>(mOnPreDrawListeners);
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                final OnPreDrawListener preDrawListener = listeners.get(i);
                final boolean listenerCanceledDraw = !(preDrawListener.onPreDraw());
                cancelDraw |= listenerCanceledDraw;
            }
        }
        return cancelDraw;
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
