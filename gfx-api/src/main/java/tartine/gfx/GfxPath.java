package tartine.gfx;

/**
 * Immutable path geometry for the canvas seam.
 *
 * <p>One verb stream plus its float coordinates and a fill rule, mirroring
 * Skia's path verbs without leaking a renderer type. Each verb consumes a
 * fixed number of coordinates: {@link #MOVE}/{@link #LINE} two,
 * {@link #QUAD} four, {@link #CUBIC} six, {@link #CLOSE} none. The framework's
 * {@code android.graphics.Path} produces it from its geometry model; a
 * backend replays it against its native path.
 */
public final class GfxPath {

    /** Starts a new contour at (x, y). */
    public static final int MOVE = 0;
    /** Adds a line to (x, y). */
    public static final int LINE = 1;
    /** Adds a quadratic curve with control (x1, y1) to (x2, y2). */
    public static final int QUAD = 2;
    /** Adds a cubic curve with controls (x1, y1), (x2, y2) to (x3, y3). */
    public static final int CUBIC = 3;
    /** Closes the current contour. */
    public static final int CLOSE = 4;

    /** Fill rules, matching {@code android.graphics.Path.FillType}. */
    public enum FillMode {
        WINDING, EVEN_ODD, INVERSE_WINDING, INVERSE_EVEN_ODD
    }

    /** Verb codes in path order. */
    public final int[] verbs;
    /** Coordinates, verb by verb. */
    public final float[] points;
    /** Fill rule applied to the whole path. */
    public final FillMode fillMode;

    /**
     * Copies the arrays so a caller can reuse its buffers.
     *
     * @param verbs the verb stream
     * @param points the coordinate stream
     * @param fillMode the fill rule, or {@link FillMode#WINDING} when null
     */
    public GfxPath(int[] verbs, float[] points, FillMode fillMode) {
        this.verbs = verbs.clone();
        this.points = points.clone();
        this.fillMode = fillMode == null ? FillMode.WINDING : fillMode;
    }
}
