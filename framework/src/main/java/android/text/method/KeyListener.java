package android.text.method;

import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;

/**
 * AOSP {@code android.text.method.KeyListener}: translates key events into
 * text edits for a widget. The shell has no input-method pipeline yet, so the
 * interface exists for linkage (AndroidX's emoji helper wraps the installed
 * listener) and no event is delivered here.
 */
public interface KeyListener {

    /** Returns the input type flags this listener produces. */
    int getInputType();

    /** Handles a key-down event; returns whether it consumed the event. */
    boolean onKeyDown(View view, Editable text, int keyCode, KeyEvent event);

    /** Handles a key-up event; returns whether it consumed the event. */
    boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event);

    /** Handles a multi-character key event. */
    boolean onKeyOther(View view, Editable text, KeyEvent event);

    /** Clears the meta-key state selected by {@code states}. */
    void clearMetaKeyState(View view, Editable content, int states);
}
