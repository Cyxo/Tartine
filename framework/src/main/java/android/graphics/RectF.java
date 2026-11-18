package android.graphics;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Tartine: ported from AOSP {@code android.graphics.RectF}. The float
 * rectangle is pure Java on the device too (the native code lives in the
 * canvas/skia path), so the geometry is a straight port. Parcel
 * serialization stays a gap until the shell needs it.
 */
public class RectF implements Parcelable {

    public static Parcelable.Creator CREATOR;
    public float bottom;
    public float left;
    public float right;
    public float top;

    public RectF() {
    }

    public RectF(Rect arg0) {
        set(arg0);
    }

    public RectF(RectF arg0) {
        set(arg0);
    }

    public RectF(float arg0, float arg1, float arg2, float arg3) {
        set(arg0, arg1, arg2, arg3);
    }

    public final float centerX() {
        return (left + right) * 0.5f;
    }

    public final float centerY() {
        return (top + bottom) * 0.5f;
    }

    public boolean contains(RectF arg0) {
        // AOSP RectF.contains(RectF): an empty rectangle contains nothing.
        return left < right && top < bottom
                && left <= arg0.left && top <= arg0.top
                && right >= arg0.right && bottom >= arg0.bottom;
    }

    public boolean contains(float arg0, float arg1) {
        // Left/top are inside, right/bottom are not.
        return left < right && top < bottom
                && arg0 >= left && arg0 < right
                && arg1 >= top && arg1 < bottom;
    }

    public boolean contains(float arg0, float arg1, float arg2,
            float arg3) {
        return this.left < this.right && this.top < this.bottom
                && this.left <= arg0 && this.top <= arg1
                && this.right >= arg2 && this.bottom >= arg3;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object arg0) {
        // AOSP RectF.equals: class-strict field comparison.
        if (this == arg0) {
            return true;
        }
        if (arg0 == null || getClass() != arg0.getClass()) {
            return false;
        }
        RectF other = (RectF) arg0;
        return left == other.left && top == other.top
                && right == other.right && bottom == other.bottom;
    }

    public int hashCode() {
        // AOSP RectF.hashCode, in field order.
        int result = Float.floatToIntBits(left);
        result = 31 * result + Float.floatToIntBits(top);
        result = 31 * result + Float.floatToIntBits(right);
        result = 31 * result + Float.floatToIntBits(bottom);
        return result;
    }

    public final float height() {
        return bottom - top;
    }

    public void inset(float arg0, float arg1) {
        left += arg0;
        top += arg1;
        right -= arg0;
        bottom -= arg1;
    }

    public boolean intersect(RectF arg0) {
        return intersect(arg0.left, arg0.top, arg0.right, arg0.bottom);
    }

    public boolean intersect(float arg0, float arg1, float arg2,
            float arg3) {
        if (this.left < arg2 && arg0 < this.right
                && this.top < arg3 && arg1 < this.bottom) {
            if (this.left < arg0) {
                this.left = arg0;
            }
            if (this.top < arg1) {
                this.top = arg1;
            }
            if (this.right > arg2) {
                this.right = arg2;
            }
            if (this.bottom > arg3) {
                this.bottom = arg3;
            }
            return true;
        }
        return false;
    }

    public static boolean intersects(RectF arg0, RectF arg1) {
        return arg0.left < arg1.right && arg1.left < arg0.right
                && arg0.top < arg1.bottom && arg1.top < arg0.bottom;
    }

    public boolean intersects(float arg0, float arg1, float arg2,
            float arg3) {
        return this.left < arg2 && arg0 < this.right
                && this.top < arg3 && arg1 < this.bottom;
    }

    public final boolean isEmpty() {
        return left >= right || top >= bottom;
    }

    public void offset(float arg0, float arg1) {
        left += arg0;
        top += arg1;
        right += arg0;
        bottom += arg1;
    }

    public void offsetTo(float arg0, float arg1) {
        right += arg0 - left;
        bottom += arg1 - top;
        left = arg0;
        top = arg1;
    }

    public void readFromParcel(Parcel arg0) {
        throw new UnsupportedOperationException(
                "Tartine: not implemented: Landroid/graphics/RectF;->readFromParcel(Landroid/os/Parcel;)V");
    }

    public void round(Rect arg0) {
        arg0.set(Math.round(left), Math.round(top), Math.round(right),
                Math.round(bottom));
    }

    public void roundOut(Rect arg0) {
        arg0.set((int) Math.floor(left), (int) Math.floor(top),
                (int) Math.ceil(right), (int) Math.ceil(bottom));
    }

    public void set(Rect arg0) {
        left = arg0.left;
        top = arg0.top;
        right = arg0.right;
        bottom = arg0.bottom;
    }

    public void set(RectF arg0) {
        left = arg0.left;
        top = arg0.top;
        right = arg0.right;
        bottom = arg0.bottom;
    }

    public void set(float arg0, float arg1, float arg2, float arg3) {
        left = arg0;
        top = arg1;
        right = arg2;
        bottom = arg3;
    }

    public void setEmpty() {
        left = right = top = bottom = 0;
    }

    public boolean setIntersect(RectF arg0, RectF arg1) {
        if (arg0.left < arg1.right && arg1.left < arg0.right
                && arg0.top < arg1.bottom && arg1.top < arg0.bottom) {
            left = Math.max(arg0.left, arg1.left);
            top = Math.max(arg0.top, arg1.top);
            right = Math.min(arg0.right, arg1.right);
            bottom = Math.min(arg0.bottom, arg1.bottom);
            return true;
        }
        return false;
    }

    public void sort() {
        if (left > right) {
            float temp = left;
            left = right;
            right = temp;
        }
        if (top > bottom) {
            float temp = top;
            top = bottom;
            bottom = temp;
        }
    }

    public String toShortString() {
        return toShortString(new StringBuilder(32));
    }

    /** AOSP RectF.toShortString(StringBuilder): compact "[l,t][r,b]". */
    public String toShortString(StringBuilder arg0) {
        arg0.setLength(0);
        arg0.append('[');
        arg0.append(left);
        arg0.append(',');
        arg0.append(top);
        arg0.append("][");
        arg0.append(right);
        arg0.append(',');
        arg0.append(bottom);
        arg0.append(']');
        return arg0.toString();
    }

    public String toString() {
        return "RectF(" + left + ", " + top + ", " + right + ", "
                + bottom + ")";
    }

    public void union(RectF arg0) {
        union(arg0.left, arg0.top, arg0.right, arg0.bottom);
    }

    public void union(float arg0, float arg1) {
        if (arg0 > right) {
            right = arg0;
        } else if (arg0 < left) {
            left = arg0;
        }
        if (arg1 > bottom) {
            bottom = arg1;
        } else if (arg1 < top) {
            top = arg1;
        }
    }

    public void union(float arg0, float arg1, float arg2, float arg3) {
        if (arg0 < arg2 && arg1 < arg3) {
            if (this.left < this.right && this.top < this.bottom) {
                if (this.left > arg0) {
                    this.left = arg0;
                }
                if (this.top > arg1) {
                    this.top = arg1;
                }
                if (this.right < arg2) {
                    this.right = arg2;
                }
                if (this.bottom < arg3) {
                    this.bottom = arg3;
                }
            } else {
                this.left = arg0;
                this.top = arg1;
                this.right = arg2;
                this.bottom = arg3;
            }
        }
    }

    public final float width() {
        return right - left;
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException(
                "Tartine: not implemented: Landroid/graphics/RectF;->writeToParcel(Landroid/os/Parcel;I)V");
    }
}
