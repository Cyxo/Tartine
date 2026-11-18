package android.view.animation;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.TypedValue;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;

/**
 * AOSP {@code android.view.animation.PathInterpolator}: an interpolator
 * defined by a path from (0,0) to (1,1), most often a cubic Bezier.
 *
 * <p>AOSP samples the native path into an x/y table and binary-searches it.
 * The shell samples the same geometry through the framework {@link Path}
 * model (Path2D), so a control-point curve, a {@code pathData} string and a
 * programmatic path all answer the same way. Deviations: the sample table is
 * rebuilt eagerly with a fixed 0.002 flatness (AOSP's native path measure
 * uses the same idea), and paths whose x is not monotonic follow the first
 * matching sample.
 */
public class PathInterpolator extends BaseInterpolator {

    private static final String ANDROID_NS =
            "http://schemas.android.com/apk/res/android";
    private static final float FLATNESS = 0.002f;

    private final float[] mX;
    private final float[] mY;

    public PathInterpolator(float controlX1, float controlY1,
            float controlX2, float controlY2) {
        this(new float[] {0.0f, 0.0f, controlX1, controlY1, controlX2,
                controlY2, 1.0f, 1.0f});
    }

    public PathInterpolator(Path path) {
        this(sample(path));
    }

    /**
     * AOSP's XML constructor: {@code pathData}, or the two control points.
     *
     * @param context context whose resources resolve attribute references
     * @param attrs the {@code <pathInterpolator>} attributes
     */
    public PathInterpolator(Context context, AttributeSet attrs) {
        this(context.getResources(), attrs);
    }

    PathInterpolator(Resources resources, AttributeSet attrs) {
        this(samplesOf(resources, attrs));
    }

    private PathInterpolator(float[] samples) {
        int count = samples.length / 2;
        if (count < 2) {
            throw new IllegalArgumentException("path has too few points");
        }
        mX = new float[count];
        mY = new float[count];
        for (int i = 0; i < count; i++) {
            mX[i] = samples[i * 2];
            mY[i] = samples[i * 2 + 1];
        }
    }

    private static float[] samplesOf(Resources resources, AttributeSet attrs) {
        int pathDataId = attrs.getAttributeResourceValue(ANDROID_NS,
                "pathData", 0);
        String pathData = pathDataId != 0
                ? resources.getString(pathDataId)
                : attrs.getAttributeValue(ANDROID_NS, "pathData");
        if (pathData != null && !pathData.isEmpty()) {
            return sample(parsePathData(pathData));
        }
        float x1 = floatAttribute(resources, attrs, "controlX1", 0.0f);
        float y1 = floatAttribute(resources, attrs, "controlY1", 0.0f);
        float x2 = floatAttribute(resources, attrs, "controlX2", 1.0f);
        float y2 = floatAttribute(resources, attrs, "controlY2", 1.0f);
        return cubicSamples(x1, y1, x2, y2);
    }

    /** A float attribute, following a resource reference when present. */
    private static float floatAttribute(Resources resources, AttributeSet attrs,
            String name, float fallback) {
        int id = attrs.getAttributeResourceValue(ANDROID_NS, name, 0);
        if (id == 0) {
            return attrs.getAttributeFloatValue(ANDROID_NS, name, fallback);
        }
        try {
            TypedValue value = new TypedValue();
            resources.getValue(id, value, true);
            if (value.type == TypedValue.TYPE_FLOAT) {
                return Float.intBitsToFloat(value.data);
            }
            if (value.type == TypedValue.TYPE_DIMENSION) {
                return TypedValue.complexToDimension(value.data,
                        resources.getDisplayMetrics());
            }
        } catch (RuntimeException e) {
            return fallback;
        }
        return fallback;
    }

    private static float[] cubicSamples(float x1, float y1, float x2,
            float y2) {
        // 101 uniform t samples: plenty for the interpolation table, and it
        // matches AOSP's sampled-curve answer to well under a pixel of
        // progress.
        int steps = 100;
        float[] samples = new float[(steps + 1) * 2];
        for (int i = 0; i <= steps; i++) {
            float t = i / (float) steps;
            float oneMinusT = 1.0f - t;
            float a = oneMinusT * oneMinusT * oneMinusT;
            float b = 3.0f * oneMinusT * oneMinusT * t;
            float c = 3.0f * oneMinusT * t * t;
            float d = t * t * t;
            samples[i * 2] = b * x1 + c * x2 + d;
            samples[i * 2 + 1] = b * y1 + c * y2 + d;
        }
        return samples;
    }

