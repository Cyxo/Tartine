package android.os;

    public abstract class VibrationEffect implements android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;
    public static final int DEFAULT_AMPLITUDE = -1;
    public static final int EFFECT_CLICK = 0;
    public static final int EFFECT_DOUBLE_CLICK = 1;
    public static final int EFFECT_HEAVY_CLICK = 5;
    public static final int EFFECT_TICK = 2;

    public static android.os.VibrationEffect createOneShot(long arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/VibrationEffect;->createOneShot(JI)Landroid/os/VibrationEffect;");
    }

    public static android.os.VibrationEffect createPredefined(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/VibrationEffect;->createPredefined(I)Landroid/os/VibrationEffect;");
    }

    public static android.os.VibrationEffect createRepeatingEffect(android.os.VibrationEffect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/VibrationEffect;->createRepeatingEffect(Landroid/os/VibrationEffect;)Landroid/os/VibrationEffect;");
    }

    public static android.os.VibrationEffect createRepeatingEffect(android.os.VibrationEffect arg0, android.os.VibrationEffect arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/VibrationEffect;->createRepeatingEffect(Landroid/os/VibrationEffect;Landroid/os/VibrationEffect;)Landroid/os/VibrationEffect;");
    }

    public static android.os.VibrationEffect createWaveform(long[] arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/VibrationEffect;->createWaveform([JI)Landroid/os/VibrationEffect;");
    }

    public static android.os.VibrationEffect createWaveform(long[] arg0, int[] arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/VibrationEffect;->createWaveform([J[II)Landroid/os/VibrationEffect;");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/VibrationEffect;->describeContents()I");
    }

    public static android.os.VibrationEffect.Composition startComposition() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/VibrationEffect;->startComposition()Landroid/os/VibrationEffect$Composition;");
    }
        public static class Composition {

        public static final int DELAY_TYPE_PAUSE = 0;
        public static final int DELAY_TYPE_RELATIVE_START_OFFSET = 1;
        public static final int PRIMITIVE_CLICK = 1;
        public static final int PRIMITIVE_LOW_TICK = 8;
        public static final int PRIMITIVE_QUICK_FALL = 6;
        public static final int PRIMITIVE_QUICK_RISE = 4;
        public static final int PRIMITIVE_SLOW_RISE = 5;
        public static final int PRIMITIVE_SPIN = 3;
        public static final int PRIMITIVE_THUD = 2;
        public static final int PRIMITIVE_TICK = 7;

        public android.os.VibrationEffect.Composition addPrimitive(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/VibrationEffect$Composition;->addPrimitive(I)Landroid/os/VibrationEffect$Composition;");
        }

        public android.os.VibrationEffect.Composition addPrimitive(int arg0, float arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/VibrationEffect$Composition;->addPrimitive(IF)Landroid/os/VibrationEffect$Composition;");
        }

        public android.os.VibrationEffect.Composition addPrimitive(int arg0, float arg1, int arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/VibrationEffect$Composition;->addPrimitive(IFI)Landroid/os/VibrationEffect$Composition;");
        }

        public android.os.VibrationEffect.Composition addPrimitive(int arg0, float arg1, int arg2, int arg3) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/VibrationEffect$Composition;->addPrimitive(IFII)Landroid/os/VibrationEffect$Composition;");
        }

        public android.os.VibrationEffect compose() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/VibrationEffect$Composition;->compose()Landroid/os/VibrationEffect;");
        }
        }
    }
