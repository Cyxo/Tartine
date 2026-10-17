package android.view;

    public interface ScrollCaptureCallback {

    public abstract void onScrollCaptureEnd(java.lang.Runnable arg0);

    public abstract void onScrollCaptureImageRequest(android.view.ScrollCaptureSession arg0, android.os.CancellationSignal arg1, android.graphics.Rect arg2, java.util.function.Consumer<android.graphics.Rect> arg3);

    public abstract void onScrollCaptureSearch(android.os.CancellationSignal arg0, java.util.function.Consumer<android.graphics.Rect> arg1);

    public abstract void onScrollCaptureStart(android.view.ScrollCaptureSession arg0, android.os.CancellationSignal arg1, java.lang.Runnable arg2);
    }
