package tartine.guest;

/**
 * Read-only {@code java.lang.Runtime} facade for guest code.
 *
 * <p>{@code Runtime.getRuntime()} is rewritten by the translator to this
 * class, and every instance call on {@code java.lang.Runtime} becomes a
 * static call here. The facade exposes the harmless memory/CPU queries and a
 * no-op {@code gc}; process control ({@code exec}, {@code exit},
 * {@code halt}), shutdown hooks, and native loading stay denied at the
 * translation pass.
 */
public final class GuestRuntime {
    private static final GuestRuntime INSTANCE = new GuestRuntime();

    private GuestRuntime() {
    }

    /** The singleton the translated {@code Runtime.getRuntime()} returns. */
    public static GuestRuntime getRuntime() {
        return INSTANCE;
    }

    public int availableProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }

    public long totalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    public long freeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    public long maxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    /** Guest-visible GC is advisory only; the host JVM owns collection. */
    public void gc() {
        // no-op
    }
}
