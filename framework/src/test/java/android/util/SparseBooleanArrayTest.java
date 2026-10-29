package android.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * AOSP parity for the ascending-array int-to-boolean container. Source:
 * aosp-mirror/platform_frameworks_base
 * core/java/android/util/SparseBooleanArray.java (ContainerHelpers /
 * GrowingArrayUtils semantics are inlined in the port).
 */
class SparseBooleanArrayTest {

    @Test
    void startsEmptyAndReturnsTheRequestedDefault() {
        SparseBooleanArray map = new SparseBooleanArray();
        assertEquals(0, map.size());
        assertFalse(map.get(42));
        assertTrue(map.get(42, true));
        assertEquals(-1, map.indexOfKey(42));
        assertEquals(-1, map.indexOfValue(false));
    }

    @Test
    void putKeepsKeysAscendingAndOverwritesExistingKeys() {
        SparseBooleanArray map = new SparseBooleanArray();
        map.put(3, true);
        map.put(1, false);
        map.put(2, true);
        map.put(1, true);

        assertEquals(3, map.size());
        assertEquals(1, map.keyAt(0));
        assertEquals(2, map.keyAt(1));
        assertEquals(3, map.keyAt(2));
        assertTrue(map.get(1));
        assertTrue(map.get(2));
        assertTrue(map.get(3));

        map.put(2, false);
        assertEquals(3, map.size());
        assertFalse(map.valueAt(1));
    }

    @Test
    void deleteAndRemoveAtShiftTheTail() {
        SparseBooleanArray map = new SparseBooleanArray();
        map.put(1, true);
        map.put(2, true);
        map.put(3, true);

        map.delete(2);
        assertEquals(2, map.size());
        assertEquals(1, map.keyAt(0));
        assertEquals(3, map.keyAt(1));
        assertFalse(map.get(2));

        map.removeAt(0);
        assertEquals(1, map.size());
        assertEquals(3, map.keyAt(0));
        assertTrue(map.get(3));
    }

    @Test
    void appendUsesTheTailFastPathAndFallsBackToPut() {
        SparseBooleanArray map = new SparseBooleanArray();
        map.append(10, true);
        map.append(20, false);
        map.append(30, true);
        assertEquals(3, map.size());
        assertEquals(10, map.keyAt(0));
        assertEquals(20, map.keyAt(1));
        assertEquals(30, map.keyAt(2));

        // Out-of-order append must insert, not clobber the tail.
        map.append(15, true);
        assertEquals(4, map.size());
        assertEquals(15, map.keyAt(1));
        assertTrue(map.get(15));

        // Appending an existing key overwrites in place.
        map.append(20, true);
        assertEquals(4, map.size());
        assertTrue(map.get(20));
    }

    @Test
    void indexOfKeyAndValueFollowAosp() {
        SparseBooleanArray map = new SparseBooleanArray();
        map.put(5, true);
        map.put(10, false);
        map.put(15, true);

        assertEquals(0, map.indexOfKey(5));
        assertEquals(1, map.indexOfKey(10));
        assertEquals(2, map.indexOfKey(15));
        assertTrue(map.indexOfKey(7) < 0);

        // Multiple keys may hold the same value; the first wins.
        assertEquals(0, map.indexOfValue(true));
        assertEquals(1, map.indexOfValue(false));

        SparseBooleanArray onlyTrue = new SparseBooleanArray();
        onlyTrue.put(1, true);
        assertEquals(-1, onlyTrue.indexOfValue(false));
    }

    @Test
    void clearKeepsTheObjectUsable() {
        SparseBooleanArray map = new SparseBooleanArray();
        map.put(1, true);
        map.put(2, false);
        map.clear();
        assertEquals(0, map.size());
        assertFalse(map.get(1));
        map.put(3, true);
        assertEquals(1, map.size());
        assertTrue(map.get(3));
    }

    @Test
    void cloneIsIndependent() {
        SparseBooleanArray original = new SparseBooleanArray();
        original.put(1, true);
        SparseBooleanArray copy = original.clone();
        assertEquals(original, copy);

        copy.put(2, false);
        assertEquals(1, original.size());
        assertEquals(2, copy.size());
        assertFalse(original.get(2));
    }

    @Test
    void setValueAtWritesThroughTheIndex() {
        SparseBooleanArray map = new SparseBooleanArray();
        map.put(1, false);
        map.setValueAt(0, true);
        assertTrue(map.valueAt(0));
        assertTrue(map.get(1));
    }

    @Test
    void outOfRangeIndexesThrow() {
        SparseBooleanArray empty = new SparseBooleanArray();
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> empty.keyAt(0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> empty.valueAt(0));
        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> empty.setValueAt(0, true));

        SparseBooleanArray map = new SparseBooleanArray();
        map.put(1, true);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> map.keyAt(1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> map.valueAt(1));
        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> map.setValueAt(1, false));
    }

    @Test
    void equalsHashCodeAndToString() {
        SparseBooleanArray a = new SparseBooleanArray();
        a.put(1, true);
        a.put(2, false);
        SparseBooleanArray b = new SparseBooleanArray();
        b.put(1, true);
        b.put(2, false);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, new SparseBooleanArray());
        assertNotEquals(a, null);
        assertNotEquals(a, "1=true");

        assertEquals("{1=true, 2=false}", a.toString());
        assertEquals("{}", new SparseBooleanArray().toString());
    }
}
