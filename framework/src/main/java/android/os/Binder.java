package android.os;

/**
 * Binder IPC core — the desktop shell runs no Binder driver, so this is a
 * type-only skeleton: guests may reference/catch it, but no transactions
 * ever occur. The interpreter never implements Binder as a builtin; it's a
 * guest framework class resolved as a placeholder.
 */
public class Binder {

    public Binder() {
    }
}
