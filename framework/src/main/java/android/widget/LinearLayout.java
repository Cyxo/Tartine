package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

/**
 * AOSP {@code LinearLayout}, ported for the common vertical/horizontal cases:
 * layout margins, orientation, gravity, {@code layout_weight} distribution and
 * wrap/content sizing. Baseline collection is honored for horizontal rows.
 *
 * <p>Staging reductions: dividers, {@code measureWithLargestChild}, RTL
 * resolution beyond the shared gravity helper, and baseline collection of
 * children without a baseline are simplified.
 */
public class LinearLayout extends ViewGroup {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    public static final int SHOW_DIVIDER_NONE = 0;
    public static final int SHOW_DIVIDER_BEGINNING = 1;
    public static final int SHOW_DIVIDER_MIDDLE = 2;
    public static final int SHOW_DIVIDER_END = 4;

    private static final int ATTR_ORIENTATION = 0x010100C4;
    private static final int ATTR_GRAVITY = 0x010100AF;
    private static final int ATTR_WEIGHT_SUM = 0x01010128;
    private static final int ATTR_BASELINE_ALIGNED = 0x01010126;
    private static final int ATTR_MEASURE_WITH_LARGEST_CHILD = 0x010102D4;
    private static final int ATTR_LAYOUT_WEIGHT = 0x01010181;
    private static final int ATTR_LAYOUT_GRAVITY = 0x010100B3;

    private int mOrientation = HORIZONTAL;
    private int mGravity = Gravity.START | Gravity.TOP;
    private float mWeightSum = 0.0f;
    private boolean mBaselineAligned = true;
    private int mBaselineAlignedChildIndex = -1;
    private boolean mUseLargestChild;

    public LinearLayout(Context context) {
        super(context);
    }

