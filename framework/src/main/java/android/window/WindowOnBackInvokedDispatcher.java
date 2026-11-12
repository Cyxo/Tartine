package android.window;

import java.util.ArrayList;
import java.util.List;

/**
 * Tartine: the shell's back-navigation dispatcher, ported in shape from AOSP
 * {@code android.window.WindowOnBackInvokedDispatcher}. Guests register
 * callbacks at a priority; the highest-priority registration wins when the
 * back gesture is delivered. The desktop shell has no system back gesture, so
 * {@link #onBackInvoked()} runs only when the host explicitly dispatches one.
 */
public class WindowOnBackInvokedDispatcher implements OnBackInvokedDispatcher {

    /** One registered callback with its priority. */
    private static final class Entry {
        private final int priority;
        private final OnBackInvokedCallback callback;

        private Entry(int priority, OnBackInvokedCallback callback) {
            this.priority = priority;
            this.callback = callback;
        }
    }

    /** Highest priority first, insertion order within a priority. */
    private final List<Entry> mEntries = new ArrayList<>();

    public void registerOnBackInvokedCallback(int priority, OnBackInvokedCallback callback) {
        if (callback == null) {
            return;
        }
        int index = 0;
        while (index < mEntries.size() && mEntries.get(index).priority >= priority) {
            index++;
        }
        mEntries.add(index, new Entry(priority, callback));
    }

    public void unregisterOnBackInvokedCallback(OnBackInvokedCallback callback) {
        for (int i = mEntries.size() - 1; i >= 0; i--) {
            if (mEntries.get(i).callback == callback) {
                mEntries.remove(i);
            }
        }
    }

    /** Whether any callback is currently registered. */
    public boolean hasCallback() {
        return !mEntries.isEmpty();
    }

    /** Dispatches back to the highest-priority registered callback. */
    public void onBackInvoked() {
        if (!mEntries.isEmpty()) {
            mEntries.get(0).callback.onBackInvoked();
        }
    }
}