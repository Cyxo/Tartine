package android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.SystemClock;
import android.view.accessibility.AccessibilityEvent;
import android.window.OnBackInvokedDispatcher;
import java.io.FileDescriptor;

/**
 * Tartine: the desktop twin of AOSP's ViewRootImpl. It owns the attach
 * handshake between a window's decor view and its ViewRootImpl, the
 * measure/layout traversal and the input entry points. The device class
 * talks to WindowManager through Binder and schedules traversals through
 * Choreographer; the desktop host loop calls {@link #performTraversals}
 * directly once per frame, so the shell keeps only the observable tree-side
 * effects.
 */
public final class ViewRootImpl implements ViewParent {

    private final View.AttachInfo mAttachInfo;
    private final IBinder mWindowToken = new WindowToken();

    private View mView;
    private boolean mLayoutRequested = true;
    private boolean mDirty = true;
    private boolean mFirst = true;
    private boolean mHasWindowFocus;

    /**
     * Creates the per-window root. The caller must invoke
     * {@link #setView(View)} with the decor before the first traversal.
     */
    public ViewRootImpl(Context context) {
        ViewTreeObserver observer = new ViewTreeObserver(context);
        mAttachInfo = new View.AttachInfo(this, observer);
        mAttachInfo.mWindowToken = mWindowToken;
    }

    /** AOSP ViewRootImpl.setView: run the attach handshake for the tree. */
    public void setView(View view) {
        if (mView == view) {
            return;
        }
        if (mView != null) {
            mView.dispatchDetachedFromWindow();
            mAttachInfo.mTreeObserver.dispatchOnWindowDetached();
        }
        mView = view;
        if (view == null) {
            return;
        }
        view.mParent = this;
        mAttachInfo.mRootView = view;
        mAttachInfo.mWindowVisibility = View.VISIBLE;
        view.dispatchAttachedToWindow(mAttachInfo, View.VISIBLE);
        mAttachInfo.mTreeObserver.dispatchOnWindowAttached();
        mAttachInfo.mTreeObserver.dispatchOnWindowVisibilityChange(
                View.VISIBLE);
        mFirst = true;
        requestLayout();
    }

    /** Detaches the tree (window removal). */
    public void die(boolean immediate) {
        if (mView != null) {
            mView.dispatchDetachedFromWindow();
            mView.mParent = null;
            mAttachInfo.mTreeObserver.dispatchOnWindowDetached();
            mView = null;
        }
    }

    public View getView() {
        return mView;
    }

    public ViewTreeObserver getViewTreeObserver() {
        return mAttachInfo.mTreeObserver;
    }

    public View.AttachInfo getAttachInfo() {
        return mAttachInfo;
    }

    /**
     * The desktop VSync traversal: measure and lay out when requested, then
     * dispatch pre-draw, draw, and the draw/global-layout listeners. The
     * canvas and size come from the host surface each frame, so a resize is
     * honored without a configuration round-trip.
     */
    public void performTraversals(Canvas canvas, int width, int height) {
        if (mView == null) {
            return;
        }
        if (mLayoutRequested) {
            int widthSpec = View.MeasureSpec.makeMeasureSpec(width,
                    View.MeasureSpec.MODE_EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(height,
                    View.MeasureSpec.MODE_EXACTLY);
            mView.measure(widthSpec, heightSpec);
            mView.layout(0, 0, mView.getMeasuredWidth(),
                    mView.getMeasuredHeight());
            mLayoutRequested = false;
            mAttachInfo.mTreeObserver.dispatchOnGlobalLayout();
        }
        if (mFirst || mDirty) {
            mFirst = false;
            mDirty = false;
            mAttachInfo.mDrawingTime = SystemClock.uptimeMillis();
            if (!mAttachInfo.mTreeObserver.dispatchOnPreDraw()) {
                mAttachInfo.mTreeObserver.dispatchOnDraw();
                mView.draw(canvas);
            }
        }
    }

    /** Reads and clears the draw-dirty flag (render-loop seam). */
    public boolean tartineConsumeDirty() {
        boolean dirty = mDirty;
        mDirty = false;
        return dirty;
    }

    /**
     * Host-driver seam: true when the next VSync must run a traversal (first
     * frame, a pending layout, or an invalidated subtree).
     */
    public boolean tartineNeedsTraversal() {
        return mFirst || mDirty || mLayoutRequested
                || (mView != null && mView.isLayoutRequested());
    }

    /** True when a layout pass is pending (render-loop seam). */
    public boolean isLayoutRequested() {
        return mLayoutRequested || (mView != null && mView.isLayoutRequested());
    }

    /** AOSP ViewRootImpl.requestLayout: schedule a traversal. */
    @Override
    public void requestLayout() {
        mLayoutRequested = true;
        mDirty = true;
    }

    /** AOSP ViewRootImpl.windowFocusChanged: focus and visibility fan-out. */
    public void windowFocusChanged(boolean hasFocus) {
        mHasWindowFocus = hasFocus;
        mAttachInfo.mHasWindowFocus = hasFocus;
        mAttachInfo.mTreeObserver.dispatchOnWindowFocusChange(hasFocus);
        if (mView != null) {
            mView.dispatchWindowFocusChanged(hasFocus);
        }
    }

    public boolean hasWindowFocus() {
        return mHasWindowFocus;
    }

    /** AOSP ViewRootImpl.dispatchTouchEvent. */
    public boolean dispatchTouchEvent(MotionEvent event) {
        return mView != null && mView.dispatchTouchEvent(event);
    }

    /** AOSP ViewRootImpl.dispatchKeyEvent. */
    public boolean dispatchKeyEvent(KeyEvent event) {
        return mView != null && mView.dispatchKeyEvent(event);
    }

    /** AOSP ViewRootImpl.dispatchGenericMotionEvent. */
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        return mView != null && mView.dispatchGenericMotionEvent(event);
    }

