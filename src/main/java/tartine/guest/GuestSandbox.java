package tartine.guest;

import tartine.tx.TranslatedLoader;

/**
 * Guest-side helper for {@code Class.forName} lookups.
 *
 * <p>The translator rewrites the one- and two-argument
 * {@code Class.forName} forms and the three-argument
 * {@code Class.forName(String, boolean, ClassLoader)} to the overloads
 * below. The three-argument loader must be a {@link GuestClassLoader}
 * produced by {@code Class.getClassLoader}; a host loader is refused rather
 * than used, and a {@code null} loader maps to the calling guest class's
 * sandboxed loader. The caller-injecting forms take the calling guest class
 * and apply the sandbox allowlist before resolving the name.
 */
public final class GuestSandbox {
    private GuestSandbox() {
    }

    /** Mirrors {@code Class.forName}; only the guest loader is accepted. */
    public static Class<?> forName(String name, boolean initialize,
            Object loader) throws ClassNotFoundException {
        if (name == null) {
            throw new NullPointerException("name");
        }
        if (!(loader instanceof GuestClassLoader guest)) {
            throw new ClassNotFoundException(
                    "Tartine: sandbox denied: Class.forName with a host loader");
        }
        return Class.forName(name, initialize, guest);
    }

    /**
     * Translator entry for {@code Class.forName(String, boolean, loader)}.
     * A {@code null} loader means "the caller's loader" on Android; here it
     * maps to the calling guest class's sandboxed loader rather than the
     * bootstrap loader, so the name stays behind the allowlist.
     */
    public static Class<?> forName(String name, boolean initialize,
            Object loader, Class<?> caller) throws ClassNotFoundException {
        if (loader == null) {
            return forName(name, initialize, caller);
        }
        return forName(name, initialize, loader);
    }

    public static Class<?> forName(String name, Class<?> caller)
            throws ClassNotFoundException {
        return forName(name, true, caller);
    }

    /** Resolves within the calling guest class's sandboxed loader. */
    public static Class<?> forName(String name, boolean initialize,
            Class<?> caller) throws ClassNotFoundException {
        if (name == null) {
            throw new NullPointerException("name");
        }
        if (caller == null) {
            throw new ClassNotFoundException(
                    "Tartine: sandbox denied: Class.forName without a caller");
        }
        if (!(caller.getClassLoader() instanceof TranslatedLoader loader)) {
            throw new ClassNotFoundException(
                    "Tartine: sandbox denied: Class.forName outside the guest loader");
        }
        // Gate first, then let Class.forName initialize through the same
        // loader (its loadClass is linkage-only and unfiltered).
        loader.loadGuestClass(name);
        return Class.forName(name, initialize, loader);
    }
}
