package android.text;

/**
 * AOSP {@code android.text.TextWatcher}: the three text-change callbacks a
 * TextView delivers around a replacement.
 */
public interface TextWatcher extends NoCopySpan {

    /** Called before {@code s} is replaced. */
    void beforeTextChanged(CharSequence s, int start, int count, int after);

    /** Called as {@code s} is replaced. */
    void onTextChanged(CharSequence s, int start, int before, int count);

    /** Called after {@code s} was replaced. */
    void afterTextChanged(Editable s);
}
