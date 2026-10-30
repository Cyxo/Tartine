package android.view.accessibility;

import android.os.Parcel;
import android.os.Parcelable;

    public class AccessibilityEvent extends AccessibilityRecord implements Parcelable {

    public static final int CONTENT_CHANGE_TYPE_CHECKED = 8192;
    public static final int CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION = 4;
    public static final int CONTENT_CHANGE_TYPE_CONTENT_INVALID = 1024;
    public static final int CONTENT_CHANGE_TYPE_DRAG_CANCELLED = 512;
    public static final int CONTENT_CHANGE_TYPE_DRAG_DROPPED = 256;
    public static final int CONTENT_CHANGE_TYPE_DRAG_STARTED = 128;
    public static final int CONTENT_CHANGE_TYPE_ENABLED = 4096;
    public static final int CONTENT_CHANGE_TYPE_ERROR = 2048;
    public static final int CONTENT_CHANGE_TYPE_EXPANDED = 16384;
    public static final int CONTENT_CHANGE_TYPE_PANE_APPEARED = 16;
    public static final int CONTENT_CHANGE_TYPE_PANE_DISAPPEARED = 32;
    public static final int CONTENT_CHANGE_TYPE_PANE_TITLE = 8;
    public static final int CONTENT_CHANGE_TYPE_STATE_DESCRIPTION = 64;
    public static final int CONTENT_CHANGE_TYPE_SUBTREE = 1;
    public static final int CONTENT_CHANGE_TYPE_SUPPLEMENTAL_DESCRIPTION = 32768;
    public static final int CONTENT_CHANGE_TYPE_TEXT = 2;
    public static final int CONTENT_CHANGE_TYPE_UNDEFINED = 0;
    public static Parcelable.Creator CREATOR;
    public static final int INVALID_POSITION = -1;
    public static final int MAX_TEXT_LENGTH = 500;
    public static final int SPEECH_STATE_LISTENING_END = 8;
    public static final int SPEECH_STATE_LISTENING_START = 4;
    public static final int SPEECH_STATE_SPEAKING_END = 2;
    public static final int SPEECH_STATE_SPEAKING_START = 1;
    public static final int TYPES_ALL_MASK = -1;
    public static final int TYPE_ANNOUNCEMENT = 16384;
    public static final int TYPE_ASSIST_READING_CONTEXT = 16777216;
    public static final int TYPE_GESTURE_DETECTION_END = 524288;
    public static final int TYPE_GESTURE_DETECTION_START = 262144;
    public static final int TYPE_NOTIFICATION_STATE_CHANGED = 64;
    public static final int TYPE_SPEECH_STATE_CHANGE = 33554432;
    public static final int TYPE_TOUCH_EXPLORATION_GESTURE_END = 1024;
    public static final int TYPE_TOUCH_EXPLORATION_GESTURE_START = 512;
    public static final int TYPE_TOUCH_INTERACTION_END = 2097152;
    public static final int TYPE_TOUCH_INTERACTION_START = 1048576;
    public static final int TYPE_VIEW_ACCESSIBILITY_FOCUSED = 32768;
    public static final int TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED = 65536;
    public static final int TYPE_VIEW_CLICKED = 1;
    public static final int TYPE_VIEW_CONTEXT_CLICKED = 8388608;
    public static final int TYPE_VIEW_FOCUSED = 8;
    public static final int TYPE_VIEW_HOVER_ENTER = 128;
    public static final int TYPE_VIEW_HOVER_EXIT = 256;
    public static final int TYPE_VIEW_LONG_CLICKED = 2;
    public static final int TYPE_VIEW_SCROLLED = 4096;
    public static final int TYPE_VIEW_SELECTED = 4;
    public static final int TYPE_VIEW_TARGETED_BY_SCROLL = 67108864;
    public static final int TYPE_VIEW_TEXT_CHANGED = 16;
    public static final int TYPE_VIEW_TEXT_SELECTION_CHANGED = 8192;
    public static final int TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY = 131072;
    public static final int TYPE_WINDOWS_CHANGED = 4194304;
    public static final int TYPE_WINDOW_CONTENT_CHANGED = 2048;
    public static final int TYPE_WINDOW_STATE_CHANGED = 32;
    public static final int WINDOWS_CHANGE_ACCESSIBILITY_FOCUSED = 128;
    public static final int WINDOWS_CHANGE_ACTIVE = 32;
    public static final int WINDOWS_CHANGE_ADDED = 1;
    public static final int WINDOWS_CHANGE_BOUNDS = 8;
    public static final int WINDOWS_CHANGE_CHILDREN = 512;
    public static final int WINDOWS_CHANGE_FOCUSED = 64;
    public static final int WINDOWS_CHANGE_LAYER = 16;
    public static final int WINDOWS_CHANGE_PARENT = 256;
    public static final int WINDOWS_CHANGE_PIP = 1024;
    public static final int WINDOWS_CHANGE_REMOVED = 2;
    public static final int WINDOWS_CHANGE_TITLE = 4;

    public AccessibilityEvent() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;-><init>()V");
    }

    public AccessibilityEvent(AccessibilityEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;-><init>(Landroid/view/accessibility/AccessibilityEvent;)V");
    }

    public AccessibilityEvent(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;-><init>(I)V");
    }

    public void appendRecord(AccessibilityRecord arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;->appendRecord(Landroid/view/accessibility/AccessibilityRecord;)V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;->describeContents()I");
    }

    public static String eventTypeToString(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;->eventTypeToString(I)Ljava/lang/String;");
    }

    public int getAction() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;->getAction()I");
    }

    public int getContentChangeTypes() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;->getContentChangeTypes()I");
    }

    public long getEventTime() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;->getEventTime()J");
    }

    public int getEventType() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;->getEventType()I");
    }

    public int getMovementGranularity() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;->getMovementGranularity()I");
    }

    public CharSequence getPackageName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;->getPackageName()Ljava/lang/CharSequence;");
    }

    public AccessibilityRecord getRecord(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;->getRecord(I)Landroid/view/accessibility/AccessibilityRecord;");
    }

    public int getRecordCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;->getRecordCount()I");
    }

    public int getSpeechStateChangeTypes() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;->getSpeechStateChangeTypes()I");
    }

    public int getWindowChanges() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;->getWindowChanges()I");
    }

    public void initFromParcel(Parcel arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;->initFromParcel(Landroid/os/Parcel;)V");
    }

    public boolean isAccessibilityDataSensitive() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;->isAccessibilityDataSensitive()Z");
    }

    public static AccessibilityEvent obtain() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;->obtain()Landroid/view/accessibility/AccessibilityEvent;");
    }

    public static AccessibilityEvent obtain(AccessibilityEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;->obtain(Landroid/view/accessibility/AccessibilityEvent;)Landroid/view/accessibility/AccessibilityEvent;");
    }

    public static AccessibilityEvent obtain(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;->obtain(I)Landroid/view/accessibility/AccessibilityEvent;");
    }

    public void recycle() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;->recycle()V");
    }

    public void setAccessibilityDataSensitive(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;->setAccessibilityDataSensitive(Z)V");
    }

    public void setAction(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;->setAction(I)V");
    }

    public void setContentChangeTypes(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;->setContentChangeTypes(I)V");
    }

    public void setEventTime(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;->setEventTime(J)V");
    }

    public void setEventType(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;->setEventType(I)V");
    }

    public void setMovementGranularity(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;->setMovementGranularity(I)V");
    }

    public void setPackageName(CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;->setPackageName(Ljava/lang/CharSequence;)V");
    }

    public void setSpeechStateChangeTypes(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;->setSpeechStateChangeTypes(I)V");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityEvent;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
