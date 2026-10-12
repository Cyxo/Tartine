package aine.vm;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import aine.dex.Dex;
import aine.dex.DexSet;
import aine.dex.FixtureDex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Runs the shared fixture dex on the interpreter. */
class VmTest {
    @TempDir
    private Path tempDir;

    private Interpreter interpreter;

    @BeforeEach
    void loadFixture() throws Exception {
        Path dexFile = tempDir.resolve("fixture.dex");
        FixtureDex.writeHello(dexFile);
        interpreter = new Interpreter(DexSet.of(Dex.load(dexFile.toFile())));
    }

    @Test
    void twiceCallsIdAndDoubles() {
        assertEquals(Integer.valueOf(42), interpreter.call(FixtureDex.HELLO, "twice", 21));
    }

    @Test
    void idPassesThrough() {
        assertEquals(Integer.valueOf(7), interpreter.call(FixtureDex.HELLO, "id", 7));
    }

    @Test
    void greetReturnsString() {
        assertEquals("hi", interpreter.call(FixtureDex.HELLO, "greet"));
    }

    @Test
    void unknownMethodFailsLoudly() {
        assertThrows(VmException.class, () -> interpreter.call(FixtureDex.HELLO, "nope"));
    }

    @Test
    void makeUsesHeapField() {
        assertEquals(Integer.valueOf(5), interpreter.call(FixtureDex.HELLO, "make"));
    }

    @Test
    void sumUsesArray() {
        assertEquals(Integer.valueOf(24), interpreter.call(FixtureDex.HELLO, "sum"));
    }

    @Test
    void lenReadsArrayLength() {
        assertEquals(Integer.valueOf(5), interpreter.call(FixtureDex.HELLO, "len"));
    }

    @Test
    void safelyCatchesThrow() {
        assertEquals(Integer.valueOf(7), interpreter.call(FixtureDex.HELLO, "safely"));
    }

    @Test
    void bootRunsClinit() {
        assertEquals(Integer.valueOf(42), interpreter.call(FixtureDex.HELLO, "boot"));
    }

    @Test
    void nullCastPasses() {
        assertNull(interpreter.call(FixtureDex.HELLO, "nullCast"));
    }

    @Test
    void castOkWalksHierarchy() {
        assertEquals(Integer.valueOf(1), interpreter.call(FixtureDex.HELLO, "castOk"));
    }

    @Test
    void castFailThrows() {
        assertThrows(ClassCastException.class,
                () -> interpreter.call(FixtureDex.HELLO, "castFail"));
    }

    @Test
    void cmpTinyComparesLongs() {
        assertEquals(Integer.valueOf(-1), interpreter.call(FixtureDex.HELLO, "cmpTiny"));
    }

    @Test
    void stepsWereExecuted() {
        interpreter.call(FixtureDex.HELLO, "twice", 21);
        assertTrue(interpreter.steps() > 0);
    }
}
