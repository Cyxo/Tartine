package tartine.apk;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tartine.fw.ResValue;
import tartine.fw.XmlNode;

/**
 * Tests {@link ResourceHostAdapter} end to end against a fixture APK: the
 * hand-built {@link Fixtures} arsc + AXML drive value resolution, theme
 * lookups, XML parsing and asset access exactly as the installed app would.
 */
class ResourceHostAdapterTest {

    private static final int STYLE_APP = 0x7F010000;
    private static final int COLOR_PRIMARY = 0x7F020000;
    private static final int LAYOUT_MAIN = 0x7F030000;
    private static final int ATTR_TEXT = 0x0101014F;
    private static final int ATTR_LAYOUT_WIDTH = 0x010100F4;

    @TempDir
    Path dir;

    private ApkReader reader;
    private ResourceHostAdapter adapter;

    @BeforeEach
    void setUp() throws Exception {
        byte[] arsc = Fixtures.arsc("com.example.test", List.of(
                new Fixtures.ArscType("style", List.of(
                        new Fixtures.ArscEntry("AppTheme", List.of(
                                Fixtures.Row.bag(0, 0, List.of(
                                        Fixtures.MapRow.ref(ATTR_TEXT, COLOR_PRIMARY))))))),
                new Fixtures.ArscType("color", List.of(
                        new Fixtures.ArscEntry("primary", List.of(
                                Fixtures.Row.typed(0, BinaryXml.TYPE_COLOR_ARGB8,
                                        0xFF00FF00))))),
                new Fixtures.ArscType("layout", List.of(
                        new Fixtures.ArscEntry("main", List.of(
                                Fixtures.Row.file(0, "res/layout/main.xml")))))));
        byte[] layout = Fixtures.axml(Fixtures.AxNode.leaf("TextView",
                        Fixtures.AxAttr.android("layout_width",
                                BinaryXml.TYPE_DIMENSION, 0x00000001)),
                Map.of("layout_width", ATTR_LAYOUT_WIDTH));
        Path apk = dir.resolve("app.apk");
        Fixtures.zip(apk, Map.of(
                "resources.arsc", arsc,
                "res/layout/main.xml", layout,
                "assets/note.txt", "hi".getBytes(StandardCharsets.UTF_8)));
        reader = ApkReader.open(apk);
        ResourceTable table = ResourceTable.parse(reader.readEntry("resources.arsc"));
        adapter = new ResourceHostAdapter(table, reader,
                ResourceTable.DeviceConfig.any());
    }

    @AfterEach
    void tearDown() throws Exception {
        if (reader != null) {
            reader.close();
        }
    }

    @Test
    void resolvesTypedValues() {
        ResValue color = adapter.value(COLOR_PRIMARY);
        assertNotNull(color);
        assertEquals(BinaryXml.TYPE_COLOR_ARGB8, color.type);
        assertEquals(0xFF00FF00, color.data);
        assertNull(adapter.value(0x7F0200FF));
    }

    @Test
    void resolvesThemedAttributesThroughReferences() {
        ResValue value = adapter.attribute(ATTR_TEXT, new int[] {STYLE_APP});
        assertNotNull(value);
        assertEquals(0xFF00FF00, value.data);
        assertNull(adapter.attribute(0x01010099, new int[] {STYLE_APP}));
        assertNull(adapter.attribute(ATTR_TEXT, new int[0]));
    }

    @Test
    void parsesLayoutXmlWithAttributeResourceIds() {
        XmlNode root = adapter.xml(LAYOUT_MAIN);
        assertNotNull(root);
        assertEquals("TextView", root.name);
        assertEquals(1, root.attributes.size());
        XmlNode.Attribute attr = root.attributes.get(0);
        assertEquals("layout_width", attr.name);
        assertEquals(ATTR_LAYOUT_WIDTH, attr.resourceId);
        assertEquals(Fixtures.ANDROID_NS, attr.namespace);
    }

    @Test
    void namesAndIdentifiersRoundTrip() {
        assertEquals("layout", adapter.resourceName(LAYOUT_MAIN, 1));
        assertEquals("main", adapter.resourceName(LAYOUT_MAIN, 2));
        assertEquals("com.example.test", adapter.resourceName(LAYOUT_MAIN, 0));
        assertEquals(LAYOUT_MAIN, adapter.identifier("main", "layout",
                "com.example.test"));
        assertEquals(0, adapter.identifier("main", "layout", "other.pkg"));
    }

    @Test
    void assetsAreReadAndListed() {
        assertArrayEquals("hi".getBytes(StandardCharsets.UTF_8),
                adapter.asset("assets/note.txt"));
        assertNull(adapter.asset("assets/missing.txt"));
        assertArrayEquals(new String[] {"note.txt"}, adapter.assetList("assets"));
        assertNull(adapter.assetList("nope"));
    }
}
