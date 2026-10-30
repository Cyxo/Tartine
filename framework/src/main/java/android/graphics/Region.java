package android.graphics;

import android.os.Parcel;
import android.os.Parcelable;

    public class Region implements Parcelable {

    public static Parcelable.Creator CREATOR;

    public Region() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;-><init>()V");
    }

    public Region(Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;-><init>(Landroid/graphics/Rect;)V");
    }

    public Region(Region arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;-><init>(Landroid/graphics/Region;)V");
    }

    public Region(int arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;-><init>(IIII)V");
    }

    public boolean contains(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;->contains(II)Z");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;->describeContents()I");
    }

    public boolean equals(Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;->equals(Ljava/lang/Object;)Z");
    }

    protected void finalize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;->finalize()V");
    }

    public Path getBoundaryPath() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;->getBoundaryPath()Landroid/graphics/Path;");
    }

    public boolean getBoundaryPath(Path arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;->getBoundaryPath(Landroid/graphics/Path;)Z");
    }

    public Rect getBounds() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;->getBounds()Landroid/graphics/Rect;");
    }

    public boolean getBounds(Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;->getBounds(Landroid/graphics/Rect;)Z");
    }

    public boolean isComplex() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;->isComplex()Z");
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;->isEmpty()Z");
    }

    public boolean isRect() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;->isRect()Z");
    }

    public boolean op(Rect arg0, Region arg1, Region.Op arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;->op(Landroid/graphics/Rect;Landroid/graphics/Region;Landroid/graphics/Region$Op;)Z");
    }

    public boolean op(Rect arg0, Region.Op arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;->op(Landroid/graphics/Rect;Landroid/graphics/Region$Op;)Z");
    }

    public boolean op(Region arg0, Region arg1, Region.Op arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;->op(Landroid/graphics/Region;Landroid/graphics/Region;Landroid/graphics/Region$Op;)Z");
    }

    public boolean op(Region arg0, Region.Op arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;->op(Landroid/graphics/Region;Landroid/graphics/Region$Op;)Z");
    }

    public boolean op(int arg0, int arg1, int arg2, int arg3, Region.Op arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;->op(IIIILandroid/graphics/Region$Op;)Z");
    }

    public boolean quickContains(Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;->quickContains(Landroid/graphics/Rect;)Z");
    }

    public boolean quickContains(int arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;->quickContains(IIII)Z");
    }

    public boolean quickReject(Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;->quickReject(Landroid/graphics/Rect;)Z");
    }

    public boolean quickReject(Region arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;->quickReject(Landroid/graphics/Region;)Z");
    }

    public boolean quickReject(int arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;->quickReject(IIII)Z");
    }

    public boolean set(Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;->set(Landroid/graphics/Rect;)Z");
    }

    public boolean set(Region arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;->set(Landroid/graphics/Region;)Z");
    }

    public boolean set(int arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;->set(IIII)Z");
    }

    public void setEmpty() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;->setEmpty()V");
    }

    public boolean setPath(Path arg0, Region arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;->setPath(Landroid/graphics/Path;Landroid/graphics/Region;)Z");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;->toString()Ljava/lang/String;");
    }

    public void translate(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;->translate(II)V");
    }

    public void translate(int arg0, int arg1, Region arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;->translate(IILandroid/graphics/Region;)V");
    }

    public final boolean union(Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;->union(Landroid/graphics/Rect;)Z");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Region;->writeToParcel(Landroid/os/Parcel;I)V");
    }
        public static enum Op {
            DIFFERENCE, INTERSECT, REPLACE, REVERSE_DIFFERENCE, UNION, XOR;
        }
    }
