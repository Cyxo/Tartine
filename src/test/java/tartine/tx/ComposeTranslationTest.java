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
}
