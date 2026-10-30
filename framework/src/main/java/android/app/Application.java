package android.app;

import android.content.ContextWrapper;
import android.os.Bundle;
import java.util.ArrayList;

/**
 * Tartine: minimal skeleton. Ported per-method from
 * aosp-frameworks-base/core/java/android/app/Application.java (AOSP):
 * <ul>
 *   <li>{@code registerActivityLifecycleCallbacks} /
 *       {@code unregisterActivityLifecycleCallbacks} — an
 *       ArrayList guarded by the list itself, append/remove under the same
 *       monitor (AOSP lines 301-308). The list is package-private so future
 *       Activity.performCreate dispatch hosts snapshot it like the AOSP
 *       does (line 585: {@code mActivityLifecycleCallbacks.toArray()}).</li>
 *   <li>The nested {@link ActivityLifecycleCallbacks} surface carries the
 *       AOSP pre/post split: the plain callbacks are abstract (guest code
 *       must implement them), the {@code onActivityPre/Post*} and
 *       {@code onActivityConfigurationChanged} defaults are inert until
 *       the corresponding shell-side lifecycle steps dispatch them.</li>
 *   <li>The AOSP ctor chains {@code super(null)} — the shell Application
 *       has no attached base context yet.</li>
 * </ul>
 */
public class Application extends ContextWrapper {

    /** AOSP line 58. */
    final ArrayList<ActivityLifecycleCallbacks> mActivityLifecycleCallbacks =
            new ArrayList<>();

    public Application() {
        super(null);
    }

    /** AOSP lines 300-303. */
    public void registerActivityLifecycleCallbacks(
            ActivityLifecycleCallbacks callback) {
        synchronized (mActivityLifecycleCallbacks) {
            mActivityLifecycleCallbacks.add(callback);
        }
    }

    /** AOSP lines 306-309. */
    public void unregisterActivityLifecycleCallbacks(
            ActivityLifecycleCallbacks callback) {
        synchronized (mActivityLifecycleCallbacks) {
            mActivityLifecycleCallbacks.remove(callback);
        }
    }

    /** AOSP Application$ActivityLifecycleCallbacks. */
    public interface ActivityLifecycleCallbacks {

        /** AOSP onActivityPreCreated default — inert until dispatch lands. */
        default void onActivityPreCreated(Activity activity,
                Bundle savedInstanceState) {
        }

        void onActivityCreated(Activity activity,
                Bundle savedInstanceState);

        /** AOSP onActivityPostCreated default. */
        default void onActivityPostCreated(Activity activity,
                Bundle savedInstanceState) {
        }

        /** AOSP onActivityPreStarted default. */
        default void onActivityPreStarted(Activity activity) {
        }

        void onActivityStarted(Activity activity);

        /** AOSP onActivityPostStarted default. */
        default void onActivityPostStarted(Activity activity) {
        }

        /** AOSP onActivityPreResumed default. */
        default void onActivityPreResumed(Activity activity) {
        }

        void onActivityResumed(Activity activity);

        /** AOSP onActivityPostResumed default. */
        default void onActivityPostResumed(Activity activity) {
        }

        /** AOSP onActivityPrePaused default. */
        default void onActivityPrePaused(Activity activity) {
        }

        void onActivityPaused(Activity activity);

        /** AOSP onActivityPostPaused default. */
        default void onActivityPostPaused(Activity activity) {
        }

        /** AOSP onActivityPreStopped default. */
        default void onActivityPreStopped(Activity activity) {
        }

        void onActivityStopped(Activity activity);

        /** AOSP onActivityPostStopped default. */
        default void onActivityPostStopped(Activity activity) {
        }

        /** AOSP onActivityPreSaveInstanceState default. */
        default void onActivityPreSaveInstanceState(Activity activity,
                Bundle outState) {
        }

        void onActivitySaveInstanceState(Activity activity,
                Bundle outState);

        /** AOSP onActivityPostSaveInstanceState default. */
        default void onActivityPostSaveInstanceState(Activity activity,
                Bundle outState) {
        }

        /** AOSP onActivityPreDestroyed default. */
        default void onActivityPreDestroyed(Activity activity) {
        }

        void onActivityDestroyed(Activity activity);

        /** AOSP onActivityPostDestroyed default. */
        default void onActivityPostDestroyed(Activity activity) {
        }

        /** AOSP onActivityConfigurationChanged (hide) default. */
        default void onActivityConfigurationChanged(Activity activity) {
        }
    }
}
