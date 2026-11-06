package android.text;

import android.graphics.Canvas;
import android.graphics.Paint;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import tartine.gfx.GfxBackend;
import tartine.gfx.testing.RecordingBackend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Layout.draw must walk the line table, not hand the whole string to the
 * backend: Skia renders '\n' as a missing-glyph box. The recording backend's
 * fixed metrics (top -17, ascent -15, descent 4, bottom 5, leading 0) with the
 * AOSP default includePad put line 0 at baseline 17 (top..descent) and line 1
 * at 36 (ascent..bottom).
 */
class LayoutDrawTest {

    @AfterEach
    void uninstallBackend() {
        GfxBackend.uninstall();
        Paint.tartineSetPreferredFamilies(null);
    }

    @Test
    void explicitNewlinesDrawOnSuccessiveBaselines() {
        RecordingBackend backend = new RecordingBackend();
        GfxBackend.install(backend);
        backend.createSurface(100, 100);
        Canvas canvas = Canvas.tartineCreate(backend.lastSurface.getCanvas());
        TextPaint paint = new TextPaint();
        paint.setTextSize(14.0f);
        StaticLayout layout = StaticLayout.Builder
                .obtain("a\nb", 0, 3, paint, 100)
                .build();

        layout.draw(canvas);

        List<String> draws = backend.calls.stream()
                .filter(call -> call.startsWith("drawText("))
                .toList();
        assertEquals(2, draws.size());
        assertTrue(draws.get(0).startsWith("drawText(a,0.0,17.0,"));
        assertTrue(draws.get(1).startsWith("drawText(b,0.0,36.0,"));
    }
}
