package tartine.graphics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import tartine.gfx.GfxCanvas;
import tartine.gfx.GfxSurface;

/**
 * Pins the render loop's rules: draw when a frame is due (callback or dirty
 * view), skip idle ticks, stop on close, on a dropped frame and on a failed
 * draw, reuse one readback buffer, and rebuild it when a resize replaces the
 * surface. A fake clock keeps every test deterministic.
 */
class RenderLoopTest {

    private static final long PERIOD = 10_000_000L; // 100 Hz, one int per ms

    @Test
    void drawsOnlyWhenFrameIsDue() {
        Harness harness = new Harness();
        harness.source.closeAfterDraws = 3;

        harness.run();

        // One draw per period: the second and third draws wait for the park.
        assertEquals(List.of(0L, PERIOD, 2 * PERIOD), harness.source.drawTimes);
        assertEquals(3, harness.source.pumps);
        assertEquals(3, harness.display.frames.size());
    }

    @Test
    void idleTicksSkipTheRedraw() {
        Harness harness = new Harness();
        harness.source.callbacks = 0;
        harness.source.closeAfterPumps = 5;

        harness.run();

        // First frame always draws; the four idle ticks post nothing.
        assertEquals(1, harness.source.draws);
        assertEquals(1, harness.display.frames.size());
        assertTrue(harness.output().contains("render: drew 1 frames"));
    }

    @Test
    void dirtyViewDrawsWithoutCallbacks() {
        Harness harness = new Harness();
        harness.source.callbacks = 0;
        harness.source.dirty = true;
        harness.source.closeAfterDraws = 3;

        harness.run();

        assertEquals(3, harness.source.draws);
        assertEquals(3, harness.display.frames.size());
    }

    @Test
    void stopsWhenTheWindowCloses() {
        Harness harness = new Harness();
        harness.source.closeAfterDrains = 1;

        harness.run();

        assertEquals(0, harness.source.pumps);
        assertEquals(0, harness.source.draws);
        assertEquals(0, harness.display.frames.size());
        assertTrue(harness.target.closed);
    }

    @Test
    void stopsWhenDisplayDropsFrame() {
        Harness harness = new Harness();
        harness.display.open = false;

        harness.run();

        assertEquals(1, harness.source.draws);
        assertEquals(1, harness.source.pumps);
        assertEquals(0, harness.display.frames.size());
        assertTrue(harness.output().contains(
                "render: display closed; stopping"));
        assertTrue(harness.target.closed);
    }

    @Test
    void drawFailureStopsLoopAndFailedFrameIsNotPresented() {
        Harness harness = new Harness();
        harness.source.failures = new RuntimeException[] {
                null,
                new IllegalStateException("layout state is not idle"),
                new IllegalArgumentException("must not be reached"),
        };

        harness.run();

        // The good frame was presented; the failed draw was not retried and
        // its partial surface was not posted.
        assertEquals(2, harness.source.draws);
        assertEquals(1, harness.display.frames.size());
        String text = harness.output();
        assertEquals(1, occurrences(text, "layout state is not idle"), text);
        assertFalse(text.contains("must not be reached"), text);
        assertTrue(text.contains("render: draw failed at: IllegalStateException:"
                + " layout state is not idle; stopping"), text);
        assertTrue(text.contains("render: drew 1 frames,"
                + " stopped after a draw failure"), text);
        assertTrue(harness.target.closed);
    }

    @Test
    void firstFrameFailurePostsNothing() {
        Harness harness = new Harness();
        harness.source.failures = new RuntimeException[] {
                new IllegalStateException("boom"),
        };

        harness.run();

        assertEquals(1, harness.source.draws);
        assertEquals(1, harness.source.pumps);
        assertEquals(0, harness.display.frames.size());
        assertTrue(harness.output().contains(
                "render: drew 0 frames, stopped after a draw failure"),
                harness.output());
    }

    @Test
    void reusesOneReadbackBufferForEveryFrame() {
        Harness harness = new Harness();
        harness.source.closeAfterDraws = 3;

        harness.run();

        FakeSurface surface = harness.target.current;
        assertEquals(3, surface.reads);
        assertSame(surface.readBuffers.get(0),
                surface.readBuffers.get(1));
        assertSame(surface.readBuffers.get(1),
                surface.readBuffers.get(2));
        for (HostDisplay.Frame frame : harness.display.frames) {
            assertSame(surface.readBuffers.get(0), frame.argb());
        }
    }

    @Test
    void resizeReplacesReadbackBufferAndForcesRedraw() {
        Harness harness = new Harness();
        harness.source.callbacks = 0;
        harness.source.closeAfterDraws = 2;
        harness.source.onDrain = () -> {
            if (harness.source.draws == 1) {
                harness.target.resize(8, 4);
            }
        };

        harness.run();

        // No callback and no dirty flag: the resize alone asks for the
        // second draw, at the new pixel viewport.
        assertEquals(2, harness.source.draws);
        assertEquals(2, harness.display.frames.size());
        HostDisplay.Frame first = harness.display.frames.get(0);
        HostDisplay.Frame second = harness.display.frames.get(1);
        assertEquals(4, first.width());
        assertEquals(2, first.height());
        assertEquals(8, second.width());
        assertEquals(4, second.height());
        assertEquals(32, second.argb().length);
        assertNotSame(first.argb(), second.argb());
    }

