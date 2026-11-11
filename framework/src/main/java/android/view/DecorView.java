package android.view;

import android.content.Context;

/**
 * Tartine: shell decor view. The device decor belongs to PhoneWindow
 * (AOSP com/android/internal/policy/PhoneWindow.java — a ViewGroup wrapping
 * the content container, which itself carries
 * {@link Window#ID_ANDROID_CONTENT android.R.id.content}); the shell
 * flattens both into one ViewGroup — the window's content holder. It wears
 * the android.R.id.content id so the device lookup pattern keeps working:
 * androidx setContent probes
 * {@code decor.findViewById(R.id.content).getChildAt(0)} for a prior
 * ComposeView, and a fresh shell window answers no children — the same
 * settle-the-new-content behavior a fresh device window shows.
 */
public class DecorView extends ViewGroup {

    /**
     * The device PhoneWindow.buildDecor seeds the decor-level container
     * hierarchy; the shell seeds context resources through the View ctor
     * and assumes the content id (Window.ID_ANDROID_CONTENT).
     */
    public DecorView(Context context) {
        super(context);
        setId(WINDOW_CONTENT_ID);
    }

    /**
     * FrameLayout-shaped decor measure: measure every visible child against
     * the incoming specs, then resolve the decor's own size (EXACTLY wins,
     * AT_MOST clamps to the largest child, UNSPECIFIED uses the child).
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = 0;
        int maxHeight = 0;
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            maxWidth = Math.max(maxWidth,
                    child.getMeasuredWidth() + mPaddingLeft + mPaddingRight);
            maxHeight = Math.max(maxHeight,
                    child.getMeasuredHeight() + mPaddingTop + mPaddingBottom);
        }
        int width = View.MeasureSpec.getMode(widthMeasureSpec)
                == View.MeasureSpec.MODE_EXACTLY
                        ? View.MeasureSpec.getSize(widthMeasureSpec)
                        : Math.max(maxWidth,
                                View.MeasureSpec.getSize(widthMeasureSpec));
        int height = View.MeasureSpec.getMode(heightMeasureSpec)
                == View.MeasureSpec.MODE_EXACTLY
                        ? View.MeasureSpec.getSize(heightMeasureSpec)
                        : Math.max(maxHeight,
                                View.MeasureSpec.getSize(heightMeasureSpec));
        setMeasuredDimension(width, height);
    }

    /** FrameLayout-shaped decor layout: each visible child fills the frame. */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
            int bottom) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            child.layout(mPaddingLeft, mPaddingTop,
                    mPaddingLeft + child.getMeasuredWidth(),
                    mPaddingTop + child.getMeasuredHeight());
        }
    }

    /**
     * AOSP Window.ID_ANDROID_CONTENT (Window.java line 270 —
     * com.android.internal.R.id.content, the android.R.id.content int the
     * guests probe); inlined because the hidden internal R class does not
     * port.
     */
    public static final int WINDOW_CONTENT_ID = 16908290;
}
