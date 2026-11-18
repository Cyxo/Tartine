package android.graphics;

import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import tartine.gfx.GfxPath;

/**
 * AOSP {@code android.graphics.Path}: a geometry container of lines, curves
 * and contours.
 *
 * <p>Skia owns the native path on a device; the desktop shell models the same
 * geometry with {@link Path2D}, which gives move/line/quad/cubic verbs,
 * elliptical arcs, boolean operations ({@link Area}) and affine transforms.
 * The model is renderer-neutral: the renderer seam receives an immutable
 * {@link GfxPath} snapshot through {@code Canvas.drawPath}, so the framework
 * still never imports a renderer library.
 *
 * <p>Deviations (noted at each site): {@code computeBounds} reports the
 * control-point bounds (Skia's conservative mode) rather than solving curve
 * extrema, {@code isConvex} answers false conservatively, and contour
 * direction hints are ignored because a path's fill rule makes them
 * immaterial for the software model.
 */
public class Path {

    private Path2D.Double path = new Path2D.Double(Path2D.WIND_NON_ZERO);
    private FillType fillType = FillType.WINDING;
    private boolean inverseFillType;
    private int generationId;

    public Path() {
        // AOSP Path(): an empty path with the WINDING fill type.
    }

    public Path(Path arg0) {
        set(arg0);
    }

    public void addArc(RectF arg0, float arg1, float arg2) {
        arcTo(arg0, arg1, arg2, true);
    }

    public void addArc(float arg0, float arg1, float arg2, float arg3,
            float arg4, float arg5) {
        arcTo(arg0, arg1, arg2, arg3, arg4, arg5, true);
    }

    public void addCircle(float arg0, float arg1, float arg2,
            Path.Direction arg3) {
        // AOSP addCircle: a new contour; the direction hint is immaterial
        // for a single convex contour (see the class deviation note).
        Ellipse2D.Double oval = new Ellipse2D.Double(arg0 - arg2, arg1 - arg2,
                arg2 * 2.0, arg2 * 2.0);
        append(oval, false);
    }

    public void addOval(RectF arg0, Path.Direction arg1) {
        appendOval(arg0.left, arg0.top, arg0.right, arg0.bottom);
    }

    public void addOval(float arg0, float arg1, float arg2, float arg3,
            Path.Direction arg4) {
        appendOval(arg0, arg1, arg2, arg3);
    }

    private void appendOval(float left, float top, float right, float bottom) {
        append(new Ellipse2D.Double(left, top, right - left, bottom - top),
                false);
    }

    public void addPath(Path arg0) {
        path.append(arg0.path, false);
        changed();
    }

    public void addPath(Path arg0, Matrix arg1) {
        if (arg1 == null) {
            addPath(arg0);
            return;
        }
        path.append(arg1.affineTransform().createTransformedShape(arg0.path),
                false);
        changed();
    }

    public void addPath(Path arg0, float arg1, float arg2) {
        path.append(AffineTransform.getTranslateInstance(arg1, arg2)
                .createTransformedShape(arg0.path), false);
        changed();
    }

    public void addRect(RectF arg0, Path.Direction arg1) {
        addRect(arg0.left, arg0.top, arg0.right, arg0.bottom, arg1);
    }

    public void addRect(float arg0, float arg1, float arg2, float arg3,
            Path.Direction arg4) {
        // AOSP addRect: a new, closed contour; direction is a winding hint
        // only (see the class deviation note).
        append(new Rectangle2D.Float(arg0, arg1, arg2 - arg0, arg3 - arg1),
                false);
    }

    public void addRoundRect(RectF arg0, float arg1, float arg2,
            Path.Direction arg3) {
        addRoundRect(arg0.left, arg0.top, arg0.right, arg0.bottom, arg1, arg2,
                arg3);
    }

    public void addRoundRect(RectF arg0, float[] arg1, Path.Direction arg2) {
        addRoundRect(arg0.left, arg0.top, arg0.right, arg0.bottom, arg1, arg2);
    }

    public void addRoundRect(float arg0, float arg1, float arg2, float arg3,
            float arg4, float arg5, Path.Direction arg6) {
        // AWT's RoundRectangle2D takes arc diameters, Android takes radii.
        double width = Math.max(0.0f, arg2 - arg0);
        double height = Math.max(0.0f, arg3 - arg1);
        double rx = Math.min(arg4 * 2.0, width);
        double ry = Math.min(arg5 * 2.0, height);
        append(new RoundRectangle2D.Double(arg0, arg1, width, height, rx, ry),
                false);
    }

    public void addRoundRect(float arg0, float arg1, float arg2, float arg3,
            float[] arg4, Path.Direction arg5) {
        appendRoundRectRadii(arg0, arg1, arg2, arg3, arg4);
    }

