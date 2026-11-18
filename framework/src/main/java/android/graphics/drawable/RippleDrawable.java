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
 * AOSP {@code RippleDrawable}, reduced to its resting appearance: the content
 * layer an app places inside {@code <ripple>} is drawn, and the pressed-state
 * ripple animation is not rendered yet (the shell has no animator/ripple
 * pipeline). The ripple color is parsed so themable widgets keep their
 * declaration, but drawing is limited to the content drawable.
 */
public class RippleDrawable extends Drawable implements Drawable.Callback {

    private static final int ATTR_COLOR = 0x010101A5;
    private static final int ATTR_DRAWABLE = 0x01010199;

    private ColorStateList mColor;
    private Drawable mContent;
    private boolean mHasContent;

    public RippleDrawable() {
    }

    public RippleDrawable(ColorStateList color, Drawable content,
            Drawable mask) {
        mColor = color;
        mContent = content;
        mHasContent = content != null;
        if (content != null) {
            content.setCallback(this);
            content.setVisible(isVisible(), false);
        }
    }

    public void setColor(ColorStateList color) {
        mColor = color;
        invalidateSelf();
    }

    public ColorStateList getColor() {
        return mColor;
    }

    public void setRadius(float radius) {
        // The ripple geometry is not drawn yet.
    }

    public float getRadius() {
        return 0.0f;
    }

    public Drawable getDrawable(int index) {
        return index == 0 ? mContent : null;
    }

    public void setDrawable(int index, Drawable drawable) {
        if (index == 0) {
            mContent = drawable;
            mHasContent = drawable != null;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (mContent != null) {
            mContent.setBounds(getBounds());
            mContent.draw(canvas);
        }
    }

    @Override
    public int getIntrinsicWidth() {
        return mContent == null ? -1 : mContent.getIntrinsicWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return mContent == null ? -1 : mContent.getIntrinsicHeight();
    }

    @Override
    public boolean getPadding(Rect padding) {
        return mContent != null && mContent.getPadding(padding);
    }

    @Override
    public int getAlpha() {
        return mContent == null ? 255 : mContent.getAlpha();
    }

    @Override
    public void setAlpha(int alpha) {
        if (mContent != null) {
            mContent.setAlpha(alpha);
        }
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        if (mContent != null) {
            mContent.setColorFilter(colorFilter);
        }
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return mContent == null ? PixelFormat.TRANSLUCENT
                : mContent.getOpacity();
    }

    @Override
    public boolean isStateful() {
        return true;
    }

    @Override
    public boolean onStateChange(int[] stateSet) {
        return mContent != null && mContent.isStateful()
                && mContent.setState(stateSet);
    }

    @Override
    public void setTintList(ColorStateList tint) {
        super.setTintList(tint);
        if (mContent != null) {
            mContent.setTintList(tint);
        }
    }

    @Override
    public boolean setVisible(boolean visible, boolean restart) {
        boolean changed = super.setVisible(visible, restart);
        if (mContent != null) {
            mContent.setVisible(visible, restart);
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
                new int[] {ATTR_COLOR});
        try {
            mColor = a.getColorStateList(0);
        } finally {
            a.recycle();
        }
        final int rootDepth = parser.getDepth();
        int type;
        while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
            if (type == XmlPullParser.END_TAG
                    && parser.getDepth() == rootDepth) {
                break;
            }
            if (type != XmlPullParser.START_TAG
                    || !"item".equals(parser.getName())) {
                continue;
            }
            AttributeSet itemAttrs = parser instanceof AttributeSet
                    ? (AttributeSet) parser : null;
            android.content.res.TypedArray item = resources.obtainAttributes(
                    itemAttrs, new int[] {ATTR_DRAWABLE});
            try {
                int drawableId = item.getResourceId(0, 0);
                if (drawableId != 0 && !mHasContent) {
                    mContent = resources.getDrawable(drawableId, theme);
                    mHasContent = true;
                    mContent.setCallback(this);
                    mContent.setVisible(isVisible(), false);
                }
            } finally {
                item.recycle();
            }
        }
    }
}
