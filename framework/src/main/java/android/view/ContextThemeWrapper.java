package android.view;

import android.content.Context;
import android.content.res.Resources;

/**
 * Tartine: the public context-theme wrapper, mirroring AOSP
 * {@code android.view.ContextThemeWrapper}. All behavior lives in its
 * superclass {@link android.app.ContextThemeWrapper}; this class only widens
 * the constructor surface the public API exposes (and which AppCompat
 * targets).
 */
public class ContextThemeWrapper extends android.app.ContextThemeWrapper {

    public ContextThemeWrapper() {
        super();
    }

    public ContextThemeWrapper(Context base, Resources.Theme theme) {
        super(base, theme);
    }

    public ContextThemeWrapper(Context base, int themeResId) {
        super(base, themeResId);
    }
}
