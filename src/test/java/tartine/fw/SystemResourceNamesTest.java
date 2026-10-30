package tartine.fw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Pins the generated canonical android.R$color name -> id table (source:
 * javap -v of the LOCAL android-36 android.jar; the same constant names
 * ROM overlays customize, values aside).
 */
class SystemResourceNamesTest {
    @Test
    void canonicalNamesAnswerJarIds() {
        // Ids cross-pinned against the baseline palette / prior tests:
        // system_accent1_10 (SystemColorsTest palette pin 17170488) and
        // the light surface tone the sample app renders (17170538).
        assertEquals(17170488, SystemResourceNames.idForName(
                "system_accent1_10").intValue());
        assertEquals(17170538, SystemResourceNames.idForName(
                "system_background_light").intValue());
        // Legacy pre-Material names are in the jar too.
        assertEquals(17170444, SystemResourceNames.idForName("black").intValue());
    }

    @Test
    void unknownNameAnswersNull() {
        assertNull(SystemResourceNames.idForName("light_theme_foreground"));
        assertNull(SystemResourceNames.idForName("fullscreen"));
    }
}
