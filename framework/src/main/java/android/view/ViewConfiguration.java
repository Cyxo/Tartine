package android.view;

import android.content.Context;

/**
 * View configuration constants (android.jar stub re-embodied as shell Java).
 * The desktop reports the device-default values a small phone would use —
 * enough for Compose's touch-slop and fling bookkeeping.
 */
public class ViewConfiguration {

    private final int mScaledTouchSlop;
    private final int mScaledMinimumFlingVelocity;

    /**
     * AOSP frameworks-base core/java/android/view/ViewConfiguration.java#
     * ViewConfiguration(Context): reads the density-scaled constants from
     * resources; the shell hard-codes the mdpi defaults (touch slop 8,
     * minimum fling 50), the values a small physical device reports.
     */
    private ViewConfiguration(Context context) {
        mScaledTouchSlop = 8;
        mScaledMinimumFlingVelocity = 50;
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
}
