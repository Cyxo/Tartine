package tartine.tx;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tartine.dex.Dex;
import tartine.dex.DexSet;
import tartine.dex.FixtureDex;
import tartine.dex.Insn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Guest/host reference resolution and the up-front unresolved census. */
final class ReferenceResolverTest {
    private static TranslatedLoader loader;

    @BeforeAll
    static void loadFixture() throws Exception {
        Path dexFile = Files.createTempFile("refs-fixture", ".dex");
        FixtureDex.writeHello(dexFile);
        DexSet dex = DexSet.of(Dex.load(dexFile.toFile()));
        loader = new TranslatedLoader(ReferenceResolverTest.class.getClassLoader(), dex);
    }

    private static ReferenceResolver resolver() {
        return loader.resolver();
    }

    @Test
    void resolvesFieldsToTheirDeclaringClass() {
        assertEquals(FixtureDex.COUNTER, resolver().fieldDeclaring(
                new Insn.FieldRef(FixtureDex.COUNTER, "count", "I")));
        // No dex class declares this field: it lives on the synthesized owner.
        assertEquals(FixtureDex.HELLO, resolver().fieldDeclaring(
                new Insn.FieldRef(FixtureDex.HELLO, "counter", "I")));
        // Guest subclass, field declared by a host ancestor.
        assertEquals("Landroid/view/ViewGroup;", resolver().fieldDeclaring(
                new Insn.FieldRef(FixtureDex.VG, "mGroupFlags", "I")));
        assertTrue(resolver().hasField(
                new Insn.FieldRef("Landroid/view/ViewGroup$LayoutParams;", "width", "I")));
        assertFalse(resolver().hasField(
                new Insn.FieldRef(FixtureDex.HELLO, "nope", "I")));
    }

    @Test
    void detectsGuestAndHostInterfaces() {
        assertTrue(resolver().isInterface("Lkotlin/jvm/functions/Function0;"));
        assertTrue(resolver().isInterface("Ljava/util/List;"));
        assertFalse(resolver().isInterface(FixtureDex.HELLO));
        assertFalse(resolver().isInterface("Landroid/view/ViewGroup;"));
        assertFalse(resolver().isInterface("[Ljava/lang/Object;"));
    }

    @Test
    void resolvesMethodsAcrossGuestAndHostHierarchies() {
        assertTrue(resolver().hasMethod(new Insn.MethodRef(FixtureDex.SUB, "who",
                List.of(), "Ljava/lang/String;")));
        assertTrue(resolver().hasMethod(new Insn.MethodRef("Landroid/view/View;",
                "setLayoutParams", List.of("Landroid/view/ViewGroup$LayoutParams;"), "V")));
        assertTrue(resolver().hasMethod(new Insn.MethodRef("Ljava/lang/invoke/MethodHandle;",
                "invokeExact", List.of("I", "J"), "J")));
        assertTrue(resolver().hasMethod(new Insn.MethodRef("[Ljava/lang/Object;",
                "clone", List.of(), "Ljava/lang/Object;")));
        assertFalse(resolver().hasMethod(new Insn.MethodRef(FixtureDex.FW_GHOST,
                "<init>", List.of(), "V")));
        assertFalse(resolver().hasMethod(new Insn.MethodRef(FixtureDex.HELLO,
                "nope", List.of(), "V")));
    }

    @Test
    void censusNamesMissingFixtureReferences() {
        List<ReferenceResolver.Problem> problems = loader.referenceProblems();
        String text = problems.toString();
        assertTrue(text.contains("Lfw/Base;"), text);
        assertTrue(text.contains("Lfw/Ghost;"), text);
        assertTrue(text.contains("Lfw/Sub;"), text);
        // Resolvable guest/host references stay out of the census.
        assertFalse(text.contains("Ltest/Counter;->count:I"), text);
        assertFalse(text.contains("Ltest/Sub;->who()Ljava/lang/String;"), text);
        assertFalse(text.contains("Landroid/view/View;->setLayoutParams"), text);
    }
}
