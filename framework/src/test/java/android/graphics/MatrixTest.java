package android.graphics;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Affine-math pins for the Matrix port. Compose's pointer path builds its
 * window matrix from these operations (ADR-0014), so the composition order
 * (pre vs post) and the mapping itself are the contract.
 */
class MatrixTest {

    @Test
    void newMatrixIsIdentity() {
        Matrix matrix = new Matrix();

        assertTrue(matrix.isIdentity());
        assertArrayEquals(new float[] {1, 0, 0, 0, 1, 0, 0, 0, 1},
                valuesOf(matrix), 0f);
    }

    @Test
    void setTranslateMovesPoints() {
        Matrix matrix = new Matrix();
        matrix.setTranslate(3f, 4f);
        float[] point = {1f, 1f};

        matrix.mapPoints(point);

        assertArrayEquals(new float[] {4f, 5f}, point, 0f);
    }

    @Test
    void postTranslateAppliesBeforeTheExistingTransform() {
        Matrix matrix = new Matrix();
        matrix.setScale(2f, 2f);
        matrix.postTranslate(1f, 1f);
        float[] point = {1f, 0f};

        matrix.mapPoints(point);

        // postTranslate acts in the pre-existing input space: (1,0) is
        // shifted by (1,1) and only then scaled by 2 -> (4,2).
        assertArrayEquals(new float[] {4f, 2f}, point, 0f);
    }

    @Test
    void preTranslateAppliesAfterTheExistingTransform() {
        Matrix matrix = new Matrix();
        matrix.setScale(2f, 2f);
        matrix.preTranslate(1f, 1f);
        float[] point = {1f, 0f};

        matrix.mapPoints(point);

        assertArrayEquals(new float[] {3f, 1f}, point, 0f);
    }

    @Test
    void invertRoundTripsPoint() {
        Matrix matrix = new Matrix();
        matrix.setTranslate(3f, 4f);
        matrix.postScale(2f, 2f);
        Matrix inverse = new Matrix();
        float[] point = {5f, 7f};
        matrix.mapPoints(point);

        assertTrue(matrix.invert(inverse));
        inverse.mapPoints(point);

        assertArrayEquals(new float[] {5f, 7f}, point, 1e-4f);
    }

    @Test
    void mapRectBoundsQuarterTurn() {
        Matrix matrix = new Matrix();
        // Exact quarter turn: cos 0, sin 1, so rectStaysRect stays true.
        matrix.setSinCos(1f, 0f);
        RectF rect = new RectF(0f, 0f, 2f, 1f);

        assertTrue(matrix.mapRect(rect));
        assertEquals(-1f, rect.left, 1e-5f);
        assertEquals(0f, rect.top, 1e-5f);
        assertEquals(0f, rect.right, 1e-5f);
        assertEquals(2f, rect.bottom, 1e-5f);
    }

    @Test
    void setValuesRoundTripsThroughGetValues() {
        Matrix matrix = new Matrix();
        matrix.setRotate(30f);
        Matrix copy = new Matrix();

        copy.setValues(valuesOf(matrix));

        assertEquals(matrix, copy);
        assertEquals(matrix.hashCode(), copy.hashCode());
        assertFalse(matrix.isIdentity());
    }

    private static float[] valuesOf(Matrix matrix) {
        float[] values = new float[9];
        matrix.getValues(values);
        return values;
    }
}
