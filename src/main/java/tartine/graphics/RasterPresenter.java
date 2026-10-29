package tartine.graphics;

import tartine.gfx.GfxBackend;
import tartine.gfx.GfxSurface;

/**
 * SurfaceFlinger-side raster host: creates the renderer surface one Android
 * Window draws into and binds it to a framework
 * {@link android.graphics.Canvas}. The installed {@link GfxBackend} owns the
 * renderer, so this class never imports a renderer library; the host installs
 * {@code tartine.gfx.skija.SkijaBackend} once at boot (see
 * {@code tartine.Main}).
 */
public final class RasterPresenter {

    private RasterPresenter() {
    }

    /** Creates a 480x960-style software frame surface. */
    public static GfxSurface createSurface(int width, int height) {
        return GfxBackend.get().createSurface(width, height);
    }

    /**
     * Mints a framework canvas bound to the given surface (host renderer
     * path; on-device ViewRootImpl/HardwareRenderer plays this role).
     */
    public static android.graphics.Canvas createSurfaceCanvas(GfxSurface surface) {
        return android.graphics.Canvas.tartineCreate(surface.getCanvas());
    }
}
