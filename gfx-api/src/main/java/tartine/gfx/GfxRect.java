package tartine.gfx;

/**
 * Axis-aligned rectangle in seam coordinates (floats, y down). Used for
 * device clip bounds and measured text bounds; no renderer type crosses the
 * seam.
 */
public final class GfxRect {

    public final float left;
    public final float top;
    public final float right;
    public final float bottom;

    public GfxRect(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    /** The empty rectangle at the origin. */
    public static GfxRect empty() {
        return new GfxRect(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public float width() {
        return right - left;
    }

    public float height() {
        return bottom - top;
    }

    public boolean isEmpty() {
        return left >= right || top >= bottom;
    }

    /** Whether {@code other} lies entirely inside this rectangle. */
    public boolean contains(GfxRect other) {
        return left <= other.left && top <= other.top
                && right >= other.right && bottom >= other.bottom;
    }

    /** Intersection, or an empty rectangle when the two do not overlap. */
    public GfxRect intersect(GfxRect other) {
        float l = Math.max(left, other.left);
        float t = Math.max(top, other.top);
        float r = Math.min(right, other.right);
        float b = Math.min(bottom, other.bottom);
        return l >= r || t >= b ? empty() : new GfxRect(l, t, r, b);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof GfxRect)) {
            return false;
        }
        GfxRect rect = (GfxRect) other;
        return Float.compare(left, rect.left) == 0
                && Float.compare(top, rect.top) == 0
                && Float.compare(right, rect.right) == 0
                && Float.compare(bottom, rect.bottom) == 0;
    }

    @Override
    public int hashCode() {
        int result = Float.floatToIntBits(left);
        result = 31 * result + Float.floatToIntBits(top);
        result = 31 * result + Float.floatToIntBits(right);
        result = 31 * result + Float.floatToIntBits(bottom);
        return result;
    }

    @Override
    public String toString() {
        return "GfxRect[" + left + ", " + top + ", " + right + ", " + bottom + "]";
    }
}
