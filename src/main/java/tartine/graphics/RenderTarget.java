package tartine.graphics;

import tartine.gfx.GfxSurface;

/**
 * The guest's current raster target. The host window's framebuffer size is
 * the source of truth for layout, so a window resize replaces the surface
 * with one of the new pixel size; the render loop therefore asks for the
 * current {@link GfxSurface} every tick instead of caching it. When the
 * identity or size changes, the loop reallocates its readback buffer and
 * forces one redraw.
 *
 * <p>The target is owned by the guest render loop and closed when the loop
 * stops.
 */
public interface RenderTarget extends AutoCloseable {

    /** The current surface; never {@code null} while the target is open. */
    GfxSurface surface();

    @Override
    void close();
}
