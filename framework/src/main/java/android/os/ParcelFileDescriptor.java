package android.os;

    public class ParcelFileDescriptor implements java.io.Closeable, android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;
    public static final int MODE_APPEND = 33554432;
    public static final int MODE_CREATE = 134217728;
    public static final int MODE_READ_ONLY = 268435456;
    public static final int MODE_READ_WRITE = 805306368;
    public static final int MODE_TRUNCATE = 67108864;
    public static final int MODE_WORLD_READABLE = 1;
    public static final int MODE_WORLD_WRITEABLE = 2;
    public static final int MODE_WRITE_ONLY = 536870912;

    public ParcelFileDescriptor(android.os.ParcelFileDescriptor arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ParcelFileDescriptor;-><init>(Landroid/os/ParcelFileDescriptor;)V");
    }

    public static android.os.ParcelFileDescriptor adoptFd(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ParcelFileDescriptor;->adoptFd(I)Landroid/os/ParcelFileDescriptor;");
    }

    public boolean canDetectErrors() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ParcelFileDescriptor;->canDetectErrors()Z");
    }

    public void checkError() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ParcelFileDescriptor;->checkError()V");
    }

    public void close() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ParcelFileDescriptor;->close()V");
    }

    public void closeWithError(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ParcelFileDescriptor;->closeWithError(Ljava/lang/String;)V");
    }

    public static android.os.ParcelFileDescriptor[] createPipe() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ParcelFileDescriptor;->createPipe()[Landroid/os/ParcelFileDescriptor;");
    }

    public static android.os.ParcelFileDescriptor[] createReliablePipe() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ParcelFileDescriptor;->createReliablePipe()[Landroid/os/ParcelFileDescriptor;");
    }

    public static android.os.ParcelFileDescriptor[] createReliableSocketPair() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ParcelFileDescriptor;->createReliableSocketPair()[Landroid/os/ParcelFileDescriptor;");
    }

    public static android.os.ParcelFileDescriptor[] createSocketPair() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ParcelFileDescriptor;->createSocketPair()[Landroid/os/ParcelFileDescriptor;");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ParcelFileDescriptor;->describeContents()I");
    }

    public int detachFd() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ParcelFileDescriptor;->detachFd()I");
    }

    public android.os.ParcelFileDescriptor dup() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ParcelFileDescriptor;->dup()Landroid/os/ParcelFileDescriptor;");
    }

    public static android.os.ParcelFileDescriptor dup(java.io.FileDescriptor arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ParcelFileDescriptor;->dup(Ljava/io/FileDescriptor;)Landroid/os/ParcelFileDescriptor;");
    }

    protected void finalize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ParcelFileDescriptor;->finalize()V");
    }

    public static android.os.ParcelFileDescriptor fromDatagramSocket(java.net.DatagramSocket arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ParcelFileDescriptor;->fromDatagramSocket(Ljava/net/DatagramSocket;)Landroid/os/ParcelFileDescriptor;");
    }

    public static android.os.ParcelFileDescriptor fromFd(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ParcelFileDescriptor;->fromFd(I)Landroid/os/ParcelFileDescriptor;");
    }

    public static android.os.ParcelFileDescriptor fromSocket(java.net.Socket arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ParcelFileDescriptor;->fromSocket(Ljava/net/Socket;)Landroid/os/ParcelFileDescriptor;");
    }

    public int getFd() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ParcelFileDescriptor;->getFd()I");
    }

    public java.io.FileDescriptor getFileDescriptor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ParcelFileDescriptor;->getFileDescriptor()Ljava/io/FileDescriptor;");
    }

    public long getStatSize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ParcelFileDescriptor;->getStatSize()J");
    }

    public static android.os.ParcelFileDescriptor open(java.io.File arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ParcelFileDescriptor;->open(Ljava/io/File;I)Landroid/os/ParcelFileDescriptor;");
    }

    public static android.os.ParcelFileDescriptor open(java.io.File arg0, int arg1, android.os.Handler arg2, android.os.ParcelFileDescriptor.OnCloseListener arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ParcelFileDescriptor;->open(Ljava/io/File;ILandroid/os/Handler;Landroid/os/ParcelFileDescriptor$OnCloseListener;)Landroid/os/ParcelFileDescriptor;");
    }

    public static int parseMode(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ParcelFileDescriptor;->parseMode(Ljava/lang/String;)I");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ParcelFileDescriptor;->toString()Ljava/lang/String;");
    }

    public static android.os.ParcelFileDescriptor wrap(android.os.ParcelFileDescriptor arg0, android.os.Handler arg1, android.os.ParcelFileDescriptor.OnCloseListener arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ParcelFileDescriptor;->wrap(Landroid/os/ParcelFileDescriptor;Landroid/os/Handler;Landroid/os/ParcelFileDescriptor$OnCloseListener;)Landroid/os/ParcelFileDescriptor;");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ParcelFileDescriptor;->writeToParcel(Landroid/os/Parcel;I)V");
    }
        public static interface OnCloseListener {

        public abstract void onClose(java.io.IOException arg0);
        }
    }
