package android.app;

import android.content.Context;
import android.content.ContextWrapper;

/**
 * Tartine: minimal skeleton for the translation spike. The fw-gen closure
 * for the real class is deferred (regen cannot merge into existing parent
 * files yet); grows when a wall demands its members.
 */
public class ContextThemeWrapper extends ContextWrapper {

    public ContextThemeWrapper() {
    }

    public ContextThemeWrapper(Context arg0, int arg1) {
        super(arg0);
    }
}
