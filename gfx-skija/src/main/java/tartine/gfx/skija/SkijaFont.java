package tartine.gfx.skija;

import io.github.humbleui.skija.Font;
import io.github.humbleui.skija.FontEdging;
import io.github.humbleui.skija.FontMetrics;
import io.github.humbleui.skija.Typeface;
import tartine.gfx.GfxFont;
import tartine.gfx.GfxFontMetrics;
import tartine.gfx.GfxRect;

/**
 * Skija font: owns one native {@link Font} for a fixed typeface and size and
 * releases it on {@link #close()}. The framework's Paint caches this object,
 * so repeated measurements reuse the native font instead of allocating a new
 * one per call.
 */
final class SkijaFont implements GfxFont {

    private final Font font;
    private final boolean subpixel;

    SkijaFont(Typeface typeface, float size, boolean subpixel) {
        this.subpixel = subpixel;
        font = new Font(typeface, size);
        // AOSP stores SUBPIXEL_TEXT_FLAG in the native SkFont and uses it for
        // both positioning and the edging fallback; mirror it here so the
        // draw path and the measurement path agree.
        font.setSubpixel(subpixel);
        font.setEdging(subpixel ? FontEdging.SUBPIXEL_ANTI_ALIAS
                : FontEdging.ANTI_ALIAS);
    }

    Font font() {
        return font;
    }

    @Override
    public GfxFontMetrics getMetrics() {
        FontMetrics metrics = font.getMetrics();
        return new GfxFontMetrics(metrics._top, metrics._ascent,
                metrics._descent, metrics._bottom, metrics._leading);
    }

    @Override
    public float measureTextWidth(String text) {
        if (subpixel) {
            return font.measureTextWidth(text);
        }
        // Subpixel positioning off: AOSP (Minikin) rounds each glyph advance
        // to an integer before summing, so measureText("ab") is
        // round(w(a)) + round(w(b)), not round(w(a) + w(b)). Skia's
        // setSubpixel(false) does not change measureTextWidth, so round the
        // shaped advances explicitly.
        short[] glyphs = font.getStringGlyphs(text);
        float[] widths = font.getWidths(glyphs);
        float total = 0.0f;
        for (float width : widths) {
            total += Math.round(width);
        }
        return total;
    }

    @Override
    public GfxRect measureText(String text) {
        return SkijaMappings.rect(font.measureText(text));
    }

    @Override
    public void close() {
        font.close();
    }
}
