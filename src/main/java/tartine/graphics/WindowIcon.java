package tartine.graphics;

/**
 * One icon image for a desktop window: straight-alpha (unpremultiplied)
 * RGBA bytes, top-down rows. Copied by the OS on
 * {@code glfwSetWindowIcon}, so buffers are free to be transient.
 */
public record WindowIcon(int width, int height, byte[] rgba) {
}
