package tartine.graphics;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Threading pins for the host display seam: the window loop and guest
 * main-looper work always run on different threads, and guest/display
 * failures reach the caller after the window closes.
 */
class HostDisplayTest {

    @Test
    void frameRejectsBadSizeAndPixels() {
        assertThrows(IllegalArgumentException.class,
                () -> new HostDisplay.Frame(0, 1, new int[0]));
        assertThrows(IllegalArgumentException.class,
                () -> new HostDisplay.Frame(2, 2, new int[3]));
        assertThrows(IllegalArgumentException.class,
                () -> new HostDisplay.Frame(2, 2, null));
        assertEquals(4, new HostDisplay.Frame(2, 2, new int[4]).argb().length);
    }

    @Test
    void dedicatedDisplayThreadLeavesGuestOnCaller() throws Exception {
        FakeDisplay display = new FakeDisplay();
        AtomicReference<String> guestThread = new AtomicReference<>();
        HostDisplay.Frame frame =
                new HostDisplay.Frame(1, 1, new int[] {0xFF112233});

        HostDisplays.run(display, false, guest -> {
            guestThread.set(Thread.currentThread().getName());
            guest.postFrame(frame);
            guest.close();
        });

        assertEquals("fake-display", display.loopThread.get());
        assertNotEquals(display.loopThread.get(), guestThread.get());
        assertSame(frame, display.posted.get());
    }

    @Test
    void macPolicyRunsDisplayLoopOnCallerAndGuestOnItsOwnThread()
            throws Exception {
        FakeDisplay display = new FakeDisplay();
        String caller = Thread.currentThread().getName();
        AtomicReference<String> guestThread = new AtomicReference<>();

        HostDisplays.run(display, true, guest -> {
            guestThread.set(Thread.currentThread().getName());
            guest.postFrame(new HostDisplay.Frame(1, 1, new int[] {0}));
            guest.close();
        });

        assertEquals(caller, display.loopThread.get());
        assertEquals("tartine-guest", guestThread.get());
        assertNotEquals(caller, guestThread.get());
    }

    @Test
    void guestFailurePropagatesAfterTheDisplayCloses() {
        FakeDisplay display = new FakeDisplay();
        IllegalStateException failure = new IllegalStateException("guest");

        IllegalStateException thrown = assertThrows(IllegalStateException.class,
                () -> HostDisplays.run(display, false, guest -> {
                    throw failure;
                }));

        assertSame(failure, thrown);
        assertEquals(0, display.done.getCount());
    }

    @Test
    void macGuestFailurePropagatesAfterTheLoopReturns() {
        FakeDisplay display = new FakeDisplay();
        IllegalStateException failure = new IllegalStateException("guest");

        IllegalStateException thrown = assertThrows(IllegalStateException.class,
                () -> HostDisplays.run(display, true, guest -> {
                    throw failure;
                }));

        assertSame(failure, thrown);
    }

    @Test
    void displayFailureIsReportedToTheCaller() {
        FakeDisplay display = new FakeDisplay();
        IllegalStateException failure = new IllegalStateException("display");
        display.failure = failure;

        IllegalStateException thrown = assertThrows(IllegalStateException.class,
                () -> HostDisplays.run(display, false,
                        HostDisplay::close));

        assertSame(failure, thrown);
    }

    /** Minimal display loop that records its thread and delivers frames. */
    private static final class FakeDisplay implements DisplayLoop {
        private final CountDownLatch closeLatch = new CountDownLatch(1);
        private final CountDownLatch done = new CountDownLatch(1);
        private final AtomicBoolean started = new AtomicBoolean();
        private final AtomicReference<HostDisplay.Frame> posted =
                new AtomicReference<>();
        private final AtomicReference<String> loopThread =
                new AtomicReference<>();
        private volatile RuntimeException failure;

        @Override
        public void postFrame(Frame frame) {
            posted.set(frame);
        }

        @Override
        public void awaitClose() {
            try {
                done.await();
            } catch (InterruptedException interrupted) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException(interrupted);
            }
            if (failure != null) {
                throw failure;
            }
        }

        @Override
        public void close() {
            closeLatch.countDown();
        }

        @Override
        public void startDedicated() {
            requireFirstStart();
            Thread thread = new Thread(this::loop, "fake-display");
            thread.setDaemon(true);
            thread.start();
        }

        @Override
        public void runOnCallingThread() {
            requireFirstStart();
            loop();
        }

        private void requireFirstStart() {
            if (!started.compareAndSet(false, true)) {
                throw new IllegalStateException("already started");
            }
        }

        private void loop() {
            loopThread.set(Thread.currentThread().getName());
            try {
                closeLatch.await();
            } catch (InterruptedException interrupted) {
                Thread.currentThread().interrupt();
            } finally {
                done.countDown();
            }
        }
    }
}
