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

    SkijaFont(Typeface typeface, float size) {
        font = new Font(typeface, size);
        font.setSubpixel(true);
        font.setEdging(FontEdging.SUBPIXEL_ANTI_ALIAS);
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
        return font.measureTextWidth(text);
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
