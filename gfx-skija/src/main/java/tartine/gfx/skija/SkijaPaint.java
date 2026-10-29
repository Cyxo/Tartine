package tartine.gfx.skija;

import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.PaintMode;
import tartine.gfx.GfxPaint;
import tartine.gfx.GfxPaintStyle;

/**
 * Transient Skija paint built from an immutable seam snapshot. Closed after
 * each draw: Skia copies the paint state into the draw operation, so the
 * native object has no reason to outlive the call.
 */
final class SkijaPaint implements AutoCloseable {

    private final Paint paint = new Paint();

    SkijaPaint(GfxPaint source) {
        paint.setColor(source.color);
        paint.setAntiAlias(source.antiAlias);
        switch (source.style) {
            case STROKE:
                paint.setMode(PaintMode.STROKE);
                break;
            case FILL_AND_STROKE:
                paint.setMode(PaintMode.STROKE_AND_FILL);
                break;
            default:
                paint.setMode(PaintMode.FILL);
                break;
        }
        if (source.strokeWidth > 0.0f) {
            paint.setStrokeWidth(source.strokeWidth);
        }
    }

    Paint paint() {
        return paint;
    }

    @Override
    public void close() {
        paint.close();
    }
}
