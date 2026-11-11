package tartine.sdk;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Stream;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Exact Android API-coverage metric: of every concrete method the public SDK
 * {@code android.jar} declares, how many have a real body in the compiled
 * {@code framework} classes rather than the auto-generated
 * {@code Tartine: not implemented} gap.
 *
 * <p>Unlike {@code scripts/coverage.py} (a source-only estimate of how much of
 * the <em>emitted skeleton</em> is logic vs. stubs), this measures the ported
 * subset against the whole public API surface. The two are different questions:
 * this number rises as families are ported, while the skeleton ratio can fall
 * when a new seed pulls in fresh stubs (AGENTS.md "Framework in Java").
 *
 * <p>The denominator is concrete methods only: {@code abstract} members,
 * compiler-generated {@code synthetic}/{@code bridge} members and the
 * {@code <clinit>} class initializer are excluded, so a method is never counted
 * against a body it cannot have. A method counts as implemented when the
 * compiled class declares the same name and descriptor and its body is not a
 * gap throw, so a guest helper that is not part of the SDK never enters the
 * numerator.
 *
 * <p>Dev-time only (the {@code fw-tools} module): it reads the local SDK jar
 * and the compiled framework, and is never on the runtime classpath.
 */
public final class ApiCoverage {
    /** Prefix of the gap marker {@link SkeletonGen} writes into stub bodies. */
    private static final String MARKER = "Tartine: not implemented:";
    /** Only the modeled Android surface counts, not the jar's java and javax. */
    private static final String ANDROID = "Landroid/";
    private static final int ACC_BRIDGE = 0x0040;
    private static final int ACC_ABSTRACT = 0x0400;
    private static final int ACC_SYNTHETIC = 0x1000;

    private ApiCoverage() {
    }

    /** Coverage counts for one {@code android.*} package. */
    public record PackageCoverage(String name, int eligible, int implemented, int stubs,
            int absent) {
    }

    /** Totals for one scan: the headline figure plus the context behind it. */
    public record ApiReport(int eligible, int implemented, int stubs, int absent,
            int presentEligible, int presentImplemented, int classesPresent, int classesTotal,
            int excludedAbstract, int excludedSynthetic) {
        /** Implemented share of every concrete android.* method, as a percentage. */
        public double percent() {
            return eligible == 0 ? 0.0 : implemented * 100.0 / eligible;
        }

        /** Implemented share of eligible methods in classes the framework compiles. */
        public double presentPercent() {
            return presentEligible == 0 ? 0.0 : presentImplemented * 100.0 / presentEligible;
        }
    }

    /** Scans every {@code android.*} class in {@code jar} against the compiled framework. */
    public static ApiReport scan(FwIndex jar, Path frameworkClasses) throws IOException {
        return toReport(analyze(jar, frameworkClasses));
    }

    /** Returns the per-package breakdown, sorted by package name. */
    public static List<PackageCoverage> packages(FwIndex jar, Path frameworkClasses)
            throws IOException {
        return rows(analyze(jar, frameworkClasses));
    }

