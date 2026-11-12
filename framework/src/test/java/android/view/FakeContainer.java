package android.view;

import android.content.Context;
import android.util.AttributeSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Test-only container for the inflation path: records the parent-side calls
 * (layout-param generation, child attach, finish-inflate) so the inflater's
 * contract can be asserted without the platform widget toolkit, which is not
 * ported.
 */
public class FakeContainer extends ViewGroup {

    /** Children attached during inflation, in order. */
    public final List<View> attached = new ArrayList<>();
    /** How many times {@code onFinishInflate} ran. */
    int finishInflateCount;
    /** How many children asked the container to generate their params. */
    int generateParamsCount;

    public FakeContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        attached.add(child);
        super.addView(child, params);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        generateParamsCount++;
        return new ViewGroup.LayoutParams(123, 456);
    }

    @Override
    protected void onFinishInflate() {
        finishInflateCount++;
    }
}
