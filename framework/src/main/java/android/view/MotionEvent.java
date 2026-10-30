package android.view;

import android.graphics.Matrix;
import android.os.Parcel;
import android.os.Parcelable;

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

    public static String actionToString(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->actionToString(I)Ljava/lang/String;");
    }

    public void addBatch(long arg0, MotionEvent.PointerCoords[] arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->addBatch(J[Landroid/view/MotionEvent$PointerCoords;I)V");
    }

    public void addBatch(long arg0, float arg1, float arg2, float arg3, float arg4, int arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->addBatch(JFFFFI)V");
    }

    public static int axisFromString(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->axisFromString(Ljava/lang/String;)I");
    }

    public static String axisToString(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->axisToString(I)Ljava/lang/String;");
    }

    protected void finalize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->finalize()V");
    }

    public int findPointerIndex(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->findPointerIndex(I)I");
    }

    public int getAction() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getAction()I");
    }

    public int getActionButton() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getActionButton()I");
    }

    public int getActionIndex() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getActionIndex()I");
    }

    public int getActionMasked() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getActionMasked()I");
    }

    public float getAxisValue(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getAxisValue(I)F");
    }

    public float getAxisValue(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getAxisValue(II)F");
    }

    public int getButtonState() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getButtonState()I");
    }

    public int getClassification() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getClassification()I");
    }

    public int getDeviceId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getDeviceId()I");
    }

    public long getDownTime() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getDownTime()J");
    }

    public int getEdgeFlags() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getEdgeFlags()I");
    }

    public long getEventTime() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getEventTime()J");
    }

    public long getEventTimeNanos() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getEventTimeNanos()J");
    }

    public int getFlags() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getFlags()I");
    }

    public float getHistoricalAxisValue(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getHistoricalAxisValue(II)F");
    }

    public float getHistoricalAxisValue(int arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getHistoricalAxisValue(III)F");
    }

    public long getHistoricalEventTime(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getHistoricalEventTime(I)J");
    }

    public long getHistoricalEventTimeNanos(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getHistoricalEventTimeNanos(I)J");
    }

    public float getHistoricalOrientation(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getHistoricalOrientation(I)F");
    }

    public float getHistoricalOrientation(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getHistoricalOrientation(II)F");
    }

    public void getHistoricalPointerCoords(int arg0, int arg1, MotionEvent.PointerCoords arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getHistoricalPointerCoords(IILandroid/view/MotionEvent$PointerCoords;)V");
    }

    public float getHistoricalPressure(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getHistoricalPressure(I)F");
    }

    public float getHistoricalPressure(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getHistoricalPressure(II)F");
    }

    public float getHistoricalSize(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getHistoricalSize(I)F");
    }

    public float getHistoricalSize(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getHistoricalSize(II)F");
    }

    public float getHistoricalToolMajor(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getHistoricalToolMajor(I)F");
    }

    public float getHistoricalToolMajor(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getHistoricalToolMajor(II)F");
    }

    public float getHistoricalToolMinor(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getHistoricalToolMinor(I)F");
    }

    public float getHistoricalToolMinor(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getHistoricalToolMinor(II)F");
    }

    public float getHistoricalTouchMajor(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getHistoricalTouchMajor(I)F");
    }

    public float getHistoricalTouchMajor(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getHistoricalTouchMajor(II)F");
    }

    public float getHistoricalTouchMinor(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getHistoricalTouchMinor(I)F");
    }

    public float getHistoricalTouchMinor(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getHistoricalTouchMinor(II)F");
    }

    public float getHistoricalX(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getHistoricalX(I)F");
    }

    public float getHistoricalX(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getHistoricalX(II)F");
    }

    public float getHistoricalY(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getHistoricalY(I)F");
    }

    public float getHistoricalY(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getHistoricalY(II)F");
    }

    public int getHistorySize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getHistorySize()I");
    }

    public int getMetaState() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getMetaState()I");
    }

    public float getOrientation() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getOrientation()F");
    }

    public float getOrientation(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getOrientation(I)F");
    }

    public void getPointerCoords(int arg0, MotionEvent.PointerCoords arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getPointerCoords(ILandroid/view/MotionEvent$PointerCoords;)V");
    }

    public int getPointerCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getPointerCount()I");
    }

    public int getPointerId(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getPointerId(I)I");
    }

    public void getPointerProperties(int arg0, MotionEvent.PointerProperties arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getPointerProperties(ILandroid/view/MotionEvent$PointerProperties;)V");
    }

    public float getPressure() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getPressure()F");
    }

    public float getPressure(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getPressure(I)F");
    }

    public float getRawX() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getRawX()F");
    }

    public float getRawX(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getRawX(I)F");
    }

    public float getRawY() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getRawY()F");
    }

    public float getRawY(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getRawY(I)F");
    }

    public float getSize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getSize()F");
    }

    public float getSize(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getSize(I)F");
    }

    public int getSource() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getSource()I");
    }

    public float getToolMajor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getToolMajor()F");
    }

    public float getToolMajor(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getToolMajor(I)F");
    }

    public float getToolMinor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getToolMinor()F");
    }

    public float getToolMinor(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getToolMinor(I)F");
    }

    public int getToolType(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getToolType(I)I");
    }

    public float getTouchMajor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getTouchMajor()F");
    }

    public float getTouchMajor(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getTouchMajor(I)F");
    }

    public float getTouchMinor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getTouchMinor()F");
    }

    public float getTouchMinor(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getTouchMinor(I)F");
    }

    public float getX() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getX()F");
    }

    public float getX(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getX(I)F");
    }

    public float getXPrecision() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getXPrecision()F");
    }

    public float getY() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getY()F");
    }

    public float getY(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getY(I)F");
    }

    public float getYPrecision() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->getYPrecision()F");
    }

    public boolean isButtonPressed(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->isButtonPressed(I)Z");
    }

    public static MotionEvent obtain(MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->obtain(Landroid/view/MotionEvent;)Landroid/view/MotionEvent;");
    }

    public static MotionEvent obtain(long arg0, long arg1, int arg2, float arg3, float arg4, float arg5, float arg6, int arg7, float arg8, float arg9, int arg10, int arg11) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->obtain(JJIFFFFIFFII)Landroid/view/MotionEvent;");
    }

    public static MotionEvent obtain(long arg0, long arg1, int arg2, float arg3, float arg4, int arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->obtain(JJIFFI)Landroid/view/MotionEvent;");
    }

    public static MotionEvent obtain(long arg0, long arg1, int arg2, int arg3, MotionEvent.PointerProperties[] arg4, MotionEvent.PointerCoords[] arg5, int arg6, int arg7, float arg8, float arg9, int arg10, int arg11, int arg12, int arg13) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->obtain(JJII[Landroid/view/MotionEvent$PointerProperties;[Landroid/view/MotionEvent$PointerCoords;IIFFIIII)Landroid/view/MotionEvent;");
    }

    public static MotionEvent obtain(long arg0, long arg1, int arg2, int arg3, MotionEvent.PointerProperties[] arg4, MotionEvent.PointerCoords[] arg5, int arg6, int arg7, float arg8, float arg9, int arg10, int arg11, int arg12, int arg13, int arg14, int arg15) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->obtain(JJII[Landroid/view/MotionEvent$PointerProperties;[Landroid/view/MotionEvent$PointerCoords;IIFFIIIIII)Landroid/view/MotionEvent;");
    }

    public static MotionEvent obtain(long arg0, long arg1, int arg2, int arg3, float arg4, float arg5, float arg6, float arg7, int arg8, float arg9, float arg10, int arg11, int arg12) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->obtain(JJIIFFFFIFFII)Landroid/view/MotionEvent;");
    }

    public static MotionEvent obtain(long arg0, long arg1, int arg2, int arg3, int[] arg4, MotionEvent.PointerCoords[] arg5, int arg6, float arg7, float arg8, int arg9, int arg10, int arg11, int arg12) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->obtain(JJII[I[Landroid/view/MotionEvent$PointerCoords;IFFIIII)Landroid/view/MotionEvent;");
    }

    public static MotionEvent obtainNoHistory(MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->obtainNoHistory(Landroid/view/MotionEvent;)Landroid/view/MotionEvent;");
    }

    public void offsetLocation(float arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->offsetLocation(FF)V");
    }

    public void recycle() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->recycle()V");
    }

    public void setAction(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->setAction(I)V");
    }

    public void setEdgeFlags(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->setEdgeFlags(I)V");
    }

    public void setLocation(float arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->setLocation(FF)V");
    }

    public void setSource(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->setSource(I)V");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->toString()Ljava/lang/String;");
    }

    public void transform(Matrix arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->transform(Landroid/graphics/Matrix;)V");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent;->writeToParcel(Landroid/os/Parcel;I)V");
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
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent$PointerCoords;-><init>()V");
        }

        public PointerCoords(MotionEvent.PointerCoords arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent$PointerCoords;-><init>(Landroid/view/MotionEvent$PointerCoords;)V");
        }

        public void clear() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent$PointerCoords;->clear()V");
        }

        public void copyFrom(MotionEvent.PointerCoords arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent$PointerCoords;->copyFrom(Landroid/view/MotionEvent$PointerCoords;)V");
        }

        public float getAxisValue(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent$PointerCoords;->getAxisValue(I)F");
        }

        public boolean isResampled() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent$PointerCoords;->isResampled()Z");
        }

        public void setAxisValue(int arg0, float arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent$PointerCoords;->setAxisValue(IF)V");
        }
        }
        public static class PointerProperties {

        public int id;
        public int toolType;

        public PointerProperties() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent$PointerProperties;-><init>()V");
        }

        public PointerProperties(MotionEvent.PointerProperties arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent$PointerProperties;-><init>(Landroid/view/MotionEvent$PointerProperties;)V");
        }

        public void clear() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent$PointerProperties;->clear()V");
        }

        public void copyFrom(MotionEvent.PointerProperties arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent$PointerProperties;->copyFrom(Landroid/view/MotionEvent$PointerProperties;)V");
        }

        public boolean equals(Object arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent$PointerProperties;->equals(Ljava/lang/Object;)Z");
        }

        public int hashCode() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MotionEvent$PointerProperties;->hashCode()I");
        }
        }
    }
