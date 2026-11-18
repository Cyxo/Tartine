package android.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * AOSP {@code android.widget.RadioButton}: a {@link CompoundButton} whose
 * default indicator is the round radio disc. Mutual exclusion is owned by
 * {@link RadioGroup}.
 */
public class RadioButton extends CompoundButton {

    private static final int RADIO_BUTTON_STYLE = 0x0101007E;

    public RadioButton(Context context) {
        this(context, null);
    }

    public RadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, RADIO_BUTTON_STYLE);
    }

    public RadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RadioButton(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return "android.widget.RadioButton";
    }
}
