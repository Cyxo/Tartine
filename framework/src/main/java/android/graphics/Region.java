package android.graphics;

import android.os.Parcel;
import android.os.Parcelable;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

/**
 * AOSP {@code android.graphics.Region}: the set of integer pixels inside one
 * or more rectangles.
 *
 * <p>Skia keeps an integer scanline region natively. The desktop model keeps
 * an exact {@link Area}, which gives the same set algebra ({@link #op}) while
 * accepting the arbitrary contours {@link #setPath} can produce. Deviations
 * (noted at each site): the model is not quantized to integer scanlines, so a
 * path-produced region can have fractional edges and {@link #isRect} only
 * recognizes an axis-aligned rectangle exactly; {@link #contains} tests pixel
 * centers.
 *
 * <p>Material's shape pipeline uses a region to cache the transparent area
 * around a shape, so {@code set}, {@code setPath} and the difference operator
 * are the operations that matter first; the remaining surface is filled in
 * for API completeness.
 */
public class Region implements Parcelable {

    public static Parcelable.Creator CREATOR = new Parcelable.Creator<Region>() {
        @Override
        public Region createFromParcel(Parcel source) {
            return new Region(source.readInt(), source.readInt(),
                    source.readInt(), source.readInt());
        }

        @Override
        public Region[] newArray(int size) {
            return new Region[size];
        }
    };

    private Area area = new Area();

    public Region() {
        // AOSP Region(): an empty region.
    }

    public Region(Rect arg0) {
        set(arg0);
    }

    public Region(Region arg0) {
        set(arg0);
    }

    public Region(int arg0, int arg1, int arg2, int arg3) {
        set(arg0, arg1, arg2, arg3);
    }

    public boolean contains(int arg0, int arg1) {
        // Deviation: pixel-center test stands in for the integer scanline
        // test (see the class note).
        return area.contains(arg0 + 0.5, arg1 + 0.5);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object arg0) {
        if (this == arg0) {
            return true;
        }
        if (!(arg0 instanceof Region)) {
            return false;
        }
        return area.equals(((Region) arg0).area);
    }

    @Override
    public int hashCode() {
        return area.hashCode();
    }

    public Path getBoundaryPath() {
        Path path = new Path();
        path.tartineSetShape(area);
        return path;
    }

    public boolean getBoundaryPath(Path arg0) {
        if (arg0 == null || area.isEmpty()) {
            return false;
        }
        arg0.tartineSetShape(area);
        return true;
    }

    public Rect getBounds() {
        Rect bounds = new Rect();
        getBounds(bounds);
        return bounds;
    }

    public boolean getBounds(Rect arg0) {
        if (area.isEmpty()) {
            arg0.setEmpty();
            return false;
        }
        Rectangle2D bounds = area.getBounds2D();
        // Skia's integer bounds are the smallest rectangle covering the
        // fractional edges, so partial pixels stay inside.
        arg0.set((int) Math.floor(bounds.getX()),
                (int) Math.floor(bounds.getY()),
                (int) Math.ceil(bounds.getX() + bounds.getWidth()),
                (int) Math.ceil(bounds.getY() + bounds.getHeight()));
        return true;
    }

    public boolean isComplex() {
        return !isRect();
    }

    public boolean isEmpty() {
        return area.isEmpty();
    }

    public boolean isRect() {
        // Deviation: exact recognition only (see the class note).
        return !area.isEmpty() && area.equals(areaOf(getBounds()));
    }

    public boolean op(Rect arg0, Region arg1, Region.Op arg2) {
        return combine(new Region(arg0), arg1, arg2);
    }

    public boolean op(Rect arg0, Region.Op arg1) {
        return combine(new Region(arg0), this, arg1);
    }

    public boolean op(Region arg0, Region arg1, Region.Op arg2) {
        return combine(arg0, arg1, arg2);
    }

    public boolean op(Region arg0, Region.Op arg1) {
        return combine(this, arg0, arg1);
    }

    public boolean op(int arg0, int arg1, int arg2, int arg3,
            Region.Op arg4) {
        return combine(new Region(arg0, arg1, arg2, arg3), this, arg4);
    }

    /** Sets this region to {@code arg0 op arg1}. */
    private boolean combine(Region arg0, Region arg1, Region.Op arg2) {
        if (arg0 == null || arg1 == null) {
            return false;
        }
        Area result = new Area(arg0.area);
        if (!applyOp(result, arg1.area,
                arg2 == null ? Op.REPLACE : arg2)) {
            return false;
        }
        area = result;
        return true;
    }

