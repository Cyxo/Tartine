package tartine.apk;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal binary reader for {@code resources.arsc} (the resource table).
 *
 * <p>Model follows androidfw/ResourceTypes.h: one table chunk, one global
 * string pool, then one package chunk per package id (0x7f for the app).
 * Every package lists its types ({@code mipmap}, {@code drawable}, ...)
 * and entry names in per-package string pools, followed by one
 * {@code ResTable_type} chunk per (type, configuration) pair holding the
 * entry values (file paths, inline colors, or references). This gives
 * manifest references like {@code 0x7f080000} (package, type index, entry
 * index) a name-based {@code (@mipmap/ic_launcher)} and value-based view.
 *
 * <p>Configurations are reduced to the fields the icon picker needs
 * (density, declared version) — locale etc. are ignored, same as aapt2's
 * dump when read with --settings.
 */
public final class ResourceTable {
    // Chunk types (androidfw/ResourceTypes.h).
    private static final int RES_TABLE_TYPE = 0x0002;
    private static final int RES_STRING_POOL_TYPE = 0x0001;
    private static final int RES_TABLE_PACKAGE_TYPE = 0x0200;
    private static final int RES_TABLE_TYPE_TYPE = 0x0201;

    private static final int UTF8_FLAG = 1 << 8;
    private static final int NO_ENTRY = -1;
    // ResTable_entry flag: entry carries multiple bag values, not one value.
    private static final int FLAG_COMPLEX = 0x0001;
    // ResTable_type flags.
    private static final int FLAG_SPARSE = 0x0001;
    private static final int FLAG_OFFSET16 = 0x0002;

    // ResTable_config density constants.
    static final int DENSITY_DEFAULT = 0;
    static final int DENSITY_NONE = 0xffff;

    private final List<String> globalStrings = new ArrayList<>();
    private final Map<Long, List<ConfValue>> byId = new LinkedHashMap<>();
    private final Map<String, List<ConfValue>> byName = new LinkedHashMap<>();
    private String packageName = "";

    /** One (type, entry) value under one configuration. */
    public record ConfValue(
            /** Enclosing type name, e.g. {@code mipmap}. */
            String type,
            /** Entry name from the package key pool, e.g. {@code ic_launcher}. */
            String entry,
            /** Configuration density (0 = default/any). */
            int density,
            /** Declared version (0 = none). */
            int sdkVersion,
            /** Res_value dataType (raw). */
            int dataType,
            /** Res_value data word (raw). */
            int data) {
    }

    private ResourceTable() {
    }

    public static ResourceTable parse(byte[] data) throws IOException {
        ByteBuffer buf = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        ResourceTable table = new ResourceTable();
        int rootStart = buf.position();
        if (buf.remaining() < 12) {
            throw new IOException("too short for resources.arsc");
        }
        int chunkType = u16(buf);
        u16(buf); // header size
        int chunkSize = size(buf);
        if (chunkType != RES_TABLE_TYPE || chunkSize > buf.remaining() + 12) {
            throw new IOException("not a resource table chunk");
        }
        size(buf); // packageCount, informational (dynamic tables may lie)
        int tableEnd = rootStart + chunkSize;
        buf.position(rootStart + Math.max(12, buf.position() - rootStart));
        while (buf.position() < tableEnd - 8) {
            int start = buf.position();
            int type = u16(buf);
            u16(buf); // header size
            int bodySize = size(buf);
            if (bodySize <= 8) {
                break;
            }
            int end = start + bodySize;
            if (type == RES_STRING_POOL_TYPE) {
                table.globalStrings.addAll(readStringPool(data, buf, start, end));
            } else if (type == RES_TABLE_PACKAGE_TYPE) {
                table.readPackage(data, buf, start, end);
            }
            buf.position(end);
        }
        return table;
    }

    private void readPackage(byte[] data, ByteBuffer buf, int start, int end)
            throws IOException {
        if (buf.remaining() < 12) {
            throw new IOException("truncated package header");
        }
        int id = buf.getInt();
        byte[] nameBytes = new byte[256];
        buf.get(nameBytes);
        int typeStringsOff = buf.getInt();
        buf.getInt(); // lastPublicType, informational
        int keyStringsOff = buf.getInt();
        buf.getInt(); // lastPublicKey, informational
        if (typeStringsOff >= end - start || keyStringsOff >= end - start) {
            throw new IOException("bad package string pool offsets");
        }
        packageName = utf16Ascii(nameBytes);
        int headerSize = u16(data, start + 2);
        if (headerSize <= 0 || start + headerSize > end) {
            throw new IOException("bad package header size");
        }
        // The pools live inside the package chunk; walk the body by chunk.
        buf.position(start + headerSize);
        while (buf.position() < end - 8) {
            int chunkStart = buf.position();
            int type = u16(buf);
            u16(buf); // header size
            int size = size(buf);
            if (size <= 8 || chunkStart + size > end) {
                break;
            }
            int chunkEnd = chunkStart + size;
            if (type == RES_STRING_POOL_TYPE) {
                if (chunkStart == start + typeStringsOff) {
                    typeNames = readStringPool(data, buf, chunkStart, chunkEnd);
                } else if (chunkStart == start + keyStringsOff) {
                    keyNames = readStringPool(data, buf, chunkStart, chunkEnd);
                }
                // Any other pool (extra sources) is skipped.
            } else if (type == RES_TABLE_TYPE_TYPE) {
                readType(data, buf, chunkStart, chunkEnd, id);
            }
            buf.position(chunkEnd);
        }
    }

