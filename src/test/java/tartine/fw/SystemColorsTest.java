package tartine.fw;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Pins the android-package system color baseline: fixed ids answer the
 * AOSP core/res platform values, unknown ids read null so callers can
 * surface a named gap instead of a silent zero.
 */
class SystemColorsTest {
    @Test
    void baselinePaletteAnsweredByResourceId() {
        // `android.R.color.system_accent1_10` id from the LOCAL android-36
        // android.jar stub constants; value (#FEFBFF, opaque packaged) from
        // AOSP frameworks/base core/res/res/values/colors.xml ("Colors used
        // in Android system, from design system", fetched 2026-10-19).
        assertEquals(0xFFFEFBFF, SystemColors.get(17170488).intValue());
    }

    @Test
    void classicPlatformColorsAreInTheBaseline() {
        // android.R.color.white / black / transparent ids from the LOCAL
        // android-36 android.jar stub constants; values from AOSP
        // frameworks/base core/res/res/values/colors.xml, cross-checked
        // against the API-36 platform package dump.
        assertEquals(0xFFFFFFFF, SystemColors.get(17170443).intValue());
        assertEquals(0xFF000000, SystemColors.get(17170444).intValue());
        assertEquals(0x00000000, SystemColors.get(17170445).intValue());
        assertEquals(0xFF33B5E5, SystemColors.get(17170450).intValue());
    }

    @Test
    void stateListColorsAreNotFlatBaselineRows() {
        // primary_text_dark (0x01060001) is a res/color selector, not an
        // ARGB literal: it must stay absent so callers see the named gap
        // rather than an invented single color.
        assertNull(SystemColors.get(17170433));
    }

    @Test
    void unknownIdReadsNull() {
        // 0x7f-prefixed ids are app-package resources: never baseline rows.
        assertNull(SystemColors.get(0x7f010203));
    }

    @Test
    void configOverrideBeatsBaselineAndClears() {
        // Host config lifecycle (tartine.config.HostConfig): an override
        // wins per id; clearing restores the pure generated baseline.
        // The id is the pin from baselinePaletteAnsweredByResourceId.
        try {
            SystemColors.setOverride(17170488, 0x00123456);
            assertEquals(0x00123456, SystemColors.get(17170488).intValue());
        } finally {
            SystemColors.clearOverrides();
        }
        assertEquals(0xFFFEFBFF, SystemColors.get(17170488).intValue());
    }

    @Test
    void overrideAnswersEvenUnknownIds() {
        // Overrides are host-config, not baseline rows: an overridden id
        // outside the generated table still answers instead of null.
        try {
            SystemColors.setOverride(0x7f010203, 0xFF112233);
            assertEquals(0xFF112233, SystemColors.get(0x7f010203).intValue());
        } finally {
            SystemColors.clearOverrides();
        }
        assertNull(SystemColors.get(0x7f010203));
    }
}
