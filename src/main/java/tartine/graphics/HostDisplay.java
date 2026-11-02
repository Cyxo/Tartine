package tartine.graphics;

/**
 * Host display seam: the windowing side of the graphics mapping
 * (SurfaceFlinger-lite). The implementation owns GLFW on its host UI thread
 * and presents finished rasters there; guest-driven drawing stays on the
 * guest main-looper thread and only hands frames across this interface.
 *
 * <p>The frame path is the Choreographer to SurfaceFlinger shape: draw and
 * read back on the guest thread, {@link #postFrame} the packed pixels, and
 * let the host UI thread upload/present them at VSync. The guest never calls
 * GLFW.
 */
public interface HostDisplay extends AutoCloseable {

    /**
     * One finished frame: packed premultiplied ARGB ints, row-major.
     * Ownership transfers to the display on {@link #postFrame}; callers must
     * not mutate the array afterwards.
     */
    record Frame(int width, int height, int[] argb) {
        public Frame {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException(
                        "frame size must be positive: " + width + "x" + height);
            }
            if (argb == null || argb.length != width * height) {
                throw new IllegalArgumentException("frame pixels must be "
                        + (width * height) + " ints");
            }
        }
    }

    /**
     * Hands a finished frame to the host UI thread. Callable from the guest
     * main-looper thread (or any other); the newest frame wins when
     * presentation is behind.
     */
    void postFrame(Frame frame);

    /** Waits until the window closes, by user request or timeout. */
    void awaitClose();

    /**
     * Requests the window to close. Idempotent and callable from any thread;
     * the host UI thread notices on its next loop iteration.
     */
    @Override
    void close();
}
