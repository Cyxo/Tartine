package tartine.graphics;

/**
 * Lifecycle half of a {@link HostDisplay}, kept package-private so the public
 * seam stays post/await/close. {@link HostDisplays} decides which thread owns
 * the loop: a dedicated host UI thread everywhere except macOS, where GLFW
 * requires the process main thread.
 */
interface DisplayLoop extends HostDisplay {

    /** Runs the window loop on a new dedicated host UI thread. */
    void startDedicated();

    /**
     * Runs the window loop on the calling thread (macOS process main). The
     * loop must clean up and release {@link #awaitClose} before returning;
     * a loop failure is reported by {@code awaitClose}, not thrown here.
     */
    void runOnCallingThread();
}
