package tartine.gfx;

/**
 * A backend font at a fixed typeface and size.
 *
 * <p>A font owns native resources in most backends, so it is
 * {@link AutoCloseable}. {@link #close()} must be idempotent. Framework code
 * closes transient fonts and caches long-lived ones (see the Paint font
 * cache).
 */
public interface GfxFont extends AutoCloseable {

    /** Metrics for this font. Never {@code null}. */
    GfxFontMetrics getMetrics();

    /** Advance width of {@code text} in pixels. */
    float measureTextWidth(String text);

    /**
     * Tight bounding box of {@code text} relative to the baseline origin
     * (y-up values are negative, matching Android's text bounds).
     */
    GfxRect measureText(String text);

    @Override
    void close();
}
