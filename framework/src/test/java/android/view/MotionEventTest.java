package android.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** Pointer-coordinate pins for the host input path (ADR-0014). */
class MotionEventTest {

    @Test
    void offsetLocationMovesThePointerButKeepsTheRawLocation() {
        MotionEvent event = MotionEvent.obtain(0L, 0L,
                MotionEvent.ACTION_DOWN, 10f, 20f, 0);

        event.offsetLocation(5f, -3f);

        assertEquals(15f, event.getX());
        assertEquals(17f, event.getY());
        assertEquals(10f, event.getRawX());
        assertEquals(20f, event.getRawY());
    }

    @Test
    void obtainCopiesThePointerCoords() {
        MotionEvent original = MotionEvent.obtain(0L, 0L,
                MotionEvent.ACTION_DOWN, 10f, 20f, 0);

        MotionEvent copy = MotionEvent.obtain(original);
        copy.offsetLocation(1f, 1f);

        assertEquals(10f, original.getX());
        assertEquals(11f, copy.getX());
        assertEquals(10f, copy.getRawX());
    }

    @Test
    void sixArgObtainBuildsOnePointerWithTheMetaState() {
        MotionEvent event = MotionEvent.obtain(0L, 0L,
                MotionEvent.ACTION_DOWN, 10f, 20f,
                KeyEvent.META_SHIFT_ON);

        assertEquals(1, event.getPointerCount());
        assertEquals(10f, event.getX());
        assertEquals(KeyEvent.META_SHIFT_ON, event.getMetaState());
    }
}
