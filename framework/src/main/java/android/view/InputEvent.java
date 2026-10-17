package android.view;

    public abstract class InputEvent implements android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputEvent;->describeContents()I");
    }

    public final android.view.InputDevice getDevice() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputEvent;->getDevice()Landroid/view/InputDevice;");
    }

    public abstract int getDeviceId();

    public abstract long getEventTime();

    public abstract int getSource();

    public boolean isFromSource(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputEvent;->isFromSource(I)Z");
    }
    }
