package android.util;

/**
 * Desktop mapping (Tartine). Ported verbatim (bodies are trivial
 * constructor-forwarders) from
 * aosp-frameworks-base/core/java/android/util/AndroidException.java:
 * "Base class for all checked exceptions thrown by the Android frameworks".
 */
public class AndroidException extends Exception {
    public AndroidException() {
    }

    public AndroidException(String name) {
        super(name);
    }

    public AndroidException(String name, Throwable cause) {
        super(name, cause);
    }

    public AndroidException(Exception cause) {
        super(cause);
    }

    protected AndroidException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
