package android.content.res;

    public class AssetFileDescriptor implements java.io.Closeable, android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;
    public static final long UNKNOWN_LENGTH = -1L;

    public AssetFileDescriptor(android.os.ParcelFileDescriptor arg0, long arg1, long arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetFileDescriptor;-><init>(Landroid/os/ParcelFileDescriptor;JJ)V");
    }

    public AssetFileDescriptor(android.os.ParcelFileDescriptor arg0, long arg1, long arg2, android.os.Bundle arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetFileDescriptor;-><init>(Landroid/os/ParcelFileDescriptor;JJLandroid/os/Bundle;)V");
    }

    public void close() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetFileDescriptor;->close()V");
    }

    public java.io.FileInputStream createInputStream() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetFileDescriptor;->createInputStream()Ljava/io/FileInputStream;");
    }

    public java.io.FileOutputStream createOutputStream() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetFileDescriptor;->createOutputStream()Ljava/io/FileOutputStream;");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetFileDescriptor;->describeContents()I");
    }

    public long getDeclaredLength() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetFileDescriptor;->getDeclaredLength()J");
    }

    public android.os.Bundle getExtras() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetFileDescriptor;->getExtras()Landroid/os/Bundle;");
    }

    public java.io.FileDescriptor getFileDescriptor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetFileDescriptor;->getFileDescriptor()Ljava/io/FileDescriptor;");
    }

    public long getLength() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetFileDescriptor;->getLength()J");
    }

    public android.os.ParcelFileDescriptor getParcelFileDescriptor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetFileDescriptor;->getParcelFileDescriptor()Landroid/os/ParcelFileDescriptor;");
    }

    public long getStartOffset() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetFileDescriptor;->getStartOffset()J");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetFileDescriptor;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetFileDescriptor;->writeToParcel(Landroid/os/Parcel;I)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public AssetFileDescriptor() {
        }
    }
