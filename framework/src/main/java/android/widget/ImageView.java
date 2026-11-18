package android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * AOSP {@code android.widget.ImageView}: hosts one drawable and places it in
 * the content box according to {@link ScaleType}. The shell has no bitmap/
 * vector rasteriser yet, so the class owns sizing and destination bounds; the
 * actual pixels wait for the image transport.
 */
public class ImageView extends View {

    /** AOSP ImageView.ScaleType. */
    public enum ScaleType {
        MATRIX, FIT_XY, FIT_START, FIT_CENTER, FIT_END, CENTER, CENTER_CROP,
        CENTER_INSIDE
    }

    private static final int ATTR_SRC = 0x01010119;
    private static final int ATTR_SCALE_TYPE = 0x0101011D;
    private static final int ATTR_ADJUST_VIEW_BOUNDS = 0x0101011E;
    private static final int ATTR_MAX_WIDTH = 0x0101011F;
    private static final int ATTR_MAX_HEIGHT = 0x01010120;
    private static final int ATTR_TINT = 0x01010121;
    private static final int ATTR_ALPHA = 0x0101031F;

    private Drawable mDrawable;
    private ScaleType mScaleType = ScaleType.FIT_CENTER;
    private boolean mAdjustViewBounds;
    private int mMaxWidth = Integer.MAX_VALUE;
    private int mMaxHeight = Integer.MAX_VALUE;
    private int mAlpha = 255;
    private final Matrix mMatrix = new Matrix();
    private ColorStateList mTint;

    public ImageView(Context context) {
        this(context, null);
    }

