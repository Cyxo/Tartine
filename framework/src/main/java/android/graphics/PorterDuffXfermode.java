package android.graphics;

/**
 * AOSP {@code android.graphics.PorterDuffXfermode}: a transfer mode backed by
 * a {@link PorterDuff.Mode}. The material shape pipeline installs one
 * ({@code DST_OUT}) on its clear paint to punch holes in a rasterized shape;
 * the software seam records the mode so the copy/read-back contract works,
 * while actual compositing waits for the rasterizer.
 */
public class PorterDuffXfermode extends Xfermode {

    private final PorterDuff.Mode porterDuffMode;

    /**
     * AOSP {@code PorterDuffXfermode(PorterDuff.Mode)}: stores the mode.
     *
     * @param mode the Porter-Duff operator
     */
    public PorterDuffXfermode(PorterDuff.Mode mode) {
        porterDuffMode = mode;
    }
}
