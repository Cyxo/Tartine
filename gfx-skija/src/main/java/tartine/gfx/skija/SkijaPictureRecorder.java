package tartine.gfx.skija;

import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.PictureRecorder;
import io.github.humbleui.types.Rect;
import tartine.gfx.GfxCanvas;
import tartine.gfx.GfxPicture;
import tartine.gfx.GfxPictureRecorder;

/** Skija display-list recorder. */
final class SkijaPictureRecorder implements GfxPictureRecorder {

    private final PictureRecorder recorder = new PictureRecorder();

    @Override
    public GfxCanvas beginRecording(float width, float height) {
        Canvas canvas = recorder.beginRecording(
                Rect.makeXYWH(0.0f, 0.0f, width, height));
        return new SkijaCanvas(canvas, (int) width, (int) height);
    }

    @Override
    public GfxPicture finishRecordingAsPicture() {
        return new SkijaPicture(recorder.finishRecordingAsPicture());
    }

    @Override
    public void close() {
        recorder.close();
    }
}
