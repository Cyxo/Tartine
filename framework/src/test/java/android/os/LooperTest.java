package android.os;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Queue and loop pins for the desktop Looper event channel. */
class LooperTest {

    @Test
    void hostPostedActionRunsOnTheLooperThreadAndQuitEndsTheLoop()
            throws Exception {
        AtomicReference<String> actionThread = new AtomicReference<>();
        CountDownLatch ran = new CountDownLatch(1);
        CountDownLatch loopEnded = new CountDownLatch(1);
        Thread looper = new Thread(() -> {
            try {
                Looper.prepare();
                MessageQueue queue = Looper.myQueue();
                // Post from a different thread before the loop starts, the
                // shape the display thread uses for host events.
                Thread poster = new Thread(() -> queue.post(() -> {
                    actionThread.set(Thread.currentThread().getName());
                    ran.countDown();
                    Looper.myLooper().quit();
                }), "event-poster");
                poster.start();
                Looper.loop();
            } finally {
                loopEnded.countDown();
            }
        }, "test-looper");
        looper.start();

        assertTrue(ran.await(2, TimeUnit.SECONDS));
        assertTrue(loopEnded.await(2, TimeUnit.SECONDS));
        assertEquals("test-looper", actionThread.get());
    }

    @Test
    void quitBeforeLoopReturnsImmediately() throws Exception {
        AtomicBoolean ran = new AtomicBoolean();
        Thread looper = new Thread(() -> {
            Looper.prepare();
            Looper.myLooper().quit();
            Looper.myQueue().post(() -> ran.set(true));
            Looper.loop();
        }, "test-looper-quit");
        looper.start();
        looper.join(2_000);
        assertFalse(looper.isAlive());
        assertFalse(ran.get());
    }

    @Test
    void queueDropsPostsAfterQuit() {
        MessageQueue queue = new MessageQueue();
        AtomicBoolean ran = new AtomicBoolean();
        queue.quit();
        queue.post(() -> ran.set(true));
        assertTrue(queue.isIdle());
        assertFalse(ran.get());
    }
}
