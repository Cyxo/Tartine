package android.graphics;

/**
 * AOSP {@code android.graphics.PorterDuffColorFilter}: tints source pixels
 * with one color through a {@link PorterDuff.Mode}. Guests (drawable tinting,
 * tile color filtering) construct these and hand them to
 * {@link Paint#setColorFilter}; the software seam keeps the pair and answers
 * the value-equality contract, while tint compositing waits for the
 * rasterizer.
 */
public class PorterDuffColorFilter extends ColorFilter {

    private final int color;
    private final PorterDuff.Mode mode;

    /**
     * AOSP {@code PorterDuffColorFilter(int, PorterDuff.Mode)}.
     *
     * @param color the ARGB tint
     * @param mode the Porter-Duff operator
     */
    public PorterDuffColorFilter(int color, PorterDuff.Mode mode) {
        this.color = color;
        this.mode = mode;
    }

    /** AOSP: two filters are equal when color and mode match. */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        PorterDuffColorFilter other = (PorterDuffColorFilter) object;
        return color == other.color && mode == other.mode;
    }

    /** AOSP: hash joined from the mode and color. */
    @Override
    public int hashCode() {
        return 31 * mode.hashCode() + color;
    }
}
