package tartine.tx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import tartine.dex.Dex;
import tartine.dex.DexSet;
import tartine.dex.FixtureDex;

/**
 * Behavioral differential against dex2jar, the independent DEX-to-JVM
 * translator recorded as a dev-time oracle.
 *
 * <p>The synthetic fixture is translated twice — by {@link TxTranslator} and
 * by dex2jar — and the pure methods are executed on both classfiles. Results
 * must agree; an exception on one side must be the same exception class on
 * the other. dex2jar is a mature reference implementation with its own gaps,
 * so a reference-side {@link LinkageError} (its whole class can fail
 * verification over an unrelated method) or a conversion stub it baked into
 * the body (its {@code d2j} message marker) is skipped, never treated as a
 * Tartine failure. A minimum comparison count keeps a fully-skipped run from
 * passing silently.
 */
final class OracleDifferentialTest {
    /** dex2jar 2.4 distribution; fetched by {@code mise run fetch-tools}. */
    private static final Path DIST = Path.of("tools", "dex-tools-v2.4");
    private static final Path TOOL_JAR = DIST.resolve("lib")
            .resolve("dex-tools-v2.4.jar");
    /** The fixture's demo class both translators define. */
    private static final String CLASS_NAME = "test.Hello";
    private static final String CLASS_ENTRY = "test/Hello.class";
    /** Methods that must actually compare; reference bugs can skip some. */
    private static final int MIN_COMPARED = 20;

    @TempDir
    Path temp;

    @Test
    void pureFixtureMethodsMatchDex2jar() throws Exception {
        assumeTrue(Files.isRegularFile(TOOL_JAR),
                "dex2jar missing: run mise run fetch-tools");
        Path dexFile = temp.resolve("fixture.dex");
        FixtureDex.writeHello(dexFile);
        Path referenceJar = runDex2jar(dexFile);

        DexSet dex = DexSet.of(Dex.load(dexFile.toFile()));
        TranslatedLoader loader = new TranslatedLoader(
                OracleDifferentialTest.class.getClassLoader(), dex);
        Class<?> tartine = loader.loadClass(CLASS_NAME);
        byte[] referenceBytes = readEntry(referenceJar, CLASS_ENTRY);

        int compared = 0;
        int skipped = 0;
        List<String> mismatches = new ArrayList<>();
        for (Case testCase : cases()) {
            Outcome ours = invoke(tartine, testCase);
            Outcome reference = invokeReference(referenceBytes, testCase);
            if (reference.linkage()) {
                skipped++;
                continue;
            }
            compared++;
            if (!ours.sameAs(reference)) {
                mismatches.add(testCase.method + ": tartine=" + ours
                        + " dex2jar=" + reference);
            }
        }
        final int comparedCount = compared;
        final int skippedCount = skipped;
        assertTrue(comparedCount >= MIN_COMPARED,
                () -> "only " + comparedCount + " method(s) compared (skipped "
                        + skippedCount + "); dex2jar may be broken or missing");
        assertTrue(mismatches.isEmpty(),
                () -> "translation differs from dex2jar:\n  "
                        + String.join("\n  ", mismatches));
    }

    /**
     * Pure fixture demos: arithmetic (including wide int/long/float/double),
     * strings, arrays, casts, switches, collections, and the throwing paths.
     * Framework-dependent demos are deliberately absent — they need the guest
     * runtime, not just a classfile translator.
     */
    private static List<Case> cases() {
        return List.of(
                noArgs("make"), noArgs("sum"), noArgs("len"),
                noArgs("nops"), noArgs("longArith"), noArgs("floatArith"),
                noArgs("doubleArith"), noArgs("greet"), noArgs("sb"),
                noArgs("throwCatch"), noArgs("safely"),
                noArgs("nullCast"), noArgs("castOk"), noArgs("covarPrim"),
                noArgs("switchDemo"), noArgs("arrayCopy"),
                noArgs("fillDemo"), noArgs("fillFloatDemo"),
                noArgs("copyDemo"), noArgs("repeatDemo"),
                noArgs("strictSqrt"), noArgs("mathMax"),
                noArgs("intBox"), noArgs("stringOps"),
                noArgs("boolStr"), noArgs("charStr"), noArgs("emptyDemo"),
                noArgs("cmpTiny"), noArgs("mapDemo"),
                noArgs("linkedMapDemo"), noArgs("listDemo"),
                noArgs("setDemo"), noArgs("iterDemo"),
                noArgs("weakGet"), noArgs("weakClear"),
                noArgs("intUpdaterDemo"), noArgs("ordinalDemo"),
                noArgs("casDemo"), noArgs("nullRecv"),
                intArg("id", 7), intArg("twice", 21));
    }

    private static Case noArgs(String method) {
        return new Case(method, new Class<?>[0], new Object[0]);
    }

    private static Case intArg(String method, int value) {
        return new Case(method, new Class<?>[] {int.class},
                new Object[] {value});
    }

    /** Invokes the method on a class the caller already loaded. */
    private static Outcome invoke(Class<?> owner, Case testCase)
            throws Exception {
        Method method = owner.getDeclaredMethod(
                testCase.method, testCase.params);
        return invoke(method, testCase.args);
    }

