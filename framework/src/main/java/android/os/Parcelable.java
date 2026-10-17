package android.os;

    public interface Parcelable {

    public static final int CONTENTS_FILE_DESCRIPTOR = 1;
    public static final int PARCELABLE_WRITE_RETURN_VALUE = 1;

    public abstract int describeContents();

    public abstract void writeToParcel(android.os.Parcel arg0, int arg1);
        public static interface Creator<T> {

        public abstract T createFromParcel(android.os.Parcel arg0);

        public abstract T[] newArray(int arg0);
        }
    }
