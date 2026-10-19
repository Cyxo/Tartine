package tartine.apk;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import tartine.graphics.WindowIcon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the icon pipeline end to end against a synthetic APK: manifest
 * reference → arsc → drawable conversion (adaptive icon, vectors with a
 * group transform, gradients) or Skija raster decode. The binary fixture
 * writers are independent of the readers (see {@link Fixtures}); real-APK
 * rendering gets eyeballed through {@code mise run gui}.
 */
class AppIconTest {

    /** Collects the resolution report lines. */
    private static final class Collector implements AppIcon.Reporter {
        final List<String> lines = new ArrayList<>();

        @Override
        public void say(String line) {
            lines.add(line);
        }
    }

    @TempDir
    private Path tempDir;

    private Path apk;

    private static final int ICON_REF = 0x7F010000; // drawable/adaptive

    @BeforeEach
    void buildFixture() {
        apk = tempDir.resolve("icon.apk");
    }

    private static Fixtures.AxNode manifest(int iconRef) {
        return Fixtures.AxNode.node("manifest", List.of(
                Fixtures.AxNode.leaf("application",
                        Fixtures.AxAttr.android("icon",
                                BinaryXml.TYPE_REFERENCE, iconRef))),
                Fixtures.AxAttr.plain("package", "com.example.test"));
    }

    private static Fixtures.AxNode adaptiveIcon(int backgroundRef,
            int foregroundRef) {
        return Fixtures.AxNode.node("adaptive-icon", List.of(
                Fixtures.AxNode.leaf("background",
                        Fixtures.AxAttr.android("drawable",
                                BinaryXml.TYPE_REFERENCE, backgroundRef)),
                Fixtures.AxNode.leaf("foreground",
                        Fixtures.AxAttr.android("drawable",
                                BinaryXml.TYPE_REFERENCE, foregroundRef))));
    }

    private static Fixtures.AxNode vector(Fixtures.AxNode... children) {
        return Fixtures.AxNode.node("vector", List.of(children),
                Fixtures.AxAttr.android("width", BinaryXml.TYPE_DIMENSION, 0x6C01),
                Fixtures.AxAttr.android("height", BinaryXml.TYPE_DIMENSION, 0x6C01),
                Fixtures.AxAttr.android("viewportWidth",
                        BinaryXml.TYPE_FLOAT, Float.floatToIntBits(108f)),
                Fixtures.AxAttr.android("viewportHeight",
                        BinaryXml.TYPE_FLOAT, Float.floatToIntBits(108f)));
    }

    private static Fixtures.AxNode group(float scaleX, Fixtures.AxNode child) {
        return Fixtures.AxNode.node("group", List.of(child),
                Fixtures.AxAttr.android("scaleX",
                        BinaryXml.TYPE_FLOAT, Float.floatToIntBits(scaleX)));
    }

    private static Fixtures.AxNode payload(String pathData, int argb) {
        return Fixtures.AxNode.leaf("path",
                Fixtures.AxAttr.androidRaw("fillColor", aapt(argb),
                        BinaryXml.TYPE_STRING, -1),
                Fixtures.AxAttr.androidRaw("pathData", pathData,
                        BinaryXml.TYPE_STRING, -1));
    }

    private static String aapt(int argb) {
        return String.format("#%08x", argb).toUpperCase();
    }

    private List<WindowIcon> load(Collector reporter) throws IOException {
        try (ApkReader reader = ApkReader.open(apk)) {
            return AppIcon.load(reader, reporter);
        }
    }

    @Test
    void composesAdaptiveVectorLayersWithGroupTransform() throws Exception {
        // background: full-frame green; foreground: white rect on the left
        // half inside a group scaled 2x on X — the group transform must
        // widen it over the whole frame.
        byte[] arsc = Fixtures.arsc("com.example.test", List.of(
                new Fixtures.ArscType("drawable", List.of(
                        new Fixtures.ArscEntry("adaptive", List.of(
                                Fixtures.Row.file(0, "res/drawable/adaptive.xml"))),
                        new Fixtures.ArscEntry("background", List.of(
                                Fixtures.Row.file(0, "res/drawable/bg.xml"))),
                        new Fixtures.ArscEntry("foreground", List.of(
                                Fixtures.Row.file(0, "res/drawable/fg.xml")))))));
        byte[] manifest = Fixtures.axml(manifest(ICON_REF));
        byte[] adaptive = Fixtures.axml(adaptiveIcon(
                0x7F010001, 0x7F010002));
        byte[] bg = Fixtures.axml(vector(
                payload("M0,0h108v108h-108z", 0xFF22AA55)));
        byte[] fg = Fixtures.axml(vector(
                group(2f, payload("M0,0h54v108h-54z", 0xFFFFFFFF))));
        Fixtures.zip(apk, Map.of(
                "AndroidManifest.xml", manifest,
                "resources.arsc", arsc,
                "res/drawable/adaptive.xml", adaptive,
                "res/drawable/bg.xml", bg,
                "res/drawable/fg.xml", fg));

        Collector reporter = new Collector();
        List<WindowIcon> icons = load(reporter);

        assertTrue(icons.size() == 3, reporter.lines.toString());
        assertEquals(List.of(16, 32, 48),
                icons.stream().map(WindowIcon::width).toList());
        WindowIcon icon48 = icons.get(2);
        assertEquals(48, icon48.width());
        assertEquals(48 * 48 * 4, icon48.rgba().length);
        // Every sampled pixel (corners + centre) stays opaque foreground
        // white — the scaled group covered the frame despite its 108-wide
        // path being drawn over a 54-unit half.
        int[][] probes = {{2, 2}, {24, 2}, {45, 45}, {2, 45}, {46, 46}};
        for (int[] xy : probes) {
            int base = (xy[1] * 48 + xy[0]) * 4;
            assertEquals((byte) 0xFF, icon48.rgba()[base]);
            assertEquals((byte) 0xFF, icon48.rgba()[base + 1]);
            assertEquals((byte) 0xFF, icon48.rgba()[base + 2]);
            assertEquals((byte) 0xFF, icon48.rgba()[base + 3]);
        }
    }

