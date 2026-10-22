package android.util;

/**
 * Desktop mapping (Tartine). Ported verbatim (bodies are trivial
 * constructor-forwarders) from
 * aosp-frameworks-base/core/java/android/util/AndroidException.java:
 * "Base class for all checked exceptions thrown by the Android frameworks".
 */
public class AndroidException extends java.lang.Exception {
    public AndroidException() {
    }

    public AndroidException(java.lang.String name) {
        super(name);
    }

    public AndroidException(java.lang.String name, java.lang.Throwable cause) {
        super(name, cause);
    }

    public AndroidException(java.lang.Exception cause) {
        super(cause);
    }

    protected AndroidException(java.lang.String message, java.lang.Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
