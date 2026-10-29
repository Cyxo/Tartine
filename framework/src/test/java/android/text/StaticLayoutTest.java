package android.text;

import android.graphics.Paint;
import android.graphics.Rect;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * AOSP line algebra: StaticLayout.generate stores
 * {@code above}/{@code below} from the font metrics (ascent/descent, or
 * top/bottom with includePad), folds line spacing into the descent, and
 * Layout.getLineBaseline is {@code getLineTop(line+1) - getLineDescent(line)}.
 * Source: aosp-mirror/platform_frameworks_base
 * core/java/android/text/StaticLayout.java (generate) and
 * core/java/android/text/Layout.java (getHeight/getLineBaseline).
 *
 * <p>FixedPaint keeps the assertions independent of the host's installed
 * fonts; the framework module has no Skija platform natives on its test
 * classpath, so a native touch would fail loudly.
 */
class StaticLayoutTest {

    /** TextPaint with fixed metrics so the assertions do not depend on the
     *  host's installed fonts. */
    private static final class FixedPaint extends TextPaint {
        private final float textSize;
        private final float ascent;
        private final float descent;
        private final float top;
        private final float bottom;

        FixedPaint() {
            this(16.0f, -15.0f, 4.0f, -17.0f, 5.0f);
        }

        FixedPaint(float textSize, float ascent, float descent,
                float top, float bottom) {
            this.textSize = textSize;
            this.ascent = ascent;
            this.descent = descent;
            this.top = top;
            this.bottom = bottom;
        }

        @Override
        public float getTextSize() {
            return textSize;
        }

        @Override
        public Paint.FontMetrics getFontMetrics() {
            Paint.FontMetrics fm = new Paint.FontMetrics();
            fm.ascent = ascent;
            fm.descent = descent;
            fm.top = top;
            fm.bottom = bottom;
            fm.leading = 0.0f;
            return fm;
        }

        @Override
        public float measureText(String text) {
            return text.length() * 6.0f;
        }
    }

    private static StaticLayout build(boolean includePad, float spacingAdd) {
        return build("hi", 0, 2, includePad, spacingAdd, 1.0f, new FixedPaint());
    }

    private static StaticLayout build(String text, int start, int end,
            boolean includePad, float spacingAdd, float spacingMul,
            TextPaint paint) {
        return StaticLayout.Builder
                .obtain(text, start, end, paint, 100)
                .setIncludePad(includePad)
                .setLineSpacing(spacingAdd, spacingMul)
                .build();
    }

    @Test
    void noPaddingLineMetricsFollowAosp() {
        StaticLayout layout = build(false, 0.0f);
        assertEquals(1, layout.getLineCount());
        assertEquals(100, layout.getWidth());
        assertEquals(19, layout.getHeight());          // descent - ascent
        assertEquals(15, layout.getLineBaseline(0));   // -ascent
        assertEquals(4, layout.getLineDescent(0));
        assertEquals(-15, layout.getLineAscent(0));
        assertEquals(0, layout.getLineTop(0));
        assertEquals(19, layout.getLineTop(1));
        assertEquals(19, layout.getLineBottom(0));
    }

    @Test
    void includePadUsesTopAndBottom() {
        StaticLayout layout = build(true, 0.0f);
        assertEquals(22, layout.getHeight());          // bottom - top
        assertEquals(17, layout.getLineBaseline(0));   // -top
        assertEquals(5, layout.getLineDescent(0));
        assertEquals(-17, layout.getLineAscent(0));
    }

    @Test
    void lineSpacingFoldsIntoHeightNotBaseline() {
        StaticLayout layout = build(false, 6.0f);
        assertEquals(25, layout.getHeight());          // 19 + 6
        assertEquals(15, layout.getLineBaseline(0));   // baseline stays -ascent
        assertEquals(10, layout.getLineDescent(0));    // descent + extra
        assertEquals(6, layout.getLineExtra(0));
        // AOSP Layout.java:4368/4395: the spacing accessors read the stored
        // multiplier/addition (getSpacingAdd is an alias of getLineSpacingAmount).
        assertEquals(6.0f, layout.getSpacingAdd());
        assertEquals(6.0f, layout.getLineSpacingAmount());
        assertEquals(1.0f, layout.getSpacingMultiplier());
        assertEquals(1.0f, layout.getLineSpacingMultiplier());
    }

    @Test
    void emptyTextHasNoLines() {
        StaticLayout layout = StaticLayout.Builder
                .obtain("", 0, 0, new FixedPaint(), 100)
                .build();
        assertEquals(0, layout.getLineCount());
        assertEquals(0, layout.getHeight());
    }

    @Test
    void nullSourceBehavesLikeEmptyText() {
        StaticLayout layout = StaticLayout.Builder
                .obtain(null, 0, 0, new FixedPaint(), 100)
                .build();
        assertEquals(0, layout.getLineCount());
        assertEquals(0, layout.getHeight());
        assertEquals("", layout.getText().toString());
    }

