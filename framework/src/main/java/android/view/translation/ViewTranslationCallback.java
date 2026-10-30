package android.view.translation;

import android.view.View;

    public interface ViewTranslationCallback {

    public abstract boolean onClearTranslation(View arg0);

    public abstract boolean onHideTranslation(View arg0);

    public abstract boolean onShowTranslation(View arg0);
    }
