package android.content.res;

import java.util.Locale;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * AOSP parity for the configuration value object. Source:
 * aosp-mirror/platform_frameworks_base
 * core/java/android/content/res/Configuration.java (setToDefaults,
 * copy constructor, setTo; the no-arg constructor runs unset() and leaves
 * fontScale at 0, while setToDefaults() restores 1).
 */
class ConfigurationTest {

    private static Configuration populated() {
        Configuration c = new Configuration();
        c.colorMode = Configuration.COLOR_MODE_HDR_YES
                | Configuration.COLOR_MODE_WIDE_COLOR_GAMUT_YES;
        c.densityDpi = 320;
        c.fontScale = 1.25f;
        c.fontWeightAdjustment = 50;
        c.hardKeyboardHidden = Configuration.HARDKEYBOARDHIDDEN_YES;
        c.keyboard = Configuration.KEYBOARD_QWERTY;
        c.keyboardHidden = Configuration.KEYBOARDHIDDEN_NO;
        c.locale = Locale.CANADA_FRENCH;
        c.mcc = 302;
        c.mnc = 720;
        c.navigation = Configuration.NAVIGATION_DPAD;
        c.navigationHidden = Configuration.NAVIGATIONHIDDEN_NO;
        c.orientation = Configuration.ORIENTATION_LANDSCAPE;
        c.screenHeightDp = 411;
        c.screenLayout = Configuration.SCREENLAYOUT_SIZE_LARGE
                | Configuration.SCREENLAYOUT_LONG_YES;
        c.screenWidthDp = 731;
        c.smallestScreenWidthDp = 411;
        c.touchscreen = Configuration.TOUCHSCREEN_FINGER;
        c.uiMode = Configuration.UI_MODE_TYPE_NORMAL
                | Configuration.UI_MODE_NIGHT_YES;
        return c;
    }

    @Test
    void noArgConstructorIsCompletelyUndefined() {
        Configuration c = new Configuration();
        assertEquals(0, c.colorMode);
        assertEquals(0, c.densityDpi);
        assertEquals(0.0f, c.fontScale);
        assertEquals(0, c.fontWeightAdjustment);
        assertEquals(0, c.hardKeyboardHidden);
        assertEquals(0, c.keyboard);
        assertEquals(0, c.keyboardHidden);
        assertNull(c.locale);
        assertEquals(0, c.mcc);
        assertEquals(0, c.mnc);
        assertEquals(0, c.navigation);
        assertEquals(0, c.navigationHidden);
        assertEquals(0, c.orientation);
        assertEquals(0, c.screenHeightDp);
        assertEquals(0, c.screenLayout);
        assertEquals(0, c.screenWidthDp);
        assertEquals(0, c.smallestScreenWidthDp);
        assertEquals(0, c.touchscreen);
        assertEquals(0, c.uiMode);
    }

    @Test
    void setToDefaultsMatchesAosp() {
        Configuration c = populated();
        c.setToDefaults();
        assertEquals(1.0f, c.fontScale);
        assertEquals(Configuration.DENSITY_DPI_UNDEFINED, c.densityDpi);
        assertEquals(Configuration.FONT_WEIGHT_ADJUSTMENT_UNDEFINED,
                c.fontWeightAdjustment);
        assertEquals(Configuration.COLOR_MODE_UNDEFINED, c.colorMode);
        assertEquals(Configuration.UI_MODE_TYPE_UNDEFINED, c.uiMode);
        assertEquals(Configuration.TOUCHSCREEN_UNDEFINED, c.touchscreen);
        assertEquals(Configuration.KEYBOARD_UNDEFINED, c.keyboard);
        assertEquals(Configuration.KEYBOARDHIDDEN_UNDEFINED, c.keyboardHidden);
        assertEquals(Configuration.HARDKEYBOARDHIDDEN_UNDEFINED,
                c.hardKeyboardHidden);
        assertEquals(Configuration.NAVIGATION_UNDEFINED, c.navigation);
        assertEquals(Configuration.NAVIGATIONHIDDEN_UNDEFINED,
                c.navigationHidden);
        assertEquals(Configuration.ORIENTATION_UNDEFINED, c.orientation);
        assertEquals(Configuration.SCREENLAYOUT_UNDEFINED, c.screenLayout);
        assertEquals(Configuration.SCREEN_WIDTH_DP_UNDEFINED, c.screenWidthDp);
        assertEquals(Configuration.SCREEN_HEIGHT_DP_UNDEFINED, c.screenHeightDp);
        assertEquals(Configuration.SMALLEST_SCREEN_WIDTH_DP_UNDEFINED,
                c.smallestScreenWidthDp);
        assertNull(c.locale);
    }

