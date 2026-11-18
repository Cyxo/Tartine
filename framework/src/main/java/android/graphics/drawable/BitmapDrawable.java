package android.graphics.drawable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;

/**
 * AOSP {@code BitmapDrawable}: wraps a decoded {@link Bitmap}.
 *
 * <p>The shell has no bitmap decoder yet ({@code BitmapFactory} and
 * {@code Canvas.drawBitmap} are named gaps), so an inflated {@code <bitmap>}
 * keeps its source resource id and draws nothing. The class exists so
 * resource inflation reaches a drawable instead of failing at class load.
 */
public class BitmapDrawable extends Drawable {

    private static final int ATTR_SRC = 0x01010119;
    private static final int ATTR_GRAVITY = 0x010100AF;

    private Bitmap mBitmap;
    private Paint mPaint;
    private int mGravity;
    private int mTargetDensity;
    private int mSrcResourceId;

    public BitmapDrawable() {
    }

    public BitmapDrawable(Resources resources) {
        mTargetDensity = resources == null ? 0
                : resources.getDisplayMetrics().densityDpi;
    }

    public BitmapDrawable(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public BitmapDrawable(Resources resources, Bitmap bitmap) {
        this(resources);
        mBitmap = bitmap;
    }

    public BitmapDrawable(Resources resources, InputStream stream) {
        this(resources);
        // Decoding a stream waits for a BitmapFactory port; keeping the
        // constructor total lets icon-heavy layouts inflate without crashing.
    }

    public final Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        invalidateSelf();
    }

    public int getGravity() {
        return mGravity;
    }

    public void setGravity(int gravity) {
        mGravity = gravity;
    }

    @Override
    public void draw(Canvas canvas) {
        if (mBitmap == null) {
            return;
        }
        // Canvas.drawBitmap is a named gap; drawing an icon requires the
        // bitmap/paint transport the renderer seam does not have yet.
        canvas.drawBitmap(mBitmap, (Rect) null, getBounds(), paint());
    }

    @Override
    public int getIntrinsicWidth() {
        return mBitmap == null ? -1 : mBitmap.getWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return mBitmap == null ? -1 : mBitmap.getHeight();
    }

    @Override
    public int getAlpha() {
        return mBitmap == null ? 255 : paint().getAlpha();
    }

    @Override
    public void setAlpha(int alpha) {
        paint().setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        paint().setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return mBitmap == null ? PixelFormat.TRANSLUCENT
                : PixelFormat.TRANSLUCENT;
    }

    public void setTargetDensity(DisplayMetrics metrics) {
        mTargetDensity = metrics.densityDpi;
    }

    public void setTargetDensity(int density) {
        mTargetDensity = density;
    }

    public int getTargetDensity() {
        return mTargetDensity;
    }

    /** Source resource id retained for the future bitmap factory port. */
    int getSrcResourceId() {
        return mSrcResourceId;
    }

    private Paint paint() {
        if (mPaint == null) {
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG
                    | Paint.FILTER_BITMAP_FLAG);
        }
        return mPaint;
    }

    @Override
    public void inflate(Resources resources, XmlPullParser parser,
            AttributeSet attrs, Resources.Theme theme) {
        super.inflate(resources, parser, attrs, theme);
        android.content.res.TypedArray a = resources.obtainAttributes(attrs,
                new int[] {ATTR_SRC, ATTR_GRAVITY});
        try {
            mSrcResourceId = a.getResourceId(0, 0);
            mGravity = a.getInt(1, mGravity);
        } finally {
            a.recycle();
        }
    }
}
