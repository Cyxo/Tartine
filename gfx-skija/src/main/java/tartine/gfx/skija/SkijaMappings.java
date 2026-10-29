package tartine.gfx.skija;

import io.github.humbleui.skija.ClipMode;
import io.github.humbleui.types.IRect;
import io.github.humbleui.types.RRect;
import io.github.humbleui.types.Rect;
import tartine.gfx.GfxClipMode;
import tartine.gfx.GfxRect;

/** Conversions between seam primitives and Skija types. */
final class SkijaMappings {

    private SkijaMappings() {
    }

    static Rect rect(float left, float top, float right, float bottom) {
        return Rect.makeLTRB(left, top, right, bottom);
    }

    static RRect rrect(float left, float top, float right, float bottom,
            float rx, float ry) {
        return RRect.makeLTRB(left, top, right, bottom, rx, ry);
    }

    static ClipMode clipMode(GfxClipMode mode) {
        return mode == GfxClipMode.DIFFERENCE
                ? ClipMode.DIFFERENCE : ClipMode.INTERSECT;
    }

    static GfxRect rect(IRect rect) {
        return new GfxRect(rect.getLeft(), rect.getTop(),
                rect.getRight(), rect.getBottom());
    }

    static GfxRect rect(Rect rect) {
        return new GfxRect(rect._left, rect._top, rect._right, rect._bottom);
    }
}
