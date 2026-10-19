package tartine.apk;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests {@link ResourceTable} against a hand-built arsc fixture (the
 * writer encodes independently of the reader — see {@link Fixtures}).
 */
class ResourceTableTest {

    private static byte[] fixture() {
        // drawable/icon: mdpi path + xxxhdpi path; color/accent: typed argb
        return Fixtures.arsc("com.example.test", List.of(
                new Fixtures.ArscType("drawable", List.of(
                        new Fixtures.ArscEntry("icon", List.of(
                                Fixtures.Row.file(160, "res/drawable-mdpi/icon.png"),
                                Fixtures.Row.file(640, "res/drawable-xxxhdpi/icon.png"))))),
                new Fixtures.ArscType("color", List.of(
                        new Fixtures.ArscEntry("accent", List.of(
                                Fixtures.Row.typed(0, BinaryXml.TYPE_COLOR_ARGB8,
                                        0xFF3366AA)))))));
    }

    /** Packed app-resource id: 0x7f-ttttxxxx with 1-based type index. */
    private static int ref(int typeIndex, int entryIndex) {
        return 0x7F000000 | (typeIndex << 16) | entryIndex;
    }

    @Test
    void listsValuesByIdAcrossConfigs() throws Exception {
        ResourceTable table = ResourceTable.parse(fixture());
        List<ResourceTable.ConfValue> values = table.byId(ref(1, 0));
        assertEquals(2, values.size());
        // First-seen config order is preserved.
        assertEquals(160, values.get(0).density());
        assertEquals(640, values.get(1).density());
        assertEquals(BinaryXml.TYPE_STRING, values.get(0).dataType());
        assertEquals(BinaryXml.TYPE_STRING, values.get(1).dataType());
    }

    @Test
    void resolvesFilePathsFromGlobalPool() throws Exception {
        ResourceTable table = ResourceTable.parse(fixture());
        List<ResourceTable.ConfValue> values = table.byId(ref(1, 0));
        assertEquals("res/drawable-mdpi/icon.png", table.string(values.get(0).data()));
        assertEquals("res/drawable-xxxhdpi/icon.png", table.string(values.get(1).data()));
    }

    @Test
    void resolvesByName() throws Exception {
        ResourceTable table = ResourceTable.parse(fixture());
        assertEquals(table.byId(ref(2, 0)), table.byName("color", "accent"));
        assertEquals(table.byId(ref(1, 0)).size(), table.byName("@drawable/icon").size());
    }

    @Test
    void parsesTypedColorData() throws Exception {
        ResourceTable table = ResourceTable.parse(fixture());
        List<ResourceTable.ConfValue> values = table.byId(ref(2, 0));
        assertEquals(1, values.size());
        assertEquals(BinaryXml.TYPE_COLOR_ARGB8, values.get(0).dataType());
        assertEquals(0xFF3366AA, values.get(0).data());
        // density-0 configs normalize to DENSITY_DEFAULT (0).
        assertEquals(0, values.get(0).density());
    }

    @Test
    void unknownPackedIdIsEmptyList() throws Exception {
        ResourceTable table = ResourceTable.parse(fixture());
        assertTrue(table.byId(ref(1, 7)).isEmpty());
        assertTrue(table.byName("mipmap", "missing").isEmpty());
    }

    @Test
    void packageNamesComeFromThePackageChunk() throws Exception {
        ResourceTable table = ResourceTable.parse(fixture());
        assertEquals("com.example.test", table.packageName());
    }
}

