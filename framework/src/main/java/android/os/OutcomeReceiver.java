package android.os;

    public interface OutcomeReceiver<R, E> {

    public default void onError(E arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/OutcomeReceiver;->onError(Ljava/lang/Throwable;)V");
    }

    public abstract void onResult(R arg0);
    }
