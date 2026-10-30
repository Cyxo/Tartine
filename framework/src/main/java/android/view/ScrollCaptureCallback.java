package android.view;

import android.graphics.Rect;
import android.os.CancellationSignal;
import java.util.function.Consumer;

    public interface ScrollCaptureCallback {

    public abstract void onScrollCaptureEnd(Runnable arg0);

    public abstract void onScrollCaptureImageRequest(ScrollCaptureSession arg0, CancellationSignal arg1, Rect arg2, Consumer<Rect> arg3);

    public abstract void onScrollCaptureSearch(CancellationSignal arg0, Consumer<Rect> arg1);

    public abstract void onScrollCaptureStart(ScrollCaptureSession arg0, CancellationSignal arg1, Runnable arg2);
    }
