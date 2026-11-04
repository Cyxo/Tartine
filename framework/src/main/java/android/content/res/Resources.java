package android.content.res;

import android.content.pm.ApplicationInfo;
import android.content.res.loader.ResourcesLoader;
import android.graphics.Movie;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import java.io.InputStream;
import tartine.fw.DisplayConfig;
import tartine.fw.SystemColors;
import tartine.fw.ThemeMode;

    public class Resources {

    public static final int ID_NULL = 0;

    public Resources(AssetManager arg0, DisplayMetrics arg1, Configuration arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;-><init>(Landroid/content/res/AssetManager;Landroid/util/DisplayMetrics;Landroid/content/res/Configuration;)V");
    }

    public void addLoaders(ResourcesLoader[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->addLoaders([Landroid/content/res/loader/ResourcesLoader;)V");
    }

    public final void finishPreloading() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->finishPreloading()V");
    }

    public final void flushLayoutCache() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->flushLayoutCache()V");
    }

    public XmlResourceParser getAnimation(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getAnimation(I)Landroid/content/res/XmlResourceParser;");
    }

    public final AssetManager getAssets() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getAssets()Landroid/content/res/AssetManager;");
    }

    public static int getAttributeSetSourceResId(AttributeSet arg0) {
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
    public int getColor(int arg0, Resources.Theme arg1) {
        return getColor(arg0);
    }

    public ColorStateList getColorStateList(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getColorStateList(I)Landroid/content/res/ColorStateList;");
    }

    public ColorStateList getColorStateList(int arg0, Resources.Theme arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getColorStateList(ILandroid/content/res/Resources$Theme;)Landroid/content/res/ColorStateList;");
    }

    /**
     * The device answers its AssetManager-scoped configuration (the
     * ResourcesKey's shared mMetrics.config); the shell owns one
     * Configuration per Resources.
     *
     * <p>This must be the *live* device configuration, not AOSP's "completely
     * undefined" {@code new Configuration()} (whose ctor runs unset() and
     * leaves {@code fontScale = 0}). Compose derives its {@code Density} from
     * exactly these two fields (androidx.compose.ui.unit.Density(context):
     * {@code displayMetrics.density} and {@code configuration.fontScale}, see
     * AndroidDensity.android.kt), and an {@code sp} value converts to
     * {@code value * fontScale * density} px. With fontScale left at 0 every
     * guest text size collapsed to 0 px, which the draw path then papered
     * over with a hardcoded 14 px fallback.
     */
    private final Configuration mConfiguration =
            probeConfiguration();

    /**
     * Desktop device configuration built from the host window metrics.
     * Mirrors AOSP Configuration.setToDefaults() (fontScale = 1) and fills
     * the display values a real device reports: the framebuffer is the
     * pixel viewport, and the host content scale becomes the density, so dp
     * sizes stay stable across HiDPI windows. The screen-layout size/long
     * bits come from AOSP's {@code reduceScreenLayout} thresholds, and the
     * night bit follows the host light/dark preference resolved at startup
     * (see {@link ThemeMode}).
     */
    private static Configuration probeConfiguration() {
        Configuration config =
                new Configuration();
        config.setToDefaults();
        config.densityDpi = DisplayConfig.densityDpi();
        config.screenWidthDp = DisplayConfig.screenWidthDp();
        config.screenHeightDp = DisplayConfig.screenHeightDp();
        config.smallestScreenWidthDp =
                DisplayConfig.smallestScreenWidthDp();
        config.orientation = config.screenWidthDp > config.screenHeightDp
                ? Configuration.ORIENTATION_LANDSCAPE
                : Configuration.ORIENTATION_PORTRAIT;
        // The window's dp size is the display's dp size here, so the
        // device's screen layout follows from it. Start from a fully
        // claimed layout (LTR + long + xlarge) and let the AOSP reduction
        // lower the size/long bits; that leaves both bits defined instead
        // of the undefined value setToDefaults() installed.
        config.screenLayout = Configuration.reduceScreenLayout(
                Configuration.SCREENLAYOUT_LAYOUTDIR_LTR
                        | Configuration.SCREENLAYOUT_LONG_YES
                        | Configuration.SCREENLAYOUT_SIZE_XLARGE,
                Math.max(config.screenWidthDp, config.screenHeightDp),
                Math.min(config.screenWidthDp, config.screenHeightDp));
        config.touchscreen =
                Configuration.TOUCHSCREEN_FINGER;
        config.keyboard = Configuration.KEYBOARD_NOKEYS;
        config.keyboardHidden =
                Configuration.KEYBOARDHIDDEN_YES;
        config.hardKeyboardHidden =
                Configuration.HARDKEYBOARDHIDDEN_YES;
        config.navigation = Configuration.NAVIGATION_NONAV;
        config.navigationHidden =
                Configuration.NAVIGATIONHIDDEN_YES;
        config.uiMode = Configuration.UI_MODE_TYPE_NORMAL
                | (ThemeMode.isDark()
                        ? Configuration.UI_MODE_NIGHT_YES
                        : Configuration.UI_MODE_NIGHT_NO);
        return config;
    }

    /**
     * AOSP Resources.getDisplayMetrics (Resources.java:2238-2240) returns the
     * ResourcesImpl metrics; the desktop shell has no ResourcesImpl, so the
     * host window's metrics are owned here.
     */
    private final DisplayMetrics mMetrics =
            probeDisplayMetrics(mConfiguration.fontScale);

    /** AOSP DisplayMetrics field contract: {@code scaledDensity} is the
     *  density multiplied by the configuration font scale (the legacy
     *  text-size path; Compose reads density + fontScale directly). */
    private static DisplayMetrics probeDisplayMetrics(
            float fontScale) {
        DisplayMetrics metrics = new DisplayMetrics();
        metrics.widthPixels = DisplayConfig.widthPixels();
        metrics.heightPixels = DisplayConfig.heightPixels();
        metrics.density = DisplayConfig.density();
        metrics.densityDpi = DisplayConfig.densityDpi();
        metrics.scaledDensity = metrics.density * fontScale;
        metrics.xdpi = metrics.densityDpi;
        metrics.ydpi = metrics.densityDpi;
        return metrics;
    }

    public Configuration getConfiguration() {
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

    public DisplayMetrics getDisplayMetrics() {
        return mMetrics;
    }

    public Drawable getDrawable(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getDrawable(I)Landroid/graphics/drawable/Drawable;");
    }

    public Drawable getDrawable(int arg0, Resources.Theme arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getDrawable(ILandroid/content/res/Resources$Theme;)Landroid/graphics/drawable/Drawable;");
    }

    public Drawable getDrawableForDensity(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getDrawableForDensity(II)Landroid/graphics/drawable/Drawable;");
    }

    public Drawable getDrawableForDensity(int arg0, int arg1, Resources.Theme arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getDrawableForDensity(IILandroid/content/res/Resources$Theme;)Landroid/graphics/drawable/Drawable;");
    }

    public float getFloat(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getFloat(I)F");
    }

    public Typeface getFont(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getFont(I)Landroid/graphics/Typeface;");
    }

    public float getFraction(int arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getFraction(III)F");
    }

    public int getIdentifier(String arg0, String arg1, String arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getIdentifier(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)I");
    }

    public int[] getIntArray(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getIntArray(I)[I");
    }

    public int getInteger(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getInteger(I)I");
    }

    public XmlResourceParser getLayout(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getLayout(I)Landroid/content/res/XmlResourceParser;");
    }

    public Movie getMovie(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getMovie(I)Landroid/graphics/Movie;");
    }

    public String getQuantityString(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getQuantityString(II)Ljava/lang/String;");
    }

    public String getQuantityString(int arg0, int arg1, Object[] arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getQuantityString(II[Ljava/lang/Object;)Ljava/lang/String;");
    }

    public CharSequence getQuantityText(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getQuantityText(II)Ljava/lang/CharSequence;");
    }

    public String getResourceEntryName(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getResourceEntryName(I)Ljava/lang/String;");
    }

    public String getResourceName(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getResourceName(I)Ljava/lang/String;");
    }

    public String getResourcePackageName(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getResourcePackageName(I)Ljava/lang/String;");
    }

    public String getResourceTypeName(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getResourceTypeName(I)Ljava/lang/String;");
    }

    public String getString(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getString(I)Ljava/lang/String;");
    }

    public String getString(int arg0, Object[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getString(I[Ljava/lang/Object;)Ljava/lang/String;");
    }

    public String[] getStringArray(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getStringArray(I)[Ljava/lang/String;");
    }

    public static Resources getSystem() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getSystem()Landroid/content/res/Resources;");
    }

    public CharSequence getText(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getText(I)Ljava/lang/CharSequence;");
    }

    public CharSequence getText(int arg0, CharSequence arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getText(ILjava/lang/CharSequence;)Ljava/lang/CharSequence;");
    }

    public CharSequence[] getTextArray(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getTextArray(I)[Ljava/lang/CharSequence;");
    }

    public void getValue(int arg0, TypedValue arg1, boolean arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getValue(ILandroid/util/TypedValue;Z)V");
    }

    public void getValue(String arg0, TypedValue arg1, boolean arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getValue(Ljava/lang/String;Landroid/util/TypedValue;Z)V");
    }

    public void getValueForDensity(int arg0, int arg1, TypedValue arg2, boolean arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getValueForDensity(IILandroid/util/TypedValue;Z)V");
    }

    public XmlResourceParser getXml(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->getXml(I)Landroid/content/res/XmlResourceParser;");
    }

    public final Resources.Theme newTheme() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->newTheme()Landroid/content/res/Resources$Theme;");
    }

    public TypedArray obtainAttributes(AttributeSet arg0, int[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->obtainAttributes(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;");
    }

    public TypedArray obtainTypedArray(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->obtainTypedArray(I)Landroid/content/res/TypedArray;");
    }

    public InputStream openRawResource(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->openRawResource(I)Ljava/io/InputStream;");
    }

    public InputStream openRawResource(int arg0, TypedValue arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->openRawResource(ILandroid/util/TypedValue;)Ljava/io/InputStream;");
    }

    public AssetFileDescriptor openRawResourceFd(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->openRawResourceFd(I)Landroid/content/res/AssetFileDescriptor;");
    }

    public void parseBundleExtra(String arg0, AttributeSet arg1, Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->parseBundleExtra(Ljava/lang/String;Landroid/util/AttributeSet;Landroid/os/Bundle;)V");
    }

    public void parseBundleExtras(XmlResourceParser arg0, Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->parseBundleExtras(Landroid/content/res/XmlResourceParser;Landroid/os/Bundle;)V");
    }

    public static void registerResourcePaths(String arg0, ApplicationInfo arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->registerResourcePaths(Ljava/lang/String;Landroid/content/pm/ApplicationInfo;)V");
    }

    public void removeLoaders(ResourcesLoader[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->removeLoaders([Landroid/content/res/loader/ResourcesLoader;)V");
    }

    public void updateConfiguration(Configuration arg0, DisplayMetrics arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources;->updateConfiguration(Landroid/content/res/Configuration;Landroid/util/DisplayMetrics;)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public Resources() {
        }
        public static class Theme {

        public void applyStyle(int arg0, boolean arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->applyStyle(IZ)V");
        }

        public void dump(int arg0, String arg1, String arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->dump(ILjava/lang/String;Ljava/lang/String;)V");
        }

        public boolean equals(Object arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->equals(Ljava/lang/Object;)Z");
        }

        public int[] getAttributeResolutionStack(int arg0, int arg1, int arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->getAttributeResolutionStack(III)[I");
        }

        public int getChangingConfigurations() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->getChangingConfigurations()I");
        }

        public Drawable getDrawable(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->getDrawable(I)Landroid/graphics/drawable/Drawable;");
        }

        public int getExplicitStyle(AttributeSet arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->getExplicitStyle(Landroid/util/AttributeSet;)I");
        }

        public Resources getResources() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->getResources()Landroid/content/res/Resources;");
        }

        public int hashCode() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->hashCode()I");
        }

        public TypedArray obtainStyledAttributes(AttributeSet arg0, int[] arg1, int arg2, int arg3) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->obtainStyledAttributes(Landroid/util/AttributeSet;[III)Landroid/content/res/TypedArray;");
        }

        public TypedArray obtainStyledAttributes(int arg0, int[] arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->obtainStyledAttributes(I[I)Landroid/content/res/TypedArray;");
        }

        public TypedArray obtainStyledAttributes(int[] arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->obtainStyledAttributes([I)Landroid/content/res/TypedArray;");
        }

        public void rebase() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->rebase()V");
        }

        public boolean resolveAttribute(int arg0, TypedValue arg1, boolean arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->resolveAttribute(ILandroid/util/TypedValue;Z)Z");
        }

        public void setTo(Resources.Theme arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->setTo(Landroid/content/res/Resources$Theme;)V");
        }

        public String toString() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Resources$Theme;->toString()Ljava/lang/String;");
        }

            /** Scratch mirror constructor (SkeletonGen). */
            public Theme() {
            }
        }
    }
