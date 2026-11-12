package tartine.apk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

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

    @Test
    void parsesBagEntriesIntoNameValuePairs() throws Exception {
        int attrPrimary = 0x01010001;
        int attrLabel = 0x01010002;
        ResourceTable table = ResourceTable.parse(Fixtures.arsc("com.example.test",
                List.of(new Fixtures.ArscType("style", List.of(
                        new Fixtures.ArscEntry("AppTheme", List.of(
                                Fixtures.Row.bag(0, 0, List.of(
                                        Fixtures.MapRow.value(attrPrimary,
                                                BinaryXml.TYPE_COLOR_ARGB8, 0xFF112233),
                                        Fixtures.MapRow.text(attrLabel, "hello"))))))))));
        long id = table.idFor("style", "AppTheme");
        assertEquals(0x7F010000L, id);
        assertEquals("style", table.typeName(id));
        assertEquals("AppTheme", table.entryName(id));
        ResourceTable.Bag bag = table.bag(id, ResourceTable.DeviceConfig.any());
        assertEquals(2, bag.values().size());
        assertEquals(attrPrimary, bag.values().get(0).key());
        assertEquals(0xFF112233, bag.values().get(0).data());
        assertEquals("hello", bag.values().get(1).string());
    }

    @Test
    void bagParentsAreRecorded() throws Exception {
        ResourceTable table = ResourceTable.parse(Fixtures.arsc("com.example.test",
                List.of(new Fixtures.ArscType("style", List.of(
                        new Fixtures.ArscEntry("Child", List.of(
                                Fixtures.Row.bag(0, 0x7F010001, List.of()))))))));
        ResourceTable.Bag bag = table.bag("style", "Child",
                ResourceTable.DeviceConfig.any());
        assertEquals(0x7F010001L, bag.parent());
    }

    @Test
    void selectsTheDensityClosestToTheDevice() throws Exception {
        ResourceTable table = ResourceTable.parse(fixture());
        // An exact bucket wins.
        ResourceTable.ConfValue exact = table.value(ref(1, 0),
                new ResourceTable.DeviceConfig(640, 35));
        assertEquals(640, exact.density());
        // Otherwise the smallest bucket at or above the device (downscale).
        ResourceTable.ConfValue above = table.value(ref(1, 0),
                new ResourceTable.DeviceConfig(100, 35));
        assertEquals(160, above.density());
    }

    @Test
    void rejectsConfigsNeedingNewerPlatform() throws Exception {
        ResourceTable table = ResourceTable.parse(Fixtures.arsc("com.example.test",
                List.of(new Fixtures.ArscType("string", List.of(
                        new Fixtures.ArscEntry("label", List.of(
                                Fixtures.Row.fileSdk(0, 31, "res/values-v31/label"),
                                Fixtures.Row.fileSdk(0, 0, "res/values/label"))))))));
        ResourceTable.ConfValue older = table.value(table.idFor("string", "label"),
                new ResourceTable.DeviceConfig(160, 30));
        assertEquals(0, older.sdkVersion());
        ResourceTable.ConfValue newer = table.value(table.idFor("string", "label"),
                new ResourceTable.DeviceConfig(160, 31));
        assertEquals(31, newer.sdkVersion());
    }

    @Test
    void unknownIdsHaveNoValueOrName() throws Exception {
        ResourceTable table = ResourceTable.parse(fixture());
        assertNull(table.value(0x7F0100FFL, ResourceTable.DeviceConfig.any()));
        assertNull(table.entryName(0x7F0100FFL));
        assertEquals(0, table.idFor("style", "missing"));
    }
}

