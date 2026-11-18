package android.graphics;

/**
 * AOSP {@code android.graphics.ColorFilter}: the base tag for paint color
 * filters. It carries no state of its own; a concrete filter
 * ({@link PorterDuffColorFilter}) holds the color and blend mode. The
 * software seam keeps the object for {@link Paint#getColorFilter()}
 * round-trips and for a future renderer to inspect.
 */
public class ColorFilter {

    /** AOSP has an implicit no-argument constructor. */
    public ColorFilter() {
    }
}
