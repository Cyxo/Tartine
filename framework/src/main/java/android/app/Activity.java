package android.app;

/**
 * Tartine: minimal skeleton for the translation spike. The fw-gen closure
 * for the real class drags ActionBar/Fragment (whose nested types cannot
 * merge into existing parent files yet); grows when a wall demands members.
 */
public class Activity extends ContextThemeWrapper {

    public Activity() {
    }

    /**
     * Signature from aosp-frameworks-base/core/java/android/app/Activity.java
     * ({@code protected void onCreate(Bundle)}). The real device body runs
     * the ActivityThread lifecycle dispatch (performCreate →
     * dispatchActivityPreCreated → ComposeHelper...); the desktop shell
     * Activity owns no Fragment/Compose state to dispatch yet, and the
     * guest's ComponentActivity chain performs the app-side bookkeeping, so
     * the shell body is inert until the window layer lands.
     */
    protected void onCreate(android.os.Bundle arg0) {
    }

    /**
     * AOSP Activity lines 927-928: per-Activity callback list; register and
     * unregister under the list monitor (AOSP lines 1530-1550). Dispatch is
     * inert until the shell-side lifecycle steps land.
     */
    private final java.util.ArrayList<Application.ActivityLifecycleCallbacks>
            mActivityLifecycleCallbacks =
            new java.util.ArrayList<Application.ActivityLifecycleCallbacks>();

    /** AOSP Activity lines 1530-1535. */
    public void registerActivityLifecycleCallbacks(
            Application.ActivityLifecycleCallbacks callback) {
        synchronized (mActivityLifecycleCallbacks) {
            mActivityLifecycleCallbacks.add(callback);
        }
    }

    /** AOSP Activity lines 1545-1550. */
    public void unregisterActivityLifecycleCallbacks(
            Application.ActivityLifecycleCallbacks callback) {
        synchronized (mActivityLifecycleCallbacks) {
            mActivityLifecycleCallbacks.remove(callback);
        }
    }

    /**
     * AOSP Activity.setContentView(View) (Activity.java lines 3906-3911)
     * delegates to the window; the shell pairs it with the flattened
     * window content flow.
     */
    public void setContentView(android.view.View arg0) {
        getWindow().setContentView(arg0);
    }

    /**
     * AOSP Activity.setContentView(View, LayoutParams) (Activity.java lines
     * 3922+) delegates to the window with the caller's params.
     */
    public void setContentView(android.view.View arg0,
            android.view.ViewGroup.LayoutParams arg1) {
        getWindow().setContentView(arg0, arg1);
    }

    /**
     * AOSP Activity.getWindow() (Activity.java line 1350 — the device
     * answers the mWindow field created in attach at line 8969 via
     * PhoneWindow); the shell lazily owns one concrete Window per
     * activity standing in for PhoneWindow.
     */
    public android.view.Window getWindow() {
        if (mWindow == null) {
            mWindow = new android.view.Window(this);
        }
        return mWindow;
    }

    private android.view.Window mWindow;

    /**
     * AOSP Activity.getFragmentManager() (line 3582 — the device delegates
     * to the FragmentController's HostCallbacks); the shell owns one lazy
     * manager per activity and no attached fragment history.
     */
    public android.app.FragmentManager getFragmentManager() {
        if (mFragmentManager == null) {
            mFragmentManager = new android.app.FragmentManager();
        }
        return mFragmentManager;
    }

    private android.app.FragmentManager mFragmentManager;
}
