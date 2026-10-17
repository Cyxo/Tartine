package android.os;

    public abstract class Vibrator {

    public static final int VIBRATION_EFFECT_SUPPORT_NO = 2;
    public static final int VIBRATION_EFFECT_SUPPORT_UNKNOWN = 0;
    public static final int VIBRATION_EFFECT_SUPPORT_YES = 1;

    public final int areAllEffectsSupported(int[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Vibrator;->areAllEffectsSupported([I)I");
    }

    public final boolean areAllPrimitivesSupported(int[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Vibrator;->areAllPrimitivesSupported([I)Z");
    }

    public int[] areEffectsSupported(int[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Vibrator;->areEffectsSupported([I)[I");
    }

    public boolean areEnvelopeEffectsSupported() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Vibrator;->areEnvelopeEffectsSupported()Z");
    }

    public boolean[] arePrimitivesSupported(int[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Vibrator;->arePrimitivesSupported([I)[Z");
    }

    public abstract void cancel();

    public android.os.vibrator.VibratorEnvelopeEffectInfo getEnvelopeEffectInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Vibrator;->getEnvelopeEffectInfo()Landroid/os/vibrator/VibratorEnvelopeEffectInfo;");
    }

    public android.os.vibrator.VibratorFrequencyProfile getFrequencyProfile() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Vibrator;->getFrequencyProfile()Landroid/os/vibrator/VibratorFrequencyProfile;");
    }

    public int getId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Vibrator;->getId()I");
    }

    public int[] getPrimitiveDurations(int[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Vibrator;->getPrimitiveDurations([I)[I");
    }

    public float getQFactor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Vibrator;->getQFactor()F");
    }

    public float getResonantFrequency() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Vibrator;->getResonantFrequency()F");
    }

    public abstract boolean hasAmplitudeControl();

    public abstract boolean hasVibrator();

    public void vibrate(android.os.VibrationEffect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Vibrator;->vibrate(Landroid/os/VibrationEffect;)V");
    }

    public void vibrate(android.os.VibrationEffect arg0, android.media.AudioAttributes arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Vibrator;->vibrate(Landroid/os/VibrationEffect;Landroid/media/AudioAttributes;)V");
    }

    public void vibrate(android.os.VibrationEffect arg0, android.os.VibrationAttributes arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Vibrator;->vibrate(Landroid/os/VibrationEffect;Landroid/os/VibrationAttributes;)V");
    }

    public void vibrate(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Vibrator;->vibrate(J)V");
    }

    public void vibrate(long arg0, android.media.AudioAttributes arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Vibrator;->vibrate(JLandroid/media/AudioAttributes;)V");
    }

    public void vibrate(long[] arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Vibrator;->vibrate([JI)V");
    }

    public void vibrate(long[] arg0, int arg1, android.media.AudioAttributes arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Vibrator;->vibrate([JILandroid/media/AudioAttributes;)V");
    }
    }