    @Test
    void nonPositiveWidthFallsBackToTheShellDefault() {
        StaticLayout zero = StaticLayout.Builder
                .obtain("hi", 0, 2, new FixedPaint(), 0)
                .build();
        assertEquals(480, zero.getWidth());

        StaticLayout negative = StaticLayout.Builder
                .obtain("hi", 0, 2, new FixedPaint(), -10)
                .build();
        assertEquals(480, negative.getWidth());
    }

    @Test
    void builderHonorsTheRequestedSlice() {
        StaticLayout layout = StaticLayout.Builder
                .obtain("hello", 1, 3, new FixedPaint(), 100)
                .build();
        assertEquals(1, layout.getLineCount());
        assertEquals(0, layout.getLineStart(0));
        assertEquals(2, layout.getLineEnd(0));          // "el"
        assertEquals(12.0f, layout.getLineMax(0));
        assertEquals(12.0f, layout.getLineWidth(0));
    }

    @Test
    void getLineBoundsFillsTheLineBoxAndReturnsTheBaseline() {
        StaticLayout layout = build(false, 0.0f);
        Rect bounds = new Rect();
        int baseline = layout.getLineBounds(0, bounds);
        assertEquals(15, baseline);
        assertEquals(0, bounds.left);
        assertEquals(0, bounds.top);
        assertEquals(0, bounds.right);                  // LTR start
        assertEquals(19, bounds.bottom);
    }

    @Test
    void getLineBottomHonorsTheIncludeSpacingFlag() {
        StaticLayout layout = build(false, 6.0f);
        assertEquals(25, layout.getLineBottom(0));
        // AOSP Layout.getLineBottom(line, includeLineSpacing=false)
        // subtracts the line's extra spacing.
        assertEquals(19, layout.getLineBottom(0, false));
        assertEquals(25, layout.getLineBottom(0, true));
    }

    @Test
    void getLineFontMetricsReflectsThePaintMetrics() {
        StaticLayout layout = build(false, 0.0f);
        Paint.FontMetricsInt fm = layout.getLineFontMetrics(0);
        assertEquals(-15, fm.ascent);
        assertEquals(4, fm.descent);
        assertEquals(-17, fm.top);
        assertEquals(5, fm.bottom);
        assertEquals(0, fm.leading);
    }

    @Test
    void getDesiredWidthUsesThePaintMeasurement() {
        FixedPaint paint = new FixedPaint();
        assertEquals(18.0f, Layout.getDesiredWidth("abc", paint));
        assertEquals(0.0f, Layout.getDesiredWidth(null, paint));
        assertEquals(12.0f, Layout.getDesiredWidth("hello", 1, 3, paint));
    }

    @Test
    void zeroPaintMetricsFallBackToTheTextSize() {
        StaticLayout layout = build("hi", 0, 2, false, 0.0f, 1.0f,
                new FixedPaint(16.0f, 0.0f, 0.0f, 0.0f, 0.0f));
        // above >= 0 -> -textSize; below <= 0 -> textSize / 4.
        assertEquals(20, layout.getHeight());
        assertEquals(16, layout.getLineBaseline(0));
        assertEquals(4, layout.getLineDescent(0));
        assertEquals(-16, layout.getLineAscent(0));
    }

    @Test
    void spacingMultiplierScalesTheWholeFontBox() {
        StaticLayout layout = build("hi", 0, 2, false, 0.0f, 2.0f,
                new FixedPaint());
        // extra = (below - above) * (multiplier - 1) = 19
        assertEquals(38, layout.getHeight());
        assertEquals(23, layout.getLineDescent(0));
        assertEquals(15, layout.getLineBaseline(0));
        assertEquals(19, layout.getLineExtra(0));
    }

    @Test
    void negativeSpacingRoundsHalfAwayFromZero() {
        StaticLayout layout = build(false, -2.5f);
        // ex = -2.5 -> -(int) (2.5 + 0.5) = -3
        assertEquals(16, layout.getHeight());
        assertEquals(1, layout.getLineDescent(0));
        assertEquals(15, layout.getLineBaseline(0));
        assertEquals(-3, layout.getLineExtra(0));
    }

    @Test
    void staticLayoutMirrorsTheLineTable() {
        StaticLayout layout = build(false, 0.0f);
        assertEquals(1, layout.mLineCount);
        assertEquals(0, layout.mLineStarts[0]);
        assertEquals(2, layout.mLineEnds[0]);
        assertEquals(12.0f, layout.mLineWidths[0]);
        assertEquals(0, layout.mLineTops[0]);
        assertEquals(19, layout.mLineTops[1]);
        assertEquals(19, layout.mHeight);
        assertTrue(layout.mText.toString().equals("hi"));
    }
}
