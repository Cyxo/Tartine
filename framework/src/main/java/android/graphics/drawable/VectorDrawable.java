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
 * AOSP {@code VectorDrawable}, reduced to its declared size: the shell has no
 * path renderer yet ({@code Canvas.drawPath} is a named gap), so a vector
 * icon measures and occupies space but rasterises to nothing. Keeping the
 * class present lets resource-heavy layouts inflate and lay out.
 */
public class VectorDrawable extends Drawable {

    private static final int ATTR_WIDTH = 0x01010159;
    private static final int ATTR_HEIGHT = 0x01010155;
    private static final int ATTR_ALPHA = 0x0101031F;

    private int mWidth = -1;
    private int mHeight = -1;
    private int mAlpha = 255;

    public VectorDrawable() {
    }

    @Override
    public void draw(Canvas canvas) {
        // Vector paths are not rasterised yet; the icon leaves the frame
        // transparent instead of failing the draw pass.
    }

    @Override
    public int getIntrinsicWidth() {
        return mWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mHeight;
    }

    @Override
    public int getAlpha() {
        return mAlpha;
    }

    @Override
    public void setAlpha(int alpha) {
        mAlpha = alpha;
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        // Paths are not drawn yet, so there is nothing to filter.
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setTintList(ColorStateList tint) {
        super.setTintList(tint);
    }

    @Override
    public void inflate(Resources resources, XmlPullParser parser,
            AttributeSet attrs, Resources.Theme theme) {
        super.inflate(resources, parser, attrs, theme);
        android.content.res.TypedArray a = resources.obtainAttributes(attrs,
                new int[] {ATTR_WIDTH, ATTR_HEIGHT, ATTR_ALPHA});
        try {
            mWidth = a.getDimensionPixelSize(0, mWidth);
            mHeight = a.getDimensionPixelSize(1, mHeight);
            mAlpha = a.getInt(2, mAlpha);
        } finally {
            a.recycle();
        }
    }
}
