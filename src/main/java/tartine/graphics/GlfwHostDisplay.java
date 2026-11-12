package tartine.graphics;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetFramebufferSize;
import static org.lwjgl.glfw.GLFW.glfwGetWindowContentScale;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCharCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCursorEnterCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowFocusCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_REPLACE;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV_MODE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexEnvi;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glVertex2i;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL12.GL_BGRA;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

/**
 * GLFW implementation of {@link HostDisplay}: one Android Window on one
 * desktop window (SurfaceFlinger-lite).
 *
 * <p>The GLFW loop owns the window, GL context, and texture upload, and only
 * ever runs on the display thread chosen by {@link HostDisplays}. Frames
 * arrive through {@link #postFrame} (the BufferQueue handoff) and are
 * uploaded once; the newest frame wins if the guest outpaces VSync. The
 * window stays hidden until the first frame, then shows and starts the
 * timeout clock; it is letterboxed and presented with
 * {@code glfwSwapInterval(1)}.
 *
 * <p>The reverse path is the event channel: GLFW input, resize and focus
 * callbacks publish {@link HostDisplay.Event}s into a {@link HostEventChannel}
 * that buffers until the guest registers a listener and replays then. The
 * loop publishes {@link HostDisplay.Closed} exactly once before releasing
 * {@link #awaitClose}, and {@link #postFrame} reports a closed window so a
 * render loop can stop without polling.
 *
 * <p>The frame is expected fully opaque: the seam readback is premultiplied
 * ARGB, which presents as-is, since premultiplication only differs from
 * straight colors where the canvas has partial alpha. Transparent-frame
 * windows will need the un-premultiply step when they exist.
 */
final class GlfwHostDisplay implements DisplayLoop {
    private static final Logger log = Logger.getLogger(GlfwHostDisplay.class.getName());
    private static final int WIDTH = 480;
    private static final int HEIGHT = 960;

    private final String title;
    private final long timeoutSecs;
    private final List<WindowIcon> icons;
    /**
     * BufferQueue-lite handoff. The guest reuses its readback array, so
     * {@link #postFrame} copies into these display-owned buffers under a
     * lock. Three buffers cover guest writes, the pending frame, and the
     * frame being uploaded; the newest posted frame wins when presentation is
     * behind.
     */
    private final Object frameLock = new Object();
    private final Deque<int[]> freeFramePixels = new ArrayDeque<>();
    private int[] pendingPixels;
    private int pendingWidth;
    private int pendingHeight;
    /** Reused GL upload bytes; display thread only. */
    private ByteBuffer uploadBuffer;
    private final AtomicBoolean closeRequested = new AtomicBoolean();
    private final AtomicBoolean closePublished = new AtomicBoolean();
    private final AtomicBoolean started = new AtomicBoolean();
    private final CountDownLatch closed = new CountDownLatch(1);
    private final CountDownLatch metricsReady = new CountDownLatch(1);
    private final HostEventChannel events = new HostEventChannel();
    private volatile HostDisplay.WindowMetrics metrics;
    private volatile Throwable failure;

    GlfwHostDisplay(String title, long timeoutSecs, List<WindowIcon> icons) {
        this.title = title;
        this.timeoutSecs = timeoutSecs;
        this.icons = icons == null ? List.of() : List.copyOf(icons);
    }

    /** macOS runs GLFW on the process main thread (`-XstartOnFirstThread`). */
    static boolean requiresProcessMainThread() {
        return System.getProperty("os.name", "").toLowerCase(Locale.ROOT)
                .contains("mac");
    }

    @Override
    public boolean postFrame(Frame frame) {
        if (frame == null) {
            throw new NullPointerException("frame");
        }
        if (closeRequested.get()) {
            return false;
        }
        int[] source = frame.argb();
        synchronized (frameLock) {
            int[] copy = freeFramePixels.poll();
            if (copy == null || copy.length != source.length) {
                copy = new int[source.length];
            }
            System.arraycopy(source, 0, copy, 0, source.length);
            if (pendingPixels != null) {
                freeFramePixels.add(pendingPixels);
            }
            pendingPixels = copy;
            pendingWidth = frame.width();
            pendingHeight = frame.height();
        }
        return true;
    }

