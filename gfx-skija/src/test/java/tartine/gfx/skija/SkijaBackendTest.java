package tartine.gfx.skija;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import io.github.humbleui.skija.FontMgr;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tartine.gfx.GfxFont;
import tartine.gfx.GfxTypeface;

/**
 * The renderer's font fallback: a machine with none of the requested desktop
 * families still gets the bundled Roboto face instead of {@code null}, so a
 * text draw cannot fail just because the host is missing fonts.
 *
 * <p>Every case here drives the Skija native text stack, so the class is
 * skipped when that library cannot load (a headless host without the matching
 * platform artifact): there is nothing meaningful to assert and it must not
 * fail the build.
 */
class SkijaBackendTest {

    /**
     * Skips the class when the Skija native font/GL stack is unavailable.
     * {@link FontMgr#getDefault()} forces the native library to load and the
     * platform font manager to initialize; a host without the natives throws
     * a {@link LinkageError} here, which the other tests would otherwise turn
     * into a hard failure.
     */
    @BeforeAll
    static void requireSkijaNativeStack() {
        assumeTrue(skijaNativeStackLoads(),
                "Skija native font/GL stack unavailable on this host");
    }

    private static boolean skijaNativeStackLoads() {
        try {
            return FontMgr.getDefault() != null;
        } catch (LinkageError | RuntimeException unavailable) {
            return false;
        }
    }

    @Test
    void missingFamiliesFallBackToBundledRoboto() {
        SkijaBackend backend = new SkijaBackend();

        GfxTypeface resolved = backend.defaultTypeface(new String[] {
                "Tartine No Such Family", "Also Not Installed 42"});

        assertNotNull(resolved);
        SkijaTypeface typeface =
                assertInstanceOf(SkijaTypeface.class, resolved);
        assertEquals("Roboto", typeface.typeface().getFamilyName());

        GfxFont font = backend.newFont(resolved, 16f, false);
        try {
            assertTrue(font.measureTextWidth("Tartine") > 0);
        } finally {
            font.close();
        }
    }

    @Test
    void nullAndEmptyFamilyListsAlsoUseTheFallback() {
        SkijaBackend backend = new SkijaBackend();

        GfxTypeface forNull = backend.defaultTypeface(null);
        GfxTypeface forEmpty = backend.defaultTypeface(new String[0]);

        assertNotNull(forNull);
        assertNotNull(forEmpty);
        assertEquals("Roboto", ((SkijaTypeface) forNull).typeface()
                .getFamilyName());
        assertSame(((SkijaTypeface) forNull).typeface(),
                ((SkijaTypeface) forEmpty).typeface());
    }

    @Test
    void subpixelOffRoundsEachGlyphAdvanceToAnInteger() {
        SkijaBackend backend = new SkijaBackend();
        GfxTypeface resolved = backend.defaultTypeface(new String[] {
                "Tartine No Such Family"});

        GfxFont subpixel = backend.newFont(resolved, 48f, true);
        GfxFont integerAdvances = backend.newFont(resolved, 48f, false);
        try {
            // Subpixel positioning off rounds each glyph advance to the
            // nearest integer before summing, so a single-glyph advance is an
            // integer and a two-glyph total is the two advances summed (not
            // the total rounded once). Only same-font quantities are
            // compared: the exact advance depends on the host font stack, and
            // an earlier version compared it against the subpixel width while
            // assuming the rounding direction, which flaked across hosts.
            float roundedSingle = integerAdvances.measureTextWidth("W");
            float roundedDouble = integerAdvances.measureTextWidth("WW");
            assertTrue(roundedSingle == Math.round(roundedSingle),
                    "expected an integer glyph advance, got " + roundedSingle);
            assertEquals(2f * roundedSingle, roundedDouble, 0f,
                    "the total is the per-glyph advances summed");

            // The subpixel path keeps and sums the raw advances.
            float rawSingle = subpixel.measureTextWidth("W");
            assertEquals(2f * rawSingle, subpixel.measureTextWidth("WW"),
                    1e-3f, "the subpixel path sums the raw advances");
        } finally {
            subpixel.close();
            integerAdvances.close();
        }
    }

    @Test
    void usingTheFallbackIsLogged() {
        Logger skijaLogger = Logger.getLogger(SkijaBackend.class.getName());
        List<LogRecord> records = new ArrayList<>();
        Handler capture = new Handler() {
            @Override
            public void publish(LogRecord record) {
                records.add(record);
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() {
            }
        };
        skijaLogger.addHandler(capture);
        try {
            SkijaBackend backend = new SkijaBackend();
            backend.defaultTypeface(new String[] {"Tartine No Such Family"});
        } finally {
            skijaLogger.removeHandler(capture);
        }

        boolean found = records.stream().anyMatch(record -> String
                .valueOf(record.getMessage())
                .contains("font: using bundled Roboto fallback"));
        assertTrue(found, records.toString());
    }
}
