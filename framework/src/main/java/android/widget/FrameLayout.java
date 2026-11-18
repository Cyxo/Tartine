package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

/**
 * AOSP {@code FrameLayout}: children stack in z-order, each placed by its
 * {@code android:layout_gravity}. The measured size is the largest child
 * (margins included) resolved against the parent spec.
 */
public class FrameLayout extends ViewGroup {

    /** AOSP FrameLayout.LayoutParams.UNSPECIFIED_GRAVITY. */
    public static final int UNSPECIFIED_GRAVITY = -1;

    private static final int ATTR_LAYOUT_GRAVITY = 0x010100B3;

    public FrameLayout(Context context) {
        super(context);
    }

    public FrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(
            ViewGroup.LayoutParams params) {
        if (params instanceof LayoutParams) {
            return new LayoutParams((LayoutParams) params);
        }
        if (params instanceof MarginLayoutParams) {
            return new LayoutParams((MarginLayoutParams) params);
        }
        return new LayoutParams(params);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams params) {
        return params instanceof LayoutParams;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int count = getChildCount();
        int maxWidth = 0;
        int maxHeight = 0;
        int childState = 0;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            measureChildWithMargins(child, widthMeasureSpec, 0,
                    heightMeasureSpec, 0);
            final MarginLayoutParams lp =
                    (MarginLayoutParams) child.getLayoutParams();
            maxWidth = Math.max(maxWidth, child.getMeasuredWidth()
                    + lp.leftMargin + lp.rightMargin);
            maxHeight = Math.max(maxHeight, child.getMeasuredHeight()
                    + lp.topMargin + lp.bottomMargin);
            childState = combineMeasuredStates(childState,
                    child.getMeasuredState());
        }
        maxWidth += getPaddingLeft() + getPaddingRight();
        maxHeight += getPaddingTop() + getPaddingBottom();
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        setMeasuredDimension(
                resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec,
                        childState << MEASURED_HEIGHT_STATE_SHIFT));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
            int bottom) {
        final int count = getChildCount();
        final int parentLeft = getPaddingLeft();
        final int parentRight = right - left - getPaddingRight();
        final int parentTop = getPaddingTop();
        final int parentBottom = bottom - top - getPaddingBottom();
        final int layoutDirection = getLayoutDirection();
        final Rect container = new Rect();
        final Rect out = new Rect();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();
            final int width = child.getMeasuredWidth();
            final int height = child.getMeasuredHeight();
            final int gravity = lp.gravity == UNSPECIFIED_GRAVITY
                    ? (Gravity.TOP | Gravity.START) : lp.gravity;
            container.set(parentLeft + lp.leftMargin,
                    parentTop + lp.topMargin,
                    parentRight - lp.rightMargin,
                    parentBottom - lp.bottomMargin);
            Gravity.apply(gravity, width, height, container, out,
                    layoutDirection);
            child.layout(out.left, out.top, out.right, out.bottom);
        }
    }

    /**
     * AOSP FrameLayout.LayoutParams: margins plus a gravity placed inside the
     * frame.
     */
    public static class LayoutParams extends MarginLayoutParams {

        public int gravity = UNSPECIFIED_GRAVITY;

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray a = context.obtainStyledAttributes(attrs,
                    new int[] {ATTR_LAYOUT_GRAVITY});
            try {
                gravity = a.getInt(0, gravity);
            } finally {
                a.recycle();
            }
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height);
            this.gravity = gravity;
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(LayoutParams source) {
            super(source);
            this.gravity = source.gravity;
        }
    }
}
