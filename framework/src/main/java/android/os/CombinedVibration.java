package android.os;

    public class CombinedVibration implements Parcelable {

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/CombinedVibration;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    public static Parcelable.Creator CREATOR;

    public static CombinedVibration createParallel(VibrationEffect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/CombinedVibration;->createParallel(Landroid/os/VibrationEffect;)Landroid/os/CombinedVibration;");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/CombinedVibration;->describeContents()I");
    }

    public static CombinedVibration.ParallelCombination startParallel() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/CombinedVibration;->startParallel()Landroid/os/CombinedVibration$ParallelCombination;");
    }
        public static class ParallelCombination {

        public CombinedVibration.ParallelCombination addVibrator(int arg0, VibrationEffect arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/CombinedVibration$ParallelCombination;->addVibrator(ILandroid/os/VibrationEffect;)Landroid/os/CombinedVibration$ParallelCombination;");
        }

        public CombinedVibration combine() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/CombinedVibration$ParallelCombination;->combine()Landroid/os/CombinedVibration;");
        }
        }
    }
