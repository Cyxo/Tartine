package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Insets;
import android.graphics.Outline;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.StateSet;
import android.util.TypedValue;
import java.io.InputStream;
import java.util.Arrays;
import org.xmlpull.v1.XmlPullParser;

    public class Drawable {

    /** AOSP Drawable.ZERO_BOUNDS_RECT: the "bounds never set" sentinel. */
    private static final Rect ZERO_BOUNDS_RECT = new Rect();

    private Rect mBounds = ZERO_BOUNDS_RECT;
    private Drawable.Callback mCallback;
    private int[] mState = StateSet.WILD_CARD;
    private int mLevel;
    private boolean mVisible = true;
    private int mLayoutDirection = 0;
    private int mAlpha = 255;
    private boolean mAutoMirrored;
    private boolean mFilterBitmap;
    private int mChangingConfigurations;
    private ColorFilter mColorFilter;
    private ColorStateList mTintList;
    private PorterDuff.Mode mTintMode;
    private BlendMode mTintBlendMode;

    public Drawable() {
    }

    public void applyTheme(Resources.Theme arg0) {
        // No theme-dependent state in the base drawable.
    }

    public boolean canApplyTheme() {
        return false;
    }

    public void clearColorFilter() {
        mColorFilter = null;
    }

    public final Rect copyBounds() {
        return new Rect(mBounds);
    }

    public final void copyBounds(Rect arg0) {
        arg0.set(mBounds);
    }

    public static Drawable createFromPath(String arg0) {
        throw new UnsupportedOperationException(
                "Tartine: not implemented: "
                + "Landroid/graphics/drawable/Drawable;->createFromPath"
                + "(Ljava/lang/String;)Landroid/graphics/drawable/Drawable;");
    }

    public static Drawable createFromResourceStream(Resources arg0,
            TypedValue arg1, InputStream arg2, String arg3) {
        throw new UnsupportedOperationException(
                "Tartine: not implemented: "
                + "Landroid/graphics/drawable/Drawable;->createFromResourceStream"
                + "(Landroid/content/res/Resources;Landroid/util/TypedValue;"
                + "Ljava/io/InputStream;Ljava/lang/String;)"
                + "Landroid/graphics/drawable/Drawable;");
    }

    public static Drawable createFromResourceStream(Resources arg0,
            TypedValue arg1, InputStream arg2, String arg3,
            BitmapFactory.Options arg4) {
        throw new UnsupportedOperationException(
                "Tartine: not implemented: "
                + "Landroid/graphics/drawable/Drawable;->createFromResourceStream"
                + "(Landroid/content/res/Resources;Landroid/util/TypedValue;"
                + "Ljava/io/InputStream;Ljava/lang/String;"
                + "Landroid/graphics/BitmapFactory$Options;)"
                + "Landroid/graphics/drawable/Drawable;");
    }

    public static Drawable createFromStream(InputStream arg0, String arg1) {
        throw new UnsupportedOperationException(
                "Tartine: not implemented: "
                + "Landroid/graphics/drawable/Drawable;->createFromStream"
                + "(Ljava/io/InputStream;Ljava/lang/String;)"
                + "Landroid/graphics/drawable/Drawable;");
    }

    public static Drawable createFromXml(Resources arg0,
            XmlPullParser arg1) {
        return createFromXml(arg0, arg1, null);
    }

    /**
     * AOSP Drawable.createFromXml: advance to the root start tag, dispatch on
     * the element name, and let the concrete drawable read its attributes.
     */
    public static Drawable createFromXml(Resources arg0,
            XmlPullParser arg1, Resources.Theme arg2) {
        AttributeSet attrs = arg1 instanceof AttributeSet
                ? (AttributeSet) arg1 : null;
        int type;
        do {
            type = arg1.next();
        } while (type != XmlPullParser.START_TAG
                && type != XmlPullParser.END_DOCUMENT);
        if (type != XmlPullParser.START_TAG) {
            throw new RuntimeException("Tartine: drawable XML has no root element");
        }
        return createFromXmlInner(arg0, arg1, attrs, arg2);
    }

    public static Drawable createFromXmlInner(Resources arg0,
            XmlPullParser arg1, AttributeSet arg2) {
        return createFromXmlInner(arg0, arg1, arg2, null);
    }

    /**
     * AOSP Drawable.createFromXmlInner: the platform drawable tags a compiled
     * {@code res/drawable/*.xml} can use. Unknown tags stay a named gap.
     */
    public static Drawable createFromXmlInner(Resources arg0,
            XmlPullParser arg1, AttributeSet arg2, Resources.Theme arg3) {
        String name = arg1.getName();
        Drawable drawable;
        if ("selector".equals(name)) {
            drawable = new StateListDrawable();
        } else if ("shape".equals(name)) {
            drawable = new GradientDrawable();
        } else if ("layer-list".equals(name)) {
            drawable = new LayerDrawable();
        } else if ("inset".equals(name)) {
            drawable = new InsetDrawable();
        } else if ("ripple".equals(name)) {
            drawable = new RippleDrawable();
        } else if ("color".equals(name)) {
            drawable = new ColorDrawable();
        } else if ("bitmap".equals(name)) {
            drawable = new BitmapDrawable();
        } else if ("vector".equals(name)) {
            drawable = new VectorDrawable();
        } else if ("animated-vector".equals(name)
                || "animated-selector".equals(name)
                || "level-list".equals(name)
                || "transition".equals(name)
                || "rotate".equals(name)
                || "scale".equals(name)
                || "clip".equals(name)
                || "nine-patch".equals(name)) {
            throw new UnsupportedOperationException(
                    "Tartine: not implemented: drawable XML <" + name + ">");
        } else {
            throw new UnsupportedOperationException(
                    "Tartine: not implemented: drawable XML <" + name + ">");
        }
        drawable.inflate(arg0, arg1, arg2, arg3);
        return drawable;
    }

    public void draw(Canvas arg0) {
        // AOSP Drawable.draw is abstract (core/java/android/graphics/drawable/
        // Drawable.java). The shell keeps a concrete no-op so an unported
        // subclass linked against it still loads; real drawables override.
    }

    public int getAlpha() {
        return mAlpha;
    }

    public final Rect getBounds() {
        return mBounds == ZERO_BOUNDS_RECT ? new Rect() : mBounds;
    }

    public Drawable.Callback getCallback() {
        return mCallback;
    }

    public int getChangingConfigurations() {
        return mChangingConfigurations;
    }

    public ColorFilter getColorFilter() {
        return mColorFilter;
    }

    public Drawable.ConstantState getConstantState() {
        return null;
    }

    public Drawable getCurrent() {
        return this;
    }

    public Rect getDirtyBounds() {
        return getBounds();
    }

    public void getHotspotBounds(Rect arg0) {
        arg0.set(getBounds());
    }

    public int getIntrinsicHeight() {
        return -1;
    }

    public int getIntrinsicWidth() {
        return -1;
    }

    public int getLayoutDirection() {
        return mLayoutDirection;
    }

    public final int getLevel() {
        return mLevel;
    }

    public int getMinimumHeight() {
        return 0;
    }

    public int getMinimumWidth() {
        return 0;
    }

    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public Insets getOpticalInsets() {
        return Insets.NONE;
    }

    public void getOutline(Outline arg0) {
        Rect bounds = getBounds();
        if (!bounds.isEmpty()) {
            arg0.setRect(bounds);
        }
    }

    public boolean getPadding(Rect arg0) {
        return false;
    }

    public int[] getState() {
        return mState;
    }

    public Region getTransparentRegion() {
        return null;
    }

    public boolean hasFocusStateSpecified() {
        return false;
    }

    public void inflate(Resources arg0, XmlPullParser arg1,
            AttributeSet arg2) {
        inflate(arg0, arg1, arg2, null);
    }

    public void inflate(Resources arg0, XmlPullParser arg1,
            AttributeSet arg2, Resources.Theme arg3) {
        // Base drawables without attributes need no inflation.
    }

    public void invalidateSelf() {
        if (mCallback != null) {
            mCallback.invalidateDrawable(this);
        }
    }

    public boolean isAutoMirrored() {
        return mAutoMirrored;
    }

    public boolean isFilterBitmap() {
        return mFilterBitmap;
    }

    public boolean isProjected() {
        return false;
    }

    public boolean isStateful() {
        return false;
    }

    public final boolean isVisible() {
        return mVisible;
    }

    public void jumpToCurrentState() {
    }

    public Drawable mutate() {
        return this;
    }

    protected void onBoundsChange(Rect arg0) {
    }

    public boolean onLayoutDirectionChanged(int arg0) {
        return false;
    }

    protected boolean onLevelChange(int arg0) {
        return false;
    }

    protected boolean onStateChange(int[] arg0) {
        return false;
    }

    public static int resolveOpacity(int arg0, int arg1) {
        if (arg0 == arg1) {
            return arg0;
        }
        if (arg0 == PixelFormat.TRANSLUCENT
                || arg1 == PixelFormat.TRANSLUCENT) {
            return PixelFormat.TRANSLUCENT;
        }
        if (arg0 == PixelFormat.TRANSPARENT) {
            return arg1;
        }
        if (arg1 == PixelFormat.TRANSPARENT) {
            return arg0;
        }
        return PixelFormat.TRANSLUCENT;
    }

    public void scheduleSelf(Runnable arg0, long arg1) {
        if (mCallback != null) {
            mCallback.scheduleDrawable(this, arg0, arg1);
        }
    }

    public void setAlpha(int arg0) {
        mAlpha = arg0;
    }

    public void setAutoMirrored(boolean arg0) {
        mAutoMirrored = arg0;
    }

    public void setBounds(Rect arg0) {
        setBounds(arg0.left, arg0.top, arg0.right, arg0.bottom);
    }

    public void setBounds(int arg0, int arg1, int arg2, int arg3) {
        if (mBounds == ZERO_BOUNDS_RECT) {
            mBounds = new Rect();
        }
        if (mBounds.left != arg0 || mBounds.top != arg1
                || mBounds.right != arg2 || mBounds.bottom != arg3) {
            mBounds.set(arg0, arg1, arg2, arg3);
            onBoundsChange(mBounds);
        }
    }

    public final void setCallback(Drawable.Callback arg0) {
        mCallback = arg0;
    }

    public void setChangingConfigurations(int arg0) {
        mChangingConfigurations = arg0;
    }

    public void setColorFilter(ColorFilter arg0) {
        mColorFilter = arg0;
    }

    public void setColorFilter(int arg0, PorterDuff.Mode arg1) {
        // No color-filter model; Android's PorterDuff overload builds a
        // ColorFilter on a device. Keeping the call inert preserves the
        // drawable's own paint until filters land.
    }

    public void setDither(boolean arg0) {
    }

    public void setFilterBitmap(boolean arg0) {
        mFilterBitmap = arg0;
    }

    public void setHotspot(float arg0, float arg1) {
    }

    public void setHotspotBounds(int arg0, int arg1, int arg2, int arg3) {
    }

    public final boolean setLayoutDirection(int arg0) {
        if (mLayoutDirection != arg0) {
            mLayoutDirection = arg0;
            return onLayoutDirectionChanged(arg0);
        }
        return false;
    }

    public final boolean setLevel(int arg0) {
        if (mLevel != arg0) {
            mLevel = arg0;
            return onLevelChange(arg0);
        }
        return false;
    }

    public boolean setState(int[] arg0) {
        if (mState == arg0) {
            return false;
        }
        int[] state = arg0 == null ? StateSet.WILD_CARD : arg0;
        if (Arrays.equals(mState, state)) {
            mState = state;
            return false;
        }
        mState = state;
        if (onStateChange(state)) {
            invalidateSelf();
            return true;
        }
        return false;
    }

    public void setTint(int arg0) {
        setTintList(ColorStateList.valueOf(arg0));
    }

    public void setTintBlendMode(BlendMode arg0) {
        mTintBlendMode = arg0;
    }

    public void setTintList(ColorStateList arg0) {
        mTintList = arg0;
    }

    public void setTintMode(PorterDuff.Mode arg0) {
        mTintMode = arg0;
    }

    public boolean setVisible(boolean arg0, boolean arg1) {
        if (mVisible != arg0) {
            mVisible = arg0;
            return true;
        }
        return false;
    }

    public void unscheduleSelf(Runnable arg0) {
        if (mCallback != null) {
            mCallback.unscheduleDrawable(this, arg0);
        }
    }

    /** Current tint list, for subclass painting. */
    protected ColorStateList getTintList() {
        return mTintList;
    }

    /** Current tint mode, for subclass painting. */
    protected PorterDuff.Mode getTintMode() {
        return mTintMode;
    }

    /** Current tint blend mode, for subclass painting. */
    protected BlendMode getTintBlendMode() {
        return mTintBlendMode;
    }

        public static interface Callback {

        public abstract void invalidateDrawable(Drawable arg0);

        public abstract void scheduleDrawable(Drawable arg0, Runnable arg1, long arg2);

        public abstract void unscheduleDrawable(Drawable arg0, Runnable arg1);
        }
        public static class ConstantState {

        public ConstantState() {
        }

        public boolean canApplyTheme() {
            return false;
        }

        public int getChangingConfigurations() {
            return 0;
        }

        public Drawable newDrawable() {
            return null;
        }

        public Drawable newDrawable(Resources arg0) {
            return newDrawable();
        }

        public Drawable newDrawable(Resources arg0, Resources.Theme arg1) {
            return newDrawable();
        }
        }
    }
