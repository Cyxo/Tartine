package android.view;

import android.graphics.Matrix;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

    public class MotionEvent extends InputEvent implements Parcelable {

    public static final int ACTION_BUTTON_PRESS = 11;
    public static final int ACTION_BUTTON_RELEASE = 12;
    public static final int ACTION_CANCEL = 3;
    public static final int ACTION_DOWN = 0;
    public static final int ACTION_HOVER_ENTER = 9;
    public static final int ACTION_HOVER_EXIT = 10;
    public static final int ACTION_HOVER_MOVE = 7;
    public static final int ACTION_MASK = 255;
    public static final int ACTION_MOVE = 2;
    public static final int ACTION_OUTSIDE = 4;
    public static final int ACTION_POINTER_1_DOWN = 5;
    public static final int ACTION_POINTER_1_UP = 6;
    public static final int ACTION_POINTER_2_DOWN = 261;
    public static final int ACTION_POINTER_2_UP = 262;
    public static final int ACTION_POINTER_3_DOWN = 517;
    public static final int ACTION_POINTER_3_UP = 518;
    public static final int ACTION_POINTER_DOWN = 5;
    public static final int ACTION_POINTER_ID_MASK = 65280;
    public static final int ACTION_POINTER_ID_SHIFT = 8;
    public static final int ACTION_POINTER_INDEX_MASK = 65280;
    public static final int ACTION_POINTER_INDEX_SHIFT = 8;
    public static final int ACTION_POINTER_UP = 6;
    public static final int ACTION_SCROLL = 8;
    public static final int ACTION_UP = 1;
    public static final int AXIS_BRAKE = 23;
    public static final int AXIS_DISTANCE = 24;
    public static final int AXIS_GAS = 22;
    public static final int AXIS_GENERIC_1 = 32;
    public static final int AXIS_GENERIC_10 = 41;
    public static final int AXIS_GENERIC_11 = 42;
    public static final int AXIS_GENERIC_12 = 43;
    public static final int AXIS_GENERIC_13 = 44;
    public static final int AXIS_GENERIC_14 = 45;
    public static final int AXIS_GENERIC_15 = 46;
    public static final int AXIS_GENERIC_16 = 47;
    public static final int AXIS_GENERIC_2 = 33;
    public static final int AXIS_GENERIC_3 = 34;
    public static final int AXIS_GENERIC_4 = 35;
    public static final int AXIS_GENERIC_5 = 36;
    public static final int AXIS_GENERIC_6 = 37;
    public static final int AXIS_GENERIC_7 = 38;
    public static final int AXIS_GENERIC_8 = 39;
    public static final int AXIS_GENERIC_9 = 40;
    public static final int AXIS_GESTURE_PINCH_SCALE_FACTOR = 52;
    public static final int AXIS_GESTURE_SCROLL_X_DISTANCE = 50;
    public static final int AXIS_GESTURE_SCROLL_Y_DISTANCE = 51;
    public static final int AXIS_GESTURE_X_OFFSET = 48;
    public static final int AXIS_GESTURE_Y_OFFSET = 49;
    public static final int AXIS_HAT_X = 15;
    public static final int AXIS_HAT_Y = 16;
    public static final int AXIS_HSCROLL = 10;
    public static final int AXIS_LTRIGGER = 17;
    public static final int AXIS_ORIENTATION = 8;
    public static final int AXIS_PRESSURE = 2;
    public static final int AXIS_RELATIVE_X = 27;
    public static final int AXIS_RELATIVE_Y = 28;
    public static final int AXIS_RTRIGGER = 18;
    public static final int AXIS_RUDDER = 20;
    public static final int AXIS_RX = 12;
    public static final int AXIS_RY = 13;
    public static final int AXIS_RZ = 14;
    public static final int AXIS_SCROLL = 26;
    public static final int AXIS_SIZE = 3;
    public static final int AXIS_THROTTLE = 19;
    public static final int AXIS_TILT = 25;
    public static final int AXIS_TOOL_MAJOR = 6;
    public static final int AXIS_TOOL_MINOR = 7;
    public static final int AXIS_TOUCH_MAJOR = 4;
    public static final int AXIS_TOUCH_MINOR = 5;
    public static final int AXIS_VSCROLL = 9;
    public static final int AXIS_WHEEL = 21;
    public static final int AXIS_X = 0;
    public static final int AXIS_Y = 1;
    public static final int AXIS_Z = 11;
    public static final int BUTTON_BACK = 8;
    public static final int BUTTON_FORWARD = 16;
    public static final int BUTTON_PRIMARY = 1;
    public static final int BUTTON_SECONDARY = 2;
    public static final int BUTTON_STYLUS_PRIMARY = 32;
    public static final int BUTTON_STYLUS_SECONDARY = 64;
    public static final int BUTTON_TERTIARY = 4;
    public static final int CLASSIFICATION_AMBIGUOUS_GESTURE = 1;
    public static final int CLASSIFICATION_DEEP_PRESS = 2;
    public static final int CLASSIFICATION_NONE = 0;
    public static final int CLASSIFICATION_PINCH = 5;
    public static final int CLASSIFICATION_TWO_FINGER_SWIPE = 3;
    public static Parcelable.Creator CREATOR;
    public static final int EDGE_BOTTOM = 2;
    public static final int EDGE_LEFT = 4;
    public static final int EDGE_RIGHT = 8;
    public static final int EDGE_TOP = 1;
    public static final int FLAG_CANCELED = 32;
    public static final int FLAG_WINDOW_IS_OBSCURED = 1;
    public static final int FLAG_WINDOW_IS_PARTIALLY_OBSCURED = 2;
    public static final int INVALID_POINTER_ID = -1;
    public static final int TOOL_TYPE_ERASER = 4;
    public static final int TOOL_TYPE_FINGER = 1;
    public static final int TOOL_TYPE_MOUSE = 3;
    public static final int TOOL_TYPE_STYLUS = 2;
    public static final int TOOL_TYPE_UNKNOWN = 0;

    /** One historical sample: its event time and per-pointer coords. */
    private static final class HistoricalSample {
        final long time;
        final PointerCoords[] coords;

        HistoricalSample(long time, PointerCoords[] coords) {
            this.time = time;
            this.coords = coords;
        }
    }

    private long mDownTime;
    private long mEventTime;
    private int mAction;
    private int mEdgeFlags;
    private int mMetaState;
    private int mButtonState;
    private int mFlags;
    private int mDeviceId;
    private int mSource;
    private float mXOffset;
    private float mYOffset;
    private float mXPrecision = 1.0f;
    private float mYPrecision = 1.0f;
    private PointerProperties[] mPointerProperties;
    private PointerCoords[] mPointerCoords;
    private int mPointerCount;
    private final List<HistoricalSample> mHistory = new ArrayList<>();

    /** Pool-free constructions all funnel here (desktop host, no recycling). */
    private MotionEvent() {
    }

    public static String actionToString(int arg0) {
        switch (arg0) {
            case ACTION_DOWN:
                return "ACTION_DOWN";
            case ACTION_UP:
                return "ACTION_UP";
            case ACTION_CANCEL:
                return "ACTION_CANCEL";
            case ACTION_OUTSIDE:
                return "ACTION_OUTSIDE";
            case ACTION_MOVE:
                return "ACTION_MOVE";
            case ACTION_HOVER_MOVE:
                return "ACTION_HOVER_MOVE";
            case ACTION_HOVER_ENTER:
                return "ACTION_HOVER_ENTER";
            case ACTION_HOVER_EXIT:
                return "ACTION_HOVER_EXIT";
            case ACTION_SCROLL:
                return "ACTION_SCROLL";
            case ACTION_BUTTON_PRESS:
                return "ACTION_BUTTON_PRESS";
            case ACTION_BUTTON_RELEASE:
                return "ACTION_BUTTON_RELEASE";
            default:
                int index = arg0 & ACTION_POINTER_INDEX_MASK;
                int masked = arg0 & ACTION_MASK;
                if (masked == ACTION_POINTER_DOWN) {
                    return "ACTION_POINTER_DOWN("
                            + (index >> ACTION_POINTER_INDEX_SHIFT) + ")";
                }
                if (masked == ACTION_POINTER_UP) {
                    return "ACTION_POINTER_UP("
                            + (index >> ACTION_POINTER_INDEX_SHIFT) + ")";
                }
                return Integer.toString(arg0);
        }
    }

    public void addBatch(long arg0, MotionEvent.PointerCoords[] arg1, int arg2) {
        PointerCoords[] copy = new PointerCoords[mPointerCount];
        for (int i = 0; i < mPointerCount; i++) {
            copy[i] = new PointerCoords(arg1[i]);
        }
        mHistory.add(new HistoricalSample(arg0, copy));
    }

    public void addBatch(long arg0, float arg1, float arg2, float arg3,
            float arg4, int arg5) {
        PointerCoords coords = new PointerCoords();
        coords.x = arg1;
        coords.y = arg2;
        coords.pressure = arg3;
        coords.size = arg4;
        PointerCoords[] copy = new PointerCoords[mPointerCount];
        for (int i = 0; i < mPointerCount; i++) {
            copy[i] = new PointerCoords(coords);
        }
        mHistory.add(new HistoricalSample(arg0, copy));
    }

    public static int axisFromString(String arg0) {
        if (arg0 == null) {
            return -1;
        }
        switch (arg0) {
            case "AXIS_X":
                return AXIS_X;
            case "AXIS_Y":
                return AXIS_Y;
            case "AXIS_PRESSURE":
                return AXIS_PRESSURE;
            case "AXIS_SIZE":
                return AXIS_SIZE;
            case "AXIS_TOUCH_MAJOR":
                return AXIS_TOUCH_MAJOR;
            case "AXIS_TOUCH_MINOR":
                return AXIS_TOUCH_MINOR;
            case "AXIS_TOOL_MAJOR":
                return AXIS_TOOL_MAJOR;
            case "AXIS_TOOL_MINOR":
                return AXIS_TOOL_MINOR;
            case "AXIS_ORIENTATION":
                return AXIS_ORIENTATION;
            case "AXIS_VSCROLL":
                return AXIS_VSCROLL;
            case "AXIS_HSCROLL":
                return AXIS_HSCROLL;
            case "AXIS_Z":
                return AXIS_Z;
            case "AXIS_RX":
                return AXIS_RX;
            case "AXIS_RY":
                return AXIS_RY;
            case "AXIS_RZ":
                return AXIS_RZ;
            case "AXIS_HAT_X":
                return AXIS_HAT_X;
            case "AXIS_HAT_Y":
                return AXIS_HAT_Y;
            case "AXIS_LTRIGGER":
                return AXIS_LTRIGGER;
            case "AXIS_RTRIGGER":
                return AXIS_RTRIGGER;
            case "AXIS_THROTTLE":
                return AXIS_THROTTLE;
            case "AXIS_RUDDER":
                return AXIS_RUDDER;
            case "AXIS_WHEEL":
                return AXIS_WHEEL;
            case "AXIS_GAS":
                return AXIS_GAS;
            case "AXIS_BRAKE":
                return AXIS_BRAKE;
            case "AXIS_DISTANCE":
                return AXIS_DISTANCE;
            case "AXIS_TILT":
                return AXIS_TILT;
            case "AXIS_SCROLL":
                return AXIS_SCROLL;
            case "AXIS_RELATIVE_X":
                return AXIS_RELATIVE_X;
            case "AXIS_RELATIVE_Y":
                return AXIS_RELATIVE_Y;
            default:
                return -1;
        }
    }

    public static String axisToString(int arg0) {
        switch (arg0) {
            case AXIS_X:
                return "AXIS_X";
            case AXIS_Y:
                return "AXIS_Y";
            case AXIS_PRESSURE:
                return "AXIS_PRESSURE";
            case AXIS_SIZE:
                return "AXIS_SIZE";
            case AXIS_TOUCH_MAJOR:
                return "AXIS_TOUCH_MAJOR";
            case AXIS_TOUCH_MINOR:
                return "AXIS_TOUCH_MINOR";
            case AXIS_TOOL_MAJOR:
                return "AXIS_TOOL_MAJOR";
            case AXIS_TOOL_MINOR:
                return "AXIS_TOOL_MINOR";
            case AXIS_ORIENTATION:
                return "AXIS_ORIENTATION";
            case AXIS_VSCROLL:
                return "AXIS_VSCROLL";
            case AXIS_HSCROLL:
                return "AXIS_HSCROLL";
            case AXIS_Z:
                return "AXIS_Z";
            case AXIS_RX:
                return "AXIS_RX";
            case AXIS_RY:
                return "AXIS_RY";
            case AXIS_RZ:
                return "AXIS_RZ";
            case AXIS_HAT_X:
                return "AXIS_HAT_X";
            case AXIS_HAT_Y:
                return "AXIS_HAT_Y";
            case AXIS_LTRIGGER:
                return "AXIS_LTRIGGER";
            case AXIS_RTRIGGER:
                return "AXIS_RTRIGGER";
            case AXIS_THROTTLE:
                return "AXIS_THROTTLE";
            case AXIS_RUDDER:
                return "AXIS_RUDDER";
            case AXIS_WHEEL:
                return "AXIS_WHEEL";
            case AXIS_GAS:
                return "AXIS_GAS";
            case AXIS_BRAKE:
                return "AXIS_BRAKE";
            case AXIS_DISTANCE:
                return "AXIS_DISTANCE";
            case AXIS_TILT:
                return "AXIS_TILT";
            case AXIS_SCROLL:
                return "AXIS_SCROLL";
            case AXIS_RELATIVE_X:
                return "AXIS_RELATIVE_X";
            case AXIS_RELATIVE_Y:
                return "AXIS_RELATIVE_Y";
            default:
                return Integer.toString(arg0);
        }
    }

    protected void finalize() {
    }

    public int findPointerIndex(int arg0) {
        for (int i = 0; i < mPointerCount; i++) {
            if (mPointerProperties[i].id == arg0) {
                return i;
            }
        }
        return -1;
    }

    public int getAction() {
        return mAction;
    }

    public int getActionButton() {
        return 0;
    }

    public int getActionIndex() {
        return (mAction & ACTION_POINTER_INDEX_MASK)
                >> ACTION_POINTER_INDEX_SHIFT;
    }

    public int getActionMasked() {
        return mAction & ACTION_MASK;
    }

    public float getAxisValue(int arg0) {
        return getAxisValue(arg0, 0);
    }

    public float getAxisValue(int arg0, int arg1) {
        return mPointerCoords[arg1].getAxisValue(arg0);
    }

    public int getButtonState() {
        return mButtonState;
    }

    public int getClassification() {
        return CLASSIFICATION_NONE;
    }

    public int getDeviceId() {
        return mDeviceId;
    }

    public long getDownTime() {
        return mDownTime;
    }

    public int getEdgeFlags() {
        return mEdgeFlags;
    }

    public long getEventTime() {
        return mEventTime;
    }

    public long getEventTimeNanos() {
        return mEventTime * 1_000_000L;
    }

    public int getFlags() {
        return mFlags;
    }

    public float getHistoricalAxisValue(int arg0, int arg1) {
        return getHistoricalAxisValue(arg0, 0, arg1);
    }

    public float getHistoricalAxisValue(int arg0, int arg1, int arg2) {
        return mHistory.get(arg2).coords[arg1].getAxisValue(arg0);
    }

    public long getHistoricalEventTime(int arg0) {
        return mHistory.get(arg0).time;
    }

    public long getHistoricalEventTimeNanos(int arg0) {
        return mHistory.get(arg0).time * 1_000_000L;
    }

    public float getHistoricalOrientation(int arg0) {
        return getHistoricalOrientation(arg0, 0);
    }

    public float getHistoricalOrientation(int arg0, int arg1) {
        return mHistory.get(arg0).coords[arg1].orientation;
    }

    public void getHistoricalPointerCoords(int arg0, int arg1,
            MotionEvent.PointerCoords arg2) {
        arg2.copyFrom(mHistory.get(arg0).coords[arg1]);
    }

    public float getHistoricalPressure(int arg0) {
        return getHistoricalPressure(arg0, 0);
    }

    public float getHistoricalPressure(int arg0, int arg1) {
        return mHistory.get(arg0).coords[arg1].pressure;
    }

    public float getHistoricalSize(int arg0) {
        return getHistoricalSize(arg0, 0);
    }

    public float getHistoricalSize(int arg0, int arg1) {
        return mHistory.get(arg0).coords[arg1].size;
    }

    public float getHistoricalToolMajor(int arg0) {
        return getHistoricalToolMajor(arg0, 0);
    }

    public float getHistoricalToolMajor(int arg0, int arg1) {
        return mHistory.get(arg0).coords[arg1].toolMajor;
    }

    public float getHistoricalToolMinor(int arg0) {
        return getHistoricalToolMinor(arg0, 0);
    }

    public float getHistoricalToolMinor(int arg0, int arg1) {
        return mHistory.get(arg0).coords[arg1].toolMinor;
    }

    public float getHistoricalTouchMajor(int arg0) {
        return getHistoricalTouchMajor(arg0, 0);
    }

    public float getHistoricalTouchMajor(int arg0, int arg1) {
        return mHistory.get(arg0).coords[arg1].touchMajor;
    }

    public float getHistoricalTouchMinor(int arg0) {
        return getHistoricalTouchMinor(arg0, 0);
    }

    public float getHistoricalTouchMinor(int arg0, int arg1) {
        return mHistory.get(arg0).coords[arg1].touchMinor;
    }

    public float getHistoricalX(int arg0) {
        return getHistoricalX(arg0, 0);
    }

    public float getHistoricalX(int arg0, int arg1) {
        return mHistory.get(arg0).coords[arg1].x;
    }

    public float getHistoricalY(int arg0) {
        return getHistoricalY(arg0, 0);
    }

    public float getHistoricalY(int arg0, int arg1) {
        return mHistory.get(arg0).coords[arg1].y;
    }

    public int getHistorySize() {
        return mHistory.size();
    }

    public int getMetaState() {
        return mMetaState;
    }

    public float getOrientation() {
        return getOrientation(0);
    }

    public float getOrientation(int arg0) {
        return mPointerCoords[arg0].orientation;
    }

    public void getPointerCoords(int arg0, MotionEvent.PointerCoords arg1) {
        arg1.copyFrom(mPointerCoords[arg0]);
    }

    public int getPointerCount() {
        return mPointerCount;
    }

    public int getPointerId(int arg0) {
        return mPointerProperties[arg0].id;
    }

    public void getPointerProperties(int arg0,
            MotionEvent.PointerProperties arg1) {
        arg1.copyFrom(mPointerProperties[arg0]);
    }

    public float getPressure() {
        return getPressure(0);
    }

    public float getPressure(int arg0) {
        return mPointerCoords[arg0].pressure;
    }

    public float getRawX() {
        return getRawX(0);
    }

    public float getRawX(int arg0) {
        // AOSP: the raw location is the pointer location before the
        // containing-window/view adjustment offsetLocation applied. The
        // coords already carry that adjustment, so undo the accumulator.
        return mPointerCoords[arg0].x - mXOffset;
    }

    public float getRawY() {
        return getRawY(0);
    }

    public float getRawY(int arg0) {
        return mPointerCoords[arg0].y - mYOffset;
    }

    public float getSize() {
        return getSize(0);
    }

    public float getSize(int arg0) {
        return mPointerCoords[arg0].size;
    }

    public int getSource() {
        return mSource;
    }

    public float getToolMajor() {
        return getToolMajor(0);
    }

    public float getToolMajor(int arg0) {
        return mPointerCoords[arg0].toolMajor;
    }

    public float getToolMinor() {
        return getToolMinor(0);
    }

    public float getToolMinor(int arg0) {
        return mPointerCoords[arg0].toolMinor;
    }

    public int getToolType(int arg0) {
        return mPointerProperties[arg0].toolType;
    }

    public float getTouchMajor() {
        return getTouchMajor(0);
    }

    public float getTouchMajor(int arg0) {
        return mPointerCoords[arg0].touchMajor;
    }

    public float getTouchMinor() {
        return getTouchMinor(0);
    }

    public float getTouchMinor(int arg0) {
        return mPointerCoords[arg0].touchMinor;
    }

    public float getX() {
        return getX(0);
    }

    public float getX(int arg0) {
        return mPointerCoords[arg0].x;
    }

    public float getXPrecision() {
        return mXPrecision;
    }

    public float getY() {
        return getY(0);
    }

    public float getY(int arg0) {
        return mPointerCoords[arg0].y;
    }

    public float getYPrecision() {
        return mYPrecision;
    }

    public boolean isButtonPressed(int arg0) {
        return arg0 != 0 && (mButtonState & arg0) == arg0;
    }

    public static MotionEvent obtain(MotionEvent arg0) {
        MotionEvent event = new MotionEvent();
        event.mDownTime = arg0.mDownTime;
        event.mEventTime = arg0.mEventTime;
        event.mAction = arg0.mAction;
        event.mEdgeFlags = arg0.mEdgeFlags;
        event.mMetaState = arg0.mMetaState;
        event.mButtonState = arg0.mButtonState;
        event.mFlags = arg0.mFlags;
        event.mDeviceId = arg0.mDeviceId;
        event.mSource = arg0.mSource;
        event.mXOffset = arg0.mXOffset;
        event.mYOffset = arg0.mYOffset;
        event.mXPrecision = arg0.mXPrecision;
        event.mYPrecision = arg0.mYPrecision;
        event.mPointerCount = arg0.mPointerCount;
        event.mPointerProperties = new PointerProperties[event.mPointerCount];
        event.mPointerCoords = new PointerCoords[event.mPointerCount];
        for (int i = 0; i < event.mPointerCount; i++) {
            event.mPointerProperties[i] =
                    new PointerProperties(arg0.mPointerProperties[i]);
            event.mPointerCoords[i] = new PointerCoords(arg0.mPointerCoords[i]);
        }
        event.mHistory.addAll(arg0.mHistory);
        return event;
    }

    public static MotionEvent obtain(long arg0, long arg1, int arg2, float arg3,
            float arg4, float arg5, float arg6, int arg7, float arg8, float arg9,
            int arg10, int arg11) {
        MotionEvent event = obtain(arg0, arg1, arg2, arg3, arg4, arg11);
        event.mMetaState = arg7;
        event.mXPrecision = arg8;
        event.mYPrecision = arg9;
        event.mDeviceId = arg10;
        event.mPointerCoords[0].pressure = arg5;
        event.mPointerCoords[0].size = arg6;
        return event;
    }

    public static MotionEvent obtain(long arg0, long arg1, int arg2, float arg3,
            float arg4, int arg5) {
        PointerProperties properties = new PointerProperties();
        properties.id = 0;
        properties.toolType = TOOL_TYPE_FINGER;
        PointerCoords coords = new PointerCoords();
        coords.x = arg3;
        coords.y = arg4;
        coords.pressure = 1.0f;
        coords.size = 1.0f;
        // AOSP: the trailing int is the meta state; the event always has one
        // pointer (id 0). Passing arg5 as the pointer count produced an
        // empty coordinate array and crashed every later getX/getY.
        return obtain(arg0, arg1, arg2, 1,
                new PointerProperties[] {properties},
                new PointerCoords[] {coords}, arg5, 0, 1.0f, 1.0f, 0, 0, 0, 0);
    }

    public static MotionEvent obtain(long arg0, long arg1, int arg2, int arg3,
            MotionEvent.PointerProperties[] arg4, MotionEvent.PointerCoords[] arg5,
            int arg6, int arg7, float arg8, float arg9, int arg10, int arg11,
            int arg12, int arg13) {
        return obtain(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8,
                arg9, arg10, arg11, arg12, arg13, 0, 0);
    }

    public static MotionEvent obtain(long arg0, long arg1, int arg2, int arg3,
            MotionEvent.PointerProperties[] arg4, MotionEvent.PointerCoords[] arg5,
            int arg6, int arg7, float arg8, float arg9, int arg10, int arg11,
            int arg12, int arg13, int arg14, int arg15) {
        MotionEvent event = new MotionEvent();
        event.mDownTime = arg0;
        event.mEventTime = arg1;
        event.mAction = arg2;
        event.mPointerCount = arg3;
        event.mPointerProperties = new PointerProperties[arg3];
        event.mPointerCoords = new PointerCoords[arg3];
        for (int i = 0; i < arg3; i++) {
            event.mPointerProperties[i] = new PointerProperties(arg4[i]);
            event.mPointerCoords[i] = new PointerCoords(arg5[i]);
        }
        event.mMetaState = arg6;
        event.mButtonState = arg7;
        event.mXPrecision = arg8;
        event.mYPrecision = arg9;
        event.mDeviceId = arg10;
        event.mEdgeFlags = arg11;
        event.mSource = arg12;
        event.mFlags = arg13;
        return event;
    }

    public static MotionEvent obtain(long arg0, long arg1, int arg2, int arg3,
            float arg4, float arg5, float arg6, float arg7, int arg8, float arg9,
            float arg10, int arg11, int arg12) {
        MotionEvent event = obtain(arg0, arg1, arg3, arg4, arg5, arg12);
        event.mAction = arg2;
        event.mMetaState = arg8;
        event.mXPrecision = arg9;
        event.mYPrecision = arg10;
        event.mDeviceId = arg11;
        event.mPointerCoords[0].pressure = arg6;
        event.mPointerCoords[0].size = arg7;
        return event;
    }

    public static MotionEvent obtain(long arg0, long arg1, int arg2, int arg3,
            int[] arg4, MotionEvent.PointerCoords[] arg5, int arg6, float arg7,
            float arg8, int arg9, int arg10, int arg11, int arg12) {
        PointerProperties[] properties = new PointerProperties[arg3];
        for (int i = 0; i < arg3; i++) {
            properties[i] = new PointerProperties();
            properties[i].id = arg4[i];
            properties[i].toolType = TOOL_TYPE_FINGER;
        }
        return obtain(arg0, arg1, arg2, arg3, properties, arg5, arg6, 0, arg7,
                arg8, arg9, arg10, arg11, arg12, 0, 0);
    }

    public static MotionEvent obtainNoHistory(MotionEvent arg0) {
        MotionEvent event = obtain(arg0);
        event.mHistory.clear();
        return event;
    }

    public void offsetLocation(float arg0, float arg1) {
        mXOffset += arg0;
        mYOffset += arg1;
        for (int i = 0; i < mPointerCount; i++) {
            mPointerCoords[i].x += arg0;
            mPointerCoords[i].y += arg1;
        }
    }

    public void recycle() {
        // No pool on the desktop host; let GC reclaim the event.
    }

    public void setAction(int arg0) {
        mAction = arg0;
    }

    public void setEdgeFlags(int arg0) {
        mEdgeFlags = arg0;
    }

    public void setLocation(float arg0, float arg1) {
        float dx = arg0 - mPointerCoords[0].x;
        float dy = arg1 - mPointerCoords[0].y;
        for (int i = 0; i < mPointerCount; i++) {
            mPointerCoords[i].x += dx;
            mPointerCoords[i].y += dy;
        }
    }

    public void setSource(int arg0) {
        mSource = arg0;
    }

    public String toString() {
        return "MotionEvent { action=" + actionToString(mAction)
                + ", x=" + getX() + ", y=" + getY() + " }";
    }

    public void transform(Matrix arg0) {
        if (arg0 == null) {
            return;
        }
        float[] point = new float[2];
        for (int i = 0; i < mPointerCount; i++) {
            point[0] = mPointerCoords[i].x;
            point[1] = mPointerCoords[i].y;
            arg0.mapPoints(point);
            mPointerCoords[i].x = point[0];
            mPointerCoords[i].y = point[1];
        }
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException(
                "Tartine: MotionEvent parceling is not used on the desktop host");
    }
        public static class PointerCoords {

        public float orientation;
        public float pressure;
        public float size;
        public float toolMajor;
        public float toolMinor;
        public float touchMajor;
        public float touchMinor;
        public float x;
        public float y;

        public PointerCoords() {
        }

        public PointerCoords(MotionEvent.PointerCoords arg0) {
            copyFrom(arg0);
        }

        public void clear() {
            orientation = 0;
            pressure = 0;
            size = 0;
            toolMajor = 0;
            toolMinor = 0;
            touchMajor = 0;
            touchMinor = 0;
            x = 0;
            y = 0;
        }

        public void copyFrom(MotionEvent.PointerCoords arg0) {
            orientation = arg0.orientation;
            pressure = arg0.pressure;
            size = arg0.size;
            toolMajor = arg0.toolMajor;
            toolMinor = arg0.toolMinor;
            touchMajor = arg0.touchMajor;
            touchMinor = arg0.touchMinor;
            x = arg0.x;
            y = arg0.y;
        }

        public float getAxisValue(int arg0) {
            switch (arg0) {
                case AXIS_X:
                    return x;
                case AXIS_Y:
                    return y;
                case AXIS_PRESSURE:
                    return pressure;
                case AXIS_SIZE:
                    return size;
                case AXIS_TOUCH_MAJOR:
                    return touchMajor;
                case AXIS_TOUCH_MINOR:
                    return touchMinor;
                case AXIS_TOOL_MAJOR:
                    return toolMajor;
                case AXIS_TOOL_MINOR:
                    return toolMinor;
                case AXIS_ORIENTATION:
                    return orientation;
                default:
                    return 0;
            }
        }

        public boolean isResampled() {
            return false;
        }

        public void setAxisValue(int arg0, float arg1) {
            switch (arg0) {
                case AXIS_X:
                    x = arg1;
                    break;
                case AXIS_Y:
                    y = arg1;
                    break;
                case AXIS_PRESSURE:
                    pressure = arg1;
                    break;
                case AXIS_SIZE:
                    size = arg1;
                    break;
                case AXIS_TOUCH_MAJOR:
                    touchMajor = arg1;
                    break;
                case AXIS_TOUCH_MINOR:
                    touchMinor = arg1;
                    break;
                case AXIS_TOOL_MAJOR:
                    toolMajor = arg1;
                    break;
                case AXIS_TOOL_MINOR:
                    toolMinor = arg1;
                    break;
                case AXIS_ORIENTATION:
                    orientation = arg1;
                    break;
                default:
                    break;
            }
        }
        }
        public static class PointerProperties {

        public int id;
        public int toolType;

        public PointerProperties() {
        }

        public PointerProperties(MotionEvent.PointerProperties arg0) {
            copyFrom(arg0);
        }

        public void clear() {
            id = 0;
            toolType = TOOL_TYPE_UNKNOWN;
        }

        public void copyFrom(MotionEvent.PointerProperties arg0) {
            id = arg0.id;
            toolType = arg0.toolType;
        }

        public boolean equals(Object arg0) {
            if (this == arg0) {
                return true;
            }
            if (!(arg0 instanceof PointerProperties other)) {
                return false;
            }
            return id == other.id && toolType == other.toolType;
        }

        public int hashCode() {
            return 31 * id + toolType;
        }
        }
    }
