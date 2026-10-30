package android.os;

import android.media.AudioAttributes;
import android.os.vibrator.VibratorEnvelopeEffectInfo;
import android.os.vibrator.VibratorFrequencyProfile;

    public class Vibrator {

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

    public void cancel() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Vibrator;->cancel()V");
    }

    public VibratorEnvelopeEffectInfo getEnvelopeEffectInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Vibrator;->getEnvelopeEffectInfo()Landroid/os/vibrator/VibratorEnvelopeEffectInfo;");
    }

    public VibratorFrequencyProfile getFrequencyProfile() {
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

    public boolean hasAmplitudeControl() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Vibrator;->hasAmplitudeControl()Z");
    }

    public boolean hasVibrator() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Vibrator;->hasVibrator()Z");
    }

    public void vibrate(VibrationEffect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Vibrator;->vibrate(Landroid/os/VibrationEffect;)V");
    }

    public void vibrate(VibrationEffect arg0, AudioAttributes arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Vibrator;->vibrate(Landroid/os/VibrationEffect;Landroid/media/AudioAttributes;)V");
    }

    public void vibrate(VibrationEffect arg0, VibrationAttributes arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Vibrator;->vibrate(Landroid/os/VibrationEffect;Landroid/os/VibrationAttributes;)V");
    }

    public void vibrate(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Vibrator;->vibrate(J)V");
    }

    public void vibrate(long arg0, AudioAttributes arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Vibrator;->vibrate(JLandroid/media/AudioAttributes;)V");
    }

    public void vibrate(long[] arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Vibrator;->vibrate([JI)V");
    }

    public void vibrate(long[] arg0, int arg1, AudioAttributes arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Vibrator;->vibrate([JILandroid/media/AudioAttributes;)V");
    }
    }
