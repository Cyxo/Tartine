package android.util;

import java.util.Arrays;

/**
 * Sparse int→boolean map ported from AOSP frameworks-base
 * core/java/android/util/SparseBooleanArray.java (keys ascending parallel
 * arrays + binary search; ContainerHelpers/GrowingArrayUtils semantics
 * inlined privately below with per-method cites). The shell needs the real
 * container: Compose's key-input bookkeeping puts/reads mappings through
 * the same call order a device runs.
 */
public class SparseBooleanArray implements Cloneable {

    private int[] mKeys;
    private boolean[] mValues;
    private int mSize;

    /**
     * AOSP .../SparseBooleanArray.java#SparseBooleanArray(): this(0).
     */
    public SparseBooleanArray() {
        this(0);
    }

    /**
     * AOSP .../SparseBooleanArray.java#SparseBooleanArray(int
     * initialCapacity): capacity 0 aliases the shared empty arrays and the
     * shell has no ArrayUtils.newUnpadded* copy (same grow-on-write
     * behavior follows from the private insert/append helpers), so the
     * positive-capacity branch allocates plainly.
     */
    public SparseBooleanArray(int initialCapacity) {
        if (initialCapacity == 0) {
            mKeys = EmptyArray.INT;
            mValues = EmptyArray.BOOLEAN;
        } else {
            mKeys = new int[initialCapacity];
            mValues = new boolean[initialCapacity];
        }
        mSize = 0;
    }

