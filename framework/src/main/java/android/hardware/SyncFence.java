package android.hardware;

    public class SyncFence implements java.lang.AutoCloseable, android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;
    public static final long SIGNAL_TIME_INVALID = -1L;
    public static final long SIGNAL_TIME_PENDING = 9223372036854775807L;

    public SyncFence(android.hardware.SyncFence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SyncFence;-><init>(Landroid/hardware/SyncFence;)V");
    }

    public boolean await(java.time.Duration arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SyncFence;->await(Ljava/time/Duration;)Z");
    }

    public boolean awaitForever() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SyncFence;->awaitForever()Z");
    }

    public void close() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SyncFence;->close()V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SyncFence;->describeContents()I");
    }

    public long getSignalTime() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SyncFence;->getSignalTime()J");
    }

    public boolean isValid() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SyncFence;->isValid()Z");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SyncFence;->writeToParcel(Landroid/os/Parcel;I)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public SyncFence() {
        }
    }
