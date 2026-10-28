package android.text;

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
    public static android.text.Layout.TextInclusionStrategy INCLUSION_STRATEGY_ANY_OVERLAP;
    public static android.text.Layout.TextInclusionStrategy INCLUSION_STRATEGY_CONTAINS_ALL;
    public static android.text.Layout.TextInclusionStrategy INCLUSION_STRATEGY_CONTAINS_CENTER;
    public static final int JUSTIFICATION_MODE_INTER_CHARACTER = 2;
    public static final int JUSTIFICATION_MODE_INTER_WORD = 1;
    public static final int JUSTIFICATION_MODE_NONE = 0;

    protected java.lang.CharSequence text;
    protected android.text.TextPaint paint;
    protected int width;
    protected float spacingMul = 1.0f;
    protected float spacingAdd = 0.0f;
    protected int height;
    protected Layout(java.lang.CharSequence arg0, android.text.TextPaint arg1, int arg2, android.text.Layout.Alignment arg3, float arg4, float arg5) {
        this.text = (arg0 == null ? "" : arg0);
        if (arg1 == null) this.paint = new android.text.TextPaint();
        else this.paint = arg1;
        this.width = (arg2 <= 0) ? 480 : arg2;
        this.spacingMul = arg4;
        this.spacingAdd = arg5;
    }

    public android.graphics.RectF computeDrawingBoundingBox() {
        return null;
    }

    public void draw(android.graphics.Canvas canvas) {
        if (canvas == null || text == null) return;
        String s = text.toString();
        if (s.isEmpty()) return;
        float baseline = Math.max(14.0f, ((paint == null) ? new android.text.TextPaint() : paint).getTextSize() * 1.2f);
        canvas.drawText(s, 0.0f, baseline, (paint == null) ? new android.text.TextPaint() : paint);
    }

    public void draw(android.graphics.Canvas arg0, android.graphics.Path arg1, android.graphics.Paint arg2, int arg3) {
        draw(arg0);
    }

    public void draw(android.graphics.Canvas arg0, java.util.List<android.graphics.Path> arg1, java.util.List<android.graphics.Paint> arg2, android.graphics.Path arg3, android.graphics.Paint arg4, int arg5) {

    }

    public void drawBackground(android.graphics.Canvas arg0) {

    }

    public void drawText(android.graphics.Canvas arg0) {

    }

    public void fillCharacterBounds(int arg0, int arg1, float[] arg2, int arg3) {

    }

    public final android.text.Layout.Alignment getAlignment() {
        return null;
    }

    public int getBottomPadding() {
        return 0;
    }

    public final int getBreakStrategy() {
        return 0;
    }

    public void getCursorPath(int arg0, android.graphics.Path arg1, java.lang.CharSequence arg2) {

    }

    public static float getDesiredWidth(java.lang.CharSequence arg0, android.text.TextPaint arg1) {
        return getDesiredWidth(arg0, 0, arg0 == null ? 0 : arg0.length(), arg1);
    }

    public static float getDesiredWidth(java.lang.CharSequence arg0, int arg1, int arg2, android.text.TextPaint arg3) {
        CharSequence body = (arg0 == null) ? "" : (arg1 == 0 && arg2 == 0 ? arg0 : arg0.subSequence(arg1, Math.min(arg0.length(),arg2)));
        return arg3 == null ? (float) body.length() * 10.0f : arg3.measureText(body.toString());
    }

    public int getEllipsisCount(int arg0) {
        return 0;
    }

    public int getEllipsisStart(int arg0) {
        return 0;
    }

    public final android.text.TextUtils.TruncateAt getEllipsize() {
        return null;
    }

    public int getEllipsizedWidth() {
        return 0;
    }

    public int getHeight() {
        return (height <= 0 ? Math.round((paint==null?14.0f:paint.getTextSize())*1.3f) : height);
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
        return 0;
    }

    public final int getLineBaseline(int arg0) {
        return 0;
    }

    public final int getLineBottom(int arg0) {
        return 0;
    }

    public int getLineBottom(int arg0, boolean arg1) {
        return 0;
    }

    public int getLineBounds(int arg0, android.graphics.Rect arg1) {
        return 0;
    }

    public android.graphics.text.LineBreakConfig getLineBreakConfig() {
        return null;
    }

    public boolean getLineContainsTab(int arg0) {
        return false;
    }

    public int getLineCount() {
        return 1;
    }

    public int getLineDescent(int arg0) {
        return 0;
    }

    public android.text.Layout.Directions getLineDirections(int arg0) {
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
        return 0.0f;
    }

    public float getLineRight(int arg0) {
        return 0.0f;
    }

    public final float getLineSpacingAmount() {
        return 0.0f;
    }

    public final float getLineSpacingMultiplier() {
        return 0.0f;
    }

    public int getLineStart(int arg0) {
        return 0;
    }

    public int getLineTop(int arg0) {
        return 0;
    }

    public int getLineVisibleEnd(int arg0) {
        return 0;
    }

    public float getLineWidth(int arg0) {
        return 0.0f;
    }

    public final int getMaxLines() {
        return 0;
    }

    public android.graphics.Paint.FontMetrics getMinimumFontMetrics() {
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

    public final android.text.TextPaint getPaint() {
        if (paint == null) paint = new android.text.TextPaint();
        return paint;
    }

    public final android.text.Layout.Alignment getParagraphAlignment(int arg0) {
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

    public int[] getRangeForRect(android.graphics.RectF arg0, android.text.SegmentFinder arg1, android.text.Layout.TextInclusionStrategy arg2) {
        return null;
    }

    public final int[] getRightIndents() {
        return null;
    }

    public float getSecondaryHorizontal(int arg0) {
        return 0.0f;
    }

    public void getSelectionPath(int arg0, int arg1, android.graphics.Path arg2) {

    }

    public boolean getShiftDrawingOffsetForStartOverhang() {
        return false;
    }

    public final float getSpacingAdd() {
        return 0.0f;
    }

    public final float getSpacingMultiplier() {
        return 0.0f;
    }

    public final java.lang.CharSequence getText() {
        return (text == null) ? new android.text.SpannableString("") : text;
    }

    public final android.text.TextDirectionHeuristic getTextDirectionHeuristic() {
        return null;
    }

    public int getTopPadding() {
        return 0;
    }

    public boolean getUseBoundsForWidth() {
        return false;
    }

    public final int getWidth() {
        return 0;
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

    public android.graphics.Paint.FontMetricsInt getLineFontMetrics(int arg0) {
        android.graphics.Paint.FontMetricsInt fm = new android.graphics.Paint.FontMetricsInt();
        if (paint != null) {
            android.graphics.Paint.FontMetrics m = paint.getFontMetrics();
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

        public abstract boolean isSegmentInside(android.graphics.RectF arg0, android.graphics.RectF arg1);
        }
    }
