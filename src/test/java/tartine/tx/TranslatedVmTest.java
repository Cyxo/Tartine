package tartine.tx;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import android.graphics.Paint;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;

import tartine.dex.Dex;
import tartine.dex.DexSet;
import tartine.dex.FixtureDex;
import tartine.gfx.GfxBackend;
import tartine.gfx.skija.SkijaBackend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Translator suite: the shared fixture dex runs as translated JVM bytecode
 * (classfile v49) through {@link TranslatedLoader}, invoking the launcher
 * class's static demos reflectively. This replaces the former interpreter
 * {@code VmTest} (see {@code git log}): expectations are real-JVM results,
 * not interpreter builtins.
 *
 * <p>Demos that only exercised interpreter seams are not repeated here:
 * the framework index ({@code fw.*} placeholders), {@code VmObject} field
 * mirrors, the VFS File bridge, role-faked dispatch (a class declared as
 * Object yet interface-called as Map/Comparator), protected-member access
 * from an unrelated synthetic class, AndroidX/Kotlin classes the fixture
 * does not carry, and reflection over the interpreter's single universe.
 * Real APK code compiles its supertypes and bundled libraries, so the
 * translator sees those the same way the device does.
 */
final class TranslatedVmTest {
    @TempDir
    Path temp;

    private TranslatedLoader loader;

    @BeforeAll
    static void installBackend() {
        // Text measurement (StaticLayout/desired width) resolves the seam.
        GfxBackend.install(new SkijaBackend());
    }

    @AfterAll
    static void uninstallBackend() {
        GfxBackend.uninstall();
    }

    @BeforeEach
    void loadFixture() throws Exception {
        Path dexFile = temp.resolve("fixture.dex");
        FixtureDex.writeHello(dexFile);
        DexSet dex = DexSet.of(Dex.load(dexFile.toFile()));
        loader = new TranslatedLoader(getClass().getClassLoader(), dex);
    }

    // ------------------------------------------------------------------
    // Harness
    // ------------------------------------------------------------------

    private Object call(String name) throws Exception {
        return call(name, new Object[0]);
    }

    private Object call(String name, Object... input) throws Exception {
        Class<?> hello = loader.loadClass("test.Hello");
        Class<?>[] sig = new Class<?>[input.length];
        for (int i = 0; i < input.length; i++) {
            sig[i] = prim(input[i].getClass());
        }
        Method m = hello.getDeclaredMethod(name, sig);
        m.setAccessible(true);
        try {
            return m.invoke(null, input);
        } catch (InvocationTargetException x) {
            // The guest's own exception surfaces; reflection wraps it.
            Throwable c = x.getCause();
            if (c instanceof Exception) {
                throw (Exception) c;
            }
            if (c instanceof Error) {
                throw (Error) c;
            }
            throw new RuntimeException(c);
        }
    }

    /** Reflection on translated methods wants primitive descriptors. */
    private static Class<?> prim(Class<?> boxed) {
        if (boxed == Integer.class) return int.class;
        if (boxed == Long.class) return long.class;
        if (boxed == Float.class) return float.class;
        if (boxed == Double.class) return double.class;
        if (boxed == Byte.class) return byte.class;
        if (boxed == Short.class) return short.class;
        if (boxed == Boolean.class) return boolean.class;
        if (boxed == Character.class) return char.class;
        return boxed;
    }

    private static int length(Object array) {
        return Array.getLength(array);
    }

    private static Object element(Object array, int index) {
        return Array.get(array, index);
    }

    // ------------------------------------------------------------------
    // Arithmetic, control flow, stack shapes
    // ------------------------------------------------------------------

