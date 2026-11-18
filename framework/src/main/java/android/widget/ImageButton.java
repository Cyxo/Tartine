package android.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * AOSP {@code android.widget.ImageButton}: an {@link ImageView} that behaves
 * like a button. The theme supplies the background ({@code imageButtonStyle},
 * usually a borderless selector); the image and scale type come from
 * {@link ImageView}.
 */
public class ImageButton extends ImageView {

    private static final int IMAGE_BUTTON_STYLE = 0x01010072;

    public ImageButton(Context context) {
        this(context, null);
    }

    public ImageButton(Context context, AttributeSet attrs) {
        this(context, attrs, IMAGE_BUTTON_STYLE);
    }

    public ImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ImageButton(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setFocusable(true);
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return "android.widget.ImageButton";
    }
}
