package android.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import java.util.ArrayList;

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
    protected void onCreate(Bundle arg0) {
    }

    /**
     * AOSP Activity lines 927-928: per-Activity callback list; register and
     * unregister under the list monitor (AOSP lines 1530-1550). Dispatch is
     * inert until the shell-side lifecycle steps land.
     */
    private final ArrayList<Application.ActivityLifecycleCallbacks>
            mActivityLifecycleCallbacks =
            new ArrayList<Application.ActivityLifecycleCallbacks>();

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
    public void setContentView(View arg0) {
        getWindow().setContentView(arg0);
    }

    /**
     * AOSP Activity.setContentView(View, LayoutParams) (Activity.java lines
     * 3922+) delegates to the window with the caller's params.
     */
    public void setContentView(View arg0,
            ViewGroup.LayoutParams arg1) {
        getWindow().setContentView(arg0, arg1);
    }

    /**
     * AOSP Activity.getWindow() (Activity.java line 1350 — the device
     * answers the mWindow field created in attach at line 8969 via
     * PhoneWindow); the shell lazily owns one concrete Window per
     * activity standing in for PhoneWindow.
     */
    public Window getWindow() {
        if (mWindow == null) {
            mWindow = new Window(this);
        }
        return mWindow;
    }

    private Window mWindow;

    /**
     * AOSP Activity.getFragmentManager() (line 3582 — the device delegates
     * to the FragmentController's HostCallbacks); the shell owns one lazy
     * manager per activity and no attached fragment history.
     */
    public FragmentManager getFragmentManager() {
        if (mFragmentManager == null) {
            mFragmentManager = new FragmentManager();
        }
        return mFragmentManager;
    }

    private FragmentManager mFragmentManager;

    /**
     * AOSP frameworks-base core/java/android/app/Activity.java:1312-1314
     * getApplication: return mApplication (set by device attach()). The
     * shell lazily owns one Application per activity standing in for the
     * process singleton (matches the interpreter's per-activity cached
     * placeholder).
     */
    public final Application getApplication() {
        if (mApplication == null) {
            mApplication = new Application();
        }
        return mApplication;
    }

    private Application mApplication;

    /**
     * AOSP frameworks-base core/java/android/app/Activity.java:1167-1169
     * getIntent: return mIntent (set by launched-intent bookkeeping in
     * device attach()/setIntent). Handless launches carry no action, data,
     * or extras: the shell lazily owns one stable empty Intent per
     * activity (matches the interpreter's cached-placeholder builtin).
     */
    public Intent getIntent() {
        if (mIntent == null) {
            mIntent = new Intent();
        }
        return mIntent;
    }

    private Intent mIntent;

    /**
     * AOSP frameworks-base core/java/android/app/Activity.java:3431-3434
     * getLastNonConfigurationInstance: mLastNonConfigurationInstances !=
     * null ? mLastNonConfigurationInstances.activity : null — the field is
     * only populated when a config change retained state (attach(), device
     * ActivityThread). A fresh activity answers null ("no config change
     * registered"); the shell retains none, same result.
     */
    public Object getLastNonConfigurationInstance() {
        return null;
    }

    /**
     * AOSP frameworks-base core/java/android/app/Activity.java:3488-3489
     * onRetainNonConfigurationInstance: the default activity retains
     * nothing (return null); subclasses override.
     */
    public Object onRetainNonConfigurationInstance() {
        return null;
    }
}
