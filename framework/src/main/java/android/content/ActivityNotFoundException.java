package android.content;

/**
 * Thrown when a call to {@link android.content.Context#startActivity(Intent)}
 * or similar cannot find a matching activity. The desktop shell resolves no
 * activities, so this exception exists as a type for guests to catch/throw;
 * its message carries the failing intent. Matches the interpreter's
 * placeholder-model approach (the interpreter never implements this class
 * as a builtin — it's a guest framework class resolved as a placeholder).
 */
public class ActivityNotFoundException extends java.lang.RuntimeException {

    public ActivityNotFoundException() {
        super();
    }

    public ActivityNotFoundException(java.lang.String detailMessage) {
        super(detailMessage);
    }
}
