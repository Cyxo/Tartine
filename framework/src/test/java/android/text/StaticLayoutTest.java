package android.text;

import android.graphics.Paint;
import android.graphics.Rect;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * AOSP line algebra: StaticLayout.generate stores
 * {@code above}/{@code below} from the integer {@link Paint.FontMetricsInt}
 * (ascent/descent, or top/bottom on the first/last line when includePad),
 * folds line spacing into the descent, and Layout.getLineBaseline is
 * {@code getLineTop(line+1) - getLineDescent(line)}.
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
    void getDesiredWidthUsesTheWidestExplicitLine() {
        FixedPaint paint = new FixedPaint();
        // Four characters on the first line, one on the second, two on the
        // third: the intrinsic width is 4 * 6, not 7 * 6.
        assertEquals(24.0f, Layout.getDesiredWidth("abcd\nx\nyz", paint));
        // Trailing and blank lines contribute height, never width.
        assertEquals(18.0f, Layout.getDesiredWidth("abc\n", paint));
        assertEquals(18.0f, Layout.getDesiredWidth("abc\n\n", paint));
        // CRLF: the '\r' is a line terminator, not a glyph.
        assertEquals(18.0f, Layout.getDesiredWidth("abc\r\nx", paint));
    }

    @Test
    void getDesiredWidthSlicesTheRequestedRange() {
        FixedPaint paint = new FixedPaint();
        // "ab\ncdef" sliced to [1, 6) is "b\ncde": widest line is 3 chars.
        assertEquals(18.0f, Layout.getDesiredWidth("ab\ncdef", 1, 6, paint));
        // A slice that ends on a newline still measures the line it closes.
        assertEquals(12.0f, Layout.getDesiredWidth("xabc\ny", 2, 5, paint));
    }

    @Test
    void getDesiredWidthWithoutPaintFallsBackToLineLength() {
        // No paint to measure with: widest line length * 10, the documented
        // fallback on the two-argument overload.
        assertEquals(30.0f, Layout.getDesiredWidth("abc\nde", null));
        assertEquals(30.0f, Layout.getDesiredWidth("abc\r\nde", null));
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

    @Test
    void explicitNewlineStartsANewLine() {
        StaticLayout layout = build("a\nb", 0, 3, false, 0.0f, 1.0f,
                new FixedPaint());
        assertEquals(2, layout.getLineCount());
        assertEquals(38, layout.getHeight());
        assertEquals(0, layout.getLineTop(0));
        assertEquals(19, layout.getLineTop(1));
        assertEquals(38, layout.getLineTop(2));
        assertEquals(15, layout.getLineBaseline(0));
        assertEquals(34, layout.getLineBaseline(1));
        assertEquals(0, layout.getLineStart(0));
        assertEquals(2, layout.getLineEnd(0));          // includes '\n'
        assertEquals(1, layout.getLineVisibleEnd(0));   // excludes '\n'
        assertEquals(2, layout.getLineStart(1));
        assertEquals(3, layout.getLineEnd(1));
        assertEquals(6.0f, layout.getLineMax(0));
        assertEquals(6.0f, layout.getLineMax(1));
        assertEquals(6.0f, layout.getLineWidth(1));
        assertEquals(2, layout.mLineCount);
        assertArrayEquals(new int[] {0, 2}, layout.mLineStarts);
        assertArrayEquals(new int[] {2, 3}, layout.mLineEnds);
        assertArrayEquals(new float[] {6.0f, 6.0f}, layout.mLineWidths);
        assertArrayEquals(new int[] {0, 19, 38}, layout.mLineTops);
        assertEquals(38, layout.mHeight);
    }

    @Test
    void trailingNewlineAddsAnEmptyLastLine() {
        StaticLayout layout = build("a\n", 0, 2, false, 0.0f, 1.0f,
                new FixedPaint());
        assertEquals(2, layout.getLineCount());
        assertEquals(38, layout.getHeight());
        assertEquals(2, layout.getLineStart(1));
        assertEquals(2, layout.getLineEnd(1));
        assertEquals(2, layout.getLineVisibleEnd(1));
        assertEquals(0.0f, layout.getLineMax(1));
        assertEquals(34, layout.getLineBaseline(1));
    }

    @Test
    void carriageReturnBeforeNewlineIsNotDrawn() {
        StaticLayout layout = build("a\r\nb", 0, 4, false, 0.0f, 1.0f,
                new FixedPaint());
        assertEquals(2, layout.getLineCount());
        assertEquals(1, layout.getLineVisibleEnd(0));
        assertEquals(3, layout.getLineEnd(0));          // includes CRLF
        assertEquals(3, layout.getLineStart(1));
        assertEquals(4, layout.getLineEnd(1));
        assertEquals(6.0f, layout.getLineMax(0));
    }

    @Test
    void consecutiveNewlinesProduceBlankLines() {
        StaticLayout layout = build("a\n\nb", 0, 4, false, 0.0f, 1.0f,
                new FixedPaint());
        assertEquals(3, layout.getLineCount());
        assertEquals(57, layout.getHeight());
        assertEquals(0.0f, layout.getLineMax(1));
        assertEquals(53, layout.getLineBaseline(2));
    }

    @Test
    void builderDefaultsToIncludePadLikeAosp() {
        StaticLayout layout = StaticLayout.Builder
                .obtain("hi", 0, 2, new FixedPaint(), 100)
                .build();
        // No setIncludePad: AOSP defaults to true, so top/bottom pad the box.
        assertEquals(22, layout.getHeight());          // bottom - top
        assertEquals(17, layout.getLineBaseline(0));   // -top
    }

    @Test
    void includePadPadsOnlyTheFirstAndLastLines() {
        StaticLayout layout = build("a\nb", 0, 3, true, 0.0f, 1.0f,
                new FixedPaint());
        // AOSP StaticLayout.generate: first line above=top, last line
        // below=bottom; interior lines keep ascent/descent.
        assertEquals(41, layout.getHeight());          // 21 + 20
        assertEquals(21, layout.getLineTop(1));
        assertEquals(41, layout.getLineTop(2));
        assertEquals(17, layout.getLineBaseline(0));   // 21 - descent(4)
        assertEquals(36, layout.getLineBaseline(1));   // 41 - bottom(5)
        assertEquals(4, layout.getLineDescent(0));
        assertEquals(5, layout.getLineDescent(1));
    }

    @Test
    void deviceFontMetricsIntFollowsTheAospLineAlgebra() {
        // A Pixel-class device reports these FontMetricsInt at 48px.
        TextPaint paint = new FixedPaint(48.0f, -45.0f, 12.0f, -51.0f, 14.0f);
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        assertEquals(-51, fm.top);       // floor
        assertEquals(-45, fm.ascent);    // round
        assertEquals(12, fm.descent);    // round
        assertEquals(14, fm.bottom);     // ceil
        assertEquals(0, fm.leading);
        assertEquals(57, paint.getFontMetricsInt(fm)); // descent-ascent+leading

        // No pad: two lines of (descent - ascent).
        assertEquals(114, build("a\nb", 0, 3, false, 0.0f, 1.0f, paint)
                .getHeight());
        // includePad: (descent - top) + (bottom - ascent), not two padded
        // lines. This is the difference the device line height exposed.
        assertEquals(122, build("a\nb", 0, 3, true, 0.0f, 1.0f, paint)
                .getHeight());
    }
}