    @Test
    void copyConstructorCopiesEveryField() {
        Configuration source = populated();
        Configuration copy = new Configuration(source);

        assertEquals(source.colorMode, copy.colorMode);
        assertEquals(source.densityDpi, copy.densityDpi);
        assertEquals(source.fontScale, copy.fontScale);
        assertEquals(source.fontWeightAdjustment, copy.fontWeightAdjustment);
        assertEquals(source.hardKeyboardHidden, copy.hardKeyboardHidden);
        assertEquals(source.keyboard, copy.keyboard);
        assertEquals(source.keyboardHidden, copy.keyboardHidden);
        assertEquals(source.locale, copy.locale);
        assertEquals(source.mcc, copy.mcc);
        assertEquals(source.mnc, copy.mnc);
        assertEquals(source.navigation, copy.navigation);
        assertEquals(source.navigationHidden, copy.navigationHidden);
        assertEquals(source.orientation, copy.orientation);
        assertEquals(source.screenHeightDp, copy.screenHeightDp);
        assertEquals(source.screenLayout, copy.screenLayout);
        assertEquals(source.screenWidthDp, copy.screenWidthDp);
        assertEquals(source.smallestScreenWidthDp, copy.smallestScreenWidthDp);
        assertEquals(source.touchscreen, copy.touchscreen);
        assertEquals(source.uiMode, copy.uiMode);
    }

    @Test
    void copyConstructorRejectsNull() {
        assertThrows(NullPointerException.class, () -> new Configuration(null));
    }

    @Test
    void setToCopiesEveryFieldAndRejectsNull() {
        Configuration source = populated();
        Configuration target = new Configuration();
        target.setTo(source);

        assertEquals(source.colorMode, target.colorMode);
        assertEquals(source.densityDpi, target.densityDpi);
        assertEquals(source.fontScale, target.fontScale);
        assertEquals(source.fontWeightAdjustment, target.fontWeightAdjustment);
        assertEquals(source.hardKeyboardHidden, target.hardKeyboardHidden);
        assertEquals(source.keyboard, target.keyboard);
        assertEquals(source.keyboardHidden, target.keyboardHidden);
        assertEquals(source.locale, target.locale);
        assertEquals(source.mcc, target.mcc);
        assertEquals(source.mnc, target.mnc);
        assertEquals(source.navigation, target.navigation);
        assertEquals(source.navigationHidden, target.navigationHidden);
        assertEquals(source.orientation, target.orientation);
        assertEquals(source.screenHeightDp, target.screenHeightDp);
        assertEquals(source.screenLayout, target.screenLayout);
        assertEquals(source.screenWidthDp, target.screenWidthDp);
        assertEquals(source.smallestScreenWidthDp, target.smallestScreenWidthDp);
        assertEquals(source.touchscreen, target.touchscreen);
        assertEquals(source.uiMode, target.uiMode);

        assertThrows(NullPointerException.class, () -> target.setTo(null));
    }

    @Test
    void getLayoutDirectionIsLtrOnTheShell() {
        // AOSP Configuration.getLayoutDirection returns mLayoutDirection;
        // the shell has no RTL configuration yet and reports LTR (0).
        assertEquals(0, new Configuration().getLayoutDirection());
        assertEquals(0, populated().getLayoutDirection());
    }
}
