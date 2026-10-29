package tartine.gfx.skija;

import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.ClipMode;
import io.github.humbleui.types.IRect;
import io.github.humbleui.types.Rect;
import tartine.gfx.GfxCanvas;
import tartine.gfx.GfxClipMode;
import tartine.gfx.GfxFont;
import tartine.gfx.GfxPaint;
import tartine.gfx.GfxPicture;
import tartine.gfx.GfxRect;

/** Skija implementation of the canvas seam. */
final class SkijaCanvas implements GfxCanvas {

    private final Canvas canvas;
    private final int width;
    private final int height;

    SkijaCanvas(Canvas canvas, int width, int height) {
        this.canvas = canvas;
        this.width = width;
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void save() {
        canvas.save();
    }

    @Override
    public void restore() {
        canvas.restore();
    }

    @Override
    public void translate(float dx, float dy) {
        canvas.translate(dx, dy);
    }

    @Override
    public void rotate(float degrees) {
        canvas.rotate(degrees);
    }

    @Override
    public void scale(float sx, float sy) {
        canvas.scale(sx, sy);
    }

    @Override
    public void skew(float sx, float sy) {
        canvas.skew(sx, sy);
    }

    @Override
    public boolean clipRect(float left, float top, float right, float bottom,
            GfxClipMode mode) {
        canvas.clipRect(SkijaMappings.rect(left, top, right, bottom),
                SkijaMappings.clipMode(mode));
        IRect bounds = canvas.getDeviceClipBounds();
        return bounds != null
                && bounds.getLeft() < bounds.getRight()
                && bounds.getTop() < bounds.getBottom();
    }

    @Override
    public GfxRect getDeviceClipBounds() {
        return SkijaMappings.rect(canvas.getDeviceClipBounds());
    }

    @Override
    public boolean quickReject(float left, float top, float right,
            float bottom) {
        return canvas.quickReject(
                SkijaMappings.rect(left, top, right, bottom));
    }

    @Override
    public void drawColor(int color) {
        canvas.drawColor(color);
    }

    @Override
    public void drawRect(float left, float top, float right, float bottom,
            GfxPaint paint) {
        try (SkijaPaint skijaPaint = new SkijaPaint(paint)) {
            canvas.drawRect(
                    SkijaMappings.rect(left, top, right, bottom),
                    skijaPaint.paint());
        }
    }

    @Override
    public void drawRoundRect(float left, float top, float right,
            float bottom, float rx, float ry, GfxPaint paint) {
        try (SkijaPaint skijaPaint = new SkijaPaint(paint)) {
            canvas.drawRRect(
                    SkijaMappings.rrect(left, top, right, bottom, rx, ry),
                    skijaPaint.paint());
        }
    }

    @Override
    public void drawText(String text, float x, float y, GfxFont font,
            GfxPaint paint) {
        if (!(font instanceof SkijaFont)) {
            throw new IllegalArgumentException(
                    "Tartine: SkijaCanvas requires a SkijaFont");
        }
        try (SkijaPaint skijaPaint = new SkijaPaint(paint)) {
            canvas.drawString(text, x, y,
                    ((SkijaFont) font).font(), skijaPaint.paint());
        }
    }

    @Override
    public void drawPicture(GfxPicture picture) {
        if (!(picture instanceof SkijaPicture)) {
            throw new IllegalArgumentException(
                    "Tartine: SkijaCanvas requires a SkijaPicture");
        }
        canvas.drawPicture(((SkijaPicture) picture).picture());
    }
}
