package tartine.fw;

/**
 * Dev seam for framework mirror virtual dispatch — the reverse bridge of
 * AGENTS.md framework-in-Java (2): "framework dispatch checks guest override
 * first (runs DEX), then reflective host invocation". Guest-driven virtual
 * calls already resolve guest DEX first; but once execution enters a
 * framework mirror body (plain host Java), that body's own virtual call
 * sites ({@code View.draw}'s onDraw/dispatchDraw/onDrawForeground legs,
 * {@code ViewGroup.drawChild}'s child.draw, ...) cannot see guest overrides
 * — host Java has no route into guest DEX bodies. Call sites route through
 * {@link #on}: when the hosted interpreter finds a guest override on the
 * receiver's model, the guest body runs and the mirror default is skipped.
 *
 * <p>The Interpreter installs itself at construction via {@link #install};
 * framework binaries built (or tests run) without an interpreter keep the
 * default handler and every call site falls through to the framework
 * default inline.</p>
 */
public final class VirtualCall {

    /** The receiver has no guest override for the method (default must run). */
    public static final Object NOT_GUEST = new Object();

    /** Runs a guest override if one exists; else returns {@link #NOT_GUEST}. */
    public interface Handler {
        Object invoke(Object receiver, String name, String descriptor,
                Object[] args);
    }

    private static volatile Handler handler;

    /** Installs the interpreter-backed handler (one live host per process). */
    public static void install(Handler h) {
        handler = h;
    }

    /** Runs a guest override if one exists; else returns {@link #NOT_GUEST}. */
    public static Object on(Object receiver, String name, String descriptor,
            Object[] args) {
        Handler h = handler;
        if (h == null) {
            return NOT_GUEST;
        }
        return h.invoke(receiver, name, descriptor, args);
    }

    private VirtualCall() {
    }
}
