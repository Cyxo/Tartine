package tartine.gfx;

/**
 * Renderer seam: the only thing the {@code android.*} framework knows about
 * the 2D backend.
 *
 * <p>The host installs one backend at boot ({@link #install(GfxBackend)}) and
 * the framework resolves it lazily through {@link #get()}. Nothing in the
 * framework imports a renderer library; the backend implementation (for
 * example {@code gfx-skija}) is the only place that does.
 *
 * <p>All geometry crosses the seam as primitives and all renderer objects are
 * opaque markers, so a backend swap never leaks renderer types into the
 * framework. Add methods here only when a framework call site or a test needs
 * them.
 */
public interface GfxBackend {

    /**
     * Installs the process-wide backend. Intended to run once at boot; a
     * second, different backend is rejected rather than silently replacing
     * the first. Re-installing the same instance is a no-op.
     */
    static void install(GfxBackend backend) {
        if (backend == null) {
            throw new NullPointerException("backend");
        }
        synchronized (GfxBackendHolder.class) {
            GfxBackend current = GfxBackendHolder.installed;
            if (current != null && current != backend) {
                throw new IllegalStateException(
                        "Tartine: a graphics backend is already installed: " + current);
            }
            GfxBackendHolder.installed = backend;
        }
    }

    /**
     * Returns the installed backend, or fails loudly when boot forgot to
     * install one. There is deliberately no fallback: a silent no-op renderer
     * hides the wiring bug until pixels go missing.
     */
    static GfxBackend get() {
        GfxBackend backend = GfxBackendHolder.installed;
        if (backend == null) {
            throw new IllegalStateException(
                    "Tartine: no graphics backend installed;"
                            + " call GfxBackend.install(...) at boot");
        }
        return backend;
    }

    /** Whether a backend has been installed. */
    static boolean isInstalled() {
        return GfxBackendHolder.installed != null;
    }

    /**
     * Removes and returns the installed backend (tests and host shutdown).
     * Returns {@code null} when nothing was installed.
     */
    static GfxBackend uninstall() {
        synchronized (GfxBackendHolder.class) {
            GfxBackend previous = GfxBackendHolder.installed;
            GfxBackendHolder.installed = null;
            return previous;
        }
    }

    /** Creates an offscreen raster surface; the caller owns and closes it. */
    GfxSurface createSurface(int width, int height);

    /** Creates a recorder for a display list; the caller owns and closes it. */
    GfxPictureRecorder newPictureRecorder();

    /**
     * Resolves the first available family from {@code preferredFamilies}.
     * Implementations should fall back to a bundled face when none is
     * present, so text can still draw; {@code null} is reserved for a backend
     * that cannot resolve any face at all. The framework turns that null into
     * its named "host typeface lookup" gap.
     */
    GfxTypeface defaultTypeface(String[] preferredFamilies);

    /**
     * Creates a font for {@code typeface} at {@code size} pixels. The caller
     * owns and closes it.
     *
     * <p>{@code subpixel} mirrors {@code Paint.SUBPIXEL_TEXT_FLAG}: when it is
     * off (the Android default) a backend rounds each glyph advance to an
     * integer before reporting a width, matching Minikin's fixed-point
     * advances. When it is on, the raw fractional advance is reported.
     */
    GfxFont newFont(GfxTypeface typeface, float size, boolean subpixel);

    /**
     * Decodes encoded image bytes (PNG/JPEG/WebP), or returns {@code null}
     * when the bytes are not a decodable image. The caller owns and closes
     * the result.
     */
    GfxImage decodeImage(byte[] encoded);
}
