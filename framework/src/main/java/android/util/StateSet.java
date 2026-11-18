package android.util;

/**
 * AOSP {@code android.util.StateSet}: state-array matching and the packed
 * {@code android.R.attr.state_*} ids used by {@link StateListDrawable}-style
 * resources.
 *
 * <p>The state ids are the compiled {@code android.R.attr} values (the same
 * ones an AXML resource map carries), exposed here so the framework's view
 * state and drawable selectors agree without a generated {@code android.R}
 * table.
 */
public final class StateSet {

    /** AOSP StateSet.WILD_CARD: an empty state list matches every state. */
    public static final int[] WILD_CARD = new int[0];

    public static final int STATE_FOCUSED = 0x0101009C;
    public static final int STATE_WINDOW_FOCUSED = 0x0101009D;
    public static final int STATE_ENABLED = 0x0101009E;
    public static final int STATE_CHECKABLE = 0x0101009F;
    public static final int STATE_CHECKED = 0x010100A0;
    public static final int STATE_SELECTED = 0x010100A1;
    public static final int STATE_ACTIVE = 0x010100A2;
    public static final int STATE_PRESSED = 0x010100A7;
    public static final int STATE_ACTIVATED = 0x010102FE;
    public static final int STATE_ACCELERATED = 0x0101031B;
    public static final int STATE_HOVERED = 0x01010367;
    public static final int STATE_DRAG_CAN_ACCEPT = 0x01010368;
    public static final int STATE_DRAG_HOVERED = 0x01010369;

    private StateSet() {
    }

    /**
     * AOSP StateSet.stateSetMatches: a wild-card spec matches anything;
     * otherwise every positive spec entry must be present and every negated
     * entry (a state id) must be absent.
     */
    public static boolean stateSetMatches(int[] stateSpec, int[] stateSet) {
        if (stateSet == null) {
            return stateSpec == null || stateSpec.length == 0;
        }
        for (int spec : stateSpec) {
            if (spec == 0) {
                continue;
            }
            final int wanted = Math.abs(spec);
            final boolean desired = spec > 0;
            boolean found = false;
            for (int state : stateSet) {
                if (state == wanted) {
                    found = true;
                    break;
                }
            }
            if (desired != found) {
                return false;
            }
        }
        return true;
    }

    /** AOSP StateSet.stateSetMatches against one current state id. */
    public static boolean stateSetMatches(int[] stateSpec, int state) {
        if (stateSpec == null) {
            return true;
        }
        for (int spec : stateSpec) {
            if (spec == 0) {
                continue;
            }
            if (Math.abs(spec) == state) {
                return spec > 0;
            }
        }
        return stateSpec.length == 0;
    }

    /** AOSP StateSet.trimStateSet: copies the first {@code size} entries. */
    public static int[] trimStateSet(int[] states, int newSize) {
        if (states.length == newSize) {
            return states;
        }
        int[] trimmed = new int[newSize];
        System.arraycopy(states, 0, trimmed, 0, newSize);
        return trimmed;
    }
}
