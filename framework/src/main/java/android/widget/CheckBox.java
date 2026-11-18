package android.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * AOSP {@code android.widget.CheckBox}: a {@link CompoundButton} whose
 * default indicator is the square check box.
 */
public class CheckBox extends CompoundButton {

    private static final int CHECKBOX_STYLE = 0x0101006C;

    public CheckBox(Context context) {
        this(context, null);
    }

    public CheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, CHECKBOX_STYLE);
    }

    public CheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CheckBox(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return "android.widget.CheckBox";
    }
}
