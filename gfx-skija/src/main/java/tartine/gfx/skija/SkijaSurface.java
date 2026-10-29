package tartine.gfx.skija;

import java.nio.ByteBuffer;

import io.github.humbleui.skija.Bitmap;
import io.github.humbleui.skija.Pixmap;
import io.github.humbleui.skija.Surface;
import tartine.gfx.GfxCanvas;
import tartine.gfx.GfxSurface;

/** Skija raster surface plus its canvas and ARGB readback. */
final class SkijaSurface implements GfxSurface {

    private final Surface surface;
    private final SkijaCanvas canvas;

    SkijaSurface(Surface surface) {
        this.surface = surface;
        this.canvas = new SkijaCanvas(surface.getCanvas(),
                surface.getWidth(), surface.getHeight());
    }

    @Override
    public GfxCanvas getCanvas() {
        return canvas;
    }

    @Override
    public int getWidth() {
        return surface.getWidth();
    }

    @Override
    public int getHeight() {
        return surface.getHeight();
    }

    @Override
    public int[] readPixels() {
        int width = getWidth();
        int height = getHeight();
        Bitmap bitmap = new Bitmap();
        try {
            bitmap.allocN32Pixels(width, height, false);
            if (!surface.readPixels(bitmap, 0, 0)) {
                throw new IllegalStateException("readPixels failed");
            }
            Pixmap pixmap = bitmap.peekPixels();
            if (pixmap == null) {
                throw new IllegalStateException("peekPixels failed");
            }
            try {
                ByteBuffer buffer = pixmap.getBuffer();
                int[] out = new int[width * height];
                for (int i = 0; i < out.length; i++) {
                    int base = i * 4;
                    int bb = buffer.get(base) & 0xFF;
                    int gg = buffer.get(base + 1) & 0xFF;
                    int rr = buffer.get(base + 2) & 0xFF;
                    int aa = buffer.get(base + 3) & 0xFF;
                    out[i] = (aa << 24) | (rr << 16) | (gg << 8) | bb;
                }
                return out;
            } finally {
                pixmap.close();
            }
        } finally {
            bitmap.close();
        }
    }

    @Override
    public void close() {
        surface.close();
    }
}
