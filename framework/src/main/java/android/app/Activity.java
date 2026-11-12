package android.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import java.util.ArrayList;
import tartine.fw.PermissionManager;

/**
 * Tartine: minimal skeleton for the translation spike. The fw-gen closure
 * for the real class drags ActionBar/Fragment (whose nested types cannot
 * merge into existing parent files yet); grows when a wall demands members.
 *
 * <p>AOSP {@code Activity} implements {@link Window.Callback} and installs
 * itself as the window's callback in {@code attach}; AppCompat's window
 * wrapper delegates every callback method back to that activity, so the
 * shell declares the same interface.
 */
public class Activity extends ContextThemeWrapper implements Window.Callback {

    public Activity() {
    }

    /**
     * AOSP Activity.requestPermissions(String[], int): resolve the requests
     * through the host consent seam and deliver the result to
     * {@link #onRequestPermissionsResult}. The desktop Handler delivers
     * inline, so the callback runs before this method returns.
     */
    public final void requestPermissions(String[] arg0, int arg1) {
        PermissionManager.request(this, arg0, arg1);
    }

    /** User-scoped overload; the desktop has one user. */
    public final void requestPermissions(String[] arg0, int arg1, int arg2) {
        requestPermissions(arg0, arg1);
    }

    /** AOSP default: subclasses override to consume the result. */
    public void onRequestPermissionsResult(int arg0, String[] arg1,
            int[] arg2) {
    }

    /** User-scoped overload forwarding to the three-argument callback. */
    public void onRequestPermissionsResult(int arg0, String[] arg1,
            int[] arg2, int arg3) {
        onRequestPermissionsResult(arg0, arg1, arg2);
    }

    /**
     * AOSP asks whether a previous denial should show rationale. The
     * desktop prompts once and remembers the answer, so there is no
     * separate rationale step.
     */
    public boolean shouldShowRequestPermissionRationale(String arg0) {
        return false;
    }

    /** User-scoped overload; the desktop has one user. */
    public boolean shouldShowRequestPermissionRationale(String arg0,
            int arg1) {
        return false;
    }

    /**
     * AOSP Activity.onCreate default: subclasses perform their own setup and
     * normally call through to super. The shell keeps the body inert (the
     * lifecycle dispatch itself lives in {@link #performCreate}).
     */
    protected void onCreate(Bundle arg0) {
    }

    /** AOSP Activity.onPostCreate: runs after onCreate returns. */
    protected void onPostCreate(Bundle arg0) {
    }

    /** AOSP Activity.onStart default. */
    protected void onStart() {
    }

    /** AOSP Activity.onResume default. */
    protected void onResume() {
    }

    /** AOSP Activity.onPostResume: runs after onResume returns. */
    protected void onPostResume() {
    }

    /** AOSP Activity.onPause default. */
    protected void onPause() {
    }

    /** AOSP Activity.onStop default. */
    protected void onStop() {
    }

    /** AOSP Activity.onDestroy default. */
    protected void onDestroy() {
    }

    /** AOSP Activity.onSaveInstanceState default. */
    protected void onSaveInstanceState(Bundle arg0) {
    }

    /** AOSP Activity.onRestoreInstanceState default. */
    protected void onRestoreInstanceState(Bundle arg0) {
    }

    /** AOSP Activity.onWindowFocusChanged default. */
    public void onWindowFocusChanged(boolean arg0) {
    }

    /** AOSP Activity.onContentChanged default. */
    public void onContentChanged() {
    }

    /**
     * AOSP Activity.invalidateOptionsMenu: the activity's options menu needs
     * rebuilding. androidx calls this through ComponentActivity's menu host;
     * the desktop shell has no options-menu presenter yet, so the body is
     * inert until a menu wall needs the dispatch.
     */
    public void invalidateOptionsMenu() {
    }

    /** AOSP Activity.openOptionsMenu: no options-menu presenter yet. */
    public void openOptionsMenu() {
    }

    /** AOSP Activity.closeOptionsMenu: no options-menu presenter yet. */
    public void closeOptionsMenu() {
    }

    /** AOSP Activity.onAttachedToWindow default. */
    public void onAttachedToWindow() {
    }

    /** AOSP Activity.onDetachedFromWindow default. */
    public void onDetachedFromWindow() {
    }