    private static boolean applyOp(Area target, Area other, Op op) {
        switch (op) {
            case DIFFERENCE:
                target.subtract(other);
                return true;
            case INTERSECT:
                target.intersect(other);
                return true;
            case REPLACE:
                target.reset();
                target.add(other);
                return true;
            case REVERSE_DIFFERENCE:
                Area reversed = new Area(other);
                reversed.subtract(target);
                target.reset();
                target.add(reversed);
                return true;
            case UNION:
                target.add(other);
                return true;
            case XOR:
                target.exclusiveOr(other);
                return true;
            default:
                return false;
        }
    }

    public boolean quickContains(Rect arg0) {
        if (arg0 == null) {
            return false;
        }
        return quickContains(arg0.left, arg0.top, arg0.right, arg0.bottom);
    }

    public boolean quickContains(int arg0, int arg1, int arg2, int arg3) {
        // AOSP answers true only when the region is a single rectangle that
        // contains the query, and false for an empty region.
        return isRect() && containsRect(arg0, arg1, arg2, arg3);
    }

    public boolean quickReject(Rect arg0) {
        if (arg0 == null) {
            return false;
        }
        return quickReject(arg0.left, arg0.top, arg0.right, arg0.bottom);
    }

    public boolean quickReject(Region arg0) {
        if (arg0 == null || arg0.isEmpty() || isEmpty()) {
            return false;
        }
        Area overlap = new Area(area);
        overlap.intersect(arg0.area);
        return overlap.isEmpty();
    }

    public boolean quickReject(int arg0, int arg1, int arg2, int arg3) {
        Area overlap = new Area(area);
        overlap.intersect(areaOf(new Rect(arg0, arg1, arg2, arg3)));
        return overlap.isEmpty();
    }

    public boolean set(Rect arg0) {
        if (arg0 == null) {
            setEmpty();
        } else {
            set(arg0.left, arg0.top, arg0.right, arg0.bottom);
        }
        return true;
    }

    public boolean set(Region arg0) {
        if (arg0 == null) {
            setEmpty();
        } else {
            area = new Area(arg0.area);
        }
        return true;
    }

    public boolean set(int arg0, int arg1, int arg2, int arg3) {
        if (arg2 > arg0 && arg3 > arg1) {
            area = areaOf(new Rect(arg0, arg1, arg2, arg3));
        } else {
            setEmpty();
        }
        return true;
    }

    public void setEmpty() {
        area = new Area();
    }

    public boolean setPath(Path arg0, Region arg1) {
        if (arg0 == null) {
            setEmpty();
            return false;
        }
        Area result = new Area(arg0.tartineShape());
        if (arg1 != null) {
            result.intersect(arg1.area);
        }
        if (result.isEmpty()) {
            setEmpty();
            return false;
        }
        area = result;
        return true;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "Region(empty)";
        }
        return "Region(" + getBounds() + ")";
    }

    public void translate(int arg0, int arg1) {
        translate(arg0, arg1, this);
    }

    public void translate(int arg0, int arg1, Region arg2) {
        if (arg0 == 0 && arg1 == 0) {
            if (arg2 != this) {
                arg2.set(this);
            }
            return;
        }
        Path2D.Double shift = new Path2D.Double();
        shift.append(area, false);
        shift.transform(java.awt.geom.AffineTransform
                .getTranslateInstance(arg0, arg1));
        Area moved = new Area(shift);
        if (arg2 == this) {
            area = moved;
        } else {
            arg2.area = moved;
        }
    }

    public final boolean union(Rect arg0) {
        if (arg0 == null) {
            return false;
        }
        area.add(areaOf(arg0));
        return true;
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        // AOSP serializes the region bounds.
        Rect bounds = getBounds();
        arg0.writeInt(bounds.left);
        arg0.writeInt(bounds.top);
        arg0.writeInt(bounds.right);
        arg0.writeInt(bounds.bottom);
    }

    private boolean containsRect(int left, int top, int right, int bottom) {
        if (right <= left || bottom <= top) {
            return false;
        }
        return containsAll(areaOf(new Rect(left, top, right, bottom)));
    }

    private boolean containsAll(Area query) {
        Area remainder = new Area(query);
        remainder.subtract(area);
        return remainder.isEmpty();
    }

    private static Area areaOf(Rect rect) {
        Area area = new Area();
        if (rect.right > rect.left && rect.bottom > rect.top) {
            area.add(new Area(new Rectangle2D.Float(rect.left, rect.top,
                    rect.right - rect.left, rect.bottom - rect.top)));
        }
        return area;
    }

    public static enum Op {
        DIFFERENCE, INTERSECT, REPLACE, REVERSE_DIFFERENCE, UNION, XOR;
    }
}
