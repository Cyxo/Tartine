package android.view;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.accessibility.AccessibilityEvent;
import android.window.OnBackInvokedDispatcher;

    public interface ViewParent {

    public abstract void bringChildToFront(View arg0);

    public abstract boolean canResolveLayoutDirection();

    public abstract boolean canResolveTextAlignment();

    public abstract boolean canResolveTextDirection();

    public abstract void childDrawableStateChanged(View arg0);

    public abstract void childHasTransientStateChanged(View arg0, boolean arg1);

    public abstract void clearChildFocus(View arg0);

    public abstract void createContextMenu(ContextMenu arg0);

    public default OnBackInvokedDispatcher findOnBackInvokedDispatcherForChild(View arg0, View arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewParent;->findOnBackInvokedDispatcherForChild(Landroid/view/View;Landroid/view/View;)Landroid/window/OnBackInvokedDispatcher;");
    }

    public abstract View focusSearch(View arg0, int arg1);

    public abstract void focusableViewAvailable(View arg0);

    public abstract boolean getChildVisibleRect(View arg0, Rect arg1, Point arg2);

    public abstract int getLayoutDirection();

    public abstract ViewParent getParent();

    public abstract ViewParent getParentForAccessibility();

    public abstract int getTextAlignment();

    public abstract int getTextDirection();

    public abstract void invalidateChild(View arg0, Rect arg1);

    public abstract ViewParent invalidateChildInParent(int[] arg0, Rect arg1);

    public abstract boolean isLayoutDirectionResolved();

    public abstract boolean isLayoutRequested();

    public abstract boolean isTextAlignmentResolved();

    public abstract boolean isTextDirectionResolved();

    public abstract View keyboardNavigationClusterSearch(View arg0, int arg1);

    public abstract void notifySubtreeAccessibilityStateChanged(View arg0, View arg1, int arg2);

    public default void onDescendantInvalidated(View arg0, View arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewParent;->onDescendantInvalidated(Landroid/view/View;Landroid/view/View;)V");
    }

    public abstract boolean onNestedFling(View arg0, float arg1, float arg2, boolean arg3);

    public abstract boolean onNestedPreFling(View arg0, float arg1, float arg2);

    public abstract boolean onNestedPrePerformAccessibilityAction(View arg0, int arg1, Bundle arg2);

    public abstract void onNestedPreScroll(View arg0, int arg1, int arg2, int[] arg3);

    public abstract void onNestedScroll(View arg0, int arg1, int arg2, int arg3, int arg4);

    public abstract void onNestedScrollAccepted(View arg0, View arg1, int arg2);

    public abstract boolean onStartNestedScroll(View arg0, View arg1, int arg2);

    public abstract void onStopNestedScroll(View arg0);

    public abstract void recomputeViewAttributes(View arg0);

    public abstract void requestChildFocus(View arg0, View arg1);

    public abstract boolean requestChildRectangleOnScreen(View arg0, Rect arg1, boolean arg2);

    public abstract void requestDisallowInterceptTouchEvent(boolean arg0);

    public abstract void requestFitSystemWindows();

    public abstract void requestLayout();

    public abstract boolean requestSendAccessibilityEvent(View arg0, AccessibilityEvent arg1);

    public abstract void requestTransparentRegion(View arg0);

    public abstract boolean showContextMenuForChild(View arg0);

    public abstract boolean showContextMenuForChild(View arg0, float arg1, float arg2);

    public abstract ActionMode startActionModeForChild(View arg0, ActionMode.Callback arg1);

    public abstract ActionMode startActionModeForChild(View arg0, ActionMode.Callback arg1, int arg2);
    }
