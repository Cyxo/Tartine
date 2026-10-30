package android.os;

import android.util.Printer;

/**
 * Desktop mapping (Tartine). Ported from
 * aosp-frameworks-base/core/java/android/os/Handler.java:
 * <ul>
 *   <li>Constructors, {@code createAsync} and {@code getLooper} keep the AOSP
 *       shapes: a Handler binds the given {@link Looper} (or the current
 *       thread's looper), records the callback and the async flag, and
 *       reports the same {@code "Can't create handler inside thread ..."}
 *       failure when no Looper was prepared (Handler.java:228-236,
 *       :287-291, :305-312).</li>
 *   <li>The probe has no Looper pump yet, so posted work runs inline at the
 *       post site — the delivery contract is "runs inline". AOSP's
 *       {@code post} -> {@code sendMessageDelayed} -> {@code enqueueMessage}
 *       chain (Handler.java:438-440, :646-648, :680-690) lands unchanged
 *       once the desktop message pump exists; until then delays cannot be
 *       observed and asynchronous ordering has no effect.</li>
 *   <li>{@code removeCallbacks}/{@code removeMessages}/{@code hasMessages}
 *       therefore find nothing to remove/report.</li>
 * </ul>
 */
public class Handler {

    final Looper mLooper;
    final MessageQueue mQueue;
    final Callback mCallback;
    final boolean mAsynchronous;

    /** Handler.java:218-236. */
    public Handler() {
        this(null, false);
    }

    /** Handler.java:155-158. */
    public Handler(Handler.Callback arg0) {
        this(arg0, false);
    }

    /** Handler.java:165-167. */
    public Handler(Looper arg0) {
        this(arg0, null, false);
    }

    /** Handler.java:176-178. */
    public Handler(Looper arg0, Handler.Callback arg1) {
        this(arg0, arg1, false);
    }

    /** Handler.java:218-236. */
    public Handler(Handler.Callback arg0, boolean arg1) {
        this.mLooper = Looper.myLooper();
        if (mLooper == null) {
            throw new RuntimeException("Can't create handler inside thread "
                    + Thread.currentThread() + " that has not called Looper.prepare()");
        }
        mQueue = mLooper.getQueue();
        mCallback = arg0;
        mAsynchronous = arg1;
    }

    /** Handler.java:260-264 / Desktop mapping for the hidden 3-arg ctor. */
    private Handler(Looper arg0, Handler.Callback arg1,
            boolean arg2) {
        mLooper = arg0;
        mQueue = arg0.getQueue();
        mCallback = arg1;
        mAsynchronous = arg2;
    }

    /** Handler.java:287-291. */
    public static Handler createAsync(Looper arg0) {
        if (arg0 == null) {
            throw new NullPointerException("looper must not be null");
        }
        return new Handler(arg0, null, true);
    }

    /** Handler.java:305-312. */
    public static Handler createAsync(Looper arg0,
            Handler.Callback arg1) {
        if (arg0 == null) {
            throw new NullPointerException("looper must not be null");
        }
        if (arg1 == null) {
            throw new NullPointerException("callback must not be null");
        }
        return new Handler(arg0, arg1, true);
    }

    public void dispatchMessage(Message arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Handler;->dispatchMessage(Landroid/os/Message;)V");
    }

    public final void dump(Printer arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Handler;->dump(Landroid/util/Printer;Ljava/lang/String;)V");
    }

    /** Handler.java:349-351. */
    public final Looper getLooper() {
        return mLooper;
    }

    public String getMessageName(Message arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Handler;->getMessageName(Landroid/os/Message;)Ljava/lang/String;");
    }

    public void handleMessage(Message arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Handler;->handleMessage(Landroid/os/Message;)V");
    }

    public final boolean hasCallbacks(Runnable arg0) {
        // Inline delivery: nothing stays queued (see class javadoc).
        return false;
    }

    public final boolean hasMessages(int arg0) {
        // Inline delivery: nothing stays queued (see class javadoc).
        return false;
    }

    public final boolean hasMessages(int arg0, Object arg1) {
        // Inline delivery: nothing stays queued (see class javadoc).
        return false;
    }

