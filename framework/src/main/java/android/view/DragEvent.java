package android.view;

    public class DragEvent implements android.os.Parcelable {

    public static final int ACTION_DRAG_ENDED = 4;
    public static final int ACTION_DRAG_ENTERED = 5;
    public static final int ACTION_DRAG_EXITED = 6;
    public static final int ACTION_DRAG_LOCATION = 2;
    public static final int ACTION_DRAG_STARTED = 1;
    public static final int ACTION_DROP = 3;
    public static android.os.Parcelable.Creator CREATOR;

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/DragEvent;->describeContents()I");
    }

    public int getAction() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/DragEvent;->getAction()I");
    }

    public android.content.ClipData getClipData() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/DragEvent;->getClipData()Landroid/content/ClipData;");
    }

    public android.content.ClipDescription getClipDescription() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/DragEvent;->getClipDescription()Landroid/content/ClipDescription;");
    }

    public java.lang.Object getLocalState() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/DragEvent;->getLocalState()Ljava/lang/Object;");
    }

    public boolean getResult() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/DragEvent;->getResult()Z");
    }

    public float getX() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/DragEvent;->getX()F");
    }

    public float getY() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/DragEvent;->getY()F");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/DragEvent;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/DragEvent;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
