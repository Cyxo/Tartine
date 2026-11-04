package android.os;

import android.util.Printer;
import java.util.logging.Logger;

/**
 * Desktop mapping (Tartine). Ported per-method from
 * aosp-frameworks-base/core/java/android/os/Looper.java (AOSP):
 * <ul>
 *   <li>{@code prepare}/{@code prepareMainLooper}/{@code myLooper}/
 *       {@code getMainLooper} — verbatim AOSP mechanics: a per-thread
 *       ThreadLocal plus the {@code sMainLooper} static guarded by
 *       {@code Looper.class} ("Only one Looper may be created per thread",
 *       "The main Looper has already been prepared.").</li>
 *   <li>The AOSP constructor pins the owning thread and builds the
 *       MessageQueue; the desktop does the same. The queue is the host
 *       driver's action channel: the display thread posts input/close
 *       actions, {@link #loop()} runs them on the looper thread, and
 *       {@link MessageQueue#quit()} asks the loop to return. This is the
 *       desktop stand-in for Linux epoll middleware, not a frame clock.</li>
 *   <li>{@code dump} stays a named gap; {@code setMessageLogging} remains
 *       one too (no Printer sink yet).</li>
 * </ul>
 */
public class Looper {
    private static final Logger log = Logger.getLogger(Looper.class.getName());

    /** AOSP line 80: per-thread looper table. */
    static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<>();

    /** AOSP line 83: main looper, guarded by Looper.class. */
    private static Looper sMainLooper;

    /** AOSP ctor line 432: the queue the looper thread pumps. */
    private final MessageQueue mQueue;

    /** AOSP ctor line 433. */
    private final Thread mThread;

    /** AOSP line 432: pin the thread and build its queue. */
    private Looper() {
        mThread = Thread.currentThread();
        mQueue = new MessageQueue();
    }

    /** AOSP prepare(true). */
    public static void prepare() {
        prepare(true);
    }

    /** AOSP line 118ff. */
    private static void prepare(boolean quitAllowed) {
        if (sThreadLocal.get() != null) {
            throw new RuntimeException("Only one Looper may be created per thread");
        }
        sThreadLocal.set(new Looper());
    }

    /** AOSP line 135ff. */
    public static void prepareMainLooper() {
        prepare(false);
        synchronized (Looper.class) {
            if (sMainLooper != null) {
                throw new IllegalStateException("The main Looper has already been prepared.");
            }
            sMainLooper = myLooper();
        }
    }

    /** AOSP line 149ff. */
    public static Looper getMainLooper() {
        log.finest("tx: Looper.getMainLooper() -> " + sMainLooper
                + " from=" + Thread.currentThread().getName());
        synchronized (Looper.class) {
            return sMainLooper;
        }
    }

    /** AOSP myLooper(): the thread's looper or null. */
    public static Looper myLooper() {
        return sThreadLocal.get();
    }

    /** AOSP myQueue(): myLooper().mQueue. */
    public static MessageQueue myQueue() {
        Looper me = myLooper();
        if (me == null) {
            throw new NullPointerException(
                    "myQueue() called without a prepared Looper");
        }
        return me.mQueue;
    }

    public MessageQueue getQueue() {
        return mQueue;
    }

    /** AOSP getThread(): the looper's owning thread. */
    public Thread getThread() {
        log.finest("tx: Looper.getThread() mThread=" + mThread
                + " current=" + Thread.currentThread()
                + " same=" + (mThread == Thread.currentThread()));
        return mThread;
    }

    /** AOSP line 440. */
    public boolean isCurrentThread() {
        return Thread.currentThread() == mThread;
    }

    /**
     * AOSP loop(): run the thread's queue until {@link #quit()}. The
     * desktop queue holds host actions (input, close), so this is the
     * guest-side pump for {@code HostDisplay} events.
     */
    public static void loop() {
        Looper me = myLooper();
        if (me == null) {
            throw new RuntimeException("No Looper; Looper.prepare() wasn't "
                    + "called on this thread.");
        }
        MessageQueue queue = me.mQueue;
        while (true) {
            Runnable action = queue.take();
            if (action == null) {
                return;
            }
            action.run();
        }
    }

    /**
     * AOSP quit(): drop pending actions and make {@link #loop()} return.
     * AOSP refuses to quit the main looper; the desktop allows it because a
     * closed window is the process-exit stand-in until activity finish
     * exists.
     */
    public void quit() {
        mQueue.quit();
    }

    /**
     * AOSP quitSafely(): the desktop queue holds plain actions, so this
     * folds into {@link #quit()}.
     */
    public void quitSafely() {
        quit();
    }

    public void setMessageLogging(Printer arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Looper;->setMessageLogging(Landroid/util/Printer;)V");
    }

    public void dump(Printer arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Looper;->dump(Landroid/util/Printer;Ljava/lang/String;)V");
    }

    public String toString() {
        return "Looper (" + mThread.getName()
                + ", desktop main-thread looper)";
    }
}
