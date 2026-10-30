package android.graphics;

    public class Outline {

    // Ported from aosp-mirror/platform_frameworks_base
    // graphics/java/android/graphics/Outline.java (constants, fields, and
    // the empty Outline() ctor). mRect stays null until the module Rect
    // gains real bodies: AOSP eagerly inits mRect = new Rect() (final, never
    // null on device) but our module Rect() is still a gap stub. set()'s
    // mPath/mRect legs land with that port too. Hand-fields are non-final
    // so mirror/model field sync can write them (keys Outline;.mMode /
    // Outline;.mRect / Outline;.mRadius / Outline;.mAlpha).

    private static final float RADIUS_UNDEFINED = Float.NEGATIVE_INFINITY;

    public static final int MODE_EMPTY = 0;
    public static final int MODE_ROUND_RECT = 1;
    public static final int MODE_PATH = 2;

    public int mMode = MODE_EMPTY;

    /** Only guaranteed to be non-null when mode == MODE_PATH. */
    public Path mPath;

    public Rect mRect;

    public float mRadius = RADIUS_UNDEFINED;

    /**{@code setAlpha}/{@code getAlpha} backing field, defaults to 0. */
    public float mAlpha;

    /** AOSP Outline() is an empty body: the fields carry the empty state. */
    public Outline() {
    }

    public Outline(Outline arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Outline;-><init>(Landroid/graphics/Outline;)V");
    }

    public boolean canClip() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Outline;->canClip()Z");
    }

    /**
     * Ported from aosp-mirror/platform_frameworks_base
     * graphics/java/android/graphics/Outline.java ({@code setAlpha}):
     * plain field assignment, no clamping on the AOSP side.
     */
    public float getAlpha() {
        return mAlpha;
    }

    public float getRadius() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Outline;->getRadius()F");
    }

    public boolean getRect(Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Outline;->getRect(Landroid/graphics/Rect;)Z");
    }

    public boolean isEmpty() {
        // Ported from aosp-mirror/platform_frameworks_base
        // graphics/java/android/graphics/Outline.java (isEmpty): mode check.
        return mMode == MODE_EMPTY;
    }

    public void offset(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Outline;->offset(II)V");
    }

    public void set(Outline arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Outline;->set(Landroid/graphics/Outline;)V");
    }

    /** Ported verbatim from AOSP {@code Outline#setAlpha} (same file). */
    public void setAlpha(float arg0) {
        mAlpha = arg0;
    }

    public void setConvexPath(Path arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Outline;->setConvexPath(Landroid/graphics/Path;)V");
    }

    /**
     * Ported from aosp-mirror/platform_frameworks_base
     * graphics/java/android/graphics/Outline.java (setEmpty): mPath rewind
     * plus mode/radius reset; on-device mRect.setEmpty() runs too, but our
     * mRect is null pending the module Rect port, so that leg is gated.
     */
    public void setEmpty() {
        if (mPath != null) {
            mPath.rewind();
        }
        mMode = MODE_EMPTY;
        mRadius = RADIUS_UNDEFINED;
    }

    public void setOval(Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Outline;->setOval(Landroid/graphics/Rect;)V");
    }

    public void setOval(int arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Outline;->setOval(IIII)V");
    }

    public void setPath(Path arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Outline;->setPath(Landroid/graphics/Path;)V");
    }

    public void setRect(Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Outline;->setRect(Landroid/graphics/Rect;)V");
    }

    public void setRect(int arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Outline;->setRect(IIII)V");
    }

    public void setRoundRect(Rect arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Outline;->setRoundRect(Landroid/graphics/Rect;F)V");
    }

    public void setRoundRect(int arg0, int arg1, int arg2, int arg3, float arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Outline;->setRoundRect(IIIIF)V");
    }
    }
