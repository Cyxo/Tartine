package tartine.graphics;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import tartine.gfx.GfxSurface;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetFramebufferSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
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
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexEnvi;
import static org.lwjgl.opengl.GL11.glVertex2i;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL12.GL_BGRA;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * One Android Window on one desktop window (SurfaceFlinger-lite).
 *
 * <p>First bridge complete: the headless probe draws the
 * window's content view into a renderer surface
 * ({@link RasterPresenter#createSurfaceCanvas}) and this class composites it —
 * the surface is uploaded once as a GL texture and stretched into the
 * framebuffer in a letterboxed quad, VSync-paced by GLFW. Following the
 * AGENTS graphics decision, GL holds no responsibility for the content
 * itself: the texture upload is the whole BufferQueue step, and the
 * Choreographer-driven redraw path (plus the Looper-vs-GLFW threading
 * bridge) lands here later. Everything runs on the calling thread, so the
 * macOS {@code -XstartOnFirstThread} script flag covers it.
 *
 * <p>The frame is expected fully opaque: the seam readback is premultiplied
 * ARGB, which presents as-is, since premultiplication only differs from
 * straight colors where the canvas has partial alpha. Transparent-frame
 * windows will need the un-premultiply step when they exist.
 */
public final class DesktopWindow {
    private static final int WIDTH = 480;
    private static final int HEIGHT = 960;

    private DesktopWindow() {
    }

    /**
     * Shows the window until closed, or {@code timeoutSecs} elapses when
     * positive (automated runs). {@code frame} may be null (draw never
     * reached the surface): the window then shows the dark clear color.
     */
    public static void show(String title, long timeoutSecs, GfxSurface frame) {
        show(title, timeoutSecs, frame, List.of());
    }

    /**
     * Same as {@link #show(String, long, GfxSurface)} with window icons (the
     * app's launcher artwork, resolving through the APK). Empty means the
     * platform default icon stands.
     */
    public static void show(String title, long timeoutSecs, GfxSurface frame,
            List<WindowIcon> icons) {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("glfwInit failed (no display?)");
        }
        try {
            glfwDefaultWindowHints();
            glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
            glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
            long window = glfwCreateWindow(WIDTH, HEIGHT, title, NULL, NULL);
            if (window == NULL) {
                throw new IllegalStateException("glfwCreateWindow failed");
            }
            try {
                setWindowIcon(window, icons);
                glfwMakeContextCurrent(window);
                glfwSwapInterval(1);
                glfwShowWindow(window);
                GL.createCapabilities();
                int texture = frame == null ? 0 : uploadFrameTexture(frame);
                int frameWidth = frame == null ? 0 : frame.getWidth();
                int frameHeight = frame == null ? 0 : frame.getHeight();
                long deadline = timeoutSecs <= 0 ? Long.MAX_VALUE
                        : System.nanoTime() + timeoutSecs * 1_000_000_000L;
                try {
                    while (!glfwWindowShouldClose(window)
                            && System.nanoTime() < deadline) {
                        glfwPollEvents();
                        compose(window, texture, frameWidth, frameHeight);
                        glfwSwapBuffers(window);
                    }
                } finally {
                    if (texture != 0) {
                        glDeleteTextures(texture);
                    }
                }
            } finally {
                glfwDestroyWindow(window);
            }
        } finally {
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
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
        System.out.println("window icon: " + String.join(", ", sizes));
    }

    /**
     * Uploads the frame's pixels as one texture. The seam readback is packed
     * premultiplied ARGB ints; the GL upload packs them as B, G, R, A bytes,
     * which {@code GL_BGRA} + {@code GL_UNSIGNED_BYTE} consume exactly, so
     * no per-pixel channel swap is needed.
     */
    private static int uploadFrameTexture(GfxSurface frame) {
        int width = frame.getWidth();
        int height = frame.getHeight();
        int[] argb = frame.readPixels();
        ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);
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
            int left = (bufferWidth - drawWidth) / 2;
            int top = (bufferHeight - drawHeight) / 2;
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
