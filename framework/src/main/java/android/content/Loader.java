package android.content;

    public class Loader<D> {

    public Loader(android.content.Context arg0) {
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

    public java.lang.String dataToString(D arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->dataToString(Ljava/lang/Object;)Ljava/lang/String;");
    }

    public void deliverCancellation() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->deliverCancellation()V");
    }

    public void deliverResult(D arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->deliverResult(Ljava/lang/Object;)V");
    }

    public void dump(java.lang.String arg0, java.io.FileDescriptor arg1, java.io.PrintWriter arg2, java.lang.String[] arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->dump(Ljava/lang/String;Ljava/io/FileDescriptor;Ljava/io/PrintWriter;[Ljava/lang/String;)V");
    }

    public void forceLoad() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->forceLoad()V");
    }

    public android.content.Context getContext() {
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

    public void registerListener(int arg0, android.content.Loader.OnLoadCompleteListener<D> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->registerListener(ILandroid/content/Loader$OnLoadCompleteListener;)V");
    }

    public void registerOnLoadCanceledListener(android.content.Loader.OnLoadCanceledListener<D> arg0) {
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

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->toString()Ljava/lang/String;");
    }

    public void unregisterListener(android.content.Loader.OnLoadCompleteListener<D> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->unregisterListener(Landroid/content/Loader$OnLoadCompleteListener;)V");
    }

    public void unregisterOnLoadCanceledListener(android.content.Loader.OnLoadCanceledListener<D> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Loader;->unregisterOnLoadCanceledListener(Landroid/content/Loader$OnLoadCanceledListener;)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public Loader() {
        }
        public static interface OnLoadCanceledListener<D> {

        public abstract void onLoadCanceled(android.content.Loader<D> arg0);
        }
        public static interface OnLoadCompleteListener<D> {

        public abstract void onLoadComplete(android.content.Loader<D> arg0, D arg1);
        }
    }
