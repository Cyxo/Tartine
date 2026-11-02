/**
 * Minimal WindowManager + SurfaceFlinger-lite: one Android Window/Surface
 * per desktop window. {@link tartine.graphics.HostDisplay} is the frame
 * handoff from guest code to the dedicated host UI thread that owns GLFW;
 * see {@code docs/architecture.md} and ADR-0004/ADR-0007.
 */
package tartine.graphics;
