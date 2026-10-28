package android.text.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;

public abstract class ReplacementSpan extends MetricAffectingSpan {
    private CharSequence mContentDescription = null;

    public abstract int getSize(Paint paint, CharSequence text,
                        int start, int end,
                        Paint.FontMetricsInt fm);

    public abstract void draw(Canvas canvas, CharSequence text,
                              int start, int end, float x,
                              int top, int y, int bottom, Paint paint);

    public CharSequence getContentDescription() {
        return mContentDescription;
    }

    public void setContentDescription(CharSequence contentDescription) {
        mContentDescription = contentDescription;
    }

    @Override
    public void updateMeasureState(TextPaint p) { }

    @Override
    public void updateDrawState(TextPaint ds) { }
}
