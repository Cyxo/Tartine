package android.text.method;

import android.graphics.Rect;
import android.view.View;

/**
 * AOSP {@code android.text.method.TransformationMethod}: maps displayed
 * text through a filter (password dots, all caps, single-line flattening)
 * without changing the store the widget keeps.
 */
public interface TransformationMethod {

    /** Returns the display form of {@code source} for {@code view}. */
    CharSequence getTransformation(CharSequence source, View view);

    /** AOSP hook: a widget with focus calls this to update state. */
    void onFocusChanged(View view, CharSequence sourceText, boolean focused,
            int direction, Rect previouslyFocusedRect);
}
