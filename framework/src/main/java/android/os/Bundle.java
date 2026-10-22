package android.os;

/**
 * Desktop mapping (Tartine). Ported per-method from
 * aosp-frameworks-base/core/java/android/os/Bundle.java (AOSP): the
 * table lives in BaseBundle (mMap); the reference-type putters/getters
 * below are plain value stores (AOSP putBundle/putParcelable family:
 * {@code mMap.put(key, value)}) and typed getters with the documented
 * default on absence or wrong-typed value. Parcel game (CREATOR,
 * writeToParcel) stays a named gap until the Parcel seam lands.
 */
    public class Bundle extends android.os.BaseBundle implements java.lang.Cloneable, android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;
    public static android.os.Bundle EMPTY;

    public Bundle() {
        super(null, 0);
    }

    /** AOSP Bundle(Bundle): super copy + mFlags; flags are host-irrelevant. */
    public Bundle(android.os.Bundle arg0) {
        super(arg0);
    }

    public Bundle(android.os.PersistableBundle arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;-><init>(Landroid/os/PersistableBundle;)V");
    }

    public Bundle(int arg0) {
        super(null, arg0);
    }

    public Bundle(java.lang.ClassLoader arg0) {
        super(arg0, 0);
    }

    public void clear() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->clear()V");
    }

    public java.lang.Object clone() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->clone()Ljava/lang/Object;");
    }

    public android.os.Bundle deepCopy() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->deepCopy()Landroid/os/Bundle;");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->describeContents()I");
    }

    public android.os.IBinder getBinder(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getBinder(Ljava/lang/String;)Landroid/os/IBinder;");
    }

    /** AOSP getBundle(key). */
    public android.os.Bundle getBundle(java.lang.String arg0) {
        Object o = mMap.get(arg0);
        try {
            return (android.os.Bundle) o;
        } catch (ClassCastException e) {
            return null;
        }
    }

    public byte getByte(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getByte(Ljava/lang/String;)B");
    }

    public java.lang.Byte getByte(java.lang.String arg0, byte arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getByte(Ljava/lang/String;B)Ljava/lang/Byte;");
    }

    public byte[] getByteArray(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getByteArray(Ljava/lang/String;)[B");
    }

    public char getChar(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getChar(Ljava/lang/String;)C");
    }

    public char getChar(java.lang.String arg0, char arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getChar(Ljava/lang/String;C)C");
    }

    public char[] getCharArray(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getCharArray(Ljava/lang/String;)[C");
    }

    /** AOSP getCharSequence(key): null -> null; cast-miss -> null. */
    public java.lang.CharSequence getCharSequence(java.lang.String arg0) {
        return getCharSequence(arg0, null);
    }

    public java.lang.CharSequence getCharSequence(java.lang.String arg0, java.lang.CharSequence arg1) {
        Object o = mMap.get(arg0);
        if (o == null) {
            return arg1;
        }
        try {
            return (CharSequence) o;
        } catch (ClassCastException e) {
            return arg1;
        }
    }

    public java.lang.CharSequence[] getCharSequenceArray(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getCharSequenceArray(Ljava/lang/String;)[Ljava/lang/CharSequence;");
    }

    public java.util.ArrayList<java.lang.CharSequence> getCharSequenceArrayList(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getCharSequenceArrayList(Ljava/lang/String;)Ljava/util/ArrayList;");
    }

    /** AOSP getClassLoader(): super (BaseBundle never leaves it null). */
    public java.lang.ClassLoader getClassLoader() {
        return super.getClassLoader();
    }

    public float getFloat(java.lang.String arg0) {
        return getFloat(arg0, 0.0f);
    }

    /** AOSP getFloat(key, defaultValue). */
    public float getFloat(java.lang.String arg0, float arg1) {
        Object o = mMap.get(arg0);
        if (o == null) {
            return arg1;
        }
        try {
            return (Float) o;
        } catch (ClassCastException e) {
            return arg1;
        }
    }

    public float[] getFloatArray(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getFloatArray(Ljava/lang/String;)[F");
    }

    /** AOSP getIntegerArrayList(key): value-or-null typed get. */
    public java.util.ArrayList<java.lang.Integer> getIntegerArrayList(java.lang.String arg0) {
        return typedList(arg0, java.util.ArrayList.class);
    }

    private <T extends java.util.List<?>> T typedList(String arg0, Class<T> cls) {
        Object o = mMap.get(arg0);
        try {
            return cls.cast(o);
        } catch (ClassCastException e) {
            return null;
        }
    }

    public <T> T getParcelable(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getParcelable(Ljava/lang/String;)Landroid/os/Parcelable;");
    }

    public <T> T getParcelable(java.lang.String arg0, java.lang.Class<T> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getParcelable(Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;");
    }

    public android.os.Parcelable[] getParcelableArray(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getParcelableArray(Ljava/lang/String;)[Landroid/os/Parcelable;");
    }

    public <T> T[] getParcelableArray(java.lang.String arg0, java.lang.Class<T> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getParcelableArray(Ljava/lang/String;Ljava/lang/Class;)[Ljava/lang/Object;");
    }

    public <T> java.util.ArrayList<T> getParcelableArrayList(java.lang.String arg0) {
        // Erased AOSP contract: (ArrayList<T>) mMap.get(key) or null.
        Object o = mMap.get(arg0);
        try {
            return (java.util.ArrayList<T>) o;
        } catch (ClassCastException e) {
            return null;
        }
    }

    public <T> java.util.ArrayList<T> getParcelableArrayList(java.lang.String arg0, java.lang.Class<? extends T> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getParcelableArrayList(Ljava/lang/String;Ljava/lang/Class;)Ljava/util/ArrayList;");
    }

    /** AOSP getSerializable(key). */
    public java.io.Serializable getSerializable(java.lang.String arg0) {
        Object o = mMap.get(arg0);
        try {
            return (java.io.Serializable) o;
        } catch (ClassCastException e) {
            return null;
        }
    }

    public <T> T getSerializable(java.lang.String arg0, java.lang.Class<T> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getSerializable(Ljava/lang/String;Ljava/lang/Class;)Ljava/io/Serializable;");
    }

    public short getShort(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getShort(Ljava/lang/String;)S");
    }

    public short getShort(java.lang.String arg0, short arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getShort(Ljava/lang/String;S)S");
    }

    public short[] getShortArray(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getShortArray(Ljava/lang/String;)[S");
    }

    public android.util.Size getSize(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getSize(Ljava/lang/String;)Landroid/util/Size;");
    }

    public android.util.SizeF getSizeF(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getSizeF(Ljava/lang/String;)Landroid/util/SizeF;");
    }

    public <T> android.util.SparseArray<T> getSparseParcelableArray(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getSparseParcelableArray(Ljava/lang/String;)Landroid/util/SparseArray;");
    }

    public <T> android.util.SparseArray<T> getSparseParcelableArray(java.lang.String arg0, java.lang.Class<? extends T> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getSparseParcelableArray(Ljava/lang/String;Ljava/lang/Class;)Landroid/util/SparseArray;");
    }

    /** AOSP getStringArrayList(key): value-or-null typed get. */
    public java.util.ArrayList<java.lang.String> getStringArrayList(java.lang.String arg0) {
        return typedList(arg0, java.util.ArrayList.class);
    }

    public boolean hasFileDescriptors() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->hasFileDescriptors()Z");
    }

    /** AOSP Bundle.putAll(bundle): unparcel() is a no-op; table copy. */
    public void putAll(android.os.Bundle arg0) {
        mMap.putAll(arg0.mMap);
    }

    public void putBinder(java.lang.String arg0, android.os.IBinder arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->putBinder(Ljava/lang/String;Landroid/os/IBinder;)V");
    }

    /** AOSP putBundle(key, value). */
    public void putBundle(java.lang.String arg0, android.os.Bundle arg1) {
        mMap.put(arg0, arg1);
    }

    public void putByte(java.lang.String arg0, byte arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->putByte(Ljava/lang/String;B)V");
    }

    public void putByteArray(java.lang.String arg0, byte[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->putByteArray(Ljava/lang/String;[B)V");
    }

    public void putChar(java.lang.String arg0, char arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->putChar(Ljava/lang/String;C)V");
    }

    public void putCharArray(java.lang.String arg0, char[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->putCharArray(Ljava/lang/String;[C)V");
    }

    /** AOSP putCharSequence(key, value). */
    public void putCharSequence(java.lang.String arg0, java.lang.CharSequence arg1) {
        mMap.put(arg0, arg1);
    }

    public void putCharSequenceArray(java.lang.String arg0, java.lang.CharSequence[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->putCharSequenceArray(Ljava/lang/String;[Ljava/lang/CharSequence;)V");
    }

    public void putCharSequenceArrayList(java.lang.String arg0, java.util.ArrayList<java.lang.CharSequence> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->putCharSequenceArrayList(Ljava/lang/String;Ljava/util/ArrayList;)V");
    }

    /** AOSP putFloat(key, value). */
    public void putFloat(java.lang.String arg0, float arg1) {
        mMap.put(arg0, arg1);
    }

    public void putFloatArray(java.lang.String arg0, float[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->putFloatArray(Ljava/lang/String;[F)V");
    }

    /** AOSP putIntegerArrayList(key, value). */
    public void putIntegerArrayList(java.lang.String arg0, java.util.ArrayList<java.lang.Integer> arg1) {
        mMap.put(arg0, arg1);
    }

    /** AOSP putParcelable(key, value): the Parcelable is stored as-is. */
    public void putParcelable(java.lang.String arg0, android.os.Parcelable arg1) {
        mMap.put(arg0, arg1);
    }

    /** AOSP putParcelableArray(key, value). */
    public void putParcelableArray(java.lang.String arg0, android.os.Parcelable[] arg1) {
        mMap.put(arg0, arg1);
    }

    /** AOSP putParcelableArrayList(key, value). */
    public void putParcelableArrayList(java.lang.String arg0, java.util.ArrayList<? extends android.os.Parcelable> arg1) {
        mMap.put(arg0, arg1);
    }

    /** AOSP putSerializable(key, value). */
    public void putSerializable(java.lang.String arg0, java.io.Serializable arg1) {
        mMap.put(arg0, arg1);
    }

    public void putShort(java.lang.String arg0, short arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->putShort(Ljava/lang/String;S)V");
    }

    public void putShortArray(java.lang.String arg0, short[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->putShortArray(Ljava/lang/String;[S)V");
    }

    public void putSize(java.lang.String arg0, android.util.Size arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->putSize(Ljava/lang/String;Landroid/util/Size;)V");
    }

    public void putSizeF(java.lang.String arg0, android.util.SizeF arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->putSizeF(Ljava/lang/String;Landroid/util/SizeF;)V");
    }

    public void putSparseParcelableArray(java.lang.String arg0, android.util.SparseArray<? extends android.os.Parcelable> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->putSparseParcelableArray(Ljava/lang/String;Landroid/util/SparseArray;)V");
    }

    /** AOSP putStringArrayList(key, value). */
    public void putStringArrayList(java.lang.String arg0, java.util.ArrayList<java.lang.String> arg1) {
        mMap.put(arg0, arg1);
    }

    public void readFromParcel(android.os.Parcel arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->readFromParcel(Landroid/os/Parcel;)V");
    }

    public void remove(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->remove(Ljava/lang/String;)V");
    }

    public void setClassLoader(java.lang.ClassLoader arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->setClassLoader(Ljava/lang/ClassLoader;)V");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
