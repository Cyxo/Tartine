package android.graphics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * AOSP parity for the pure-Java float rectangle. Source:
 * aosp-mirror/platform_frameworks_base graphics/java/android/graphics/
 * RectF.java (every method ported here is plain Java on the device; only
 * parcel serialization stays a gap).
 */
class FloatRectTest {

    @Test
    void constructorAndAccessors() {
        RectF r = new RectF(10.0f, 20.0f, 30.0f, 50.0f);
        assertEquals(10.0f, r.left);
        assertEquals(20.0f, r.top);
        assertEquals(30.0f, r.right);
        assertEquals(50.0f, r.bottom);
        assertEquals(20.0f, r.width());
        assertEquals(30.0f, r.height());
        assertEquals(20.0f, r.centerX());
        assertEquals(35.0f, r.centerY());
    }

    @Test
    void copyConstructorsAndSet() {
        RectF source = new RectF(1.5f, 2.5f, 3.5f, 4.5f);
        assertEquals(source, new RectF(source));

        Rect intSource = new Rect(1, 2, 3, 4);
        assertEquals(new RectF(1.0f, 2.0f, 3.0f, 4.0f), new RectF(intSource));

        RectF target = new RectF();
        target.set(source);
        assertEquals(source, target);
        target.set(9.0f, 8.0f, 7.0f, 6.0f);
        assertEquals(9.0f, target.left);
        assertEquals(6.0f, target.bottom);
    }

    @Test
    void emptyContainsNothingButItself() {
        RectF empty = new RectF();
        assertTrue(empty.isEmpty());
        assertTrue(new RectF(10.0f, 0.0f, 5.0f, 5.0f).isEmpty());
        assertTrue(new RectF(0.0f, 10.0f, 5.0f, 5.0f).isEmpty());

        RectF r = new RectF(0.0f, 0.0f, 10.0f, 10.0f);
        assertFalse(r.isEmpty());
        assertTrue(r.contains(0.0f, 0.0f));
        assertFalse(r.contains(10.0f, 10.0f)); // right/bottom are exclusive
        assertTrue(r.contains(new RectF(1.0f, 1.0f, 9.0f, 9.0f)));
        // AOSP RectF.contains(RectF) is inclusive on every edge, so a
        // rectangle contains itself (unlike Rect.contains(Rect)).
        assertTrue(r.contains(new RectF(0.0f, 0.0f, 10.0f, 10.0f)));
        assertFalse(empty.contains(empty));

        RectF e = new RectF(1.0f, 1.0f, 2.0f, 2.0f);
        e.setEmpty();
        assertTrue(e.isEmpty());
        assertEquals(0.0f, e.left);
    }

    @Test
    void insetShrinksAndOffsetMoves() {
        RectF r = new RectF(0.0f, 0.0f, 10.0f, 10.0f);
        r.inset(2.0f, 3.0f);
        assertEquals(new RectF(2.0f, 3.0f, 8.0f, 7.0f), r);

        r.offset(1.0f, -1.0f);
        assertEquals(new RectF(3.0f, 2.0f, 9.0f, 6.0f), r);

        r.offsetTo(20.0f, 30.0f);
        assertEquals(new RectF(20.0f, 30.0f, 26.0f, 34.0f), r);
    }

    @Test
    void intersectClampsAndReportsDisjoint() {
        RectF r = new RectF(0.0f, 0.0f, 10.0f, 10.0f);
        assertTrue(r.intersect(5.0f, 5.0f, 20.0f, 20.0f));
        assertEquals(new RectF(5.0f, 5.0f, 10.0f, 10.0f), r);

        assertFalse(r.intersect(new RectF(20.0f, 20.0f, 30.0f, 30.0f)));
        assertEquals(new RectF(5.0f, 5.0f, 10.0f, 10.0f), r);

        RectF a = new RectF(0.0f, 0.0f, 4.0f, 4.0f);
        RectF b = new RectF(2.0f, 2.0f, 6.0f, 6.0f);
        assertTrue(RectF.intersects(a, b));
        assertTrue(a.intersects(2.0f, 2.0f, 6.0f, 6.0f));
        assertFalse(a.intersects(4.0f, 4.0f, 6.0f, 6.0f));

        RectF target = new RectF();
        assertTrue(target.setIntersect(a, b));
        assertEquals(new RectF(2.0f, 2.0f, 4.0f, 4.0f), target);
    }

    @Test
    void unionExpandsToCoverBoth() {
        RectF r = new RectF(1.0f, 1.0f, 2.0f, 2.0f);
        r.union(0.0f, 3.0f);
        assertEquals(new RectF(0.0f, 1.0f, 2.0f, 3.0f), r);

        r.union(new RectF(-1.0f, 2.0f, 5.0f, 4.0f));
        assertEquals(new RectF(-1.0f, 1.0f, 5.0f, 4.0f), r);

        // An empty rectangle adopts the non-empty union argument wholesale.
        RectF empty = new RectF();
        empty.union(new RectF(2.0f, 3.0f, 4.0f, 5.0f));
        assertEquals(new RectF(2.0f, 3.0f, 4.0f, 5.0f), empty);
    }

    @Test
    void roundRoundingAndSort() {
        RectF r = new RectF(1.4f, 2.6f, 3.5f, 4.2f);
        Rect rounded = new Rect();
        r.round(rounded);
        assertEquals(new Rect(1, 3, 4, 4), rounded);

        Rect out = new Rect();
        r.roundOut(out);
        assertEquals(new Rect(1, 2, 4, 5), out);

        RectF inverted = new RectF(10.0f, 20.0f, 0.0f, 5.0f);
        inverted.sort();
        assertEquals(new RectF(0.0f, 5.0f, 10.0f, 20.0f), inverted);
    }

    @Test
    void valueSemanticsAndText() {
        RectF a = new RectF(1.0f, 2.0f, 3.0f, 4.0f);
        RectF b = new RectF(1.0f, 2.0f, 3.0f, 4.0f);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, new RectF());
        assertNotEquals(a, null);
        assertEquals("RectF(1.0, 2.0, 3.0, 4.0)", a.toString());
        assertEquals("[1.0,2.0][3.0,4.0]", a.toShortString());
    }
}
