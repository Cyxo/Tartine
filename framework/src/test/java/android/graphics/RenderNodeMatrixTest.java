package android.graphics;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * RenderNode transform-matrix pins. AOSP composes the 2D leg as
 * {@code T(translation) * T(pivot) * R(rotationZ) * S(scale) * T(-pivot)};
 * Compose's ViewLayer reads it through View.getMatrix for hit testing and
 * layer bounds (ADR-0014).
 */
class RenderNodeMatrixTest {

    @Test
    void freshNodeReportsIdentity() {
        RenderNode node = new RenderNode("test");
        Matrix matrix = new Matrix();

        node.getMatrix(matrix);

        assertTrue(node.hasIdentityMatrix());
        assertTrue(matrix.isIdentity());
    }

    @Test
    void translationMovesTheOrigin() {
        RenderNode node = new RenderNode("test");
        node.setTranslationX(30f);
        node.setTranslationY(40f);
        Matrix matrix = new Matrix();
        float[] point = {0f, 0f};

        node.getMatrix(matrix);
        matrix.mapPoints(point);

        assertFalse(node.hasIdentityMatrix());
        assertArrayEquals(new float[] {30f, 40f}, point, 0f);
    }

    @Test
    void scaleGrowsAboutThePivot() {
        RenderNode node = new RenderNode("test");
        node.setPivotX(10f);
        node.setPivotY(10f);
        node.setScaleX(2f);
        node.setScaleY(3f);
        Matrix matrix = new Matrix();

        node.getMatrix(matrix);

        // The pivot is fixed; a point one unit away scales by the factor.
        float[] pivot = {10f, 10f};
        matrix.mapPoints(pivot);
        assertArrayEquals(new float[] {10f, 10f}, pivot, 1e-5f);

        float[] offset = {11f, 11f};
        matrix.mapPoints(offset);
        assertArrayEquals(new float[] {12f, 13f}, offset, 1e-5f);
    }

    @Test
    void inverseMatrixRoundTripsPoint() {
        RenderNode node = new RenderNode("test");
        node.setPivotX(5f);
        node.setPivotY(5f);
        node.setRotationZ(30f);
        node.setScaleX(2f);
        node.setScaleY(2f);
        node.setTranslationX(7f);
        node.setTranslationY(-3f);

        Matrix forward = new Matrix();
        Matrix inverse = new Matrix();
        node.getMatrix(forward);
        node.getInverseMatrix(inverse);

        float[] point = {3f, 4f};
        forward.mapPoints(point);
        inverse.mapPoints(point);

        assertArrayEquals(new float[] {3f, 4f}, point, 1e-4f);
    }
}
