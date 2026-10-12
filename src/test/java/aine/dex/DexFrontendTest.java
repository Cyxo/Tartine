package aine.dex;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the dexlib2 frontend against the shared writer-built fixture:
 * structural reads plus golden dump text. Branch/payload-heavy real-dex
 * coverage lives in the manual baksmali cross-check (THIRD-PARTY.md).
 */
class DexFrontendTest {
    @TempDir
    private Path tempDir;

    private Dex dex;

    @BeforeEach
    void buildFixture() throws Exception {
        Path dexFile = tempDir.resolve("fixture.dex");
        FixtureDex.writeHello(dexFile);
        dex = Dex.load(dexFile.toFile());
    }

    @Test
    void loadsClassesAndMethods() {
        assertEquals(4, dex.classes().size());
        DexClass cls = dex.findClass(FixtureDex.HELLO);
        assertNotNull(cls);
        assertNull(dex.findClass("Ltest/Missing;"));
        assertEquals(14, cls.methods.size());
        assertNotNull(cls.method("<init>"));
        assertNotNull(cls.method("twice"));
        assertNotNull(dex.findClass(FixtureDex.COUNTER));
        assertNotNull(dex.findClass(FixtureDex.BASE));
        assertNotNull(dex.findClass(FixtureDex.SUB));
    }

    @Test
    void readsRegisterCounts() {
        assertEquals(1, dex.findClass(FixtureDex.HELLO).method("<init>").registerCount);
        assertEquals(2, dex.findClass(FixtureDex.HELLO).method("twice").registerCount);
        for (DexMethod method : dex.findClass(FixtureDex.HELLO).methods) {
            assertTrue(method.hasImplementation);
        }
    }

    @Test
    void dumpsGoldenText() {
        StringBuilder actual = new StringBuilder();
        for (DexClass cls : dex.classes()) {
            actual.append(DexDump.dump(cls));
        }
        String expected =
                "class Ltest/Base;\n"
                + "method <init>()V regs=1\n"
                + "  0: invoke-direct v0, Ljava/lang/Object;-><init>()V\n"
                + "  3: return-void\n"
                + "class Ltest/Counter;\n"
                + "method <init>()V regs=1\n"
                + "  0: invoke-direct v0, Ljava/lang/Object;-><init>()V\n"
                + "  3: return-void\n"
                + "class Ltest/Hello;\n"
                + "method <clinit>()V regs=1\n"
                + "  0: const-16 v0, #9\n"
                + "  2: mul-int-lit8 v0, v0, #4\n"
                + "  4: add-int-lit8 v0, v0, #5\n"
                + "  6: sput v0, Ltest/Hello;->counter:I\n"
                + "  8: return-void\n"
                + "method <init>()V regs=1\n"
                + "  0: invoke-direct v0, Ljava/lang/Object;-><init>()V\n"
                + "  3: return-void\n"
                + "method boot()I regs=1\n"
                + "  0: sget v0, Ltest/Hello;->counter:I\n"
                + "  2: add-int-lit8 v0, v0, #1\n"
                + "  4: return v0\n"
                + "method castFail()V regs=1\n"
                + "  0: new-instance v0, Ltest/Sub;\n"
                + "  2: move-result-object v0\n"
                + "  3: invoke-direct v0, Ltest/Sub;-><init>()V\n"
                + "  6: check-cast v0, Ltest/Counter;\n"
                + "  8: return-void\n"
                + "method castOk()I regs=2\n"
                + "  0: new-instance v0, Ltest/Sub;\n"
                + "  2: move-result-object v0\n"
                + "  3: invoke-direct v0, Ltest/Sub;-><init>()V\n"
                + "  6: check-cast v0, Ltest/Base;\n"
                + "  8: instance-of v1, v0, Ltest/Base;\n"
                + "  10: return v1\n"
                + "method cmpTiny()I regs=5\n"
                + "  0: const-wide-16 v0, #5\n"
                + "  2: const-wide-16 v2, #7\n"
                + "  4: cmp-long v4, v0, v2\n"
                + "  6: return v4\n"
                + "method greet()Ljava/lang/String; regs=1\n"
                + "  0: const-string v0, hi\n"
                + "  2: return-object v0\n"
                + "method id(I)I regs=2\n"
                + "  0: move v0, v1\n"
                + "  1: return v0\n"
                + "method len()I regs=2\n"
                + "  0: const-4 v0, #5\n"
                + "  1: new-array v1, v0, [I\n"
                + "  3: array-length v0, v1\n"
                + "  4: return v0\n"
                + "method make()I regs=3\n"
                + "  0: new-instance v0, Ltest/Counter;\n"
                + "  2: move-result-object v0\n"
                + "  3: invoke-direct v0, Ltest/Counter;-><init>()V\n"
                + "  6: const-4 v1, #4\n"
                + "  7: iput v1, v0, Ltest/Counter;->count:I\n"
                + "  9: iget v2, v0, Ltest/Counter;->count:I\n"
                + "  11: add-int-lit8 v2, v2, #1\n"
                + "  13: return v2\n"
                + "method nullCast()V regs=1\n"
                + "  0: check-cast v0, Ltest/Counter;\n"
                + "  2: return-void\n"
                + "method safely()I regs=1\n"
                + "  0: const-string v0, boom\n"
                + "  2: throw v0\n"
                + "  3: const-4 v0, #3\n"
                + "  4: return v0\n"
                + "  5: move-exception v0\n"
                + "  6: const-4 v0, #7\n"
                + "  7: return v0\n"
                + "method sum()I regs=3\n"
                + "  0: const-4 v1, #3\n"
                + "  1: new-array v0, v1, [I\n"
                + "  3: const-4 v1, #0\n"
                + "  4: const-16 v2, #7\n"
                + "  6: aput v2, v0, v1\n"
                + "  8: const-4 v1, #1\n"
                + "  9: const-16 v2, #8\n"
                + "  11: aput v2, v0, v1\n"
                + "  13: const-4 v1, #2\n"
                + "  14: const-16 v2, #9\n"
                + "  16: aput v2, v0, v1\n"
                + "  18: const-4 v1, #0\n"
                + "  19: aget v1, v0, v1\n"
                + "  21: const-4 v2, #1\n"
                + "  22: aget v2, v0, v2\n"
                + "  24: add-int v1, v1, v2\n"
                + "  26: const-4 v2, #2\n"
                + "  27: aget v2, v0, v2\n"
                + "  29: add-int v1, v1, v2\n"
                + "  31: return v1\n"
                + "method twice(I)I regs=2\n"
                + "  0: invoke-static v1, Ltest/Hello;->id(I)I\n"
                + "  3: move-result v0\n"
                + "  4: add-int-2addr v0, v0\n"
                + "  5: return v0\n"
                + "class Ltest/Sub;\n"
                + "method <init>()V regs=1\n"
                + "  0: invoke-direct v0, Ltest/Base;-><init>()V\n"
                + "  3: return-void\n";
        assertEquals(expected, actual.toString());
    }
}
