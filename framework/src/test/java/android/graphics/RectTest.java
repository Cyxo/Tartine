package android.graphics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * AOSP parity for the pure-Java geometry core. Source:
 * aosp-mirror/platform_frameworks_base
 * graphics/java/android/graphics/Rect.java (every method in this class is
 * plain Java on the device; the framework port cites the method body).
 */
class RectTest {

    @Test
    void constructorAndAccessors() {
        Rect r = new Rect(10, 20, 30, 50);
        assertEquals(10, r.left);
        assertEquals(20, r.top);
        assertEquals(30, r.right);
        assertEquals(50, r.bottom);
        assertEquals(20, r.width());
        assertEquals(30, r.height());
        assertEquals(20, r.centerX());
        assertEquals(35, r.centerY());
        assertEquals(20.0f, r.exactCenterX());
        assertEquals(35.0f, r.exactCenterY());
    }

    @Test
    void defaultAndCopyConstructors() {
        Rect empty = new Rect();
        assertTrue(empty.isEmpty());
        assertEquals(0, empty.left);

        Rect source = new Rect(1, 2, 3, 4);
        Rect copy = new Rect(source);
        assertEquals(source, copy);
        assertNotEquals(source, new Rect());

        // AOSP Rect(Rect) treats null as an empty rectangle.
        Rect nullCopy = new Rect(null);
        assertTrue(nullCopy.isEmpty());

        Rect target = new Rect();
        target.set(source);
        assertEquals(source, target);
        target.set(9, 8, 7, 6);
        assertEquals(9, target.left);
        assertEquals(8, target.top);
        assertEquals(7, target.right);
        assertEquals(6, target.bottom);
    }

    @Test
    void isEmptyFollowsAosp() {
        assertTrue(new Rect().isEmpty());
        assertTrue(new Rect(0, 0, 0, 10).isEmpty());   // left >= right
        assertTrue(new Rect(0, 0, 10, 0).isEmpty());   // top >= bottom
        assertTrue(new Rect(5, 5, 4, 4).isEmpty());    // inverted
        assertFalse(new Rect(0, 0, 1, 1).isEmpty());
    }

    @Test
    void offsetAndOffsetTo() {
        Rect r = new Rect(1, 2, 3, 4);
        r.offset(10, -5);
        assertEquals(new Rect(11, -3, 13, -1), r);
        // Size is unchanged by offset.
        assertEquals(2, r.width());
        assertEquals(2, r.height());

        r.offsetTo(100, 200);
        assertEquals(new Rect(100, 200, 102, 202), r);
    }

    @Test
    void insetShrinksOnEverySide() {
        Rect r = new Rect(0, 0, 100, 50);
        r.inset(5, 10);
        assertEquals(new Rect(5, 10, 95, 40), r);

        r.inset(1, 2, 3, 4);
        assertEquals(new Rect(6, 12, 92, 36), r);
    }

    @Test
    void containsExcludesRightAndBottomEdges() {
        Rect r = new Rect(10, 20, 30, 40);
        assertTrue(r.contains(10, 20));       // left/top inclusive
        assertTrue(r.contains(29, 39));
        assertFalse(r.contains(30, 39));      // right exclusive
        assertFalse(r.contains(29, 40));      // bottom exclusive
        assertFalse(r.contains(9, 20));
        assertFalse(r.contains(10, 19));
    }

    @Test
    void containsRectRequiresFullyInsideAndNonEmptyThis() {
        Rect outer = new Rect(0, 0, 100, 100);
        assertTrue(outer.contains(new Rect(10, 10, 20, 20)));
        assertTrue(outer.contains(new Rect(0, 0, 100, 100)));
        assertFalse(outer.contains(new Rect(-1, 10, 20, 20)));
        assertFalse(outer.contains(new Rect(10, 10, 101, 20)));
        // An empty receiver contains nothing (AOSP's left < right guard).
        assertFalse(new Rect().contains(new Rect(0, 0, 0, 0)));
    }

