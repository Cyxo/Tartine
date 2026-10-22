package android.content.res;

    public class Configuration implements java.lang.Comparable<android.content.res.Configuration>, android.os.Parcelable {

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
    public static android.os.Parcelable.Creator CREATOR;
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
    public java.util.Locale locale;
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
     * AOSP Configuration ctor (lines 1059-1061) runs unset() — the device
     * setToDefaults lands uiMode at UI_MODE_TYPE_UNDEFINED (0, i.e. no
     * night info; dark-mode detection then reads light), and every other
     * device default is zero for now — so the skeleton's zero-initialized
     * fields match the current consumer surface (nonzero device defaults
     * like fontScale trail until a wall reads one).
     */
    public Configuration() {
    }

    public Configuration(android.content.res.Configuration arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;-><init>(Landroid/content/res/Configuration;)V");
    }

    public int compareTo(android.content.res.Configuration arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->compareTo(Landroid/content/res/Configuration;)I");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->describeContents()I");
    }

    public int diff(android.content.res.Configuration arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->diff(Landroid/content/res/Configuration;)I");
    }

    public boolean equals(android.content.res.Configuration arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->equals(Landroid/content/res/Configuration;)Z");
    }

    public boolean equals(java.lang.Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->equals(Ljava/lang/Object;)Z");
    }

    public static android.content.res.Configuration generateDelta(android.content.res.Configuration arg0, android.content.res.Configuration arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->generateDelta(Landroid/content/res/Configuration;Landroid/content/res/Configuration;)Landroid/content/res/Configuration;");
    }

    public int getGrammaticalGender() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->getGrammaticalGender()I");
    }

    public int getLayoutDirection() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->getLayoutDirection()I");
    }

    public android.os.LocaleList getLocales() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->getLocales()Landroid/os/LocaleList;");
    }

    public int hashCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->hashCode()I");
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

    public void readFromParcel(android.os.Parcel arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->readFromParcel(Landroid/os/Parcel;)V");
    }

    public void setLayoutDirection(java.util.Locale arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->setLayoutDirection(Ljava/util/Locale;)V");
    }

    public void setLocale(java.util.Locale arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->setLocale(Ljava/util/Locale;)V");
    }

    public void setLocales(android.os.LocaleList arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->setLocales(Landroid/os/LocaleList;)V");
    }

    public void setTo(android.content.res.Configuration arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->setTo(Landroid/content/res/Configuration;)V");
    }

    public void setToDefaults() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->setToDefaults()V");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->toString()Ljava/lang/String;");
    }

    public int updateFrom(android.content.res.Configuration arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->updateFrom(Landroid/content/res/Configuration;)I");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/Configuration;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
