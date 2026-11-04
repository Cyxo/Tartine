package tartine.graphics;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

/**
 * Threading policy for {@link HostDisplay}.
 *
 * <p>GLFW is single-threaded: the window loop must run on one thread, which
 * is the process main thread on macOS (the launchers pass
 * {@code -XstartOnFirstThread}) and a dedicated {@code tartine-display}
 * thread elsewhere. Guest app code always runs on its own main-looper
 * thread, never on the display thread: on macOS that is a spawned
 * {@code tartine-guest} thread, elsewhere it is the caller.
 *
 * <p>The display and guest threads meet on {@link HostDisplay}: frames flow
 * display-ward through {@code postFrame}, window events (input, resize,
 * focus, close) flow guest-ward through {@code setEventListener}. When the
 * window closes while guest work is still running, {@code close()} is
 * signalled before the guest thread is joined, and that join is bounded so a
 * guest that ignores the close event cannot hang the caller.
 */
public final class HostDisplays {
    private static final Logger log = Logger.getLogger(HostDisplays.class.getName());

    /** How long the macOS path waits for the guest after a close. */
    private static final long DEFAULT_GUEST_JOIN_MILLIS = 5_000;

    /** Guest work that owns the guest main-looper thread. */
    @FunctionalInterface
    public interface GuestMain {
        void run(HostDisplay display) throws Exception;
    }

    private HostDisplays() {
    }

    /**
     * Starts a desktop display and runs {@code guestMain} on the guest
     * main-looper thread. The window opens on the first posted frame and
     * auto-closes {@code timeoutSecs} after that (0 = stay open). Returns
     * after the window closes and guest work has finished; guest and display
     * failures propagate to the caller.
     */
    public static void run(String title, long timeoutSecs,
            List<WindowIcon> icons, GuestMain guestMain) throws Exception {
        GlfwHostDisplay display =
                new GlfwHostDisplay(title, timeoutSecs, icons);
        run(display, GlfwHostDisplay.requiresProcessMainThread(), guestMain);
    }

    /** Package-private policy seam used by the tests with a fake display. */
    static void run(DisplayLoop display, boolean mainThread,
            GuestMain guestMain) throws Exception {
        run(display, mainThread, guestMain, DEFAULT_GUEST_JOIN_MILLIS);
    }

    /** Test seam: same policy with an explicit guest-join timeout. */
    static void run(DisplayLoop display, boolean mainThread,
            GuestMain guestMain, long guestJoinMillis) throws Exception {
        if (mainThread) {
            runOnMainThread(display, guestMain, guestJoinMillis);
        } else {
            runOnGuestThread(display, guestMain);
        }
    }

    /** macOS: the caller is the display thread, guest work is spawned. */
    private static void runOnMainThread(DisplayLoop display,
            GuestMain guestMain, long guestJoinMillis) throws Exception {
        AtomicReference<Throwable> guestFailure = new AtomicReference<>();
        Thread guest = new Thread(() -> {
            try {
                guestMain.run(display);
            } catch (Throwable failure) {
                guestFailure.set(failure);
            } finally {
                display.close();
            }
        }, "tartine-guest");
        guest.setDaemon(true);
        guest.start();
        try {
            display.runOnCallingThread();
        } finally {
            // The window can go away while the guest is still running: tell
            // it to stop before waiting, and never wait forever.
            display.close();
            joinGuest(guest, guestJoinMillis);
        }
        finish(display, guestFailure.get());
    }

    /** Bounded join, then interrupt; never blocks the caller forever. */
    private static void joinGuest(Thread guest, long millis) {
        long deadline = System.nanoTime()
                + TimeUnit.MILLISECONDS.toNanos(millis);
        boolean interrupted = false;
        while (guest.isAlive()) {
            long remaining = deadline - System.nanoTime();
            if (remaining <= 0) {
                break;
            }
            try {
                guest.join(Math.max(1,
                        TimeUnit.NANOSECONDS.toMillis(remaining)));
            } catch (InterruptedException interruptedJoin) {
                interrupted = true;
                guest.interrupt();
                break;
            }
        }
        if (guest.isAlive()) {
            guest.interrupt();
            try {
                guest.join(1_000);
            } catch (InterruptedException interruptedJoin) {
                interrupted = true;
            }
            if (guest.isAlive()) {
                log.warning("host display: guest thread did not stop after close");
            }
        }
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
    }

    /** Everywhere else: a dedicated display thread, guest work on caller. */
    private static void runOnGuestThread(DisplayLoop display,
            GuestMain guestMain) throws Exception {
        display.startDedicated();
        Throwable guestFailure = null;
        try {
            guestMain.run(display);
        } catch (Throwable failure) {
            guestFailure = failure;
        } finally {
            display.close();
        }
        finish(display, guestFailure);
    }

    /** Waits for the display loop, then reports the first real failure. */
    private static void finish(DisplayLoop display, Throwable guestFailure)
            throws Exception {
        try {
            display.awaitClose();
        } catch (RuntimeException displayFailure) {
            if (guestFailure != null && guestFailure != displayFailure) {
                displayFailure.addSuppressed(guestFailure);
            }
            throw displayFailure;
        }
        if (guestFailure instanceof Exception exception) {
            throw exception;
        }
        if (guestFailure instanceof Error error) {
            throw error;
        }
        if (guestFailure != null) {
            throw new IllegalStateException(guestFailure);
        }
    }
}