    public ImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ImageView(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr);
        if (attrs == null) {
            return;
        }
        android.content.res.TypedArray a = context.obtainStyledAttributes(
                attrs, new int[] {ATTR_SRC, ATTR_SCALE_TYPE,
                        ATTR_ADJUST_VIEW_BOUNDS, ATTR_MAX_WIDTH, ATTR_MAX_HEIGHT,
                        ATTR_TINT, ATTR_ALPHA}, defStyleAttr, defStyleRes);
        try {
            if (a.hasValue(0)) {
                setImageDrawable(a.getDrawable(0));
            }
            if (a.hasValue(1)) {
                int index = a.getInt(1, 3);
                ScaleType[] values = ScaleType.values();
                if (index >= 0 && index < values.length) {
                    mScaleType = values[index];
                }
            }
            mAdjustViewBounds = a.getBoolean(2, false);
            mMaxWidth = a.getDimensionPixelSize(3, Integer.MAX_VALUE);
            mMaxHeight = a.getDimensionPixelSize(4, Integer.MAX_VALUE);
            mTint = a.getColorStateList(5);
            if (a.hasValue(6)) {
                mAlpha = a.getInt(6, 255);
            }
        } finally {
            a.recycle();
        }
    }

    /** Sets the image from a drawable resource id. */
    public void setImageResource(int resId) {
        setImageDrawable(resId == 0 ? null : getResources().getDrawable(resId));
    }

    /** Sets the image from decoded bitmap pixels. */
    public void setImageBitmap(Bitmap bitmap) {
        setImageDrawable(bitmap == null ? null
                : new BitmapDrawable(getResources(), bitmap));
    }

    /** Sets the hosted drawable. */
    public void setImageDrawable(Drawable drawable) {
        if (mDrawable == drawable) {
            return;
        }
        mDrawable = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
            drawable.setVisible(getVisibility() == VISIBLE, false);
        }
        requestLayout();
        invalidate();
    }

    /** Returns the hosted drawable. */
    public Drawable getDrawable() {
        return mDrawable;
    }

    /** Sets the placement policy. */
    public void setScaleType(ScaleType scaleType) {
        if (scaleType != null && mScaleType != scaleType) {
            mScaleType = scaleType;
            requestLayout();
            invalidate();
        }
    }

    /** Returns the placement policy. */
    public ScaleType getScaleType() {
        return mScaleType;
    }

    /** Sets whether the view size follows the image aspect ratio. */
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        mAdjustViewBounds = adjustViewBounds;
        if (adjustViewBounds) {
            mScaleType = ScaleType.FIT_CENTER;
        }
    }

    /** Returns whether the view size follows the image aspect ratio. */
    public boolean getAdjustViewBounds() {
        return mAdjustViewBounds;
    }

    /** Sets the maximum width used when measuring. */
    public void setMaxWidth(int maxWidth) {
        mMaxWidth = maxWidth;
        requestLayout();
    }

    /** Returns the maximum width used when measuring. */
    public int getMaxWidth() {
        return mMaxWidth;
    }

    /** Sets the maximum height used when measuring. */
    public void setMaxHeight(int maxHeight) {
        mMaxHeight = maxHeight;
        requestLayout();
    }

    /** Returns the maximum height used when measuring. */
    public int getMaxHeight() {
        return mMaxHeight;
    }

    /** Sets the drawable alpha. */
    public void setImageAlpha(int alpha) {
        mAlpha = alpha;
        if (mDrawable != null) {
            mDrawable.setAlpha(alpha);
        }
        invalidate();
    }

    /** Returns the drawable alpha. */
    public int getImageAlpha() {
        return mAlpha;
    }

    /** Sets the image tint list. */
    public void setImageTintList(ColorStateList tint) {
        mTint = tint;
        if (mDrawable != null) {
            mDrawable.setTintList(tint);
        }
    }

    /** Returns the image tint list. */
    public ColorStateList getImageTintList() {
        return mTint;
    }

    /** Sets the image tint mode (stored only). */
    public void setImageTintMode(PorterDuff.Mode mode) {
        // The drawable tint pipeline is not ported yet.
    }

    /** Returns the image tint mode. */
    public PorterDuff.Mode getImageTintMode() {
        return PorterDuff.Mode.SRC_IN;
    }

    /** Sets a color filter on the hosted drawable. */
    public void setColorFilter(ColorFilter filter) {
        if (mDrawable != null) {
            mDrawable.setColorFilter(filter);
        }
        invalidate();
    }

    /** Returns the image matrix (identity placement). */
    public Matrix getImageMatrix() {
        return mMatrix;
    }

    /** Sets the image matrix and switches to {@link ScaleType#MATRIX}. */
    public void setImageMatrix(Matrix matrix) {
        mMatrix.set(matrix);
        mScaleType = ScaleType.MATRIX;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        int height = 0;
        if (mDrawable != null) {
            width = Math.max(0, mDrawable.getIntrinsicWidth());
            height = Math.max(0, mDrawable.getIntrinsicHeight());
        }
        width += getPaddingLeft() + getPaddingRight();
        height += getPaddingTop() + getPaddingBottom();
        width = Math.max(width, getSuggestedMinimumWidth());
        height = Math.max(height, getSuggestedMinimumHeight());
        width = Math.min(width, mMaxWidth);
        height = Math.min(height, mMaxHeight);
        setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec, 0),
                resolveSizeAndState(height, heightMeasureSpec, 0));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDrawable == null) {
            return;
        }
        drawDrawable(canvas);
    }

    private void drawDrawable(Canvas canvas) {
        int vwidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int vheight = getHeight() - getPaddingTop() - getPaddingBottom();
        if (vwidth <= 0 || vheight <= 0) {
            return;
        }
        int dwidth = Math.max(0, mDrawable.getIntrinsicWidth());
        int dheight = Math.max(0, mDrawable.getIntrinsicHeight());
        if (dwidth <= 0 || dheight <= 0) {
            mDrawable.setBounds(0, 0, vwidth, vheight);
            drawInside(canvas);
            return;
        }
        Rect bounds = computeBounds(vwidth, vheight, dwidth, dheight);
        mDrawable.setBounds(bounds);
        drawInside(canvas);
    }

    private void drawInside(Canvas canvas) {
        int save = canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());
        mDrawable.draw(canvas);
        canvas.restoreToCount(save);
    }

    private Rect computeBounds(int vwidth, int vheight, int dwidth,
            int dheight) {
        Rect out = new Rect();
        switch (mScaleType) {
            case FIT_XY:
                out.set(0, 0, vwidth, vheight);
                break;
            case CENTER:
                out.set((vwidth - dwidth) / 2, (vheight - dheight) / 2,
                        (vwidth + dwidth) / 2, (vheight + dheight) / 2);
                break;
            case CENTER_CROP: {
                float scale = Math.max((float) vwidth / dwidth,
                        (float) vheight / dheight);
                int width = (int) (dwidth * scale + 0.5f);
                int height = (int) (dheight * scale + 0.5f);
                out.set((vwidth - width) / 2, (vheight - height) / 2,
                        (vwidth + width) / 2, (vheight + height) / 2);
                break;
            }
            case CENTER_INSIDE: {
                float scale = Math.min(1.0f, Math.min(
                        (float) vwidth / dwidth, (float) vheight / dheight));
                int width = (int) (dwidth * scale + 0.5f);
                int height = (int) (dheight * scale + 0.5f);
                out.set((vwidth - width) / 2, (vheight - height) / 2,
                        (vwidth + width) / 2, (vheight + height) / 2);
                break;
            }
            case FIT_START:
            case FIT_END:
            case FIT_CENTER:
            default: {
                float scale = Math.min((float) vwidth / dwidth,
                        (float) vheight / dheight);
                int width = (int) (dwidth * scale + 0.5f);
                int height = (int) (dheight * scale + 0.5f);
                int left = 0;
                if (mScaleType == ScaleType.FIT_CENTER) {
                    left = (vwidth - width) / 2;
                } else if (mScaleType == ScaleType.FIT_END) {
                    left = vwidth - width;
                }
                int top = (vheight - height) / 2;
                out.set(left, top, left + width, top + height);
                break;
            }
        }
        return out;
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return "android.widget.ImageView";
    }
}
