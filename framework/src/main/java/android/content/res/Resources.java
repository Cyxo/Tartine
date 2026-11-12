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
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import tartine.fw.AppResourceHost;
import tartine.fw.AppResources;
import tartine.fw.DisplayConfig;
import tartine.fw.ResValue;
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
        return getXml(arg0);
    }

    /**
     * The shell answers its own {@link AssetManager}, reading APK assets
     * through the installed {@link AppResourceHost} (a real device routes this
     * through its native asset manager).
     */
    public final AssetManager getAssets() {
        return mAssets;
    }

    private final AssetManager mAssets = new AssetManager();

    public static int getAttributeSetSourceResId(AttributeSet arg0) {
        return 0;
    }

    public boolean getBoolean(int arg0) {
        ResValue value = hostValue(arg0);
        if (value != null && value.type == TypedValue.TYPE_INT_BOOLEAN) {
            return value.data != 0;
        }
        throw gap("getBoolean(I)Z", arg0);
    }

    /**
     * Ported behavior. AOSP: {@code Resources.getColor(int)} returns
     * {@code getColorStateList(id).getDefaultColor()}
     * (frameworks/base core/java/android/content/res/Resources.java). The
     * installed app's table answers first; the generated android-package
     * baseline ({@link SystemColors}) backs system color ids.
     */
    public int getColor(int arg0) {
        ResValue value = hostValue(arg0);
        if (value != null) {
            if (value.type >= TypedValue.TYPE_FIRST_COLOR_INT
                    && value.type <= TypedValue.TYPE_LAST_COLOR_INT) {
                return value.data;
            }
            if (value.type == TypedValue.TYPE_STRING && value.string != null) {
                return parseColor(value.string);
            }
            return value.data;
        }
        Integer fallback = SystemColors.get(arg0);
        if (fallback == null) {
            throw gap("getColor(I)I", arg0);
        }
        return fallback;
    }

    /**
     * AOSP: {@code Resources.getColor(int, Theme)} resolves through theme-based
     * getValue; the installed table already resolves references, so both
     * overloads read the same value.
     */
    public int getColor(int arg0, Resources.Theme arg1) {
        return getColor(arg0);
    }

    public ColorStateList getColorStateList(int arg0) {
        return ColorStateList.valueOf(getColor(arg0));
    }

    public ColorStateList getColorStateList(int arg0, Resources.Theme arg1) {
        return getColorStateList(arg0);
    }

    /** Parses {@code #rgb}/{@code #argb}/{@code #rrggbb}/{@code #aarrggbb}. */
    private static int parseColor(String text) {
        if (text == null || text.isEmpty() || text.charAt(0) != '#') {
            return 0;
        }
        try {
            String digits = text.substring(1);
            if (digits.length() == 3 || digits.length() == 4) {
                StringBuilder expanded = new StringBuilder();
                for (int i = 0; i < digits.length(); i++) {
                    expanded.append(digits.charAt(i)).append(digits.charAt(i));
                }
                digits = expanded.toString();
            }
            if (digits.length() == 6) {
                digits = "ff" + digits;
            }
            return (int) Long.parseLong(digits, 16);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * AOSP Resources.getDisplayMetrics (Resources.java:2238-2240) returns the
     * ResourcesImpl metrics; the desktop shell has no ResourcesImpl, so the
     * host window's metrics are owned here.
     */
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
        ResValue value = requireValue("getDimension(I)F", arg0);
        return TypedValue.complexToDimension(value.data, mMetrics);
    }

    public int getDimensionPixelOffset(int arg0) {
        ResValue value = requireValue("getDimensionPixelOffset(I)I", arg0);
        return TypedValue.complexToDimensionPixelOffset(value.data, mMetrics);
    }

    public int getDimensionPixelSize(int arg0) {
        ResValue value = requireValue("getDimensionPixelSize(I)I", arg0);
        return TypedValue.complexToDimensionPixelSize(value.data, mMetrics);
    }

    public DisplayMetrics getDisplayMetrics() {
        return mMetrics;
    }

    public Drawable getDrawable(int arg0) {
        throw gap("getDrawable(I)Landroid/graphics/drawable/Drawable;", arg0);
    }

    public Drawable getDrawable(int arg0, Resources.Theme arg1) {
        return getDrawable(arg0);
    }

    public Drawable getDrawableForDensity(int arg0, int arg1) {
        return getDrawable(arg0);
    }

    public Drawable getDrawableForDensity(int arg0, int arg1, Resources.Theme arg2) {
        return getDrawable(arg0);
    }

    public float getFloat(int arg0) {
        ResValue value = requireValue("getFloat(I)F", arg0);
        return Float.intBitsToFloat(value.data);
    }

    public Typeface getFont(int arg0) {
        throw gap("getFont(I)Landroid/graphics/Typeface;", arg0);
    }

    public float getFraction(int arg0, int arg1, int arg2) {
        ResValue value = requireValue("getFraction(III)F", arg0);
        return TypedValue.complexToFraction(value.data, arg1, arg2);
    }

    public int getIdentifier(String arg0, String arg1, String arg2) {
        AppResourceHost host = AppResources.host();
        return host == null ? 0 : host.identifier(arg0, arg1, arg2);
    }

    public int[] getIntArray(int arg0) {
        List<ResValue> values = hostArray(arg0);
        if (values == null) {
            throw gap("getIntArray(I)[I", arg0);
        }
        int[] result = new int[values.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = values.get(i).data;
        }
        return result;
    }

    public int getInteger(int arg0) {
        return (int) getFloat(arg0);
    }

    public XmlResourceParser getLayout(int arg0) {
        return getXml(arg0);
    }

    public Movie getMovie(int arg0) {
        throw gap("getMovie(I)Landroid/graphics/Movie;", arg0);
    }

    public String getQuantityString(int arg0, int arg1) {
        return getString(arg0);
    }

    public String getQuantityString(int arg0, int arg1, Object[] arg2) {
        return getString(arg0, arg2);
    }

    public CharSequence getQuantityText(int arg0, int arg1) {
        return getText(arg0);
    }

    public String getResourceEntryName(int arg0) {
        return resourceNamePart(arg0, 2);
    }

    public String getResourceName(int arg0) {
        String pkg = resourceNamePart(arg0, 0);
        String type = resourceNamePart(arg0, 1);
        String entry = resourceNamePart(arg0, 2);
        if (pkg == null || type == null || entry == null) {
            throw gap("getResourceName(I)Ljava/lang/String;", arg0);
        }
        return pkg + ":" + type + "/" + entry;
    }

    public String getResourcePackageName(int arg0) {
        return resourceNamePart(arg0, 0);
    }

    public String getResourceTypeName(int arg0) {
        return resourceNamePart(arg0, 1);
    }

    private String resourceNamePart(int arg0, int part) {
        AppResourceHost host = AppResources.host();
        if (host == null) {
            throw gap("getResourceName(I)Ljava/lang/String;", arg0);
        }
        return host.resourceName(arg0, part);
    }

    public String getString(int arg0) {
        ResValue value = requireValue("getString(I)Ljava/lang/String;", arg0);
        if (value.string != null) {
            return value.string;
        }
        String text = TypedValue.coerceToString(value.type, value.data);
        return text == null ? "" : text;
    }

    public String getString(int arg0, Object[] arg1) {
        return String.format(getString(arg0), arg1);
    }

    public String[] getStringArray(int arg0) {
        List<ResValue> values = hostArray(arg0);
        if (values == null) {
            throw gap("getStringArray(I)[Ljava/lang/String;", arg0);
        }
        String[] result = new String[values.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = values.get(i).string;
        }
        return result;
    }

    public static Resources getSystem() {
        throw new UnsupportedOperationException(
                "Tartine: not implemented: Resources.getSystem");
    }

    public CharSequence getText(int arg0) {
        return getString(arg0);
    }

    public CharSequence getText(int arg0, CharSequence arg1) {
        ResValue value = hostValue(arg0);
        if (value == null) {
            return arg1;
        }
        return value.string != null ? value.string
                : TypedValue.coerceToString(value.type, value.data);
    }

    public CharSequence[] getTextArray(int arg0) {
        return getStringArray(arg0);
    }

    public void getValue(int arg0, TypedValue arg1, boolean arg2) {
        ResValue value = requireValue("getValue(ILandroid/util/TypedValue;Z)V", arg0);
        arg1.type = value.type;
        arg1.data = value.data;
        arg1.string = value.string;
        arg1.resourceId = value.sourceId;
    }

    public void getValue(String arg0, TypedValue arg1, boolean arg2) {
        throw new UnsupportedOperationException(
                "Tartine: not implemented: Resources.getValue(String, TypedValue, boolean)");
    }

    public void getValueForDensity(int arg0, int arg1, TypedValue arg2, boolean arg3) {
        getValue(arg0, arg2, arg3);
    }

    /**
     * AOSP Resources.getXml reads the compiled XML resource as a pull parser;
     * the host decodes the app's AXML into {@code tartine.fw.XmlNode} and the
     * shell replays it.
     */
    public XmlResourceParser getXml(int arg0) {
        AppResourceHost host = AppResources.host();
        if (host != null) {
            tartine.fw.XmlNode node = host.xml(arg0);
            if (node != null) {
                return new XmlResourceParserImpl(node);
            }
        }
        throw gap("getXml(I)Landroid/content/res/XmlResourceParser;", arg0);
    }

    public final Resources.Theme newTheme() {
        return new Theme(this);
    }

    public TypedArray obtainAttributes(AttributeSet arg0, int[] arg1) {
        return Theme.readAttributes(this, null, arg0, arg1);
    }

    public TypedArray obtainTypedArray(int arg0) {
        List<ResValue> values = hostArray(arg0);
        if (values == null) {
            throw gap("obtainTypedArray(I)Landroid/content/res/TypedArray;", arg0);
        }
        ResValue[] array = values.toArray(new ResValue[0]);
        return new TypedArray(this, null, new int[array.length], array);
    }

    public InputStream openRawResource(int arg0) {
        return openRawResource(arg0, new TypedValue());
    }

    public InputStream openRawResource(int arg0, TypedValue arg1) {
        ResValue value = hostValue(arg0);
        AppResourceHost host = AppResources.host();
        if (value != null && value.type == TypedValue.TYPE_STRING
                && value.string != null && host != null) {
            byte[] bytes = host.asset(value.string);
            if (bytes != null) {
                arg1.type = value.type;
                arg1.data = value.data;
                arg1.string = value.string;
                return new ByteArrayInputStream(bytes);
            }
        }
        throw gap("openRawResource(I)Ljava/io/InputStream;", arg0);
    }

    public AssetFileDescriptor openRawResourceFd(int arg0) {
        throw gap("openRawResourceFd(I)Landroid/content/res/AssetFileDescriptor;", arg0);
    }

    public void parseBundleExtra(String arg0, AttributeSet arg1, Bundle arg2) {
        throw new UnsupportedOperationException(
                "Tartine: not implemented: Resources.parseBundleExtra");
    }

    public void parseBundleExtras(XmlResourceParser arg0, Bundle arg1) {
        throw new UnsupportedOperationException(
                "Tartine: not implemented: Resources.parseBundleExtras");
    }

    public static void registerResourcePaths(String arg0, ApplicationInfo arg1) {
        throw new UnsupportedOperationException(
                "Tartine: not implemented: Resources.registerResourcePaths");
    }

    public void removeLoaders(ResourcesLoader[] arg0) {
        throw new UnsupportedOperationException(
                "Tartine: not implemented: Resources.removeLoaders");
    }

    public void updateConfiguration(Configuration arg0, DisplayMetrics arg1) {
        throw new UnsupportedOperationException(
                "Tartine: not implemented: Resources.updateConfiguration");
    }

    // ---- host value helpers ---------------------------------------------

    private ResValue hostValue(int resId) {
        AppResourceHost host = AppResources.host();
        return host == null ? null : host.value(resId);
    }

    private ResValue requireValue(String signature, int resId) {
        ResValue value = hostValue(resId);
        if (value == null) {
            throw gap(signature, resId);
        }
        return value;
    }

    /** Bag values (arrays, plurals) as an ordered value list, or null. */
    private List<ResValue> hostArray(int resId) {
        AppResourceHost host = AppResources.host();
        return host == null ? null : host.arrayValues(resId);
    }

    private UnsupportedOperationException gap(String signature, int resId) {
        return new UnsupportedOperationException(
                "Tartine: not implemented: Landroid/content/res/Resources;->"
                        + signature + " (id=" + Integer.toHexString(resId) + ")");
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
     * AOSP Resources.getDisplayMetrics (Resources.java:2238-2240) returns the
     * ResourcesImpl metrics; the desktop shell has no ResourcesImpl, so the
     * host window's metrics are owned here. Declared after {@code mConfiguration}
     * because the metrics scale with the configuration's font scale.
     */
    private final DisplayMetrics mMetrics =
            probeDisplayMetrics(mConfiguration.fontScale);

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

    /** Scratch mirror constructor (SkeletonGen). */
        public Resources() {
        }

        /**
         * Tartine: a resource theme, mirroring AOSP
         * {@code android.content.res.Resources.Theme}. Holds the ordered list
         * of style/theme resource ids applied to it (later ones win) and
         * resolves attributes through the installed {@link AppResourceHost}.
         */
        public static class Theme {

        private final Resources mResources;
        private final List<Integer> mStyles = new ArrayList<>();

        Theme(Resources resources) {
            mResources = resources;
        }

        /** Scratch mirror constructor (SkeletonGen). */
        public Theme() {
            this(null);
        }

        public void applyStyle(int arg0, boolean arg1) {
            if (arg0 == 0) {
                return;
            }
            if (arg1 || !mStyles.contains(arg0)) {
                mStyles.add(arg0);
            }
        }

        public void dump(int arg0, String arg1, String arg2) {
            // diagnostic only; nothing to dump in the shell
        }

        public boolean equals(Object arg0) {
            return this == arg0;
        }

        public int[] getAttributeResolutionStack(int arg0, int arg1, int arg2) {
            return new int[0];
        }

        public int getChangingConfigurations() {
            return 0;
        }

        public Drawable getDrawable(int arg0) {
            return mResources == null ? null : mResources.getDrawable(arg0, this);
        }

        public int getExplicitStyle(AttributeSet arg0) {
            return 0;
        }

        public Resources getResources() {
            return mResources;
        }

        public int hashCode() {
            return System.identityHashCode(this);
        }

        public TypedArray obtainStyledAttributes(AttributeSet arg0, int[] arg1, int arg2, int arg3) {
            return readAttributes(mResources, this, arg0, arg1, arg2, arg3);
        }

        public TypedArray obtainStyledAttributes(int arg0, int[] arg1) {
            return readAttributes(mResources, this, null, arg1, 0, arg0);
        }

        public TypedArray obtainStyledAttributes(int[] arg0) {
            return readAttributes(mResources, this, null, arg0, 0, 0);
        }

        public void rebase() {
            // no resource overlays in the shell
        }

        public boolean resolveAttribute(int arg0, TypedValue arg1, boolean arg2) {
            ResValue value = resolve(arg0);
            if (value == null) {
                return false;
            }
            arg1.type = value.type;
            arg1.data = value.data;
            arg1.string = value.string;
            arg1.resourceId = value.sourceId;
            return true;
        }

        private ResValue resolve(int attrId) {
            AppResourceHost host = AppResources.host();
            return host == null ? null : host.attribute(attrId, styleIds());
        }

        private int[] styleIds() {
            int[] ids = new int[mStyles.size()];
            for (int i = 0; i < ids.length; i++) {
                ids[i] = mStyles.get(i);
            }
            return ids;
        }

        void addStyles(List<Integer> extra) {
            mStyles.addAll(extra);
        }

        List<Integer> styles() {
            return mStyles;
        }

        public void setTo(Resources.Theme arg0) {
            mStyles.clear();
            mStyles.addAll(arg0.mStyles);
        }

        public String toString() {
            return "Theme{styles=" + mStyles + "}";
        }

        // ---- styled-attribute reading -----------------------------------

        /** Non-themed {@code Resources.obtainAttributes}: XML attributes only. */
        static TypedArray readAttributes(Resources resources, Resources.Theme theme,
                AttributeSet set, int[] attrs) {
            return readAttributes(resources, theme, set, attrs, 0, 0);
        }

        /**
         * AOSP {@code Theme.obtainStyledAttributes}: resolve each requested
         * attribute from the element's own attributes first, then through the
         * order of styles (theme, {@code defStyleAttr}, {@code defStyleRes},
         * element {@code style}), then follow {@code ?attr} indirection.
         */
        static TypedArray readAttributes(Resources resources, Resources.Theme theme,
                AttributeSet set, int[] attrs, int defStyleAttr, int defStyleRes) {
            AppResourceHost host = AppResources.host();
            List<Integer> styles = new ArrayList<>();
            if (defStyleRes != 0) {
                styles.add(defStyleRes);
            }
            if (defStyleAttr != 0 && host != null) {
                ResValue defStyle = host.attribute(defStyleAttr,
                        theme == null ? new int[0] : theme.styleIds());
                if (defStyle != null && defStyle.type == TypedValue.TYPE_REFERENCE) {
                    styles.add(defStyle.data);
                }
            }
            if (theme != null) {
                styles.addAll(theme.styles());
            }
            if (set instanceof XmlResourceParserImpl) {
                int elementStyle = ((XmlResourceParserImpl) set).getStyleAttribute();
                if (elementStyle != 0) {
                    styles.add(elementStyle);
                }
            }
            int[] styleIds = new int[styles.size()];
            for (int i = 0; i < styleIds.length; i++) {
                styleIds[i] = styles.get(i);
            }
            ResValue[] values = new ResValue[attrs.length];
            for (int i = 0; i < attrs.length; i++) {
                values[i] = resolveAttribute(host, set, attrs[i], styleIds);
            }
            return new TypedArray(resources, theme, attrs, values);
        }

        private static ResValue resolveAttribute(AppResourceHost host,
                AttributeSet set, int attrId, int[] styleIds) {
            if (attrId == 0) {
                return null;
            }
            if (set instanceof XmlResourceParserImpl) {
                ResValue fromSet = ((XmlResourceParserImpl) set).valueForAttribute(attrId);
                if (fromSet != null) {
                    return fromSet;
                }
            }
            if (host == null) {
                return null;
            }
            ResValue value = host.attribute(attrId, styleIds);
            if (value != null && value.type == TypedValue.TYPE_ATTRIBUTE) {
                value = host.attribute(value.data, styleIds);
            }
            return value;
        }
        }
    }
