package android.view;

import android.graphics.Rect;

/**
 * AOSP {@code android.view.Gravity}: the relative/absolute gravity bit model
 * and the rectangle placement helpers widgets use for layout.
 */
public class Gravity {

    public static final int NO_GRAVITY = 0x0000;
    public static final int AXIS_SPECIFIED = 0x0001;
    public static final int AXIS_PULL_BEFORE = 0x0002;
    public static final int AXIS_PULL_AFTER = 0x0004;
    public static final int AXIS_CLIP = 0x0008;
    public static final int AXIS_X_SHIFT = 0;
    public static final int AXIS_Y_SHIFT = 4;

    public static final int TOP = 0x30;
    public static final int BOTTOM = 0x50;
    public static final int LEFT = 0x03;
    public static final int RIGHT = 0x05;
    public static final int CENTER_VERTICAL = 0x10;
    public static final int FILL_VERTICAL = 0x70;
    public static final int CENTER_HORIZONTAL = 0x01;
    public static final int FILL_HORIZONTAL = 0x07;
    public static final int CENTER = 0x11;
    public static final int FILL = 0x77;
    public static final int CLIP_HORIZONTAL = 0x08;
    public static final int CLIP_VERTICAL = 0x80;
    public static final int RELATIVE_LAYOUT_DIRECTION = 0x00800000;
    public static final int HORIZONTAL_GRAVITY_MASK = 0x07;
    public static final int VERTICAL_GRAVITY_MASK = 0x70;
    public static final int DISPLAY_CLIP_VERTICAL = 0x10000000;
    public static final int DISPLAY_CLIP_HORIZONTAL = 0x01000000;

    public static final int START = 0x00800003;
    public static final int END = 0x00800005;

    public Gravity() {
    }

    /**
     * AOSP Gravity.getAbsoluteGravity: resolves START/END against the layout
     * direction and clears the relative bit. The desktop shell is LTR-only,
     * but callers pass the direction explicitly.
     */
    public static int getAbsoluteGravity(int gravity, int layoutDirection) {
        if ((gravity & RELATIVE_LAYOUT_DIRECTION) == 0) {
            return gravity;
        }
        final boolean rtl = layoutDirection == View.LAYOUT_DIRECTION_RTL;
        int result = gravity & ~RELATIVE_LAYOUT_DIRECTION;
        if ((gravity & START) == START) {
            result = (gravity & ~START) | (rtl ? RIGHT : LEFT);
        } else if ((gravity & END) == END) {
            result = (gravity & ~END) | (rtl ? LEFT : RIGHT);
        }
        return result;
    }

    /** AOSP Gravity.apply: place a w×h rectangle in {@code container}. */
    public static void apply(int gravity, int w, int h, Rect container,
            Rect outRect, int layoutDirection) {
        apply(gravity, w, h, container, outRect, layoutDirection, 0, 0);
    }

    public static void apply(int gravity, int w, int h, Rect container,
            Rect outRect, int layoutDirection, int xAdj, int yAdj) {
        int absGravity = getAbsoluteGravity(gravity, layoutDirection);
        int left = container.left + xAdj;
        int top = container.top + yAdj;
        int hor = absGravity & HORIZONTAL_GRAVITY_MASK;
        int ver = absGravity & VERTICAL_GRAVITY_MASK;
        if ((hor & RIGHT) == RIGHT) {
            left = container.right - w + xAdj;
        } else if (hor == CENTER_HORIZONTAL) {
            left = container.left + (container.width() - w) / 2 + xAdj;
        }
        if ((ver & BOTTOM) == BOTTOM) {
            top = container.bottom - h + yAdj;
        } else if (ver == CENTER_VERTICAL) {
            top = container.top + (container.height() - h) / 2 + yAdj;
        }
        outRect.set(left, top, left + w, top + h);
    }

    /** AOSP Gravity.applyDisplay: clamps a placed rect inside the display. */
    public static void applyDisplay(int gravity, Rect display, Rect inout,
            int layoutDirection) {
        if ((gravity & DISPLAY_CLIP_VERTICAL) != 0) {
            if (inout.top < display.top) {
                inout.top = display.top;
            }
            if (inout.bottom > display.bottom) {
                inout.bottom = display.bottom;
            }
        } else {
            if (inout.top < display.top) {
                inout.offset(0, display.top - inout.top);
            }
            if (inout.bottom > display.bottom) {
                inout.offset(0, display.bottom - inout.bottom);
            }
        }
        if ((gravity & DISPLAY_CLIP_HORIZONTAL) != 0) {
            if (inout.left < display.left) {
                inout.left = display.left;
            }
            if (inout.right > display.right) {
                inout.right = display.right;
            }
        } else {
            if (inout.left < display.left) {
                inout.offset(display.left - inout.left, 0);
            }
            if (inout.right > display.right) {
                inout.offset(display.right - inout.right, 0);
            }
        }
    }

    public static boolean isVertical(int gravity) {
        return (gravity & VERTICAL_GRAVITY_MASK) != 0;
    }

    public static boolean isHorizontal(int gravity) {
        return (gravity & HORIZONTAL_GRAVITY_MASK) != 0;
    }

    public static boolean isStart(int gravity) {
        return (gravity & RELATIVE_LAYOUT_DIRECTION) != 0
                && (gravity & START) == START;
    }

    public static boolean isEnd(int gravity) {
        return (gravity & RELATIVE_LAYOUT_DIRECTION) != 0
                && (gravity & END) == END;
    }

    public static String toString(int gravity) {
        return "Gravity: 0x" + Integer.toHexString(gravity);
    }
}
