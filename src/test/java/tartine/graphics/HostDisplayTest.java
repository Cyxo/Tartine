package tartine.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Threading and event pins for the host display seam: the window loop and
 * guest main-looper work always run on different threads, frames and events
 * are reported across the seam, and guest/display failures reach the caller
 * after the window closes without hanging on a guest that ignores it.
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
    void eventChannelBuffersAndReplaysUntilTheListenerArrives() {
        HostEventChannel channel = new HostEventChannel();
        List<HostDisplay.Event> seen = new ArrayList<>();
        HostDisplay.Key key = new HostDisplay.Key(65, 30,
                HostDisplay.Action.PRESS, 1);
        HostDisplay.Resized resized = new HostDisplay.Resized(1200, 800);
        HostDisplay.Closed closed = new HostDisplay.Closed();

        channel.publish(key);
        channel.publish(resized);
        channel.publish(closed);
        channel.setListener(seen::add);

        assertEquals(List.of(key, resized, closed), seen);

        HostDisplay.FocusChanged focus =
                new HostDisplay.FocusChanged(true);
        channel.publish(focus);
        assertSame(focus, seen.get(seen.size() - 1));
        assertEquals(4, seen.size());
    }

    @Test
    void dedicatedDisplayThreadLeavesGuestOnCaller() throws Exception {
        FakeDisplay display = new FakeDisplay();
        AtomicReference<String> guestThread = new AtomicReference<>();
        HostDisplay.Frame frame =
                new HostDisplay.Frame(1, 1, new int[] {0xFF112233});

        HostDisplays.run(display, false, guest -> {
            guestThread.set(Thread.currentThread().getName());
            assertTrue(guest.postFrame(frame));
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
    void windowCloseWhileGuestRunsReachesTheGuest() throws Exception {
        FakeDisplay display = new FakeDisplay();
        display.exitLoopOnStart = true;
        CountDownLatch closeEvent = new CountDownLatch(1);
        AtomicReference<HostDisplay.Event> seen = new AtomicReference<>();
        ExecutorService runner = Executors.newSingleThreadExecutor();

        try {
            Future<?> run = runner.submit(() -> {
                try {
                    HostDisplays.run(display, true, guest -> {
                        guest.setEventListener(event -> {
                            seen.set(event);
                            if (event instanceof HostDisplay.Closed) {
                                closeEvent.countDown();
                            }
                        });
                        guest.awaitClose();
                    });
                } catch (Exception failure) {
                    throw new RuntimeException(failure);
                }
            });
            run.get(2, TimeUnit.SECONDS);
        } finally {
            runner.shutdownNow();
        }

        assertEquals(0, closeEvent.getCount());
        assertInstanceOf(HostDisplay.Closed.class, seen.get());
    }

    @Test
    void postFrameReportsClosedWindow() {
        GlfwHostDisplay display = new GlfwHostDisplay("test", 0, List.of());
        display.close();
        assertFalse(display.postFrame(
                new HostDisplay.Frame(1, 1, new int[] {0})));
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
    void macGuestThatIgnoresCloseDoesNotHangTheCaller() throws Exception {
        FakeDisplay display = new FakeDisplay();
        display.exitLoopOnStart = true;
        long start = System.nanoTime();

        HostDisplays.run(display, true, guest -> {
            while (true) {
                try {
                    Thread.sleep(1_000);
                } catch (InterruptedException interrupted) {
                    return;
                }
            }
        }, 50);

        long elapsedMillis = TimeUnit.NANOSECONDS.toMillis(
                System.nanoTime() - start);
        assertTrue(elapsedMillis < 2_000, "join took " + elapsedMillis + "ms");
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
        private final AtomicBoolean closed = new AtomicBoolean();
        private final AtomicBoolean closePublished = new AtomicBoolean();
        private final AtomicReference<HostDisplay.Frame> posted =
                new AtomicReference<>();
        private final AtomicReference<String> loopThread =
                new AtomicReference<>();
        private final HostEventChannel events = new HostEventChannel();
        private volatile RuntimeException failure;
        private volatile boolean exitLoopOnStart;

        @Override
        public boolean postFrame(Frame frame) {
            if (closed.get()) {
                return false;
            }
            posted.set(frame);
            return true;
        }

        @Override
        public void setEventListener(EventListener listener) {
            events.setListener(listener);
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
            closed.set(true);
            if (closePublished.compareAndSet(false, true)) {
                events.publish(new HostDisplay.Closed());
            }
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
                if (!exitLoopOnStart) {
                    closeLatch.await();
                }
            } catch (InterruptedException interrupted) {
                Thread.currentThread().interrupt();
            } finally {
                done.countDown();
            }
        }
    }
}
