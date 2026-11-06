package tartine.graphics;

import android.graphics.Canvas;
import tartine.gfx.GfxSurface;

/**
 * The guest window's raster target: one {@link GfxSurface} plus the
 * framework {@link Canvas} that draws into it. A host resize replaces the
 * pair on the guest thread; the surface and canvas always change together,
 * so the draw path can never keep drawing into the old pixel size.
 *
 * <p>Guest-thread only: the host posts {@code Resized} events to the guest
 * looper, and the drain that handles them also runs the render loop.
 */
public final class GuestSurface implements RenderTarget {

    private GfxSurface surface;
    private Canvas canvas;
    private int width;
    private int height;

    /** Creates the initial surface at the host window's pixel size. */
    public GuestSurface(int width, int height) {
        open(width, height);
    }

    @Override
    public GfxSurface surface() {
        return surface;
    }

    /** The canvas bound to the current surface. */
    public Canvas canvas() {
        return canvas;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    /**
     * Replaces the surface when the pixel viewport changed. Returns
     * {@code false} for a duplicate resize event, so replays and repeated
     * GLFW callbacks stay cheap.
     */
    public boolean resize(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException(
                    "viewport must be positive: " + width + "x" + height);
        }
        if (width == this.width && height == this.height) {
            return false;
        }
        GfxSurface next = RasterPresenter.createSurface(width, height);
        Canvas nextCanvas;
        try {
            nextCanvas = RasterPresenter.createSurfaceCanvas(next);
        } catch (RuntimeException failure) {
            next.close();
            throw failure;
        }
        final GfxSurface previous = surface;
        surface = next;
        canvas = nextCanvas;
        this.width = width;
        this.height = height;
        if (previous != null) {
            previous.close();
        }
        return true;
    }

    private void open(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException(
                    "viewport must be positive: " + width + "x" + height);
        }
        GfxSurface created = RasterPresenter.createSurface(width, height);
        Canvas createdCanvas;
        try {
            createdCanvas = RasterPresenter.createSurfaceCanvas(created);
        } catch (RuntimeException failure) {
            created.close();
            throw failure;
        }
        surface = created;
        canvas = createdCanvas;
        this.width = width;
        this.height = height;
    }

    /** Closes the current surface; safe to call more than once. */
    @Override
    public void close() {
        GfxSurface current = surface;
        surface = null;
        canvas = null;
        if (current != null) {
            current.close();
        }
    }
}
