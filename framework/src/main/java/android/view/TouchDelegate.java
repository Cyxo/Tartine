package android.view;

import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;

    public class TouchDelegate {

    public static final int ABOVE = 1;
    public static final int BELOW = 2;
    public static final int TO_LEFT = 4;
    public static final int TO_RIGHT = 8;

    public TouchDelegate(Rect arg0, View arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/TouchDelegate;-><init>(Landroid/graphics/Rect;Landroid/view/View;)V");
    }

    public AccessibilityNodeInfo.TouchDelegateInfo getTouchDelegateInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/TouchDelegate;->getTouchDelegateInfo()Landroid/view/accessibility/AccessibilityNodeInfo$TouchDelegateInfo;");
    }

    public boolean onTouchEvent(MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/TouchDelegate;->onTouchEvent(Landroid/view/MotionEvent;)Z");
    }

    public boolean onTouchExplorationHoverEvent(MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/TouchDelegate;->onTouchExplorationHoverEvent(Landroid/view/MotionEvent;)Z");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public TouchDelegate() {
        }
    }
