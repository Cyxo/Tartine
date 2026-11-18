package android.graphics;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * AOSP {@code android.graphics.PointF}: a mutable point with float
 * coordinates. Material's shape pipeline keeps its arc-join scratch points in
 * one, so the plain value semantics are all that is needed.
 */
public class PointF implements Parcelable {

    public static Parcelable.Creator CREATOR = new Parcelable.Creator<PointF>() {
        @Override
        public PointF createFromParcel(Parcel source) {
            PointF point = new PointF();
            point.readFromParcel(source);
            return point;
        }

        @Override
        public PointF[] newArray(int size) {
            return new PointF[size];
        }
    };
    public float x;
    public float y;

    public PointF() {
    }

    public PointF(Point arg0) {
        x = arg0.x;
        y = arg0.y;
    }

    public PointF(PointF arg0) {
        x = arg0.x;
        y = arg0.y;
    }

    public PointF(float arg0, float arg1) {
        x = arg0;
        y = arg1;
    }

    public int describeContents() {
        return 0;
    }

    public final boolean equals(float arg0, float arg1) {
        return x == arg0 && y == arg1;
    }

    @Override
    public boolean equals(Object arg0) {
        if (this == arg0) {
            return true;
        }
        if (!(arg0 instanceof PointF)) {
            return false;
        }
        PointF other = (PointF) arg0;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        int result = Float.floatToIntBits(x);
        return 31 * result + Float.floatToIntBits(y);
    }

    public final float length() {
        return length(x, y);
    }

    public static float length(float arg0, float arg1) {
        return (float) Math.hypot(arg0, arg1);
    }

    public final void negate() {
        x = -x;
        y = -y;
    }

    public final void offset(float arg0, float arg1) {
        x += arg0;
        y += arg1;
    }

    public void readFromParcel(Parcel arg0) {
        x = arg0.readFloat();
        y = arg0.readFloat();
    }

    public final void set(PointF arg0) {
        x = arg0.x;
        y = arg0.y;
    }

    public final void set(float arg0, float arg1) {
        x = arg0;
        y = arg1;
    }

    @Override
    public String toString() {
        return "PointF(" + x + ", " + y + ")";
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        arg0.writeFloat(x);
        arg0.writeFloat(y);
    }
}
