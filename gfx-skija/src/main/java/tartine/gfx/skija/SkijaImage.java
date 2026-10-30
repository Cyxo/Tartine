package tartine.gfx.skija;

import io.github.humbleui.skija.Bitmap;
import io.github.humbleui.skija.Image;
import io.github.humbleui.skija.Pixmap;
import java.nio.ByteBuffer;
import tartine.gfx.GfxImage;

/**
 * Skija-decoded image. The underlying {@link Image} owns the decoded pixels
 * and is released on {@link #close()}; {@link #readPixels()} converts the
 * premultiplied N32 bytes to straight-alpha ARGB for icon compositing.
 */
final class SkijaImage implements GfxImage {

    private final Image image;

    SkijaImage(Image image) {
        this.image = image;
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }

    @Override
    public int[] readPixels() {
        int width = getWidth();
        int height = getHeight();
        Bitmap bitmap = new Bitmap();
        try {
            bitmap.allocN32Pixels(width, height, false);
            if (!image.readPixels(bitmap, 0, 0)) {
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
                    int b = buffer.get(base) & 0xFF;
                    int g = buffer.get(base + 1) & 0xFF;
                    int r = buffer.get(base + 2) & 0xFF;
                    int a = buffer.get(base + 3) & 0xFF;
                    // N32 stores premultiplied; icons want straight alpha.
                    if (a != 0 && a != 255) {
                        r = Math.min(255, Math.round(r * 255f / a));
                        g = Math.min(255, Math.round(g * 255f / a));
                        b = Math.min(255, Math.round(b * 255f / a));
                    }
                    out[i] = (a << 24) | (r << 16) | (g << 8) | b;
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
        image.close();
    }
}
