package android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * AOSP {@code android.widget.ProgressBar}, reduced to the determinate bar the
 * probe layouts use: a rounded track with a proportional fill. Animation,
 * indeterminate cycling and the nine-patch progress drawable wait for the
 * image pipeline.
 */
public class ProgressBar extends View {

    private static final int ATTR_MAX = 0x01010136;
    private static final int ATTR_PROGRESS = 0x01010137;
    private static final int ATTR_INDETERMINATE = 0x01010139;
    private static final int ATTR_PROGRESS_DRAWABLE = 0x0101013C;
    private static final int ATTR_MIN = 0x01010539;
    private static final int ATTR_COLOR_PRIMARY = 0x01010433;
    private static final int ATTR_COLOR_CONTROL_ACTIVATED = 0x0101042A;

    private int mMax = 100;
    private int mProgress;
    private int mSecondaryProgress;
    private int mMin;
    private boolean mIndeterminate;
    private Drawable mProgressDrawable;
    private ColorStateList mProgressTint;
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mProgressColor = 0xFF3F51B5;

    public ProgressBar(Context context) {
        this(context, null);
    }

    public ProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ProgressBar(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr);
        float density = getResources().getDisplayMetrics().density;
        int size = (int) (48.0f * (density > 0.0f ? density : 1.0f));
        if (getSuggestedMinimumWidth() == 0) {
            setMinimumWidth(size);
        }
        if (getSuggestedMinimumHeight() == 0) {
            setMinimumHeight(size);
        }
        if (attrs != null) {
            android.content.res.TypedArray a = context.obtainStyledAttributes(
                    attrs, new int[] {ATTR_MAX, ATTR_PROGRESS,
                            ATTR_INDETERMINATE, ATTR_PROGRESS_DRAWABLE,
                            ATTR_MIN}, defStyleAttr, defStyleRes);
            try {
                mMax = a.getInt(0, mMax);
                mProgress = a.getInt(1, mProgress);
                mIndeterminate = a.getBoolean(2, false);
                mProgressDrawable = a.getDrawable(3);
                mMin = a.getInt(4, mMin);
            } finally {
                a.recycle();
            }
        }
        android.content.res.TypedArray accent = context.obtainStyledAttributes(
                new int[] {ATTR_COLOR_CONTROL_ACTIVATED, ATTR_COLOR_PRIMARY});
        try {
            mProgressColor = accent.getColor(0, mProgressColor);
            mProgressColor = accent.getColor(1, mProgressColor);
        } finally {
            accent.recycle();
        }
    }

    /** Sets the maximum progress value. */
    public synchronized void setMax(int max) {
        if (max < 0) {
            max = 0;
        }
        if (mMax != max) {
            mMax = max;
            postInvalidate();
        }
    }

    /** Returns the maximum progress value. */
    public synchronized int getMax() {
        return mMax;
    }

    /** Sets the minimum progress value. */
    public synchronized void setMin(int min) {
        mMin = min;
        postInvalidate();
    }

    /** Returns the minimum progress value. */
    public synchronized int getMin() {
        return mMin;
    }

    /** Sets the current progress and repaints. */
    public synchronized void setProgress(int progress) {
        setProgressInternal(progress);
    }

    /** Sets the current progress with an optional animation (direct here). */
    public synchronized void setProgress(int progress, boolean animate) {
        setProgressInternal(progress);
    }

    private void setProgressInternal(int progress) {
        if (progress < mMin) {
            progress = mMin;
        }
        if (progress > mMax) {
            progress = mMax;
        }
        if (mProgress != progress) {
            mProgress = progress;
            postInvalidate();
        }
    }

    /** Returns the current progress. */
    public synchronized int getProgress() {
        return mIndeterminate ? 0 : mProgress;
    }

    /** Sets the secondary progress and repaints. */
    public synchronized void setSecondaryProgress(int secondaryProgress) {
        if (secondaryProgress < 0) {
            secondaryProgress = 0;
        }
        if (secondaryProgress > mMax) {
            secondaryProgress = mMax;
        }
        mSecondaryProgress = secondaryProgress;
        postInvalidate();
    }

    /** Returns the secondary progress. */
    public synchronized int getSecondaryProgress() {
        return mIndeterminate ? 0 : mSecondaryProgress;
    }

    /** Sets the indeterminate mode. */
    public synchronized void setIndeterminate(boolean indeterminate) {
        mIndeterminate = indeterminate;
        postInvalidate();
    }

    /** Returns whether the bar is indeterminate. */
    public synchronized boolean isIndeterminate() {
        return mIndeterminate;
    }

    /** Sets the progress drawable (kept for compatibility). */
    public void setProgressDrawable(Drawable drawable) {
        mProgressDrawable = drawable;
        postInvalidate();
    }

    /** Returns the progress drawable. */
    public Drawable getProgressDrawable() {
        return mProgressDrawable;
    }

    /** Sets the progress tint. */
    public void setProgressTintList(ColorStateList tint) {
        mProgressTint = tint;
        if (tint != null) {
            mProgressColor = tint.getDefaultColor();
        }
        postInvalidate();
    }

    /** Returns the progress tint. */
    public ColorStateList getProgressTintList() {
        return mProgressTint;
    }

    /** Sets the progress tint mode (stored only). */
    public void setProgressTintMode(PorterDuff.Mode mode) {
        // The bar is filled with a flat color in the staging renderer.
    }

    /** Returns the progress tint mode. */
    public PorterDuff.Mode getProgressTintMode() {
        return PorterDuff.Mode.SRC_IN;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                resolveSizeAndState(
                        Math.max(getSuggestedMinimumWidth(),
                                getPaddingLeft() + getPaddingRight()),
                        widthMeasureSpec, 0),
                resolveSizeAndState(
                        Math.max(getSuggestedMinimumHeight(),
                                getPaddingTop() + getPaddingBottom()),
                        heightMeasureSpec, 0));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = getWidth() - getPaddingRight();
        int bottom = getHeight() - getPaddingBottom();
        if (right <= left || bottom <= top) {
            return;
        }
        float radius = (bottom - top) / 2.0f;
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0xFFE0E0E0);
        canvas.drawRoundRect(left, top, right, bottom, radius, radius, mPaint);
        int range = mMax - mMin;
        float fraction = mIndeterminate ? 0.25f
                : range <= 0 ? 0.0f
                        : (float) (mProgress - mMin) / range;
        float fill = left + (right - left) * Math.max(0.0f,
                Math.min(1.0f, fraction));
        if (fill > left) {
            mPaint.setColor(isEnabled() ? mProgressColor : 0xFF9E9E9E);
            canvas.drawRoundRect(left, top, fill, bottom, radius, radius,
                    mPaint);
        }
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return "android.widget.ProgressBar";
    }
}
