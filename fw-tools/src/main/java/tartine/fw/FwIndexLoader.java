package tartine.fw;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import tartine.dex.Descriptors;

/**
 * Dev-time loader: parses a platform {@code android.jar} (JVM classfiles) into
 * an {@link FwIndex}, which {@link SkeletonGen} drives skeleton emission from.
 *
 * <p>Lives in the {@code fw-tools} module so the runtime cannot reach SDK
 * parsing by accident: {@code android.jar} is a build input for regeneration
 * only, never a runtime dependency.
 */
public final class FwIndexLoader {
    private FwIndexLoader() {
    }

    /** Indexes every class in the jar (skips module/package info and multi-release overlays). */
    public static FwIndex loadJar(Path jar) throws IOException {
        FwIndex index = new FwIndex();
        try (ZipFile zip = new ZipFile(jar.toFile())) {
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();
                if (!name.endsWith(".class") || name.equals("module-info.class")
                        || name.equals("package-info.class")
                        || name.startsWith("META-INF/versions/")) {
                    continue;
                }
                try (InputStream in = zip.getInputStream(entry)) {
                    index.put(parse(in.readAllBytes()));
                }
            }
        }
        return index;
    }

    static FwIndex.FwClass parse(byte[] data) throws IOException {
        ByteBuffer buf = ByteBuffer.wrap(data).order(ByteOrder.BIG_ENDIAN);
        if (u4(buf) != 0xCAFEBABE) {
            throw new IOException("not a class file");
        }
        u2(buf); // minor
        u2(buf); // major
        Object[] pool = new Object[u2(buf)];
        for (int i = 1; i < pool.length; i++) {
            switch (u1(buf)) {
                case 1: {
                    int length = u2(buf);
                    if (buf.remaining() < length) {
                        throw new IOException("truncated Utf8");
                    }
                    byte[] bytes = new byte[length];
                    buf.get(bytes);
                    pool[i] = decodeMutF8(bytes);
                    break;
                }
                case 3:
                    pool[i] = u4(buf);
                    break;
                case 4:
                    pool[i] = Float.intBitsToFloat(u4(buf));
                    break;
                case 5:
                    pool[i] = buf.getLong();
                    i++;
                    break;
                case 6:
                    pool[i] = Double.longBitsToDouble(buf.getLong());
                    i++;
                    break;
                case 7:
                    pool[i] = u2(buf);
                    break;
                case 8:
                    // CONSTANT_String stores the Utf8 index; keep it distinct
                    // from Integer values (tag 3), which are values already.
                    pool[i] = new StringRef(u2(buf));
                    break;
                case 9:
                case 10:
                case 11:
                case 12:
                    skip(buf, 4);
                    break;
                case 15:
                    skip(buf, 3);
                    break;
                case 16:
                    skip(buf, 2);
                    break;
                case 17:
                case 18:
                    skip(buf, 4);
                    break;
                case 19:
                case 20:
                    skip(buf, 2);
                    break;
                default:
                    throw new IOException("bad constant tag");
            }
        }
        int classAccess = u2(buf);
        String type = className(pool, u2(buf));
        int superIndex = u2(buf);
        String superclass = superIndex == 0 ? null : className(pool, superIndex);
        List<String> interfaces = new ArrayList<>();
        int interfaceCount = u2(buf);
        for (int i = 0; i < interfaceCount; i++) {
            interfaces.add(className(pool, u2(buf)));
        }
        List<FwIndex.FwField> fields = new ArrayList<>();
        Map<String, Object> constants = new HashMap<>();
        int fieldCount = u2(buf);
        for (int i = 0; i < fieldCount; i++) {
            int access = u2(buf);
            String name = utf8(pool, u2(buf));
            String descriptor = utf8(pool, u2(buf));
            int attrCount = u2(buf);
            for (int a = 0; a < attrCount; a++) {
                String attrName = utf8(pool, u2(buf));
                int length = u4(buf);
                if (attrName.equals("ConstantValue") && length == 2) {
                    constants.put(name, constant(pool, u2(buf)));
                } else {
                    skip(buf, length);
                }
            }
            fields.add(new FwIndex.FwField(name, descriptor, access));
        }
        List<FwIndex.FwMethod> methods = new ArrayList<>();
        int methodCount = u2(buf);
        for (int i = 0; i < methodCount; i++) {
            int access = u2(buf);
            String name = utf8(pool, u2(buf));
            String descriptor = utf8(pool, u2(buf));
            String methodSignature = null;
            int attrCount = u2(buf);
            for (int a = 0; a < attrCount; a++) {
                String attrName = utf8(pool, u2(buf));
                int attrLength = u4(buf);
                if (attrName.equals("Signature") && attrLength == 2) {
                    methodSignature = utf8(pool, u2(buf));
                } else {
                    skip(buf, attrLength);
                }
            }
            int open = descriptor.indexOf('(');
            methods.add(new FwIndex.FwMethod(name, Descriptors.splitTypes(
                    descriptor.substring(open + 1, descriptor.indexOf(')', open))),
                    descriptor.substring(descriptor.indexOf(')') + 1), access,
                    methodSignature));
        }
        String signature = null;
        int classAttrCount = u2(buf);
        for (int i = 0; i < classAttrCount; i++) {
            String attrName = utf8(pool, u2(buf));
            int length = u4(buf);
            if (attrName.equals("Signature") && length == 2) {
                signature = utf8(pool, u2(buf));
            } else {
                skip(buf, length);
            }
        }
        return new FwIndex.FwClass(type, superclass, interfaces, methods, fields, constants,
                classAccess, signature);
    }

    private static String className(Object[] pool, int index) throws IOException {
        if (!(pool[index] instanceof Integer)) {
            throw new IOException("bad class entry");
        }
        return "L" + utf8(pool, (Integer) pool[index]) + ";";
    }

    private static String utf8(Object[] pool, int index) throws IOException {
        if (index <= 0 || index >= pool.length || !(pool[index] instanceof String)) {
            throw new IOException("bad string reference");
        }
        return (String) pool[index];
    }

    private static Object constant(Object[] pool, int index) throws IOException {
        if (index <= 0 || index >= pool.length) {
            throw new IOException("bad constant reference");
        }
        Object value = pool[index];
        if (value instanceof StringRef) {
            return utf8(pool, ((StringRef) value).index);
        }
        if (value instanceof Integer || value instanceof Long
                || value instanceof Float || value instanceof Double) {
            return value;
        }
        throw new IOException("bad constant value");
    }

    /** CONSTANT_String entry: index of the Utf8 bytes (distinct from Integer values). */
    private static final class StringRef {
        final int index;

        StringRef(int index) {
            this.index = index;
        }
    }

    private static String decodeMutF8(byte[] data) throws IOException {
        StringBuilder out = new StringBuilder();
        int pos = 0;
        while (pos < data.length) {
            int b = data[pos++] & 0xFF;
            if (b < 0x80) {
                out.append((char) b);
            } else if ((b & 0xE0) == 0xC0) {
                if (pos >= data.length) {
                    throw new IOException("bad MUTF-8");
                }
                int b2 = data[pos++] & 0xFF;
                out.append((char) (((b & 0x1F) << 6) | (b2 & 0x3F)));
            } else if ((b & 0xF0) == 0xE0) {
                if (pos + 1 >= data.length) {
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

    private static int u1(ByteBuffer buf) throws IOException {
        if (buf.remaining() < 1) {
            throw new IOException("truncated class file");
        }
        return buf.get() & 0xFF;
    }

    private static int u2(ByteBuffer buf) throws IOException {
        if (buf.remaining() < 2) {
            throw new IOException("truncated class file");
        }
        return buf.getShort() & 0xFFFF;
    }

    private static int u4(ByteBuffer buf) throws IOException {
        if (buf.remaining() < 4) {
            throw new IOException("truncated class file");
        }
        return buf.getInt();
    }

    private static void skip(ByteBuffer buf, int length) throws IOException {
        if (length < 0 || buf.remaining() < length) {
            throw new IOException("truncated class file");
        }
        buf.position(buf.position() + length);
    }
}