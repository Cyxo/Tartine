package android.graphics;

    public class Outline {

    // Ported from aosp-mirror/platform_frameworks_base
    // graphics/java/android/graphics/Outline.java (constants, fields, the
    // empty Outline() ctor and the rectangle/path setters). AOSP's mRect is
    // final and eagerly created; the shell mirrors that now that Rect has
    // real bodies. Hand-fields are non-final so mirror/model field sync can
    // write them (keys Outline;.mMode / Outline;.mRect / Outline;.mRadius /
    // Outline;.mAlpha).

    private static final float RADIUS_UNDEFINED = Float.NEGATIVE_INFINITY;

    public static final int MODE_EMPTY = 0;
    public static final int MODE_ROUND_RECT = 1;
    public static final int MODE_PATH = 2;

    public int mMode = MODE_EMPTY;

    /** Only guaranteed to be non-null when mode == MODE_PATH. */
    public Path mPath;

    public Rect mRect = new Rect();

    public float mRadius = RADIUS_UNDEFINED;

    /**{@code setAlpha}/{@code getAlpha} backing field, defaults to 0. */
    public float mAlpha;

    /** AOSP Outline() is an empty body: the fields carry the empty state. */
    public Outline() {
    }

    public Outline(Outline arg0) {
        set(arg0);
    }

    /**
     * Ported from AOSP {@code Outline#canClip}: only a path outline cannot
     * be clipped by a rectangle.
     */
    public boolean canClip() {
        return mMode != MODE_PATH;
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
        return mRadius;
    }

    public boolean getRect(Rect outRect) {
        if (mMode != MODE_ROUND_RECT) {
            return false;
        }
        outRect.set(mRect);
        return true;
    }

    public boolean isEmpty() {
        // Ported from aosp-mirror/platform_frameworks_base
        // graphics/java/android/graphics/Outline.java (isEmpty): mode check.
        return mMode == MODE_EMPTY;
    }

    /** Ported from AOSP {@code Outline#offset}. */
    public void offset(int dx, int dy) {
        if (mMode == MODE_ROUND_RECT) {
            mRect.offset(dx, dy);
        } else if (mMode == MODE_PATH) {
            mPath.offset(dx, dy);
        }
    }

    /** Ported from AOSP {@code Outline#set(Outline src)}. */
    public void set(Outline src) {
        if (src.mMode == MODE_PATH) {
            if (mPath == null) {
                mPath = new Path();
            }
            mPath.set(src.mPath);
        } else {
            mRect.set(src.mRect);
        }
        mMode = src.mMode;
        mRadius = src.mRadius;
        mAlpha = src.mAlpha;
    }

    /** Ported verbatim from AOSP {@code Outline#setAlpha} (same file). */
    public void setAlpha(float arg0) {
        mAlpha = arg0;
    }

    /** Ported from AOSP {@code Outline#setConvexPath}. */
    public void setConvexPath(Path convexPath) {
        if (convexPath.isEmpty()) {
            setEmpty();
            return;
        }
        if (!convexPath.isConvex()) {
            throw new IllegalArgumentException("path must be convex");
        }
        if (mPath == null) {
            mPath = new Path();
        }
        mMode = MODE_PATH;
        mPath.set(convexPath);
    }

    /**
     * Ported from aosp-mirror/platform_frameworks_base
     * graphics/java/android/graphics/Outline.java (setEmpty): mPath rewind
     * plus mode/radius/rect/alpha reset.
     */
    public void setEmpty() {
        if (mPath != null) {
            mPath.rewind();
        }
        mMode = MODE_EMPTY;
        mRect.setEmpty();
        mRadius = RADIUS_UNDEFINED;
        mAlpha = 0;
    }

    /** Ported from AOSP {@code Outline#setOval(Rect)}. */
    public void setOval(Rect rect) {
        setOval(rect.left, rect.top, rect.right, rect.bottom);
    }

    /**
     * Ported from AOSP {@code Outline#setOval(int, int, int, int)}: a circle
     * stays a round rect, any other oval becomes a path.
     */
    public void setOval(int left, int top, int right, int bottom) {
        if (left >= right || top >= bottom) {
            setEmpty();
            return;
        }
        if ((bottom - top) == (right - left)) {
            setRoundRect(left, top, right, bottom,
                    (bottom - top) / 2.0f);
        } else {
            if (mPath == null) {
                mPath = new Path();
            }
            mPath.reset();
            mPath.addOval(left, top, right, bottom, Path.Direction.CW);
            mMode = MODE_PATH;
        }
    }

    /** Ported from AOSP {@code Outline#setPath}. */
    public void setPath(Path path) {
        if (mPath == null) {
            mPath = new Path();
        }
        mPath.set(path);
        mMode = MODE_PATH;
    }

    /** Ported from AOSP {@code Outline#setRect(Rect)}. */
    public void setRect(Rect rect) {
        setRect(rect.left, rect.top, rect.right, rect.bottom);
    }

    /** Ported from AOSP {@code Outline#setRect(int, int, int, int)}. */
    public void setRect(int left, int top, int right, int bottom) {
        setRoundRect(left, top, right, bottom, 0.0f);
    }

    /** Ported from AOSP {@code Outline#setRoundRect(Rect, float)}. */
    public void setRoundRect(Rect rect, float radius) {
        setRoundRect(rect.left, rect.top, rect.right, rect.bottom, radius);
    }

    /** Ported from AOSP {@code Outline#setRoundRect(int, int, int, int, float)}. */
    public void setRoundRect(int left, int top, int right, int bottom,
            float radius) {
        if (left >= right || top >= bottom) {
            setEmpty();
            return;
        }
        mMode = MODE_ROUND_RECT;
        mRect.set(left, top, right, bottom);
        mRadius = radius;
    }
    }
