package tartine.graphics;

/**
 * Host display seam: the windowing side of the graphics mapping
 * (SurfaceFlinger-lite). The implementation owns GLFW on its host UI thread
 * and presents finished rasters there; guest-driven drawing stays on the
 * guest main-looper thread and only hands frames across this interface.
 *
 * <p>The frame path is the Choreographer to SurfaceFlinger shape: draw and
 * read back on the guest thread, {@link #postFrame} the packed pixels, and
 * let the host UI thread upload/present them at VSync. The guest never calls
 * GLFW.
 *
 * <p>The reverse path carries window events: input, resize, focus and close
 * are published by the display thread and delivered through an
 * {@link EventListener}, which the guest normally implements by posting the
 * event to its {@code Looper} queue.
 */
public interface HostDisplay extends AutoCloseable {

    /**
     * One finished frame: packed premultiplied ARGB ints, row-major.
     * The caller may reuse {@code argb} once {@link #postFrame} returns;
     * displays that keep pixels beyond that call must copy them.
     */
    record Frame(int width, int height, int[] argb) {
        /** Rejects non-positive sizes and mismatched pixel counts. */
        public Frame {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException(
                        "frame size must be positive: " + width + "x" + height);
            }
            if (argb == null || argb.length != width * height) {
                throw new IllegalArgumentException("frame pixels must be "
                        + (width * height) + " ints");
            }
        }
    }

    /**
     * The host window's drawable metrics: framebuffer pixels plus the
     * content scale that maps those pixels to desktop points. The guest
     * viewport is the framebuffer size; the guest density is the content
     * scale, so a HiDPI window keeps the same dp size as an mdpi one.
     */
    record WindowMetrics(int width, int height, float contentScale) {
        /** Rejects non-positive sizes and a non-finite/non-positive scale. */
        public WindowMetrics {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("window size must be "
                        + "positive: " + width + "x" + height);
            }
            if (!(contentScale > 0) || !Float.isFinite(contentScale)) {
                throw new IllegalArgumentException(
                        "content scale must be positive and finite: "
                                + contentScale);
            }
        }

        /** The historical 480x960 mdpi surface, used by fakes and no-window runs. */
        public static final WindowMetrics DEFAULT =
                new WindowMetrics(480, 960, 1.0f);
    }

    /**
     * A window event. Events are serial values so listeners can switch on
     * them without a GLFW dependency. Input values stay in desktop/GLFW
     * space until the framework maps them to Android key and motion codes.
     */
    sealed interface Event permits Closed, Resized, FocusChanged, Key,
            Text, CursorMoved, Button, Scrolled {
    }

    /** The window is gone; no further frames or events will arrive. */
    record Closed() implements Event {
    }

    /**
     * The drawable framebuffer size changed, in pixels (not GLFW window
     * points), so the new width/height can become the guest pixel viewport
     * directly.
     */
    record Resized(int width, int height) implements Event {
        /** Rejects a non-positive framebuffer size. */
        public Resized {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("window size must be "
                        + "positive: " + width + "x" + height);
            }
        }
    }

    /** The window gained ({@code true}) or lost keyboard focus. */
    record FocusChanged(boolean focused) implements Event {
    }

    /** A desktop key transition; key/scancode/modifiers are GLFW values. */
    record Key(int key, int scancode, Action action, int mods)
            implements Event {
    }

    /** A Unicode code point typed into the window. */
    record Text(int codepoint) implements Event {
    }

    /** Cursor position in window coordinates. */
    record CursorMoved(double x, double y) implements Event {
    }

    /** A mouse button transition; button/modifiers are GLFW values. */
    record Button(int button, Action action, int mods) implements Event {
    }

    /** Scroll wheel delta. */
    record Scrolled(double scrollX, double scrollY) implements Event {
    }

    /** GLFW release/press/repeat actions, independent of its constants. */
    enum Action {
        RELEASE, PRESS, REPEAT
    }

    /** Receives window events; must not block the display thread. */
    @FunctionalInterface
    interface EventListener {
        void onEvent(Event event);
    }

    /**
     * Hands a finished frame to the host UI thread. Callable from the guest
     * main-looper thread (or any other); the newest frame wins when
     * presentation is behind.
     *
     * @return {@code false} when the window is already closed and the frame
     *         was dropped
     */
    boolean postFrame(Frame frame);

    /**
     * Waits for the window to exist and returns its drawable metrics. The
     * guest calls this once before launching app code, so framework display
     * configuration starts from the real window instead of a fixed surface.
     * Fake displays and headless runs keep the 480x960 mdpi default.
     */
    default WindowMetrics awaitMetrics() {
        return WindowMetrics.DEFAULT;
    }

    /**
     * Registers the event listener and replays events that arrived before
     * registration. The listener is called on the display thread for live
     * events and on the registering thread while replaying; it must be
     * thread-safe and should only enqueue work.
     */
    void setEventListener(EventListener listener);

    /** Waits until the window closes, by user request or timeout. */
    void awaitClose();

    /**
     * Requests the window to close. Idempotent and callable from any thread;
     * the host UI thread notices on its next loop iteration.
     */
    @Override
    void close();
}
