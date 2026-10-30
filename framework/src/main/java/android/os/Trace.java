package android.os;

/**
 * Systrace hooks — the desktop shell runs no trace system, so every call
 * is a no-op and tracing reports disabled.
 */
public final class Trace {

    /**
     * AOSP frameworks-base core/java/android/os/Trace.java beginSection:
     * begins a trace section; the shell is untraced, so this is inert.
     */
    public static void beginSection(String arg0) {
    }

    /**
     * AOSP .../Trace.java endSection: ends a trace section; inert on the
     * shell.
     */
    public static void endSection() {
    }

    /**
     * AOSP .../Trace.java isEnabled: tracing is off headless, returns 0
     * (matches the interpreter's isEnabled builtin).
     */
    public static boolean isEnabled() {
        return false;
    }

    private Trace() {
    }
}
