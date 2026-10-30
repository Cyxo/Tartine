package android.content.res;

import android.os.Bundle;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;

    public class AssetFileDescriptor implements Closeable, Parcelable {

    public static Parcelable.Creator CREATOR;
    public static final long UNKNOWN_LENGTH = -1L;

    public AssetFileDescriptor(ParcelFileDescriptor arg0, long arg1, long arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetFileDescriptor;-><init>(Landroid/os/ParcelFileDescriptor;JJ)V");
    }

    public AssetFileDescriptor(ParcelFileDescriptor arg0, long arg1, long arg2, Bundle arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetFileDescriptor;-><init>(Landroid/os/ParcelFileDescriptor;JJLandroid/os/Bundle;)V");
    }

    public void close() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetFileDescriptor;->close()V");
    }

    public FileInputStream createInputStream() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetFileDescriptor;->createInputStream()Ljava/io/FileInputStream;");
    }

    public FileOutputStream createOutputStream() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetFileDescriptor;->createOutputStream()Ljava/io/FileOutputStream;");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetFileDescriptor;->describeContents()I");
    }

    public long getDeclaredLength() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetFileDescriptor;->getDeclaredLength()J");
    }

    public Bundle getExtras() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetFileDescriptor;->getExtras()Landroid/os/Bundle;");
    }

    public FileDescriptor getFileDescriptor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetFileDescriptor;->getFileDescriptor()Ljava/io/FileDescriptor;");
    }

    public long getLength() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetFileDescriptor;->getLength()J");
    }

    public ParcelFileDescriptor getParcelFileDescriptor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetFileDescriptor;->getParcelFileDescriptor()Landroid/os/ParcelFileDescriptor;");
    }

    public long getStartOffset() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetFileDescriptor;->getStartOffset()J");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetFileDescriptor;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetFileDescriptor;->writeToParcel(Landroid/os/Parcel;I)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public AssetFileDescriptor() {
        }
    }
