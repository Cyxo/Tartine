package tartine.sdk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Pins {@link ApiCoverage}'s classification: a concrete jar method counts as
 * implemented only when the compiled framework declares the same signature with
 * a non-gap body, and abstract/synthetic members never enter the denominator.
 */
class ApiCoverageTest {
    @TempDir
    private Path tempDir;

    @Test
    void classifiesImplementedStubAndAbsent() throws Exception {
        Path jarClasses = compileTo("jar", Map.of(
                "android.os.Thing",
                "package android.os;\n"
                + "public class Thing {\n"
                + "    public Thing() {}\n"
                + "    public int live() { return 1; }\n"
                + "    public int gap() { return 2; }\n"
                + "}\n",
                "android.os.Gone",
                "package android.os;\n"
                + "public class Gone {\n"
                + "    public void gone() {}\n"
                + "}\n"));
        Path frameworkClasses = compileTo("fw", Map.of(
                "android.os.Thing",
                "package android.os;\n"
                + "public class Thing {\n"
                + "    public Thing() {}\n"
                + "    public int live() { return 42; }\n"
                + "    public int gap() {\n"
                + "        throw new UnsupportedOperationException(\n"
                + "                \"Tartine: not implemented: Landroid/os/Thing;->gap()I\");\n"
                + "    }\n"
                + "}\n"));

        FwIndex jar = FwIndexLoader.loadJar(jarOf(jarClasses));
        ApiCoverage.ApiReport report = ApiCoverage.scan(jar, frameworkClasses);

        assertEquals(5, report.eligible());
        assertEquals(2, report.implemented());
        assertEquals(1, report.stubs());
        assertEquals(2, report.absent());
        assertEquals(3, report.presentEligible());
        assertEquals(2, report.presentImplemented());
        assertEquals(1, report.classesPresent());
        assertEquals(2, report.classesTotal());

        List<ApiCoverage.PackageCoverage> rows = ApiCoverage.packages(jar, frameworkClasses);
        assertEquals(1, rows.size());
        assertEquals("android/os", rows.get(0).name());
        assertEquals(5, rows.get(0).eligible());
        assertEquals(2, rows.get(0).implemented());
    }

    @Test
    void excludesAbstractMembers() throws Exception {
        Path jarClasses = compileTo("abstract", Map.of(
                "android.os.Iface",
                "package android.os;\n"
                + "public interface Iface {\n"
                + "    void run();\n"
                + "}\n",
                "android.os.Base",
                "package android.os;\n"
                + "public abstract class Base {\n"
                + "    public abstract void must();\n"
                + "    public void done() {}\n"
                + "}\n"));
        Path frameworkClasses = compileTo("abstract-fw", Map.of());

        FwIndex jar = FwIndexLoader.loadJar(jarOf(jarClasses));
        ApiCoverage.ApiReport report = ApiCoverage.scan(jar, frameworkClasses);

        // Iface.run and Base.must are abstract: never counted. What remains is
        // Base.<init>() and Base.done(), both absent from the empty framework.
        assertEquals(2, report.excludedAbstract());
        assertEquals(2, report.eligible());
        assertEquals(0, report.implemented());
        assertEquals(2, report.absent());
        assertEquals(0, report.classesPresent());
        assertEquals(2, report.classesTotal());
    }

    /** Compiles one source per map entry into its own class-output directory. */
    private Path compileTo(String name, Map<String, String> sources) throws Exception {
        Path classes = tempDir.resolve(name + "-classes");
        Files.createDirectories(classes);
        List<String> args = new ArrayList<>();
        args.add("-d");
        args.add(classes.toString());
        // The framework is built at the repo's Java 21 floor; ASM reads that
        // version, so compile the fixtures the same way instead of at the
        // running JDK's (newer) class-file version.
        args.add("--release");
        args.add("21");
        Path sourceRoot = tempDir.resolve(name + "-src");
        for (Map.Entry<String, String> source : sources.entrySet()) {
            Path file = sourceRoot.resolve(source.getKey().replace('.', '/') + ".java");
            Path parent = file.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.writeString(file, source.getValue());
            args.add(file.toString());
        }
        if (!sources.isEmpty()) {
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            assertNotNull(compiler, "tests require a JDK");
            assertEquals(0, compiler.run(null, null, null, args.toArray(new String[0])));
        }
        return classes;
    }

    /** Packs a class-output directory into a jar the way an SDK jar is laid out. */
    private Path jarOf(Path classesDir) throws Exception {
        Path jar = tempDir.resolve(classesDir.getFileName() + ".jar");
        try (ZipOutputStream zip = new ZipOutputStream(Files.newOutputStream(jar));
                Stream<Path> walk = Files.walk(classesDir)) {
            for (Path file : (Iterable<Path>) walk
                    .filter(p -> p.toString().endsWith(".class"))::iterator) {
                zip.putNextEntry(new ZipEntry(classesDir.relativize(file)
                        .toString().replace('\\', '/')));
                zip.write(Files.readAllBytes(file));
                zip.closeEntry();
            }
        }
        return jar;
    }
}
