package tartine.gfx;

/** Process-wide slot for the installed backend (see {@link GfxBackend}). */
final class GfxBackendHolder {

    private GfxBackendHolder() {
    }

    static volatile GfxBackend installed;
}
