package android.graphics.drawable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.TypedValue;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tartine.fw.AppResources;
import tartine.fw.DisplayConfig;
import tartine.fw.FakeAppResourceHost;
import tartine.fw.XmlNode;
import tartine.gfx.GfxBackend;
import tartine.gfx.testing.RecordingBackend;

/** Static vector inflation, path-data parsing and viewport-mapped drawing. */
class VectorDrawableTest {

    private static final String ANDROID_NS =
            "http://schemas.android.com/apk/res/android";
    private static final int VECTOR_ID = 0x7F070001;
    private static final int ATTR_WIDTH = 0x01010159;
    private static final int ATTR_HEIGHT = 0x01010155;
    private static final int ATTR_VIEWPORT_WIDTH = 0x01010402;
    private static final int ATTR_VIEWPORT_HEIGHT = 0x01010403;
    private static final int ATTR_FILL_COLOR = 0x01010404;
    private static final int ATTR_PATH_DATA = 0x01010405;
    private static final int ANDROID_WHITE = 0x0106000B;

    private RecordingBackend backend;

    @BeforeEach
    void installFakes() {
        DisplayConfig.reset();
        GfxBackend.uninstall();
        backend = new RecordingBackend();
        GfxBackend.install(backend);
    }

    @AfterEach
    void uninstallFakes() {
        AppResources.install(null);
        GfxBackend.uninstall();
        DisplayConfig.reset();
    }

    @Test
    void closeIconInflatesAndDrawsItsCompactFilledPath() {
        String data = "M19,6.41L17.59,5 12,10.59 6.41,5 5,6.41 "
                + "10.59,12 5,17.59 6.41,19 12,13.41 17.59,19 "
                + "19,17.59 13.41,12z";
        VectorDrawable drawable = inflateCloseIcon(data);
        assertEquals(24, drawable.getIntrinsicWidth());
        assertEquals(24, drawable.getIntrinsicHeight());

        backend.createSurface(80, 80);
        Canvas canvas = Canvas.tartineCreate(backend.lastSurface.getCanvas());
        drawable.setBounds(2, 3, 50, 27);
        drawable.setAlpha(128);
        drawable.draw(canvas);

        List<String> ops = ((RecordingBackend.FakeCanvas)
                backend.lastSurface.getCanvas()).ops();
        assertEquals("save", ops.get(0));
        assertEquals("translate(2.0,3.0)", ops.get(1));
        assertEquals("scale(2.0,1.0)", ops.get(2));
        String draw = ops.get(3);
        assertTrue(draw.startsWith("drawPath(verbs=[0, 1, 1, 1, 1, 1, 1, 1,"),
                draw);
        assertTrue(draw.contains(
                "points=[19.0, 6.41, 17.59, 5.0, 12.0, 10.59"));
        assertTrue(draw.contains("13.41, 12.0],fill=WINDING"));
        assertTrue(draw.contains("color=0x80ffffff"));
        assertEquals("restore", ops.get(4));
    }

    @Test
    void relativeMovetoTurnsLaterPairsIntoRelativeLines() {
        android.graphics.Path path = VectorPathParser.parse("M1,2l3,4 2,-1z");

        RectF bounds = new RectF();
        path.computeBounds(bounds);
        assertEquals(new RectF(1.0f, 2.0f, 6.0f, 6.0f), bounds);
    }

    private VectorDrawable inflateCloseIcon(String pathData) {
        XmlNode root = new XmlNode(null, "vector", List.of(
                dimension("width", ATTR_WIDTH, 24),
                dimension("height", ATTR_HEIGHT, 24),
                number("viewportWidth", ATTR_VIEWPORT_WIDTH, 24.0f),
                number("viewportHeight", ATTR_VIEWPORT_HEIGHT, 24.0f)),
                List.of(new XmlNode(null, "path", List.of(
                        new XmlNode.Attribute(ANDROID_NS, "fillColor",
                                ATTR_FILL_COLOR, TypedValue.TYPE_REFERENCE,
                                ANDROID_WHITE, null),
                        new XmlNode.Attribute(ANDROID_NS, "pathData",
                                ATTR_PATH_DATA, TypedValue.TYPE_STRING,
                                0, pathData)), List.of())));
        FakeAppResourceHost host = new FakeAppResourceHost();
        host.putDocument(VECTOR_ID, root);
        AppResources.install(host);
        Resources resources = new Resources();
        try (XmlResourceParser parser = resources.getXml(VECTOR_ID)) {
            return assertInstanceOf(VectorDrawable.class,
                    Drawable.createFromXml(resources, parser));
        }
    }

    private static XmlNode.Attribute dimension(String name, int id, int dp) {
        // COMPLEX_RADIX_23p0 is zero, so its bits need no explicit OR.
        int data = (dp << TypedValue.COMPLEX_MANTISSA_SHIFT)
                | TypedValue.COMPLEX_UNIT_DIP;
        return new XmlNode.Attribute(ANDROID_NS, name, id,
                TypedValue.TYPE_DIMENSION, data, null);
    }

    private static XmlNode.Attribute number(String name, int id, float value) {
        return new XmlNode.Attribute(ANDROID_NS, name, id,
                TypedValue.TYPE_FLOAT, Float.floatToIntBits(value), null);
    }
}
