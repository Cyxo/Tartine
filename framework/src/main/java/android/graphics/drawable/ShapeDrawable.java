package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.shapes.Shape;

/**
 * AOSP {@code android.graphics.drawable.ShapeDrawable}: draws a resizable
 * {@link Shape} through a managed {@link Paint}.
 *
 * <p>Staging reductions: XML inflation and the tint blend-mode filter wait on
 * the drawable XML/style pipeline (the shell already inflates app
 * {@code <shape>} resources into {@code GradientDrawable}); the paint,
 * shape-resize, alpha and outline behavior is ported. Tint state falls back to
 * {@link Drawable}'s stored tint list.
 */
public class ShapeDrawable extends Drawable {

    private ShapeState mShapeState;
    private boolean mMutated;

    /** AOSP ShapeDrawable(): a drawable with no shape yet. */
    public ShapeDrawable() {
        this(new ShapeState(), null);
    }

    /**
     * AOSP ShapeDrawable(Shape): a drawable that resizes and draws {@code s}.
     *
     * @param s the shape to manage
     */
    public ShapeDrawable(Shape s) {
        this(new ShapeState(), null);
        mShapeState.mShape = s;
    }

    private ShapeDrawable(ShapeState state, Resources res) {
        mShapeState = state;
        updateLocalState();
    }

    /** Returns the managed shape, or null when none was installed. */
    public Shape getShape() {
        return mShapeState.mShape;
    }

    /** Installs a new shape and resizes it to the current bounds. */
    public void setShape(Shape s) {
        mShapeState.mShape = s;
        updateShape();
    }

    /** Installs the shader factory queried on every resize. */
    public void setShaderFactory(ShaderFactory fact) {
        mShapeState.mShaderFactory = fact;
    }

    /** Returns the shader factory, or null. */
    public ShaderFactory getShaderFactory() {
        return mShapeState.mShaderFactory;
    }

    /** Returns the paint used to draw the shape. */
    public Paint getPaint() {
        return mShapeState.mPaint;
    }

    /**
     * Sets shape padding; all-zero clears it.
     *
     * @param left   left padding in pixels
     * @param top    top padding in pixels
     * @param right  right padding in pixels
     * @param bottom bottom padding in pixels
     */
    public void setPadding(int left, int top, int right, int bottom) {
        if ((left | top | right | bottom) == 0) {
            mShapeState.mPadding = null;
        } else {
            if (mShapeState.mPadding == null) {
                mShapeState.mPadding = new Rect();
            }
            mShapeState.mPadding.set(left, top, right, bottom);
        }
        invalidateSelf();
    }

    /** Sets shape padding from a rect; null clears it. */
    public void setPadding(Rect padding) {
        if (padding == null) {
            mShapeState.mPadding = null;
        } else {
            if (mShapeState.mPadding == null) {
                mShapeState.mPadding = new Rect();
            }
            mShapeState.mPadding.set(padding);
        }
        invalidateSelf();
    }

    /** Sets the intrinsic (default) width in pixels. */
    public void setIntrinsicWidth(int width) {
        mShapeState.mIntrinsicWidth = width;
        invalidateSelf();
    }

    /** Sets the intrinsic (default) height in pixels. */
    public void setIntrinsicHeight(int height) {
        mShapeState.mIntrinsicHeight = height;
        invalidateSelf();
    }

    @Override
    public int getIntrinsicWidth() {
        return mShapeState.mIntrinsicWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mShapeState.mIntrinsicHeight;
    }

    @Override
    public boolean getPadding(Rect padding) {
        if (mShapeState.mPadding != null) {
            padding.set(mShapeState.mPadding);
            return true;
        }
        return super.getPadding(padding);
    }

    private static int modulateAlpha(int paintAlpha, int alpha) {
        int scale = alpha + (alpha >>> 7); // convert to 0..256
        return paintAlpha * scale >>> 8;
    }

    /**
     * Called after the canvas is translated to the bounds origin. Subclasses
     * may override to add effects around the shape.
     */
    protected void onDraw(Shape shape, Canvas canvas, Paint paint) {
        shape.draw(canvas, paint);
    }

    @Override
    public void draw(Canvas canvas) {
        final Rect r = getBounds();
        final ShapeState state = mShapeState;
        final Paint paint = state.mPaint;

        final int prevAlpha = paint.getAlpha();
        paint.setAlpha(modulateAlpha(prevAlpha, state.mAlpha));

        // Only draw the shape if it may affect output.
        if (paint.getAlpha() != 0 || paint.getXfermode() != null) {
            if (state.mShape != null) {
                // Save both for the translate and for the (unknown) Shape.
                final int count = canvas.save();
                canvas.translate(r.left, r.top);
                onDraw(state.mShape, canvas, paint);
                canvas.restoreToCount(count);
            } else {
                canvas.drawRect(r, paint);
            }
        }

        paint.setAlpha(prevAlpha);
    }

