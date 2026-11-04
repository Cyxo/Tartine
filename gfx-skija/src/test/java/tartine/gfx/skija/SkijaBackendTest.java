package tartine.gfx.skija;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
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
        SkijaBackend backend = new SkijaBackend();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream previous = System.out;
        try (PrintStream captured = new PrintStream(bytes, true,
                StandardCharsets.UTF_8)) {
            System.setOut(captured);
            backend.defaultTypeface(new String[] {"Tartine No Such Family"});
        } finally {
            System.setOut(previous);
        }

        assertTrue(bytes.toString(StandardCharsets.UTF_8)
                .contains("font: using bundled Roboto fallback"),
                bytes.toString(StandardCharsets.UTF_8));
    }
}
