package tartine.graphics;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.concurrent.locks.LockSupport;
import tartine.gfx.GfxSurface;

/**
 * The desktop VSync pass (SurfaceFlinger-lite pacing): each tick drains
 * queued guest work, fires the due {@code Choreographer} frame callbacks
 * (the clock Compose's {@code withFrameNanos} suspends on), and
 * measures/layouts/draws the composed root when the first frame, a callback
 * or a dirty view asks for it. Idle ticks skip the redraw and post nothing,
 * so a static screen costs one blit, not a draw per VSync.
 *
 * <p>Stops when the frame source reports the window closed, when the display
 * refuses a frame ({@code postFrame} returns {@code false}), or when a draw
 * throws. A failed traversal usually leaves guest state wedged (Compose's
 * "layout state is not idle" is the common one), so retrying every VSync only
 * chains the same failure. The error is logged once and the failed frame is
 * not presented; the last successfully posted frame remains on screen.
 *
 * <p>The loop reads back into one reused pixel array through
 * {@link GfxSurface#readPixels(int[])}; per-frame hash and painted-pixel
 * statistics run only when {@code debug} is set, since each costs a full
 * raster scan. A resize replaces the target's surface; the loop notices the
 * new identity/size after draining the queue, reallocates the readback
 * buffer and forces one redraw at the new pixel viewport.
 */
public final class RenderLoop {

    /** Guest-side work the loop drives; all methods run on the guest thread. */
    public interface FrameSource {
        /** Runs queued guest actions (posted host events). */
        void drain();

        /** Fires the frame callbacks due at {@code frameTimeNanos}. */
        int pumpCallbacks(long frameTimeNanos);

        /** Whether the composed root needs a redraw. */
        boolean needsRedraw();

        /** Measures, lays out and draws one frame into the surface. */
        void draw();

        /** Whether the window closed; the loop stops before the next step. */
        boolean isClosed();
    }

    /** Time and wait seam; tests inject a deterministic fake. */
    public interface Clock {
        long nanoTime();

        void parkNanos(long nanos);
    }

    private static final Clock SYSTEM_CLOCK = new Clock() {
        @Override
        public long nanoTime() {
            return System.nanoTime();
        }

        @Override
        public void parkNanos(long nanos) {
            LockSupport.parkNanos(nanos);
        }
    };

    private final HostDisplay display;
    private final RenderTarget target;
    private final FrameSource source;
    private final long framePeriodNanos;
    private final boolean debug;
    private final PrintStream out;
    private final Clock clock;

    /**
     * {@code framePeriodNanos}: the guest tick period (see
     * {@code HostConfig.framePeriodNanos()}); {@code debug} adds per-frame
     * hash and painted-pixel statistics to {@code out}.
     */
    public RenderLoop(HostDisplay display, RenderTarget target,
            FrameSource source, long framePeriodNanos, boolean debug,
            PrintStream out) {
        this(display, target, source, framePeriodNanos, debug, out,
                SYSTEM_CLOCK);
    }

    /** Test seam: same loop with an injected clock. */
    RenderLoop(HostDisplay display, RenderTarget target, FrameSource source,
            long framePeriodNanos, boolean debug, PrintStream out,
            Clock clock) {
        if (display == null) {
            throw new NullPointerException("display");
        }
        if (target == null) {
            throw new NullPointerException("target");
        }
        if (source == null) {
            throw new NullPointerException("source");
        }
        if (clock == null) {
            throw new NullPointerException("clock");
        }
        if (out == null) {
            throw new NullPointerException("out");
        }
        if (framePeriodNanos <= 0) {
            throw new IllegalArgumentException("frame period must be positive: "
                    + framePeriodNanos + "ns");
        }
        this.display = display;
        this.target = target;
        this.source = source;
        this.framePeriodNanos = framePeriodNanos;
        this.debug = debug;
        this.out = out;
        this.clock = clock;
    }

    /**
     * Runs until the window closes, the display drops a frame, or a draw
     * fails.
     */
    public void run() {
        GfxSurface surface = target.surface();
        int width = surface.getWidth();
        int height = surface.getHeight();
        int[] pixels = new int[width * height];
        long nextFrame = clock.nanoTime();
        int frames = 0;
        int changes = 0;
        int lastHash = 0;
        boolean drawFailed = false;
        try {
            while (!source.isClosed()) {
                source.drain();
                if (source.isClosed()) {
                    break;
                }
                long now = clock.nanoTime();
                if (now < nextFrame) {
                    clock.parkNanos(nextFrame - now);
                    continue;
                }
                nextFrame = now + framePeriodNanos;
                int callbacks = source.pumpCallbacks(now);
                source.drain();
                if (source.isClosed()) {
                    break;
                }
                // A queued Resized event may have replaced the target's
                // surface (window framebuffer size is the viewport).
                GfxSurface current = target.surface();
                int currentWidth = current.getWidth();
                int currentHeight = current.getHeight();
                boolean targetChanged = current != surface
                        || currentWidth != width || currentHeight != height;
                if (targetChanged) {
                    surface = current;
                    width = currentWidth;
                    height = currentHeight;
                    if (pixels.length != width * height) {
                        pixels = new int[width * height];
                    }
                }
                if (frames > 0 && callbacks == 0 && !source.needsRedraw()
                        && !targetChanged) {
                    continue;
                }
                try {
                    source.draw();
                } catch (RuntimeException failure) {
                    // Fail fast: a wedged traversal keeps failing on every
                    // VSync, and presenting the partial raster would leave a
                    // torn frame on screen.
                    out.println("render: draw failed at: " + describe(failure)
                            + "; stopping");
                    drawFailed = true;
                    break;
                }
                surface.readPixels(pixels);
                if (debug) {
                    int hash = Arrays.hashCode(pixels);
                    if (hash != lastHash) {
                        changes++;
                        lastHash = hash;
                    }
                    if (frames == 0 || frames % 30 == 0) {
                        out.println("render: frame " + frames + " callbacks "
                                + callbacks + " painted "
                                + countPainted(pixels) + "/" + pixels.length
                                + " hash " + Integer.toHexString(hash));
                    }
                }
                frames++;
                if (!display.postFrame(new HostDisplay.Frame(width, height,
                        pixels))) {
                    out.println("render: display closed; stopping");
                    break;
                }
            }
        } finally {
            target.close();
        }
        out.println("render: drew " + frames + " frames"
                + (debug ? " (" + changes + " pixel changes)" : "")
                + (drawFailed ? ", stopped after a draw failure" : ""));
    }

    /** One printable line for a draw failure. */
    private static String describe(RuntimeException failure) {
        StringBuilder text =
                new StringBuilder(failure.getClass().getSimpleName());
        String message = failure.getMessage();
        if (message != null && !message.isBlank()) {
            text.append(": ").append(message.replace('\n', ' '));
        }
        return text.toString();
    }

    private static int countPainted(int[] pixels) {
        int painted = 0;
        for (int pixel : pixels) {
            if ((pixel >>> 24) != 0) {
                painted++;
            }
        }
        return painted;
    }
}