    /** Takes the newest pending frame, or null; display thread only. */
    private Frame takePendingFrame() {
        synchronized (frameLock) {
            if (pendingPixels == null) {
                return null;
            }
            Frame frame = new Frame(pendingWidth, pendingHeight, pendingPixels);
            pendingPixels = null;
            return frame;
        }
    }

    /** Returns an uploaded frame's pixels to the display-owned pool. */
    private void releaseFrame(Frame frame) {
        synchronized (frameLock) {
            freeFramePixels.add(frame.argb());
        }
    }

    @Override
    public WindowMetrics awaitMetrics() {
        try {
            metricsReady.await();
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(
                    "interrupted waiting for window metrics", interrupted);
        }
        WindowMetrics known = metrics;
        if (known != null) {
            return known;
        }
        Throwable problem = failure;
        if (problem != null) {
            throw new IllegalStateException(
                    "host display failed before reporting window metrics",
                    problem);
        }
        throw new IllegalStateException(
                "host display closed before reporting window metrics");
    }

    @Override
    public void setEventListener(EventListener listener) {
        events.setListener(listener);
    }

    @Override
    public void awaitClose() {
        try {
            closed.await();
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(
                    "interrupted waiting for the host display", interrupted);
        }
        Throwable problem = failure;
        if (problem != null) {
            throw new IllegalStateException("host display failed", problem);
        }
    }

    @Override
    public void close() {
        closeRequested.set(true);
    }

    @Override
    public void startDedicated() {
        requireFirstStart();
        Thread thread = new Thread(this::runLoop, "tartine-display");
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void runOnCallingThread() {
        requireFirstStart();
        runLoop();
    }

    private void requireFirstStart() {
        if (!started.compareAndSet(false, true)) {
            throw new IllegalStateException("host display already started");
        }
    }

    /** The whole GLFW lifetime; failures release {@link #closed}. */
    private void runLoop() {
        GLFWErrorCallback errors = GLFWErrorCallback.createPrint(System.err);
        glfwSetErrorCallback(errors);
        try {
            if (!glfwInit()) {
                throw new IllegalStateException("glfwInit failed (no display?)");
            }
            try {
                if (!closeRequested.get()) {
                    runWindow();
                }
            } finally {
                glfwTerminate();
            }
        } catch (Throwable problem) {
            failure = problem;
        } finally {
            closeRequested.set(true);
            if (closePublished.compareAndSet(false, true)) {
                events.publish(new HostDisplay.Closed());
            }
            // Release a guest blocked on awaitMetrics even when no window
            // was created; the null metrics then surface the failure.
            metricsReady.countDown();
            GLFWErrorCallback previous = glfwSetErrorCallback(null);
            if (previous != null) {
                previous.free();
            }
            closed.countDown();
        }
    }

    /** One window: create, present until close/timeout, destroy. */
    private void runWindow() {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        long window = glfwCreateWindow(WIDTH, HEIGHT, title, NULL, NULL);
        if (window == NULL) {
            throw new IllegalStateException("glfwCreateWindow failed");
        }
        try {
            WindowMetrics initial = windowMetrics(window);
            metrics = initial;
            metricsReady.countDown();
            log.info("display: window " + initial.width() + "x"
                    + initial.height() + " px @ " + initial.contentScale()
                    + "x scale");
            setWindowIcon(window, icons);
            installEventCallbacks(window);
            glfwMakeContextCurrent(window);
            glfwSwapInterval(1);
            GL.createCapabilities();
            int texture = 0;
            int frameWidth = 0;
            int frameHeight = 0;
            boolean shown = false;
            long deadline = Long.MAX_VALUE;
            try {
                while (!closeRequested.get()
                        && !glfwWindowShouldClose(window)
                        && System.nanoTime() < deadline) {
                    glfwPollEvents();
                    Frame frame = takePendingFrame();
                    if (frame != null) {
                        try {
                            if (texture != 0) {
                                glDeleteTextures(texture);
                            }
                            texture = uploadFrameTexture(frame);
                        } finally {
                            releaseFrame(frame);
                        }
                        frameWidth = frame.width();
                        frameHeight = frame.height();
                    }
                    if (frame != null && !shown) {
                        // Show with the first frame and start the timeout
                        // there, matching the old show-after-draw behavior.
                        glfwShowWindow(window);
                        shown = true;
                        if (timeoutSecs > 0) {
                            deadline = System.nanoTime()
                                    + timeoutSecs * 1_000_000_000L;
                        }
                        log.fine("display: first frame presented ("
                                + frameWidth + "x" + frameHeight + ")");
                    }
                    if (!shown) {
                        // Hidden while the guest loads: don't compose or
                        // spin at the vsync rate before the first frame.
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException interrupted) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                        continue;
                    }
                    compose(window, texture, frameWidth, frameHeight);
                    glfwSwapBuffers(window);
                }
            } finally {
                if (texture != 0) {
                    glDeleteTextures(texture);
                }
            }
        } finally {
            Callbacks.glfwFreeCallbacks(window);
            glfwDestroyWindow(window);
        }
    }

