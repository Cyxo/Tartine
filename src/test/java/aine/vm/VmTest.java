package aine.vm;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import aine.dex.Dex;
import aine.dex.FixtureDex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Runs the shared fixture dex on the interpreter spike. */
class VmTest {
    @TempDir
    private Path tempDir;

    private Interpreter interpreter;

    @BeforeEach
    void loadFixture() throws Exception {
        Path dexFile = tempDir.resolve("fixture.dex");
        FixtureDex.writeHello(dexFile);
        interpreter = new Interpreter(Dex.load(dexFile.toFile()));
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
}
