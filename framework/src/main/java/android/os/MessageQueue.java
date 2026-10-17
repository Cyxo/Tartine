package android.os;

    public class MessageQueue {

    public void addIdleHandler(android.os.MessageQueue.IdleHandler arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/MessageQueue;->addIdleHandler(Landroid/os/MessageQueue$IdleHandler;)V");
    }

    public void addOnFileDescriptorEventListener(java.io.FileDescriptor arg0, int arg1, android.os.MessageQueue.OnFileDescriptorEventListener arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/MessageQueue;->addOnFileDescriptorEventListener(Ljava/io/FileDescriptor;ILandroid/os/MessageQueue$OnFileDescriptorEventListener;)V");
    }

    protected void finalize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/MessageQueue;->finalize()V");
    }

    public boolean isIdle() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/MessageQueue;->isIdle()Z");
    }

    public void removeIdleHandler(android.os.MessageQueue.IdleHandler arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/MessageQueue;->removeIdleHandler(Landroid/os/MessageQueue$IdleHandler;)V");
    }

    public void removeOnFileDescriptorEventListener(java.io.FileDescriptor arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/MessageQueue;->removeOnFileDescriptorEventListener(Ljava/io/FileDescriptor;)V");
    }
        public static interface IdleHandler {

        public abstract boolean queueIdle();
        }
        public static interface OnFileDescriptorEventListener {

        public static final int EVENT_ERROR = 4;
        public static final int EVENT_INPUT = 1;
        public static final int EVENT_OUTPUT = 2;

        public abstract int onFileDescriptorEvents(java.io.FileDescriptor arg0, int arg1);
        }
    }
