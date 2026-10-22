package android.view;

    public class View implements android.view.accessibility.AccessibilityEventSource, android.graphics.drawable.Drawable.Callback, android.view.KeyEvent.Callback {

    public static final int ACCESSIBILITY_DATA_SENSITIVE_AUTO = 0;
    public static final int ACCESSIBILITY_DATA_SENSITIVE_NO = 2;
    public static final int ACCESSIBILITY_DATA_SENSITIVE_YES = 1;
    public static final int ACCESSIBILITY_LIVE_REGION_ASSERTIVE = 2;
    public static final int ACCESSIBILITY_LIVE_REGION_NONE = 0;
    public static final int ACCESSIBILITY_LIVE_REGION_POLITE = 1;
    public static android.util.Property ALPHA;
    public static final int AUTOFILL_FLAG_INCLUDE_NOT_IMPORTANT_VIEWS = 1;
    public static final java.lang.String AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DATE = "creditCardExpirationDate";
    public static final java.lang.String AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DAY = "creditCardExpirationDay";
    public static final java.lang.String AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_MONTH = "creditCardExpirationMonth";
    public static final java.lang.String AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_YEAR = "creditCardExpirationYear";
    public static final java.lang.String AUTOFILL_HINT_CREDIT_CARD_NUMBER = "creditCardNumber";
    public static final java.lang.String AUTOFILL_HINT_CREDIT_CARD_SECURITY_CODE = "creditCardSecurityCode";
    public static final java.lang.String AUTOFILL_HINT_EMAIL_ADDRESS = "emailAddress";
    public static final java.lang.String AUTOFILL_HINT_NAME = "name";
    public static final java.lang.String AUTOFILL_HINT_PASSWORD = "password";
    public static final java.lang.String AUTOFILL_HINT_PHONE = "phone";
    public static final java.lang.String AUTOFILL_HINT_POSTAL_ADDRESS = "postalAddress";
    public static final java.lang.String AUTOFILL_HINT_POSTAL_CODE = "postalCode";
    public static final java.lang.String AUTOFILL_HINT_USERNAME = "username";
    public static final int AUTOFILL_TYPE_DATE = 4;
    public static final int AUTOFILL_TYPE_LIST = 3;
    public static final int AUTOFILL_TYPE_NONE = 0;
    public static final int AUTOFILL_TYPE_TEXT = 1;
    public static final int AUTOFILL_TYPE_TOGGLE = 2;
    public static final int CONTENT_SENSITIVITY_AUTO = 0;
    public static final int CONTENT_SENSITIVITY_NOT_SENSITIVE = 2;
    public static final int CONTENT_SENSITIVITY_SENSITIVE = 1;
    public static final int DRAG_FLAG_ACCESSIBILITY_ACTION = 1024;
    public static final int DRAG_FLAG_GLOBAL = 256;
    public static final int DRAG_FLAG_GLOBAL_PERSISTABLE_URI_PERMISSION = 64;
    public static final int DRAG_FLAG_GLOBAL_PREFIX_URI_PERMISSION = 128;
    public static final int DRAG_FLAG_GLOBAL_SAME_APPLICATION = 4096;
    public static final int DRAG_FLAG_GLOBAL_URI_READ = 1;
    public static final int DRAG_FLAG_GLOBAL_URI_WRITE = 2;
    public static final int DRAG_FLAG_HIDE_CALLING_TASK_ON_DRAG_START = 16384;
    public static final int DRAG_FLAG_OPAQUE = 512;
    public static final int DRAG_FLAG_START_INTENT_SENDER_ON_UNHANDLED_DRAG = 8192;
    public static final int DRAWING_CACHE_QUALITY_AUTO = 0;
    public static final int DRAWING_CACHE_QUALITY_HIGH = 1048576;
    public static final int DRAWING_CACHE_QUALITY_LOW = 524288;
    protected static int[] EMPTY_STATE_SET;
    protected static int[] ENABLED_FOCUSED_SELECTED_STATE_SET;
    protected static int[] ENABLED_FOCUSED_SELECTED_WINDOW_FOCUSED_STATE_SET;
    protected static int[] ENABLED_FOCUSED_STATE_SET;
    protected static int[] ENABLED_FOCUSED_WINDOW_FOCUSED_STATE_SET;
    protected static int[] ENABLED_SELECTED_STATE_SET;
    protected static int[] ENABLED_SELECTED_WINDOW_FOCUSED_STATE_SET;
    protected static int[] ENABLED_STATE_SET;
    protected static int[] ENABLED_WINDOW_FOCUSED_STATE_SET;
    public static final int FIND_VIEWS_WITH_CONTENT_DESCRIPTION = 2;
    public static final int FIND_VIEWS_WITH_TEXT = 1;
    public static final int FOCUSABLE = 1;
    public static final int FOCUSABLES_ALL = 0;
    public static final int FOCUSABLES_TOUCH_MODE = 1;
    public static final int FOCUSABLE_AUTO = 16;
    protected static int[] FOCUSED_SELECTED_STATE_SET;
    protected static int[] FOCUSED_SELECTED_WINDOW_FOCUSED_STATE_SET;
    protected static int[] FOCUSED_STATE_SET;
    protected static int[] FOCUSED_WINDOW_FOCUSED_STATE_SET;
    public static final int FOCUS_BACKWARD = 1;
    public static final int FOCUS_DOWN = 130;
    public static final int FOCUS_FORWARD = 2;
    public static final int FOCUS_LEFT = 17;
    public static final int FOCUS_RIGHT = 66;
    public static final int FOCUS_UP = 33;
    public static final int GONE = 8;
    public static final int HAPTIC_FEEDBACK_ENABLED = 268435456;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_AUTO = 0;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_NO = 2;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS = 4;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_YES = 1;
    public static final int IMPORTANT_FOR_AUTOFILL_AUTO = 0;
    public static final int IMPORTANT_FOR_AUTOFILL_NO = 2;
    public static final int IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS = 8;
    public static final int IMPORTANT_FOR_AUTOFILL_YES = 1;
    public static final int IMPORTANT_FOR_AUTOFILL_YES_EXCLUDE_DESCENDANTS = 4;
    public static final int IMPORTANT_FOR_CONTENT_CAPTURE_AUTO = 0;
    public static final int IMPORTANT_FOR_CONTENT_CAPTURE_NO = 2;
    public static final int IMPORTANT_FOR_CONTENT_CAPTURE_NO_EXCLUDE_DESCENDANTS = 8;
    public static final int IMPORTANT_FOR_CONTENT_CAPTURE_YES = 1;
    public static final int IMPORTANT_FOR_CONTENT_CAPTURE_YES_EXCLUDE_DESCENDANTS = 4;
    public static final int INVISIBLE = 4;
    public static final int KEEP_SCREEN_ON = 67108864;
    public static final int LAYER_TYPE_HARDWARE = 2;
    public static final int LAYER_TYPE_NONE = 0;
    public static final int LAYER_TYPE_SOFTWARE = 1;
    public static final int LAYOUT_DIRECTION_INHERIT = 2;
    public static final int LAYOUT_DIRECTION_LOCALE = 3;
    public static final int LAYOUT_DIRECTION_LTR = 0;
    public static final int LAYOUT_DIRECTION_RTL = 1;
    public static final int MEASURED_HEIGHT_STATE_SHIFT = 16;
    public static final int MEASURED_SIZE_MASK = 16777215;
    public static final int MEASURED_STATE_MASK = -16777216;
    public static final int MEASURED_STATE_TOO_SMALL = 16777216;
    public static final int NOT_FOCUSABLE = 0;
    public static final int NO_ID = -1;
    public static final int OVER_SCROLL_ALWAYS = 0;
    public static final int OVER_SCROLL_IF_CONTENT_SCROLLS = 1;
    public static final int OVER_SCROLL_NEVER = 2;
    protected static int[] PRESSED_ENABLED_FOCUSED_SELECTED_STATE_SET;
    protected static int[] PRESSED_ENABLED_FOCUSED_SELECTED_WINDOW_FOCUSED_STATE_SET;
    protected static int[] PRESSED_ENABLED_FOCUSED_STATE_SET;
    protected static int[] PRESSED_ENABLED_FOCUSED_WINDOW_FOCUSED_STATE_SET;
    protected static int[] PRESSED_ENABLED_SELECTED_STATE_SET;
    protected static int[] PRESSED_ENABLED_SELECTED_WINDOW_FOCUSED_STATE_SET;
    protected static int[] PRESSED_ENABLED_STATE_SET;
    protected static int[] PRESSED_ENABLED_WINDOW_FOCUSED_STATE_SET;
    protected static int[] PRESSED_FOCUSED_SELECTED_STATE_SET;
    protected static int[] PRESSED_FOCUSED_SELECTED_WINDOW_FOCUSED_STATE_SET;
    protected static int[] PRESSED_FOCUSED_STATE_SET;
    protected static int[] PRESSED_FOCUSED_WINDOW_FOCUSED_STATE_SET;
    protected static int[] PRESSED_SELECTED_STATE_SET;
    protected static int[] PRESSED_SELECTED_WINDOW_FOCUSED_STATE_SET;
    protected static int[] PRESSED_STATE_SET;
    protected static int[] PRESSED_WINDOW_FOCUSED_STATE_SET;
    public static final float REQUESTED_FRAME_RATE_CATEGORY_DEFAULT = Float.NaN;
    public static final float REQUESTED_FRAME_RATE_CATEGORY_HIGH = -4.0f;
    public static final float REQUESTED_FRAME_RATE_CATEGORY_LOW = -2.0f;
    public static final float REQUESTED_FRAME_RATE_CATEGORY_NORMAL = -3.0f;
    public static final float REQUESTED_FRAME_RATE_CATEGORY_NO_PREFERENCE = -1.0f;
    public static android.util.Property ROTATION;
    public static android.util.Property ROTATION_X;
    public static android.util.Property ROTATION_Y;
    public static android.util.Property SCALE_X;
    public static android.util.Property SCALE_Y;
    public static final int SCREEN_STATE_OFF = 0;
    public static final int SCREEN_STATE_ON = 1;
    public static final int SCROLLBARS_INSIDE_INSET = 16777216;
    public static final int SCROLLBARS_INSIDE_OVERLAY = 0;
    public static final int SCROLLBARS_OUTSIDE_INSET = 50331648;
    public static final int SCROLLBARS_OUTSIDE_OVERLAY = 33554432;
    public static final int SCROLLBAR_POSITION_DEFAULT = 0;
    public static final int SCROLLBAR_POSITION_LEFT = 1;
    public static final int SCROLLBAR_POSITION_RIGHT = 2;
    public static final int SCROLL_AXIS_HORIZONTAL = 1;
    public static final int SCROLL_AXIS_NONE = 0;
    public static final int SCROLL_AXIS_VERTICAL = 2;
    public static final int SCROLL_CAPTURE_HINT_AUTO = 0;
    public static final int SCROLL_CAPTURE_HINT_EXCLUDE = 1;
    public static final int SCROLL_CAPTURE_HINT_EXCLUDE_DESCENDANTS = 4;
    public static final int SCROLL_CAPTURE_HINT_INCLUDE = 2;
    public static final int SCROLL_INDICATOR_BOTTOM = 2;
    public static final int SCROLL_INDICATOR_END = 32;
    public static final int SCROLL_INDICATOR_LEFT = 4;
    public static final int SCROLL_INDICATOR_RIGHT = 8;
    public static final int SCROLL_INDICATOR_START = 16;
    public static final int SCROLL_INDICATOR_TOP = 1;
    protected static int[] SELECTED_STATE_SET;
    protected static int[] SELECTED_WINDOW_FOCUSED_STATE_SET;
    public static final int SOUND_EFFECTS_ENABLED = 134217728;
    public static final int STATUS_BAR_HIDDEN = 1;
    public static final int STATUS_BAR_VISIBLE = 0;
    public static final int SYSTEM_UI_FLAG_FULLSCREEN = 4;
    public static final int SYSTEM_UI_FLAG_HIDE_NAVIGATION = 2;
    public static final int SYSTEM_UI_FLAG_IMMERSIVE = 2048;
    public static final int SYSTEM_UI_FLAG_IMMERSIVE_STICKY = 4096;
    public static final int SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN = 1024;
    public static final int SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION = 512;
    public static final int SYSTEM_UI_FLAG_LAYOUT_STABLE = 256;
    public static final int SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR = 16;
    public static final int SYSTEM_UI_FLAG_LIGHT_STATUS_BAR = 8192;
    public static final int SYSTEM_UI_FLAG_LOW_PROFILE = 1;
    public static final int SYSTEM_UI_FLAG_VISIBLE = 0;
    public static final int SYSTEM_UI_LAYOUT_FLAGS = 1536;
    public static final int TEXT_ALIGNMENT_CENTER = 4;
    public static final int TEXT_ALIGNMENT_GRAVITY = 1;
    public static final int TEXT_ALIGNMENT_INHERIT = 0;
    public static final int TEXT_ALIGNMENT_TEXT_END = 3;
    public static final int TEXT_ALIGNMENT_TEXT_START = 2;
    public static final int TEXT_ALIGNMENT_VIEW_END = 6;
    public static final int TEXT_ALIGNMENT_VIEW_START = 5;
    public static final int TEXT_DIRECTION_ANY_RTL = 2;
    public static final int TEXT_DIRECTION_FIRST_STRONG = 1;
    public static final int TEXT_DIRECTION_FIRST_STRONG_LTR = 6;
    public static final int TEXT_DIRECTION_FIRST_STRONG_RTL = 7;
    public static final int TEXT_DIRECTION_INHERIT = 0;
    public static final int TEXT_DIRECTION_LOCALE = 5;
    public static final int TEXT_DIRECTION_LTR = 3;
    public static final int TEXT_DIRECTION_RTL = 4;
    public static android.util.Property TRANSLATION_X;
    public static android.util.Property TRANSLATION_Y;
    public static android.util.Property TRANSLATION_Z;
    protected static final java.lang.String VIEW_LOG_TAG = "View";
    public static final int VISIBLE = 0;
    protected static int[] WINDOW_FOCUSED_STATE_SET;
    public static android.util.Property X;
    public static android.util.Property Y;
    public static android.util.Property Z;

    /**
     * AOSP View.View(Context) (lines 5853-5855): stores the context and
     * seeds mResources from it. The shell keeps the same seed (androidx
     * EdgeToEdge flows assert the decor's resources non-null, so the
     * window's context must flow in through the decor's ctor).
     */
    public View(android.content.Context arg0) {
        mContext = arg0;
        mResources = arg0 != null ? arg0.getResources() : null;
    }

    /** AOSP View.View(Context, AttributeSet) (lines 5938-5939): the device
     * delegates to the 3-arg ctor with defStyleAttr 0; the shell flattens
     * the chain onto the 1-arg constructor's seed (mContext + mResources).
     */
    public View(android.content.Context arg0, android.util.AttributeSet arg1) {
        this(arg0);
    }

    /** AOSP View.View(Context, AttributeSet, int defStyleAttr)
     * (lines 5959-5960): the device delegates to the 4-arg ctor with a
     * zero-def style resource; the shell flattens onto the 1-arg seed. */
    public View(android.content.Context arg0, android.util.AttributeSet arg1, int arg2) {
        this(arg0);
    }

    /** AOSP View.View(Context, AttributeSet, int, int) (line 5996): the
     * device runs full attribute-flavored init; the shell (no attr sets
     * yet) flattens onto the 1-arg seed which carries only the context and
     * resources. */
    public View(android.content.Context arg0, android.util.AttributeSet arg1, int arg2,
            int arg3) {
        this(arg0);
    }

    public void addChildrenForAccessibility(java.util.ArrayList<android.view.View> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->addChildrenForAccessibility(Ljava/util/ArrayList;)V");
    }

    public void addExtraDataToAccessibilityNodeInfo(android.view.accessibility.AccessibilityNodeInfo arg0, java.lang.String arg1, android.os.Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->addExtraDataToAccessibilityNodeInfo(Landroid/view/accessibility/AccessibilityNodeInfo;Ljava/lang/String;Landroid/os/Bundle;)V");
    }

    public void addFocusables(java.util.ArrayList<android.view.View> arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->addFocusables(Ljava/util/ArrayList;I)V");
    }

    public void addFocusables(java.util.ArrayList<android.view.View> arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->addFocusables(Ljava/util/ArrayList;II)V");
    }

    public void addKeyboardNavigationClusters(java.util.Collection<android.view.View> arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->addKeyboardNavigationClusters(Ljava/util/Collection;I)V");
    }

    /** AOSP field mOnAttachStateChangeListeners (View.java line 5118 — the
     * device nests it in ListenerInfo); the shell keeps one list per view. */
    private java.util.concurrent.CopyOnWriteArrayList<OnAttachStateChangeListener>
            mOnAttachStateChangeListeners;

    /** AOSP View.addOnAttachStateChangeListener (lines 7826-7833): device
     * stores into the ListenerInfo bag; same store on the shell (attach
     * dispatch trails with the window-attach chain). */
    public void addOnAttachStateChangeListener(android.view.View.OnAttachStateChangeListener arg0) {
        if (mOnAttachStateChangeListeners == null) {
            mOnAttachStateChangeListeners =
                    new java.util.concurrent.CopyOnWriteArrayList<OnAttachStateChangeListener>();
        }
        mOnAttachStateChangeListeners.add(arg0);
    }

    public void addOnLayoutChangeListener(android.view.View.OnLayoutChangeListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->addOnLayoutChangeListener(Landroid/view/View$OnLayoutChangeListener;)V");
    }

    public void addOnUnhandledKeyEventListener(android.view.View.OnUnhandledKeyEventListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->addOnUnhandledKeyEventListener(Landroid/view/View$OnUnhandledKeyEventListener;)V");
    }

    public void addTouchables(java.util.ArrayList<android.view.View> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->addTouchables(Ljava/util/ArrayList;)V");
    }

    public android.view.ViewPropertyAnimator animate() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->animate()Landroid/view/ViewPropertyAnimator;");
    }

    public void announceForAccessibility(java.lang.CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->announceForAccessibility(Ljava/lang/CharSequence;)V");
    }

    public void autofill(android.util.SparseArray<android.view.autofill.AutofillValue> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->autofill(Landroid/util/SparseArray;)V");
    }

    public void autofill(android.view.autofill.AutofillValue arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->autofill(Landroid/view/autofill/AutofillValue;)V");
    }

    protected boolean awakenScrollBars() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->awakenScrollBars()Z");
    }

    protected boolean awakenScrollBars(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->awakenScrollBars(I)Z");
    }

    protected boolean awakenScrollBars(int arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->awakenScrollBars(IZ)Z");
    }

    public void bringToFront() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->bringToFront()V");
    }

    public void buildDrawingCache() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->buildDrawingCache()V");
    }

    public void buildDrawingCache(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->buildDrawingCache(Z)V");
    }

    public void buildLayer() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->buildLayer()V");
    }

    public boolean callOnClick() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->callOnClick()Z");
    }

    public boolean canResolveLayoutDirection() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->canResolveLayoutDirection()Z");
    }

    public boolean canResolveTextAlignment() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->canResolveTextAlignment()Z");
    }

    public boolean canResolveTextDirection() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->canResolveTextDirection()Z");
    }

    public boolean canScrollHorizontally(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->canScrollHorizontally(I)Z");
    }

    public boolean canScrollVertically(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->canScrollVertically(I)Z");
    }

    public final void cancelDragAndDrop() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->cancelDragAndDrop()V");
    }

    public void cancelLongPress() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->cancelLongPress()V");
    }

    public final void cancelPendingInputEvents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->cancelPendingInputEvents()V");
    }

    public boolean checkInputConnectionProxy(android.view.View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->checkInputConnectionProxy(Landroid/view/View;)Z");
    }

    public void clearAnimation() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->clearAnimation()V");
    }

    public void clearFocus() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->clearFocus()V");
    }

    public void clearPendingCredentialRequest() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->clearPendingCredentialRequest()V");
    }

    public void clearViewTranslationCallback() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->clearViewTranslationCallback()V");
    }

    public static int combineMeasuredStates(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->combineMeasuredStates(II)I");
    }

    protected int computeHorizontalScrollExtent() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->computeHorizontalScrollExtent()I");
    }

    protected int computeHorizontalScrollOffset() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->computeHorizontalScrollOffset()I");
    }

    protected int computeHorizontalScrollRange() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->computeHorizontalScrollRange()I");
    }

    public void computeScroll() {
        // AOSP View.java:21594: default View computes nothing; Scroller-driven
        // scroll overrides later (no scroll model ported).
    }

    public android.view.WindowInsets computeSystemWindowInsets(android.view.WindowInsets arg0, android.graphics.Rect arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->computeSystemWindowInsets(Landroid/view/WindowInsets;Landroid/graphics/Rect;)Landroid/view/WindowInsets;");
    }

    protected int computeVerticalScrollExtent() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->computeVerticalScrollExtent()I");
    }

    protected int computeVerticalScrollOffset() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->computeVerticalScrollOffset()I");
    }

    protected int computeVerticalScrollRange() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->computeVerticalScrollRange()I");
    }

    public android.view.accessibility.AccessibilityNodeInfo createAccessibilityNodeInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->createAccessibilityNodeInfo()Landroid/view/accessibility/AccessibilityNodeInfo;");
    }

    public void createContextMenu(android.view.ContextMenu arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->createContextMenu(Landroid/view/ContextMenu;)V");
    }

    public void destroyDrawingCache() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->destroyDrawingCache()V");
    }

    public android.view.WindowInsets dispatchApplyWindowInsets(android.view.WindowInsets arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchApplyWindowInsets(Landroid/view/WindowInsets;)Landroid/view/WindowInsets;");
    }

    public boolean dispatchCapturedPointerEvent(android.view.MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchCapturedPointerEvent(Landroid/view/MotionEvent;)Z");
    }

    public void dispatchConfigurationChanged(android.content.res.Configuration arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchConfigurationChanged(Landroid/content/res/Configuration;)V");
    }

    public void dispatchCreateViewTranslationRequest(java.util.Map<android.view.autofill.AutofillId, long[]> arg0, int[] arg1, android.view.translation.TranslationCapability arg2, java.util.List<android.view.translation.ViewTranslationRequest> arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchCreateViewTranslationRequest(Ljava/util/Map;[ILandroid/view/translation/TranslationCapability;Ljava/util/List;)V");
    }

    public void dispatchDisplayHint(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchDisplayHint(I)V");
    }

    public boolean dispatchDragEvent(android.view.DragEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchDragEvent(Landroid/view/DragEvent;)Z");
    }

    protected void dispatchDraw(android.graphics.Canvas arg0) {
        // AOSP View.java: default View dispatchDraw is empty (ViewGroup
        // overrides to walk children — its port lands in ViewGroup).
    }

    public void dispatchDrawableHotspotChanged(float arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchDrawableHotspotChanged(FF)V");
    }

    public void dispatchFinishTemporaryDetach() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchFinishTemporaryDetach()V");
    }

    protected boolean dispatchGenericFocusedEvent(android.view.MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchGenericFocusedEvent(Landroid/view/MotionEvent;)Z");
    }

    public boolean dispatchGenericMotionEvent(android.view.MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchGenericMotionEvent(Landroid/view/MotionEvent;)Z");
    }

    protected boolean dispatchGenericPointerEvent(android.view.MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchGenericPointerEvent(Landroid/view/MotionEvent;)Z");
    }

    protected boolean dispatchHoverEvent(android.view.MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchHoverEvent(Landroid/view/MotionEvent;)Z");
    }

    public boolean dispatchKeyEvent(android.view.KeyEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchKeyEvent(Landroid/view/KeyEvent;)Z");
    }

    public boolean dispatchKeyEventPreIme(android.view.KeyEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchKeyEventPreIme(Landroid/view/KeyEvent;)Z");
    }

    public boolean dispatchKeyShortcutEvent(android.view.KeyEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchKeyShortcutEvent(Landroid/view/KeyEvent;)Z");
    }

    public boolean dispatchNestedFling(float arg0, float arg1, boolean arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchNestedFling(FFZ)Z");
    }

    public boolean dispatchNestedPreFling(float arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchNestedPreFling(FF)Z");
    }

    public boolean dispatchNestedPrePerformAccessibilityAction(int arg0, android.os.Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchNestedPrePerformAccessibilityAction(ILandroid/os/Bundle;)Z");
    }

    public boolean dispatchNestedPreScroll(int arg0, int arg1, int[] arg2, int[] arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchNestedPreScroll(II[I[I)Z");
    }

    public boolean dispatchNestedScroll(int arg0, int arg1, int arg2, int arg3, int[] arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchNestedScroll(IIII[I)Z");
    }

    public void dispatchPointerCaptureChanged(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchPointerCaptureChanged(Z)V");
    }

    public boolean dispatchPopulateAccessibilityEvent(android.view.accessibility.AccessibilityEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchPopulateAccessibilityEvent(Landroid/view/accessibility/AccessibilityEvent;)Z");
    }

    public void dispatchProvideAutofillStructure(android.view.ViewStructure arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchProvideAutofillStructure(Landroid/view/ViewStructure;I)V");
    }

    public void dispatchProvideStructure(android.view.ViewStructure arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchProvideStructure(Landroid/view/ViewStructure;)V");
    }

    protected void dispatchRestoreInstanceState(android.util.SparseArray<android.os.Parcelable> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchRestoreInstanceState(Landroid/util/SparseArray;)V");
    }

    protected void dispatchSaveInstanceState(android.util.SparseArray<android.os.Parcelable> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchSaveInstanceState(Landroid/util/SparseArray;)V");
    }

    public void dispatchScrollCaptureSearch(android.graphics.Rect arg0, android.graphics.Point arg1, java.util.function.Consumer<android.view.ScrollCaptureTarget> arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchScrollCaptureSearch(Landroid/graphics/Rect;Landroid/graphics/Point;Ljava/util/function/Consumer;)V");
    }

    protected void dispatchSetActivated(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchSetActivated(Z)V");
    }

    protected void dispatchSetPressed(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchSetPressed(Z)V");
    }

    protected void dispatchSetSelected(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchSetSelected(Z)V");
    }

    public void dispatchStartTemporaryDetach() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchStartTemporaryDetach()V");
    }

    public void dispatchSystemUiVisibilityChanged(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchSystemUiVisibilityChanged(I)V");
    }

    public boolean dispatchTouchEvent(android.view.MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchTouchEvent(Landroid/view/MotionEvent;)Z");
    }

    public boolean dispatchTrackballEvent(android.view.MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchTrackballEvent(Landroid/view/MotionEvent;)Z");
    }

    public boolean dispatchUnhandledMove(android.view.View arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchUnhandledMove(Landroid/view/View;I)Z");
    }

    protected void dispatchVisibilityChanged(android.view.View arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchVisibilityChanged(Landroid/view/View;I)V");
    }

    public void dispatchWindowFocusChanged(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchWindowFocusChanged(Z)V");
    }

    public void dispatchWindowInsetsAnimationEnd(android.view.WindowInsetsAnimation arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchWindowInsetsAnimationEnd(Landroid/view/WindowInsetsAnimation;)V");
    }

    public void dispatchWindowInsetsAnimationPrepare(android.view.WindowInsetsAnimation arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchWindowInsetsAnimationPrepare(Landroid/view/WindowInsetsAnimation;)V");
    }

    public android.view.WindowInsets dispatchWindowInsetsAnimationProgress(android.view.WindowInsets arg0, java.util.List<android.view.WindowInsetsAnimation> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchWindowInsetsAnimationProgress(Landroid/view/WindowInsets;Ljava/util/List;)Landroid/view/WindowInsets;");
    }

    public android.view.WindowInsetsAnimation.Bounds dispatchWindowInsetsAnimationStart(android.view.WindowInsetsAnimation arg0, android.view.WindowInsetsAnimation.Bounds arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchWindowInsetsAnimationStart(Landroid/view/WindowInsetsAnimation;Landroid/view/WindowInsetsAnimation$Bounds;)Landroid/view/WindowInsetsAnimation$Bounds;");
    }

    public void dispatchWindowSystemUiVisiblityChanged(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchWindowSystemUiVisiblityChanged(I)V");
    }

    public void dispatchWindowVisibilityChanged(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchWindowVisibilityChanged(I)V");
    }

    /**
     * AOSP View.java:25032-25114: the common (no fading edges) path. Step 1
     * drawBackground; step 3 onDraw; step 4 dispatchDraw; step 6
     * onDrawForeground; step 7 default focus highlight. Staging deviations:
     * fading-edge branch unreachable (mViewFlags edge bits are 0 by
     * construction of the reduced field set — see fields comment);
     * overlay/autofill/focus-highlight decorations are inert until their
     * models land (AOSP:25188-25216, :25216-25230 no-op without state).
     */
    public void draw(android.graphics.Canvas arg0) {
        drawBackground(arg0);
        // Overridable hooks reach guest overrides via the VirtualCall seam
        // (framework-in-Java (2)); NOT_GUEST falls back to the framework
        // default inline, matching AOSP's empty View defaults.
        if (tartine.fw.VirtualCall.on(this, "onDraw",
                "(Landroid/graphics/Canvas;)V",
                new Object[]{arg0}) == tartine.fw.VirtualCall.NOT_GUEST) {
            onDraw(arg0);
        }
        if (tartine.fw.VirtualCall.on(this, "dispatchDraw",
                "(Landroid/graphics/Canvas;)V",
                new Object[]{arg0}) == tartine.fw.VirtualCall.NOT_GUEST) {
            dispatchDraw(arg0);
        }
        // drawAutofilledHighlight: no autofill model yet (AOSP needs
        // PFLAG3_AUTOFILLED_HIGHLIGHTED state); no-op.
        if (tartine.fw.VirtualCall.on(this, "onDrawForeground",
                "(Landroid/graphics/Canvas;)V",
                new Object[]{arg0}) == tartine.fw.VirtualCall.NOT_GUEST) {
            onDrawForeground(arg0);
        }
        // drawDefaultFocusHighlight: no focus-highlight state yet
        // (AOSP draws only under PFLAG3_DEFAULT_FOCUS_HIGHLIGHTED); no-op.
    }

    /** AOSP View.java:25267-25275: no background, nothing to draw here. */
    private void drawBackground(android.graphics.Canvas canvas) {
        android.graphics.drawable.Drawable background = mBackground;
        if (background == null) {
            return;
        }
        // Deviation: background dimension/alpha sync (mBackgroundSizeChanged,
        // :25267-25335) waits for a Drawable model.
    }

    /**
     * AOSP View.java:24709-24980, reduced to the software (no RenderNode)
     * leg our pipeline runs: no AttachInfo yet, so drawsWithRenderNode
     * (AOSP:24691-2495) is false; no legacy animation (getAnimation null, see
     * that port); no static-transform flag group; no drawing cache (layer
     * types are NONE by construction). Deviations, each a wall away from
     * being revisited: private-flag dirty bookkeeping omitted (no
     * invalidation-driven redraw model yet): PFLAG_DRAWN/PFLAG_DIRTY_MASK
     * writes dropped (AOSP:24736); PFLAG2_VIEW_QUICK_REJECTED store dropped
     * (AOSP:24800-24799) — the quickReject skip itself stays because it
     * changes observable draw calls.
     */
    boolean draw(android.graphics.Canvas canvas, ViewGroup parent, long drawingTime) {
        boolean drawingWithRenderNode = false; // drawsWithRenderNode: mAttachInfo null
        boolean hardwareAcceleratedCanvas = canvas.isHardwareAccelerated();

        final boolean childHasIdentityMatrix = hasIdentityMatrix();
        // Parent flags default is set in the ViewGroup ctor port (AOSP :726-727).
        final int parentFlags = parent.mGroupFlags;

        if (!childHasIdentityMatrix) {
            // concatMatrix only matters with a real transform apply below;
            // staging views carry identity or plain translate props, both
            // covered by the transform getters, so no matrix concat yet.
        }

        if (!childHasIdentityMatrix && hardwareAcceleratedCanvas == false
                && (parentFlags & ViewGroup.FLAG_CLIP_CHILDREN)
                        == ViewGroup.FLAG_CLIP_CHILDREN
                && canvas.quickReject(getLeft(), getTop(), getRight(), getBottom())) {
            return false; // AOSP:24796-24799 (more = false here)
        }

        int restoreTo = canvas.save(); // offsetForScroll = true
        canvas.translate(getLeft() - mScrollX, getTop() - mScrollY);

        // Alpha block (AOSP:24844-24884) collapses: no transformation state,
        // alpha 1. mClipBounds clip (AOSP:24907-24910) has no port state yet.
        if ((parentFlags & ViewGroup.FLAG_CLIP_CHILDREN) != 0) {
            canvas.clipRect(mScrollX, mScrollY,
                    mScrollX + getWidth(), mScrollY + getHeight());
        }

        if ((mPrivateFlags & PFLAG_SKIP_DRAW) == PFLAG_SKIP_DRAW) {
            // AOSP:24966-24969 — skip-draw views dispatch their children;
            // the hook is virtual (guest overrides first, VirtualCall seam).
            if (tartine.fw.VirtualCall.on(this, "dispatchDraw",
                    "(Landroid/graphics/Canvas;)V",
                    new Object[]{canvas}) == tartine.fw.VirtualCall.NOT_GUEST) {
                dispatchDraw(canvas);
            }
        } else {
            // AOSP:24971-24973 — normal views run the full draw(Canvas).
            if (tartine.fw.VirtualCall.on(this, "draw",
                    "(Landroid/graphics/Canvas;)V",
                    new Object[]{canvas}) == tartine.fw.VirtualCall.NOT_GUEST) {
                draw(canvas);
            }
        }
        canvas.restoreToCount(restoreTo);
        return false; // 'more' stays false without animation progress
    }

    boolean hasIdentityMatrix() {
        // AOSP: view matrix identity check over the RenderNode transform
        // props. Staging reads the ported transform getters (defaults are
        // identity until a transform port sets them).
        return getTranslationX() == 0 && getTranslationY() == 0
                && getRotation() == 0 && getRotationX() == 0 && getRotationY() == 0
                && getScaleX() == 1 && getScaleY() == 1;
    }

    public void drawableHotspotChanged(float arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->drawableHotspotChanged(FF)V");
    }

    protected void drawableStateChanged() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->drawableStateChanged()V");
    }

    public android.view.View findFocus() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->findFocus()Landroid/view/View;");
    }

    public final android.window.OnBackInvokedDispatcher findOnBackInvokedDispatcher() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->findOnBackInvokedDispatcher()Landroid/window/OnBackInvokedDispatcher;");
    }

    /**
     * AOSP View.findViewById (lines 27782-27787, final): NO_ID answers
     * null, otherwise the traversal; the device plain-View traversal
     * (findViewTraversal, lines 27732-27737) answers the view itself only
     * when the id matches mID — the shell mirrors both verbatim (a child
     * walk belongs to the ViewGroup port). The bound {@code extends View}
     * matters: it fixes the erasure to {@code (I)Landroid/view/View;}, the
     * descriptor guest call sites resolve (a bare {@code <T>} erases to
     * Object and the invoke misses — same hazard flagged for SkeletonGen's
     * bare type-param emission).
     */
    @SuppressWarnings("unchecked")
    public final <T extends android.view.View> T findViewById(int arg0) {
        if (arg0 == NO_ID) {
            return null;
        }
        return (T) (arg0 == mID ? this : null);
    }

    public final <T> T findViewWithTag(java.lang.Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->findViewWithTag(Ljava/lang/Object;)Landroid/view/View;");
    }

    public void findViewsWithText(java.util.ArrayList<android.view.View> arg0, java.lang.CharSequence arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->findViewsWithText(Ljava/util/ArrayList;Ljava/lang/CharSequence;I)V");
    }

    protected boolean fitSystemWindows(android.graphics.Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->fitSystemWindows(Landroid/graphics/Rect;)Z");
    }

    public android.view.View focusSearch(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->focusSearch(I)Landroid/view/View;");
    }

    public void forceHasOverlappingRendering(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->forceHasOverlappingRendering(Z)V");
    }

    public void forceLayout() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->forceLayout()V");
    }

    public boolean gatherTransparentRegion(android.graphics.Region arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->gatherTransparentRegion(Landroid/graphics/Region;)Z");
    }

    public void generateDisplayHash(java.lang.String arg0, android.graphics.Rect arg1, java.util.concurrent.Executor arg2, android.view.displayhash.DisplayHashResultCallback arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->generateDisplayHash(Ljava/lang/String;Landroid/graphics/Rect;Ljava/util/concurrent/Executor;Landroid/view/displayhash/DisplayHashResultCallback;)V");
    }

    /**
     * Ported from aosp-mirror/platform_frameworks_base
     * core/java/android/view/View.java (generateViewId): CAS loop over
     * sNextGeneratedId, clamped below the aapt-generated (high-byte) ID range.
     */
    private static final java.util.concurrent.atomic.AtomicInteger
            sNextGeneratedId = new java.util.concurrent.atomic.AtomicInteger(1);

    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the
            // range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) {
                newValue = 1; // Roll over to 1, not 0.
            }
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    public java.lang.CharSequence getAccessibilityClassName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAccessibilityClassName()Ljava/lang/CharSequence;");
    }

    public android.view.View.AccessibilityDelegate getAccessibilityDelegate() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAccessibilityDelegate()Landroid/view/View$AccessibilityDelegate;");
    }

    public int getAccessibilityLiveRegion() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAccessibilityLiveRegion()I");
    }

    public android.view.accessibility.AccessibilityNodeProvider getAccessibilityNodeProvider() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAccessibilityNodeProvider()Landroid/view/accessibility/AccessibilityNodeProvider;");
    }

    public java.lang.CharSequence getAccessibilityPaneTitle() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAccessibilityPaneTitle()Ljava/lang/CharSequence;");
    }

    public int getAccessibilityTraversalAfter() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAccessibilityTraversalAfter()I");
    }

    public int getAccessibilityTraversalBefore() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAccessibilityTraversalBefore()I");
    }

    public java.lang.String getAllowedHandwritingDelegatePackageName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAllowedHandwritingDelegatePackageName()Ljava/lang/String;");
    }

    public java.lang.String getAllowedHandwritingDelegatorPackageName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAllowedHandwritingDelegatorPackageName()Ljava/lang/String;");
    }

    public float getAlpha() {
        // AOSP View.java getAlpha(): mTransformationInfo != null ? ... : 1.
        // No transformation-info model yet, so the default-identity answer
        // is the whole port; setAlpha lands with it.
        return 1;
    }

    public android.view.animation.Animation getAnimation() {
        // AOSP View.java: getAnimation returns the legacy animation state;
        // no animation model yet, so no animation is reported (null leg of
        // AOSP draw(Canvas,ViewGroup,long)).
        return null;
    }

    public android.graphics.Matrix getAnimationMatrix() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAnimationMatrix()Landroid/graphics/Matrix;");
    }

    public android.os.IBinder getApplicationWindowToken() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getApplicationWindowToken()Landroid/os/IBinder;");
    }

    public int[] getAttributeResolutionStack(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAttributeResolutionStack(I)[I");
    }

    public java.util.Map<java.lang.Integer, java.lang.Integer> getAttributeSourceResourceMap() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAttributeSourceResourceMap()Ljava/util/Map;");
    }

    public java.lang.String[] getAutofillHints() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAutofillHints()[Ljava/lang/String;");
    }

    public final android.view.autofill.AutofillId getAutofillId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAutofillId()Landroid/view/autofill/AutofillId;");
    }

    public int getAutofillType() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAutofillType()I");
    }

    public android.view.autofill.AutofillValue getAutofillValue() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAutofillValue()Landroid/view/autofill/AutofillValue;");
    }

    public android.graphics.drawable.Drawable getBackground() {
        // AOSP View.java: mBackground (null without a background set/setter —
        // setBackground is a wall when anything calls it).
        return mBackground;
    }

    public android.graphics.BlendMode getBackgroundTintBlendMode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getBackgroundTintBlendMode()Landroid/graphics/BlendMode;");
    }

    public android.content.res.ColorStateList getBackgroundTintList() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getBackgroundTintList()Landroid/content/res/ColorStateList;");
    }

    public android.graphics.PorterDuff.Mode getBackgroundTintMode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getBackgroundTintMode()Landroid/graphics/PorterDuff$Mode;");
    }

    public int getBaseline() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getBaseline()I");
    }

    /** Ported from AOSP {@code View#getBottom} field getter. */
    public final int getBottom() {
        return mBottom;
    }

    protected float getBottomFadingEdgeStrength() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getBottomFadingEdgeStrength()F");
    }

    protected int getBottomPaddingOffset() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getBottomPaddingOffset()I");
    }

    public float getCameraDistance() {
        // Ported from aosp-mirror/platform_frameworks_base
        // core/java/android/view/View.java (getCameraDistance):
        // mRenderNode value scaled by densityDpi; our pipeline has no
        // Resources/metrics hand-field yet, so dpi = 1 (round-trips the
        // setter symmetrically).
        return mRenderNode.getCameraDistance() * 1.0f;
    }

    /**
     * Clip-bounds state ported from aosp-mirror/platform_frameworks_base
     * core/java/android/view/View.java ({@code mClipBounds}). Non-final for
     * field sync (key {@code View;.mClipBounds}); null means no clipping.
     */
    protected android.graphics.Rect mClipBounds;

    /*
     * Draw-chain state (AOSP View.java fields, staging reductions noted at
     * each use site). mBackground backs getBackground (AOSP View.java mBackground,
     * default null); mPrivateFlags carries only the bits the draw port reads;
     * scroll offsets default to theunscrolled view.
     */
    protected android.graphics.drawable.Drawable mBackground;

    // AOSP View.java:2752: PFLAG_SKIP_DRAW = 0x80 (@hide constant, cited).
    private static final int PFLAG_SKIP_DRAW = 0x00000080;

    protected int mPrivateFlags;

    protected int mScrollX;
    protected int mScrollY;

    /** Ported from AOSP {@code View#getClipBounds()}: copy or null. */
    public android.graphics.Rect getClipBounds() {
        return (mClipBounds != null) ? new android.graphics.Rect(mClipBounds) : null;
    }

    /** Ported from AOSP {@code View#getClipBounds(Rect)}. */
    public boolean getClipBounds(android.graphics.Rect arg0) {
        if (mClipBounds != null) {
            arg0.set(mClipBounds);
            return true;
        }
        return false;
    }

    /** Ported from AOSP {@code View#getClipToOutline}: RenderNode read. */
    public final boolean getClipToOutline() {
        return mRenderNode.getClipToOutline();
    }

    public final android.view.contentcapture.ContentCaptureSession getContentCaptureSession() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getContentCaptureSession()Landroid/view/contentcapture/ContentCaptureSession;");
    }

    public java.lang.CharSequence getContentDescription() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getContentDescription()Ljava/lang/CharSequence;");
    }

    public final int getContentSensitivity() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getContentSensitivity()I");
    }

    public final android.content.Context getContext() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getContext()Landroid/content/Context;");
    }

    protected android.view.ContextMenu.ContextMenuInfo getContextMenuInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getContextMenuInfo()Landroid/view/ContextMenu$ContextMenuInfo;");
    }

    public final boolean getDefaultFocusHighlightEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getDefaultFocusHighlightEnabled()Z");
    }

    public static int getDefaultSize(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getDefaultSize(II)I");
    }

    public android.view.Display getDisplay() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getDisplay()Landroid/view/Display;");
    }

    public final int[] getDrawableState() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getDrawableState()[I");
    }

    public android.graphics.Bitmap getDrawingCache() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getDrawingCache()Landroid/graphics/Bitmap;");
    }

    public android.graphics.Bitmap getDrawingCache(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getDrawingCache(Z)Landroid/graphics/Bitmap;");
    }

    public int getDrawingCacheBackgroundColor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getDrawingCacheBackgroundColor()I");
    }

    public int getDrawingCacheQuality() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getDrawingCacheQuality()I");
    }

    public void getDrawingRect(android.graphics.Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getDrawingRect(Landroid/graphics/Rect;)V");
    }

    public long getDrawingTime() {
        // AOSP View.java:23414: mAttachInfo != null ? mAttachInfo.mDrawingTime
        // : 0. Staging has no AttachInfo yet, so the unattached leg (0) is
        // the whole port until a real ViewRootImpl-style attach exists.
        return 0;
    }

    /** Ported from AOSP {@code View#getElevation}: RenderNode read. */
    public float getElevation() {
        return mRenderNode.getElevation();
    }

    public int getExplicitStyle() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getExplicitStyle()I");
    }

    public boolean getFilterTouchesWhenObscured() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getFilterTouchesWhenObscured()Z");
    }

    public boolean getFitsSystemWindows() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getFitsSystemWindows()Z");
    }

    public int getFocusable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getFocusable()I");
    }

    public java.util.ArrayList<android.view.View> getFocusables(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getFocusables(I)Ljava/util/ArrayList;");
    }

    public void getFocusedRect(android.graphics.Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getFocusedRect(Landroid/graphics/Rect;)V");
    }

    public android.graphics.drawable.Drawable getForeground() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getForeground()Landroid/graphics/drawable/Drawable;");
    }

    public int getForegroundGravity() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getForegroundGravity()I");
    }

    public android.graphics.BlendMode getForegroundTintBlendMode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getForegroundTintBlendMode()Landroid/graphics/BlendMode;");
    }

    public android.content.res.ColorStateList getForegroundTintList() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getForegroundTintList()Landroid/content/res/ColorStateList;");
    }

    public android.graphics.PorterDuff.Mode getForegroundTintMode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getForegroundTintMode()Landroid/graphics/PorterDuff$Mode;");
    }

    public float getFrameContentVelocity() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getFrameContentVelocity()F");
    }

    public final boolean getGlobalVisibleRect(android.graphics.Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getGlobalVisibleRect(Landroid/graphics/Rect;)Z");
    }

    public boolean getGlobalVisibleRect(android.graphics.Rect arg0, android.graphics.Point arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getGlobalVisibleRect(Landroid/graphics/Rect;Landroid/graphics/Point;)Z");
    }

    public android.os.Handler getHandler() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getHandler()Landroid/os/Handler;");
    }

    public float getHandwritingBoundsOffsetBottom() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getHandwritingBoundsOffsetBottom()F");
    }

    public float getHandwritingBoundsOffsetLeft() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getHandwritingBoundsOffsetLeft()F");
    }

    public float getHandwritingBoundsOffsetRight() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getHandwritingBoundsOffsetRight()F");
    }

    public float getHandwritingBoundsOffsetTop() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getHandwritingBoundsOffsetTop()F");
    }

    public int getHandwritingDelegateFlags() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getHandwritingDelegateFlags()I");
    }

    public java.lang.Runnable getHandwritingDelegatorCallback() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getHandwritingDelegatorCallback()Ljava/lang/Runnable;");
    }

    public final boolean getHasOverlappingRendering() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getHasOverlappingRendering()Z");
    }

    public void getHitRect(android.graphics.Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getHitRect(Landroid/graphics/Rect;)V");
    }

    public int getHorizontalFadingEdgeLength() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getHorizontalFadingEdgeLength()I");
    }

    protected int getHorizontalScrollbarHeight() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getHorizontalScrollbarHeight()I");
    }

    public android.graphics.drawable.Drawable getHorizontalScrollbarThumbDrawable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getHorizontalScrollbarThumbDrawable()Landroid/graphics/drawable/Drawable;");
    }

    public android.graphics.drawable.Drawable getHorizontalScrollbarTrackDrawable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getHorizontalScrollbarTrackDrawable()Landroid/graphics/drawable/Drawable;");
    }

    /** AOSP View.getId (lines 27987-27989): the raw mID field. */
    public int getId() {
        return mID;
    }

    /** AOSP View field mID, defaulting to NO_ID. */
    private int mID = NO_ID;

    public int getImportantForAccessibility() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getImportantForAccessibility()I");
    }

    public int getImportantForAutofill() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getImportantForAutofill()I");
    }

    public int getImportantForContentCapture() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getImportantForContentCapture()I");
    }

    public boolean getKeepScreenOn() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getKeepScreenOn()Z");
    }

    public android.view.KeyEvent.DispatcherState getKeyDispatcherState() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getKeyDispatcherState()Landroid/view/KeyEvent$DispatcherState;");
    }

    public int getLabelFor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getLabelFor()I");
    }

    public int getLayerType() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getLayerType()I");
    }

    /**
     * Mirror state for layout params: the SDK stub jar declares no
     * instance fields, so interpreter framework-dispatch synchronizes the
     * {@code View;.mLayoutParams} mapping key onto this field.
     */
    protected android.view.ViewGroup.LayoutParams mLayoutParams;

    /**
     * Mirror state for the view tree: framework-dispatch syncs the model
     * keys ({@code View;.mParent}, {@code View;.mAttached}) onto these
     * fields so ported ViewGroup bodies (addViewInLayout attaches,
     * removeAllViews detaches) mutate the same state the legacy builtin
     * readers use, and the interpreter unbridges mirrors back to their
     * model objects after every dispatch.
     */
    protected android.view.ViewParent mParent;

    protected int mAttached;

    /**
     * Mirror state for the layout geometry: syncs the model keys
     * ({@code View;.mLeft} / {@code .mTop} / {@code .mRight} /
     * {@code .mBottom}) onto these fields; still default-zero until a
     * layout pass writes them.
     */
    protected int mLeft;

    protected int mTop;

    protected int mRight;

    protected int mBottom;

    public android.view.ViewGroup.LayoutParams getLayoutParams() {
        // Real Android throws when no params were attached via inflation
        // or addView; our composition measures bare views directly, so the
        // read defaults to a WRAP_CONTENT pair. setLayoutParams preserves
        // caller identity, so attach-time params round-trip exactly.
        if (mLayoutParams == null) {
            mLayoutParams = new android.view.ViewGroup.LayoutParams(
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        return mLayoutParams;
    }

    public int getLayoutDirection() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getLayoutDirection()I");
    }

    /** Ported from AOSP {@code View#getLeft} field getter. */
    public final int getLeft() {
        return mLeft;
    }

    protected float getLeftFadingEdgeStrength() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getLeftFadingEdgeStrength()F");
    }

    protected int getLeftPaddingOffset() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getLeftPaddingOffset()I");
    }

    public final boolean getLocalVisibleRect(android.graphics.Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getLocalVisibleRect(Landroid/graphics/Rect;)Z");
    }

    public void getLocationInSurface(int[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getLocationInSurface([I)V");
    }

    /**
     * Location pair ported from aosp-mirror/platform_frameworks_base
     * core/java/android/view/View.java (getLocationInWindow /
     * transformFromViewToWindowSpace / getLocationOnScreen): array-length
     * guard, then the unattached-view branch zeroes the location
     * (mAttachInfo is absent in this pipeline - window-space transforms
     * and the window offset land with the wm ports).
     */
    public void getLocationInWindow(int[] arg0) {
        if (arg0 == null || arg0.length < 2) {
            throw new IllegalArgumentException(
                    "outLocation must be an array of two integers");
        }
        // Unattached view: both coordinates zero (AOSP
        // transformFromViewToWindowSpace early return).
        arg0[0] = 0;
        arg0[1] = 0;
    }

    public void getLocationOnScreen(int[] arg0) {
        getLocationInWindow(arg0);
        // AttachInfo window offset leg pending the wm port; no-op here.
    }

    public android.graphics.Matrix getMatrix() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getMatrix()Landroid/graphics/Matrix;");
    }

    public final int getMeasuredHeight() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getMeasuredHeight()I");
    }

    public final int getMeasuredHeightAndState() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getMeasuredHeightAndState()I");
    }

    public final int getMeasuredState() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getMeasuredState()I");
    }

    public final int getMeasuredWidth() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getMeasuredWidth()I");
    }

    public final int getMeasuredWidthAndState() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getMeasuredWidthAndState()I");
    }

    public int getMinimumHeight() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getMinimumHeight()I");
    }

    public int getMinimumWidth() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getMinimumWidth()I");
    }

    public int getNextClusterForwardId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getNextClusterForwardId()I");
    }

    public int getNextFocusDownId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getNextFocusDownId()I");
    }

    public int getNextFocusForwardId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getNextFocusForwardId()I");
    }

    public int getNextFocusLeftId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getNextFocusLeftId()I");
    }

    public int getNextFocusRightId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getNextFocusRightId()I");
    }

    public int getNextFocusUpId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getNextFocusUpId()I");
    }

    public android.view.View.OnFocusChangeListener getOnFocusChangeListener() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getOnFocusChangeListener()Landroid/view/View$OnFocusChangeListener;");
    }

    public int getOutlineAmbientShadowColor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getOutlineAmbientShadowColor()I");
    }

    public int getOutlineSpotShadowColor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getOutlineSpotShadowColor()I");
    }

    public int getOverScrollMode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getOverScrollMode()I");
    }

    public android.view.ViewOverlay getOverlay() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getOverlay()Landroid/view/ViewOverlay;");
    }

    public int getPaddingBottom() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getPaddingBottom()I");
    }

    public int getPaddingEnd() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getPaddingEnd()I");
    }

    public int getPaddingLeft() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getPaddingLeft()I");
    }

    public int getPaddingRight() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getPaddingRight()I");
    }

    public int getPaddingStart() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getPaddingStart()I");
    }

    public int getPaddingTop() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getPaddingTop()I");
    }

    /** AOSP View.getParent (View.java:18873-18875) — final in AOSP; guests
     * cannot override it. */
    public final android.view.ViewParent getParent() {
        return mParent;
    }

    public android.view.ViewParent getParentForAccessibility() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getParentForAccessibility()Landroid/view/ViewParent;");
    }

    public final android.os.OutcomeReceiver<android.credentials.GetCredentialResponse, android.credentials.GetCredentialException> getPendingCredentialCallback() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getPendingCredentialCallback()Landroid/os/OutcomeReceiver;");
    }

    public final android.credentials.GetCredentialRequest getPendingCredentialRequest() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getPendingCredentialRequest()Landroid/credentials/GetCredentialRequest;");
    }

    /**
     * Ported from aosp-mirror/platform_frameworks_base
     * core/java/android/view/View.java ctor
     * ({@code mRenderNode = RenderNode.create(name, ViewAnimationHostBridge)}):
     * the animation-host bridge is unported internal machinery, so the node
     * names itself the concrete class with a null host. Non-final for field
     * sync (key {@code View;.mRenderNode}).
     */
    protected android.graphics.RenderNode mRenderNode =
            new android.graphics.RenderNode(getClass().getName());

    /**
     * Ported from aosp-mirror/platform_frameworks_base
     * core/java/android/view/View.java ({@code mOutlineProvider}, default
     * {@code ViewOutlineProvider.BACKGROUND}). Non-final for field sync
     * (key {@code View;.mOutlineProvider}).
     */
    protected android.view.ViewOutlineProvider mOutlineProvider =
            android.view.ViewOutlineProvider.BACKGROUND;

    /**
     * Ported from AOSP {@code View#setOutlineProvider}: identity-guarded
     * assignment; the invalidateOutline leg is unported invalidation
     * machinery (no draw pipeline yet).
     */
    public void setOutlineProvider(android.view.ViewOutlineProvider arg0) {
        if (mOutlineProvider != arg0) {
            mOutlineProvider = arg0;
        }
    }

    /** Ported from AOSP {@code View#getOutlineProvider}. */
    public android.view.ViewOutlineProvider getOutlineProvider() {
        return mOutlineProvider;
    }

    /**
     * Ported from aosp-mirror/platform_frameworks_base
     * core/java/android/view/View.java (sanitizeFloatPropertyValue):
     * out-of-range transform values clamp to the range (the AOSP
     * sThrowOnInvalidFloatProperties debug throw leg is omitted).
     */
    private static float sanitizeFloatPropertyValue(float value,
            String propertyName) {
        float min = -Float.MAX_VALUE;
        float max = Float.MAX_VALUE;
        if (value >= min && value <= max) {
            return value;
        }
        if (value < min || value == Float.NEGATIVE_INFINITY) {
            return min;
        }
        if (value > max || Float.isInfinite(value)) {
            return max;
        }
        return value;
    }

    public float getPivotX() {
        // Ported from AOSP getPivotX: reads straight from the RenderNode.
        return mRenderNode.getPivotX();
    }

    public float getPivotY() {
        return mRenderNode.getPivotY();
    }

    public android.view.PointerIcon getPointerIcon() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getPointerIcon()Landroid/view/PointerIcon;");
    }

    public final java.util.List<android.graphics.Rect> getPreferKeepClearRects() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getPreferKeepClearRects()Ljava/util/List;");
    }

    public java.lang.String[] getReceiveContentMimeTypes() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getReceiveContentMimeTypes()[Ljava/lang/String;");
    }

    public float getRequestedFrameRate() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getRequestedFrameRate()F");
    }

    /** AOSP View.getResources() (lines 25854-25856 return the field seeded
     * from the context — shell ctor seeds the same, lines 5853-5855). */
    private android.content.Context mContext;

    private android.content.res.Resources mResources;

    public android.content.res.Resources getResources() {
        return mResources;
    }

    public final boolean getRevealOnFocusHint() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getRevealOnFocusHint()Z");
    }

    /** Ported from AOSP {@code View#getRight} field getter. */
    public final int getRight() {
        return mRight;
    }

    protected float getRightFadingEdgeStrength() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getRightFadingEdgeStrength()F");
    }

    protected int getRightPaddingOffset() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getRightPaddingOffset()I");
    }

    public android.view.AttachedSurfaceControl getRootSurfaceControl() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getRootSurfaceControl()Landroid/view/AttachedSurfaceControl;");
    }

    public android.view.View getRootView() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getRootView()Landroid/view/View;");
    }

    public android.view.WindowInsets getRootWindowInsets() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getRootWindowInsets()Landroid/view/WindowInsets;");
    }

    /**
     * Transform getters ported from aosp-mirror/platform_frameworks_base
     * core/java/android/view/View.java (getRotation/getRotationX/
     * getRotationY/getScaleX/getScaleY): direct RenderNode reads; rotation
     * is the node's rotationZ.
     */
    public float getRotation() {
        return mRenderNode.getRotationZ();
    }

    public float getRotationX() {
        return mRenderNode.getRotationX();
    }

    public float getRotationY() {
        return mRenderNode.getRotationY();
    }

    public float getScaleX() {
        return mRenderNode.getScaleX();
    }

    public float getScaleY() {
        return mRenderNode.getScaleY();
    }

    public int getScrollBarDefaultDelayBeforeFade() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getScrollBarDefaultDelayBeforeFade()I");
    }

    public int getScrollBarFadeDuration() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getScrollBarFadeDuration()I");
    }

    public int getScrollBarSize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getScrollBarSize()I");
    }

    public int getScrollBarStyle() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getScrollBarStyle()I");
    }

    public int getScrollCaptureHint() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getScrollCaptureHint()I");
    }

    public int getScrollIndicators() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getScrollIndicators()I");
    }

    public final int getScrollX() {
        // AOSP View.java: mScrollX field read (scroll model lands later).
        return mScrollX;
    }

    public final int getScrollY() {
        // AOSP View.java: mScrollY field read (scroll model lands later).
        return mScrollY;
    }

    public int getSolidColor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getSolidColor()I");
    }

    public int getSourceLayoutResId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getSourceLayoutResId()I");
    }

    public final java.lang.CharSequence getStateDescription() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getStateDescription()Ljava/lang/CharSequence;");
    }

    public android.animation.StateListAnimator getStateListAnimator() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getStateListAnimator()Landroid/animation/StateListAnimator;");
    }

    protected int getSuggestedMinimumHeight() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getSuggestedMinimumHeight()I");
    }

    protected int getSuggestedMinimumWidth() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getSuggestedMinimumWidth()I");
    }

    public java.lang.CharSequence getSupplementalDescription() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getSupplementalDescription()Ljava/lang/CharSequence;");
    }

    public java.util.List<android.graphics.Rect> getSystemGestureExclusionRects() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getSystemGestureExclusionRects()Ljava/util/List;");
    }

    /** AOSP field mSystemUiVisibility; setter/getter below. */
    private int mSystemUiVisibility;

    /**
     * AOSP View.getSystemUiVisibility (lines 29011-29013): plain field
     * return of the flags stored by setSystemUiVisibility — the shell
     * stores the same value with no visibility dispatch (nothing presents
     * it at the display layer yet).
     */
    public int getSystemUiVisibility() {
        return mSystemUiVisibility;
    }

    public java.lang.Object getTag() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getTag()Ljava/lang/Object;");
    }

    /**
     * AOSP View.getTag(int) (lines 28042-28045) — null when no keyed tag
     * was stored. The device holds mKeyedTags in a SparseArray<Object>;
     * the shell mirrors the store through a host HashMap (framework-internal
     * storage only — guests cross it as opaque java.lang.Object).
     */
    public java.lang.Object getTag(int arg0) {
        if (mKeyedTags != null) {
            return mKeyedTags.get(arg0);
        }
        return null;
    }

    public int getTextAlignment() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getTextAlignment()I");
    }

    public int getTextDirection() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getTextDirection()I");
    }

    public java.lang.CharSequence getTooltipText() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getTooltipText()Ljava/lang/CharSequence;");
    }

    /** Ported from AOSP {@code View#getTop} field getter. */
    public final int getTop() {
        return mTop;
    }

    protected float getTopFadingEdgeStrength() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getTopFadingEdgeStrength()F");
    }

    protected int getTopPaddingOffset() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getTopPaddingOffset()I");
    }

    public android.view.TouchDelegate getTouchDelegate() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getTouchDelegate()Landroid/view/TouchDelegate;");
    }

    public java.util.ArrayList<android.view.View> getTouchables() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getTouchables()Ljava/util/ArrayList;");
    }

    public float getTransitionAlpha() {
        // AOSP View.java: transitions alpha (transition/overlay path, default 1).
        return 1;
    }

    public java.lang.String getTransitionName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getTransitionName()Ljava/lang/String;");
    }

    /**
     * Ported from the same file (getTranslationX/Y/Z): direct RenderNode
     * reads.
     */
    public float getTranslationX() {
        return mRenderNode.getTranslationX();
    }

    public float getTranslationY() {
        return mRenderNode.getTranslationY();
    }

    public float getTranslationZ() {
        return mRenderNode.getTranslationZ();
    }

    public long getUniqueDrawingId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getUniqueDrawingId()J");
    }

    public int getVerticalFadingEdgeLength() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getVerticalFadingEdgeLength()I");
    }

    public int getVerticalScrollbarPosition() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getVerticalScrollbarPosition()I");
    }

    public android.graphics.drawable.Drawable getVerticalScrollbarThumbDrawable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getVerticalScrollbarThumbDrawable()Landroid/graphics/drawable/Drawable;");
    }

    public android.graphics.drawable.Drawable getVerticalScrollbarTrackDrawable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getVerticalScrollbarTrackDrawable()Landroid/graphics/drawable/Drawable;");
    }

    public int getVerticalScrollbarWidth() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getVerticalScrollbarWidth()I");
    }

    public android.view.translation.ViewTranslationResponse getViewTranslationResponse() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getViewTranslationResponse()Landroid/view/translation/ViewTranslationResponse;");
    }

    /** AOSP View.getViewTreeObserver (View.java:27442-27450). The device's
     * attached branch returns mAttachInfo.mTreeObserver; our AttachInfo is
     * still a bare null-or-present ref (window-attach chain not landed), so
     * only the floating-observer path exists here — one lazily-created
     * observer per view, which is exactly the pre-attach device behavior. */
    private android.view.ViewTreeObserver mFloatingTreeObserver;

    public android.view.ViewTreeObserver getViewTreeObserver() {
        // The mAttachInfo branch (AOSP 27443-27445) lands with the real
        // AttachInfo holder; the ref is always null until then.
        if (mFloatingTreeObserver == null) {
            mFloatingTreeObserver = new android.view.ViewTreeObserver(mContext);
        }
        return mFloatingTreeObserver;
    }

    public int getVisibility() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getVisibility()I");
    }

    /**
     * Ported from aosp-mirror/platform_frameworks_base
     * core/java/android/view/View.java (getWidth/getHeight): mRight-mLeft
     * and mBottom-mTop.
     */
    public final int getWidth() {
        return mRight - mLeft;
    }

    public final int getHeight() {
        return mBottom - mTop;
    }

    protected int getWindowAttachCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getWindowAttachCount()I");
    }

    public android.view.WindowId getWindowId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getWindowId()Landroid/view/WindowId;");
    }

    public android.view.WindowInsetsController getWindowInsetsController() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getWindowInsetsController()Landroid/view/WindowInsetsController;");
    }

    public int getWindowSystemUiVisibility() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getWindowSystemUiVisibility()I");
    }

    public android.os.IBinder getWindowToken() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getWindowToken()Landroid/os/IBinder;");
    }

    public int getWindowVisibility() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getWindowVisibility()I");
    }

    public void getWindowVisibleDisplayFrame(android.graphics.Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getWindowVisibleDisplayFrame(Landroid/graphics/Rect;)V");
    }

    public float getX() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getX()F");
    }

    public float getY() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getY()F");
    }

    public float getZ() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getZ()F");
    }

    public boolean hasExplicitFocusable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->hasExplicitFocusable()Z");
    }

    public boolean hasFocus() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->hasFocus()Z");
    }

    public boolean hasFocusable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->hasFocusable()Z");
    }

    public boolean hasNestedScrollingParent() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->hasNestedScrollingParent()Z");
    }

    public boolean hasOnClickListeners() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->hasOnClickListeners()Z");
    }

    public boolean hasOnLongClickListeners() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->hasOnLongClickListeners()Z");
    }

    public boolean hasOverlappingRendering() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->hasOverlappingRendering()Z");
    }

    public boolean hasPointerCapture() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->hasPointerCapture()Z");
    }

    public boolean hasTransientState() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->hasTransientState()Z");
    }

    public boolean hasWindowFocus() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->hasWindowFocus()Z");
    }

    public static android.view.View inflate(android.content.Context arg0, int arg1, android.view.ViewGroup arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->inflate(Landroid/content/Context;ILandroid/view/ViewGroup;)Landroid/view/View;");
    }

    /**
     * Invalidate cluster ported from aosp-mirror/platform_frameworks_base
     * core/java/android/view/View.java (invalidate()/invalidate(Rect)/
     * invalidate(IIII)/invalidate(boolean)/invalidateInternal/isDirty).
     * Tracked up to observable state on an unattached view: the AOSP body
     * short-circuits (ghost views, skipInvalidate, mAttachInfo-null) and
     * marks PFLAG_DIRTY - our surrogate marks an mDirty boolean (isDirty
     * key {@code View;.mDirty}). The parent-damage propagation leg
     * (AttachInfo + invalidateChild) lands with the wm/draw ports, and
     * scroll offsets stand in as 0 (mScroll untracked).
     */
    protected boolean mDirty;

    public void invalidate() {
        invalidate(true);
    }

    public void invalidate(android.graphics.Rect arg0) {
        invalidateInternal(arg0.left, arg0.top, arg0.right, arg0.bottom, true, false);
    }

    public void invalidate(int arg0, int arg1, int arg2, int arg3) {
        invalidateInternal(arg0, arg1, arg2, arg3, true, false);
    }

    void invalidate(boolean arg0) {
        invalidateInternal(0, 0, mRight - mLeft, mBottom - mTop, arg0, true);
    }

    private void invalidateInternal(int l, int t, int r, int b,
            boolean invalidateCache, boolean fullInvalidate) {
        // skipInvalidate() holds for every view here: no current animation,
        // and visibility/attach state is untracked (treated not-visible),
        // so the only observable effect is the dirty flag.
        mDirty = true;
    }

    public void invalidateDrawable(android.graphics.drawable.Drawable arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->invalidateDrawable(Landroid/graphics/drawable/Drawable;)V");
    }

    public void invalidateOutline() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->invalidateOutline()V");
    }

    public boolean isAccessibilityDataSensitive() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isAccessibilityDataSensitive()Z");
    }

    public boolean isAccessibilityFocused() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isAccessibilityFocused()Z");
    }

    public boolean isAccessibilityHeading() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isAccessibilityHeading()Z");
    }

    public boolean isActivated() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isActivated()Z");
    }

    /** AOSP View.java:14146-14148 — attach state == the ViewRootImpl's
     * AttachInfo reference (null until the window attaches this view tree).
     * The shell holds the same single ref; the window-attach chain sets it
     * when that migration lands. Field is package-private so only framework
     * classes flip it — guests never reference the hidden mAttachInfo. */
    Object mAttachInfo;

    /** AOSP View.isAttachedToWindow (View.java:14146-14148). */
    public boolean isAttachedToWindow() {
        return mAttachInfo != null;
    }

    public boolean isAutoHandwritingEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isAutoHandwritingEnabled()Z");
    }

    public boolean isClickable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isClickable()Z");
    }

    public final boolean isContentSensitive() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isContentSensitive()Z");
    }

    public boolean isContextClickable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isContextClickable()Z");
    }

    public boolean isCredential() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isCredential()Z");
    }

    /** Ported from AOSP {@code View#isDirty} (PFLAG_DIRTY surrogate). */
    public boolean isDirty() {
        return mDirty;
    }

    public boolean isDrawingCacheEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isDrawingCacheEnabled()Z");
    }

    public boolean isDuplicateParentStateEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isDuplicateParentStateEnabled()Z");
    }

    public boolean isEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isEnabled()Z");
    }

    public final boolean isFocusable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isFocusable()Z");
    }

    public final boolean isFocusableInTouchMode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isFocusableInTouchMode()Z");
    }

    public boolean isFocused() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isFocused()Z");
    }

    public final boolean isFocusedByDefault() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isFocusedByDefault()Z");
    }

    public boolean isForceDarkAllowed() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isForceDarkAllowed()Z");
    }

    public boolean isHandwritingDelegate() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isHandwritingDelegate()Z");
    }

    public boolean isHapticFeedbackEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isHapticFeedbackEnabled()Z");
    }

    public boolean isHardwareAccelerated() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isHardwareAccelerated()Z");
    }

    public boolean isHorizontalFadingEdgeEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isHorizontalFadingEdgeEnabled()Z");
    }

    public boolean isHorizontalScrollBarEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isHorizontalScrollBarEnabled()Z");
    }

    public boolean isHovered() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isHovered()Z");
    }

    public boolean isImportantForAccessibility() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isImportantForAccessibility()Z");
    }

    public final boolean isImportantForAutofill() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isImportantForAutofill()Z");
    }

    public final boolean isImportantForContentCapture() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isImportantForContentCapture()Z");
    }

    public boolean isInEditMode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isInEditMode()Z");
    }

    public boolean isInLayout() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isInLayout()Z");
    }

    public boolean isInTouchMode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isInTouchMode()Z");
    }

    public final boolean isKeyboardNavigationCluster() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isKeyboardNavigationCluster()Z");
    }

    public boolean isLaidOut() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isLaidOut()Z");
    }

    public boolean isLayoutDirectionResolved() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isLayoutDirectionResolved()Z");
    }

    public boolean isLayoutRequested() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isLayoutRequested()Z");
    }

    public boolean isLongClickable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isLongClickable()Z");
    }

    public boolean isNestedScrollingEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isNestedScrollingEnabled()Z");
    }

    public boolean isOpaque() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isOpaque()Z");
    }

    protected boolean isPaddingOffsetRequired() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isPaddingOffsetRequired()Z");
    }

    public boolean isPaddingRelative() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isPaddingRelative()Z");
    }

    public boolean isPivotSet() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isPivotSet()Z");
    }

    public final boolean isPreferKeepClear() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isPreferKeepClear()Z");
    }

    public boolean isPressed() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isPressed()Z");
    }

    public boolean isSaveEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isSaveEnabled()Z");
    }

    public boolean isSaveFromParentEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isSaveFromParentEnabled()Z");
    }

    public boolean isScreenReaderFocusable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isScreenReaderFocusable()Z");
    }

    public boolean isScrollContainer() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isScrollContainer()Z");
    }

    public boolean isScrollbarFadingEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isScrollbarFadingEnabled()Z");
    }

    public boolean isSelected() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isSelected()Z");
    }

    public final boolean isShowingLayoutBounds() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isShowingLayoutBounds()Z");
    }

    public boolean isShown() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isShown()Z");
    }

    public boolean isSoundEffectsEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isSoundEffectsEnabled()Z");
    }

    public final boolean isTemporarilyDetached() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isTemporarilyDetached()Z");
    }

    public boolean isTextAlignmentResolved() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isTextAlignmentResolved()Z");
    }

    public boolean isTextDirectionResolved() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isTextDirectionResolved()Z");
    }

    public boolean isVerticalFadingEdgeEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isVerticalFadingEdgeEnabled()Z");
    }

    public boolean isVerticalScrollBarEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isVerticalScrollBarEnabled()Z");
    }

    public boolean isVisibleToUserForAutofill(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isVisibleToUserForAutofill(I)Z");
    }

    public void jumpDrawablesToCurrentState() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->jumpDrawablesToCurrentState()V");
    }

    public android.view.View keyboardNavigationClusterSearch(android.view.View arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->keyboardNavigationClusterSearch(Landroid/view/View;I)Landroid/view/View;");
    }

    /**
     * Ported from aosp-mirror/platform_frameworks_base
     * core/java/android/view/View.java (layout): bounds + onLayout callback
     * flow. Omitted legs (documented deviations from the AOSP body):
     * the measure-pending re-measure arm (mPrivateFlags3 not tracked yet),
     * optical-layout arm (default LAYOUT_MODE_UNSPECIFIED never takes it),
     * tracing, round-scrollbar, OnLayoutChangeListener notification,
     * focus-transfer and autofill/content-capture notification.
     */
    public void layout(int arg0, int arg1, int arg2, int arg3) {
        int oldL = mLeft;
        int oldT = mTop;
        int oldB = mBottom;
        int oldR = mRight;

        boolean changed = setFrame(arg0, arg1, arg2, arg3);

        if (changed) {
            onLayout(changed, arg0, arg1, arg2, arg3);
        }

        // The rest of the AOSP body (PFLAG_FORCE_LAYOUT/PFLAG3_IS_LAID_OUT
        // bookkeeping and the focus block) is untracked-flag territory.
    }

    /**
     * Ported from the same file (setFrame): bounds-diff assignment pushing
     * into the RenderNode, plus sizeChange when the size differs. Omitted
     * legs: invalidate runs (invalidation machinery unported), ghost
     * views, background/foreground size-change flags, accessibility
     * subtree notification.
     */
    protected boolean setFrame(int arg0, int arg1, int arg2, int arg3) {
        boolean changed = false;

        if (mLeft != arg0 || mRight != arg2 || mTop != arg1 || mBottom != arg3) {
            changed = true;

            int oldWidth = mRight - mLeft;
            int oldHeight = mBottom - mTop;
            int newWidth = arg2 - arg0;
            int newHeight = arg3 - arg1;
            boolean sizeChanged = (newWidth != oldWidth) || (newHeight != oldHeight);

            mLeft = arg0;
            mTop = arg1;
            mRight = arg2;
            mBottom = arg3;
            mRenderNode.setLeftTopRightBottom(mLeft, mTop, mRight, mBottom);

            if (sizeChanged) {
                sizeChange(newWidth, newHeight, oldWidth, oldHeight);
            }
        }
        return changed;
    }

    public final void measure(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->measure(II)V");
    }

    protected static int[] mergeDrawableStates(int[] arg0, int[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->mergeDrawableStates([I[I)[I");
    }

    public void offsetLeftAndRight(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->offsetLeftAndRight(I)V");
    }

    public void offsetTopAndBottom(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->offsetTopAndBottom(I)V");
    }

    protected void onAnimationEnd() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onAnimationEnd()V");
    }

    protected void onAnimationStart() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onAnimationStart()V");
    }

    public android.view.WindowInsets onApplyWindowInsets(android.view.WindowInsets arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onApplyWindowInsets(Landroid/view/WindowInsets;)Landroid/view/WindowInsets;");
    }

    protected void onAttachedToWindow() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onAttachedToWindow()V");
    }

    public void onCancelPendingInputEvents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onCancelPendingInputEvents()V");
    }

    public boolean onCapturedPointerEvent(android.view.MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onCapturedPointerEvent(Landroid/view/MotionEvent;)Z");
    }

    public boolean onCheckIsTextEditor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onCheckIsTextEditor()Z");
    }

    protected void onConfigurationChanged(android.content.res.Configuration arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onConfigurationChanged(Landroid/content/res/Configuration;)V");
    }

    protected void onCreateContextMenu(android.view.ContextMenu arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onCreateContextMenu(Landroid/view/ContextMenu;)V");
    }

    protected int[] onCreateDrawableState(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onCreateDrawableState(I)[I");
    }

    public android.view.inputmethod.InputConnection onCreateInputConnection(android.view.inputmethod.EditorInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onCreateInputConnection(Landroid/view/inputmethod/EditorInfo;)Landroid/view/inputmethod/InputConnection;");
    }

    public void onCreateViewTranslationRequest(int[] arg0, java.util.function.Consumer<android.view.translation.ViewTranslationRequest> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onCreateViewTranslationRequest([ILjava/util/function/Consumer;)V");
    }

    public void onCreateVirtualViewTranslationRequests(long[] arg0, int[] arg1, java.util.function.Consumer<android.view.translation.ViewTranslationRequest> arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onCreateVirtualViewTranslationRequests([J[ILjava/util/function/Consumer;)V");
    }

    protected void onDetachedFromWindow() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onDetachedFromWindow()V");
    }

    protected void onDisplayHint(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onDisplayHint(I)V");
    }

    public boolean onDragEvent(android.view.DragEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onDragEvent(Landroid/view/DragEvent;)Z");
    }

    protected void onDraw(android.graphics.Canvas arg0) {
        // AOSP View.java:22474: default View draws nothing here; subclasses
        // override (guest overrides dispatch through the interpreter).
    }

    public void onDrawForeground(android.graphics.Canvas arg0) {
        // AOSP View.java onDrawForeground: draws only with a foreground set
        // (mForegroundInfo); no foreground model yet, so the null leg runs.
    }

    protected final void onDrawScrollBars(android.graphics.Canvas arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onDrawScrollBars(Landroid/graphics/Canvas;)V");
    }

    public boolean onFilterTouchEventForSecurity(android.view.MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onFilterTouchEventForSecurity(Landroid/view/MotionEvent;)Z");
    }

    protected void onFinishInflate() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onFinishInflate()V");
    }

    public void onFinishTemporaryDetach() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onFinishTemporaryDetach()V");
    }

    protected void onFocusChanged(boolean arg0, int arg1, android.graphics.Rect arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onFocusChanged(ZILandroid/graphics/Rect;)V");
    }

    public boolean onGenericMotionEvent(android.view.MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onGenericMotionEvent(Landroid/view/MotionEvent;)Z");
    }

    public void onHoverChanged(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onHoverChanged(Z)V");
    }

    public boolean onHoverEvent(android.view.MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onHoverEvent(Landroid/view/MotionEvent;)Z");
    }

    public void onInitializeAccessibilityEvent(android.view.accessibility.AccessibilityEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onInitializeAccessibilityEvent(Landroid/view/accessibility/AccessibilityEvent;)V");
    }

    public void onInitializeAccessibilityNodeInfo(android.view.accessibility.AccessibilityNodeInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onInitializeAccessibilityNodeInfo(Landroid/view/accessibility/AccessibilityNodeInfo;)V");
    }

    public boolean onKeyDown(int arg0, android.view.KeyEvent arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onKeyDown(ILandroid/view/KeyEvent;)Z");
    }

    public boolean onKeyLongPress(int arg0, android.view.KeyEvent arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onKeyLongPress(ILandroid/view/KeyEvent;)Z");
    }

    public boolean onKeyMultiple(int arg0, int arg1, android.view.KeyEvent arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onKeyMultiple(IILandroid/view/KeyEvent;)Z");
    }

    public boolean onKeyPreIme(int arg0, android.view.KeyEvent arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onKeyPreIme(ILandroid/view/KeyEvent;)Z");
    }

    public boolean onKeyShortcut(int arg0, android.view.KeyEvent arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onKeyShortcut(ILandroid/view/KeyEvent;)Z");
    }

    public boolean onKeyUp(int arg0, android.view.KeyEvent arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onKeyUp(ILandroid/view/KeyEvent;)Z");
    }

    /**
     * Ported from aosp-mirror/platform_frameworks_base
     * core/java/android/view/View.java (onLayout): empty on device; View
     * subclasses with children override. NOTE: guest overrides of View
     * subclasses are not yet reachable from mirror-body virtual calls
     * (pending the generated-peer migration); guest-override-first applies
     * only at frameworkDispatch ref entry points so far.
     */
    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
    }

    protected void onMeasure(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onMeasure(II)V");
    }

    protected void onOverScrolled(int arg0, int arg1, boolean arg2, boolean arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onOverScrolled(IIZZ)V");
    }

    public void onPointerCaptureChange(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onPointerCaptureChange(Z)V");
    }

    public void onPopulateAccessibilityEvent(android.view.accessibility.AccessibilityEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onPopulateAccessibilityEvent(Landroid/view/accessibility/AccessibilityEvent;)V");
    }

    public void onProvideAutofillStructure(android.view.ViewStructure arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onProvideAutofillStructure(Landroid/view/ViewStructure;I)V");
    }

    public void onProvideAutofillVirtualStructure(android.view.ViewStructure arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onProvideAutofillVirtualStructure(Landroid/view/ViewStructure;I)V");
    }

    public void onProvideContentCaptureStructure(android.view.ViewStructure arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onProvideContentCaptureStructure(Landroid/view/ViewStructure;I)V");
    }

    public void onProvideStructure(android.view.ViewStructure arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onProvideStructure(Landroid/view/ViewStructure;)V");
    }

    public void onProvideVirtualStructure(android.view.ViewStructure arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onProvideVirtualStructure(Landroid/view/ViewStructure;)V");
    }

    public android.view.ContentInfo onReceiveContent(android.view.ContentInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onReceiveContent(Landroid/view/ContentInfo;)Landroid/view/ContentInfo;");
    }

    public android.view.PointerIcon onResolvePointerIcon(android.view.MotionEvent arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onResolvePointerIcon(Landroid/view/MotionEvent;I)Landroid/view/PointerIcon;");
    }

    protected void onRestoreInstanceState(android.os.Parcelable arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onRestoreInstanceState(Landroid/os/Parcelable;)V");
    }

    public void onRtlPropertiesChanged(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onRtlPropertiesChanged(I)V");
    }

    protected android.os.Parcelable onSaveInstanceState() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onSaveInstanceState()Landroid/os/Parcelable;");
    }

    public void onScreenStateChanged(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onScreenStateChanged(I)V");
    }

    public void onScrollCaptureSearch(android.graphics.Rect arg0, android.graphics.Point arg1, java.util.function.Consumer<android.view.ScrollCaptureTarget> arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onScrollCaptureSearch(Landroid/graphics/Rect;Landroid/graphics/Point;Ljava/util/function/Consumer;)V");
    }

    protected void onScrollChanged(int arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onScrollChanged(IIII)V");
    }

    protected boolean onSetAlpha(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onSetAlpha(I)Z");
    }

    /**
     * Ported from aosp-mirror/platform_frameworks_base
     * core/java/android/view/View.java (onSizeChanged): empty hook on
     * device; subclasses size themselves here.
     */
    protected void onSizeChanged(int arg0, int arg1, int arg2, int arg3) {
    }

    /**
     * Ported from the same file (sizeChange): calls the onSizeChanged hook.
     * Omitted legs: frame-rate bookkeeping, size-change listeners.
     */
    private void sizeChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);
    }

    public void onStartTemporaryDetach() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onStartTemporaryDetach()V");
    }

    public boolean onTouchEvent(android.view.MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onTouchEvent(Landroid/view/MotionEvent;)Z");
    }

    public boolean onTrackballEvent(android.view.MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onTrackballEvent(Landroid/view/MotionEvent;)Z");
    }

    public void onViewTranslationResponse(android.view.translation.ViewTranslationResponse arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onViewTranslationResponse(Landroid/view/translation/ViewTranslationResponse;)V");
    }

    public void onVirtualViewTranslationResponses(android.util.LongSparseArray<android.view.translation.ViewTranslationResponse> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onVirtualViewTranslationResponses(Landroid/util/LongSparseArray;)V");
    }

    public void onVisibilityAggregated(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onVisibilityAggregated(Z)V");
    }

    protected void onVisibilityChanged(android.view.View arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onVisibilityChanged(Landroid/view/View;I)V");
    }

    public void onWindowFocusChanged(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onWindowFocusChanged(Z)V");
    }

    public void onWindowSystemUiVisibilityChanged(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onWindowSystemUiVisibilityChanged(I)V");
    }

    protected void onWindowVisibilityChanged(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onWindowVisibilityChanged(I)V");
    }

    protected boolean overScrollBy(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, boolean arg8) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->overScrollBy(IIIIIIIIZ)Z");
    }

    public boolean performAccessibilityAction(int arg0, android.os.Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->performAccessibilityAction(ILandroid/os/Bundle;)Z");
    }

    public boolean performClick() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->performClick()Z");
    }

    public boolean performContextClick() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->performContextClick()Z");
    }

    public boolean performContextClick(float arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->performContextClick(FF)Z");
    }

    public boolean performHapticFeedback(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->performHapticFeedback(I)Z");
    }

    public boolean performHapticFeedback(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->performHapticFeedback(II)Z");
    }

    public boolean performLongClick() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->performLongClick()Z");
    }

    public boolean performLongClick(float arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->performLongClick(FF)Z");
    }

    public android.view.ContentInfo performReceiveContent(android.view.ContentInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->performReceiveContent(Landroid/view/ContentInfo;)Landroid/view/ContentInfo;");
    }

    public void playSoundEffect(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->playSoundEffect(I)V");
    }

    public boolean post(java.lang.Runnable arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->post(Ljava/lang/Runnable;)Z");
    }

    public boolean postDelayed(java.lang.Runnable arg0, long arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->postDelayed(Ljava/lang/Runnable;J)Z");
    }

    public void postInvalidate() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->postInvalidate()V");
    }

    public void postInvalidate(int arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->postInvalidate(IIII)V");
    }

    public void postInvalidateDelayed(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->postInvalidateDelayed(J)V");
    }

    public void postInvalidateDelayed(long arg0, int arg1, int arg2, int arg3, int arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->postInvalidateDelayed(JIIII)V");
    }

    public void postInvalidateOnAnimation() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->postInvalidateOnAnimation()V");
    }

    public void postInvalidateOnAnimation(int arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->postInvalidateOnAnimation(IIII)V");
    }

    public void postOnAnimation(java.lang.Runnable arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->postOnAnimation(Ljava/lang/Runnable;)V");
    }

    public void postOnAnimationDelayed(java.lang.Runnable arg0, long arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->postOnAnimationDelayed(Ljava/lang/Runnable;J)V");
    }

    public void refreshDrawableState() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->refreshDrawableState()V");
    }

    public void releasePointerCapture() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->releasePointerCapture()V");
    }

    public boolean removeCallbacks(java.lang.Runnable arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->removeCallbacks(Ljava/lang/Runnable;)Z");
    }

    /** AOSP View.removeOnAttachStateChangeListener (lines 7842-7848). */
    public void removeOnAttachStateChangeListener(android.view.View.OnAttachStateChangeListener arg0) {
        if (mOnAttachStateChangeListeners == null) {
            return;
        }
        mOnAttachStateChangeListeners.remove(arg0);
    }

    public void removeOnLayoutChangeListener(android.view.View.OnLayoutChangeListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->removeOnLayoutChangeListener(Landroid/view/View$OnLayoutChangeListener;)V");
    }

    public void removeOnUnhandledKeyEventListener(android.view.View.OnUnhandledKeyEventListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->removeOnUnhandledKeyEventListener(Landroid/view/View$OnUnhandledKeyEventListener;)V");
    }

    public void reportAppJankStats(android.app.jank.AppJankStats arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->reportAppJankStats(Landroid/app/jank/AppJankStats;)V");
    }

    public void requestApplyInsets() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->requestApplyInsets()V");
    }

    public void requestFitSystemWindows() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->requestFitSystemWindows()V");
    }

    public final boolean requestFocus() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->requestFocus()Z");
    }

    public final boolean requestFocus(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->requestFocus(I)Z");
    }

    public boolean requestFocus(int arg0, android.graphics.Rect arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->requestFocus(ILandroid/graphics/Rect;)Z");
    }

    public final boolean requestFocusFromTouch() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->requestFocusFromTouch()Z");
    }

    public void requestLayout() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->requestLayout()V");
    }

    public void requestPointerCapture() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->requestPointerCapture()V");
    }

    public boolean requestRectangleOnScreen(android.graphics.Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->requestRectangleOnScreen(Landroid/graphics/Rect;)Z");
    }

    public boolean requestRectangleOnScreen(android.graphics.Rect arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->requestRectangleOnScreen(Landroid/graphics/Rect;Z)Z");
    }

    public final void requestUnbufferedDispatch(android.view.MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->requestUnbufferedDispatch(Landroid/view/MotionEvent;)V");
    }

    public final void requestUnbufferedDispatch(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->requestUnbufferedDispatch(I)V");
    }

    public final <T> T requireViewById(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->requireViewById(I)Landroid/view/View;");
    }

    public void resetPivot() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->resetPivot()V");
    }

    public static int resolveSize(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->resolveSize(II)I");
    }

    public static int resolveSizeAndState(int arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->resolveSizeAndState(III)I");
    }

    public boolean restoreDefaultFocus() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->restoreDefaultFocus()Z");
    }

    public void restoreHierarchyState(android.util.SparseArray<android.os.Parcelable> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->restoreHierarchyState(Landroid/util/SparseArray;)V");
    }

    public final void saveAttributeDataForStyleable(android.content.Context arg0, int[] arg1, android.util.AttributeSet arg2, android.content.res.TypedArray arg3, int arg4, int arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->saveAttributeDataForStyleable(Landroid/content/Context;[ILandroid/util/AttributeSet;Landroid/content/res/TypedArray;II)V");
    }

    public void saveHierarchyState(android.util.SparseArray<android.os.Parcelable> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->saveHierarchyState(Landroid/util/SparseArray;)V");
    }

    public void scheduleDrawable(android.graphics.drawable.Drawable arg0, java.lang.Runnable arg1, long arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->scheduleDrawable(Landroid/graphics/drawable/Drawable;Ljava/lang/Runnable;J)V");
    }

    public void scrollBy(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->scrollBy(II)V");
    }

    public void scrollTo(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->scrollTo(II)V");
    }

    public void sendAccessibilityEvent(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->sendAccessibilityEvent(I)V");
    }

    public void sendAccessibilityEventUnchecked(android.view.accessibility.AccessibilityEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->sendAccessibilityEventUnchecked(Landroid/view/accessibility/AccessibilityEvent;)V");
    }

    public void setAccessibilityDataSensitive(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setAccessibilityDataSensitive(I)V");
    }

    public void setAccessibilityDelegate(android.view.View.AccessibilityDelegate arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setAccessibilityDelegate(Landroid/view/View$AccessibilityDelegate;)V");
    }

    public void setAccessibilityHeading(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setAccessibilityHeading(Z)V");
    }

    public void setAccessibilityLiveRegion(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setAccessibilityLiveRegion(I)V");
    }

    public void setAccessibilityPaneTitle(java.lang.CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setAccessibilityPaneTitle(Ljava/lang/CharSequence;)V");
    }

    public void setAccessibilityTraversalAfter(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setAccessibilityTraversalAfter(I)V");
    }

    public void setAccessibilityTraversalBefore(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setAccessibilityTraversalBefore(I)V");
    }

    public void setActivated(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setActivated(Z)V");
    }

    public void setAllowClickWhenDisabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setAllowClickWhenDisabled(Z)V");
    }

    public void setAllowedHandwritingDelegatePackage(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setAllowedHandwritingDelegatePackage(Ljava/lang/String;)V");
    }

    public void setAllowedHandwritingDelegatorPackage(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setAllowedHandwritingDelegatorPackage(Ljava/lang/String;)V");
    }

    public void setAlpha(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setAlpha(F)V");
    }

    public void setAnimation(android.view.animation.Animation arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setAnimation(Landroid/view/animation/Animation;)V");
    }

    public void setAnimationMatrix(android.graphics.Matrix arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setAnimationMatrix(Landroid/graphics/Matrix;)V");
    }

    public void setAutoHandwritingEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setAutoHandwritingEnabled(Z)V");
    }

    public void setAutofillHints(java.lang.String[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setAutofillHints([Ljava/lang/String;)V");
    }

    public void setAutofillId(android.view.autofill.AutofillId arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setAutofillId(Landroid/view/autofill/AutofillId;)V");
    }

    public void setBackground(android.graphics.drawable.Drawable arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setBackground(Landroid/graphics/drawable/Drawable;)V");
    }

    public void setBackgroundColor(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setBackgroundColor(I)V");
    }

    public void setBackgroundDrawable(android.graphics.drawable.Drawable arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setBackgroundDrawable(Landroid/graphics/drawable/Drawable;)V");
    }

    public void setBackgroundResource(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setBackgroundResource(I)V");
    }

    public void setBackgroundTintBlendMode(android.graphics.BlendMode arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setBackgroundTintBlendMode(Landroid/graphics/BlendMode;)V");
    }

    public void setBackgroundTintList(android.content.res.ColorStateList arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setBackgroundTintList(Landroid/content/res/ColorStateList;)V");
    }

    public void setBackgroundTintMode(android.graphics.PorterDuff.Mode arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setBackgroundTintMode(Landroid/graphics/PorterDuff$Mode;)V");
    }

    public final void setBottom(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setBottom(I)V");
    }

    public void setCameraDistance(float arg0) {
        // Ported from AOSP setCameraDistance: Math.abs(distance) / dpi;
        // dpi = 1 pending the Resources hand-field (mirrors the getter).
        mRenderNode.setCameraDistance(Math.abs(arg0) / 1.0f);
    }

    public void setClickable(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setClickable(Z)V");
    }

    /**
     * Ported from aosp-mirror/platform_frameworks_base
     * core/java/android/view/View.java (setClipBounds): identity/equals
     * guarded, mClipBounds copy-or-set, RenderNode delegation; the
     * invalidateViewProperty leg is unported invalidation machinery.
     */
    public void setClipBounds(android.graphics.Rect arg0) {
        if (arg0 == mClipBounds
                || (arg0 != null && arg0.equals(mClipBounds))) {
            return;
        }
        if (arg0 != null) {
            if (mClipBounds == null) {
                mClipBounds = new android.graphics.Rect(arg0);
            } else {
                mClipBounds.set(arg0);
            }
        } else {
            mClipBounds = null;
        }
        mRenderNode.setClipRect(mClipBounds);
    }

    /**
     * Ported from aosp-mirror/platform_frameworks_base
     * core/java/android/view/View.java (setClipToOutline): change-guarded
     * RenderNode delegation; the damageInParent leg is unported
     * invalidation machinery.
     */
    public void setClipToOutline(boolean arg0) {
        if (getClipToOutline() != arg0) {
            mRenderNode.setClipToOutline(arg0);
        }
    }

    public void setContentCaptureSession(android.view.contentcapture.ContentCaptureSession arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setContentCaptureSession(Landroid/view/contentcapture/ContentCaptureSession;)V");
    }

    public void setContentDescription(java.lang.CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setContentDescription(Ljava/lang/CharSequence;)V");
    }

    public final void setContentSensitivity(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setContentSensitivity(I)V");
    }

    public void setContextClickable(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setContextClickable(Z)V");
    }

    public void setDefaultFocusHighlightEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setDefaultFocusHighlightEnabled(Z)V");
    }

    public void setDrawingCacheBackgroundColor(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setDrawingCacheBackgroundColor(I)V");
    }

    public void setDrawingCacheEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setDrawingCacheEnabled(Z)V");
    }

    public void setDrawingCacheQuality(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setDrawingCacheQuality(I)V");
    }

    public void setDuplicateParentStateEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setDuplicateParentStateEnabled(Z)V");
    }

    /**
     * Ported from AOSP setElevation: guard + sanitize + RenderNode write;
     * invalidation legs omitted (no draw pipeline yet).
     */
    public void setElevation(float arg0) {
        if (arg0 != getElevation()) {
            arg0 = sanitizeFloatPropertyValue(arg0, "elevation");
            mRenderNode.setElevation(arg0);
        }
    }

    public void setEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setEnabled(Z)V");
    }

    public void setFadingEdgeLength(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setFadingEdgeLength(I)V");
    }

    public void setFilterTouchesWhenObscured(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setFilterTouchesWhenObscured(Z)V");
    }

    public void setFitsSystemWindows(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setFitsSystemWindows(Z)V");
    }

    public void setFocusable(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setFocusable(Z)V");
    }

    public void setFocusable(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setFocusable(I)V");
    }

    public void setFocusableInTouchMode(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setFocusableInTouchMode(Z)V");
    }

    public void setFocusedByDefault(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setFocusedByDefault(Z)V");
    }

    public void setForceDarkAllowed(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setForceDarkAllowed(Z)V");
    }

    public void setForeground(android.graphics.drawable.Drawable arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setForeground(Landroid/graphics/drawable/Drawable;)V");
    }

    public void setForegroundGravity(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setForegroundGravity(I)V");
    }

    public void setForegroundTintBlendMode(android.graphics.BlendMode arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setForegroundTintBlendMode(Landroid/graphics/BlendMode;)V");
    }

    public void setForegroundTintList(android.content.res.ColorStateList arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setForegroundTintList(Landroid/content/res/ColorStateList;)V");
    }

    public void setForegroundTintMode(android.graphics.PorterDuff.Mode arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setForegroundTintMode(Landroid/graphics/PorterDuff$Mode;)V");
    }

    public void setFrameContentVelocity(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setFrameContentVelocity(F)V");
    }

    public void setHandwritingBoundsOffsets(float arg0, float arg1, float arg2, float arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setHandwritingBoundsOffsets(FFFF)V");
    }

    public void setHandwritingDelegateFlags(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setHandwritingDelegateFlags(I)V");
    }

    public void setHandwritingDelegatorCallback(java.lang.Runnable arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setHandwritingDelegatorCallback(Ljava/lang/Runnable;)V");
    }

    public void setHapticFeedbackEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setHapticFeedbackEnabled(Z)V");
    }

    public void setHasTransientState(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setHasTransientState(Z)V");
    }

    public void setHorizontalFadingEdgeEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setHorizontalFadingEdgeEnabled(Z)V");
    }

    public void setHorizontalScrollBarEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setHorizontalScrollBarEnabled(Z)V");
    }

    public void setHorizontalScrollbarThumbDrawable(android.graphics.drawable.Drawable arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setHorizontalScrollbarThumbDrawable(Landroid/graphics/drawable/Drawable;)V");
    }

    public void setHorizontalScrollbarTrackDrawable(android.graphics.drawable.Drawable arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setHorizontalScrollbarTrackDrawable(Landroid/graphics/drawable/Drawable;)V");
    }

    public void setHovered(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setHovered(Z)V");
    }

    /**
     * AOSP View.setId (lines 27941-27946) — device body stores the id and
     * auto-reserves one when clearing to NO_ID while mLabelForId points
     * at another view; the shell carries the store only (the labelFor
     * auto-id branch lands with the labelFor port).
     */
    public void setId(int arg0) {
        mID = arg0;
    }

    public void setImportantForAccessibility(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setImportantForAccessibility(I)V");
    }

    public void setImportantForAutofill(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setImportantForAutofill(I)V");
    }

    public void setImportantForContentCapture(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setImportantForContentCapture(I)V");
    }

    public void setIsCredential(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setIsCredential(Z)V");
    }

    public void setIsHandwritingDelegate(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setIsHandwritingDelegate(Z)V");
    }

    public void setKeepScreenOn(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setKeepScreenOn(Z)V");
    }

    public void setKeyboardNavigationCluster(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setKeyboardNavigationCluster(Z)V");
    }

    public void setLabelFor(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setLabelFor(I)V");
    }

    public void setLayerPaint(android.graphics.Paint arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setLayerPaint(Landroid/graphics/Paint;)V");
    }

    public void setLayerType(int arg0, android.graphics.Paint arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setLayerType(ILandroid/graphics/Paint;)V");
    }

    public void setLayoutDirection(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setLayoutDirection(I)V");
    }

    public void setLayoutParams(android.view.ViewGroup.LayoutParams arg0) {
        mLayoutParams = arg0;
    }

    public final void setLeft(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setLeft(I)V");
    }

    /** Ported from the same file (setLeftTopRightBottom): setFrame. */
    public final void setLeftTopRightBottom(int arg0, int arg1, int arg2, int arg3) {
        setFrame(arg0, arg1, arg2, arg3);
    }

    public void setLongClickable(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setLongClickable(Z)V");
    }

    protected final void setMeasuredDimension(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setMeasuredDimension(II)V");
    }

    public void setMinimumHeight(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setMinimumHeight(I)V");
    }

    public void setMinimumWidth(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setMinimumWidth(I)V");
    }

    public void setNestedScrollingEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setNestedScrollingEnabled(Z)V");
    }

    public void setNextClusterForwardId(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setNextClusterForwardId(I)V");
    }

    public void setNextFocusDownId(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setNextFocusDownId(I)V");
    }

    public void setNextFocusForwardId(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setNextFocusForwardId(I)V");
    }

    public void setNextFocusLeftId(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setNextFocusLeftId(I)V");
    }

    public void setNextFocusRightId(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setNextFocusRightId(I)V");
    }

    public void setNextFocusUpId(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setNextFocusUpId(I)V");
    }

    public void setOnApplyWindowInsetsListener(android.view.View.OnApplyWindowInsetsListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setOnApplyWindowInsetsListener(Landroid/view/View$OnApplyWindowInsetsListener;)V");
    }

    public void setOnCapturedPointerListener(android.view.View.OnCapturedPointerListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setOnCapturedPointerListener(Landroid/view/View$OnCapturedPointerListener;)V");
    }

    public void setOnClickListener(android.view.View.OnClickListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setOnClickListener(Landroid/view/View$OnClickListener;)V");
    }

    public void setOnContextClickListener(android.view.View.OnContextClickListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setOnContextClickListener(Landroid/view/View$OnContextClickListener;)V");
    }

    public void setOnCreateContextMenuListener(android.view.View.OnCreateContextMenuListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setOnCreateContextMenuListener(Landroid/view/View$OnCreateContextMenuListener;)V");
    }

    public void setOnDragListener(android.view.View.OnDragListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setOnDragListener(Landroid/view/View$OnDragListener;)V");
    }

    public void setOnFocusChangeListener(android.view.View.OnFocusChangeListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setOnFocusChangeListener(Landroid/view/View$OnFocusChangeListener;)V");
    }

    public void setOnGenericMotionListener(android.view.View.OnGenericMotionListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setOnGenericMotionListener(Landroid/view/View$OnGenericMotionListener;)V");
    }

    public void setOnHoverListener(android.view.View.OnHoverListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setOnHoverListener(Landroid/view/View$OnHoverListener;)V");
    }

    public void setOnKeyListener(android.view.View.OnKeyListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setOnKeyListener(Landroid/view/View$OnKeyListener;)V");
    }

    public void setOnLongClickListener(android.view.View.OnLongClickListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setOnLongClickListener(Landroid/view/View$OnLongClickListener;)V");
    }

    public void setOnReceiveContentListener(java.lang.String[] arg0, android.view.OnReceiveContentListener arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setOnReceiveContentListener([Ljava/lang/String;Landroid/view/OnReceiveContentListener;)V");
    }

    public void setOnScrollChangeListener(android.view.View.OnScrollChangeListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setOnScrollChangeListener(Landroid/view/View$OnScrollChangeListener;)V");
    }

    public void setOnSystemUiVisibilityChangeListener(android.view.View.OnSystemUiVisibilityChangeListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setOnSystemUiVisibilityChangeListener(Landroid/view/View$OnSystemUiVisibilityChangeListener;)V");
    }

    public void setOnTouchListener(android.view.View.OnTouchListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setOnTouchListener(Landroid/view/View$OnTouchListener;)V");
    }

    public void setOutlineAmbientShadowColor(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setOutlineAmbientShadowColor(I)V");
    }

    public void setOutlineSpotShadowColor(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setOutlineSpotShadowColor(I)V");
    }

    public void setOverScrollMode(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setOverScrollMode(I)V");
    }

    public void setPadding(int arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setPadding(IIII)V");
    }

    public void setPaddingRelative(int arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setPaddingRelative(IIII)V");
    }

    public void setPendingCredentialRequest(android.credentials.GetCredentialRequest arg0, android.os.OutcomeReceiver<android.credentials.GetCredentialResponse, android.credentials.GetCredentialException> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setPendingCredentialRequest(Landroid/credentials/GetCredentialRequest;Landroid/os/OutcomeReceiver;)V");
    }

    /**
     * Ported from aosp-mirror/platform_frameworks_base
     * core/java/android/view/View.java (setPivotX): conditionally delegates
     * to the RenderNode; the invalidateViewProperty /
     * invalidateParentIfNeededAndWasQuickRejected legs are unported
     * invalidation machinery (no draw pipeline yet).
     */
    public void setPivotX(float arg0) {
        if (!mRenderNode.isPivotExplicitlySet() || arg0 != getPivotX()) {
            mRenderNode.setPivotX(arg0);
        }
    }

    public void setPivotY(float arg0) {
        if (!mRenderNode.isPivotExplicitlySet() || arg0 != getPivotY()) {
            mRenderNode.setPivotY(arg0);
        }
    }

    public void setPointerIcon(android.view.PointerIcon arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setPointerIcon(Landroid/view/PointerIcon;)V");
    }

    public final void setPreferKeepClear(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setPreferKeepClear(Z)V");
    }

    public final void setPreferKeepClearRects(java.util.List<android.graphics.Rect> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setPreferKeepClearRects(Ljava/util/List;)V");
    }

    public void setPressed(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setPressed(Z)V");
    }

    public void setRenderEffect(android.graphics.RenderEffect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setRenderEffect(Landroid/graphics/RenderEffect;)V");
    }

    public void setRequestedFrameRate(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setRequestedFrameRate(F)V");
    }

    public final void setRevealOnFocusHint(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setRevealOnFocusHint(Z)V");
    }

    public final void setRight(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setRight(I)V");
    }

    /**
     * Rotation setters ported from the same file (setRotation delegates to
     * the node's rotationZ): change-guarded writes; invalidation and
     * accessibility-notification legs omitted.
     */
    public void setRotation(float arg0) {
        if (arg0 != getRotation()) {
            mRenderNode.setRotationZ(arg0);
        }
    }

    public void setRotationX(float arg0) {
        if (arg0 != getRotationX()) {
            mRenderNode.setRotationX(arg0);
        }
    }

    public void setRotationY(float arg0) {
        if (arg0 != getRotationY()) {
            mRenderNode.setRotationY(arg0);
        }
    }

    public void setSaveEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setSaveEnabled(Z)V");
    }

    public void setSaveFromParentEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setSaveFromParentEnabled(Z)V");
    }

    /**
     * Scale setters ported from AOSP setScaleX/setScaleY: guard + sanitize
     * (scale values clamp); invalidation legs omitted.
     */
    public void setScaleX(float arg0) {
        if (arg0 != getScaleX()) {
            arg0 = sanitizeFloatPropertyValue(arg0, "scaleX");
            mRenderNode.setScaleX(arg0);
        }
    }

    public void setScaleY(float arg0) {
        if (arg0 != getScaleY()) {
            arg0 = sanitizeFloatPropertyValue(arg0, "scaleY");
            mRenderNode.setScaleY(arg0);
        }
    }

    public void setScreenReaderFocusable(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setScreenReaderFocusable(Z)V");
    }

    public void setScrollBarDefaultDelayBeforeFade(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setScrollBarDefaultDelayBeforeFade(I)V");
    }

    public void setScrollBarFadeDuration(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setScrollBarFadeDuration(I)V");
    }

    public void setScrollBarSize(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setScrollBarSize(I)V");
    }

    public void setScrollBarStyle(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setScrollBarStyle(I)V");
    }

    public final void setScrollCaptureCallback(android.view.ScrollCaptureCallback arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setScrollCaptureCallback(Landroid/view/ScrollCaptureCallback;)V");
    }

    public void setScrollCaptureHint(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setScrollCaptureHint(I)V");
    }

    public void setScrollContainer(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setScrollContainer(Z)V");
    }

    public void setScrollIndicators(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setScrollIndicators(I)V");
    }

    public void setScrollIndicators(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setScrollIndicators(II)V");
    }

    public void setScrollX(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setScrollX(I)V");
    }

    public void setScrollY(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setScrollY(I)V");
    }

    public void setScrollbarFadingEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setScrollbarFadingEnabled(Z)V");
    }

    public void setSelected(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setSelected(Z)V");
    }

    public void setSoundEffectsEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setSoundEffectsEnabled(Z)V");
    }

    public void setStateDescription(java.lang.CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setStateDescription(Ljava/lang/CharSequence;)V");
    }

    public void setStateListAnimator(android.animation.StateListAnimator arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setStateListAnimator(Landroid/animation/StateListAnimator;)V");
    }

    public void setSupplementalDescription(java.lang.CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setSupplementalDescription(Ljava/lang/CharSequence;)V");
    }

    public void setSystemGestureExclusionRects(java.util.List<android.graphics.Rect> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setSystemGestureExclusionRects(Ljava/util/List;)V");
    }

    /**
     * AOSP View.setSystemUiVisibility (lines 28990+): the device stores the
     * flag and dispatches visibility changes; the shell keeps the field
     * store only (no window/display layer listens yet). The androidx
     * shim layer reads the same value back through getSystemUiVisibility
     * and merges system-ui flags on top, so round-tripping the int is what
     * the compat path needs.
     */
    public void setSystemUiVisibility(int arg0) {
        mSystemUiVisibility = arg0;
    }

    /**
     * AOSP View.setTag(int, Object) (lines 28068-28077): rejects keys
     * below the 0x02000000 package-id floor (undefined/framework keys) the
     * same way, then stores the keyed tag.
     */
    public void setTag(int arg0, java.lang.Object arg1) {
        if ((arg0 >>> 24) < 2) {
            throw new IllegalArgumentException("The key must be an application-specific "
                    + "resource id.");
        }
        if (mKeyedTags == null) {
            mKeyedTags = new java.util.HashMap<Integer, java.lang.Object>();
        }
        mKeyedTags.put(arg0, arg1);
    }

    private java.util.HashMap<Integer, java.lang.Object> mKeyedTags;

    /** AOSP View.setTag(Object): plain mTag store. */
    public void setTag(java.lang.Object arg0) {
        mTag = arg0;
    }

    private java.lang.Object mTag;

    public void setTextAlignment(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setTextAlignment(I)V");
    }

    public void setTextDirection(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setTextDirection(I)V");
    }

    public void setTooltipText(java.lang.CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setTooltipText(Ljava/lang/CharSequence;)V");
    }

    public final void setTop(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setTop(I)V");
    }

    public void setTouchDelegate(android.view.TouchDelegate arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setTouchDelegate(Landroid/view/TouchDelegate;)V");
    }

    public void setTransitionAlpha(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setTransitionAlpha(F)V");
    }

    public final void setTransitionName(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setTransitionName(Ljava/lang/String;)V");
    }

    public void setTransitionVisibility(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setTransitionVisibility(I)V");
    }

    /**
     * Translation setters ported from the same file: guard writes;
     * setTranslationZ sanitizes like AOSP; the PFLAG4_HAS_MOVED and
     * invalidation legs are untracked-flag/unported-machinery territory.
     */
    public void setTranslationX(float arg0) {
        if (arg0 != getTranslationX()) {
            mRenderNode.setTranslationX(arg0);
        }
    }

    public void setTranslationY(float arg0) {
        if (arg0 != getTranslationY()) {
            mRenderNode.setTranslationY(arg0);
        }
    }

    public void setTranslationZ(float arg0) {
        if (arg0 != getTranslationZ()) {
            arg0 = sanitizeFloatPropertyValue(arg0, "translationZ");
            mRenderNode.setTranslationZ(arg0);
        }
    }

    public void setVerticalFadingEdgeEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setVerticalFadingEdgeEnabled(Z)V");
    }

    public void setVerticalScrollBarEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setVerticalScrollBarEnabled(Z)V");
    }

    public void setVerticalScrollbarPosition(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setVerticalScrollbarPosition(I)V");
    }

    public void setVerticalScrollbarThumbDrawable(android.graphics.drawable.Drawable arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setVerticalScrollbarThumbDrawable(Landroid/graphics/drawable/Drawable;)V");
    }

    public void setVerticalScrollbarTrackDrawable(android.graphics.drawable.Drawable arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setVerticalScrollbarTrackDrawable(Landroid/graphics/drawable/Drawable;)V");
    }

    public void setViewTranslationCallback(android.view.translation.ViewTranslationCallback arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setViewTranslationCallback(Landroid/view/translation/ViewTranslationCallback;)V");
    }

    public void setVisibility(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setVisibility(I)V");
    }

    public void setWillNotCacheDrawing(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setWillNotCacheDrawing(Z)V");
    }

    public void setWillNotDraw(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setWillNotDraw(Z)V");
    }

    public void setWindowInsetsAnimationCallback(android.view.WindowInsetsAnimation.Callback arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setWindowInsetsAnimationCallback(Landroid/view/WindowInsetsAnimation$Callback;)V");
    }

    public void setX(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setX(F)V");
    }

    public void setY(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setY(F)V");
    }

    public void setZ(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setZ(F)V");
    }

    public boolean showContextMenu() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->showContextMenu()Z");
    }

    public boolean showContextMenu(float arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->showContextMenu(FF)Z");
    }

    public android.view.ActionMode startActionMode(android.view.ActionMode.Callback arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->startActionMode(Landroid/view/ActionMode$Callback;)Landroid/view/ActionMode;");
    }

    public android.view.ActionMode startActionMode(android.view.ActionMode.Callback arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->startActionMode(Landroid/view/ActionMode$Callback;I)Landroid/view/ActionMode;");
    }

    public void startAnimation(android.view.animation.Animation arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->startAnimation(Landroid/view/animation/Animation;)V");
    }

    public final boolean startDrag(android.content.ClipData arg0, android.view.View.DragShadowBuilder arg1, java.lang.Object arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->startDrag(Landroid/content/ClipData;Landroid/view/View$DragShadowBuilder;Ljava/lang/Object;I)Z");
    }

    public final boolean startDragAndDrop(android.content.ClipData arg0, android.view.View.DragShadowBuilder arg1, java.lang.Object arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->startDragAndDrop(Landroid/content/ClipData;Landroid/view/View$DragShadowBuilder;Ljava/lang/Object;I)Z");
    }

    public boolean startNestedScroll(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->startNestedScroll(I)Z");
    }

    public void stopNestedScroll() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->stopNestedScroll()V");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->toString()Ljava/lang/String;");
    }

    public void transformMatrixToGlobal(android.graphics.Matrix arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->transformMatrixToGlobal(Landroid/graphics/Matrix;)V");
    }

    public void transformMatrixToLocal(android.graphics.Matrix arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->transformMatrixToLocal(Landroid/graphics/Matrix;)V");
    }

    public void unscheduleDrawable(android.graphics.drawable.Drawable arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->unscheduleDrawable(Landroid/graphics/drawable/Drawable;)V");
    }

    public void unscheduleDrawable(android.graphics.drawable.Drawable arg0, java.lang.Runnable arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->unscheduleDrawable(Landroid/graphics/drawable/Drawable;Ljava/lang/Runnable;)V");
    }

    public final void updateDragShadow(android.view.View.DragShadowBuilder arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->updateDragShadow(Landroid/view/View$DragShadowBuilder;)V");
    }

    protected boolean verifyDrawable(android.graphics.drawable.Drawable arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->verifyDrawable(Landroid/graphics/drawable/Drawable;)Z");
    }

    public boolean willNotCacheDrawing() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->willNotCacheDrawing()Z");
    }

    public boolean willNotDraw() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->willNotDraw()Z");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public View() {
        }
        public static class AccessibilityDelegate {

        public AccessibilityDelegate() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$AccessibilityDelegate;-><init>()V");
        }

        public void addExtraDataToAccessibilityNodeInfo(android.view.View arg0, android.view.accessibility.AccessibilityNodeInfo arg1, java.lang.String arg2, android.os.Bundle arg3) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$AccessibilityDelegate;->addExtraDataToAccessibilityNodeInfo(Landroid/view/View;Landroid/view/accessibility/AccessibilityNodeInfo;Ljava/lang/String;Landroid/os/Bundle;)V");
        }

        public boolean dispatchPopulateAccessibilityEvent(android.view.View arg0, android.view.accessibility.AccessibilityEvent arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$AccessibilityDelegate;->dispatchPopulateAccessibilityEvent(Landroid/view/View;Landroid/view/accessibility/AccessibilityEvent;)Z");
        }

        public android.view.accessibility.AccessibilityNodeProvider getAccessibilityNodeProvider(android.view.View arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$AccessibilityDelegate;->getAccessibilityNodeProvider(Landroid/view/View;)Landroid/view/accessibility/AccessibilityNodeProvider;");
        }

        public void onInitializeAccessibilityEvent(android.view.View arg0, android.view.accessibility.AccessibilityEvent arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$AccessibilityDelegate;->onInitializeAccessibilityEvent(Landroid/view/View;Landroid/view/accessibility/AccessibilityEvent;)V");
        }

        public void onInitializeAccessibilityNodeInfo(android.view.View arg0, android.view.accessibility.AccessibilityNodeInfo arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$AccessibilityDelegate;->onInitializeAccessibilityNodeInfo(Landroid/view/View;Landroid/view/accessibility/AccessibilityNodeInfo;)V");
        }

        public void onPopulateAccessibilityEvent(android.view.View arg0, android.view.accessibility.AccessibilityEvent arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$AccessibilityDelegate;->onPopulateAccessibilityEvent(Landroid/view/View;Landroid/view/accessibility/AccessibilityEvent;)V");
        }

        public boolean onRequestSendAccessibilityEvent(android.view.ViewGroup arg0, android.view.View arg1, android.view.accessibility.AccessibilityEvent arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$AccessibilityDelegate;->onRequestSendAccessibilityEvent(Landroid/view/ViewGroup;Landroid/view/View;Landroid/view/accessibility/AccessibilityEvent;)Z");
        }

        public boolean performAccessibilityAction(android.view.View arg0, int arg1, android.os.Bundle arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$AccessibilityDelegate;->performAccessibilityAction(Landroid/view/View;ILandroid/os/Bundle;)Z");
        }

        public void sendAccessibilityEvent(android.view.View arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$AccessibilityDelegate;->sendAccessibilityEvent(Landroid/view/View;I)V");
        }

        public void sendAccessibilityEventUnchecked(android.view.View arg0, android.view.accessibility.AccessibilityEvent arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$AccessibilityDelegate;->sendAccessibilityEventUnchecked(Landroid/view/View;Landroid/view/accessibility/AccessibilityEvent;)V");
        }
        }
        public static class DragShadowBuilder {

        public DragShadowBuilder() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$DragShadowBuilder;-><init>()V");
        }

        public DragShadowBuilder(android.view.View arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$DragShadowBuilder;-><init>(Landroid/view/View;)V");
        }

        public final android.view.View getView() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$DragShadowBuilder;->getView()Landroid/view/View;");
        }

        public void onDrawShadow(android.graphics.Canvas arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$DragShadowBuilder;->onDrawShadow(Landroid/graphics/Canvas;)V");
        }

        public void onProvideShadowMetrics(android.graphics.Point arg0, android.graphics.Point arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$DragShadowBuilder;->onProvideShadowMetrics(Landroid/graphics/Point;Landroid/graphics/Point;)V");
        }
        }
        public static interface OnApplyWindowInsetsListener {

        public abstract android.view.WindowInsets onApplyWindowInsets(android.view.View arg0, android.view.WindowInsets arg1);
        }
        public static interface OnAttachStateChangeListener {

        public abstract void onViewAttachedToWindow(android.view.View arg0);

        public abstract void onViewDetachedFromWindow(android.view.View arg0);
        }
        public static interface OnCapturedPointerListener {

        public abstract boolean onCapturedPointer(android.view.View arg0, android.view.MotionEvent arg1);
        }
        public static interface OnClickListener {

        public abstract void onClick(android.view.View arg0);
        }
        public static interface OnContextClickListener {

        public abstract boolean onContextClick(android.view.View arg0);
        }
        public static interface OnCreateContextMenuListener {

        public abstract void onCreateContextMenu(android.view.ContextMenu arg0, android.view.View arg1, android.view.ContextMenu.ContextMenuInfo arg2);
        }
        public static interface OnDragListener {

        public abstract boolean onDrag(android.view.View arg0, android.view.DragEvent arg1);
        }
        public static interface OnFocusChangeListener {

        public abstract void onFocusChange(android.view.View arg0, boolean arg1);
        }
        public static interface OnGenericMotionListener {

        public abstract boolean onGenericMotion(android.view.View arg0, android.view.MotionEvent arg1);
        }
        public static interface OnHoverListener {

        public abstract boolean onHover(android.view.View arg0, android.view.MotionEvent arg1);
        }
        public static interface OnKeyListener {

        public abstract boolean onKey(android.view.View arg0, int arg1, android.view.KeyEvent arg2);
        }
        public static interface OnLayoutChangeListener {

        public abstract void onLayoutChange(android.view.View arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8);
        }
        public static interface OnLongClickListener {

        public abstract boolean onLongClick(android.view.View arg0);

        public default boolean onLongClickUseDefaultHapticFeedback(android.view.View arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$OnLongClickListener;->onLongClickUseDefaultHapticFeedback(Landroid/view/View;)Z");
        }
        }
        public static interface OnScrollChangeListener {

        public abstract void onScrollChange(android.view.View arg0, int arg1, int arg2, int arg3, int arg4);
        }
        public static interface OnSystemUiVisibilityChangeListener {

        public abstract void onSystemUiVisibilityChange(int arg0);
        }
        public static interface OnTouchListener {

        public abstract boolean onTouch(android.view.View arg0, android.view.MotionEvent arg1);
        }
        public static interface OnUnhandledKeyEventListener {

        public abstract boolean onUnhandledKeyEvent(android.view.View arg0, android.view.KeyEvent arg1);
        }
    }
