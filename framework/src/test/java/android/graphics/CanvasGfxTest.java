package android.graphics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tartine.gfx.GfxBackend;
import tartine.gfx.GfxFontMetrics;
import tartine.gfx.testing.RecordingBackend;

/**
 * Framework-side seam tests: android.graphics.Canvas/Paint/RecordingCanvas/
 * RenderNode drive the installed backend with no natives. The recording
 * backend gives deterministic font metrics and a readable call log.
 */
class CanvasGfxTest {

    private RecordingBackend backend;

    @BeforeEach
    void installBackend() {
        GfxBackend.uninstall();
        backend = new RecordingBackend();
        GfxBackend.install(backend);
        // Clear the static default-typeface cache so each test resolves
        // through its own backend instance.
        Paint.tartineSetPreferredFamilies(null);
    }

    @AfterEach
    void uninstallBackend() {
        GfxBackend.uninstall();
    }

    private Canvas canvas(int width, int height) {
        backend.createSurface(width, height);
        return Canvas.tartineCreate(backend.lastSurface.getCanvas());
    }

    @Test
    void widthAndHeightComeFromTheSurface() {
        Canvas canvas = canvas(120, 80);

        assertEquals(120, canvas.getWidth());
        assertEquals(80, canvas.getHeight());
    }

    @Test
    void clipRectIntersectReportsNonEmptyAndUpdatesBounds() {
        Canvas canvas = canvas(100, 100);

        assertTrue(canvas.clipRect(10, 10, 50, 50));
        assertEquals(new Rect(10, 10, 50, 50), canvas.getClipBounds());

        assertFalse(canvas.clipRect(60, 60, 80, 80));
        assertTrue(canvas.getClipBounds().isEmpty());
    }

    @Test
    void clipRectDifferenceForwardsTheMode() {
        Canvas canvas = canvas(100, 100);

        assertTrue(canvas.clipRect(0, 0, 100, 100, Region.Op.INTERSECT));
        assertFalse(canvas.clipRect(0, 0, 100, 100, Region.Op.DIFFERENCE));

        assertTrue(canvas.getClipBounds().isEmpty());
        assertTrue(backend.calls.contains(
                "clipRect(0.0,0.0,100.0,100.0,DIFFERENCE)=false"));
    }

    @Test
    void saveRestoreTracksTheSaveCountAndForwards() {
        Canvas canvas = canvas(100, 100);

        // AOSP/Skia: the base state is save count 1 and save() returns the
        // count *before* the save.
        assertEquals(1, canvas.getSaveCount());
        assertEquals(1, canvas.save());
        assertEquals(2, canvas.getSaveCount());
        assertEquals(2, canvas.save());
        assertEquals(3, canvas.getSaveCount());
        canvas.restore();
        assertEquals(2, canvas.getSaveCount());
        canvas.restore();
        assertEquals(1, canvas.getSaveCount());
        canvas.restore();
        assertEquals(1, canvas.getSaveCount());

        assertTrue(backend.calls.contains("save"));
        assertTrue(backend.calls.contains("restore"));
    }

    @Test
    void restoreToCountPopsBalancedSaves() {
        Canvas canvas = canvas(100, 100);
        canvas.clipRect(0, 0, 100, 100);

        int restoreTo = canvas.save();
        canvas.clipRect(0, 0, 0, 0);
        assertTrue(canvas.getClipBounds().isEmpty());

        canvas.restoreToCount(restoreTo);
        assertEquals(1, canvas.getSaveCount());
        assertEquals(new Rect(0, 0, 100, 100), canvas.getClipBounds());
    }

    @Test
    void drawRectForwardsThePaintSnapshot() {
        final Canvas canvas = canvas(100, 100);
        Paint paint = new Paint(0);
        paint.setColor(0xFF336699);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3.0f);

        canvas.drawRect(1, 2, 3, 4, paint);

