package tartine.graphics;

import android.graphics.Canvas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tartine.gfx.GfxBackend;
import tartine.gfx.GfxSurface;
import tartine.gfx.skija.SkijaBackend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pins the guest raster target's resize contract: the surface and canvas
 * are replaced together, a duplicate resize is a no-op, and closing is
 * idempotent (the render loop and an early-return cleanup can both close it).
 */
class GuestSurfaceTest {

    @BeforeEach
    void installBackend() {
        GfxBackend.install(new SkijaBackend());
    }

    @AfterEach
    void removeBackend() {
        GfxBackend.uninstall();
    }

    @Test
    void resizeReplacesSurfaceAndCanvasTogether() {
        GuestSurface guest = new GuestSurface(4, 2);
        GfxSurface first = guest.surface();
        Canvas firstCanvas = guest.canvas();

        assertTrue(guest.resize(8, 4));

        assertNotSame(first, guest.surface());
        assertNotSame(firstCanvas, guest.canvas());
        assertEquals(8, guest.width());
        assertEquals(4, guest.height());
        assertEquals(8, guest.surface().getWidth());
        assertEquals(4, guest.surface().getHeight());
        assertFalse(guest.resize(8, 4));
    }

    @Test
    void closeIsIdempotentAndRejectsBadViewports() {
        GuestSurface guest = new GuestSurface(4, 2);
        assertThrows(IllegalArgumentException.class,
                () -> guest.resize(0, 4));
        assertThrows(IllegalArgumentException.class,
                () -> guest.resize(4, -1));

        guest.close();
        guest.close();

        assertNull(guest.surface());
        assertNull(guest.canvas());
    }
}
