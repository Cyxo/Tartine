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
    void loadsOneClassWithFourMethods() {
        assertEquals(1, dex.classes().size());
        DexClass cls = dex.findClass(FixtureDex.HELLO);
        assertNotNull(cls);
        assertNull(dex.findClass("Ltest/Missing;"));
        assertEquals(4, cls.methods.size());
        assertNotNull(cls.method("<init>"));
        assertNotNull(cls.method("twice"));
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
        String expected =
                "class Ltest/Hello;\n"
                + "method <init>()V regs=1\n"
                + "  0: invoke-direct v0, Ljava/lang/Object;-><init>()V\n"
                + "  3: return-void\n"
                + "method greet()Ljava/lang/String; regs=1\n"
                + "  0: const-string v0, hi\n"
                + "  2: return-object v0\n"
                + "method id(I)I regs=2\n"
                + "  0: move v0, v1\n"
                + "  1: return v0\n"
                + "method twice(I)I regs=2\n"
                + "  0: invoke-static v1, Ltest/Hello;->id(I)I\n"
                + "  3: move-result v0\n"
                + "  4: add-int-2addr v0, v0\n"
                + "  5: return v0\n";
        assertEquals(expected, DexDump.dump(dex.findClass(FixtureDex.HELLO)));
    }
}