    @Test
    void intersectMutatesOnlyOnOverlap() {
        Rect r = new Rect(0, 0, 100, 100);
        assertTrue(r.intersect(50, 50, 150, 150));
        assertEquals(new Rect(50, 50, 100, 100), r);

        Rect noOverlap = new Rect(0, 0, 10, 10);
        assertFalse(noOverlap.intersect(20, 20, 30, 30));
        assertEquals(new Rect(0, 0, 10, 10), noOverlap); // untouched

        Rect viaRect = new Rect(0, 0, 100, 100);
        assertTrue(viaRect.intersect(new Rect(25, 25, 75, 75)));
        assertEquals(new Rect(25, 25, 75, 75), viaRect);
    }

    @Test
    void setIntersectWritesTheIntersectionOrLeavesTargetAlone() {
        Rect target = new Rect(1, 2, 3, 4);
        assertTrue(target.setIntersect(new Rect(0, 0, 100, 100),
                new Rect(10, 20, 30, 40)));
        assertEquals(new Rect(10, 20, 30, 40), target);

        assertFalse(target.setIntersect(new Rect(0, 0, 10, 10),
                new Rect(20, 20, 30, 30)));
        assertEquals(new Rect(10, 20, 30, 40), target); // untouched
    }

    @Test
    void intersectsChecksWithoutMutating() {
        Rect a = new Rect(0, 0, 10, 10);
        Rect b = new Rect(5, 5, 15, 15);
        assertTrue(Rect.intersects(a, b));
        assertTrue(a.intersects(5, 5, 15, 15));
        assertFalse(a.intersects(10, 10, 20, 20)); // edges only touch
        assertEquals(new Rect(0, 0, 10, 10), a);
        assertEquals(new Rect(5, 5, 15, 15), b);
    }

    @Test
    void unionExpandsOrAdoptsAnEmptyRect() {
        Rect r = new Rect(0, 0, 10, 10);
        r.union(new Rect(-5, 5, 5, 20));
        assertEquals(new Rect(-5, 0, 10, 20), r);

        // AOSP: an empty receiver adopts the argument wholesale.
        Rect empty = new Rect();
        empty.union(new Rect(10, 20, 30, 40));
        assertEquals(new Rect(10, 20, 30, 40), empty);

        // Degenerate arguments are ignored.
        Rect unchanged = new Rect(0, 0, 10, 10);
        unchanged.union(20, 20, 20, 30);
        assertEquals(new Rect(0, 0, 10, 10), unchanged);
    }

    @Test
    void unionPointExpandsToIncludeThePoint() {
        Rect r = new Rect(10, 10, 20, 20);
        r.union(5, 15);
        assertEquals(new Rect(5, 10, 20, 20), r);
        r.union(25, 30);
        assertEquals(new Rect(5, 10, 25, 30), r);

        // AOSP's empty-rect case keeps left/top at zero and grows the far edges.
        Rect empty = new Rect();
        empty.union(5, 7);
        assertEquals(new Rect(0, 0, 5, 7), empty);
    }

    @Test
    void sortSwapsInvertedEdges() {
        Rect r = new Rect(100, 200, 10, 20);
        r.sort();
        assertEquals(new Rect(10, 20, 100, 200), r);

        Rect alreadySorted = new Rect(1, 2, 3, 4);
        alreadySorted.sort();
        assertEquals(new Rect(1, 2, 3, 4), alreadySorted);
    }

    @Test
    void equalsHashCodeAndStringForms() {
        Rect a = new Rect(1, 2, 3, 4);
        Rect b = new Rect(1, 2, 3, 4);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, new Rect(1, 2, 3, 5));
        assertNotEquals(a, null);
        assertNotEquals(a, "Rect(1, 2, 3, 4)");

        assertEquals("Rect(1, 2 - 3, 4)", a.toString());
        assertEquals("[1,2][3,4]", a.toShortString());
        assertEquals("1 2 3 4", a.flattenToString());
    }

    @Test
    void setEmptyClearsEveryEdge() {
        Rect r = new Rect(1, 2, 3, 4);
        r.setEmpty();
        assertEquals(new Rect(0, 0, 0, 0), r);
        assertTrue(r.isEmpty());
    }
}
