package android.content;

import android.content.res.Configuration;

    public interface ComponentCallbacks {

    public abstract void onConfigurationChanged(Configuration arg0);

    public abstract void onLowMemory();
    }