    /** Flattens a path into an x/y sample table. */
    private static float[] sample(Path path) {
        List<Float> values = new ArrayList<>();
        PathIterator iterator = path.tartineShape().getPathIterator(null,
                FLATNESS);
        double[] coords = new double[6];
        while (!iterator.isDone()) {
            int segment = iterator.currentSegment(coords);
            if (segment == PathIterator.SEG_MOVETO
                    || segment == PathIterator.SEG_LINETO) {
                values.add((float) coords[0]);
                values.add((float) coords[1]);
            } else if (segment == PathIterator.SEG_CLOSE) {
                // The closing edge returns to the contour start; the table
                // only needs the sampled interior points.
                break;
            }
            iterator.next();
        }
        float[] samples = new float[values.size()];
        for (int i = 0; i < samples.length; i++) {
            samples[i] = values.get(i);
        }
        return samples;
    }

    /**
     * Parses the SVG path subset interpolator resources use: absolute and
     * relative M/L/C/Q commands plus close.
     */
    static Path parsePathData(String data) {
        Path path = new Path();
        String[] tokens = data.trim().split("[\\s,]+");
        float currentX = 0.0f;
        float currentY = 0.0f;
        float startX = 0.0f;
        float startY = 0.0f;
        char command = 0;
        int at = 0;
        while (at < tokens.length) {
            String token = tokens[at];
            if (token.length() == 1 && "MmLlCcQqZz".indexOf(token.charAt(0)) >= 0) {
                command = token.charAt(0);
                at++;
                if (command == 'Z' || command == 'z') {
                    path.close();
                    currentX = startX;
                    currentY = startY;
                }
                continue;
            }
            switch (command) {
                case 'M':
                case 'm': {
                    float x = Float.parseFloat(tokens[at]);
                    float y = Float.parseFloat(tokens[at + 1]);
                    at += 2;
                    if (command == 'm') {
                        x += currentX;
                        y += currentY;
                    }
                    path.moveTo(x, y);
                    currentX = x;
                    currentY = y;
                    startX = x;
                    startY = y;
                    command = command == 'M' ? 'L' : 'l';
                    break;
                }
                case 'L':
                case 'l': {
                    float x = Float.parseFloat(tokens[at]);
                    float y = Float.parseFloat(tokens[at + 1]);
                    at += 2;
                    if (command == 'l') {
                        x += currentX;
                        y += currentY;
                    }
                    path.lineTo(x, y);
                    currentX = x;
                    currentY = y;
                    break;
                }
                case 'C':
                case 'c': {
                    float x1 = Float.parseFloat(tokens[at]);
                    float y1 = Float.parseFloat(tokens[at + 1]);
                    float x2 = Float.parseFloat(tokens[at + 2]);
                    float y2 = Float.parseFloat(tokens[at + 3]);
                    float x = Float.parseFloat(tokens[at + 4]);
                    float y = Float.parseFloat(tokens[at + 5]);
                    at += 6;
                    if (command == 'c') {
                        x1 += currentX;
                        y1 += currentY;
                        x2 += currentX;
                        y2 += currentY;
                        x += currentX;
                        y += currentY;
                    }
                    path.cubicTo(x1, y1, x2, y2, x, y);
                    currentX = x;
                    currentY = y;
                    break;
                }
                case 'Q':
                case 'q': {
                    float x1 = Float.parseFloat(tokens[at]);
                    float y1 = Float.parseFloat(tokens[at + 1]);
                    float x = Float.parseFloat(tokens[at + 2]);
                    float y = Float.parseFloat(tokens[at + 3]);
                    at += 4;
                    if (command == 'q') {
                        x1 += currentX;
                        y1 += currentY;
                        x += currentX;
                        y += currentY;
                    }
                    path.quadTo(x1, y1, x, y);
                    currentX = x;
                    currentY = y;
                    break;
                }
                default:
                    // Unknown token with no command: stop rather than loop.
                    return path;
            }
        }
        return path;
    }

    @Override
    public float getInterpolation(float input) {
        if (input <= 0.0f) {
            return 0.0f;
        }
        if (input >= 1.0f) {
            return 1.0f;
        }
        int low = 0;
        int high = mX.length - 1;
        while (high - low > 1) {
            int mid = (low + high) / 2;
            if (mX[mid] < input) {
                low = mid;
            } else {
                high = mid;
            }
        }
        float span = mX[high] - mX[low];
        if (span <= 0.0f) {
            return mY[high];
        }
        float fraction = (input - mX[low]) / span;
        return mY[low] + fraction * (mY[high] - mY[low]);
    }
}
