package tartine.graphics;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Threading policy for {@link HostDisplay}.
 *
 * <p>GLFW is single-threaded: the window loop must run on one thread, which
 * is the process main thread on macOS (the launchers pass
 * {@code -XstartOnFirstThread}) and a dedicated {@code tartine-display}
 * thread elsewhere. Guest app code always runs on its own main-looper
 * thread, never on the display thread: on macOS that is a spawned
 * {@code tartine-guest} thread, elsewhere it is the caller.
 */
public final class HostDisplays {

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
        if (mainThread) {
            runOnMainThread(display, guestMain);
        } else {
            runOnGuestThread(display, guestMain);
        }
    }

    /** macOS: the caller is the display thread, guest work is spawned. */
    private static void runOnMainThread(DisplayLoop display,
            GuestMain guestMain) throws Exception {
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
            guest.join();
        }
        finish(display, guestFailure.get());
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
