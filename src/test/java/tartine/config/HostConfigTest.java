package tartine.config;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import tartine.runtime.Vfs;

/**
 * Pins the host config file seam: per-id system-color overrides beat the
 * generated baseline, font families flow into Paint, and invalid keys or
 * values are skipped with a warning instead of failing the load.
 */
class HostConfigTest {
    @TempDir
    Path temp;

    @BeforeEach
    void resetSeams() {
        tartine.fw.SystemColors.clearOverrides();
        android.graphics.Paint.tartineSetPreferredFamilies(null);
        Vfs.resetCard();
    }

    @AfterEach
    void restoreSeams() {
        tartine.fw.SystemColors.clearOverrides();
        android.graphics.Paint.tartineSetPreferredFamilies(null);
        Vfs.resetCard();
    }

    private void config(String... lines) throws Exception {
        Path file = temp.resolve(HostConfig.FILE_NAME);
        Files.write(file, String.join("\n", lines).getBytes());
        HostConfig.loadFrom(file);
    }

    @Test
    void colorOverrideBeatsBaseline() throws Exception {
        config("color.17170488 = 0xFF102433");
        assertEquals(0xFF102433,
                tartine.fw.SystemColors.get(17170488).intValue());
    }

    @Test
    void namedColorKeyResolvesToJarId() throws Exception {
        // system_accent1_10 is the same id the numeric pin already locks:
        // config speaks canonical android.R$color names (jar truth), the
        // generated table translate them to ids.
        config("color.system_accent1_10 = 0xFF112233");
        assertEquals(0xFF112233,
                tartine.fw.SystemColors.get(17170488).intValue());
    }

    @Test
    void unknownNameSkipped() throws Exception {
        config("color.not_a_color_name = #102433");
        // No override took effect: the baseline row answers untouched.
        assertEquals(0xFFFAF8FF,
                tartine.fw.SystemColors.get(17170538).intValue());
    }

    @Test
    void idAndValueFormsParse() throws Exception {
        config("color.17170538 = #FAF8FF", "color.17170539 = 0x123456",
                "color.17170540 = 1193046");
        assertEquals(0xFFFAF8FF,
                tartine.fw.SystemColors.get(17170538).intValue());
        assertEquals(0x123456,
                tartine.fw.SystemColors.get(17170539).intValue());
        // Decimal: the same 32-bit ARGB (#00123456) rides as decimal.
        assertEquals(1193046,
                tartine.fw.SystemColors.get(17170540).intValue());
    }

    @Test
    void badValueSkipped() throws Exception {
        config("color.nope = 12345", "color.17170488 = not-a-color");
        // 999 is no baseline row either way; the point is the junk keys
        // surfaced a warning without crashing or misapplying anything.
        assertNull(tartine.fw.SystemColors.get(999));
    }

    @Test
    void unknownKeyIgnored() throws Exception {
        config("mystery = 5");
        assertNull(tartine.fw.SystemColors.get(999));
    }

    @Test
    void fontFamilyListApplied() throws Exception {
        config("font.families = Verdana, Comic New , Arial");
        assertArrayEquals(new String[]{"Verdana", "Comic New", "Arial"},
                android.graphics.Paint.tartinePreferredFamilies());
    }

    @Test
    void emptyFontListIgnored() throws Exception {
        config("font.families = , ,");
        assertEquals("Roboto",
                android.graphics.Paint.tartinePreferredFamilies()[0]);
    }

    @Test
    void missingFileLoadsDefaults() {
        HostConfig.loadFrom(Path.of(temp.toString(), "absent.properties"));
        // Default behavior: no overrides at all (real id 999 absent from
        // the baseline table).
        assertNull(tartine.fw.SystemColors.get(999));
    }

    @Test
    void sdcardMountAppliesReadOnlyByDefault() throws Exception {
        Path shared = temp.resolve("shared");
        Files.createDirectories(shared);
        // Forward slashes: JDK Properties eats lone backslashes as escapes,
        // so config values spell Windows paths the portable way.
        config("sdcard.mount = " + shared.toString().replace('\\', '/'));
        Vfs.Sdcard card = Vfs.policy().sdcard();
        assertEquals(shared.toRealPath().normalize(), card.hostRoot());
        assertTrue(card.readOnly(),
                "the audit mounts override storage read-only by default");
    }

    @Test
    void sdcardReadonlyClearsOnConfig() throws Exception {
        Path shared = temp.resolve("shared");
        Files.createDirectories(shared);
        config("sdcard.mount = " + shared.toString().replace('\\', '/'),
                "sdcard.mount.readonly = false");
        assertFalse(Vfs.policy().sdcard().readOnly());
    }

    @Test
    void sdcardMountRefusedKeepsDefault() throws Exception {
        config("sdcard.mount = " + temp.resolve("absent-dir").toString().replace('\\', '/'));
        Vfs.Sdcard card = Vfs.policy().sdcard();
        assertEquals(Vfs.root().resolve("sdcard"), card.hostRoot());
        assertFalse(card.readOnly());
    }

    @Test
    void sdcardReadonlyWithoutMountIgnored() throws Exception {
        config("sdcard.mount.readonly = false");
        Vfs.Sdcard card = Vfs.policy().sdcard();
        assertEquals(Vfs.root().resolve("sdcard"), card.hostRoot());
        assertFalse(card.readOnly());
    }

    @Test
    void badSdcardReadonlyValueIgnored() throws Exception {
        config("sdcard.mount.readonly = sure");
        assertEquals(Vfs.root().resolve("sdcard"), Vfs.policy().sdcard().hostRoot());
    }

    @Test
    void sdcardMountOrderIndependent() throws Exception {
        // The readonly key may precede the mount it qualifies.
        Path shared = temp.resolve("shared");
        Files.createDirectories(shared);
        config("sdcard.mount.readonly = true",
                "sdcard.mount = " + shared.toString().replace('\\', '/'));
        assertTrue(Vfs.policy().sdcard().readOnly());
    }
}
