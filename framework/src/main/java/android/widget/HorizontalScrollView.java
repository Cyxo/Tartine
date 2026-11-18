package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * AOSP {@code android.widget.HorizontalScrollView}: a {@link FrameLayout}
 * holding one child wider than the viewport, with clamped horizontal
 * scrolling and the {@code fillViewport} option.
 */
public class HorizontalScrollView extends FrameLayout {

    private static final int ATTR_FILL_VIEWPORT = 0x0101017A;

    private boolean mFillViewport;

    public HorizontalScrollView(Context context) {
        this(context, null);
    }

    public HorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalScrollView(Context context, AttributeSet attrs,
            int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public HorizontalScrollView(Context context, AttributeSet attrs,
            int defStyleAttr, int defStyleRes) {
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

    /** Sets whether a narrow child is stretched to the viewport width. */
    public void setFillViewport(boolean fillViewport) {
        if (mFillViewport != fillViewport) {
            mFillViewport = fillViewport;
            requestLayout();
        }
    }

    /** Returns whether a narrow child is stretched to the viewport width. */
    public boolean isFillViewport() {
        return mFillViewport;
    }

    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec,
            int parentHeightMeasureSpec) {
        ViewGroup.LayoutParams lp = child.getLayoutParams();
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.MODE_UNSPECIFIED);
        int childHeightMeasureSpec = getChildMeasureSpec(
                parentHeightMeasureSpec, getPaddingTop() + getPaddingBottom(),
                lp.height);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    protected void measureChildWithMargins(View child,
            int parentWidthMeasureSpec, int widthUsed,
            int parentHeightMeasureSpec, int heightUsed) {
        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                lp.leftMargin + lp.rightMargin, MeasureSpec.MODE_UNSPECIFIED);
        int childHeightMeasureSpec = getChildMeasureSpec(
                parentHeightMeasureSpec,
                getPaddingTop() + getPaddingBottom() + lp.topMargin
                        + lp.bottomMargin + heightUsed, lp.height);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!mFillViewport) {
            return;
        }
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode == MeasureSpec.MODE_UNSPECIFIED) {
            return;
        }
        if (getChildCount() > 0) {
            final View child = getChildAt(0);
            final int width = getMeasuredWidth();
            if (child.getMeasuredWidth() < width) {
                ViewGroup.LayoutParams lp = child.getLayoutParams();
                int childHeightMeasureSpec = getChildMeasureSpec(
                        heightMeasureSpec,
                        getPaddingTop() + getPaddingBottom(), lp.height);
                child.measure(MeasureSpec.makeMeasureSpec(
                        width - getPaddingLeft() - getPaddingRight(),
                        MeasureSpec.MODE_EXACTLY), childHeightMeasureSpec);
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
        int maxX = getScrollRange();
        int clamped = Math.max(0, Math.min(x, maxX));
        if (clamped != x) {
            x = clamped;
        }
        super.scrollTo(x, y);
    }

    /** The horizontal scroll range of the content child. */
    public int getScrollRange() {
        if (getChildCount() == 0) {
            return 0;
        }
        View child = getChildAt(0);
        return Math.max(0, child.getWidth() + getPaddingLeft()
                + getPaddingRight() - getWidth());
    }

    /** Scrolls by a delta, clamped to the content range. */
    public void scrollBy(int x, int y) {
        scrollTo(getScrollX() + x, getScrollY() + y);
    }

    /** Scrolls to the start or end of the content. */
    public final void fullScroll(int direction) {
        scrollTo(direction > 0 ? getScrollRange() : 0, getScrollY());
    }

    /** Scrolls one viewport in {@code direction}. */
    public boolean pageScroll(int direction) {
        int amount = Math.max(1, getWidth() - getPaddingLeft()
                - getPaddingRight());
        scrollBy(direction > 0 ? amount : -amount, 0);
        return true;
    }

    /** Scrolls by a small step in {@code direction}. */
    public boolean arrowScroll(int direction) {
        int amount = Math.max(1, getWidth() / 8);
        scrollBy(direction > 0 ? amount : -amount, 0);
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
        return "android.widget.HorizontalScrollView";
    }
}