    @Test
    void arithmeticAndControlFlow() throws Exception {
        assertEquals(5, (int) (Integer) call("make"));
        assertEquals(24, (int) (Integer) call("sum"));
        assertEquals(5, (int) (Integer) call("len"));
        assertEquals(9, (int) (Integer) call("nops"));
        assertEquals(-1, (int) (Integer) call("cmpTiny"));
        assertEquals(42, (int) (Integer) call("boot"));
        assertEquals(100L, (long) (Long) call("longArith"));
        assertEquals(5.5f, (float) (Float) call("floatArith"));
        assertEquals(3.5, (double) (Double) call("doubleArith"));
        assertEquals(0x3F800000, (int) (Integer) call("high16Demo"));
        assertEquals(0x3FF0000000000000L, (long) (Long) call("wideHigh16Demo"));
        assertEquals(2, (int) (Integer) call("fillDemo"));
        assertEquals(0x3F800000, (int) (Integer) call("fillFloatDemo"));
        assertEquals(7, (int) (Integer) call("zeroCheckDemo"));
        assertEquals(7, (int) (Integer) call("nullCheckDemo"));
        assertEquals(7, (int) (Integer) call("switchDemo"));
        assertEquals(99, (int) (Integer) call("switchDefaultDemo"));
        assertEquals(8, (int) (Integer) call("arrayCopy"));
        assertEquals(7, (int) (Integer) call("mathMax"));
        assertEquals(7, (int) (Integer) call("stringOps"));
        assertEquals(42, (int) (Integer) call("hostFillDemo"));
    }

    @Test
    void invokesAndArguments() throws Exception {
        assertEquals(42, (int) (Integer) call("twice", 21));
        assertEquals(7, (int) (Integer) call("id", 7));
        assertEquals("hi", (String) call("greet"));
        assertEquals("a1", (String) call("sb"));
        assertEquals("desk", (String) call("throwCatch"));
        assertEquals(42, (int) (Integer) call("intBox"));
        assertEquals("true", (String) call("boolStr"));
        assertEquals("A", (String) call("charStr"));
        assertEquals("ababab", (String) call("repeatDemo"));
        assertEquals(1, (int) (Integer) call("emptyDemo"));
        assertEquals(4.0, (double) (Double) call("strictSqrt"));
        assertEquals("a", (String) call("cloneDemo"));
        assertEquals("a", (String) call("copyDemo"));
    }

    @Test
    void arraysAndClasses() throws Exception {
        assertEquals(7, (int) (Integer) call("safely"));
        assertNull(call("nullCast"));
        assertEquals(1, (int) (Integer) call("castOk"));
        assertEquals(1, (int) (Integer) call("clazzIsInstance"));
        assertEquals("test.Counter", (String) call("constClass"));
        assertEquals("test.Counter", (String) call("getClassDemo"));
        assertEquals("java.lang.String", (String) call("getClassHostDemo"));
        assertEquals(1, (int) (Integer) call("covarOk"));
        assertEquals(1, (int) (Integer) call("appendableCast"));
        assertEquals(3, (int) (Integer) call("enumValuesDemo"));
        assertEquals(1, (int) (Integer) call("ordinalDemo"));
        assertEquals(0, (int) (Integer) call("annoDemo"));
    }

    @Test
    void guestExceptionsSurface() throws Exception {
        assertThrows(ClassCastException.class, () -> call("castFail"));
        assertThrows(ClassCastException.class, () -> call("covarPrim"));
        assertThrows(ClassCastException.class, () -> call("badCast"));
        assertThrows(NullPointerException.class, () -> call("nullRecv"));
    }

    @Test
    void collectionsAndUpdaters() throws Exception {
        assertEquals(42, (int) (Integer) call("mapDemo"));
        assertEquals(42, (int) (Integer) call("linkedMapDemo"));
        assertEquals(8, (int) (Integer) call("listDemo"));
        assertEquals(1, (int) (Integer) call("setDemo"));
        assertEquals(5, (int) (Integer) call("iterDemo"));
        assertEquals("k", (String) call("entryDemo"));
        assertFalse((boolean) (Boolean) call("iterableDemo"));
        assertEquals(5, (int) (Integer) call("intUpdaterDemo"));
        // casDemo returns Z; reflection faithfully yields Boolean.
        assertTrue((boolean) (Boolean) call("casDemo"));
    }

