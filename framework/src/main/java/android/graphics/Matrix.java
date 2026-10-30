package android.graphics;

import java.io.PrintWriter;

    public class Matrix {

    public static Matrix IDENTITY_MATRIX;
    public static final int MPERSP_0 = 6;
    public static final int MPERSP_1 = 7;
    public static final int MPERSP_2 = 8;
    public static final int MSCALE_X = 0;
    public static final int MSCALE_Y = 4;
    public static final int MSKEW_X = 1;
    public static final int MSKEW_Y = 3;
    public static final int MTRANS_X = 2;
    public static final int MTRANS_Y = 5;

    public Matrix() {
        // AOSP frameworks-base core/java/android/graphics/Matrix.java#Matrix():
        // constructs the identity matrix; the shell Matrix carries no
        // transform state (no mArray field is ported yet), so an empty
        // body is the identity by convention.
    }

    public Matrix(Matrix arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;-><init>(Landroid/graphics/Matrix;)V");
    }

    public final void dump(PrintWriter arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->dump(Ljava/io/PrintWriter;)V");
    }

    public boolean equals(Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->equals(Ljava/lang/Object;)Z");
    }

    public void getValues(float[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->getValues([F)V");
    }

    public int hashCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->hashCode()I");
    }

    public boolean invert(Matrix arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->invert(Landroid/graphics/Matrix;)Z");
    }

    public boolean isAffine() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->isAffine()Z");
    }

    public boolean isIdentity() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->isIdentity()Z");
    }

    public void mapPoints(float[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->mapPoints([F)V");
    }

    public void mapPoints(float[] arg0, float[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->mapPoints([F[F)V");
    }

    public void mapPoints(float[] arg0, int arg1, float[] arg2, int arg3, int arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->mapPoints([FI[FII)V");
    }

    public float mapRadius(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->mapRadius(F)F");
    }

    public boolean mapRect(RectF arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->mapRect(Landroid/graphics/RectF;)Z");
    }

    public boolean mapRect(RectF arg0, RectF arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->mapRect(Landroid/graphics/RectF;Landroid/graphics/RectF;)Z");
    }

    public void mapVectors(float[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->mapVectors([F)V");
    }

    public void mapVectors(float[] arg0, float[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->mapVectors([F[F)V");
    }

    public void mapVectors(float[] arg0, int arg1, float[] arg2, int arg3, int arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->mapVectors([FI[FII)V");
    }

    public boolean postConcat(Matrix arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->postConcat(Landroid/graphics/Matrix;)Z");
    }

    public boolean postRotate(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->postRotate(F)Z");
    }

    public boolean postRotate(float arg0, float arg1, float arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->postRotate(FFF)Z");
    }

    public boolean postScale(float arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->postScale(FF)Z");
    }

    public boolean postScale(float arg0, float arg1, float arg2, float arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->postScale(FFFF)Z");
    }

    public boolean postSkew(float arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->postSkew(FF)Z");
    }

    public boolean postSkew(float arg0, float arg1, float arg2, float arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->postSkew(FFFF)Z");
    }

    public boolean postTranslate(float arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->postTranslate(FF)Z");
    }

    public boolean preConcat(Matrix arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->preConcat(Landroid/graphics/Matrix;)Z");
    }

    public boolean preRotate(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->preRotate(F)Z");
    }

    public boolean preRotate(float arg0, float arg1, float arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->preRotate(FFF)Z");
    }

    public boolean preScale(float arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->preScale(FF)Z");
    }

    public boolean preScale(float arg0, float arg1, float arg2, float arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->preScale(FFFF)Z");
    }

    public boolean preSkew(float arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->preSkew(FF)Z");
    }

    public boolean preSkew(float arg0, float arg1, float arg2, float arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->preSkew(FFFF)Z");
    }

    public boolean preTranslate(float arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->preTranslate(FF)Z");
    }

    public boolean rectStaysRect() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->rectStaysRect()Z");
    }

    public void reset() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->reset()V");
    }

    public void set(Matrix arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->set(Landroid/graphics/Matrix;)V");
    }

    public boolean setConcat(Matrix arg0, Matrix arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->setConcat(Landroid/graphics/Matrix;Landroid/graphics/Matrix;)Z");
    }

    public boolean setPolyToPoly(float[] arg0, int arg1, float[] arg2, int arg3, int arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->setPolyToPoly([FI[FII)Z");
    }

    public boolean setRectToRect(RectF arg0, RectF arg1, Matrix.ScaleToFit arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->setRectToRect(Landroid/graphics/RectF;Landroid/graphics/RectF;Landroid/graphics/Matrix$ScaleToFit;)Z");
    }

    public void setRotate(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->setRotate(F)V");
    }

    public void setRotate(float arg0, float arg1, float arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->setRotate(FFF)V");
    }

    public void setScale(float arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->setScale(FF)V");
    }

    public void setScale(float arg0, float arg1, float arg2, float arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->setScale(FFFF)V");
    }

    public void setSinCos(float arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->setSinCos(FF)V");
    }

    public void setSinCos(float arg0, float arg1, float arg2, float arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->setSinCos(FFFF)V");
    }

    public void setSkew(float arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->setSkew(FF)V");
    }

    public void setSkew(float arg0, float arg1, float arg2, float arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->setSkew(FFFF)V");
    }

    public void setTranslate(float arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->setTranslate(FF)V");
    }

    public void setValues(float[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->setValues([F)V");
    }

    public String toShortString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->toShortString()Ljava/lang/String;");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->toString()Ljava/lang/String;");
    }
        public static enum ScaleToFit {
            CENTER, END, FILL, START;
        }
    }
