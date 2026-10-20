package android.view;

    public class Choreographer {

    public static android.view.Choreographer getInstance() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Choreographer;->getInstance()Landroid/view/Choreographer;");
    }

    public void postFrameCallback(android.view.Choreographer.FrameCallback arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Choreographer;->postFrameCallback(Landroid/view/Choreographer$FrameCallback;)V");
    }

    public void postFrameCallbackDelayed(android.view.Choreographer.FrameCallback arg0, long arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Choreographer;->postFrameCallbackDelayed(Landroid/view/Choreographer$FrameCallback;J)V");
    }

    public void postVsyncCallback(android.view.Choreographer.VsyncCallback arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Choreographer;->postVsyncCallback(Landroid/view/Choreographer$VsyncCallback;)V");
    }

    public void removeFrameCallback(android.view.Choreographer.FrameCallback arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Choreographer;->removeFrameCallback(Landroid/view/Choreographer$FrameCallback;)V");
    }

    public void removeVsyncCallback(android.view.Choreographer.VsyncCallback arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Choreographer;->removeVsyncCallback(Landroid/view/Choreographer$VsyncCallback;)V");
    }
        public static interface FrameCallback {

        public abstract void doFrame(long arg0);
        }
        public static class FrameData {

        public long getFrameTimeNanos() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Choreographer$FrameData;->getFrameTimeNanos()J");
        }

        public android.view.Choreographer.FrameTimeline[] getFrameTimelines() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Choreographer$FrameData;->getFrameTimelines()[Landroid/view/Choreographer$FrameTimeline;");
        }

        public android.view.Choreographer.FrameTimeline getPreferredFrameTimeline() {
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

        public abstract void onVsync(android.view.Choreographer.FrameData arg0);
        }
    }
