package android.graphics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * AOSP parity for Paint's pure-Java font metric value classes. Source:
 * aosp-mirror/platform_frameworks_base
 * graphics/java/android/graphics/Paint.java (FontMetrics at :2320,
 * FontMetricsInt at :2540; set(FontMetrics) uses the
 * GraphicsJNI::set_metrics_int floor/round/ceil conversion).
 */
class FontMetricsTest {

    @Test
    void fontMetricsDefaultsAreZero() {
        Paint.FontMetrics fm = new Paint.FontMetrics();
        assertEquals(0.0f, fm.top);
        assertEquals(0.0f, fm.ascent);
        assertEquals(0.0f, fm.descent);
        assertEquals(0.0f, fm.bottom);
        assertEquals(0.0f, fm.leading);
    }

    @Test
    void fontMetricsEqualsHashCodeAndToString() {
        Paint.FontMetrics a = new Paint.FontMetrics();
        a.top = -17.0f;
        a.ascent = -15.0f;
        a.descent = 4.0f;
        a.bottom = 5.0f;
        a.leading = 1.0f;

        Paint.FontMetrics b = new Paint.FontMetrics();
        b.top = -17.0f;
        b.ascent = -15.0f;
        b.descent = 4.0f;
        b.bottom = 5.0f;
        b.leading = 1.0f;

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, new Paint.FontMetrics());
        assertNotEquals(a, null);
        assertEquals("FontMetrics{top=-17.0, ascent=-15.0, descent=4.0,"
                + " bottom=5.0, leading=1.0}", a.toString());
    }

    @Test
    void fontMetricsIntDefaultsAreZero() {
        Paint.FontMetricsInt fm = new Paint.FontMetricsInt();
        assertEquals(0, fm.top);
        assertEquals(0, fm.ascent);
        assertEquals(0, fm.descent);
        assertEquals(0, fm.bottom);
        assertEquals(0, fm.leading);
    }

    @Test
    void fontMetricsIntSetFromFloatMetricsRoundsLikeAosp() {
        Paint.FontMetrics source = new Paint.FontMetrics();
        source.top = -17.6f;
        source.ascent = -15.4f;
        source.descent = 4.5f;
        source.bottom = 5.2f;
        source.leading = 0.5f;

        Paint.FontMetricsInt target = new Paint.FontMetricsInt();
        target.set(source);
        assertEquals(-18, target.top);       // floor
        assertEquals(-15, target.ascent);    // round
        assertEquals(5, target.descent);     // round
        assertEquals(6, target.bottom);      // ceil
        assertEquals(1, target.leading);     // round
    }

    @Test
    void fontMetricsIntSetFromIntMetricsCopiesFields() {
        Paint.FontMetricsInt source = new Paint.FontMetricsInt();
        source.top = -20;
        source.ascent = -16;
        source.descent = 5;
        source.bottom = 7;
        source.leading = 2;

        Paint.FontMetricsInt target = new Paint.FontMetricsInt();
        target.set(source);
        assertEquals(source, target);
    }

    @Test
    void fontMetricsIntEqualsHashCodeAndToString() {
        Paint.FontMetricsInt a = new Paint.FontMetricsInt();
        a.top = -17;
        a.ascent = -15;
        a.descent = 4;
        a.bottom = 5;
        a.leading = 0;

        Paint.FontMetricsInt b = new Paint.FontMetricsInt();
        b.set(a);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, new Paint.FontMetricsInt());
        assertNotEquals(a, null);
        assertEquals("FontMetricsInt: top=-17 ascent=-15 descent=4"
                + " bottom=5 leading=0", a.toString());
    }
}
