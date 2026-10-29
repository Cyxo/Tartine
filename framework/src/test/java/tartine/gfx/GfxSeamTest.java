package tartine.gfx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tartine.gfx.testing.RecordingBackend;
import tartine.gfx.testing.RecordingBackend.FakeCanvas;
import tartine.gfx.testing.RecordingBackend.FakeFont;
import tartine.gfx.testing.RecordingBackend.FakeImage;
import tartine.gfx.testing.RecordingBackend.FakePicture;
import tartine.gfx.testing.RecordingBackend.FakePictureRecorder;
import tartine.gfx.testing.RecordingBackend.FakeSurface;

/**
 * Contract tests for the renderer seam: install lifecycle, clipping,
 * save/restore, draw forwarding with immutable paint snapshots, deterministic
 * font metrics, surface readback, and picture recording. Runs native-free
 * against {@link RecordingBackend}.
 */
class GfxSeamTest {

    private RecordingBackend backend;

    @BeforeEach
    void installBackend() {
        GfxBackend.uninstall();
        backend = new RecordingBackend();
        GfxBackend.install(backend);
    }

    @AfterEach
    void uninstallBackend() {
        GfxBackend.uninstall();
    }

    private FakeCanvas canvas() {
        return (FakeCanvas) backend.createSurface(100, 100).getCanvas();
    }

    @Test
    void getFailsLoudlyWithoutBackend() {
        GfxBackend.uninstall();

        IllegalStateException error = assertThrows(IllegalStateException.class,
                GfxBackend::get);

        assertTrue(error.getMessage().contains("no graphics backend installed"));
        assertFalse(GfxBackend.isInstalled());
    }

    @Test
    void installIsIdempotentForTheSameBackend() {
        GfxBackend.install(backend);

        assertSame(backend, GfxBackend.get());
        assertTrue(GfxBackend.isInstalled());
    }

    @Test
    void installRejectsASecondBackend() {
        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> GfxBackend.install(new RecordingBackend()));

