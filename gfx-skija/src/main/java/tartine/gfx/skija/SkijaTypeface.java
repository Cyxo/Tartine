package tartine.gfx.skija;

import io.github.humbleui.skija.Typeface;
import tartine.gfx.GfxTypeface;

/** Opaque Skija typeface handle. */
final class SkijaTypeface implements GfxTypeface {

    private final Typeface typeface;

    SkijaTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    Typeface typeface() {
        return typeface;
    }
}