        assertTrue(backend.calls.contains(
                "drawRect(1.0,2.0,3.0,4.0,GfxPaint[color=0xff336699,"
                        + " antiAlias=true, style=STROKE, strokeWidth=3.0])"));
    }

    @Test
    void drawTextUsesFixedMetricsAndKeepsTheCachedFont() {
        Canvas canvas = canvas(100, 100);
        Paint paint = new Paint(0);
        paint.setTextSize(14.0f);

        canvas.drawText("abc", 1, 2, paint);

        assertTrue(backend.calls.contains(
                "drawText(abc,1.0,2.0,FakeFont[size=14.0],"
                        + "GfxPaint[color=0xff000000, antiAlias=false,"
                        + " style=FILL, strokeWidth=0.0])"));
        assertEquals(1, backend.fonts.size());
        assertFalse(backend.fonts.get(0).isClosed());

        assertEquals(18.0f, paint.measureText("abc"));
        assertEquals(-15.0f, paint.ascent());
        assertEquals(-17.0f, paint.getFontMetrics().top);
        assertEquals(4.0f, paint.getFontMetrics().descent);

        Rect bounds = new Rect();
        paint.getTextBounds("abc", 0, 3, bounds);
        assertEquals(18, bounds.width());
        assertEquals(19, bounds.height());

        // Measurement reused the draw's font instead of allocating another.
        assertEquals(1, backend.fonts.size());
    }

    @Test
    void drawTextFoldsExplicitNewlinesIntoOneRun() {
        Canvas canvas = canvas(100, 100);
        Paint paint = new Paint(0);
        paint.setTextSize(14.0f);

        canvas.drawText("a\nb", 0, 15, paint);

        List<String> draws = backend.calls.stream()
                .filter(call -> call.startsWith("drawText("))
                .toList();
        // AOSP shapes the whole run and gives '\n' zero advance and no glyph,
        // so a device renders "a\nb" as "ab" on a single baseline.
        assertEquals(1, draws.size());
        assertTrue(draws.get(0).startsWith("drawText(ab,0.0,15.0,"));
    }

    @Test
    void drawTextFoldsConsecutiveAndCarriageReturnBreaks() {
        Canvas canvas = canvas(100, 100);
        Paint paint = new Paint(0);
        paint.setTextSize(14.0f);

        canvas.drawText("a\n\nb", 0, 0, paint);
        canvas.drawText("a\r\nb", 0, 0, paint);

        List<String> draws = backend.calls.stream()
                .filter(call -> call.startsWith("drawText("))
                .toList();
        assertEquals(2, draws.size());
        assertTrue(draws.get(0).startsWith("drawText(ab,0.0,0.0,"));
        assertTrue(draws.get(1).startsWith("drawText(ab,0.0,0.0,"));
    }

    @Test
    void measureTextFoldsNewlinesAndMeasuresTheWholeText() {
        Paint paint = new Paint(0);
        paint.setTextSize(14.0f);

        // AOSP gives '\n' zero advance, so a multi-line string measures like
        // the concatenation of its lines, not like its widest line.
        assertEquals(6.0f, paint.measureText("a"));
        assertEquals(12.0f, paint.measureText("a\nb"));
        assertEquals(paint.measureText("ab"), paint.measureText("a\nb"));
        assertEquals(24.0f, paint.measureText("abc\nd"));
        assertEquals(18.0f, paint.measureText("a\nbc"));
        assertEquals(12.0f, paint.measureText("a\r\nb"));
        assertEquals(12.0f, paint.measureText("a\rb"));

        // Every overload follows the same fold.
        assertEquals(12.0f, paint.measureText("xab\nc", 1, 4));
        assertEquals(12.0f, paint.measureText(
                new char[] {'a', '\n', 'b'}, 0, 3));
    }

    @Test
    void getTextBoundsFoldsNewlinesAndMeasuresTheWholeText() {
        Paint paint = new Paint(0);
        paint.setTextSize(14.0f);
        Rect joined = new Rect();
        paint.getTextBounds("ab", 0, 2, joined);
        Rect multi = new Rect();
        paint.getTextBounds("a\nb", 0, 3, multi);

        assertEquals(joined.width(), multi.width());
        assertEquals(joined.height(), multi.height());
        assertEquals(12, multi.width());
        assertEquals(19, multi.height());
        assertEquals(joined.width(),
                Math.round(paint.measureText("a\nb")));

        // Unlike getDesiredWidth, Paint does not pick a widest line.
        Rect longText = new Rect();
        paint.getTextBounds("a\nbc", 0, 4, longText);
        assertEquals(18, longText.width());
        assertEquals(19, longText.height());

        Rect crlf = new Rect();
        paint.getTextBounds("a\r\nb", 0, 4, crlf);
        assertEquals(joined.width(), crlf.width());
        assertEquals(joined.height(), crlf.height());

        Rect chars = new Rect();
        paint.getTextBounds(new char[] {'a', '\n', 'b'}, 0, 3, chars);
        assertEquals(joined.width(), chars.width());
        assertEquals(joined.height(), chars.height());
    }

    @Test
    void getFontMetricsIntRoundsLikeAosp() {
        // GraphicsJNI::set_metrics_int: top floor, ascent/descent round,
        // bottom ceil, leading round.
        backend.metrics = new GfxFontMetrics(-17.4f, -15.6f, 4.4f, 5.2f, 0.3f);
        Paint paint = new Paint(0);
        paint.setTextSize(14.0f);

        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        assertEquals(-18, fm.top);       // floor(-17.4)
        assertEquals(-16, fm.ascent);    // round(-15.6)
        assertEquals(4, fm.descent);     // round(4.4)
        assertEquals(6, fm.bottom);      // ceil(5.2)
        assertEquals(0, fm.leading);     // round(0.3)

        Paint.FontMetricsInt into = new Paint.FontMetricsInt();
        assertEquals(4 - (-16) + 0, paint.getFontMetricsInt(into));
        assertEquals(fm, into);
    }

    @Test
    void subpixelTextFlagRoundTripsAndRebuildsTheFont() {
        // AOSP's HIDDEN_DEFAULT_PAINT_FLAGS does not set SUBPIXEL_TEXT_FLAG,
        // so a fresh paint measures with integer advances.
        Paint paint = new Paint(0);
        paint.setTextSize(14.0f);

        assertFalse(paint.isSubpixelText());
        paint.measureText("abc");
        assertEquals(1, backend.fonts.size());
        assertTrue(backend.calls.contains("newFont(14.0,false)"));

        paint.setSubpixelText(true);
        assertTrue(paint.isSubpixelText());
        assertEquals(Paint.SUBPIXEL_TEXT_FLAG,
                paint.getFlags() & Paint.SUBPIXEL_TEXT_FLAG);
        paint.measureText("abc");
        assertEquals(2, backend.fonts.size());
        assertTrue(backend.fonts.get(0).isClosed());
        assertTrue(backend.calls.contains("newFont(14.0,true)"));

        paint.setSubpixelText(false);
        assertFalse(paint.isSubpixelText());
        paint.measureText("abc");
        assertEquals(3, backend.fonts.size());
        assertTrue(backend.fonts.get(1).isClosed());
    }

    @Test
    void paintReusesOneFontUntilTheTextSizeChanges() {
        Paint paint = new Paint(0);
        paint.setTextSize(14.0f);

        paint.measureText("abc");
        paint.measureText("de");
        paint.ascent();

        assertEquals(1, backend.fonts.size());
        assertFalse(backend.fonts.get(0).isClosed());

        paint.setTextSize(20.0f);
        paint.measureText("abc");

        assertEquals(2, backend.fonts.size());
        assertTrue(backend.fonts.get(0).isClosed());
        assertFalse(backend.fonts.get(1).isClosed());
    }

    @Test
    void paintRebuildsTheFontWhenTheResolvedTypefaceChanges() {
        Paint paint = new Paint(0);
        paint.setTextSize(14.0f);
        paint.measureText("abc");
        assertEquals(1, backend.fonts.size());

        Paint.tartineSetPreferredFamilies(new String[] {"Segoe UI"});
        paint.measureText("abc");

        assertEquals(2, backend.fonts.size());
        assertTrue(backend.fonts.get(0).isClosed());
        assertFalse(backend.fonts.get(1).isClosed());
    }

    @Test
    void quickRejectForwardsTheDeviceClip() {
        Canvas canvas = canvas(100, 100);
        canvas.clipRect(10, 10, 50, 50);

        assertTrue(canvas.quickReject(0, 0, 5, 5));
        assertFalse(canvas.quickReject(20, 20, 30, 30));
        assertTrue(canvas.quickReject(60, 60, 80, 80));
    }

    @Test
    void renderNodeRecordingReplaysTheSealedPicture() {
        final Canvas target = canvas(100, 100);
        RenderNode node = new RenderNode("test");
        RecordingCanvas recording = node.beginRecording(30, 40);
        recording.drawRect(1, 2, 3, 4, new Paint(0));
        node.endRecording();

        assertTrue(node.hasDisplayList());
        assertTrue(backend.calls.contains("recorder.close"));

        target.drawRenderNode(node);

        assertTrue(backend.calls.stream().anyMatch(
                call -> call.startsWith("drawPicture(FakePicture[30.0x40.0]")));
    }

    @Test
    void rootlessCanvasKeepsStateWithoutBackend() {
        Canvas canvas = new Canvas();

        assertEquals(0, canvas.getWidth());
        assertEquals(0, canvas.getHeight());
        assertEquals(1, canvas.save());
        canvas.translate(1, 2);
        canvas.restore();
        assertEquals(1, canvas.getSaveCount());
        assertTrue(canvas.clipRect(0, 0, 10, 10));
    }
}
