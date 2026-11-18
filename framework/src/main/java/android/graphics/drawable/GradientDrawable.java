package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import java.util.Arrays;
import org.xmlpull.v1.XmlPullParser;

/**
 * AOSP {@code GradientDrawable}, reduced to the attributes a compiled
 * {@code <shape>} drawable carries: shape, solid color, corner radii, stroke,
 * padding and explicit size.
 *
 * <p>Gradients are parsed but painted as their start color for now: the
 * renderer seam has no shader upload yet, and a solid approximation keeps
 * layout and text readable instead of dropping the background entirely.
 */
public class GradientDrawable extends Drawable {

    public static final int LINE = 2;
    public static final int OVAL = 1;
    public static final int RECTANGLE = 0;
    public static final int RING = 3;

    public static final int LINEAR_GRADIENT = 0;
    public static final int RADIAL_GRADIENT = 1;
    public static final int SWEEP_GRADIENT = 2;

    private static final int ATTR_SHAPE = 0x0101019A;
    private static final int ATTR_USE_LEVEL = 0x0101019F;
    private static final int ATTR_COLOR = 0x010101A5;
    private static final int ATTR_RADIUS = 0x010101A8;
    private static final int ATTR_TOP_LEFT_RADIUS = 0x010101A9;
    private static final int ATTR_TOP_RIGHT_RADIUS = 0x010101AA;
    private static final int ATTR_BOTTOM_LEFT_RADIUS = 0x010101AB;
    private static final int ATTR_BOTTOM_RIGHT_RADIUS = 0x010101AC;
    private static final int ATTR_WIDTH = 0x01010159;
    private static final int ATTR_HEIGHT = 0x01010155;
    private static final int ATTR_LEFT = 0x010101AD;
    private static final int ATTR_TOP = 0x010101AE;
    private static final int ATTR_RIGHT = 0x010101AF;
    private static final int ATTR_BOTTOM = 0x010101B0;
    private static final int ATTR_START_COLOR = 0x0101019D;
    private static final int ATTR_END_COLOR = 0x0101019E;
    private static final int ATTR_CENTER_COLOR = 0x0101020B;
    private static final int ATTR_ANGLE = 0x010101A0;
    private static final int ATTR_GRADIENT_TYPE = 0x010101A1;
    private static final int ATTR_GRADIENT_RADIUS = 0x010101A4;

    private int mShape = RECTANGLE;
    private int mGradientType = LINEAR_GRADIENT;
    private int mSolidColor;
    private ColorStateList mSolidColors;
    private int mStartColor;
    private int mEndColor;
    private int mCenterColor;
    private boolean mHasCenterColor;
    private boolean mUseLevel;
    private float mGradientRadius;
    private float mGradientAngle;
    private float mCornerRadius;
    private final float[] mCornerRadii = new float[8];
    private int mStrokeWidth;
    private int mStrokeColor;
    private float mStrokeDashWidth;
    private float mStrokeDashGap;
    private int mWidth = -1;
    private int mHeight = -1;
    private final Rect mPadding = new Rect();
    private final Paint mFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public GradientDrawable() {
    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        if (bounds.isEmpty()) {
            return;
        }
        if (mShape == LINE) {
            drawLine(canvas, bounds);
            return;
        }
        int fill = fillColor();
        boolean rounded = isRounded();
        if ((fill >>> 24) != 0) {
            mFillPaint.setColor(fill);
            mFillPaint.setStyle(Paint.Style.FILL);
            if (mShape == OVAL || mShape == RING) {
                canvas.drawRoundRect(bounds.left, bounds.top, bounds.right,
                        bounds.bottom, bounds.width() / 2.0f,
                        bounds.height() / 2.0f, mFillPaint);
            } else if (rounded) {
                float radius = maxCornerRadius();
                canvas.drawRoundRect(bounds.left, bounds.top, bounds.right,
                        bounds.bottom, radius, radius, mFillPaint);
            } else {
                canvas.drawRect(bounds, mFillPaint);
            }
        }
        if (mStrokeWidth > 0 && (mStrokeColor >>> 24) != 0) {
            mStrokePaint.setColor(mStrokeColor);
            mStrokePaint.setStyle(Paint.Style.STROKE);
            mStrokePaint.setStrokeWidth(mStrokeWidth);
            if (mShape == OVAL || mShape == RING) {
                canvas.drawRoundRect(bounds.left, bounds.top, bounds.right,
                        bounds.bottom, bounds.width() / 2.0f,
                        bounds.height() / 2.0f, mStrokePaint);
            } else if (rounded) {
                float radius = maxCornerRadius();
                canvas.drawRoundRect(bounds.left, bounds.top, bounds.right,
                        bounds.bottom, radius, radius, mStrokePaint);
            } else {
                canvas.drawRect(bounds, mStrokePaint);
            }
        }
    }

