package tartine.vm;

import java.nio.file.Path;
import java.util.Arrays;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    void postRunsInline() {
        assertEquals(Integer.valueOf(7), interpreter.call(FixtureDex.HELLO, "postDemo"));
    }

    @Test
    void handlerDispatchesMessage() {
        assertEquals(Integer.valueOf(42), interpreter.call(FixtureDex.HELLO, "handlerDemo"));
    }

    @Test
    void threadDemoNames() {
        Object result = interpreter.call(FixtureDex.HELLO, "threadDemo");
        assertTrue(result instanceof String);
    }

    @Test
    void eqComparesIntsByValue() {
        assertTrue(Interpreter.eq(Integer.valueOf(1000000), Integer.valueOf(1000000)));
    }

    @Test
    void eqComparesRefsByIdentity() {
        assertFalse(Interpreter.eq(new String("a"), new String("a")));
        assertTrue(Interpreter.eq(null, null));
        assertFalse(Interpreter.eq(null, "a"));
        Object same = new Object();
        assertTrue(Interpreter.eq(same, same));
    }

    @Test
    void mapDemoStores() {
        assertEquals(Integer.valueOf(42), interpreter.call(FixtureDex.HELLO, "mapDemo"));
    }

    @Test
    void listDemoIndexes() {
        assertEquals(Integer.valueOf(8), interpreter.call(FixtureDex.HELLO, "listDemo"));
    }

    @Test
    void setDemoDedupes() {
        assertEquals(Integer.valueOf(1), interpreter.call(FixtureDex.HELLO, "setDemo"));
    }

    @Test
    void iterDemoWalks() {
        assertEquals(Integer.valueOf(5), interpreter.call(FixtureDex.HELLO, "iterDemo"));
    }

    @Test
    void nullishCastsLikeNull() {
        assertEquals(Integer.valueOf(1), interpreter.call(FixtureDex.HELLO, "nullish"));
    }

    @Test
    void nullRecvFailsLikeNull() {
        assertThrows(NullPointerException.class,
                () -> interpreter.call(FixtureDex.HELLO, "nullRecv"));
    }

    @Test
    void boolStrBoxes() {
        assertEquals("true", interpreter.call(FixtureDex.HELLO, "boolStr"));
    }

    @Test
    void charStrBoxes() {
        assertEquals("A", interpreter.call(FixtureDex.HELLO, "charStr"));
    }

    @Test
    void weakGetReadsReferent() {
        interpreter.setFramework(framework());
        assertEquals("x", interpreter.call(FixtureDex.HELLO, "weakGet"));
    }

    @Test
    void weakClearEmpties() {
        interpreter.setFramework(framework());
        assertNull(interpreter.call(FixtureDex.HELLO, "weakClear"));
    }

    @Test
    void colorCmpComparesOrdinals() {
        interpreter.setFramework(framework());
        assertEquals(Integer.valueOf(-1), interpreter.call(FixtureDex.HELLO, "colorCmp"));
    }

    @Test
    void colorNameReads() {
        interpreter.setFramework(framework());
        assertEquals("RED", interpreter.call(FixtureDex.HELLO, "colorName"));
    }

    @Test
    void colorValueOfFinds() {
        interpreter.setFramework(framework());
        assertEquals("BLUE", interpreter.call(FixtureDex.HELLO, "colorValueOf"));
    }

    @Test
    void mapDefaultsEmulates() {
        assertEquals(Integer.valueOf(7), interpreter.call(FixtureDex.HELLO, "mapDefaults"));
    }

    @Test
    void mapPutAbsentShortCircuits() {
        assertEquals(Integer.valueOf(7), interpreter.call(FixtureDex.HELLO, "mapPutAbsent"));
    }

    @Test
    void repeatDemoReflects() {
        assertEquals("ababab", interpreter.call(FixtureDex.HELLO, "repeatDemo"));
    }

    @Test
    void emptyDemoReflectsField() {
        assertEquals(Integer.valueOf(1), interpreter.call(FixtureDex.HELLO, "emptyDemo"));
    }

    @Test
    void strictSqrtReflectsStatic() {
        assertEquals(Double.valueOf(4.0), interpreter.call(FixtureDex.HELLO, "strictSqrt"));
    }

    @Test
    void linkedMapDemoAllocatesReflectively() {
        assertEquals(Integer.valueOf(42), interpreter.call(FixtureDex.HELLO, "linkedMapDemo"));
    }

    @Test
    void blockedExecStaysDenied() {
        VmException e = assertThrows(VmException.class,
                () -> interpreter.call(FixtureDex.HELLO, "blockedExec"));
        assertTrue(e.getMessage().contains("unimplemented"), e.getMessage());
    }

    @Test
    void txChainPassesThrough() {
        assertEquals(Integer.valueOf(0), interpreter.call(FixtureDex.HELLO, "txChain"));
    }

    @Test
    void argbDemoPacks() {
        assertEquals(Integer.valueOf(0xFF010203), interpreter.call(FixtureDex.HELLO, "argbDemo"));
    }

    @Test
    void parseDemoParses() {
        assertEquals(Integer.valueOf(0xFF010203), interpreter.call(FixtureDex.HELLO, "parseDemo"));
    }

    @Test
    void activityWindowPlaceholds() {
        interpreter.setFramework(framework());
        assertNotNull(interpreter.call(FixtureDex.HELLO, "activityWindow"));
    }

    @Test
    void viewVisRoundTrips() {
        interpreter.setFramework(framework());
        assertEquals(Integer.valueOf(8), interpreter.call(FixtureDex.HELLO, "viewVis"));
    }

    @Test
    void layoutParamsKeepsSize() {
        interpreter.setFramework(framework());
        assertEquals(Integer.valueOf(-1), interpreter.call(FixtureDex.HELLO, "lpDemo"));
    }

    @Test
    void paintFlagsRoundTrip() {
        interpreter.setFramework(framework());
        assertEquals(Integer.valueOf(1), interpreter.call(FixtureDex.HELLO, "paintDemo"));
    }

    @Test
    void paintColorRoundTrips() {
        interpreter.setFramework(framework());
        assertEquals(Integer.valueOf(0x0102), interpreter.call(FixtureDex.HELLO, "colorDemo"));
    }

    @Test
    void paintStrokeRoundTrips() {
        interpreter.setFramework(framework());
        assertEquals(Float.valueOf(2.0f), interpreter.call(FixtureDex.HELLO, "strokeDemo"));
    }

    @Test
    void frameworkEnumReportsOrdinal() {
        interpreter.setFramework(framework());
        assertEquals(Integer.valueOf(1), interpreter.call(FixtureDex.HELLO, "styleDemo"));
    }

    @Test
    void hostEnumKeepsBehavior() {
        interpreter.setFramework(framework());
        assertEquals(Long.valueOf(60_000_000_000L),
                interpreter.call(FixtureDex.HELLO, "nanosDemo"));
    }

    @Test
    void paintStyleRoundTrips() {
        interpreter.setFramework(framework());
        assertEquals(Integer.valueOf(1),
                interpreter.call(FixtureDex.HELLO, "styleRoundDemo"));
    }

    @Test
    void sysServiceResolves() {
        interpreter.setFramework(framework());
        assertNotNull(interpreter.call(FixtureDex.HELLO, "sysServiceDemo"));
    }

    @Test
    void a11yListIsEmpty() {
        interpreter.setFramework(framework());
        assertEquals(Integer.valueOf(1), interpreter.call(FixtureDex.HELLO, "a11yDemo"));
    }

    @Test
    void sortDemoSortsRange() {
        assertEquals(Integer.valueOf(13), interpreter.call(FixtureDex.HELLO, "sortDemo"));
    }

    @Test
    void autofillServiceResolves() {
        interpreter.setFramework(framework());
        assertNotNull(interpreter.call(FixtureDex.HELLO, "autofillDemo"));
    }

    @Test
    void importantForAutofillIgnores() {
        interpreter.setFramework(framework());
        assertNotNull(interpreter.call(FixtureDex.HELLO, "importantDemo"));
    }

    @Test
    void clipboardServiceResolves() {
        interpreter.setFramework(framework());
        assertNotNull(interpreter.call(FixtureDex.HELLO, "clipboardDemo"));
    }

    @Test
    void treeSetBridgesComparator() {
        assertEquals(Integer.valueOf(2), interpreter.call(FixtureDex.HELLO, "treeDemo"));
    }

    @Test
    void ctorDemoConstructs() {
        assertNotNull(interpreter.call(FixtureDex.HELLO, "ctorDemo"));
    }

    @Test
    void layoutDirDefaultsLtr() {
        interpreter.setFramework(framework());
        assertEquals(Integer.valueOf(0), interpreter.call(FixtureDex.HELLO, "layoutDirDemo"));
    }

    @Test
    void touchModeStaysFalse() {
        interpreter.setFramework(framework());
        assertEquals(Integer.valueOf(0), interpreter.call(FixtureDex.HELLO, "touchModeDemo"));
    }

    @Test
    void willNotDrawIgnores() {
        interpreter.setFramework(framework());
        assertNotNull(interpreter.call(FixtureDex.HELLO, "willNotDrawDemo"));
    }

    @Test
    void focusableIgnores() {
        interpreter.setFramework(framework());
        assertNotNull(interpreter.call(FixtureDex.HELLO, "focusableDemo"));
    }

    @Test
    void viewConfigResolves() {
        interpreter.setFramework(framework());
        assertNotNull(interpreter.call(FixtureDex.HELLO, "viewConfigDemo"));
    }

    @Test
    void appContextResolves() {
        interpreter.setFramework(framework());
        assertNotNull(interpreter.call(FixtureDex.HELLO, "appCtxDemo"));
    }

    @Test
    void contentGroupResolves() {
        interpreter.setFramework(framework());
        assertNotNull(interpreter.call(FixtureDex.HELLO, "contentGroup"));
    }

    @Test
    void missFindStaysNull() {
        interpreter.setFramework(framework());
        assertNull(interpreter.call(FixtureDex.HELLO, "missFind"));
    }

    @Test
    void contentChildStartsNull() {
        interpreter.setFramework(framework());
        assertNull(interpreter.call(FixtureDex.HELLO, "contentChild"));
    }

    @Test
    void viewCtxKeepsContext() {
        interpreter.setFramework(framework());
        assertNotNull(interpreter.call(FixtureDex.HELLO, "viewCtx"));
    }

    @Test
    void clipDemoPassesThrough() {
        interpreter.setFramework(framework());
        assertNotNull(interpreter.call(FixtureDex.HELLO, "clipDemo"));
    }

    @Test
    void attachDemoIgnores() {
        interpreter.setFramework(framework());
        assertNotNull(interpreter.call(FixtureDex.HELLO, "attachDemo"));
    }

    @Test
    void tagDemoRoundTrips() {
        interpreter.setFramework(framework());
        assertEquals("seven", interpreter.call(FixtureDex.HELLO, "tagDemo"));
    }

    @Test
    void tagMissStaysNull() {
        interpreter.setFramework(framework());
        assertNull(interpreter.call(FixtureDex.HELLO, "tagMiss"));
    }

    @Test
    void attachedDemoStaysFalse() {
        interpreter.setFramework(framework());
        assertEquals(Integer.valueOf(0), interpreter.call(FixtureDex.HELLO, "attachedDemo"));
    }

    @Test
    void parentDemoStaysNull() {
        interpreter.setFramework(framework());
        assertNull(interpreter.call(FixtureDex.HELLO, "parentDemo"));
    }

    @Test
    void vtoDemoPlaceholds() {
        interpreter.setFramework(framework());
        assertNotNull(interpreter.call(FixtureDex.HELLO, "vtoDemo"));
    }

    @Test
    void drawDemoIgnores() {
        interpreter.setFramework(framework());
        assertNotNull(interpreter.call(FixtureDex.HELLO, "drawDemo"));
    }

    @Test
    void contentViewDemoInstalls() {
        interpreter.setFramework(framework());
        assertNotNull(interpreter.call(FixtureDex.HELLO, "contentViewDemo"));
    }

    @Test
    void windowDemoPlaceholds() {
        interpreter.setFramework(framework());
        assertNotNull(interpreter.call(FixtureDex.HELLO, "windowDemo"));
    }

    @Test
    void parentLinkDemoLinks() {
        interpreter.setFramework(framework());
        assertNotNull(interpreter.call(FixtureDex.HELLO, "parentLinkDemo"));
    }

    @Test
    void attachLinkDemoAttaches() {
        interpreter.setFramework(framework());
        assertEquals(Integer.valueOf(1),
                interpreter.call(FixtureDex.HELLO, "attachLinkDemo"));
    }

    @Test
    void choreoDemoPlaceholds() {
        interpreter.setFramework(framework());
        assertNotNull(interpreter.call(FixtureDex.HELLO, "choreoDemo"));
    }

    @Test
    void handlerAsyncCreates() {
        interpreter.setFramework(framework());
        assertNotNull(interpreter.call(FixtureDex.HELLO, "handlerAsyncDemo"));
    }

    @Test
    void high16DemoShifts() {
        assertEquals(Integer.valueOf(0x3F800000),
                interpreter.call(FixtureDex.HELLO, "high16Demo"));
    }

    @Test
    void wideHigh16DemoShifts() {
        assertEquals(Long.valueOf(0x3FF0000000000000L),
                interpreter.call(FixtureDex.HELLO, "wideHigh16Demo"));
    }

    @Test
    void fillDemoFills() {
        assertEquals(Integer.valueOf(2), interpreter.call(FixtureDex.HELLO, "fillDemo"));
    }

    @Test
    void fillFloatDemoKeepsBits() {
        assertEquals(Integer.valueOf(0x3F800000),
                interpreter.call(FixtureDex.HELLO, "fillFloatDemo"));
    }

    @Test
    void runtimeDemoPositive() {
        assertTrue((Long) interpreter.call(FixtureDex.HELLO, "runtimeDemo") > 0);
    }

    @Test
    void cpuDemoPositive() {
        assertTrue((Integer) interpreter.call(FixtureDex.HELLO, "cpuDemo") > 0);
    }

    @Test
    void propDemoStaysNull() {
        assertNull(interpreter.call(FixtureDex.HELLO, "propDemo"));
    }

    @Test
    void parentIfDemoStaysNull() {
        interpreter.setFramework(framework());
        assertNull(interpreter.call(FixtureDex.HELLO, "parentIfDemo"));
    }

    @Test
    void idDemoIsNoId() {
        interpreter.setFramework(framework());
        assertEquals(Integer.valueOf(-1), interpreter.call(FixtureDex.HELLO, "idDemo"));
    }

    @Test
    void viewHandlerDemoHandsHandler() {
        interpreter.setFramework(framework());
        assertNotNull(interpreter.call(FixtureDex.HELLO, "viewHandlerDemo"));
    }

    @Test
    void detachedHandlerDemoStaysNull() {
        interpreter.setFramework(framework());
        assertNull(interpreter.call(FixtureDex.HELLO, "detachedHandlerDemo"));
    }

    @Test
    void reflectDemoInvokes() {
        assertNotNull(interpreter.call(FixtureDex.HELLO, "reflectDemo"));
    }

    @Test
    void reflectMissDemoThrows() {
        VmThrow thrown = assertThrows(VmThrow.class,
                () -> interpreter.call(FixtureDex.HELLO, "reflectMissDemo"));
        assertTrue(thrown.value instanceof NoSuchMethodException, String.valueOf(thrown.value));
    }

    @Test
    void longUpdaterAdds() {
        assertEquals(Long.valueOf(10L), interpreter.call(FixtureDex.HELLO, "longUpdaterDemo"));
    }

    @Test
    void intUpdaterKeeps() {
        assertEquals(Integer.valueOf(5), interpreter.call(FixtureDex.HELLO, "intUpdaterDemo"));
    }

    @Test
    void cloneDemoCopies() {
        assertEquals("a", interpreter.call(FixtureDex.HELLO, "cloneDemo"));
    }

    @Test
    void ordinalDemoReadsOne() {
        assertEquals(Integer.valueOf(1), interpreter.call(FixtureDex.HELLO, "ordinalDemo"));
    }

    @Test
    void switchDemoTakesCase() {
        assertEquals(Integer.valueOf(7), interpreter.call(FixtureDex.HELLO, "switchDemo"));
    }

    @Test
    void switchDefaultDemoFallsThrough() {
        assertEquals(Integer.valueOf(99),
                interpreter.call(FixtureDex.HELLO, "switchDefaultDemo"));
    }

    @Test
    void copyDemoKeepsType() {
        assertEquals("a", interpreter.call(FixtureDex.HELLO, "copyDemo"));
    }

    @Test
    void loaderDemoPlaceholds() {
        assertNotNull(interpreter.call(FixtureDex.HELLO, "loaderDemo"));
    }

    @Test
    void serviceDemoEmpty() {
        assertEquals(Integer.valueOf(0), interpreter.call(FixtureDex.HELLO, "serviceDemo"));
    }

    @Test
    void sdkIntDemoReports() {
        interpreter.setFramework(framework());
        assertEquals(Integer.valueOf(36), interpreter.call(FixtureDex.HELLO, "sdkIntDemo"));
    }

    @Test
    void suppressedDemoRecords() {
        Object result = interpreter.call(FixtureDex.HELLO, "suppressedDemo");
        assertTrue(result instanceof VmArray);
        VmArray array = (VmArray) result;
        assertEquals(1, array.length());
        assertTrue(array.get(0) instanceof VmObject);
        assertEquals("Ltest/Boom;",
                ((VmObject) array.get(0)).type.type);
    }

    @Test
    void zeroCheckHeap() {
        assertEquals(Integer.valueOf(7), interpreter.call(FixtureDex.HELLO, "zeroCheckDemo"));
    }

    @Test
    void nullCheckBits() {
        assertEquals(Integer.valueOf(7), interpreter.call(FixtureDex.HELLO, "nullCheckDemo"));
    }

    @Test
    void frameIdDemoMatches() {
        interpreter.setFramework(framework());
        assertEquals(Integer.valueOf(0x1020002), interpreter.call(FixtureDex.HELLO, "frameIdDemo"));
    }

    @Test
    void frameCbDemoIgnores() {
        interpreter.setFramework(framework());
        assertNotNull(interpreter.call(FixtureDex.HELLO, "frameCbDemo"));
    }

    @Test
    void postCbDemoRecords() {
        interpreter.setFramework(framework());
        assertNotNull(interpreter.call(FixtureDex.HELLO, "postCbDemo"));
    }

    @Test
    void plusCollAdds() {
        assertEquals(Integer.valueOf(2), interpreter.call(FixtureDex.HELLO, "plusCollDemo"));
    }

    @Test
    void childAddKeeps() {
        interpreter.setFramework(framework());
        assertNotNull(interpreter.call(FixtureDex.HELLO, "childAddDemo"));
    }

    @Test
    void childRemoveClears() {
        interpreter.setFramework(framework());
        assertEquals(Integer.valueOf(0), interpreter.call(FixtureDex.HELLO, "childRemoveDemo"));
    }

    @Test
    void ctxDemoKeepsContext() {
        interpreter.setFramework(framework());
        assertNotNull(interpreter.call(FixtureDex.HELLO, "ctxDemo"));
    }

    @Test
    void resDemoPlaceholds() {
        interpreter.setFramework(framework());
        assertNotNull(interpreter.call(FixtureDex.HELLO, "resDemo"));
    }

    @Test
    void metricsDemoReadsDensity() {
        interpreter.setFramework(framework());
        assertEquals(Float.valueOf(1.0f), interpreter.call(FixtureDex.HELLO, "metricsDemo"));
    }

    @Test
    void configDemoReadsFontScale() {
        interpreter.setFramework(framework());
        assertEquals(Float.valueOf(1.0f), interpreter.call(FixtureDex.HELLO, "configDemo"));
    }

    @Test
    void hostFillDemoFills() {
        assertEquals(Integer.valueOf(42), interpreter.call(FixtureDex.HELLO, "hostFillDemo"));
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
        index.put(new FwClass("Landroid/view/ViewGroup$LayoutParams;", "Ljava/lang/Object;",
                Collections.<String>emptyList(),
                Collections.<FwIndex.FwMethod>emptyList(),
                Collections.<FwIndex.FwField>emptyList(),
                Collections.<String, Object>emptyMap()));
        index.put(new FwClass("Landroid/view/ViewGroup;", "Landroid/view/View;",
                Collections.<String>emptyList(),
                Collections.<FwIndex.FwMethod>emptyList(),
                Collections.<FwIndex.FwField>emptyList(),
                Collections.<String, Object>emptyMap()));
        index.put(new FwClass("Landroid/os/Build$VERSION;", "Ljava/lang/Object;",
                Collections.<String>emptyList(),
                Collections.<FwIndex.FwMethod>emptyList(),
                Collections.singletonList(new FwField("SDK_INT", "I")),
                Collections.<String, Object>emptyMap()));
        index.put(new FwClass("Landroid/graphics/Paint;", "Ljava/lang/Object;",
                Collections.<String>emptyList(),
                Collections.<FwIndex.FwMethod>emptyList(),
                Collections.<FwIndex.FwField>emptyList(),
                Collections.<String, Object>emptyMap()));
        index.put(new FwClass("Landroid/graphics/Paint$Style;", "Ljava/lang/Enum;",
                Collections.<String>emptyList(),
                Collections.<FwIndex.FwMethod>emptyList(),
                Arrays.asList(
                        new FwField("FILL", "Landroid/graphics/Paint$Style;"),
                        new FwField("STROKE", "Landroid/graphics/Paint$Style;"),
                        new FwField("FILL_AND_STROKE", "Landroid/graphics/Paint$Style;")),
                Collections.<String, Object>emptyMap()));
        index.put(new FwClass("Ljava/lang/ref/Reference;", "Ljava/lang/Object;",
                Collections.<String>emptyList(),
                Collections.<FwIndex.FwMethod>emptyList(),
                Collections.<FwIndex.FwField>emptyList(),
                Collections.<String, Object>emptyMap()));
        index.put(new FwClass("Ljava/lang/ref/WeakReference;", "Ljava/lang/ref/Reference;",
                Collections.<String>emptyList(),
                Collections.<FwIndex.FwMethod>emptyList(),
                Collections.<FwIndex.FwField>emptyList(),
                Collections.<String, Object>emptyMap()));
        index.put(new FwClass("Ljava/lang/Enum;", "Ljava/lang/Object;",
                Collections.<String>emptyList(),
                Collections.<FwIndex.FwMethod>emptyList(),
                Collections.<FwIndex.FwField>emptyList(),
                Collections.<String, Object>emptyMap()));
        return index;
    }
}
