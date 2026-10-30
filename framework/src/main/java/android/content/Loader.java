package android.content;

import java.io.FileDescriptor;
import java.io.PrintWriter;

    public class Loader<D> {

    public Loader(Context arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;-><init>(Landroid/content/Context;)V");
    }

    public void abandon() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->abandon()V");
    }

    public boolean cancelLoad() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->cancelLoad()Z");
    }

    public void commitContentChanged() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->commitContentChanged()V");
    }

    public String dataToString(D arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->dataToString(Ljava/lang/Object;)Ljava/lang/String;");
    }

    public void deliverCancellation() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->deliverCancellation()V");
    }

    public void deliverResult(D arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->deliverResult(Ljava/lang/Object;)V");
    }

    public void dump(String arg0, FileDescriptor arg1, PrintWriter arg2, String[] arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->dump(Ljava/lang/String;Ljava/io/FileDescriptor;Ljava/io/PrintWriter;[Ljava/lang/String;)V");
    }

    public void forceLoad() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->forceLoad()V");
    }

    public Context getContext() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->getContext()Landroid/content/Context;");
    }

    public int getId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->getId()I");
    }

    public boolean isAbandoned() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->isAbandoned()Z");
    }

    public boolean isReset() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->isReset()Z");
    }

    public boolean isStarted() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->isStarted()Z");
    }

    protected void onAbandon() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->onAbandon()V");
    }

    protected boolean onCancelLoad() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->onCancelLoad()Z");
    }

    public void onContentChanged() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->onContentChanged()V");
    }

    protected void onForceLoad() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->onForceLoad()V");
    }

    protected void onReset() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->onReset()V");
    }

    protected void onStartLoading() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->onStartLoading()V");
    }

    protected void onStopLoading() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->onStopLoading()V");
    }

    public void registerListener(int arg0, Loader.OnLoadCompleteListener<D> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->registerListener(ILandroid/content/Loader$OnLoadCompleteListener;)V");
    }

    public void registerOnLoadCanceledListener(Loader.OnLoadCanceledListener<D> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->registerOnLoadCanceledListener(Landroid/content/Loader$OnLoadCanceledListener;)V");
    }

    public void reset() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->reset()V");
    }

    public void rollbackContentChanged() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->rollbackContentChanged()V");
    }

    public final void startLoading() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->startLoading()V");
    }

    public void stopLoading() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->stopLoading()V");
    }

    public boolean takeContentChanged() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->takeContentChanged()Z");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->toString()Ljava/lang/String;");
    }

    public void unregisterListener(Loader.OnLoadCompleteListener<D> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->unregisterListener(Landroid/content/Loader$OnLoadCompleteListener;)V");
    }

    public void unregisterOnLoadCanceledListener(Loader.OnLoadCanceledListener<D> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->unregisterOnLoadCanceledListener(Landroid/content/Loader$OnLoadCanceledListener;)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public Loader() {
        }
        public static interface OnLoadCanceledListener<D> {

        public abstract void onLoadCanceled(Loader<D> arg0);
        }
        public static interface OnLoadCompleteListener<D> {

        public abstract void onLoadComplete(Loader<D> arg0, D arg1);
        }
    }
