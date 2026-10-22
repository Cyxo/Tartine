package android.os;

import java.util.HashMap;
import java.util.Set;

/**
 * Desktop mapping (Tartine). Ported per-method from
 * aosp-frameworks-base/core/java/android/os/BaseBundle.java (AOSP):
 * <ul>
 *   <li>{@code mMap} (AOSP line 105, {@code ArrayMap<String, Object>}) — a
 *       host {@link HashMap} stand-in until android.util.ArrayMap ports;
 *       put/get/remove semantics match.</li>
 *   <li>AOSP's parcel-backed lazy table is not ported: a desktop Bundle is
 *       always materialized, so {@code unparcel()} is a no-op.</li>
 *   <li>{@code mClassLoader} (AOSP line 140/157): a null construction loader
 *       resolves to the bundle's own classloader, so it is never null.</li>
 *   <li>Typed getters mirror AOSP's {@code (Boolean) o} casts: a null value
 *       yields the documented default and a wrong-typed value logs through
 *       AOSP {@code typeWarning()} then returns the default — mirrored
 *       without a logging backend.</li>
 * </ul>
 * Methods still throwing below are unreferenced gaps, keeping the
 * gap-driven loop.
 */
public class BaseBundle {

    /** AOSP BaseBundle line 105. */
    HashMap<String, Object> mMap = new HashMap<>();

    /** AOSP BaseBundle line 140. */
    private ClassLoader mClassLoader;

    /** AOSP BaseBundle(ClassLoader, capacity): null loader -> own loader. */
    BaseBundle(ClassLoader loader, int capacity) {
        mClassLoader = loader == null ? getClass().getClassLoader() : loader;
        mMap = new HashMap<>();
    }

    BaseBundle() {
        this((ClassLoader) null, 0);
    }

    BaseBundle(BaseBundle from) {
        mClassLoader = from.mClassLoader;
        mMap = new HashMap<>(from.mMap);
    }

    public void clear() {
        mMap.clear();
    }

    public boolean containsKey(java.lang.String arg0) {
        return mMap.containsKey(arg0);
    }

    public java.lang.Object get(java.lang.String arg0) {
        return mMap.get(arg0);
    }

    public boolean getBoolean(java.lang.String arg0) {
        return getBoolean(arg0, false);
    }

    /** AOSP getBoolean(key, defaultValue): null -> default; cast-miss -> default. */
    public boolean getBoolean(java.lang.String arg0, boolean arg1) {
        Object o = mMap.get(arg0);
        if (o == null) {
            return arg1;
        }
        try {
            return (Boolean) o;
        } catch (ClassCastException e) {
            return arg1;
        }
    }

    public boolean[] getBooleanArray(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/BaseBundle;->getBooleanArray(Ljava/lang/String;)[Z");
    }

    public double getDouble(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/BaseBundle;->getDouble(Ljava/lang/String;)D");
    }

    public double getDouble(java.lang.String arg0, double arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/BaseBundle;->getDouble(Ljava/lang/String;D)D");
    }

    public double[] getDoubleArray(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/BaseBundle;->getDoubleArray(Ljava/lang/String;)[D");
    }

    /** AOSP getInt(key): getInt(key, 0). */
    public int getInt(java.lang.String arg0) {
        return getInt(arg0, 0);
    }

    /** AOSP getInt(key, defaultValue). */
    public int getInt(java.lang.String arg0, int arg1) {
        Object o = mMap.get(arg0);
        if (o == null) {
            return arg1;
        }
        try {
            return (Integer) o;
        } catch (ClassCastException e) {
            return arg1;
        }
    }

    public int[] getIntArray(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/BaseBundle;->getIntArray(Ljava/lang/String;)[I");
    }

    public long getLong(java.lang.String arg0) {
        return getLong(arg0, 0L);
    }

    public long getLong(java.lang.String arg0, long arg1) {
        Object o = mMap.get(arg0);
        if (o == null) {
            return arg1;
        }
        try {
            return (Long) o;
        } catch (ClassCastException e) {
            return arg1;
        }
    }

    public long[] getLongArray(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/BaseBundle;->getLongArray(Ljava/lang/String;)[J");
    }

    public java.lang.String getString(java.lang.String arg0) {
        return getString(arg0, null);
    }

    public java.lang.String getString(java.lang.String arg0, java.lang.String arg1) {
        Object o = mMap.get(arg0);
        if (o == null) {
            return arg1;
        }
        try {
            return (String) o;
        } catch (ClassCastException e) {
            return arg1;
        }
    }

    public java.lang.String[] getStringArray(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/BaseBundle;->getStringArray(Ljava/lang/String;)[Ljava/lang/String;");
    }

    public boolean isEmpty() {
        return mMap.isEmpty();
    }

    public Set<java.lang.String> keySet() {
        return mMap.keySet();
    }

    /** AOSP BaseBundle(ClassLoader, capacity): host loader pairing. */
    public ClassLoader getClassLoader() {
        return mClassLoader;
    }

    public void putAll(android.os.PersistableBundle arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/BaseBundle;->putAll(Landroid/os/PersistableBundle;)V");
    }

    public void putBoolean(java.lang.String arg0, boolean arg1) {
        mMap.put(arg0, arg1);
    }

    public void putBooleanArray(java.lang.String arg0, boolean[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/BaseBundle;->putBooleanArray(Ljava/lang/String;[Z)V");
    }

    public void putDouble(java.lang.String arg0, double arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/BaseBundle;->putDouble(Ljava/lang/String;D)V");
    }

    public void putDoubleArray(java.lang.String arg0, double[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/BaseBundle;->putDoubleArray(Ljava/lang/String;[D)V");
    }

    public void putInt(java.lang.String arg0, int arg1) {
        mMap.put(arg0, arg1);
    }

    public void putIntArray(java.lang.String arg0, int[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/BaseBundle;->putIntArray(Ljava/lang/String;[I)V");
    }

    public void putLong(java.lang.String arg0, long arg1) {
        mMap.put(arg0, arg1);
    }

    public void putLongArray(java.lang.String arg0, long[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/BaseBundle;->putLongArray(Ljava/lang/String;[J)V");
    }

    public void putString(java.lang.String arg0, java.lang.String arg1) {
        mMap.put(arg0, arg1);
    }

    public void putStringArray(java.lang.String arg0, java.lang.String[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/BaseBundle;->putStringArray(Ljava/lang/String;[Ljava/lang/String;)V");
    }

    public void remove(java.lang.String arg0) {
        mMap.remove(arg0);
    }

    public int size() {
        return mMap.size();
    }
}
