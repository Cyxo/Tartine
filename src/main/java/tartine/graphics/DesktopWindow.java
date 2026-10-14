package tartine.graphics;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
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
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * One Android Window on one desktop window (SurfaceFlinger-lite, first cut).
 *
 * <p>No pixels yet: composition still waits for attach, so the window shows
 * an uninitialized (black) buffer. This step proves the substrate —
 * GLFW window, GL context, VSync-paced event pump — that frames and
 * Choreographer callbacks will land on later. Size is a phone-portrait
 * placeholder until DisplayMetrics wiring supplies it.
 */
public final class DesktopWindow {
    private static final int WIDTH = 480;
    private static final int HEIGHT = 960;

    private DesktopWindow() {
    }

    /**
     * Shows the window until closed, or {@code timeoutSecs} elapses when
     * positive (automated runs).
     */
    public static void show(String title, long timeoutSecs) {
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
                glfwMakeContextCurrent(window);
                glfwSwapInterval(1);
                glfwShowWindow(window);
                GL.createCapabilities();
                glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
                long deadline = timeoutSecs <= 0 ? Long.MAX_VALUE
                        : System.nanoTime() + timeoutSecs * 1_000_000_000L;
                while (!glfwWindowShouldClose(window)
                        && System.nanoTime() < deadline) {
                    glClear(GL_COLOR_BUFFER_BIT);
                    glfwSwapBuffers(window);
                    glfwPollEvents();
                }
            } finally {
                glfwDestroyWindow(window);
            }
        } finally {
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
    }
}
