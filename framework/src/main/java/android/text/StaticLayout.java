package android.text;

public class StaticLayout extends Layout {
    public StaticLayout() {
    }

    public static class Builder {
        private Builder() {
        }

        public static StaticLayout.Builder obtain(CharSequence source, int start, int end, TextPaint paint, int width) {
            return new Builder();
        }

        public Builder setAlignment(Layout.Alignment align) { return this; }
        public Builder setBreakStrategy(int breakStrategy) { return this; }
        public Builder setEllipsize(TextUtils.TruncateAt where) { return this; }
        public Builder setEllipsizedWidth(int width) { return this; }
        public Builder setHyphenationFrequency(int frequency) { return this; }
        public Builder setIncludePad(boolean includepad) { return this; }
        public Builder setIndents(int[] first, int[] rest) { return this; }
        public Builder setJustificationMode(int mode) { return this; }
        public Builder setLineBreakConfig(android.graphics.text.LineBreakConfig c) { return this; }
        public Builder setLineSpacing(float spacingAdd, float spacingMult) { return this; }
        public Builder setMaxLines(int maxLines) { return this; }
        public Builder setMinimumFontMetrics(android.graphics.Paint.FontMetrics m) { return this; }
        public Builder setShiftDrawingOffsetForStartOverhang(boolean v) { return this; }
        public Builder setText(CharSequence text) { return this; }
        public Builder setTextDirection(TextDirectionHeuristic heuristic) { return this; }
        public Builder setUseBoundsForWidth(boolean v) { return this; }
        public Builder setUseLineSpacingFromFallbacks(boolean v) { return this; }

        public StaticLayout build() {
            return new StaticLayout();
        }
    }
}
