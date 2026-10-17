package android.os;

    public interface IBinder {

    public static final int DUMP_TRANSACTION = 1598311760;
    public static final int FIRST_CALL_TRANSACTION = 1;
    public static final int FLAG_ONEWAY = 1;
    public static final int INTERFACE_TRANSACTION = 1598968902;
    public static final int LAST_CALL_TRANSACTION = 16777215;
    public static final int LIKE_TRANSACTION = 1598835019;
    public static final int PING_TRANSACTION = 1599098439;
    public static final int TWEET_TRANSACTION = 1599362900;

    public default void addFrozenStateChangeCallback(java.util.concurrent.Executor arg0, android.os.IBinder.FrozenStateChangeCallback arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/IBinder;->addFrozenStateChangeCallback(Ljava/util/concurrent/Executor;Landroid/os/IBinder$FrozenStateChangeCallback;)V");
    }

    public abstract void dump(java.io.FileDescriptor arg0, java.lang.String[] arg1);

    public abstract void dumpAsync(java.io.FileDescriptor arg0, java.lang.String[] arg1);

    public abstract java.lang.String getInterfaceDescriptor();

    public static int getSuggestedMaxIpcSizeBytes() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/IBinder;->getSuggestedMaxIpcSizeBytes()I");
    }

    public abstract boolean isBinderAlive();

    public abstract void linkToDeath(android.os.IBinder.DeathRecipient arg0, int arg1);

    public abstract boolean pingBinder();

    public abstract android.os.IInterface queryLocalInterface(java.lang.String arg0);

    public default boolean removeFrozenStateChangeCallback(android.os.IBinder.FrozenStateChangeCallback arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/IBinder;->removeFrozenStateChangeCallback(Landroid/os/IBinder$FrozenStateChangeCallback;)Z");
    }

    public abstract boolean transact(int arg0, android.os.Parcel arg1, android.os.Parcel arg2, int arg3);

    public abstract boolean unlinkToDeath(android.os.IBinder.DeathRecipient arg0, int arg1);
        public static interface DeathRecipient {

        public abstract void binderDied();

        public default void binderDied(android.os.IBinder arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/IBinder$DeathRecipient;->binderDied(Landroid/os/IBinder;)V");
        }
        }
        public static interface FrozenStateChangeCallback {

        public static final int STATE_FROZEN = 0;
        public static final int STATE_UNFROZEN = 1;

        public abstract void onFrozenStateChanged(android.os.IBinder arg0, int arg1);
        }
    }
