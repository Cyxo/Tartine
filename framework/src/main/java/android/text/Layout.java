package android.text;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.text.LineBreakConfig;
import java.util.List;

    public class Layout {

    public static final int BREAK_STRATEGY_BALANCED = 2;
    public static final int BREAK_STRATEGY_HIGH_QUALITY = 1;
    public static final int BREAK_STRATEGY_SIMPLE = 0;
    public static final float DEFAULT_LINESPACING_ADDITION = 0.0f;
    public static final float DEFAULT_LINESPACING_MULTIPLIER = 1.0f;
    public static final int DIR_LEFT_TO_RIGHT = 1;
    public static final int DIR_RIGHT_TO_LEFT = -1;
    public static final int HYPHENATION_FREQUENCY_FULL = 2;
    public static final int HYPHENATION_FREQUENCY_FULL_FAST = 4;
    public static final int HYPHENATION_FREQUENCY_NONE = 0;
    public static final int HYPHENATION_FREQUENCY_NORMAL = 1;
    public static final int HYPHENATION_FREQUENCY_NORMAL_FAST = 3;
    public static Layout.TextInclusionStrategy INCLUSION_STRATEGY_ANY_OVERLAP;
    public static Layout.TextInclusionStrategy INCLUSION_STRATEGY_CONTAINS_ALL;
    public static Layout.TextInclusionStrategy INCLUSION_STRATEGY_CONTAINS_CENTER;
    public static final int JUSTIFICATION_MODE_INTER_CHARACTER = 2;
    public static final int JUSTIFICATION_MODE_INTER_WORD = 1;
    public static final int JUSTIFICATION_MODE_NONE = 0;

    protected CharSequence text;
    protected TextPaint paint;
    protected int width;
    protected float spacingMul = 1.0f;
    protected float spacingAdd = 0.0f;
    protected int height;
    protected int lineAscent;
    protected int lineDescent;
    protected int lineTop;
    protected int lineBaseline;
    protected int lineExtra;
    /** AOSP StaticLayout.Builder.setIncludePad: when true the first line's
     *  top and the last line's bottom use the font's {@code top}/{@code bottom}
     *  (extra vertical padding) instead of {@code ascent}/{@code descent}. */
    protected boolean includePad;

    protected Layout(CharSequence arg0, TextPaint arg1, int arg2, Layout.Alignment arg3, float arg4, float arg5) {
        this(arg0, arg1, arg2, arg3, arg4, arg5, false);
    }

    protected Layout(CharSequence arg0, TextPaint arg1, int arg2, Layout.Alignment arg3, float arg4, float arg5, boolean arg6) {
        this.text = (arg0 == null ? "" : arg0);
        if (arg1 == null) this.paint = new TextPaint();
        else this.paint = arg1;
        this.width = (arg2 <= 0) ? 480 : arg2;
        this.spacingMul = arg4;
        this.spacingAdd = arg5;
        this.includePad = arg6;
        recomputeLineMetrics();
    }

    /**
     * One-line line metrics, following AOSP's StaticLayout.generate
     * (frameworks-base core/java/android/text/StaticLayout.java:1153-1256):
     * {@code above}/{@code below} come from the font metrics (ascent/descent,
     * or top/bottom when includePad), line spacing adds
     * {@code (below-above)*(spacingMul-1)+spacingAdd}, and the stored descent
     * is {@code below + extra}. Layout.getLineBaseline is then
     * {@code getLineTop(line+1) - getLineDescent(line)} and Layout.getHeight
     * is {@code getLineTop(getLineCount())}
     * (core/java/android/text/Layout.java:1233-1235 and :2820-2823), which
     * makes the baseline independent of the added spacing.
     */
    protected void recomputeLineMetrics() {
        if (this.paint == null) this.paint = new TextPaint();
        float size = this.paint.getTextSize() > 0.0f
                ? this.paint.getTextSize() : 14.0f;
        Paint.FontMetrics fm = this.paint.getFontMetrics();
        float above = this.includePad ? fm.top : fm.ascent;
        float below = this.includePad ? fm.bottom : fm.descent;
        if (above >= 0.0f) above = -size;
        if (below <= 0.0f) below = size * 0.25f;
        int intAbove = (int) Math.floor(above);
        int intBelow = (int) Math.ceil(below);
        float ex = (intBelow - intAbove) * (this.spacingMul - 1.0f)
                + this.spacingAdd;
        int extra = ex >= 0.0f
                ? (int) (ex + 0.5f) : -(int) (-ex + 0.5f);
        this.lineTop = 0;
        this.lineExtra = extra;
        this.lineDescent = intBelow + extra;
        this.height = (intBelow - intAbove) + extra;
        this.lineBaseline = this.height - this.lineDescent;
        this.lineAscent = -this.lineBaseline;
    }

    public RectF computeDrawingBoundingBox() {
        return null;
    }

    /**
     * AOSP Layout.draw(Canvas) → draw(c, null, null, 0) → drawText/line loop;
     * plain LTR unspanned text takes the direct drawText leg with
     * {@code baseline = lbottom - getLineDescent(l)} where
     * {@code lbottom = getLineTop(l+1)} (core/java/android/text/Layout.java
     * drawText(Canvas,int,int) and the builtin parity note). One line only
     * here, so that is exactly {@link #getLineBaseline(int)}.
     */
    public void draw(Canvas canvas) {
        if (canvas == null || text == null) return;
        String s = text.toString();
        if (s.isEmpty()) return;
        TextPaint pnt = (paint == null) ? new TextPaint() : paint;
        canvas.drawText(s, 0.0f, (float) getLineBaseline(0), pnt);
    }

    public void draw(Canvas arg0, Path arg1, Paint arg2, int arg3) {
        draw(arg0);
    }

    public void draw(Canvas arg0, List<Path> arg1, List<Paint> arg2, Path arg3, Paint arg4, int arg5) {

    }

    public void drawBackground(Canvas arg0) {

    }

    public void drawText(Canvas arg0) {

    }

    public void fillCharacterBounds(int arg0, int arg1, float[] arg2, int arg3) {

    }

    public final Layout.Alignment getAlignment() {
        return null;
    }

    public int getBottomPadding() {
        return 0;
    }

    public final int getBreakStrategy() {
        return 0;
    }

    public void getCursorPath(int arg0, Path arg1, CharSequence arg2) {

    }

    public static float getDesiredWidth(CharSequence arg0, TextPaint arg1) {
        return getDesiredWidth(arg0, 0, arg0 == null ? 0 : arg0.length(), arg1);
    }

    public static float getDesiredWidth(CharSequence arg0, int arg1, int arg2, TextPaint arg3) {
        CharSequence body = (arg0 == null) ? "" : (arg1 == 0 && arg2 == 0 ? arg0 : arg0.subSequence(arg1, Math.min(arg0.length(),arg2)));
        return arg3 == null ? (float) body.length() * 10.0f : arg3.measureText(body.toString());
    }

    public int getEllipsisCount(int arg0) {
        return 0;
    }

    public int getEllipsisStart(int arg0) {
        return 0;
    }

    public final TextUtils.TruncateAt getEllipsize() {
        return null;
    }

    public int getEllipsizedWidth() {
        return 0;
    }

    public int getHeight() {
        // AOSP Layout.java:1233-1235: getHeight() = getLineTop(getLineCount()).
        return getLineTop(getLineCount());
    }

    public final int getHyphenationFrequency() {
        return 0;
    }

    public final int getJustificationMode() {
        return 0;
    }

    public final int[] getLeftIndents() {
        return null;
    }

    public final int getLineAscent(int arg0) {
        // AOSP Layout.java:2829-2831: getLineTop(line) - getLineBaseline(line).
        return getLineTop(arg0) - getLineBaseline(arg0);
    }

    public final int getLineBaseline(int arg0) {
        // AOSP Layout.java:2820-2823:
        // getLineTop(line+1) - getLineDescent(line).
        return getLineTop(arg0 + 1) - getLineDescent(arg0);
    }

    public final int getLineBottom(int arg0) {
        return getLineTop(arg0 + 1);
    }

    public int getLineBottom(int arg0, boolean arg1) {
        // AOSP Layout.java:2809-2815: the spacing-aware variant subtracts
        // the line's extra spacing when includeLineSpacing is false.
        return arg1 ? getLineTop(arg0 + 1)
                : getLineTop(arg0 + 1) - getLineExtra(arg0);
    }

    public int getLineBounds(int arg0, Rect arg1) {
        // AOSP Layout.java: getLineBounds fills left/top/right/bottom from
        // the line box and returns the baseline.
        if (arg1 != null) {
            arg1.left = (int) getLineLeft(arg0);
            arg1.top = getLineTop(arg0);
            arg1.right = (int) getLineRight(arg0);
            arg1.bottom = getLineBottom(arg0);
        }
        return getLineBaseline(arg0);
    }

    public LineBreakConfig getLineBreakConfig() {
        return null;
    }

    public boolean getLineContainsTab(int arg0) {
        return false;
    }

    public int getLineCount() {
        return (text == null || text.length() == 0) ? 0 : 1;
    }

    public int getLineDescent(int arg0) {
        // AOSP StaticLayout.generate stores DESCENT = below + extra
        // (core/java/android/text/StaticLayout.java:1229-1236).
        return lineDescent;
    }

    /** @hide AOSP StaticLayout.getLineExtra: spacing folded into descent. */
    public int getLineExtra(int arg0) {
        return lineExtra;
    }

    public Layout.Directions getLineDirections(int arg0) {
        return null;
    }

    public final int getLineEnd(int arg0) {
        return (text == null) ? 0 : text.length();
    }

    public int getLineForOffset(int arg0) {
        return 0;
    }

    public int getLineForVertical(int arg0) {
        return 0;
    }

    public float getLineLeft(int arg0) {
        return 0.0f;
    }

    public int getLineLetterSpacingUnitCount(int arg0, boolean arg1) {
        return 0;
    }

    public float getLineMax(int arg0) {
        if (text == null) return 0.0f;
        return paint == null ? 0.0f : paint.measureText(text.toString());
    }

    public float getLineRight(int arg0) {
        return 0.0f;
    }

    public final float getLineSpacingAmount() {
        return spacingAdd;
    }

    public final float getLineSpacingMultiplier() {
        return spacingMul;
    }

    public int getLineStart(int arg0) {
        return 0;
    }

    public int getLineTop(int arg0) {
        // AOSP StaticLayout.getLineTop reads the TOP column; a one-line
        // layout has TOP[0] = 0 and TOP[1] = height
        // (core/java/android/text/StaticLayout.java:1436-1439 and
        // generate()'s v += (below - above) + extra).
        if (arg0 <= 0) return 0;
        return height;
    }

    public int getLineVisibleEnd(int arg0) {
        return (text == null) ? 0 : text.length();
    }

    public float getLineWidth(int arg0) {
        return getLineMax(arg0);
    }

    public final int getMaxLines() {
        return 0;
    }

    public Paint.FontMetrics getMinimumFontMetrics() {
        return null;
    }

    public int getOffsetForHorizontal(int arg0, float arg1) {
        return 0;
    }

    public int getOffsetToLeftOf(int arg0) {
        return 0;
    }

    public int getOffsetToRightOf(int arg0) {
        return 0;
    }

    public final TextPaint getPaint() {
        if (paint == null) paint = new TextPaint();
        return paint;
    }

    public final Layout.Alignment getParagraphAlignment(int arg0) {
        return null;
    }

    public int getParagraphDirection(int arg0) {
        return 0;
    }

    public final int getParagraphLeft(int arg0) {
        return 0;
    }

    public final int getParagraphRight(int arg0) {
        return 0;
    }

    public float getPrimaryHorizontal(int arg0) {
        return 0.0f;
    }

    public int[] getRangeForRect(RectF arg0, SegmentFinder arg1, Layout.TextInclusionStrategy arg2) {
        return null;
    }

    public final int[] getRightIndents() {
        return null;
    }

    public float getSecondaryHorizontal(int arg0) {
        return 0.0f;
    }

    public void getSelectionPath(int arg0, int arg1, Path arg2) {

    }

    public boolean getShiftDrawingOffsetForStartOverhang() {
        return false;
    }

    public final float getSpacingAdd() {
        return spacingAdd;
    }

    public final float getSpacingMultiplier() {
        return spacingMul;
    }

    public final CharSequence getText() {
        return (text == null) ? new SpannableString("") : text;
    }

    public final TextDirectionHeuristic getTextDirectionHeuristic() {
        return null;
    }

    public int getTopPadding() {
        return 0;
    }

    public boolean getUseBoundsForWidth() {
        return false;
    }

    public final int getWidth() {
        return width;
    }

    public final void increaseWidthTo(int arg0) {

    }

    public boolean isFallbackLineSpacingEnabled() {
        return false;
    }

    public final boolean isFontPaddingIncluded() {
        return false;
    }

    public boolean isRtlCharAt(int arg0) {
        return false;
    }

    public Paint.FontMetricsInt getLineFontMetrics(int arg0) {
        Paint.FontMetricsInt fm = new Paint.FontMetricsInt();
        if (paint != null) {
            Paint.FontMetrics m = paint.getFontMetrics();
            fm.ascent = (int) m.ascent;
            fm.descent = (int) m.descent;
            fm.top = (int) m.top;
            fm.bottom = (int) m.bottom;
            fm.leading = (int) m.leading;
        }
        return fm;
    }

    protected final boolean isSpanned() {
        return false;
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public Layout() {
        }
        public static enum Alignment {
            ALIGN_CENTER, ALIGN_NORMAL, ALIGN_OPPOSITE;
        }
        public static class Directions {
        }
        public static interface TextInclusionStrategy {

        public abstract boolean isSegmentInside(RectF arg0, RectF arg1);
        }
    }
