package android.graphics;

import java.io.PrintWriter;
import java.util.Arrays;

    public class Matrix {

    public static final Matrix IDENTITY_MATRIX = new Matrix();
    public static final int MPERSP_0 = 6;
    public static final int MPERSP_1 = 7;
    public static final int MPERSP_2 = 8;
    public static final int MSCALE_X = 0;
    public static final int MSCALE_Y = 4;
    public static final int MSKEW_X = 1;
    public static final int MSKEW_Y = 3;
    public static final int MTRANS_X = 2;
    public static final int MTRANS_Y = 5;

    /**
     * Row-major 3x3 matrix, the same layout Skia and the platform use:
     * {@code [MSCALE_X MSKEW_X MTRANS_X; MSKEW_Y MSCALE_Y MTRANS_Y; MPERSP_0
     * MPERSP_1 MPERSP_2]}. A point maps as {@code x' = m[0]*x + m[1]*y +
     * m[2]}, {@code y' = m[3]*x + m[4]*y + m[5]}, the pair divided by the
     * perspective row {@code m[6]*x + m[7]*y + m[8]} when it is not
     * {@code (0, 0, 1)}.
     */
    private final float[] mValues = new float[9];

    public Matrix() {
        reset();
    }

    public Matrix(Matrix arg0) {
        set(arg0);
    }

    public final void dump(PrintWriter arg0) {
        arg0.println(toString());
    }

    public boolean equals(Object arg0) {
        return arg0 instanceof Matrix other
                && Arrays.equals(mValues, other.mValues);
    }

    public void getValues(float[] arg0) {
        System.arraycopy(mValues, 0, arg0, 0, 9);
    }

    public int hashCode() {
        return Arrays.hashCode(mValues);
    }

    public boolean invert(Matrix arg0) {
        float[] inverse = new float[9];
        if (!invertValues(mValues, inverse)) {
            return false;
        }
        if (arg0 != null) {
            arg0.setValues(inverse);
        }
        return true;
    }

    public boolean isAffine() {
        return mValues[MPERSP_0] == 0f && mValues[MPERSP_1] == 0f
                && mValues[MPERSP_2] == 1f;
    }

    public boolean isIdentity() {
        return mValues[MSCALE_X] == 1f && mValues[MSKEW_X] == 0f
                && mValues[MTRANS_X] == 0f && mValues[MSKEW_Y] == 0f
                && mValues[MSCALE_Y] == 1f && mValues[MTRANS_Y] == 0f
                && mValues[MPERSP_0] == 0f && mValues[MPERSP_1] == 0f
                && mValues[MPERSP_2] == 1f;
    }

    public void mapPoints(float[] arg0) {
        mapPoints(arg0, 0, arg0, 0, arg0.length >> 1);
    }

    public void mapPoints(float[] arg0, float[] arg1) {
        mapPoints(arg0, 0, arg1, 0, arg1.length >> 1);
    }

    public void mapPoints(float[] arg0, int arg1, float[] arg2, int arg3,
            int arg4) {
        for (int i = 0; i < arg4; i++) {
            float x = arg2[arg3];
            float y = arg2[arg3 + 1];
            float px = mValues[0] * x + mValues[1] * y + mValues[2];
            float py = mValues[3] * x + mValues[4] * y + mValues[5];
            float w = mValues[6] * x + mValues[7] * y + mValues[8];
            if (w != 1f && w != 0f) {
                float iw = 1f / w;
                px *= iw;
                py *= iw;
            }
            arg0[arg1] = px;
            arg0[arg1 + 1] = py;
            arg1 += 2;
            arg3 += 2;
        }
    }

    public float mapRadius(float arg0) {
        // Skia SkMatrix::mapRadius: the geometric mean of the mapped radius
        // vectors (vec[0] = (r,0), vec[1] = (0,r)).
        float[] vec = {arg0, 0f, 0f, arg0};
        mapVectors(vec);
        double d0 = Math.hypot(vec[0], vec[1]);
        double d1 = Math.hypot(vec[2], vec[3]);
        return (float) Math.sqrt(d0 * d1);
    }

    public boolean mapRect(RectF arg0) {
        return mapRect(arg0, arg0);
    }

    public boolean mapRect(RectF arg0, RectF arg1) {
        float[] pts = {arg1.left, arg1.top, arg1.right, arg1.top,
                arg1.right, arg1.bottom, arg1.left, arg1.bottom};
        mapPoints(pts);
        float minX = Math.min(Math.min(pts[0], pts[2]),
                Math.min(pts[4], pts[6]));
        float maxX = Math.max(Math.max(pts[0], pts[2]),
                Math.max(pts[4], pts[6]));
        float minY = Math.min(Math.min(pts[1], pts[3]),
                Math.min(pts[5], pts[7]));
        float maxY = Math.max(Math.max(pts[1], pts[3]),
                Math.max(pts[5], pts[7]));
        arg0.set(minX, minY, maxX, maxY);
        return rectStaysRect();
    }

    public void mapVectors(float[] arg0) {
        mapVectors(arg0, 0, arg0, 0, arg0.length >> 1);
    }

    public void mapVectors(float[] arg0, float[] arg1) {
        mapVectors(arg0, 0, arg1, 0, arg1.length >> 1);
    }

    public void mapVectors(float[] arg0, int arg1, float[] arg2, int arg3,
            int arg4) {
        for (int i = 0; i < arg4; i++) {
            float x = arg2[arg3];
            float y = arg2[arg3 + 1];
            arg0[arg1] = mValues[0] * x + mValues[1] * y;
            arg0[arg1 + 1] = mValues[3] * x + mValues[4] * y;
            arg1 += 2;
            arg3 += 2;
        }
    }

    // The pre*/post* families mirror Skia's SkMatrix naming exactly, which is
    // the opposite of the textbook convention:
    //   preConcat(other)  -> this = this * other   (multiplyOnRight)
    //   postConcat(other) -> this = other * this   (multiplyOnLeft)
    // With column-vector point mapping (p' = M * [x y 1]), "pre" operations
    // therefore act in the source space (applied before the existing
    // transform) and "post" operations in the destination space.

    public boolean postConcat(Matrix arg0) {
        multiplyOnLeft(arg0.mValues);
        return true;
    }

    public boolean postRotate(float arg0) {
        return postRotate(arg0, 0f, 0f);
    }

    public boolean postRotate(float arg0, float arg1, float arg2) {
        multiplyOnLeft(rotationValues(arg0, arg1, arg2));
        return true;
    }

    public boolean postScale(float arg0, float arg1) {
        multiplyOnLeft(scaleValues(arg0, arg1, 0f, 0f));
        return true;
    }

    public boolean postScale(float arg0, float arg1, float arg2, float arg3) {
        multiplyOnLeft(scaleValues(arg0, arg1, arg2, arg3));
        return true;
    }

    public boolean postSkew(float arg0, float arg1) {
        float[] skew = {1f, arg0, 0f, arg1, 1f, 0f, 0f, 0f, 1f};
        multiplyOnLeft(skew);
        return true;
    }

    public boolean postSkew(float arg0, float arg1, float arg2, float arg3) {
        float[] skew = {1f, arg0, -arg0 * arg3, arg1, 1f, -arg1 * arg2,
                0f, 0f, 1f};
        multiplyOnLeft(skew);
        return true;
    }

    /**
     * Skia {@code SkMatrix::postTranslate}: {@code this = T(dx, dy) * this},
     * the translation in the destination space (a plain add for an affine
     * matrix).
     */
    public boolean postTranslate(float arg0, float arg1) {
        float[] translate = {1f, 0f, arg0, 0f, 1f, arg1, 0f, 0f, 1f};
        multiplyOnLeft(translate);
        return true;
    }

    public boolean preConcat(Matrix arg0) {
        multiplyOnRight(arg0.mValues);
        return true;
    }

    public boolean preRotate(float arg0) {
        return preRotate(arg0, 0f, 0f);
    }

    public boolean preRotate(float arg0, float arg1, float arg2) {
        multiplyOnRight(rotationValues(arg0, arg1, arg2));
        return true;
    }

    public boolean preScale(float arg0, float arg1) {
        multiplyOnRight(scaleValues(arg0, arg1, 0f, 0f));
        return true;
    }

    public boolean preScale(float arg0, float arg1, float arg2, float arg3) {
        multiplyOnRight(scaleValues(arg0, arg1, arg2, arg3));
        return true;
    }

    public boolean preSkew(float arg0, float arg1) {
        float[] skew = {1f, arg0, 0f, arg1, 1f, 0f, 0f, 0f, 1f};
        multiplyOnRight(skew);
        return true;
    }

    public boolean preSkew(float arg0, float arg1, float arg2, float arg3) {
        float[] skew = {1f, arg0, -arg0 * arg3, arg1, 1f, -arg1 * arg2,
                0f, 0f, 1f};
        multiplyOnRight(skew);
        return true;
    }

    /**
     * Skia {@code SkMatrix::preTranslate}: {@code this = this * T(dx, dy)},
     * the translation in the matrix's source space (weighted by the current
     * scale/skew for an affine matrix).
     */
    public boolean preTranslate(float arg0, float arg1) {
        float[] translate = {1f, 0f, arg0, 0f, 1f, arg1, 0f, 0f, 1f};
        multiplyOnRight(translate);
        return true;
    }

    /** Row-major {@code R(degrees, px, py)} as Skia's setRotate builds it. */
    private static float[] rotationValues(float degrees, float px, float py) {
        float radians = (float) (degrees * Math.PI / 180.0);
        float sin = (float) Math.sin(radians);
        float cos = (float) Math.cos(radians);
        return new float[] {cos, -sin, px - px * cos + py * sin,
                sin, cos, py - px * sin - py * cos, 0f, 0f, 1f};
    }

    /** Row-major {@code S(sx, sy, px, py)} as Skia's setScale builds it. */
    private static float[] scaleValues(float sx, float sy, float px, float py) {
        return new float[] {sx, 0f, px - px * sx, 0f, sy, py - py * sy,
                0f, 0f, 1f};
    }

    public boolean rectStaysRect() {
        return mValues[MPERSP_0] == 0f && mValues[MPERSP_1] == 0f
                && ((mValues[MSCALE_X] == 0f && mValues[MSCALE_Y] == 0f
                        && mValues[MSKEW_X] != 0f && mValues[MSKEW_Y] != 0f)
                    || (mValues[MSCALE_X] != 0f && mValues[MSCALE_Y] != 0f
                        && mValues[MSKEW_X] == 0f && mValues[MSKEW_Y] == 0f));
    }

    public void reset() {
        mValues[MSCALE_X] = 1f;
        mValues[MSKEW_X] = 0f;
        mValues[MTRANS_X] = 0f;
        mValues[MSKEW_Y] = 0f;
        mValues[MSCALE_Y] = 1f;
        mValues[MTRANS_Y] = 0f;
        mValues[MPERSP_0] = 0f;
        mValues[MPERSP_1] = 0f;
        mValues[MPERSP_2] = 1f;
    }

    public void set(Matrix arg0) {
        System.arraycopy(arg0.mValues, 0, mValues, 0, 9);
    }

    public boolean setConcat(Matrix arg0, Matrix arg1) {
        multiply(arg0.mValues, arg1.mValues, mValues);
        return true;
    }

    public boolean setPolyToPoly(float[] arg0, int arg1, float[] arg2, int arg3,
            int arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->setPolyToPoly([FI[FII)Z");
    }

    public boolean setRectToRect(RectF arg0, RectF arg1,
            Matrix.ScaleToFit arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Matrix;->setRectToRect(Landroid/graphics/RectF;Landroid/graphics/RectF;Landroid/graphics/Matrix$ScaleToFit;)Z");
    }

    public void setRotate(float arg0) {
        setRotate(arg0, 0f, 0f);
    }

    public void setRotate(float arg0, float arg1, float arg2) {
        float radians = (float) (arg0 * Math.PI / 180.0);
        float sin = (float) Math.sin(radians);
        float cos = (float) Math.cos(radians);
        mValues[MSCALE_X] = cos;
        mValues[MSKEW_X] = -sin;
        mValues[MTRANS_X] = arg1 - arg1 * cos + arg2 * sin;
        mValues[MSKEW_Y] = sin;
        mValues[MSCALE_Y] = cos;
        mValues[MTRANS_Y] = arg2 - arg1 * sin - arg2 * cos;
        mValues[MPERSP_0] = 0f;
        mValues[MPERSP_1] = 0f;
        mValues[MPERSP_2] = 1f;
    }

    public void setScale(float arg0, float arg1) {
        mValues[MSCALE_X] = arg0;
        mValues[MSKEW_X] = 0f;
        mValues[MTRANS_X] = 0f;
        mValues[MSKEW_Y] = 0f;
        mValues[MSCALE_Y] = arg1;
        mValues[MTRANS_Y] = 0f;
        mValues[MPERSP_0] = 0f;
        mValues[MPERSP_1] = 0f;
        mValues[MPERSP_2] = 1f;
    }

    public void setScale(float arg0, float arg1, float arg2, float arg3) {
        mValues[MSCALE_X] = arg0;
        mValues[MSKEW_X] = 0f;
        mValues[MTRANS_X] = arg2 - arg2 * arg0;
        mValues[MSKEW_Y] = 0f;
        mValues[MSCALE_Y] = arg1;
        mValues[MTRANS_Y] = arg3 - arg3 * arg1;
        mValues[MPERSP_0] = 0f;
        mValues[MPERSP_1] = 0f;
        mValues[MPERSP_2] = 1f;
    }

    public void setSinCos(float arg0, float arg1) {
        mValues[MSCALE_X] = arg1;
        mValues[MSKEW_X] = -arg0;
        mValues[MTRANS_X] = 0f;
        mValues[MSKEW_Y] = arg0;
        mValues[MSCALE_Y] = arg1;
        mValues[MTRANS_Y] = 0f;
        mValues[MPERSP_0] = 0f;
        mValues[MPERSP_1] = 0f;
        mValues[MPERSP_2] = 1f;
    }

    public void setSinCos(float arg0, float arg1, float arg2, float arg3) {
        mValues[MSCALE_X] = arg1;
        mValues[MSKEW_X] = -arg0;
        mValues[MTRANS_X] = arg2 - arg2 * arg1 + arg3 * arg0;
        mValues[MSKEW_Y] = arg0;
        mValues[MSCALE_Y] = arg1;
        mValues[MTRANS_Y] = arg3 - arg2 * arg0 - arg3 * arg1;
        mValues[MPERSP_0] = 0f;
        mValues[MPERSP_1] = 0f;
        mValues[MPERSP_2] = 1f;
    }

    public void setSkew(float arg0, float arg1) {
        mValues[MSCALE_X] = 1f;
        mValues[MSKEW_X] = arg0;
        mValues[MTRANS_X] = 0f;
        mValues[MSKEW_Y] = arg1;
        mValues[MSCALE_Y] = 1f;
        mValues[MTRANS_Y] = 0f;
        mValues[MPERSP_0] = 0f;
        mValues[MPERSP_1] = 0f;
        mValues[MPERSP_2] = 1f;
    }

    public void setSkew(float arg0, float arg1, float arg2, float arg3) {
        mValues[MSCALE_X] = 1f;
        mValues[MSKEW_X] = arg0;
        mValues[MTRANS_X] = -arg0 * arg3;
        mValues[MSKEW_Y] = arg1;
        mValues[MSCALE_Y] = 1f;
        mValues[MTRANS_Y] = -arg1 * arg2;
        mValues[MPERSP_0] = 0f;
        mValues[MPERSP_1] = 0f;
        mValues[MPERSP_2] = 1f;
    }

    public void setTranslate(float arg0, float arg1) {
        mValues[MSCALE_X] = 1f;
        mValues[MSKEW_X] = 0f;
        mValues[MTRANS_X] = arg0;
        mValues[MSKEW_Y] = 0f;
        mValues[MSCALE_Y] = 1f;
        mValues[MTRANS_Y] = arg1;
        mValues[MPERSP_0] = 0f;
        mValues[MPERSP_1] = 0f;
        mValues[MPERSP_2] = 1f;
    }

    public void setValues(float[] arg0) {
        System.arraycopy(arg0, 0, mValues, 0, 9);
    }

    public String toShortString() {
        return "Matrix{[" + mValues[0] + ", " + mValues[1] + ", " + mValues[2]
                + "][" + mValues[3] + ", " + mValues[4] + ", " + mValues[5]
                + "][" + mValues[6] + ", " + mValues[7] + ", " + mValues[8]
                + "]}";
    }

    public String toString() {
        return toShortString();
    }

    /** {@code out = lhs * rhs} for two row-major 3x3 arrays. */
    private static void multiply(float[] lhs, float[] rhs, float[] out) {
        for (int i = 0; i < 9; i += 3) {
            for (int j = 0; j < 3; j++) {
                out[i + j] = lhs[i] * rhs[j] + lhs[i + 1] * rhs[3 + j]
                        + lhs[i + 2] * rhs[6 + j];
            }
        }
    }

    /**
     * {@code this = this * rhs} (Skia's {@code preConcat}); a copy keeps an
     * in-place product safe.
     */
    private void multiplyOnRight(float[] rhs) {
        float[] out = new float[9];
        multiply(mValues, rhs, out);
        System.arraycopy(out, 0, mValues, 0, 9);
    }

    /**
     * {@code this = lhs * this} (Skia's {@code postConcat}); a copy keeps an
     * in-place product safe.
     */
    private void multiplyOnLeft(float[] lhs) {
        float[] out = new float[9];
        multiply(lhs, mValues, out);
        System.arraycopy(out, 0, mValues, 0, 9);
    }

    /**
     * Writes the inverse of {@code m} into {@code out}. Returns false and
     * leaves {@code out} as the identity when {@code m} is singular.
     */
    private static boolean invertValues(float[] m, float[] out) {
        float a = m[0];
        float b = m[1];
        float c = m[2];
        float d = m[3];
        float e = m[4];
        float f = m[5];
        float g = m[6];
        float h = m[7];
        float i = m[8];
        float det = a * (e * i - f * h) - b * (d * i - f * g)
                + c * (d * h - e * g);
        if (det == 0f) {
            out[0] = 1f;
            out[1] = 0f;
            out[2] = 0f;
            out[3] = 0f;
            out[4] = 1f;
            out[5] = 0f;
            out[6] = 0f;
            out[7] = 0f;
            out[8] = 1f;
            return false;
        }
        float invDet = 1f / det;
        out[0] = (e * i - f * h) * invDet;
        out[1] = (c * h - b * i) * invDet;
        out[2] = (b * f - c * e) * invDet;
        out[3] = (f * g - d * i) * invDet;
        out[4] = (a * i - c * g) * invDet;
        out[5] = (c * d - a * f) * invDet;
        out[6] = (d * h - e * g) * invDet;
        out[7] = (b * g - a * h) * invDet;
        out[8] = (a * e - b * d) * invDet;
        return true;
    }

        public static enum ScaleToFit {
            CENTER, END, FILL, START;
        }
    }
