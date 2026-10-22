package android.view;

/**
 * Tartine: minimal shell window. The device concrete is PhoneWindow,
 * created in AOSP Activity.attach (core/java/android/app/Activity.java
 * line 8969 {@code mWindow = new PhoneWindow(this, ...)}); until the
 * desktop windowing stack owns Activity content, the shell lazily owns
 * one concrete Window per Activity standing in for it. Its decor is an
 * inert shell View — androidx Activity code asserts the decor non-null
 * (Intrinsics.checkNotNullExpressionValue on window.decorView) and
 * fully-drawn-reporting posts against it.
 */
public class Window {

    /**
     * AOSP Window.ID_ANDROID_CONTENT (Window.java line 270): the device
     * com.android.internal.R.id.content — the id android.R.id.content uses;
     * kept raw because the internal R class is hidden on the shell too.
     */
    public static final int ID_ANDROID_CONTENT = 16908290;

    private final ViewGroup mDecor;

    /**
     * The device concrete PhoneWindow carries the activity context (AOSP
     * Activity.attach line 8969 `mWindow = new PhoneWindow(this, ...)`
     * supplies it); the shell mirrors it so the decor's view can seed its
     * resources from the context the way the device does. The decor is the
     * shell's flattened content holder ({@link DecorView}).
     */
    public Window(android.content.Context context) {
        mDecor = new DecorView(context);
    }

    /** AOSP Window.getDecorView(). */
    public View getDecorView() {
        return mDecor;
    }

    /**
     * AOSP Window.setContentView(View, ...) is abstract (line 1803;
     * PhoneWindow.setContentView(View, params) at
     * com/android/internal/policy/PhoneWindow.java lines 558-586 removes
     * previous content and adds the new child). The shell keeps that
     * replace-one-content-child flow on the flattened decor (transition
     * scenes, insets-Appling and the Callback.onContentChanged dispatch
     * trail until those layers land).
     */
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (mDecor.getChildCount() > 0) {
            mDecor.removeAllViews();
        }
        mDecor.addView(view, params);
    }

    /**
     * AOSP Window.setContentView(View) is abstract (line 1782;
     * PhoneWindow.setContentView(View) at line 553-555 wraps it into
     * setContentView(view, new LayoutParams(MATCH_PARENT, MATCH_PARENT))).
     */
    public void setContentView(View view) {
        setContentView(view, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /** AOSP core/java/android/view/Window.java lines 1568-1569: an inert
     * deprecated body (insets handling rides the insets controller, not
     * this flag; desktop shell has no system-bar insets yet). */
    public void setDecorFitsSystemWindows(boolean arg0) {
    }

    /**
     * AOSP Window.setStatusBarColor is abstract (PhoneWindow implements in
     * com/android/internal/policy/PhoneWindow.java); the desktop shell has
     * no system status bar, so the color has no visual channel and the
     * body is inert until the window-chrome layer decides to render one.
     */
    public void setStatusBarColor(int arg0) {
    }

    /**
     * AOSP Window.setNavigationBarColor is abstract (PhoneWindow implements
     * in com/android/internal/policy/PhoneWindow.java); the desktop shell
     * has no system navigation bar, so the body is inert until the
     * window-chrome layer decides to render one.
     */
    public void setNavigationBarColor(int arg0) {
    }

    /** AOSP Window.setStatusBarContrastEnforced is abstract (PhoneWindow
     * stores the flag for the system bar chrome); the desktop shell has no
     * status bar to enforce contrast on, so the body is inert. */
    public void setStatusBarContrastEnforced(boolean arg0) {
    }

    /** AOSP Window.setNavigationBarContrastEnforced is abstract (PhoneWindow
     * stores the flag for the system bar chrome); the desktop shell has no
     * navigation bar to enforce contrast on, so the body is inert. */
    public void setNavigationBarContrastEnforced(boolean arg0) {
    }

    /**
     * AOSP Window.getAttributes (line 1597, final, returns mAttributes);
     * the shell owns one LayoutParams per window so guest writes — e.g.
     * androidx adjustLayoutInDisplayCutoutMode's layoutInDisplayCutoutMode
     * store — land on window-scoped state.
     */
    public WindowManager.LayoutParams getAttributes() {
        if (mAttributes == null) {
            mAttributes = new WindowManager.LayoutParams();
        }
        return mAttributes;
    }

    private WindowManager.LayoutParams mAttributes;

    /**
     * AOSP Window.getInsetsController (line 2995-2997) — the base Window
     * answers null and PhoneWindow overrides with the real
     * android.view.InsetsController; the shell specifically stands in the
     * PhoneWindow duty and answers one lazily-owned shell
     * {@link InsetsController} per window (non-null: androidx
     * EdgeToEdge flows bind it and dispatch appearance flags through it).
     */
    public WindowInsetsController getInsetsController() {
        if (mInsetsController == null) {
            mInsetsController = new InsetsController();
        }
        return mInsetsController;
    }

    private WindowInsetsController mInsetsController;
}
