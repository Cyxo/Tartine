package tartine.gfx.skija;

import io.github.humbleui.skija.Picture;
import tartine.gfx.GfxPicture;

/** Opaque Skija picture handle. */
final class SkijaPicture implements GfxPicture {

    private final Picture picture;

    SkijaPicture(Picture picture) {
        this.picture = picture;
    }

    Picture picture() {
        return picture;
    }
}