    /**
     * Android's eight-radius round rect: [top-left-x, top-left-y,
     * top-right-x, top-right-y, bottom-right-x, bottom-right-y,
     * bottom-left-x, bottom-left-y].
     */
    private void appendRoundRectRadii(float left, float top, float right,
            float bottom, float[] radii) {
        if (radii == null || radii.length < 8) {
            throw new ArrayIndexOutOfBoundsException(
                    "radii must have 8 entries");
        }
        float width = Math.max(0.0f, right - left);
        float height = Math.max(0.0f, bottom - top);
        float tlx = Math.min(radii[0], width / 2.0f);
        float tly = Math.min(radii[1], height / 2.0f);
        float trx = Math.min(radii[2], width / 2.0f);
        float tryy = Math.min(radii[3], height / 2.0f);
        float brx = Math.min(radii[4], width / 2.0f);
        float bry = Math.min(radii[5], height / 2.0f);
        float blx = Math.min(radii[6], width / 2.0f);
        float bly = Math.min(radii[7], height / 2.0f);
        path.moveTo(left + tlx, top);
        path.lineTo(right - trx, top);
        path.quadTo(right, top, right, top + tryy);
        path.lineTo(right, bottom - bry);
        path.quadTo(right, bottom, right - brx, bottom);
        path.lineTo(left + blx, bottom);
        path.quadTo(left, bottom, left, bottom - bly);
        path.lineTo(left, top + tly);
        path.quadTo(left, top, left + tlx, top);
        path.closePath();
        changed();
    }

    public float[] approximate(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Path;->approximate(F)[F");
    }

    public void arcTo(RectF arg0, float arg1, float arg2) {
        arcTo(arg0, arg1, arg2, false);
    }

    public void arcTo(RectF arg0, float arg1, float arg2, boolean arg3) {
        arcTo(arg0.left, arg0.top, arg0.right, arg0.bottom, arg1, arg2, arg3);
    }

    public void arcTo(float arg0, float arg1, float arg2, float arg3,
            float arg4, float arg5, boolean arg6) {
        float width = arg2 - arg0;
        float height = arg3 - arg1;
        if (width <= 0.0f || height <= 0.0f) {
            return;
        }
        // AOSP angles run clockwise from 3 o'clock; Java2D runs
        // counter-clockwise, so the sweep is negated.
        Arc2D.Double arc = new Arc2D.Double(arg0, arg1, width, height, arg4,
                -arg5, Arc2D.OPEN);
        // forceMoveTo=false connects the current point to the arc start with
        // a line, which is exactly Path2D.append(connect=true).
        path.append(arc, !arg6);
        changed();
    }

    public void close() {
        // Skia's close on an empty path is a no-op; Java2D throws.
        if (path.getCurrentPoint() != null) {
            path.closePath();
            changed();
        }
    }

    public void computeBounds(RectF arg0) {
        computeBounds(arg0, true);
    }

    public void computeBounds(RectF arg0, boolean arg1) {
        // Deviation: both modes report the control-point bounds (Skia's
        // conservative bounds); tight curve extrema wait for a wall.
        Rectangle2D bounds = path.getBounds2D();
        arg0.set((float) bounds.getX(), (float) bounds.getY(),
                (float) (bounds.getX() + bounds.getWidth()),
                (float) (bounds.getY() + bounds.getHeight()));
    }

    public void conicTo(float arg0, float arg1, float arg2, float arg3,
            float arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Path;->conicTo(FFFFF)V");
    }

    public void cubicTo(float arg0, float arg1, float arg2, float arg3,
            float arg4, float arg5) {
        ensureStarted();
        path.curveTo(arg0, arg1, arg2, arg3, arg4, arg5);
        changed();
    }

    public Path.FillType getFillType() {
        return fillType;
    }

    public int getGenerationId() {
        return generationId;
    }

    public PathIterator getPathIterator() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Path;->getPathIterator()Landroid/graphics/PathIterator;");
    }

    public void incReserve(int arg0) {
        // Capacity hint only; Path2D grows as needed.
    }

    public boolean interpolate(Path arg0, float arg1, Path arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Path;->interpolate(Landroid/graphics/Path;FLandroid/graphics/Path;)Z");
    }

    public boolean isConvex() {
        // Deviation: conservative answer. Skia's fast path skips this
        // optimization; correctness of fills is unaffected.
        return false;
    }

    public boolean isEmpty() {
        return path.getPathIterator(null).isDone();
    }

    public boolean isInterpolatable(Path arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Path;->isInterpolatable(Landroid/graphics/Path;)Z");
    }

    public boolean isInverseFillType() {
        return inverseFillType;
    }