    @Override
    public int getChangingConfigurations() {
        return super.getChangingConfigurations()
                | mShapeState.getChangingConfigurations();
    }

    @Override
    public void setAlpha(int alpha) {
        mShapeState.mAlpha = alpha;
        invalidateSelf();
    }

    @Override
    public int getAlpha() {
        return mShapeState.mAlpha;
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mShapeState.mPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        if (mShapeState.mShape == null) {
            final Paint p = mShapeState.mPaint;
            if (p.getXfermode() == null) {
                final int alpha = p.getAlpha();
                if (alpha == 0) {
                    return PixelFormat.TRANSPARENT;
                }
                if (alpha == 255) {
                    return PixelFormat.OPAQUE;
                }
            }
        }
        // Not sure, so be safe.
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setDither(boolean dither) {
        mShapeState.mPaint.setDither(dither);
        invalidateSelf();
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        updateShape();
    }

    @Override
    public boolean isStateful() {
        final ShapeState s = mShapeState;
        return super.isStateful() || (s.mTint != null && s.mTint.isStateful());
    }

    private void updateShape() {
        if (mShapeState.mShape != null) {
            final Rect r = getBounds();
            final int w = r.width();
            final int h = r.height();

            mShapeState.mShape.resize(w, h);
            if (mShapeState.mShaderFactory != null) {
                mShapeState.mPaint.setShader(
                        mShapeState.mShaderFactory.resize(w, h));
            }
        }
        invalidateSelf();
    }

    @Override
    public void getOutline(Outline outline) {
        if (mShapeState.mShape != null) {
            mShapeState.mShape.getOutline(outline);
            outline.setAlpha(getAlpha() / 255.0f);
        }
    }

    @Override
    public ConstantState getConstantState() {
        mShapeState.mChangingConfigurations = getChangingConfigurations();
        return mShapeState;
    }

    @Override
    public Drawable mutate() {
        if (!mMutated && super.mutate() == this) {
            mShapeState = new ShapeState(mShapeState);
            updateLocalState();
            mMutated = true;
        }
        return this;
    }

    /** AOSP (hidden) clearMutated: forget the private state copy. */
    public void clearMutated() {
        mMutated = false;
    }

    private void updateLocalState() {
        // Tint is held by Drawable; nothing to rebuild without the blend-mode
        // color-filter pipeline.
    }

    /**
     * AOSP {@code ShapeDrawable.ShaderFactory}: produces the shader used for
     * a given drawable size.
     */
    public abstract static class ShaderFactory {
        /** Returns a shader for a {@code width}x{@code height} shape. */
        public abstract Shader resize(int width, int height);
    }

    /** AOSP ShapeDrawable.ShapeState: the constant state of a shape. */
    static final class ShapeState extends ConstantState {
        final Paint mPaint;

        int mChangingConfigurations;
        Shape mShape;
        ColorStateList mTint;
        Rect mPadding;
        int mIntrinsicWidth;
        int mIntrinsicHeight;
        int mAlpha = 255;
        ShaderFactory mShaderFactory;

        ShapeState() {
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }

        ShapeState(ShapeState orig) {
            mChangingConfigurations = orig.mChangingConfigurations;
            mPaint = new Paint(orig.mPaint);
            if (orig.mShape != null) {
                try {
                    mShape = orig.mShape.clone();
                } catch (CloneNotSupportedException e) {
                    // Well, at least we tried.
                    mShape = orig.mShape;
                }
            }
            mTint = orig.mTint;
            if (orig.mPadding != null) {
                mPadding = new Rect(orig.mPadding);
            }
            mIntrinsicWidth = orig.mIntrinsicWidth;
            mIntrinsicHeight = orig.mIntrinsicHeight;
            mAlpha = orig.mAlpha;
            mShaderFactory = orig.mShaderFactory;
        }

        @Override
        public Drawable newDrawable() {
            return new ShapeDrawable(this, null);
        }

        @Override
        public Drawable newDrawable(Resources res) {
            return new ShapeDrawable(this, res);
        }

        @Override
        public int getChangingConfigurations() {
            return mChangingConfigurations;
        }
    }
}
