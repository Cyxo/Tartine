package android.text;

/**
 * AOSP {@code android.text.InputFilter}: a text transformation applied by
 * {@link android.widget.TextView} (and editable fields) on every text
 * change — a length cap, an emoji rewriter, a validator.
 *
 * <p>Source: frameworks-base core/java/android/text/InputFilter.java. The
 * contract is identical: {@code filter} receives the fragment being inserted
 * ({@code source}, {@code [start, end)}) and the destination it replaces
 * ({@code dest}, {@code [dstart, dend)}), and returns the accepted
 * replacement, or null to accept {@code source} unchanged. The
 * {@code LengthFilter} nested class is not ported yet — add it when a guest
 * references it.
 */
public interface InputFilter {
    CharSequence filter(CharSequence source, int start, int end,
            Spanned dest, int dstart, int dend);
}
