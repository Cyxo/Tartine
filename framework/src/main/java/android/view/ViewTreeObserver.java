package android.view;

import android.content.Context;
import android.graphics.Rect;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

    public class ViewTreeObserver {

    /** AOSP ViewTreeObserver.java:80 — mutated only by kill()/merge(); a
     * floating observer (unmerged, pre-window-attachment) stays alive. */
    private boolean mAlive = true;

    /** AOSP ViewTreeObserver.java:377-380 — package-private ctor. The
     * device seeds sIllegalOnDrawModificationIsFatal from the app's
     * targetSdkVersion; our draw chain has no on-draw modification guard
     * yet, so nothing is tracked here. */
    ViewTreeObserver(Context arg0) {
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
    private ArrayList<OnDrawListener> mOnDrawListeners;

    /** AOSP ViewTreeObserver.java:613-623 addOnGlobalFocusChangeListener. */
    private ArrayList<OnGlobalFocusChangeListener> mOnGlobalFocusListeners;

    /** AOSP ViewTreeObserver.java:648-658 addOnGlobalLayoutListener. */
    private ArrayList<OnGlobalLayoutListener> mOnGlobalLayoutListeners;

    /** AOSP ViewTreeObserver.java:698-708 addOnPreDrawListener. */
    private ArrayList<OnPreDrawListener> mOnPreDrawListeners;

    /** AOSP ViewTreeObserver.java:868-878 addOnScrollChangedListener. */
    private ArrayList<OnScrollChangedListener> mOnScrollChangedListeners;

    /** AOSP ViewTreeObserver.java:902-912 addOnTouchModeChangeListener. */
    private ArrayList<OnTouchModeChangeListener> mOnTouchModeChangeListeners;

    /** AOSP ViewTreeObserver.java:504-514 addOnWindowAttachListener. */
    private ArrayList<OnWindowAttachListener> mOnWindowAttachListeners;

    /** AOSP ViewTreeObserver.java:539-549 addOnWindowFocusChangeListener. */
    private ArrayList<OnWindowFocusChangeListener> mOnWindowFocusListeners;

    /** AOSP ViewTreeObserver.java:574-585 addOnWindowVisibilityChangeListener. */
    private ArrayList<OnWindowVisibilityChangeListener> mOnWindowVisibilityListeners;

    /** AOSP ViewTreeObserver.java:1004-1013 addOnSystemGestureExclusionRectsChangedListener. */
    private ArrayList<Consumer<List<Rect>>> mGestureExclusionListeners;

    /** AOSP ViewTreeObserver.java:831-840 registerFrameCommitCallback. */
    private ArrayList<Runnable> mOnFrameCommitListeners;

    /** AOSP ViewTreeObserver.java:1230-1232 dispatchOnDraw's re-entrancy
     * flag (device-wide fatal flag never armed on the shell). */
    private boolean mInDispatchOnDraw;

    public void addOnDrawListener(ViewTreeObserver.OnDrawListener arg0) {
        // AOSP ViewTreeObserver.java:773-790: checkIsAlive, lazy list,
        // in-dispatch guard, add.
        checkIsAlive();
        if (mOnDrawListeners == null) {
            mOnDrawListeners = new ArrayList<OnDrawListener>();
        }
        if (mInDispatchOnDraw) {
            // AOSP 780-788: the fatal flag (sIllegalOnDrawModificationIsFatal)
            // is only armed for apps targeting the guarded API level; the
            // default is a logged error. The shell logs nothing.
        }
        mOnDrawListeners.add(arg0);
    }

    public void addOnGlobalFocusChangeListener(ViewTreeObserver.OnGlobalFocusChangeListener arg0) {
        // AOSP ViewTreeObserver.java:613-623: checkIsAlive, lazy list, add.
        checkIsAlive();
        if (mOnGlobalFocusListeners == null) {
            mOnGlobalFocusListeners = new ArrayList<>();
        }
        mOnGlobalFocusListeners.add(arg0);
    }

    public void addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener arg0) {
        // AOSP ViewTreeObserver.java:648-658: checkIsAlive, lazy list, add.
        checkIsAlive();
        if (mOnGlobalLayoutListeners == null) {
            mOnGlobalLayoutListeners = new ArrayList<>();
        }
        mOnGlobalLayoutListeners.add(arg0);
    }

    public void addOnPreDrawListener(ViewTreeObserver.OnPreDrawListener arg0) {
        // AOSP ViewTreeObserver.java:698-708: checkIsAlive, lazy list, add.
        checkIsAlive();
        if (mOnPreDrawListeners == null) {
            mOnPreDrawListeners = new ArrayList<>();
        }
        mOnPreDrawListeners.add(arg0);
    }

    public void addOnScrollChangedListener(ViewTreeObserver.OnScrollChangedListener arg0) {
        // AOSP ViewTreeObserver.java:868-878: checkIsAlive, lazy list, add.
        checkIsAlive();
        if (mOnScrollChangedListeners == null) {
            mOnScrollChangedListeners = new ArrayList<>();
        }
        mOnScrollChangedListeners.add(arg0);
    }

    public void addOnSystemGestureExclusionRectsChangedListener(Consumer<List<Rect>> arg0) {
        // AOSP ViewTreeObserver.java:1004-1013: checkIsAlive, lazy list, add.
        checkIsAlive();
        if (mGestureExclusionListeners == null) {
            mGestureExclusionListeners = new ArrayList<>();
        }
        mGestureExclusionListeners.add(arg0);
    }

    public void addOnTouchModeChangeListener(ViewTreeObserver.OnTouchModeChangeListener arg0) {
        // AOSP ViewTreeObserver.java:902-912: checkIsAlive, lazy list, add.
        checkIsAlive();
        if (mOnTouchModeChangeListeners == null) {
            mOnTouchModeChangeListeners = new ArrayList<>();
        }
        mOnTouchModeChangeListeners.add(arg0);
    }

    public void addOnWindowAttachListener(ViewTreeObserver.OnWindowAttachListener arg0) {
        // AOSP ViewTreeObserver.java:504-514: checkIsAlive, lazy list, add.
        checkIsAlive();
        if (mOnWindowAttachListeners == null) {
            mOnWindowAttachListeners = new ArrayList<>();
        }
        mOnWindowAttachListeners.add(arg0);
    }

    public void addOnWindowFocusChangeListener(ViewTreeObserver.OnWindowFocusChangeListener arg0) {
        // AOSP ViewTreeObserver.java:539-549: checkIsAlive, lazy list, add.
        checkIsAlive();
        if (mOnWindowFocusListeners == null) {
            mOnWindowFocusListeners = new ArrayList<>();
        }
        mOnWindowFocusListeners.add(arg0);
    }

    public void addOnWindowVisibilityChangeListener(ViewTreeObserver.OnWindowVisibilityChangeListener arg0) {
        // AOSP ViewTreeObserver.java:574-585: checkIsAlive, lazy list, add.
        checkIsAlive();
        if (mOnWindowVisibilityListeners == null) {
            mOnWindowVisibilityListeners = new ArrayList<>();
        }
        mOnWindowVisibilityListeners.add(arg0);
    }

    public void registerFrameCommitCallback(Runnable arg0) {
        // AOSP ViewTreeObserver.java:831-840: checkIsAlive, lazy list, add.
        checkIsAlive();
        if (mOnFrameCommitListeners == null) {
            mOnFrameCommitListeners = new ArrayList<>();
        }
        mOnFrameCommitListeners.add(arg0);
    }

    // -----------------------------------------------------------------
    // Removals — every body is AOSP's checkIsAlive + null-guarded remove.
    // -----------------------------------------------------------------

    public void removeGlobalOnLayoutListener(ViewTreeObserver.OnGlobalLayoutListener arg0) {
        // AOSP ViewTreeObserver.java:670-672 removeGlobalOnLayoutListener:
        // deprecated alias delegating to removeOnGlobalLayoutListener.
        removeOnGlobalLayoutListener(arg0);
    }

    public void removeOnDrawListener(ViewTreeObserver.OnDrawListener arg0) {
        // AOSP ViewTreeObserver.java:797-806: checkIsAlive, null-guarded
        // remove, in-dispatch guard (not armed on the shell).
        checkIsAlive();
        if (mOnDrawListeners == null) {
            return;
        }
        mOnDrawListeners.remove(arg0);
    }

    public void removeOnGlobalFocusChangeListener(ViewTreeObserver.OnGlobalFocusChangeListener arg0) {
        // AOSP ViewTreeObserver.java:632-640: checkIsAlive, null-guarded
        // remove.
        checkIsAlive();
        if (mOnGlobalFocusListeners == null) {
            return;
        }
        mOnGlobalFocusListeners.remove(arg0);
    }

    public void removeOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener arg0) {
        // AOSP ViewTreeObserver.java:683-692: checkIsAlive, null-guarded
        // remove.
        checkIsAlive();
        if (mOnGlobalLayoutListeners == null) {
            return;
        }
        mOnGlobalLayoutListeners.remove(arg0);
    }

    public void removeOnPreDrawListener(ViewTreeObserver.OnPreDrawListener arg0) {
        // AOSP ViewTreeObserver.java:717-725: checkIsAlive, null-guarded
        // remove.
        checkIsAlive();
        if (mOnPreDrawListeners == null) {
            return;
        }
        mOnPreDrawListeners.remove(arg0);
    }

    public void removeOnScrollChangedListener(ViewTreeObserver.OnScrollChangedListener arg0) {
        // AOSP ViewTreeObserver.java:887-895: checkIsAlive, null-guarded
        // remove.
        checkIsAlive();
        if (mOnScrollChangedListeners == null) {
            return;
        }
        mOnScrollChangedListeners.remove(arg0);
    }

    public void removeOnSystemGestureExclusionRectsChangedListener(Consumer<List<Rect>> arg0) {
        // AOSP ViewTreeObserver.java:1018-1026: checkIsAlive, null-guarded
        // remove.
        checkIsAlive();
        if (mGestureExclusionListeners == null) {
            return;
        }
        mGestureExclusionListeners.remove(arg0);
    }

    public void removeOnTouchModeChangeListener(ViewTreeObserver.OnTouchModeChangeListener arg0) {
        // AOSP ViewTreeObserver.java:921-929: checkIsAlive, null-guarded
        // remove.
        checkIsAlive();
        if (mOnTouchModeChangeListeners == null) {
            return;
        }
        mOnTouchModeChangeListeners.remove(arg0);
    }

    public void removeOnWindowAttachListener(ViewTreeObserver.OnWindowAttachListener arg0) {
        // AOSP ViewTreeObserver.java:524-532: checkIsAlive, null-guarded
        // remove.
        checkIsAlive();
        if (mOnWindowAttachListeners == null) {
            return;
        }
        mOnWindowAttachListeners.remove(arg0);
    }

    public void removeOnWindowFocusChangeListener(ViewTreeObserver.OnWindowFocusChangeListener arg0) {
        // AOSP ViewTreeObserver.java:559-567: checkIsAlive, null-guarded
        // remove.
        checkIsAlive();
        if (mOnWindowFocusListeners == null) {
            return;
        }
        mOnWindowFocusListeners.remove(arg0);
    }

    public void removeOnWindowVisibilityChangeListener(ViewTreeObserver.OnWindowVisibilityChangeListener arg0) {
        // AOSP ViewTreeObserver.java:596-604: checkIsAlive, null-guarded
        // remove.
        checkIsAlive();
        if (mOnWindowVisibilityListeners == null) {
            return;
        }
        mOnWindowVisibilityListeners.remove(arg0);
    }

    public boolean unregisterFrameCommitCallback(Runnable arg0) {
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
            ArrayList<OnDrawListener> listeners =
                    new ArrayList<>(mOnDrawListeners);
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
            ArrayList<OnGlobalLayoutListener> listeners =
                    new ArrayList<>(mOnGlobalLayoutListeners);
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
            ArrayList<OnPreDrawListener> listeners =
                    new ArrayList<>(mOnPreDrawListeners);
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

        public abstract void onGlobalFocusChanged(View arg0, View arg1);
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
