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

    /**
     * Reads the surface back into {@code out}, which must hold exactly
     * {@code width * height} ints. A render loop reuses one array for every
     * frame, so this overload is the allocation-free readback path; it must
     * not retain {@code out}.
     *
     * <p>The default copies from {@link #readPixels()}; implementations that
     * can read straight into the caller's array (and reuse their own native
     * readback storage) should override it.
     */
    default void readPixels(int[] out) {
        if (out == null) {
            throw new NullPointerException("out");
        }
        int[] pixels = readPixels();
        if (out.length != pixels.length) {
            throw new IllegalArgumentException("readback buffer must be "
                    + pixels.length + " ints, got " + out.length);
        }
        System.arraycopy(pixels, 0, out, 0, pixels.length);
    }

    @Override
    void close();
}
