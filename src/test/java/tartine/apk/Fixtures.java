package tartine.apk;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Test-only fixture builders: hand-encoded binary XML and
 * {@code resources.arsc} — an independent <em>writer</em> that keeps the
 * {@link BinaryXml}/{@link ResourceTable} readers honest, plus the zip
 * assembly they hang inside. Encoding shape follows the manifest writer
 * in {@link ApkReaderTest} (UTF-16 pools, unsorted); chunk layouts mirror
 * androidfw/ResourceTypes.h.
 */
final class Fixtures {
    private Fixtures() {
    }

    static final String ANDROID_NS = "http://schemas.android.com/apk/res/android";
    private static final int NO_ENTRY = 0xFFFFFFFF;

    // ---- binary XML ------------------------------------------------------

    /**
     * Attribute for the writer. {@code data} is the typed value word; for
     * STRING attributes the encoder patches it to the raw value's pool
     * index (readers rebuild the text from it). Android-namespaced
     * attributes carry the android URI, mirroring aapt2 output.
     */
    record AxAttr(String name, int dataType, int data, String raw,
            boolean androidNamespace) {

        static AxAttr android(String name, int dataType, int data) {
            return new AxAttr(name, dataType, data, null, true);
        }

        /** Plain (no-namespace) string attribute. */
        static AxAttr plain(String name, String raw) {
            return new AxAttr(name, BinaryXml.TYPE_STRING, -1, raw, false);
        }

        /** Android-namespaced attribute carrying raw text alongside data. */
        static AxAttr androidRaw(String name, String raw, int dataType,
                int data) {
            return new AxAttr(name, dataType, data, raw, true);
        }
    }

    record AxNode(String name, List<AxAttr> attrs, List<AxNode> children) {

        static AxNode leaf(String name, AxAttr... attrs) {
            return new AxNode(name, List.of(attrs), List.of());
        }

        static AxNode node(String name, List<AxNode> children, AxAttr... attrs) {
            return new AxNode(name, List.of(attrs), List.copyOf(children));
        }
    }

    /** Encodes one element tree as binary XML (no text/comment chunks). */
    static byte[] axml(AxNode root) {
        LinkedHashSet<String> strings = new LinkedHashSet<>();
        strings.add(ANDROID_NS);
        collectStrings(root, strings);
        ByteBuffer out = ByteBuffer.allocate(16384).order(ByteOrder.LITTLE_ENDIAN);
        putU16(out, 0x0003);
        putU16(out, 8);
        int fileSizePos = out.position();
        putU32(out, 0);
        writeUtf16Pool(out, new ArrayList<>(strings));
        emitElement(out, strings, root);
        out.putInt(fileSizePos, out.position());
        byte[] result = new byte[out.position()];
        out.flip();
        out.get(result);
        return result;
    }

    private static void collectStrings(AxNode node, LinkedHashSet<String> strings) {
        strings.add(node.name());
        for (AxAttr attr : node.attrs()) {
            strings.add(attr.name());
            if (attr.raw() != null) {
                strings.add(attr.raw());
            }
        }
        for (AxNode child : node.children()) {
            collectStrings(child, strings);
        }
    }

    private static int stringIndex(LinkedHashSet<String> strings, String value) {
        int index = 0;
        for (String candidate : strings) {
            if (candidate.equals(value)) {
                return index;
            }
            index++;
        }
        throw new IllegalStateException("string not in pool: " + value);
    }

    private static void emitElement(ByteBuffer out, LinkedHashSet<String> strings,
            AxNode node) {
        int nameIdx = stringIndex(strings, node.name());
        startElement(out, nameIdx, strings, node.attrs());
        for (AxNode child : node.children()) {
            emitElement(out, strings, child);
        }
        endElement(out, nameIdx);
    }

    private static void startElement(ByteBuffer out, int nameIdx,
            LinkedHashSet<String> strings, List<AxAttr> attrs) {
        int chunkStart = out.position();
        putU16(out, 0x0102);
        putU16(out, 16);
        int sizePos = out.position();
        putU32(out, 0);
        putU32(out, 1); // lineNumber
        putU32(out, -1); // comment
        putU32(out, -1); // ns
        putU32(out, nameIdx);
        putU16(out, 20); // attributeStart
        putU16(out, 20); // attributeSize
        putU16(out, attrs.size());
        putU16(out, 0xFFFF); // idIndex
        putU16(out, 0xFFFF); // classIndex
        putU16(out, 0xFFFF); // styleIndex
        for (AxAttr attr : attrs) {
            putU32(out, attr.androidNamespace()
                    ? stringIndex(strings, ANDROID_NS) : -1); // ns
            putU32(out, stringIndex(strings, attr.name())); // name
            putU32(out, attr.raw() == null
                    ? -1 : stringIndex(strings, attr.raw())); // rawValue
            putU16(out, 8); // value size
            out.put((byte) 0); // res0
            out.put((byte) attr.dataType());
            int data = attr.dataType() == BinaryXml.TYPE_STRING
                    ? stringIndex(strings, attr.raw()) : attr.data();
            putU32(out, data);
        }
        out.putInt(sizePos, out.position() - chunkStart);
    }

