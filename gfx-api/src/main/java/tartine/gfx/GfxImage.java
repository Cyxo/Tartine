package tartine.gfx;

/**
 * A decoded image. Unlike a surface, this is source content: its pixels are
 * straight-alpha ARGB, ready for icon compositing and other host-side use.
 */
public interface GfxImage extends AutoCloseable {

    int getWidth();

    int getHeight();

    /** Pixels in row-major order as straight-alpha packed ARGB ints. */
    int[] readPixels();

    @Override
    void close();
}
