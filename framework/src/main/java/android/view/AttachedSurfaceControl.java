package android.view;

import android.graphics.Rect;
import android.graphics.Region;
import android.window.InputTransferToken;
import java.util.concurrent.Executor;

    public interface AttachedSurfaceControl {

    public default void addOnBufferTransformHintChangedListener(AttachedSurfaceControl.OnBufferTransformHintChangedListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/AttachedSurfaceControl;->addOnBufferTransformHintChangedListener(Landroid/view/AttachedSurfaceControl$OnBufferTransformHintChangedListener;)V");
    }

    public abstract boolean applyTransactionOnDraw(SurfaceControl.Transaction arg0);

    public abstract SurfaceControl.Transaction buildReparentTransaction(SurfaceControl arg0);

    public default int getBufferTransformHint() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/AttachedSurfaceControl;->getBufferTransformHint()I");
    }

    public default InputTransferToken getInputTransferToken() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/AttachedSurfaceControl;->getInputTransferToken()Landroid/window/InputTransferToken;");
    }

    public default SurfaceControl.OnJankDataListenerRegistration registerOnJankDataListener(Executor arg0, SurfaceControl.OnJankDataListener arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/AttachedSurfaceControl;->registerOnJankDataListener(Ljava/util/concurrent/Executor;Landroid/view/SurfaceControl$OnJankDataListener;)Landroid/view/SurfaceControl$OnJankDataListenerRegistration;");
    }

    public default void removeOnBufferTransformHintChangedListener(AttachedSurfaceControl.OnBufferTransformHintChangedListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/AttachedSurfaceControl;->removeOnBufferTransformHintChangedListener(Landroid/view/AttachedSurfaceControl$OnBufferTransformHintChangedListener;)V");
    }

    public default void setChildBoundingInsets(Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/AttachedSurfaceControl;->setChildBoundingInsets(Landroid/graphics/Rect;)V");
    }

    public default void setTouchableRegion(Region arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/AttachedSurfaceControl;->setTouchableRegion(Landroid/graphics/Region;)V");
    }
        public static interface OnBufferTransformHintChangedListener {

        public abstract void onBufferTransformHintChanged(int arg0);
        }
    }
