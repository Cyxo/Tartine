package android.view.animation;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.TypedValue;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * AOSP AnimationUtils, ported wall-by-wall: the guest needs the animation
 * clock ({@code currentAnimationTimeMillis}) and interpolator loading
 * ({@code loadInterpolator}). Compose reaches the clock from its hover path
 * ({@code AndroidComposeView.recalculateWindowPosition}); Material's motion
 * helpers reach {@code loadInterpolator} while inflating a text field.
 * Further members (loadAnimation/loadLayoutAnimation) attach when a guest
 * actually calls them and degrade to a named gap until then.
 */
public class AnimationUtils {

    private static final String ANDROID_NS =
            "http://schemas.android.com/apk/res/android";

    public static long currentAnimationTimeMillis() {
        return SystemClock.uptimeMillis();
    }

    /**
     * AOSP {@code AnimationUtils.loadInterpolator(Context, int)}: read the
     * interpolator XML resource and build the matching platform interpolator.
     *
     * @param context context whose resources resolve the XML
     * @param id the interpolator resource id
     * @return the constructed interpolator
     * @throws Resources.NotFoundException when the resource cannot be read
     */
    public static Interpolator loadInterpolator(Context context, int id)
            throws Resources.NotFoundException {
        XmlResourceParser parser = null;
        try {
            parser = context.getResources().getAnimation(id);
            return createInterpolatorFromXml(context.getResources(), parser);
        } catch (XmlPullParserException | IOException e) {
            throw new Resources.NotFoundException(
                    "Can't load animation resource ID #0x"
                            + Integer.toHexString(id), e);
        } finally {
            if (parser != null) {
                parser.close();
            }
        }
    }

    private static Interpolator createInterpolatorFromXml(Resources resources,
            XmlPullParser parser) throws XmlPullParserException, IOException {
        BaseInterpolator interpolator = null;
        int depth = parser.getDepth();
        int type;
        while (((type = parser.next()) != XmlPullParser.END_TAG
                || parser.getDepth() > depth)
                && type != XmlPullParser.END_DOCUMENT) {
            if (type != XmlPullParser.START_TAG) {
                continue;
            }
            AttributeSet attrs = parser instanceof AttributeSet
                    ? (AttributeSet) parser : null;
            String name = parser.getName();
            if ("linearInterpolator".equals(name)) {
                interpolator = new LinearInterpolator();
            } else if ("accelerateInterpolator".equals(name)) {
                interpolator = new AccelerateInterpolator(
                        factor(resources, attrs));
            } else if ("decelerateInterpolator".equals(name)) {
                interpolator = new DecelerateInterpolator(
                        factor(resources, attrs));
            } else if ("accelerateDecelerateInterpolator".equals(name)) {
                interpolator = new AccelerateDecelerateInterpolator();
            } else if ("pathInterpolator".equals(name)) {
                interpolator = attrs == null
                        ? new PathInterpolator(0.4f, 0.0f, 0.2f, 1.0f)
                        : new PathInterpolator(resources, attrs);
            } else {
                throw new Resources.NotFoundException(
                        "Unknown interpolator name: " + name);
            }
        }
        return interpolator;
    }

    /** The {@code factor} attribute; AOSP's default is 1.0. */
    private static float factor(Resources resources, AttributeSet attrs) {
        if (attrs == null) {
            return 1.0f;
        }
        int id = attrs.getAttributeResourceValue(ANDROID_NS, "factor", 0);
        if (id == 0) {
            return attrs.getAttributeFloatValue(ANDROID_NS, "factor", 1.0f);
        }
        try {
            TypedValue value = new TypedValue();
            resources.getValue(id, value, true);
            return Float.intBitsToFloat(value.data);
        } catch (RuntimeException e) {
            return 1.0f;
        }
    }
}
