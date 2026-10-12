package aine.apk;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import aine.dex.DexMagic;

/**
 * Hand-rolled test for {@link ApkReader} and {@link BinaryXml}.
 *
 * <p>Builds a synthetic APK (including a hand-encoded binary manifest, so
 * this test covers our AXML <em>reader</em> against an independent
 * <em>writer</em>) and asserts entry listing, DEX ordering, ABI routing,
 * and manifest decoding. Real-APK cross-checks against apktool live outside
 * this test; see THIRD-PARTY.md.
 */
public final class ApkReaderTest {
    private static final String ANDROID_NS = "http://schemas.android.com/apk/res/android";

    private ApkReaderTest() {
    }

    public static void main(String[] args) throws Exception {
        Path apk = Files.createTempFile("aine-step1-", ".apk");
        try {
            buildTestApk(apk);
            try (ApkReader reader = ApkReader.open(apk)) {
                check(reader.entries().equals(Arrays.asList(
                        "AndroidManifest.xml",
                        "META-INF/CERT.SF",
                        "assets/data.bin",
                        "classes.dex",
                        "classes2.dex",
                        "lib/armeabi-v7a/libfoo.so",
                        "lib/x86_64/libfoo.so",
                        "res/layout/main.xml",
                        "resources.arsc")), "entry listing");
                check(reader.hasManifest(), "has manifest");
                check(reader.hasResources(), "has resources");
                check(reader.dexEntries().equals(Arrays.asList("classes.dex", "classes2.dex")),
                        "dex order");
                check(DexMagic.hasDexMagic(reader.readEntry("classes.dex")), "dex magic");
                check(reader.nativeAbis().equals(
                        new TreeSet<>(Arrays.asList("armeabi-v7a", "x86_64"))), "abi set");
                String host = Abi.hostAbi();
                String expectedPick = reader.nativeAbis().contains(host) ? host : "armeabi-v7a";
                check(expectedPick.equals(Abi.pickBest(reader.nativeAbis())), "abi pick");

                BinaryXml.Element root = reader.manifest();
                check(root.name.equals("manifest") && root.namespaceUri.isEmpty(), "root");
                check("com.example.test".equals(root.attr("package")), "package");
                check("3".equals(root.attr("versionCode")), "versionCode");
                check(root.children.size() == 2, "child count");
                BinaryXml.Element app = root.child("application");
                check(app != null, "application child");
                String label = null;
                String labelNs = null;
                for (BinaryXml.Attribute attr : app.attributes) {
                    if (attr.name.equals("label")) {
                        label = attr.value;
                        labelNs = attr.namespaceUri;
                    }
                }
                check("TestApp".equals(label), "label value");
                check(ANDROID_NS.equals(labelNs), "label namespace");
                check("true".equals(app.attr("debuggable")), "debuggable");
                BinaryXml.Element sdk = root.child("uses-sdk");
                check(sdk != null && "21".equals(sdk.attr("minSdkVersion")), "minSdk");
            }
            System.out.println("ApkReaderTest: PASS");
        } finally {
            Files.deleteIfExists(apk);
        }
    }

    private static void check(boolean condition, String name) {
        if (!condition) {
            throw new AssertionError("FAILED: " + name);
        }
    }

    private static void buildTestApk(Path apk) throws Exception {
        try (ZipOutputStream zip = new ZipOutputStream(Files.newOutputStream(apk))) {
            writeEntry(zip, "AndroidManifest.xml", buildManifest());
            writeEntry(zip, "classes.dex",
                    new byte[]{0x64, 0x65, 0x78, 0x0A, '0', '3', '9', 0});
            writeEntry(zip, "classes2.dex",
                    new byte[]{0x64, 0x65, 0x78, 0x0A, '0', '3', '9', 0});
            writeEntry(zip, "resources.arsc", new byte[0]);
            writeEntry(zip, "res/layout/main.xml", "<view/>".getBytes(StandardCharsets.UTF_8));
            writeEntry(zip, "assets/data.bin", new byte[]{1, 2, 3});
            writeEntry(zip, "lib/x86_64/libfoo.so", new byte[]{0x7F, 'E', 'L', 'F'});
            writeEntry(zip, "lib/armeabi-v7a/libfoo.so", new byte[]{0x7F, 'E', 'L', 'F'});
            writeEntry(zip, "META-INF/CERT.SF", "signature".getBytes(StandardCharsets.UTF_8));
        }
    }

