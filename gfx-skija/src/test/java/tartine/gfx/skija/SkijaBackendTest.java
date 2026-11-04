package tartine.gfx.skija;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;
import tartine.gfx.GfxFont;
import tartine.gfx.GfxTypeface;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The renderer's font fallback: a machine with none of the requested desktop
 * families still gets the bundled Roboto face instead of {@code null}, so a
 * text draw cannot fail just because the host is missing fonts.
 */
class SkijaBackendTest {

    @Test
    void missingFamiliesFallBackToBundledRoboto() {
        SkijaBackend backend = new SkijaBackend();

        GfxTypeface resolved = backend.defaultTypeface(new String[] {
                "Tartine No Such Family", "Also Not Installed 42"});

        assertNotNull(resolved);
        SkijaTypeface typeface =
                assertInstanceOf(SkijaTypeface.class, resolved);
        assertEquals("Roboto", typeface.typeface().getFamilyName());

        GfxFont font = backend.newFont(resolved, 16f);
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
