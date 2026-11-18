package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

/**
 * AOSP {@code LayerDrawable}: an ordered stack of drawables, inflated from
 * {@code <layer-list><item android:drawable="..."/></layer-list>}. Per-item
 * insets and explicit widths/heights are honored; gravity is deferred.
 */
public class LayerDrawable extends Drawable implements Drawable.Callback {

    private static final int ATTR_DRAWABLE = 0x01010199;
    private static final int ATTR_LEFT = 0x010101AD;
    private static final int ATTR_TOP = 0x010101AE;
    private static final int ATTR_RIGHT = 0x010101AF;
    private static final int ATTR_BOTTOM = 0x010101B0;
    private static final int ATTR_WIDTH = 0x01010159;
    private static final int ATTR_HEIGHT = 0x01010155;
    private static final int ATTR_ID = 0x010100D0;

    private static final class Layer {
        private Drawable drawable;
        private int id;
        private int left;
        private int top;
        private int right;
        private int bottom;
        private int width = -1;
        private int height = -1;

        private Layer(Drawable drawable) {
            this.drawable = drawable;
        }
    }

    private final List<Layer> mLayers = new ArrayList<>();
    private final Rect mPadding = new Rect();
    private int mAlpha = 255;
    private ColorFilter mColorFilter;

    public LayerDrawable(Drawable[] layers) {
        for (Drawable drawable : layers) {
            mLayers.add(new Layer(drawable));
        }
    }

    public LayerDrawable() {
    }

    public Drawable addLayer(Drawable drawable) {
        Layer layer = new Layer(drawable);
        mLayers.add(layer);
        drawable.setCallback(this);
        drawable.setVisible(isVisible(), false);
        return drawable;
    }

    public int getNumberOfLayers() {
        return mLayers.size();
    }

    public Drawable getDrawable(int index) {
        return mLayers.get(index).drawable;
    }

