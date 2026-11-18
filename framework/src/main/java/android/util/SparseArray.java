package android.util;

/**
 * AOSP {@code android.util.SparseArray}: an int-keyed map backed by a sorted
 * key array and a parallel value array.
 *
 * <p>The platform implementation splits its array helpers into the hidden
 * {@code ContainerHelpers} / {@code GrowingArrayUtils} classes. They are
 * internal to this type, so the shell keeps the same algorithms inline
 * (binary search, grow-by-insert) instead of adding hidden classes nothing
 * else would link against.
 */
public class SparseArray<E> implements Cloneable {

    private static final Object DELETED = new Object();

    private boolean mGarbage = false;
    private int[] mKeys;
    private Object[] mValues;
    private int mSize;

    public SparseArray() {
        this(10);
    }

    public SparseArray(int arg0) {
        if (arg0 == 0) {
            mKeys = new int[0];
            mValues = new Object[0];
        } else {
            mValues = new Object[arg0];
            mKeys = new int[mValues.length];
        }
        mSize = 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public SparseArray<E> clone() {
        try {
            SparseArray<E> clone = (SparseArray<E>) super.clone();
            clone.mKeys = mKeys.clone();
            clone.mValues = mValues.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            // SparseArray is Cloneable; this cannot happen.
            throw new AssertionError(e);
        }
    }

    public void append(int arg0, E arg1) {
        if (mSize != 0 && arg0 <= mKeys[mSize - 1]) {
            put(arg0, arg1);
            return;
        }
        if (mGarbage && mSize >= mKeys.length) {
            gc();
        }
        mKeys = insert(mKeys, mSize, mSize, arg0);
        mValues = insert(mValues, mSize, mSize, arg1);
        mSize++;
    }

    public void clear() {
        int n = mSize;
        Object[] values = mValues;
        for (int i = 0; i < n; i++) {
            values[i] = null;
        }
        mSize = 0;
        mGarbage = false;
    }

    public boolean contains(int arg0) {
        return indexOfKey(arg0) >= 0;
    }

    public boolean contentEquals(SparseArray<?> arg0) {
        if (arg0 == null) {
            return false;
        }
        if (this == arg0) {
            return true;
        }
        if (mSize != arg0.mSize) {
            return false;
        }
        for (int i = 0; i < mSize; i++) {
            if (keyAt(i) != arg0.keyAt(i)) {
                return false;
            }
            E value = valueAt(i);
            Object other = arg0.valueAt(i);
            if (value == null ? other != null : !value.equals(other)) {
                return false;
            }
        }
        return true;
    }

    public int contentHashCode() {
        int hash = 0;
        for (int i = 0; i < mSize; i++) {
            int key = keyAt(i);
            E value = valueAt(i);
            hash = 31 * hash + key;
            hash = 31 * hash + (value == null ? 0 : value.hashCode());
        }
        return hash;
    }

    public void delete(int arg0) {
        remove(arg0);
    }

    @SuppressWarnings("unchecked")
    public E get(int arg0) {
        return get(arg0, null);
    }

    @SuppressWarnings("unchecked")
    public E get(int arg0, E arg1) {
        int i = binarySearch(mKeys, mSize, arg0);
        if (i < 0 || mValues[i] == DELETED) {
            return arg1;
        }
        return (E) mValues[i];
    }

    public int indexOfKey(int arg0) {
        if (mGarbage) {
            gc();
        }
        return binarySearch(mKeys, mSize, arg0);
    }

    public int indexOfValue(E arg0) {
        if (mGarbage) {
            gc();
        }
        for (int i = 0; i < mSize; i++) {
            if (mValues[i] == arg0) {
                return i;
            }
        }
        return -1;
    }

    public int keyAt(int arg0) {
        if (mGarbage) {
            gc();
        }
        return mKeys[arg0];
    }

    @SuppressWarnings("unchecked")
    public E valueAt(int arg0) {
        if (mGarbage) {
            gc();
        }
        return (E) mValues[arg0];
    }

    public void setValueAt(int arg0, E arg1) {
        if (mGarbage) {
            gc();
        }
        mValues[arg0] = arg1;
    }

    public void put(int arg0, E arg1) {
        int i = binarySearch(mKeys, mSize, arg0);
        if (i >= 0) {
            mValues[i] = arg1;
            return;
        }
        i = ~i;
        if (i < mSize && mValues[i] == DELETED) {
            mKeys[i] = arg0;
            mValues[i] = arg1;
            return;
        }
        if (mGarbage && mSize >= mKeys.length) {
            gc();
            // Search again because indices may have changed.
            i = ~binarySearch(mKeys, mSize, arg0);
        }
        mKeys = insert(mKeys, mSize, i, arg0);
        mValues = insert(mValues, mSize, i, arg1);
        mSize++;
    }

    public void remove(int arg0) {
        int i = binarySearch(mKeys, mSize, arg0);
        if (i >= 0) {
            if (mValues[i] != DELETED) {
                mValues[i] = DELETED;
                mGarbage = true;
            }
        }
    }

    public void removeAt(int arg0) {
        if (mValues[arg0] != DELETED) {
            mValues[arg0] = DELETED;
            mGarbage = true;
        }
    }

    public void removeAtRange(int arg0, int arg1) {
        final int end = Math.min(mSize, arg0 + arg1);
        for (int i = arg0; i < end; i++) {
            removeAt(i);
        }
    }

    public void set(int arg0, E arg1) {
        put(arg0, arg1);
    }

    public int size() {
        if (mGarbage) {
            gc();
        }
        return mSize;
    }

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
            int key = keyAt(i);
            buffer.append(key);
            buffer.append('=');
            Object value = valueAt(i);
            if (value != this) {
                buffer.append(value);
            } else {
                buffer.append("(this Map)");
            }
        }
        buffer.append('}');
        return buffer.toString();
    }

    /** AOSP ContainerHelpers.binarySearch (ascending array, size entries). */
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

    /** AOSP GrowingArrayUtils.insert for int arrays. */
    private static int[] insert(int[] array, int currentSize, int index,
            int element) {
        if (currentSize + 1 <= array.length) {
            System.arraycopy(array, index, array, index + 1,
                    currentSize - index);
            array[index] = element;
            return array;
        }
        int[] newArray = new int[growSize(currentSize)];
        System.arraycopy(array, 0, newArray, 0, index);
        newArray[index] = element;
        System.arraycopy(array, index, newArray, index + 1,
                array.length - index);
        return newArray;
    }

    /** AOSP GrowingArrayUtils.insert for object arrays. */
    private static Object[] insert(Object[] array, int currentSize, int index,
            Object element) {
        if (currentSize + 1 <= array.length) {
            System.arraycopy(array, index, array, index + 1,
                    currentSize - index);
            array[index] = element;
            return array;
        }
        Object[] newArray = new Object[growSize(currentSize)];
        System.arraycopy(array, 0, newArray, 0, index);
        newArray[index] = element;
        System.arraycopy(array, index, newArray, index + 1,
                array.length - index);
        return newArray;
    }

    /** AOSP GrowingArrayUtils.growSize: 1.5x growth. */
    private static int growSize(int currentSize) {
        return currentSize <= 4 ? 8 : currentSize + (currentSize >> 1);
    }

    /** AOSP SparseArray.gc: compacts the arrays after removals. */
    private void gc() {
        int n = mSize;
        int o = 0;
        int[] keys = mKeys;
        Object[] values = mValues;
        for (int i = 0; i < n; i++) {
            Object val = values[i];
            if (val != DELETED) {
                if (i != o) {
                    keys[o] = keys[i];
                    values[o] = val;
                    values[i] = null;
                }
                o++;
            }
        }
        mGarbage = false;
        mSize = o;
    }
}
