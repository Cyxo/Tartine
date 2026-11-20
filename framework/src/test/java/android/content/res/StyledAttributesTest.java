package android.content.res;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import android.content.Context;
import android.util.TypedValue;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xmlpull.v1.XmlPullParser;
import tartine.fw.AppResources;
import tartine.fw.FakeAppResourceHost;
import tartine.fw.XmlNode;

/**
 * The styled-attribute lookup chain the inflation path runs on: an element's
 * own attributes beat the theme, the applied style list resolves last-wins,
 * {@code defStyleAttr}/{@code defStyleRes}/element-{@code style} participate
 * in AOSP order, and a {@code ?attr} value follows one hop. All lookups run
 * against a hand-built {@link FakeAppResourceHost}, so no APK is required.
 */
class StyledAttributesTest {

    private static final int ATTR_WIDTH = 0x01010000;
    private static final int ATTR_TINT = 0x01010001;
    private static final int ATTR_DEF_STYLE = 0x01010002;
    private static final int THEME_STYLE = 0x7F010001;
    private static final int OTHER_STYLE = 0x7F010002;
    private static final int DEF_STYLE = 0x7F010003;
    private static final int ELEMENT_STYLE = 0x7F010004;

    private final FakeAppResourceHost host = new FakeAppResourceHost();

    @BeforeEach
    void installHost() {
        AppResources.install(host);
    }

    @AfterEach
    void clearHost() {
        AppResources.install(null);
    }

    @Test
    void elementAttributeBeatsTheme() {
        host.putStyleAttribute(THEME_STYLE, ATTR_WIDTH,
                FakeAppResourceHost.typed(TypedValue.TYPE_INT_DEC, 11));
        Context context = contextWithTheme(THEME_STYLE);
        XmlResourceParserImpl element = element(
                attr(ATTR_WIDTH, TypedValue.TYPE_INT_DEC, 22));

        TypedArray array = context.getTheme().obtainStyledAttributes(element,
                new int[] {ATTR_WIDTH}, 0, 0);

        assertTrue(array.hasValue(0));
        assertEquals(22, array.getInt(0, -1));
    }

    @Test
    void lastAppliedThemeStyleWins() {
        host.putStyleAttribute(THEME_STYLE, ATTR_WIDTH,
                FakeAppResourceHost.typed(TypedValue.TYPE_INT_DEC, 1));
        host.putStyleAttribute(OTHER_STYLE, ATTR_WIDTH,
                FakeAppResourceHost.typed(TypedValue.TYPE_INT_DEC, 2));
        Context context = contextWithTheme(THEME_STYLE);
        context.getTheme().applyStyle(OTHER_STYLE, true);

        TypedArray array = context.getTheme().obtainStyledAttributes(
                new int[] {ATTR_WIDTH});

        assertEquals(2, array.getInt(0, -1));
    }

    @Test
    void defStyleResBacksMissingAttribute() {
        host.putStyleAttribute(DEF_STYLE, ATTR_WIDTH,
                FakeAppResourceHost.typed(TypedValue.TYPE_INT_DEC, 7));

        TypedArray array = new Context().getTheme().obtainStyledAttributes(
                null, new int[] {ATTR_WIDTH}, 0, DEF_STYLE);

        assertEquals(7, array.getInt(0, -1));
    }

    @Test
    void defStyleAttrResolvesThroughThemeReference() {
        host.putStyleAttribute(THEME_STYLE, ATTR_DEF_STYLE,
                FakeAppResourceHost.reference(DEF_STYLE));
        host.putStyleAttribute(DEF_STYLE, ATTR_WIDTH,
                FakeAppResourceHost.typed(TypedValue.TYPE_INT_DEC, 9));
        Context context = contextWithTheme(THEME_STYLE);

        TypedArray array = context.getTheme().obtainStyledAttributes(
                null, new int[] {ATTR_WIDTH}, ATTR_DEF_STYLE, 0);

        assertEquals(9, array.getInt(0, -1));
    }

    @Test
    void attributeReferenceFollowsOneHop() {
        host.putStyleAttribute(THEME_STYLE, ATTR_WIDTH,
                FakeAppResourceHost.typed(TypedValue.TYPE_ATTRIBUTE, ATTR_TINT));
        host.putStyleAttribute(THEME_STYLE, ATTR_TINT,
                FakeAppResourceHost.typed(TypedValue.TYPE_INT_DEC, 5));
        Context context = contextWithTheme(THEME_STYLE);

        TypedArray array = context.getTheme().obtainStyledAttributes(
                new int[] {ATTR_WIDTH});

        assertEquals(5, array.getInt(0, -1));
    }

