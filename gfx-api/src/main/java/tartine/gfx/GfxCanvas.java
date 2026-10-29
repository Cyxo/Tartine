package tartine.gfx;

/**
 * A 2D draw target. The framework's {@code android.graphics.Canvas} delegates
 * every ported operation here.
 *
 * <p>The save/restore stack and transform are backend state; the framework
 * tracks its own save count for the AOSP-visible contract. All coordinates are
 * floats and all colors are packed ARGB ints, so no renderer type crosses the
 * seam.
 */
public interface GfxCanvas {

    /** Width of the target in pixels, or 0 when it has no device. */
    int getWidth();

    /** Height of the target in pixels, or 0 when it has no device. */
    int getHeight();

    void save();

    void restore();

    void translate(float dx, float dy);

    void rotate(float degrees);

    void scale(float sx, float sy);

    void skew(float sx, float sy);

    /**
     * Applies a rectangular clip and reports whether the resulting device
     * clip is non-empty (AOSP's {@code nClipRect} answer).
     */
    boolean clipRect(float left, float top, float right, float bottom,
            GfxClipMode mode);

    /** Current device clip bounds; never {@code null}. */
    GfxRect getDeviceClipBounds();

    /**
     * Whether the rectangle cannot intersect the current device clip
     * (Skia's conservative quick-reject test).
     */
    boolean quickReject(float left, float top, float right, float bottom);

    void drawColor(int color);

    void drawRect(float left, float top, float right, float bottom,
            GfxPaint paint);

    void drawRoundRect(float left, float top, float right, float bottom,
            float rx, float ry, GfxPaint paint);

    /** Draws {@code text} with its baseline origin at ({@code x}, {@code y}). */
    void drawText(String text, float x, float y, GfxFont font, GfxPaint paint);

    void drawPicture(GfxPicture picture);
}
