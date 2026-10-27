package android.graphics.text;

public class LineBreakConfig {
    public static class Builder {
        public Builder() {
        }
        public Builder setLineBreakStyle(int s) { return this; }
        public Builder setLineBreakWordStyle(int w) { return this; }
        public Builder setHyphenationFrequency(int f) { return this; }
        public LineBreakConfig build() { return new LineBreakConfig(); }
    }
}
