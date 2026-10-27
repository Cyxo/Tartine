package android.view;

    public class ViewGroup extends android.view.View implements android.view.ViewManager, android.view.ViewParent {

    protected static final int CLIP_TO_PADDING_MASK = 34;
    public static final int FOCUS_AFTER_DESCENDANTS = 262144;
    public static final int FOCUS_BEFORE_DESCENDANTS = 131072;
    public static final int FOCUS_BLOCK_DESCENDANTS = 393216;
    public static final int LAYOUT_MODE_CLIP_BOUNDS = 0;
    public static final int LAYOUT_MODE_OPTICAL_BOUNDS = 1;
    public static final int PERSISTENT_ALL_CACHES = 3;
    public static final int PERSISTENT_ANIMATION_CACHE = 1;
    public static final int PERSISTENT_NO_CACHE = 0;
    public static final int PERSISTENT_SCROLLING_CACHE = 2;

    /** AOSP ViewGroup(Context) (line 702-704): chains this(context, null). */
    public ViewGroup(android.content.Context arg0) {
        this(arg0, null);
    }

    /** AOSP ViewGroup(Context, AttributeSet) (line 706-708): chains
     * this(context, attrs, 0); the shell keeps the chain (the
     * attribute-flavored members flatten onto the View seed). */
    public ViewGroup(android.content.Context arg0, android.util.AttributeSet arg1) {
        this(arg0, arg1, 0);
    }

    /** AOSP ViewGroup(Context, AttributeSet, int defStyleAttr) (lines
     * 710-712): chains this(..., defStyleAttr, 0). */
    public ViewGroup(android.content.Context arg0, android.util.AttributeSet arg1, int arg2) {
        this(arg0, arg1, arg2, 0);
    }

    /**
     * AOSP ViewGroup(Context, AttributeSet, int, int) (lines 714-718):
     * super-chains then runs initViewGroup()/initFromAttributes(). The
     * shell carries the super seed plus the staged flag seeds
     * (initViewGroup); initFromAttributes defers with the XML-attr
     * machinery.
     */
    public ViewGroup(android.content.Context arg0, android.util.AttributeSet arg1, int arg2,
            int arg3) {
        super(arg0, arg1, arg2, arg3);
        initViewGroup();
    }

    /**
     * AOSP ViewGroup.initViewGroup (lines 721-739): the device seeds
     * WILL_NOT_DRAW, the clip/animation flag family, descendant
     * focusability and the mChildren array; the shell seeds the staged
     * flag bits only (see the mGroupFlags staging note — draw-chain
     * readers), while the children live in the mChildren list defaulting
     * empty.
     */
    private void initViewGroup() {
        mGroupFlags |= FLAG_CLIP_CHILDREN;
        mGroupFlags |= FLAG_CLIP_TO_PADDING;
    }

    public void addChildrenForAccessibility(java.util.ArrayList<android.view.View> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->addChildrenForAccessibility(Ljava/util/ArrayList;)V");
    }

    public void addExtraDataToAccessibilityNodeInfo(android.view.accessibility.AccessibilityNodeInfo arg0, java.lang.String arg1, android.os.Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->addExtraDataToAccessibilityNodeInfo(Landroid/view/accessibility/AccessibilityNodeInfo;Ljava/lang/String;Landroid/os/Bundle;)V");
    }

    public void addFocusables(java.util.ArrayList<android.view.View> arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->addFocusables(Ljava/util/ArrayList;II)V");
    }

    public void addKeyboardNavigationClusters(java.util.Collection<android.view.View> arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->addKeyboardNavigationClusters(Ljava/util/Collection;I)V");
    }

    public boolean addStatesFromChildren() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->addStatesFromChildren()Z");
    }

    public void addTouchables(java.util.ArrayList<android.view.View> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->addTouchables(Ljava/util/ArrayList;)V");
    }

    // Ported from aosp-mirror/platform_frameworks_base core/java/android/view/ViewGroup.java:
    // addView null-checks are IllegalArgumentException and live in the addView
    // overloads (addViewInLayout itself performs none; a null child would trip
    // NPEs deeper in attachViewToParent on device).
    public void addView(android.view.View arg0) {
        if (arg0 == null) {
            throw new IllegalArgumentException("Cannot add a null child view to a ViewGroup");
        }
        addViewInLayout(arg0, -1, null, false);
    }

    public void addView(android.view.View arg0, android.view.ViewGroup.LayoutParams arg1) {
        if (arg0 == null) {
            throw new IllegalArgumentException("Cannot add a null child view to a ViewGroup");
        }
        addViewInLayout(arg0, -1, arg1, false);
    }

    public void addView(android.view.View arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->addView(Landroid/view/View;I)V");
    }

    public void addView(android.view.View arg0, int arg1, android.view.ViewGroup.LayoutParams arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->addView(Landroid/view/View;ILandroid/view/ViewGroup$LayoutParams;)V");
    }

    public void addView(android.view.View arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->addView(Landroid/view/View;II)V");
    }

    /**
     * Child registry for the ported child-management surface. Named to
     * match the model key the interpreter syncs ({@code
     * ViewGroup;.mChildren}); the interpreter unbridges entries back to
     * their model objects after every mirror call, so old model-state
     * readers (window attach, attach propagation) keep working. Not
     * final: the interpreter swaps in the synced list object per call,
     * and between syncs the contents are a mirror/model mix.
     */
    private java.util.List<android.view.View> mChildren =
            new java.util.ArrayList<android.view.View>();

    /**
     * Attach child during an inflight layout pass. Composition attaches the
     * content root here; the params walk through the child's own
     * setLayoutParams (identity preserved), index -1 appends, and the
     * matched View entry keeps reference identity across mirror bridging.
     */
    protected boolean addViewInLayout(android.view.View arg0, int arg1,
            android.view.ViewGroup.LayoutParams arg2) {
        return addViewInLayout(arg0, arg1, arg2, false);
    }

    protected boolean addViewInLayout(android.view.View arg0, int arg1,
            android.view.ViewGroup.LayoutParams arg2, boolean arg3) {
        java.util.List<android.view.View> children = mChildren;
        // Ported from AOSP ViewGroup#addView-to-addViewInLayout flow: null
        // params falls back to generateDefaultLayoutParams() (the base
        // returns a WRAP_CONTENT pair, never null).
        arg0.setLayoutParams(arg2 == null ? generateDefaultLayoutParams() : arg2);
        arg0.mParent = this;
        if (mAttached != 0) {
            arg0.mAttached = 1;
        }
        if (arg1 >= 0 && arg1 < children.size()) {
            children.add(arg1, arg0);
        } else {
            children.add(arg0);
        }
        return true;
    }

    /**
     * Base addView family: same attach semantics as the inflight variant,
     * params null means the (null) base default, index -1 appends. The
     * (View, int) / (View, int, ...) index overloads route here on device;
     * their port stays UOE until a probe wall needs them.
     */

    protected void attachLayoutAnimationParameters(android.view.View arg0, android.view.ViewGroup.LayoutParams arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->attachLayoutAnimationParameters(Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;II)V");
    }

    protected void attachViewToParent(android.view.View arg0, int arg1, android.view.ViewGroup.LayoutParams arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->attachViewToParent(Landroid/view/View;ILandroid/view/ViewGroup$LayoutParams;)V");
    }

    public void bringChildToFront(android.view.View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->bringChildToFront(Landroid/view/View;)V");
    }

    protected boolean canAnimate() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->canAnimate()Z");
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams arg0) {
        // Base ViewGroup validation: params exist and belong to this family
        // (each container refines shape checks; bare containers accept any).
        return arg0 != null;
    }

    public void childDrawableStateChanged(android.view.View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->childDrawableStateChanged(Landroid/view/View;)V");
    }

    public void childHasTransientStateChanged(android.view.View arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->childHasTransientStateChanged(Landroid/view/View;Z)V");
    }

    protected void cleanupLayoutState(android.view.View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->cleanupLayoutState(Landroid/view/View;)V");
    }

    public void clearChildFocus(android.view.View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->clearChildFocus(Landroid/view/View;)V");
    }

    public void clearDisappearingChildren() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->clearDisappearingChildren()V");
    }

    public void clearFocus() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->clearFocus()V");
    }

    protected void debug(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->debug(I)V");
    }

    protected void detachAllViewsFromParent() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->detachAllViewsFromParent()V");
    }

    protected void detachViewFromParent(android.view.View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->detachViewFromParent(Landroid/view/View;)V");
    }

    protected void detachViewFromParent(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->detachViewFromParent(I)V");
    }

    protected void detachViewsFromParent(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->detachViewsFromParent(II)V");
    }

    public android.view.WindowInsets dispatchApplyWindowInsets(android.view.WindowInsets arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchApplyWindowInsets(Landroid/view/WindowInsets;)Landroid/view/WindowInsets;");
    }

    public boolean dispatchCapturedPointerEvent(android.view.MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchCapturedPointerEvent(Landroid/view/MotionEvent;)Z");
    }

    public void dispatchConfigurationChanged(android.content.res.Configuration arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchConfigurationChanged(Landroid/content/res/Configuration;)V");
    }

    public void dispatchCreateViewTranslationRequest(java.util.Map<android.view.autofill.AutofillId, long[]> arg0, int[] arg1, android.view.translation.TranslationCapability arg2, java.util.List<android.view.translation.ViewTranslationRequest> arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchCreateViewTranslationRequest(Ljava/util/Map;[ILandroid/view/translation/TranslationCapability;Ljava/util/List;)V");
    }

    public void dispatchDisplayHint(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchDisplayHint(I)V");
    }

    public boolean dispatchDragEvent(android.view.DragEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchDragEvent(Landroid/view/DragEvent;)Z");
    }

    /**
     * AOSP ViewGroup.java:282/286/302 draw-related group flags. Staging
     * carries only the two the draw chain reads; padding model is absent so
     * CLIP_TO_PADDING_MASK semantics reduce to explicit CLIP_TO_PADDING for
     * now (mGroupFlags defaults set at first dispatch — Unsafe-mirrors skip
     * field initializers, see syncFields note in Interpreter).
     */
    protected int mGroupFlags;

    // AOSP ViewGroup.java:282: FLAG_CLIP_CHILDREN = 0x1.
    static final int FLAG_CLIP_CHILDREN = 0x1;
    // AOSP ViewGroup.java:286: FLAG_CLIP_TO_PADDING = 0x2.
    static final int FLAG_CLIP_TO_PADDING = 0x2;

    /** Lazy initViewGroup() default for the flags the draw chain reads
     *  (AOSP ViewGroup.java:726-727). */
    int stagingGroupFlags() {
        if (mGroupFlags == 0) {
            mGroupFlags = FLAG_CLIP_CHILDREN | FLAG_CLIP_TO_PADDING;
        }
        return mGroupFlags;
    }

    /**
     * AOSP ViewGroup.java:4253-4382 staging reduction: no run-animation leg
     * (FLAG_RUN_ANIMATION unset), no transient/disappearing views (models
     * absent), no Z preordering (plain index order), children counted
     * VISIBLE (no mViewFlags model yet). The clipToPadding save is dropped
     * until the padding model lands (fresh groups have FLAG_PADDING_NOT_NULL
     * unset, AOSP:3999-4005, so on-device the clip does not happen either).
     */
    @Override
    protected void dispatchDraw(android.graphics.Canvas arg0) {
        final java.util.List<android.view.View> children = mChildren;
        final int childrenCount = children.size();
        int flags = stagingGroupFlags();
        boolean more = false;
        final long drawingTime = getDrawingTime();
        for (int i = 0; i < childrenCount; i++) {
            final android.view.View child = children.get(i);
            more |= drawChild(arg0, child, drawingTime);
        }
        // AOSP:4371-4376 FLAG_INVALIDATE_REQUIRED/animation-listener legs and
        // canvas.enableZ/disableZ stay inert without their models (base
        // Canvas enableZ/disableZ are no-ops, see those ports).
    }

    public void dispatchDrawableHotspotChanged(float arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchDrawableHotspotChanged(FF)V");
    }

    public void dispatchFinishTemporaryDetach() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchFinishTemporaryDetach()V");
    }

    protected void dispatchFreezeSelfOnly(android.util.SparseArray<android.os.Parcelable> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchFreezeSelfOnly(Landroid/util/SparseArray;)V");
    }

    protected boolean dispatchGenericFocusedEvent(android.view.MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchGenericFocusedEvent(Landroid/view/MotionEvent;)Z");
    }

    protected boolean dispatchGenericPointerEvent(android.view.MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchGenericPointerEvent(Landroid/view/MotionEvent;)Z");
    }

    protected boolean dispatchHoverEvent(android.view.MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchHoverEvent(Landroid/view/MotionEvent;)Z");
    }

    public boolean dispatchKeyEvent(android.view.KeyEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchKeyEvent(Landroid/view/KeyEvent;)Z");
    }

    public boolean dispatchKeyEventPreIme(android.view.KeyEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchKeyEventPreIme(Landroid/view/KeyEvent;)Z");
    }

    public boolean dispatchKeyShortcutEvent(android.view.KeyEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchKeyShortcutEvent(Landroid/view/KeyEvent;)Z");
    }

    public void dispatchPointerCaptureChanged(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchPointerCaptureChanged(Z)V");
    }

    public void dispatchProvideAutofillStructure(android.view.ViewStructure arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchProvideAutofillStructure(Landroid/view/ViewStructure;I)V");
    }

    public void dispatchProvideStructure(android.view.ViewStructure arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchProvideStructure(Landroid/view/ViewStructure;)V");
    }

    protected void dispatchRestoreInstanceState(android.util.SparseArray<android.os.Parcelable> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchRestoreInstanceState(Landroid/util/SparseArray;)V");
    }

    protected void dispatchSaveInstanceState(android.util.SparseArray<android.os.Parcelable> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchSaveInstanceState(Landroid/util/SparseArray;)V");
    }

    public void dispatchScrollCaptureSearch(android.graphics.Rect arg0, android.graphics.Point arg1, java.util.function.Consumer<android.view.ScrollCaptureTarget> arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchScrollCaptureSearch(Landroid/graphics/Rect;Landroid/graphics/Point;Ljava/util/function/Consumer;)V");
    }

    public void dispatchSetActivated(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchSetActivated(Z)V");
    }

    protected void dispatchSetPressed(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchSetPressed(Z)V");
    }

    public void dispatchSetSelected(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchSetSelected(Z)V");
    }

    public void dispatchStartTemporaryDetach() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchStartTemporaryDetach()V");
    }

    public void dispatchSystemUiVisibilityChanged(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchSystemUiVisibilityChanged(I)V");
    }

    protected void dispatchThawSelfOnly(android.util.SparseArray<android.os.Parcelable> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchThawSelfOnly(Landroid/util/SparseArray;)V");
    }

    public boolean dispatchTouchEvent(android.view.MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchTouchEvent(Landroid/view/MotionEvent;)Z");
    }

    public boolean dispatchTrackballEvent(android.view.MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchTrackballEvent(Landroid/view/MotionEvent;)Z");
    }

    public boolean dispatchUnhandledMove(android.view.View arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchUnhandledMove(Landroid/view/View;I)Z");
    }

    protected void dispatchVisibilityChanged(android.view.View arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchVisibilityChanged(Landroid/view/View;I)V");
    }

    public void dispatchWindowFocusChanged(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchWindowFocusChanged(Z)V");
    }

    public void dispatchWindowInsetsAnimationEnd(android.view.WindowInsetsAnimation arg0) {
        // AOSP ViewGroup.dispatchWindowInsetsAnimationEnd: notifies children
        // that an insets animation ended. No insets system runs on the
        // shell, so no-op (matches the interpreter's no-op builtin).
    }

    public void dispatchWindowInsetsAnimationPrepare(android.view.WindowInsetsAnimation arg0) {
        // AOSP ViewGroup.dispatchWindowInsetsAnimationPrepare: notifies
        // children that an insets animation is about to start. No-op on
        // the shell.
    }

    public android.view.WindowInsets dispatchWindowInsetsAnimationProgress(android.view.WindowInsets arg0,
            java.util.List<android.view.WindowInsetsAnimation> arg1) {
        // AOSP ViewGroup.dispatchWindowInsetsAnimationProgress: returns
        // updated insets during an animation. No animation runs on the
        // shell, so returns the input unchanged.
        return arg0;
    }

    public android.view.WindowInsetsAnimation.Bounds dispatchWindowInsetsAnimationStart(
            android.view.WindowInsetsAnimation arg0, android.view.WindowInsetsAnimation.Bounds arg1) {
        // AOSP ViewGroup.dispatchWindowInsetsAnimationStart: returns the
        // starting bounds for an insets animation. No animation runs on
        // the shell, so returns the input unchanged.
        return arg1;
    }

    public void dispatchWindowSystemUiVisiblityChanged(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchWindowSystemUiVisiblityChanged(I)V");
    }

    public void dispatchWindowVisibilityChanged(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->dispatchWindowVisibilityChanged(I)V");
    }

    /** AOSP ViewGroup.java:4567: drawChild forwards to the child's
     *  draw(canvas, parent, drawingTime). The 3-arg draw is virtual:
     *  guest overrides of {@code draw} run first via the VirtualCall seam
     *  (framework-in-Java (2) reverse bridge), then the framework software
     *  leg (View.draw(C,V,J)) runs for non-guest receivers. */
    protected boolean drawChild(android.graphics.Canvas arg0, android.view.View arg1, long arg2) {
        Object drawn = tartine.fw.VirtualCall.on(arg1, "draw",
                "(Landroid/graphics/Canvas;Landroid/view/ViewGroup;J)Z",
                new Object[]{arg0, this, arg2});
        if (drawn != tartine.fw.VirtualCall.NOT_GUEST) {
            return drawn instanceof Boolean ? (Boolean) drawn : false;
        }
        return arg1.draw(arg0, this, arg2);
    }

    protected void drawableStateChanged() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->drawableStateChanged()V");
    }

    public void endViewTransition(android.view.View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->endViewTransition(Landroid/view/View;)V");
    }

    public android.view.View findFocus() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->findFocus()Landroid/view/View;");
    }

    public android.window.OnBackInvokedDispatcher findOnBackInvokedDispatcherForChild(android.view.View arg0, android.view.View arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->findOnBackInvokedDispatcherForChild(Landroid/view/View;Landroid/view/View;)Landroid/window/OnBackInvokedDispatcher;");
    }

    public void findViewsWithText(java.util.ArrayList<android.view.View> arg0, java.lang.CharSequence arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->findViewsWithText(Ljava/util/ArrayList;Ljava/lang/CharSequence;I)V");
    }

    public android.view.View focusSearch(android.view.View arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->focusSearch(Landroid/view/View;I)Landroid/view/View;");
    }

    public void focusableViewAvailable(android.view.View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->focusableViewAvailable(Landroid/view/View;)V");
    }

    public boolean gatherTransparentRegion(android.graphics.Region arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->gatherTransparentRegion(Landroid/graphics/Region;)Z");
    }

    /**
     * Ported from aosp-mirror/platform_frameworks_base
     * core/java/android/view/ViewGroup.java ({@code
     * generateDefaultLayoutParams}): the base returns a WRAP_CONTENT pair,
     * not null. Containers override; their overrides port per wall.
     */
    protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new android.view.ViewGroup.LayoutParams(
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(android.util.AttributeSet arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->generateLayoutParams(Landroid/util/AttributeSet;)Landroid/view/ViewGroup$LayoutParams;");
    }

    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->generateLayoutParams(Landroid/view/ViewGroup$LayoutParams;)Landroid/view/ViewGroup$LayoutParams;");
    }

    public java.lang.CharSequence getAccessibilityClassName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->getAccessibilityClassName()Ljava/lang/CharSequence;");
    }

    /**
     * Ported verbatim from aosp-mirror/platform_frameworks_base
     * core/java/android/view/ViewGroup.java ({@code getChildAt}): out of
     * range indexes return {@code null}, matching the Javadoc; only the
     * ArrayList-backed inner helpers throw. androidx.activity's setContent
     * probes {@code content.getChildAt(0)} unguarded on a fresh window and
     * relies on the null return to take its create-new-ComposeView branch.
     */
    public android.view.View getChildAt(int arg0) {
        if (arg0 < 0 || arg0 >= mChildren.size()) {
            return null;
        }
        return mChildren.get(arg0);
    }

    public int getChildCount() {
        return mChildren.size();
    }

    public final int getChildDrawingOrder(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->getChildDrawingOrder(I)I");
    }

    protected int getChildDrawingOrder(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->getChildDrawingOrder(II)I");
    }

    public static int getChildMeasureSpec(int arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->getChildMeasureSpec(III)I");
    }

    protected boolean getChildStaticTransformation(android.view.View arg0, android.view.animation.Transformation arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->getChildStaticTransformation(Landroid/view/View;Landroid/view/animation/Transformation;)Z");
    }

    public boolean getChildVisibleRect(android.view.View arg0, android.graphics.Rect arg1, android.graphics.Point arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->getChildVisibleRect(Landroid/view/View;Landroid/graphics/Rect;Landroid/graphics/Point;)Z");
    }

    /** AOSP ViewGroup.getClipChildren (lines 4596-4598): reads the
     * staged mGroupFlags bit. */
    public boolean getClipChildren() {
        return (mGroupFlags & FLAG_CLIP_CHILDREN) != 0;
    }

    /** AOSP ViewGroup.getClipToPadding (lines 4654-4656): reads the
     * staged mGroupFlags bit. */
    public boolean getClipToPadding() {
        return (mGroupFlags & FLAG_CLIP_TO_PADDING) != 0;
    }

    public int getDescendantFocusability() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->getDescendantFocusability()I");
    }

    public android.view.View getFocusedChild() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->getFocusedChild()Landroid/view/View;");
    }

    public android.view.animation.LayoutAnimationController getLayoutAnimation() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->getLayoutAnimation()Landroid/view/animation/LayoutAnimationController;");
    }

    public android.view.animation.Animation.AnimationListener getLayoutAnimationListener() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->getLayoutAnimationListener()Landroid/view/animation/Animation$AnimationListener;");
    }

    public int getLayoutMode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->getLayoutMode()I");
    }

    public android.animation.LayoutTransition getLayoutTransition() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->getLayoutTransition()Landroid/animation/LayoutTransition;");
    }

    public int getNestedScrollAxes() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->getNestedScrollAxes()I");
    }

    public android.view.ViewGroupOverlay getOverlay() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->getOverlay()Landroid/view/ViewGroupOverlay;");
    }

    public int getPersistentDrawingCache() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->getPersistentDrawingCache()I");
    }

    public boolean getTouchscreenBlocksFocus() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->getTouchscreenBlocksFocus()Z");
    }

    public boolean hasFocus() {
        // AOSP ViewGroup inherits View.hasFocus() — no override on device.
        // The shell has no focus subsystem, so always false (matches the
        // interpreter's hasFocus builtin returning 0).
        return false;
    }

    public boolean hasTransientState() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->hasTransientState()Z");
    }

    public int indexOfChild(android.view.View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->indexOfChild(Landroid/view/View;)I");
    }

    public final void invalidateChild(android.view.View arg0, android.graphics.Rect arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->invalidateChild(Landroid/view/View;Landroid/graphics/Rect;)V");
    }

    public android.view.ViewParent invalidateChildInParent(int[] arg0, android.graphics.Rect arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->invalidateChildInParent([ILandroid/graphics/Rect;)Landroid/view/ViewParent;");
    }

    public boolean isAlwaysDrawnWithCacheEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->isAlwaysDrawnWithCacheEnabled()Z");
    }

    public boolean isAnimationCacheEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->isAnimationCacheEnabled()Z");
    }

    protected boolean isChildrenDrawingOrderEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->isChildrenDrawingOrderEnabled()Z");
    }

    protected boolean isChildrenDrawnWithCacheEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->isChildrenDrawnWithCacheEnabled()Z");
    }

    public boolean isLayoutSuppressed() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->isLayoutSuppressed()Z");
    }

    public boolean isMotionEventSplittingEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->isMotionEventSplittingEnabled()Z");
    }

    public boolean isTransitionGroup() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->isTransitionGroup()Z");
    }

    public void jumpDrawablesToCurrentState() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->jumpDrawablesToCurrentState()V");
    }

    public final void layout(int arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->layout(IIII)V");
    }

    protected void measureChild(android.view.View arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->measureChild(Landroid/view/View;II)V");
    }

    protected void measureChildWithMargins(android.view.View arg0, int arg1, int arg2, int arg3, int arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->measureChildWithMargins(Landroid/view/View;IIII)V");
    }

    protected void measureChildren(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->measureChildren(II)V");
    }

    public void notifySubtreeAccessibilityStateChanged(android.view.View arg0, android.view.View arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->notifySubtreeAccessibilityStateChanged(Landroid/view/View;Landroid/view/View;I)V");
    }

    public final void offsetDescendantRectToMyCoords(android.view.View arg0, android.graphics.Rect arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->offsetDescendantRectToMyCoords(Landroid/view/View;Landroid/graphics/Rect;)V");
    }

    public final void offsetRectIntoDescendantCoords(android.view.View arg0, android.graphics.Rect arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->offsetRectIntoDescendantCoords(Landroid/view/View;Landroid/graphics/Rect;)V");
    }

    protected void onAttachedToWindow() {
        // AOSP frameworks-base core/java/android/view/ViewGroup.java:5209-5212
        // onAttachedToWindow: super.onAttachedToWindow() then
        // clearCachedLayoutMode() (ViewGroup.java:5202-5206, resets
        // mLayoutMode when FLAG_LAYOUT_MODE_WAS_EXPLICITLY_SET is clear).
        // The shell carries no cached layout mode, so the reset is a no-op;
        // the View super is the shell no-op below (attachment is already
        // tracked through mAttachInfo).
    }

    protected int[] onCreateDrawableState(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->onCreateDrawableState(I)[I");
    }

    public void onDescendantInvalidated(android.view.View arg0, android.view.View arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->onDescendantInvalidated(Landroid/view/View;Landroid/view/View;)V");
    }

    protected void onDetachedFromWindow() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->onDetachedFromWindow()V");
    }

    public boolean onInterceptHoverEvent(android.view.MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->onInterceptHoverEvent(Landroid/view/MotionEvent;)Z");
    }

    public boolean onInterceptTouchEvent(android.view.MotionEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->onInterceptTouchEvent(Landroid/view/MotionEvent;)Z");
    }

    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->onLayout(ZIIII)V");
    }

    public boolean onNestedFling(android.view.View arg0, float arg1, float arg2, boolean arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->onNestedFling(Landroid/view/View;FFZ)Z");
    }

    public boolean onNestedPreFling(android.view.View arg0, float arg1, float arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->onNestedPreFling(Landroid/view/View;FF)Z");
    }

    public boolean onNestedPrePerformAccessibilityAction(android.view.View arg0, int arg1, android.os.Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->onNestedPrePerformAccessibilityAction(Landroid/view/View;ILandroid/os/Bundle;)Z");
    }

    public void onNestedPreScroll(android.view.View arg0, int arg1, int arg2, int[] arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->onNestedPreScroll(Landroid/view/View;II[I)V");
    }

    public void onNestedScroll(android.view.View arg0, int arg1, int arg2, int arg3, int arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->onNestedScroll(Landroid/view/View;IIII)V");
    }

    public void onNestedScrollAccepted(android.view.View arg0, android.view.View arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->onNestedScrollAccepted(Landroid/view/View;Landroid/view/View;I)V");
    }

    protected boolean onRequestFocusInDescendants(int arg0, android.graphics.Rect arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->onRequestFocusInDescendants(ILandroid/graphics/Rect;)Z");
    }

    public boolean onRequestSendAccessibilityEvent(android.view.View arg0, android.view.accessibility.AccessibilityEvent arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->onRequestSendAccessibilityEvent(Landroid/view/View;Landroid/view/accessibility/AccessibilityEvent;)Z");
    }

    public android.view.PointerIcon onResolvePointerIcon(android.view.MotionEvent arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->onResolvePointerIcon(Landroid/view/MotionEvent;I)Landroid/view/PointerIcon;");
    }

    public boolean onStartNestedScroll(android.view.View arg0, android.view.View arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->onStartNestedScroll(Landroid/view/View;Landroid/view/View;I)Z");
    }

    public void onStopNestedScroll(android.view.View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->onStopNestedScroll(Landroid/view/View;)V");
    }

    public void onViewAdded(android.view.View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->onViewAdded(Landroid/view/View;)V");
    }

    public void onViewRemoved(android.view.View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->onViewRemoved(Landroid/view/View;)V");
    }

    public void propagateRequestedFrameRate(float arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->propagateRequestedFrameRate(FZ)V");
    }

    public void recomputeViewAttributes(android.view.View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->recomputeViewAttributes(Landroid/view/View;)V");
    }

    public void removeAllViews() {
        for (Object child : mChildren) {
            if (child instanceof android.view.View) {
                ((android.view.View) child).mParent = null;
                ((android.view.View) child).mAttached = 0;
            }
        }
        mChildren.clear();
    }

    public void removeAllViewsInLayout() {
        for (Object child : mChildren) {
            if (child instanceof android.view.View) {
                ((android.view.View) child).mParent = null;
                ((android.view.View) child).mAttached = 0;
            }
        }
        mChildren.clear();
    }

    protected void removeDetachedView(android.view.View arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->removeDetachedView(Landroid/view/View;Z)V");
    }

    public void removeView(android.view.View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->removeView(Landroid/view/View;)V");
    }

    public void removeViewAt(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->removeViewAt(I)V");
    }

    public void removeViewInLayout(android.view.View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->removeViewInLayout(Landroid/view/View;)V");
    }

    public void removeViews(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->removeViews(II)V");
    }

    public void removeViewsInLayout(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->removeViewsInLayout(II)V");
    }

    public void requestChildFocus(android.view.View arg0, android.view.View arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->requestChildFocus(Landroid/view/View;Landroid/view/View;)V");
    }

    public boolean requestChildRectangleOnScreen(android.view.View arg0, android.graphics.Rect arg1, boolean arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->requestChildRectangleOnScreen(Landroid/view/View;Landroid/graphics/Rect;Z)Z");
    }

    public void requestDisallowInterceptTouchEvent(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->requestDisallowInterceptTouchEvent(Z)V");
    }

    public boolean requestFocus(int arg0, android.graphics.Rect arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->requestFocus(ILandroid/graphics/Rect;)Z");
    }

    public boolean requestSendAccessibilityEvent(android.view.View arg0, android.view.accessibility.AccessibilityEvent arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->requestSendAccessibilityEvent(Landroid/view/View;Landroid/view/accessibility/AccessibilityEvent;)Z");
    }

    public void requestTransparentRegion(android.view.View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->requestTransparentRegion(Landroid/view/View;)V");
    }

    public boolean restoreDefaultFocus() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->restoreDefaultFocus()Z");
    }

    public void scheduleLayoutAnimation() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->scheduleLayoutAnimation()V");
    }

    public void setAddStatesFromChildren(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->setAddStatesFromChildren(Z)V");
    }

    public void setAlwaysDrawnWithCacheEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->setAlwaysDrawnWithCacheEnabled(Z)V");
    }

    public void setAnimationCacheEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->setAnimationCacheEnabled(Z)V");
    }

    protected void setChildrenDrawingCacheEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->setChildrenDrawingCacheEnabled(Z)V");
    }

    protected void setChildrenDrawingOrderEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->setChildrenDrawingOrderEnabled(Z)V");
    }

    protected void setChildrenDrawnWithCacheEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->setChildrenDrawnWithCacheEnabled(Z)V");
    }

    /**
     * AOSP ViewGroup.setClipChildren (lines 4608-4620): the device compares
     * against the flag, updates via setBooleanFlag, syncs every child's
     * RenderNode clip bounds and invalidates; the shell keeps the flag
     * update (children render-node plumbing and invalidate land with the
     * draw chain).
     */
    public void setClipChildren(boolean arg0) {
        boolean previousValue = (mGroupFlags & FLAG_CLIP_CHILDREN) == FLAG_CLIP_CHILDREN;
        if (arg0 != previousValue) {
            if (arg0) {
                mGroupFlags |= FLAG_CLIP_CHILDREN;
            } else {
                mGroupFlags &= ~FLAG_CLIP_CHILDREN;
            }
        }
    }

    /**
     * AOSP ViewGroup.setClipToPadding (lines 4633-4638): flag compare + bit
     * update + invalidate(true); the shell keeps the flag update
     * (invalidate rides the draw chain).
     */
    public void setClipToPadding(boolean arg0) {
        boolean previousValue = (mGroupFlags & FLAG_CLIP_TO_PADDING) == FLAG_CLIP_TO_PADDING;
        if (arg0 != previousValue) {
            if (arg0) {
                mGroupFlags |= FLAG_CLIP_TO_PADDING;
            } else {
                mGroupFlags &= ~FLAG_CLIP_TO_PADDING;
            }
        }
    }

    public void setDescendantFocusability(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->setDescendantFocusability(I)V");
    }

    public void setLayoutAnimation(android.view.animation.LayoutAnimationController arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->setLayoutAnimation(Landroid/view/animation/LayoutAnimationController;)V");
    }

    public void setLayoutAnimationListener(android.view.animation.Animation.AnimationListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->setLayoutAnimationListener(Landroid/view/animation/Animation$AnimationListener;)V");
    }

    public void setLayoutMode(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->setLayoutMode(I)V");
    }

    public void setLayoutTransition(android.animation.LayoutTransition arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->setLayoutTransition(Landroid/animation/LayoutTransition;)V");
    }

    public void setMotionEventSplittingEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->setMotionEventSplittingEnabled(Z)V");
    }

    public void setOnHierarchyChangeListener(android.view.ViewGroup.OnHierarchyChangeListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->setOnHierarchyChangeListener(Landroid/view/ViewGroup$OnHierarchyChangeListener;)V");
    }

    public void setPersistentDrawingCache(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->setPersistentDrawingCache(I)V");
    }

    public void setRequestedFrameRate(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->setRequestedFrameRate(F)V");
    }

    protected void setStaticTransformationsEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->setStaticTransformationsEnabled(Z)V");
    }

    public void setTouchscreenBlocksFocus(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->setTouchscreenBlocksFocus(Z)V");
    }

    public void setTransitionGroup(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->setTransitionGroup(Z)V");
    }

    public void setWindowInsetsAnimationCallback(android.view.WindowInsetsAnimation.Callback arg0) {
        // AOSP ViewGroup.setWindowInsetsAnimationCallback: sets the callback
        // for insets animation events. No insets system on the shell, so
        // no-op (matches the interpreter's no-op builtin).
    }

    public boolean shouldDelayChildPressedState() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->shouldDelayChildPressedState()Z");
    }

    public boolean showContextMenuForChild(android.view.View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->showContextMenuForChild(Landroid/view/View;)Z");
    }

    public boolean showContextMenuForChild(android.view.View arg0, float arg1, float arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->showContextMenuForChild(Landroid/view/View;FF)Z");
    }

    public android.view.ActionMode startActionModeForChild(android.view.View arg0, android.view.ActionMode.Callback arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->startActionModeForChild(Landroid/view/View;Landroid/view/ActionMode$Callback;)Landroid/view/ActionMode;");
    }

    public android.view.ActionMode startActionModeForChild(android.view.View arg0, android.view.ActionMode.Callback arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->startActionModeForChild(Landroid/view/View;Landroid/view/ActionMode$Callback;I)Landroid/view/ActionMode;");
    }

    public void startLayoutAnimation() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->startLayoutAnimation()V");
    }

    public void startViewTransition(android.view.View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->startViewTransition(Landroid/view/View;)V");
    }

    public void suppressLayout(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->suppressLayout(Z)V");
    }

    public void updateViewLayout(android.view.View arg0, android.view.ViewGroup.LayoutParams arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup;->updateViewLayout(Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public ViewGroup() {
        }
        public static class LayoutParams {

        public static final int FILL_PARENT = -1;
        public static final int MATCH_PARENT = -1;
        public static final int WRAP_CONTENT = -2;
        public int height;
        public android.view.animation.LayoutAnimationController.AnimationParameters layoutAnimationParameters;
        public int width;

        public LayoutParams(android.content.Context arg0, android.util.AttributeSet arg1) {
            // Real Android inflates width/height from XML attrs; nothing
            // reads XML layouts yet, so the default pair applies until
            // attribute parsing ports.
            width = WRAP_CONTENT;
            height = WRAP_CONTENT;
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams arg0) {
            width = arg0.width;
            height = arg0.height;
        }

        public LayoutParams(int arg0, int arg1) {
            width = arg0;
            height = arg1;
        }

        public void resolveLayoutDirection(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup$LayoutParams;->resolveLayoutDirection(I)V");
        }

        protected void setBaseAttributes(android.content.res.TypedArray arg0, int arg1, int arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewGroup$LayoutParams;->setBaseAttributes(Landroid/content/res/TypedArray;II)V");
        }

            /** Scratch mirror constructor (SkeletonGen). */
            public LayoutParams() {
            }
        }
        public static interface OnHierarchyChangeListener {

        public abstract void onChildViewAdded(android.view.View arg0, android.view.View arg1);

        public abstract void onChildViewRemoved(android.view.View arg0, android.view.View arg1);
        }
    }
