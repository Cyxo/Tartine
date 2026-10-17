package android.os;

    public class Message implements android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;
    public int arg1;
    public int arg2;
    public java.lang.Object obj;
    public android.os.Messenger replyTo;
    public int sendingUid;
    public int what;

    public Message() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;-><init>()V");
    }

    public void copyFrom(android.os.Message arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->copyFrom(Landroid/os/Message;)V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->describeContents()I");
    }

    public java.lang.Runnable getCallback() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->getCallback()Ljava/lang/Runnable;");
    }

    public android.os.Bundle getData() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->getData()Landroid/os/Bundle;");
    }

    public android.os.Handler getTarget() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->getTarget()Landroid/os/Handler;");
    }

    public long getWhen() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->getWhen()J");
    }

    public boolean isAsynchronous() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->isAsynchronous()Z");
    }

    public static android.os.Message obtain() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->obtain()Landroid/os/Message;");
    }

    public static android.os.Message obtain(android.os.Handler arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->obtain(Landroid/os/Handler;)Landroid/os/Message;");
    }

    public static android.os.Message obtain(android.os.Handler arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->obtain(Landroid/os/Handler;I)Landroid/os/Message;");
    }

    public static android.os.Message obtain(android.os.Handler arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->obtain(Landroid/os/Handler;III)Landroid/os/Message;");
    }

    public static android.os.Message obtain(android.os.Handler arg0, int arg1, int arg2, int arg3, java.lang.Object arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->obtain(Landroid/os/Handler;IIILjava/lang/Object;)Landroid/os/Message;");
    }

    public static android.os.Message obtain(android.os.Handler arg0, int arg1, java.lang.Object arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->obtain(Landroid/os/Handler;ILjava/lang/Object;)Landroid/os/Message;");
    }

    public static android.os.Message obtain(android.os.Handler arg0, java.lang.Runnable arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->obtain(Landroid/os/Handler;Ljava/lang/Runnable;)Landroid/os/Message;");
    }

    public static android.os.Message obtain(android.os.Message arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->obtain(Landroid/os/Message;)Landroid/os/Message;");
    }

    public android.os.Bundle peekData() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->peekData()Landroid/os/Bundle;");
    }

    public void recycle() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->recycle()V");
    }

    public void sendToTarget() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->sendToTarget()V");
    }

    public void setAsynchronous(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->setAsynchronous(Z)V");
    }

    public void setData(android.os.Bundle arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->setData(Landroid/os/Bundle;)V");
    }

    public void setTarget(android.os.Handler arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->setTarget(Landroid/os/Handler;)V");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
