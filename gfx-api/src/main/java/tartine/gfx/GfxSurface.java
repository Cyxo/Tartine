package tartine.gfx;

/**
 * An offscreen raster surface plus the canvas that draws into it. The host
 * owns the surface and closes it when the window goes away.
 */
public interface GfxSurface extends AutoCloseable {

    /** The canvas bound to this surface. Never {@code null}. */
    GfxCanvas getCanvas();

    int getWidth();

    int getHeight();

    /**
     * Reads the surface back as packed ARGB ints in row-major order.
     * Premultiplied alpha, matching the frame-buffer path that blits it.
     */
    int[] readPixels();

    @Override
    void close();
}