    private static Outcome invoke(Method method, Object[] args) {
        try {
            return Outcome.value(method.invoke(null, args));
        } catch (InvocationTargetException x) {
            Throwable cause = x.getCause();
            return Outcome.thrown(cause, cause instanceof LinkageError);
        } catch (ReflectiveOperationException x) {
            return Outcome.thrown(x, true);
        }
    }

    /**
     * Loads a copy of the reference class carrying only {@code method}.
     * dex2jar may emit an invalid body for another method, and the JVM
     * verifies the whole class when reflection enumerates it; isolating the
     * body keeps a reference bug from hiding the method under test.
     */
    private static Outcome invokeReference(byte[] classBytes, Case testCase) {
        try {
            byte[] single = keepOnly(classBytes, testCase.method);
            Class<?> owner = new SingleClassLoader(
                    OracleDifferentialTest.class.getClassLoader())
                    .define(CLASS_NAME, single);
            return invoke(owner, testCase).asReferenceGap();
        } catch (LinkageError x) {
            return Outcome.thrown(x, true);
        } catch (Exception x) {
            return Outcome.thrown(x, true);
        }
    }

    /** Keeps exactly one method (and no {@code <clinit>}). */
    private static byte[] keepOnly(byte[] classBytes, String method) {
        ClassReader reader = new ClassReader(classBytes);
        ClassWriter writer = new ClassWriter(0);
        reader.accept(new ClassVisitor(Opcodes.ASM9, writer) {
            @Override
            public MethodVisitor visitMethod(int access, String name,
                    String descriptor, String signature,
                    String[] exceptions) {
                if (!method.equals(name)) {
                    return null;
                }
                return super.visitMethod(access, name, descriptor, signature,
                        exceptions);
            }
        }, 0);
        return writer.toByteArray();
    }

    private static byte[] readEntry(Path jar, String entry) throws Exception {
        try (ZipFile zip = new ZipFile(jar.toFile())) {
            ZipEntry found = zip.getEntry(entry);
            if (found == null) {
                throw new IllegalStateException(entry + " missing from " + jar);
            }
            try (InputStream in = zip.getInputStream(found)) {
                return in.readAllBytes();
            }
        }
    }

    /** Runs the dex2jar CLI; its stderr warnings are not fatal. */
    private Path runDex2jar(Path dexFile) throws Exception {
        Path jar = temp.resolve("fixture-dex2jar.jar");
        Path log = temp.resolve("dex2jar.log");
        String java = Path.of(System.getProperty("java.home"), "bin", "java")
                .toString();
        String classpath = DIST.toAbsolutePath().resolve("lib")
                + File.separator + "*";
        ProcessBuilder builder = new ProcessBuilder(java,
                "-cp", classpath,
                "com.googlecode.dex2jar.tools.Dex2jarCmd",
                "--force", "-o", jar.toString(), dexFile.toString());
        builder.directory(temp.toFile());
        builder.redirectErrorStream(true);
        builder.redirectOutput(log.toFile());
        Process process = builder.start();
        try {
            assertTrue(process.waitFor(120, TimeUnit.SECONDS),
                    "dex2jar timed out");
        } finally {
            if (process.isAlive()) {
                process.destroyForcibly();
            }
        }
        assertTrue(Files.isRegularFile(jar), () -> "dex2jar produced no jar:\n"
                + readOrEmpty(log));
        return jar;
    }

    private static String readOrEmpty(Path log) {
        try {
            return Files.readString(log, StandardCharsets.UTF_8);
        } catch (Exception x) {
            return "<no log: " + x + ">";
        }
    }

    /** One differential method and its arguments. */
    private static final class Case {
        final String method;
        final Class<?>[] params;
        final Object[] args;

        Case(String method, Class<?>[] params, Object[] args) {
            this.method = method;
            this.params = params;
            this.args = args;
        }
    }

    /** Return value or thrown class, tagged with a reference-side gap flag. */
    private static final class Outcome {
        private final Object value;
        private final Class<?> thrown;
        private final String message;
        private final boolean referenceGap;

        private Outcome(Object value, Class<?> thrown, String message,
                boolean referenceGap) {
            this.value = value;
            this.thrown = thrown;
            this.message = message;
            this.referenceGap = referenceGap;
        }

        static Outcome value(Object value) {
            return new Outcome(value, null, null, false);
        }

        static Outcome thrown(Throwable cause, boolean referenceGap) {
            return new Outcome(null, cause.getClass(), cause.getMessage(),
                    referenceGap);
        }

        /** Marks dex2jar's own conversion stubs ({@code d2j} message). */
        Outcome asReferenceGap() {
            if (referenceGap || thrown == null || message == null
                    || !message.contains("d2j")) {
                return this;
            }
            return new Outcome(null, thrown, message, true);
        }

        boolean linkage() {
            return referenceGap;
        }

        boolean sameAs(Outcome other) {
            if (thrown != null || other.thrown != null) {
                return thrown == other.thrown;
            }
            return Objects.equals(value, other.value);
        }

        @Override
        public String toString() {
            if (thrown != null) {
                return "throws " + thrown.getSimpleName()
                        + (message == null ? "" : " (" + message + ")");
            }
            return String.valueOf(value);
        }
    }

    /** Defines one sanitized reference class in isolation. */
    private static final class SingleClassLoader extends ClassLoader {
        SingleClassLoader(ClassLoader parent) {
            super(parent);
        }

        Class<?> define(String name, byte[] bytes) {
            return defineClass(name, bytes, 0, bytes.length);
        }
    }
}
