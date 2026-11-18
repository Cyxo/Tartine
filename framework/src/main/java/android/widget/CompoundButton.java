package android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/**
 * AOSP {@code android.widget.CompoundButton}: a two-state button with a check
 * indicator and a change listener.
 *
 * <p>The shell has no bitmap/path indicator pipeline yet, so a checked/
 * unchecked box (or radio disc) is drawn directly in {@link #onDraw}; an
 * application-supplied button drawable is kept and drawn as well when it can
 * produce pixels.
 */
public class CompoundButton extends Button implements Checkable {

    /** Listener for checked-state changes. */
    public interface OnCheckedChangeListener {
        /** Called after {@code button}'s checked state changed. */
        void onCheckedChanged(CompoundButton button, boolean isChecked);
    }

    private static final int[] CHECKED_STATE_SET = {0x010100A0};
    private static final int ATTR_BUTTON = 0x01010107;
    private static final int ATTR_BUTTON_TINT = 0x0101046F;
    private static final int ATTR_COLOR_PRIMARY = 0x01010433;
    private static final int ATTR_COLOR_CONTROL_ACTIVATED = 0x0101042A;

    private boolean mChecked;
    private boolean mBroadcasting;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private Drawable mButtonDrawable;
    private ColorStateList mButtonTintList;
    private final Paint mIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mAccentColor = 0xFF3F51B5;

    public CompoundButton(Context context) {
        this(context, null);
    }

    public CompoundButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CompoundButton(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CompoundButton(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        android.content.res.TypedArray a = context.obtainStyledAttributes(
                attrs, new int[] {ATTR_BUTTON, ATTR_BUTTON_TINT}, defStyleAttr,
                defStyleRes);
        try {
            mButtonDrawable = a.getDrawable(0);
            mButtonTintList = a.getColorStateList(1);
        } finally {
            a.recycle();
        }
        android.content.res.TypedArray accent = context.obtainStyledAttributes(
                new int[] {ATTR_COLOR_CONTROL_ACTIVATED, ATTR_COLOR_PRIMARY});
        try {
            mAccentColor = accent.getColor(0, mAccentColor);
            mAccentColor = accent.getColor(1, mAccentColor);
        } finally {
            accent.recycle();
        }
        setClickable(true);
        setFocusable(1);
    }

    @Override
    public void setChecked(boolean checked) {
        if (mChecked == checked) {
            return;
        }
        mChecked = checked;
        refreshDrawableState();
        invalidate();
        if (mBroadcasting) {
            return;
        }
        mBroadcasting = true;
        try {
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
            }
        } finally {
            mBroadcasting = false;
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    /** Sets the checked-state listener. */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    /** Sets the optional button indicator drawable. */
    public void setButtonDrawable(Drawable drawable) {
        mButtonDrawable = drawable;
        invalidate();
    }

    /** Sets the optional button indicator drawable from a resource. */
    public void setButtonDrawable(int resId) {
        setButtonDrawable(resId == 0 ? null
                : getResources().getDrawable(resId));
    }

    /** Returns the optional button indicator drawable. */
    public Drawable getButtonDrawable() {
        return mButtonDrawable;
    }

    /** Sets the button indicator tint. */
    public void setButtonTintList(ColorStateList tint) {
        mButtonTintList = tint;
    }

    /** Returns the button indicator tint. */
    public ColorStateList getButtonTintList() {
        return mButtonTintList;
    }

    /** Sets the button indicator tint mode (stored only). */
    public void setButtonTintMode(android.graphics.PorterDuff.Mode mode) {
        // The shell's indicator has no tint pipeline yet.
    }

    /** Returns the button indicator tint mode. */
    public android.graphics.PorterDuff.Mode getButtonTintMode() {
        return android.graphics.PorterDuff.Mode.SRC_IN;
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawIndicator(canvas);
        if (mButtonDrawable != null) {
            int top = getPaddingTop();
            int bottom = getHeight() - getPaddingBottom();
            int size = Math.min(bottom - top, indicatorSize());
            int left = getPaddingLeft();
            mButtonDrawable.setBounds(left, top + (bottom - top - size) / 2,
                    left + size, top + (bottom - top - size) / 2 + size);
            mButtonDrawable.draw(canvas);
        }
        super.onDraw(canvas);
    }

    private void drawIndicator(Canvas canvas) {
        int size = indicatorSize();
        int left = getPaddingLeft();
        int contentHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        int top = getPaddingTop() + Math.max(0, (contentHeight - size) / 2);
        int radius = (int) (size / 5.0f);
        mIndicatorPaint.setStyle(Paint.Style.FILL);
        mIndicatorPaint.setColor(isEnabled() ? mAccentColor : 0xFFBDBDBD);
        if (mChecked) {
            canvas.drawRoundRect(left, top, left + size, top + size, radius,
                    radius, mIndicatorPaint);
            mIndicatorPaint.setColor(0xFFFFFFFF);
            float inset = size / 4.0f;
            if (this instanceof RadioButton) {
                canvas.drawRoundRect(left + inset, top + inset,
                        left + size - inset, top + size - inset,
                        radius / 2.0f, radius / 2.0f, mIndicatorPaint);
            } else {
                canvas.drawRoundRect(left + inset, top + inset,
                        left + size - inset, top + size - inset, radius / 3.0f,
                        radius / 3.0f, mIndicatorPaint);
            }
        } else {
            mIndicatorPaint.setStyle(Paint.Style.STROKE);
            mIndicatorPaint.setStrokeWidth(Math.max(1.0f, size / 12.0f));
            mIndicatorPaint.setColor(isEnabled() ? 0xFF757575 : 0xFFBDBDBD);
            canvas.drawRoundRect(left, top, left + size, top + size, radius,
                    radius, mIndicatorPaint);
        }
    }

    private int indicatorSize() {
        float density = getResources().getDisplayMetrics().density;
        return (int) (20.0f * (density > 0.0f ? density : 1.0f));
    }

    @Override
    public int getCompoundPaddingLeft() {
        return super.getCompoundPaddingLeft() + indicatorSize()
                + (int) (4.0f * getResources().getDisplayMetrics().density);
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return "android.widget.CompoundButton";
    }
}
