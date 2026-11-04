package tartine.gfx.skija;

import io.github.humbleui.skija.Data;
import io.github.humbleui.skija.FontMgr;
import io.github.humbleui.skija.FontStyle;
import io.github.humbleui.skija.Image;
import io.github.humbleui.skija.Surface;
import io.github.humbleui.skija.Typeface;
import java.io.IOException;
import java.io.InputStream;
import tartine.gfx.GfxBackend;
import tartine.gfx.GfxFont;
import tartine.gfx.GfxImage;
import tartine.gfx.GfxPictureRecorder;
import tartine.gfx.GfxSurface;
import tartine.gfx.GfxTypeface;

/**
 * Skija backend: the process-wide adapter installed at boot. Every native
 * object it creates is wrapped in a seam type; the framework never sees a
 * {@code io.github.humbleui} type.
 */
public final class SkijaBackend implements GfxBackend {

    /**
     * Bundled fallback face: Roboto 2.138 (Apache-2.0, see THIRD-PARTY.md),
     * used when none of the requested host families exists. Keeping a real
     * face on the classpath means a text draw never fails just because the
     * machine has none of the configured desktop fonts.
     */
    static final String FALLBACK_FONT_RESOURCE =
            "/tartine/fonts/Roboto-Regular.ttf";

    /** Loaded once and shared; typefaces live for the process lifetime. */
    private static Typeface fallbackTypeface;

    public SkijaBackend() {
    }

    @Override
    public GfxSurface createSurface(int width, int height) {
        return new SkijaSurface(Surface.makeRasterN32Premul(width, height));
    }

    @Override
    public GfxPictureRecorder newPictureRecorder() {
        return new SkijaPictureRecorder();
    }

    @Override
    public GfxTypeface defaultTypeface(String[] preferredFamilies) {
        FontMgr manager = FontMgr.getDefault();
        if (preferredFamilies != null) {
            for (String family : preferredFamilies) {
                if (family == null || family.isBlank()) {
                    continue;
                }
                Typeface found = manager.matchFamilyStyle(family,
                        FontStyle.NORMAL);
                if (found != null) {
                    return new SkijaTypeface(found);
                }
            }
        }
        return new SkijaTypeface(fallbackTypeface());
    }

    /** Loads the bundled Roboto face once; fails loudly if it is broken. */
    private static synchronized Typeface fallbackTypeface() {
        if (fallbackTypeface == null) {
            byte[] bytes;
            try (InputStream in = SkijaBackend.class.getResourceAsStream(
                    FALLBACK_FONT_RESOURCE)) {
                if (in == null) {
                    throw new IllegalStateException(
                            "Tartine: bundled fallback font is missing: "
                                    + FALLBACK_FONT_RESOURCE);
                }
                bytes = in.readAllBytes();
            } catch (IOException failure) {
                throw new IllegalStateException(
                        "Tartine: cannot read bundled fallback font: "
                                + FALLBACK_FONT_RESOURCE, failure);
            }
            Typeface loaded = FontMgr.getDefault()
                    .makeFromData(Data.makeFromBytes(bytes));
            if (loaded == null) {
                throw new IllegalStateException(
                        "Tartine: bundled fallback font is not a readable "
                                + "typeface: " + FALLBACK_FONT_RESOURCE);
            }
            fallbackTypeface = loaded;
        }
        return fallbackTypeface;
    }

    @Override
    public GfxFont newFont(GfxTypeface typeface, float size) {
        if (!(typeface instanceof SkijaTypeface)) {
            throw new IllegalArgumentException(
                    "Tartine: SkijaBackend.newFont requires a SkijaTypeface");
        }
        return new SkijaFont(((SkijaTypeface) typeface).typeface(), size);
    }

    @Override
    public GfxImage decodeImage(byte[] encoded) {
        if (encoded == null) {
            return null;
        }
        Image image = Image.makeFromEncoded(encoded);
        return image == null ? null : new SkijaImage(image);
    }
}
