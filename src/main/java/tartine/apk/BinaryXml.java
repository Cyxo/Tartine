package tartine.apk;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Minimal binary-XML (AXML) decoder for compiled manifests and layouts.
 *
 * <p>Understands the string pool (UTF-16 and MUTF-8) and start/end elements
 * with typed attributes. Anything else (namespace chunks, resource map,
 * CDATA) is skipped by chunk size so newer files still parse.
 */
public final class BinaryXml {
    private BinaryXml() {
    }

    private static final int RES_STRING_POOL_TYPE = 0x0001;
    private static final int RES_XML_TYPE = 0x0003;
    private static final int RES_XML_START_ELEMENT_TYPE = 0x0102;
    private static final int RES_XML_END_ELEMENT_TYPE = 0x0103;

    private static final int UTF8_FLAG = 1 << 8;
    private static final int NO_INDEX = -1;

    // Res_value data types (androidfw/ResourceTypes.h).
    private static final int TYPE_REFERENCE = 0x01;
    private static final int TYPE_STRING = 0x03;
    private static final int TYPE_FLOAT = 0x04;
    private static final int TYPE_INT_DEC = 0x10;
    private static final int TYPE_INT_HEX = 0x11;
    private static final int TYPE_INT_BOOLEAN = 0x12;

    /** A decoded attribute with its value resolved to text. */
    public static final class Attribute {
        public final String namespaceUri;
        public final String name;
        public final String value;

        Attribute(String namespaceUri, String name, String value) {
            this.namespaceUri = namespaceUri;
            this.name = name;
            this.value = value;
        }
    }

    /** A decoded element with attributes and children in document order. */
    public static final class Element {
        public final String namespaceUri;
        public final String name;
        public final List<Attribute> attributes = new ArrayList<>();
        public final List<Element> children = new ArrayList<>();

        Element(String namespaceUri, String name) {
            this.namespaceUri = namespaceUri;
            this.name = name;
        }

        /** Value of the first attribute with the given local name, or null. */
        public String attr(String name) {
            for (Attribute attribute : attributes) {
                if (attribute.name.equals(name)) {
                    return attribute.value;
                }
            }
            return null;
        }

        /** First child with the given local name, or null. */
        public Element child(String name) {
            for (Element element : children) {
                if (element.name.equals(name)) {
                    return element;
                }
            }
            return null;
        }
    }

    public static Element parse(byte[] data) throws IOException {
        if (data == null || data.length < 8) {
            throw new IOException("too short for binary XML");
        }
        ByteBuffer buf = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        if (u16(buf) != RES_XML_TYPE || u16(buf) != 8) {
            throw new IOException("not binary XML");
        }
        size(buf); // total size, informational
        List<String> pool = new ArrayList<>();
        Element root = null;
        Deque<Element> stack = new ArrayDeque<>();
        while (buf.remaining() >= 8) {
            int chunkStart = buf.position();
            int chunkType = u16(buf);
            u16(buf); // header size, implied by type
            int chunkSize = size(buf);
            if (chunkSize > buf.remaining() + 8) {
                throw new IOException("corrupt chunk at offset " + chunkStart);
            }
            int chunkEnd = chunkStart + chunkSize;
            switch (chunkType) {
                case RES_STRING_POOL_TYPE:
                    pool = readStringPool(data, buf, chunkStart, chunkEnd);
                    break;
                case RES_XML_START_ELEMENT_TYPE:
                    Element element = readStartElement(buf, pool);
                    if (stack.isEmpty()) {
                        if (root != null) {
                            throw new IOException("multiple root elements");
                        }
                        root = element;
                    } else {
                        stack.peek().children.add(element);
                    }
                    stack.push(element);
                    break;
                case RES_XML_END_ELEMENT_TYPE:
                    readEndElement(buf, pool, stack);
                    break;
                default:
                    break; // namespaces, resource map, CDATA: skip by size
            }
            buf.position(chunkEnd);
        }
        if (root == null) {
            throw new IOException("no root element");
        }
        if (!stack.isEmpty()) {
            throw new IOException("unbalanced elements");
        }
        return root;
    }

    private static List<String> readStringPool(byte[] data, ByteBuffer buf, int chunkStart, int chunkEnd)
            throws IOException {
        int stringCount = size(buf);
        int styleCount = size(buf);
        int flags = buf.getInt();
        int stringsStart = size(buf);
        size(buf); // stylesStart, styles unsupported
        boolean utf8 = (flags & UTF8_FLAG) != 0;
        int[] offsets = new int[stringCount];
        for (int i = 0; i < stringCount; i++) {
            offsets[i] = size(buf);
        }
        for (int i = 0; i < styleCount; i++) {
            size(buf);
        }
        int dataStart = chunkStart + stringsStart;
        if (dataStart < buf.position() || dataStart > chunkEnd) {
            throw new IOException("bad string pool data offset");
        }
        List<String> pool = new ArrayList<>(stringCount);
        for (int i = 0; i < stringCount; i++) {
            int pos = dataStart + offsets[i];
            if (pos < dataStart || pos >= chunkEnd) {
                throw new IOException("bad string offset");
            }
            pool.add(readString(data, pos, utf8));
        }
        return pool;
    }

