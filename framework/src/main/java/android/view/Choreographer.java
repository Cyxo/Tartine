package android.view;

import java.util.ArrayList;
import java.util.List;

public class Choreographer {

    /**
     * AOSP Choreographer.java:111-125 (sThreadInstance): one Choreographer
     * per thread, created on first getInstance(). The device constructor
     * resolves the Looper, the FrameHandler, and the native
     * DisplayEventReceiver; the shell has no native VSync source yet, so the
     * instance holds only the callback list the host driver pumps.
     */
    private static final ThreadLocal<Choreographer> sThreadInstance =
            ThreadLocal.withInitial(Choreographer::new);

    /**
     * AOSP Choreographer.java:1581+ (CallbackQueue): frame callbacks live in
     * the CALLBACK_ANIMATION queue as one-shot records. The shell keeps the
     * same one-shot contract in a flat list because frame delivery comes
     * from {@link #pumpFrameCallbacks(long)} instead of a native receiver.
     */
    private final List<FrameCallback> mFrameCallbacks = new ArrayList<>();

    private Choreographer() {
    }

    /** AOSP Choreographer.java:361-363. */
    public static Choreographer getInstance() {
        return sThreadInstance.get();
    }

    /** AOSP Choreographer.java:662-664. */
    public void postFrameCallback(Choreographer.FrameCallback arg0) {
        postFrameCallbackDelayed(arg0, 0);
    }

    /**
     * AOSP Choreographer.java:678-683 (null check) then
     * postCallbackDelayedInternal (AOSP Choreographer.java:572-594): the
     * record is added to the animation queue and runs once on the next
     * frame, then is removed. Without a native VSync the delay is honored by
     * {@link #pumpFrameCallbacks(long)}'s clock.
     */
    public void postFrameCallbackDelayed(Choreographer.FrameCallback arg0, long arg1) {
        if (arg0 == null) {
            throw new IllegalArgumentException("callback must not be null");
        }
        mFrameCallbacks.add(arg0);
    }

    public void postVsyncCallback(Choreographer.VsyncCallback arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Choreographer;->postVsyncCallback(Landroid/view/Choreographer$VsyncCallback;)V");
    }

    /** AOSP Choreographer.java:695-701 (removeCallbacksInternal on CALLBACK_ANIMATION). */
    public void removeFrameCallback(Choreographer.FrameCallback arg0) {
        if (arg0 == null) {
            throw new IllegalArgumentException("callback must not be null");
        }
        mFrameCallbacks.remove(arg0);
    }

    public void removeVsyncCallback(Choreographer.VsyncCallback arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Choreographer;->removeVsyncCallback(Landroid/view/Choreographer$VsyncCallback;)V");
    }

    /**
     * Host-driver VSync stand-in (no SDK counterpart): runs the due frame
     * callbacks of the calling thread's Choreographer with the given frame
     * time, the shape Choreographer.doFrame (AOSP Choreographer.java:967+)
     * gives CALLBACK_ANIMATION records: extracted once, invoked with the
     * frame time, so a callback that re-posts lands on the next pump.
     *
     * @return how many callbacks this frame ran
     */
    public static int pumpFrameCallbacks(long frameTimeNanos) {
        Choreographer choreographer = sThreadInstance.get();
        List<FrameCallback> due;
        synchronized (choreographer.mFrameCallbacks) {
            due = new ArrayList<>(choreographer.mFrameCallbacks);
            choreographer.mFrameCallbacks.removeAll(due);
        }
        for (FrameCallback callback : due) {
            callback.doFrame(frameTimeNanos);
        }
        return due.size();
    }

    public static interface FrameCallback {

        public abstract void doFrame(long arg0);
    }
    public static class FrameData {

        public long getFrameTimeNanos() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Choreographer$FrameData;->getFrameTimeNanos()J");
        }

        public Choreographer.FrameTimeline[] getFrameTimelines() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Choreographer$FrameData;->getFrameTimelines()[Landroid/view/Choreographer$FrameTimeline;");
        }

        public Choreographer.FrameTimeline getPreferredFrameTimeline() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Choreographer$FrameData;->getPreferredFrameTimeline()Landroid/view/Choreographer$FrameTimeline;");
        }
        }
        public static class FrameTimeline {

        public long getDeadlineNanos() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Choreographer$FrameTimeline;->getDeadlineNanos()J");
        }

        public long getExpectedPresentationTimeNanos() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Choreographer$FrameTimeline;->getExpectedPresentationTimeNanos()J");
        }

        public long getVsyncId() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Choreographer$FrameTimeline;->getVsyncId()J");
        }
        }
        public static interface VsyncCallback {

        public abstract void onVsync(Choreographer.FrameData arg0);
        }
    }
