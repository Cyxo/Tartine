package android.content;

import android.os.IBinder;

    public interface ServiceConnection {

    public default void onBindingDied(ComponentName arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ServiceConnection;->onBindingDied(Landroid/content/ComponentName;)V");
    }

    public default void onNullBinding(ComponentName arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ServiceConnection;->onNullBinding(Landroid/content/ComponentName;)V");
    }

    public abstract void onServiceConnected(ComponentName arg0, IBinder arg1);

    public abstract void onServiceDisconnected(ComponentName arg0);
    }
