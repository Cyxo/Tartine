package tartine.graphics;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.github.humbleui.skija.Bitmap;
import io.github.humbleui.skija.Pixmap;
import io.github.humbleui.skija.Surface;
import tartine.gfx.GfxCanvas;
import tartine.gfx.skija.SkijaBackend;

/**
 * First-cut SurfaceFlinger-side raster host: owns the Skia software surface
 * one Android Window draws into, binds it to a framework
 * {@link android.graphics.Canvas} (the AGENTS 2026-10-18 decision: Skia via
 * Skija backs all UI draws; LWJGL's GL context only blits and composites).
 * Skija loads its natives on first use from the platform jars.
 */
public final class SkiaPresenter {
    private static Method tartineCreate;

    private SkiaPresenter() {
    }

    static {
        try {
            // Reflection seam: tartineCreate is package-private in the
            // framework module on purpose (guest dex never references it).
            tartineCreate = android.graphics.Canvas.class.getDeclaredMethod(
                    "tartineCreate", GfxCanvas.class);
            tartineCreate.setAccessible(true);
        } catch (ReflectiveOperationException | LinkageError e) {
            throw new IllegalStateException("Canvas binding seam missing", e);
        }
    }

    /** Creates a 480x960-style software frame surface. */
    public static Surface createSurface(int width, int height) {
        return Surface.makeRasterN32Premul(width, height);
    }

    /**
     * Mints a framework canvas bound to the given surface (host renderer
     * path; on-device ViewRootImpl/HardwareRenderer plays this role).
     */
    public static android.graphics.Canvas createSurfaceCanvas(Surface surface) {
        try {
            return (android.graphics.Canvas) tartineCreate.invoke(null,
                    SkijaBackend.canvasFor(surface));
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            throw new IllegalStateException("Canvas binding failed", cause);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Canvas binding failed", e);
        }
    }

    /**
     * Reads the surface back as ARGB ints for the headless probe to assert
     * on (first-draw evidence independent of the desktop window). N32 premul
     * on little-endian stores bytes BB GG RR AA per pixel, so the byte order
     * is swapped into 0xAARRGGBB here.
     */
    public static int[] readPixels(Surface surface, int width, int height) {
        Bitmap bitmap = new Bitmap();
        bitmap.allocN32Pixels(width, height, false);
        if (!surface.readPixels(bitmap, 0, 0)) {
            throw new IllegalStateException("readPixels failed");
        }
        Pixmap pixmap = bitmap.peekPixels();
        if (pixmap == null) {
            throw new IllegalStateException("peekPixels failed");
        }
        java.nio.ByteBuffer buffer = pixmap.getBuffer();
        int[] out = new int[width * height];
        for (int i = 0; i < out.length; i++) {
            int base = i * 4;
            int bb = buffer.get(base) & 0xFF;
            int gg = buffer.get(base + 1) & 0xFF;
            int rr = buffer.get(base + 2) & 0xFF;
            int aa = buffer.get(base + 3) & 0xFF;
            out[i] = (aa << 24) | (rr << 16) | (gg << 8) | bb;
        }
        return out;
    }
}
