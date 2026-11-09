package tartine.fw;

/**
 * Host consent seam for dangerous permissions. The desktop shell installs one
 * in GUI runs so a first use or an explicit {@code requestPermissions} can
 * show a real dialog; headless runs leave it unset, which answers
 * {@link Decision#UNAVAILABLE}. Consent is a fidelity feature, not an
 * enforcement boundary (ADR-0012).
 */
public interface PermissionPrompt {

    /** What the user answered, or that no prompt can be shown. */
    enum Decision {
        GRANTED,
        DENIED,
        UNAVAILABLE
    }

    /**
     * Asks the user about one permission.
     *
     * @param packageName the guest package requesting access
     * @param permission the full Android permission name
     * @param label the short human-readable capability label
     * @return the user's decision, or {@link Decision#UNAVAILABLE} when the
     *         host has no UI
     */
    Decision request(String packageName, String permission, String label);
}