    private List<String> typeNames = List.of();
    private List<String> keyNames = List.of();

    private void readType(byte[] data, ByteBuffer buf, int start, int end, int packageId)
            throws IOException {
        if (buf.remaining() < 20) {
            throw new IOException("truncated type chunk");
        }
        int typeId = buf.get() & 0xFF;
        buf.get(); // res0
        int flags = buf.getShort() & 0xFFFF;
        int entryCount = buf.getInt();
        int entriesStart = buf.getInt();
        int configSize = buf.getInt();
        if (configSize < 8 || 20 + configSize > end - start) {
            throw new IOException("bad config size");
        }
        // ResTable_config fields we model: density (offset 14), sdkVersion
        // (offset 24, only when the config actually carries it).
        int configStart = start + 20;
        int density = configSize >= 16 ? u16(data, configStart + 14) : DENSITY_DEFAULT;
        int sdkVersion = configSize >= 28 ? u16(data, configStart + 24) : 0;
        int offsetsStart = start + 20 + configSize;
        int[] indexes = new int[entryCount];
        int[] offsets = new int[entryCount];
        buf.position(offsetsStart);
        if ((flags & FLAG_SPARSE) != 0) {
            // uint16 pairs: (entryIndex << 15) | (offset / 4).
            for (int i = 0; i < entryCount; i++) {
                int packed = u16(buf);
                indexes[i] = packed >>> 15;
                offsets[i] = packed == 0xFFFF ? NO_ENTRY : (packed & 0x7FFF) << 2;
            }
        } else if ((flags & FLAG_OFFSET16) != 0) {
            for (int i = 0; i < entryCount; i++) {
                int packed = u16(buf);
                indexes[i] = i;
                offsets[i] = packed == 0xFFFF ? NO_ENTRY : packed << 2;
            }
        } else {
            for (int i = 0; i < entryCount; i++) {
                int offset = buf.getInt();
                indexes[i] = i;
                offsets[i] = offset == 0xFFFFFFFF ? NO_ENTRY : offset;
            }
        }
        if (typeId <= 0 || typeId - 1 >= typeNames.size()) {
            return; // unnamed/extra type: nothing addressable
        }
        String typeName = typeNames.get(typeId - 1);
        for (int i = 0; i < entryCount; i++) {
            int entryIndex = indexes[i];
            if (offsets[i] == NO_ENTRY) {
                continue;
            }
            int entryStart = start + entriesStart + offsets[i];
            if (entryStart + 8 > end) {
                throw new IOException("entry overruns type chunk");
            }
            int entrySize = u16(data, entryStart);
            int entryFlags = u16(data, entryStart + 2);
            int key = (int) u32(data, entryStart + 4);
            if ((entryFlags & FLAG_COMPLEX) != 0 || entrySize < 8) {
                continue; // bag entries: not addressable by name/value here
            }
            if (key < 0 || key >= keyNames.size()) {
                throw new IOException("bad key index " + key);
            }
            String entryName = keyNames.get(key);
            // Res_value follows the entry header.
            int valuePos = entryStart + entrySize;
            if (valuePos + 8 > end) {
                throw new IOException("value overruns type chunk");
            }
            int dataType = data[valuePos + 3] & 0xFF;
            int valueData = (int) u32(data, valuePos + 4);
            ConfValue value = new ConfValue(typeName, entryName,
                    normalizeDensity(density), sdkVersion, dataType, valueData);
            long packedId = ((long) (packageId & 0xFF) << 24)
                    | ((long) typeId << 16) | entryIndex;
            byId.computeIfAbsent(packedId, k -> new ArrayList<>()).add(value);
            byName.computeIfAbsent(typeName + "/" + entryName, k -> new ArrayList<>())
                    .add(value);
        }
    }

    private static int normalizeDensity(int density) {
        return density == DENSITY_NONE || density <= 0 ? DENSITY_DEFAULT : density;
    }

    /** Values for a packed resource id ({@code 0xpptteeee}), any config. */
    public List<ConfValue> byId(long packedId) {
        List<ConfValue> values = byId.get(packedId);
        return values == null ? List.of() : values;
    }

    /** Values for a type/entry name pair, e.g. {@code byName("mipmap", "ic_launcher")}. */
    public List<ConfValue> byName(String type, String entry) {
        List<ConfValue> values = byName.get(type + "/" + entry);
        return values == null ? List.of() : values;
    }

