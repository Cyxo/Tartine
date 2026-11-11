package android.view;

import android.content.Context;

/**
 * View configuration constants (android.jar stub re-embodied as shell Java).
 * The desktop reports the device-default values a small phone would use -
 * enough for Compose's touch-slop, tap/long-press timing and fling
 * bookkeeping. Values are the mdpi AOSP defaults; density scaling is folded
 * into the constants because the shell targets a single density.
 */
public class ViewConfiguration {

    private static final int LONG_PRESS_TIMEOUT = 500;
    private static final int TAP_TIMEOUT = 100;
    private static final int DOUBLE_TAP_TIMEOUT = 300;
    private static final int MAXIMUM_FLING_VELOCITY = 8000;
    private static final int MINIMUM_FLING_VELOCITY = 50;
    private static final int PAGING_TOUCH_SLOP = 16;
    private static final int HOVER_SLOP = 8;
    private static final int HANDWRITING_SLOP = 8;
    private static final int HANDWRITING_GESTURE_LINE_MARGIN = 16;
    private static final float HORIZONTAL_SCROLL_FACTOR = 64f;
    private static final float VERTICAL_SCROLL_FACTOR = 64f;
    private static final float SCROLL_FRICTION = 0.015f;

    private final int mScaledTouchSlop;
    private final int mScaledMinimumFlingVelocity;

    /**
     * AOSP frameworks-base core/java/android/view/ViewConfiguration.java#*
     * ViewConfiguration(Context): reads the density-scaled constants from
     * resources; the shell hard-codes the mdpi defaults (touch slop 8,
     * minimum fling 50), the values a small physical device reports.
     */
    private ViewConfiguration(Context context) {
        mScaledTouchSlop = 8;
        mScaledMinimumFlingVelocity = MINIMUM_FLING_VELOCITY;
    }

    private static ViewConfiguration sInstance;

    /**
     * AOSP .../ViewConfiguration.java#get(Context): process-wide singleton
     * cached per context density; the shell keeps one global instance.
     */
    public static ViewConfiguration get(Context context) {
        if (sInstance == null) {
            sInstance = new ViewConfiguration(context);
        }
        return sInstance;
    }

    /**
     * AOSP .../ViewConfiguration.java#getScaledTouchSlop: the distance in
     * pixels a touch can drift before being treated as a gesture; shell
     * default 8 px (mdpi phone).
     */
    public int getScaledTouchSlop() {
        return mScaledTouchSlop;
    }

    /**
     * AOSP .../ViewConfiguration.java#getScaledMinimumFlingVelocity: shell
     * default 50 dp/s expressed as pixels on mdpi.
     */
    public int getScaledMinimumFlingVelocity() {
        return mScaledMinimumFlingVelocity;
    }

    /** AOSP .../ViewConfiguration.java#getScaledMaximumFlingVelocity. */
    public int getScaledMaximumFlingVelocity() {
        return MAXIMUM_FLING_VELOCITY;
    }

    /**
     * AOSP .../ViewConfiguration.java#getScaledMaximumFlingVelocity(int,
     * int, int): a per-axis override; the shell reports the device default
     * for every axis.
     */
    public int getScaledMaximumFlingVelocity(int inputDeviceId, int pointerId,
            int axis) {
        return MAXIMUM_FLING_VELOCITY;
    }

    /**
     * AOSP .../ViewConfiguration.java#getScaledMinimumFlingVelocity(int,
     * int, int): per-axis override; device default for every axis.
     */
    public int getScaledMinimumFlingVelocity(int inputDeviceId, int pointerId,
            int axis) {
        return MINIMUM_FLING_VELOCITY;
    }

    /**
     * AOSP .../ViewConfiguration.java#getScaledPagingTouchSlop: the wider
     * slop used for horizontal paging gestures (2x touch slop).
     */
    public int getScaledPagingTouchSlop() {
        return PAGING_TOUCH_SLOP;
    }

    /** AOSP .../ViewConfiguration.java#getScaledHoverSlop. */
    public int getScaledHoverSlop() {
        return HOVER_SLOP;
    }

    /** AOSP .../ViewConfiguration.java#getScaledHandwritingSlop. */
    public int getScaledHandwritingSlop() {
        return HANDWRITING_SLOP;
    }

    /** AOSP .../ViewConfiguration.java#getScaledHandwritingGestureLineMargin. */
    public int getScaledHandwritingGestureLineMargin() {
        return HANDWRITING_GESTURE_LINE_MARGIN;
    }

    /** AOSP .../ViewConfiguration.java#getScaledHorizontalScrollFactor. */
    public float getScaledHorizontalScrollFactor() {
        return HORIZONTAL_SCROLL_FACTOR;
    }

    /** AOSP .../ViewConfiguration.java#getScaledVerticalScrollFactor. */
    public float getScaledVerticalScrollFactor() {
        return VERTICAL_SCROLL_FACTOR;
    }

    /** AOSP .../ViewConfiguration.java#getLongPressTimeout (static). */
    public static int getLongPressTimeout() {
        return LONG_PRESS_TIMEOUT;
    }

    /** AOSP .../ViewConfiguration.java#getTapTimeout (static). */
    public static int getTapTimeout() {
        return TAP_TIMEOUT;
    }

    /** AOSP .../ViewConfiguration.java#getDoubleTapTimeout (static). */
    public static int getDoubleTapTimeout() {
        return DOUBLE_TAP_TIMEOUT;
    }

    /** AOSP .../ViewConfiguration.java#getScrollFriction (static). */
    public static float getScrollFriction() {
        return SCROLL_FRICTION;
    }

    /**
     * AOSP .../ViewConfiguration.java#hasPermanentMenuKey: the desktop shell
     * exposes no hardware menu key.
     */
    public boolean hasPermanentMenuKey() {
        return false;
    }

    /**
     * AOSP .../ViewConfiguration.java#shouldShowMenuShortcutsWhenKeyboardPresent:
     * a physical keyboard does not imply overflow-menu shortcuts here.
     */
    public boolean shouldShowMenuShortcutsWhenKeyboardPresent() {
        return false;
    }
}
