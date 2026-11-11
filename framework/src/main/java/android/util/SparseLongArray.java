package android.util;

import java.util.Arrays;

/**
 * AOSP android.util.SparseLongArray: an int-keyed map of longs kept in
 * sorted key order with binary-search lookup. Compose's MotionEventAdapter
 * uses it to track pointer ids per input device, so the shell being an empty
 * class made the first hover/pointer event die with {@code
 * NoSuchMethodError: SparseLongArray.clear()}.
 */
public class SparseLongArray {

    private int[] mKeys;
    private long[] mValues;
    private int mSize;

    public SparseLongArray() {
        this(10);
    }

    public SparseLongArray(int initialCapacity) {
        if (initialCapacity == 0) {
            mKeys = new int[0];
            mValues = new long[0];
        } else {
            mKeys = new int[initialCapacity];
            mValues = new long[initialCapacity];
        }
        mSize = 0;
    }

    public long get(int key) {
        return get(key, 0L);
    }

    public long get(int key, long valueIfKeyNotFound) {
        int index = indexOfKey(key);
        if (index < 0) {
            return valueIfKeyNotFound;
        }
        return mValues[index];
    }

    public void delete(int key) {
        int index = indexOfKey(key);
        if (index >= 0) {
            removeAt(index);
        }
    }

    public void removeAt(int index) {
        System.arraycopy(mKeys, index + 1, mKeys, index, mSize - (index + 1));
        System.arraycopy(mValues, index + 1, mValues, index, mSize - (index + 1));
        mSize--;
    }

    public void put(int key, long value) {
        int index = indexOfKey(key);
        if (index >= 0) {
            mValues[index] = value;
            return;
        }
        index = ~index;
        mKeys = insert(mKeys, mSize, index, key);
        mValues = insert(mValues, mSize, index, value);
        mSize++;
    }

    public void append(int key, long value) {
        if (mSize != 0 && key <= mKeys[mSize - 1]) {
            put(key, value);
            return;
        }
        mKeys = insert(mKeys, mSize, mSize, key);
        mValues = insert(mValues, mSize, mSize, value);
        mSize++;
    }

    public int size() {
        return mSize;
    }

    public int keyAt(int index) {
        return mKeys[index];
    }

    public long valueAt(int index) {
        return mValues[index];
    }

    public void clear() {
        mSize = 0;
    }

    /**
     * Returns the index of the key, or its bitwise complement when absent
     * (so {@code ~indexOfKey(k)} is the insertion point), as AOSP does.
     */
    public int indexOfKey(int key) {
        int low = 0;
        int high = mSize - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int midVal = mKeys[mid];
            if (midVal < key) {
                low = mid + 1;
            } else if (midVal > key) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return ~low;
    }

    public int indexOfValue(long value) {
        for (int i = 0; i < mSize; i++) {
            if (mValues[i] == value) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        if (mSize <= 0) {
            return "{}";
        }
        StringBuilder buffer = new StringBuilder(mSize * 28);
        buffer.append('{');
        for (int i = 0; i < mSize; i++) {
            if (i > 0) {
                buffer.append(", ");
            }
            buffer.append(keyAt(i)).append('=').append(valueAt(i));
        }
        buffer.append('}');
        return buffer.toString();
    }

    private static int[] insert(int[] array, int currentSize, int index,
            int value) {
        int[] out = array;
        if (currentSize + 1 > array.length) {
            int grown = array.length + (array.length >> 1) + 1;
            out = Arrays.copyOf(array, Math.max(currentSize + 1, grown));
        }
        System.arraycopy(out, index, out, index + 1, currentSize - index);
        out[index] = value;
        return out;
    }

    private static long[] insert(long[] array, int currentSize, int index,
            long value) {
        long[] out = array;
        if (currentSize + 1 > array.length) {
            int grown = array.length + (array.length >> 1) + 1;
            out = Arrays.copyOf(array, Math.max(currentSize + 1, grown));
        }
        System.arraycopy(out, index, out, index + 1, currentSize - index);
        out[index] = value;
        return out;
    }
}
