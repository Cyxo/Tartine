package android.text;

import android.graphics.Paint;
import android.graphics.RectF;
import android.text.Layout;
import android.text.TextDirectionHeuristic;
import android.text.TextPaint;

public class BoringLayout extends Layout {
    public BoringLayout(CharSequence source, TextPaint paint, int width, Alignment align, float spacingmult, float spacingadd, BoringLayout.Metrics metrics, boolean includePad) {
    }

    public BoringLayout(CharSequence source, TextPaint paint, int width, Alignment align, float spacingmult, float spacingadd, BoringLayout.Metrics metrics, boolean includePad, TextUtils.TruncateAt ellipsize, int ellipsizedWidth) {
    }

    public BoringLayout(CharSequence source, TextPaint paint, int width, Alignment align, float spacingmult, float spacingadd, BoringLayout.Metrics metrics, boolean includePad, TextUtils.TruncateAt ellipsize, int ellipsizedWidth, boolean unknown) {
    }

    public static class Metrics extends Paint.FontMetricsInt {
        public int width;
        public Metrics() { }
        public RectF getDrawingBoundingBox() { return new RectF(); }
        public String toString() { return ""; }
    }

    public static BoringLayout.Metrics isBoring(CharSequence source, TextPaint paint) { return null; }
    public static BoringLayout.Metrics isBoring(CharSequence source, TextPaint paint, BoringLayout.Metrics metrics) { return null; }
    public static BoringLayout.Metrics isBoring(CharSequence source, TextPaint paint, TextDirectionHeuristic heuristic, boolean includePad, BoringLayout.Metrics metrics) { return null; }

    public static BoringLayout make(CharSequence source, TextPaint paint, int width, Alignment align, BoringLayout.Metrics metrics, boolean includePad,
                                     TextUtils.TruncateAt ellipsize, int ellipsizedWidth, boolean unknown) {
        return null;
    }

    public static BoringLayout make(CharSequence source, TextPaint paint, int width, Alignment align, float spacingmult, float spacingadd, BoringLayout.Metrics metrics, boolean includePad) {
        return null;
    }

    public static BoringLayout make(CharSequence source, TextPaint paint, int width, Alignment align, float spacingmult, float spacingadd, BoringLayout.Metrics metrics, boolean includePad, TextUtils.TruncateAt ellipsize, int ellipsizedWidth) {
        return null;
    }

    public BoringLayout replaceOrMake(CharSequence source, TextPaint paint, int width, Alignment align, BoringLayout.Metrics metrics, boolean includePad, TextUtils.TruncateAt ellipsize, int ellipsizedWidth, boolean unknown) { return null; }
    public BoringLayout replaceOrMake(CharSequence source, TextPaint paint, int width, Alignment align, float spacingmult, float spacingadd, BoringLayout.Metrics metrics, boolean includePad) { return null; }
    public BoringLayout replaceOrMake(CharSequence source, TextPaint paint, int width, Alignment align, float spacingmult, float spacingadd, BoringLayout.Metrics metrics, boolean includePad, TextUtils.TruncateAt ellipsize, int ellipsizedWidth) { return null; }
}
