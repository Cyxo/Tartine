package android.view;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;

    public class RoundedCorner implements Parcelable {

    public static Parcelable.Creator CREATOR;
    public static final int POSITION_BOTTOM_LEFT = 3;
    public static final int POSITION_BOTTOM_RIGHT = 2;
    public static final int POSITION_TOP_LEFT = 0;
    public static final int POSITION_TOP_RIGHT = 1;

    public RoundedCorner(int arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/RoundedCorner;-><init>(IIII)V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/RoundedCorner;->describeContents()I");
    }

    public boolean equals(Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/RoundedCorner;->equals(Ljava/lang/Object;)Z");
    }

    public Point getCenter() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/RoundedCorner;->getCenter()Landroid/graphics/Point;");
    }

    public int getPosition() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/RoundedCorner;->getPosition()I");
    }

    public int getRadius() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/RoundedCorner;->getRadius()I");
    }

    public int hashCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/RoundedCorner;->hashCode()I");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/RoundedCorner;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/RoundedCorner;->writeToParcel(Landroid/os/Parcel;I)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public RoundedCorner() {
        }
    }
