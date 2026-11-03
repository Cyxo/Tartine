package tartine.gfx.skija;

import io.github.humbleui.skija.Bitmap;
import io.github.humbleui.skija.Pixmap;
import io.github.humbleui.skija.Surface;
import java.nio.ByteBuffer;
import tartine.gfx.GfxCanvas;
import tartine.gfx.GfxSurface;

/**
 * Skija raster surface plus its canvas and ARGB readback. The native bitmap
 * used for readback is allocated once and reused, so a per-frame
 * {@link #readPixels(int[])} costs one copy into the caller's buffer.
 */
final class SkijaSurface implements GfxSurface {

    private final Surface surface;
    private final SkijaCanvas canvas;
    /** Native readback storage; allocated on the first read, reused after. */
    private Bitmap readback;

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
        int[] out = new int[getWidth() * getHeight()];
        readPixels(out);
        return out;
    }

    @Override
    public void readPixels(int[] out) {
        int width = getWidth();
        int height = getHeight();
        if (out == null) {
            throw new NullPointerException("out");
        }
        if (out.length != width * height) {
            throw new IllegalArgumentException("readback buffer must be "
                    + (width * height) + " ints, got " + out.length);
        }
        if (readback == null) {
            readback = new Bitmap();
            readback.allocN32Pixels(width, height, false);
        }
        if (!surface.readPixels(readback, 0, 0)) {
            throw new IllegalStateException("readPixels failed");
        }
        Pixmap pixmap = readback.peekPixels();
        if (pixmap == null) {
            throw new IllegalStateException("peekPixels failed");
        }
        try {
            ByteBuffer buffer = pixmap.getBuffer();
            for (int i = 0; i < out.length; i++) {
                int base = i * 4;
                int bb = buffer.get(base) & 0xFF;
                int gg = buffer.get(base + 1) & 0xFF;
                int rr = buffer.get(base + 2) & 0xFF;
                int aa = buffer.get(base + 3) & 0xFF;
                out[i] = (aa << 24) | (rr << 16) | (gg << 8) | bb;
            }
        } finally {
            pixmap.close();
        }
    }

    @Override
    public void close() {
        Bitmap bitmap = readback;
        readback = null;
        if (bitmap != null) {
            bitmap.close();
        }
        surface.close();
    }
}