    /** Value for a string resource reference like {@code @mipmap/ic_launcher}. */
    public List<ConfValue> byName(String ref) {
        if (ref == null || ref.length() < 3 || ref.charAt(0) != '@') {
            return List.of();
        }
        int slash = ref.indexOf('/');
        if (slash <= 0 || slash >= ref.length() - 1) {
            return List.of();
        }
        String type = ref.substring(1, slash);
        String entry = ref.substring(slash + 1);
        if (type.equals("android")) {
            return List.of(); // system resources are not visible to apps
        }
        return byName(type, entry);
    }

    /** Global string pool entry (file paths and names). */
    public String string(int index) {
        if (index < 0 || index >= globalStrings.size()) {
            return null;
        }
        return globalStrings.get(index);
    }

    public String packageName() {
        return packageName;
    }

    // ---- chunk readers shared with BinaryXml ----------------------------

    private static List<String> readStringPool(byte[] data, ByteBuffer buf,
            int chunkStart, int chunkEnd) throws IOException {
        int stringCount = buf.getInt();
        int styleCount = buf.getInt();
        int flags = buf.getInt();
        int stringsStart = buf.getInt();
        buf.getInt(); // stylesStart
        boolean utf8 = (flags & UTF8_FLAG) != 0;
        int[] offsets = new int[stringCount];
        for (int i = 0; i < stringCount; i++) {
            offsets[i] = buf.getInt();
        }
        int dataStart = chunkStart + stringsStart;
        List<String> pool = new ArrayList<>(Math.max(0, stringCount));
        for (int i = 0; i < stringCount; i++) {
            int pos = dataStart + offsets[i];
            pool.add(pos > 0 && pos < chunkEnd
                    ? readString(data, pos, utf8) : "");
        }
        return pool;
    }

    private static String readString(byte[] data, int pos, boolean utf8)
            throws IOException {
        if (utf8) {
            // UTF-8 pools carry two varint lengths: character count first,
            // then encoded byte count; the text follows the second.
            int[] cursor = new int[1];
            int charLen = readLength8(data, pos, cursor);
            if (charLen < 0) {
                throw new IOException("bad utf8 string header");
            }
            int byteLen = readLength8(data, cursor[0], cursor);
            if (byteLen < 0 || cursor[0] + byteLen > data.length) {
                throw new IOException("utf8 string overruns pool data");
            }
            return new String(data, cursor[0], byteLen, StandardCharsets.UTF_8);
        }
        int len = u16(data, pos);
        pos += 2;
        if ((len & 0x8000) != 0) {
            len = ((len & 0x7FFF) << 16) | u16(data, pos);
            pos += 2;
        }
        if (pos + len * 2 > data.length) {
            throw new IOException("string overruns pool data");
        }
        char[] chars = new char[len];
        for (int i = 0; i < len; i++) {
            chars[i] = (char) u16(data, pos + i * 2);
        }
        return new String(chars);
    }

    private static int readLength8(byte[] data, int pos, int[] cursor)
            throws IOException {
        if (pos >= data.length) {
            throw new IOException("string overruns pool data");
        }
        int first = data[pos] & 0xFF;
        if ((first & 0x80) == 0) {
            cursor[0] = pos + 1;
            return first;
        }
        if (pos + 1 >= data.length) {
            throw new IOException("string overruns pool data");
        }
        cursor[0] = pos + 2;
        return ((first & 0x7F) << 8) | (data[pos + 1] & 0xFF);
    }

    private static int u16(ByteBuffer buf) throws IOException {
        if (buf.remaining() < 2) {
            throw new IOException("truncated");
        }
        return buf.getShort() & 0xFFFF;
    }

    private static int u16(byte[] data, int pos) {
        if (pos < 0 || pos + 1 >= data.length) {
            return 0;
        }
        return ((data[pos + 1] & 0xFF) << 8) | (data[pos] & 0xFF);
    }

    private static long u32(byte[] data, int pos) {
        if (pos < 0 || pos + 3 >= data.length) {
            return 0;
        }
        return ((long) (data[pos + 3] & 0xFF) << 24)
                | ((data[pos + 2] & 0xFF) << 16)
                | ((data[pos + 1] & 0xFF) << 8)
                | (data[pos] & 0xFF);
    }

    private static int size(ByteBuffer buf) throws IOException {
        if (buf.remaining() < 4) {
            throw new IOException("truncated");
        }
        int value = buf.getInt();
        if (value < 0) {
            throw new IOException("bad size or offset");
        }
        return value;
    }

    private static String utf16Ascii(byte[] bytes) {
        // ResTable_package.name is UTF-16LE, padded; take while printable.
        StringBuilder out = new StringBuilder();
        for (int pos = 0; pos + 1 < bytes.length && bytes[pos + 1] == 0; pos += 2) {
            int c = bytes[pos] & 0xFF;
            if (c == 0) {
                break;
            }
            if (c >= 0x20 && c < 0x7F) {
                out.append((char) c);
            } else {
                return out.toString();
            }
        }
        return out.toString();
    }
}
