package android.view;

    public interface AttachedSurfaceControl {

    public default void addOnBufferTransformHintChangedListener(android.view.AttachedSurfaceControl.OnBufferTransformHintChangedListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/AttachedSurfaceControl;->addOnBufferTransformHintChangedListener(Landroid/view/AttachedSurfaceControl$OnBufferTransformHintChangedListener;)V");
    }

    public abstract boolean applyTransactionOnDraw(android.view.SurfaceControl.Transaction arg0);

    public abstract android.view.SurfaceControl.Transaction buildReparentTransaction(android.view.SurfaceControl arg0);

    public default int getBufferTransformHint() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/AttachedSurfaceControl;->getBufferTransformHint()I");
    }

    public default android.window.InputTransferToken getInputTransferToken() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/AttachedSurfaceControl;->getInputTransferToken()Landroid/window/InputTransferToken;");
    }

    public default android.view.SurfaceControl.OnJankDataListenerRegistration registerOnJankDataListener(java.util.concurrent.Executor arg0, android.view.SurfaceControl.OnJankDataListener arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/AttachedSurfaceControl;->registerOnJankDataListener(Ljava/util/concurrent/Executor;Landroid/view/SurfaceControl$OnJankDataListener;)Landroid/view/SurfaceControl$OnJankDataListenerRegistration;");
    }

    public default void removeOnBufferTransformHintChangedListener(android.view.AttachedSurfaceControl.OnBufferTransformHintChangedListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/AttachedSurfaceControl;->removeOnBufferTransformHintChangedListener(Landroid/view/AttachedSurfaceControl$OnBufferTransformHintChangedListener;)V");
    }

    public default void setChildBoundingInsets(android.graphics.Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/AttachedSurfaceControl;->setChildBoundingInsets(Landroid/graphics/Rect;)V");
    }

    public default void setTouchableRegion(android.graphics.Region arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/AttachedSurfaceControl;->setTouchableRegion(Landroid/graphics/Region;)V");
    }
        public static interface OnBufferTransformHintChangedListener {

        public abstract void onBufferTransformHintChanged(int arg0);
        }
    }
