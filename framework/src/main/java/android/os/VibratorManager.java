package android.os;

    public abstract class VibratorManager {

    public abstract void cancel();

    public abstract android.os.Vibrator getDefaultVibrator();

    public abstract android.os.Vibrator getVibrator(int arg0);

    public abstract int[] getVibratorIds();

    public final void vibrate(android.os.CombinedVibration arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/VibratorManager;->vibrate(Landroid/os/CombinedVibration;)V");
    }

    public final void vibrate(android.os.CombinedVibration arg0, android.os.VibrationAttributes arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/VibratorManager;->vibrate(Landroid/os/CombinedVibration;Landroid/os/VibrationAttributes;)V");
    }
    }
