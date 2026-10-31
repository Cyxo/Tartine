package tartine.dex;

import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Typed references and payload links in the internal instruction model. */
class InsnModelTest {
    @TempDir
    private Path tempDir;

    private Dex dex;

    @BeforeEach
    void buildFixture() throws Exception {
        Path dexFile = tempDir.resolve("fixture.dex");
        FixtureDex.writeHello(dexFile);
        dex = Dex.load(dexFile.toFile());
    }

    private Insn insn(String method, String opcode) {
        for (Insn in : dex.findClass(FixtureDex.HELLO).method(method).instructions) {
            if (in.opcode.equals(opcode)) {
                return in;
            }
        }
        throw new AssertionError("no " + opcode + " in " + method);
    }

    @Test
    void decodesTypedFieldReference() {
        Insn.FieldRef field = assertInstanceOf(Insn.FieldRef.class,
                insn("make", "iput").reference);
        assertEquals(FixtureDex.COUNTER, field.owner());
        assertEquals("count", field.name());
        assertEquals("I", field.type());
        assertEquals("Ltest/Counter;->count:I", field.text());
    }

    @Test
    void decodesTypedMethodReference() {
        Insn.MethodRef method = assertInstanceOf(Insn.MethodRef.class,
                insn("twice", "invoke-static").reference);
        assertEquals(FixtureDex.HELLO, method.owner());
        assertEquals("id", method.name());
        assertEquals(List.of("I"), method.parameterTypes());
        assertEquals("I", method.returnType());
        assertEquals("(I)I", method.descriptor());
        assertEquals("Ltest/Hello;->id(I)I", method.text());
    }

    @Test
    void decodesStringAndTypeReferences() {
        Insn.StringRef string = assertInstanceOf(Insn.StringRef.class,
                insn("greet", "const-string").reference);
        assertEquals("hi", string.value());
        assertEquals("hi", string.text());

        Insn.TypeRef type = assertInstanceOf(Insn.TypeRef.class,
                insn("constClass", "const-class").reference);
        assertEquals("Ltest/Counter;", type.descriptor());
        assertEquals("Ltest/Counter;", type.text());
    }

    @Test
    void linksSwitchPayload() {
        Insn sw = insn("switchDemo", "packed-switch");
        assertTrue(sw.payloadIndex >= 0);
        Insn payload = dex.findClass(FixtureDex.HELLO).method("switchDemo")
                .instructions.get(sw.payloadIndex);
        assertEquals("packed-switch-payload", payload.opcode);
        assertEquals(sw.codeOffset + sw.branchOffset, payload.codeOffset);
        assertFalse(payload.payloadKeys.isEmpty());
        assertEquals(payload.payloadKeys.size(), payload.payloadTargets.size());
    }

    @Test
    void linksFillArrayPayload() {
        Insn fill = insn("fillDemo", "fill-array-data");
        assertTrue(fill.payloadIndex >= 0);
        Insn payload = dex.findClass(FixtureDex.HELLO).method("fillDemo")
                .instructions.get(fill.payloadIndex);
        assertEquals("array-payload", payload.opcode);
        assertEquals(fill.codeOffset + fill.branchOffset, payload.codeOffset);
        assertFalse(payload.payloadData.isEmpty());
    }
}