    @Test
    void elementStyleAttributeOutranksTheme() {
        host.putStyleAttribute(THEME_STYLE, ATTR_WIDTH,
                FakeAppResourceHost.typed(TypedValue.TYPE_INT_DEC, 1));
        host.putStyleAttribute(ELEMENT_STYLE, ATTR_WIDTH,
                FakeAppResourceHost.typed(TypedValue.TYPE_INT_DEC, 2));
        Context context = contextWithTheme(THEME_STYLE);
        XmlResourceParserImpl element = element(new XmlNode.Attribute(null,
                "style", 0, TypedValue.TYPE_REFERENCE, ELEMENT_STYLE, null));

        TypedArray array = context.getTheme().obtainStyledAttributes(element,
                new int[] {ATTR_WIDTH}, 0, 0);

        assertEquals(2, array.getInt(0, -1));
    }

    @Test
    void elementReferenceReportsItsTargetId() {
        XmlResourceParserImpl element = element(new XmlNode.Attribute(null,
                "background", ATTR_TINT, TypedValue.TYPE_REFERENCE,
                ELEMENT_STYLE, null));

        TypedArray array = new Context().getResources().obtainAttributes(
                element, new int[] {ATTR_TINT});

        assertTrue(array.hasValue(0));
        assertEquals(TypedValue.TYPE_REFERENCE, array.getType(0));
        assertEquals(ELEMENT_STYLE, array.getResourceId(0, -1));
    }

    @Test
    void elementLiteralValuesDoNotReportResourceIds() {
        XmlResourceParserImpl element = element(
                attr(ATTR_WIDTH, TypedValue.TYPE_INT_DEC, 17),
                attr(ATTR_TINT, TypedValue.TYPE_DIMENSION, 0x00000301),
                new XmlNode.Attribute(null, "attr", ATTR_DEF_STYLE,
                        TypedValue.TYPE_STRING, 0, "literal"));

        TypedArray array = new Context().getResources().obtainAttributes(
                element, new int[] {ATTR_WIDTH, ATTR_TINT, ATTR_DEF_STYLE});

        for (int i = 0; i < 3; i++) {
            assertTrue(array.hasValue(i));
            assertEquals(-1, array.getResourceId(i, -1));
        }
    }

    @Test
    void elementLocalValuesSurviveWithoutHost() {
        AppResources.install(null);
        XmlResourceParserImpl element = element(
                attr(ATTR_WIDTH, TypedValue.TYPE_INT_DEC, 1));

        TypedArray array = new Context().getTheme().obtainStyledAttributes(
                element, new int[] {ATTR_WIDTH, ATTR_TINT}, 0, 0);

        assertTrue(array.hasValue(0));
        assertEquals(1, array.getInt(0, -1));
        assertFalse(array.hasValue(1));
        assertEquals(-1, array.getInt(1, -1));
    }

    @Test
    void missingThemeAttributeDegradesToTheElementOnly() {
        XmlResourceParserImpl element = element(
                attr(ATTR_WIDTH, TypedValue.TYPE_INT_DEC, 3));

        TypedArray array = new Context().getTheme().obtainStyledAttributes(
                element, new int[] {ATTR_WIDTH, ATTR_TINT}, 0, 0);

        assertEquals(3, array.getInt(0, -1));
        assertFalse(array.hasValue(1));
    }

    @Test
    void indexCountReportsOnlyPresentSlots() {
        // AOSP's TypedArray.getIndexCount counts the attributes that
        // actually resolved; getIndex maps each back to the styleable slot
        // (a layout setting two of three attributes reports two).
        XmlResourceParserImpl element = element(
                attr(ATTR_WIDTH, TypedValue.TYPE_INT_DEC, 3),
                attr(ATTR_DEF_STYLE, TypedValue.TYPE_INT_DEC, 4));

        TypedArray array = new Context().getResources().obtainAttributes(
                element, new int[] {ATTR_WIDTH, ATTR_TINT, ATTR_DEF_STYLE});

        assertEquals(2, array.getIndexCount());
        assertEquals(0, array.getIndex(0));
        assertEquals(2, array.getIndex(1));
        assertThrows(IndexOutOfBoundsException.class, () -> array.getIndex(2));
    }

    private static Context contextWithTheme(int styleId) {
        Context context = new Context();
        context.getTheme().applyStyle(styleId, true);
        return context;
    }

    private static XmlResourceParserImpl element(XmlNode.Attribute... attributes) {
        XmlResourceParserImpl parser = new XmlResourceParserImpl(
                new XmlNode(null, "View", List.of(attributes), List.of()));
        assertEquals(XmlPullParser.START_TAG, parser.next());
        return parser;
    }

    private static XmlNode.Attribute attr(int resId, int type, int data) {
        return new XmlNode.Attribute(null, "attr", resId, type, data, null);
    }
}