    @Test
    void decodesHighestDensityRaster() throws Exception {
        // One mipmap entry, two densities: mdpi red, xxxhdpi blue.
        byte[] arsc = Fixtures.arsc("com.example.test", List.of(
                new Fixtures.ArscType("mipmap", List.of(
                        new Fixtures.ArscEntry("launcher", List.of(
                                Fixtures.Row.file(160, "res/mipmap/red.png"),
                                Fixtures.Row.file(640, "res/mipmap/blue.png")))))));
        byte[] manifest = Fixtures.axml(manifest(0x7F010000)); // mipmap type 1
        Fixtures.zip(apk, Map.of(
                "AndroidManifest.xml", manifest,
                "resources.arsc", arsc,
                "res/mipmap/red.png", png(4, 0xFFCC3322),
                "res/mipmap/blue.png", png(6, 0xFF2233CC)));

        Collector reporter = new Collector();
        List<WindowIcon> icons = load(reporter);

        assertEquals(List.of(16, 32, 48),
                icons.stream().map(WindowIcon::width).toList());
        WindowIcon icon32 = icons.get(1);
        int base = (16 * 32 + 16) * 4; // centre pixel
        assertEquals((byte) 0x22, icon32.rgba()[base]);
        assertEquals((byte) 0x33, icon32.rgba()[base + 1]);
        assertEquals((byte) 0xCC, icon32.rgba()[base + 2]);
        assertEquals((byte) 0xFF, icon32.rgba()[base + 3]);
    }

    @Test
    void colorResourcesServeAsIcons() throws Exception {
        byte[] arsc = Fixtures.arsc("com.example.test", List.of(
                new Fixtures.ArscType("color", List.of(
                        new Fixtures.ArscEntry("accent", List.of(
                                Fixtures.Row.typed(0,
                                        BinaryXml.TYPE_COLOR_ARGB8,
                                        0xFF3366AA)))))));
        byte[] manifest = Fixtures.axml(manifest(0x7F010000)); // color type 1
        Fixtures.zip(apk, Map.of(
                "AndroidManifest.xml", manifest,
                "resources.arsc", arsc));

        List<WindowIcon> icons = load(new Collector());

        WindowIcon icon16 = icons.get(0);
        for (int pixel = 0; pixel < 16 * 16; pixel++) {
            int base = pixel * 4;
            assertEquals((byte) 0x33, icon16.rgba()[base]);
            assertEquals((byte) 0x66, icon16.rgba()[base + 1]);
            assertEquals((byte) 0xAA, icon16.rgba()[base + 2]);
            assertEquals((byte) 0xFF, icon16.rgba()[base + 3]);
        }
    }

    @Test
    void unsupportedDrawablesReportGapsInsteadOfIcons() throws Exception {
        byte[] arsc = Fixtures.arsc("com.example.test", List.of(
                new Fixtures.ArscType("drawable", List.of(
                        new Fixtures.ArscEntry("icon", List.of(
                                Fixtures.Row.file(0, "res/drawable/icon.xml")))))));
        byte[] manifest = Fixtures.axml(manifest(ICON_REF));
        byte[] inset = Fixtures.axml(Fixtures.AxNode.leaf("inset",
                Fixtures.AxAttr.android("drawable",
                        BinaryXml.TYPE_REFERENCE, 0x7F010001)));
        Fixtures.zip(apk, Map.of(
                "AndroidManifest.xml", manifest,
                "resources.arsc", arsc,
                "res/drawable/icon.xml", inset));

        Collector reporter = new Collector();
        List<WindowIcon> icons = load(reporter);

        assertTrue(icons.isEmpty());
        assertTrue(reporter.lines.stream().anyMatch(
                line -> line.startsWith("icon:") && line.contains("not converted")),
                reporter.lines.toString());
    }

    @Test
    void missingResourcesReportFailure() throws Exception {
        byte[] manifest = Fixtures.axml(manifest(ICON_REF));
        Fixtures.zip(apk, Map.of("AndroidManifest.xml", manifest)); // no arsc

        Collector reporter = new Collector();
        List<WindowIcon> icons = load(reporter);

        assertTrue(icons.isEmpty());
        assertTrue(reporter.lines.stream().anyMatch(
                line -> line.startsWith("icon:unavailable")
                        || line.startsWith("icon: unavailable")),
                reporter.lines.toString());
    }

    private static byte[] png(int size, int rgb) throws IOException {
        BufferedImage image = new BufferedImage(size, size,
                BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                image.setRGB(x, y, rgb);
            }
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        return out.toByteArray();
    }
}

