package tartine.gfx.skija;

import io.github.humbleui.skija.FontMgr;
import io.github.humbleui.skija.FontStyle;
import io.github.humbleui.skija.Image;
import io.github.humbleui.skija.Surface;
import io.github.humbleui.skija.Typeface;
import tartine.gfx.GfxBackend;
import tartine.gfx.GfxCanvas;
import tartine.gfx.GfxFont;
import tartine.gfx.GfxImage;
import tartine.gfx.GfxPictureRecorder;
import tartine.gfx.GfxSurface;
import tartine.gfx.GfxTypeface;

/**
 * Skija backend: the process-wide adapter installed at boot. Every native
 * object it creates is wrapped in a seam type; the framework never sees a
 * {@code io.github.humbleui} type.
 */
public final class SkijaBackend implements GfxBackend {

    public SkijaBackend() {
    }

    @Override
    public GfxSurface createSurface(int width, int height) {
        return new SkijaSurface(Surface.makeRasterN32Premul(width, height));
    }

    @Override
    public GfxPictureRecorder newPictureRecorder() {
        return new SkijaPictureRecorder();
    }

    @Override
    public GfxTypeface defaultTypeface(String[] preferredFamilies) {
        if (preferredFamilies == null) {
            return null;
        }
        FontMgr manager = FontMgr.getDefault();
        for (String family : preferredFamilies) {
            Typeface found = manager.matchFamilyStyle(family,
                    FontStyle.NORMAL);
            if (found != null) {
                return new SkijaTypeface(found);
            }
        }
        return null;
    }

    @Override
    public GfxFont newFont(GfxTypeface typeface, float size) {
        if (!(typeface instanceof SkijaTypeface)) {
            throw new IllegalArgumentException(
                    "Tartine: SkijaBackend.newFont requires a SkijaTypeface");
        }
        return new SkijaFont(((SkijaTypeface) typeface).typeface(), size);
    }

    @Override
    public GfxImage decodeImage(byte[] encoded) {
        if (encoded == null) {
            return null;
        }
        Image image = Image.makeFromEncoded(encoded);
        return image == null ? null : new SkijaImage(image);
    }

    /**
     * Host bridge for a surface the caller already owns: wraps its canvas in
     * the seam without taking ownership of the surface. Removed once the host
     * creates surfaces through {@link #createSurface(int, int)}.
     */
    public static GfxCanvas canvasFor(Surface surface) {
        return new SkijaCanvas(surface.getCanvas(),
                surface.getWidth(), surface.getHeight());
    }
}
