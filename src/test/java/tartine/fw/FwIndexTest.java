package tartine.fw;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import tartine.fw.FwIndex.FwClass;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the classfile parser against javac output (including lambdas and
 * string concatenation, which exercise invokedynamic skipping) and SDK
 * resolution against a fake SDK tree.
 */
class FwIndexTest {
    @TempDir
    private Path tempDir;

    @Test
    void parsesHierarchy() throws Exception {
        Map<String, byte[]> classes = compile(fixtureSources());
        FwClass child = FwIndex.parse(classes.get("fwtest.Child"));
        assertEquals("Lfwtest/Child;", child.descriptor);
        assertEquals("Lfwtest/Base;", child.superclass);
        assertTrue(child.interfaces.isEmpty());
        assertTrue(child.methods.stream().anyMatch(m -> m.name.equals("extra")));

        FwClass base = FwIndex.parse(classes.get("fwtest.Base"));
        assertEquals("Ljava/lang/Object;", base.superclass);
        assertEquals(Integer.valueOf(7), base.constants.get("K"));
        assertEquals("base", base.constants.get("NAME"));
        assertEquals(Long.valueOf(1234567890123L), base.constants.get("BIG"));
        assertTrue(base.fields.stream()
                .anyMatch(f -> f.name.equals("instanceField") && f.type.equals("I")));
        assertTrue(base.hasMethod("greet",
                Collections.singletonList("Ljava/lang/String;"), "Ljava/lang/String;"));
        assertTrue(base.hasMethod("add", Arrays.asList("I", "I"), "I"));
    }

    @Test
    void resolvesSdkJar() throws Exception {
        Map<String, byte[]> classes = compile(fixtureSources());
        Path root = tempDir.resolve("sdk");
        writeJar(root.resolve("platforms/android-36/android.jar"), classes);
        writeJar(root.resolve("platforms/android-34/android.jar"), classes);
        assertEquals(root.resolve("platforms/android-36/android.jar"),
                Sdk.findJar(36, Collections.singletonList(root)));
        assertEquals(root.resolve("platforms/android-36/android.jar"),
                Sdk.findJar(99, Collections.singletonList(root)));
        assertNull(Sdk.findJar(36, Collections.singletonList(tempDir.resolve("empty"))));
        assertEquals(33, Sdk.apiLevel("android-33-ext4"));
        assertEquals(-1, Sdk.apiLevel("build-tools"));
    }

    @Test
    void loadsJarIndex() throws Exception {
        Map<String, byte[]> classes = compile(fixtureSources());
        Path jar = tempDir.resolve("android.jar");
        writeJar(jar, classes);
        FwIndex index = FwIndex.loadJar(jar);
        assertNotNull(index.find("Lfwtest/Base;"));
        assertNotNull(index.find("Lfwtest/Child;"));
        assertNull(index.find("Lfwtest/Missing;"));
        assertTrue(index.classCount() >= 2);
    }

    @Test
    void syntheticModelQueries() {
        FwIndex index = new FwIndex();
        Map<String, Object> constants = new HashMap<>();
        constants.put("VISIBLE", 0);
        index.put(new FwClass("Landroid/view/View;", "Ljava/lang/Object;",
                Collections.<String>emptyList(), Collections.<FwIndex.FwMethod>emptyList(),
                Collections.singletonList(new FwIndex.FwField("VISIBLE", "I")), constants));
        FwClass view = index.find("Landroid/view/View;");
        assertNotNull(view);
        assertEquals(Integer.valueOf(0), view.constants.get("VISIBLE"));
        assertTrue(view.hasField("VISIBLE"));
    }

    private static Map<String, String> fixtureSources() {
        Map<String, String> sources = new HashMap<>();
        sources.put("fwtest.Base",
                "package fwtest;\n"
                + "public class Base {\n"
                + "    public static final int K = 7;\n"
                + "    public static final String NAME = \"base\";\n"
                + "    public static final long BIG = 1234567890123L;\n"
                + "    public int instanceField;\n"
                + "    public static String mutable;\n"
                + "    public String greet(String name) { return \"hi \" + name; }\n"
                + "    public static int add(int a, int b) { return a + b; }\n"
                + "    public Runnable r = () -> System.out.println(\"x\");\n"
                + "}\n");
        sources.put("fwtest.Child",
                "package fwtest;\n"
                + "public class Child extends Base {\n"
                + "    public void extra() {}\n"
                + "}\n");
        return sources;
    }

    private static void writeJar(Path jar, Map<String, byte[]> classes) throws Exception {
        Files.createDirectories(jar.getParent());
        try (ZipOutputStream zip = new ZipOutputStream(Files.newOutputStream(jar))) {
            for (Map.Entry<String, byte[]> entry : classes.entrySet()) {
                zip.putNextEntry(new ZipEntry(entry.getKey().replace('.', '/') + ".class"));
                zip.write(entry.getValue());
                zip.closeEntry();
            }
        }
    }

    private static Map<String, byte[]> compile(Map<String, String> sources) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        assertNotNull(compiler, "tests require a JDK");
        StandardJavaFileManager standard =
                compiler.getStandardFileManager(null, null, null);
        Map<String, ByteArrayOutputStream> output = new HashMap<>();
        JavaFileManager files = new ForwardingJavaFileManager<StandardJavaFileManager>(standard) {
            @Override
            public JavaFileObject getJavaFileForOutput(Location location, String className,
                    JavaFileObject.Kind kind, FileObject sibling) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                output.put(className, bytes);
                return new SimpleJavaFileObject(
                        URI.create("mem:///" + className + ".class"), kind) {
                    @Override
                    public OutputStream openOutputStream() {
                        return bytes;
                    }
                };
            }
        };
        java.util.List<JavaFileObject> units = new java.util.ArrayList<>();
        for (Map.Entry<String, String> source : sources.entrySet()) {
            String simple = source.getKey().substring(source.getKey().lastIndexOf('.') + 1);
            units.add(new SimpleJavaFileObject(
                    URI.create("mem:///" + simple + ".java"),
                    JavaFileObject.Kind.SOURCE) {
                @Override
                public CharSequence getCharContent(boolean ignoreErrors) {
                    return source.getValue();
                }
            });
        }
        assertTrue(compiler.getTask(null, files, null, null, null, units).call());
        Map<String, byte[]> result = new HashMap<>();
        for (Map.Entry<String, ByteArrayOutputStream> entry : output.entrySet()) {
            result.put(entry.getKey(), entry.getValue().toByteArray());
        }
        return result;
    }
}
