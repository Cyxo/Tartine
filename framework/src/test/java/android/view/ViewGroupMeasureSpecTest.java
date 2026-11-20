package android.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** The AOSP parent-spec/child-dimension measure-spec table. */
class ViewGroupMeasureSpecTest {

    @Test
    void childMeasureSpecCoversEveryParentModeAndChildDimension() {
        final int available = 90;
        int[][] cases = {
            {View.MeasureSpec.MODE_EXACTLY, 37,
                    View.MeasureSpec.MODE_EXACTLY, 37},
            {View.MeasureSpec.MODE_EXACTLY, ViewGroup.LayoutParams.MATCH_PARENT,
                    View.MeasureSpec.MODE_EXACTLY, available},
            {View.MeasureSpec.MODE_EXACTLY, ViewGroup.LayoutParams.WRAP_CONTENT,
                    View.MeasureSpec.MODE_AT_MOST, available},
            {View.MeasureSpec.MODE_AT_MOST, 37,
                    View.MeasureSpec.MODE_EXACTLY, 37},
            {View.MeasureSpec.MODE_AT_MOST, ViewGroup.LayoutParams.MATCH_PARENT,
                    View.MeasureSpec.MODE_AT_MOST, available},
            {View.MeasureSpec.MODE_AT_MOST, ViewGroup.LayoutParams.WRAP_CONTENT,
                    View.MeasureSpec.MODE_AT_MOST, available},
            {View.MeasureSpec.MODE_UNSPECIFIED, 37,
                    View.MeasureSpec.MODE_EXACTLY, 37},
            {View.MeasureSpec.MODE_UNSPECIFIED, ViewGroup.LayoutParams.MATCH_PARENT,
                    View.MeasureSpec.MODE_UNSPECIFIED, available},
            {View.MeasureSpec.MODE_UNSPECIFIED, ViewGroup.LayoutParams.WRAP_CONTENT,
                    View.MeasureSpec.MODE_UNSPECIFIED, available}
        };

        for (int[] testCase : cases) {
            assertChildSpec(testCase[0], 100, 10, testCase[1],
                    testCase[2], testCase[3]);
        }
    }

    @Test
    void childMeasureSpecClampsUnavailableSpaceToZero() {
        int[] parentModes = {
            View.MeasureSpec.MODE_EXACTLY,
            View.MeasureSpec.MODE_AT_MOST,
            View.MeasureSpec.MODE_UNSPECIFIED
        };
        int[] childDimensions = {
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        };

        for (int parentMode : parentModes) {
            assertChildSpec(parentMode, 5, 9, 37, View.MeasureSpec.MODE_EXACTLY, 37);
            for (int childDimension : childDimensions) {
                int expectedMode = parentMode == View.MeasureSpec.MODE_UNSPECIFIED
                        ? View.MeasureSpec.MODE_UNSPECIFIED
                        : parentMode == View.MeasureSpec.MODE_EXACTLY
                                && childDimension == ViewGroup.LayoutParams.MATCH_PARENT
                        ? View.MeasureSpec.MODE_EXACTLY : View.MeasureSpec.MODE_AT_MOST;
                assertChildSpec(parentMode, 5, 9, childDimension, expectedMode, 0);
            }
        }
    }

    private static void assertChildSpec(int parentMode, int parentSize, int padding,
            int childDimension, int expectedMode, int expectedSize) {
        int parentSpec = View.MeasureSpec.makeMeasureSpec(parentSize, parentMode);
        int childSpec = ViewGroup.getChildMeasureSpec(parentSpec, padding, childDimension);
        String description = "parentMode=" + parentMode + ", parentSize=" + parentSize
                + ", padding=" + padding + ", childDimension=" + childDimension;

        assertEquals(expectedMode, View.MeasureSpec.getMode(childSpec), description);
        assertEquals(expectedSize, View.MeasureSpec.getSize(childSpec), description);
    }
}
