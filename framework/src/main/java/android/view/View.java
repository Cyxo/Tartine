package android.view;

import android.animation.StateListAnimator;
import android.app.jank.AppJankStats;
import android.content.ClipData;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.credentials.GetCredentialException;
import android.credentials.GetCredentialRequest;
import android.credentials.GetCredentialResponse;
import android.graphics.Bitmap;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.RenderEffect;
import android.graphics.RenderNode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.OutcomeReceiver;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.LongSparseArray;
import android.util.Property;
import android.util.SparseArray;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityEventSource;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import android.view.animation.Animation;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillValue;
import android.view.contentcapture.ContentCaptureSession;
import android.view.displayhash.DisplayHashResultCallback;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.translation.TranslationCapability;
import android.view.translation.ViewTranslationCallback;
import android.view.translation.ViewTranslationRequest;
import android.view.translation.ViewTranslationResponse;
import android.window.OnBackInvokedDispatcher;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

    public class View implements AccessibilityEventSource, Drawable.Callback, KeyEvent.Callback {

    public static final int ACCESSIBILITY_DATA_SENSITIVE_AUTO = 0;
    public static final int ACCESSIBILITY_DATA_SENSITIVE_NO = 2;
    public static final int ACCESSIBILITY_DATA_SENSITIVE_YES = 1;
    public static final int ACCESSIBILITY_LIVE_REGION_ASSERTIVE = 2;
    public static final int ACCESSIBILITY_LIVE_REGION_NONE = 0;
    public static final int ACCESSIBILITY_LIVE_REGION_POLITE = 1;
    public static Property ALPHA;
    public static final int AUTOFILL_FLAG_INCLUDE_NOT_IMPORTANT_VIEWS = 1;
    public static final String AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DATE = "creditCardExpirationDate";
    public static final String AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DAY = "creditCardExpirationDay";
    public static final String AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_MONTH = "creditCardExpirationMonth";
    public static final String AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_YEAR = "creditCardExpirationYear";
    public static final String AUTOFILL_HINT_CREDIT_CARD_NUMBER = "creditCardNumber";
    public static final String AUTOFILL_HINT_CREDIT_CARD_SECURITY_CODE = "creditCardSecurityCode";
    public static final String AUTOFILL_HINT_EMAIL_ADDRESS = "emailAddress";
    public static final String AUTOFILL_HINT_NAME = "name";
    public static final String AUTOFILL_HINT_PASSWORD = "password";
    public static final String AUTOFILL_HINT_PHONE = "phone";
    public static final String AUTOFILL_HINT_POSTAL_ADDRESS = "postalAddress";
    public static final String AUTOFILL_HINT_POSTAL_CODE = "postalCode";
    public static final String AUTOFILL_HINT_USERNAME = "username";
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
    public static Property ROTATION;
    public static Property ROTATION_X;
    public static Property ROTATION_Y;
    public static Property SCALE_X;
    public static Property SCALE_Y;
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
    public static Property TRANSLATION_X;
    public static Property TRANSLATION_Y;
    public static Property TRANSLATION_Z;
    protected static final String VIEW_LOG_TAG = "View";
    public static final int VISIBLE = 0;
    protected static int[] WINDOW_FOCUSED_STATE_SET;
    public static Property X;
    public static Property Y;
    public static Property Z;

    /**
     * AOSP View.View(Context) (lines 5853-5855): stores the context and
     * seeds mResources from it. The shell keeps the same seed (androidx
     * EdgeToEdge flows assert the decor's resources non-null, so the
     * window's context must flow in through the decor's ctor).
     */
    public View(Context arg0) {
        mContext = arg0;
        mResources = arg0 != null ? arg0.getResources() : null;
    }

    /** AOSP View.View(Context, AttributeSet) (lines 5938-5939): the device
     * delegates to the 3-arg ctor with defStyleAttr 0; the shell flattens
     * the chain onto the 1-arg constructor's seed (mContext + mResources).
     */
    public View(Context arg0, AttributeSet arg1) {
        this(arg0);
    }

    /** AOSP View.View(Context, AttributeSet, int defStyleAttr)
     * (lines 5959-5960): the device delegates to the 4-arg ctor with a
     * zero-def style resource; the shell flattens onto the 1-arg seed. */
    public View(Context arg0, AttributeSet arg1, int arg2) {
        this(arg0);
    }

    /** AOSP View.View(Context, AttributeSet, int, int) (line 5996): the
     * device runs full attribute-flavored init; the shell (no attr sets
     * yet) flattens onto the 1-arg seed which carries only the context and
     * resources. */
    public View(Context arg0, AttributeSet arg1, int arg2,
            int arg3) {
        this(arg0);
    }

    public void addChildrenForAccessibility(ArrayList<View> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->addChildrenForAccessibility(Ljava/util/ArrayList;)V");
    }

    public void addExtraDataToAccessibilityNodeInfo(AccessibilityNodeInfo arg0, String arg1, Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->addExtraDataToAccessibilityNodeInfo(Landroid/view/accessibility/AccessibilityNodeInfo;Ljava/lang/String;Landroid/os/Bundle;)V");
    }

    public void addFocusables(ArrayList<View> arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->addFocusables(Ljava/util/ArrayList;I)V");
    }

    public void addFocusables(ArrayList<View> arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->addFocusables(Ljava/util/ArrayList;II)V");
    }

    public void addKeyboardNavigationClusters(Collection<View> arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->addKeyboardNavigationClusters(Ljava/util/Collection;I)V");
    }

    /** AOSP field mOnAttachStateChangeListeners (View.java line 5118 — the
     * device nests it in ListenerInfo); the shell keeps one list per view. */
    private CopyOnWriteArrayList<OnAttachStateChangeListener>
            mOnAttachStateChangeListeners;

    /** AOSP View.addOnAttachStateChangeListener (lines 7826-7833): device
     * stores into the ListenerInfo bag; same store on the shell (attach
     * dispatch trails with the window-attach chain). */
    public void addOnAttachStateChangeListener(View.OnAttachStateChangeListener arg0) {
        if (mOnAttachStateChangeListeners == null) {
            mOnAttachStateChangeListeners =
                    new CopyOnWriteArrayList<OnAttachStateChangeListener>();
        }
        mOnAttachStateChangeListeners.add(arg0);
    }

    public void addOnLayoutChangeListener(View.OnLayoutChangeListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->addOnLayoutChangeListener(Landroid/view/View$OnLayoutChangeListener;)V");
    }

    public void addOnUnhandledKeyEventListener(View.OnUnhandledKeyEventListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->addOnUnhandledKeyEventListener(Landroid/view/View$OnUnhandledKeyEventListener;)V");
    }

    public void addTouchables(ArrayList<View> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->addTouchables(Ljava/util/ArrayList;)V");
    }

    public ViewPropertyAnimator animate() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->animate()Landroid/view/ViewPropertyAnimator;");
    }

    public void announceForAccessibility(CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->announceForAccessibility(Ljava/lang/CharSequence;)V");
    }

    public void autofill(SparseArray<AutofillValue> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->autofill(Landroid/util/SparseArray;)V");
    }

    public void autofill(AutofillValue arg0) {
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
        // AOSP View.cancelPendingInputEvents: flush the window root's input
        // queue. The desktop shell delivers events synchronously on the
        // guest thread (HostInput -> ViewRootImpl), so there is never a
        // pending batch to cancel; the call is still reachable from
        // androidx teardown paths and must not throw.
    }

    public boolean checkInputConnectionProxy(View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->checkInputConnectionProxy(Landroid/view/View;)Z");
    }

    public void clearAnimation() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->clearAnimation()V");
    }

    public void clearFocus() {
        if (mFocused) {
            mFocused = false;
            if (mOnFocusChangeListener != null) {
                mOnFocusChangeListener.onFocusChange(this, false);
            }
            onFocusChanged(false, 0, null);
            invalidate();
        }
    }

    public void clearPendingCredentialRequest() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->clearPendingCredentialRequest()V");
    }

    public void clearViewTranslationCallback() {
        // AOSP frameworks-base core/java/android/view/View.java:33969-33972
        // clearViewTranslationCallback: mViewTranslationCallback = null.
        // The shell stores no callback, so clearing is a no-op.
    }

    public static int combineMeasuredStates(int arg0, int arg1) {
        return arg0 | arg1;
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

    public WindowInsets computeSystemWindowInsets(WindowInsets arg0, Rect arg1) {
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

    public AccessibilityNodeInfo createAccessibilityNodeInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->createAccessibilityNodeInfo()Landroid/view/accessibility/AccessibilityNodeInfo;");
    }

    public void createContextMenu(ContextMenu arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->createContextMenu(Landroid/view/ContextMenu;)V");
    }

    public void destroyDrawingCache() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->destroyDrawingCache()V");
    }

    public WindowInsets dispatchApplyWindowInsets(WindowInsets arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchApplyWindowInsets(Landroid/view/WindowInsets;)Landroid/view/WindowInsets;");
    }

    public boolean dispatchCapturedPointerEvent(MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchCapturedPointerEvent(Landroid/view/MotionEvent;)Z");
    }

    public void dispatchConfigurationChanged(Configuration arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchConfigurationChanged(Landroid/content/res/Configuration;)V");
    }

    public void dispatchCreateViewTranslationRequest(Map<AutofillId, long[]> arg0, int[] arg1, TranslationCapability arg2, List<ViewTranslationRequest> arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchCreateViewTranslationRequest(Ljava/util/Map;[ILandroid/view/translation/TranslationCapability;Ljava/util/List;)V");
    }

    public void dispatchDisplayHint(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchDisplayHint(I)V");
    }

    public boolean dispatchDragEvent(DragEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchDragEvent(Landroid/view/DragEvent;)Z");
    }

    protected void dispatchDraw(Canvas arg0) {
        // AOSP View.java: default View dispatchDraw is empty (ViewGroup
        // overrides to walk children — its port lands in ViewGroup).
    }

    public void dispatchDrawableHotspotChanged(float arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchDrawableHotspotChanged(FF)V");
    }

    public void dispatchFinishTemporaryDetach() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchFinishTemporaryDetach()V");
    }

    protected boolean dispatchGenericFocusedEvent(MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchGenericFocusedEvent(Landroid/view/MotionEvent;)Z");
    }

    public boolean dispatchGenericMotionEvent(MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchGenericMotionEvent(Landroid/view/MotionEvent;)Z");
    }

    protected boolean dispatchGenericPointerEvent(MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchGenericPointerEvent(Landroid/view/MotionEvent;)Z");
    }

    protected boolean dispatchHoverEvent(MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchHoverEvent(Landroid/view/MotionEvent;)Z");
    }

    public boolean dispatchKeyEvent(KeyEvent arg0) {
        if (mOnKeyListener != null && mEnabled
                && mOnKeyListener.onKey(this, arg0.getKeyCode(), arg0)) {
            return true;
        }
        return arg0.dispatch(this);
    }

    public boolean dispatchKeyEventPreIme(KeyEvent arg0) {
        return arg0.dispatch(this);
    }

    public boolean dispatchKeyShortcutEvent(KeyEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchKeyShortcutEvent(Landroid/view/KeyEvent;)Z");
    }

    public boolean dispatchNestedFling(float arg0, float arg1, boolean arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchNestedFling(FFZ)Z");
    }

    public boolean dispatchNestedPreFling(float arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchNestedPreFling(FF)Z");
    }

    public boolean dispatchNestedPrePerformAccessibilityAction(int arg0, Bundle arg1) {
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

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchPopulateAccessibilityEvent(Landroid/view/accessibility/AccessibilityEvent;)Z");
    }

    public void dispatchProvideAutofillStructure(ViewStructure arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchProvideAutofillStructure(Landroid/view/ViewStructure;I)V");
    }

    public void dispatchProvideStructure(ViewStructure arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchProvideStructure(Landroid/view/ViewStructure;)V");
    }

    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchRestoreInstanceState(Landroid/util/SparseArray;)V");
    }

    protected void dispatchSaveInstanceState(SparseArray<Parcelable> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchSaveInstanceState(Landroid/util/SparseArray;)V");
    }

    public void dispatchScrollCaptureSearch(Rect arg0, Point arg1, Consumer<ScrollCaptureTarget> arg2) {
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

    public boolean dispatchTouchEvent(MotionEvent arg0) {
        if (mOnTouchListener != null && mEnabled
                && mOnTouchListener.onTouch(this, arg0)) {
            return true;
        }
        return onTouchEvent(arg0);
    }

    public boolean dispatchTrackballEvent(MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchTrackballEvent(Landroid/view/MotionEvent;)Z");
    }

    public boolean dispatchUnhandledMove(View arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchUnhandledMove(Landroid/view/View;I)Z");
    }

    protected void dispatchVisibilityChanged(View arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchVisibilityChanged(Landroid/view/View;I)V");
    }

    public void dispatchWindowFocusChanged(boolean arg0) {
        onWindowFocusChanged(arg0);
    }

    public void dispatchWindowInsetsAnimationEnd(WindowInsetsAnimation arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchWindowInsetsAnimationEnd(Landroid/view/WindowInsetsAnimation;)V");
    }

    public void dispatchWindowInsetsAnimationPrepare(WindowInsetsAnimation arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchWindowInsetsAnimationPrepare(Landroid/view/WindowInsetsAnimation;)V");
    }

    public WindowInsets dispatchWindowInsetsAnimationProgress(WindowInsets arg0, List<WindowInsetsAnimation> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchWindowInsetsAnimationProgress(Landroid/view/WindowInsets;Ljava/util/List;)Landroid/view/WindowInsets;");
    }

    public WindowInsetsAnimation.Bounds dispatchWindowInsetsAnimationStart(WindowInsetsAnimation arg0, WindowInsetsAnimation.Bounds arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchWindowInsetsAnimationStart(Landroid/view/WindowInsetsAnimation;Landroid/view/WindowInsetsAnimation$Bounds;)Landroid/view/WindowInsetsAnimation$Bounds;");
    }

    public void dispatchWindowSystemUiVisiblityChanged(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->dispatchWindowSystemUiVisiblityChanged(I)V");
    }

    public void dispatchWindowVisibilityChanged(int arg0) {
        onWindowVisibilityChanged(arg0);
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
    public void draw(Canvas arg0) {
        drawBackground(arg0);
        // Overridable hooks dispatch through ordinary JVM virtual calls: a
        // translated guest subclass that overrides onDraw/dispatchDraw/
        // onDrawForeground runs its own bytecode; otherwise the framework
        // default (AOSP's empty View default) runs.
        onDraw(arg0);
        dispatchDraw(arg0);
        // drawAutofilledHighlight: no autofill model yet (AOSP needs
        // PFLAG3_AUTOFILLED_HIGHLIGHTED state); no-op.
        onDrawForeground(arg0);
        // drawDefaultFocusHighlight: no focus-highlight state yet
        // (AOSP draws only under PFLAG3_DEFAULT_FOCUS_HIGHLIGHTED); no-op.
    }

    /** AOSP View.java:25267-25275: no background, nothing to draw here. */
    private void drawBackground(Canvas canvas) {
        Drawable background = mBackground;
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
    boolean draw(Canvas canvas, ViewGroup parent, long drawingTime) {
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
            // AOSP:24966-24969 — skip-draw views dispatch their children.
            dispatchDraw(canvas);
        } else {
            // AOSP:24971-24973 — normal views run the full draw(Canvas).
            draw(canvas);
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

    public View findFocus() {
        return mFocused ? this : null;
    }

    public final OnBackInvokedDispatcher findOnBackInvokedDispatcher() {
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
    public final <T extends View> T findViewById(int arg0) {
        if (arg0 == NO_ID) {
            return null;
        }
        return (T) (arg0 == mID ? this : null);
    }

    public final <T> T findViewWithTag(Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->findViewWithTag(Ljava/lang/Object;)Landroid/view/View;");
    }

    public void findViewsWithText(ArrayList<View> arg0, CharSequence arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->findViewsWithText(Ljava/util/ArrayList;Ljava/lang/CharSequence;I)V");
    }

    protected boolean fitSystemWindows(Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->fitSystemWindows(Landroid/graphics/Rect;)Z");
    }

    public View focusSearch(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->focusSearch(I)Landroid/view/View;");
    }

    public void forceHasOverlappingRendering(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->forceHasOverlappingRendering(Z)V");
    }

    public void forceLayout() {
        mLayoutRequested = true;
    }

    public boolean gatherTransparentRegion(Region arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->gatherTransparentRegion(Landroid/graphics/Region;)Z");
    }

    public void generateDisplayHash(String arg0, Rect arg1, Executor arg2, DisplayHashResultCallback arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->generateDisplayHash(Ljava/lang/String;Landroid/graphics/Rect;Ljava/util/concurrent/Executor;Landroid/view/displayhash/DisplayHashResultCallback;)V");
    }

    /**
     * Ported from aosp-mirror/platform_frameworks_base
     * core/java/android/view/View.java (generateViewId): CAS loop over
     * sNextGeneratedId, clamped below the aapt-generated (high-byte) ID range.
     */
    private static final AtomicInteger
            sNextGeneratedId = new AtomicInteger(1);

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

    public CharSequence getAccessibilityClassName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAccessibilityClassName()Ljava/lang/CharSequence;");
    }

    public View.AccessibilityDelegate getAccessibilityDelegate() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAccessibilityDelegate()Landroid/view/View$AccessibilityDelegate;");
    }

    public int getAccessibilityLiveRegion() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAccessibilityLiveRegion()I");
    }

    public AccessibilityNodeProvider getAccessibilityNodeProvider() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAccessibilityNodeProvider()Landroid/view/accessibility/AccessibilityNodeProvider;");
    }

    public CharSequence getAccessibilityPaneTitle() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAccessibilityPaneTitle()Ljava/lang/CharSequence;");
    }

    public int getAccessibilityTraversalAfter() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAccessibilityTraversalAfter()I");
    }

    public int getAccessibilityTraversalBefore() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAccessibilityTraversalBefore()I");
    }

    public String getAllowedHandwritingDelegatePackageName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAllowedHandwritingDelegatePackageName()Ljava/lang/String;");
    }

    public String getAllowedHandwritingDelegatorPackageName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAllowedHandwritingDelegatorPackageName()Ljava/lang/String;");
    }

    public float getAlpha() {
        // AOSP View.getAlpha: the transformation-info alpha, identity when
        // absent. The shell keeps the single store on the render node.
        return mRenderNode.getAlpha();
    }

    public Animation getAnimation() {
        // AOSP View.java: getAnimation returns the legacy animation state;
        // no animation model yet, so no animation is reported (null leg of
        // AOSP draw(Canvas,ViewGroup,long)).
        return null;
    }

    public Matrix getAnimationMatrix() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAnimationMatrix()Landroid/graphics/Matrix;");
    }

    public IBinder getApplicationWindowToken() {
        // AOSP View.getApplicationWindowToken: the window token's
        // application token; the shell has no WindowManager binder, so the
        // window token itself is the answer.
        return getWindowToken();
    }

    public int[] getAttributeResolutionStack(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAttributeResolutionStack(I)[I");
    }

    public Map<Integer, Integer> getAttributeSourceResourceMap() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAttributeSourceResourceMap()Ljava/util/Map;");
    }

    public String[] getAutofillHints() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAutofillHints()[Ljava/lang/String;");
    }

    public final AutofillId getAutofillId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAutofillId()Landroid/view/autofill/AutofillId;");
    }

    public int getAutofillType() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAutofillType()I");
    }

    public AutofillValue getAutofillValue() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getAutofillValue()Landroid/view/autofill/AutofillValue;");
    }

    public Drawable getBackground() {
        // AOSP View.java: mBackground (null without a background set/setter —
        // setBackground is a wall when anything calls it).
        return mBackground;
    }

    public BlendMode getBackgroundTintBlendMode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getBackgroundTintBlendMode()Landroid/graphics/BlendMode;");
    }

    public ColorStateList getBackgroundTintList() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getBackgroundTintList()Landroid/content/res/ColorStateList;");
    }

    public PorterDuff.Mode getBackgroundTintMode() {
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
    protected Rect mClipBounds;

    /*
     * Draw-chain state (AOSP View.java fields, staging reductions noted at
     * each use site). mBackground backs getBackground (AOSP View.java mBackground,
     * default null); mPrivateFlags carries only the bits the draw port reads;
     * scroll offsets default to theunscrolled view.
     */
    protected Drawable mBackground;

    // AOSP View.java:2752: PFLAG_SKIP_DRAW = 0x80 (@hide constant, cited).
    private static final int PFLAG_SKIP_DRAW = 0x00000080;

    protected int mPrivateFlags;

    /**
     * AOSP View.mLayerType: one of LAYER_TYPE_NONE/SOFTWARE/HARDWARE. The
     * shell's raster pipeline has no layer compositing, so the value only
     * round-trips through setLayerType/getLayerType (Compose's ViewLayer
     * switches it per frame and reads it back for its draw strategy).
     */
    private int mLayerType = LAYER_TYPE_NONE;

    protected int mMeasuredWidth;
    protected int mMeasuredHeight;
    private static final int PFLAG_MEASURED_DIMENSION_SET = 0x80000000;

    /** AOSP View.mOldWidthMeasureSpec / mOldHeightMeasureSpec: the specs the
     *  last measure pass used, so an unchanged request can skip onMeasure. */
    private int mOldWidthMeasureSpec = Integer.MIN_VALUE;
    private int mOldHeightMeasureSpec = Integer.MIN_VALUE;

    /** AOSP PFLAG_FORCE_LAYOUT surrogate, read by isLayoutRequested(). */
    private boolean mLayoutRequested = true;

    /** AOSP PFLAG_LAYOUT_REQUIRED surrogate: a measure pass ran, so the
     *  next layout call must run onLayout even when bounds are unchanged. */
    private boolean mLayoutRequired = true;

    /** AOSP View.mPaddingLeft/Top/Right/Bottom (absolute storage; relative
     *  setters write the same fields because the shell keeps one LTR
     *  direction). */
    protected int mPaddingLeft;
    protected int mPaddingTop;
    protected int mPaddingRight;
    protected int mPaddingBottom;

    /** AOSP View.mMinWidth / mMinHeight (setMinimumWidth/Height). */
    private int mMinWidth;
    private int mMinHeight;

    protected int mScrollX;
    protected int mScrollY;

    /** Ported from AOSP {@code View#getClipBounds()}: copy or null. */
    public Rect getClipBounds() {
        return (mClipBounds != null) ? new Rect(mClipBounds) : null;
    }

    /** Ported from AOSP {@code View#getClipBounds(Rect)}. */
    public boolean getClipBounds(Rect arg0) {
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

    public final ContentCaptureSession getContentCaptureSession() {
        // AOSP frameworks-base core/java/android/view/View.java:10988-10990
        // getContentCaptureSession: cached session or the context's default
        // manager session; the disabled walk-through returns null when
        // content capture is off for this view. The desktop raises no
        // content-capture service (matches the interpreter's null builtin).
        return null;
    }

    public CharSequence getContentDescription() {
        return mContentDescription;
    }

    public final int getContentSensitivity() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getContentSensitivity()I");
    }

    /** AOSP View.getContext (View.java:17297-17299). */
    public final Context getContext() {
        return mContext;
    }

    protected ContextMenu.ContextMenuInfo getContextMenuInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getContextMenuInfo()Landroid/view/ContextMenu$ContextMenuInfo;");
    }

    public final boolean getDefaultFocusHighlightEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getDefaultFocusHighlightEnabled()Z");
    }

    public static int getDefaultSize(int arg0, int arg1) {
        int result = arg0;
        int specMode = MeasureSpec.getMode(arg1);
        int specSize = MeasureSpec.getSize(arg1);
        if (specMode == MeasureSpec.MODE_AT_MOST
                || specMode == MeasureSpec.MODE_EXACTLY) {
            result = specSize;
        }
        return result;
    }

    public Display getDisplay() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getDisplay()Landroid/view/Display;");
    }

    public final int[] getDrawableState() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getDrawableState()[I");
    }

    public Bitmap getDrawingCache() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getDrawingCache()Landroid/graphics/Bitmap;");
    }

    public Bitmap getDrawingCache(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getDrawingCache(Z)Landroid/graphics/Bitmap;");
    }

    public int getDrawingCacheBackgroundColor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getDrawingCacheBackgroundColor()I");
    }

    public int getDrawingCacheQuality() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getDrawingCacheQuality()I");
    }

    public void getDrawingRect(Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getDrawingRect(Landroid/graphics/Rect;)V");
    }

    public long getDrawingTime() {
        // AOSP View.java:23414: mAttachInfo != null ? mAttachInfo.mDrawingTime
        // : 0. ViewRootImpl stamps mDrawingTime before each draw.
        return mAttachInfo != null ? mAttachInfo.mDrawingTime : 0;
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

    public ArrayList<View> getFocusables(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getFocusables(I)Ljava/util/ArrayList;");
    }

    public void getFocusedRect(Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getFocusedRect(Landroid/graphics/Rect;)V");
    }

    public Drawable getForeground() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getForeground()Landroid/graphics/drawable/Drawable;");
    }

    public int getForegroundGravity() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getForegroundGravity()I");
    }

    public BlendMode getForegroundTintBlendMode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getForegroundTintBlendMode()Landroid/graphics/BlendMode;");
    }

    public ColorStateList getForegroundTintList() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getForegroundTintList()Landroid/content/res/ColorStateList;");
    }

    public PorterDuff.Mode getForegroundTintMode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getForegroundTintMode()Landroid/graphics/PorterDuff$Mode;");
    }

    public float getFrameContentVelocity() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getFrameContentVelocity()F");
    }

    public final boolean getGlobalVisibleRect(Rect arg0) {
        return getGlobalVisibleRect(arg0, null);
    }

    public boolean getGlobalVisibleRect(Rect arg0, Point arg1) {
        if (!isShown()) {
            return false;
        }
        int[] location = new int[2];
        getLocationOnScreen(location);
        arg0.set(location[0], location[1],
                location[0] + mRight - mLeft, location[1] + mBottom - mTop);
        if (arg1 != null) {
            arg1.x = location[0];
            arg1.y = location[1];
        }
        return true;
    }

    /**
     * AOSP View.getHandler (View.java:21262-21268): the attach info's handler
     * when attached, null otherwise. The shell's AttachInfo is still the
     * {@link #mAttachInfo} marker object (no AttachInfo members yet), so the
     * main-looper handler is kept on the view as the stand-in for
     * {@code attachInfo.mHandler}.
     */
    public Handler getHandler() {
        return mAttachInfo != null ? mAttachInfo.mHandler : null;
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

    public Runnable getHandwritingDelegatorCallback() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getHandwritingDelegatorCallback()Ljava/lang/Runnable;");
    }

    public final boolean getHasOverlappingRendering() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getHasOverlappingRendering()Z");
    }

    public void getHitRect(Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getHitRect(Landroid/graphics/Rect;)V");
    }

    public int getHorizontalFadingEdgeLength() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getHorizontalFadingEdgeLength()I");
    }

    protected int getHorizontalScrollbarHeight() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getHorizontalScrollbarHeight()I");
    }

    public Drawable getHorizontalScrollbarThumbDrawable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getHorizontalScrollbarThumbDrawable()Landroid/graphics/drawable/Drawable;");
    }

    public Drawable getHorizontalScrollbarTrackDrawable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getHorizontalScrollbarTrackDrawable()Landroid/graphics/drawable/Drawable;");
    }

    /** AOSP View.getId (lines 27987-27989): the raw mID field. */
    public int getId() {
        return mID;
    }

    /** AOSP View field mID, defaulting to NO_ID. */
    private int mID = NO_ID;

    public int getImportantForAccessibility() {
        // AOSP frameworks-base core/java/android/view/View.java#getImportantForAccessibility:
        // returns mImportantForAccessibility (default AUTO = 0); no view
        // hierarchy/accessibility services headless → 0
        // (matches the interpreter's headless builtin).
        return 0;
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

    public KeyEvent.DispatcherState getKeyDispatcherState() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getKeyDispatcherState()Landroid/view/KeyEvent$DispatcherState;");
    }

    public int getLabelFor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getLabelFor()I");
    }

    public int getLayerType() {
        // AOSP View.getLayerType: the staged mLayerType.
        return mLayerType;
    }

    /**
     * Mirror state for layout params: the SDK stub jar declares no
     * instance fields, so interpreter framework-dispatch synchronizes the
     * {@code View;.mLayoutParams} mapping key onto this field.
     */
    protected ViewGroup.LayoutParams mLayoutParams;

    /**
     * Mirror state for the view tree: framework-dispatch syncs the model
     * keys ({@code View;.mParent}, {@code View;.mAttached}) onto these
     * fields so ported ViewGroup bodies (addViewInLayout attaches,
     * removeAllViews detaches) mutate the same state the legacy builtin
     * readers use, and the interpreter unbridges mirrors back to their
     * model objects after every dispatch.
     */
    protected ViewParent mParent;

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

    public ViewGroup.LayoutParams getLayoutParams() {
        // Real Android throws when no params were attached via inflation
        // or addView; our composition measures bare views directly, so the
        // read defaults to a WRAP_CONTENT pair. setLayoutParams preserves
        // caller identity, so attach-time params round-trip exactly.
        if (mLayoutParams == null) {
            mLayoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
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

    public final boolean getLocalVisibleRect(Rect arg0) {
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
        getLocationOnScreen(arg0);
    }

    public void getLocationOnScreen(int[] arg0) {
        int x = mLeft;
        int y = mTop;
        ViewParent parent = mParent;
        while (parent instanceof View view) {
            x += view.mLeft - view.mScrollX;
            y += view.mTop - view.mScrollY;
            parent = view.mParent;
        }
        arg0[0] = x;
        arg0[1] = y;
    }

    public Matrix getMatrix() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getMatrix()Landroid/graphics/Matrix;");
    }

    public final int getMeasuredHeight() {
        return mMeasuredHeight;
    }

    public final int getMeasuredHeightAndState() {
        return mMeasuredHeight;
    }

    public final int getMeasuredState() {
        return 0;
    }

    public final int getMeasuredWidth() {
        return mMeasuredWidth;
    }

    public final int getMeasuredWidthAndState() {
        return mMeasuredWidth;
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

    public View.OnFocusChangeListener getOnFocusChangeListener() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getOnFocusChangeListener()Landroid/view/View$OnFocusChangeListener;");
    }

    public int getOutlineAmbientShadowColor() {
        return mRenderNode.getAmbientShadowColor();
    }

    public int getOutlineSpotShadowColor() {
        return mRenderNode.getSpotShadowColor();
    }

    public int getOverScrollMode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getOverScrollMode()I");
    }

    public ViewOverlay getOverlay() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getOverlay()Landroid/view/ViewOverlay;");
    }

    public int getPaddingBottom() {
        return mPaddingBottom;
    }

    public int getPaddingEnd() {
        return mPaddingRight;
    }

    public int getPaddingLeft() {
        return mPaddingLeft;
    }

    public int getPaddingRight() {
        return mPaddingRight;
    }

    public int getPaddingStart() {
        return mPaddingLeft;
    }

    public int getPaddingTop() {
        return mPaddingTop;
    }

    /** AOSP View.getParent (View.java:18873-18875) — final in AOSP; guests
     * cannot override it. */
    public final ViewParent getParent() {
        return mParent;
    }

    public ViewParent getParentForAccessibility() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getParentForAccessibility()Landroid/view/ViewParent;");
    }

    public final OutcomeReceiver<GetCredentialResponse, GetCredentialException> getPendingCredentialCallback() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getPendingCredentialCallback()Landroid/os/OutcomeReceiver;");
    }

    public final GetCredentialRequest getPendingCredentialRequest() {
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
    protected RenderNode mRenderNode =
            new RenderNode(getClass().getName());

    /**
     * Ported from aosp-mirror/platform_frameworks_base
     * core/java/android/view/View.java ({@code mOutlineProvider}, default
     * {@code ViewOutlineProvider.BACKGROUND}). Non-final for field sync
     * (key {@code View;.mOutlineProvider}).
     */
    protected ViewOutlineProvider mOutlineProvider =
            ViewOutlineProvider.BACKGROUND;

    /**
     * Ported from AOSP {@code View#setOutlineProvider}: identity-guarded
     * assignment; the invalidateOutline leg is unported invalidation
     * machinery (no draw pipeline yet).
     */
    public void setOutlineProvider(ViewOutlineProvider arg0) {
        if (mOutlineProvider != arg0) {
            mOutlineProvider = arg0;
        }
    }

    /** Ported from AOSP {@code View#getOutlineProvider}. */
    public ViewOutlineProvider getOutlineProvider() {
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

    public PointerIcon getPointerIcon() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getPointerIcon()Landroid/view/PointerIcon;");
    }

    public final List<Rect> getPreferKeepClearRects() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getPreferKeepClearRects()Ljava/util/List;");
    }

    public String[] getReceiveContentMimeTypes() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getReceiveContentMimeTypes()[Ljava/lang/String;");
    }

    public float getRequestedFrameRate() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getRequestedFrameRate()F");
    }

    /** AOSP View.getResources() (lines 25854-25856 return the field seeded
     * from the context — shell ctor seeds the same, lines 5853-5855). */
    private Context mContext;

    private Resources mResources;

    public Resources getResources() {
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

    public AttachedSurfaceControl getRootSurfaceControl() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getRootSurfaceControl()Landroid/view/AttachedSurfaceControl;");
    }

    public View getRootView() {
        View root = this;
        while (root.mParent instanceof View parent) {
            root = parent;
        }
        return root;
    }

    public WindowInsets getRootWindowInsets() {
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

    public final CharSequence getStateDescription() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getStateDescription()Ljava/lang/CharSequence;");
    }

    public StateListAnimator getStateListAnimator() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getStateListAnimator()Landroid/animation/StateListAnimator;");
    }

    protected int getSuggestedMinimumHeight() {
        return mMinHeight;
    }

    protected int getSuggestedMinimumWidth() {
        return mMinWidth;
    }

    public CharSequence getSupplementalDescription() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getSupplementalDescription()Ljava/lang/CharSequence;");
    }

    public List<Rect> getSystemGestureExclusionRects() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getSystemGestureExclusionRects()Ljava/util/List;");
    }

    /** AOSP field mSystemUiVisibility; setter/getter below. */
    private int mSystemUiVisibility;

    /** AOSP View.mViewFlags visibility bits: VISIBLE/INVISIBLE/GONE. */
    private int mVisibility = VISIBLE;

    /**
     * AOSP View.getSystemUiVisibility (lines 29011-29013): plain field
     * return of the flags stored by setSystemUiVisibility — the shell
     * stores the same value with no visibility dispatch (nothing presents
     * it at the display layer yet).
     */
    public int getSystemUiVisibility() {
        return mSystemUiVisibility;
    }

    public Object getTag() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getTag()Ljava/lang/Object;");
    }

    /**
     * AOSP View.getTag(int) (lines 28042-28045) — null when no keyed tag
     * was stored. The device holds mKeyedTags in a SparseArray<Object>;
     * the shell mirrors the store through a host HashMap (framework-internal
     * storage only — guests cross it as opaque java.lang.Object).
     */
    public Object getTag(int arg0) {
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

    public CharSequence getTooltipText() {
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

    public TouchDelegate getTouchDelegate() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getTouchDelegate()Landroid/view/TouchDelegate;");
    }

    public ArrayList<View> getTouchables() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getTouchables()Ljava/util/ArrayList;");
    }

    public float getTransitionAlpha() {
        // AOSP View.java: transitions alpha (transition/overlay path, default 1).
        return 1;
    }

    public String getTransitionName() {
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

    public Drawable getVerticalScrollbarThumbDrawable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getVerticalScrollbarThumbDrawable()Landroid/graphics/drawable/Drawable;");
    }

    public Drawable getVerticalScrollbarTrackDrawable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getVerticalScrollbarTrackDrawable()Landroid/graphics/drawable/Drawable;");
    }

    public int getVerticalScrollbarWidth() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getVerticalScrollbarWidth()I");
    }

    public ViewTranslationResponse getViewTranslationResponse() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getViewTranslationResponse()Landroid/view/translation/ViewTranslationResponse;");
    }

    /** AOSP View.getViewTreeObserver (View.java:27442-27450): the attach
     * info's observer when attached, else a lazily-created floating one. */
    private ViewTreeObserver mFloatingTreeObserver;

    public ViewTreeObserver getViewTreeObserver() {
        if (mAttachInfo != null) {
            return mAttachInfo.mTreeObserver;
        }
        if (mFloatingTreeObserver == null) {
            mFloatingTreeObserver = new ViewTreeObserver(mContext);
        }
        return mFloatingTreeObserver;
    }

    public int getVisibility() {
        // AOSP View.getVisibility (mViewFlags & VISIBILITY_MASK).
        return mVisibility;
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

    /** AOSP View.getWindowAttachCount: attach handshake bookkeeping. */
    protected int getWindowAttachCount() {
        return mWindowAttachCount;
    }

    public WindowId getWindowId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getWindowId()Landroid/view/WindowId;");
    }

    public WindowInsetsController getWindowInsetsController() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getWindowInsetsController()Landroid/view/WindowInsetsController;");
    }

    public int getWindowSystemUiVisibility() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getWindowSystemUiVisibility()I");
    }

    public IBinder getWindowToken() {
        return mAttachInfo != null ? mAttachInfo.mWindowToken : null;
    }

    public int getWindowVisibility() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->getWindowVisibility()I");
    }

    public void getWindowVisibleDisplayFrame(Rect arg0) {
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
        return mOnClickListener != null;
    }

    public boolean hasOnLongClickListeners() {
        return mOnLongClickListener != null;
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
        return mAttachInfo != null && mAttachInfo.mHasWindowFocus;
    }

    public static View inflate(Context arg0, int arg1, ViewGroup arg2) {
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

    public void invalidate(Rect arg0) {
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
        mDirty = true;
        if (mParent != null) {
            mParent.onDescendantInvalidated(this, this);
        }
    }

    public void invalidateDrawable(Drawable arg0) {
        invalidate();
    }

    public void invalidateOutline() {
        // AOSP View.invalidateOutline: rebuild the outline from the provider,
        // then invalidate. The shell's RenderNode carries no outline yet, so
        // the rebuild is a no-op and the invalidation is the whole effect;
        // androidx Compose calls this from OutlineResolver.updateOutlineResolver
        // after replacing the provider.
        invalidate();
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

    /**
     * AOSP View.java:14146-14148 — attach state is exactly the ViewRootImpl's
     * {@link AttachInfo} reference (null until the window attaches this view
     * tree). Package-private so only framework classes flip it.
     */
    AttachInfo mAttachInfo;

    /**
     * AOSP View.AttachInfo: window-scoped state shared by every view in one
     * attached tree. The shell carries the members the desktop pipeline
     * reads — handler, tree observer, root view, window token, focus and
     * visibility — and omits the display-list/threaded-renderer members.
     */
    static final class AttachInfo {

        final ViewRootImpl mViewRootImpl;
        final ViewTreeObserver mTreeObserver;
        final Handler mHandler;

        View mRootView;
        IBinder mWindowToken;
        boolean mHasWindowFocus;
        int mWindowVisibility = VISIBLE;
        long mDrawingTime;

        AttachInfo(ViewRootImpl viewRootImpl, ViewTreeObserver treeObserver) {
            mViewRootImpl = viewRootImpl;
            mTreeObserver = treeObserver;
            mHandler = new Handler(Looper.getMainLooper());
        }
    }

    /** AOSP View.mWindowAttachCount (attach/detach bookkeeping). */
    private int mWindowAttachCount;

    /** AOSP View.mFocused flag (single-focused-view model). */
    private boolean mFocused;

    /** AOSP View.mViewFlags reduced to the input/click state the shell
     *  tracks (visibility lives in mVisibility below). */
    private boolean mEnabled = true;
    private boolean mClickable;
    private boolean mLongClickable;
    private boolean mPressed;
    private boolean mSelected;
    private int mFocusable;

    private OnClickListener mOnClickListener;
    private OnLongClickListener mOnLongClickListener;
    private OnTouchListener mOnTouchListener;
    private OnKeyListener mOnKeyListener;
    private OnFocusChangeListener mOnFocusChangeListener;

    /**
     * AOSP View.dispatchAttachedToWindow: record the attach info, notify
     * listeners, then run onAttachedToWindow. ViewGroup overrides to walk
     * its children first.
     */
    void dispatchAttachedToWindow(AttachInfo info, int visibility) {
        mAttachInfo = info;
        mWindowAttachCount++;
        if (mOnAttachStateChangeListeners != null) {
            for (OnAttachStateChangeListener listener
                    : mOnAttachStateChangeListeners) {
                listener.onViewAttachedToWindow(this);
            }
        }
        onAttachedToWindow();
        if (visibility != VISIBLE) {
            dispatchWindowVisibilityChanged(visibility);
        }
    }

    /**
     * AOSP View.dispatchDetachedFromWindow: run onDetachedFromWindow, notify
     * listeners, then drop the attach info.
     */
    void dispatchDetachedFromWindow() {
        onDetachedFromWindow();
        if (mOnAttachStateChangeListeners != null) {
            for (OnAttachStateChangeListener listener
                    : mOnAttachStateChangeListeners) {
                listener.onViewDetachedFromWindow(this);
            }
        }
        mAttachInfo = null;
        mWindowAttachCount--;
    }

    public boolean isAttachedToWindow() {
        return mAttachInfo != null;
    }

    public boolean isAutoHandwritingEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isAutoHandwritingEnabled()Z");
    }

    public boolean isClickable() {
        return mClickable;
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

    /**
     * Host-driver seam (no SDK counterpart): reads and clears the dirty
     * surrogate, the way ViewRootImpl clears PFLAG_DIRTY at the start of a
     * traversal. A render loop calls this once per frame.
     */
    public boolean tartineConsumeDirty() {
        boolean dirty = mDirty;
        mDirty = false;
        return dirty;
    }

    public boolean isDrawingCacheEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isDrawingCacheEnabled()Z");
    }

    public boolean isDuplicateParentStateEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isDuplicateParentStateEnabled()Z");
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public final boolean isFocusable() {
        return mFocusable == FOCUSABLE;
    }

    public final boolean isFocusableInTouchMode() {
        return mFocusable == FOCUSABLE;
    }

    public boolean isFocused() {
        return mFocused;
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
        return false;
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
        // AOSP frameworks-base core/java/android/view/View.java#isInTouchMode:
        // true while a touch is down; no input exists headless → false
        // (matches the interpreter's headless builtin).
        return false;
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
        return mLayoutRequested;
    }

    public boolean isLongClickable() {
        return mLongClickable;
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
        return mPressed;
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
        return mSelected;
    }

    public final boolean isShowingLayoutBounds() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->isShowingLayoutBounds()Z");
    }

    public boolean isShown() {
        View current = this;
        while (current != null) {
            if (current.getVisibility() != VISIBLE) {
                return false;
            }
            ViewParent parent = current.mParent;
            current = parent instanceof View view ? view : null;
        }
        return true;
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

    public View keyboardNavigationClusterSearch(View arg0, int arg1) {
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

        if (changed || mLayoutRequired) {
            onLayout(changed, arg0, arg1, arg2, arg3);
            mLayoutRequired = false;
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

    /**
     * Ported from AOSP View.measure with two shell deviations: the
     * PFLAG_MEASURED_DIMENSION_SET contract is enforced leniently (an
     * override that forgets setMeasuredDimension falls back to the default
     * size instead of throwing), and the optical-bounds/skip-measure arms
     * are absent because their models are not carried.
     */
    public final void measure(int arg0, int arg1) {
        boolean forceLayout = mLayoutRequested
                || arg0 != mOldWidthMeasureSpec
                || arg1 != mOldHeightMeasureSpec;
        if (forceLayout) {
            mPrivateFlags &= ~PFLAG_MEASURED_DIMENSION_SET;
            onMeasure(arg0, arg1);
            if ((mPrivateFlags & PFLAG_MEASURED_DIMENSION_SET)
                    != PFLAG_MEASURED_DIMENSION_SET) {
                setMeasuredDimension(
                        getDefaultSize(getSuggestedMinimumWidth(), arg0),
                        getDefaultSize(getSuggestedMinimumHeight(), arg1));
            }
            mOldWidthMeasureSpec = arg0;
            mOldHeightMeasureSpec = arg1;
            mLayoutRequired = true;
        }
        mLayoutRequested = false;
    }

    protected static int[] mergeDrawableStates(int[] arg0, int[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->mergeDrawableStates([I[I)[I");
    }

    /**
     * Ported from aosp-mirror/platform_frameworks_base
     * core/java/android/view/View.java (offsetLeftAndRight): shift the
     * position both on the node and in the view's own bounds, then dirty the
     * view. Deviations: the AOSP attach-info visibility bookkeeping and the
     * background/foreground bounds sync (no Drawable bounds port) collapse
     * into invalidate() on an attached view.
     */
    public void offsetLeftAndRight(int arg0) {
        if (arg0 == 0) {
            return;
        }
        mLeft += arg0;
        mRight += arg0;
        mRenderNode.offsetLeftAndRight(arg0);
        invalidate();
    }

    /** Ported from the same file (offsetTopAndBottom); see the left twin. */
    public void offsetTopAndBottom(int arg0) {
        if (arg0 == 0) {
            return;
        }
        mTop += arg0;
        mBottom += arg0;
        mRenderNode.offsetTopAndBottom(arg0);
        invalidate();
    }

    protected void onAnimationEnd() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onAnimationEnd()V");
    }

    protected void onAnimationStart() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onAnimationStart()V");
    }

    public WindowInsets onApplyWindowInsets(WindowInsets arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onApplyWindowInsets(Landroid/view/WindowInsets;)Landroid/view/WindowInsets;");
    }

    protected void onAttachedToWindow() {
        // AOSP frameworks-base core/java/android/view/View.java:22503+
        // onAttachedToWindow: transparent-region request, drawable jump,
        // AccessibilityNodeIdManager registration, outline rebuild — none
        // have shell counterparts (no accessibility registry, the shell
        // tracks attachment through mAttachInfo alone, set by the driver
        // at setContentView parity). Matches the interpreter's mAttached
        // mark builtin.
    }

    public void onCancelPendingInputEvents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onCancelPendingInputEvents()V");
    }

    public boolean onCapturedPointerEvent(MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onCapturedPointerEvent(Landroid/view/MotionEvent;)Z");
    }

    public boolean onCheckIsTextEditor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onCheckIsTextEditor()Z");
    }

    protected void onConfigurationChanged(Configuration arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onConfigurationChanged(Landroid/content/res/Configuration;)V");
    }

    protected void onCreateContextMenu(ContextMenu arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onCreateContextMenu(Landroid/view/ContextMenu;)V");
    }

    protected int[] onCreateDrawableState(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onCreateDrawableState(I)[I");
    }

    public InputConnection onCreateInputConnection(EditorInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onCreateInputConnection(Landroid/view/inputmethod/EditorInfo;)Landroid/view/inputmethod/InputConnection;");
    }

    public void onCreateViewTranslationRequest(int[] arg0, Consumer<ViewTranslationRequest> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onCreateViewTranslationRequest([ILjava/util/function/Consumer;)V");
    }

    public void onCreateVirtualViewTranslationRequests(long[] arg0, int[] arg1, Consumer<ViewTranslationRequest> arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onCreateVirtualViewTranslationRequests([J[ILjava/util/function/Consumer;)V");
    }

    protected void onDetachedFromWindow() {
        // AOSP View.onDetachedFromWindow: clears pressed state and stops
        // animations; the shell tracks only the pressed flag.
        setPressed(false);
    }

    protected void onDisplayHint(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onDisplayHint(I)V");
    }

    public boolean onDragEvent(DragEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onDragEvent(Landroid/view/DragEvent;)Z");
    }

    protected void onDraw(Canvas arg0) {
        // AOSP View.java:22474: default View draws nothing here; subclasses
        // override (guest overrides dispatch through the interpreter).
    }

    public void onDrawForeground(Canvas arg0) {
        // AOSP View.java onDrawForeground: draws only with a foreground set
        // (mForegroundInfo); no foreground model yet, so the null leg runs.
    }

    protected final void onDrawScrollBars(Canvas arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onDrawScrollBars(Landroid/graphics/Canvas;)V");
    }

    public boolean onFilterTouchEventForSecurity(MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onFilterTouchEventForSecurity(Landroid/view/MotionEvent;)Z");
    }

    protected void onFinishInflate() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onFinishInflate()V");
    }

    public void onFinishTemporaryDetach() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onFinishTemporaryDetach()V");
    }

    protected void onFocusChanged(boolean arg0, int arg1, Rect arg2) {
    }

    public boolean onGenericMotionEvent(MotionEvent arg0) {
        return false;
    }

    public void onHoverChanged(boolean arg0) {
    }

    public boolean onHoverEvent(MotionEvent arg0) {
        return false;
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onInitializeAccessibilityEvent(Landroid/view/accessibility/AccessibilityEvent;)V");
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onInitializeAccessibilityNodeInfo(Landroid/view/accessibility/AccessibilityNodeInfo;)V");
    }

    public boolean onKeyDown(int arg0, KeyEvent arg1) {
        if (arg0 == KeyEvent.KEYCODE_DPAD_CENTER
                || arg0 == KeyEvent.KEYCODE_ENTER
                || arg0 == KeyEvent.KEYCODE_SPACE) {
            return mClickable && performClick();
        }
        return false;
    }

    public boolean onKeyLongPress(int arg0, KeyEvent arg1) {
        return false;
    }

    public boolean onKeyMultiple(int arg0, int arg1, KeyEvent arg2) {
        return false;
    }

    public boolean onKeyPreIme(int arg0, KeyEvent arg1) {
        return false;
    }

    public boolean onKeyShortcut(int arg0, KeyEvent arg1) {
        return false;
    }

    public boolean onKeyUp(int arg0, KeyEvent arg1) {
        return false;
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
        setMeasuredDimension(
                getDefaultSize(getSuggestedMinimumWidth(), arg0),
                getDefaultSize(getSuggestedMinimumHeight(), arg1));
    }

    protected void onOverScrolled(int arg0, int arg1, boolean arg2, boolean arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onOverScrolled(IIZZ)V");
    }

    public void onPointerCaptureChange(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onPointerCaptureChange(Z)V");
    }

    public void onPopulateAccessibilityEvent(AccessibilityEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onPopulateAccessibilityEvent(Landroid/view/accessibility/AccessibilityEvent;)V");
    }

    public void onProvideAutofillStructure(ViewStructure arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onProvideAutofillStructure(Landroid/view/ViewStructure;I)V");
    }

    public void onProvideAutofillVirtualStructure(ViewStructure arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onProvideAutofillVirtualStructure(Landroid/view/ViewStructure;I)V");
    }

    public void onProvideContentCaptureStructure(ViewStructure arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onProvideContentCaptureStructure(Landroid/view/ViewStructure;I)V");
    }

    public void onProvideStructure(ViewStructure arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onProvideStructure(Landroid/view/ViewStructure;)V");
    }

    public void onProvideVirtualStructure(ViewStructure arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onProvideVirtualStructure(Landroid/view/ViewStructure;)V");
    }

    public ContentInfo onReceiveContent(ContentInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onReceiveContent(Landroid/view/ContentInfo;)Landroid/view/ContentInfo;");
    }

    public PointerIcon onResolvePointerIcon(MotionEvent arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onResolvePointerIcon(Landroid/view/MotionEvent;I)Landroid/view/PointerIcon;");
    }

    protected void onRestoreInstanceState(Parcelable arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onRestoreInstanceState(Landroid/os/Parcelable;)V");
    }

    public void onRtlPropertiesChanged(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onRtlPropertiesChanged(I)V");
    }

    protected Parcelable onSaveInstanceState() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onSaveInstanceState()Landroid/os/Parcelable;");
    }

    public void onScreenStateChanged(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onScreenStateChanged(I)V");
    }

    public void onScrollCaptureSearch(Rect arg0, Point arg1, Consumer<ScrollCaptureTarget> arg2) {
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

    public boolean onTouchEvent(MotionEvent arg0) {
        final int action = arg0.getActionMasked();
        if (!mEnabled) {
            return mClickable || mLongClickable;
        }
        if (action == MotionEvent.ACTION_DOWN) {
            if (mClickable || mLongClickable) {
                setPressed(true);
                return true;
            }
            return false;
        }
        if (action == MotionEvent.ACTION_UP) {
            if (mPressed) {
                setPressed(false);
                if (mClickable) {
                    performClick();
                }
                return true;
            }
            return false;
        }
        if (action == MotionEvent.ACTION_CANCEL) {
            if (mPressed) {
                setPressed(false);
                return true;
            }
            return false;
        }
        return mClickable;
    }

    public boolean onTrackballEvent(MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onTrackballEvent(Landroid/view/MotionEvent;)Z");
    }

    public void onViewTranslationResponse(ViewTranslationResponse arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onViewTranslationResponse(Landroid/view/translation/ViewTranslationResponse;)V");
    }

    public void onVirtualViewTranslationResponses(LongSparseArray<ViewTranslationResponse> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onVirtualViewTranslationResponses(Landroid/util/LongSparseArray;)V");
    }

    public void onVisibilityAggregated(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onVisibilityAggregated(Z)V");
    }

    protected void onVisibilityChanged(View arg0, int arg1) {
    }

    public void onWindowFocusChanged(boolean arg0) {
    }

    public void onWindowSystemUiVisibilityChanged(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->onWindowSystemUiVisibilityChanged(I)V");
    }

    protected void onWindowVisibilityChanged(int arg0) {
    }

    protected boolean overScrollBy(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, boolean arg8) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->overScrollBy(IIIIIIIIZ)Z");
    }

    public boolean performAccessibilityAction(int arg0, Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->performAccessibilityAction(ILandroid/os/Bundle;)Z");
    }

    public boolean performClick() {
        if (mOnClickListener != null) {
            mOnClickListener.onClick(this);
            return true;
        }
        return false;
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
        return performLongClick(0f, 0f);
    }

    public boolean performLongClick(float arg0, float arg1) {
        if (mOnLongClickListener != null) {
            return mOnLongClickListener.onLongClick(this);
        }
        return false;
    }

    public ContentInfo performReceiveContent(ContentInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->performReceiveContent(Landroid/view/ContentInfo;)Landroid/view/ContentInfo;");
    }

    public void playSoundEffect(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->playSoundEffect(I)V");
    }

    /**
     * AOSP View.post: attached views post through the AttachInfo handler.
     * The shell's Handler delivers inline (see Handler.java), and a view
     * posted to before attach runs inline too instead of waiting in a
     * run queue.
     */
    public boolean post(Runnable action) {
        AttachInfo attachInfo = mAttachInfo;
        if (attachInfo != null) {
            return attachInfo.mHandler.post(action);
        }
        action.run();
        return true;
    }

    /** AOSP View.postDelayed: AttachInfo handler, inline in the shell. */
    public boolean postDelayed(Runnable action, long delayMillis) {
        AttachInfo attachInfo = mAttachInfo;
        if (attachInfo != null) {
            return attachInfo.mHandler.postDelayed(action, delayMillis);
        }
        action.run();
        return true;
    }

    public void postInvalidate() {
        // AOSP posts a no-arg invalidate to the UI handler; delivery is
        // inline in the shell, so invalidate directly.
        invalidate();
    }

    public void postInvalidate(int left, int top, int right, int bottom) {
        invalidate(left, top, right, bottom);
    }

    public void postInvalidateDelayed(long arg0) {
        invalidate();
    }

    public void postInvalidateDelayed(long arg0, int arg1, int arg2, int arg3, int arg4) {
        invalidate(arg1, arg2, arg3, arg4);
    }

    public void postInvalidateOnAnimation() {
        invalidate();
    }

    public void postInvalidateOnAnimation(int arg0, int arg1, int arg2, int arg3) {
        invalidate(arg0, arg1, arg2, arg3);
    }

    /**
     * AOSP View.postOnAnimation: deliver on the animation callback. The
     * desktop Choreographer is pumped by the render loop, so the runnable
     * runs on the next guest frame.
     */
    public void postOnAnimation(Runnable action) {
        Choreographer.getInstance().postFrameCallback(
                frameTimeNanos -> action.run());
    }

    /** AOSP View.postOnAnimationDelayed: delayed animation callback. */
    public void postOnAnimationDelayed(Runnable action, long delayMillis) {
        Choreographer.getInstance().postFrameCallbackDelayed(
                frameTimeNanos -> action.run(), delayMillis);
    }

    public void refreshDrawableState() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->refreshDrawableState()V");
    }

    public void releasePointerCapture() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->releasePointerCapture()V");
    }

    /**
     * AOSP View.removeCallbacks: drop a pending post from the AttachInfo
     * handler. The shell's handler delivers inline, so there is normally
     * nothing queued, but the call must stay reachable from detach paths.
     */
    public boolean removeCallbacks(Runnable action) {
        if (action != null && mAttachInfo != null) {
            mAttachInfo.mHandler.removeCallbacks(action);
        }
        return true;
    }

    /** AOSP View.removeOnAttachStateChangeListener (lines 7842-7848). */
    public void removeOnAttachStateChangeListener(View.OnAttachStateChangeListener arg0) {
        if (mOnAttachStateChangeListeners == null) {
            return;
        }
        mOnAttachStateChangeListeners.remove(arg0);
    }

    public void removeOnLayoutChangeListener(View.OnLayoutChangeListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->removeOnLayoutChangeListener(Landroid/view/View$OnLayoutChangeListener;)V");
    }

    public void removeOnUnhandledKeyEventListener(View.OnUnhandledKeyEventListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->removeOnUnhandledKeyEventListener(Landroid/view/View$OnUnhandledKeyEventListener;)V");
    }

    public void reportAppJankStats(AppJankStats arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->reportAppJankStats(Landroid/app/jank/AppJankStats;)V");
    }

    public void requestApplyInsets() {
        // AOSP View.requestApplyInsets: walk up to the window root, which
        // re-dispatches insets on the next traversal. The shell models no
        // insets (see setOnApplyWindowInsetsListener), so ViewRootImpl's
        // end of the walk is the no-op; the walk itself is still the shape
        // guests expect and keeps the listener plumbing reachable.
        if (mParent != null) {
            mParent.requestFitSystemWindows();
        }
    }

    public void requestFitSystemWindows() {
        // Deprecated AOSP alias for requestApplyInsets().
        requestApplyInsets();
    }

    public final boolean requestFocus() {
        return requestFocus(View.FOCUS_DOWN);
    }

    public final boolean requestFocus(int arg0) {
        return requestFocus(arg0, null);
    }

    public boolean requestFocus(int arg0, Rect arg1) {
        if (!isFocusable() || !mEnabled) {
            return false;
        }
        if (mFocused) {
            return true;
        }
        mFocused = true;
        if (mOnFocusChangeListener != null) {
            mOnFocusChangeListener.onFocusChange(this, true);
        }
        onFocusChanged(true, arg0, arg1);
        invalidate();
        return true;
    }

    public final boolean requestFocusFromTouch() {
        return requestFocus(View.FOCUS_DOWN);
    }

    public void requestLayout() {
        mLayoutRequested = true;
        mLayoutRequired = true;
        mDirty = true;
        if (mParent != null && !mParent.isLayoutRequested()) {
            mParent.requestLayout();
        }
    }

    public void requestPointerCapture() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->requestPointerCapture()V");
    }

    public boolean requestRectangleOnScreen(Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->requestRectangleOnScreen(Landroid/graphics/Rect;)Z");
    }

    public boolean requestRectangleOnScreen(Rect arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->requestRectangleOnScreen(Landroid/graphics/Rect;Z)Z");
    }

    public final void requestUnbufferedDispatch(MotionEvent arg0) {
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

    public void restoreHierarchyState(SparseArray<Parcelable> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->restoreHierarchyState(Landroid/util/SparseArray;)V");
    }

    public final void saveAttributeDataForStyleable(Context arg0, int[] arg1, AttributeSet arg2, TypedArray arg3, int arg4, int arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->saveAttributeDataForStyleable(Landroid/content/Context;[ILandroid/util/AttributeSet;Landroid/content/res/TypedArray;II)V");
    }

    public void saveHierarchyState(SparseArray<Parcelable> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->saveHierarchyState(Landroid/util/SparseArray;)V");
    }

    public void scheduleDrawable(Drawable arg0, Runnable arg1, long arg2) {
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

    public void sendAccessibilityEventUnchecked(AccessibilityEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->sendAccessibilityEventUnchecked(Landroid/view/accessibility/AccessibilityEvent;)V");
    }

    public void setAccessibilityDataSensitive(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setAccessibilityDataSensitive(I)V");
    }

    public void setAccessibilityDelegate(View.AccessibilityDelegate delegate) {
        // AOSP frameworks-base core/java/android/view/View.java#setAccessibilityDelegate:
        // installs the delegate; no accessibility services headless → no-op
        // (matches the interpreter's headless builtin).
    }

    public void setAccessibilityHeading(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setAccessibilityHeading(Z)V");
    }

    public void setAccessibilityLiveRegion(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setAccessibilityLiveRegion(I)V");
    }

    public void setAccessibilityPaneTitle(CharSequence arg0) {
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

    public void setAllowedHandwritingDelegatePackage(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setAllowedHandwritingDelegatePackage(Ljava/lang/String;)V");
    }

    public void setAllowedHandwritingDelegatorPackage(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setAllowedHandwritingDelegatorPackage(Ljava/lang/String;)V");
    }

    public void setAlpha(float arg0) {
        // AOSP View.setAlpha: the transformation info holds the value and
        // syncs it to the render node. The shell has no transformation-info
        // object; the node itself is the single store, and the host raster
        // path does not consume node alpha yet (see RenderNode.mAlpha).
        if (arg0 != getAlpha()) {
            mRenderNode.setAlpha(arg0);
            invalidate();
        }
    }

    public void setAnimation(Animation arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setAnimation(Landroid/view/animation/Animation;)V");
    }

    public void setAnimationMatrix(Matrix arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setAnimationMatrix(Landroid/graphics/Matrix;)V");
    }

    public void setAutoHandwritingEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setAutoHandwritingEnabled(Z)V");
    }

    public void setAutofillHints(String[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setAutofillHints([Ljava/lang/String;)V");
    }

    public void setAutofillId(AutofillId arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setAutofillId(Landroid/view/autofill/AutofillId;)V");
    }

    public void setBackground(Drawable arg0) {
        mBackground = arg0;
        requestLayout();
        invalidate();
    }

    public void setBackgroundColor(int arg0) {
        // The shell's draw chain paints only a Drawable background; keep the
        // call's invalidation observable and leave the color for the real
        // Drawable model (no ColorDrawable skeleton yet).
        invalidate();
    }

    public void setBackgroundDrawable(Drawable arg0) {
        setBackground(arg0);
    }

    public void setBackgroundResource(int arg0) {
        invalidate();
    }

    public void setBackgroundTintBlendMode(BlendMode arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setBackgroundTintBlendMode(Landroid/graphics/BlendMode;)V");
    }

    public void setBackgroundTintList(ColorStateList arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setBackgroundTintList(Landroid/content/res/ColorStateList;)V");
    }

    public void setBackgroundTintMode(PorterDuff.Mode arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setBackgroundTintMode(Landroid/graphics/PorterDuff$Mode;)V");
    }

    /**
     * Ported from aosp-mirror/platform_frameworks_base
     * core/java/android/view/View.java (setBottom): move the bottom edge,
     * report a size change, and mirror the new bounds onto the node.
     */
    public final void setBottom(int arg0) {
        if (arg0 != mBottom) {
            int oldHeight = mBottom - mTop;
            mBottom = arg0;
            int newHeight = mBottom - mTop;
            sizeChange(mRight - mLeft, newHeight, mRight - mLeft, oldHeight);
            mRenderNode.setLeftTopRightBottom(mLeft, mTop, mRight, mBottom);
        }
    }

    public void setCameraDistance(float arg0) {
        // Ported from AOSP setCameraDistance: Math.abs(distance) / dpi;
        // dpi = 1 pending the Resources hand-field (mirrors the getter).
        mRenderNode.setCameraDistance(Math.abs(arg0) / 1.0f);
    }

    public void setClickable(boolean arg0) {
        mClickable = arg0;
    }

    /**
     * Ported from aosp-mirror/platform_frameworks_base
     * core/java/android/view/View.java (setClipBounds): identity/equals
     * guarded, mClipBounds copy-or-set, RenderNode delegation; the
     * invalidateViewProperty leg is unported invalidation machinery.
     */
    public void setClipBounds(Rect arg0) {
        if (arg0 == mClipBounds
                || (arg0 != null && arg0.equals(mClipBounds))) {
            return;
        }
        if (arg0 != null) {
            if (mClipBounds == null) {
                mClipBounds = new Rect(arg0);
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

    public void setContentCaptureSession(ContentCaptureSession arg0) {
        // AOSP frameworks-base core/java/android/view/View.java:10976-10978
        // setContentCaptureSession: mContentCaptureSession = contentCapture-
        // Session. The shell stores no session (nothing reads it).
    }

    public void setContentDescription(CharSequence arg0) {
        mContentDescription = arg0;
    }

    private CharSequence mContentDescription;

    public final void setContentSensitivity(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setContentSensitivity(I)V");
    }

    public void setContextClickable(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setContextClickable(Z)V");
    }

    public void setDefaultFocusHighlightEnabled(boolean defaultFocusHighlightEnabled) {
        // AOSP frameworks-base core/java/android/view/View.java#setDefaultFocusHighlightEnabled:
        // focus highlight styling; no focus UI headless → no-op.
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
        if (mEnabled != arg0) {
            mEnabled = arg0;
            if (!arg0) {
                setPressed(false);
            }
            invalidate();
        }
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

    public void setFocusable(boolean focusable) {
        setFocusable(focusable ? FOCUSABLE : NOT_FOCUSABLE);
    }

    public void setFocusable(int focusable) {
        mFocusable = focusable;
    }

    public void setFocusableInTouchMode(boolean focusableInTouchMode) {
        if (focusableInTouchMode) {
            mFocusable = FOCUSABLE;
        }
    }

    public void setFocusedByDefault(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setFocusedByDefault(Z)V");
    }

    public void setForceDarkAllowed(boolean arg0) {
        // AOSP frameworks-base core/java/android/view/View.java#setForceDarkAllowed:
        // force-dark is a display feature; no headless effect → no-op
        // (matches the interpreter's builtin).
    }

    public void setForeground(Drawable arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setForeground(Landroid/graphics/drawable/Drawable;)V");
    }

    public void setForegroundGravity(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setForegroundGravity(I)V");
    }

    public void setForegroundTintBlendMode(BlendMode arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setForegroundTintBlendMode(Landroid/graphics/BlendMode;)V");
    }

    public void setForegroundTintList(ColorStateList arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setForegroundTintList(Landroid/content/res/ColorStateList;)V");
    }

    public void setForegroundTintMode(PorterDuff.Mode arg0) {
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

    public void setHandwritingDelegatorCallback(Runnable arg0) {
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

    public void setHorizontalScrollbarThumbDrawable(Drawable arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setHorizontalScrollbarThumbDrawable(Landroid/graphics/drawable/Drawable;)V");
    }

    public void setHorizontalScrollbarTrackDrawable(Drawable arg0) {
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

    public void setImportantForAccessibility(int importantForAccessibility) {
        // AOSP frameworks-base core/java/android/view/View.java#setImportantForAccessibility:
        // flags; no accessibility services headless → no-op
        // (matches the interpreter's headless builtin).
    }

    public void setImportantForAutofill(int arg0) {
        // AOSP frameworks-base core/java/android/view/View.java#setImportantForAutofill:
        // autofill mode has no desktop effect without an AutofillManager;
        // matches the interpreter's headless no-op builtin.
    }

    public void setImportantForContentCapture(int arg0) {
        // AOSP frameworks-base core/java/android/view/View.java:10712-10718
        // setImportantForContentCapture: reset then re-set the PFLAG4_
        // IMPORTANT_FOR_CONTENT_CAPTURE_MASK bits. The shell keeps no
        // capture flags (no capture pipeline reads them).
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

    public void setLayerPaint(Paint arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setLayerPaint(Landroid/graphics/Paint;)V");
    }

    public void setLayerType(int arg0, Paint arg1) {
        // AOSP View.setLayerType: validates the enum and stores the paint;
        // the shell has no layer compositing, so only the enum round-trips.
        if (arg0 < LAYER_TYPE_NONE || arg0 > LAYER_TYPE_HARDWARE) {
            throw new IllegalArgumentException(
                    "Layer type must be LAYER_TYPE_NONE, LAYER_TYPE_SOFTWARE, "
                            + "or LAYER_TYPE_HARDWARE");
        }
        if (mLayerType != arg0) {
            mLayerType = arg0;
            invalidate();
        }
    }

    public void setLayoutDirection(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setLayoutDirection(I)V");
    }

    public void setLayoutParams(ViewGroup.LayoutParams arg0) {
        mLayoutParams = arg0;
    }

    /**
     * Ported from aosp-mirror/platform_frameworks_base
     * core/java/android/view/View.java (setLeft): move the left edge, report
     * a size change, and mirror the new bounds onto the node.
     */
    public final void setLeft(int arg0) {
        if (arg0 != mLeft) {
            int oldWidth = mRight - mLeft;
            mLeft = arg0;
            int newWidth = mRight - mLeft;
            sizeChange(newWidth, mBottom - mTop, oldWidth, mBottom - mTop);
            mRenderNode.setLeftTopRightBottom(mLeft, mTop, mRight, mBottom);
        }
    }

    /** Ported from the same file (setLeftTopRightBottom): setFrame. */
    public final void setLeftTopRightBottom(int arg0, int arg1, int arg2, int arg3) {
        setFrame(arg0, arg1, arg2, arg3);
    }

    public void setLongClickable(boolean arg0) {
        mLongClickable = arg0;
    }

    protected final void setMeasuredDimension(int measuredWidth, int measuredHeight) {
        mMeasuredWidth = measuredWidth;
        mMeasuredHeight = measuredHeight;
        mPrivateFlags |= PFLAG_MEASURED_DIMENSION_SET;
    }

    public void setMinimumHeight(int arg0) {
        mMinHeight = arg0;
        requestLayout();
    }

    public void setMinimumWidth(int arg0) {
        mMinWidth = arg0;
        requestLayout();
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

    public void setOnApplyWindowInsetsListener(View.OnApplyWindowInsetsListener arg0) {
        // AOSP frameworks-base core/java/android/view/View.java
        // setOnApplyWindowInsetsListener: stores the listener for window
        // insets dispatch. The shell has no window insets system, so the
        // listener is recorded nowhere (no-op).
    }

    public void setOnCapturedPointerListener(View.OnCapturedPointerListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setOnCapturedPointerListener(Landroid/view/View$OnCapturedPointerListener;)V");
    }

    public void setOnClickListener(View.OnClickListener arg0) {
        if (!mClickable) {
            setClickable(true);
        }
        mOnClickListener = arg0;
    }

    public void setOnContextClickListener(View.OnContextClickListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setOnContextClickListener(Landroid/view/View$OnContextClickListener;)V");
    }

    public void setOnCreateContextMenuListener(View.OnCreateContextMenuListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setOnCreateContextMenuListener(Landroid/view/View$OnCreateContextMenuListener;)V");
    }

    public void setOnDragListener(View.OnDragListener listener) {
        // AOSP frameworks-base core/java/android/view/View.java#setOnDragListener:
        // drag handling; no drag input headless → no-op
        // (matches the interpreter's headless builtin).
    }

    public void setOnFocusChangeListener(View.OnFocusChangeListener arg0) {
        mOnFocusChangeListener = arg0;
    }

    public void setOnGenericMotionListener(View.OnGenericMotionListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setOnGenericMotionListener(Landroid/view/View$OnGenericMotionListener;)V");
    }

    public void setOnHoverListener(View.OnHoverListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setOnHoverListener(Landroid/view/View$OnHoverListener;)V");
    }

    public void setOnKeyListener(View.OnKeyListener arg0) {
        mOnKeyListener = arg0;
    }

    public void setOnLongClickListener(View.OnLongClickListener arg0) {
        if (!mLongClickable) {
            setLongClickable(true);
        }
        mOnLongClickListener = arg0;
    }

    public void setOnReceiveContentListener(String[] arg0, OnReceiveContentListener arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setOnReceiveContentListener([Ljava/lang/String;Landroid/view/OnReceiveContentListener;)V");
    }

    public void setOnScrollChangeListener(View.OnScrollChangeListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setOnScrollChangeListener(Landroid/view/View$OnScrollChangeListener;)V");
    }

    public void setOnSystemUiVisibilityChangeListener(View.OnSystemUiVisibilityChangeListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setOnSystemUiVisibilityChangeListener(Landroid/view/View$OnSystemUiVisibilityChangeListener;)V");
    }

    public void setOnTouchListener(View.OnTouchListener arg0) {
        mOnTouchListener = arg0;
    }

    /** Ported from AOSP {@code View#setOutlineAmbientShadowColor} semantics. */
    public void setOutlineAmbientShadowColor(int arg0) {
        if (arg0 != mRenderNode.getAmbientShadowColor()) {
            mRenderNode.setAmbientShadowColor(arg0);
            invalidate();
        }
    }

    /** Ported from AOSP {@code View#setOutlineSpotShadowColor} semantics. */
    public void setOutlineSpotShadowColor(int arg0) {
        if (arg0 != mRenderNode.getSpotShadowColor()) {
            mRenderNode.setSpotShadowColor(arg0);
            invalidate();
        }
    }

    public void setOverScrollMode(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setOverScrollMode(I)V");
    }

    public void setPadding(int arg0, int arg1, int arg2, int arg3) {
        mPaddingLeft = arg0;
        mPaddingTop = arg1;
        mPaddingRight = arg2;
        mPaddingBottom = arg3;
        requestLayout();
        invalidate();
    }

    public void setPaddingRelative(int arg0, int arg1, int arg2, int arg3) {
        setPadding(arg0, arg1, arg2, arg3);
    }

    public void setPendingCredentialRequest(GetCredentialRequest arg0, OutcomeReceiver<GetCredentialResponse, GetCredentialException> arg1) {
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

    public void setPointerIcon(PointerIcon arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setPointerIcon(Landroid/view/PointerIcon;)V");
    }

    public final void setPreferKeepClear(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setPreferKeepClear(Z)V");
    }

    public final void setPreferKeepClearRects(List<Rect> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setPreferKeepClearRects(Ljava/util/List;)V");
    }

    public void setPressed(boolean arg0) {
        if (mPressed != arg0) {
            mPressed = arg0;
            invalidate();
        }
    }

    /** Ported from AOSP {@code View#setRenderEffect} semantics. */
    public void setRenderEffect(RenderEffect arg0) {
        if (arg0 != mRenderNode.getRenderEffect()) {
            mRenderNode.setRenderEffect(arg0);
            invalidate();
        }
    }

    public void setRequestedFrameRate(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setRequestedFrameRate(F)V");
    }

    public final void setRevealOnFocusHint(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setRevealOnFocusHint(Z)V");
    }

    /**
     * Ported from aosp-mirror/platform_frameworks_base
     * core/java/android/view/View.java (setRight): move the right edge,
     * report a size change, and mirror the new bounds onto the node.
     */
    public final void setRight(int arg0) {
        if (arg0 != mRight) {
            int oldWidth = mRight - mLeft;
            mRight = arg0;
            int newWidth = mRight - mLeft;
            sizeChange(newWidth, mBottom - mTop, oldWidth, mBottom - mTop);
            mRenderNode.setLeftTopRightBottom(mLeft, mTop, mRight, mBottom);
        }
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

    public final void setScrollCaptureCallback(ScrollCaptureCallback arg0) {
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
        if (mSelected != arg0) {
            mSelected = arg0;
            invalidate();
        }
    }

    public void setSoundEffectsEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setSoundEffectsEnabled(Z)V");
    }

    public void setStateDescription(CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setStateDescription(Ljava/lang/CharSequence;)V");
    }

    public void setStateListAnimator(StateListAnimator arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setStateListAnimator(Landroid/animation/StateListAnimator;)V");
    }

    public void setSupplementalDescription(CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setSupplementalDescription(Ljava/lang/CharSequence;)V");
    }

    public void setSystemGestureExclusionRects(List<Rect> arg0) {
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
    public void setTag(int arg0, Object arg1) {
        if ((arg0 >>> 24) < 2) {
            throw new IllegalArgumentException("The key must be an application-specific "
                    + "resource id.");
        }
        if (mKeyedTags == null) {
            mKeyedTags = new HashMap<Integer, Object>();
        }
        mKeyedTags.put(arg0, arg1);
    }

    private HashMap<Integer, Object> mKeyedTags;

    /** AOSP View.setTag(Object): plain mTag store. */
    public void setTag(Object arg0) {
        mTag = arg0;
    }

    private Object mTag;

    public void setTextAlignment(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setTextAlignment(I)V");
    }

    public void setTextDirection(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setTextDirection(I)V");
    }

    public void setTooltipText(CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setTooltipText(Ljava/lang/CharSequence;)V");
    }

    /**
     * Ported from aosp-mirror/platform_frameworks_base
     * core/java/android/view/View.java (setTop): move the top edge, report a
     * size change, and mirror the new bounds onto the node.
     */
    public final void setTop(int arg0) {
        if (arg0 != mTop) {
            int oldHeight = mBottom - mTop;
            mTop = arg0;
            int newHeight = mBottom - mTop;
            sizeChange(mRight - mLeft, newHeight, mRight - mLeft, oldHeight);
            mRenderNode.setLeftTopRightBottom(mLeft, mTop, mRight, mBottom);
        }
    }

    public void setTouchDelegate(TouchDelegate arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setTouchDelegate(Landroid/view/TouchDelegate;)V");
    }

    public void setTransitionAlpha(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setTransitionAlpha(F)V");
    }

    public final void setTransitionName(String arg0) {
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

    public void setVerticalScrollbarThumbDrawable(Drawable arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setVerticalScrollbarThumbDrawable(Landroid/graphics/drawable/Drawable;)V");
    }

    public void setVerticalScrollbarTrackDrawable(Drawable arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setVerticalScrollbarTrackDrawable(Landroid/graphics/drawable/Drawable;)V");
    }

    public void setViewTranslationCallback(ViewTranslationCallback arg0) {
        // AOSP frameworks-base core/java/android/view/View.java:33964-33966
        // setViewTranslationCallback: mViewTranslationCallback = callback.
        // The shell stores no callback (no translation pipeline reads it).
    }

    public void setVisibility(int arg0) {
        // AOSP View.setVisibility: stores the visibility bits; the reduced
        // shell has no display list to invalidate yet.
        mVisibility = arg0;
    }

    public void setWillNotCacheDrawing(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->setWillNotCacheDrawing(Z)V");
    }

    public void setWillNotDraw(boolean arg0) {
        // AOSP frameworks-base core/java/android/view/View.java#setWillNotDraw:
        // records whether this view skips draw passes; no draw dispatch
        // exists headless → no-op (matches the interpreter builtin).
    }

    public void setWindowInsetsAnimationCallback(WindowInsetsAnimation.Callback arg0) {
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

    public ActionMode startActionMode(ActionMode.Callback arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->startActionMode(Landroid/view/ActionMode$Callback;)Landroid/view/ActionMode;");
    }

    public ActionMode startActionMode(ActionMode.Callback arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->startActionMode(Landroid/view/ActionMode$Callback;I)Landroid/view/ActionMode;");
    }

    public void startAnimation(Animation arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->startAnimation(Landroid/view/animation/Animation;)V");
    }

    public final boolean startDrag(ClipData arg0, View.DragShadowBuilder arg1, Object arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->startDrag(Landroid/content/ClipData;Landroid/view/View$DragShadowBuilder;Ljava/lang/Object;I)Z");
    }

    public final boolean startDragAndDrop(ClipData arg0, View.DragShadowBuilder arg1, Object arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->startDragAndDrop(Landroid/content/ClipData;Landroid/view/View$DragShadowBuilder;Ljava/lang/Object;I)Z");
    }

    public boolean startNestedScroll(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->startNestedScroll(I)Z");
    }

    public void stopNestedScroll() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->stopNestedScroll()V");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->toString()Ljava/lang/String;");
    }

    public void transformMatrixToGlobal(Matrix arg0) {
        // AOSP View.transformMatrixToGlobal: walk up to the root premultiplying
        // each level's position, so the result maps this view's local points
        // into root/window coordinates. The desktop window root has no extra
        // transform, so a non-View parent stops the walk.
        arg0.preTranslate(mLeft, mTop);
        if (mParent instanceof View) {
            ((View) mParent).transformMatrixToGlobal(arg0);
        }
    }

    public void transformMatrixToLocal(Matrix arg0) {
        // AOSP View.transformMatrixToLocal: the inverse walk, post-multiplying
        // each level's negative position so the result maps window points back
        // into this view's local coordinates.
        arg0.postTranslate(-mLeft, -mTop);
        if (mParent instanceof View) {
            ((View) mParent).transformMatrixToLocal(arg0);
        }
    }

    public void unscheduleDrawable(Drawable arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->unscheduleDrawable(Landroid/graphics/drawable/Drawable;)V");
    }

    public void unscheduleDrawable(Drawable arg0, Runnable arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->unscheduleDrawable(Landroid/graphics/drawable/Drawable;Ljava/lang/Runnable;)V");
    }

    public final void updateDragShadow(View.DragShadowBuilder arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View;->updateDragShadow(Landroid/view/View$DragShadowBuilder;)V");
    }

    protected boolean verifyDrawable(Drawable arg0) {
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
            // AOSP frameworks-base core/java/android/view/View.java:32839:
            // AccessibilityDelegate has no explicit constructor and no
            // fields, so the implicit <init> body is empty. The
            // androidx core adapter subclasses this class and the compat
            // <clinit> allocates a plain DEFAULT_DELEGATE.
        }

        public void addExtraDataToAccessibilityNodeInfo(View arg0, AccessibilityNodeInfo arg1, String arg2, Bundle arg3) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$AccessibilityDelegate;->addExtraDataToAccessibilityNodeInfo(Landroid/view/View;Landroid/view/accessibility/AccessibilityNodeInfo;Ljava/lang/String;Landroid/os/Bundle;)V");
        }

        public boolean dispatchPopulateAccessibilityEvent(View arg0, AccessibilityEvent arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$AccessibilityDelegate;->dispatchPopulateAccessibilityEvent(Landroid/view/View;Landroid/view/accessibility/AccessibilityEvent;)Z");
        }

        public AccessibilityNodeProvider getAccessibilityNodeProvider(View arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$AccessibilityDelegate;->getAccessibilityNodeProvider(Landroid/view/View;)Landroid/view/accessibility/AccessibilityNodeProvider;");
        }

        public void onInitializeAccessibilityEvent(View arg0, AccessibilityEvent arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$AccessibilityDelegate;->onInitializeAccessibilityEvent(Landroid/view/View;Landroid/view/accessibility/AccessibilityEvent;)V");
        }

        public void onInitializeAccessibilityNodeInfo(View arg0, AccessibilityNodeInfo arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$AccessibilityDelegate;->onInitializeAccessibilityNodeInfo(Landroid/view/View;Landroid/view/accessibility/AccessibilityNodeInfo;)V");
        }

        public void onPopulateAccessibilityEvent(View arg0, AccessibilityEvent arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$AccessibilityDelegate;->onPopulateAccessibilityEvent(Landroid/view/View;Landroid/view/accessibility/AccessibilityEvent;)V");
        }

        public boolean onRequestSendAccessibilityEvent(ViewGroup arg0, View arg1, AccessibilityEvent arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$AccessibilityDelegate;->onRequestSendAccessibilityEvent(Landroid/view/ViewGroup;Landroid/view/View;Landroid/view/accessibility/AccessibilityEvent;)Z");
        }

        public boolean performAccessibilityAction(View arg0, int arg1, Bundle arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$AccessibilityDelegate;->performAccessibilityAction(Landroid/view/View;ILandroid/os/Bundle;)Z");
        }

        public void sendAccessibilityEvent(View arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$AccessibilityDelegate;->sendAccessibilityEvent(Landroid/view/View;I)V");
        }

        public void sendAccessibilityEventUnchecked(View arg0, AccessibilityEvent arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$AccessibilityDelegate;->sendAccessibilityEventUnchecked(Landroid/view/View;Landroid/view/accessibility/AccessibilityEvent;)V");
        }
        }
        public static class DragShadowBuilder {

        public DragShadowBuilder() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$DragShadowBuilder;-><init>()V");
        }

        public DragShadowBuilder(View arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$DragShadowBuilder;-><init>(Landroid/view/View;)V");
        }

        public final View getView() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$DragShadowBuilder;->getView()Landroid/view/View;");
        }

        public void onDrawShadow(Canvas arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$DragShadowBuilder;->onDrawShadow(Landroid/graphics/Canvas;)V");
        }

        public void onProvideShadowMetrics(Point arg0, Point arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$DragShadowBuilder;->onProvideShadowMetrics(Landroid/graphics/Point;Landroid/graphics/Point;)V");
        }
        }
        public static interface OnApplyWindowInsetsListener {

        public abstract WindowInsets onApplyWindowInsets(View arg0, WindowInsets arg1);
        }
        public static interface OnAttachStateChangeListener {

        public abstract void onViewAttachedToWindow(View arg0);

        public abstract void onViewDetachedFromWindow(View arg0);
        }
        public static interface OnCapturedPointerListener {

        public abstract boolean onCapturedPointer(View arg0, MotionEvent arg1);
        }
        public static interface OnClickListener {

        public abstract void onClick(View arg0);
        }
        public static interface OnContextClickListener {

        public abstract boolean onContextClick(View arg0);
        }
        public static interface OnCreateContextMenuListener {

        public abstract void onCreateContextMenu(ContextMenu arg0, View arg1, ContextMenu.ContextMenuInfo arg2);
        }
        public static interface OnDragListener {

        public abstract boolean onDrag(View arg0, DragEvent arg1);
        }
        public static interface OnFocusChangeListener {

        public abstract void onFocusChange(View arg0, boolean arg1);
        }
        public static interface OnGenericMotionListener {

        public abstract boolean onGenericMotion(View arg0, MotionEvent arg1);
        }
        public static interface OnHoverListener {

        public abstract boolean onHover(View arg0, MotionEvent arg1);
        }
        public static interface OnKeyListener {

        public abstract boolean onKey(View arg0, int arg1, KeyEvent arg2);
        }
        public static interface OnLayoutChangeListener {

        public abstract void onLayoutChange(View arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8);
        }
        public static interface OnLongClickListener {

        public abstract boolean onLongClick(View arg0);

        public default boolean onLongClickUseDefaultHapticFeedback(View arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/View$OnLongClickListener;->onLongClickUseDefaultHapticFeedback(Landroid/view/View;)Z");
        }
        }
        public static interface OnScrollChangeListener {

        public abstract void onScrollChange(View arg0, int arg1, int arg2, int arg3, int arg4);
        }
        public static interface OnSystemUiVisibilityChangeListener {

        public abstract void onSystemUiVisibilityChange(int arg0);
        }
        public static interface OnTouchListener {

        public abstract boolean onTouch(View arg0, MotionEvent arg1);
        }
        public static interface OnUnhandledKeyEventListener {

        public abstract boolean onUnhandledKeyEvent(View arg0, KeyEvent arg1);
        }

        /**
         * MeasureSpec packs a dimension size and its mode into a single int:
         * mode occupies the top two bits (bits 30-31), size the lower 30 bits.
         * Pure arithmetic — no state. Ported verbatim from AOSP's
         * View.MeasureSpec makeMeasureSpec encoding.
         */
        public static final class MeasureSpec {

            /** MeasureSpec mode: the parent has not imposed any constraint on the child. */
            public static final int MODE_UNSPECIFIED = 0;
            /** MeasureSpec mode: the parent has determined an exact size for the child. */
            public static final int MODE_EXACTLY = 0x1 << 30;
            /** MeasureSpec mode: the child can be as large as it wants up to the specified size. */
            public static final int MODE_AT_MOST = 0x2 << 30;

            private static final int MODE_MASK = 0x3 << 30;

            private MeasureSpec() {
            }

            /**
             * AOSP frameworks-base core/java/android/view/View.java
             * makeMeasureSpec(size, mode): (size & ~MODE_MASK) | (mode & MODE_MASK).
             */
            public static int makeMeasureSpec(int size, int mode) {
                return (size & ~MODE_MASK) | (mode & MODE_MASK);
            }

            /**
             * AOSP .../View.java getMode(spec): spec & MODE_MASK.
             */
            public static int getMode(int spec) {
                return spec & MODE_MASK;
            }

            /**
             * AOSP .../View.java getSize(spec): spec & ~MODE_MASK.
             */
            public static int getSize(int spec) {
                return spec & ~MODE_MASK;
            }
        }
    }

