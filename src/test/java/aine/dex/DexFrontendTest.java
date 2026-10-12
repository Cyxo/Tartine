package aine.dex;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;

import org.jf.dexlib2.AccessFlags;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.builder.MutableMethodImplementation;
import org.jf.dexlib2.builder.instruction.BuilderInstruction10x;
import org.jf.dexlib2.builder.instruction.BuilderInstruction11n;
import org.jf.dexlib2.builder.instruction.BuilderInstruction11x;
import org.jf.dexlib2.builder.instruction.BuilderInstruction12x;
import org.jf.dexlib2.builder.instruction.BuilderInstruction21c;
import org.jf.dexlib2.builder.instruction.BuilderInstruction23x;
import org.jf.dexlib2.builder.instruction.BuilderInstruction35c;
import org.jf.dexlib2.immutable.ImmutableMethodParameter;
import org.jf.dexlib2.immutable.reference.ImmutableMethodReference;
import org.jf.dexlib2.writer.builder.BuilderMethod;
import org.jf.dexlib2.writer.builder.BuilderMethodReference;
import org.jf.dexlib2.writer.builder.DexBuilder;
import org.jf.dexlib2.writer.io.FileDataStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the dexlib2 frontend: a fixture dex is built with dexlib2's own
 * writer, round-tripped through real bytes, loaded via {@link Dex}, and
 * asserted structurally plus as golden dump text. Branch/payload-heavy
 * real-dex coverage lives in the manual baksmali cross-check (THIRD-PARTY.md).
 */
class DexFrontendTest {
    private static final String HELLO = "Ltest/Hello;";

    @TempDir
    private Path tempDir;

    private Dex dex;

    @BeforeEach
    void buildFixture() throws Exception {
        Path dexFile = tempDir.resolve("fixture.dex");
        writeFixtureDex(dexFile);
        dex = Dex.load(dexFile.toFile());
    }

    @Test
    void loadsOneClassWithFourMethods() {
        assertEquals(1, dex.classes().size());
        DexClass cls = dex.findClass(HELLO);
        assertNotNull(cls);
        assertNull(dex.findClass("Ltest/Missing;"));
        assertEquals(4, cls.methods.size());
        assertNotNull(cls.method("<init>"));
        assertNotNull(cls.method("twice"));
    }

    @Test
    void readsRegisterCounts() {
        assertEquals(1, dex.findClass(HELLO).method("<init>").registerCount);
        assertEquals(2, dex.findClass(HELLO).method("twice").registerCount);
        for (DexMethod method : dex.findClass(HELLO).methods) {
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
        assertEquals(expected, DexDump.dump(dex.findClass(HELLO)));
    }

    private static void writeFixtureDex(Path dexFile) throws Exception {
        Opcodes opcodes = Opcodes.getDefault();
        DexBuilder builder = new DexBuilder(opcodes);
        BuilderMethodReference objectInit = builder.internMethodReference(
                new ImmutableMethodReference("Ljava/lang/Object;", "<init>",
                        Collections.<String>emptyList(), "V"));
        BuilderMethodReference helloId = builder.internMethodReference(
                new ImmutableMethodReference(HELLO, "id",
                        Collections.singletonList("I"), "I"));

        MutableMethodImplementation initImpl = new MutableMethodImplementation(1);
        initImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0, 0,
                objectInit));
        initImpl.addInstruction(new BuilderInstruction10x(Opcode.RETURN_VOID));
        BuilderMethod init = builder.internMethod(HELLO, "<init>",
                Collections.<ImmutableMethodParameter>emptyList(), "V",
                AccessFlags.PUBLIC.getValue() | AccessFlags.CONSTRUCTOR.getValue(),
                Collections.emptySet(), Collections.emptySet(), initImpl);

        MutableMethodImplementation idImpl = new MutableMethodImplementation(2);
        idImpl.addInstruction(new BuilderInstruction12x(Opcode.MOVE, 0, 1));
        idImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod id = builder.internMethod(HELLO, "id",
                Collections.singletonList(
                        new ImmutableMethodParameter("I", Collections.emptySet(), "x")),
                "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), idImpl);

        MutableMethodImplementation twiceImpl = new MutableMethodImplementation(2);
        twiceImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 1, 0, 0, 0, 0,
                helloId));
        twiceImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        twiceImpl.addInstruction(new BuilderInstruction12x(Opcode.ADD_INT_2ADDR, 0, 0));
        twiceImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod twice = builder.internMethod(HELLO, "twice",
                Collections.singletonList(
                        new ImmutableMethodParameter("I", Collections.emptySet(), "x")),
                "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), twiceImpl);

        MutableMethodImplementation greetImpl = new MutableMethodImplementation(1);
        greetImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 0,
                builder.internStringReference("hi")));
        greetImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod greet = builder.internMethod(HELLO, "greet",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/String;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), greetImpl);

        builder.internClassDef(HELLO,
                AccessFlags.PUBLIC.getValue(), "Ljava/lang/Object;",
                Collections.<String>emptyList(), "Hello.java",
                Collections.emptySet(), Collections.<org.jf.dexlib2.writer.builder.BuilderField>emptyList(),
                Arrays.asList(init, id, twice, greet));
        builder.writeTo(new FileDataStore(dexFile.toFile()));
    }
}
