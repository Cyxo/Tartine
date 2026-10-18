package android.view;

    public class WindowId implements android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowId;->describeContents()I");
    }

    public boolean equals(java.lang.Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowId;->equals(Ljava/lang/Object;)Z");
    }

    public int hashCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowId;->hashCode()I");
    }

    public boolean isFocused() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowId;->isFocused()Z");
    }

    public void registerFocusObserver(android.view.WindowId.FocusObserver arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowId;->registerFocusObserver(Landroid/view/WindowId$FocusObserver;)V");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowId;->toString()Ljava/lang/String;");
    }

    public void unregisterFocusObserver(android.view.WindowId.FocusObserver arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowId;->unregisterFocusObserver(Landroid/view/WindowId$FocusObserver;)V");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowId;->writeToParcel(Landroid/os/Parcel;I)V");
    }
        public static class FocusObserver {

        public FocusObserver() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowId$FocusObserver;-><init>()V");
        }

        public void onFocusGained(android.view.WindowId arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowId$FocusObserver;->onFocusGained(Landroid/view/WindowId;)V");
        }

        public void onFocusLost(android.view.WindowId arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowId$FocusObserver;->onFocusLost(Landroid/view/WindowId;)V");
        }
        }
    }
