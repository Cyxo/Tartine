package android.view;

import android.os.Parcel;
import android.os.Parcelable;

    public class WindowId implements Parcelable {

    public static Parcelable.Creator CREATOR;

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowId;->describeContents()I");
    }

    public boolean equals(Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowId;->equals(Ljava/lang/Object;)Z");
    }

    public int hashCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowId;->hashCode()I");
    }

    public boolean isFocused() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowId;->isFocused()Z");
    }

    public void registerFocusObserver(WindowId.FocusObserver arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowId;->registerFocusObserver(Landroid/view/WindowId$FocusObserver;)V");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowId;->toString()Ljava/lang/String;");
    }

    public void unregisterFocusObserver(WindowId.FocusObserver arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowId;->unregisterFocusObserver(Landroid/view/WindowId$FocusObserver;)V");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowId;->writeToParcel(Landroid/os/Parcel;I)V");
    }
        public static class FocusObserver {

        public FocusObserver() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowId$FocusObserver;-><init>()V");
        }

        public void onFocusGained(WindowId arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowId$FocusObserver;->onFocusGained(Landroid/view/WindowId;)V");
        }

        public void onFocusLost(WindowId arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowId$FocusObserver;->onFocusLost(Landroid/view/WindowId;)V");
        }
        }
    }
