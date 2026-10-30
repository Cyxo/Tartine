package android.view;

import android.os.Parcel;
import android.os.Parcelable;

    public class InputEvent implements Parcelable {

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputEvent;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    public static Parcelable.Creator CREATOR;

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputEvent;->describeContents()I");
    }

    public final InputDevice getDevice() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputEvent;->getDevice()Landroid/view/InputDevice;");
    }

    public int getDeviceId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputEvent;->getDeviceId()I");
    }

    public long getEventTime() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputEvent;->getEventTime()J");
    }

    public int getSource() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputEvent;->getSource()I");
    }

    public boolean isFromSource(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputEvent;->isFromSource(I)Z");
    }
    }