    @Test
    void clocksThreadsAndStatics() throws Exception {
        assertTrue(call("threadDemo") instanceof String);
        assertEquals(60_000_000_000L, (long) (Long) call("nanosDemo"));
        assertTrue((long) (Long) call("sysClock") >= 0L);
        assertTrue((int) (Integer) call("stackDemo") >= 2);
        assertTrue((long) (Long) call("runtimeDemo") > 0L);
        assertTrue((int) (Integer) call("cpuDemo") > 0);
        assertNull(call("propDemo"));
    }

    @Test
    void weakReferencesAndTypefaces() throws Exception {
        assertEquals("x", (String) call("weakGet"));
        assertNull(call("weakClear"));

        Object first = call("typefaceDemo");
        Object second = call("typefaceDemo");
        assertNotNull(first);
        assertSame(first, second);

        Object ltr = call("heuristicDemo");
        assertSame(ltr, call("heuristicDemo"));
        assertSame(TextDirectionHeuristics.LTR, ltr);
        assertNull(call("boringDemo"));
    }

    // ------------------------------------------------------------------
    // Framework behavior through real framework classes
    // ------------------------------------------------------------------

    @Test
    void frameworkEnumsAndColors() throws Exception {
        assertEquals(0, (int) (Integer) call("vis"));
        assertEquals("RED", (String) call("colorName"));
        assertEquals(-1, (int) (Integer) call("colorCmp"));
        assertEquals(0x0102, (int) (Integer) call("colorDemo"));
        assertEquals(0xFF010203, (int) (Integer) call("argbDemo"));
        assertEquals(0xFF010203, (int) (Integer) call("parseDemo"));
        // Paint.Style ordinals follow AOSP: FILL=0, STROKE=1.
        assertEquals(1, (int) (Integer) call("styleDemo"));
        assertEquals(1, (int) (Integer) call("styleRoundDemo"));
        assertEquals(7, (int) (Integer) call("paintDemo"));
        assertEquals(2.0f, (float) (Float) call("strokeDemo"));
        int flags = (int) (Integer) call("textPaintDemo");
        assertTrue((flags & Paint.ANTI_ALIAS_FLAG) != 0);
    }

    @Test
    void paintTextStateRoundTrips() throws Exception {
        Object result = call("paintTextDemo");
        assertTrue(result instanceof Paint);
        Paint paint = (Paint) result;
        assertEquals(Paint.HINTING_ON, paint.getHinting());
        assertEquals(7.5f, paint.getTextSize(), 0.0f);
        assertNull(paint.getTypeface());
        assertNull(paint.getShader());
        assertEquals(2.0f, paint.getTextScaleX(), 0.0f);
    }

    @Test
    void sdkLevelIsApi36() throws Exception {
        assertEquals(36, (int) (Integer) call("sdk"));
        assertEquals(36, (int) (Integer) call("sdkIntDemo"));
    }

    @Test
    void activityAndWindowBasics() throws Exception {
        assertNotNull(call("activityWindow"));
        assertNotNull(call("windowDemo"));
        assertNotNull(call("choreoDemo"));
        assertNotNull(call("vtoDemo"));
        assertFalse((boolean) (Boolean) call("windowFocusDemo"));
        assertFalse((boolean) (Boolean) call("hwDemo"));
        assertFalse((boolean) (Boolean) call("touchModeDemo"));
        assertNull(call("captureDemo"));
    }