    @Test
    void statisticsOnlyWhenDebugIsEnabled() {
        Harness quiet = new Harness();
        quiet.source.closeAfterDraws = 1;
        quiet.run();
        assertFalse(quiet.output().contains("painted"));
        assertFalse(quiet.output().contains("pixel changes"));

        Harness verbose = new Harness();
        verbose.source.closeAfterDraws = 1;
        verbose.debug = true;
        verbose.run();
        assertTrue(verbose.output().contains("render: frame 0 callbacks 1"
                + " painted 8/8"), verbose.output());
        assertTrue(verbose.output().contains("(1 pixel changes)"),
                verbose.output());
    }

    @Test
    void rejectsNonPositiveFramePeriod() {
        Harness harness = new Harness();
        assertThrows(IllegalArgumentException.class,
                () -> new RenderLoop(harness.display, harness.target,
                        harness.source, 0L, false,
                        new PrintStream(new ByteArrayOutputStream()),
                        harness.clock));
    }

    private static int occurrences(String text, String needle) {
        int count = 0;
        for (int index = text.indexOf(needle); index >= 0;
                index = text.indexOf(needle, index + needle.length())) {
            count++;
        }
        return count;
    }

    /** Fake clock, source, display and render target wired to one loop. */
    private static final class Harness {
        final FakeClock clock = new FakeClock();
        final FakeSource source = new FakeSource(clock);
        final FakeDisplay display = new FakeDisplay();
        final FakeTarget target = new FakeTarget(4, 2);
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        boolean debug;

        void run() {
            new RenderLoop(display, target, source, PERIOD, debug,
                    new PrintStream(bytes, true, StandardCharsets.UTF_8),
                    clock).run();
        }

        String output() {
            return bytes.toString(StandardCharsets.UTF_8);
        }
    }

    /** Advances only when the loop parks, so due frames are exact. */
    private static final class FakeClock implements RenderLoop.Clock {
        long now;
        int parks;

        @Override
        public long nanoTime() {
            return now;
        }

        @Override
        public void parkNanos(long nanos) {
            if (++parks > 1_000) {
                throw new AssertionError("loop parked without progressing");
            }
            now += nanos;
        }
    }

    private static final class FakeSource implements RenderLoop.FrameSource {
        private final FakeClock clock;
        int callbacks = 1;
        boolean dirty;
        int drains;
        int pumps;
        int draws;
        int closeAfterDrains = Integer.MAX_VALUE;
        int closeAfterPumps = Integer.MAX_VALUE;
        int closeAfterDraws = Integer.MAX_VALUE;
        RuntimeException[] failures = new RuntimeException[0];
        Runnable onDrain;
        final List<Long> drawTimes = new ArrayList<>();
        boolean closed;

        FakeSource(FakeClock clock) {
            this.clock = clock;
        }

        @Override
        public void drain() {
            drains++;
            if (onDrain != null) {
                onDrain.run();
            }
            if (drains >= closeAfterDrains) {
                closed = true;
            }
        }

        @Override
        public int pumpCallbacks(long frameTimeNanos) {
            pumps++;
            if (pumps >= closeAfterPumps) {
                closed = true;
            }
            return callbacks;
        }

        @Override
        public boolean needsRedraw() {
            return dirty;
        }

        @Override
        public void draw() {
            int index = draws++;
            drawTimes.add(clock.now);
            RuntimeException failure =
                    index < failures.length ? failures[index] : null;
            if (draws >= closeAfterDraws) {
                closed = true;
            }
            if (failure != null) {
                throw failure;
            }
        }

        @Override
        public boolean isClosed() {
            return closed;
        }
    }

    private static final class FakeDisplay implements HostDisplay {
        final List<Frame> frames = new ArrayList<>();
        boolean open = true;

        @Override
        public boolean postFrame(Frame frame) {
            if (!open) {
                return false;
            }
            frames.add(frame);
            return true;
        }

        @Override
        public void setEventListener(EventListener listener) {
        }

        @Override
        public void awaitClose() {
        }

        @Override
        public void close() {
            open = false;
        }
    }

    /** Replaces its surface on resize, like the GLFW-backed guest target. */
    private static final class FakeTarget implements RenderTarget {
        FakeSurface current;
        boolean closed;

        FakeTarget(int width, int height) {
            current = new FakeSurface(width, height);
        }

        @Override
        public GfxSurface surface() {
            return current;
        }

        void resize(int width, int height) {
            if (current.getWidth() == width && current.getHeight() == height) {
                return;
            }
            current = new FakeSurface(width, height);
        }

        @Override
        public void close() {
            closed = true;
        }
    }

    /** Records the identity of every readback buffer the loop hands over. */
    private static final class FakeSurface implements GfxSurface {
        private final int width;
        private final int height;
        final List<int[]> readBuffers = new ArrayList<>();
        int reads;

        FakeSurface(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public GfxCanvas getCanvas() {
            return null;
        }

        @Override
        public int getWidth() {
            return width;
        }

        @Override
        public int getHeight() {
            return height;
        }

        @Override
        public int[] readPixels() {
            throw new AssertionError("the loop must reuse readPixels(int[])");
        }

        @Override
        public void readPixels(int[] out) {
            readBuffers.add(out);
            reads++;
            Arrays.fill(out, 0xFF102030);
        }

        @Override
        public void close() {
        }
    }
}
