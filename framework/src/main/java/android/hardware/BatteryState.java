package android.hardware;

    public class BatteryState {

    public static final int STATUS_CHARGING = 2;
    public static final int STATUS_DISCHARGING = 3;
    public static final int STATUS_FULL = 5;
    public static final int STATUS_NOT_CHARGING = 4;
    public static final int STATUS_UNKNOWN = 1;

    public BatteryState() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/BatteryState;-><init>()V");
    }

    public float getCapacity() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/BatteryState;->getCapacity()F");
    }

    public int getStatus() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/BatteryState;->getStatus()I");
    }

    public boolean isPresent() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/BatteryState;->isPresent()Z");
    }
    }