    /** AOSP ViewRootImpl.dispatchHoverEvent. */
    public boolean dispatchHoverEvent(MotionEvent event) {
        View view = mView;
        return view instanceof ViewGroup group
                ? group.dispatchHoverEvent(event)
                : view != null && view.onHoverEvent(event);
    }

    // ------------------------------------------------------------------
    // ViewParent
    // ------------------------------------------------------------------

    @Override
    public void bringChildToFront(View child) {
        // The shell has no window Z ordering; child order is draw order.
    }

    @Override
    public boolean canResolveLayoutDirection() {
        return true;
    }

    @Override
    public boolean canResolveTextAlignment() {
        return false;
    }

    @Override
    public boolean canResolveTextDirection() {
        return true;
    }

    @Override
    public void childDrawableStateChanged(View child) {
    }

    @Override
    public void childHasTransientStateChanged(View child, boolean hasTransientState) {
    }

    @Override
    public void clearChildFocus(View child) {
    }

    @Override
    public void createContextMenu(ContextMenu menu) {
    }

    @Override
    public OnBackInvokedDispatcher findOnBackInvokedDispatcherForChild(
            View parent, View child) {
        return null;
    }

    @Override
    public View focusSearch(View focused, int direction) {
        return null;
    }

    @Override
    public void focusableViewAvailable(View view) {
    }

    @Override
    public boolean getChildVisibleRect(View child, Rect r, Point offset) {
        if (child == null) {
            return false;
        }
        if (offset != null) {
            r.offset(offset.x, offset.y);
        }
        return true;
    }

    @Override
    public int getLayoutDirection() {
        return View.LAYOUT_DIRECTION_LTR;
    }

    @Override
    public ViewParent getParent() {
        return null;
    }

    @Override
    public ViewParent getParentForAccessibility() {
        return null;
    }

    @Override
    public int getTextAlignment() {
        return View.TEXT_ALIGNMENT_VIEW_START;
    }

    @Override
    public int getTextDirection() {
        return View.TEXT_DIRECTION_FIRST_STRONG;
    }

    @Override
    public void invalidateChild(View child, Rect dirty) {
        mDirty = true;
    }

    @Override
    public ViewParent invalidateChildInParent(int[] location, Rect dirty) {
        mDirty = true;
        return null;
    }

    @Override
    public boolean isLayoutDirectionResolved() {
        return true;
    }

    @Override
    public boolean isTextAlignmentResolved() {
        return true;
    }

    @Override
    public boolean isTextDirectionResolved() {
        return true;
    }

    @Override
    public View keyboardNavigationClusterSearch(View currentCluster,
            int direction) {
        return null;
    }

    @Override
    public void notifySubtreeAccessibilityStateChanged(View child,
            View source, int changeType) {
    }

    @Override
    public void onDescendantInvalidated(View child, View target) {
        mDirty = true;
    }

    @Override
    public boolean onNestedFling(View target, float velocityX,
            float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX,
            float velocityY) {
        return false;
    }

    @Override
    public boolean onNestedPrePerformAccessibilityAction(View target,
            int action, Bundle args) {
        return false;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed,
            int dxUnconsumed, int dyUnconsumed) {
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int axes) {
        return false;
    }

    @Override
    public void onStopNestedScroll(View target) {
    }

    @Override
    public void recomputeViewAttributes(View child) {
    }

    @Override
    public void requestChildFocus(View child, View focused) {
    }

    @Override
    public boolean requestChildRectangleOnScreen(View child, Rect rectangle,
            boolean immediate) {
        return false;
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    @Override
    public void requestFitSystemWindows() {
    }

    @Override
    public boolean requestSendAccessibilityEvent(View child,
            AccessibilityEvent event) {
        return true;
    }

    @Override
    public void requestTransparentRegion(View child) {
    }

    @Override
    public boolean showContextMenuForChild(View originalView) {
        return false;
    }

    @Override
    public boolean showContextMenuForChild(View originalView, float x,
            float y) {
        return false;
    }

    @Override
    public ActionMode startActionModeForChild(View originalView,
            ActionMode.Callback callback) {
        return null;
    }

    @Override
    public ActionMode startActionModeForChild(View originalView,
            ActionMode.Callback callback, int type) {
        return null;
    }

    /** Stand-in for the window's binder token (no Binder driver on the host). */
    private static final class WindowToken implements IBinder {
        @Override
        public void dump(FileDescriptor fd, String[] args) {
        }

        @Override
        public void dumpAsync(FileDescriptor fd, String[] args) {
        }

        @Override
        public String getInterfaceDescriptor() {
            return "tartine.WindowToken";
        }

        @Override
        public boolean isBinderAlive() {
            return true;
        }

        @Override
        public void linkToDeath(DeathRecipient recipient, int flags) {
        }

        @Override
        public boolean pingBinder() {
            return true;
        }

        @Override
        public IInterface queryLocalInterface(String descriptor) {
            return null;
        }

        @Override
        public boolean transact(int code, Parcel data, Parcel reply,
                int flags) {
            return false;
        }

        @Override
        public boolean unlinkToDeath(DeathRecipient recipient, int flags) {
            return false;
        }
    }
}