    public void setDrawable(int index, Drawable drawable) {
        Layer layer = mLayers.get(index);
        if (layer.drawable != null) {
            layer.drawable.setCallback(null);
        }
        layer.drawable = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
            drawable.setVisible(isVisible(), false);
        }
        invalidateSelf();
    }

    public int getId(int index) {
        return mLayers.get(index).id;
    }

    public void setId(int index, int id) {
        mLayers.get(index).id = id;
    }

    public int indexOfChild(Drawable child) {
        for (int i = 0; i < mLayers.size(); i++) {
            if (mLayers.get(i).drawable == child) {
                return i;
            }
        }
        return -1;
    }

    public Drawable findDrawableByLayerId(int id) {
        for (Layer layer : mLayers) {
            if (layer.id == id) {
                return layer.drawable;
            }
        }
        return null;
    }

    public void setLayerInset(int index, int left, int top, int right,
            int bottom) {
        Layer layer = mLayers.get(index);
        layer.left = left;
        layer.top = top;
        layer.right = right;
        layer.bottom = bottom;
        invalidateSelf();
    }

    public void setLayerWidth(int index, int width) {
        mLayers.get(index).width = width;
        invalidateSelf();
    }

    public void setLayerHeight(int index, int height) {
        mLayers.get(index).height = height;
        invalidateSelf();
    }

    public void setLayerGravity(int index, int gravity) {
        // Gravity placement waits for a full gravity solver.
    }

    public void setPadding(int left, int top, int right, int bottom) {
        mPadding.set(left, top, right, bottom);
    }

    public void setPaddingRelative(int start, int top, int end, int bottom) {
        setPadding(start, top, end, bottom);
    }

    @Override
    public boolean getPadding(Rect padding) {
        padding.set(mPadding);
        return !mPadding.isEmpty();
    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        if (bounds.isEmpty()) {
            return;
        }
        for (Layer layer : mLayers) {
            if (layer.drawable == null) {
                continue;
            }
            Rect layerBounds = layerBounds(layer, bounds);
            layer.drawable.setBounds(layerBounds);
            layer.drawable.draw(canvas);
        }
    }

    private static Rect layerBounds(Layer layer, Rect bounds) {
        int left = bounds.left + layer.left;
        int top = bounds.top + layer.top;
        int right = bounds.right - layer.right;
        int bottom = bounds.bottom - layer.bottom;
        if (layer.width >= 0) {
            left = bounds.left + (bounds.width() - layer.width) / 2;
            right = left + layer.width;
        }
        if (layer.height >= 0) {
            top = bounds.top + (bounds.height() - layer.height) / 2;
            bottom = top + layer.height;
        }
        return new Rect(left, top, right, bottom);
    }

    @Override
    public int getIntrinsicWidth() {
        int width = -1;
        for (Layer layer : mLayers) {
            if (layer.drawable != null) {
                width = Math.max(width, layer.drawable.getIntrinsicWidth()
                        + layer.left + layer.right);
            }
        }
        return width;
    }

    @Override
    public int getIntrinsicHeight() {
        int height = -1;
        for (Layer layer : mLayers) {
            if (layer.drawable != null) {
                height = Math.max(height, layer.drawable.getIntrinsicHeight()
                        + layer.top + layer.bottom);
            }
        }
        return height;
    }

    @Override
    public int getAlpha() {
        return mAlpha;
    }

    @Override
    public void setAlpha(int alpha) {
        mAlpha = alpha;
        for (Layer layer : mLayers) {
            if (layer.drawable != null) {
                layer.drawable.setAlpha(alpha);
            }
        }
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mColorFilter = colorFilter;
        for (Layer layer : mLayers) {
            if (layer.drawable != null) {
                layer.drawable.setColorFilter(colorFilter);
            }
        }
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        int opacity = PixelFormat.TRANSPARENT;
        for (Layer layer : mLayers) {
            if (layer.drawable != null) {
                opacity = Drawable.resolveOpacity(opacity,
                        layer.drawable.getOpacity());
            }
        }
        return opacity;
    }

    @Override
    public boolean isStateful() {
        for (Layer layer : mLayers) {
            if (layer.drawable != null && layer.drawable.isStateful()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onStateChange(int[] stateSet) {
        boolean changed = false;
        for (Layer layer : mLayers) {
            if (layer.drawable != null && layer.drawable.isStateful()) {
                changed |= layer.drawable.setState(stateSet);
            }
        }
        return changed;
    }

    @Override
    public void setTintList(ColorStateList tint) {
        super.setTintList(tint);
        for (Layer layer : mLayers) {
            if (layer.drawable != null) {
                layer.drawable.setTintList(tint);
            }
        }
    }

    @Override
    public boolean setVisible(boolean visible, boolean restart) {
        boolean changed = super.setVisible(visible, restart);
        for (Layer layer : mLayers) {
            if (layer.drawable != null) {
                layer.drawable.setVisible(visible, restart);
            }
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
            if (!"item".equals(parser.getName())) {
                continue;
            }
            AttributeSet itemAttrs = parser instanceof AttributeSet
                    ? (AttributeSet) parser : null;
            android.content.res.TypedArray a = resources.obtainAttributes(
                    itemAttrs, new int[] {ATTR_DRAWABLE, ATTR_LEFT,
                            ATTR_TOP, ATTR_RIGHT, ATTR_BOTTOM, ATTR_WIDTH,
                            ATTR_HEIGHT, ATTR_ID});
            try {
                int drawableId = a.getResourceId(0, 0);
                if (drawableId == 0) {
                    continue;
                }
                Drawable drawable = resources.getDrawable(drawableId, theme);
                Layer layer = new Layer(drawable);
                layer.left = a.getDimensionPixelSize(1, 0);
                layer.top = a.getDimensionPixelSize(2, 0);
                layer.right = a.getDimensionPixelSize(3, 0);
                layer.bottom = a.getDimensionPixelSize(4, 0);
                layer.width = a.getDimensionPixelSize(5, -1);
                layer.height = a.getDimensionPixelSize(6, -1);
                layer.id = a.getResourceId(7, 0);
                drawable.setCallback(this);
                mLayers.add(layer);
            } finally {
                a.recycle();
            }
        }
        if (!mLayers.isEmpty()) {
            setAlpha(mAlpha);
        }
    }
}
