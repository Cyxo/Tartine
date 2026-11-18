package android.text.method;

import android.graphics.Rect;
import android.view.View;

/**
 * AOSP {@code PasswordTransformationMethod}: displays one bullet per
 * character. The singleton is stateless, matching the device.
 */
public class PasswordTransformationMethod implements TransformationMethod {

    private static final char BULLET = '\u2022';
    private static PasswordTransformationMethod sInstance;

    /** Returns the shared singleton. */
    public static TransformationMethod getInstance() {
        if (sInstance == null) {
            sInstance = new PasswordTransformationMethod();
        }
        return sInstance;
    }

    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        if (source == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder(source.length());
        for (int i = 0; i < source.length(); i++) {
            builder.append(BULLET);
        }
        return builder;
    }

    @Override
    public void onFocusChanged(View view, CharSequence sourceText,
            boolean focused, int direction, Rect previouslyFocusedRect) {
    }

    @Override
    public String toString() {
        return "PasswordTransformationMethod";
    }
}