    public LinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, new int[] {
                ATTR_ORIENTATION, ATTR_GRAVITY, ATTR_WEIGHT_SUM,
                ATTR_BASELINE_ALIGNED, ATTR_MEASURE_WITH_LARGEST_CHILD});
        try {
            mOrientation = a.getInt(0, mOrientation);
            mGravity = a.getInt(1, mGravity);
            mWeightSum = a.getFloat(2, mWeightSum);
            mBaselineAligned = a.getBoolean(3, true);
            mUseLargestChild = a.getBoolean(4, false);
        } finally {
            a.recycle();
        }
    }

    public void setOrientation(int orientation) {
        if (mOrientation != orientation) {
            mOrientation = orientation;
            requestLayout();
        }
    }

    public int getOrientation() {
        return mOrientation;
    }

    public void setGravity(int gravity) {
        if (mGravity != gravity) {
            mGravity = gravity;
            requestLayout();
        }
    }

    public int getGravity() {
        return mGravity;
    }

    public void setWeightSum(float weightSum) {
        mWeightSum = Math.max(0.0f, weightSum);
    }

    public float getWeightSum() {
        return mWeightSum;
    }

    public void setBaselineAligned(boolean baselineAligned) {
        mBaselineAligned = baselineAligned;
    }

    public boolean isBaselineAligned() {
        return mBaselineAligned;
    }

    public void setMeasureWithLargestChildEnabled(boolean enabled) {
        mUseLargestChild = enabled;
    }

    public void setShowDividers(int showDividers) {
        // Dividers are not drawn yet.
    }

    public void setDividerDrawable(android.graphics.drawable.Drawable divider) {
        // Dividers are not drawn yet.
    }

    public void setDividerPadding(int padding) {
        // Dividers are not drawn yet.
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return mOrientation == VERTICAL
                ? new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT)
                : new LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.MATCH_PARENT);
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
        if (mOrientation == VERTICAL) {
            measureVertical(widthMeasureSpec, heightMeasureSpec);
        } else {
            measureHorizontal(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void measureVertical(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int totalLength = 0;
        int maxWidth = 0;
        float totalWeight = 0.0f;
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();
            totalWeight += lp.weight;
            final boolean useExcessSpace = lp.height == 0 && lp.weight > 0;
            if (heightMode == MeasureSpec.MODE_EXACTLY && useExcessSpace) {
                // Height is fully decided by the weight pass; only margins
                // count toward the used space.
                totalLength += lp.topMargin + lp.bottomMargin;
            } else {
                final int oldHeight = lp.height;
                if (useExcessSpace) {
                    lp.height = LayoutParams.WRAP_CONTENT;
                }
                measureChildWithMargins(child, widthMeasureSpec, 0,
                        heightMeasureSpec, totalLength);
                lp.height = oldHeight;
                totalLength += child.getMeasuredHeight()
                        + lp.topMargin + lp.bottomMargin;
            }
            maxWidth = Math.max(maxWidth, child.getMeasuredWidth()
                    + lp.leftMargin + lp.rightMargin);
        }
        totalLength += getPaddingTop() + getPaddingBottom();
        maxWidth += getPaddingLeft() + getPaddingRight();
        final int measuredHeight = resolveSizeAndState(
                Math.max(totalLength, getSuggestedMinimumHeight()),
                heightMeasureSpec, 0);
        int remaining = heightSize - totalLength;
        if (totalWeight > 0.0f && remaining > 0) {
            if (mWeightSum > 0.0f) {
                totalWeight = mWeightSum;
            }
            for (int i = 0; i < count; i++) {
                final View child = getChildAt(i);
                if (child.getVisibility() == GONE) {
                    continue;
                }
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp.weight <= 0.0f) {
                    continue;
                }
                final int share = (int) (remaining * lp.weight / totalWeight);
                remeasureHeight(child, widthMeasureSpec, lp,
                        child.getMeasuredHeight() + share);
            }
        }
        setMeasuredDimension(
                resolveSizeAndState(
                        Math.max(maxWidth, getSuggestedMinimumWidth()),
                        widthMeasureSpec, 0),
                measuredHeight);
    }

    private void measureHorizontal(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int totalLength = 0;
        int maxHeight = 0;
        float totalWeight = 0.0f;
        int maxAscent = 0;
        int maxDescent = 0;
        boolean baselineAligned = mBaselineAligned && heightMode
                != MeasureSpec.MODE_UNSPECIFIED;
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();
            totalWeight += lp.weight;
            final boolean useExcessSpace = lp.width == 0 && lp.weight > 0;
            if (widthMode == MeasureSpec.MODE_EXACTLY && useExcessSpace) {
                totalLength += lp.leftMargin + lp.rightMargin;
            } else {
                final int oldWidth = lp.width;
                if (useExcessSpace) {
                    lp.width = LayoutParams.WRAP_CONTENT;
                }
                measureChildWithMargins(child, widthMeasureSpec, totalLength,
                        heightMeasureSpec, 0);
                lp.width = oldWidth;
                totalLength += child.getMeasuredWidth()
                        + lp.leftMargin + lp.rightMargin;
            }
            final int childHeight = child.getMeasuredHeight()
                    + lp.topMargin + lp.bottomMargin;
            maxHeight = Math.max(maxHeight, childHeight);
            if (baselineAligned) {
                final int baseline = child.getBaseline();
                if (baseline >= 0) {
                    maxAscent = Math.max(maxAscent, baseline + lp.topMargin);
                    maxDescent = Math.max(maxDescent, childHeight - baseline);
                }
            }
        }
        totalLength += getPaddingLeft() + getPaddingRight();
        maxHeight += getPaddingTop() + getPaddingBottom();
        if (baselineAligned) {
            maxHeight = Math.max(maxHeight, maxAscent + maxDescent
                    + getPaddingTop() + getPaddingBottom());
        }
        final int measuredWidth = resolveSizeAndState(
                Math.max(totalLength, getSuggestedMinimumWidth()),
                widthMeasureSpec, 0);
        int remaining = widthSize - totalLength;
        if (totalWeight > 0.0f && remaining > 0) {
            if (mWeightSum > 0.0f) {
                totalWeight = mWeightSum;
            }
            for (int i = 0; i < count; i++) {
                final View child = getChildAt(i);
                if (child.getVisibility() == GONE) {
                    continue;
                }
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp.weight <= 0.0f) {
                    continue;
                }
                final int share = (int) (remaining * lp.weight / totalWeight);
                remeasureWidth(child, heightMeasureSpec, lp,
                        child.getMeasuredWidth() + share);
            }
        }
        setMeasuredDimension(measuredWidth,
                resolveSizeAndState(
                        Math.max(maxHeight, getSuggestedMinimumHeight()),
                        heightMeasureSpec, 0));
    }

    private void remeasureHeight(View child, int widthMeasureSpec,
            LayoutParams lp, int height) {
        final int childWidthSpec = getChildMeasureSpec(widthMeasureSpec,
                getPaddingLeft() + getPaddingRight() + lp.leftMargin
                        + lp.rightMargin, lp.width);
        child.measure(childWidthSpec,
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.MODE_EXACTLY));
    }

    private void remeasureWidth(View child, int heightMeasureSpec,
            LayoutParams lp, int width) {
        final int childHeightSpec = getChildMeasureSpec(heightMeasureSpec,
                getPaddingTop() + getPaddingBottom() + lp.topMargin
                        + lp.bottomMargin, lp.height);
        child.measure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.MODE_EXACTLY),
                childHeightSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mOrientation == VERTICAL) {
            layoutVertical();
        } else {
            layoutHorizontal();
        }
    }

    private void layoutVertical() {
        final int count = getChildCount();
        final int paddingLeft = getPaddingLeft();
        final int width = getWidth();
        final int layoutDirection = getLayoutDirection();
        final int absoluteGravity =
                Gravity.getAbsoluteGravity(mGravity, layoutDirection);
        final int horizontalGravity =
                absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK;
        final int childRight = width - getPaddingRight();
        int childTop = getPaddingTop();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();
            final int childWidth = child.getMeasuredWidth();
            final int childHeight = child.getMeasuredHeight();
            int childLeft;
            if (horizontalGravity == Gravity.CENTER_HORIZONTAL) {
                childLeft = paddingLeft
                        + (childRight - paddingLeft - childWidth) / 2
                        + lp.leftMargin - lp.rightMargin;
            } else if (horizontalGravity == Gravity.RIGHT) {
                childLeft = childRight - childWidth - lp.rightMargin;
            } else {
                childLeft = paddingLeft + lp.leftMargin;
            }
            childTop += lp.topMargin;
            child.layout(childLeft, childTop, childLeft + childWidth,
                    childTop + childHeight);
            childTop += childHeight + lp.bottomMargin;
        }
    }

    private void layoutHorizontal() {
        final int count = getChildCount();
        final int paddingTop = getPaddingTop();
        final int height = getHeight();
        final int layoutDirection = getLayoutDirection();
        final int absoluteGravity =
                Gravity.getAbsoluteGravity(mGravity, layoutDirection);
        final int verticalGravity =
                absoluteGravity & Gravity.VERTICAL_GRAVITY_MASK;
        final int childBottom = height - getPaddingBottom();
        int childLeft = getPaddingLeft();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();
            final int childWidth = child.getMeasuredWidth();
            final int childHeight = child.getMeasuredHeight();
            final int childGravity = lp.gravity >= 0
                    ? Gravity.getAbsoluteGravity(lp.gravity, layoutDirection)
                    : absoluteGravity;
            final int childVerticalGravity =
                    childGravity & Gravity.VERTICAL_GRAVITY_MASK;
            int childTop;
            if (childVerticalGravity == Gravity.CENTER_VERTICAL) {
                childTop = paddingTop
                        + (childBottom - paddingTop - childHeight) / 2
                        + lp.topMargin - lp.bottomMargin;
            } else if (childVerticalGravity == Gravity.BOTTOM) {
                childTop = childBottom - childHeight - lp.bottomMargin;
            } else {
                childTop = paddingTop + lp.topMargin;
            }
            childLeft += lp.leftMargin;
            child.layout(childLeft, childTop, childLeft + childWidth,
                    childTop + childHeight);
            childLeft += childWidth + lp.rightMargin;
        }
    }

    @Override
    public int getBaseline() {
        if (mBaselineAlignedChildIndex >= 0
                && mBaselineAlignedChildIndex < getChildCount()) {
            return getChildAt(mBaselineAlignedChildIndex).getBaseline();
        }
        return super.getBaseline();
    }

    /**
     * AOSP LinearLayout.LayoutParams: margins, a weight for excess-space
     * distribution, and an optional gravity override.
     */
    public static class LayoutParams extends MarginLayoutParams {

        public float weight;
        public int gravity = -1;

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray a = context.obtainStyledAttributes(attrs,
                    new int[] {ATTR_LAYOUT_WEIGHT, ATTR_LAYOUT_GRAVITY});
            try {
                weight = a.getFloat(0, weight);
                gravity = a.getInt(1, gravity);
            } finally {
                a.recycle();
            }
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, float weight) {
            super(width, height);
            this.weight = weight;
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(LayoutParams source) {
            super(source);
            this.weight = source.weight;
            this.gravity = source.gravity;
        }
    }
}