    private void drawLine(Canvas canvas, Rect bounds) {
        int color = (mStrokeColor >>> 24) != 0 ? mStrokeColor : fillColor();
        float thickness = mStrokeWidth > 0 ? mStrokeWidth : 1.0f;
        if ((color >>> 24) == 0) {
            return;
        }
        mFillPaint.setColor(color);
        mFillPaint.setStyle(Paint.Style.FILL);
        float centerY = (bounds.top + bounds.bottom) / 2.0f;
        canvas.drawRect(bounds.left, centerY - thickness / 2.0f, bounds.right,
                centerY + thickness / 2.0f, mFillPaint);
    }

    private boolean isRounded() {
        if (mCornerRadius > 0) {
            return true;
        }
        for (float radius : mCornerRadii) {
            if (radius > 0) {
                return true;
            }
        }
        return false;
    }

    private float maxCornerRadius() {
        float radius = mCornerRadius;
        for (float corner : mCornerRadii) {
            radius = Math.max(radius, corner);
        }
        return Math.min(radius, Math.min(getBounds().width(),
                getBounds().height()) / 2.0f);
    }

    private int fillColor() {
        int base = mSolidColors != null
                ? mSolidColors.getColorForState(getState(), mSolidColor)
                : mSolidColor;
        if (getTintList() != null) {
            return getTintList().getColorForState(getState(), base);
        }
        if ((base >>> 24) == 0 && mStartColor != 0) {
            return mStartColor;
        }
        return base;
    }

    @Override
    public int getAlpha() {
        return fillColor() >>> 24;
    }

