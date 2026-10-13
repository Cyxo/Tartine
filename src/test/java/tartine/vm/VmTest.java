package tartine.vm;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import tartine.dex.Dex;
import tartine.dex.DexSet;
import tartine.dex.FixtureDex;
import tartine.fw.FwIndex;
import tartine.fw.FwIndex.FwClass;
import tartine.fw.FwIndex.FwField;

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
    void nopsSkips() {
        assertEquals(Integer.valueOf(9), interpreter.call(FixtureDex.HELLO, "nops"));
    }

    @Test
    void covarOkAccepts() {
        assertEquals(Integer.valueOf(1), interpreter.call(FixtureDex.HELLO, "covarOk"));
    }

    @Test
    void covarPrimRejects() {
        assertThrows(ClassCastException.class,
                () -> interpreter.call(FixtureDex.HELLO, "covarPrim"));
    }

    @Test
    void constClassNames() {
        assertEquals("test.Counter", interpreter.call(FixtureDex.HELLO, "constClass"));
    }

    @Test
    void clazzIsInstanceWalks() {
        assertEquals(Integer.valueOf(1), interpreter.call(FixtureDex.HELLO, "clazzIsInstance"));
    }

    @Test
    void updaterCasAndGet() {
        assertEquals(Integer.valueOf(8), interpreter.call(FixtureDex.HELLO, "updater"));
    }

    @Test
    void sysClockTicks() {
        Object result = interpreter.call(FixtureDex.HELLO, "sysClock");
        assertTrue(result instanceof Long && (Long) result > 0);
    }

    @Test
    void longArithComputes() {
        assertEquals(Long.valueOf(100L), interpreter.call(FixtureDex.HELLO, "longArith"));
    }

    @Test
    void floatArithComputes() {
        assertEquals(Float.valueOf(5.5f), interpreter.call(FixtureDex.HELLO, "floatArith"));
    }

    @Test
    void doubleArithComputes() {
        assertEquals(Double.valueOf(3.5), interpreter.call(FixtureDex.HELLO, "doubleArith"));
    }

    @Test
    void lazyDemoInitializesOnce() {
        assertEquals(Integer.valueOf(99), interpreter.call(FixtureDex.HELLO, "lazyDemo"));
    }

    @Test
    void lazyIsInstanceMatches() {
        assertEquals(Integer.valueOf(1), interpreter.call(FixtureDex.HELLO, "lazyIsInstance"));
    }

    @Test
    void executorRunsInline() {
        assertEquals(Integer.valueOf(7), interpreter.call(FixtureDex.HELLO, "executorDemo"));
    }

    @Test
    void stepsWereExecuted() {
        interpreter.call(FixtureDex.HELLO, "twice", 21);
        assertTrue(interpreter.steps() > 0);
    }

    @Test
    void sbRunsHostStringBuilder() {
        assertEquals("a1", interpreter.call(FixtureDex.HELLO, "sb"));
    }

    @Test
    void throwCatchKeepsMessage() {
        assertEquals("desk", interpreter.call(FixtureDex.HELLO, "throwCatch"));
    }

    @Test
    void intBoxRoundTrips() {
        assertEquals(Integer.valueOf(42), interpreter.call(FixtureDex.HELLO, "intBox"));
    }

    @Test
    void arrayCopyCopies() {
        assertEquals(Integer.valueOf(8), interpreter.call(FixtureDex.HELLO, "arrayCopy"));
    }

    @Test
    void appendableCastWalksHostInterfaces() {
        assertEquals(Integer.valueOf(1), interpreter.call(FixtureDex.HELLO, "appendableCast"));
    }

    @Test
    void badCastRejects() {
        assertThrows(ClassCastException.class,
                () -> interpreter.call(FixtureDex.HELLO, "badCast"));
    }

    @Test
    void stringOpsCompute() {
        assertEquals(Integer.valueOf(7), interpreter.call(FixtureDex.HELLO, "stringOps"));
    }

    @Test
    void mathMaxComputes() {
        assertEquals(Integer.valueOf(7), interpreter.call(FixtureDex.HELLO, "mathMax"));
    }

    @Test
    void missingMethodWithoutIndexIsUnimplemented() {
        VmException e = assertThrows(VmException.class,
                () -> interpreter.call(FixtureDex.HELLO, "missing"));
        assertTrue(e.getMessage().startsWith("unimplemented: Lfw/Base;->nope()V"),
                e.getMessage());
    }

    @Test
    void missingMethodWithIndexIsPrecise() {
        interpreter.setFramework(framework());
        VmException e = assertThrows(VmException.class,
                () -> interpreter.call(FixtureDex.HELLO, "missing"));
        assertTrue(e.getMessage().startsWith("no such method: Lfw/Base;->nope()V"),
                e.getMessage());
    }

    @Test
    void fwCastMergesHierarchies() {
        interpreter.setFramework(framework());
        assertEquals(Integer.valueOf(1), interpreter.call(FixtureDex.HELLO, "fwCast"));
    }

    @Test
    void fwGhostNamesUnknownSuperclass() {
        interpreter.setFramework(framework());
        VmException e = assertThrows(VmException.class,
                () -> interpreter.call(FixtureDex.HELLO, "fwGhost"));
        assertTrue(e.getMessage().contains("unimplemented class: Lfw/Ghost;"), e.getMessage());
    }

    @Test
    void fwConstantsServeWithoutInit() {
        interpreter.setFramework(framework());
        assertEquals(Integer.valueOf(0), interpreter.call(FixtureDex.HELLO, "vis"));
    }

    @Test
    void fwMissingStaticIsUnimplemented() {
        interpreter.setFramework(framework());
        VmException e = assertThrows(VmException.class,
                () -> interpreter.call(FixtureDex.HELLO, "sdk"));
        assertTrue(e.getMessage().contains("unimplemented:"), e.getMessage());
    }

    @Test
    void frameworkMissingIsUnimplemented() {
        VmException e = assertThrows(VmException.class,
                () -> interpreter.call(FixtureDex.HELLO, "vis"));
        assertTrue(e.getMessage().contains("unimplemented:"), e.getMessage());
    }

    @Test
    void fwInitNeedsIndex() {
        VmException e = assertThrows(VmException.class,
                () -> interpreter.call(FixtureDex.HELLO, "fwInit"));
        assertTrue(e.getMessage().contains("unimplemented: Lfw/Sub;"), e.getMessage());
    }

    @Test
    void fwInitWithIndexPasses() {
        interpreter.setFramework(framework());
        assertNull(interpreter.call(FixtureDex.HELLO, "fwInit"));
    }

    @Test
    void activityStubRuns() {
        interpreter.setFramework(framework());
        assertNull(interpreter.call(FixtureDex.HELLO, "activityStub"));
    }

    private static FwIndex framework() {
        FwIndex index = new FwIndex();
        Map<String, Object> viewConstants = new HashMap<>();
        viewConstants.put("VISIBLE", 0);
        index.put(new FwClass("Landroid/view/View;", "Ljava/lang/Object;",
                Collections.<String>emptyList(),
                Collections.<FwIndex.FwMethod>emptyList(),
                Collections.singletonList(new FwField("VISIBLE", "I")), viewConstants));
        index.put(new FwClass("Landroid/os/Build$VERSION;", "Ljava/lang/Object;",
                Collections.<String>emptyList(),
                Collections.<FwIndex.FwMethod>emptyList(),
                Collections.<FwIndex.FwField>emptyList(),
                Collections.<String, Object>emptyMap()));
        index.put(new FwClass("Lfw/Base;", "Ljava/lang/Object;",
                Collections.<String>emptyList(),
                Collections.<FwIndex.FwMethod>emptyList(),
                Collections.<FwIndex.FwField>emptyList(),
                Collections.<String, Object>emptyMap()));
        index.put(new FwClass("Lfw/Sub;", "Lfw/Base;",
                Collections.<String>emptyList(),
                Collections.<FwIndex.FwMethod>emptyList(),
                Collections.<FwIndex.FwField>emptyList(),
                Collections.<String, Object>emptyMap()));
        index.put(new FwClass("Landroid/app/Activity;", "Ljava/lang/Object;",
                Collections.<String>emptyList(),
                Collections.<FwIndex.FwMethod>emptyList(),
                Collections.<FwIndex.FwField>emptyList(),
                Collections.<String, Object>emptyMap()));
        return index;
    }
}
