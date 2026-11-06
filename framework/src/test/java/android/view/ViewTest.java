package android.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Render-loop seam pins: the dirty surrogate and the layout offset cluster. */
class ViewTest {

    @Test
    void dirtyFlagIsConsumedOncePerTraversal() {
        View view = new View(null);

        assertFalse(view.tartineConsumeDirty());

        view.invalidate();
        assertTrue(view.tartineConsumeDirty());
        assertFalse(view.tartineConsumeDirty());
    }

    @Test
    void layoutOffsetsMoveTheBoundsAndDirtyTheView() {
        View view = new View(null);
        view.layout(0, 0, 100, 50);
        assertFalse(view.tartineConsumeDirty());

        view.offsetLeftAndRight(10);
        view.offsetTopAndBottom(-5);

        assertEquals(10, view.getLeft());
        assertEquals(-5, view.getTop());
        assertEquals(110, view.getRight());
        assertEquals(45, view.getBottom());
        assertTrue(view.tartineConsumeDirty());
    }

    @Test
    void unoffsetDoesNotDirtyTheView() {
        View view = new View(null);
        view.layout(0, 0, 100, 50);
        view.tartineConsumeDirty();

        view.offsetLeftAndRight(0);
        view.offsetTopAndBottom(0);

        assertFalse(view.tartineConsumeDirty());
        assertEquals(0, view.getLeft());
        assertEquals(0, view.getTop());
    }
}