    @Override
    public void setAlpha(int alpha) {
        int color = fillColor();
        mSolidColor = (color & 0x00FFFFFF) | ((alpha & 0xFF) << 24);
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
        int alpha = fillColor() >>> 24;
        if (alpha == 255) {
            return PixelFormat.OPAQUE;
        }
        if (alpha == 0) {
            return PixelFormat.TRANSPARENT;
        }
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public boolean getPadding(Rect padding) {
        padding.set(mPadding);
        return !mPadding.isEmpty();
    }

    @Override
    public int getIntrinsicWidth() {
        return mWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mHeight;
    }

    public int getShape() {
        return mShape;
    }

    public void setShape(int shape) {
        mShape = shape;
        invalidateSelf();
    }

    public void setColor(int argb) {
        mSolidColor = argb;
        mSolidColors = null;
        invalidateSelf();
    }

    public void setColor(ColorStateList colorStateList) {
        mSolidColors = colorStateList;
        mSolidColor = colorStateList == null ? 0
                : colorStateList.getDefaultColor();
        invalidateSelf();
    }

    public ColorStateList getColor() {
        return mSolidColors;
    }

    public void setCornerRadius(float radius) {
        mCornerRadius = Math.max(0.0f, radius);
        Arrays.fill(mCornerRadii, 0.0f);
        invalidateSelf();
    }

    public float getCornerRadius() {
        return mCornerRadius;
    }

    public void setCornerRadii(float[] radii) {
        if (radii == null) {
            Arrays.fill(mCornerRadii, 0.0f);
            mCornerRadius = 0.0f;
        } else {
            System.arraycopy(radii, 0, mCornerRadii, 0,
                    Math.min(radii.length, mCornerRadii.length));
        }
        invalidateSelf();
    }

    public float[] getCornerRadii() {
        return mCornerRadii.clone();
    }

    public void setStroke(int width, int color) {
        mStrokeWidth = width;
        mStrokeColor = color;
        invalidateSelf();
    }

    public void setStroke(int width, int color, float dashWidth,
            float dashGap) {
        setStroke(width, color);
        mStrokeDashWidth = dashWidth;
        mStrokeDashGap = dashGap;
    }

    public void setStroke(int width, ColorStateList colorStateList) {
        setStroke(width, colorStateList == null ? 0
                : colorStateList.getDefaultColor());
    }

    public int getStrokeWidth() {
        return mStrokeWidth;
    }

    public ColorStateList getStrokeColor() {
        return ColorStateList.valueOf(mStrokeColor);
    }

    public void setSize(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    public void setGradientType(int gradient) {
        mGradientType = gradient;
    }

    public int getGradientType() {
        return mGradientType;
    }

    public void setGradientRadius(float gradientRadius) {
        mGradientRadius = gradientRadius;
    }

    public void setGradientCenter(float x, float y) {
    }

    public void setUseLevel(boolean useLevel) {
        mUseLevel = useLevel;
    }

    @Override
    public void inflate(Resources resources, XmlPullParser parser,
            AttributeSet attrs, Resources.Theme theme) {
        super.inflate(resources, parser, attrs, theme);
        android.content.res.TypedArray root = resources.obtainAttributes(attrs,
                new int[] {ATTR_SHAPE, ATTR_USE_LEVEL, ATTR_WIDTH, ATTR_HEIGHT,
                        ATTR_COLOR});
        try {
            mShape = root.getInt(0, mShape);
            mUseLevel = root.getBoolean(1, mUseLevel);
            int width = root.getDimensionPixelSize(2, -1);
            int height = root.getDimensionPixelSize(3, -1);
            if (width >= 0) {
                mWidth = width;
            }
            if (height >= 0) {
                mHeight = height;
            }
            int color = root.getColor(4, 0);
            if (color != 0) {
                mSolidColor = color;
            }
        } finally {
            root.recycle();
        }
        final int rootDepth = parser.getDepth();
        int type;
        while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
            if (type == XmlPullParser.END_TAG
                    && parser.getDepth() == rootDepth) {
                break;
            }
            if (type != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            AttributeSet childAttrs = parser instanceof AttributeSet
                    ? (AttributeSet) parser : null;
            inflateChild(resources, theme, tag, childAttrs);
        }
    }

    private void inflateChild(Resources resources, Resources.Theme theme,
            String tag, AttributeSet attrs) {
        if ("solid".equals(tag)) {
            android.content.res.TypedArray a = resources.obtainAttributes(
                    attrs, new int[] {ATTR_COLOR});
            try {
                mSolidColor = a.getColor(0, mSolidColor);
                mSolidColors = a.getColorStateList(0);
            } finally {
                a.recycle();
            }
        } else if ("corners".equals(tag)) {
            android.content.res.TypedArray a = resources.obtainAttributes(
                    attrs, new int[] {ATTR_RADIUS, ATTR_TOP_LEFT_RADIUS,
                            ATTR_TOP_RIGHT_RADIUS, ATTR_BOTTOM_LEFT_RADIUS,
                            ATTR_BOTTOM_RIGHT_RADIUS});
            try {
                float radius = a.getDimension(0, 0.0f);
                if (radius > 0) {
                    mCornerRadius = radius;
                }
                float topLeft = a.getDimension(1, radius);
                float topRight = a.getDimension(2, radius);
                float bottomLeft = a.getDimension(3, radius);
                float bottomRight = a.getDimension(4, radius);
                mCornerRadii[0] = topLeft;
                mCornerRadii[1] = topLeft;
                mCornerRadii[2] = topRight;
                mCornerRadii[3] = topRight;
                mCornerRadii[4] = bottomRight;
                mCornerRadii[5] = bottomRight;
                mCornerRadii[6] = bottomLeft;
                mCornerRadii[7] = bottomLeft;
            } finally {
                a.recycle();
            }
        } else if ("stroke".equals(tag)) {
            android.content.res.TypedArray a = resources.obtainAttributes(
                    attrs, new int[] {ATTR_WIDTH, ATTR_COLOR});
            try {
                mStrokeWidth = a.getDimensionPixelSize(0, mStrokeWidth);
                mStrokeColor = a.getColor(1, mStrokeColor);
            } finally {
                a.recycle();
            }
        } else if ("padding".equals(tag)) {
            android.content.res.TypedArray a = resources.obtainAttributes(
                    attrs, new int[] {ATTR_LEFT, ATTR_TOP, ATTR_RIGHT,
                            ATTR_BOTTOM});
            try {
                mPadding.left = a.getDimensionPixelSize(0, 0);
                mPadding.top = a.getDimensionPixelSize(1, 0);
                mPadding.right = a.getDimensionPixelSize(2, 0);
                mPadding.bottom = a.getDimensionPixelSize(3, 0);
            } finally {
                a.recycle();
            }
        } else if ("size".equals(tag)) {
            android.content.res.TypedArray a = resources.obtainAttributes(
                    attrs, new int[] {ATTR_WIDTH, ATTR_HEIGHT});
            try {
                mWidth = a.getDimensionPixelSize(0, mWidth);
                mHeight = a.getDimensionPixelSize(1, mHeight);
            } finally {
                a.recycle();
            }
        } else if ("gradient".equals(tag)) {
            android.content.res.TypedArray a = resources.obtainAttributes(
                    attrs, new int[] {ATTR_START_COLOR, ATTR_END_COLOR,
                            ATTR_CENTER_COLOR, ATTR_ANGLE, ATTR_GRADIENT_TYPE,
                            ATTR_GRADIENT_RADIUS, ATTR_USE_LEVEL});
            try {
                mStartColor = a.getColor(0, mStartColor);
                mEndColor = a.getColor(1, mEndColor);
                mCenterColor = a.getColor(2, 0);
                mHasCenterColor = a.hasValue(2);
                mGradientAngle = a.getFloat(3, mGradientAngle);
                mGradientType = a.getInt(4, mGradientType);
                mGradientRadius = a.getDimension(5, mGradientRadius);
                mUseLevel = a.getBoolean(6, mUseLevel);
            } finally {
                a.recycle();
            }
        }
    }
}
