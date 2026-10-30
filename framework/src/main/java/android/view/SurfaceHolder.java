package android.view;

import android.graphics.Canvas;
import android.graphics.Rect;

    public interface SurfaceHolder {

    public static final int SURFACE_TYPE_GPU = 2;
    public static final int SURFACE_TYPE_HARDWARE = 1;
    public static final int SURFACE_TYPE_NORMAL = 0;
    public static final int SURFACE_TYPE_PUSH_BUFFERS = 3;

    public abstract void addCallback(SurfaceHolder.Callback arg0);

    public abstract Surface getSurface();

    public abstract Rect getSurfaceFrame();

    public abstract boolean isCreating();

    public abstract Canvas lockCanvas();

    public abstract Canvas lockCanvas(Rect arg0);

    public default Canvas lockHardwareCanvas() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceHolder;->lockHardwareCanvas()Landroid/graphics/Canvas;");
    }

    public abstract void removeCallback(SurfaceHolder.Callback arg0);

    public abstract void setFixedSize(int arg0, int arg1);

    public abstract void setFormat(int arg0);

    public abstract void setKeepScreenOn(boolean arg0);

    public abstract void setSizeFromLayout();

    public abstract void setType(int arg0);

    public abstract void unlockCanvasAndPost(Canvas arg0);
        public static interface Callback2 extends SurfaceHolder.Callback {

        public abstract void surfaceRedrawNeeded(SurfaceHolder arg0);

        public default void surfaceRedrawNeededAsync(SurfaceHolder arg0, Runnable arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceHolder$Callback2;->surfaceRedrawNeededAsync(Landroid/view/SurfaceHolder;Ljava/lang/Runnable;)V");
        }
        }
        public static interface Callback {

        public abstract void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3);

        public abstract void surfaceCreated(SurfaceHolder arg0);

        public abstract void surfaceDestroyed(SurfaceHolder arg0);
        }
    }
