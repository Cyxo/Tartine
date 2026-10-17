package android.os;

    public abstract class CombinedVibration implements android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;

    public static android.os.CombinedVibration createParallel(android.os.VibrationEffect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/CombinedVibration;->createParallel(Landroid/os/VibrationEffect;)Landroid/os/CombinedVibration;");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/CombinedVibration;->describeContents()I");
    }

    public static android.os.CombinedVibration.ParallelCombination startParallel() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/CombinedVibration;->startParallel()Landroid/os/CombinedVibration$ParallelCombination;");
    }
        public static class ParallelCombination {

        public android.os.CombinedVibration.ParallelCombination addVibrator(int arg0, android.os.VibrationEffect arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/CombinedVibration$ParallelCombination;->addVibrator(ILandroid/os/VibrationEffect;)Landroid/os/CombinedVibration$ParallelCombination;");
        }

        public android.os.CombinedVibration combine() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/CombinedVibration$ParallelCombination;->combine()Landroid/os/CombinedVibration;");
        }
        }
    }
