package android.text;

/**
 * AOSP {@code android.text.Editable}: text whose content can be replaced in
 * place. The shell keeps a plain character buffer; span APIs are the no-op
 * {@link Spannable} surface of the staging text model.
 */
public interface Editable extends CharSequence, Spannable {

    /** Replaces a range with a slice of {@code source}. */
    Editable replace(int st, int en, CharSequence source, int start, int end);

    /** Replaces a range with {@code text}. */
    Editable replace(int st, int en, CharSequence text);

    /** Inserts a slice of {@code text} at {@code where}. */
    Editable insert(int where, CharSequence text, int start, int end);

    /** Inserts {@code text} at {@code where}. */
    Editable insert(int where, CharSequence text);

    /** Deletes a range. */
    Editable delete(int st, int en);

    /** Appends text. */
    Editable append(CharSequence text);

    /** Appends a slice of text. */
    Editable append(CharSequence text, int start, int end);

    /** Appends one character. */
    Editable append(char text);

    /** Empties the buffer. */
    void clear();

    /** Removes every span. */
    void clearSpans();

    /**
     * AOSP {@code Editable.Factory}: creates the editable buffer backing a
     * text field. AndroidX's emoji integration subclasses this to wrap the
     * editable in a {@code SpannableBuilder}.
     */
    class Factory {
        private static final Factory sInstance = new Factory();

        public static Factory getInstance() {
            return sInstance;
        }

        public Editable newEditable(CharSequence source) {
            return new SpannableStringBuilder(source);
        }
    }
}
