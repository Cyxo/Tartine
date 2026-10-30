package android.text;

import android.content.ClipData;
import android.content.Context;

/**
 * Clipboard service handle (android.jar stub re-embodied as shell Java).
 * The desktop exposes no clipboard service; every query answers the empty
 * state and every listener registration is a no-op, matching a device
 * with no bound clipboard manager.
 */
public class ClipboardManager {

    /**
     * AOSP frameworks-base core/java/android/text/ClipboardManager.java#ClipboardManager(Context):
     * base shell no-op — holds the context for later delegation.
     */
    public ClipboardManager(Context context) {
    }

    /**
     * AOSP .../ClipboardManager.java#hasPrimaryClip: no service → no clip.
     */
    public boolean hasPrimaryClip() {
        return false;
    }

    /**
     * AOSP .../ClipboardManager.java#getPrimaryClip: no service → null;
     * consumers (Compose clipboard bookkeeping) must tolerate null.
     */
    public ClipData getPrimaryClip() {
        return null;
    }

    /**
     * AOSP .../ClipboardManager.java#setPrimaryClip: no service → dropped.
     */
    public void setPrimaryClip(ClipData clip) {
    }

    /**
     * AOSP .../ClipboardManager.java#addPrimaryClipChangedListener: no
     * service → never fires; registration is bookkeeping only.
     */
    public void addPrimaryClipChangedListener(OnPrimaryClipChangedListener listener) {
    }

    /**
     * AOSP .../ClipboardManager.java#removePrimaryClipChangedListener.
     */
    public void removePrimaryClipChangedListener(OnPrimaryClipChangedListener listener) {
    }

    /** Listener contract from android.jar signatures. */
    public interface OnPrimaryClipChangedListener {
        void onPrimaryClipChanged();
    }
}
