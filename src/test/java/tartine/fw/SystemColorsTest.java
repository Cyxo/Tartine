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
    void unknownIdReadsNull() {
        // 0x7f-prefixed ids are app-package resources: never baseline rows.
        assertNull(SystemColors.get(0x7f010203));
    }
}
