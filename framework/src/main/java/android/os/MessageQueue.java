package android.os;

import java.io.FileDescriptor;

    public class MessageQueue {

    public void addIdleHandler(MessageQueue.IdleHandler arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/MessageQueue;->addIdleHandler(Landroid/os/MessageQueue$IdleHandler;)V");
    }

    public void addOnFileDescriptorEventListener(FileDescriptor arg0, int arg1, MessageQueue.OnFileDescriptorEventListener arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/MessageQueue;->addOnFileDescriptorEventListener(Ljava/io/FileDescriptor;ILandroid/os/MessageQueue$OnFileDescriptorEventListener;)V");
    }

    protected void finalize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/MessageQueue;->finalize()V");
    }

    public boolean isIdle() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/MessageQueue;->isIdle()Z");
    }

    public void removeIdleHandler(MessageQueue.IdleHandler arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/MessageQueue;->removeIdleHandler(Landroid/os/MessageQueue$IdleHandler;)V");
    }

    public void removeOnFileDescriptorEventListener(FileDescriptor arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/MessageQueue;->removeOnFileDescriptorEventListener(Ljava/io/FileDescriptor;)V");
    }
        public static interface IdleHandler {

        public abstract boolean queueIdle();
        }
        public static interface OnFileDescriptorEventListener {

        public static final int EVENT_ERROR = 4;
        public static final int EVENT_INPUT = 1;
        public static final int EVENT_OUTPUT = 2;

        public abstract int onFileDescriptorEvents(FileDescriptor arg0, int arg1);
        }
    }