    public boolean isRect(RectF arg0) {
        // Deviation: conservative answer (see isConvex); callers fall back
        // to the general path draw.
        return false;
    }

    public void lineTo(float arg0, float arg1) {
        ensureStarted();
        path.lineTo(arg0, arg1);
        changed();
    }

    public void moveTo(float arg0, float arg1) {
        path.moveTo(arg0, arg1);
        changed();
    }

    public void offset(float arg0, float arg1) {
        path.transform(AffineTransform.getTranslateInstance(arg0, arg1));
        changed();
    }

    public void offset(float arg0, float arg1, Path arg2) {
        if (arg2 == null) {
            offset(arg0, arg1);
            return;
        }
        arg2.set(this);
        arg2.offset(arg0, arg1);
    }

    public boolean op(Path arg0, Path arg1, Path.Op arg2) {
        if (arg0 == null || arg1 == null) {
            return false;
        }
        try {
            Area result = new Area(arg0.path);
            applyOp(result, arg1.path, arg2);
            replaceWith(result);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public boolean op(Path arg0, Path.Op arg1) {
        if (arg0 == null) {
            return false;
        }
        try {
            Area result = new Area(path);
            applyOp(result, arg0.path, arg1);
            replaceWith(result);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    private static void applyOp(Area target, java.awt.Shape other, Op op) {
        switch (op) {
            case DIFFERENCE:
                target.subtract(new Area(other));
                break;
            case INTERSECT:
                target.intersect(new Area(other));
                break;
            case REVERSE_DIFFERENCE:
                Area reversed = new Area(other);
                reversed.subtract(target);
                target.reset();
                target.add(reversed);
                break;
            case UNION:
                target.add(new Area(other));
                break;
            case XOR:
                target.exclusiveOr(new Area(other));
                break;
            default:
                throw new IllegalArgumentException("unknown path op");
        }
    }

    public void quadTo(float arg0, float arg1, float arg2, float arg3) {
        ensureStarted();
        path.quadTo(arg0, arg1, arg2, arg3);
        changed();
    }

    public void rConicTo(float arg0, float arg1, float arg2, float arg3,
            float arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Path;->rConicTo(FFFFF)V");
    }

    public void rCubicTo(float arg0, float arg1, float arg2, float arg3,
            float arg4, float arg5) {
        ensureStarted();
        double[] current = currentPoint();
        path.curveTo(current[0] + arg0, current[1] + arg1,
                current[0] + arg2, current[1] + arg3,
                current[0] + arg4, current[1] + arg5);
        changed();
    }

    public void rLineTo(float arg0, float arg1) {
        ensureStarted();
        double[] current = currentPoint();
        path.lineTo(current[0] + arg0, current[1] + arg1);
        changed();
    }

    public void rMoveTo(float arg0, float arg1) {
        double[] current = currentPoint();
        path.moveTo(current[0] + arg0, current[1] + arg1);
        changed();
    }

    public void rQuadTo(float arg0, float arg1, float arg2, float arg3) {
        ensureStarted();
        double[] current = currentPoint();
        path.quadTo(current[0] + arg0, current[1] + arg1,
                current[0] + arg2, current[1] + arg3);
        changed();
    }

    public void reset() {
        path = new Path2D.Double(Path2D.WIND_NON_ZERO);
        fillType = FillType.WINDING;
        inverseFillType = false;
        changed();
    }

    public void rewind() {
        // AOSP rewind: clear the geometry but keep the fill type.
        path = new Path2D.Double(windingRule());
        changed();
    }

    public void set(Path arg0) {
        path = (Path2D.Double) arg0.path.clone();
        path.setWindingRule(arg0.windingRule());
        fillType = arg0.fillType;
        inverseFillType = arg0.inverseFillType;
        changed();
    }

    public void setFillType(Path.FillType arg0) {
        fillType = arg0 == null ? FillType.WINDING : arg0;
        path.setWindingRule(windingRule());
        changed();
    }

    public void setLastPoint(float arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Path;->setLastPoint(FF)V");
    }

    public void toggleInverseFillType() {
        inverseFillType = !inverseFillType;
        changed();
    }

    public void transform(Matrix arg0) {
        transform(arg0, null);
    }

    public void transform(Matrix arg0, Path arg1) {
        if (arg0 == null) {
            throw new NullPointerException("matrix must not be null");
        }
        Path2D.Double transformed = new Path2D.Double(windingRule());
        transformed.append(arg0.affineTransform().createTransformedShape(path),
                false);
        if (arg1 == null) {
            path = transformed;
            changed();
            return;
        }
        arg1.path = transformed;
        arg1.fillType = fillType;
        arg1.inverseFillType = inverseFillType;
        arg1.changed();
    }

    /**
     * Host geometry bridge: the live model shape, for internal framework
     * consumers ({@link Region}, the animation interpolators). Not public
     * API; callers must not mutate it.
     */
    public java.awt.Shape tartineShape() {
        return path;
    }

    /** Host geometry bridge for {@link Region#getBoundaryPath()}. */
    void tartineSetShape(java.awt.Shape shape) {
        Path2D.Double replacement = new Path2D.Double(windingRule());
        replacement.append(shape, false);
        path = replacement;
        changed();
    }

    /** Renderer-seam snapshot: verbs, coordinates and fill rule. */
    GfxPath tartineGfx() {
        int verbCount = 0;
        int pointCount = 0;
        java.awt.geom.PathIterator counter = path.getPathIterator(null);
        double[] coords = new double[6];
        while (!counter.isDone()) {
            int segment = counter.currentSegment(coords);
            verbCount++;
            pointCount += coordinateCount(segment);
            counter.next();
        }
        int[] verbs = new int[verbCount];
        float[] points = new float[pointCount];
        java.awt.geom.PathIterator iterator = path.getPathIterator(null);
        int verbAt = 0;
        int pointAt = 0;
        while (!iterator.isDone()) {
            int segment = iterator.currentSegment(coords);
            switch (segment) {
                case java.awt.geom.PathIterator.SEG_MOVETO:
                    verbs[verbAt++] = GfxPath.MOVE;
                    break;
                case java.awt.geom.PathIterator.SEG_LINETO:
                    verbs[verbAt++] = GfxPath.LINE;
                    break;
                case java.awt.geom.PathIterator.SEG_QUADTO:
                    verbs[verbAt++] = GfxPath.QUAD;
                    break;
                case java.awt.geom.PathIterator.SEG_CUBICTO:
                    verbs[verbAt++] = GfxPath.CUBIC;
                    break;
                case java.awt.geom.PathIterator.SEG_CLOSE:
                    verbs[verbAt++] = GfxPath.CLOSE;
                    break;
                default:
                    throw new IllegalStateException("unknown path segment");
            }
            int count = coordinateCount(segment);
            for (int i = 0; i < count; i++) {
                points[pointAt++] = (float) coords[i];
            }
            iterator.next();
        }
        return new GfxPath(verbs, points, gfxFillMode());
    }

    private static int coordinateCount(int segment) {
        switch (segment) {
            case java.awt.geom.PathIterator.SEG_MOVETO:
            case java.awt.geom.PathIterator.SEG_LINETO:
                return 2;
            case java.awt.geom.PathIterator.SEG_QUADTO:
                return 4;
            case java.awt.geom.PathIterator.SEG_CUBICTO:
                return 6;
            default:
                return 0;
        }
    }

    private GfxPath.FillMode gfxFillMode() {
        switch (fillType) {
            case EVEN_ODD:
                return GfxPath.FillMode.EVEN_ODD;
            case INVERSE_WINDING:
                return GfxPath.FillMode.INVERSE_WINDING;
            case INVERSE_EVEN_ODD:
                return GfxPath.FillMode.INVERSE_EVEN_ODD;
            default:
                return GfxPath.FillMode.WINDING;
        }
    }

    private int windingRule() {
        return fillType == FillType.EVEN_ODD
                || fillType == FillType.INVERSE_EVEN_ODD
                ? Path2D.WIND_EVEN_ODD : Path2D.WIND_NON_ZERO;
    }

    private void append(java.awt.Shape shape, boolean connect) {
        path.append(shape, connect);
        changed();
    }

    private void replaceWith(Area area) {
        Path2D.Double replacement = new Path2D.Double(windingRule());
        replacement.append(area, false);
        path = replacement;
        changed();
    }

    private double[] currentPoint() {
        java.awt.geom.Point2D point = path.getCurrentPoint();
        if (point == null) {
            return new double[] {0.0, 0.0};
        }
        return new double[] {point.getX(), point.getY()};
    }

    /**
     * Skia injects a moveTo(0,0) when a segment op starts an empty path
     * (SkPath::injectMoveToIfNeeded); Java2D's Path2D instead throws
     * IllegalPathStateException. Emulate Skia so callers that start with a
     * line/curve draw the same geometry.
     */
    private void ensureStarted() {
        if (path.getCurrentPoint() == null) {
            path.moveTo(0.0, 0.0);
        }
    }

    private void changed() {
        generationId++;
    }

    public static enum Direction {
        CCW, CW;
    }

    public static enum FillType {
        EVEN_ODD, INVERSE_EVEN_ODD, INVERSE_WINDING, WINDING;
    }

    public static enum Op {
        DIFFERENCE, INTERSECT, REVERSE_DIFFERENCE, UNION, XOR;
    }
}
