package android.graphics;

/**
 * AOSP {@code android.graphics.Xfermode}: the base tag for paint transfer
 * modes. It carries no state of its own; a concrete mode
 * ({@link PorterDuffXfermode}, {@code BlendModeColorFilter} neighbors) holds
 * the actual operator. The software seam keeps the object for
 * {@link Paint#getXfermode()} round-trips and for a future renderer to
 * inspect.
 */
public class Xfermode {

    /** AOSP has an implicit no-argument constructor. */
    public Xfermode() {
    }
}
