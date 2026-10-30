package android.content;

/**
 * Clipboard service handle (android.jar stub re-embodied as shell Java).
 * Delegates to android.text.ClipboardManager's no-op shell semantics —
 * the desktop exposes no clipboard service.
 */
public class ClipboardManager extends android.text.ClipboardManager {

    /**
     * AOSP frameworks-base core/java/android/content/ClipboardManager.java#
     * ClipboardManager(Context): shell constructor delegates to the text
     * layer.
     */
    public ClipboardManager(Context context) {
        super(context);
    }

    @Override
    public boolean hasPrimaryClip() {
        return super.hasPrimaryClip();
    }

    @Override
    public ClipData getPrimaryClip() {
        return super.getPrimaryClip();
    }

    @Override
    public void setPrimaryClip(ClipData clip) {
        super.setPrimaryClip(clip);
    }

    @Override
    public void addPrimaryClipChangedListener(
            android.text.ClipboardManager.OnPrimaryClipChangedListener listener) {
        super.addPrimaryClipChangedListener(listener);
    }

    @Override
    public void removePrimaryClipChangedListener(
            android.text.ClipboardManager.OnPrimaryClipChangedListener listener) {
        super.removePrimaryClipChangedListener(listener);
    }
}
