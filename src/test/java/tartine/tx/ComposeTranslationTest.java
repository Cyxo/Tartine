package tartine.tx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import tartine.apk.ApkReader;
import tartine.dex.DexSet;

/**
 * Real-corpus translation pin: the committed Compose APK's reflection
 * bridge exercises shapes the synthetic fixture does not. Auditing one class
 * keeps the test fast while still running strict translation over real d8
 * output.
 */
final class ComposeTranslationTest {
    /**
     * {@code ComposableMethod.invoke} used to degrade at its loop-exit
     * {@code array-length}: a dead throw arm overwrote the array register,
     * and the linear replay erased the descriptor before the join. Keep the
     * class clean under strict mode.
     */
    @Test
    void composableMethodTranslatesUnderStrictMode() throws Exception {
        Path apk = Path.of("apks", "app-debug.apk");
        assumeTrue(Files.exists(apk), "committed Compose fixture APK missing");
        try (ApkReader reader = ApkReader.open(apk)) {
            DexSet dex = reader.dexSet();
            TranslatedLoader loader = new TranslatedLoader(
                    ComposeTranslationTest.class.getClassLoader(), dex);
            StrictAudit.Result result = StrictAudit.run(
                    dex, loader, "Landroidx/compose/runtime/reflect/"
                            + "ComposableMethod;");
            assertEquals(1, result.audited());
            assertEquals(1, result.translated());
            assertTrue(result.clean(),
                    () -> result.translatorGaps().toString());
        }
    }

    /**
     * Join/scan shapes the synthetic fixture does not reach: a zero-const
     * fallthrough arm must not erase a branch crossing's array descriptor
     * ({@code LazyStaggeredGridMeasureKt.measure}), a skipped z-test then a
     * dst-only conversion consumes an int ({@code MathKt.log}), a scan must
     * not cross a fork ({@code FastFloatParserKt.nextFloat}), and a
     * finally-restore {@code move-wide} must not trust the linear replay's
     * stale narrow kind ({@code PainterNode.draw}).
     */
    @Test
    void handlerAndJoinShapesTranslateUnderStrictMode() throws Exception {
        Path apk = Path.of("apks", "app-debug.apk");
        assumeTrue(Files.exists(apk), "committed Compose fixture APK missing");
        String[] filters = {
            "Landroidx/compose/ui/draw/PainterNode;",
            "Landroidx/compose/foundation/lazy/staggeredgrid/"
                    + "LazyStaggeredGridMeasureKt;",
            "Lkotlin/math/MathKt__MathJVMKt;",
            "Landroidx/compose/ui/graphics/vector/FastFloatParserKt;",
        };
        try (ApkReader reader = ApkReader.open(apk)) {
            DexSet dex = reader.dexSet();
            TranslatedLoader loader = new TranslatedLoader(
                    ComposeTranslationTest.class.getClassLoader(), dex);
            for (String filter : filters) {
                StrictAudit.Result result = StrictAudit.run(dex, loader, filter);
                assertTrue(result.translated() > 0, filter);
                assertTrue(result.clean(),
                        () -> filter + ": " + result.translatorGaps());
            }
        }
    }
}
