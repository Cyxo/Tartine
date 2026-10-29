package tartine.gfx;

/**
 * Immutable snapshot of the draw state the framework's
 * {@code android.graphics.Paint} carries. A draw call takes a snapshot, so a
 * backend never retains a mutable framework paint.
 */
public final class GfxPaint {

    public final int color;
    public final boolean antiAlias;
    public final GfxPaintStyle style;
    public final float strokeWidth;

    public GfxPaint(int color, boolean antiAlias, GfxPaintStyle style,
            float strokeWidth) {
        this.color = color;
        this.antiAlias = antiAlias;
        this.style = style == null ? GfxPaintStyle.FILL : style;
        this.strokeWidth = strokeWidth;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof GfxPaint)) {
            return false;
        }
        GfxPaint paint = (GfxPaint) other;
        return color == paint.color
                && antiAlias == paint.antiAlias
                && style == paint.style
                && Float.compare(strokeWidth, paint.strokeWidth) == 0;
    }

    @Override
    public int hashCode() {
        int result = color;
        result = 31 * result + (antiAlias ? 1 : 0);
        result = 31 * result + style.hashCode();
        result = 31 * result + Float.floatToIntBits(strokeWidth);
        return result;
    }

    @Override
    public String toString() {
        return "GfxPaint[color=0x" + Integer.toHexString(color)
                + ", antiAlias=" + antiAlias + ", style=" + style
                + ", strokeWidth=" + strokeWidth + "]";
    }
}
