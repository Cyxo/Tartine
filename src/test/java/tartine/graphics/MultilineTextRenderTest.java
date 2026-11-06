package tartine.graphics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import android.graphics.Canvas;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tartine.gfx.GfxBackend;
import tartine.gfx.GfxSurface;
import tartine.gfx.skija.SkijaBackend;

/**
 * End-to-end multiline text: six one-character lines must rasterise as a
 * tall, narrow column. Before the newline line table, {@code Layout.draw}
 * handed the whole string to Skija (one wide row of missing-glyph boxes) and
 * {@code getDesiredWidth} measured all eleven characters as one line, so both
 * the ink and the intrinsic width came out wide and short.
 */
class MultilineTextRenderTest {

    private GfxSurface surface;

    @BeforeEach
    void installBackend() {
        GfxBackend.install(new SkijaBackend());
        surface = GfxBackend.get().createSurface(200, 400);
    }

    @AfterEach
    void removeBackend() {
        surface.close();
        GfxBackend.uninstall();
    }

    @Test
    void oneCharacterPerLineRendersTallAndNarrow() {
        String text = "W\nW\nW\nW\nW\nW";
        TextPaint paint = new TextPaint();
        paint.setTextSize(20.0f);
        paint.setColor(0xFF000000);
        StaticLayout layout = StaticLayout.Builder
                .obtain(text, 0, text.length(), paint, surface.getWidth())
                .build();
        Canvas canvas = Canvas.tartineCreate(surface.getCanvas());

        layout.draw(canvas);

        int[] pixels = surface.readPixels();
        int width = surface.getWidth();
        int height = surface.getHeight();
        int minX = width;
        int maxX = -1;
        int minY = height;
        int maxY = -1;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if ((pixels[y * width + x] >>> 24) == 0) {
                    continue;
                }
                if (x < minX) {
                    minX = x;
                }
                if (x > maxX) {
                    maxX = x;
                }
                if (y < minY) {
                    minY = y;
                }
                if (y > maxY) {
                    maxY = y;
                }
            }
        }

        assertTrue(maxX >= minX && maxY >= minY, "text left no ink");
        int inkWidth = maxX - minX + 1;
        int inkHeight = maxY - minY + 1;
        // Six stacked glyphs, one per line: the ink column must be clearly
        // taller than it is wide.
        assertTrue(inkHeight > inkWidth * 3,
                "expected a tall narrow column, got " + inkWidth + "x" + inkHeight);

        assertEquals(6, layout.getLineCount());
        // The intrinsic width is the widest line (a single glyph), so the
        // six-line layout is much taller than that width is wide.
        float desiredWidth = Layout.getDesiredWidth(text, paint);
        assertTrue(desiredWidth > 0.0f);
        assertTrue(layout.getHeight() > desiredWidth * 4,
                "height " + layout.getHeight()
                        + " should dwarf the widest line " + desiredWidth);
    }
}
