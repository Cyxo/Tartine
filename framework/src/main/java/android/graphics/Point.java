package android.graphics;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * AOSP {@code android.graphics.Point}: a mutable point with integer
 * coordinates.
 */
public class Point implements Parcelable {

    public static Parcelable.Creator CREATOR = new Parcelable.Creator<Point>() {
        @Override
        public Point createFromParcel(Parcel source) {
            Point point = new Point();
            point.readFromParcel(source);
            return point;
        }

        @Override
        public Point[] newArray(int size) {
            return new Point[size];
        }
    };
    public int x;
    public int y;

    public Point() {
    }

    public Point(Point arg0) {
        x = arg0.x;
        y = arg0.y;
    }

    public Point(int arg0, int arg1) {
        x = arg0;
        y = arg1;
    }

    public int describeContents() {
        return 0;
    }

    public final boolean equals(int arg0, int arg1) {
        return x == arg0 && y == arg1;
    }

    @Override
    public boolean equals(Object arg0) {
        if (this == arg0) {
            return true;
        }
        if (!(arg0 instanceof Point)) {
            return false;
        }
        Point other = (Point) arg0;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }

    public final void negate() {
        x = -x;
        y = -y;
    }

    public final void offset(int arg0, int arg1) {
        x += arg0;
        y += arg1;
    }

    public void readFromParcel(Parcel arg0) {
        x = arg0.readInt();
        y = arg0.readInt();
    }

    public void set(int arg0, int arg1) {
        x = arg0;
        y = arg1;
    }

    @Override
    public String toString() {
        return "Point(" + x + ", " + y + ")";
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        arg0.writeInt(x);
        arg0.writeInt(y);
    }
}