        assertTrue(error.getMessage().contains("already installed"));
        assertSame(backend, GfxBackend.get());
    }

    @Test
    void uninstallReturnsTheInstalledBackend() {
        assertSame(backend, GfxBackend.uninstall());
        assertNull(GfxBackend.uninstall());
        assertFalse(GfxBackend.isInstalled());
    }

    @Test
    void clipRectIntersectUpdatesTheDeviceClip() {
        FakeCanvas canvas = canvas();

        assertTrue(canvas.clipRect(10, 10, 50, 50, GfxClipMode.INTERSECT));
        assertEquals(new GfxRect(10, 10, 50, 50), canvas.getDeviceClipBounds());

        assertTrue(canvas.clipRect(20, 20, 80, 80, GfxClipMode.INTERSECT));
        assertEquals(new GfxRect(20, 20, 50, 50), canvas.getDeviceClipBounds());

        assertFalse(canvas.clipRect(60, 60, 80, 80, GfxClipMode.INTERSECT));
        assertTrue(canvas.getDeviceClipBounds().isEmpty());
        assertEquals(List.of(
                "clipRect(10.0,10.0,50.0,50.0,INTERSECT)=true",
                "clipRect(20.0,20.0,80.0,80.0,INTERSECT)=true",
                "clipRect(60.0,60.0,80.0,80.0,INTERSECT)=false"),
                canvas.ops());
    }

    @Test
    void clipRectDifferenceRemovesCoveredArea() {
        FakeCanvas canvas = canvas();
        assertTrue(canvas.clipRect(0, 0, 100, 100, GfxClipMode.INTERSECT));

        assertFalse(canvas.clipRect(0, 0, 100, 100, GfxClipMode.DIFFERENCE));

        assertTrue(canvas.getDeviceClipBounds().isEmpty());
        assertEquals(List.of(
                "clipRect(0.0,0.0,100.0,100.0,INTERSECT)=true",
                "clipRect(0.0,0.0,100.0,100.0,DIFFERENCE)=false"),
                canvas.ops());
    }

    @Test
    void clipRectDifferenceLeavesAPartialClipNonEmpty() {
        FakeCanvas canvas = canvas();
        assertTrue(canvas.clipRect(0, 0, 100, 100, GfxClipMode.INTERSECT));

        // The recording fake keeps the clip unchanged for a partial
        // difference (an over-approximation); the contract under test is the
        // non-empty answer and the forwarded mode.
        assertTrue(canvas.clipRect(10, 10, 20, 20, GfxClipMode.DIFFERENCE));

        assertEquals(new GfxRect(0, 0, 100, 100), canvas.getDeviceClipBounds());
    }

    @Test
    void saveRestoreAndTransformsAreForwarded() {
        FakeCanvas canvas = canvas();
        assertEquals(0, canvas.getSaveDepth());

        canvas.save();
        canvas.save();
        assertEquals(2, canvas.getSaveDepth());

        canvas.translate(1, 2);
        canvas.rotate(90);
        canvas.scale(2, 3);
        canvas.skew(0.5f, 0.25f);

        canvas.restore();
        assertEquals(1, canvas.getSaveDepth());
        canvas.restore();
        assertEquals(0, canvas.getSaveDepth());
        canvas.restore();
        assertEquals(0, canvas.getSaveDepth());

        assertEquals(List.of(
                "save", "save",
                "translate(1.0,2.0)", "rotate(90.0)",
                "scale(2.0,3.0)", "skew(0.5,0.25)",
                "restore", "restore", "restore"),
                canvas.ops());
    }

    @Test
    void quickRejectUsesTheDeviceClip() {
        FakeCanvas canvas = canvas();
        canvas.clipRect(10, 10, 50, 50, GfxClipMode.INTERSECT);

        assertTrue(canvas.quickReject(0, 0, 5, 5));
        assertFalse(canvas.quickReject(20, 20, 30, 30));
        assertTrue(canvas.quickReject(60, 60, 80, 80));
    }

    @Test
    void drawsForwardImmutablePaintSnapshots() {
        FakeCanvas canvas = canvas();
        GfxPaint paint = new GfxPaint(0xFF336699, true,
                GfxPaintStyle.STROKE, 3.0f);

        canvas.drawColor(0xFF112233);
        canvas.drawRect(1, 2, 3, 4, paint);
        canvas.drawRoundRect(5, 6, 7, 8, 2, 3, paint);

        assertEquals(List.of(
                "drawColor(0xff112233)",
                "drawRect(1.0,2.0,3.0,4.0," + paint + ")",
                "drawRoundRect(5.0,6.0,7.0,8.0,2.0,3.0," + paint + ")"),
                canvas.ops());
    }

    @Test
    void drawTextForwardsFontAndPaint() {
        FakeCanvas canvas = canvas();
        GfxPaint paint = new GfxPaint(0xFF000000, false,
                GfxPaintStyle.FILL, 0.0f);
        FakeFont font = (FakeFont) backend.newFont(backend.typeface, 14.0f);

        canvas.drawText("abc", 1, 2, font, paint);
        assertFalse(font.isClosed());

        font.close();
        assertTrue(font.isClosed());
        assertEquals(List.of("drawText(abc,1.0,2.0," + font + "," + paint + ")"),
                canvas.ops());
    }

    @Test
    void drawPictureReplaysTheSealedDisplayList() {
        FakeCanvas target = canvas();
        GfxPaint paint = new GfxPaint(0xFF000000, false,
                GfxPaintStyle.FILL, 0.0f);

        backend.newPictureRecorder();
        FakePictureRecorder recorder = backend.lastRecorder;
        GfxCanvas recording = recorder.beginRecording(30, 40);
        recording.drawRect(1, 2, 3, 4, paint);
        GfxPicture picture = recorder.finishRecordingAsPicture();

        target.drawPicture(picture);

        assertEquals("FakePicture[30.0x40.0]", picture.toString());
        assertEquals(List.of("drawPicture(" + picture + ")"), target.ops());
    }

    @Test
    void pictureRecorderCapturesOperationsAndCloses() {
        GfxPaint paint = new GfxPaint(0xFF336699, true,
                GfxPaintStyle.FILL, 0.0f);
        backend.newPictureRecorder();
        FakePictureRecorder recorder = backend.lastRecorder;

        GfxCanvas recording = recorder.beginRecording(30, 40);
        recording.drawColor(0xFF112233);
        recording.drawRect(1, 2, 3, 4, paint);
        FakePicture picture = (FakePicture) recorder.finishRecordingAsPicture();
        recorder.close();

        assertEquals(List.of(
                "drawColor(0xff112233)",
                "drawRect(1.0,2.0,3.0,4.0," + paint + ")"), picture.ops());
        assertTrue(recorder.isClosed());
        assertTrue(backend.calls.contains("finishRecordingAsPicture"));
    }

    @Test
    void fontMetricsAndMeasurementsAreDeterministic() {
        FakeFont font = (FakeFont) backend.newFont(backend.typeface, 14.0f);

        assertEquals(new GfxFontMetrics(-17, -15, 4, 5, 0),
                font.getMetrics());
        assertEquals(18.0f, font.measureTextWidth("abc"));
        assertEquals(new GfxRect(0, -15, 18, 4), font.measureText("abc"));

        font.close();
        assertTrue(font.isClosed());
    }

    @Test
    void defaultTypefaceForwardsTheFamilyList() {
        assertSame(backend.typeface,
                backend.defaultTypeface(new String[]{"Roboto", "Segoe UI"}));
        assertTrue(backend.calls.contains(
                "defaultTypeface(Roboto,Segoe UI)"));
    }

    @Test
    void surfaceReadbackIsRowMajorArgb() {
        backend.createSurface(4, 2);
        FakeSurface surface = backend.lastSurface;

        assertEquals(4, surface.getWidth());
        assertEquals(2, surface.getHeight());
        int[] pixels = surface.readPixels();
        assertEquals(8, pixels.length);
        assertEquals(0xFF000000, pixels[0]);
        assertEquals(0xFF000007, pixels[7]);

        surface.close();
        assertTrue(surface.isClosed());
    }

    @Test
    void decodeImageReturnsPixelsAndCloses() {
        backend.decodeImage(new byte[]{1, 2, 3});
        FakeImage image = backend.lastImage;

        assertEquals(2, image.getWidth());
        assertEquals(2, image.getHeight());
        assertEquals(4, image.readPixels().length);

        image.close();
        assertTrue(image.isClosed());
    }
}