    /**
     * AOSP Activity.dispatchKeyEvent: the window's decor gets the first
     * chance, then the activity's {@code onKeyDown}/{@code onKeyUp} run. The
     * shell's decor consumes nothing yet, so the events fall through.
     */
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (getWindow().superDispatchKeyEvent(event)) {
            return true;
        }
        int keyCode = event.getKeyCode();
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            return onKeyDown(keyCode, event);
        }
        return onKeyUp(keyCode, event);
    }

    /** AOSP Activity.onKeyDown default: not handled by the activity. */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    /** AOSP Activity.onKeyUp default: not handled by the activity. */
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }

    /** AOSP Activity.dispatchKeyShortcutEvent: no shortcut layer yet. */
    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        return false;
    }

    /** AOSP Activity.dispatchTouchEvent: the window decor dispatches. */
    public boolean dispatchTouchEvent(MotionEvent event) {
        return getWindow().superDispatchTouchEvent(event);
    }

    /** AOSP Activity.dispatchTrackballEvent. */
    public boolean dispatchTrackballEvent(MotionEvent event) {
        return getWindow().superDispatchTrackballEvent(event);
    }

    /** AOSP Activity.dispatchGenericMotionEvent. */
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        return getWindow().superDispatchGenericMotionEvent(event);
    }

    /** AOSP Activity.dispatchPopulateAccessibilityEvent. */
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        return false;
    }

    /** AOSP Activity.onCreatePanelView default: no panel view. */
    public View onCreatePanelView(int featureId) {
        return null;
    }

    /** AOSP Activity.onCreatePanelMenu default: no menu created. */
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        return false;
    }

    /** AOSP Activity.onPreparePanel default: nothing to prepare. */
    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        return false;
    }

    /** AOSP Activity.onMenuOpened default: the menu is accepted. */
    public boolean onMenuOpened(int featureId, Menu menu) {
        return false;
    }

    /** AOSP Activity.onMenuItemSelected default: nothing selected. */
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return false;
    }

    /** AOSP Activity.onWindowAttributesChanged default. */
    public void onWindowAttributesChanged(WindowManager.LayoutParams attrs) {
    }

    /** AOSP Activity.onPanelClosed default. */
    public void onPanelClosed(int featureId, Menu menu) {
    }

    /** AOSP Activity.onSearchRequested default: not handled. */
    public boolean onSearchRequested() {
        return false;
    }

    /** AOSP Activity.onSearchRequested(SearchEvent) default: not handled. */
    public boolean onSearchRequested(SearchEvent searchEvent) {
        return false;
    }

    /** AOSP Activity.onWindowStartingActionMode default: no action mode. */
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        return null;
    }

    /** AOSP Activity.onWindowStartingActionMode(Callback, int) default. */
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int type) {
        return null;
    }

    /** AOSP Activity.onActionModeStarted default. */
    public void onActionModeStarted(ActionMode mode) {
    }

    /** AOSP Activity.onActionModeFinished default. */
    public void onActionModeFinished(ActionMode mode) {
    }

    /**
     * AOSP Activity.getComponentName (line 1367): the activity's own
     * component. The device seeds {@code mComponent} in attach; the shell
     * derives it from the running class, which is the same component the host
     * launched.
     */
    public android.content.ComponentName getComponentName() {
        if (mComponent == null) {
            mComponent = new android.content.ComponentName(this, getClass());
        }
        return mComponent;
    }

    private android.content.ComponentName mComponent;

    /**
     * AOSP Activity.getOnBackInvokedDispatcher (line 4052): the per-activity
     * dispatcher AOSP seeds from the window. The shell lazily owns one; it
     * carries the registrations guests make and answers a back dispatch only
     * when the host sends one.
     */
    public android.window.OnBackInvokedDispatcher getOnBackInvokedDispatcher() {
        if (mOnBackInvokedDispatcher == null) {
            mOnBackInvokedDispatcher = new android.window.WindowOnBackInvokedDispatcher();
        }
        return mOnBackInvokedDispatcher;
    }

    private android.window.WindowOnBackInvokedDispatcher mOnBackInvokedDispatcher;

    /**
     * AOSP Activity.performCreate: pre-created callbacks, onCreate, created
     * callbacks, onPostCreate, post-created callbacks. Both the application
     * list and the activity-local list are dispatched, which is what
     * androidx.lifecycle.ReportFragment relies on.
     */
    public final void performCreate(Bundle savedInstanceState) {
        dispatchActivityPreCreated(savedInstanceState);
        onCreate(savedInstanceState);
        dispatchActivityCreated(savedInstanceState);
        onPostCreate(savedInstanceState);
        dispatchActivityPostCreated(savedInstanceState);
    }

    /** AOSP Activity.performStart. */
    public final void performStart() {
        dispatchActivityPreStarted();
        onStart();
        dispatchActivityStarted();
        dispatchActivityPostStarted();
    }

    /** AOSP Activity.performResume. */
    public final void performResume() {
        dispatchActivityPreResumed();
        onResume();
        dispatchActivityResumed();
        onPostResume();
        dispatchActivityPostResumed();
    }

    /** AOSP Activity.performPause. */
    public final void performPause() {
        dispatchActivityPrePaused();
        onPause();
        dispatchActivityPaused();
        dispatchActivityPostPaused();
    }

    /** AOSP Activity.performStop. */
    public final void performStop() {
        dispatchActivityPreStopped();
        onStop();
        dispatchActivityStopped();
        dispatchActivityPostStopped();
    }

    /** AOSP Activity.performDestroy. */
    public final void performDestroy() {
        dispatchActivityPreDestroyed();
        mDestroyed = true;
        onDestroy();
        dispatchActivityDestroyed();
        dispatchActivityPostDestroyed();
    }

    /** AOSP Activity.isFinishing: true once finish() has been called. */
    public boolean isFinishing() {
        return mFinished;
    }

    /** AOSP Activity.isDestroyed: true once performDestroy has started. */
    public boolean isDestroyed() {
        return mDestroyed;
    }

    /**
     * AOSP Activity.isChangingConfigurations: the retain-instance flag.
     * The shell never recreates an activity for a configuration change
     * (a resize keeps the running instance), so it stays false.
     */
    public boolean isChangingConfigurations() {
        return mChangingConfigurations;
    }

    /** AOSP Activity.finish: mark finishing; the host owns the teardown. */
    public void finish() {
        mFinished = true;
    }

    private boolean mFinished;
    private boolean mDestroyed;
    private boolean mChangingConfigurations;

    /** AOSP Activity.performSaveInstanceState. */
    public final void performSaveInstanceState(Bundle outState) {
        dispatchActivityPreSaveInstanceState(outState);
        onSaveInstanceState(outState);
        dispatchActivitySaveInstanceState(outState);
        dispatchActivityPostSaveInstanceState(outState);
    }

    private Object[] collectActivityLifecycleCallbacks() {
        synchronized (mActivityLifecycleCallbacks) {
            return mActivityLifecycleCallbacks.toArray();
        }
    }

    private void dispatchActivityPreCreated(Bundle savedInstanceState) {
        getApplication().dispatchActivityPreCreated(this, savedInstanceState);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        for (Object callback : callbacks) {
            ((Application.ActivityLifecycleCallbacks) callback)
                    .onActivityPreCreated(this, savedInstanceState);
        }
    }

    private void dispatchActivityCreated(Bundle savedInstanceState) {
        getApplication().dispatchActivityCreated(this, savedInstanceState);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        for (Object callback : callbacks) {
            ((Application.ActivityLifecycleCallbacks) callback)
                    .onActivityCreated(this, savedInstanceState);
        }
    }

    private void dispatchActivityPostCreated(Bundle savedInstanceState) {
        getApplication().dispatchActivityPostCreated(this, savedInstanceState);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        for (Object callback : callbacks) {
            ((Application.ActivityLifecycleCallbacks) callback)
                    .onActivityPostCreated(this, savedInstanceState);
        }
    }

    private void dispatchActivityPreStarted() {
        getApplication().dispatchActivityPreStarted(this);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        for (Object callback : callbacks) {
            ((Application.ActivityLifecycleCallbacks) callback)
                    .onActivityPreStarted(this);
        }
    }

    private void dispatchActivityStarted() {
        getApplication().dispatchActivityStarted(this);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        for (Object callback : callbacks) {
            ((Application.ActivityLifecycleCallbacks) callback)
                    .onActivityStarted(this);
        }
    }

    private void dispatchActivityPostStarted() {
        getApplication().dispatchActivityPostStarted(this);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        for (Object callback : callbacks) {
            ((Application.ActivityLifecycleCallbacks) callback)
                    .onActivityPostStarted(this);
        }
    }

    private void dispatchActivityPreResumed() {
        getApplication().dispatchActivityPreResumed(this);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        for (Object callback : callbacks) {
            ((Application.ActivityLifecycleCallbacks) callback)
                    .onActivityPreResumed(this);
        }
    }

    private void dispatchActivityResumed() {
        getApplication().dispatchActivityResumed(this);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        for (Object callback : callbacks) {
            ((Application.ActivityLifecycleCallbacks) callback)
                    .onActivityResumed(this);
        }
    }

    private void dispatchActivityPostResumed() {
        getApplication().dispatchActivityPostResumed(this);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        for (Object callback : callbacks) {
            ((Application.ActivityLifecycleCallbacks) callback)
                    .onActivityPostResumed(this);
        }
    }

    private void dispatchActivityPrePaused() {
        getApplication().dispatchActivityPrePaused(this);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        for (Object callback : callbacks) {
            ((Application.ActivityLifecycleCallbacks) callback)
                    .onActivityPrePaused(this);
        }
    }

    private void dispatchActivityPaused() {
        getApplication().dispatchActivityPaused(this);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        for (Object callback : callbacks) {
            ((Application.ActivityLifecycleCallbacks) callback)
                    .onActivityPaused(this);
        }
    }

    private void dispatchActivityPostPaused() {
        getApplication().dispatchActivityPostPaused(this);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        for (Object callback : callbacks) {
            ((Application.ActivityLifecycleCallbacks) callback)
                    .onActivityPostPaused(this);
        }
    }

    private void dispatchActivityPreStopped() {
        getApplication().dispatchActivityPreStopped(this);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        for (Object callback : callbacks) {
            ((Application.ActivityLifecycleCallbacks) callback)
                    .onActivityPreStopped(this);
        }
    }

    private void dispatchActivityStopped() {
        getApplication().dispatchActivityStopped(this);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        for (Object callback : callbacks) {
            ((Application.ActivityLifecycleCallbacks) callback)
                    .onActivityStopped(this);
        }
    }

    private void dispatchActivityPostStopped() {
        getApplication().dispatchActivityPostStopped(this);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        for (Object callback : callbacks) {
            ((Application.ActivityLifecycleCallbacks) callback)
                    .onActivityPostStopped(this);
        }
    }

    private void dispatchActivityPreSaveInstanceState(Bundle outState) {
        getApplication().dispatchActivityPreSaveInstanceState(this, outState);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        for (Object callback : callbacks) {
            ((Application.ActivityLifecycleCallbacks) callback)
                    .onActivityPreSaveInstanceState(this, outState);
        }
    }

    private void dispatchActivitySaveInstanceState(Bundle outState) {
        getApplication().dispatchActivitySaveInstanceState(this, outState);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        for (Object callback : callbacks) {
            ((Application.ActivityLifecycleCallbacks) callback)
                    .onActivitySaveInstanceState(this, outState);
        }
    }

    private void dispatchActivityPostSaveInstanceState(Bundle outState) {
        getApplication().dispatchActivityPostSaveInstanceState(this, outState);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        for (Object callback : callbacks) {
            ((Application.ActivityLifecycleCallbacks) callback)
                    .onActivityPostSaveInstanceState(this, outState);
        }
    }

    private void dispatchActivityPreDestroyed() {
        getApplication().dispatchActivityPreDestroyed(this);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        for (Object callback : callbacks) {
            ((Application.ActivityLifecycleCallbacks) callback)
                    .onActivityPreDestroyed(this);
        }
    }

    private void dispatchActivityDestroyed() {
        getApplication().dispatchActivityDestroyed(this);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        for (Object callback : callbacks) {
            ((Application.ActivityLifecycleCallbacks) callback)
                    .onActivityDestroyed(this);
        }
    }

    private void dispatchActivityPostDestroyed() {
        getApplication().dispatchActivityPostDestroyed(this);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        for (Object callback : callbacks) {
            ((Application.ActivityLifecycleCallbacks) callback)
                    .onActivityPostDestroyed(this);
        }
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
            // AOSP Activity.attach installs the activity as the window's
            // callback (mWindow.setCallback(this)); AppCompat wraps it.
            mWindow.setCallback(this);
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
