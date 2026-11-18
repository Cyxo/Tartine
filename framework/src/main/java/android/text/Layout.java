package android.text;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.text.LineBreakConfig;
import java.util.ArrayList;
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
    /** Line start offsets, including the character after an explicit '\n'. */
    protected int[] lineStarts = new int[0];
    /** Line text end offsets, excluding the terminating '\n'. */
    protected int[] lineTextEnds = new int[0];
    /** AOSP getLineEnd offsets, including the terminating '\n'. */
    protected int[] lineEnds = new int[0];
    /** Line tops plus the final bottom, so length is line count + 1. */
    protected int[] lineTops = new int[0];
    protected int[] lineDescents = new int[0];
    protected int[] lineExtras = new int[0];
    /** AOSP StaticLayout.Builder.setIncludePad: when true the first line's
     *  top and the last line's bottom use the font's {@code top}/{@code bottom}
     *  (extra vertical padding) instead of {@code ascent}/{@code descent}. */
    protected boolean includePad;
    /** Maximum number of lines; {@code Integer.MAX_VALUE} means unbounded. */
    protected int maxLines = Integer.MAX_VALUE;
    /** Ellipsis policy for the last line, or null to clip instead. */
    protected TextUtils.TruncateAt ellipsize;
    /** Width the ellipsis is measured against, or -1 for the layout width. */
    protected int ellipsizedWidth = -1;
    /** Ellipsized line index, or -1 when no line is ellipsized. */
    protected int ellipsisLine = -1;
    /** Offset where the ellipsis replaces text on {@link #ellipsisLine}. */
    protected int ellipsisOffset = -1;
    /** Line alignment requested by the builder/constructor. */
    protected Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;

    protected Layout(CharSequence arg0, TextPaint arg1, int arg2, Layout.Alignment arg3, float arg4, float arg5) {
        this(arg0, arg1, arg2, arg3, arg4, arg5, false);
    }

    protected Layout(CharSequence arg0, TextPaint arg1, int arg2, Layout.Alignment arg3, float arg4, float arg5, boolean arg6) {
        this.text = (arg0 == null ? "" : arg0);
        if (arg1 == null) this.paint = new TextPaint();
        else this.paint = arg1;
        this.width = (arg2 <= 0) ? 480 : arg2;
        this.alignment = arg3 == null ? Layout.Alignment.ALIGN_NORMAL : arg3;
        this.spacingMul = arg4;
        this.spacingAdd = arg5;
        this.includePad = arg6;
        recomputeLineMetrics();
    }

    /**
     * Explicit line breaks ('\n') split the text into lines with the AOSP
     * StaticLayout.generate line algebra
     * (frameworks-base core/java/android/text/StaticLayout.java:754-1281):
     * the line box comes from the paint's integer {@code FontMetricsInt}
     * (ascent/descent, or top/bottom when includePad), line spacing adds
     * {@code (below-above)*(spacingMul-1)+spacingAdd}, and the stored descent
     * is {@code below + extra}. {@code includePad} pads only the first line's
     * top ({@code above = fm.top}) and the last line's bottom
     * ({@code below = fm.bottom}); interior lines keep ascent/descent.
     * Layout.getLineBaseline is {@code getLineTop(line+1) - getLineDescent(line)}
     * and Layout.getHeight is {@code getLineTop(getLineCount())}
     * (core/java/android/text/Layout.java:1233-1235 and :2820-2823).
     *
     * <p>The line box follows the integer metrics (top floor, ascent/descent
     * round, bottom ceil — {@code GraphicsJNI::set_metrics_int}), matching a
     * device rather than the raw float ascent/descent Tartine used before.
     *
     * <p>Deviation kept from AOSP: spacing {@code extra} is applied to the
     * last line too (AOSP skips it unless {@code addLastLineLineSpacing} is
     * set).
     *
     * <p>Lines break on explicit newlines and, for a paragraph wider than the
     * layout width, at the last fitting space (or between characters when a
     * single word exceeds the width). Alignment is applied at draw time from
     * {@link #getLineLeft}.
     */
    protected void recomputeLineMetrics() {
        if (this.paint == null) this.paint = new TextPaint();
        int size = Math.round(this.paint.getTextSize() > 0.0f
                ? this.paint.getTextSize() : 14.0f);
        // AOSP StaticLayout.generate reads Paint.getFontMetricsInt(); a
        // zero-metric face (no installed font) falls back to the text size so
        // the layout still has a usable box. AOSP's real fonts are never
        // all-zero.
        Paint.FontMetricsInt fm = this.paint.getFontMetricsInt();
        int ascent = fm.ascent;
        int descent = fm.descent;
        int top = fm.top;
        int bottom = fm.bottom;
        if (ascent >= 0) ascent = -size;
        if (descent <= 0) descent = size / 4;
        if (top >= 0) top = -size;
        if (bottom <= 0) bottom = size / 4;

        String body = this.text == null ? "" : this.text.toString();
        if (body.isEmpty()) {
            this.height = 0;
            this.lineStarts = new int[0];
            this.lineTextEnds = new int[0];
            this.lineEnds = new int[0];
            this.lineTops = new int[0];
            this.lineDescents = new int[0];
            this.lineExtras = new int[0];
            return;
        }

        // Paragraph splitting plus greedy width wrapping. Each entry is
        // {lineStart, visibleTextEnd, lineEnd}: visibleTextEnd drops trailing
        // spaces (getLineVisibleEnd), lineEnd includes them and the paragraph
        // newline (getLineEnd).
        ArrayList<int[]> lineTable = new ArrayList<>();
        int paraStart = 0;
        while (true) {
            int newline = body.indexOf('\n', paraStart);
            int paraEnd = newline < 0 ? body.length() : newline;
            int paragraphEnd = newline < 0 ? body.length() : newline + 1;
            wrapParagraph(body, paraStart, paraEnd, paragraphEnd, lineTable);
            if (newline < 0) {
                break;
            }
            paraStart = newline + 1;
            if (paraStart == body.length()) {
                lineTable.add(new int[] {paraStart, paraStart, paraStart});
                break;
            }
        }

        int lines = lineTable.size();
        int generatedLines = lines;
        if (maxLines > 0 && lines > maxLines) {
            while (lineTable.size() > maxLines) {
                lineTable.remove(lineTable.size() - 1);
            }
            lines = lineTable.size();
        }
        this.ellipsisLine = -1;
        this.ellipsisOffset = -1;
        if (ellipsize != null && lines > 0 && lines == maxLines
                && generatedLines > lines) {
            int[] last = lineTable.get(lines - 1);
            int available = ellipsizedWidth > 0 ? ellipsizedWidth : this.width;
            String ellipsis = "\u2026";
            float ellipsisWidth = this.paint.measureText(ellipsis);
            int fit = last[0];
            while (fit < last[1]
                    && this.paint.measureText(body.substring(last[0], fit + 1))
                            + ellipsisWidth <= available) {
                fit++;
            }
            last[1] = fit;
            last[2] = fit;
            this.ellipsisLine = lines - 1;
            this.ellipsisOffset = fit;
        }
        int[] starts = new int[lines];
        int[] textEnds = new int[lines];
        int[] ends = new int[lines];
        int[] tops = new int[lines + 1];
        int[] descents = new int[lines];
        int[] extras = new int[lines];
        int v = 0;
        for (int line = 0; line < lines; line++) {
            int[] entry = lineTable.get(line);
            // AOSP StaticLayout.generate: includePad only replaces the first
            // line's above (top) and the last line's below (bottom).
            int above = ascent;
            int below = descent;
            if (this.includePad && line == 0) above = top;
            if (this.includePad && line == lines - 1) below = bottom;
            double ex = (below - above) * (this.spacingMul - 1.0f)
                    + this.spacingAdd;
            int extra = ex >= 0.0
                    ? (int) (ex + 0.5) : -(int) (-ex + 0.5);

            starts[line] = entry[0];
            textEnds[line] = entry[1];
            ends[line] = entry[2];
            tops[line] = v;
            descents[line] = below + extra;
            extras[line] = extra;
            v += (below - above) + extra;
        }
        tops[lines] = v;
        this.height = v;
        this.lineStarts = starts;
        this.lineTextEnds = textEnds;
        this.lineEnds = ends;
        this.lineTops = tops;
        this.lineDescents = descents;
        this.lineExtras = extras;
    }

    /**
     * Greedy word wrap for one paragraph: break after the last space whose
     * prefix still fits {@link #width}; when a single word is wider than the
     * line, break between characters so the paragraph always makes progress.
     * Trailing spaces are excluded from the visible line.
     */
    private void wrapParagraph(String body, int start, int end,
            int paragraphEnd, List<int[]> lines) {
        if (start >= end) {
            lines.add(new int[] {start, start, paragraphEnd});
            return;
        }
        int lineStart = start;
        while (lineStart < end) {
            int lineEnd = findLineBreak(body, lineStart, end);
            int visibleEnd = lineEnd;
            // Deviation from AOSP: drop the '\r' of a CRLF break from the
            // drawn line. Minikin gives it zero advance; Skia would draw a
            // missing-glyph box, so it never reaches the backend.
            if (visibleEnd > lineStart && body.charAt(visibleEnd - 1) == '\r') {
                visibleEnd--;
            }
            while (visibleEnd > lineStart && body.charAt(visibleEnd - 1) == ' ') {
                visibleEnd--;
            }
            lines.add(new int[] {lineStart, visibleEnd,
                    lineEnd < end ? lineEnd : paragraphEnd});
            lineStart = lineEnd;
            while (lineStart < end && body.charAt(lineStart) == ' ') {
                lineStart++;
            }
        }
    }

    /** First offset at or before {@code limit} whose prefix fits the width. */
    private int findLineBreak(String body, int start, int limit) {
        if (this.paint.measureText(body.substring(start, limit))
                <= this.width) {
            return limit;
        }
        int lastSpace = -1;
        for (int i = start + 1; i < limit; i++) {
            if (body.charAt(i) != ' ') {
                continue;
            }
            if (this.paint.measureText(body.substring(start, i))
                    <= this.width) {
                lastSpace = i;
            } else {
                break;
            }
        }
        if (lastSpace > start) {
            return lastSpace;
        }
        int fit = start + 1;
        for (int i = start + 1; i <= limit; i++) {
            if (this.paint.measureText(body.substring(start, i))
                    <= this.width) {
                fit = i;
            } else {
                break;
            }
        }
        return fit;
    }

    public RectF computeDrawingBoundingBox() {
        return null;
    }

    /**
     * AOSP Layout.draw(Canvas) → draw(c, null, null, 0) → drawText/line loop;
     * plain LTR unspanned text draws one run per line with
     * {@code baseline = lbottom - getLineDescent(l)} where
     * {@code lbottom = getLineTop(l+1)} (core/java/android/text/Layout.java
     * drawText(Canvas,int,int) and the builtin parity note). The line table
     * splits explicit '\n' breaks, so a newline never reaches the backend as
     * a glyph.
     */
    public void draw(Canvas canvas) {
        if (canvas == null || text == null) return;
        String s = text.toString();
        if (s.isEmpty()) return;
        TextPaint pnt = (paint == null) ? new TextPaint() : paint;
        for (int line = 0; line < getLineCount(); line++) {
            int start = lineStarts[line];
            int end = lineTextEnds[line];
            if (line == ellipsisLine && ellipsisOffset >= 0) {
                end = Math.min(end, ellipsisOffset);
                if (end > start) {
                    canvas.drawText(s.substring(start, end), getLineLeft(line),
                            (float) getLineBaseline(line), pnt);
                }
                String prefix = s.substring(start, Math.max(start, end));
                canvas.drawText("\u2026",
                        getLineLeft(line) + pnt.measureText(prefix),
                        (float) getLineBaseline(line), pnt);
            } else if (end > start) {
                canvas.drawText(s.substring(start, end), getLineLeft(line),
                        (float) getLineBaseline(line), pnt);
            }
        }
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
        return alignment;
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
        CharSequence body;
        if (arg0 == null) {
            body = "";
        } else if (arg1 == 0 && arg2 == 0) {
            body = arg0;
        } else {
            int start = Math.max(0, Math.min(arg1, arg0.length()));
            int end = Math.max(start, Math.min(arg2, arg0.length()));
            body = arg0.subSequence(start, end);
        }
        // AOSP Layout.getDesiredWidthWithLimit walks each '\n' paragraph and
        // returns the widest measurePara width (core/java/android/text/
        // Layout.java:271-296), which is also what Compose's LayoutIntrinsics
        // asks for when sizing a Text node. Deviation: Trim '\r' from CRLF
        // before measuring. AOSP includes it in the paragraph, but Minikin
        // gives control characters zero advance; trimming also keeps the
        // '\r' out of the width in this backend.
        String text = body.toString();
        float widest = 0.0f;
        int widestLength = 0;
        int start = 0;
        while (true) {
            int newline = text.indexOf('\n', start);
            int end = newline < 0 ? text.length() : newline;
            if (end > start && text.charAt(end - 1) == '\r') {
                end--;
            }
            int length = end - start;
            if (length > widestLength) {
                widestLength = length;
            }
            if (arg3 != null && length > 0) {
                widest = Math.max(widest,
                        arg3.measureText(text.substring(start, end)));
            }
            if (newline < 0) break;
            start = newline + 1;
        }
        return arg3 == null ? widestLength * 10.0f : widest;
    }

    public int getEllipsisCount(int arg0) {
        return arg0 == ellipsisLine ? 1 : 0;
    }

    public int getEllipsisStart(int arg0) {
        return arg0 == ellipsisLine ? ellipsisOffset : 0;
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
        return lineStarts.length;
    }

    public int getLineDescent(int arg0) {
        // AOSP StaticLayout.generate stores DESCENT = below + extra
        // (core/java/android/text/StaticLayout.java:1229-1236).
        if (lineDescents.length == 0) return 0;
        return lineDescents[Math.max(0,
                Math.min(arg0, lineDescents.length - 1))];
    }

    /** @hide AOSP StaticLayout.getLineExtra: spacing folded into descent. */
    public int getLineExtra(int arg0) {
        if (lineExtras.length == 0) return 0;
        return lineExtras[Math.max(0, Math.min(arg0, lineExtras.length - 1))];
    }

    public Layout.Directions getLineDirections(int arg0) {
        return null;
    }

    public final int getLineEnd(int arg0) {
        if (text == null) return 0;
        if (lineEnds.length == 0) return text.length();
        return lineEnds[Math.max(0, Math.min(arg0, lineEnds.length - 1))];
    }

    public int getLineForOffset(int arg0) {
        if (lineStarts.length == 0) {
            return 0;
        }
        int length = text == null ? 0 : text.length();
        int offset = Math.max(0, Math.min(arg0, length));
        int line = 0;
        for (int i = 0; i < lineStarts.length; i++) {
            if (lineStarts[i] <= offset) {
                line = i;
            } else {
                break;
            }
        }
        return line;
    }

    public int getLineForVertical(int arg0) {
        if (lineStarts.length == 0) {
            return 0;
        }
        for (int line = 0; line < lineStarts.length; line++) {
            if (arg0 < lineTops[line + 1]) {
                return line;
            }
        }
        return lineStarts.length - 1;
    }

    public float getLineLeft(int arg0) {
        if (lineStarts.length == 0) {
            return 0.0f;
        }
        int line = Math.max(0, Math.min(arg0, lineStarts.length - 1));
        float lineWidth = getLineMax(line);
        float max = Math.max(0.0f, this.width - lineWidth);
        if (alignment == Layout.Alignment.ALIGN_CENTER) {
            return max / 2.0f;
        }
        if (alignment == Layout.Alignment.ALIGN_OPPOSITE) {
            return max;
        }
        return 0.0f;
    }

    public int getLineLetterSpacingUnitCount(int arg0, boolean arg1) {
        return 0;
    }

    public float getLineMax(int arg0) {
        if (text == null || paint == null || lineTextEnds.length == 0) {
            return 0.0f;
        }
        int line = Math.max(0, Math.min(arg0, lineTextEnds.length - 1));
        return paint.measureText(text.subSequence(lineStarts[line],
                lineTextEnds[line]).toString());
    }

    public float getLineRight(int arg0) {
        if (lineStarts.length == 0) {
            return 0.0f;
        }
        int line = Math.max(0, Math.min(arg0, lineStarts.length - 1));
        return getLineLeft(line) + getLineMax(line);
    }

    public final float getLineSpacingAmount() {
        return spacingAdd;
    }

    public final float getLineSpacingMultiplier() {
        return spacingMul;
    }

    public int getLineStart(int arg0) {
        if (lineStarts.length == 0) return 0;
        return lineStarts[Math.max(0,
                Math.min(arg0, lineStarts.length - 1))];
    }

    public int getLineTop(int arg0) {
        // AOSP StaticLayout.getLineTop reads the TOP column; the last entry
        // is the layout bottom (core/java/android/text/StaticLayout.java:
        // 1436-1439 and generate()'s v += (below - above) + extra).
        if (arg0 <= 0 || lineTops.length == 0) return 0;
        if (arg0 >= lineTops.length) return lineTops[lineTops.length - 1];
        return lineTops[arg0];
    }

    public int getLineVisibleEnd(int arg0) {
        if (text == null) return 0;
        if (lineTextEnds.length == 0) return text.length();
        return lineTextEnds[Math.max(0,
                Math.min(arg0, lineTextEnds.length - 1))];
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
        if (lineStarts.length == 0) {
            return 0;
        }
        int line = Math.max(0, Math.min(arg0, lineStarts.length - 1));
        int start = lineStarts[line];
        int end = lineTextEnds[line];
        float relative = Math.max(0.0f, arg1 - getLineLeft(line));
        int count = end - start;
        int best = 0;
        for (int i = 1; i <= count; i++) {
            if (paint.measureText(text.subSequence(start, start + i).toString())
                    <= relative) {
                best = i;
            } else {
                break;
            }
        }
        return start + best;
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
        return alignment;
    }

    public int getParagraphDirection(int arg0) {
        return DIR_LEFT_TO_RIGHT;
    }

    public final int getParagraphLeft(int arg0) {
        return 0;
    }

    public final int getParagraphRight(int arg0) {
        return width;
    }

    public float getPrimaryHorizontal(int arg0) {
        int line = getLineForOffset(arg0);
        int start = getLineStart(line);
        int end = getLineVisibleEnd(line);
        int offset = Math.max(start, Math.min(arg0, end));
        return getLineLeft(line) + paint.measureText(
                text.subSequence(start, offset).toString());
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
            fm.set(paint.getFontMetricsInt());
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
