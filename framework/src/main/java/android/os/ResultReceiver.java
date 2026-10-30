package android.os;

    public class ResultReceiver implements Parcelable {

    public static Parcelable.Creator CREATOR;

    public ResultReceiver(Handler arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ResultReceiver;-><init>(Landroid/os/Handler;)V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ResultReceiver;->describeContents()I");
    }

    protected void onReceiveResult(int arg0, Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ResultReceiver;->onReceiveResult(ILandroid/os/Bundle;)V");
    }

    public void send(int arg0, Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ResultReceiver;->send(ILandroid/os/Bundle;)V");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/ResultReceiver;->writeToParcel(Landroid/os/Parcel;I)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public ResultReceiver() {
        }
    }
