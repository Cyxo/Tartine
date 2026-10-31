package tartine.guest;

import tartine.tx.TranslatedLoader;

/**
 * Guest-side helper for {@code Class.forName(String)}.
 *
 * <p>The translator rewrites the one-argument call to
 * {@code GuestSandbox.forName(String, Class)} with the calling guest class
 * injected, so the lookup always goes through the guest loader (and therefore
 * through the sandbox allowlist). A host caller is refused rather than
 * falling back to the host loader.
 */
public final class GuestSandbox {
    private GuestSandbox() {
    }

    public static Class<?> forName(String name, Class<?> caller)
            throws ClassNotFoundException {
        if (name == null) {
            throw new NullPointerException("name");
        }
        if (caller == null) {
            throw new ClassNotFoundException(
                    "Tartine: sandbox denied: Class.forName without a caller");
        }
        ClassLoader loader = caller.getClassLoader();
        if (!(loader instanceof TranslatedLoader)) {
            throw new ClassNotFoundException(
                    "Tartine: sandbox denied: Class.forName outside the guest loader");
        }
        return Class.forName(name, true, loader);
    }
}
