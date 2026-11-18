package android.text.method;

import android.graphics.Rect;
import android.view.View;

/**
 * AOSP {@code SingleLineTransformationMethod}: flattens newlines to spaces
 * so a single-line field never shows a broken line.
 */
public class SingleLineTransformationMethod implements TransformationMethod {

    private static SingleLineTransformationMethod sInstance;

    /** Returns the shared singleton. */
    public static TransformationMethod getInstance() {
        if (sInstance == null) {
            sInstance = new SingleLineTransformationMethod();
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
            char c = source.charAt(i);
            builder.append(c == '\n' || c == '\r' ? ' ' : c);
        }
        return builder;
    }

    @Override
    public void onFocusChanged(View view, CharSequence sourceText,
            boolean focused, int direction, Rect previouslyFocusedRect) {
    }

    @Override
    public String toString() {
        return "SingleLineTransformationMethod";
    }
}
