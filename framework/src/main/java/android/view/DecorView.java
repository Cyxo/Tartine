package android.view;

/**
 * Tartine: shell decor view. The device decor belongs to PhoneWindow
 * (AOSP com/android/internal/policy/PhoneWindow.java — a ViewGroup wrapping
 * the content container, which itself carries
 * {@link Window#ID_ANDROID_CONTENT android.R.id.content}); the shell
 * flattens both into one ViewGroup — the window's content holder. It wears
 * the android.R.id.content id so the device lookup pattern keeps working:
 * androidx setContent probes
 * {@code decor.findViewById(R.id.content).getChildAt(0)} for a prior
 * ComposeView, and a fresh shell window answers no children — the same
 * settle-the-new-content behavior a fresh device window shows.
 */
public class DecorView extends ViewGroup {

    /**
     * The device PhoneWindow.buildDecor seeds the decor-level container
     * hierarchy; the shell seeds context resources through the View ctor
     * and assumes the content id (Window.ID_ANDROID_CONTENT).
     */
    public DecorView(android.content.Context context) {
        super(context);
        setId(WINDOW_CONTENT_ID);
    }

    /**
     * AOSP Window.ID_ANDROID_CONTENT (Window.java line 270 —
     * com.android.internal.R.id.content, the android.R.id.content int the
     * guests probe); inlined because the hidden internal R class does not
     * port.
     */
    public static final int WINDOW_CONTENT_ID = 16908290;
}