    private static void endElement(ByteBuffer out, int nameIdx) {
        putU16(out, 0x0103);
        putU16(out, 16);
        putU32(out, 24);
        putU32(out, 1);
        putU32(out, -1);
        putU32(out, -1);
        putU32(out, nameIdx);
    }

    // ---- resources.arsc --------------------------------------------------

    /** One typed value under one configuration. */
    record Row(int density, int sdkVersion, int dataType, int data,
            String path) {

        /** Inline typed value (color, reference, ...). */
        static Row typed(int density, int dataType, int data) {
            return new Row(density, 0, dataType, data, null);
        }

        /** File-path string value backed by the global pool. */
        static Row file(int density, String path) {
            return new Row(density, 0, BinaryXml.TYPE_STRING, -1, path);
        }

        static Row fileSdk(int density, int sdkVersion, String path) {
            return new Row(density, sdkVersion, BinaryXml.TYPE_STRING, -1, path);
        }
    }

    record ArscEntry(String name, List<Row> rows) {
    }

    record ArscType(String name, List<ArscEntry> entries) {
    }

    /** Builds a minimal resources.arsc: one package (id 0x7f), dense. */
    static byte[] arsc(String packageName, List<ArscType> types) {
        LinkedHashSet<String> globalStrings = new LinkedHashSet<>();
        LinkedHashSet<String> typeNames = new LinkedHashSet<>();
        List<String> keyNames = new ArrayList<>();
        for (ArscType type : types) {
            typeNames.add(type.name());
            for (ArscEntry entry : type.entries()) {
                if (!keyNames.contains(entry.name())) {
                    keyNames.add(entry.name());
                }
                for (Row row : entry.rows()) {
                    if (row.path() != null) {
                        globalStrings.add(row.path());
                    }
                }
            }
        }
        List<String> globalList = new ArrayList<>(globalStrings);
        Map<String, Integer> globalIds = new LinkedHashMap<>();
        for (int index = 0; index < globalList.size(); index++) {
            globalIds.put(globalList.get(index), index);
        }
        ByteBuffer out = ByteBuffer.allocate(65536).order(ByteOrder.LITTLE_ENDIAN);
        // ResTable_header: header(8) + packageCount(4), headerSize 12.
        putU16(out, 0x0002);
        putU16(out, 12);
        int tableSizePos = out.position();
        putU32(out, 0);
        putU32(out, 1); // packageCount
        writeUtf16Pool(out, globalList);

        int packageStart = out.position();
        // ResTable_package: header(8) id(4) name[128 u16] + 4 u32 = 288.
        putU16(out, 0x0200);
        putU16(out, 288);
        int packageSizePos = out.position();
        putU32(out, 0);
        putU32(out, 0x7F); // package id
        for (int unit = 0; unit < 128; unit++) {
            out.putChar(unit < packageName.length()
                    ? packageName.charAt(unit) : '\0');
        }
        putU32(out, 288); // typeStringsOff
        putU32(out, typeNames.size()); // lastPublicType
        int keyStringsOffPos = out.position();
        putU32(out, 0);
        putU32(out, keyNames.size()); // lastPublicKey
        while (out.position() - packageStart < 288) {
            out.put((byte) 0);
        }
        List<String> typeList = new ArrayList<>(typeNames);
        writeUtf16Pool(out, typeList);
        out.putInt(keyStringsOffPos, out.position() - packageStart);
        writeUtf16Pool(out, keyNames);
        for (ArscType type : types) {
            int typeId = typeList.indexOf(type.name()) + 1;
            writeTypeChunk(out, typeId, type.entries(), keyNames, globalIds);
        }
        int end = out.position();
        out.putInt(packageSizePos, end - packageStart);
        out.putInt(tableSizePos, end);
        byte[] result = new byte[out.position()];
        out.flip();
        out.get(result);
        return result;
    }

