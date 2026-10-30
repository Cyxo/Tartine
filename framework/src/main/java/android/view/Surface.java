package android.view;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.os.Parcel;
import android.os.Parcelable;

    public class Surface implements Parcelable {

    public static final int CHANGE_FRAME_RATE_ALWAYS = 1;
    public static final int CHANGE_FRAME_RATE_ONLY_IF_SEAMLESS = 0;
    public static Parcelable.Creator CREATOR;
    public static final int FRAME_RATE_COMPATIBILITY_AT_LEAST = 2;
    public static final int FRAME_RATE_COMPATIBILITY_DEFAULT = 0;
    public static final int FRAME_RATE_COMPATIBILITY_FIXED_SOURCE = 1;
    public static final int ROTATION_0 = 0;
    public static final int ROTATION_180 = 2;
    public static final int ROTATION_270 = 3;
    public static final int ROTATION_90 = 1;

    public Surface(SurfaceTexture arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Surface;-><init>(Landroid/graphics/SurfaceTexture;)V");
    }

    public Surface(SurfaceControl arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Surface;-><init>(Landroid/view/SurfaceControl;)V");
    }

    public void clearFrameRate() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Surface;->clearFrameRate()V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Surface;->describeContents()I");
    }

    protected void finalize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Surface;->finalize()V");
    }

    public boolean isValid() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Surface;->isValid()Z");
    }

    public Canvas lockCanvas(Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Surface;->lockCanvas(Landroid/graphics/Rect;)Landroid/graphics/Canvas;");
    }

    public Canvas lockHardwareCanvas() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Surface;->lockHardwareCanvas()Landroid/graphics/Canvas;");
    }

    public void readFromParcel(Parcel arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Surface;->readFromParcel(Landroid/os/Parcel;)V");
    }

    public void release() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Surface;->release()V");
    }

    public void setFrameRate(float arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Surface;->setFrameRate(FI)V");
    }

    public void setFrameRate(float arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Surface;->setFrameRate(FII)V");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Surface;->toString()Ljava/lang/String;");
    }

    public void unlockCanvas(Canvas arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Surface;->unlockCanvas(Landroid/graphics/Canvas;)V");
    }

    public void unlockCanvasAndPost(Canvas arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Surface;->unlockCanvasAndPost(Landroid/graphics/Canvas;)V");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Surface;->writeToParcel(Landroid/os/Parcel;I)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public Surface() {
        }
    }
