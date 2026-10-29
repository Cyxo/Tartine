package tartine.gfx;

/**
 * Font metrics at the seam: the same five values Android's
 * {@code Paint.FontMetrics} exposes, with plain float fields.
 */
public final class GfxFontMetrics {

    public final float top;
    public final float ascent;
    public final float descent;
    public final float bottom;
    public final float leading;

    public GfxFontMetrics(float top, float ascent, float descent,
            float bottom, float leading) {
        this.top = top;
        this.ascent = ascent;
        this.descent = descent;
        this.bottom = bottom;
        this.leading = leading;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof GfxFontMetrics)) {
            return false;
        }
        GfxFontMetrics metrics = (GfxFontMetrics) other;
        return Float.compare(top, metrics.top) == 0
                && Float.compare(ascent, metrics.ascent) == 0
                && Float.compare(descent, metrics.descent) == 0
                && Float.compare(bottom, metrics.bottom) == 0
                && Float.compare(leading, metrics.leading) == 0;
    }

    @Override
    public int hashCode() {
        int result = Float.floatToIntBits(top);
        result = 31 * result + Float.floatToIntBits(ascent);
        result = 31 * result + Float.floatToIntBits(descent);
        result = 31 * result + Float.floatToIntBits(bottom);
        result = 31 * result + Float.floatToIntBits(leading);
        return result;
    }

    @Override
    public String toString() {
        return "GfxFontMetrics[top=" + top + ", ascent=" + ascent
                + ", descent=" + descent + ", bottom=" + bottom
                + ", leading=" + leading + "]";
    }
}
