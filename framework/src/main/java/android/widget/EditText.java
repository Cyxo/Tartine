package android.widget;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.AttributeSet;

/**
 * AOSP {@code android.widget.EditText}, reduced to the editable text store:
 * the content is a {@link SpannableStringBuilder}, text input types and IME
 * options are recorded, and the input-connection/selection-action surface
 * waits for the IME bridge.
 */
public class EditText extends TextView {

    private static final int EDIT_TEXT_STYLE = 0x0101006E;
    private static final int ATTR_INPUT_TYPE = 0x01010220;
    private static final int ATTR_IME_OPTIONS = 0x01010264;

    private Editable mEditable = new SpannableStringBuilder();
    private int mInputType = InputType.TYPE_CLASS_TEXT;
    private int mImeOptions;

    public EditText(Context context) {
        this(context, null);
    }

    public EditText(Context context, AttributeSet attrs) {
        this(context, attrs, EDIT_TEXT_STYLE);
    }

    public EditText(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public EditText(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if (attrs != null) {
            android.content.res.TypedArray a = context.obtainStyledAttributes(
                    attrs, new int[] {ATTR_INPUT_TYPE, ATTR_IME_OPTIONS},
                    defStyleAttr, defStyleRes);
            try {
                mInputType = a.getInt(0, mInputType);
                mImeOptions = a.getInt(1, mImeOptions);
            } finally {
                a.recycle();
            }
        }
        setClickable(true);
        setFocusable(1);
        setFocusableInTouchMode(true);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        mEditable = new SpannableStringBuilder(text);
        super.setText(mEditable, type);
    }

    @Override
    public Editable getText() {
        return mEditable;
    }

    /** The text as an {@link Editable} buffer (never null here). */
    public Editable getEditableText() {
        return mEditable;
    }

    /** Sets the text input type. */
    public void setInputType(int type) {
        mInputType = type;
    }

    /** Returns the text input type. */
    public int getInputType() {
        return mInputType;
    }

    /** Sets the IME action/options bit field. */
    public void setImeOptions(int imeOptions) {
        mImeOptions = imeOptions;
    }

    /** Returns the IME action/options bit field. */
    public int getImeOptions() {
        return mImeOptions;
    }

    /** Appends text through the editable buffer. */
    public void append(CharSequence text) {
        setText(mEditable.toString() + (text == null ? "" : text));
    }

    /** Selects the whole buffer. */
    public void selectAll() {
        setSelection(0, mEditable.length());
    }

    /** Extends the selection to {@code index}. */
    public void extendSelection(int index) {
        setSelection(Math.min(getSelectionStart(), index),
                Math.max(getSelectionEnd(), index));
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return true;
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return "android.widget.EditText";
    }

    /** Registration is inherited from TextView; kept here for API parity. */
    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        super.addTextChangedListener(watcher);
    }
}
