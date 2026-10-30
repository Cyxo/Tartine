package android.os;

    public class CancellationSignal {

    public CancellationSignal() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/CancellationSignal;-><init>()V");
    }

    public void cancel() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/CancellationSignal;->cancel()V");
    }

    public boolean isCanceled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/CancellationSignal;->isCanceled()Z");
    }

    public void setOnCancelListener(CancellationSignal.OnCancelListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/CancellationSignal;->setOnCancelListener(Landroid/os/CancellationSignal$OnCancelListener;)V");
    }

    public void throwIfCanceled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/CancellationSignal;->throwIfCanceled()V");
    }
        public static interface OnCancelListener {

        public abstract void onCancel();
        }
    }
