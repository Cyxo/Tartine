package android.content.res;

import tartine.fw.SystemColors;

    public class Resources {

    public static final int ID_NULL = 0;

    public Resources(android.content.res.AssetManager arg0, android.util.DisplayMetrics arg1, android.content.res.Configuration arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;-><init>(Landroid/content/res/AssetManager;Landroid/util/DisplayMetrics;Landroid/content/res/Configuration;)V");
    }

    public void addLoaders(android.content.res.loader.ResourcesLoader[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->addLoaders([Landroid/content/res/loader/ResourcesLoader;)V");
    }

    public final void finishPreloading() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->finishPreloading()V");
    }

    public final void flushLayoutCache() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->flushLayoutCache()V");
    }

    public android.content.res.XmlResourceParser getAnimation(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getAnimation(I)Landroid/content/res/XmlResourceParser;");
    }

    public final android.content.res.AssetManager getAssets() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getAssets()Landroid/content/res/AssetManager;");
    }

    public static int getAttributeSetSourceResId(android.util.AttributeSet arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getAttributeSetSourceResId(Landroid/util/AttributeSet;)I");
    }

    public boolean getBoolean(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getBoolean(I)Z");
    }

    /**
     * Ported behavior. AOSP: {@code Resources.getColor(int)} returns
     * {@code getColorStateList(id).getDefaultColor()} (frameworks/base
     * core/java/android/content/res/Resources.java). Only android-package
     * system color ids carry a baseline here (see {@link SystemColors});
     * app-package resources would need the guest {@code resources.arsc},
     * still an explicit gap.
     */
    public int getColor(int arg0) {
        Integer value = SystemColors.get(arg0);
        if (value == null) {
            throw new UnsupportedOperationException(
                    "Tartine: not implemented: Landroid/content/res/Resources;->getColor(I)I"
                            + " (id=" + Integer.toHexString(arg0) + ")");
        }
        return value;
    }

    /**
     * Ported behavior. AOSP: {@code Resources.getColor(int, Theme)} resolves
     * through theme-based getValue; the system palette entries below are not
     * theme-resolved (no RRO/theme overlay runs here), so both overloads read
     * the same baseline table. App-package resources remain a gap.
     */
    public int getColor(int arg0, android.content.res.Resources.Theme arg1) {
        return getColor(arg0);
    }

    public android.content.res.ColorStateList getColorStateList(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getColorStateList(I)Landroid/content/res/ColorStateList;");
    }

    public android.content.res.ColorStateList getColorStateList(int arg0, android.content.res.Resources.Theme arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getColorStateList(ILandroid/content/res/Resources$Theme;)Landroid/content/res/ColorStateList;");
    }

    /**
     * The device answers its AssetManager-scoped configuration (the
     * ResourcesKey's shared mMetrics.config); the shell owns one
     * Configuration per Resources with zero-default fields.
     */
    private final android.content.res.Configuration mConfiguration =
            new android.content.res.Configuration();

    /**
     * AOSP Resources.getDisplayMetrics (Resources.java:2238-2240) returns the
     * ResourcesImpl metrics; the desktop shell has no ResourcesImpl, so the
     * probe display's metrics are owned here. Values mirror the interpreter's
     * display builtins (480x960 at density 1.0, 160 dpi).
     */
    private final android.util.DisplayMetrics mMetrics = probeDisplayMetrics();

    private static android.util.DisplayMetrics probeDisplayMetrics() {
        android.util.DisplayMetrics metrics = new android.util.DisplayMetrics();
        metrics.widthPixels = 480;
        metrics.heightPixels = 960;
        metrics.density = 1.0f;
        metrics.densityDpi = 160;
        metrics.scaledDensity = 1.0f;
        metrics.xdpi = 160.0f;
        metrics.ydpi = 160.0f;
        return metrics;
    }

    public android.content.res.Configuration getConfiguration() {
        return mConfiguration;
    }

    public float getDimension(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getDimension(I)F");
    }

    public int getDimensionPixelOffset(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getDimensionPixelOffset(I)I");
    }

    public int getDimensionPixelSize(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getDimensionPixelSize(I)I");
    }

    public android.util.DisplayMetrics getDisplayMetrics() {
        return mMetrics;
    }

    public android.graphics.drawable.Drawable getDrawable(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getDrawable(I)Landroid/graphics/drawable/Drawable;");
    }

    public android.graphics.drawable.Drawable getDrawable(int arg0, android.content.res.Resources.Theme arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getDrawable(ILandroid/content/res/Resources$Theme;)Landroid/graphics/drawable/Drawable;");
    }

    public android.graphics.drawable.Drawable getDrawableForDensity(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getDrawableForDensity(II)Landroid/graphics/drawable/Drawable;");
    }

    public android.graphics.drawable.Drawable getDrawableForDensity(int arg0, int arg1, android.content.res.Resources.Theme arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getDrawableForDensity(IILandroid/content/res/Resources$Theme;)Landroid/graphics/drawable/Drawable;");
    }

    public float getFloat(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getFloat(I)F");
    }

    public android.graphics.Typeface getFont(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getFont(I)Landroid/graphics/Typeface;");
    }

    public float getFraction(int arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getFraction(III)F");
    }

    public int getIdentifier(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getIdentifier(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)I");
    }

    public int[] getIntArray(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getIntArray(I)[I");
    }

    public int getInteger(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getInteger(I)I");
    }

    public android.content.res.XmlResourceParser getLayout(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getLayout(I)Landroid/content/res/XmlResourceParser;");
    }

    public android.graphics.Movie getMovie(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getMovie(I)Landroid/graphics/Movie;");
    }

    public java.lang.String getQuantityString(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getQuantityString(II)Ljava/lang/String;");
    }

    public java.lang.String getQuantityString(int arg0, int arg1, java.lang.Object[] arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getQuantityString(II[Ljava/lang/Object;)Ljava/lang/String;");
    }

    public java.lang.CharSequence getQuantityText(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getQuantityText(II)Ljava/lang/CharSequence;");
    }

    public java.lang.String getResourceEntryName(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getResourceEntryName(I)Ljava/lang/String;");
    }

    public java.lang.String getResourceName(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getResourceName(I)Ljava/lang/String;");
    }

    public java.lang.String getResourcePackageName(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getResourcePackageName(I)Ljava/lang/String;");
    }

    public java.lang.String getResourceTypeName(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getResourceTypeName(I)Ljava/lang/String;");
    }

    public java.lang.String getString(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getString(I)Ljava/lang/String;");
    }

    public java.lang.String getString(int arg0, java.lang.Object[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getString(I[Ljava/lang/Object;)Ljava/lang/String;");
    }

    public java.lang.String[] getStringArray(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getStringArray(I)[Ljava/lang/String;");
    }

    public static android.content.res.Resources getSystem() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getSystem()Landroid/content/res/Resources;");
    }

    public java.lang.CharSequence getText(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getText(I)Ljava/lang/CharSequence;");
    }

    public java.lang.CharSequence getText(int arg0, java.lang.CharSequence arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getText(ILjava/lang/CharSequence;)Ljava/lang/CharSequence;");
    }

    public java.lang.CharSequence[] getTextArray(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getTextArray(I)[Ljava/lang/CharSequence;");
    }

    public void getValue(int arg0, android.util.TypedValue arg1, boolean arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getValue(ILandroid/util/TypedValue;Z)V");
    }

    public void getValue(java.lang.String arg0, android.util.TypedValue arg1, boolean arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getValue(Ljava/lang/String;Landroid/util/TypedValue;Z)V");
    }

    public void getValueForDensity(int arg0, int arg1, android.util.TypedValue arg2, boolean arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getValueForDensity(IILandroid/util/TypedValue;Z)V");
    }

    public android.content.res.XmlResourceParser getXml(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getXml(I)Landroid/content/res/XmlResourceParser;");
    }

    public final android.content.res.Resources.Theme newTheme() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->newTheme()Landroid/content/res/Resources$Theme;");
    }

    public android.content.res.TypedArray obtainAttributes(android.util.AttributeSet arg0, int[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->obtainAttributes(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;");
    }

    public android.content.res.TypedArray obtainTypedArray(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->obtainTypedArray(I)Landroid/content/res/TypedArray;");
    }

    public java.io.InputStream openRawResource(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->openRawResource(I)Ljava/io/InputStream;");
    }

    public java.io.InputStream openRawResource(int arg0, android.util.TypedValue arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->openRawResource(ILandroid/util/TypedValue;)Ljava/io/InputStream;");
    }

    public android.content.res.AssetFileDescriptor openRawResourceFd(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->openRawResourceFd(I)Landroid/content/res/AssetFileDescriptor;");
    }

    public void parseBundleExtra(java.lang.String arg0, android.util.AttributeSet arg1, android.os.Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->parseBundleExtra(Ljava/lang/String;Landroid/util/AttributeSet;Landroid/os/Bundle;)V");
    }

    public void parseBundleExtras(android.content.res.XmlResourceParser arg0, android.os.Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->parseBundleExtras(Landroid/content/res/XmlResourceParser;Landroid/os/Bundle;)V");
    }

    public static void registerResourcePaths(java.lang.String arg0, android.content.pm.ApplicationInfo arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->registerResourcePaths(Ljava/lang/String;Landroid/content/pm/ApplicationInfo;)V");
    }

    public void removeLoaders(android.content.res.loader.ResourcesLoader[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->removeLoaders([Landroid/content/res/loader/ResourcesLoader;)V");
    }

    public void updateConfiguration(android.content.res.Configuration arg0, android.util.DisplayMetrics arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->updateConfiguration(Landroid/content/res/Configuration;Landroid/util/DisplayMetrics;)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public Resources() {
        }
        public static class Theme {

        public void applyStyle(int arg0, boolean arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->applyStyle(IZ)V");
        }

        public void dump(int arg0, java.lang.String arg1, java.lang.String arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->dump(ILjava/lang/String;Ljava/lang/String;)V");
        }

        public boolean equals(java.lang.Object arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->equals(Ljava/lang/Object;)Z");
        }

        public int[] getAttributeResolutionStack(int arg0, int arg1, int arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->getAttributeResolutionStack(III)[I");
        }

        public int getChangingConfigurations() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->getChangingConfigurations()I");
        }

        public android.graphics.drawable.Drawable getDrawable(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->getDrawable(I)Landroid/graphics/drawable/Drawable;");
        }

        public int getExplicitStyle(android.util.AttributeSet arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->getExplicitStyle(Landroid/util/AttributeSet;)I");
        }

        public android.content.res.Resources getResources() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->getResources()Landroid/content/res/Resources;");
        }

        public int hashCode() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->hashCode()I");
        }

        public android.content.res.TypedArray obtainStyledAttributes(android.util.AttributeSet arg0, int[] arg1, int arg2, int arg3) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->obtainStyledAttributes(Landroid/util/AttributeSet;[III)Landroid/content/res/TypedArray;");
        }

        public android.content.res.TypedArray obtainStyledAttributes(int arg0, int[] arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->obtainStyledAttributes(I[I)Landroid/content/res/TypedArray;");
        }

        public android.content.res.TypedArray obtainStyledAttributes(int[] arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->obtainStyledAttributes([I)Landroid/content/res/TypedArray;");
        }

        public void rebase() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->rebase()V");
        }

        public boolean resolveAttribute(int arg0, android.util.TypedValue arg1, boolean arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->resolveAttribute(ILandroid/util/TypedValue;Z)Z");
        }

        public void setTo(android.content.res.Resources.Theme arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->setTo(Landroid/content/res/Resources$Theme;)V");
        }

        public java.lang.String toString() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->toString()Ljava/lang/String;");
        }

            /** Scratch mirror constructor (SkeletonGen). */
            public Theme() {
            }
        }
    }
