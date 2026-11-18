package android.graphics.drawable.shapes;

import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;

/**
 * AOSP {@code android.graphics.drawable.shapes.Shape}: a resizable drawing
 * primitive consumed by {@code ShapeDrawable}.
 */
public abstract class Shape implements Cloneable {

    private float mWidth;
    private float mHeight;

    /** Returns the width last passed to {@link #resize}. */
    public final float getWidth() {
        return mWidth;
    }

    /** Returns the height last passed to {@link #resize}. */
    public final float getHeight() {
        return mHeight;
    }

    /** Draws this shape into {@code canvas} with {@code paint}. */
    public abstract void draw(Canvas canvas, Paint paint);

    /**
     * Resizes the shape; negative extents clamp to zero and a no-op resize
     * does not call {@link #onResize}.
     *
     * @param width  the new width in pixels
     * @param height the new height in pixels
     */
    public final void resize(float width, float height) {
        if (width < 0) {
            width = 0;
        }
        if (height < 0) {
            height = 0;
        }
        if (mWidth != width || mHeight != height) {
            mWidth = width;
            mHeight = height;
            onResize(width, height);
        }
    }

    /** Whether any part of the shape is translucent; default {@code true}. */
    public boolean hasAlpha() {
        return true;
    }

    /** Callback invoked when {@link #resize} actually changes the extents. */
    protected void onResize(float width, float height) {
    }

    /** Populates {@code outline}, if this shape can describe one. */
    public void getOutline(Outline outline) {
    }

    @Override
    public Shape clone() throws CloneNotSupportedException {
        return (Shape) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Shape shape = (Shape) o;
        return Float.compare(shape.mWidth, mWidth) == 0
                && Float.compare(shape.mHeight, mHeight) == 0;
    }

    @Override
    public int hashCode() {
        return 31 * Float.floatToIntBits(mWidth) + Float.floatToIntBits(mHeight);
    }
}