    /** CLI: {@code ApiCoverage <frameworkClassesDir> [androidJarPath]}. */
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            throw new IllegalArgumentException(
                    "usage: ApiCoverage <frameworkClassesDir> [androidJarPath]");
        }
        Path frameworkClasses = Path.of(args[0]);
        if (!Files.isDirectory(frameworkClasses)) {
            throw new IllegalArgumentException(
                    "no compiled framework at " + frameworkClasses
                            + " (build it first, e.g. gradle :framework:classes)");
        }
        Path jarPath = args.length > 1 ? Path.of(args[1]) : Sdk.findJar(Sdk.TARGET_API);
        if (jarPath == null) {
            throw new IllegalArgumentException(
                    "no local android.jar (set ANDROID_HOME/ANDROID_SDK_ROOT or pass the path)");
        }
        FwIndex jar = FwIndexLoader.loadJar(jarPath);
        Analysis analysis = analyze(jar, frameworkClasses);
        print(jarPath, frameworkClasses, toReport(analysis), rows(analysis));
    }

    /** Mutable accumulator: totals plus per-package [eligible, impl, stub, absent]. */
    private static final class Analysis {
        final SortedMap<String, int[]> byPackage = new TreeMap<>();
        int eligible;
        int implemented;
        int stubs;
        int absent;
        int presentEligible;
        int presentImplemented;
        int classesPresent;
        int classesTotal;
        int excludedAbstract;
        int excludedSynthetic;
    }

    private static Analysis analyze(FwIndex jar, Path frameworkClasses) throws IOException {
        Map<String, Path> compiled = indexClasses(frameworkClasses);
        Map<String, Map<String, Boolean>> methodCache = new HashMap<>();
        Analysis analysis = new Analysis();
        for (FwIndex.FwClass cls : jar.classes()) {
            if (!cls.descriptor.startsWith(ANDROID)) {
                continue;
            }
            analysis.classesTotal++;
            Path classFile = compiled.get(cls.descriptor);
            boolean present = classFile != null;
            if (present) {
                analysis.classesPresent++;
            }
            Map<String, Boolean> methods = present
                    ? methodCache.computeIfAbsent(cls.descriptor, d -> readMethods(classFile))
                    : Map.of();
            int[] row = analysis.byPackage.computeIfAbsent(packageOf(cls.descriptor),
                    k -> new int[4]);
            for (FwIndex.FwMethod method : cls.methods) {
                if ((method.access & ACC_ABSTRACT) != 0) {
                    analysis.excludedAbstract++;
                    continue;
                }
                if ((method.access & (ACC_SYNTHETIC | ACC_BRIDGE)) != 0) {
                    analysis.excludedSynthetic++;
                    continue;
                }
                if (method.name.equals("<clinit>")) {
                    continue;
                }
                analysis.eligible++;
                row[0]++;
                if (present) {
                    analysis.presentEligible++;
                }
                Boolean isStub = methods.get(key(method));
                if (!present || isStub == null) {
                    analysis.absent++;
                    row[3]++;
                } else if (isStub) {
                    analysis.stubs++;
                    row[2]++;
                } else {
                    analysis.implemented++;
                    row[1]++;
                    if (present) {
                        analysis.presentImplemented++;
                    }
                }
            }
        }
        return analysis;
    }

    private static ApiReport toReport(Analysis analysis) {
        return new ApiReport(analysis.eligible, analysis.implemented, analysis.stubs,
                analysis.absent, analysis.presentEligible, analysis.presentImplemented,
                analysis.classesPresent, analysis.classesTotal, analysis.excludedAbstract,
                analysis.excludedSynthetic);
    }

    private static List<PackageCoverage> rows(Analysis analysis) {
        List<PackageCoverage> rows = new ArrayList<>();
        for (Map.Entry<String, int[]> entry : analysis.byPackage.entrySet()) {
            int[] counts = entry.getValue();
            rows.add(new PackageCoverage(entry.getKey(), counts[0], counts[1], counts[2],
                    counts[3]));
        }
        return rows;
    }

    /** Descriptor -> class file for every compiled framework class. */
    private static Map<String, Path> indexClasses(Path root) throws IOException {
        Map<String, Path> classes = new HashMap<>();
        try (Stream<Path> walk = Files.walk(root)) {
            for (Path file : (Iterable<Path>) walk
                    .filter(p -> p.toString().endsWith(".class"))::iterator) {
                String relative = root.relativize(file).toString().replace('\\', '/');
                String descriptor = "L"
                        + relative.substring(0, relative.length() - ".class".length()) + ";";
                classes.put(descriptor, file);
            }
        }
        return classes;
    }

    /**
     * Method keys ({@code name + descriptor}) declared by one compiled class,
     * mapped to whether the body throws the gap marker.
     */
    private static Map<String, Boolean> readMethods(Path classFile) {
        Map<String, Boolean> methods = new HashMap<>();
        try {
            ClassReader reader = new ClassReader(Files.readAllBytes(classFile));
            reader.accept(new ClassVisitor(Opcodes.ASM9) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String descriptor,
                        String signature, String[] exceptions) {
                    String methodKey = name + descriptor;
                    methods.put(methodKey, Boolean.FALSE);
                    return new MethodVisitor(Opcodes.ASM9) {
                        @Override
                        public void visitLdcInsn(Object value) {
                            if (value instanceof String text && text.startsWith(MARKER)) {
                                methods.put(methodKey, Boolean.TRUE);
                            }
                        }
                    };
                }
            }, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
        } catch (IOException e) {
            return Map.of();
        }
        return methods;
    }

    /** {@code Landroid/os/Environment;} -> {@code android/os}. */
    private static String packageOf(String descriptor) {
        int slash = descriptor.lastIndexOf('/');
        return slash < 0 ? "android" : descriptor.substring(1, slash);
    }

    /** Erased {@code name(params)ret} key shared by the jar index and classfiles. */
    private static String key(FwIndex.FwMethod method) {
        StringBuilder out = new StringBuilder(method.name).append('(');
        for (String param : method.params) {
            out.append(param);
        }
        return out.append(')').append(method.ret).toString();
    }

    private static void print(Path jarPath, Path frameworkClasses, ApiReport totals,
            List<PackageCoverage> rows) {
        System.out.println("Tartine API coverage: android.* public SDK surface");
        System.out.println("  android.jar:       " + jarPath);
        System.out.println("  framework classes: " + frameworkClasses);
        System.out.println("  denominator:       concrete android.* methods (abstract,");
        System.out.println("                     synthetic/bridge and <clinit> excluded)");
        System.out.println();
        System.out.printf(Locale.ROOT, "%-28s %9s %9s %9s %9s %9s%n",
                "Package", "Methods", "Impl", "Stub", "Absent", "Impl %");
        System.out.printf(Locale.ROOT, "%-28s %9s %9s %9s %9s %9s%n",
                "----------------------------", "--------", "------", "------", "------",
                "------");
        for (PackageCoverage row : rows) {
            System.out.printf(Locale.ROOT, "%-28s %9d %9d %9d %9d %8.1f%%%n",
                    row.name(), row.eligible(), row.implemented(), row.stubs(), row.absent(),
                    row.eligible() == 0 ? 0.0 : row.implemented() * 100.0 / row.eligible());
        }
        System.out.println();
        System.out.printf(Locale.ROOT, "%-28s %9d %9d %9d %9d %8.1f%%%n",
                "TOTAL", totals.eligible(), totals.implemented(), totals.stubs(), totals.absent(),
                totals.percent());
        System.out.println();
        System.out.printf(Locale.ROOT,
                "Implemented %d of %d concrete android.* methods (%.1f%%).%n",
                totals.implemented(), totals.eligible(), totals.percent());
        System.out.printf(Locale.ROOT,
                "In classes the framework compiles (%d of %d): %d of %d (%.1f%%).%n",
                totals.classesPresent(), totals.classesTotal(), totals.presentImplemented(),
                totals.presentEligible(), totals.presentPercent());
        System.out.printf(Locale.ROOT, "Excluded: %d abstract, %d synthetic/bridge.%n",
                totals.excludedAbstract(), totals.excludedSynthetic());
    }
}
