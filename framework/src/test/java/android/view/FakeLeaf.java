package android.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Test-only leaf view: carries the {@code (Context, AttributeSet)}
 * constructor the inflater resolves element names against, so a layout can be
 * built without the platform {@code android.widget} toolkit (not ported).
 */
public class FakeLeaf extends View {

    public FakeLeaf(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
