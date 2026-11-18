package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * AOSP {@code android.widget.Space}: an invisible spacer whose only job is to
 * reserve the size its layout params request.
 */
public class Space extends View {

    public Space(Context context) {
        this(context, null);
    }

    public Space(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Space(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public Space(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                resolveSizeAndState(
                        getPaddingLeft() + getPaddingRight(),
                        widthMeasureSpec, 0),
                resolveSizeAndState(
                        getPaddingTop() + getPaddingBottom(),
                        heightMeasureSpec, 0));
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return "android.widget.Space";
    }
}
