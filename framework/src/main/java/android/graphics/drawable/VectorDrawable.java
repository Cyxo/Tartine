package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Xml;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

/**
 * AOSP {@code VectorDrawable}: a viewport-sized collection of filled or
 * stroked paths. Geometry is parsed once during inflation; drawing maps the
 * viewport onto the current bounds and replays it through {@link Canvas}.
 * Groups, clipping paths, animations and trim-path animation are not needed
 * by the static widget vectors currently supported by the shell.
 */
public class VectorDrawable extends Drawable {

    private static final int ATTR_WIDTH = 0x01010159;
    private static final int ATTR_HEIGHT = 0x01010155;
    private static final int ATTR_ALPHA = 0x0101031F;
    private static final int ATTR_VIEWPORT_WIDTH = 0x01010402;
    private static final int ATTR_VIEWPORT_HEIGHT = 0x01010403;
    private static final int ATTR_FILL_COLOR = 0x01010404;
    private static final int ATTR_PATH_DATA = 0x01010405;
    private static final int ATTR_STROKE_COLOR = 0x01010406;
    private static final int ATTR_STROKE_WIDTH = 0x01010407;
    private static final int ATTR_STROKE_ALPHA = 0x010104CB;
    private static final int ATTR_FILL_ALPHA = 0x010104CC;
    private static final int ATTR_FILL_TYPE = 0x0101051E;

    private static final class VectorPath {
        private final Path path;
        private final int fillColor;
        private final int strokeColor;
        private final float strokeWidth;
        private final float fillAlpha;
        private final float strokeAlpha;

        private VectorPath(Path path, int fillColor, int strokeColor,
                float strokeWidth, float fillAlpha, float strokeAlpha) {
            this.path = path;
            this.fillColor = fillColor;
            this.strokeColor = strokeColor;
            this.strokeWidth = strokeWidth;
            this.fillAlpha = fillAlpha;
            this.strokeAlpha = strokeAlpha;
        }
    }

    private final List<VectorPath> mPaths = new ArrayList<>();
    private final Paint mFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mWidth = -1;
    private int mHeight = -1;
    private float mViewportWidth;
    private float mViewportHeight;
    private int mAlpha = 255;

    public VectorDrawable() {
        mStrokePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        if (bounds.isEmpty() || mViewportWidth <= 0.0f
                || mViewportHeight <= 0.0f) {
            return;
        }
        int saveCount = canvas.save();
        canvas.translate(bounds.left, bounds.top);
        canvas.scale(bounds.width() / mViewportWidth,
                bounds.height() / mViewportHeight);
        ColorStateList tint = getTintList();
        for (VectorPath vector : mPaths) {
            int fill = tint == null ? vector.fillColor
                    : tint.getColorForState(getState(), vector.fillColor);
            fill = withAlpha(fill, vector.fillAlpha);
            if ((fill >>> 24) != 0) {
                mFillPaint.setColor(fill);
                canvas.drawPath(vector.path, mFillPaint);
            }
            int stroke = tint == null ? vector.strokeColor
                    : tint.getColorForState(getState(), vector.strokeColor);
            stroke = withAlpha(stroke, vector.strokeAlpha);
            if ((stroke >>> 24) != 0 && vector.strokeWidth > 0.0f) {
                mStrokePaint.setColor(stroke);
                mStrokePaint.setStrokeWidth(vector.strokeWidth);
                canvas.drawPath(vector.path, mStrokePaint);
            }
        }
        canvas.restoreToCount(saveCount);
    }

    /** Combines the path's own alpha with the drawable-wide alpha. */
    private int withAlpha(int color, float pathAlpha) {
        float opacity = Math.max(0.0f, Math.min(1.0f, pathAlpha));
        int alpha = Math.round((color >>> 24) * opacity * mAlpha / 255.0f);
        return (color & 0x00FFFFFF) | (alpha << 24);
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
        mAlpha = Math.max(0, Math.min(255, alpha));
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        super.setColorFilter(colorFilter);
        mFillPaint.setColorFilter(colorFilter);
        mStrokePaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setTintList(ColorStateList tint) {
        super.setTintList(tint);
        invalidateSelf();
    }

    @Override
    public void inflate(Resources resources, XmlPullParser parser,
            AttributeSet attrs, Resources.Theme theme) {
        super.inflate(resources, parser, attrs, theme);
        mPaths.clear();
        TypedArray root = obtainAttributes(resources, theme, attrs,
                new int[] {ATTR_WIDTH, ATTR_HEIGHT, ATTR_ALPHA,
                        ATTR_VIEWPORT_WIDTH, ATTR_VIEWPORT_HEIGHT});
        try {
            mWidth = root.getDimensionPixelSize(0, mWidth);
            mHeight = root.getDimensionPixelSize(1, mHeight);
            mAlpha = Math.round(root.getFloat(2, 1.0f) * 255.0f);
            mViewportWidth = root.getFloat(3, 0.0f);
            mViewportHeight = root.getFloat(4, 0.0f);
        } finally {
            root.recycle();
        }

        int rootDepth = parser.getDepth();
        int event;
        while ((event = parser.next()) != XmlPullParser.END_DOCUMENT) {
            if (event == XmlPullParser.END_TAG
                    && parser.getDepth() <= rootDepth) {
                break;
            }
            if (event == XmlPullParser.START_TAG
                    && "path".equals(parser.getName())) {
                inflatePath(resources, parser, theme);
            }
        }
    }

    /** Reads one static {@code <path>} child. */
    private void inflatePath(Resources resources, XmlPullParser parser,
            Resources.Theme theme) {
        AttributeSet attrs = Xml.asAttributeSet(parser);
        TypedArray a = obtainAttributes(resources, theme, attrs,
                new int[] {ATTR_PATH_DATA, ATTR_FILL_COLOR, ATTR_STROKE_COLOR,
                        ATTR_STROKE_WIDTH, ATTR_FILL_ALPHA, ATTR_STROKE_ALPHA,
                        ATTR_FILL_TYPE});
        try {
            int pathDataId = a.getResourceId(0, 0);
            String pathData = pathDataId == 0 ? a.getString(0)
                    : resources.getString(pathDataId);
            if (pathData == null || pathData.isEmpty()) {
                return;
            }
            Path path = VectorPathParser.parse(pathData);
            if (a.getInt(6, 0) == 1) {
                path.setFillType(Path.FillType.EVEN_ODD);
            }
            mPaths.add(new VectorPath(path, a.getColor(1, 0),
                    a.getColor(2, 0), a.getFloat(3, 0.0f),
                    a.getFloat(4, 1.0f), a.getFloat(5, 1.0f)));
        } finally {
            a.recycle();
        }
    }

    private static TypedArray obtainAttributes(Resources resources,
            Resources.Theme theme, AttributeSet attrs, int[] ids) {
        return theme == null ? resources.obtainAttributes(attrs, ids)
                : theme.obtainStyledAttributes(attrs, ids, 0, 0);
    }
}
