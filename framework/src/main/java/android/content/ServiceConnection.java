package android.content;

    public interface ServiceConnection {

    public default void onBindingDied(android.content.ComponentName arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ServiceConnection;->onBindingDied(Landroid/content/ComponentName;)V");
    }

    public default void onNullBinding(android.content.ComponentName arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ServiceConnection;->onNullBinding(Landroid/content/ComponentName;)V");
    }

    public abstract void onServiceConnected(android.content.ComponentName arg0, android.os.IBinder arg1);

    public abstract void onServiceDisconnected(android.content.ComponentName arg0);
    }