    private static void writeEntry(ZipOutputStream zip, String name, byte[] data) throws Exception {
        zip.putNextEntry(new ZipEntry(name));
        zip.write(data);
        zip.closeEntry();
    }

    // --- Minimal binary-XML writer (test fixture builder, UTF-16 pool) ---

    // Attribute row: {nsIdx, nameIdx, rawIdx, dataType, data}.
    private static byte[] buildManifest() {
        List<String> strings = Arrays.asList(
                ANDROID_NS, // 0
                "application", // 1
                "debuggable", // 2
                "label", // 3
                "manifest", // 4
                "package", // 5
                "versionCode", // 6
                "TestApp", // 7
                "com.example.test", // 8
                "minSdkVersion", // 9
                "uses-sdk"); // 10
        ByteBuffer out = ByteBuffer.allocate(2048).order(ByteOrder.LITTLE_ENDIAN);
        putU16(out, 0x0003);
        putU16(out, 8);
        int fileSizePos = out.position();
        putU32(out, 0);

        int poolStart = out.position();
        putU16(out, 0x0001);
        putU16(out, 28);
        int poolSizePos = out.position();
        putU32(out, 0);
        putU32(out, strings.size());
        putU32(out, 0); // styleCount
        putU32(out, 0); // flags: UTF-16, unsorted on purpose
        int stringsStartPos = out.position();
        putU32(out, 0);
        putU32(out, 0); // stylesStart
        int offsetsPos = out.position();
        for (int i = 0; i < strings.size(); i++) {
            putU32(out, 0);
        }
        List<byte[]> encoded = new ArrayList<>();
        for (String s : strings) {
            encoded.add(encodeUtf16(s));
        }
        out.putInt(stringsStartPos, out.position() - poolStart);
        for (int i = 0; i < encoded.size(); i++) {
            out.putInt(offsetsPos + i * 4, out.position() - (poolStart + out.getInt(stringsStartPos)));
            out.put(encoded.get(i));
        }
        while (out.position() % 4 != 0) {
            out.put((byte) 0);
        }
        out.putInt(poolSizePos, out.position() - poolStart);

        startElement(out, 4, new int[][]{
                {-1, 5, 8, 0x03, 8}, // package="com.example.test"
                {-1, 6, -1, 0x10, 3}, // versionCode=3
        });
        startElement(out, 1, new int[][]{
                {0, 3, 7, 0x03, 7}, // android:label="TestApp"
                {0, 2, -1, 0x12, 1}, // android:debuggable=true
        });
        endElement(out, 1);
        startElement(out, 10, new int[][]{
                {0, 9, -1, 0x10, 21}, // android:minSdkVersion=21
        });
        endElement(out, 10);
        endElement(out, 4);

        out.putInt(fileSizePos, out.position());
        byte[] result = new byte[out.position()];
        out.flip();
        out.get(result);
        return result;
    }

    private static void startElement(ByteBuffer out, int nameIdx, int[][] attrs) {
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
        putU16(out, attrs.length);
        putU16(out, 0xFFFF); // idIndex
        putU16(out, 0xFFFF); // classIndex
        putU16(out, 0xFFFF); // styleIndex
        for (int[] attr : attrs) {
            putU32(out, attr[0]); // ns
            putU32(out, attr[1]); // name
            putU32(out, attr[2]); // rawValue
            putU16(out, 8); // value size
            out.put((byte) 0); // res0
            out.put((byte) attr[3]); // dataType
            putU32(out, attr[4]); // data
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

    private static byte[] encodeUtf16(String s) {
        ByteBuffer buf = ByteBuffer.allocate(2 + s.length() * 2 + 2).order(ByteOrder.LITTLE_ENDIAN);
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
}
