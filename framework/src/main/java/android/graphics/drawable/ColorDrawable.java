package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import org.xmlpull.v1.XmlPullParser;

/**
 * AOSP {@code ColorDrawable}: a solid fill over the drawable's bounds.
 * Inflated from {@code <color android:color="..."/>} and created by
 * {@code View.setBackgroundColor}.
 */
public class ColorDrawable extends Drawable {

    private int mColor = 0;
    private Paint mPaint;
    private ColorStateList mTint;

    public ColorDrawable() {
    }

    public ColorDrawable(int color) {
        mColor = color;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        if (color != mColor) {
            mColor = color;
            invalidateSelf();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        if (bounds.isEmpty()) {
            return;
        }
        int color = effectiveColor();
        if ((color >>> 24) != 0) {
            Paint paint = paint();
            paint.setColor(color);
            canvas.drawRect(bounds, paint);
        }
    }

    @Override
    public int getAlpha() {
        return (effectiveColor() >>> 24);
    }

    @Override
    public void setAlpha(int alpha) {
        int current = effectiveColor();
        int color = (current & 0x00FFFFFF) | ((alpha & 0xFF) << 24);
        setColor(color);
    }

    @Override
    public void setTintList(ColorStateList tint) {
        mTint = tint;
        super.setTintList(tint);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        int alpha = effectiveColor() >>> 24;
        if (alpha == 255) {
            return PixelFormat.OPAQUE;
        }
        if (alpha == 0) {
            return PixelFormat.TRANSPARENT;
        }
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        super.setColorFilter(colorFilter);
        if (mPaint != null) {
            mPaint.setColorFilter(colorFilter);
        }
        invalidateSelf();
    }

    @Override
    public void inflate(Resources resources, XmlPullParser parser,
            AttributeSet attrs, Resources.Theme theme) {
        super.inflate(resources, parser, attrs, theme);
        android.content.res.TypedArray a = resources.obtainAttributes(attrs,
                new int[] {ATTR_COLOR});
        try {
            mColor = a.getColor(0, mColor);
        } finally {
            a.recycle();
        }
    }

    /** android.R.attr.color. */
    private static final int ATTR_COLOR = 0x010101A5;

    private int effectiveColor() {
        if (mTint != null) {
            return mTint.getColorForState(getState(), mColor);
        }
        return mColor;
    }

    private Paint paint() {
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
        }
        return mPaint;
    }
}
