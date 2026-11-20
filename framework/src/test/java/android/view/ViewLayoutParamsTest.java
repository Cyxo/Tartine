package android.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** Relative layout params are physical before a child enters measurement. */
class ViewLayoutParamsTest {

    private static final class CountingLayoutParams extends ViewGroup.MarginLayoutParams {
        int resolveCount;

        CountingLayoutParams() {
            super(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        @Override
        public void resolveLayoutDirection(int layoutDirection) {
            resolveCount++;
            super.resolveLayoutDirection(layoutDirection);
        }
    }

    private static final class RecordingView extends View {
        CountingLayoutParams params;
        int resolveCountAtMeasure;

        RecordingView() {
            super(null);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            resolveCountAtMeasure = params.resolveCount;
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
                    MeasureSpec.getSize(heightMeasureSpec));
        }
    }

    @Test
    void setLayoutParamsResolvesImmediatelyAndMeasureResolvesBeforeOnMeasure() {
        CountingLayoutParams params = new CountingLayoutParams();
        params.setMarginStart(7);
        params.setMarginEnd(11);
        params.setMargins(30, 0, 40, 0);
        RecordingView view = new RecordingView();
        view.params = params;

        view.setLayoutParams(params);

        assertEquals(1, params.resolveCount);
        assertEquals(7, params.leftMargin);
        assertEquals(11, params.rightMargin);

        // Dirty the physical fields again: measure must repeat resolution,
        // not rely solely on the assignment-time pass above.
        params.setMargins(30, 0, 40, 0);
        view.measure(View.MeasureSpec.makeMeasureSpec(20, View.MeasureSpec.MODE_EXACTLY),
                View.MeasureSpec.makeMeasureSpec(10, View.MeasureSpec.MODE_EXACTLY));

        assertEquals(2, view.resolveCountAtMeasure);
        assertEquals(2, params.resolveCount);
        assertEquals(7, params.leftMargin);
        assertEquals(11, params.rightMargin);
    }
}