    @Override
    public SparseBooleanArray clone() {
        SparseBooleanArray clone;
        try {
            clone = (SparseBooleanArray) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
        clone.mKeys = mKeys.clone();
        clone.mValues = mValues.clone();
        return clone;
    }

    /**
     * AOSP .../SparseBooleanArray.java#get(int): get(key, false).
     */
    public boolean get(int key) {
        return get(key, false);
    }

    /**
     * AOSP .../SparseBooleanArray.java#get(int, boolean): binary search,
     * fallback value when the key is absent.
     */
    public boolean get(int key, boolean valueIfKeyNotFound) {
        int i = binarySearch(mKeys, mSize, key);
        if (i < 0) {
            return valueIfKeyNotFound;
        }
        return mValues[i];
    }

    /**
     * AOSP .../SparseBooleanArray.java#delete(int): shift both arrays left
     * of the hit index and shrink by one.
     */
    public void delete(int key) {
        int i = binarySearch(mKeys, mSize, key);
        if (i >= 0) {
            System.arraycopy(mKeys, i + 1, mKeys, i, mSize - (i + 1));
            System.arraycopy(mValues, i + 1, mValues, i, mSize - (i + 1));
            mSize--;
        }
    }

    /**
     * AOSP .../SparseBooleanArray.java#removeAt(int): same shift as delete
     * at an already-known index.
     */
    public void removeAt(int index) {
        System.arraycopy(mKeys, index + 1, mKeys, index, mSize - (index + 1));
        System.arraycopy(mValues, index + 1, mValues, index, mSize - (index + 1));
        mSize--;
    }

    /**
     * AOSP .../SparseBooleanArray.java#put(int, boolean): overwrite on hit
     * (GrowingArrayUtils.insert shifts the tail in place), otherwise
     * insert at the complement index growing both arrays.
     */
    public void put(int key, boolean value) {
        int i = binarySearch(mKeys, mSize, key);
        if (i >= 0) {
            mValues[i] = value;
        } else {
            i = ~i;
            mKeys = insert(mKeys, mSize, i, key);
            mValues = insert(mValues, mSize, i, value);
            mSize++;
        }
    }

    /**
     * AOSP .../SparseBooleanArray.java#size: number of live mappings.
     */
    public int size() {
        return mSize;
    }

    /**
     * AOSP .../SparseBooleanArray.java#keyAt(int): upper out-of-range
     * throws AIOOBE (UtilConfig.sThrowExceptionForUpperArrayOutOfBounds
     * defaults true on-device and the check sits in the critical path
     * shape the source shows).
     */
    public int keyAt(int index) {
        if (index >= mSize) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        return mKeys[index];
    }

    /**
     * AOSP .../SparseBooleanArray.java#valueAt(int): same bound rule as
     * keyAt for the value array.
     */
    public boolean valueAt(int index) {
        if (index >= mSize) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        return mValues[index];
    }

    /**
     * AOSP .../SparseBooleanArray.java#setValueAt(int, boolean): writes
     * through the same out-of-range guard.
     */
    public void setValueAt(int index, boolean value) {
        if (index >= mSize) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        mValues[index] = value;
    }

    /**
     * AOSP .../SparseBooleanArray.java#indexOfKey(int): the binary search
     * position (negative when absent).
     */
    public int indexOfKey(int key) {
        return binarySearch(mKeys, mSize, key);
    }

    /**
     * AOSP .../SparseBooleanArray.java#indexOfValue(boolean): linear scan
     * (unlike the key lookup multiple keys may hold the value; the first
     * wins), -1 when none.
     */
    public int indexOfValue(boolean value) {
        for (int i = 0; i < mSize; i++) {
            if (mValues[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * AOSP .../SparseBooleanArray.java#clear(): size reset only, arrays
     * stay allocated.
     */
    public void clear() {
        mSize = 0;
    }

    /**
     * AOSP .../SparseBooleanArray.java#append(int, boolean): the
     * ascending-optimization arm falls back to put for out-of-order keys;
     * in-order appends grow both arrays at the tail.
     */
    public void append(int key, boolean value) {
        if (mSize != 0 && key <= mKeys[mSize - 1]) {
            put(key, value);
            return;
        }
        mKeys = growAppend(mKeys, mSize, key);
        mValues = growAppend(mValues, mSize, value);
        mSize++;
    }

    /**
     * AOSP .../SparseBooleanArray.java#hashCode: seed mSize, then the
     * exact per-entry mix the source shows (31*hash + key | value-bit).
     */
    @Override
    public int hashCode() {
        int hashCode = mSize;
        for (int i = 0; i < mSize; i++) {
            hashCode = 31 * hashCode + mKeys[i] | (mValues[i] ? 1 : 0);
        }
        return hashCode;
    }

    /**
     * AOSP .../SparseBooleanArray.java#equals: size + pairwise
     * keys/values.
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (!(that instanceof SparseBooleanArray)) {
            return false;
        }
        SparseBooleanArray other = (SparseBooleanArray) that;
        if (mSize != other.mSize) {
            return false;
        }
        for (int i = 0; i < mSize; i++) {
            if (mKeys[i] != other.mKeys[i]) {
                return false;
            }
            if (mValues[i] != other.mValues[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * AOSP .../SparseBooleanArray.java#toString: "{k=v, k=v}" over the
     * mappings, "{}" when empty.
     */
    @Override
    public String toString() {
        if (size() <= 0) {
            return "{}";
        }
        StringBuilder buffer = new StringBuilder(mSize * 28);
        buffer.append('{');
        for (int i = 0; i < mSize; i++) {
            if (i > 0) {
                buffer.append(", ");
            }
            buffer.append(mKeys[i]);
            buffer.append('=');
            buffer.append(mValues[i]);
        }
        buffer.append('}');
        return buffer.toString();
    }

    /**
     * AOSP core/java/android/util/ContainerHelpers.java#binarySearch(int[],
     * int, int): the Arrays.binarySearch walk without the argument
     * validation; the ~complement of the not-found position drives insert.
     */
    private static int binarySearch(int[] array, int size, int value) {
        int lo = 0;
        int hi = size - 1;
        while (lo <= hi) {
            final int mid = (lo + hi) >>> 1;
            final int midVal = array[mid];
            if (midVal < value) {
                lo = mid + 1;
            } else if (midVal > value) {
                hi = mid - 1;
            } else {
                return mid;
            }
        }
        return ~lo;
    }

    /**
     * AOSP core/java/com/android/internal/util/GrowingArrayUtils.java#
     * insert(int[]/boolean[]): in-place tail shift with room, else a
     * growSize() allocation and a split copy around the insertion index.
     */
    private static int[] insert(int[] array, int currentSize, int index, int element) {
        if (currentSize + 1 <= array.length) {
            System.arraycopy(array, index, array, index + 1, currentSize - index);
            array[index] = element;
            return array;
        }
        int[] newArray = new int[growSize(currentSize)];
        System.arraycopy(array, 0, newArray, 0, index);
        newArray[index] = element;
        System.arraycopy(array, index, newArray, index + 1, array.length - index);
        return newArray;
    }

    private static boolean[] insert(boolean[] array, int currentSize, int index, boolean element) {
        if (currentSize + 1 <= array.length) {
            System.arraycopy(array, index, array, index + 1, currentSize - index);
            array[index] = element;
            return array;
        }
        boolean[] newArray = new boolean[growSize(currentSize)];
        System.arraycopy(array, 0, newArray, 0, index);
        newArray[index] = element;
        System.arraycopy(array, index, newArray, index + 1, array.length - index);
        return newArray;
    }

    /**
     * AOSP core/java/com/android/internal/util/GrowingArrayUtils.java#
     * append + growSize: tail write with room, else growSize(currentSize)
     * capacity; 4 or less grows to 8, larger doubles (the boolean arm
     * mirrors the int one, value copied in place). Arrays.toString-free:
     * clone-with-copy semantics of the source's arraycopy prologue kept.
     */
    private static int[] growAppend(int[] array, int currentSize, int element) {
        if (currentSize + 1 > array.length) {
            array = Arrays.copyOf(array, growSize(currentSize));
        }
        array[currentSize] = element;
        return array;
    }

    private static boolean[] growAppend(boolean[] array, int currentSize, boolean element) {
        if (currentSize + 1 > array.length) {
            array = Arrays.copyOf(array, growSize(currentSize));
        }
        array[currentSize] = element;
        return array;
    }

    /**
     * AOSP core/java/com/android/internal/util/GrowingArrayUtils.java#
     * growSize: currentSize <= 4 ? 8 : currentSize * 2.
     */
    private static int growSize(int currentSize) {
        return currentSize <= 4 ? 8 : currentSize * 2;
    }

    /** AOSP core/java/android/util/EmptyArray.java (shared empties). */
    private static final class EmptyArray {
        static final int[] INT = new int[0];
        static final boolean[] BOOLEAN = new boolean[0];
    }
}
