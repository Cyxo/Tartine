package android.graphics;

    /**
     * Geometry core ported from aosp-mirror/platform_frameworks_base
     * graphics/java/android/graphics/Rect.java: this is a pure-Java value
     * class on AOSP (no natives), so the port is near-verbatim per method;
     * each body mirrors the AOSP source. Parcel machinery
     * (readFromParcel/writeToParcel/CREATOR) and the regex-backed
     * {@code unflattenFromString} remain gap stubs pending those surfaces.
     */
    public class Rect implements android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;
    public int bottom;
    public int left;
    public int right;
    public int top;

    /** AOSP Rect() is an empty body: fields default to 0. */
    public Rect() {
    }

    /**
     * Create a new rectangle with the specified coordinates. Note: no
     * range checking is performed, so the caller must ensure left &lt;= right
     * and top &lt;= bottom.
     */
    public Rect(int arg0, int arg1, int arg2, int arg3) {
        this.left = arg0;
        this.top = arg1;
        this.right = arg2;
        this.bottom = arg3;
    }

    public Rect(android.graphics.Rect arg0) {
        if (arg0 == null) {
            left = top = right = bottom = 0;
        } else {
            left = arg0.left;
            top = arg0.top;
            right = arg0.right;
            bottom = arg0.bottom;
        }
    }

    @Override
    public java.lang.String toString() {
        StringBuilder sb = new StringBuilder(24);
        // Ported from AOSP Rect#toString(Rect/Integer format leg).
        sb.append("Rect("); sb.append(left); sb.append(", ");
        sb.append(top); sb.append(" - "); sb.append(right);
        sb.append(", "); sb.append(bottom); sb.append(")");
        return sb.toString();
    }

    /**
     * Return a string representation of the rectangle in a compact form.
     */
    public java.lang.String toShortString() {
        StringBuilder sb = new StringBuilder(32);
        sb.append('['); sb.append(left); sb.append(',');
        sb.append(top); sb.append("]["); sb.append(right);
        sb.append(','); sb.append(bottom); sb.append(']');
        return sb.toString();
    }

    /** Return a string of the form "left top right bottom". */
    public java.lang.String flattenToString() {
        StringBuilder sb = new StringBuilder(32);
        sb.append(left);
        sb.append(' ');
        sb.append(top);
        sb.append(' ');
        sb.append(right);
        sb.append(' ');
        sb.append(bottom);
        return sb.toString();
    }

    /** Ported from AOSP Rect#isEmpty. */
    public final boolean isEmpty() {
        return left >= right || top >= bottom;
    }

    /** Ported from AOSP Rect#width. */
    public final int width() {
        return right - left;
    }

    /** Ported from AOSP Rect#height. */
    public final int height() {
        return bottom - top;
    }

    /** Ported from AOSP Rect#centerX. */
    public int centerX() {
        return (left + right) >> 1;
    }

    /** Ported from AOSP Rect#centerY. */
    public int centerY() {
        return (top + bottom) >> 1;
    }

    /** Ported from AOSP Rect#exactCenterX. */
    public float exactCenterX() {
        return (left + right) * 0.5f;
    }

    /** Ported from AOSP Rect#exactCenterY. */
    public float exactCenterY() {
        return (top + bottom) * 0.5f;
    }

    /** Ported from AOSP Rect#setEmpty. */
    public void setEmpty() {
        left = right = top = bottom = 0;
    }

    /** Ported from AOSP Rect#set(int,int,int,int). */
    public void set(int arg0, int arg1, int arg2, int arg3) {
        this.left = arg0;
        this.top = arg1;
        this.right = arg2;
        this.bottom = arg3;
    }

    /** Ported from AOSP Rect#set(Rect). */
    public void set(android.graphics.Rect arg0) {
        this.left = arg0.left;
        this.top = arg0.top;
        this.right = arg0.right;
        this.bottom = arg0.bottom;
    }

    /** Ported from AOSP Rect#offset. */
    public void offset(int arg0, int arg1) {
        left += arg0;
        top += arg1;
        right += arg0;
        bottom += arg1;
    }

    /** Ported from AOSP Rect#offsetTo. */
    public void offsetTo(int arg0, int arg1) {
        right += arg0 - left;
        bottom += arg1 - top;
        left = arg0;
        top = arg1;
    }

    /** Ported from AOSP Rect#inset(int,int). */
    public void inset(int arg0, int arg1) {
        left += arg0;
        top += arg1;
        right -= arg0;
        bottom -= arg1;
    }

    public void inset(android.graphics.Insets arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Rect;->inset(Landroid/graphics/Insets;)V");
    }

    /** Ported from AOSP Rect#inset(int,int,int,int). */
    public void inset(int arg0, int arg1, int arg2, int arg3) {
        this.left += arg0;
        this.top += arg1;
        this.right -= arg2;
        this.bottom -= arg3;
    }

    /** Ported from AOSP Rect#contains(int x, int y). */
    public boolean contains(int arg0, int arg1) {
        return left < right && top < bottom
               && arg0 >= left && arg0 < right && arg1 >= top && arg1 < bottom;
    }

    /** Ported from AOSP Rect#contains(int,int,int,int). */
    public boolean contains(int arg0, int arg1, int arg2, int arg3) {
        return this.left < this.right && this.top < this.bottom
                && this.left <= arg0 && this.top <= arg1
                && this.right >= arg2 && this.bottom >= arg3;
    }

    /** Ported from AOSP Rect#contains(Rect). */
    public boolean contains(android.graphics.Rect arg0) {
        return this.left < this.right && this.top < this.bottom
               && left <= arg0.left && top <= arg0.top
               && right >= arg0.right && bottom >= arg0.bottom;
    }

    /** Ported from AOSP Rect#intersect(int,int,int,int). */
    public boolean intersect(int arg0, int arg1, int arg2, int arg3) {
        if (this.left < arg2 && arg0 < this.right && this.top < arg3 && arg1 < this.bottom) {
            if (this.left < arg0) this.left = arg0;
            if (this.top < arg1) this.top = arg1;
            if (this.right > arg2) this.right = arg2;
            if (this.bottom > arg3) this.bottom = arg3;
            return true;
        }
        return false;
    }

    /** Ported from AOSP Rect#intersect(Rect). */
    public boolean intersect(android.graphics.Rect arg0) {
        return intersect(arg0.left, arg0.top, arg0.right, arg0.bottom);
    }

    /** Ported from AOSP Rect#setIntersect(Rect,Rect). */
    public boolean setIntersect(android.graphics.Rect arg0, android.graphics.Rect arg1) {
        if (arg0.left < arg1.right && arg1.left < arg0.right && arg0.top < arg1.bottom && arg1.top < arg0.bottom) {
            left = Math.max(arg0.left, arg1.left);
            top = Math.max(arg0.top, arg1.top);
            right = Math.min(arg0.right, arg1.right);
            bottom = Math.min(arg0.bottom, arg1.bottom);
            return true;
        }
        return false;
    }

    /** Ported from AOSP Rect#intersects(int,int,int,int). */
    public boolean intersects(int arg0, int arg1, int arg2, int arg3) {
        return this.left < arg2 && arg0 < this.right && this.top < arg3 && arg1 < this.bottom;
    }

    /** Ported from AOSP Rect#intersects(Rect,Rect). */
    public static boolean intersects(android.graphics.Rect arg0, android.graphics.Rect arg1) {
        return arg0.left < arg1.right && arg1.left < arg0.right && arg0.top < arg1.bottom && arg1.top < arg0.bottom;
    }

    /** Ported from AOSP Rect#union(int,int,int,int). */
    public void union(int arg0, int arg1, int arg2, int arg3) {
        if ((arg0 < arg2) && (arg1 < arg3)) {
            if ((this.left < this.right) && (this.top < this.bottom)) {
                if (this.left > arg0) this.left = arg0;
                if (this.top > arg1) this.top = arg1;
                if (this.right < arg2) this.right = arg2;
                if (this.bottom < arg3) this.bottom = arg3;
            } else {
                this.left = arg0;
                this.top = arg1;
                this.right = arg2;
                this.bottom = arg3;
            }
        }
    }

    /** Ported from AOSP Rect#union(Rect). */
    public void union(android.graphics.Rect arg0) {
        union(arg0.left, arg0.top, arg0.right, arg0.bottom);
    }

    /** Ported from AOSP Rect#union(int x, int y). */
    public void union(int arg0, int arg1) {
        if (arg0 < left) {
            left = arg0;
        } else if (arg0 > right) {
            right = arg0;
        }
        if (arg1 < top) {
            top = arg1;
        } else if (arg1 > bottom) {
            bottom = arg1;
        }
    }

    /** Ported from AOSP Rect#sort. */
    public void sort() {
        if (left > right) {
            int temp = left;
            left = right;
            right = temp;
        }
        if (top > bottom) {
            int temp = top;
            top = bottom;
            bottom = temp;
        }
    }

    /** Ported from AOSP Rect#equals. */
    @Override
    public boolean equals(java.lang.Object arg0) {
        if (this == arg0) return true;
        if (arg0 == null || getClass() != arg0.getClass()) return false;

        android.graphics.Rect r = (android.graphics.Rect) arg0;
        return left == r.left && top == r.top && right == r.right && bottom == r.bottom;
    }

    /** Ported from AOSP Rect#hashCode. */
    @Override
    public int hashCode() {
        int result = left;
        result = 31 * result + top;
        result = 31 * result + right;
        result = 31 * result + bottom;
        return result;
    }

    @Override
    public int describeContents() {
        // Ported from AOSP Rect#describeContents: returns 0.
        return 0;
    }

    public void readFromParcel(android.os.Parcel arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Rect;->readFromParcel(Landroid/os/Parcel;)V");
    }

    public static android.graphics.Rect unflattenFromString(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Rect;->unflattenFromString(Ljava/lang/String;)Landroid/graphics/Rect;");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Rect;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
