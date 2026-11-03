package tartine.graphics;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
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
 * <p>Stops when the frame source reports the window closed, or when the
 * display refuses a frame ({@code postFrame} returns {@code false}).
 * Failures inside a draw are counted and logged once per distinct message;
 * the partially drawn surface is still presented, matching how a blocked
 * device frame keeps whatever it managed to draw.
 *
 * <p>The loop reads back into one reused pixel array through
 * {@link GfxSurface#readPixels(int[])}; per-frame hash and painted-pixel
 * statistics run only when {@code debug} is set, since each costs a full
 * raster scan.
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
    private final GfxSurface surface;
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
    public RenderLoop(HostDisplay display, GfxSurface surface,
            FrameSource source, long framePeriodNanos, boolean debug,
            PrintStream out) {
        this(display, surface, source, framePeriodNanos, debug, out,
                SYSTEM_CLOCK);
    }

    /** Test seam: same loop with an injected clock. */
    RenderLoop(HostDisplay display, GfxSurface surface, FrameSource source,
            long framePeriodNanos, boolean debug, PrintStream out,
            Clock clock) {
        if (display == null) {
            throw new NullPointerException("display");
        }
        if (surface == null) {
            throw new NullPointerException("surface");
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
        this.surface = surface;
        this.source = source;
        this.framePeriodNanos = framePeriodNanos;
        this.debug = debug;
        this.out = out;
        this.clock = clock;
    }

    /** Runs until the window closes or the display drops a frame. */
    public void run() {
        int width = surface.getWidth();
        int height = surface.getHeight();
        int[] pixels = new int[width * height];
        long nextFrame = clock.nanoTime();
        int frames = 0;
        int changes = 0;
        int lastHash = 0;
        int drawFailures = 0;
        Set<String> failureMessages = new LinkedHashSet<>();
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
                if (frames > 0 && callbacks == 0 && !source.needsRedraw()) {
                    continue;
                }
                try {
                    source.draw();
                } catch (RuntimeException failure) {
                    drawFailures++;
                    String message = describe(failure);
                    if (failureMessages.add(message)) {
                        out.println("render: draw blocked at: " + message);
                    }
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
            surface.close();
        }
        out.println("render: drew " + frames + " frames"
                + (debug ? " (" + changes + " pixel changes)" : "")
                + (drawFailures > 0 ? ", " + drawFailures + " draw failures ("
                        + failureMessages.size() + " distinct)" : ""));
    }

    /** One printable line for a draw failure, for distinct-message logging. */
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
