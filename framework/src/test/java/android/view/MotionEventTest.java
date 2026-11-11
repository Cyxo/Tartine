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

    /**
     * Mouse buttons ride in the button-state field, never in the action: the
     * action's upper byte is the pointer index for ACTION_POINTER_DOWN/UP, so
     * an ACTION_DOWN carrying it would report a phantom second pointer. This
     * is the contract the host input translator relies on (the hover/click
     * probe hit exactly this as Compose's addFreshIds read pointer 1).
     */
    @Test
    void actionDownCarriesButtonsInTheStateNotTheActionIndex() {
        MotionEvent.PointerProperties properties =
                new MotionEvent.PointerProperties();
        properties.id = 0;
        properties.toolType = MotionEvent.TOOL_TYPE_MOUSE;
        MotionEvent.PointerCoords coords = new MotionEvent.PointerCoords();
        coords.x = 1f;
        coords.y = 2f;

        MotionEvent event = MotionEvent.obtain(0L, 0L,
                MotionEvent.ACTION_DOWN, 1,
                new MotionEvent.PointerProperties[] {properties},
                new MotionEvent.PointerCoords[] {coords}, 0,
                MotionEvent.BUTTON_PRIMARY, 1f, 1f, 0, 0,
                InputDevice.SOURCE_MOUSE, 0);

        assertEquals(0, event.getActionIndex());
        assertEquals(MotionEvent.ACTION_DOWN, event.getActionMasked());
        assertEquals(MotionEvent.BUTTON_PRIMARY, event.getButtonState());
        assertEquals(0, event.getPointerId(0));
    }
}
