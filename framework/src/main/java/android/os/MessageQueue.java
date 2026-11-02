package android.os;

import java.io.FileDescriptor;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Desktop mapping (Tartine). The device queue is epoll-backed and delivers
 * {@code Message}s; the desktop keeps only the thread-safe action queue the
 * host driver needs: {@link #post(Runnable)} from any thread, {@link #take()}
 * on the looper thread. {@link Looper#loop()} drains it, and {@link #quit()}
 * discards pending work and wakes the pump.
 *
 * <p>The idle-handler and file-descriptor-listener APIs stay named gaps:
 * the desktop has no idle/epoll middleware yet.
 */
public class MessageQueue {

    private final Deque<Runnable> mActions = new ArrayDeque<>();
    private boolean mQuitting;

    /**
     * Host-driver seam (no SDK counterpart): posts an action to this queue
     * from any thread. Posts after {@link #quit()} are dropped, like AOSP.
     */
    public void post(Runnable action) {
        if (action == null) {
            throw new NullPointerException("action");
        }
        synchronized (this) {
            if (mQuitting) {
                return;
            }
            mActions.add(action);
            notifyAll();
        }
    }

    /**
     * {@code MessageQueue.next()} stand-in: blocks until an action is
     * available or the queue quits. A {@code null} result asks the looper to
     * return.
     */
    Runnable take() {
        synchronized (this) {
            while (mActions.isEmpty() && !mQuitting) {
                try {
                    wait();
                } catch (InterruptedException interrupted) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            }
            return mActions.poll();
        }
    }

    /** AOSP {@code quit()}: discard pending work and unblock the pump. */
    public void quit() {
        synchronized (this) {
            mQuitting = true;
            mActions.clear();
            notifyAll();
        }
    }

    public void addIdleHandler(MessageQueue.IdleHandler arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/MessageQueue;->addIdleHandler(Landroid/os/MessageQueue$IdleHandler;)V");
    }

    public void addOnFileDescriptorEventListener(FileDescriptor arg0, int arg1, MessageQueue.OnFileDescriptorEventListener arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/MessageQueue;->addOnFileDescriptorEventListener(Ljava/io/FileDescriptor;ILandroid/os/MessageQueue$OnFileDescriptorEventListener;)V");
    }

    protected void finalize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/MessageQueue;->finalize()V");
    }

    public boolean isIdle() {
        synchronized (this) {
            return mActions.isEmpty();
        }
    }

    public void removeIdleHandler(MessageQueue.IdleHandler arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/MessageQueue;->removeIdleHandler(Landroid/os/MessageQueue$IdleHandler;)V");
    }

    public void removeOnFileDescriptorEventListener(FileDescriptor arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/MessageQueue;->removeOnFileDescriptorEventListener(Ljava/io/FileDescriptor;)V");
    }
        public static interface IdleHandler {

        public abstract boolean queueIdle();
        }
        public static interface OnFileDescriptorEventListener {
        public static final int EVENT_ERROR = 4;
        public static final int EVENT_INPUT = 1;
        public static final int EVENT_OUTPUT = 2;

        public abstract int onFileDescriptorEvents(FileDescriptor arg0, int arg1);
        }
    }
