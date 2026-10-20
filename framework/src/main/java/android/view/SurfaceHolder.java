package android.view;

    public interface SurfaceHolder {

    public static final int SURFACE_TYPE_GPU = 2;
    public static final int SURFACE_TYPE_HARDWARE = 1;
    public static final int SURFACE_TYPE_NORMAL = 0;
    public static final int SURFACE_TYPE_PUSH_BUFFERS = 3;

    public abstract void addCallback(android.view.SurfaceHolder.Callback arg0);

    public abstract android.view.Surface getSurface();

    public abstract android.graphics.Rect getSurfaceFrame();

    public abstract boolean isCreating();

    public abstract android.graphics.Canvas lockCanvas();

    public abstract android.graphics.Canvas lockCanvas(android.graphics.Rect arg0);

    public default android.graphics.Canvas lockHardwareCanvas() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceHolder;->lockHardwareCanvas()Landroid/graphics/Canvas;");
    }

    public abstract void removeCallback(android.view.SurfaceHolder.Callback arg0);

    public abstract void setFixedSize(int arg0, int arg1);

    public abstract void setFormat(int arg0);

    public abstract void setKeepScreenOn(boolean arg0);

    public abstract void setSizeFromLayout();

    public abstract void setType(int arg0);

    public abstract void unlockCanvasAndPost(android.graphics.Canvas arg0);
        public static interface Callback2 extends android.view.SurfaceHolder.Callback {

        public abstract void surfaceRedrawNeeded(android.view.SurfaceHolder arg0);

        public default void surfaceRedrawNeededAsync(android.view.SurfaceHolder arg0, java.lang.Runnable arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceHolder$Callback2;->surfaceRedrawNeededAsync(Landroid/view/SurfaceHolder;Ljava/lang/Runnable;)V");
        }
        }
        public static interface Callback {

        public abstract void surfaceChanged(android.view.SurfaceHolder arg0, int arg1, int arg2, int arg3);

        public abstract void surfaceCreated(android.view.SurfaceHolder arg0);

        public abstract void surfaceDestroyed(android.view.SurfaceHolder arg0);
        }
    }