    /**
     * One ResTable_type chunk per (type, configuration) group: type header
     * (20 bytes), ResTable_config (28 bytes at +20), offsets table
     * (u32 per entry at +48), entries (ResTable_entry + Res_value).
     */
    private static void writeTypeChunk(ByteBuffer out, int typeId,
            List<ArscEntry> entries, List<String> keyNames,
            Map<String, Integer> globalIds) {
        int entryCount = entries.size();
        // Config groups keyed by (density, sdk), first-seen order.
        LinkedHashSet<String> configKeys = new LinkedHashSet<>();
        for (ArscEntry entry : entries) {
            for (Row row : entry.rows()) {
                configKeys.add(row.density() + ":" + row.sdkVersion());
            }
        }
        for (String configKey : configKeys) {
            String[] parts = configKey.split(":");
            int density = Integer.parseInt(parts[0]);
            int sdkVersion = Integer.parseInt(parts[1]);
            int chunkStart = out.position();
            putU16(out, 0x0201);
            putU16(out, 20);
            int sizePos = out.position();
            putU32(out, 0);
            out.put((byte) typeId);
            out.put((byte) 0); // res0
            putU16(out, 0); // flags: dense
            putU32(out, entryCount);
            putU32(out, 0); // entriesStart, patched below
            putU32(out, 28); // configSize: struct spans +20..+47
            // ResTable_config: the struct (including its size field)
            // begins at +20; density sits at struct offset 14 (+34) and
            // sdkVersion at struct offset 24 (+44).
            int structStart = out.position() - 4;
            for (int zero = 0; zero < 6; zero++) {
                putU32(out, 0); // struct offsets 4..27
            }
            out.putShort(structStart + 14, (short) density);
            out.putShort(structStart + 24, (short) sdkVersion);
            int[] offsets = new int[entryCount];
            Arrays.fill(offsets, NO_ENTRY);
            ByteBuffer entriesBuf = ByteBuffer.allocate(entryCount * 16 + 16)
                    .order(ByteOrder.LITTLE_ENDIAN);
            for (int index = 0; index < entryCount; index++) {
                Row row = matchingRow(entries.get(index), density, sdkVersion);
                if (row == null) {
                    continue;
                }
                offsets[index] = entriesBuf.position();
                entriesBuf.putShort((short) 8); // entry size
                entriesBuf.putShort((short) 0); // entry flags: not complex
                entriesBuf.putInt(keyNames.indexOf(entries.get(index).name()));
                entriesBuf.putShort((short) 8); // Res_value size
                entriesBuf.put((byte) 0); // res0
                entriesBuf.put((byte) row.dataType());
                entriesBuf.putInt(row.path() == null
                        ? row.data() : globalIds.get(row.path()));
            }
            int entriesStart = 48 + entryCount * 4; // chunk-relative
            out.putInt(chunkStart + 16, entriesStart);
            for (int offset : offsets) {
                out.putInt(offset);
            }
            entriesBuf.flip();
            byte[] body = new byte[entriesBuf.remaining()];
            entriesBuf.get(body);
            out.put(body);
            out.putInt(sizePos, out.position() - chunkStart);
        }
    }

    private static Row matchingRow(ArscEntry entry, int density, int sdk) {
        for (Row row : entry.rows()) {
            if (row.density() == density && row.sdkVersion() == sdk) {
                return row;
            }
        }
        return null;
    }

    // ---- shared pool writer ----------------------------------------------

    /** Writes a UTF-16 string pool chunk; returns its byte size. */
    private static int writeUtf16Pool(ByteBuffer out, List<String> strings) {
        int chunkStart = out.position();
        putU16(out, 0x0001);
        putU16(out, 28);
        int poolSizePos = out.position();
        putU32(out, 0);
        putU32(out, strings.size());
        putU32(out, 0); // styleCount
        putU32(out, 0); // flags: UTF-16, unsorted
        int stringsStartPos = out.position();
        putU32(out, 0);
        putU32(out, 0); // stylesStart
        int offsetsPos = out.position();
        for (int i = 0; i < strings.size(); i++) {
            putU32(out, 0);
        }
        int stringsStart = out.position() - chunkStart;
        out.putInt(stringsStartPos, stringsStart);
        List<byte[]> encoded = new ArrayList<>();
        for (String s : strings) {
            encoded.add(encodeUtf16(s));
        }
        for (int i = 0; i < encoded.size(); i++) {
            out.putInt(offsetsPos + i * 4,
                    (out.position() - chunkStart) - stringsStart);
            out.put(encoded.get(i));
        }
        while (out.position() % 4 != 0) {
            out.put((byte) 0);
        }
        out.putInt(poolSizePos, out.position() - chunkStart);
        return out.position() - chunkStart;
    }

    private static byte[] encodeUtf16(String s) {
        ByteBuffer buf = ByteBuffer.allocate(2 + s.length() * 2 + 2)
                .order(ByteOrder.LITTLE_ENDIAN);
        buf.putShort((short) s.length());
        for (int i = 0; i < s.length(); i++) {
            buf.putChar(s.charAt(i));
        }
        buf.putShort((short) 0);
        byte[] result = new byte[buf.position()];
        buf.flip();
        buf.get(result);
        return result;
    }

    private static void putU16(ByteBuffer out, int value) {
        out.putShort((short) value);
    }

    private static void putU32(ByteBuffer out, int value) {
        out.putInt(value);
    }

    // ---- zip -------------------------------------------------------------

    /** Assembles fixture files into an unsigned APK-shaped zip. */
    static void zip(Path target, Map<String, byte[]> entries) throws Exception {
        try (ZipOutputStream stream = new ZipOutputStream(Files.newOutputStream(target))) {
            for (Map.Entry<String, byte[]> entry : entries.entrySet()) {
                stream.putNextEntry(new ZipEntry(entry.getKey()));
                stream.write(entry.getValue());
                stream.closeEntry();
            }
        }
    }
}
