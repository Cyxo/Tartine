package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import org.xmlpull.v1.XmlPullParser;

/**
 * AOSP {@code InsetDrawable}: wraps another drawable and insets its bounds.
 * Inflated from {@code <inset android:drawable="..." android:insetLeft="...">}
 * and returned by {@code Drawable.setBounds} callers that need padding built
 * into the background.
 */
public class InsetDrawable extends Drawable implements Drawable.Callback {

    private static final int ATTR_DRAWABLE = 0x01010199;
    private static final int ATTR_INSET_LEFT = 0x010101B7;
    private static final int ATTR_INSET_RIGHT = 0x010101B8;
    private static final int ATTR_INSET_TOP = 0x010101B9;
    private static final int ATTR_INSET_BOTTOM = 0x010101BA;
    private static final int ATTR_INSET = 0x010104B5;

    private Drawable mDrawable;
    private int mInsetLeft;
    private int mInsetRight;
    private int mInsetTop;
    private int mInsetBottom;

    public InsetDrawable() {
    }

    public InsetDrawable(Drawable drawable, int inset) {
        this(drawable, inset, inset, inset, inset);
    }

    public InsetDrawable(Drawable drawable, int insetLeft, int insetTop,
            int insetRight, int insetBottom) {
        mDrawable = drawable;
        mInsetLeft = insetLeft;
        mInsetTop = insetTop;
        mInsetRight = insetRight;
        mInsetBottom = insetBottom;
        if (drawable != null) {
            drawable.setCallback(this);
            drawable.setVisible(isVisible(), false);
        }
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    @Override
    public void draw(Canvas canvas) {
        if (mDrawable == null) {
            return;
        }
        mDrawable.setBounds(getBounds().left + mInsetLeft,
                getBounds().top + mInsetTop,
                getBounds().right - mInsetRight,
                getBounds().bottom - mInsetBottom);
        mDrawable.draw(canvas);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        if (mDrawable != null) {
            mDrawable.setBounds(bounds.left + mInsetLeft,
                    bounds.top + mInsetTop, bounds.right - mInsetRight,
                    bounds.bottom - mInsetBottom);
        }
    }

    @Override
    public int getIntrinsicWidth() {
        if (mDrawable == null) {
            return -1;
        }
        int width = mDrawable.getIntrinsicWidth();
        return width < 0 ? -1 : width + mInsetLeft + mInsetRight;
    }

    @Override
    public int getIntrinsicHeight() {
        if (mDrawable == null) {
            return -1;
        }
        int height = mDrawable.getIntrinsicHeight();
        return height < 0 ? -1 : height + mInsetTop + mInsetBottom;
    }

    @Override
    public int getMinimumWidth() {
        return mDrawable == null ? 0
                : mDrawable.getMinimumWidth() + mInsetLeft + mInsetRight;
    }

    @Override
    public int getMinimumHeight() {
        return mDrawable == null ? 0
                : mDrawable.getMinimumHeight() + mInsetTop + mInsetBottom;
    }

    @Override
    public boolean getPadding(Rect padding) {
        if (mDrawable == null) {
            return false;
        }
        boolean pad = mDrawable.getPadding(padding);
        padding.left += mInsetLeft;
        padding.top += mInsetTop;
        padding.right += mInsetRight;
        padding.bottom += mInsetBottom;
        return pad || !padding.isEmpty();
    }

    @Override
    public int getAlpha() {
        return mDrawable == null ? 255 : mDrawable.getAlpha();
    }

    @Override
    public void setAlpha(int alpha) {
        if (mDrawable != null) {
            mDrawable.setAlpha(alpha);
        }
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        if (mDrawable != null) {
            mDrawable.setColorFilter(colorFilter);
        }
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return mDrawable == null ? PixelFormat.TRANSLUCENT
                : mDrawable.getOpacity();
    }

    @Override
    public boolean isStateful() {
        return mDrawable != null && mDrawable.isStateful();
    }

    @Override
    public boolean onStateChange(int[] stateSet) {
        return mDrawable != null && mDrawable.isStateful()
                && mDrawable.setState(stateSet);
    }

    @Override
    public void setTintList(ColorStateList tint) {
        super.setTintList(tint);
        if (mDrawable != null) {
            mDrawable.setTintList(tint);
        }
    }

    @Override
    public boolean setVisible(boolean visible, boolean restart) {
        boolean changed = super.setVisible(visible, restart);
        if (mDrawable != null) {
            mDrawable.setVisible(visible, restart);
        }
        return changed;
    }

    @Override
    public void invalidateDrawable(Drawable who) {
        invalidateSelf();
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        scheduleSelf(what, when);
    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
        unscheduleSelf(what);
    }

    @Override
    public void inflate(Resources resources, XmlPullParser parser,
            AttributeSet attrs, Resources.Theme theme) {
        super.inflate(resources, parser, attrs, theme);
        android.content.res.TypedArray a = resources.obtainAttributes(attrs,
                new int[] {ATTR_DRAWABLE, ATTR_INSET_LEFT, ATTR_INSET_RIGHT,
                        ATTR_INSET_TOP, ATTR_INSET_BOTTOM, ATTR_INSET});
        try {
            int drawableId = a.getResourceId(0, 0);
            int inset = a.getDimensionPixelSize(5, 0);
            mInsetLeft = a.getDimensionPixelSize(1, inset);
            mInsetRight = a.getDimensionPixelSize(2, inset);
            mInsetTop = a.getDimensionPixelSize(3, inset);
            mInsetBottom = a.getDimensionPixelSize(4, inset);
            if (drawableId != 0) {
                mDrawable = resources.getDrawable(drawableId, theme);
                mDrawable.setCallback(this);
                mDrawable.setVisible(isVisible(), false);
            }
        } finally {
            a.recycle();
        }
    }
}
