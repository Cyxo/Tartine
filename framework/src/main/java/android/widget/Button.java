package android.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * AOSP {@code android.widget.Button}: a {@link TextView} whose clickable,
 * focusable, centered, single-line presentation comes entirely from the
 * theme's {@code buttonStyle}. The constructor body is empty exactly as in
 * AOSP (Button.java), so app styles — Material's included — own those
 * attributes. Deviation: the framework's own {@code Widget.Button} style is
 * not bundled, so a plain framework button without an app style loses the
 * style-supplied flags and gravity.
 */
public class Button extends TextView {

    private static final int BUTTON_STYLE = 0x01010048;

    public Button(Context context) {
        this(context, null);
    }

    public Button(Context context, AttributeSet attrs) {
        this(context, attrs, BUTTON_STYLE);
    }

    public Button(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public Button(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
