package android.os;

import android.util.ArrayMap;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import java.io.FileDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntFunction;

    public class Parcel {

    public static Parcelable.Creator STRING_CREATOR;

    public void appendFrom(Parcel arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->appendFrom(Landroid/os/Parcel;II)V");
    }

    public IBinder[] createBinderArray() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->createBinderArray()[Landroid/os/IBinder;");
    }

    public ArrayList<IBinder> createBinderArrayList() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->createBinderArrayList()Ljava/util/ArrayList;");
    }

    public boolean[] createBooleanArray() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->createBooleanArray()[Z");
    }

    public byte[] createByteArray() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->createByteArray()[B");
    }

    public char[] createCharArray() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->createCharArray()[C");
    }

    public double[] createDoubleArray() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->createDoubleArray()[D");
    }

    public <T, S> T createFixedArray(Class<T> arg0, Parcelable.Creator<S> arg1, int[] arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->createFixedArray(Ljava/lang/Class;Landroid/os/Parcelable$Creator;[I)Ljava/lang/Object;");
    }

    public <T> T createFixedArray(Class<T> arg0, int[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->createFixedArray(Ljava/lang/Class;[I)Ljava/lang/Object;");
    }

    public <T, S> T createFixedArray(Class<T> arg0, Function<IBinder, S> arg1, int[] arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->createFixedArray(Ljava/lang/Class;Ljava/util/function/Function;[I)Ljava/lang/Object;");
    }

    public float[] createFloatArray() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->createFloatArray()[F");
    }

    public int[] createIntArray() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->createIntArray()[I");
    }

    public <T> T[] createInterfaceArray(IntFunction<T[]> arg0, Function<IBinder, T> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->createInterfaceArray(Ljava/util/function/IntFunction;Ljava/util/function/Function;)[Landroid/os/IInterface;");
    }

    public <T> ArrayList<T> createInterfaceArrayList(Function<IBinder, T> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->createInterfaceArrayList(Ljava/util/function/Function;)Ljava/util/ArrayList;");
    }

    public long[] createLongArray() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->createLongArray()[J");
    }

    public String[] createStringArray() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->createStringArray()[Ljava/lang/String;");
    }

    public ArrayList<String> createStringArrayList() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->createStringArrayList()Ljava/util/ArrayList;");
    }

    public <T> T[] createTypedArray(Parcelable.Creator<T> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->createTypedArray(Landroid/os/Parcelable$Creator;)[Ljava/lang/Object;");
    }

    public <T> ArrayList<T> createTypedArrayList(Parcelable.Creator<T> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->createTypedArrayList(Landroid/os/Parcelable$Creator;)Ljava/util/ArrayList;");
    }

    public <T> ArrayMap<String, T> createTypedArrayMap(Parcelable.Creator<T> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->createTypedArrayMap(Landroid/os/Parcelable$Creator;)Landroid/util/ArrayMap;");
    }

    public <T> SparseArray<T> createTypedSparseArray(Parcelable.Creator<T> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->createTypedSparseArray(Landroid/os/Parcelable$Creator;)Landroid/util/SparseArray;");
    }

    public int dataAvail() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->dataAvail()I");
    }

    public int dataCapacity() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->dataCapacity()I");
    }

    public int dataPosition() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->dataPosition()I");
    }

    public int dataSize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->dataSize()I");
    }

    public void enforceInterface(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->enforceInterface(Ljava/lang/String;)V");
    }

    public void enforceNoDataAvail() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->enforceNoDataAvail()V");
    }

    protected void finalize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->finalize()V");
    }

    public boolean hasFileDescriptors() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->hasFileDescriptors()Z");
    }

    public boolean hasFileDescriptors(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->hasFileDescriptors(II)Z");
    }

    public byte[] marshall() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->marshall()[B");
    }

    public static Parcel obtain() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->obtain()Landroid/os/Parcel;");
    }

    public static Parcel obtain(IBinder arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->obtain(Landroid/os/IBinder;)Landroid/os/Parcel;");
    }

    public Object[] readArray(ClassLoader arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readArray(Ljava/lang/ClassLoader;)[Ljava/lang/Object;");
    }

    public <T> T[] readArray(ClassLoader arg0, Class<T> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readArray(Ljava/lang/ClassLoader;Ljava/lang/Class;)[Ljava/lang/Object;");
    }

    public ArrayList readArrayList(ClassLoader arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readArrayList(Ljava/lang/ClassLoader;)Ljava/util/ArrayList;");
    }

    public <T> ArrayList<T> readArrayList(ClassLoader arg0, Class<? extends T> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readArrayList(Ljava/lang/ClassLoader;Ljava/lang/Class;)Ljava/util/ArrayList;");
    }

    public void readBinderArray(IBinder[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readBinderArray([Landroid/os/IBinder;)V");
    }

    public void readBinderList(List<IBinder> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readBinderList(Ljava/util/List;)V");
    }

    public byte[] readBlob() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readBlob()[B");
    }

    public boolean readBoolean() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readBoolean()Z");
    }

    public void readBooleanArray(boolean[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readBooleanArray([Z)V");
    }

    public Bundle readBundle() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readBundle()Landroid/os/Bundle;");
    }

    public Bundle readBundle(ClassLoader arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readBundle(Ljava/lang/ClassLoader;)Landroid/os/Bundle;");
    }

    public byte readByte() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readByte()B");
    }

    public void readByteArray(byte[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readByteArray([B)V");
    }

    public void readCharArray(char[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readCharArray([C)V");
    }

    public double readDouble() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readDouble()D");
    }

    public void readDoubleArray(double[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readDoubleArray([D)V");
    }

    public void readException() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readException()V");
    }

    public void readException(int arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readException(ILjava/lang/String;)V");
    }

    public ParcelFileDescriptor readFileDescriptor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readFileDescriptor()Landroid/os/ParcelFileDescriptor;");
    }

    public <T> void readFixedArray(T arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readFixedArray(Ljava/lang/Object;)V");
    }

    public <T, S> void readFixedArray(T arg0, Parcelable.Creator<S> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readFixedArray(Ljava/lang/Object;Landroid/os/Parcelable$Creator;)V");
    }

    public <T, S> void readFixedArray(T arg0, Function<IBinder, S> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readFixedArray(Ljava/lang/Object;Ljava/util/function/Function;)V");
    }

    public float readFloat() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readFloat()F");
    }

    public void readFloatArray(float[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readFloatArray([F)V");
    }

    public HashMap readHashMap(ClassLoader arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readHashMap(Ljava/lang/ClassLoader;)Ljava/util/HashMap;");
    }

    public <K, V> HashMap<K, V> readHashMap(ClassLoader arg0, Class<? extends K> arg1, Class<? extends V> arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readHashMap(Ljava/lang/ClassLoader;Ljava/lang/Class;Ljava/lang/Class;)Ljava/util/HashMap;");
    }

    public int readInt() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readInt()I");
    }

    public void readIntArray(int[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readIntArray([I)V");
    }

    public <T> void readInterfaceArray(T[] arg0, Function<IBinder, T> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readInterfaceArray([Landroid/os/IInterface;Ljava/util/function/Function;)V");
    }

    public <T> void readInterfaceList(List<T> arg0, Function<IBinder, T> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readInterfaceList(Ljava/util/List;Ljava/util/function/Function;)V");
    }

    public void readList(List arg0, ClassLoader arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readList(Ljava/util/List;Ljava/lang/ClassLoader;)V");
    }

    public <T> void readList(List<? super T> arg0, ClassLoader arg1, Class<T> arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readList(Ljava/util/List;Ljava/lang/ClassLoader;Ljava/lang/Class;)V");
    }

    public long readLong() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readLong()J");
    }

    public void readLongArray(long[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readLongArray([J)V");
    }

    public void readMap(Map arg0, ClassLoader arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readMap(Ljava/util/Map;Ljava/lang/ClassLoader;)V");
    }

    public <K, V> void readMap(Map<? super K, ? super V> arg0, ClassLoader arg1, Class<K> arg2, Class<V> arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readMap(Ljava/util/Map;Ljava/lang/ClassLoader;Ljava/lang/Class;Ljava/lang/Class;)V");
    }

    public <T> T readParcelable(ClassLoader arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readParcelable(Ljava/lang/ClassLoader;)Landroid/os/Parcelable;");
    }

    public <T> T readParcelable(ClassLoader arg0, Class<T> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readParcelable(Ljava/lang/ClassLoader;Ljava/lang/Class;)Ljava/lang/Object;");
    }

    public Parcelable[] readParcelableArray(ClassLoader arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readParcelableArray(Ljava/lang/ClassLoader;)[Landroid/os/Parcelable;");
    }

    public <T> T[] readParcelableArray(ClassLoader arg0, Class<T> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readParcelableArray(Ljava/lang/ClassLoader;Ljava/lang/Class;)[Ljava/lang/Object;");
    }

    public Parcelable.Creator<?> readParcelableCreator(ClassLoader arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readParcelableCreator(Ljava/lang/ClassLoader;)Landroid/os/Parcelable$Creator;");
    }

    public <T> Parcelable.Creator<T> readParcelableCreator(ClassLoader arg0, Class<T> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readParcelableCreator(Ljava/lang/ClassLoader;Ljava/lang/Class;)Landroid/os/Parcelable$Creator;");
    }

    public <T> List<T> readParcelableList(List<T> arg0, ClassLoader arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readParcelableList(Ljava/util/List;Ljava/lang/ClassLoader;)Ljava/util/List;");
    }

    public <T> List<T> readParcelableList(List<T> arg0, ClassLoader arg1, Class<? extends T> arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readParcelableList(Ljava/util/List;Ljava/lang/ClassLoader;Ljava/lang/Class;)Ljava/util/List;");
    }

    public PersistableBundle readPersistableBundle() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readPersistableBundle()Landroid/os/PersistableBundle;");
    }

    public PersistableBundle readPersistableBundle(ClassLoader arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readPersistableBundle(Ljava/lang/ClassLoader;)Landroid/os/PersistableBundle;");
    }

    public Serializable readSerializable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readSerializable()Ljava/io/Serializable;");
    }

    public <T> T readSerializable(ClassLoader arg0, Class<T> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readSerializable(Ljava/lang/ClassLoader;Ljava/lang/Class;)Ljava/lang/Object;");
    }

    public Size readSize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readSize()Landroid/util/Size;");
    }

    public SizeF readSizeF() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readSizeF()Landroid/util/SizeF;");
    }

    public <T> SparseArray<T> readSparseArray(ClassLoader arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readSparseArray(Ljava/lang/ClassLoader;)Landroid/util/SparseArray;");
    }

    public <T> SparseArray<T> readSparseArray(ClassLoader arg0, Class<? extends T> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readSparseArray(Ljava/lang/ClassLoader;Ljava/lang/Class;)Landroid/util/SparseArray;");
    }

    public SparseBooleanArray readSparseBooleanArray() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readSparseBooleanArray()Landroid/util/SparseBooleanArray;");
    }

    public String readString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readString()Ljava/lang/String;");
    }

    public void readStringArray(String[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readStringArray([Ljava/lang/String;)V");
    }

    public void readStringList(List<String> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readStringList(Ljava/util/List;)V");
    }

    public IBinder readStrongBinder() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readStrongBinder()Landroid/os/IBinder;");
    }

    public <T> void readTypedArray(T[] arg0, Parcelable.Creator<T> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readTypedArray([Ljava/lang/Object;Landroid/os/Parcelable$Creator;)V");
    }

    public <T> void readTypedList(List<T> arg0, Parcelable.Creator<T> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readTypedList(Ljava/util/List;Landroid/os/Parcelable$Creator;)V");
    }

    public <T> T readTypedObject(Parcelable.Creator<T> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readTypedObject(Landroid/os/Parcelable$Creator;)Ljava/lang/Object;");
    }

    public Object readValue(ClassLoader arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->readValue(Ljava/lang/ClassLoader;)Ljava/lang/Object;");
    }

    public void recycle() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->recycle()V");
    }

    public void setDataCapacity(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->setDataCapacity(I)V");
    }

    public void setDataPosition(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->setDataPosition(I)V");
    }

    public void setDataSize(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->setDataSize(I)V");
    }

    public void setPropagateAllowBlocking() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->setPropagateAllowBlocking()V");
    }

    public void unmarshall(byte[] arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->unmarshall([BII)V");
    }

    public void writeArray(Object[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeArray([Ljava/lang/Object;)V");
    }

    public void writeBinderArray(IBinder[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeBinderArray([Landroid/os/IBinder;)V");
    }

    public void writeBinderList(List<IBinder> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeBinderList(Ljava/util/List;)V");
    }

    public void writeBlob(byte[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeBlob([B)V");
    }

    public void writeBlob(byte[] arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeBlob([BII)V");
    }

    public void writeBoolean(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeBoolean(Z)V");
    }

    public void writeBooleanArray(boolean[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeBooleanArray([Z)V");
    }

    public void writeBundle(Bundle arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeBundle(Landroid/os/Bundle;)V");
    }

    public void writeByte(byte arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeByte(B)V");
    }

    public void writeByteArray(byte[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeByteArray([B)V");
    }

    public void writeByteArray(byte[] arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeByteArray([BII)V");
    }

    public void writeCharArray(char[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeCharArray([C)V");
    }

    public void writeDouble(double arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeDouble(D)V");
    }

    public void writeDoubleArray(double[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeDoubleArray([D)V");
    }

    public void writeException(Exception arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeException(Ljava/lang/Exception;)V");
    }

    public void writeFileDescriptor(FileDescriptor arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeFileDescriptor(Ljava/io/FileDescriptor;)V");
    }

    public <T> void writeFixedArray(T arg0, int arg1, int[] arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeFixedArray(Ljava/lang/Object;I[I)V");
    }

    public void writeFloat(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeFloat(F)V");
    }

    public void writeFloatArray(float[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeFloatArray([F)V");
    }

    public void writeInt(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeInt(I)V");
    }

    public void writeIntArray(int[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeIntArray([I)V");
    }

    public <T> void writeInterfaceArray(T[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeInterfaceArray([Landroid/os/IInterface;)V");
    }

    public <T> void writeInterfaceList(List<T> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeInterfaceList(Ljava/util/List;)V");
    }

    public void writeInterfaceToken(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeInterfaceToken(Ljava/lang/String;)V");
    }

    public void writeList(List arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeList(Ljava/util/List;)V");
    }

    public void writeLong(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeLong(J)V");
    }

    public void writeLongArray(long[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeLongArray([J)V");
    }

    public void writeMap(Map arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeMap(Ljava/util/Map;)V");
    }

    public void writeNoException() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeNoException()V");
    }

    public void writeParcelable(Parcelable arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeParcelable(Landroid/os/Parcelable;I)V");
    }

    public <T> void writeParcelableArray(T[] arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeParcelableArray([Landroid/os/Parcelable;I)V");
    }

    public void writeParcelableCreator(Parcelable arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeParcelableCreator(Landroid/os/Parcelable;)V");
    }

    public <T> void writeParcelableList(List<T> arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeParcelableList(Ljava/util/List;I)V");
    }

    public void writePersistableBundle(PersistableBundle arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writePersistableBundle(Landroid/os/PersistableBundle;)V");
    }

    public void writeSerializable(Serializable arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeSerializable(Ljava/io/Serializable;)V");
    }

    public void writeSize(Size arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeSize(Landroid/util/Size;)V");
    }

    public void writeSizeF(SizeF arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeSizeF(Landroid/util/SizeF;)V");
    }

    public <T> void writeSparseArray(SparseArray<T> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeSparseArray(Landroid/util/SparseArray;)V");
    }

    public void writeSparseBooleanArray(SparseBooleanArray arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeSparseBooleanArray(Landroid/util/SparseBooleanArray;)V");
    }

    public void writeString(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeString(Ljava/lang/String;)V");
    }

    public void writeStringArray(String[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeStringArray([Ljava/lang/String;)V");
    }

    public void writeStringList(List<String> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeStringList(Ljava/util/List;)V");
    }

    public void writeStrongBinder(IBinder arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeStrongBinder(Landroid/os/IBinder;)V");
    }

    public void writeStrongInterface(IInterface arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeStrongInterface(Landroid/os/IInterface;)V");
    }

    public <T> void writeTypedArray(T[] arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeTypedArray([Landroid/os/Parcelable;I)V");
    }

    public <T> void writeTypedArrayMap(ArrayMap<String, T> arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeTypedArrayMap(Landroid/util/ArrayMap;I)V");
    }

    public <T> void writeTypedList(List<T> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeTypedList(Ljava/util/List;)V");
    }

    public <T> void writeTypedList(List<T> arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeTypedList(Ljava/util/List;I)V");
    }

    public <T> void writeTypedObject(T arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeTypedObject(Landroid/os/Parcelable;I)V");
    }

    public <T> void writeTypedSparseArray(SparseArray<T> arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeTypedSparseArray(Landroid/util/SparseArray;I)V");
    }

    public void writeValue(Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Parcel;->writeValue(Ljava/lang/Object;)V");
    }
    }
