package android.content.res;

import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Locale;

    public class Configuration implements Comparable<Configuration>, Parcelable {

    public static final int COLOR_MODE_HDR_MASK = 12;
    public static final int COLOR_MODE_HDR_NO = 4;
    public static final int COLOR_MODE_HDR_SHIFT = 2;
    public static final int COLOR_MODE_HDR_UNDEFINED = 0;
    public static final int COLOR_MODE_HDR_YES = 8;
    public static final int COLOR_MODE_UNDEFINED = 0;
    public static final int COLOR_MODE_WIDE_COLOR_GAMUT_MASK = 3;
    public static final int COLOR_MODE_WIDE_COLOR_GAMUT_NO = 1;
    public static final int COLOR_MODE_WIDE_COLOR_GAMUT_UNDEFINED = 0;
    public static final int COLOR_MODE_WIDE_COLOR_GAMUT_YES = 2;
    public static Parcelable.Creator CREATOR;
    public static final int DENSITY_DPI_UNDEFINED = 0;
    public static final int FONT_WEIGHT_ADJUSTMENT_UNDEFINED = 2147483647;
    public static final int GRAMMATICAL_GENDER_FEMININE = 2;
    public static final int GRAMMATICAL_GENDER_MASCULINE = 3;
    public static final int GRAMMATICAL_GENDER_NEUTRAL = 1;
    public static final int GRAMMATICAL_GENDER_NOT_SPECIFIED = 0;
    public static final int HARDKEYBOARDHIDDEN_NO = 1;
    public static final int HARDKEYBOARDHIDDEN_UNDEFINED = 0;
    public static final int HARDKEYBOARDHIDDEN_YES = 2;
    public static final int KEYBOARDHIDDEN_NO = 1;
    public static final int KEYBOARDHIDDEN_UNDEFINED = 0;
    public static final int KEYBOARDHIDDEN_YES = 2;
    public static final int KEYBOARD_12KEY = 3;
    public static final int KEYBOARD_NOKEYS = 1;
    public static final int KEYBOARD_QWERTY = 2;
    public static final int KEYBOARD_UNDEFINED = 0;
    public static final int MNC_ZERO = 65535;
    public static final int NAVIGATIONHIDDEN_NO = 1;
    public static final int NAVIGATIONHIDDEN_UNDEFINED = 0;
    public static final int NAVIGATIONHIDDEN_YES = 2;
    public static final int NAVIGATION_DPAD = 2;
    public static final int NAVIGATION_NONAV = 1;
    public static final int NAVIGATION_TRACKBALL = 3;
    public static final int NAVIGATION_UNDEFINED = 0;
    public static final int NAVIGATION_WHEEL = 4;
    public static final int ORIENTATION_LANDSCAPE = 2;
    public static final int ORIENTATION_PORTRAIT = 1;
    public static final int ORIENTATION_SQUARE = 3;
    public static final int ORIENTATION_UNDEFINED = 0;
    public static final int SCREENLAYOUT_COMPAT_NEEDED = 0x10000000;
    public static final int SCREENLAYOUT_LAYOUTDIR_LTR = 64;
    public static final int SCREENLAYOUT_LAYOUTDIR_MASK = 192;
    public static final int SCREENLAYOUT_LAYOUTDIR_RTL = 128;
    public static final int SCREENLAYOUT_LAYOUTDIR_SHIFT = 6;
    public static final int SCREENLAYOUT_LAYOUTDIR_UNDEFINED = 0;
    public static final int SCREENLAYOUT_LONG_MASK = 48;
    public static final int SCREENLAYOUT_LONG_NO = 16;
    public static final int SCREENLAYOUT_LONG_UNDEFINED = 0;
    public static final int SCREENLAYOUT_LONG_YES = 32;
    public static final int SCREENLAYOUT_ROUND_MASK = 768;
    public static final int SCREENLAYOUT_ROUND_NO = 256;
    public static final int SCREENLAYOUT_ROUND_UNDEFINED = 0;
    public static final int SCREENLAYOUT_ROUND_YES = 512;
    public static final int SCREENLAYOUT_SIZE_LARGE = 3;
    public static final int SCREENLAYOUT_SIZE_MASK = 15;
    public static final int SCREENLAYOUT_SIZE_NORMAL = 2;
    public static final int SCREENLAYOUT_SIZE_SMALL = 1;
    public static final int SCREENLAYOUT_SIZE_UNDEFINED = 0;
    public static final int SCREENLAYOUT_SIZE_XLARGE = 4;
    public static final int SCREENLAYOUT_UNDEFINED = 0;
    public static final int SCREEN_HEIGHT_DP_UNDEFINED = 0;
    public static final int SCREEN_WIDTH_DP_UNDEFINED = 0;
    public static final int SMALLEST_SCREEN_WIDTH_DP_UNDEFINED = 0;
    public static final int TOUCHSCREEN_FINGER = 3;
    public static final int TOUCHSCREEN_NOTOUCH = 1;
    public static final int TOUCHSCREEN_STYLUS = 2;
    public static final int TOUCHSCREEN_UNDEFINED = 0;
    public static final int UI_MODE_NIGHT_MASK = 48;
    public static final int UI_MODE_NIGHT_NO = 16;
    public static final int UI_MODE_NIGHT_UNDEFINED = 0;
    public static final int UI_MODE_NIGHT_YES = 32;
    public static final int UI_MODE_TYPE_APPLIANCE = 5;
    public static final int UI_MODE_TYPE_CAR = 3;
    public static final int UI_MODE_TYPE_DESK = 2;
    public static final int UI_MODE_TYPE_MASK = 15;
    public static final int UI_MODE_TYPE_NORMAL = 1;
    public static final int UI_MODE_TYPE_TELEVISION = 4;
    public static final int UI_MODE_TYPE_UNDEFINED = 0;
    public static final int UI_MODE_TYPE_VR_HEADSET = 7;
    public static final int UI_MODE_TYPE_WATCH = 6;
    public int colorMode;
    public int densityDpi;
    public float fontScale;
    public int fontWeightAdjustment;
    public int hardKeyboardHidden;
    public int keyboard;
    public int keyboardHidden;
    public Locale locale;
    public int mcc;
    public int mnc;
    public int navigation;
    public int navigationHidden;
    public int orientation;
    public int screenHeightDp;
    public int screenLayout;
    public int screenWidthDp;
    public int smallestScreenWidthDp;
    public int touchscreen;
    public int uiMode;

    /**
     * AOSP's modern locale carrier ({@code locale} is the deprecated
     * first-locale mirror). The shell keeps it here so
     * {@code getLocales()} answers a non-null list for AppCompat's
     * configuration merge.
     */
    private LocaleList mLocaleList;

    /**
     * AOSP's grammatical-gender selector ({@code getGrammaticalGender});
     * rolled into {@code compareTo}/{@code hashCode} like the platform
     * field. {@code assetsSeq} and {@code WindowConfiguration} have no
     * shell carrier yet and are omitted from those comparisons.
     */
    private int mGrammaticalGender = GRAMMATICAL_GENDER_NOT_SPECIFIED;

    /**
     * AOSP Configuration ctor runs {@code unset()} (= setToDefaults(), then
     * {@code fontScale = 0}), so a bare {@code new Configuration()} is the
     * "completely undefined" merge delta: every modeled field stays zero.
     * The live device configuration is not a bare Configuration; see
     * {@code Resources} which calls {@link #setToDefaults()} and then fills
     * the display values (density, screen dp size, uiMode, fontScale).
     */
    public Configuration() {
    }

    public Configuration(Configuration arg0) {
        // AOSP frameworks-base core/java/android/content/res/Configuration.java
        // Configuration(Configuration) copy constructor: copies all fields
        // from the source (the interpreter's copyFieldsFrom builtin does
        // the same). Null source throws NPE.
        if (arg0 == null) {
            throw new NullPointerException("config copy of null");
        }
        colorMode = arg0.colorMode;
        densityDpi = arg0.densityDpi;
        fontScale = arg0.fontScale;
        fontWeightAdjustment = arg0.fontWeightAdjustment;
        hardKeyboardHidden = arg0.hardKeyboardHidden;
        keyboard = arg0.keyboard;
        keyboardHidden = arg0.keyboardHidden;
        locale = arg0.locale;
        mGrammaticalGender = arg0.mGrammaticalGender;
        mLocaleList = arg0.mLocaleList;
        mcc = arg0.mcc;
        mnc = arg0.mnc;
        navigation = arg0.navigation;
        navigationHidden = arg0.navigationHidden;
        orientation = arg0.orientation;
        screenHeightDp = arg0.screenHeightDp;
        screenLayout = arg0.screenLayout;
        screenWidthDp = arg0.screenWidthDp;
        smallestScreenWidthDp = arg0.smallestScreenWidthDp;
        touchscreen = arg0.touchscreen;
        uiMode = arg0.uiMode;
    }

    /**
     * AOSP {@code Configuration.fixUpLocaleList}: keeps {@code mLocaleList} in
     * sync with the deprecated {@code locale} field when an older caller has
     * written it directly. The shell tolerates a null list (a bare config)
     * where the platform's setToDefaults installs the empty list.
     */
    private void fixUpLocaleList() {
        if (locale == null) {
            if (mLocaleList == null || !mLocaleList.isEmpty()) {
                mLocaleList = LocaleList.getEmptyLocaleList();
            }
        } else if (mLocaleList == null || mLocaleList.isEmpty()
                || !locale.equals(mLocaleList.get(0))) {
            mLocaleList = new LocaleList(locale);
        }
    }

    /**
     * AOSP frameworks-base
     * core/java/android/content/res/Configuration.java:compareTo (lines
     * 2186-2259), in the same field order. The shell has no
     * {@code assetsSeq} or {@code WindowConfiguration} carrier yet, so those
     * two comparison legs are omitted; every other leg is faithful.
     */
    public int compareTo(Configuration that) {
        int n;
        float a = this.fontScale;
        float b = that.fontScale;
        if (a < b) {
            return -1;
        }
        if (a > b) {
            return 1;
        }
        n = this.mcc - that.mcc;
        if (n != 0) {
            return n;
        }
        n = this.mnc - that.mnc;
        if (n != 0) {
            return n;
        }

        fixUpLocaleList();
        that.fixUpLocaleList();
        // For backward compatibility, an empty locale list is considered
        // greater than any non-empty locale list.
        if (this.mLocaleList.isEmpty()) {
            if (!that.mLocaleList.isEmpty()) {
                return 1;
            }
        } else if (that.mLocaleList.isEmpty()) {
            return -1;
        } else {
            final int minSize = Math.min(this.mLocaleList.size(),
                    that.mLocaleList.size());
            for (int i = 0; i < minSize; ++i) {
                final Locale thisLocale = this.mLocaleList.get(i);
                final Locale thatLocale = that.mLocaleList.get(i);
                n = thisLocale.getLanguage().compareTo(
                        thatLocale.getLanguage());
                if (n != 0) {
                    return n;
                }
                n = thisLocale.getCountry().compareTo(thatLocale.getCountry());
                if (n != 0) {
                    return n;
                }
                n = thisLocale.getVariant().compareTo(thatLocale.getVariant());
                if (n != 0) {
                    return n;
                }
                n = thisLocale.toLanguageTag().compareTo(
                        thatLocale.toLanguageTag());
                if (n != 0) {
                    return n;
                }
            }
            n = this.mLocaleList.size() - that.mLocaleList.size();
            if (n != 0) {
                return n;
            }
        }

        n = this.mGrammaticalGender - that.mGrammaticalGender;
        if (n != 0) {
            return n;
        }
        n = this.touchscreen - that.touchscreen;
        if (n != 0) {
            return n;
        }
        n = this.keyboard - that.keyboard;
        if (n != 0) {
            return n;
        }
        n = this.keyboardHidden - that.keyboardHidden;
        if (n != 0) {
            return n;
        }
        n = this.hardKeyboardHidden - that.hardKeyboardHidden;
        if (n != 0) {
            return n;
        }
        n = this.navigation - that.navigation;
        if (n != 0) {
            return n;
        }
        n = this.navigationHidden - that.navigationHidden;
        if (n != 0) {
            return n;
        }
        n = this.orientation - that.orientation;
        if (n != 0) {
            return n;
        }
        n = this.colorMode - that.colorMode;
        if (n != 0) {
            return n;
        }
        n = this.screenLayout - that.screenLayout;
        if (n != 0) {
            return n;
        }
        n = this.uiMode - that.uiMode;
        if (n != 0) {
            return n;
        }
        n = this.screenWidthDp - that.screenWidthDp;
        if (n != 0) {
            return n;
        }
        n = this.screenHeightDp - that.screenHeightDp;
        if (n != 0) {
            return n;
        }
        n = this.smallestScreenWidthDp - that.smallestScreenWidthDp;
        if (n != 0) {
            return n;
        }
        n = this.densityDpi - that.densityDpi;
        if (n != 0) {
            return n;
        }
        n = this.fontWeightAdjustment - that.fontWeightAdjustment;
        return n;
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->describeContents()I");
    }

    public int diff(Configuration arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->diff(Landroid/content/res/Configuration;)I");
    }

    public boolean equals(Configuration arg0) {
        if (arg0 == null) {
            return false;
        }
        if (arg0 == this) {
            return true;
        }
        return this.compareTo(arg0) == 0;
    }

    public boolean equals(Object arg0) {
        try {
            return equals((Configuration) arg0);
        } catch (ClassCastException e) {
            // AOSP: an object of another type is simply not equal.
        }
        return false;
    }

    public static Configuration generateDelta(Configuration arg0, Configuration arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->generateDelta(Landroid/content/res/Configuration;Landroid/content/res/Configuration;)Landroid/content/res/Configuration;");
    }

    public int getGrammaticalGender() {
        // AOSP Configuration.getGrammaticalGender reads the private field
        // stored by setGrammaticalGender / the resource configuration.
        return mGrammaticalGender;
    }

    public int getLayoutDirection() {
        // AOSP frameworks-base core/java/android/content/res/Configuration.java:
        // getLayoutDirection returns mLayoutDirection; no RTL on the shell
        // (matches the interpreter's headless 0).
        return 0;
    }

    public LocaleList getLocales() {
        if (mLocaleList != null) {
            return mLocaleList;
        }
        // The deprecated single-locale field is the fallback for a config
        // built through public fields; a fully undefined config answers the
        // host's default list rather than null.
        return locale != null ? new LocaleList(locale) : LocaleList.getDefault();
    }

    /**
     * AOSP Configuration.hashCode, in the platform's field order. The absent
     * {@code assetsSeq} leg is omitted (see {@link #compareTo}), which keeps
     * the hash consistent with {@code compareTo == 0}.
     */
    @Override
    public int hashCode() {
        fixUpLocaleList();
        int result = 17;
        result = 31 * result + Float.floatToIntBits(fontScale);
        result = 31 * result + mcc;
        result = 31 * result + mnc;
        result = 31 * result + mLocaleList.hashCode();
        result = 31 * result + touchscreen;
        result = 31 * result + keyboard;
        result = 31 * result + keyboardHidden;
        result = 31 * result + hardKeyboardHidden;
        result = 31 * result + navigation;
        result = 31 * result + navigationHidden;
        result = 31 * result + orientation;
        result = 31 * result + screenLayout;
        result = 31 * result + colorMode;
        result = 31 * result + uiMode;
        result = 31 * result + screenWidthDp;
        result = 31 * result + screenHeightDp;
        result = 31 * result + smallestScreenWidthDp;
        result = 31 * result + densityDpi;
        result = 31 * result + fontWeightAdjustment;
        result = 31 * result + mGrammaticalGender;
        return result;
    }

    public boolean isLayoutSizeAtLeast(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->isLayoutSizeAtLeast(I)Z");
    }

    public boolean isNightModeActive() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->isNightModeActive()Z");
    }

    public boolean isScreenHdr() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->isScreenHdr()Z");
    }

    public boolean isScreenRound() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->isScreenRound()Z");
    }

    public boolean isScreenWideColorGamut() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->isScreenWideColorGamut()Z");
    }

    public static boolean needNewResources(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->needNewResources(II)Z");
    }

    /**
     * Ported from AOSP frameworks-base
     * core/java/android/content/res/Configuration.java
     * ({@code reduceScreenLayout}, @hide): lowers a claimed screen layout to
     * the size, long, and compatibility bits implied by the display's dp
     * size. The thresholds are compatibility guarantees ("DO NOT CHANGE"):
     * {@code longSizeDp < 470} is small; 960x720 dp is xlarge; 640x480 dp is
     * large; wider than 5:3 is long; anything wider than 321 dp or taller
     * than 570 dp needs the legacy size-compat treatment.
     *
     * <p>The shell's device configuration starts from LTR plus the largest
     * claim ({@code LONG_YES | SIZE_XLARGE}) and lets this method reduce it,
     * so both the size and long bits end up defined from the host window.
     */
    static int reduceScreenLayout(int curLayout, int longSizeDp,
            int shortSizeDp) {
        int screenLayoutSize;
        boolean screenLayoutLong;
        boolean screenLayoutCompatNeeded;
        if (longSizeDp < 470) {
            screenLayoutSize = SCREENLAYOUT_SIZE_SMALL;
            screenLayoutLong = false;
            screenLayoutCompatNeeded = false;
        } else {
            if (longSizeDp >= 960 && shortSizeDp >= 720) {
                screenLayoutSize = SCREENLAYOUT_SIZE_XLARGE;
            } else if (longSizeDp >= 640 && shortSizeDp >= 480) {
                screenLayoutSize = SCREENLAYOUT_SIZE_LARGE;
            } else {
                screenLayoutSize = SCREENLAYOUT_SIZE_NORMAL;
            }
            screenLayoutCompatNeeded = shortSizeDp > 321 || longSizeDp > 570;
            screenLayoutLong = ((longSizeDp * 3) / 5) >= (shortSizeDp - 1);
        }
        if (!screenLayoutLong) {
            curLayout = (curLayout & ~SCREENLAYOUT_LONG_MASK)
                    | SCREENLAYOUT_LONG_NO;
        }
        if (screenLayoutCompatNeeded) {
            curLayout |= SCREENLAYOUT_COMPAT_NEEDED;
        }
        int curSize = curLayout & SCREENLAYOUT_SIZE_MASK;
        if (screenLayoutSize < curSize) {
            curLayout = (curLayout & ~SCREENLAYOUT_SIZE_MASK)
                    | screenLayoutSize;
        }
        return curLayout;
    }

    public void readFromParcel(Parcel arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->readFromParcel(Landroid/os/Parcel;)V");
    }

    public void setLayoutDirection(Locale arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->setLayoutDirection(Ljava/util/Locale;)V");
    }

    /** AOSP Configuration.setLocale: sets both the list and the legacy mirror. */
    public void setLocale(Locale arg0) {
        setLocales(arg0 == null ? null : new LocaleList(arg0));
    }

    /** AOSP Configuration.setLocales: list wins, legacy locale mirrors first. */
    public void setLocales(LocaleList arg0) {
        mLocaleList = arg0;
        locale = (arg0 == null || arg0.isEmpty()) ? null : arg0.get(0);
    }

    public void setTo(Configuration arg0) {
        // AOSP frameworks-base core/java/android/content/res/Configuration.java
        // setTo(Configuration): overwrites this config's fields from the
        // source (the interpreter's copyFieldsFrom builtin does the same).
        // Null source throws NPE.
        if (arg0 == null) {
            throw new NullPointerException("config setTo null");
        }
        colorMode = arg0.colorMode;
        densityDpi = arg0.densityDpi;
        fontScale = arg0.fontScale;
        fontWeightAdjustment = arg0.fontWeightAdjustment;
        hardKeyboardHidden = arg0.hardKeyboardHidden;
        keyboard = arg0.keyboard;
        keyboardHidden = arg0.keyboardHidden;
        locale = arg0.locale;
        mGrammaticalGender = arg0.mGrammaticalGender;
        mLocaleList = arg0.mLocaleList;
        mcc = arg0.mcc;
        mnc = arg0.mnc;
        navigation = arg0.navigation;
        navigationHidden = arg0.navigationHidden;
        orientation = arg0.orientation;
        screenHeightDp = arg0.screenHeightDp;
        screenLayout = arg0.screenLayout;
        screenWidthDp = arg0.screenWidthDp;
        smallestScreenWidthDp = arg0.smallestScreenWidthDp;
        touchscreen = arg0.touchscreen;
        uiMode = arg0.uiMode;
    }

    /**
     * Ported behavior. AOSP frameworks-base
     * core/java/android/content/res/Configuration.java:setToDefaults sets
     * {@code fontScale = 1} and every modeled compatibility field to its
     * UNDEFINED/zero value (the fields we do not carry, such as
     * mLocaleList/assetsSeq, have no consumer yet). Note that the AOSP
     * no-arg constructor runs {@code unset()}, which calls this and then
     * forces {@code fontScale = 0}; that internal "completely undefined"
     * state is why a bare {@code new Configuration()} must stay zeroed here
     * too. The device configuration served by {@code Resources} is built on
     * top of this method.
     */
    public void setToDefaults() {
        fontScale = 1;
        mcc = mnc = 0;
        locale = null;
        touchscreen = TOUCHSCREEN_UNDEFINED;
        keyboard = KEYBOARD_UNDEFINED;
        keyboardHidden = KEYBOARDHIDDEN_UNDEFINED;
        hardKeyboardHidden = HARDKEYBOARDHIDDEN_UNDEFINED;
        navigation = NAVIGATION_UNDEFINED;
        navigationHidden = NAVIGATIONHIDDEN_UNDEFINED;
        orientation = ORIENTATION_UNDEFINED;
        screenLayout = SCREENLAYOUT_UNDEFINED;
        colorMode = COLOR_MODE_UNDEFINED;
        uiMode = UI_MODE_TYPE_UNDEFINED;
        screenWidthDp = SCREEN_WIDTH_DP_UNDEFINED;
        screenHeightDp = SCREEN_HEIGHT_DP_UNDEFINED;
        smallestScreenWidthDp = SMALLEST_SCREEN_WIDTH_DP_UNDEFINED;
        densityDpi = DENSITY_DPI_UNDEFINED;
        fontWeightAdjustment = FONT_WEIGHT_ADJUSTMENT_UNDEFINED;
        mGrammaticalGender = GRAMMATICAL_GENDER_NOT_SPECIFIED;
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->toString()Ljava/lang/String;");
    }

    public int updateFrom(Configuration arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->updateFrom(Landroid/content/res/Configuration;)I");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
