package android.os;

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
 *       MessageQueue; the desktop keeps the thread pin (AOSP ctor line
 *       {@code mThread = Thread.currentThread()}) but leaves the queue null:
 *       message delivery here is pumped by the desktop frame clock
 *       (Choreographer seam) once it lands, not by Linux epoll middleware.
 *       Queue accessors therefore throw the named gap.</li>
 *   <li>{@code loop()}/{@code quit()}/{@code quitSafely()}/{@code dump} stay
 *       named gaps for the same reason.</li>
 * </ul>
 */
public class Looper {

    /** AOSP line 80: per-thread looper table. */
    static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<>();

    /** AOSP line 83: main looper, guarded by Looper.class. */
    private static Looper sMainLooper;

    /** AOSP ctor delivers this; the desktop pump replaces its delivery. */
    private MessageQueue mQueue;

    /** AOSP ctor line 433. */
    private final Thread mThread;

    /** AOSP line 432 (minus the MessageQueue construction, see header). */
    private Looper() {
        mThread = Thread.currentThread();
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
        System.err.println("tx: Looper.getMainLooper() -> " + sMainLooper
                + " from=" + Thread.currentThread().getName());
        synchronized (Looper.class) {
            return sMainLooper;
        }
    }

    /** AOSP myLooper(): the thread's looper or null. */
    public static Looper myLooper() {
        return sThreadLocal.get();
    }

    /** AOSP myQueue(): myLooper().mQueue — the queue is a named gap. */
    public static MessageQueue myQueue() {
        Looper me = myLooper();
        if (me == null) {
            throw new UnsupportedOperationException(
                    "Tartine: not implemented: Landroid/os/Looper;->myQueue()Landroid/os/MessageQueue;");
        }
        return me.mQueue;
    }

    public MessageQueue getQueue() {
        return mQueue;
    }

    /** AOSP getThread(): the looper's owning thread. */
    public Thread getThread() {
        System.err.println("tx: Looper.getThread() mThread=" + mThread
                + " current=" + Thread.currentThread()
                + " same=" + (mThread == Thread.currentThread()));
        return mThread;
    }

    /** AOSP line 440. */
    public boolean isCurrentThread() {
        return Thread.currentThread() == mThread;
    }

    public static void loop() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Looper;->loop()V");
    }

    public void quit() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Looper;->quit()V");
    }

    public void quitSafely() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Looper;->quitSafely()V");
    }

    public void setMessageLogging(android.util.Printer arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Looper;->setMessageLogging(Landroid/util/Printer;)V");
    }

    public void dump(android.util.Printer arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Looper;->dump(Landroid/util/Printer;Ljava/lang/String;)V");
    }

    public java.lang.String toString() {
        return "Looper (" + mThread.getName()
                + ", desktop main-thread looper)";
    }
}
