package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * AOSP {@code android.widget.ScrollView}: a {@link FrameLayout} holding one
 * child taller than the viewport, with clamped vertical scrolling and the
 * {@code fillViewport} option.
 */
public class ScrollView extends FrameLayout {

    private static final int ATTR_FILL_VIEWPORT = 0x0101017A;

    private boolean mFillViewport;

    public ScrollView(Context context) {
        this(context, null);
    }

    public ScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ScrollView(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr);
        android.content.res.TypedArray a = context.obtainStyledAttributes(
                attrs, new int[] {ATTR_FILL_VIEWPORT}, defStyleAttr,
                defStyleRes);
        try {
            mFillViewport = a.getBoolean(0, false);
        } finally {
            a.recycle();
        }
    }

    /** Sets whether a short child is stretched to the viewport height. */
    public void setFillViewport(boolean fillViewport) {
        if (mFillViewport != fillViewport) {
            mFillViewport = fillViewport;
            requestLayout();
        }
    }

    /** Returns whether a short child is stretched to the viewport height. */
    public boolean isFillViewport() {
        return mFillViewport;
    }

    /** Sets whether the scrollbar is drawn (stored only). */
    public void setSmoothScrollingEnabled(boolean smoothScrollingEnabled) {
        // Scroll animation is not ported; scrollTo moves directly.
    }

    /** Returns whether smooth scrolling is enabled. */
    public boolean isSmoothScrollingEnabled() {
        return false;
    }

    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec,
            int parentHeightMeasureSpec) {
        ViewGroup.LayoutParams lp = child.getLayoutParams();
        int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                getPaddingLeft() + getPaddingRight(), lp.width);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.MODE_UNSPECIFIED);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    protected void measureChildWithMargins(View child,
            int parentWidthMeasureSpec, int widthUsed,
            int parentHeightMeasureSpec, int heightUsed) {
        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                getPaddingLeft() + getPaddingRight() + lp.leftMargin
                        + lp.rightMargin + widthUsed, lp.width);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                lp.topMargin + lp.bottomMargin, MeasureSpec.MODE_UNSPECIFIED);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!mFillViewport) {
            return;
        }
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.MODE_UNSPECIFIED) {
            return;
        }
        if (getChildCount() > 0) {
            final View child = getChildAt(0);
            final int height = getMeasuredHeight();
            if (child.getMeasuredHeight() < height) {
                ViewGroup.LayoutParams lp = child.getLayoutParams();
                int childWidthMeasureSpec = getChildMeasureSpec(
                        widthMeasureSpec,
                        getPaddingLeft() + getPaddingRight(), lp.width);
                child.measure(childWidthMeasureSpec,
                        MeasureSpec.makeMeasureSpec(
                                height - getPaddingTop()
                                        - getPaddingBottom(),
                                MeasureSpec.MODE_EXACTLY));
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            int left = getPaddingLeft();
            int top = getPaddingTop();
            child.layout(left, top, left + childWidth, top + childHeight);
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        int maxY = getScrollRange();
        int clamped = Math.max(0, Math.min(y, maxY));
        if (clamped != y) {
            y = clamped;
        }
        super.scrollTo(x, y);
    }

    /** The vertical scroll range of the content child. */
    public int getScrollRange() {
        if (getChildCount() == 0) {
            return 0;
        }
        View child = getChildAt(0);
        return Math.max(0, child.getHeight() + getPaddingTop()
                + getPaddingBottom() - getHeight());
    }

    /** Scrolls by a delta, clamped to the content range. */
    public void scrollBy(int x, int y) {
        scrollTo(getScrollX() + x, getScrollY() + y);
    }

    /** Scrolls to the top or bottom of the content. */
    public final void fullScroll(int direction) {
        scrollTo(getScrollX(), direction > 0 ? getScrollRange() : 0);
    }

    /** Scrolls one viewport in {@code direction}. */
    public boolean pageScroll(int direction) {
        int amount = Math.max(1, getHeight() - getPaddingTop()
                - getPaddingBottom());
        scrollBy(0, direction > 0 ? amount : -amount);
        return true;
    }

    /** Scrolls by a small step in {@code direction}. */
    public boolean arrowScroll(int direction) {
        int amount = Math.max(1, getHeight() / 8);
        scrollBy(0, direction > 0 ? amount : -amount);
        return true;
    }

    /** Animated scrolling is a direct jump in the staging shell. */
    public final void smoothScrollTo(int x, int y) {
        scrollTo(x, y);
    }

    /** Animated scrolling is a direct jump in the staging shell. */
    public final void smoothScrollBy(int dx, int dy) {
        scrollBy(dx, dy);
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return "android.widget.ScrollView";
    }
}
