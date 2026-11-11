package android.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * AOSP parity for the ascending-array int-to-long container. Source:
 * aosp-mirror/platform_frameworks_base
 * core/java/android/util/SparseLongArray.java (ContainerHelpers /
 * GrowingArrayUtils semantics are inlined in the port). Compose's
 * MotionEventAdapter keys pointer ids per input device through this map, so
 * the empty shell died on the first hover event (ADR-0014).
 */
class SparseLongArrayTest {

    @Test
    void startsEmptyAndReturnsTheRequestedDefault() {
        SparseLongArray map = new SparseLongArray();
        assertEquals(0, map.size());
        assertEquals(0L, map.get(42));
        assertEquals(-7L, map.get(42, -7L));
        assertEquals(-1, map.indexOfKey(42));
        assertEquals(-1, map.indexOfValue(42L));
    }

    @Test
    void putKeepsKeysAscendingAndOverwritesExistingKeys() {
        SparseLongArray map = new SparseLongArray();
        map.put(3, 30L);
        map.put(1, 10L);
        map.put(2, 20L);
        map.put(1, 11L);

        assertEquals(3, map.size());
        assertEquals(1, map.keyAt(0));
        assertEquals(2, map.keyAt(1));
        assertEquals(3, map.keyAt(2));
        assertEquals(11L, map.get(1));
        assertEquals(20L, map.get(2));
        assertEquals(30L, map.get(3));

        map.put(2, 21L);
        assertEquals(3, map.size());
        assertEquals(21L, map.valueAt(1));
    }

    @Test
    void deleteAndRemoveAtShiftTheTail() {
        SparseLongArray map = new SparseLongArray();
        map.put(1, 10L);
        map.put(2, 20L);
        map.put(3, 30L);

        map.delete(2);
        assertEquals(2, map.size());
        assertEquals(1, map.keyAt(0));
        assertEquals(3, map.keyAt(1));
        assertEquals(0L, map.get(2));

        map.removeAt(0);
        assertEquals(1, map.size());
        assertEquals(3, map.keyAt(0));
        assertEquals(30L, map.get(3));
    }

    @Test
    void appendUsesTheTailFastPathAndFallsBackToPut() {
        SparseLongArray map = new SparseLongArray();
        map.append(10, 10L);
        map.append(20, 20L);
        map.append(30, 30L);
        assertEquals(3, map.size());
        assertEquals(10, map.keyAt(0));
        assertEquals(20, map.keyAt(1));
        assertEquals(30, map.keyAt(2));

        // Out-of-order append must insert, not clobber the tail.
        map.append(15, 15L);
        assertEquals(4, map.size());
        assertEquals(15, map.keyAt(1));
        assertEquals(15L, map.get(15));

        // Appending an existing key overwrites in place.
        map.append(20, 21L);
        assertEquals(4, map.size());
        assertEquals(21L, map.get(20));
    }

    @Test
    void indexOfKeyAndValueFollowAosp() {
        SparseLongArray map = new SparseLongArray();
        map.put(5, 50L);
        map.put(10, 20L);
        map.put(15, 50L);

        assertEquals(0, map.indexOfKey(5));
        assertEquals(1, map.indexOfKey(10));
        assertEquals(2, map.indexOfKey(15));
        assertEquals(-1, map.indexOfValue(999L));
        // Multiple keys may hold the same value; the first wins.
        assertEquals(0, map.indexOfValue(50L));
        assertEquals(1, map.indexOfValue(20L));
    }

    @Test
    void clearKeepsTheObjectUsable() {
        SparseLongArray map = new SparseLongArray();
        map.put(1, 10L);
        map.put(2, 20L);
        map.clear();
        assertEquals(0, map.size());
        assertEquals(0L, map.get(1));
        map.put(3, 30L);
        assertEquals(1, map.size());
        assertEquals(30L, map.get(3));
    }

    @Test
    void growsPastTheInitialCapacity() {
        SparseLongArray map = new SparseLongArray(0);
        for (int i = 0; i < 100; i++) {
            map.put(i, i * 2L);
        }
        assertEquals(100, map.size());
        assertEquals(0L, map.get(0));
        assertEquals(198L, map.get(99));

        // Descending insertion exercises the grow path with shifting.
        SparseLongArray descending = new SparseLongArray(1);
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
        // of range, exactly like SparseBooleanArray.
        SparseLongArray empty = new SparseLongArray(0);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> empty.keyAt(0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> empty.valueAt(0));

        SparseLongArray map = new SparseLongArray(0);
        map.put(1, 10L);
        assertEquals(10L, map.valueAt(0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> map.keyAt(1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> map.valueAt(1));
    }

    @Test
    void toStringRendersSortedPairs() {
        SparseLongArray map = new SparseLongArray();
        map.put(2, 20L);
        map.put(1, 10L);

        assertEquals("{1=10, 2=20}", map.toString());
        assertEquals("{}", new SparseLongArray().toString());
    }
}
