package android.os;

    public class Message implements Parcelable {

    public static Parcelable.Creator CREATOR;
    public int arg1;
    public int arg2;
    public Object obj;
    public Messenger replyTo;
    public int sendingUid;
    public int what;

    public Message() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;-><init>()V");
    }

    public void copyFrom(Message arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->copyFrom(Landroid/os/Message;)V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->describeContents()I");
    }

    public Runnable getCallback() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->getCallback()Ljava/lang/Runnable;");
    }

    public Bundle getData() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->getData()Landroid/os/Bundle;");
    }

    public Handler getTarget() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->getTarget()Landroid/os/Handler;");
    }

    public long getWhen() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->getWhen()J");
    }

    public boolean isAsynchronous() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->isAsynchronous()Z");
    }

    public static Message obtain() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->obtain()Landroid/os/Message;");
    }

    public static Message obtain(Handler arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->obtain(Landroid/os/Handler;)Landroid/os/Message;");
    }

    public static Message obtain(Handler arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->obtain(Landroid/os/Handler;I)Landroid/os/Message;");
    }

    public static Message obtain(Handler arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->obtain(Landroid/os/Handler;III)Landroid/os/Message;");
    }

    public static Message obtain(Handler arg0, int arg1, int arg2, int arg3, Object arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->obtain(Landroid/os/Handler;IIILjava/lang/Object;)Landroid/os/Message;");
    }

    public static Message obtain(Handler arg0, int arg1, Object arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->obtain(Landroid/os/Handler;ILjava/lang/Object;)Landroid/os/Message;");
    }

    public static Message obtain(Handler arg0, Runnable arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->obtain(Landroid/os/Handler;Ljava/lang/Runnable;)Landroid/os/Message;");
    }

    public static Message obtain(Message arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->obtain(Landroid/os/Message;)Landroid/os/Message;");
    }

    public Bundle peekData() {
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

    public void setData(Bundle arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->setData(Landroid/os/Bundle;)V");
    }

    public void setTarget(Handler arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->setTarget(Landroid/os/Handler;)V");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Message;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