    private static String readString(byte[] data, int pos, boolean utf8) throws IOException {
        if (utf8) {
            int[] afterCharLen = new int[1];
            readLength8(data, pos, afterCharLen);
            int[] afterByteLen = new int[1];
            int byteLen = readLength8(data, afterCharLen[0], afterByteLen);
            return decodeMutF8(data, afterByteLen[0], byteLen);
        }
        int len = u16(data, pos);
        pos += 2;
        if ((len & 0x8000) != 0) {
            len = ((len & 0x7FFF) << 16) | u16(data, pos);
            pos += 2;
        }
        if (pos + len * 2 > data.length) {
            throw new IOException("string overruns buffer");
        }
        char[] chars = new char[len];
        for (int i = 0; i < len; i++) {
            chars[i] = (char) u16(data, pos + i * 2);
        }
        return new String(chars);
    }

    private static int readLength8(byte[] data, int pos, int[] next) throws IOException {
        if (pos >= data.length) {
            throw new IOException("string overruns buffer");
        }
        int first = data[pos] & 0xFF;
        if ((first & 0x80) == 0) {
            next[0] = pos + 1;
            return first;
        }
        if (pos + 1 >= data.length) {
            throw new IOException("string overruns buffer");
        }
        next[0] = pos + 2;
        return ((first & 0x7F) << 8) | (data[pos + 1] & 0xFF);
    }

    private static String decodeMutF8(byte[] data, int pos, int byteLen) throws IOException {
        StringBuilder out = new StringBuilder();
        int end = pos + byteLen;
        if (pos < 0 || end > data.length) {
            throw new IOException("string overruns buffer");
        }
        while (pos < end) {
            int b = data[pos++] & 0xFF;
            if (b < 0x80) {
                out.append((char) b);
            } else if ((b & 0xE0) == 0xC0) {
                if (pos >= end) {
                    throw new IOException("bad MUTF-8");
                }
                int b2 = data[pos++] & 0xFF;
                out.append((char) (((b & 0x1F) << 6) | (b2 & 0x3F)));
            } else if ((b & 0xF0) == 0xE0) {
                if (pos + 1 >= end) {
                    throw new IOException("bad MUTF-8");
                }
                int b2 = data[pos++] & 0xFF;
                int b3 = data[pos++] & 0xFF;
                out.append((char) (((b & 0x0F) << 12) | ((b2 & 0x3F) << 6) | (b3 & 0x3F)));
            } else {
                throw new IOException("bad MUTF-8");
            }
        }
        return out.toString();
    }

    private static Element readStartElement(ByteBuffer buf, List<String> pool) throws IOException {
        size(buf); // lineNumber
        buf.getInt(); // comment
        int ns = buf.getInt();
        int name = buf.getInt();
        u16(buf); // attributeStart, always 20 in practice
        int attributeSize = u16(buf);
        if (attributeSize < 20) {
            throw new IOException("bad attribute size");
        }
        int attributeCount = u16(buf);
        u16(buf); // idIndex
        u16(buf); // classIndex
        u16(buf); // styleIndex
        Element element = new Element(poolString(pool, ns, ""), poolName(pool, name));
        for (int i = 0; i < attributeCount; i++) {
            int attrStart = buf.position();
            if (buf.remaining() < 20) {
                throw new IOException("truncated attribute");
            }
            int attrNs = buf.getInt();
            int attrName = buf.getInt();
            int rawValue = buf.getInt();
            u16(buf); // typed value size, always 8
            buf.get(); // res0
            int dataType = buf.get() & 0xFF;
            int data = buf.getInt();
            buf.position(attrStart + attributeSize);
            element.attributes.add(new Attribute(
                    poolString(pool, attrNs, ""), poolName(pool, attrName),
                    resolveValue(pool, rawValue, dataType, data)));
        }
        return element;
    }

    private static void readEndElement(ByteBuffer buf, List<String> pool, Deque<Element> stack)
            throws IOException {
        size(buf); // lineNumber
        buf.getInt(); // comment
        buf.getInt(); // ns
        int name = buf.getInt();
        if (stack.isEmpty()) {
            throw new IOException("unbalanced end element");
        }
        Element element = stack.pop();
        if (!element.name.equals(poolName(pool, name))) {
            throw new IOException("mismatched end element");
        }
    }

    private static String resolveValue(List<String> pool, int rawValue, int dataType, int data)
            throws IOException {
        switch (dataType) {
            case TYPE_STRING:
                return poolName(pool, data);
            case TYPE_INT_DEC:
                return Integer.toString(data);
            case TYPE_INT_HEX:
                return "0x" + Integer.toHexString(data);
            case TYPE_INT_BOOLEAN:
                return data != 0 ? "true" : "false";
            case TYPE_FLOAT:
                return Float.toString(Float.intBitsToFloat(data));
            case TYPE_REFERENCE:
                return "@0x" + Integer.toHexString(data);
            default:
                if (rawValue != NO_INDEX) {
                    return poolString(pool, rawValue, "0x" + Integer.toHexString(data));
                }
                return "0x" + Integer.toHexString(data);
        }
    }

    private static String poolString(List<String> pool, int index, String missing) throws IOException {
        if (index == NO_INDEX) {
            return missing;
        }
        if (index < 0 || index >= pool.size()) {
            throw new IOException("bad string index " + index);
        }
        return pool.get(index);
    }

    private static String poolName(List<String> pool, int index) throws IOException {
        if (index < 0 || index >= pool.size()) {
            throw new IOException("bad string index " + index);
        }
        return pool.get(index);
    }

    private static int u16(ByteBuffer buf) throws IOException {
        if (buf.remaining() < 2) {
            throw new IOException("truncated");
        }
        return buf.getShort() & 0xFFFF;
    }

    private static int u16(byte[] data, int pos) throws IOException {
        if (pos < 0 || pos + 1 >= data.length) {
            throw new IOException("string overruns buffer");
        }
        return ((data[pos + 1] & 0xFF) << 8) | (data[pos] & 0xFF);
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
}