    @Test
    void viewBasics() throws Exception {
        assertEquals(8, (int) (Integer) call("viewVis"));
        assertEquals(30, (int) (Integer) call("viewLp"));
        assertEquals(-1, (int) (Integer) call("lpDemo"));
        assertNotNull(call("viewConfigDemo"));
        assertNotNull(call("viewCtx"));
        assertFalse((boolean) (Boolean) call("attachedDemo"));
        assertFalse((boolean) (Boolean) call("attachLinkDemo"));
        assertNull(call("parentDemo"));
        assertEquals(0, (int) (Integer) call("childRemoveDemo"));
        assertEquals(-1, (int) (Integer) call("idDemo"));
        assertEquals(0, (int) (Integer) call("layoutDirDemo"));
        assertNull(call("missFind"));
        assertNull(call("tagMiss"));
    }

    @Test
    void viewCallbacksReturnObjects() throws Exception {
        for (String demo : new String[] {
                "attachDemo", "childAddDemo", "drawDemo", "focusableDemo",
                "willNotDrawDemo", "forceDarkDemo", "translationDemo",
                "dragDemo", "importantDemo", "a11yDelegateDemo",
                "parentLinkDemo", "vtoLayoutDemo", "vtoScrollDemo",
                "configCopyDemo", "themeDemo"}) {
            assertNotNull(call(demo), demo);
        }
    }

    @Test
    void servicesAndContext() throws Exception {
        assertNotNull(call("appCtxDemo"));
        assertNotNull(call("ctxDemo"));
        assertNotNull(call("resolverDemo"));
        assertNotNull(call("resolverObsDemo"));
        assertNotNull(call("contentObserverDemo"));
        assertNotNull(call("clipboardDemo"));
        assertNotNull(call("autofillDemo"));
        assertNotNull(call("autofillCbDemo"));
        assertNotNull(call("sysServiceDemo"));
        assertNotNull(call("compCbDemo"));
        assertNotNull(call("resDemo"));
        assertTrue((boolean) (Boolean) call("a11yDemo"));
        assertFalse((boolean) (Boolean) call("a11yEnabledDemo"));
        assertEquals(0, (int) (Integer) call("a11yImportantDemo"));
        assertNull(call("viewHandlerDemo"));
        assertNull(call("detachedHandlerDemo"));
    }

    @Test
    void settingsBundlesAndMetrics() throws Exception {
        assertEquals(7.5f, (float) (Float) call("settingsFloatDemo"));
        assertNotNull(call("settingsUriDemo"));
        assertEquals("v", (String) call("bundleDemo"));
        assertEquals(711, (int) (Integer) call("bundleIntDemo"));
        assertEquals(12, (int) (Integer) call("localeDemo"));
        assertEquals(1.0f, (float) (Float) call("metricsDemo"));
        assertEquals(1.0f, (float) (Float) call("configDemo"));
        assertEquals(0x400001E0, (int) (Integer) call("specDemo"));
    }

    @Test
    void textLayoutRunsOnTheBackend() throws Exception {
        assertNotNull(call("buildLayoutDemo"));
        assertNotNull(call("obtainDemo"));
        assertTrue((float) (Float) call("desiredSpanDemo") > 0.0f);
        assertTrue((float) (Float) call("desiredDemo") > 0.0f);

        Object layout = call("layoutGetDemo");
        assertEquals(18, length(layout));
        assertEquals("hello world test", element(layout, 13));
        assertTrue(element(layout, 14) instanceof TextPaint);
        assertEquals(8, (int) (Integer) element(layout, 15));
    }

    @Test
    void reflection() throws Exception {
        assertEquals("base", (String) call("reflectPubDemo"));
        assertThrows(NoSuchMethodException.class, () -> call("reflectMissDemo"));
    }

    @Test
    void serviceLookupFindsNoProviders() throws Exception {
        assertFalse((boolean) (Boolean) call("serviceDemo"));
        assertFalse((boolean) (Boolean) call("traceDemo"));
    }

    // Keep the field helper honest: some framework state is package-private.
    @Test
    void translatedClassesHaveTheGuestName() throws Exception {
        Class<?> hello = loader.loadClass("test.Hello");
        assertEquals("test.Hello", hello.getName());
        assertEquals(loader, hello.getClassLoader());
    }
}