package android.view;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import android.graphics.Matrix;
import android.os.Looper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * {@code transformMatrixToGlobal}/{@code Local} pins: Compose's
 * {@code CalculateMatrixToWindowApi29} builds its window matrix by walking
 * the view tree through these, so the ancestor-offset accumulation is the
 * contract (ADR-0014).
 */
class ViewTransformTest {

    @BeforeAll
    static void prepareMainLooper() {
        if (Looper.getMainLooper() == null) {
            Looper.prepareMainLooper();
        }
    }

    @Test
    void toGlobalAccumulatesAncestorOffsets() {
        ViewRootImpl root = new ViewRootImpl(null);
        DecorView decor = new DecorView(null);
        View child = new View(null);
        decor.addView(child);
        root.setView(decor);
        decor.layout(5, 7, 105, 107);
        child.layout(10, 20, 40, 50);

        Matrix toGlobal = new Matrix();
        child.transformMatrixToGlobal(toGlobal);
        float[] point = {0f, 0f};
        toGlobal.mapPoints(point);
        assertArrayEquals(new float[] {15f, 27f}, point, 0f);

        Matrix toLocal = new Matrix();
        child.transformMatrixToLocal(toLocal);
        toLocal.mapPoints(point);
        assertArrayEquals(new float[] {0f, 0f}, point, 0f);
    }
}
