package android.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * AOSP parity for the ascending-array int container. Source:
 * aosp-mirror/platform_frameworks_base core/java/android/util/SparseIntArray.java
 * (ContainerHelpers / GrowingArrayUtils semantics are inlined in the port).
 * ConstraintSet keys widget ids and design-time attributes through this map
 * during inflation.
 */
class SparseIntArrayTest {

    @Test
    void startsEmptyAndReturnsTheRequestedDefault() {
        SparseIntArray map = new SparseIntArray();
        assertEquals(0, map.size());
        assertEquals(0, map.get(42));
        assertEquals(-7, map.get(42, -7));
        assertEquals(-1, map.indexOfKey(42));
        assertEquals(-1, map.indexOfValue(42));
    }

    @Test
    void putKeepsKeysAscendingAndOverwritesExistingKeys() {
        SparseIntArray map = new SparseIntArray();
        map.put(3, 30);
        map.put(1, 10);
        map.put(2, 20);
        map.put(1, 11);

        assertEquals(3, map.size());
        assertEquals(1, map.keyAt(0));
        assertEquals(2, map.keyAt(1));
        assertEquals(3, map.keyAt(2));
        assertEquals(11, map.get(1));
        assertEquals(20, map.get(2));
        assertEquals(30, map.get(3));

        map.put(2, 21);
        assertEquals(3, map.size());
        assertEquals(21, map.valueAt(1));
    }

    @Test
    void deleteAndRemoveAtShiftTheTail() {
        SparseIntArray map = new SparseIntArray();
        map.put(1, 10);
        map.put(2, 20);
        map.put(3, 30);

        map.delete(2);
        assertEquals(2, map.size());
        assertEquals(1, map.keyAt(0));
        assertEquals(3, map.keyAt(1));
        assertEquals(0, map.get(2));

        map.removeAt(0);
        assertEquals(1, map.size());
        assertEquals(3, map.keyAt(0));
        assertEquals(30, map.get(3));
    }

    @Test
    void appendUsesTheTailFastPathAndFallsBackToPut() {
        SparseIntArray map = new SparseIntArray();
        map.append(10, 10);
        map.append(20, 20);
        map.append(30, 30);
        assertEquals(3, map.size());
        assertEquals(10, map.keyAt(0));
        assertEquals(20, map.keyAt(1));
        assertEquals(30, map.keyAt(2));

        // Out-of-order append must insert, not clobber the tail.
        map.append(15, 15);
        assertEquals(4, map.size());
        assertEquals(15, map.keyAt(1));
        assertEquals(15, map.get(15));

        // Appending an existing key overwrites in place.
        map.append(20, 21);
        assertEquals(4, map.size());
        assertEquals(21, map.get(20));
    }

    @Test
    void indexOfKeyAndValueFollowAosp() {
        SparseIntArray map = new SparseIntArray();
        map.put(5, 50);
        map.put(10, 20);
        map.put(15, 50);

        assertEquals(0, map.indexOfKey(5));
        assertEquals(1, map.indexOfKey(10));
        assertEquals(2, map.indexOfKey(15));
        assertEquals(-1, map.indexOfValue(999));
        // Multiple keys may hold the same value; the first wins.
        assertEquals(0, map.indexOfValue(50));
        assertEquals(1, map.indexOfValue(20));
        // Absent keys report the insertion point's complement.
        assertEquals(~1, map.indexOfKey(7));
        assertEquals(~3, map.indexOfKey(99));
    }

    @Test
    void setValueAtReplacesWithoutTouchingTheKeyOrder() {
        SparseIntArray map = new SparseIntArray();
        map.put(1, 10);
        map.put(2, 20);
        map.setValueAt(0, 11);

        assertEquals(11, map.get(1));
        assertEquals(2, map.keyAt(1));
        assertEquals(20, map.get(2));
    }

    @Test
    void clearKeepsTheObjectUsable() {
        SparseIntArray map = new SparseIntArray();
        map.put(1, 10);
        map.put(2, 20);
        map.clear();
        assertEquals(0, map.size());
        assertEquals(0, map.get(1));
        map.put(3, 30);
        assertEquals(1, map.size());
        assertEquals(30, map.get(3));
    }

    @Test
    void cloneCopiesTheBackingArrays() {
        SparseIntArray map = new SparseIntArray();
        map.put(1, 10);
        map.put(2, 20);

        SparseIntArray copy = map.clone();
        assertNotSame(map, copy);
        map.put(3, 30);
        map.put(1, 11);
        assertEquals(2, copy.size());
        assertEquals(10, copy.get(1));
        assertEquals(0, copy.get(3));
        assertArrayEquals(new int[] {1, 2},
                new int[] {copy.keyAt(0), copy.keyAt(1)});
    }

    @Test
    void growsPastTheInitialCapacity() {
        SparseIntArray map = new SparseIntArray(0);
        for (int i = 0; i < 100; i++) {
            map.put(i, i * 2);
        }
        assertEquals(100, map.size());
        assertEquals(0, map.get(0));
        assertEquals(198, map.get(99));

        // Descending insertion exercises the grow path with shifting.
        SparseIntArray descending = new SparseIntArray(1);
        for (int i = 50; i >= 0; i--) {
            descending.put(i, i);
        }
        assertEquals(51, descending.size());
        assertEquals(0, descending.keyAt(0));
        assertEquals(50, descending.keyAt(50));
    }

    @Test
    void outOfArrayIndexesThrow() {
        // AOSP's keyAt/valueAt index the backing array, not the logical size:
        // only a capacity-0 map (shared empty arrays) puts a plain index out
        // of range, exactly like SparseLongArray.
        SparseIntArray empty = new SparseIntArray(0);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> empty.keyAt(0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> empty.valueAt(0));

        SparseIntArray map = new SparseIntArray(0);
        map.put(1, 10);
        assertEquals(10, map.valueAt(0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> map.keyAt(1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> map.valueAt(1));
    }

    @Test
    void toStringRendersSortedPairs() {
        SparseIntArray map = new SparseIntArray();
        map.put(2, 20);
        map.put(1, 10);

        assertEquals("{1=10, 2=20}", map.toString());
        assertEquals("{}", new SparseIntArray().toString());
    }
}
