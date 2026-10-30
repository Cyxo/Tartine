package android.os;

import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Desktop mapping (Tartine). Ported per-method from
 * aosp-frameworks-base/core/java/android/os/Bundle.java (AOSP): the
 * table lives in BaseBundle (mMap); the reference-type putters/getters
 * below are plain value stores (AOSP putBundle/putParcelable family:
 * {@code mMap.put(key, value)}) and typed getters with the documented
 * default on absence or wrong-typed value. Parcel game (CREATOR,
 * writeToParcel) stays a named gap until the Parcel seam lands.
 */
    public class Bundle extends BaseBundle implements Cloneable, Parcelable {

    public static Parcelable.Creator CREATOR;
    public static Bundle EMPTY;

    public Bundle() {
        super(null, 0);
    }

    /** AOSP Bundle(Bundle): super copy + mFlags; flags are host-irrelevant. */
    public Bundle(Bundle arg0) {
        super(arg0);
    }

    public Bundle(PersistableBundle arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;-><init>(Landroid/os/PersistableBundle;)V");
    }

    public Bundle(int arg0) {
        super(null, arg0);
    }

    public Bundle(ClassLoader arg0) {
        super(arg0, 0);
    }

    public void clear() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->clear()V");
    }

    public Object clone() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->clone()Ljava/lang/Object;");
    }

    public Bundle deepCopy() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->deepCopy()Landroid/os/Bundle;");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->describeContents()I");
    }

    public IBinder getBinder(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getBinder(Ljava/lang/String;)Landroid/os/IBinder;");
    }

    /** AOSP getBundle(key). */
    public Bundle getBundle(String arg0) {
        Object o = mMap.get(arg0);
        try {
            return (Bundle) o;
        } catch (ClassCastException e) {
            return null;
        }
    }

    public byte getByte(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getByte(Ljava/lang/String;)B");
    }

    public Byte getByte(String arg0, byte arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getByte(Ljava/lang/String;B)Ljava/lang/Byte;");
    }

    public byte[] getByteArray(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getByteArray(Ljava/lang/String;)[B");
    }

    public char getChar(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getChar(Ljava/lang/String;)C");
    }

    public char getChar(String arg0, char arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getChar(Ljava/lang/String;C)C");
    }

    public char[] getCharArray(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getCharArray(Ljava/lang/String;)[C");
    }

    /** AOSP getCharSequence(key): null -> null; cast-miss -> null. */
    public CharSequence getCharSequence(String arg0) {
        return getCharSequence(arg0, null);
    }

    public CharSequence getCharSequence(String arg0, CharSequence arg1) {
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

    public CharSequence[] getCharSequenceArray(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getCharSequenceArray(Ljava/lang/String;)[Ljava/lang/CharSequence;");
    }

    public ArrayList<CharSequence> getCharSequenceArrayList(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getCharSequenceArrayList(Ljava/lang/String;)Ljava/util/ArrayList;");
    }

    /** AOSP getClassLoader(): super (BaseBundle never leaves it null). */
    public ClassLoader getClassLoader() {
        return super.getClassLoader();
    }

    public float getFloat(String arg0) {
        return getFloat(arg0, 0.0f);
    }

    /** AOSP getFloat(key, defaultValue). */
    public float getFloat(String arg0, float arg1) {
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

    public float[] getFloatArray(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getFloatArray(Ljava/lang/String;)[F");
    }

    /** AOSP getIntegerArrayList(key): value-or-null typed get. */
    public ArrayList<Integer> getIntegerArrayList(String arg0) {
        return typedList(arg0, ArrayList.class);
    }

    private <T extends List<?>> T typedList(String arg0, Class<T> cls) {
        Object o = mMap.get(arg0);
        try {
            return cls.cast(o);
        } catch (ClassCastException e) {
            return null;
        }
    }

    public <T> T getParcelable(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getParcelable(Ljava/lang/String;)Landroid/os/Parcelable;");
    }

    public <T> T getParcelable(String arg0, Class<T> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getParcelable(Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;");
    }

    public Parcelable[] getParcelableArray(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getParcelableArray(Ljava/lang/String;)[Landroid/os/Parcelable;");
    }

    public <T> T[] getParcelableArray(String arg0, Class<T> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getParcelableArray(Ljava/lang/String;Ljava/lang/Class;)[Ljava/lang/Object;");
    }

    public <T> ArrayList<T> getParcelableArrayList(String arg0) {
        // Erased AOSP contract: (ArrayList<T>) mMap.get(key) or null.
        Object o = mMap.get(arg0);
        try {
            return (ArrayList<T>) o;
        } catch (ClassCastException e) {
            return null;
        }
    }

    public <T> ArrayList<T> getParcelableArrayList(String arg0, Class<? extends T> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getParcelableArrayList(Ljava/lang/String;Ljava/lang/Class;)Ljava/util/ArrayList;");
    }

    /** AOSP getSerializable(key). */
    public Serializable getSerializable(String arg0) {
        Object o = mMap.get(arg0);
        try {
            return (Serializable) o;
        } catch (ClassCastException e) {
            return null;
        }
    }

    public <T> T getSerializable(String arg0, Class<T> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getSerializable(Ljava/lang/String;Ljava/lang/Class;)Ljava/io/Serializable;");
    }

    public short getShort(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getShort(Ljava/lang/String;)S");
    }

    public short getShort(String arg0, short arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getShort(Ljava/lang/String;S)S");
    }

    public short[] getShortArray(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getShortArray(Ljava/lang/String;)[S");
    }

    public Size getSize(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getSize(Ljava/lang/String;)Landroid/util/Size;");
    }

    public SizeF getSizeF(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getSizeF(Ljava/lang/String;)Landroid/util/SizeF;");
    }

    public <T> SparseArray<T> getSparseParcelableArray(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getSparseParcelableArray(Ljava/lang/String;)Landroid/util/SparseArray;");
    }

    public <T> SparseArray<T> getSparseParcelableArray(String arg0, Class<? extends T> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->getSparseParcelableArray(Ljava/lang/String;Ljava/lang/Class;)Landroid/util/SparseArray;");
    }

    /** AOSP getStringArrayList(key): value-or-null typed get. */
    public ArrayList<String> getStringArrayList(String arg0) {
        return typedList(arg0, ArrayList.class);
    }

    public boolean hasFileDescriptors() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->hasFileDescriptors()Z");
    }

    /** AOSP Bundle.putAll(bundle): unparcel() is a no-op; table copy. */
    public void putAll(Bundle arg0) {
        mMap.putAll(arg0.mMap);
    }

    public void putBinder(String arg0, IBinder arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->putBinder(Ljava/lang/String;Landroid/os/IBinder;)V");
    }

    /** AOSP putBundle(key, value). */
    public void putBundle(String arg0, Bundle arg1) {
        mMap.put(arg0, arg1);
    }

    public void putByte(String arg0, byte arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->putByte(Ljava/lang/String;B)V");
    }

    public void putByteArray(String arg0, byte[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->putByteArray(Ljava/lang/String;[B)V");
    }

    public void putChar(String arg0, char arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->putChar(Ljava/lang/String;C)V");
    }

    public void putCharArray(String arg0, char[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->putCharArray(Ljava/lang/String;[C)V");
    }

    /** AOSP putCharSequence(key, value). */
    public void putCharSequence(String arg0, CharSequence arg1) {
        mMap.put(arg0, arg1);
    }

    public void putCharSequenceArray(String arg0, CharSequence[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->putCharSequenceArray(Ljava/lang/String;[Ljava/lang/CharSequence;)V");
    }

    public void putCharSequenceArrayList(String arg0, ArrayList<CharSequence> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->putCharSequenceArrayList(Ljava/lang/String;Ljava/util/ArrayList;)V");
    }

    /** AOSP putFloat(key, value). */
    public void putFloat(String arg0, float arg1) {
        mMap.put(arg0, arg1);
    }

    public void putFloatArray(String arg0, float[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->putFloatArray(Ljava/lang/String;[F)V");
    }

    /** AOSP putIntegerArrayList(key, value). */
    public void putIntegerArrayList(String arg0, ArrayList<Integer> arg1) {
        mMap.put(arg0, arg1);
    }

    /** AOSP putParcelable(key, value): the Parcelable is stored as-is. */
    public void putParcelable(String arg0, Parcelable arg1) {
        mMap.put(arg0, arg1);
    }

    /** AOSP putParcelableArray(key, value). */
    public void putParcelableArray(String arg0, Parcelable[] arg1) {
        mMap.put(arg0, arg1);
    }

    /** AOSP putParcelableArrayList(key, value). */
    public void putParcelableArrayList(String arg0, ArrayList<? extends Parcelable> arg1) {
        mMap.put(arg0, arg1);
    }

    /** AOSP putSerializable(key, value). */
    public void putSerializable(String arg0, Serializable arg1) {
        mMap.put(arg0, arg1);
    }

    public void putShort(String arg0, short arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->putShort(Ljava/lang/String;S)V");
    }

    public void putShortArray(String arg0, short[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->putShortArray(Ljava/lang/String;[S)V");
    }

    public void putSize(String arg0, Size arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->putSize(Ljava/lang/String;Landroid/util/Size;)V");
    }

    public void putSizeF(String arg0, SizeF arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->putSizeF(Ljava/lang/String;Landroid/util/SizeF;)V");
    }

    public void putSparseParcelableArray(String arg0, SparseArray<? extends Parcelable> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->putSparseParcelableArray(Ljava/lang/String;Landroid/util/SparseArray;)V");
    }

    /** AOSP putStringArrayList(key, value). */
    public void putStringArrayList(String arg0, ArrayList<String> arg1) {
        mMap.put(arg0, arg1);
    }

    public void readFromParcel(Parcel arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->readFromParcel(Landroid/os/Parcel;)V");
    }

    public void remove(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->remove(Ljava/lang/String;)V");
    }

    public void setClassLoader(ClassLoader arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->setClassLoader(Ljava/lang/ClassLoader;)V");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Bundle;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
