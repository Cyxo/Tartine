package android.widget;

/**
 * AOSP {@code android.widget.Checkable}: the two-state contract shared by
 * check boxes, radio buttons and toggle buttons.
 */
public interface Checkable {

    /** Sets the checked state. */
    void setChecked(boolean checked);

    /** Returns whether the widget is checked. */
    boolean isChecked();

    /** Flips the checked state. */
    void toggle();
}
