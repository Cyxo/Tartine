package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Insets;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.TypedValue;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;

    public class Drawable {

    public Drawable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;-><init>()V");
    }

    public void applyTheme(Resources.Theme arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->applyTheme(Landroid/content/res/Resources$Theme;)V");
    }

    public boolean canApplyTheme() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->canApplyTheme()Z");
    }

    public void clearColorFilter() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->clearColorFilter()V");
    }

    public final Rect copyBounds() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->copyBounds()Landroid/graphics/Rect;");
    }

    public final void copyBounds(Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->copyBounds(Landroid/graphics/Rect;)V");
    }

    public static Drawable createFromPath(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->createFromPath(Ljava/lang/String;)Landroid/graphics/drawable/Drawable;");
    }

    public static Drawable createFromResourceStream(Resources arg0, TypedValue arg1, InputStream arg2, String arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->createFromResourceStream(Landroid/content/res/Resources;Landroid/util/TypedValue;Ljava/io/InputStream;Ljava/lang/String;)Landroid/graphics/drawable/Drawable;");
    }

    public static Drawable createFromResourceStream(Resources arg0, TypedValue arg1, InputStream arg2, String arg3, BitmapFactory.Options arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->createFromResourceStream(Landroid/content/res/Resources;Landroid/util/TypedValue;Ljava/io/InputStream;Ljava/lang/String;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/drawable/Drawable;");
    }

    public static Drawable createFromStream(InputStream arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->createFromStream(Ljava/io/InputStream;Ljava/lang/String;)Landroid/graphics/drawable/Drawable;");
    }

    public static Drawable createFromXml(Resources arg0, XmlPullParser arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->createFromXml(Landroid/content/res/Resources;Lorg/xmlpull/v1/XmlPullParser;)Landroid/graphics/drawable/Drawable;");
    }

    public static Drawable createFromXml(Resources arg0, XmlPullParser arg1, Resources.Theme arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->createFromXml(Landroid/content/res/Resources;Lorg/xmlpull/v1/XmlPullParser;Landroid/content/res/Resources$Theme;)Landroid/graphics/drawable/Drawable;");
    }

    public static Drawable createFromXmlInner(Resources arg0, XmlPullParser arg1, AttributeSet arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->createFromXmlInner(Landroid/content/res/Resources;Lorg/xmlpull/v1/XmlPullParser;Landroid/util/AttributeSet;)Landroid/graphics/drawable/Drawable;");
    }

    public static Drawable createFromXmlInner(Resources arg0, XmlPullParser arg1, AttributeSet arg2, Resources.Theme arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->createFromXmlInner(Landroid/content/res/Resources;Lorg/xmlpull/v1/XmlPullParser;Landroid/util/AttributeSet;Landroid/content/res/Resources$Theme;)Landroid/graphics/drawable/Drawable;");
    }

    public void draw(Canvas arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->draw(Landroid/graphics/Canvas;)V");
    }

    public int getAlpha() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->getAlpha()I");
    }

    public final Rect getBounds() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->getBounds()Landroid/graphics/Rect;");
    }

    public Drawable.Callback getCallback() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->getCallback()Landroid/graphics/drawable/Drawable$Callback;");
    }

    public int getChangingConfigurations() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->getChangingConfigurations()I");
    }

    public ColorFilter getColorFilter() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->getColorFilter()Landroid/graphics/ColorFilter;");
    }

    public Drawable.ConstantState getConstantState() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->getConstantState()Landroid/graphics/drawable/Drawable$ConstantState;");
    }

    public Drawable getCurrent() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->getCurrent()Landroid/graphics/drawable/Drawable;");
    }

    public Rect getDirtyBounds() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->getDirtyBounds()Landroid/graphics/Rect;");
    }

    public void getHotspotBounds(Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->getHotspotBounds(Landroid/graphics/Rect;)V");
    }

    public int getIntrinsicHeight() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->getIntrinsicHeight()I");
    }

    public int getIntrinsicWidth() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->getIntrinsicWidth()I");
    }

    public int getLayoutDirection() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->getLayoutDirection()I");
    }

    public final int getLevel() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->getLevel()I");
    }

    public int getMinimumHeight() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->getMinimumHeight()I");
    }

    public int getMinimumWidth() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->getMinimumWidth()I");
    }

    public int getOpacity() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->getOpacity()I");
    }

    public Insets getOpticalInsets() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->getOpticalInsets()Landroid/graphics/Insets;");
    }

    public void getOutline(Outline arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->getOutline(Landroid/graphics/Outline;)V");
    }

    public boolean getPadding(Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->getPadding(Landroid/graphics/Rect;)Z");
    }

    public int[] getState() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->getState()[I");
    }

    public Region getTransparentRegion() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->getTransparentRegion()Landroid/graphics/Region;");
    }

    public boolean hasFocusStateSpecified() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->hasFocusStateSpecified()Z");
    }

    public void inflate(Resources arg0, XmlPullParser arg1, AttributeSet arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->inflate(Landroid/content/res/Resources;Lorg/xmlpull/v1/XmlPullParser;Landroid/util/AttributeSet;)V");
    }

    public void inflate(Resources arg0, XmlPullParser arg1, AttributeSet arg2, Resources.Theme arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->inflate(Landroid/content/res/Resources;Lorg/xmlpull/v1/XmlPullParser;Landroid/util/AttributeSet;Landroid/content/res/Resources$Theme;)V");
    }

    public void invalidateSelf() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->invalidateSelf()V");
    }

    public boolean isAutoMirrored() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->isAutoMirrored()Z");
    }

    public boolean isFilterBitmap() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->isFilterBitmap()Z");
    }

    public boolean isProjected() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->isProjected()Z");
    }

    public boolean isStateful() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->isStateful()Z");
    }

    public final boolean isVisible() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->isVisible()Z");
    }

    public void jumpToCurrentState() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->jumpToCurrentState()V");
    }

    public Drawable mutate() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->mutate()Landroid/graphics/drawable/Drawable;");
    }

    protected void onBoundsChange(Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->onBoundsChange(Landroid/graphics/Rect;)V");
    }

    public boolean onLayoutDirectionChanged(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->onLayoutDirectionChanged(I)Z");
    }

    protected boolean onLevelChange(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->onLevelChange(I)Z");
    }

    protected boolean onStateChange(int[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->onStateChange([I)Z");
    }

    public static int resolveOpacity(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->resolveOpacity(II)I");
    }

    public void scheduleSelf(Runnable arg0, long arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->scheduleSelf(Ljava/lang/Runnable;J)V");
    }

    public void setAlpha(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->setAlpha(I)V");
    }

    public void setAutoMirrored(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->setAutoMirrored(Z)V");
    }

    public void setBounds(Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->setBounds(Landroid/graphics/Rect;)V");
    }

    public void setBounds(int arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->setBounds(IIII)V");
    }

    public final void setCallback(Drawable.Callback arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->setCallback(Landroid/graphics/drawable/Drawable$Callback;)V");
    }

    public void setChangingConfigurations(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->setChangingConfigurations(I)V");
    }

    public void setColorFilter(ColorFilter arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->setColorFilter(Landroid/graphics/ColorFilter;)V");
    }

    public void setColorFilter(int arg0, PorterDuff.Mode arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->setColorFilter(ILandroid/graphics/PorterDuff$Mode;)V");
    }

    public void setDither(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->setDither(Z)V");
    }

    public void setFilterBitmap(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->setFilterBitmap(Z)V");
    }

    public void setHotspot(float arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->setHotspot(FF)V");
    }

    public void setHotspotBounds(int arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->setHotspotBounds(IIII)V");
    }

    public final boolean setLayoutDirection(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->setLayoutDirection(I)Z");
    }

    public final boolean setLevel(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->setLevel(I)Z");
    }

    public boolean setState(int[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->setState([I)Z");
    }

    public void setTint(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->setTint(I)V");
    }

    public void setTintBlendMode(BlendMode arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->setTintBlendMode(Landroid/graphics/BlendMode;)V");
    }

    public void setTintList(ColorStateList arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->setTintList(Landroid/content/res/ColorStateList;)V");
    }

    public void setTintMode(PorterDuff.Mode arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->setTintMode(Landroid/graphics/PorterDuff$Mode;)V");
    }

    public boolean setVisible(boolean arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->setVisible(ZZ)Z");
    }

    public void unscheduleSelf(Runnable arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable;->unscheduleSelf(Ljava/lang/Runnable;)V");
    }
        public static interface Callback {

        public abstract void invalidateDrawable(Drawable arg0);

        public abstract void scheduleDrawable(Drawable arg0, Runnable arg1, long arg2);

        public abstract void unscheduleDrawable(Drawable arg0, Runnable arg1);
        }
        public static class ConstantState {

        public ConstantState() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable$ConstantState;-><init>()V");
        }

        public boolean canApplyTheme() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable$ConstantState;->canApplyTheme()Z");
        }

        public int getChangingConfigurations() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable$ConstantState;->getChangingConfigurations()I");
        }

        public Drawable newDrawable() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable$ConstantState;->newDrawable()Landroid/graphics/drawable/Drawable;");
        }

        public Drawable newDrawable(Resources arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable$ConstantState;->newDrawable(Landroid/content/res/Resources;)Landroid/graphics/drawable/Drawable;");
        }

        public Drawable newDrawable(Resources arg0, Resources.Theme arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Drawable$ConstantState;->newDrawable(Landroid/content/res/Resources;Landroid/content/res/Resources$Theme;)Landroid/graphics/drawable/Drawable;");
        }
        }
    }
