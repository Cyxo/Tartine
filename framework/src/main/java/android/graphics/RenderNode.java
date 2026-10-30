package android.graphics;

import tartine.gfx.GfxPicture;

    public class RenderNode {

    // Pivot state ported from aosp-mirror/platform_frameworks_base
    // graphics/java/android/graphics/RenderNode.java: on device these live
    // on the native side (nSetPivotX/nGetPivotX...); until a draw pipeline
    // exists the Java surface substitutes plain host fields with matching
    // semantics (setPivotX/setPivotY mark the pivot explicit and return
    // whether the value changed).

    protected float mPivotX;

    protected float mPivotY;

    protected boolean mPivotExplicitlySet;

    // Position state ported from the same file: on device the position
    // lives native-side (nGetLeft.../nSetLeftTopRightBottom); until a draw
    // pipeline exists these ints substitute for it. RenderingPosition
    // setters mark nothing (no invalidation machinery yet) and return
    // changed, matching the native boolean contracts.

    protected int mLeft;

    protected int mTop;

    protected int mRight;

    protected int mBottom;

    /** Substitutes nSetClipRect state (no read-back API used yet). */
    protected Rect mClipRectState;

    /**
     * Transform state ported from the same file: native-backed on device
     * (nSetElevation.../nGetRotationX...). Defaults match the identity
     * transform the native node starts with (scales 1.0, the rest 0);
     * mCameraDistance carries the View-documented 326.4 default instead of
     * the native -326.4 (we store positive pixels at native scale; the
     * sign flip lives in the getters/setters like the AOSP Java class).
     */

    protected float mElevation;

    protected float mTranslationX;

    protected float mTranslationY;

    protected float mTranslationZ;

    protected float mRotationX;

    protected float mRotationY;

    protected float mRotationZ;

    protected float mScaleX = 1.0f;

    protected float mScaleY = 1.0f;

    protected float mCameraDistance = -326.4f;

    /*
     * Recording state (AOSP RenderNode.java:485-526): the RecordingCanvas in
     * progress plus the finished recorded picture that drawRenderNode
     * replays.
     */
    RecordingCanvas mCurrentRecordingCanvas;
    GfxPicture tartinePicture;

    /** The sealed recorded picture, replayed by Canvas#drawRenderNode. */
    GfxPicture tartinePicture() {
        return tartinePicture;
    }

    void tartineSetPicture(GfxPicture picture) {
        this.tartinePicture = picture;
    }

    /** AOSP ctor keeps the name on the native side only; body is empty. */
    public RenderNode(String arg0) {
    }

    public RecordingCanvas beginRecording() {
        // AOSP RenderNode.java:505-507: records at the node's own size.
        return beginRecording(getWidth(), getHeight());
    }

    public RecordingCanvas beginRecording(int arg0, int arg1) {
        // AOSP RenderNode.java:485-492: one recording at a time.
        if (mCurrentRecordingCanvas != null) {
            throw new IllegalStateException(
                    "Recording currently in progress - missing #endRecording() call?");
        }
        mCurrentRecordingCanvas = new RecordingCanvas(arg0, arg1);
        return mCurrentRecordingCanvas;
    }

    public long computeApproximateMemoryUsage() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RenderNode;->computeApproximateMemoryUsage()J");
    }

    public void discardDisplayList() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RenderNode;->discardDisplayList()V");
    }

    public void endRecording() {
        // AOSP RenderNode.java:517-526: seal and recycle; null state after.
        if (mCurrentRecordingCanvas == null) {
            throw new IllegalStateException(
                    "No recording in progress, forgot to call #beginRecording()?");
        }
        Canvas canvas = mCurrentRecordingCanvas;
        mCurrentRecordingCanvas = null;
        if (canvas instanceof RecordingCanvas) {
            ((RecordingCanvas) canvas).finishRecording(this);
        }
    }

    public float getAlpha() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RenderNode;->getAlpha()F");
    }

    public int getAmbientShadowColor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RenderNode;->getAmbientShadowColor()I");
    }

    /** Ported from AOSP {@code RenderNode#getBottom} (native-backed there). */
    public int getBottom() {
        return mBottom;
    }

    /**
     * Ported from aosp-mirror/platform_frameworks_base
     * graphics/java/android/graphics/RenderNode.java (getCameraDistance):
     * finite-positive guard lives on the setter; the stored value keeps the
     * native sign (getters negate). Default flips the View-documented
     * 326.4f so the pair round-trips the AOSP Java contract.
     */
    public float getCameraDistance() {
        return -mCameraDistance;
    }

    public boolean getClipToBounds() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RenderNode;->getClipToBounds()Z");
    }

    /**
     * Ported from aosp-mirror/platform_frameworks_base
     * graphics/java/android/graphics/RenderNode.java
     * (getClipToOutline/setClipToOutline): native-backed there; plain host
     * field substitutes until a draw pipeline exists (set returns whether
     * the value changed, per nSetClipToOutline).
     */
    protected boolean mClipToOutline;

    public boolean getClipToOutline() {
        return mClipToOutline;
    }

    /** Ported from AOSP {@code RenderNode#getElevation} (native there). */
    public float getElevation() {
        return mElevation;
    }

    public int getHeight() {
        // AOSP RenderNode.java:1477-1483: height is bottom - top.
        return mBottom - mTop;
    }

    public void getInverseMatrix(Matrix arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RenderNode;->getInverseMatrix(Landroid/graphics/Matrix;)V");
    }

    /** Ported from AOSP {@code RenderNode#getLeft} (native-backed there). */
    public int getLeft() {
        return mLeft;
    }

    public void getMatrix(Matrix arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RenderNode;->getMatrix(Landroid/graphics/Matrix;)V");
    }

    /** Ported from AOSP {@code RenderNode#getPivotX} (same file). */
    public float getPivotX() {
        return mPivotX;
    }

    public float getPivotY() {
        return mPivotY;
    }

    /** Ported from AOSP {@code RenderNode#getRight} (native-backed there). */
    public int getRight() {
        return mRight;
    }

    /** Ported from AOSP {@code RenderNode#getRotationX} (native there). */
    public float getRotationX() {
        return mRotationX;
    }

    public float getRotationY() {
        return mRotationY;
    }

    public float getRotationZ() {
        return mRotationZ;
    }

    /** Ported from AOSP {@code RenderNode#getScaleX} (identity 1 default). */
    public float getScaleX() {
        return mScaleX;
    }

    public float getScaleY() {
        return mScaleY;
    }

    public int getSpotShadowColor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RenderNode;->getSpotShadowColor()I");
    }

    /** Ported from AOSP {@code RenderNode#getTop} (native-backed there). */
    public int getTop() {
        return mTop;
    }

    public float getTranslationX() {
        // AOSP RenderNode.java:1131 (nGetTranslationX native there; plain
        // field substitute here, see fields note).
        return mTranslationX;
    }

    public float getTranslationY() {
        // AOSP RenderNode.java:1152 (nGetTranslationY; plain field).
        return mTranslationY;
    }

    public float getTranslationZ() {
        // AOSP RenderNode.java translated-Z leg (nGetTranslationZ native
        // there; plain field substitute).
        return mTranslationZ;
    }

    public long getUniqueId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RenderNode;->getUniqueId()J");
    }

    public boolean getUseCompositingLayer() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RenderNode;->getUseCompositingLayer()Z");
    }

    public int getWidth() {
        // AOSP RenderNode.java:1468-1475: width is right - left.
        return mRight - mLeft;
    }

    public boolean hasDisplayList() {
        // AOSP RenderNode.java:573-575 (nIsValid): a valid picture marks the
        // display list.
        return tartinePicture != null;
    }

    public boolean hasIdentityMatrix() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RenderNode;->hasIdentityMatrix()Z");
    }

    public boolean hasOverlappingRendering() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RenderNode;->hasOverlappingRendering()Z");
    }

    public boolean hasShadow() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RenderNode;->hasShadow()Z");
    }

    public boolean isForceDarkAllowed() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RenderNode;->isForceDarkAllowed()Z");
    }

    /**
     * Ported from aosp-mirror/platform_frameworks_base
     * graphics/java/android/graphics/RenderNode.java (isPivotExplicitlySet):
     * false until a setPivotX/setPivotY runs; resetPivot pending.
     */
    public boolean isPivotExplicitlySet() {
        return mPivotExplicitlySet;
    }

    public boolean offsetLeftAndRight(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RenderNode;->offsetLeftAndRight(I)Z");
    }

    public boolean offsetTopAndBottom(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RenderNode;->offsetTopAndBottom(I)Z");
    }

    public boolean resetPivot() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RenderNode;->resetPivot()Z");
    }

    public boolean setAlpha(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RenderNode;->setAlpha(F)Z");
    }

    public boolean setAmbientShadowColor(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RenderNode;->setAmbientShadowColor(I)Z");
    }

    /** Ported from AOSP {@code RenderNode#setCameraDistance} semantics. */
    public boolean setCameraDistance(float arg0) {
        if (!Float.isFinite(arg0) || arg0 < 0.0f) {
            throw new IllegalArgumentException("distance must be finite & positive, given="
                    + arg0);
        }
        boolean changed = arg0 != -mCameraDistance;
        mCameraDistance = -arg0;
        return changed;
    }

    /**
     * Ported from aosp-mirror/platform_frameworks_base
     * graphics/java/android/graphics/RenderNode.java (setClipRect):
     * native-backed there (nSetClipRect); substitutes a stored Rect and
     * returns whether the bounds changed.
     */
    public boolean setClipRect(Rect arg0) {
        boolean changed;
        if (mClipRectState == null ? arg0 != null
                : !mClipRectState.equals(arg0)) {
            changed = true;
        } else {
            changed = false;
        }
        if (arg0 != null) {
            if (mClipRectState == null) {
                mClipRectState = new Rect(arg0);
            } else {
                mClipRectState.set(arg0);
            }
        } else {
            mClipRectState = null;
        }
        return changed;
    }

    public boolean setClipToBounds(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RenderNode;->setClipToBounds(Z)Z");
    }

    /** Ported from AOSP {@code RenderNode#setClipToOutline} semantics. */
    public boolean setClipToOutline(boolean arg0) {
        boolean changed = mClipToOutline != arg0;
        mClipToOutline = arg0;
        return changed;
    }

    /** Ported from AOSP {@code RenderNode#setElevation} semantics. */
    public boolean setElevation(float arg0) {
        boolean changed = arg0 != mElevation;
        mElevation = arg0;
        return changed;
    }

    public boolean setForceDarkAllowed(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RenderNode;->setForceDarkAllowed(Z)Z");
    }

    public boolean setHasOverlappingRendering(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RenderNode;->setHasOverlappingRendering(Z)Z");
    }

    public boolean setOutline(Outline arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RenderNode;->setOutline(Landroid/graphics/Outline;)Z");
    }

    /**
     * Ported from aosp-mirror/platform_frameworks_base
     * graphics/java/android/graphics/RenderNode.java (setPivotX): marks the
     * pivot explicit and returns whether the value changed (native
     * nSetPivotX semantics).
     */
    public boolean setPivotX(float arg0) {
        boolean changed = arg0 != mPivotX;
        mPivotX = arg0;
        mPivotExplicitlySet = true;
        return changed;
    }

    public boolean setPivotY(float arg0) {
        boolean changed = arg0 != mPivotY;
        mPivotY = arg0;
        mPivotExplicitlySet = true;
        return changed;
    }

    /**
     * Ported from aosp-mirror/platform_frameworks_base
     * graphics/java/android/graphics/RenderNode.java (setPosition(Rect) /
     * setPosition(IIII) / setLeftTopRightBottom): all three delegate to the
     * native setLeftTopRightBottom on device; here one plain-core setter
     * backs them and returns whether the bounds changed.
     */
    public boolean setPosition(Rect arg0) {
        return setLeftTopRightBottomCore(arg0.left, arg0.top, arg0.right, arg0.bottom);
    }

    public boolean setPosition(int arg0, int arg1, int arg2, int arg3) {
        return setLeftTopRightBottomCore(arg0, arg1, arg2, arg3);
    }

    public boolean setLeftTopRightBottom(int arg0, int arg1, int arg2, int arg3) {
        return setLeftTopRightBottomCore(arg0, arg1, arg2, arg3);
    }

    private boolean setLeftTopRightBottomCore(int left, int top, int right, int bottom) {
        boolean changed = mLeft != left || mTop != top
                || mRight != right || mBottom != bottom;
        mLeft = left;
        mTop = top;
        mRight = right;
        mBottom = bottom;
        return changed;
    }

    public boolean setProjectBackwards(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RenderNode;->setProjectBackwards(Z)Z");
    }

    public boolean setProjectionReceiver(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RenderNode;->setProjectionReceiver(Z)Z");
    }

    public boolean setRenderEffect(RenderEffect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RenderNode;->setRenderEffect(Landroid/graphics/RenderEffect;)Z");
    }

    /** Ported from AOSP {@code RenderNode#setRotationX} semantics. */
    public boolean setRotationX(float arg0) {
        boolean changed = arg0 != mRotationX;
        mRotationX = arg0;
        return changed;
    }

    public boolean setRotationY(float arg0) {
        boolean changed = arg0 != mRotationY;
        mRotationY = arg0;
        return changed;
    }

    public boolean setRotationZ(float arg0) {
        boolean changed = arg0 != mRotationZ;
        mRotationZ = arg0;
        return changed;
    }

    public boolean setScaleX(float arg0) {
        boolean changed = arg0 != mScaleX;
        mScaleX = arg0;
        return changed;
    }

    public boolean setScaleY(float arg0) {
        boolean changed = arg0 != mScaleY;
        mScaleY = arg0;
        return changed;
    }

    public boolean setSpotShadowColor(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RenderNode;->setSpotShadowColor(I)Z");
    }

    public boolean setTranslationX(float arg0) {
        boolean changed = arg0 != mTranslationX;
        mTranslationX = arg0;
        return changed;
    }

    public boolean setTranslationY(float arg0) {
        boolean changed = arg0 != mTranslationY;
        mTranslationY = arg0;
        return changed;
    }

    public boolean setTranslationZ(float arg0) {
        boolean changed = arg0 != mTranslationZ;
        mTranslationZ = arg0;
        return changed;
    }

    public boolean setUseCompositingLayer(boolean arg0, Paint arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RenderNode;->setUseCompositingLayer(ZLandroid/graphics/Paint;)Z");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public RenderNode() {
        }
    }
