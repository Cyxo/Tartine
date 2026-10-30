package android.text;

import android.graphics.Paint;
import android.graphics.text.LineBreakConfig;

public class StaticLayout extends Layout {
    public CharSequence mText;
    public TextPaint mPaint;
    public int mWidth;
    public int mHeight;
    public int mLineCount;
    public int[] mLineStarts;
    public int[] mLineEnds;
    public float[] mLineWidths;
    public int[] mLineTops;
    public boolean mFallbackLineSpacing;

    public StaticLayout() { }

    private StaticLayout(CharSequence source, TextPaint paint, int width,
            Layout.Alignment align, float spacingMult, float spacingAdd,
            boolean includePad, boolean fallbackLineSpacing) {
        super(source == null ? "" : source, paint == null ? new TextPaint() : paint,
                width, align == null ? Layout.Alignment.ALIGN_NORMAL : align,
                spacingMult, spacingAdd, includePad);
        this.mText = (source == null ? "" : source);
        this.mPaint = (paint == null ? new TextPaint() : paint);
        this.mWidth = (width <= 0 ? 480 : width);
        this.mLineCount = (mText.toString().isEmpty() ? 0 : 1);
        this.mFallbackLineSpacing = fallbackLineSpacing;
        if (mLineCount <= 0) {
            mHeight = 0; mLineWidths = new float[0]; mLineStarts = new int[0];
            mLineEnds = new int[0]; mLineTops = new int[0];
            return;
        }
        // Reflect the one-line metrics the Layout base computed (the static
        // layout's public arrays mirror its line table; the guest reads the
        // metrics through Layout.getLine*, not through these).
        this.mLineStarts = new int[] {0};
        this.mLineEnds = new int[] {mText.length()};
        this.mLineWidths = new float[] {mPaint.measureText(mText.toString())};
        this.mLineTops = new int[] {0, height};
        this.mHeight = height;
    }

    public static class Builder {
        private CharSequence source = "";
        private int start = 0;
        private int end = 0;
        private TextPaint paint = new TextPaint();
        private int width = 0;
        private Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;
        private float spacingMul = Layout.DEFAULT_LINESPACING_MULTIPLIER;
        private float spacingAdd = Layout.DEFAULT_LINESPACING_ADDITION;
        private int maxLines = Integer.MAX_VALUE;
        private boolean includePad = false;
        private boolean fallbackLineSpacing = false;

        private Builder() { }

        public static StaticLayout.Builder obtain(CharSequence source, int start, int end, TextPaint paint, int width) {
            StaticLayout.Builder b = new Builder();
            b.source = (source == null ? "" : source);
            b.start = Math.max(0, start);
            b.end = (end <= b.start ? b.source.length() : Math.min(b.source.length(), end));
            if (b.source.length() == 0) b.end = 0;
            b.paint = (paint == null ? new TextPaint() : paint);
            b.width = width;
            return b;
        }

        public Builder setAlignment(Layout.Alignment align) { if (align != null) this.alignment = align; return this; }
        public Builder setBreakStrategy(int breakStrategy) { return this; }
        public Builder setEllipsize(TextUtils.TruncateAt where) { return this; }
        public Builder setEllipsizedWidth(int where) { return this; }
        public Builder setHyphenationFrequency(int frequency) { return this; }
        public Builder setIncludePad(boolean includepad) { this.includePad = includepad; return this; }
        public Builder setIndents(int[] first, int[] rest) { return this; }
        public Builder setJustificationMode(int mode) { return this; }
        public Builder setLineBreakConfig(LineBreakConfig c) { return this; }
        public Builder setLineSpacing(float spacingAdd, float spacingMult) { this.spacingAdd = spacingAdd; this.spacingMul = spacingMult; return this; }
        public Builder setMaxLines(int maxLines) { this.maxLines = maxLines; return this; }
        public Builder setMinimumFontMetrics(Paint.FontMetrics m) { return this; }
        public Builder setShiftDrawingOffsetForStartOverhang(boolean v) { return this; }
        public Builder setText(CharSequence text) { this.source = (text == null ? "" : text); if (this.end > this.source.length()) this.end=this.source.length(); return this; }
        public Builder setTextDirection(TextDirectionHeuristic heuristic) { return this; }
        public Builder setUseBoundsForWidth(boolean v) { return this; }
        public Builder setUseLineSpacingFromFallbacks(boolean v) { this.fallbackLineSpacing = v; return this; }

        public StaticLayout build() { CharSequence cut = source.subSequence(start, Math.min(source.length(), end)); return new StaticLayout(cut, paint, width, alignment, spacingMul, spacingAdd, includePad, fallbackLineSpacing); }
    }
}
