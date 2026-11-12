package android.content;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Tartine: the component identifier, ported from AOSP
 * {@code android.content.ComponentName}. It is a pure value type (package +
 * class), so the shell ports it completely: androidx, AppCompat and package
 * queries construct and compare these freely.
 */
public class ComponentName implements Cloneable, Comparable<ComponentName>, Parcelable {

    /** The package part of the component. */
    private final String mPackage;

    /** The fully-qualified class part of the component. */
    private final String mClass;

    /**
     * AOSP ComponentName(String, String): a class name that starts with '.'
     * is relative to the package.
     */
    public ComponentName(String pkg, String cls) {
        if (pkg == null) {
            throw new NullPointerException("package name is null");
        }
        if (cls == null) {
            throw new NullPointerException("class name is null");
        }
        mPackage = pkg;
        mClass = cls.startsWith(".") ? pkg + cls : cls;
    }

    /** AOSP ComponentName(Context, String). */
    public ComponentName(Context pkg, String cls) {
        this(pkg.getPackageName(), cls);
    }

    /** AOSP ComponentName(Context, Class). */
    public ComponentName(Context pkg, Class<?> cls) {
        this(pkg.getPackageName(), cls.getName());
    }

    /** AOSP ComponentName(Parcel). */
    public ComponentName(Parcel in) {
        mPackage = in.readString();
        mClass = in.readString();
    }

    /** AOSP ComponentName.createRelative(Context, String). */
    public static ComponentName createRelative(Context pkg, String cls) {
        return new ComponentName(pkg.getPackageName(), cls);
    }

    /** AOSP ComponentName.createRelative(String, String). */
    public static ComponentName createRelative(String pkg, String cls) {
        return new ComponentName(pkg, cls);
    }

    public String getPackageName() {
        return mPackage;
    }

    public String getClassName() {
        return mClass;
    }

    /**
     * AOSP ComponentName.getShortClassName: strips the package prefix when the
     * class lives in the component's own package.
     */
    public String getShortClassName() {
        String prefix = mPackage + ".";
        return mClass.startsWith(prefix) ? "." + mClass.substring(prefix.length()) : mClass;
    }

    /** AOSP ComponentName.flattenToString: {@code package/class}. */
    public String flattenToString() {
        return mPackage + "/" + mClass;
    }

    /** AOSP ComponentName.flattenToShortString: {@code package/.Class}. */
    public String flattenToShortString() {
        return mPackage + "/" + getShortClassName();
    }

    /** AOSP ComponentName.unflattenFromString: parses {@code package/class}. */
    public static ComponentName unflattenFromString(String str) {
        if (str == null) {
            return null;
        }
        int sep = str.indexOf('/');
        if (sep < 0 || sep + 1 >= str.length()) {
            return null;
        }
        String pkg = str.substring(0, sep);
        String cls = str.substring(sep + 1);
        return new ComponentName(pkg, cls);
    }

    /** AOSP ComponentName.toShortString. */
    public String toShortString() {
        return "ComponentInfo{" + mPackage + "/" + mClass + "}";
    }

    @Override
    public String toString() {
        return "ComponentInfo{" + mPackage + "/" + mClass + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ComponentName)) {
            return false;
        }
        ComponentName other = (ComponentName) obj;
        return mPackage.equals(other.mPackage) && mClass.equals(other.mClass);
    }

    @Override
    public int hashCode() {
        return mPackage.hashCode() + mClass.hashCode();
    }

    @Override
    public ComponentName clone() {
        return new ComponentName(mPackage, mClass);
    }

    public int compareTo(ComponentName that) {
        int v = mPackage.compareTo(that.mPackage);
        return v != 0 ? v : mClass.compareTo(that.mClass);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mPackage);
        out.writeString(mClass);
    }

    /** AOSP ComponentName.writeToParcel(ComponentName, Parcel). */
    public static void writeToParcel(ComponentName c, Parcel out) {
        if (c != null) {
            c.writeToParcel(out, 0);
        } else {
            out.writeString(null);
        }
    }

    /** AOSP ComponentName.readFromParcel(Parcel). */
    public static ComponentName readFromParcel(Parcel in) {
        String pkg = in.readString();
        if (pkg == null) {
            return null;
        }
        String cls = in.readString();
        return new ComponentName(pkg, cls);
    }

    public static final Parcelable.Creator<ComponentName> CREATOR =
            new Parcelable.Creator<ComponentName>() {
                @Override
                public ComponentName createFromParcel(Parcel in) {
                    return new ComponentName(in);
                }

                @Override
                public ComponentName[] newArray(int size) {
                    return new ComponentName[size];
                }
            };

    /** Scratch mirror constructor (SkeletonGen). */
    public ComponentName() {
        mPackage = null;
        mClass = null;
    }
}