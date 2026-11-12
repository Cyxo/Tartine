package android.content.res;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
 * The pull-parser view of a compiled layout document: a depth-first walk over
 * the host-decoded tree (via {@code Resources.getLayout}), attribute names and
 * {@code android} resource ids at the current element, and the named gap an
 * unknown layout id still produces.
 */
class XmlResourceParserImplTest {

    private static final String ANDROID_NS =
            "http://schemas.android.com/apk/res/android";
    private static final int LAYOUT = 0x7F030000;
    private static final int MISSING_LAYOUT = 0x7F030999;
    private static final int ATTR_ID = 0x01010000;
    private static final int ATTR_TEXT = 0x01010001;

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
    void walkEmitsDepthFirstTags() {
        XmlNode leaf = new XmlNode(null, "Leaf", List.of(), List.of());
        XmlNode inner = new XmlNode(null, "Inner", List.of(), List.of(leaf));
        XmlNode root = new XmlNode(null, "Root", List.of(), List.of(inner));
        host.putDocument(LAYOUT, root);

        XmlResourceParser parser = new Context().getResources().getLayout(LAYOUT);

        assertEquals(XmlPullParser.START_DOCUMENT, parser.getEventType());
        assertEquals(XmlPullParser.START_TAG, parser.next());
        assertEquals("Root", parser.getName());
        assertEquals(0, parser.getDepth());
        assertEquals(XmlPullParser.START_TAG, parser.next());
        assertEquals("Inner", parser.getName());
        assertEquals(1, parser.getDepth());
        assertEquals(XmlPullParser.START_TAG, parser.next());
        assertEquals("Leaf", parser.getName());
        assertEquals(2, parser.getDepth());
        assertEquals(XmlPullParser.END_TAG, parser.next());
        assertEquals("Leaf", parser.getName());
        assertEquals(2, parser.getDepth());
        assertEquals(XmlPullParser.END_TAG, parser.next());
        assertEquals("Inner", parser.getName());
        assertEquals(1, parser.getDepth());
        assertEquals(XmlPullParser.END_TAG, parser.next());
        assertEquals("Root", parser.getName());
        assertEquals(0, parser.getDepth());
        assertEquals(XmlPullParser.END_DOCUMENT, parser.next());
    }

    @Test
    void elementExposesAttributeNamesAndResourceIds() {
        XmlNode node = new XmlNode(null, "View", List.of(
                new XmlNode.Attribute(null, "id", ATTR_ID,
                        TypedValue.TYPE_REFERENCE, 0x7F080001, null),
                new XmlNode.Attribute(ANDROID_NS, "text", ATTR_TEXT,
                        TypedValue.TYPE_STRING, 0, "hello")),
                List.of());
        host.putDocument(LAYOUT, node);

        XmlResourceParser parser = new Context().getResources().getLayout(LAYOUT);
        parser.next();

        assertEquals(2, parser.getAttributeCount());
        assertEquals("id", parser.getAttributeName(0));
        assertEquals(ATTR_ID, parser.getAttributeNameResource(0));
        assertEquals(0x7F080001, parser.getAttributeResourceValue(0, -1));
        assertEquals(ANDROID_NS, parser.getAttributeNamespace(1));
        assertEquals("hello", parser.getAttributeValue(1));
        assertEquals("hello", parser.getAttributeValue(ANDROID_NS, "text"));
        assertEquals(0, parser.getStyleAttribute());
    }

    @Test
    void styleAttributeReportsItsReference() {
        XmlNode node = new XmlNode(null, "View", List.of(
                new XmlNode.Attribute(null, "style", 0,
                        TypedValue.TYPE_REFERENCE, 0x7F010004, null)),
                List.of());
        host.putDocument(LAYOUT, node);

        XmlResourceParser parser = new Context().getResources().getLayout(LAYOUT);
        parser.next();

        assertEquals(0x7F010004, parser.getStyleAttribute());
    }

    @Test
    void unknownLayoutIdReportsTheGap() {
        UnsupportedOperationException error = assertThrows(
                UnsupportedOperationException.class,
                () -> new Context().getResources().getLayout(MISSING_LAYOUT));

        assertTrue(error.getMessage().contains("getXml(I)"), error.getMessage());
        assertTrue(error.getMessage().contains("id=7f030999"), error.getMessage());
    }
}