    /**
     * Registers the window event sources. GLFW invokes callbacks from
     * {@code glfwPollEvents}, so every publish happens on the display thread;
     * {@link HostEventChannel} hands them to the guest.
     */
    private void installEventCallbacks(long window) {
        glfwSetKeyCallback(window, (win, key, scancode, action, mods) ->
                events.publish(new HostDisplay.Key(key, scancode,
                        action(action), mods)));
        glfwSetCharCallback(window, (win, codepoint) ->
                events.publish(new HostDisplay.Text(codepoint)));
        glfwSetCursorPosCallback(window, (win, x, y) ->
                events.publish(new HostDisplay.CursorMoved(x, y)));
        glfwSetCursorEnterCallback(window, (win, entered) ->
                events.publish(new HostDisplay.CursorEntered(entered)));
        glfwSetMouseButtonCallback(window, (win, button, action, mods) ->
                events.publish(new HostDisplay.Button(button, action(action),
                        mods)));
        glfwSetScrollCallback(window, (win, scrollX, scrollY) ->
                events.publish(new HostDisplay.Scrolled(scrollX, scrollY)));
        // Framebuffer, not window-size, callbacks: the guest viewport is
        // already in pixels, so no point-to-pixel conversion is needed here.
        // A minimised/hidden window reports a 0x0 framebuffer on some
        // platforms; that is not a real resize (AOSP keeps the last
        // configuration), so it is skipped instead of failing the record.
        glfwSetFramebufferSizeCallback(window, (win, width, height) -> {
            if (width > 0 && height > 0) {
                events.publish(new HostDisplay.Resized(width, height));
            }
        });
        glfwSetWindowFocusCallback(window, (win, focused) ->
                events.publish(new HostDisplay.FocusChanged(focused)));
    }

