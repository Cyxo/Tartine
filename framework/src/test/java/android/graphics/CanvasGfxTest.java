package android.graphics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tartine.gfx.GfxBackend;
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

        assertEquals(1, canvas.save());
        assertEquals(2, canvas.save());
        canvas.restore();
        assertEquals(1, canvas.getSaveCount());
        canvas.restore();
        assertEquals(0, canvas.getSaveCount());

        assertTrue(backend.calls.contains("save"));
        assertTrue(backend.calls.contains("restore"));
    }

    @Test
    void drawRectForwardsThePaintSnapshot() {
        Canvas canvas = canvas(100, 100);
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
        Canvas target = canvas(100, 100);
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
    void rootlessCanvasKeepsStateWithoutABackend() {
        Canvas canvas = new Canvas();

        assertEquals(0, canvas.getWidth());
        assertEquals(0, canvas.getHeight());
        assertEquals(1, canvas.save());
        canvas.translate(1, 2);
        canvas.restore();
        assertEquals(0, canvas.getSaveCount());
        assertTrue(canvas.clipRect(0, 0, 10, 10));
    }
}
