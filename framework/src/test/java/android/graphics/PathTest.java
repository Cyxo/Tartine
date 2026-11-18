package android.graphics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Skia path-state semantics the Java2D-backed port emulates: a segment call
 * on an empty path injects an implicit moveTo(0, 0), and close() on an empty
 * path is a no-op rather than Java2D's IllegalPathStateException.
 */
class PathTest {

    @Test
    void closeOnAnEmptyPathChangesNothing() {
        Path path = new Path();
        path.close();
        assertTrue(path.isEmpty());
    }

    @Test
    void segmentOnAnEmptyPathStartsAtTheOrigin() {
        Path path = new Path();
        path.lineTo(10.0f, 20.0f);

        assertFalse(path.isEmpty());
        RectF bounds = new RectF();
        path.computeBounds(bounds);
        assertEquals(new RectF(0.0f, 0.0f, 10.0f, 20.0f), bounds);
    }

    @Test
    void curveOnAnEmptyPathStartsAtTheOrigin() {
        Path path = new Path();
        path.quadTo(5.0f, 10.0f, 15.0f, 0.0f);
        assertFalse(path.isEmpty());

        Path cubic = new Path();
        cubic.cubicTo(1.0f, 1.0f, 2.0f, 2.0f, 3.0f, 3.0f);
        RectF bounds = new RectF();
        cubic.computeBounds(bounds);
        assertEquals(new RectF(0.0f, 0.0f, 3.0f, 3.0f), bounds);
    }

    @Test
    void closeRestartsTheContourAtItsStart() {
        Path path = new Path();
        path.moveTo(1.0f, 2.0f);
        path.lineTo(5.0f, 2.0f);
        path.close();
        path.lineTo(3.0f, 6.0f);

        RectF bounds = new RectF();
        path.computeBounds(bounds);
        assertEquals(new RectF(1.0f, 2.0f, 5.0f, 6.0f), bounds);
    }

    @Test
    void resetClearsTheGeometry() {
        Path path = new Path();
        path.addRect(new RectF(1.0f, 2.0f, 3.0f, 4.0f), Path.Direction.CW);
        assertFalse(path.isEmpty());

        path.reset();
        assertTrue(path.isEmpty());
        assertEquals(Path.FillType.WINDING, path.getFillType());
    }

    @Test
    void rectBoundsComeFromTheAppendedContour() {
        Path path = new Path();
        path.addRect(2.0f, 4.0f, 8.0f, 10.0f, Path.Direction.CW);

        RectF bounds = new RectF();
        path.computeBounds(bounds);
        assertEquals(new RectF(2.0f, 4.0f, 8.0f, 10.0f), bounds);
    }
}
