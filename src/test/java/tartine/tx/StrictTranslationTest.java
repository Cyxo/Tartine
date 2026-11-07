package tartine.tx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.Set;
import java.util.TreeSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tartine.dex.Dex;
import tartine.dex.DexClass;
import tartine.dex.DexSet;
import tartine.dex.FixtureDex;
import tartine.guest.SandboxDenied;

/**
 * Strict-mode regression pin: translating the fixture corpus must degrade
 * exactly the methods documented here. A new entry means a translation
 * regression (or a new deliberate sandbox denial); a missing one means a
 * known gap was fixed and the baseline should shrink.
 */
final class StrictTranslationTest {
    /** Fixture demos for intentionally denied host/framework surfaces. */
    private static final Set<String> EXPECTED_DENIALS = Set.of(
            "Ltest/Hello;->applicationDemo()Ljava/lang/Object;",
            "Ltest/Hello;->blockedExec()V",
            "Ltest/Hello;->fwCast()I",
            "Ltest/Hello;->fwGhost()V",
            "Ltest/Hello;->fwInit()V",
            "Ltest/Hello;->intentDemo()Ljava/lang/Object;",
            "Ltest/Hello;->intentExtrasDemo()Ljava/lang/Object;",
            "Ltest/Hello;->missing()V",
            "Ltest/Hello;->retainedDemo()Ljava/lang/Object;",
            "Ltest/Hello;->runtimeExecDemo()Ljava/lang/Object;",
            "Ltest/Hello;->runtimeExitDemo()V",
            "Ltest/Hello;->runtimeHaltDemo()V",
            "Ltest/Hello;->stackDemo()I",
            "Ltest/Hello;->systemSetPropertyDemo()Ljava/lang/Object;");

    /**
     * Known translator defects in the fixture corpus: object-builder chains
     * whose initialized-instance tracking misses a register move, and a
     * second {@code <init>} on a reused register. Fixing one should remove
     * its entry here.
     */
    private static final Set<String> KNOWN_TRANSLATOR_GAPS = Set.of(
            "Ltest/Hello;->builderOptsDemo()Ljava/lang/Object;",
            "Ltest/Hello;->directionDemo()Ljava/lang/Object;",
            "Ltest/Hello;->handlerDemo()I",
            "Ltest/Hello;->lineBreakDemo()Ljava/lang/Object;");

    /** Fixture classes that deliberately extend an absent host class. */
    private static final Set<String> EXPECTED_CLASS_DENIALS = Set.of(
            "Ltest/Orphan;", "Ltest/Sub2;");

    @TempDir
    Path temp;

    @BeforeEach
    @AfterEach
    void reset() {
        TxTranslator.resetStrictGaps();
    }

    @Test
    void fixtureCorpusDegradesOnlyTheKnownMethods() throws Exception {
        Context context = fixture();
        TxTranslator.setStrictGaps(true);

        Set<String> denials = new TreeSet<>();
        Set<String> translatorGaps = new TreeSet<>();
        Set<String> classDenials = new TreeSet<>();
        for (Dex dex : context.dex.dexes()) {
            for (DexClass cls : dex.classes()) {
                translate(context, cls, denials, translatorGaps, classDenials);
            }
        }

        assertEquals(EXPECTED_DENIALS, denials);
        assertEquals(KNOWN_TRANSLATOR_GAPS, translatorGaps);
        assertEquals(EXPECTED_CLASS_DENIALS, classDenials);
    }

    @Test
    void strictAggregateNamesEveryDegradedMethod() throws Exception {
        Context context = fixture();
        DexClass hello = context.dex.findClass("Ltest/Hello;");

        // Default mode keeps the live-and-degrade behavior.
        TxTranslator.translate(hello, context.loader.fieldSynths(),
                context.loader.guestInterfaces(), context.loader.dexLookup(),
                context.parent, context.loader.resolver());

        TxTranslator.setStrictGaps(true);
        TxTranslator.TxGap gap = assertThrows(TxTranslator.TxGap.class,
                () -> TxTranslator.translate(hello,
                        context.loader.fieldSynths(),
                        context.loader.guestInterfaces(),
                        context.loader.dexLookup(), context.parent,
                        context.loader.resolver()));

        int expected = EXPECTED_DENIALS.size()
                + KNOWN_TRANSLATOR_GAPS.size();
        assertEquals(expected, gap.degradations().size());
        assertTrue(gap.getMessage().startsWith(
                "strict translation: " + expected
                        + " degraded method(s) in Ltest/Hello;:"),
                gap.getMessage());
        for (TxTranslator.TxGap.Degradation degradation
                : gap.degradations()) {
            assertTrue(gap.getMessage().contains(
                    degradation.signature() + ": " + degradation.reason()),
                    degradation.signature());
        }
    }

    private Context fixture() throws Exception {
        Path dexFile = temp.resolve("strict-fixture.dex");
        FixtureDex.writeHello(dexFile);
        DexSet dex = DexSet.of(Dex.load(dexFile.toFile()));
        ClassLoader parent = StrictTranslationTest.class.getClassLoader();
        return new Context(dex, new TranslatedLoader(parent, dex), parent);
    }

    private static void translate(Context context, DexClass cls,
            Set<String> denials, Set<String> translatorGaps,
            Set<String> classDenials) {
        try {
            TxTranslator.translate(cls, context.loader.fieldSynths(),
                    context.loader.guestInterfaces(),
                    context.loader.dexLookup(), context.parent,
                    context.loader.resolver());
        } catch (TxTranslator.TxGap gap) {
            assertFalse(gap.degradations().isEmpty(),
                    () -> "strict gap without details: " + gap.getMessage());
            for (TxTranslator.TxGap.Degradation degradation
                    : gap.degradations()) {
                if (degradation.reason().startsWith("sandbox denied: ")) {
                    denials.add(degradation.signature());
                } else {
                    translatorGaps.add(degradation.signature());
                }
            }
        } catch (SandboxDenied denied) {
            classDenials.add(cls.type);
        }
    }

    /** Fixture dex plus the loader exposing translation-time seams. */
    private record Context(DexSet dex, TranslatedLoader loader,
            ClassLoader parent) {
    }
}
