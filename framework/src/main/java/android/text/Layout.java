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

    protected Layout(java.lang.CharSequence arg0, android.text.TextPaint arg1, int arg2, android.text.Layout.Alignment arg3, float arg4, float arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;-><init>(Ljava/lang/CharSequence;Landroid/text/TextPaint;ILandroid/text/Layout$Alignment;FF)V");
    }

    public android.graphics.RectF computeDrawingBoundingBox() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->computeDrawingBoundingBox()Landroid/graphics/RectF;");
    }

    public void draw(android.graphics.Canvas arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->draw(Landroid/graphics/Canvas;)V");
    }

    public void draw(android.graphics.Canvas arg0, android.graphics.Path arg1, android.graphics.Paint arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->draw(Landroid/graphics/Canvas;Landroid/graphics/Path;Landroid/graphics/Paint;I)V");
    }

    public void draw(android.graphics.Canvas arg0, java.util.List<android.graphics.Path> arg1, java.util.List<android.graphics.Paint> arg2, android.graphics.Path arg3, android.graphics.Paint arg4, int arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->draw(Landroid/graphics/Canvas;Ljava/util/List;Ljava/util/List;Landroid/graphics/Path;Landroid/graphics/Paint;I)V");
    }

    public void drawBackground(android.graphics.Canvas arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->drawBackground(Landroid/graphics/Canvas;)V");
    }

    public void drawText(android.graphics.Canvas arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->drawText(Landroid/graphics/Canvas;)V");
    }

    public void fillCharacterBounds(int arg0, int arg1, float[] arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->fillCharacterBounds(II[FI)V");
    }

    public final android.text.Layout.Alignment getAlignment() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getAlignment()Landroid/text/Layout$Alignment;");
    }

    public int getBottomPadding() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getBottomPadding()I");
    }

    public final int getBreakStrategy() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getBreakStrategy()I");
    }

    public void getCursorPath(int arg0, android.graphics.Path arg1, java.lang.CharSequence arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getCursorPath(ILandroid/graphics/Path;Ljava/lang/CharSequence;)V");
    }

    public static float getDesiredWidth(java.lang.CharSequence arg0, android.text.TextPaint arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getDesiredWidth(Ljava/lang/CharSequence;Landroid/text/TextPaint;)F");
    }

    public static float getDesiredWidth(java.lang.CharSequence arg0, int arg1, int arg2, android.text.TextPaint arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getDesiredWidth(Ljava/lang/CharSequence;IILandroid/text/TextPaint;)F");
    }

    public int getEllipsisCount(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getEllipsisCount(I)I");
    }

    public int getEllipsisStart(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getEllipsisStart(I)I");
    }

    public final android.text.TextUtils.TruncateAt getEllipsize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getEllipsize()Landroid/text/TextUtils$TruncateAt;");
    }

    public int getEllipsizedWidth() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getEllipsizedWidth()I");
    }

    public int getHeight() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getHeight()I");
    }

    public final int getHyphenationFrequency() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getHyphenationFrequency()I");
    }

    public final int getJustificationMode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getJustificationMode()I");
    }

    public final int[] getLeftIndents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getLeftIndents()[I");
    }

    public final int getLineAscent(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getLineAscent(I)I");
    }

    public final int getLineBaseline(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getLineBaseline(I)I");
    }

    public final int getLineBottom(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getLineBottom(I)I");
    }

    public int getLineBottom(int arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getLineBottom(IZ)I");
    }

    public int getLineBounds(int arg0, android.graphics.Rect arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getLineBounds(ILandroid/graphics/Rect;)I");
    }

    public android.graphics.text.LineBreakConfig getLineBreakConfig() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getLineBreakConfig()Landroid/graphics/text/LineBreakConfig;");
    }

    public boolean getLineContainsTab(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getLineContainsTab(I)Z");
    }

    public int getLineCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getLineCount()I");
    }

    public int getLineDescent(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getLineDescent(I)I");
    }

    public android.text.Layout.Directions getLineDirections(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getLineDirections(I)Landroid/text/Layout$Directions;");
    }

    public final int getLineEnd(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getLineEnd(I)I");
    }

    public int getLineForOffset(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getLineForOffset(I)I");
    }

    public int getLineForVertical(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getLineForVertical(I)I");
    }

    public float getLineLeft(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getLineLeft(I)F");
    }

    public int getLineLetterSpacingUnitCount(int arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getLineLetterSpacingUnitCount(IZ)I");
    }

    public float getLineMax(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getLineMax(I)F");
    }

    public float getLineRight(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getLineRight(I)F");
    }

    public final float getLineSpacingAmount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getLineSpacingAmount()F");
    }

    public final float getLineSpacingMultiplier() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getLineSpacingMultiplier()F");
    }

    public int getLineStart(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getLineStart(I)I");
    }

    public int getLineTop(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getLineTop(I)I");
    }

    public int getLineVisibleEnd(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getLineVisibleEnd(I)I");
    }

    public float getLineWidth(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getLineWidth(I)F");
    }

    public final int getMaxLines() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getMaxLines()I");
    }

    public android.graphics.Paint.FontMetrics getMinimumFontMetrics() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getMinimumFontMetrics()Landroid/graphics/Paint$FontMetrics;");
    }

    public int getOffsetForHorizontal(int arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getOffsetForHorizontal(IF)I");
    }

    public int getOffsetToLeftOf(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getOffsetToLeftOf(I)I");
    }

    public int getOffsetToRightOf(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getOffsetToRightOf(I)I");
    }

    public final android.text.TextPaint getPaint() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getPaint()Landroid/text/TextPaint;");
    }

    public final android.text.Layout.Alignment getParagraphAlignment(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getParagraphAlignment(I)Landroid/text/Layout$Alignment;");
    }

    public int getParagraphDirection(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getParagraphDirection(I)I");
    }

    public final int getParagraphLeft(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getParagraphLeft(I)I");
    }

    public final int getParagraphRight(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getParagraphRight(I)I");
    }

    public float getPrimaryHorizontal(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getPrimaryHorizontal(I)F");
    }

    public int[] getRangeForRect(android.graphics.RectF arg0, android.text.SegmentFinder arg1, android.text.Layout.TextInclusionStrategy arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getRangeForRect(Landroid/graphics/RectF;Landroid/text/SegmentFinder;Landroid/text/Layout$TextInclusionStrategy;)[I");
    }

    public final int[] getRightIndents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getRightIndents()[I");
    }

    public float getSecondaryHorizontal(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getSecondaryHorizontal(I)F");
    }

    public void getSelectionPath(int arg0, int arg1, android.graphics.Path arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getSelectionPath(IILandroid/graphics/Path;)V");
    }

    public boolean getShiftDrawingOffsetForStartOverhang() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getShiftDrawingOffsetForStartOverhang()Z");
    }

    public final float getSpacingAdd() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getSpacingAdd()F");
    }

    public final float getSpacingMultiplier() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getSpacingMultiplier()F");
    }

    public final java.lang.CharSequence getText() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getText()Ljava/lang/CharSequence;");
    }

    public final android.text.TextDirectionHeuristic getTextDirectionHeuristic() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getTextDirectionHeuristic()Landroid/text/TextDirectionHeuristic;");
    }

    public int getTopPadding() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getTopPadding()I");
    }

    public boolean getUseBoundsForWidth() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getUseBoundsForWidth()Z");
    }

    public final int getWidth() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->getWidth()I");
    }

    public final void increaseWidthTo(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->increaseWidthTo(I)V");
    }

    public boolean isFallbackLineSpacingEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->isFallbackLineSpacingEnabled()Z");
    }

    public final boolean isFontPaddingIncluded() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->isFontPaddingIncluded()Z");
    }

    public boolean isRtlCharAt(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->isRtlCharAt(I)Z");
    }

    protected final boolean isSpanned() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/Layout;->isSpanned()Z");
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
