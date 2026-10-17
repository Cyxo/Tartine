package android.graphics;

    public class SurfaceTexture {

    public SurfaceTexture(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/SurfaceTexture;-><init>(Z)V");
    }

    public SurfaceTexture(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/SurfaceTexture;-><init>(I)V");
    }

    public SurfaceTexture(int arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/SurfaceTexture;-><init>(IZ)V");
    }

    public void attachToGLContext(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/SurfaceTexture;->attachToGLContext(I)V");
    }

    public void detachFromGLContext() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/SurfaceTexture;->detachFromGLContext()V");
    }

    protected void finalize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/SurfaceTexture;->finalize()V");
    }

    public int getDataSpace() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/SurfaceTexture;->getDataSpace()I");
    }

    public long getTimestamp() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/SurfaceTexture;->getTimestamp()J");
    }

    public void getTransformMatrix(float[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/SurfaceTexture;->getTransformMatrix([F)V");
    }

    public boolean isReleased() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/SurfaceTexture;->isReleased()Z");
    }

    public void release() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/SurfaceTexture;->release()V");
    }

    public void releaseTexImage() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/SurfaceTexture;->releaseTexImage()V");
    }

    public void setDefaultBufferSize(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/SurfaceTexture;->setDefaultBufferSize(II)V");
    }

    public void setOnFrameAvailableListener(android.graphics.SurfaceTexture.OnFrameAvailableListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/SurfaceTexture;->setOnFrameAvailableListener(Landroid/graphics/SurfaceTexture$OnFrameAvailableListener;)V");
    }

    public void setOnFrameAvailableListener(android.graphics.SurfaceTexture.OnFrameAvailableListener arg0, android.os.Handler arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/SurfaceTexture;->setOnFrameAvailableListener(Landroid/graphics/SurfaceTexture$OnFrameAvailableListener;Landroid/os/Handler;)V");
    }

    public void updateTexImage() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/SurfaceTexture;->updateTexImage()V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public SurfaceTexture() {
        }
        public static interface OnFrameAvailableListener {

        public abstract void onFrameAvailable(android.graphics.SurfaceTexture arg0);
        }
    }