    /**
     * Reads the drawable size in pixels and the monitor content scale. A
     * not-yet-laid-out window can report a zero framebuffer on some
     * platforms; the default surface size is used in that case, and a
     * missing scale falls back to 1.0 (mdpi).
     */
    private static WindowMetrics windowMetrics(long window) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            FloatBuffer scaleX = stack.mallocFloat(1);
            FloatBuffer scaleY = stack.mallocFloat(1);
            glfwGetFramebufferSize(window, width, height);
            glfwGetWindowContentScale(window, scaleX, scaleY);
            float scale = Math.max(scaleX.get(0), scaleY.get(0));
            if (!(scale > 0) || !Float.isFinite(scale)) {
                scale = 1.0f;
            }
            int pixelsWide = width.get(0);
            int pixelsHigh = height.get(0);
            if (pixelsWide <= 0 || pixelsHigh <= 0) {
                pixelsWide = WIDTH;
                pixelsHigh = HEIGHT;
            }
            return new WindowMetrics(pixelsWide, pixelsHigh, scale);
        }
    }

    /** Maps a GLFW press/release/repeat action to the seam action. */
    private static HostDisplay.Action action(int glfwAction) {
        return switch (glfwAction) {
            case GLFW.GLFW_RELEASE -> HostDisplay.Action.RELEASE;
            case GLFW.GLFW_PRESS -> HostDisplay.Action.PRESS;
            case GLFW.GLFW_REPEAT -> HostDisplay.Action.REPEAT;
            default -> throw new IllegalArgumentException(
                    "unknown GLFW action " + glfwAction);
        };
    }

    /**
     * Sets the OS window icons (title bar, taskbar, alt-tab). GLFW copies
     * the pixels at call time, so stack allocation is safe; icons arrive
     * straight-alpha RGBA ready for {@code GLFWImage}.
     */
    private static void setWindowIcon(long window, List<WindowIcon> icons) {
        if (icons == null || icons.isEmpty()) {
            return;
        }
        try (MemoryStack stack = MemoryStack.stackPush()) {
            GLFWImage.Buffer images = GLFWImage.malloc(icons.size(), stack);
            for (int index = 0; index < icons.size(); index++) {
                WindowIcon icon = icons.get(index);
                ByteBuffer pixels = stack.malloc(icon.rgba().length);
                pixels.put(icon.rgba()).flip();
                images.get(index).set(icon.width(), icon.height(), pixels);
            }
            GLFW.glfwSetWindowIcon(window, images);
        }
        List<String> sizes = icons.stream()
                .map(icon -> icon.width() + "x" + icon.height()).toList();
        log.fine("window icon: " + String.join(", ", sizes));
    }

    /**
     * Uploads the frame's pixels as one texture. The seam readback is packed
     * premultiplied ARGB ints; the GL upload packs them as B, G, R, A bytes,
     * which {@code GL_BGRA} + {@code GL_UNSIGNED_BYTE} consume exactly, so
     * no per-pixel channel swap is needed. The upload buffer is reused across
     * frames.
     */
    private int uploadFrameTexture(HostDisplay.Frame frame) {
        int width = frame.width();
        int height = frame.height();
        int[] argb = frame.argb();
        int bytes = width * height * 4;
        if (uploadBuffer == null || uploadBuffer.capacity() < bytes) {
            uploadBuffer = BufferUtils.createByteBuffer(bytes);
        }
        ByteBuffer pixels = uploadBuffer;
        pixels.clear();
        for (int pixel : argb) {
            pixels.put((byte) (pixel & 0xFF));          // blue
            pixels.put((byte) ((pixel >>> 8) & 0xFF));  // green
            pixels.put((byte) ((pixel >>> 16) & 0xFF)); // red
            pixels.put((byte) ((pixel >>> 24) & 0xFF)); // alpha
        }
        pixels.flip();
        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0,
                GL_BGRA, GL_UNSIGNED_BYTE, pixels);
        return texture;
    }

    /** Clears and composites the letterboxed frame quad, if one exists. */
    private static void compose(long window, int texture, int frameWidth,
            int frameHeight) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            glfwGetFramebufferSize(window, width, height);
            int bufferWidth = width.get(0);
            int bufferHeight = height.get(0);
            glViewport(0, 0, bufferWidth, bufferHeight);
            glClearColor(0.05f, 0.05f, 0.05f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);
            if (texture == 0) {
                return;
            }
            double scale = Math.min(
                    (double) bufferWidth / frameWidth,
                    (double) bufferHeight / frameHeight);
            int drawWidth = (int) Math.round(frameWidth * scale);
            int drawHeight = (int) Math.round(frameHeight * scale);
            final int left = (bufferWidth - drawWidth) / 2;
            final int top = (bufferHeight - drawHeight) / 2;
            glEnable(GL_TEXTURE_2D);
            glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_REPLACE);
            glBindTexture(GL_TEXTURE_2D, texture);
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            glOrtho(0, bufferWidth, bufferHeight, 0, -1, 1); // top-left origin
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();
            glBegin(GL_QUADS);
            glTexCoord2f(0.0f, 0.0f);
            glVertex2i(left, top);
            glTexCoord2f(1.0f, 0.0f);
            glVertex2i(left + drawWidth, top);
            glTexCoord2f(1.0f, 1.0f);
            glVertex2i(left + drawWidth, top + drawHeight);
            glTexCoord2f(0.0f, 1.0f);
            glVertex2i(left, top + drawHeight);
            glEnd();
            glDisable(GL_TEXTURE_2D);
        }
    }
}
