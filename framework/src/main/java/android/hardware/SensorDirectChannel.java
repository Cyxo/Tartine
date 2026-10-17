package android.hardware;

    public class SensorDirectChannel implements java.nio.channels.Channel {

    public static final int RATE_FAST = 2;
    public static final int RATE_NORMAL = 1;
    public static final int RATE_STOP = 0;
    public static final int RATE_VERY_FAST = 3;
    public static final int TYPE_HARDWARE_BUFFER = 2;
    public static final int TYPE_MEMORY_FILE = 1;

    public void close() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorDirectChannel;->close()V");
    }

    public int configure(android.hardware.Sensor arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorDirectChannel;->configure(Landroid/hardware/Sensor;I)I");
    }

    protected void finalize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorDirectChannel;->finalize()V");
    }

    public boolean isOpen() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorDirectChannel;->isOpen()Z");
    }
    }