    public final Message obtainMessage() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Handler;->obtainMessage()Landroid/os/Message;");
    }

    public final Message obtainMessage(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Handler;->obtainMessage(I)Landroid/os/Message;");
    }

    public final Message obtainMessage(int arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Handler;->obtainMessage(III)Landroid/os/Message;");
    }

    public final Message obtainMessage(int arg0, int arg1, int arg2, Object arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Handler;->obtainMessage(IIILjava/lang/Object;)Landroid/os/Message;");
    }

    public final Message obtainMessage(int arg0, Object arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Handler;->obtainMessage(ILjava/lang/Object;)Landroid/os/Message;");
    }

    /** Handler.java:438-440; desktop inline mapping (see class javadoc). */
    public final boolean post(Runnable arg0) {
        postDelayed(arg0, 0);
        return true;
    }

    /** Handler.java:467-469; desktop inline mapping (see class javadoc). */
    public final boolean postAtFrontOfQueue(Runnable arg0) {
        postDelayed(arg0, 0);
        return true;
    }

    /** Handler.java:563-566; desktop inline mapping (see class javadoc). */
    public final boolean postAtTime(Runnable arg0, Object arg1, long arg2) {
        postDelayed(arg0, 0);
        return true;
    }

    /** Handler.java:590-592; desktop inline mapping (see class javadoc). */
    public final boolean postAtTime(Runnable arg0, long arg1) {
        postDelayed(arg0, 0);
        return true;
    }

    /** Handler.java:540-543; desktop inline mapping (see class javadoc). */
    public final boolean postDelayed(Runnable arg0, Object arg1, long arg2) {
        postDelayed(arg0, 0);
        return true;
    }

    /** Handler.java:510-512; desktop inline mapping (see class javadoc). */
    public final boolean postDelayed(Runnable arg0, long arg1) {
        if (arg0 == null) {
            throw new NullPointerException("runnable must not be null");
        }
        arg0.run();
        return true;
    }

    /** Handler.java:624-626; inline delivery leaves nothing to remove. */
    public final void removeCallbacks(Runnable arg0) {
        // Nothing stays queued (see class javadoc).
    }

    /** Handler.java:633-635; inline delivery leaves nothing to remove. */
    public final void removeCallbacks(Runnable arg0, Object arg1) {
        // Nothing stays queued (see class javadoc).
    }

    /** Handler.java:647-649; inline delivery leaves nothing to remove. */
    public final void removeCallbacksAndMessages(Object arg0) {
        // Nothing stays queued (see class javadoc).
    }

    /** Handler.java:657-659; inline delivery leaves nothing to remove. */
    public final void removeMessages(int arg0) {
        // Nothing stays queued (see class javadoc).
    }

    /** Handler.java:670-672; inline delivery leaves nothing to remove. */
    public final void removeMessages(int arg0, Object arg1) {
        // Nothing stays queued (see class javadoc).
    }

    public final boolean sendEmptyMessage(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Handler;->sendEmptyMessage(I)Z");
    }

    public final boolean sendEmptyMessageAtTime(int arg0, long arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Handler;->sendEmptyMessageAtTime(IJ)Z");
    }

    public final boolean sendEmptyMessageDelayed(int arg0, long arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Handler;->sendEmptyMessageDelayed(IJ)Z");
    }

    public final boolean sendMessage(Message arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Handler;->sendMessage(Landroid/os/Message;)Z");
    }

    public final boolean sendMessageAtFrontOfQueue(Message arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Handler;->sendMessageAtFrontOfQueue(Landroid/os/Message;)Z");
    }

    public boolean sendMessageAtTime(Message arg0, long arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Handler;->sendMessageAtTime(Landroid/os/Message;J)Z");
    }

    public final boolean sendMessageDelayed(Message arg0, long arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Handler;->sendMessageDelayed(Landroid/os/Message;J)Z");
    }

    public String toString() {
        return "Handler (" + getClass().getName() + ") {"
                + (mCallback != null ? " callback=" + mCallback : "")
                + " async=" + mAsynchronous + "}";
    }

    public static interface Callback {

        public abstract boolean handleMessage(Message arg0);
    }
}
