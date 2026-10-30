package tartine.dex;

import com.android.tools.smali.dexlib2.AccessFlags;
import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.Opcodes;
import com.android.tools.smali.dexlib2.builder.Label;
import com.android.tools.smali.dexlib2.builder.MutableMethodImplementation;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderArrayPayload;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction10x;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction11n;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction11x;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction12x;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction21c;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction21ih;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction21lh;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction21s;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction21t;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction22b;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction22c;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction23x;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction31i;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction31t;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction35c;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction3rc;
import com.android.tools.smali.dexlib2.builder.instruction.BuilderPackedSwitchPayload;
import com.android.tools.smali.dexlib2.immutable.ImmutableMethodParameter;
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableFieldReference;
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableMethodReference;
import com.android.tools.smali.dexlib2.writer.builder.BuilderField;
import com.android.tools.smali.dexlib2.writer.builder.BuilderFieldReference;
import com.android.tools.smali.dexlib2.writer.builder.BuilderMethod;
import com.android.tools.smali.dexlib2.writer.builder.BuilderMethodReference;
import com.android.tools.smali.dexlib2.writer.builder.DexBuilder;
import com.android.tools.smali.dexlib2.writer.io.FileDataStore;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Shared dexlib2-written fixture: {@code Ltest/Hello;} exercising calls,
 * fields, statics, arrays, exceptions and comparisons, plus tiny
 * {@code Counter}/{@code Base}/{@code Sub} helpers. Written to real bytes
 * so every consumer round-trips the file format.
 */
public final class FixtureDex {
    public static final String HELLO = "Ltest/Hello;";
    public static final String COUNTER = "Ltest/Counter;";
    public static final String BASE = "Ltest/Base;";
    public static final String SUB = "Ltest/Sub;";
    public static final String SUB2 = "Ltest/Sub2;";
    public static final String VG = "Ltest/Vg;";
    public static final String ORPHAN = "Ltest/Orphan;";
    public static final String FW_BASE = "Lfw/Base;";
    public static final String FW_SUB = "Lfw/Sub;";
    public static final String FW_GHOST = "Lfw/Ghost;";

    private FixtureDex() {
    }

    public static void writeHello(Path dexFile) throws Exception {
        Opcodes opcodes = Opcodes.getDefault();
        DexBuilder builder = new DexBuilder(opcodes);
        BuilderMethodReference objectInit = builder.internMethodReference(
                new ImmutableMethodReference("Ljava/lang/Object;", "<init>",
                        Collections.<String>emptyList(), "V"));

        BuilderMethod counterInit = trivialInit(builder, objectInit, COUNTER, "Ljava/lang/Object;");
        BuilderMethod baseInit = trivialInit(builder, objectInit, BASE, "Ljava/lang/Object;");
        MutableMethodImplementation baseWhoImpl = new MutableMethodImplementation(1);
        baseWhoImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 0,
                builder.internStringReference("base")));
        baseWhoImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod baseWho = builder.internMethod(BASE, "who",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/String;",
                AccessFlags.PUBLIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), baseWhoImpl);
        BuilderMethodReference baseInitRef = builder.internMethodReference(
                new ImmutableMethodReference(BASE, "<init>",
                        Collections.<String>emptyList(), "V"));
        MutableMethodImplementation subInitImpl = new MutableMethodImplementation(1);
        subInitImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0, 0,
                baseInitRef));
        subInitImpl.addInstruction(new BuilderInstruction10x(Opcode.RETURN_VOID));
        BuilderMethod subInit = builder.internMethod(SUB, "<init>",
                Collections.<ImmutableMethodParameter>emptyList(), "V",
                AccessFlags.PUBLIC.getValue() | AccessFlags.CONSTRUCTOR.getValue(),
                Collections.emptySet(), Collections.emptySet(), subInitImpl);
        builder.internClassDef(COUNTER, AccessFlags.PUBLIC.getValue(), "Ljava/lang/Object;",
                Collections.<String>emptyList(), "Counter.java",
                Collections.emptySet(),
                Collections.<BuilderField>emptyList(),
                Collections.singletonList(counterInit));
        builder.internClassDef(BASE, AccessFlags.PUBLIC.getValue(), "Ljava/lang/Object;",
                Collections.<String>emptyList(), "Base.java",
                Collections.emptySet(),
                Collections.<BuilderField>emptyList(),
                Arrays.asList(baseInit, baseWho));
        builder.internClassDef(SUB, AccessFlags.PUBLIC.getValue(), BASE,
                Collections.<String>emptyList(), "Sub.java",
                Collections.emptySet(),
                Collections.<BuilderField>emptyList(),
                Collections.singletonList(subInit));

        // Sub2/Orphan extend framework types (exist only in a framework index).
        // Their constructors call Object.<init> directly: invalid Dalvik, but
        // our interpreter does not verify constructor chaining.
        MutableMethodImplementation sub2InitImpl = new MutableMethodImplementation(1);
        sub2InitImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, objectInit));
        sub2InitImpl.addInstruction(new BuilderInstruction10x(Opcode.RETURN_VOID));
        BuilderMethod sub2Init = builder.internMethod(SUB2, "<init>",
                Collections.<ImmutableMethodParameter>emptyList(), "V",
                AccessFlags.PUBLIC.getValue() | AccessFlags.CONSTRUCTOR.getValue(),
                Collections.emptySet(), Collections.emptySet(), sub2InitImpl);
        builder.internClassDef(SUB2, AccessFlags.PUBLIC.getValue(), FW_BASE,
                Collections.<String>emptyList(), "Sub2.java",
                Collections.emptySet(),
                Collections.<BuilderField>emptyList(),
                Collections.singletonList(sub2Init));
        MutableMethodImplementation orphanInitImpl = new MutableMethodImplementation(1);
        orphanInitImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, objectInit));
        orphanInitImpl.addInstruction(new BuilderInstruction10x(Opcode.RETURN_VOID));
        BuilderMethod orphanInit = builder.internMethod(ORPHAN, "<init>",
                Collections.<ImmutableMethodParameter>emptyList(), "V",
                AccessFlags.PUBLIC.getValue() | AccessFlags.CONSTRUCTOR.getValue(),
                Collections.emptySet(), Collections.emptySet(), orphanInitImpl);
        builder.internClassDef(ORPHAN, AccessFlags.PUBLIC.getValue(), FW_GHOST,
                Collections.<String>emptyList(), "Orphan.java",
                Collections.emptySet(),
                Collections.<BuilderField>emptyList(),
                Collections.singletonList(orphanInit));

        // Vg: a dex-defined ViewGroup subclass for mirror-bridging probes.
        // The init skips the framework super chain (sub2Init convention).
        MutableMethodImplementation vgInitImpl = new MutableMethodImplementation(1);
        vgInitImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, objectInit));
        vgInitImpl.addInstruction(new BuilderInstruction10x(Opcode.RETURN_VOID));
        BuilderMethod vgInit = builder.internMethod(VG, "<init>",
                Collections.<ImmutableMethodParameter>emptyList(), "V",
                AccessFlags.PUBLIC.getValue() | AccessFlags.CONSTRUCTOR.getValue(),
                Collections.emptySet(), Collections.emptySet(), vgInitImpl);
        builder.internClassDef(VG, AccessFlags.PUBLIC.getValue(),
                "Landroid/view/ViewGroup;",
                Collections.<String>emptyList(), "Vg.java",
                Collections.emptySet(),
                Collections.<BuilderField>emptyList(),
                Collections.singletonList(vgInit));

        // Boom: a dex-defined throwable (stays a model object, unlike
        // java.lang throwables which allocate as host exceptions).
        BuilderMethod boomInit = trivialInit(builder, objectInit, "Ltest/Boom;",
                "Ljava/lang/Exception;");
        builder.internClassDef("Ltest/Boom;", AccessFlags.PUBLIC.getValue(),
                "Ljava/lang/Exception;",
                Collections.<String>emptyList(), "Boom.java",
                Collections.emptySet(),
                Collections.<BuilderField>emptyList(),
                Collections.singletonList(boomInit));

        // Cmp: a dex-defined Comparator comparing Integers by subtraction.
        BuilderMethod cmpInit = trivialInit(builder, objectInit, "Ltest/Cmp;",
                "Ljava/lang/Object;");
        MutableMethodImplementation cmpCompareImpl = new MutableMethodImplementation(5);
        cmpCompareImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 3, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Integer;", "intValue",
                        Collections.<String>emptyList(), "I"))));
        cmpCompareImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        cmpCompareImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 4, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Integer;", "intValue",
                        Collections.<String>emptyList(), "I"))));
        cmpCompareImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 1));
        cmpCompareImpl.addInstruction(new BuilderInstruction23x(Opcode.SUB_INT, 0, 0, 1));
        cmpCompareImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod cmpCompare = builder.internMethod("Ltest/Cmp;", "compare",
                Arrays.asList(
                        new ImmutableMethodParameter("Ljava/lang/Object;",
                                Collections.emptySet(), "a"),
                        new ImmutableMethodParameter("Ljava/lang/Object;",
                                Collections.emptySet(), "b")),
                "I",
                AccessFlags.PUBLIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), cmpCompareImpl);
        builder.internClassDef("Ltest/Cmp;", AccessFlags.PUBLIC.getValue(),
                "Ljava/lang/Object;",
                Collections.<String>emptyList(), "Cmp.java",
                Collections.emptySet(),
                Collections.<BuilderField>emptyList(),
                Arrays.asList(cmpInit, cmpCompare));

        // SubSet: a dex subclass of host TreeSet with no instance state,
        // mirroring Compose's layout-node TreeSet (delegating constructor
        // plus a size wrapper over super).
        MutableMethodImplementation subSetInitImpl = new MutableMethodImplementation(2);
        subSetInitImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/TreeSet;", "<init>",
                        Collections.singletonList("Ljava/util/Comparator;"), "V"))));
        subSetInitImpl.addInstruction(new BuilderInstruction10x(Opcode.RETURN_VOID));
        BuilderMethod subSetInit = builder.internMethod("Ltest/SubSet;", "<init>",
                Collections.singletonList(new ImmutableMethodParameter(
                        "Ljava/util/Comparator;", Collections.emptySet(), "comparator")),
                "V",
                AccessFlags.PUBLIC.getValue() | AccessFlags.CONSTRUCTOR.getValue(),
                Collections.emptySet(), Collections.emptySet(), subSetInitImpl);
        MutableMethodImplementation subSetGetSizeImpl = new MutableMethodImplementation(1);
        subSetGetSizeImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_SUPER, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/TreeSet;", "size",
                        Collections.<String>emptyList(), "I"))));
        subSetGetSizeImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        subSetGetSizeImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod subSetGetSize = builder.internMethod("Ltest/SubSet;", "getSize",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), subSetGetSizeImpl);
        MutableMethodImplementation subSetSizeImpl = new MutableMethodImplementation(1);
        subSetSizeImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ltest/SubSet;", "getSize",
                        Collections.<String>emptyList(), "I"))));
        subSetSizeImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        subSetSizeImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod subSetSize = builder.internMethod("Ltest/SubSet;", "size",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), subSetSizeImpl);
        builder.internClassDef("Ltest/SubSet;", AccessFlags.PUBLIC.getValue(),
                "Ljava/util/TreeSet;",
                Collections.<String>emptyList(), "SubSet.java",
                Collections.emptySet(),
                Collections.<BuilderField>emptyList(),
                Arrays.asList(subSetInit, subSetGetSize, subSetSize));

        Map<String, BuilderMethod> demos = new HashMap<>();
        demos.putAll(writeCoreDemos(builder, objectInit));
        demos.putAll(writeRuntimeDemos(builder, objectInit));
        demos.putAll(writeViewDemos(builder, objectInit));
        demos.putAll(writeTextDemos(builder, objectInit));

        builder.internClassDef(HELLO,
                AccessFlags.PUBLIC.getValue(), "Ljava/lang/Object;",
                Collections.<String>emptyList(), "Hello.java",
                Collections.emptySet(),
                Collections.<BuilderField>emptyList(),
                Arrays.asList(demos.get("init"), demos.get("id"), demos.get("twice"), demos.get("greet"), demos.get("make"), demos.get("sum"), demos.get("len"), demos.get("safely"), demos.get("clinit"), demos.get("boot"),
                        demos.get("nullCast"), demos.get("castOk"), demos.get("castFail"), demos.get("cmpTiny"), demos.get("sb"), demos.get("throwCatch"), demos.get("intBox"), demos.get("arrayCopy"),
                        demos.get("appendableCast"), demos.get("badCast"), demos.get("stringOps"), demos.get("mathMax"), demos.get("missing"), demos.get("vis"), demos.get("sdk"), demos.get("fwCast"),
                        demos.get("fwGhost"), demos.get("fwInit"), demos.get("activityStub"), demos.get("nops"), demos.get("covarOk"), demos.get("covarPrim"), demos.get("constClass"),
                        demos.get("clazzIsInstance"), demos.get("updater"), demos.get("sysClock"), demos.get("longArith"), demos.get("floatArith"), demos.get("doubleArith"),
                        demos.get("lazyDemo"), demos.get("lazyIsInstance"), demos.get("executorDemo"), demos.get("postDemo"), demos.get("postSubDemo"), demos.get("casDemo"),
                        demos.get("handlerDemo"),
                        demos.get("threadDemo"), demos.get("mapDemo"), demos.get("listDemo"), demos.get("setDemo"), demos.get("iterDemo"), demos.get("nullish"), demos.get("nullRecv"),
                        demos.get("boolStr"), demos.get("charStr"), demos.get("weakGet"), demos.get("weakClear"), demos.get("colorCmp"), demos.get("colorName"),
                        demos.get("colorValueOf"), demos.get("mapDefaults"), demos.get("mapPutAbsent"), demos.get("repeatDemo"), demos.get("emptyDemo"),
                        demos.get("strictSqrt"), demos.get("linkedMapDemo"), demos.get("blockedExec"), demos.get("txChain"), demos.get("argbDemo"),
                        demos.get("parseDemo"), demos.get("activityWindow"), demos.get("viewVis"), demos.get("lpDemo"), demos.get("viewLp"), demos.get("vgAdd"), demos.get("paintDemo"), demos.get("textPaintDemo"),
                        demos.get("paintTextDemo"), demos.get("paintFullDemo"), demos.get("typefaceDemo"), demos.get("heuristicDemo"), demos.get("boringDemo"),
                        demos.get("desiredDemo"), demos.get("desiredSpanDemo"), demos.get("spannableDemo"), demos.get("enumValuesDemo"), demos.get("obtainDemo"),
                        demos.get("directionDemo"), demos.get("builderOptsDemo"), demos.get("lineBreakDemo"), demos.get("buildLayoutDemo"),
                        demos.get("layoutGetDemo"),
                        demos.get("colorDemo"), demos.get("strokeDemo"), demos.get("styleDemo"), demos.get("styleRoundDemo"), demos.get("nanosDemo"), demos.get("contentGroup"),
                        demos.get("missFind"), demos.get("contentChild"), demos.get("viewCtx"), demos.get("clipDemo"), demos.get("attachDemo"),
                        demos.get("tagDemo"), demos.get("tagMiss"), demos.get("attachedDemo"), demos.get("parentDemo"), demos.get("vtoDemo"),
                        demos.get("drawDemo"), demos.get("vtoLayoutDemo"), demos.get("vtoScrollDemo"), demos.get("vtoClusterDemo"),
                        demos.get("contentViewDemo"), demos.get("choreoDemo"), demos.get("handlerAsyncDemo"),
                        demos.get("high16Demo"), demos.get("wideHigh16Demo"), demos.get("fillDemo"), demos.get("runtimeDemo"),
                        demos.get("forNameDenied"),
                        demos.get("cpuDemo"), demos.get("propDemo"), demos.get("windowDemo"), demos.get("parentLinkDemo"),
                        demos.get("attachLinkDemo"), demos.get("parentIfDemo"), demos.get("idDemo"), demos.get("viewHandlerDemo"),
                        demos.get("detachedHandlerDemo"), demos.get("reflectDemo"), demos.get("reflectMissDemo"), demos.get("reflectPubDemo"),
                        demos.get("annoDemo"),
                        demos.get("longUpdaterDemo"), demos.get("intUpdaterDemo"), demos.get("cloneDemo"), demos.get("ordinalDemo"),
                        demos.get("switchDemo"), demos.get("switchDefaultDemo"), demos.get("copyDemo"), demos.get("loaderDemo"), demos.get("loadClassDemo"),
                        demos.get("serviceDemo"), demos.get("sdkIntDemo"), demos.get("suppressedDemo"), demos.get("zeroCheckDemo"),
                        demos.get("nullCheckDemo"), demos.get("frameIdDemo"), demos.get("frameCbDemo"), demos.get("postCbDemo"),
                        demos.get("plusCollDemo"), demos.get("childAddDemo"), demos.get("childRemoveDemo"), demos.get("attachPropDemo"), demos.get("ctxDemo"),
                        demos.get("resDemo"), demos.get("metricsDemo"), demos.get("configDemo"), demos.get("layoutDirDemo"), demos.get("configCopyDemo"),
                        demos.get("touchModeDemo"),
                        demos.get("willNotDrawDemo"), demos.get("focusableDemo"), demos.get("a11yImportantDemo"), demos.get("a11yDelegateDemo"),
                        demos.get("dragDemo"), demos.get("getClassDemo"), demos.get("getClassHostDemo"), demos.get("subDemo"), demos.get("layoutDemo"),
                        demos.get("measuredDemo"),
                        demos.get("insetsAnimDemo"), demos.get("specDemo"), demos.get("localeDemo"), demos.get("forceDarkDemo"), demos.get("windowFocusDemo"), demos.get("hwDemo"), demos.get("focusCheckDemo"), demos.get("translationDemo"), demos.get("stackDemo"), demos.get("traceDemo"),
                        demos.get("retainedDemo"), demos.get("bundleDemo"), demos.get("bundleIntDemo"), demos.get("applicationDemo"), demos.get("intentDemo"),
                        demos.get("intentExtrasDemo"), demos.get("iterableDemo"), demos.get("entryDemo"), demos.get("classPrimDemo"),
                        demos.get("resolverDemo"), demos.get("settingsUriDemo"), demos.get("contentObserverDemo"), demos.get("settingsFloatDemo"),
                        demos.get("resolverObsDemo"), demos.get("themeDemo"), demos.get("resColorDemo"), demos.get("compCbDemo"), demos.get("fillFloatDemo"),
                        demos.get("hostFillDemo"), demos.get("sysServiceDemo"), demos.get("a11yDemo"), demos.get("a11yEnabledDemo"), demos.get("sortDemo"),
                        demos.get("autofillDemo"), demos.get("autofillCbDemo"),
                        demos.get("importantDemo"), demos.get("captureDemo"), demos.get("clipboardDemo"), demos.get("treeDemo"), demos.get("viewConfigDemo"),
                        demos.get("appCtxDemo"),
                        demos.get("ctorDemo"), demos.get("fileDemo"),
                        demos.get("fileDenyDemo"), demos.get("fileMkdirDemo")));
        builder.writeTo(new FileDataStore(dexFile.toFile()));
    }

    private static Map<String, BuilderMethod> writeCoreDemos(DexBuilder builder, BuilderMethodReference objectInit) {
        Map<String, BuilderMethod> demos = new HashMap<>();
        BuilderMethodReference helloId = builder.internMethodReference(
                new ImmutableMethodReference(HELLO, "id",
                        Collections.singletonList("I"), "I"));
        BuilderFieldReference countField = builder.internFieldReference(
                new ImmutableFieldReference(COUNTER, "count", "I"));
        BuilderFieldReference counterStatic = builder.internFieldReference(
                new ImmutableFieldReference(HELLO, "counter", "I"));

        MutableMethodImplementation initImpl = new MutableMethodImplementation(1);
        initImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0, 0,
                objectInit));
        initImpl.addInstruction(new BuilderInstruction10x(Opcode.RETURN_VOID));
        BuilderMethod init = builder.internMethod(HELLO, "<init>",
                Collections.<ImmutableMethodParameter>emptyList(), "V",
                AccessFlags.PUBLIC.getValue() | AccessFlags.CONSTRUCTOR.getValue(),
                Collections.emptySet(), Collections.emptySet(), initImpl);
        demos.put("init", init);

        MutableMethodImplementation idImpl = new MutableMethodImplementation(2);
        idImpl.addInstruction(new BuilderInstruction12x(Opcode.MOVE, 0, 1));
        idImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod id = builder.internMethod(HELLO, "id",
                Collections.singletonList(
                        new ImmutableMethodParameter("I", Collections.emptySet(), "x")),
                "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), idImpl);
        demos.put("id", id);

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
        demos.put("twice", twice);

        MutableMethodImplementation greetImpl = new MutableMethodImplementation(1);
        greetImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 0,
                builder.internStringReference("hi")));
        greetImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod greet = builder.internMethod(HELLO, "greet",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/String;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), greetImpl);
        demos.put("greet", greet);

        // make(): Counter c = new Counter(); c.count = 4; return c.count + 1;
        MutableMethodImplementation makeImpl = new MutableMethodImplementation(3);
        makeImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference(COUNTER)));
        makeImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        makeImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(COUNTER, "<init>",
                        Collections.<String>emptyList(), "V"))));
        makeImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 4));
        makeImpl.addInstruction(new BuilderInstruction22c(Opcode.IPUT, 1, 0, countField));
        makeImpl.addInstruction(new BuilderInstruction22c(Opcode.IGET, 2, 0, countField));
        makeImpl.addInstruction(new BuilderInstruction22b(Opcode.ADD_INT_LIT8, 2, 2, 1));
        makeImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 2));
        BuilderMethod make = builder.internMethod(HELLO, "make",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), makeImpl);
        demos.put("make", make);

        // viewLp(): framework-dispatch proof. v = new View(); (placeholder,
        // argless framework init) v.setLayoutParams(new
        // ViewGroup.LayoutParams(30, 40)); I = v.getLayoutParams().width —
        // the dispatch mirrors a real android.view.View, the roundtrip
        // keeps parameter identity, and the width read is a real host
        // field access since the ported LayoutParams allocates on the host.
        MutableMethodImplementation viewLpImpl = new MutableMethodImplementation(6);
        viewLpImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        viewLpImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        viewLpImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1,
                0, 0, 0, 0, 0, builder.internMethodReference(
                        new ImmutableMethodReference("Landroid/view/View;", "<init>",
                                Collections.<String>emptyList(), "V"))));
        viewLpImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Landroid/view/ViewGroup$LayoutParams;")));
        viewLpImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        viewLpImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 2, 30));
        viewLpImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 3, 40));
        viewLpImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 3,
                1, 2, 3, 0, 0, builder.internMethodReference(
                        new ImmutableMethodReference("Landroid/view/ViewGroup$LayoutParams;",
                                "<init>", Arrays.asList("I", "I"), "V"))));
        viewLpImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2,
                0, 1, 0, 0, 0, builder.internMethodReference(
                        new ImmutableMethodReference("Landroid/view/View;",
                                "setLayoutParams",
                                Collections.singletonList("Landroid/view/ViewGroup$LayoutParams;"),
                                "V"))));
        viewLpImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1,
                0, 0, 0, 0, 0, builder.internMethodReference(
                        new ImmutableMethodReference("Landroid/view/View;",
                                "getLayoutParams",
                                Collections.<String>emptyList(),
                                "Landroid/view/ViewGroup$LayoutParams;"))));
        viewLpImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 4));
        viewLpImpl.addInstruction(new BuilderInstruction22c(Opcode.IGET, 5, 4,
                builder.internFieldReference(new ImmutableFieldReference(
                        "Landroid/view/ViewGroup$LayoutParams;", "width", "I"))));
        viewLpImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 5));
        BuilderMethod viewLp = builder.internMethod(HELLO, "viewLp",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), viewLpImpl);
        demos.put("viewLp", viewLp);

        // vgAdd(): protected-member mirror dispatch. g = new Vg() (a dex
        // class extending ViewGroup); child = new View(); params = new
        // ViewGroup.LayoutParams(30, 40); group.addViewInLayout(child, -1,
        // params, false) routes through framework-dispatch to the module's
        // protected ViewGroup port, then group.getChildCount() reads the
        // same mirror back: addResult + count = 2.
        MutableMethodImplementation vgAddImpl = new MutableMethodImplementation(9);
        vgAddImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference(VG)));
        vgAddImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        vgAddImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1,
                0, 0, 0, 0, 0, builder.internMethodReference(
                        new ImmutableMethodReference(VG, "<init>",
                                Collections.<String>emptyList(), "V"))));
        vgAddImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Landroid/view/View;")));
        vgAddImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        vgAddImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1,
                1, 0, 0, 0, 0, builder.internMethodReference(
                        new ImmutableMethodReference("Landroid/view/View;", "<init>",
                                Collections.<String>emptyList(), "V"))));
        vgAddImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 2,
                builder.internTypeReference("Landroid/view/ViewGroup$LayoutParams;")));
        vgAddImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 2));
        vgAddImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 3, 30));
        vgAddImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 4, 40));
        vgAddImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 3,
                2, 3, 4, 0, 0, builder.internMethodReference(
                        new ImmutableMethodReference("Landroid/view/ViewGroup$LayoutParams;",
                                "<init>", Arrays.asList("I", "I"), "V"))));
        vgAddImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 5, -1));
        vgAddImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 6, 0));
        vgAddImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 5,
                0, 1, 5, 2, 6, builder.internMethodReference(
                        new ImmutableMethodReference(VG, "addViewInLayout",
                                Arrays.asList("Landroid/view/View;", "I",
                                        "Landroid/view/ViewGroup$LayoutParams;", "Z"),
                                "Z"))));
        vgAddImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 7));
        vgAddImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1,
                0, 0, 0, 0, 0, builder.internMethodReference(
                        new ImmutableMethodReference("Landroid/view/ViewGroup;",
                                "getChildCount",
                                Collections.<String>emptyList(), "I"))));
        vgAddImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 8));
        vgAddImpl.addInstruction(new BuilderInstruction23x(Opcode.ADD_INT, 8, 8, 7));
        vgAddImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 8));
        BuilderMethod vgAdd = builder.internMethod(HELLO, "vgAdd",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), vgAddImpl);
        demos.put("vgAdd", vgAdd);

        // sum(): int[] a = {7, 8, 9}; return a[0] + a[1] + a[2];
        MutableMethodImplementation sumImpl = new MutableMethodImplementation(3);
        sumImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 3));
        sumImpl.addInstruction(new BuilderInstruction22c(Opcode.NEW_ARRAY, 0, 1,
                builder.internTypeReference("[I")));
        putElement(sumImpl, builder, 0, 7);
        putElement(sumImpl, builder, 1, 8);
        putElement(sumImpl, builder, 2, 9);
        sumImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 0));
        sumImpl.addInstruction(new BuilderInstruction23x(Opcode.AGET, 1, 0, 1));
        sumImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 1));
        sumImpl.addInstruction(new BuilderInstruction23x(Opcode.AGET, 2, 0, 2));
        sumImpl.addInstruction(new BuilderInstruction23x(Opcode.ADD_INT, 1, 1, 2));
        sumImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 2));
        sumImpl.addInstruction(new BuilderInstruction23x(Opcode.AGET, 2, 0, 2));
        sumImpl.addInstruction(new BuilderInstruction23x(Opcode.ADD_INT, 1, 1, 2));
        sumImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 1));
        BuilderMethod sum = builder.internMethod(HELLO, "sum",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), sumImpl);
        demos.put("sum", sum);

        // len(): return new int[5].length;
        MutableMethodImplementation lenImpl = new MutableMethodImplementation(2);
        lenImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 0, 5));
        lenImpl.addInstruction(new BuilderInstruction22c(Opcode.NEW_ARRAY, 1, 0,
                builder.internTypeReference("[I")));
        lenImpl.addInstruction(new BuilderInstruction12x(Opcode.ARRAY_LENGTH, 0, 1));
        lenImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod len = builder.internMethod(HELLO, "len",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), lenImpl);
        demos.put("len", len);

        // safely(): try { throw "boom"; } catch-all { return 7; }
        MutableMethodImplementation safeImpl = new MutableMethodImplementation(1);
        safeImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 0,
                builder.internStringReference("boom")));
        safeImpl.addInstruction(new BuilderInstruction11x(Opcode.THROW, 0));
        safeImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 0, 3));
        safeImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        safeImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_EXCEPTION, 0));
        safeImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 0, 7));
        safeImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        Label tryStart = safeImpl.newLabelForIndex(0);
        Label tryEnd = safeImpl.newLabelForIndex(4);
        safeImpl.addCatch(tryStart, tryEnd, safeImpl.newLabelForIndex(4));
        BuilderMethod safely = builder.internMethod(HELLO, "safely",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), safeImpl);
        demos.put("safely", safely);

        // <clinit>: counter = 41.  boot(): return counter + 1;
        MutableMethodImplementation clinitImpl = new MutableMethodImplementation(1);
        clinitImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 0, 9));
        clinitImpl.addInstruction(new BuilderInstruction22b(Opcode.MUL_INT_LIT8, 0, 0, 4));
        clinitImpl.addInstruction(new BuilderInstruction22b(Opcode.ADD_INT_LIT8, 0, 0, 5));
        clinitImpl.addInstruction(new BuilderInstruction21c(Opcode.SPUT, 0, counterStatic));
        clinitImpl.addInstruction(new BuilderInstruction10x(Opcode.RETURN_VOID));
        BuilderMethod clinit = builder.internMethod(HELLO, "<clinit>",
                Collections.<ImmutableMethodParameter>emptyList(), "V",
                AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), clinitImpl);
        demos.put("clinit", clinit);
        MutableMethodImplementation bootImpl = new MutableMethodImplementation(1);
        bootImpl.addInstruction(new BuilderInstruction21c(Opcode.SGET, 0, counterStatic));
        bootImpl.addInstruction(new BuilderInstruction22b(Opcode.ADD_INT_LIT8, 0, 0, 1));
        bootImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod boot = builder.internMethod(HELLO, "boot",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), bootImpl);
        demos.put("boot", boot);

        // nullCast(): check-cast null to Counter (passes).
        MutableMethodImplementation nullCastImpl = new MutableMethodImplementation(1);
        nullCastImpl.addInstruction(new BuilderInstruction21c(Opcode.CHECK_CAST, 0,
                builder.internTypeReference(COUNTER)));
        nullCastImpl.addInstruction(new BuilderInstruction10x(Opcode.RETURN_VOID));
        BuilderMethod nullCast = builder.internMethod(HELLO, "nullCast",
                Collections.<ImmutableMethodParameter>emptyList(), "V",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), nullCastImpl);
        demos.put("nullCast", nullCast);

        // castOk(): Sub s = new Sub(); return s instanceof Base ? 1 : 0.
        MutableMethodImplementation castOkImpl = new MutableMethodImplementation(2);
        castOkImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference(SUB)));
        castOkImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        castOkImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(SUB, "<init>",
                        Collections.<String>emptyList(), "V"))));
        castOkImpl.addInstruction(new BuilderInstruction21c(Opcode.CHECK_CAST, 0,
                builder.internTypeReference(BASE)));
        castOkImpl.addInstruction(new BuilderInstruction22c(Opcode.INSTANCE_OF, 1, 0,
                builder.internTypeReference(BASE)));
        castOkImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 1));
        BuilderMethod castOk = builder.internMethod(HELLO, "castOk",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), castOkImpl);
        demos.put("castOk", castOk);

        // castFail(): (Counter) new Sub() -> ClassCastException.
        MutableMethodImplementation castFailImpl = new MutableMethodImplementation(1);
        castFailImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference(SUB)));
        castFailImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        castFailImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(SUB, "<init>",
                        Collections.<String>emptyList(), "V"))));
        castFailImpl.addInstruction(new BuilderInstruction21c(Opcode.CHECK_CAST, 0,
                builder.internTypeReference(COUNTER)));
        castFailImpl.addInstruction(new BuilderInstruction10x(Opcode.RETURN_VOID));
        BuilderMethod castFail = builder.internMethod(HELLO, "castFail",
                Collections.<ImmutableMethodParameter>emptyList(), "V",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), castFailImpl);
        demos.put("castFail", castFail);

        // cmpTiny(): return Long.compare(5L, 7L);
        MutableMethodImplementation cmpImpl = new MutableMethodImplementation(5);
        cmpImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_WIDE_16, 0, 5));
        cmpImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_WIDE_16, 2, 7));
        cmpImpl.addInstruction(new BuilderInstruction23x(Opcode.CMP_LONG, 4, 0, 2));
        cmpImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 4));
        BuilderMethod cmpTiny = builder.internMethod(HELLO, "cmpTiny",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), cmpImpl);
        demos.put("cmpTiny", cmpTiny);

        // sb(): new StringBuilder().append("a").append(1).toString();
        MutableMethodImplementation sbImpl = new MutableMethodImplementation(2);
        sbImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Ljava/lang/StringBuilder;")));
        sbImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        sbImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/StringBuilder;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        sbImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("a")));
        sbImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/StringBuilder;", "append",
                        Collections.singletonList("Ljava/lang/String;"),
                        "Ljava/lang/StringBuilder;"))));
        sbImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 1));
        sbImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/StringBuilder;", "append",
                        Collections.singletonList("I"), "Ljava/lang/StringBuilder;"))));
        sbImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        sbImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 1, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/StringBuilder;", "toString",
                        Collections.<String>emptyList(), "Ljava/lang/String;"))));
        sbImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        sbImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod sb = builder.internMethod(HELLO, "sb",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/String;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), sbImpl);
        demos.put("sb", sb);

        // throwCatch(): try { throw new IllegalStateException("desk"); }
        // catch-all { return getMessage(); }
        MutableMethodImplementation throwImpl = new MutableMethodImplementation(2);
        throwImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Ljava/lang/IllegalStateException;")));
        throwImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        throwImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("desk")));
        throwImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 0, 1, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/IllegalStateException;", "<init>",
                        Collections.singletonList("Ljava/lang/String;"), "V"))));
        throwImpl.addInstruction(new BuilderInstruction11x(Opcode.THROW, 0));
        throwImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 0,
                builder.internStringReference("unreached")));
        throwImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        throwImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_EXCEPTION, 0));
        throwImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/IllegalStateException;", "getMessage",
                        Collections.<String>emptyList(), "Ljava/lang/String;"))));
        throwImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        throwImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        Label throwStart = throwImpl.newLabelForIndex(4);
        Label throwEnd = throwImpl.newLabelForIndex(7);
        throwImpl.addCatch(throwStart, throwEnd, throwImpl.newLabelForIndex(7));
        BuilderMethod throwCatch = builder.internMethod(HELLO, "throwCatch",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/String;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), throwImpl);
        demos.put("throwCatch", throwCatch);

        // intBox(): Integer.valueOf(41).intValue() + 1;
        MutableMethodImplementation intBoxImpl = new MutableMethodImplementation(1);
        intBoxImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 0, 41));
        intBoxImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Integer;", "valueOf",
                        Collections.singletonList("I"), "Ljava/lang/Integer;"))));
        intBoxImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        intBoxImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Integer;", "intValue",
                        Collections.<String>emptyList(), "I"))));
        intBoxImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        intBoxImpl.addInstruction(new BuilderInstruction22b(Opcode.ADD_INT_LIT8, 0, 0, 1));
        intBoxImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod intBox = builder.internMethod(HELLO, "intBox",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), intBoxImpl);
        demos.put("intBox", intBox);

        // arrayCopy(): copy {9, 8, 7}, return dst[1].
        MutableMethodImplementation copyImpl = new MutableMethodImplementation(5);
        copyImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 3, 3));
        copyImpl.addInstruction(new BuilderInstruction22c(Opcode.NEW_ARRAY, 0, 3,
                builder.internTypeReference("[I")));
        putElement(copyImpl, builder, 0, 9);
        putElement(copyImpl, builder, 1, 8);
        putElement(copyImpl, builder, 2, 7);
        copyImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 3, 3));
        copyImpl.addInstruction(new BuilderInstruction22c(Opcode.NEW_ARRAY, 1, 3,
                builder.internTypeReference("[I")));
        copyImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 0));
        copyImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 3, 0));
        copyImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 4, 3));
        copyImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 5, 0, 2, 1, 3, 4,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/System;", "arraycopy",
                        Arrays.asList("Ljava/lang/Object;", "I", "Ljava/lang/Object;", "I", "I"),
                        "V"))));
        copyImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 1));
        copyImpl.addInstruction(new BuilderInstruction23x(Opcode.AGET, 2, 1, 2));
        copyImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 2));
        BuilderMethod arrayCopy = builder.internMethod(HELLO, "arrayCopy",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), copyImpl);
        demos.put("arrayCopy", arrayCopy);

        // appendableCast(): (Appendable) new StringBuilder() instanceof CharSequence.
        MutableMethodImplementation appImpl = new MutableMethodImplementation(2);
        appImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Ljava/lang/StringBuilder;")));
        appImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        appImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/StringBuilder;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        appImpl.addInstruction(new BuilderInstruction21c(Opcode.CHECK_CAST, 0,
                builder.internTypeReference("Ljava/lang/Appendable;")));
        appImpl.addInstruction(new BuilderInstruction22c(Opcode.INSTANCE_OF, 1, 0,
                builder.internTypeReference("Ljava/lang/CharSequence;")));
        appImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 1));
        BuilderMethod appendableCast = builder.internMethod(HELLO, "appendableCast",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), appImpl);
        demos.put("appendableCast", appendableCast);

        // badCast(): (Integer) new StringBuilder().
        MutableMethodImplementation badImpl = new MutableMethodImplementation(1);
        badImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Ljava/lang/StringBuilder;")));
        badImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        badImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/StringBuilder;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        badImpl.addInstruction(new BuilderInstruction21c(Opcode.CHECK_CAST, 0,
                builder.internTypeReference("Ljava/lang/Integer;")));
        badImpl.addInstruction(new BuilderInstruction10x(Opcode.RETURN_VOID));
        BuilderMethod badCast = builder.internMethod(HELLO, "badCast",
                Collections.<ImmutableMethodParameter>emptyList(), "V",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), badImpl);
        demos.put("badCast", badCast);

        // stringOps(): "hello".length() + "hello".indexOf("l").
        MutableMethodImplementation strImpl = new MutableMethodImplementation(3);
        strImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 0,
                builder.internStringReference("hello")));
        strImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/String;", "length",
                        Collections.<String>emptyList(), "I"))));
        strImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 1));
        strImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 2,
                builder.internStringReference("l")));
        strImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 2, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/String;", "indexOf",
                        Collections.singletonList("Ljava/lang/String;"), "I"))));
        strImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        strImpl.addInstruction(new BuilderInstruction23x(Opcode.ADD_INT, 1, 1, 2));
        strImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 1));
        BuilderMethod stringOps = builder.internMethod(HELLO, "stringOps",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), strImpl);
        demos.put("stringOps", stringOps);

        // mathMax(): Math.max(3, 7).
        MutableMethodImplementation mathImpl = new MutableMethodImplementation(2);
        mathImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 0, 3));
        mathImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 7));
        mathImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 2, 0, 1, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Math;", "max", Arrays.asList("I", "I"), "I"))));
        mathImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        mathImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod mathMax = builder.internMethod(HELLO, "mathMax",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), mathImpl);
        demos.put("mathMax", mathMax);

        // missing(): calls a framework method that does not exist.
        MutableMethodImplementation missingImpl = new MutableMethodImplementation(0);
        missingImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 0, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Lfw/Base;", "nope", Collections.<String>emptyList(), "V"))));
        missingImpl.addInstruction(new BuilderInstruction10x(Opcode.RETURN_VOID));
        BuilderMethod missing = builder.internMethod(HELLO, "missing",
                Collections.<ImmutableMethodParameter>emptyList(), "V",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), missingImpl);
        demos.put("missing", missing);

        // vis(): android.view.View.VISIBLE.  sdk(): Build.VERSION.SDK_INT.
        MutableMethodImplementation visImpl = new MutableMethodImplementation(1);
        visImpl.addInstruction(new BuilderInstruction21c(Opcode.SGET, 0,
                builder.internFieldReference(new ImmutableFieldReference(
                        "Landroid/view/View;", "VISIBLE", "I"))));
        visImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod vis = builder.internMethod(HELLO, "vis",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), visImpl);
        demos.put("vis", vis);
        MutableMethodImplementation sdkImpl = new MutableMethodImplementation(1);
        sdkImpl.addInstruction(new BuilderInstruction21c(Opcode.SGET, 0,
                builder.internFieldReference(new ImmutableFieldReference(
                        "Landroid/os/Build$VERSION;", "SDK_INT", "I"))));
        sdkImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod sdk = builder.internMethod(HELLO, "sdk",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), sdkImpl);
        demos.put("sdk", sdk);

        // fwCast(): (fw.Base) new Sub2() instanceof fw.Base.
        MutableMethodImplementation fwCastImpl = new MutableMethodImplementation(2);
        fwCastImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference(SUB2)));
        fwCastImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        fwCastImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(SUB2, "<init>",
                        Collections.<String>emptyList(), "V"))));
        fwCastImpl.addInstruction(new BuilderInstruction21c(Opcode.CHECK_CAST, 0,
                builder.internTypeReference(FW_BASE)));
        fwCastImpl.addInstruction(new BuilderInstruction22c(Opcode.INSTANCE_OF, 1, 0,
                builder.internTypeReference(FW_BASE)));
        fwCastImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 1));
        BuilderMethod fwCast = builder.internMethod(HELLO, "fwCast",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), fwCastImpl);
        demos.put("fwCast", fwCast);

        // fwGhost(): (fw.Base) new Orphan() walks into an unknown superclass.
        MutableMethodImplementation fwGhostImpl = new MutableMethodImplementation(1);
        fwGhostImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference(ORPHAN)));
        fwGhostImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        fwGhostImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(ORPHAN, "<init>",
                        Collections.<String>emptyList(), "V"))));
        fwGhostImpl.addInstruction(new BuilderInstruction21c(Opcode.CHECK_CAST, 0,
                builder.internTypeReference(FW_BASE)));
        fwGhostImpl.addInstruction(new BuilderInstruction10x(Opcode.RETURN_VOID));
        BuilderMethod fwGhost = builder.internMethod(HELLO, "fwGhost",
                Collections.<ImmutableMethodParameter>emptyList(), "V",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), fwGhostImpl);
        demos.put("fwGhost", fwGhost);

        // fwInit(): new Lfw/Sub() (framework placeholder, needs an index).
        MutableMethodImplementation fwInitImpl = new MutableMethodImplementation(1);
        fwInitImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference(FW_SUB)));
        fwInitImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        fwInitImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(FW_SUB, "<init>",
                        Collections.<String>emptyList(), "V"))));
        fwInitImpl.addInstruction(new BuilderInstruction10x(Opcode.RETURN_VOID));
        BuilderMethod fwInit = builder.internMethod(HELLO, "fwInit",
                Collections.<ImmutableMethodParameter>emptyList(), "V",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), fwInitImpl);
        demos.put("fwInit", fwInit);

        // activityStub(): new Activity() + onCreate(null) via product stubs.
        MutableMethodImplementation stubImpl = new MutableMethodImplementation(2);
        stubImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/app/Activity;")));
        stubImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        stubImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        stubImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "onCreate",
                        Collections.singletonList("Landroid/os/Bundle;"), "V"))));
        stubImpl.addInstruction(new BuilderInstruction10x(Opcode.RETURN_VOID));
        BuilderMethod activityStub = builder.internMethod(HELLO, "activityStub",
                Collections.<ImmutableMethodParameter>emptyList(), "V",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), stubImpl);
        demos.put("activityStub", activityStub);

        // nops(): nop; return 9.
        MutableMethodImplementation nopImpl = new MutableMethodImplementation(1);
        nopImpl.addInstruction(new BuilderInstruction10x(Opcode.NOP));
        nopImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 0, 7));
        nopImpl.addInstruction(new BuilderInstruction22b(Opcode.ADD_INT_LIT8, 0, 0, 2));
        nopImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod nops = builder.internMethod(HELLO, "nops",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), nopImpl);
        demos.put("nops", nops);

        // covarOk(): (Object[]) new Counter[1] instanceof Counter[].
        MutableMethodImplementation covarImpl = new MutableMethodImplementation(2);
        covarImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 0, 1));
        covarImpl.addInstruction(new BuilderInstruction22c(Opcode.NEW_ARRAY, 0, 0,
                builder.internTypeReference("[Ltest/Counter;")));
        covarImpl.addInstruction(new BuilderInstruction21c(Opcode.CHECK_CAST, 0,
                builder.internTypeReference("[Ljava/lang/Object;")));
        covarImpl.addInstruction(new BuilderInstruction22c(Opcode.INSTANCE_OF, 1, 0,
                builder.internTypeReference("[Ltest/Counter;")));
        covarImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 1));
        BuilderMethod covarOk = builder.internMethod(HELLO, "covarOk",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), covarImpl);
        demos.put("covarOk", covarOk);

        // covarPrim(): (Object[]) new int[2] must fail.
        MutableMethodImplementation covarPrimImpl = new MutableMethodImplementation(1);
        covarPrimImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 0, 2));
        covarPrimImpl.addInstruction(new BuilderInstruction22c(Opcode.NEW_ARRAY, 0, 0,
                builder.internTypeReference("[I")));
        covarPrimImpl.addInstruction(new BuilderInstruction21c(Opcode.CHECK_CAST, 0,
                builder.internTypeReference("[Ljava/lang/Object;")));
        covarPrimImpl.addInstruction(new BuilderInstruction10x(Opcode.RETURN_VOID));
        BuilderMethod covarPrim = builder.internMethod(HELLO, "covarPrim",
                Collections.<ImmutableMethodParameter>emptyList(), "V",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), covarPrimImpl);
        demos.put("covarPrim", covarPrim);

        // constClass(): Ltest.Counter.class.getName().
        MutableMethodImplementation constImpl = new MutableMethodImplementation(1);
        constImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_CLASS, 0,
                builder.internTypeReference(COUNTER)));
        constImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Class;", "getName",
                        Collections.<String>emptyList(), "Ljava/lang/String;"))));
        constImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        constImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod constClass = builder.internMethod(HELLO, "constClass",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/String;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), constImpl);
        demos.put("constClass", constClass);

        // clazzIsInstance(): Ltest.Counter.class.isInstance(new Counter()).
        MutableMethodImplementation clazzImpl = new MutableMethodImplementation(3);
        clazzImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference(COUNTER)));
        clazzImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        clazzImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(COUNTER, "<init>",
                        Collections.<String>emptyList(), "V"))));
        clazzImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_CLASS, 1,
                builder.internTypeReference(COUNTER)));
        clazzImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 1, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Class;", "isInstance",
                        Collections.singletonList("Ljava/lang/Object;"), "Z"))));
        clazzImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        clazzImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 2));
        BuilderMethod clazzIsInstance = builder.internMethod(HELLO, "clazzIsInstance",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), clazzImpl);
        demos.put("clazzIsInstance", clazzIsInstance);

        // updater(): AtomicReferenceFieldUpdater over Counter.count.
        MutableMethodImplementation updaterImpl = new MutableMethodImplementation(5);
        updaterImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference(COUNTER)));
        updaterImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        updaterImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(COUNTER, "<init>",
                        Collections.<String>emptyList(), "V"))));
        updaterImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_CLASS, 1,
                builder.internTypeReference(COUNTER)));
        updaterImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_CLASS, 2,
                builder.internTypeReference("Ljava/lang/Object;")));
        updaterImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 3,
                builder.internStringReference("count")));
        updaterImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 3, 1, 2, 3, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;", "newUpdater",
                        Arrays.asList("Ljava/lang/Class;", "Ljava/lang/Class;",
                                "Ljava/lang/String;"),
                        "Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;"))));
        updaterImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        updaterImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 4, 1, 0, 4, 4,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;",
                        "compareAndSet",
                        Arrays.asList("Ljava/lang/Object;", "Ljava/lang/Object;",
                                "Ljava/lang/Object;"),
                        "Z"))));
        updaterImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 4));
        updaterImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 7));
        updaterImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 3, 1, 0, 2, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;", "set",
                        Arrays.asList("Ljava/lang/Object;", "Ljava/lang/Object;"), "V"))));
        updaterImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 1, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;", "get",
                        Collections.singletonList("Ljava/lang/Object;"),
                        "Ljava/lang/Object;"))));
        updaterImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 2));
        updaterImpl.addInstruction(new BuilderInstruction23x(Opcode.ADD_INT, 2, 4, 2));
        updaterImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 2));
        BuilderMethod updater = builder.internMethod(HELLO, "updater",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), updaterImpl);
        demos.put("updater", updater);

        // sysClock(): SystemClock.uptimeMillis().
        MutableMethodImplementation clockImpl = new MutableMethodImplementation(2);
        clockImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 0, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/os/SystemClock;", "uptimeMillis",
                        Collections.<String>emptyList(), "J"))));
        clockImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_WIDE, 0));
        clockImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_WIDE, 0));
        BuilderMethod sysClock = builder.internMethod(HELLO, "sysClock",
                Collections.<ImmutableMethodParameter>emptyList(), "J",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), clockImpl);
        demos.put("sysClock", sysClock);

        // longArith(): ((100 + 7 - 7) * 7 / 7) as longs.
        MutableMethodImplementation longImpl = new MutableMethodImplementation(6);
        longImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_WIDE_16, 0, 100));
        longImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_WIDE_16, 2, 7));
        longImpl.addInstruction(new BuilderInstruction23x(Opcode.ADD_LONG, 4, 0, 2));
        longImpl.addInstruction(new BuilderInstruction12x(Opcode.SUB_LONG_2ADDR, 4, 2));
        longImpl.addInstruction(new BuilderInstruction12x(Opcode.MUL_LONG_2ADDR, 4, 2));
        longImpl.addInstruction(new BuilderInstruction12x(Opcode.DIV_LONG_2ADDR, 4, 2));
        longImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_WIDE, 4));
        BuilderMethod longArith = builder.internMethod(HELLO, "longArith",
                Collections.<ImmutableMethodParameter>emptyList(), "J",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), longImpl);
        demos.put("longArith", longArith);

        // floatArith(): 7f / 2f + 2f.
        MutableMethodImplementation floatImpl = new MutableMethodImplementation(3);
        floatImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 0, 7));
        floatImpl.addInstruction(new BuilderInstruction12x(Opcode.INT_TO_FLOAT, 0, 0));
        floatImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 2));
        floatImpl.addInstruction(new BuilderInstruction12x(Opcode.INT_TO_FLOAT, 1, 1));
        floatImpl.addInstruction(new BuilderInstruction23x(Opcode.DIV_FLOAT, 2, 0, 1));
        floatImpl.addInstruction(new BuilderInstruction12x(Opcode.ADD_FLOAT_2ADDR, 2, 1));
        floatImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 2));
        BuilderMethod floatArith = builder.internMethod(HELLO, "floatArith",
                Collections.<ImmutableMethodParameter>emptyList(), "F",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), floatImpl);
        demos.put("floatArith", floatArith);

        // doubleArith(): 7.0 / 2.0.
        MutableMethodImplementation doubleImpl = new MutableMethodImplementation(3);
        doubleImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 0, 7));
        doubleImpl.addInstruction(new BuilderInstruction12x(Opcode.INT_TO_DOUBLE, 0, 0));
        doubleImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 2));
        doubleImpl.addInstruction(new BuilderInstruction12x(Opcode.INT_TO_DOUBLE, 1, 1));
        doubleImpl.addInstruction(new BuilderInstruction23x(Opcode.DIV_DOUBLE, 2, 0, 1));
        doubleImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_WIDE, 2));
        BuilderMethod doubleArith = builder.internMethod(HELLO, "doubleArith",
                Collections.<ImmutableMethodParameter>emptyList(), "D",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), doubleImpl);
        demos.put("doubleArith", doubleArith);

        // Kotlin-shaped helpers (LazyKt has NO methods: tests the dex-miss path).
        builder.internClassDef("Lkotlin/Lazy;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.INTERFACE.getValue()
                        | AccessFlags.ABSTRACT.getValue(),
                "Ljava/lang/Object;", Collections.<String>emptyList(), "Lazy.kt",
                Collections.emptySet(),
                Collections.<BuilderField>emptyList(),
                Collections.<BuilderMethod>emptyList());
        builder.internClassDef("Lkotlin/LazyKt;",
                AccessFlags.PUBLIC.getValue(), "Ljava/lang/Object;",
                Collections.<String>emptyList(), "LazyKt.kt",
                Collections.emptySet(),
                Collections.<BuilderField>emptyList(),
                Collections.<BuilderMethod>emptyList());
        builder.internClassDef("Lkotlin/jvm/functions/Function0;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.INTERFACE.getValue()
                        | AccessFlags.ABSTRACT.getValue(),
                "Ljava/lang/Object;", Collections.<String>emptyList(), "Function0.kt",
                Collections.emptySet(),
                Collections.<BuilderField>emptyList(),
                Collections.<BuilderMethod>emptyList());
        MutableMethodImplementation fnInitImpl = new MutableMethodImplementation(1);
        fnInitImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0, 0,
                objectInit));
        fnInitImpl.addInstruction(new BuilderInstruction10x(Opcode.RETURN_VOID));
        BuilderMethod fnInit = builder.internMethod("Ltest/Fn;", "<init>",
                Collections.<ImmutableMethodParameter>emptyList(), "V",
                AccessFlags.PUBLIC.getValue() | AccessFlags.CONSTRUCTOR.getValue(),
                Collections.emptySet(), Collections.emptySet(), fnInitImpl);
        MutableMethodImplementation fnInvokeImpl = new MutableMethodImplementation(1);
        fnInvokeImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 0, 99));
        fnInvokeImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod fnInvoke = builder.internMethod("Ltest/Fn;", "invoke",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), fnInvokeImpl);
        builder.internClassDef("Ltest/Fn;", AccessFlags.PUBLIC.getValue(), "Ljava/lang/Object;",
                Collections.singletonList("Lkotlin/jvm/functions/Function0;"), "Fn.java",
                Collections.emptySet(),
                Collections.<BuilderField>emptyList(),
                Arrays.asList(fnInit, fnInvoke));

        // lazyDemo(): lazy { 99 }.getValue().
        MutableMethodImplementation lazyImpl = new MutableMethodImplementation(2);
        lazyImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Ltest/Fn;")));
        lazyImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        lazyImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference("Ltest/Fn;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        lazyImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference("Lkotlin/LazyKt;",
                        "lazy", Collections.singletonList("Lkotlin/jvm/functions/Function0;"),
                        "Lkotlin/Lazy;"))));
        lazyImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        lazyImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_INTERFACE, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference("Lkotlin/Lazy;",
                        "getValue",
                        Arrays.asList("Ljava/lang/Object;", "Ljava/lang/Object;"),
                        "Ljava/lang/Object;"))));
        lazyImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        lazyImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod lazyDemo = builder.internMethod(HELLO, "lazyDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), lazyImpl);
        demos.put("lazyDemo", lazyDemo);

        // lazyIsInstance(): lazy { 99 } instanceof Lazy.
        MutableMethodImplementation lazyIsImpl = new MutableMethodImplementation(1);
        lazyIsImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Ltest/Fn;")));
        lazyIsImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        lazyIsImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference("Ltest/Fn;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        lazyIsImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference("Lkotlin/LazyKt;",
                        "lazy", Collections.singletonList("Lkotlin/jvm/functions/Function0;"),
                        "Lkotlin/Lazy;"))));
        lazyIsImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        lazyIsImpl.addInstruction(new BuilderInstruction22c(Opcode.INSTANCE_OF, 0, 0,
                builder.internTypeReference("Lkotlin/Lazy;")));
        lazyIsImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod lazyIsInstance = builder.internMethod(HELLO, "lazyIsInstance",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), lazyIsImpl);
        demos.put("lazyIsInstance", lazyIsInstance);

        // Job implements Runnable by flagging Hello.flag.
        MutableMethodImplementation jobInitImpl = new MutableMethodImplementation(1);
        jobInitImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, objectInit));
        jobInitImpl.addInstruction(new BuilderInstruction10x(Opcode.RETURN_VOID));
        BuilderMethod jobInit = builder.internMethod("Ltest/Job;", "<init>",
                Collections.<ImmutableMethodParameter>emptyList(), "V",
                AccessFlags.PUBLIC.getValue() | AccessFlags.CONSTRUCTOR.getValue(),
                Collections.emptySet(), Collections.emptySet(), jobInitImpl);
        MutableMethodImplementation jobRunImpl = new MutableMethodImplementation(2);
        jobRunImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 7));
        jobRunImpl.addInstruction(new BuilderInstruction21c(Opcode.SPUT, 1,
                builder.internFieldReference(new ImmutableFieldReference(HELLO, "flag", "I"))));
        jobRunImpl.addInstruction(new BuilderInstruction10x(Opcode.RETURN_VOID));
        BuilderMethod jobRun = builder.internMethod("Ltest/Job;", "run",
                Collections.<ImmutableMethodParameter>emptyList(), "V",
                AccessFlags.PUBLIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), jobRunImpl);
        builder.internClassDef("Ltest/Job;", AccessFlags.PUBLIC.getValue(), "Ljava/lang/Object;",
                Collections.singletonList("Ljava/lang/Runnable;"), "Job.java",
                Collections.emptySet(),
                Collections.<BuilderField>emptyList(),
                Arrays.asList(jobInit, jobRun));

        // SubJob: inherits run() from Job (host task dispatch walks up).
        BuilderMethod subJobInit = trivialInit(builder, objectInit, "Ltest/SubJob;",
                "Ltest/Job;");
        builder.internClassDef("Ltest/SubJob;", AccessFlags.PUBLIC.getValue(), "Ltest/Job;",
                Collections.<String>emptyList(), "SubJob.java",
                Collections.emptySet(),
                Collections.<BuilderField>emptyList(),
                Collections.singletonList(subJobInit));

        // executorDemo(): pool.execute(new Job()); return Hello.flag.
        MutableMethodImplementation execImpl = new MutableMethodImplementation(3);
        execImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Ltest/Job;")));
        execImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        execImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference("Ltest/Job;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        execImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 0, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/concurrent/Executors;", "defaultThreadFactory",
                        Collections.<String>emptyList(),
                        "Ljava/util/concurrent/ThreadFactory;"))));
        execImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        execImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 1));
        execImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 2, 2, 1, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/concurrent/Executors;", "newFixedThreadPool",
                        Arrays.asList("I", "Ljava/util/concurrent/ThreadFactory;"),
                        "Ljava/util/concurrent/ExecutorService;"))));
        execImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        execImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_INTERFACE, 2, 1, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/concurrent/Executor;", "execute",
                        Collections.singletonList("Ljava/lang/Runnable;"), "V"))));
        execImpl.addInstruction(new BuilderInstruction21c(Opcode.SGET, 2,
                builder.internFieldReference(new ImmutableFieldReference(HELLO, "flag", "I"))));
        execImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 2));
        BuilderMethod executorDemo = builder.internMethod(HELLO, "executorDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), execImpl);
        demos.put("executorDemo", executorDemo);

        // MyHandler with handleMessage storing msg.what in Hello.flag.
        MutableMethodImplementation handlerInitImpl = new MutableMethodImplementation(1);
        handlerInitImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, objectInit));
        handlerInitImpl.addInstruction(new BuilderInstruction10x(Opcode.RETURN_VOID));
        BuilderMethod handlerInit = builder.internMethod("Ltest/MyHandler;", "<init>",
                Collections.<ImmutableMethodParameter>emptyList(), "V",
                AccessFlags.PUBLIC.getValue() | AccessFlags.CONSTRUCTOR.getValue(),
                Collections.emptySet(), Collections.emptySet(), handlerInitImpl);
        MutableMethodImplementation handleImpl = new MutableMethodImplementation(2);
        handleImpl.addInstruction(new BuilderInstruction22c(Opcode.IGET, 0, 1,
                builder.internFieldReference(new ImmutableFieldReference(
                        "Landroid/os/Message;", "what", "I"))));
        handleImpl.addInstruction(new BuilderInstruction21c(Opcode.SPUT, 0,
                builder.internFieldReference(new ImmutableFieldReference(HELLO, "flag", "I"))));
        handleImpl.addInstruction(new BuilderInstruction10x(Opcode.RETURN_VOID));
        BuilderMethod handleMessage = builder.internMethod("Ltest/MyHandler;", "handleMessage",
                Collections.singletonList(
                        new ImmutableMethodParameter("Landroid/os/Message;",
                                Collections.emptySet(), "msg")),
                "V",
                AccessFlags.PUBLIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), handleImpl);
        builder.internClassDef("Ltest/MyHandler;", AccessFlags.PUBLIC.getValue(),
                "Ljava/lang/Object;",
                Collections.<String>emptyList(), "MyHandler.java",
                Collections.emptySet(),
                Collections.<BuilderField>emptyList(),
                Arrays.asList(handlerInit, handleMessage));

        // postDemo(): handler.post(new Job()) runs inline; return flag.
        MutableMethodImplementation postImpl = new MutableMethodImplementation(2);
        postImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Ltest/MyHandler;")));
        postImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        postImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference("Ltest/MyHandler;",
                        "<init>", Collections.<String>emptyList(), "V"))));
        postImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Ltest/Job;")));
        postImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        postImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 1, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference("Ltest/Job;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        postImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/os/Handler;", "post",
                        Collections.singletonList("Ljava/lang/Runnable;"), "Z"))));
        postImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 1));
        postImpl.addInstruction(new BuilderInstruction21c(Opcode.SGET, 1,
                builder.internFieldReference(new ImmutableFieldReference(HELLO, "flag", "I"))));
        postImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 1));
        BuilderMethod postDemo = builder.internMethod(HELLO, "postDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), postImpl);
        demos.put("postDemo", postDemo);

        // postSubDemo(): posting a Job subclass runs the inherited run().
        MutableMethodImplementation postSubImpl = new MutableMethodImplementation(2);
        postSubImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Ltest/MyHandler;")));
        postSubImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        postSubImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference("Ltest/MyHandler;",
                        "<init>", Collections.<String>emptyList(), "V"))));
        postSubImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Ltest/SubJob;")));
        postSubImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        postSubImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 1, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference("Ltest/SubJob;",
                        "<init>", Collections.<String>emptyList(), "V"))));
        postSubImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/os/Handler;", "post",
                        Collections.singletonList("Ljava/lang/Runnable;"), "Z"))));
        postSubImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 1));
        postSubImpl.addInstruction(new BuilderInstruction21c(Opcode.SGET, 1,
                builder.internFieldReference(new ImmutableFieldReference(HELLO, "flag", "I"))));
        postSubImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 1));
        BuilderMethod postSubDemo = builder.internMethod(HELLO, "postSubDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), postSubImpl);
        demos.put("postSubDemo", postSubDemo);

        // casDemo(): CAS(null, "x") on a fresh cell succeeds (const zero
        // crosses to host as null).
        MutableMethodImplementation casImpl = new MutableMethodImplementation(3);
        casImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Ljava/util/concurrent/atomic/AtomicReferenceArray;")));
        casImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        casImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 1));
        casImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 0, 1, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/concurrent/atomic/AtomicReferenceArray;", "<init>",
                        Collections.singletonList("I"), "V"))));
        casImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("x")));
        casImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 0));
        casImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 4, 0, 2, 2, 1,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/concurrent/atomic/AtomicReferenceArray;", "compareAndSet",
                        Arrays.asList("I", "Ljava/lang/Object;", "Ljava/lang/Object;"),
                        "Z"))));
        casImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        casImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod casDemo = builder.internMethod(HELLO, "casDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Z",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), casImpl);
        demos.put("casDemo", casDemo);

        // handlerDemo(): handler.sendEmptyMessage(42) dispatches; return flag.
        MutableMethodImplementation dispatchImpl = new MutableMethodImplementation(2);
        dispatchImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Ltest/MyHandler;")));
        dispatchImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        dispatchImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference("Ltest/MyHandler;",
                        "<init>", Collections.<String>emptyList(), "V"))));
        dispatchImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/os/Handler;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        dispatchImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 1, 42));
        dispatchImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/os/Handler;", "sendEmptyMessage",
                        Collections.singletonList("I"), "Z"))));
        dispatchImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 1));
        dispatchImpl.addInstruction(new BuilderInstruction21c(Opcode.SGET, 1,
                builder.internFieldReference(new ImmutableFieldReference(HELLO, "flag", "I"))));
        dispatchImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 1));
        BuilderMethod handlerDemo = builder.internMethod(HELLO, "handlerDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), dispatchImpl);
        demos.put("handlerDemo", handlerDemo);

        // threadDemo(): Thread.currentThread().getName().
        MutableMethodImplementation threadImpl = new MutableMethodImplementation(1);
        threadImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 0, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Thread;", "currentThread",
                        Collections.<String>emptyList(), "Ljava/lang/Thread;"))));
        threadImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        threadImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Thread;", "getName",
                        Collections.<String>emptyList(), "Ljava/lang/String;"))));
        threadImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        threadImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod threadDemo = builder.internMethod(HELLO, "threadDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/String;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), threadImpl);
        demos.put("threadDemo", threadDemo);

        // mapDemo(): map.put("k", 42); return map.get("k").
        MutableMethodImplementation mapImpl = new MutableMethodImplementation(4);
        mapImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Ljava/util/HashMap;")));
        mapImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        mapImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/HashMap;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        mapImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("k")));
        mapImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 2, 42));
        mapImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 2, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Integer;", "valueOf",
                        Collections.singletonList("I"), "Ljava/lang/Integer;"))));
        mapImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 2));
        mapImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 3, 0, 1, 2, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/HashMap;", "put",
                        Arrays.asList("Ljava/lang/Object;", "Ljava/lang/Object;"),
                        "Ljava/lang/Object;"))));
        mapImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 3));
        mapImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_INTERFACE, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/Map;", "get",
                        Collections.singletonList("Ljava/lang/Object;"),
                        "Ljava/lang/Object;"))));
        mapImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 2));
        mapImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 2, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Integer;", "intValue",
                        Collections.<String>emptyList(), "I"))));
        mapImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        mapImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 2));
        BuilderMethod mapDemo = builder.internMethod(HELLO, "mapDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), mapImpl);
        demos.put("mapDemo", mapDemo);

        // listDemo(): [7, 8][1].
        MutableMethodImplementation listImpl = new MutableMethodImplementation(2);
        listImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Ljava/util/ArrayList;")));
        listImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        listImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/ArrayList;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        listImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 1, 7));
        listImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 1, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Integer;", "valueOf",
                        Collections.singletonList("I"), "Ljava/lang/Integer;"))));
        listImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        listImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/ArrayList;", "add",
                        Collections.singletonList("Ljava/lang/Object;"), "Z"))));
        listImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 1));
        listImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 1, 8));
        listImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 1, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Integer;", "valueOf",
                        Collections.singletonList("I"), "Ljava/lang/Integer;"))));
        listImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        listImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/ArrayList;", "add",
                        Collections.singletonList("Ljava/lang/Object;"), "Z"))));
        listImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 1));
        listImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 1));
        listImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/ArrayList;", "get",
                        Collections.singletonList("I"), "Ljava/lang/Object;"))));
        listImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        listImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 1, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Integer;", "intValue",
                        Collections.<String>emptyList(), "I"))));
        listImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 1));
        listImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 1));
        BuilderMethod listDemo = builder.internMethod(HELLO, "listDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), listImpl);
        demos.put("listDemo", listDemo);

        // setDemo(): add(x) + add(x) again.
        MutableMethodImplementation setImpl = new MutableMethodImplementation(3);
        setImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Ljava/util/HashSet;")));
        setImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        setImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/HashSet;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        setImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 2,
                builder.internStringReference("x")));
        setImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 2, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/HashSet;", "add",
                        Collections.singletonList("Ljava/lang/Object;"), "Z"))));
        setImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 1));
        setImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 2, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/HashSet;", "add",
                        Collections.singletonList("Ljava/lang/Object;"), "Z"))));
        setImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        setImpl.addInstruction(new BuilderInstruction23x(Opcode.ADD_INT, 1, 1, 2));
        setImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 1));
        BuilderMethod setDemo = builder.internMethod(HELLO, "setDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), setImpl);
        demos.put("setDemo", setDemo);

        // iterDemo(): first element via List.iterator().
        MutableMethodImplementation iterImpl = new MutableMethodImplementation(3);
        iterImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Ljava/util/ArrayList;")));
        iterImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        iterImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/ArrayList;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        iterImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 2, 5));
        iterImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 2, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Integer;", "valueOf",
                        Collections.singletonList("I"), "Ljava/lang/Integer;"))));
        iterImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 2));
        iterImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 2, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/ArrayList;", "add",
                        Collections.singletonList("Ljava/lang/Object;"), "Z"))));
        iterImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        iterImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_INTERFACE, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/List;", "iterator",
                        Collections.<String>emptyList(), "Ljava/util/Iterator;"))));
        iterImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        iterImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_INTERFACE, 1, 1, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/Iterator;", "hasNext",
                        Collections.<String>emptyList(), "Z"))));
        iterImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        iterImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_INTERFACE, 1, 1, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/Iterator;", "next",
                        Collections.<String>emptyList(), "Ljava/lang/Object;"))));
        iterImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 2));
        iterImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 2, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Integer;", "intValue",
                        Collections.<String>emptyList(), "I"))));
        iterImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        iterImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 2));
        BuilderMethod iterDemo = builder.internMethod(HELLO, "iterDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), iterImpl);
        demos.put("iterDemo", iterDemo);

        // nullish(): const-4 0 behaves as null for casts, as Integer for self.
        MutableMethodImplementation nullishImpl = new MutableMethodImplementation(3);
        nullishImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 0, 0));
        nullishImpl.addInstruction(new BuilderInstruction21c(Opcode.CHECK_CAST, 0,
                builder.internTypeReference(COUNTER)));
        nullishImpl.addInstruction(new BuilderInstruction22c(Opcode.INSTANCE_OF, 1, 0,
                builder.internTypeReference(COUNTER)));
        nullishImpl.addInstruction(new BuilderInstruction22c(Opcode.INSTANCE_OF, 2, 0,
                builder.internTypeReference("Ljava/lang/Integer;")));
        nullishImpl.addInstruction(new BuilderInstruction23x(Opcode.ADD_INT, 1, 1, 2));
        nullishImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 1));
        BuilderMethod nullish = builder.internMethod(HELLO, "nullish",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), nullishImpl);
        demos.put("nullish", nullish);

        // nullRecv(): calling a method on zero-bits fails like null.
        MutableMethodImplementation nullRecvImpl = new MutableMethodImplementation(1);
        nullRecvImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 0, 0));
        nullRecvImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/String;", "length",
                        Collections.<String>emptyList(), "I"))));
        nullRecvImpl.addInstruction(new BuilderInstruction10x(Opcode.RETURN_VOID));
        BuilderMethod nullRecv = builder.internMethod(HELLO, "nullRecv",
                Collections.<ImmutableMethodParameter>emptyList(), "V",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), nullRecvImpl);
        demos.put("nullRecv", nullRecv);

        // boolStr(): String.valueOf(Boolean.valueOf(true)).
        MutableMethodImplementation boolImpl = new MutableMethodImplementation(1);
        boolImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 0, 1));
        boolImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Boolean;", "valueOf",
                        Collections.singletonList("Z"), "Ljava/lang/Boolean;"))));
        boolImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        boolImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/String;", "valueOf",
                        Collections.singletonList("Ljava/lang/Object;"),
                        "Ljava/lang/String;"))));
        boolImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        boolImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod boolStr = builder.internMethod(HELLO, "boolStr",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/String;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), boolImpl);
        demos.put("boolStr", boolStr);

        // charStr(): Character.valueOf(65).toString().
        MutableMethodImplementation charImpl = new MutableMethodImplementation(1);
        charImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 0, 65));
        charImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Character;", "valueOf",
                        Collections.singletonList("C"), "Ljava/lang/Character;"))));
        charImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        charImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Character;", "toString",
                        Collections.<String>emptyList(), "Ljava/lang/String;"))));
        charImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        charImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod charStr = builder.internMethod(HELLO, "charStr",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/String;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), charImpl);
        demos.put("charStr", charStr);

        // weakGet(): new WeakReference("x").get().
        MutableMethodImplementation weakGetImpl = new MutableMethodImplementation(2);
        weakGetImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Ljava/lang/ref/WeakReference;")));
        weakGetImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        weakGetImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("x")));
        weakGetImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/ref/WeakReference;", "<init>",
                        Collections.singletonList("Ljava/lang/Object;"), "V"))));
        weakGetImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/ref/WeakReference;", "get",
                        Collections.<String>emptyList(), "Ljava/lang/Object;"))));
        weakGetImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        weakGetImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod weakGet = builder.internMethod(HELLO, "weakGet",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), weakGetImpl);
        demos.put("weakGet", weakGet);

        // weakClear(): get, clear, get -> null.
        MutableMethodImplementation weakClearImpl = new MutableMethodImplementation(2);
        weakClearImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Ljava/lang/ref/WeakReference;")));
        weakClearImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        weakClearImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("x")));
        weakClearImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/ref/WeakReference;", "<init>",
                        Collections.singletonList("Ljava/lang/Object;"), "V"))));
        weakClearImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/ref/WeakReference;", "get",
                        Collections.<String>emptyList(), "Ljava/lang/Object;"))));
        weakClearImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        weakClearImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/ref/WeakReference;", "clear",
                        Collections.<String>emptyList(), "V"))));
        weakClearImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/ref/WeakReference;", "get",
                        Collections.<String>emptyList(), "Ljava/lang/Object;"))));
        weakClearImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        weakClearImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod weakClear = builder.internMethod(HELLO, "weakClear",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), weakClearImpl);
        demos.put("weakClear", weakClear);
        return demos;
    }

    private static Map<String, BuilderMethod> writeRuntimeDemos(DexBuilder builder, BuilderMethodReference objectInit) {
        Map<String, BuilderMethod> demos = new HashMap<>();
        // Color enum with RED(0) and BLUE(1); <init> delegates to Enum.<init>.
        MutableMethodImplementation colorInitImpl = new MutableMethodImplementation(3);
        colorInitImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 3, 0, 1, 2, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Enum;", "<init>",
                        Arrays.asList("Ljava/lang/String;", "I"), "V"))));
        colorInitImpl.addInstruction(new BuilderInstruction10x(Opcode.RETURN_VOID));
        BuilderMethod colorInit = builder.internMethod("Ltest/Color;", "<init>",
                Arrays.asList(
                        new ImmutableMethodParameter("Ljava/lang/String;",
                                Collections.emptySet(), "name"),
                        new ImmutableMethodParameter("I", Collections.emptySet(), "ordinal")),
                "V",
                AccessFlags.PUBLIC.getValue() | AccessFlags.CONSTRUCTOR.getValue(),
                Collections.emptySet(), Collections.emptySet(), colorInitImpl);
        MutableMethodImplementation colorClinitImpl = new MutableMethodImplementation(3);
        colorClinitImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Ltest/Color;")));
        colorClinitImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        colorClinitImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("RED")));
        colorClinitImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 0));
        colorClinitImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 3, 0, 1, 2,
                0, 0, builder.internMethodReference(new ImmutableMethodReference("Ltest/Color;",
                        "<init>",
                        Arrays.asList("Ljava/lang/String;", "I"), "V"))));
        colorClinitImpl.addInstruction(new BuilderInstruction21c(Opcode.SPUT_OBJECT, 0,
                builder.internFieldReference(new ImmutableFieldReference("Ltest/Color;", "RED",
                        "Ltest/Color;"))));
        colorClinitImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Ltest/Color;")));
        colorClinitImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        colorClinitImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("BLUE")));
        colorClinitImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 1));
        colorClinitImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 3, 0, 1, 2,
                0, 0, builder.internMethodReference(new ImmutableMethodReference("Ltest/Color;",
                        "<init>",
                        Arrays.asList("Ljava/lang/String;", "I"), "V"))));
        colorClinitImpl.addInstruction(new BuilderInstruction21c(Opcode.SPUT_OBJECT, 0,
                builder.internFieldReference(new ImmutableFieldReference("Ltest/Color;", "BLUE",
                        "Ltest/Color;"))));
        colorClinitImpl.addInstruction(new BuilderInstruction10x(Opcode.RETURN_VOID));
        BuilderMethod colorClinit = builder.internMethod("Ltest/Color;", "<clinit>",
                Collections.<ImmutableMethodParameter>emptyList(), "V",
                AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), colorClinitImpl);
        builder.internClassDef("Ltest/Color;", AccessFlags.PUBLIC.getValue(), "Ljava/lang/Enum;",
                Collections.<String>emptyList(), "Color.java",
                Collections.emptySet(),
                Collections.<BuilderField>emptyList(),
                Arrays.asList(colorInit, colorClinit));

        // colorCmp(): RED.compareTo(BLUE).
        MutableMethodImplementation colorCmpImpl = new MutableMethodImplementation(2);
        colorCmpImpl.addInstruction(new BuilderInstruction21c(Opcode.SGET_OBJECT, 0,
                builder.internFieldReference(new ImmutableFieldReference("Ltest/Color;", "RED",
                        "Ltest/Color;"))));
        colorCmpImpl.addInstruction(new BuilderInstruction21c(Opcode.SGET_OBJECT, 1,
                builder.internFieldReference(new ImmutableFieldReference("Ltest/Color;", "BLUE",
                        "Ltest/Color;"))));
        colorCmpImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference("Ltest/Color;",
                        "compareTo",
                        Collections.singletonList("Ljava/lang/Enum;"), "I"))));
        colorCmpImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        colorCmpImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod colorCmp = builder.internMethod(HELLO, "colorCmp",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), colorCmpImpl);
        demos.put("colorCmp", colorCmp);

        // colorName(): RED.name().
        MutableMethodImplementation colorNameImpl = new MutableMethodImplementation(1);
        colorNameImpl.addInstruction(new BuilderInstruction21c(Opcode.SGET_OBJECT, 0,
                builder.internFieldReference(new ImmutableFieldReference("Ltest/Color;", "RED",
                        "Ltest/Color;"))));
        colorNameImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference("Ltest/Color;",
                        "name", Collections.<String>emptyList(), "Ljava/lang/String;"))));
        colorNameImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        colorNameImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod colorName = builder.internMethod(HELLO, "colorName",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/String;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), colorNameImpl);
        demos.put("colorName", colorName);

        // colorValueOf(): Color.valueOf("BLUE").toString().
        MutableMethodImplementation colorValueImpl = new MutableMethodImplementation(2);
        colorValueImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_CLASS, 0,
                builder.internTypeReference("Ltest/Color;")));
        colorValueImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("BLUE")));
        colorValueImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Enum;", "valueOf",
                        Arrays.asList("Ljava/lang/Class;", "Ljava/lang/String;"),
                        "Ljava/lang/Object;"))));
        colorValueImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        colorValueImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ltest/Color;", "toString",
                        Collections.<String>emptyList(), "Ljava/lang/String;"))));
        colorValueImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        colorValueImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod colorValueOf = builder.internMethod(HELLO, "colorValueOf",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/String;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), colorValueImpl);
        demos.put("colorValueOf", colorValueOf);

        // FlagMap: constant map (get -> 7, containsKey -> true, put -> null).
        MutableMethodImplementation flagInitImpl = new MutableMethodImplementation(1);
        flagInitImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, objectInit));
        flagInitImpl.addInstruction(new BuilderInstruction10x(Opcode.RETURN_VOID));
        BuilderMethod flagInit = builder.internMethod("Ltest/FlagMap;", "<init>",
                Collections.<ImmutableMethodParameter>emptyList(), "V",
                AccessFlags.PUBLIC.getValue() | AccessFlags.CONSTRUCTOR.getValue(),
                Collections.emptySet(), Collections.emptySet(), flagInitImpl);
        MutableMethodImplementation flagGetImpl = new MutableMethodImplementation(2);
        flagGetImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 1, 7));
        flagGetImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 1, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Integer;", "valueOf",
                        Collections.singletonList("I"), "Ljava/lang/Integer;"))));
        flagGetImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        flagGetImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 1));
        BuilderMethod flagGet = builder.internMethod("Ltest/FlagMap;", "get",
                Collections.singletonList(
                        new ImmutableMethodParameter("Ljava/lang/Object;",
                                Collections.emptySet(), "k")),
                "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), flagGetImpl);
        MutableMethodImplementation flagContainsImpl = new MutableMethodImplementation(2);
        flagContainsImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 1));
        flagContainsImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 1));
        BuilderMethod flagContains = builder.internMethod("Ltest/FlagMap;", "containsKey",
                Collections.singletonList(
                        new ImmutableMethodParameter("Ljava/lang/Object;",
                                Collections.emptySet(), "k")),
                "Z",
                AccessFlags.PUBLIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), flagContainsImpl);
        MutableMethodImplementation flagPutImpl = new MutableMethodImplementation(3);
        flagPutImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 0));
        flagPutImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 1));
        BuilderMethod flagPut = builder.internMethod("Ltest/FlagMap;", "put",
                Arrays.asList(
                        new ImmutableMethodParameter("Ljava/lang/Object;",
                                Collections.emptySet(), "k"),
                        new ImmutableMethodParameter("Ljava/lang/Object;",
                                Collections.emptySet(), "v")),
                "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), flagPutImpl);
        builder.internClassDef("Ltest/FlagMap;", AccessFlags.PUBLIC.getValue(),
                "Ljava/lang/Object;",
                Collections.<String>emptyList(), "FlagMap.java",
                Collections.emptySet(),
                Collections.<BuilderField>emptyList(),
                Arrays.asList(flagInit, flagGet, flagContains, flagPut));

        // mapDefaults(): getOrDefault("k", 99) on a dex-implemented map.
        MutableMethodImplementation mapDefImpl = new MutableMethodImplementation(3);
        mapDefImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Ltest/FlagMap;")));
        mapDefImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        mapDefImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference("Ltest/FlagMap;",
                        "<init>", Collections.<String>emptyList(), "V"))));
        mapDefImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("k")));
        mapDefImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 2, 99));
        mapDefImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 2, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Integer;", "valueOf",
                        Collections.singletonList("I"), "Ljava/lang/Integer;"))));
        mapDefImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 2));
        mapDefImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_INTERFACE, 3, 0, 1, 2,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/Map;", "getOrDefault",
                        Arrays.asList("Ljava/lang/Object;", "Ljava/lang/Object;"),
                        "Ljava/lang/Object;"))));
        mapDefImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 2));
        mapDefImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 2, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Integer;", "intValue",
                        Collections.<String>emptyList(), "I"))));
        mapDefImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        mapDefImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 2));
        BuilderMethod mapDefaults = builder.internMethod(HELLO, "mapDefaults",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), mapDefImpl);
        demos.put("mapDefaults", mapDefaults);

        // mapPutAbsent(): putIfAbsent on a present key returns it.
        MutableMethodImplementation mapPutImpl = new MutableMethodImplementation(3);
        mapPutImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Ltest/FlagMap;")));
        mapPutImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        mapPutImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference("Ltest/FlagMap;",
                        "<init>", Collections.<String>emptyList(), "V"))));
        mapPutImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("k")));
        mapPutImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 2, 5));
        mapPutImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 2, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Integer;", "valueOf",
                        Collections.singletonList("I"), "Ljava/lang/Integer;"))));
        mapPutImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 2));
        mapPutImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_INTERFACE, 3, 0, 1, 2,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/Map;", "putIfAbsent",
                        Arrays.asList("Ljava/lang/Object;", "Ljava/lang/Object;"),
                        "Ljava/lang/Object;"))));
        mapPutImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 2));
        mapPutImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 2, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Integer;", "intValue",
                        Collections.<String>emptyList(), "I"))));
        mapPutImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        mapPutImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 2));
        BuilderMethod mapPutAbsent = builder.internMethod(HELLO, "mapPutAbsent",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), mapPutImpl);
        demos.put("mapPutAbsent", mapPutAbsent);

        // repeatDemo(): "ab".repeat(3), purely reflective (no table entry).
        MutableMethodImplementation repeatImpl = new MutableMethodImplementation(2);
        repeatImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 0,
                builder.internStringReference("ab")));
        repeatImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 3));
        repeatImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/String;", "repeat",
                        Collections.singletonList("I"), "Ljava/lang/String;"))));
        repeatImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        repeatImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod repeatDemo = builder.internMethod(HELLO, "repeatDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/String;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), repeatImpl);
        demos.put("repeatDemo", repeatDemo);

        // emptyDemo(): Collections.EMPTY_LIST (reflective field) is empty.
        MutableMethodImplementation emptyImpl = new MutableMethodImplementation(2);
        emptyImpl.addInstruction(new BuilderInstruction21c(Opcode.SGET_OBJECT, 0,
                builder.internFieldReference(new ImmutableFieldReference(
                        "Ljava/util/Collections;", "EMPTY_LIST", "Ljava/util/List;"))));
        emptyImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_INTERFACE, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/List;", "isEmpty",
                        Collections.<String>emptyList(), "Z"))));
        emptyImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 1));
        emptyImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 1));
        BuilderMethod emptyDemo = builder.internMethod(HELLO, "emptyDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), emptyImpl);
        demos.put("emptyDemo", emptyDemo);

        // strictSqrt(): StrictMath.sqrt(16.0), reflective static.
        MutableMethodImplementation strictImpl = new MutableMethodImplementation(2);
        strictImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 0, 16));
        strictImpl.addInstruction(new BuilderInstruction12x(Opcode.INT_TO_DOUBLE, 0, 0));
        strictImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/StrictMath;", "sqrt",
                        Collections.singletonList("D"), "D"))));
        strictImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_WIDE, 0));
        strictImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_WIDE, 0));
        BuilderMethod strictSqrt = builder.internMethod(HELLO, "strictSqrt",
                Collections.<ImmutableMethodParameter>emptyList(), "D",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), strictImpl);
        demos.put("strictSqrt", strictSqrt);

        // linkedMapDemo(): reflectively allocated LinkedHashMap round-trip.
        MutableMethodImplementation linkedImpl = new MutableMethodImplementation(4);
        linkedImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Ljava/util/LinkedHashMap;")));
        linkedImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        linkedImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/LinkedHashMap;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        linkedImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("k")));
        linkedImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 2, 42));
        linkedImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 2, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Integer;", "valueOf",
                        Collections.singletonList("I"), "Ljava/lang/Integer;"))));
        linkedImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 2));
        linkedImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 3, 0, 1, 2, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/Map;", "put",
                        Arrays.asList("Ljava/lang/Object;", "Ljava/lang/Object;"),
                        "Ljava/lang/Object;"))));
        linkedImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 3));
        linkedImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/Map;", "get",
                        Collections.singletonList("Ljava/lang/Object;"),
                        "Ljava/lang/Object;"))));
        linkedImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 2));
        linkedImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 2, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Integer;", "intValue",
                        Collections.<String>emptyList(), "I"))));
        linkedImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        linkedImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 2));
        BuilderMethod linkedMapDemo = builder.internMethod(HELLO, "linkedMapDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), linkedImpl);
        demos.put("linkedMapDemo", linkedMapDemo);

        // blockedExec(): Runtime.exec stays denied (getRuntime itself is a
        // harmless placeholder; only the escape hatch is blocked).
        MutableMethodImplementation blockedImpl = new MutableMethodImplementation(2);
        blockedImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 0, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Runtime;", "getRuntime",
                        Collections.<String>emptyList(), "Ljava/lang/Runtime;"))));
        blockedImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        blockedImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("id")));
        blockedImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Runtime;", "exec",
                        Collections.singletonList("Ljava/lang/String;"),
                        "Ljava/lang/Process;"))));
        blockedImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        blockedImpl.addInstruction(new BuilderInstruction10x(Opcode.RETURN_VOID));
        BuilderMethod blockedExec = builder.internMethod(HELLO, "blockedExec",
                Collections.<ImmutableMethodParameter>emptyList(), "V",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), blockedImpl);
        demos.put("blockedExec", blockedExec);

        // txChain(): pass-through chaining on framework stubs.
        MutableMethodImplementation txImpl = new MutableMethodImplementation(3);
        txImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 0,
                builder.internStringReference("tx")));
        txImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 1));
        txImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 2,
                builder.internStringReference("frag")));
        txImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 3, 0, 1, 2, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/FragmentTransaction;", "add",
                        Arrays.asList("I", "Landroid/app/Fragment;"),
                        "Landroid/app/FragmentTransaction;"))));
        txImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        txImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/FragmentTransaction;", "commit",
                        Collections.<String>emptyList(), "I"))));
        txImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        txImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod txChain = builder.internMethod(HELLO, "txChain",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), txImpl);
        demos.put("txChain", txChain);

        // argbDemo(): Color.argb(255, 1, 2, 3).
        MutableMethodImplementation argbImpl = new MutableMethodImplementation(4);
        argbImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 0, 255));
        argbImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 1));
        argbImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 2));
        argbImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 3, 3));
        argbImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 4, 0, 1, 2, 3, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Color;", "argb",
                        Arrays.asList("I", "I", "I", "I"), "I"))));
        argbImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        argbImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod argbDemo = builder.internMethod(HELLO, "argbDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), argbImpl);
        demos.put("argbDemo", argbDemo);

        // parseDemo(): Color.parseColor("#ff010203").
        MutableMethodImplementation parseImpl = new MutableMethodImplementation(1);
        parseImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 0,
                builder.internStringReference("#ff010203")));
        parseImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Color;", "parseColor",
                        Collections.singletonList("Ljava/lang/String;"), "I"))));
        parseImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        parseImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod parseDemo = builder.internMethod(HELLO, "parseDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), parseImpl);
        demos.put("parseDemo", parseDemo);

        // activityWindow(): new Activity().getWindow() is a placeholder.
        MutableMethodImplementation windowImpl = new MutableMethodImplementation(1);
        windowImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/app/Activity;")));
        windowImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        windowImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        windowImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "getWindow",
                        Collections.<String>emptyList(), "Landroid/view/Window;"))));
        windowImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        windowImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod activityWindow = builder.internMethod(HELLO, "activityWindow",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), windowImpl);
        demos.put("activityWindow", activityWindow);

        // viewVis(): View visibility round-trips through fields, default VISIBLE.
        MutableMethodImplementation viewVisImpl = new MutableMethodImplementation(2);
        viewVisImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        viewVisImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        viewVisImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        viewVisImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "getVisibility",
                        Collections.<String>emptyList(), "I"))));
        viewVisImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 1));
        viewVisImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 1, 8));
        viewVisImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "setVisibility",
                        Collections.singletonList("I"), "V"))));
        viewVisImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "getVisibility",
                        Collections.<String>emptyList(), "I"))));
        viewVisImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        viewVisImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod viewVis = builder.internMethod(HELLO, "viewVis",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), viewVisImpl);
        demos.put("viewVis", viewVis);

        // lpDemo(): LayoutParams(-1, -1).width round-trips through the ctor.
        MutableMethodImplementation lpImpl = new MutableMethodImplementation(2);
        lpImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/ViewGroup$LayoutParams;")));
        lpImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        lpImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 1, -1));
        lpImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 3, 0, 1, 1, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/ViewGroup$LayoutParams;", "<init>",
                        Arrays.asList("I", "I"), "V"))));
        lpImpl.addInstruction(new BuilderInstruction22c(Opcode.IGET, 0, 0,
                builder.internFieldReference(new ImmutableFieldReference(
                        "Landroid/view/ViewGroup$LayoutParams;", "width", "I"))));
        lpImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod lpDemo = builder.internMethod(HELLO, "lpDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), lpImpl);
        demos.put("lpDemo", lpDemo);

        // paintDemo(): Paint(ANTI_ALIAS_FLAG).setFlags(7).getFlags() is 7.
        MutableMethodImplementation paintImpl = new MutableMethodImplementation(2);
        paintImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/graphics/Paint;")));
        paintImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        paintImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 1));
        paintImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "<init>",
                        Collections.singletonList("I"), "V"))));
        paintImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 1, 7));
        paintImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "setFlags",
                        Collections.singletonList("I"), "V"))));
        paintImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "getFlags",
                        Collections.<String>emptyList(), "I"))));
        paintImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        paintImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod paintDemo = builder.internMethod(HELLO, "paintDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), paintImpl);
        demos.put("paintDemo", paintDemo);

        // textPaintDemo(): TextPaint flags round-trip through Paint getters.
        MutableMethodImplementation textPaintImpl = new MutableMethodImplementation(2);
        textPaintImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/text/TextPaint;")));
        textPaintImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        textPaintImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 1));
        textPaintImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/TextPaint;", "<init>",
                        Collections.singletonList("I"), "V"))));
        textPaintImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "getFlags",
                        Collections.<String>emptyList(), "I"))));
        textPaintImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        textPaintImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod textPaintDemo = builder.internMethod(HELLO, "textPaintDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), textPaintImpl);
        demos.put("textPaintDemo", textPaintDemo);

        // paintTextDemo(): text-measurement setters/getters round-trip.
        MutableMethodImplementation paintTextImpl = new MutableMethodImplementation(3);
        paintTextImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/graphics/Paint;")));
        paintTextImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        paintTextImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 0));
        paintTextImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "<init>",
                        Collections.singletonList("I"), "V"))));
        paintTextImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 1));
        paintTextImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "setHinting",
                        Collections.singletonList("I"), "V"))));
        paintTextImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "getHinting",
                        Collections.<String>emptyList(), "I"))));
        paintTextImpl.addInstruction(new BuilderInstruction21ih(Opcode.CONST_HIGH16, 1,
                0x40F00000));
        paintTextImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "setTextSize",
                        Collections.singletonList("F"), "V"))));
        paintTextImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "getTextSize",
                        Collections.<String>emptyList(), "F"))));
        paintTextImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 2, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "setTypeface",
                        Collections.singletonList("Landroid/graphics/Typeface;"),
                        "Landroid/graphics/Typeface;"))));
        paintTextImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "getTypeface",
                        Collections.<String>emptyList(), "Landroid/graphics/Typeface;"))));
        paintTextImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 2, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "setShader",
                        Collections.singletonList("Landroid/graphics/Shader;"),
                        "Landroid/graphics/Shader;"))));
        paintTextImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "getShader",
                        Collections.<String>emptyList(), "Landroid/graphics/Shader;"))));
        paintTextImpl.addInstruction(new BuilderInstruction21ih(Opcode.CONST_HIGH16, 1,
                0x40000000));
        paintTextImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "setTextScaleX",
                        Collections.singletonList("F"), "V"))));
        paintTextImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod paintTextDemo = builder.internMethod(HELLO, "paintTextDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), paintTextImpl);
        demos.put("paintTextDemo", paintTextDemo);

        // paintFullDemo(): the remaining text-state setters/getters run.
        MutableMethodImplementation paintFullImpl = new MutableMethodImplementation(5);
        paintFullImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/graphics/Paint;")));
        paintFullImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        paintFullImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 0));
        paintFullImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "<init>",
                        Collections.singletonList("I"), "V"))));
        paintFullImpl.addInstruction(new BuilderInstruction21ih(Opcode.CONST_HIGH16, 1,
                0x40000000));
        paintFullImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "setLetterSpacing",
                        Collections.singletonList("F"), "V"))));
        paintFullImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "getLetterSpacing",
                        Collections.<String>emptyList(), "F"))));
        paintFullImpl.addInstruction(new BuilderInstruction21ih(Opcode.CONST_HIGH16, 1,
                0x40400000));
        paintFullImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "setWordSpacing",
                        Collections.singletonList("F"), "V"))));
        paintFullImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "getWordSpacing",
                        Collections.<String>emptyList(), "F"))));
        paintFullImpl.addInstruction(new BuilderInstruction21ih(Opcode.CONST_HIGH16, 1,
                0x40800000));
        paintFullImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "setTextSkewX",
                        Collections.singletonList("F"), "V"))));
        paintFullImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "getTextSkewX",
                        Collections.<String>emptyList(), "F"))));
        String[] boolPairs = {
                "setAntiAlias:isAntiAlias", "setFilterBitmap:isFilterBitmap",
                "setDither:isDither", "setStrikeThruText:isStrikeThruText",
                "setUnderlineText:isUnderlineText", "setFakeBoldText:isFakeBoldText",
                "setElegantTextHeight:isElegantTextHeight",
        };
        for (String pair : boolPairs) {
            String[] names = pair.split(":");
            paintFullImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 1));
            paintFullImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0,
                    1, 0, 0, 0, builder.internMethodReference(new ImmutableMethodReference(
                            "Landroid/graphics/Paint;", names[0],
                            Collections.singletonList("Z"), "V"))));
            paintFullImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0,
                    0, 0, 0, builder.internMethodReference(new ImmutableMethodReference(
                            "Landroid/graphics/Paint;", names[1],
                            Collections.<String>emptyList(), "Z"))));
        }
        paintFullImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 0, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/Locale;", "getDefault",
                        Collections.<String>emptyList(), "Ljava/util/Locale;"))));
        paintFullImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        paintFullImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "setTextLocale",
                        Collections.singletonList("Ljava/util/Locale;"), "V"))));
        paintFullImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "getTextLocale",
                        Collections.<String>emptyList(), "Ljava/util/Locale;"))));
        paintFullImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 0, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/os/LocaleList;", "getDefault",
                        Collections.<String>emptyList(), "Landroid/os/LocaleList;"))));
        paintFullImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        paintFullImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "setTextLocales",
                        Collections.singletonList("Landroid/os/LocaleList;"), "V"))));
        paintFullImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "getTextLocales",
                        Collections.<String>emptyList(), "Landroid/os/LocaleList;"))));
        paintFullImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("smcp")));
        paintFullImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "setFontFeatureSettings",
                        Collections.singletonList("Ljava/lang/String;"), "V"))));
        paintFullImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "getFontFeatureSettings",
                        Collections.<String>emptyList(), "Ljava/lang/String;"))));
        paintFullImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 2, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "setColorFilter",
                        Collections.singletonList("Landroid/graphics/ColorFilter;"),
                        "Landroid/graphics/ColorFilter;"))));
        paintFullImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "getColorFilter",
                        Collections.<String>emptyList(),
                        "Landroid/graphics/ColorFilter;"))));
        paintFullImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "clearColorFilter",
                        Collections.<String>emptyList(), "V"))));
        paintFullImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 2, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "setBlendMode",
                        Collections.singletonList("Landroid/graphics/BlendMode;"), "V"))));
        paintFullImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "getBlendMode",
                        Collections.<String>emptyList(), "Landroid/graphics/BlendMode;"))));
        paintFullImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 1, 200));
        paintFullImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "setAlpha",
                        Collections.singletonList("I"), "V"))));
        paintFullImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "getAlpha",
                        Collections.<String>emptyList(), "I"))));
        paintFullImpl.addInstruction(new BuilderInstruction21ih(Opcode.CONST_HIGH16, 1,
                0x40000000));
        paintFullImpl.addInstruction(new BuilderInstruction21ih(Opcode.CONST_HIGH16, 2,
                0x40400000));
        paintFullImpl.addInstruction(new BuilderInstruction21ih(Opcode.CONST_HIGH16, 3,
                0x40800000));
        paintFullImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 4, 5));
        paintFullImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 5, 0, 1, 2,
                3, 4, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "setShadowLayer",
                        Arrays.asList("F", "F", "F", "I"), "V"))));
        paintFullImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "clearShadowLayer",
                        Collections.<String>emptyList(), "V"))));
        paintFullImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "hasShadowLayer",
                        Collections.<String>emptyList(), "Z"))));
        paintFullImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod paintFullDemo = builder.internMethod(HELLO, "paintFullDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), paintFullImpl);
        demos.put("paintFullDemo", paintFullDemo);

        // typefaceDemo(): DEFAULT materializes stably with its family name.
        MutableMethodImplementation typefaceImpl = new MutableMethodImplementation(1);
        typefaceImpl.addInstruction(new BuilderInstruction21c(Opcode.SGET_OBJECT, 0,
                builder.internFieldReference(new ImmutableFieldReference(
                        "Landroid/graphics/Typeface;", "DEFAULT",
                        "Landroid/graphics/Typeface;"))));
        typefaceImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod typefaceDemo = builder.internMethod(HELLO, "typefaceDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), typefaceImpl);
        demos.put("typefaceDemo", typefaceDemo);

        // heuristicDemo(): LTR materializes stably with its heuristic name.
        MutableMethodImplementation heuristicImpl = new MutableMethodImplementation(1);
        heuristicImpl.addInstruction(new BuilderInstruction21c(Opcode.SGET_OBJECT, 0,
                builder.internFieldReference(new ImmutableFieldReference(
                        "Landroid/text/TextDirectionHeuristics;", "LTR",
                        "Landroid/text/TextDirectionHeuristic;"))));
        heuristicImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod heuristicDemo = builder.internMethod(HELLO, "heuristicDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), heuristicImpl);
        demos.put("heuristicDemo", heuristicDemo);

        // boringDemo(): boringness is never proven headless (full layout).
        MutableMethodImplementation boringImpl = new MutableMethodImplementation(5);
        boringImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 0,
                builder.internStringReference("hello")));
        boringImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Landroid/text/TextPaint;")));
        boringImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        boringImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 0));
        boringImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 1, 2, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/TextPaint;", "<init>",
                        Collections.singletonList("I"), "V"))));
        boringImpl.addInstruction(new BuilderInstruction21c(Opcode.SGET_OBJECT, 2,
                builder.internFieldReference(new ImmutableFieldReference(
                        "Landroid/text/TextDirectionHeuristics;", "LTR",
                        "Landroid/text/TextDirectionHeuristic;"))));
        boringImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 3, 0));
        boringImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 5, 0, 1, 2, 3,
                4, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/BoringLayout;", "isBoring",
                        Arrays.asList("Ljava/lang/CharSequence;", "Landroid/text/TextPaint;",
                                "Landroid/text/TextDirectionHeuristic;", "Z",
                                "Landroid/text/BoringLayout$Metrics;"),
                        "Landroid/text/BoringLayout$Metrics;"))));
        boringImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        boringImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod boringDemo = builder.internMethod(HELLO, "boringDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), boringImpl);
        demos.put("boringDemo", boringDemo);

        // enumValuesDemo(): framework enum values() lists every constant.
        MutableMethodImplementation enumValuesImpl = new MutableMethodImplementation(1);
        enumValuesImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 0, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint$Style;", "values",
                        Collections.<String>emptyList(),
                        "[Landroid/graphics/Paint$Style;"))));
        enumValuesImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        enumValuesImpl.addInstruction(new BuilderInstruction12x(Opcode.ARRAY_LENGTH, 0, 0));
        enumValuesImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod enumValuesDemo = builder.internMethod(HELLO, "enumValuesDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), enumValuesImpl);
        demos.put("enumValuesDemo", enumValuesDemo);

        // obtainDemo(): StaticLayout builders record their inputs.
        MutableMethodImplementation obtainImpl = new MutableMethodImplementation(5);
        obtainImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 0,
                builder.internStringReference("hello")));
        obtainImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Landroid/text/TextPaint;")));
        obtainImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        obtainImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 0));
        obtainImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 1, 2, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/TextPaint;", "<init>",
                        Collections.singletonList("I"), "V"))));
        obtainImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 3, 5));
        obtainImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 4, 100));
        obtainImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 5, 0, 2, 3, 1,
                4, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/StaticLayout$Builder;", "obtain",
                        Arrays.asList("Ljava/lang/CharSequence;", "I", "I",
                                "Landroid/text/TextPaint;", "I"),
                        "Landroid/text/StaticLayout$Builder;"))));
        obtainImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        obtainImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod obtainDemo = builder.internMethod(HELLO, "obtainDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), obtainImpl);
        demos.put("obtainDemo", obtainDemo);

        // directionDemo(): text direction records and chains the builder.
        MutableMethodImplementation directionImpl = new MutableMethodImplementation(2);
        directionImpl.addInstruction(new BuilderInstruction21c(Opcode.SGET_OBJECT, 0,
                builder.internFieldReference(new ImmutableFieldReference(
                        "Landroid/text/TextDirectionHeuristics;", "LTR",
                        "Landroid/text/TextDirectionHeuristic;"))));
        directionImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Landroid/text/StaticLayout$Builder;")));
        directionImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        directionImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 1, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/StaticLayout$Builder;", "setTextDirection",
                        Collections.singletonList("Landroid/text/TextDirectionHeuristic;"),
                        "Landroid/text/StaticLayout$Builder;"))));
        directionImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        directionImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 1));
        BuilderMethod directionDemo = builder.internMethod(HELLO, "directionDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), directionImpl);
        demos.put("directionDemo", directionDemo);

        // buildDemo(): "hello world test" at 8px in width 30 breaks in 3.
        MutableMethodImplementation buildLayoutImpl = new MutableMethodImplementation(5);
        buildLayoutImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 0,
                builder.internStringReference("hello world test")));
        buildLayoutImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Landroid/text/TextPaint;")));
        buildLayoutImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        buildLayoutImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 0));
        buildLayoutImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 1, 2, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/TextPaint;", "<init>",
                        Collections.singletonList("I"), "V"))));
        buildLayoutImpl.addInstruction(new BuilderInstruction21ih(Opcode.CONST_HIGH16, 2,
                0x41000000));
        buildLayoutImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 1, 2, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "setTextSize",
                        Collections.singletonList("F"), "V"))));
        buildLayoutImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 0));
        buildLayoutImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 3, 16));
        buildLayoutImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 4, 30));
        buildLayoutImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 5, 0, 2, 3,
                1, 4, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/StaticLayout$Builder;", "obtain",
                        Arrays.asList("Ljava/lang/CharSequence;", "I", "I",
                                "Landroid/text/TextPaint;", "I"),
                        "Landroid/text/StaticLayout$Builder;"))));
        buildLayoutImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        buildLayoutImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/StaticLayout$Builder;", "build",
                        Collections.<String>emptyList(), "Landroid/text/StaticLayout;"))));
        buildLayoutImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        buildLayoutImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod buildLayoutDemo = builder.internMethod(HELLO, "buildLayoutDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), buildLayoutImpl);
        demos.put("buildLayoutDemo", buildLayoutDemo);

        // layoutGetDemo(): layout getters read the broken lines.
        MutableMethodImplementation layoutGetImpl = new MutableMethodImplementation(5);
        layoutGetImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 0,
                builder.internStringReference("hello world test")));
        layoutGetImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Landroid/text/TextPaint;")));
        layoutGetImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        layoutGetImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 0));
        layoutGetImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 1, 2, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/TextPaint;", "<init>",
                        Collections.singletonList("I"), "V"))));
        layoutGetImpl.addInstruction(new BuilderInstruction21ih(Opcode.CONST_HIGH16, 2,
                0x41000000));
        layoutGetImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 1, 2, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "setTextSize",
                        Collections.singletonList("F"), "V"))));
        layoutGetImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 0));
        layoutGetImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 3, 16));
        layoutGetImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 4, 30));
        layoutGetImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 5, 0, 2, 3,
                1, 4, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/StaticLayout$Builder;", "obtain",
                        Arrays.asList("Ljava/lang/CharSequence;", "I", "I",
                                "Landroid/text/TextPaint;", "I"),
                        "Landroid/text/StaticLayout$Builder;"))));
        layoutGetImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        layoutGetImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/StaticLayout$Builder;", "build",
                        Collections.<String>emptyList(), "Landroid/text/StaticLayout;"))));
        layoutGetImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        layoutGetImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 2, 18));
        layoutGetImpl.addInstruction(new BuilderInstruction22c(Opcode.NEW_ARRAY, 1, 2,
                builder.internTypeReference("[Ljava/lang/Object;")));
        layoutGetImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/Layout;", "getLineCount",
                        Collections.<String>emptyList(), "I"))));
        layoutGetImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        layoutGetImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 3, 0));
        layoutGetImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT_OBJECT, 2, 1, 3));
        layoutGetImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/Layout;", "getHeight",
                        Collections.<String>emptyList(), "I"))));
        layoutGetImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        layoutGetImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 3, 1));
        layoutGetImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT_OBJECT, 2, 1, 3));
        layoutGetImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/Layout;", "getWidth",
                        Collections.<String>emptyList(), "I"))));
        layoutGetImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        layoutGetImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 3, 2));
        layoutGetImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT_OBJECT, 2, 1, 3));
        layoutGetImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 1));
        layoutGetImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 2, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/Layout;", "getLineStart",
                        Collections.singletonList("I"), "I"))));
        layoutGetImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        layoutGetImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 3, 3));
        layoutGetImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT_OBJECT, 2, 1, 3));
        layoutGetImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 1));
        layoutGetImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 2, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/Layout;", "getLineEnd",
                        Collections.singletonList("I"), "I"))));
        layoutGetImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        layoutGetImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 3, 4));
        layoutGetImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT_OBJECT, 2, 1, 3));
        layoutGetImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 1));
        layoutGetImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 2, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/Layout;", "getLineTop",
                        Collections.singletonList("I"), "I"))));
        layoutGetImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        layoutGetImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 3, 5));
        layoutGetImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT_OBJECT, 2, 1, 3));
        layoutGetImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 1));
        layoutGetImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 2, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/Layout;", "getLineBottom",
                        Collections.singletonList("I"), "I"))));
        layoutGetImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        layoutGetImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 3, 6));
        layoutGetImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT_OBJECT, 2, 1, 3));
        layoutGetImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 1));
        layoutGetImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 2, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/Layout;", "getLineLeft",
                        Collections.singletonList("I"), "F"))));
        layoutGetImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        layoutGetImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 3, 7));
        layoutGetImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT_OBJECT, 2, 1, 3));
        layoutGetImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 1));
        layoutGetImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 2, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/Layout;", "getLineRight",
                        Collections.singletonList("I"), "F"))));
        layoutGetImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        layoutGetImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 3, 8));
        layoutGetImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT_OBJECT, 2, 1, 3));
        layoutGetImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 1));
        layoutGetImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 2, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/Layout;", "getEllipsisCount",
                        Collections.singletonList("I"), "I"))));
        layoutGetImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        layoutGetImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 3, 9));
        layoutGetImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT_OBJECT, 2, 1, 3));
        layoutGetImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 0));
        layoutGetImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 2, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/Layout;", "getParagraphDirection",
                        Collections.singletonList("I"), "I"))));
        layoutGetImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        layoutGetImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 3, 10));
        layoutGetImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT_OBJECT, 2, 1, 3));
        layoutGetImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 2, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/Layout;", "getLineWidth",
                        Collections.singletonList("I"), "F"))));
        layoutGetImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        layoutGetImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 3, 11));
        layoutGetImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT_OBJECT, 2, 1, 3));
        layoutGetImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/StaticLayout;", "isFallbackLineSpacingEnabled",
                        Collections.<String>emptyList(), "Z"))));
        layoutGetImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        layoutGetImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 3, 12));
        layoutGetImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT_OBJECT, 2, 1, 3));
        layoutGetImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/Layout;", "getText",
                        Collections.<String>emptyList(), "Ljava/lang/CharSequence;"))));
        layoutGetImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 2));
        layoutGetImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 3, 13));
        layoutGetImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT_OBJECT, 2, 1, 3));
        layoutGetImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/Layout;", "getPaint",
                        Collections.<String>emptyList(), "Landroid/text/TextPaint;"))));
        layoutGetImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 2));
        layoutGetImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 3, 14));
        layoutGetImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT_OBJECT, 2, 1, 3));
        layoutGetImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 1));
        layoutGetImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 2, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/Layout;", "getLineBaseline",
                        Collections.singletonList("I"), "I"))));
        layoutGetImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        layoutGetImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 3, 15));
        layoutGetImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT_OBJECT, 2, 1, 3));
        layoutGetImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 2, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/Layout;", "getLineAscent",
                        Collections.singletonList("I"), "I"))));
        layoutGetImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        layoutGetImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 3, 16));
        layoutGetImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT_OBJECT, 2, 1, 3));
        layoutGetImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 2, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/Layout;", "getLineDescent",
                        Collections.singletonList("I"), "I"))));
        layoutGetImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        layoutGetImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 3, 17));
        layoutGetImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT_OBJECT, 2, 1, 3));
        layoutGetImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 1));
        BuilderMethod layoutGetDemo = builder.internMethod(HELLO, "layoutGetDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), layoutGetImpl);
        demos.put("layoutGetDemo", layoutGetDemo);

        // builderOptsDemo(): the remaining builder options record and chain.
        MutableMethodImplementation builderOptsImpl = new MutableMethodImplementation(5);
        builderOptsImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/text/StaticLayout$Builder;")));
        builderOptsImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        builderOptsImpl.addInstruction(new BuilderInstruction21c(Opcode.SGET_OBJECT, 1,
                builder.internFieldReference(new ImmutableFieldReference(
                        "Landroid/text/Layout$Alignment;", "ALIGN_NORMAL",
                        "Landroid/text/Layout$Alignment;"))));
        builderOptsImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/StaticLayout$Builder;", "setAlignment",
                        Collections.singletonList("Landroid/text/Layout$Alignment;"),
                        "Landroid/text/StaticLayout$Builder;"))));
        builderOptsImpl.addInstruction(new BuilderInstruction21ih(Opcode.CONST_HIGH16, 1,
                0x3FC00000));
        builderOptsImpl.addInstruction(new BuilderInstruction21ih(Opcode.CONST_HIGH16, 2,
                0x40200000));
        builderOptsImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 3, 0, 1, 2,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/StaticLayout$Builder;", "setLineSpacing",
                        Arrays.asList("F", "F"),
                        "Landroid/text/StaticLayout$Builder;"))));
        builderOptsImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 1));
        builderOptsImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/StaticLayout$Builder;", "setIncludePad",
                        Collections.singletonList("Z"),
                        "Landroid/text/StaticLayout$Builder;"))));
        builderOptsImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 3, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/StaticLayout$Builder;", "setEllipsize",
                        Collections.singletonList("Landroid/text/TextUtils$TruncateAt;"),
                        "Landroid/text/StaticLayout$Builder;"))));
        builderOptsImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 1, 100));
        builderOptsImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/StaticLayout$Builder;", "setEllipsizedWidth",
                        Collections.singletonList("I"),
                        "Landroid/text/StaticLayout$Builder;"))));
        builderOptsImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 3));
        builderOptsImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/StaticLayout$Builder;", "setMaxLines",
                        Collections.singletonList("I"),
                        "Landroid/text/StaticLayout$Builder;"))));
        builderOptsImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 1));
        builderOptsImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/StaticLayout$Builder;", "setBreakStrategy",
                        Collections.singletonList("I"),
                        "Landroid/text/StaticLayout$Builder;"))));
        builderOptsImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 2));
        builderOptsImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/StaticLayout$Builder;", "setHyphenationFrequency",
                        Collections.singletonList("I"),
                        "Landroid/text/StaticLayout$Builder;"))));
        builderOptsImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 1));
        builderOptsImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/StaticLayout$Builder;", "setJustificationMode",
                        Collections.singletonList("I"),
                        "Landroid/text/StaticLayout$Builder;"))));
        builderOptsImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 4, 2));
        builderOptsImpl.addInstruction(new BuilderInstruction22c(Opcode.NEW_ARRAY, 2, 4,
                builder.internTypeReference("[I")));
        builderOptsImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 4, 0));
        builderOptsImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 3, 30));
        builderOptsImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT, 3, 2, 4));
        builderOptsImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 4, 1));
        builderOptsImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 3, 40));
        builderOptsImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT, 3, 2, 4));
        builderOptsImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 4, 2));
        builderOptsImpl.addInstruction(new BuilderInstruction22c(Opcode.NEW_ARRAY, 1, 4,
                builder.internTypeReference("[I")));
        builderOptsImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 4, 0));
        builderOptsImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 3, 10));
        builderOptsImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT, 3, 1, 4));
        builderOptsImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 4, 1));
        builderOptsImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 3, 20));
        builderOptsImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT, 3, 1, 4));
        builderOptsImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 3, 0, 1, 2,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/StaticLayout$Builder;", "setIndents",
                        Arrays.asList("[I", "[I"),
                        "Landroid/text/StaticLayout$Builder;"))));
        builderOptsImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 1));
        builderOptsImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/StaticLayout$Builder;", "setUseLineSpacingFromFallbacks",
                        Collections.singletonList("Z"),
                        "Landroid/text/StaticLayout$Builder;"))));
        builderOptsImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod builderOptsDemo = builder.internMethod(HELLO, "builderOptsDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), builderOptsImpl);
        demos.put("builderOptsDemo", builderOptsDemo);

        // lineBreakDemo(): break options record and chain the builder.
        MutableMethodImplementation lineBreakImpl = new MutableMethodImplementation(2);
        lineBreakImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/graphics/text/LineBreakConfig$Builder;")));
        lineBreakImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        lineBreakImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 1));
        lineBreakImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/text/LineBreakConfig$Builder;", "setLineBreakStyle",
                        Collections.singletonList("I"),
                        "Landroid/graphics/text/LineBreakConfig$Builder;"))));
        lineBreakImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 2));
        lineBreakImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/text/LineBreakConfig$Builder;", "setLineBreakWordStyle",
                        Collections.singletonList("I"),
                        "Landroid/graphics/text/LineBreakConfig$Builder;"))));
        lineBreakImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 3));
        lineBreakImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/text/LineBreakConfig$Builder;", "setHyphenation",
                        Collections.singletonList("I"),
                        "Landroid/graphics/text/LineBreakConfig$Builder;"))));
        lineBreakImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/text/LineBreakConfig$Builder;", "build",
                        Collections.<String>emptyList(),
                        "Landroid/graphics/text/LineBreakConfig;"))));
        lineBreakImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        lineBreakImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Landroid/text/StaticLayout$Builder;")));
        lineBreakImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        lineBreakImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 1, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/StaticLayout$Builder;", "setLineBreakConfig",
                        Collections.singletonList("Landroid/graphics/text/LineBreakConfig;"),
                        "Landroid/text/StaticLayout$Builder;"))));
        lineBreakImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        lineBreakImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 1));
        BuilderMethod lineBreakDemo = builder.internMethod(HELLO, "lineBreakDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), lineBreakImpl);
        demos.put("lineBreakDemo", lineBreakDemo);

        // desiredDemo(): 5 chars at 8px, scale 2, spacing 1: 5 * (4 * 2 + 1).
        MutableMethodImplementation desiredImpl = new MutableMethodImplementation(4);
        desiredImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 0,
                builder.internStringReference("hello")));
        desiredImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Landroid/text/TextPaint;")));
        desiredImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        desiredImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 0));
        desiredImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 1, 2, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/TextPaint;", "<init>",
                        Collections.singletonList("I"), "V"))));
        desiredImpl.addInstruction(new BuilderInstruction21ih(Opcode.CONST_HIGH16, 2,
                0x41000000));
        desiredImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 1, 2, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "setTextSize",
                        Collections.singletonList("F"), "V"))));
        desiredImpl.addInstruction(new BuilderInstruction21ih(Opcode.CONST_HIGH16, 2,
                0x40000000));
        desiredImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 1, 2, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "setTextScaleX",
                        Collections.singletonList("F"), "V"))));
        desiredImpl.addInstruction(new BuilderInstruction21ih(Opcode.CONST_HIGH16, 2,
                0x3F800000));
        desiredImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 1, 2, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "setLetterSpacing",
                        Collections.singletonList("F"), "V"))));
        desiredImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 0));
        desiredImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 3, 5));
        desiredImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 4, 0, 2, 3, 1,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/Layout;", "getDesiredWidth",
                        Arrays.asList("Ljava/lang/CharSequence;", "I", "I",
                                "Landroid/text/TextPaint;"),
                        "F"))));
        desiredImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        desiredImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod desiredDemo = builder.internMethod(HELLO, "desiredDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "F",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), desiredImpl);
        demos.put("desiredDemo", desiredDemo);

        // desiredSpanDemo(): same measure through a SpannableString wrapper.
        MutableMethodImplementation desiredSpanImpl = new MutableMethodImplementation(4);
        desiredSpanImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 0,
                builder.internStringReference("hello")));
        desiredSpanImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Landroid/text/SpannableString;")));
        desiredSpanImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        desiredSpanImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 1, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/SpannableString;", "<init>",
                        Collections.singletonList("Ljava/lang/CharSequence;"), "V"))));
        desiredSpanImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 2,
                builder.internTypeReference("Landroid/text/TextPaint;")));
        desiredSpanImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 2));
        desiredSpanImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 3, 0));
        desiredSpanImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 2, 3, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/TextPaint;", "<init>",
                        Collections.singletonList("I"), "V"))));
        desiredSpanImpl.addInstruction(new BuilderInstruction21ih(Opcode.CONST_HIGH16, 3,
                0x41000000));
        desiredSpanImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 2, 3, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "setTextSize",
                        Collections.singletonList("F"), "V"))));
        desiredSpanImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 3, 0));
        desiredSpanImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 0, 5));
        desiredSpanImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 4, 1, 3, 0,
                2, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/Layout;", "getDesiredWidth",
                        Arrays.asList("Ljava/lang/CharSequence;", "I", "I",
                                "Landroid/text/TextPaint;"),
                        "F"))));
        desiredSpanImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        desiredSpanImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod desiredSpanDemo = builder.internMethod(HELLO, "desiredSpanDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "F",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), desiredSpanImpl);
        demos.put("desiredSpanDemo", desiredSpanDemo);

        // spannableDemo(): wrap, span [1,4), query, read, transitions:
        // 5 * 100 + 1 * 10 + 101 + 1 * 1000 + 4 * 10000.
        MutableMethodImplementation spannableImpl = new MutableMethodImplementation(6);
        spannableImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 0,
                builder.internStringReference("hello")));
        spannableImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Landroid/text/SpannableString;")));
        spannableImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        spannableImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 1, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/SpannableString;", "<init>",
                        Collections.singletonList("Ljava/lang/CharSequence;"), "V"))));
        spannableImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 0,
                builder.internStringReference("b")));
        spannableImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 1));
        spannableImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 3, 4));
        spannableImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 4, 33));
        spannableImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 5, 1, 0, 2,
                3, 4, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/SpannableString;", "setSpan",
                        Arrays.asList("Ljava/lang/Object;", "I", "I", "I"), "V"))));
        spannableImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_CLASS, 0,
                builder.internTypeReference("Ljava/lang/String;")));
        spannableImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 0));
        spannableImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 3, 5));
        spannableImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 4, 1, 2, 3,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/SpannableString;", "getSpans",
                        Arrays.asList("I", "I", "Ljava/lang/Class;"),
                        "[Ljava/lang/Object;"))));
        spannableImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        spannableImpl.addInstruction(new BuilderInstruction12x(Opcode.ARRAY_LENGTH, 4, 0));
        spannableImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 1, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/SpannableString;", "length",
                        Collections.<String>emptyList(), "I"))));
        spannableImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        spannableImpl.addInstruction(new BuilderInstruction22b(Opcode.MUL_INT_LIT8, 0, 0, 100));
        spannableImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_INTERFACE, 1, 1, 0,
                0, 0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/CharSequence;", "length",
                        Collections.<String>emptyList(), "I"))));
        spannableImpl.addInstruction(new BuilderInstruction22b(Opcode.MUL_INT_LIT8, 4, 4, 10));
        spannableImpl.addInstruction(new BuilderInstruction23x(Opcode.ADD_INT, 0, 0, 4));
        spannableImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 1));
        spannableImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 1, 2, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/SpannableString;", "charAt",
                        Collections.singletonList("I"), "C"))));
        spannableImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 3));
        spannableImpl.addInstruction(new BuilderInstruction23x(Opcode.ADD_INT, 0, 0, 3));
        spannableImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_CLASS, 2,
                builder.internTypeReference("Ljava/lang/String;")));
        spannableImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 3, 0));
        spannableImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 4, 5));
        spannableImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_INTERFACE, 4, 1, 3,
                4, 2, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/Spanned;", "nextSpanTransition",
                        Arrays.asList("I", "I", "Ljava/lang/Class;"), "I"))));
        spannableImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 3));
        spannableImpl.addInstruction(new BuilderInstruction22b(Opcode.MUL_INT_LIT8, 3, 3, 10));
        spannableImpl.addInstruction(new BuilderInstruction22b(Opcode.MUL_INT_LIT8, 3, 3, 100));
        spannableImpl.addInstruction(new BuilderInstruction23x(Opcode.ADD_INT, 0, 0, 3));
        spannableImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 4, 2));
        spannableImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_CLASS, 2,
                builder.internTypeReference("Ljava/lang/String;")));
        spannableImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 5, 5));
        spannableImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_INTERFACE, 4, 1, 4,
                5, 2, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/text/Spanned;", "nextSpanTransition",
                        Arrays.asList("I", "I", "Ljava/lang/Class;"), "I"))));
        spannableImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 4));
        spannableImpl.addInstruction(new BuilderInstruction22b(Opcode.MUL_INT_LIT8, 4, 4, 100));
        spannableImpl.addInstruction(new BuilderInstruction22b(Opcode.MUL_INT_LIT8, 4, 4, 100));
        spannableImpl.addInstruction(new BuilderInstruction23x(Opcode.ADD_INT, 0, 0, 4));
        spannableImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod spannableDemo = builder.internMethod(HELLO, "spannableDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), spannableImpl);
        demos.put("spannableDemo", spannableDemo);

        // colorDemo(): Paint.setColor/getColor round-trips an ARGB int.
        MutableMethodImplementation colorImpl = new MutableMethodImplementation(2);
        colorImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/graphics/Paint;")));
        colorImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        colorImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 1));
        colorImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "<init>",
                        Collections.singletonList("I"), "V"))));
        colorImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 1, 0x0102));
        colorImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "setColor",
                        Collections.singletonList("I"), "V"))));
        colorImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "getColor",
                        Collections.<String>emptyList(), "I"))));
        colorImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        colorImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod colorDemo = builder.internMethod(HELLO, "colorDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), colorImpl);
        demos.put("colorDemo", colorDemo);

        // strokeDemo(): Paint.setStrokeWidth/getStrokeWidth round-trips 2.0f.
        MutableMethodImplementation strokeImpl = new MutableMethodImplementation(2);
        strokeImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/graphics/Paint;")));
        strokeImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        strokeImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 0));
        strokeImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "<init>",
                        Collections.singletonList("I"), "V"))));
        strokeImpl.addInstruction(
                new BuilderInstruction21ih(Opcode.CONST_HIGH16, 1, 0x40000000));
        strokeImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "setStrokeWidth",
                        Collections.singletonList("F"), "V"))));
        strokeImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "getStrokeWidth",
                        Collections.<String>emptyList(), "F"))));
        strokeImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        strokeImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod strokeDemo = builder.internMethod(HELLO, "strokeDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "F",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), strokeImpl);
        demos.put("strokeDemo", strokeDemo);

        // styleRoundDemo(): setStyle(STROKE).getStyle().ordinal() is 1.
        MutableMethodImplementation styleRoundImpl = new MutableMethodImplementation(2);
        styleRoundImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/graphics/Paint;")));
        styleRoundImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        styleRoundImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 0));
        styleRoundImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "<init>",
                        Collections.singletonList("I"), "V"))));
        styleRoundImpl.addInstruction(new BuilderInstruction21c(Opcode.SGET_OBJECT, 1,
                builder.internFieldReference(new ImmutableFieldReference(
                        "Landroid/graphics/Paint$Style;", "STROKE",
                        "Landroid/graphics/Paint$Style;"))));
        styleRoundImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "setStyle",
                        Collections.singletonList("Landroid/graphics/Paint$Style;"), "V"))));
        styleRoundImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/graphics/Paint;", "getStyle",
                        Collections.<String>emptyList(),
                        "Landroid/graphics/Paint$Style;"))));
        styleRoundImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        styleRoundImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Enum;", "ordinal",
                        Collections.<String>emptyList(), "I"))));
        styleRoundImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        styleRoundImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod styleRoundDemo = builder.internMethod(HELLO, "styleRoundDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), styleRoundImpl);
        demos.put("styleRoundDemo", styleRoundDemo);

        // styleDemo(): framework enum constant reports its ordinal (STROKE=1).
        MutableMethodImplementation styleImpl = new MutableMethodImplementation(1);
        styleImpl.addInstruction(new BuilderInstruction21c(Opcode.SGET_OBJECT, 0,
                builder.internFieldReference(new ImmutableFieldReference(
                        "Landroid/graphics/Paint$Style;", "STROKE",
                        "Landroid/graphics/Paint$Style;"))));
        styleImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Enum;", "ordinal",
                        Collections.<String>emptyList(), "I"))));
        styleImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        styleImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod styleDemo = builder.internMethod(HELLO, "styleDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), styleImpl);
        demos.put("styleDemo", styleDemo);

        // nanosDemo(): host enum constant keeps real behavior (60s to nanos).
        MutableMethodImplementation nanosImpl = new MutableMethodImplementation(3);
        nanosImpl.addInstruction(new BuilderInstruction21c(Opcode.SGET_OBJECT, 0,
                builder.internFieldReference(new ImmutableFieldReference(
                        "Ljava/util/concurrent/TimeUnit;", "SECONDS",
                        "Ljava/util/concurrent/TimeUnit;"))));
        nanosImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_WIDE_16, 1, 60));
        nanosImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/concurrent/TimeUnit;", "toNanos",
                        Collections.singletonList("J"), "J"))));
        nanosImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_WIDE, 0));
        nanosImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_WIDE, 0));
        BuilderMethod nanosDemo = builder.internMethod(HELLO, "nanosDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "J",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), nanosImpl);
        demos.put("nanosDemo", nanosDemo);

        // contentGroup(): findViewById(android.R.id.content) resolves the frame.
        MutableMethodImplementation contentGroupImpl = new MutableMethodImplementation(2);
        contentGroupImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        contentGroupImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        contentGroupImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        contentGroupImpl.addInstruction(new BuilderInstruction31i(Opcode.CONST, 1, 0x1020002));
        contentGroupImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "findViewById",
                        Collections.singletonList("I"), "Landroid/view/View;"))));
        contentGroupImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        contentGroupImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod contentGroup = builder.internMethod(HELLO, "contentGroup",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), contentGroupImpl);
        demos.put("contentGroup", contentGroup);

        // missFind(): any other id misses headless (no inflated hierarchy).
        MutableMethodImplementation missFindImpl = new MutableMethodImplementation(2);
        missFindImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        missFindImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        missFindImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        missFindImpl.addInstruction(new BuilderInstruction31i(Opcode.CONST, 1, 0x7f010203));
        missFindImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "findViewById",
                        Collections.singletonList("I"), "Landroid/view/View;"))));
        missFindImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        missFindImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod missFind = builder.internMethod(HELLO, "missFind",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), missFindImpl);
        demos.put("missFind", missFind);

        // contentChild(): the headless content frame starts childless.
        // Device getChildAt throws when out of range, so the probe counts
        // instead of poking index 0.
        MutableMethodImplementation contentChildImpl = new MutableMethodImplementation(2);
        contentChildImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        contentChildImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        contentChildImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        contentChildImpl.addInstruction(new BuilderInstruction31i(Opcode.CONST, 1, 0x1020002));
        contentChildImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "findViewById",
                        Collections.singletonList("I"), "Landroid/view/View;"))));
        contentChildImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        contentChildImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/ViewGroup;", "getChildCount",
                        Collections.<String>emptyList(), "I"))));
        contentChildImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        contentChildImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod contentChild = builder.internMethod(HELLO, "contentChild",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), contentChildImpl);
        demos.put("contentChild", contentChild);

        // viewCtx(): ViewGroup(Context).getContext() keeps the context.
        MutableMethodImplementation viewCtxImpl = new MutableMethodImplementation(3);
        viewCtxImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/ViewGroup;")));
        viewCtxImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        viewCtxImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Landroid/app/Activity;")));
        viewCtxImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        viewCtxImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 1, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        viewCtxImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 0));
        viewCtxImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 4, 0, 1, 2, 2,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/ViewGroup;", "<init>",
                        Arrays.asList("Landroid/content/Context;",
                                "Landroid/util/AttributeSet;", "I"),
                        "V"))));
        viewCtxImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "getContext",
                        Collections.<String>emptyList(), "Landroid/content/Context;"))));
        viewCtxImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        viewCtxImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod viewCtx = builder.internMethod(HELLO, "viewCtx",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), viewCtxImpl);
        demos.put("viewCtx", viewCtx);

        // clipDemo(): clip setters are no-ops that keep the view flowing.
        MutableMethodImplementation clipImpl = new MutableMethodImplementation(2);
        clipImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        clipImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        clipImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        clipImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 0));
        clipImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "setClipChildren",
                        Collections.singletonList("Z"), "V"))));
        clipImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "setClipToPadding",
                        Collections.singletonList("Z"), "V"))));
        clipImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod clipDemo = builder.internMethod(HELLO, "clipDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), clipImpl);
        demos.put("clipDemo", clipDemo);
        return demos;
    }

    private static Map<String, BuilderMethod> writeViewDemos(DexBuilder builder, BuilderMethodReference objectInit) {
        Map<String, BuilderMethod> demos = new HashMap<>();
        // attachDemo(): attach-listener registration is a silent no-op.
        MutableMethodImplementation attachImpl = new MutableMethodImplementation(2);
        attachImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        attachImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        attachImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        attachImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 0));
        attachImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "addOnAttachStateChangeListener",
                        Collections.singletonList(
                                "Landroid/view/View$OnAttachStateChangeListener;"),
                        "V"))));
        attachImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod attachDemo = builder.internMethod(HELLO, "attachDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), attachImpl);
        demos.put("attachDemo", attachDemo);

        // tagDemo(): setTag/getTag round-trips an object by id.
        MutableMethodImplementation tagImpl = new MutableMethodImplementation(3);
        tagImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        tagImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        tagImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        tagImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("seven")));
        tagImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 7));
        tagImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 3, 0, 2, 1, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "setTag",
                        Arrays.asList("I", "Ljava/lang/Object;"), "V"))));
        tagImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 2, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "getTag",
                        Collections.singletonList("I"), "Ljava/lang/Object;"))));
        tagImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        tagImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod tagDemo = builder.internMethod(HELLO, "tagDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), tagImpl);
        demos.put("tagDemo", tagDemo);

        // tagMiss(): unknown tag ids stay null.
        MutableMethodImplementation tagMissImpl = new MutableMethodImplementation(2);
        tagMissImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        tagMissImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        tagMissImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        tagMissImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 3));
        tagMissImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "getTag",
                        Collections.singletonList("I"), "Ljava/lang/Object;"))));
        tagMissImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        tagMissImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod tagMiss = builder.internMethod(HELLO, "tagMiss",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), tagMissImpl);
        demos.put("tagMiss", tagMiss);

        // attachedDemo(): headless views report detached.
        MutableMethodImplementation attachedImpl = new MutableMethodImplementation(1);
        attachedImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        attachedImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        attachedImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        attachedImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "isAttachedToWindow",
                        Collections.<String>emptyList(), "Z"))));
        attachedImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        attachedImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod attachedDemo = builder.internMethod(HELLO, "attachedDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Z",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), attachedImpl);
        demos.put("attachedDemo", attachedDemo);

        // parentDemo(): headless views are parentless.
        MutableMethodImplementation parentImpl = new MutableMethodImplementation(1);
        parentImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        parentImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        parentImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        parentImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "getParent",
                        Collections.<String>emptyList(), "Landroid/view/ViewParent;"))));
        parentImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        parentImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod parentDemo = builder.internMethod(HELLO, "parentDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), parentImpl);
        demos.put("parentDemo", parentDemo);

        // vtoDemo(): getViewTreeObserver yields a placeholder.
        MutableMethodImplementation vtoImpl = new MutableMethodImplementation(1);
        vtoImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        vtoImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        vtoImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        vtoImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "getViewTreeObserver",
                        Collections.<String>emptyList(),
                        "Landroid/view/ViewTreeObserver;"))));
        vtoImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        vtoImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod vtoDemo = builder.internMethod(HELLO, "vtoDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), vtoImpl);
        demos.put("vtoDemo", vtoDemo);

        // drawDemo(): draw-listener registration is a silent no-op.
        MutableMethodImplementation drawImpl = new MutableMethodImplementation(3);
        drawImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        drawImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        drawImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        drawImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "getViewTreeObserver",
                        Collections.<String>emptyList(),
                        "Landroid/view/ViewTreeObserver;"))));
        drawImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        drawImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 0));
        drawImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 1, 2, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/ViewTreeObserver;", "addOnDrawListener",
                        Collections.singletonList(
                                "Landroid/view/ViewTreeObserver$OnDrawListener;"),
                        "V"))));
        drawImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod drawDemo = builder.internMethod(HELLO, "drawDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), drawImpl);
        demos.put("drawDemo", drawDemo);

        // vtoLayoutDemo(): global-layout registration is a silent no-op.
        MutableMethodImplementation vtoLayoutImpl = new MutableMethodImplementation(3);
        vtoLayoutImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        vtoLayoutImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        vtoLayoutImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        vtoLayoutImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "getViewTreeObserver",
                        Collections.<String>emptyList(),
                        "Landroid/view/ViewTreeObserver;"))));
        vtoLayoutImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        vtoLayoutImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 0));
        vtoLayoutImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 1, 2, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/ViewTreeObserver;", "addOnGlobalLayoutListener",
                        Collections.singletonList(
                                "Landroid/view/ViewTreeObserver$OnGlobalLayoutListener;"),
                        "V"))));
        vtoLayoutImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod vtoLayoutDemo = builder.internMethod(HELLO, "vtoLayoutDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), vtoLayoutImpl);
        demos.put("vtoLayoutDemo", vtoLayoutDemo);

        // vtoScrollDemo(): scroll-changed registration is a silent no-op.
        MutableMethodImplementation vtoScrollImpl = new MutableMethodImplementation(3);
        vtoScrollImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        vtoScrollImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        vtoScrollImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        vtoScrollImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "getViewTreeObserver",
                        Collections.<String>emptyList(),
                        "Landroid/view/ViewTreeObserver;"))));
        vtoScrollImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        vtoScrollImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 0));
        vtoScrollImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 1, 2, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/ViewTreeObserver;", "addOnScrollChangedListener",
                        Collections.singletonList(
                                "Landroid/view/ViewTreeObserver$OnScrollChangedListener;"),
                        "V"))));
        vtoScrollImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod vtoScrollDemo = builder.internMethod(HELLO, "vtoScrollDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), vtoScrollImpl);
        demos.put("vtoScrollDemo", vtoScrollDemo);

        // vtoClusterDemo(): the remaining observer registrations are silent
        // no-ops (touch-mode, pre-draw, window focus/attach, insets, plus
        // the matching removals).
        MutableMethodImplementation vtoClusterImpl = new MutableMethodImplementation(3);
        vtoClusterImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        vtoClusterImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        vtoClusterImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        vtoClusterImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "getViewTreeObserver",
                        Collections.<String>emptyList(),
                        "Landroid/view/ViewTreeObserver;"))));
        vtoClusterImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        vtoClusterImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 0));
        String[][] clusterCalls = {
                {"addOnTouchModeChangeListener",
                        "Landroid/view/ViewTreeObserver$OnTouchModeChangeListener;"},
                {"addOnPreDrawListener",
                        "Landroid/view/ViewTreeObserver$OnPreDrawListener;"},
                {"addOnWindowFocusChangeListener",
                        "Landroid/view/ViewTreeObserver$OnWindowFocusChangeListener;"},
                {"addOnWindowAttachListener",
                        "Landroid/view/ViewTreeObserver$OnWindowAttachListener;"},
                {"addOnComputeInternalInsetsListener",
                        "Landroid/view/ViewTreeObserver$OnComputeInternalInsetsListener;"},
                {"removeOnGlobalLayoutListener",
                        "Landroid/view/ViewTreeObserver$OnGlobalLayoutListener;"},
                {"removeOnScrollChangedListener",
                        "Landroid/view/ViewTreeObserver$OnScrollChangedListener;"},
                {"removeOnPreDrawListener",
                        "Landroid/view/ViewTreeObserver$OnPreDrawListener;"},
                {"removeOnTouchModeChangeListener",
                        "Landroid/view/ViewTreeObserver$OnTouchModeChangeListener;"},
        };
        for (String[] call : clusterCalls) {
            vtoClusterImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 1,
                    2, 0, 0, 0, builder.internMethodReference(new ImmutableMethodReference(
                            "Landroid/view/ViewTreeObserver;", call[0],
                            Collections.singletonList(call[1]), "V"))));
        }
        vtoClusterImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod vtoClusterDemo = builder.internMethod(HELLO, "vtoClusterDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), vtoClusterImpl);
        demos.put("vtoClusterDemo", vtoClusterDemo);

        // contentViewDemo(): setContentView installs silently headless.
        MutableMethodImplementation contentViewImpl = new MutableMethodImplementation(4);
        contentViewImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/app/Activity;")));
        contentViewImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        contentViewImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        contentViewImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Landroid/view/View;")));
        contentViewImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        contentViewImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 1, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        contentViewImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 2,
                builder.internTypeReference("Landroid/view/ViewGroup$LayoutParams;")));
        contentViewImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 2));
        contentViewImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 3, -1));
        contentViewImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 3, 2, 3, 3,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/ViewGroup$LayoutParams;", "<init>",
                        Arrays.asList("I", "I"), "V"))));
        contentViewImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 3, 0, 1, 2,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "setContentView",
                        Arrays.asList("Landroid/view/View;",
                                "Landroid/view/ViewGroup$LayoutParams;"),
                        "V"))));
        contentViewImpl.addInstruction(new BuilderInstruction22c(Opcode.IGET_OBJECT, 0, 0,
                builder.internFieldReference(new ImmutableFieldReference(
                        "Landroid/app/Activity;", "mContentView", "Landroid/view/View;"))));
        contentViewImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod contentViewDemo = builder.internMethod(HELLO, "contentViewDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), contentViewImpl);
        demos.put("contentViewDemo", contentViewDemo);

        // choreoDemo(): Choreographer.getInstance() yields a singleton-ish.
        MutableMethodImplementation choreoImpl = new MutableMethodImplementation(1);
        choreoImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 0, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/Choreographer;", "getInstance",
                        Collections.<String>emptyList(),
                        "Landroid/view/Choreographer;"))));
        choreoImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        choreoImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod choreoDemo = builder.internMethod(HELLO, "choreoDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), choreoImpl);
        demos.put("choreoDemo", choreoDemo);

        // handlerAsyncDemo(): Handler.createAsync(mainLooper) builds a handler.
        MutableMethodImplementation handlerAsyncImpl = new MutableMethodImplementation(1);
        handlerAsyncImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 0, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/os/Looper;", "getMainLooper",
                        Collections.<String>emptyList(), "Landroid/os/Looper;"))));
        handlerAsyncImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        handlerAsyncImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/os/Handler;", "createAsync",
                        Collections.singletonList("Landroid/os/Looper;"),
                        "Landroid/os/Handler;"))));
        handlerAsyncImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        handlerAsyncImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod handlerAsyncDemo = builder.internMethod(HELLO, "handlerAsyncDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), handlerAsyncImpl);
        demos.put("handlerAsyncDemo", handlerAsyncDemo);

        // high16Demo(): const/high16 with the bits of float 1.0.
        MutableMethodImplementation high16Impl = new MutableMethodImplementation(1);
        high16Impl.addInstruction(
                new BuilderInstruction21ih(Opcode.CONST_HIGH16, 0, 0x3F800000));
        high16Impl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod high16Demo = builder.internMethod(HELLO, "high16Demo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), high16Impl);
        demos.put("high16Demo", high16Demo);

        // wideHigh16Demo(): const-wide/high16 with the bits of double 1.0.
        MutableMethodImplementation wideHigh16Impl = new MutableMethodImplementation(1);
        wideHigh16Impl.addInstruction(new BuilderInstruction21lh(Opcode.CONST_WIDE_HIGH16, 0,
                0x3FF0000000000000L));
        wideHigh16Impl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_WIDE, 0));
        BuilderMethod wideHigh16Demo = builder.internMethod(HELLO, "wideHigh16Demo",
                Collections.<ImmutableMethodParameter>emptyList(), "J",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), wideHigh16Impl);
        demos.put("wideHigh16Demo", wideHigh16Demo);

        // fillDemo(): new int[]{1,2,3} via fill-array-data; returns a[1].
        // Labels bind to existing locations, so the payload is added first
        // and the fill inserted before it (dexlib2 shifts the rest along).
        MutableMethodImplementation fillImpl = new MutableMethodImplementation(2);
        fillImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 3));
        fillImpl.addInstruction(new BuilderInstruction22c(Opcode.NEW_ARRAY, 0, 1,
                builder.internTypeReference("[I")));
        fillImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 1));
        fillImpl.addInstruction(new BuilderInstruction23x(Opcode.AGET, 0, 0, 1));
        fillImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        fillImpl.addInstruction(new BuilderArrayPayload(4, Arrays.asList(1, 2, 3)));
        Label fillData = fillImpl.newLabelForIndex(5);
        fillImpl.addInstruction(2,
                new BuilderInstruction31t(Opcode.FILL_ARRAY_DATA, 0, fillData));
        BuilderMethod fillDemo = builder.internMethod(HELLO, "fillDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), fillImpl);
        demos.put("fillDemo", fillDemo);

        // fillFloatDemo(): float[] filled with 1.0f bits reads back the bits.
        MutableMethodImplementation fillFloatImpl = new MutableMethodImplementation(2);
        fillFloatImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 1));
        fillFloatImpl.addInstruction(new BuilderInstruction22c(Opcode.NEW_ARRAY, 0, 1,
                builder.internTypeReference("[F")));
        fillFloatImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 0));
        fillFloatImpl.addInstruction(new BuilderInstruction23x(Opcode.AGET, 0, 0, 1));
        fillFloatImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        fillFloatImpl.addInstruction(new BuilderArrayPayload(4, Arrays.asList(0x3F800000)));
        Label fillFloatData = fillFloatImpl.newLabelForIndex(5);
        fillFloatImpl.addInstruction(2,
                new BuilderInstruction31t(Opcode.FILL_ARRAY_DATA, 0, fillFloatData));
        BuilderMethod fillFloatDemo = builder.internMethod(HELLO, "fillFloatDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), fillFloatImpl);
        demos.put("fillFloatDemo", fillFloatDemo);

        // runtimeDemo(): Runtime.getRuntime().maxMemory() is positive.
        MutableMethodImplementation runtimeImpl = new MutableMethodImplementation(1);
        runtimeImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 0, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Runtime;", "getRuntime",
                        Collections.<String>emptyList(), "Ljava/lang/Runtime;"))));
        runtimeImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        runtimeImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Runtime;", "maxMemory",
                        Collections.<String>emptyList(), "J"))));
        runtimeImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_WIDE, 0));
        runtimeImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_WIDE, 0));
        BuilderMethod runtimeDemo = builder.internMethod(HELLO, "runtimeDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "J",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), runtimeImpl);
        demos.put("runtimeDemo", runtimeDemo);

        // cpuDemo(): Runtime.getRuntime().availableProcessors() is positive.
        MutableMethodImplementation cpuImpl = new MutableMethodImplementation(1);
        cpuImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 0, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Runtime;", "getRuntime",
                        Collections.<String>emptyList(), "Ljava/lang/Runtime;"))));
        cpuImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        cpuImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Runtime;", "availableProcessors",
                        Collections.<String>emptyList(), "I"))));
        cpuImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        cpuImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod cpuDemo = builder.internMethod(HELLO, "cpuDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), cpuImpl);
        demos.put("cpuDemo", cpuDemo);

        // fileDemo(): new File("/sdcard/Hello.txt").getAbsolutePath()
        // answers the guest form: the bridge remaps paths through the Vfs
        // seam and reports no host layout.
        MutableMethodImplementation fileImpl = new MutableMethodImplementation(2);
        fileImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 0,
                builder.internStringReference("/sdcard/Hello.txt")));
        fileImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Ljava/io/File;")));
        fileImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        fileImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 1, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/io/File;", "<init>",
                        Collections.singletonList("Ljava/lang/String;"), "V"))));
        fileImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 1, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/io/File;", "getAbsolutePath",
                        Collections.<String>emptyList(), "Ljava/lang/String;"))));
        fileImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        fileImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod fileDemo = builder.internMethod(HELLO, "fileDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/String;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), fileImpl);
        demos.put("fileDemo", fileDemo);

        // fileDenyDemo(): an unmapped device path fails closed — the File
        // exists for nobody (the sandbox answers nonexistent).
        MutableMethodImplementation fileDenyImpl = new MutableMethodImplementation(2);
        fileDenyImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 0,
                builder.internStringReference("/proc/self/maps")));
        fileDenyImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Ljava/io/File;")));
        fileDenyImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        fileDenyImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 1, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/io/File;", "<init>",
                        Collections.singletonList("Ljava/lang/String;"), "V"))));
        fileDenyImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 1, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/io/File;", "exists",
                        Collections.<String>emptyList(), "Z"))));
        fileDenyImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        fileDenyImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod fileDenyDemo = builder.internMethod(HELLO, "fileDenyDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), fileDenyImpl);
        demos.put("fileDenyDemo", fileDenyDemo);

        // fileMkdirDemo(): mkdirs under the card succeed once (guest RW).
        MutableMethodImplementation fileMkdirImpl = new MutableMethodImplementation(2);
        fileMkdirImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 0,
                builder.internStringReference("/sdcard/TartineProbeDir")));
        fileMkdirImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Ljava/io/File;")));
        fileMkdirImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        fileMkdirImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 1, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/io/File;", "<init>",
                        Collections.singletonList("Ljava/lang/String;"), "V"))));
        fileMkdirImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 1, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/io/File;", "mkdirs",
                        Collections.<String>emptyList(), "Z"))));
        fileMkdirImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        fileMkdirImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod fileMkdirDemo = builder.internMethod(HELLO, "fileMkdirDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), fileMkdirImpl);
        demos.put("fileMkdirDemo", fileMkdirDemo);

        // forNameDenied(): Class.forName("java.lang.Runtime") stays
        // unresolvable — the audit's reflection-bypass probe. Class lookup
        // resolves the dex+framework universe only; a host class the
        // allowlist never carries answers ClassNotFoundException.
        MutableMethodImplementation forNameImpl = new MutableMethodImplementation(2);
        forNameImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 0,
                builder.internStringReference("java.lang.Runtime")));
        forNameImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Class;", "forName",
                        Collections.singletonList("Ljava/lang/String;"),
                        "Ljava/lang/Class;"))));
        forNameImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        forNameImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 0, 1));
        forNameImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        forNameImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_EXCEPTION, 0));
        forNameImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 0, 0));
        forNameImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        // Try covers const-string + invoke-static; the handler starts at
        // the move-exception (instruction index 5).
        Label forNameStart = forNameImpl.newLabelForIndex(0);
        Label forNameEnd = forNameImpl.newLabelForIndex(3);
        forNameImpl.addCatch(forNameStart, forNameEnd, forNameImpl.newLabelForIndex(5));
        BuilderMethod forNameDenied = builder.internMethod(HELLO, "forNameDenied",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), forNameImpl);
        demos.put("forNameDenied", forNameDenied);

        // propDemo(): unknown system properties stay null headless.
        MutableMethodImplementation propImpl = new MutableMethodImplementation(1);
        propImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 0,
                builder.internStringReference("tartine.missing")));
        propImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/System;", "getProperty",
                        Collections.singletonList("Ljava/lang/String;"),
                        "Ljava/lang/String;"))));
        propImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        propImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod propDemo = builder.internMethod(HELLO, "propDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), propImpl);
        demos.put("propDemo", propDemo);

        // windowDemo(): Activity.getWindow() yields a window.
        MutableMethodImplementation winImpl = new MutableMethodImplementation(1);
        winImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/app/Activity;")));
        winImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        winImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        winImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "getWindow",
                        Collections.<String>emptyList(), "Landroid/view/Window;"))));
        winImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        winImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod windowDemo = builder.internMethod(HELLO, "windowDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), winImpl);
        demos.put("windowDemo", windowDemo);

        // parentLinkDemo(): setContentView parents the view under the frame.
        MutableMethodImplementation parentLinkImpl = new MutableMethodImplementation(4);
        parentLinkImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/app/Activity;")));
        parentLinkImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        parentLinkImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        parentLinkImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Landroid/view/View;")));
        parentLinkImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        parentLinkImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 1, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        parentLinkImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 2,
                builder.internTypeReference("Landroid/view/ViewGroup$LayoutParams;")));
        parentLinkImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 2));
        parentLinkImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 3, -1));
        parentLinkImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 3, 2, 3, 3,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/ViewGroup$LayoutParams;", "<init>",
                        Arrays.asList("I", "I"), "V"))));
        parentLinkImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 3, 0, 1, 2,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "setContentView",
                        Arrays.asList("Landroid/view/View;",
                                "Landroid/view/ViewGroup$LayoutParams;"),
                        "V"))));
        parentLinkImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 1, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "getParent",
                        Collections.<String>emptyList(), "Landroid/view/ViewParent;"))));
        parentLinkImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        parentLinkImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod parentLinkDemo = builder.internMethod(HELLO, "parentLinkDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), parentLinkImpl);
        demos.put("parentLinkDemo", parentLinkDemo);

        // attachLinkDemo(): setContentView attaches the view to the window.
        MutableMethodImplementation attachLinkImpl = new MutableMethodImplementation(2);
        attachLinkImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/app/Activity;")));
        attachLinkImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        attachLinkImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        attachLinkImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Landroid/view/View;")));
        attachLinkImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        attachLinkImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 1, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        attachLinkImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "setContentView",
                        Collections.singletonList("Landroid/view/View;"), "V"))));
        attachLinkImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 1, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "isAttachedToWindow",
                        Collections.<String>emptyList(), "Z"))));
        attachLinkImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 1));
        attachLinkImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 1));
        BuilderMethod attachLinkDemo = builder.internMethod(HELLO, "attachLinkDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Z",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), attachLinkImpl);
        demos.put("attachLinkDemo", attachLinkDemo);

        // parentIfDemo(): ViewParent.getParent reads the same linkage.
        MutableMethodImplementation parentIfImpl = new MutableMethodImplementation(1);
        parentIfImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        parentIfImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        parentIfImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        parentIfImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_INTERFACE, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/ViewParent;", "getParent",
                        Collections.<String>emptyList(), "Landroid/view/ViewParent;"))));
        parentIfImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        parentIfImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod parentIfDemo = builder.internMethod(HELLO, "parentIfDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), parentIfImpl);
        demos.put("parentIfDemo", parentIfDemo);

        // idDemo(): headless views carry NO_ID.
        MutableMethodImplementation viewIdImpl = new MutableMethodImplementation(1);
        viewIdImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        viewIdImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        viewIdImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        viewIdImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "getId",
                        Collections.<String>emptyList(), "I"))));
        viewIdImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        viewIdImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod idDemo = builder.internMethod(HELLO, "idDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), viewIdImpl);
        demos.put("idDemo", idDemo);

        // viewHandlerDemo(): attached views report a main-looper handler.
        MutableMethodImplementation viewHandlerImpl = new MutableMethodImplementation(2);
        viewHandlerImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/app/Activity;")));
        viewHandlerImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        viewHandlerImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        viewHandlerImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Landroid/view/View;")));
        viewHandlerImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        viewHandlerImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 1, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        viewHandlerImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "setContentView",
                        Collections.singletonList("Landroid/view/View;"), "V"))));
        viewHandlerImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 1, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "getHandler",
                        Collections.<String>emptyList(), "Landroid/os/Handler;"))));
        viewHandlerImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        viewHandlerImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod viewHandlerDemo = builder.internMethod(HELLO, "viewHandlerDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), viewHandlerImpl);
        demos.put("viewHandlerDemo", viewHandlerDemo);

        // detachedHandlerDemo(): detached views have no handler.
        MutableMethodImplementation detachedHandlerImpl = new MutableMethodImplementation(1);
        detachedHandlerImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        detachedHandlerImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        detachedHandlerImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0,
                0, 0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        detachedHandlerImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0,
                0, 0, 0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "getHandler",
                        Collections.<String>emptyList(), "Landroid/os/Handler;"))));
        detachedHandlerImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        detachedHandlerImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod detachedHandlerDemo = builder.internMethod(HELLO, "detachedHandlerDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), detachedHandlerImpl);
        demos.put("detachedHandlerDemo", detachedHandlerDemo);

        // reflectDemo(): Handler.createAsync via getDeclaredMethod + invoke.
        MutableMethodImplementation reflectImpl = new MutableMethodImplementation(4);
        reflectImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_CLASS, 0,
                builder.internTypeReference("Landroid/os/Handler;")));
        reflectImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_CLASS, 1,
                builder.internTypeReference("Landroid/os/Looper;")));
        reflectImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 1));
        reflectImpl.addInstruction(new BuilderInstruction22c(Opcode.NEW_ARRAY, 2, 2,
                builder.internTypeReference("[Ljava/lang/Class;")));
        reflectImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 3, 0));
        reflectImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT_OBJECT, 1, 2, 3));
        reflectImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 3,
                builder.internStringReference("createAsync")));
        reflectImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 3, 0, 3, 2,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Class;", "getDeclaredMethod",
                        Arrays.asList("Ljava/lang/String;", "[Ljava/lang/Class;"),
                        "Ljava/lang/reflect/Method;"))));
        reflectImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        reflectImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 0, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/os/Looper;", "getMainLooper",
                        Collections.<String>emptyList(), "Landroid/os/Looper;"))));
        reflectImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        reflectImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 1));
        reflectImpl.addInstruction(new BuilderInstruction22c(Opcode.NEW_ARRAY, 2, 2,
                builder.internTypeReference("[Ljava/lang/Object;")));
        reflectImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 3, 0));
        reflectImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT_OBJECT, 1, 2, 3));
        reflectImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 0));
        reflectImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 3, 0, 1, 2,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/reflect/Method;", "invoke",
                        Arrays.asList("Ljava/lang/Object;", "[Ljava/lang/Object;"),
                        "Ljava/lang/Object;"))));
        reflectImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        reflectImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod reflectDemo = builder.internMethod(HELLO, "reflectDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), reflectImpl);
        demos.put("reflectDemo", reflectDemo);

        // reflectMissDemo(): unknown methods fail lookup, not invoke.
        MutableMethodImplementation reflectMissImpl = new MutableMethodImplementation(3);
        reflectMissImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_CLASS, 0,
                builder.internTypeReference("Ljava/lang/Object;")));
        reflectMissImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 0));
        reflectMissImpl.addInstruction(new BuilderInstruction22c(Opcode.NEW_ARRAY, 1, 1,
                builder.internTypeReference("[Ljava/lang/Class;")));
        reflectMissImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 2,
                builder.internStringReference("nope")));
        reflectMissImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 3, 0, 2, 1,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Class;", "getDeclaredMethod",
                        Arrays.asList("Ljava/lang/String;", "[Ljava/lang/Class;"),
                        "Ljava/lang/reflect/Method;"))));
        reflectMissImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        reflectMissImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod reflectMissDemo = builder.internMethod(HELLO, "reflectMissDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), reflectMissImpl);
        demos.put("reflectMissDemo", reflectMissDemo);

        // reflectPubDemo(): Sub inherits who() from Base; getMethod finds it.
        MutableMethodImplementation reflectPubImpl = new MutableMethodImplementation(4);
        reflectPubImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference(SUB)));
        reflectPubImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        reflectPubImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(SUB, "<init>",
                        Collections.<String>emptyList(), "V"))));
        reflectPubImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_CLASS, 1,
                builder.internTypeReference(SUB)));
        reflectPubImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 0));
        reflectPubImpl.addInstruction(new BuilderInstruction22c(Opcode.NEW_ARRAY, 2, 2,
                builder.internTypeReference("[Ljava/lang/Class;")));
        reflectPubImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 3,
                builder.internStringReference("who")));
        reflectPubImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 3, 1, 3, 2,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Class;", "getMethod",
                        Arrays.asList("Ljava/lang/String;", "[Ljava/lang/Class;"),
                        "Ljava/lang/reflect/Method;"))));
        reflectPubImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        reflectPubImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 0));
        reflectPubImpl.addInstruction(new BuilderInstruction22c(Opcode.NEW_ARRAY, 2, 2,
                builder.internTypeReference("[Ljava/lang/Object;")));
        reflectPubImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 3, 1, 0, 2,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/reflect/Method;", "invoke",
                        Arrays.asList("Ljava/lang/Object;", "[Ljava/lang/Object;"),
                        "Ljava/lang/Object;"))));
        reflectPubImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        reflectPubImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod reflectPubDemo = builder.internMethod(HELLO, "reflectPubDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), reflectPubImpl);
        demos.put("reflectPubDemo", reflectPubDemo);

        // annoDemo(): model methods carry no annotations.
        MutableMethodImplementation annoImpl = new MutableMethodImplementation(3);
        annoImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_CLASS, 0,
                builder.internTypeReference("Ljava/lang/Object;")));
        annoImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 0));
        annoImpl.addInstruction(new BuilderInstruction22c(Opcode.NEW_ARRAY, 1, 1,
                builder.internTypeReference("[Ljava/lang/Class;")));
        annoImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 2,
                builder.internStringReference("toString")));
        annoImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 3, 0, 2, 1, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Class;", "getMethod",
                        Arrays.asList("Ljava/lang/String;", "[Ljava/lang/Class;"),
                        "Ljava/lang/reflect/Method;"))));
        annoImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        annoImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/reflect/Method;", "getAnnotations",
                        Collections.<String>emptyList(),
                        "[Ljava/lang/annotation/Annotation;"))));
        annoImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        annoImpl.addInstruction(new BuilderInstruction12x(Opcode.ARRAY_LENGTH, 0, 0));
        annoImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod annoDemo = builder.internMethod(HELLO, "annoDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), annoImpl);
        demos.put("annoDemo", annoDemo);

        // longUpdaterDemo(): set 7, cas 7->8, add 2 == 10 (model fields are
        // untyped; the asLong normalization is what's tested). Wide args
        // occupy doubled registers, so multi-wide calls use /range.
        MutableMethodImplementation longUpdaterImpl = new MutableMethodImplementation(7);
        longUpdaterImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 2,
                builder.internTypeReference(COUNTER)));
        longUpdaterImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 2));
        longUpdaterImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 2, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(COUNTER,
                        "<init>", Collections.<String>emptyList(), "V"))));
        longUpdaterImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_CLASS, 0,
                builder.internTypeReference(COUNTER)));
        longUpdaterImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 3,
                builder.internStringReference("count")));
        longUpdaterImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 2, 0, 3, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;", "newUpdater",
                        Arrays.asList("Ljava/lang/Class;", "Ljava/lang/String;"),
                        "Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;"))));
        longUpdaterImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        longUpdaterImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_WIDE_16, 3, 7));
        longUpdaterImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 4, 0));
        longUpdaterImpl.addInstruction(new BuilderInstruction3rc(Opcode.INVOKE_VIRTUAL_RANGE, 1, 4,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;", "set",
                        Arrays.asList("Ljava/lang/Object;", "J"), "V"))));
        longUpdaterImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_WIDE_16, 3, 7));
        longUpdaterImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 4, 0));
        longUpdaterImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_WIDE_16, 5, 8));
        longUpdaterImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 6, 0));
        longUpdaterImpl.addInstruction(new BuilderInstruction3rc(Opcode.INVOKE_VIRTUAL_RANGE, 1, 6,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;", "compareAndSet",
                        Arrays.asList("Ljava/lang/Object;", "J", "J"), "Z"))));
        longUpdaterImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 3));
        longUpdaterImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_WIDE_16, 3, 2));
        longUpdaterImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 4, 0));
        longUpdaterImpl.addInstruction(new BuilderInstruction3rc(Opcode.INVOKE_VIRTUAL_RANGE, 1, 4,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;", "addAndGet",
                        Arrays.asList("Ljava/lang/Object;", "J"), "J"))));
        longUpdaterImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_WIDE, 3));
        longUpdaterImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_WIDE, 3));
        BuilderMethod longUpdaterDemo = builder.internMethod(HELLO, "longUpdaterDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "J",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), longUpdaterImpl);
        demos.put("longUpdaterDemo", longUpdaterDemo);

        // intUpdaterDemo(): failed cas preserves the set value.
        MutableMethodImplementation intUpdaterImpl = new MutableMethodImplementation(5);
        intUpdaterImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference(COUNTER)));
        intUpdaterImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        intUpdaterImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(COUNTER,
                        "<init>", Collections.<String>emptyList(), "V"))));
        intUpdaterImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_CLASS, 1,
                builder.internTypeReference(COUNTER)));
        intUpdaterImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 2,
                builder.internStringReference("count")));
        intUpdaterImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 2, 1, 2, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;", "newUpdater",
                        Arrays.asList("Ljava/lang/Class;", "Ljava/lang/String;"),
                        "Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;"))));
        intUpdaterImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        intUpdaterImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 5));
        intUpdaterImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 3, 1, 0, 2,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;", "set",
                        Arrays.asList("Ljava/lang/Object;", "I"), "V"))));
        intUpdaterImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 6));
        intUpdaterImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 3, 9));
        intUpdaterImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 4, 1, 0, 2,
                3, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;",
                        "compareAndSet",
                        Arrays.asList("Ljava/lang/Object;", "I", "I"), "Z"))));
        intUpdaterImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 4));
        intUpdaterImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 1, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;", "get",
                        Collections.singletonList("Ljava/lang/Object;"), "I"))));
        intUpdaterImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 4));
        intUpdaterImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 4));
        BuilderMethod intUpdaterDemo = builder.internMethod(HELLO, "intUpdaterDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), intUpdaterImpl);
        demos.put("intUpdaterDemo", intUpdaterDemo);

        // cloneDemo(): Object[].clone() copies contents.
        MutableMethodImplementation cloneImpl = new MutableMethodImplementation(3);
        cloneImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 2));
        cloneImpl.addInstruction(new BuilderInstruction22c(Opcode.NEW_ARRAY, 0, 1,
                builder.internTypeReference("[Ljava/lang/Object;")));
        cloneImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("a")));
        cloneImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 0));
        cloneImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT_OBJECT, 1, 0, 2));
        cloneImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "[Ljava/lang/Object;", "clone",
                        Collections.<String>emptyList(), "Ljava/lang/Object;"))));
        cloneImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        cloneImpl.addInstruction(new BuilderInstruction23x(Opcode.AGET_OBJECT, 0, 0, 2));
        cloneImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod cloneDemo = builder.internMethod(HELLO, "cloneDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), cloneImpl);
        demos.put("cloneDemo", cloneDemo);

        // ordinalDemo(): BLUE.ordinal() is 1.
        MutableMethodImplementation ordinalImpl = new MutableMethodImplementation(1);
        ordinalImpl.addInstruction(new BuilderInstruction21c(Opcode.SGET_OBJECT, 0,
                builder.internFieldReference(new ImmutableFieldReference("Ltest/Color;", "BLUE",
                        "Ltest/Color;"))));
        ordinalImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Enum;", "ordinal",
                        Collections.<String>emptyList(), "I"))));
        ordinalImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        ordinalImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod ordinalDemo = builder.internMethod(HELLO, "ordinalDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), ordinalImpl);
        demos.put("ordinalDemo", ordinalDemo);

        // switchDemo(): packed-switch on 1 takes the case (7, not 99).
        // Labels bind to existing locations, so cases and payload are added
        // first and the switch inserted before them.
        MutableMethodImplementation switchImpl = new MutableMethodImplementation(1);
        switchImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 0, 1));
        switchImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 0, 99));
        switchImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        switchImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 0, 7));
        switchImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        switchImpl.addInstruction(new BuilderPackedSwitchPayload(1,
                Collections.singletonList(switchImpl.newLabelForIndex(3))));
        Label switchData = switchImpl.newLabelForIndex(5);
        switchImpl.addInstruction(1,
                new BuilderInstruction31t(Opcode.PACKED_SWITCH, 0, switchData));
        BuilderMethod switchDemo = builder.internMethod(HELLO, "switchDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), switchImpl);
        demos.put("switchDemo", switchDemo);

        // switchDefaultDemo(): packed-switch on 5 falls through (99).
        MutableMethodImplementation switchDefaultImpl = new MutableMethodImplementation(1);
        switchDefaultImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 0, 5));
        switchDefaultImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 0, 99));
        switchDefaultImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        switchDefaultImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 0, 7));
        switchDefaultImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        switchDefaultImpl.addInstruction(new BuilderPackedSwitchPayload(1,
                Collections.singletonList(switchDefaultImpl.newLabelForIndex(3))));
        Label switchDefaultData = switchDefaultImpl.newLabelForIndex(5);
        switchDefaultImpl.addInstruction(1,
                new BuilderInstruction31t(Opcode.PACKED_SWITCH, 0, switchDefaultData));
        BuilderMethod switchDefaultDemo = builder.internMethod(HELLO, "switchDefaultDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), switchDefaultImpl);
        demos.put("switchDefaultDemo", switchDefaultDemo);

        // copyDemo(): copyOfRange keeps String[] (check-cast passes).
        MutableMethodImplementation copyRangeImpl = new MutableMethodImplementation(3);
        copyRangeImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 2));
        copyRangeImpl.addInstruction(new BuilderInstruction22c(Opcode.NEW_ARRAY, 0, 1,
                builder.internTypeReference("[Ljava/lang/String;")));
        copyRangeImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("a")));
        copyRangeImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 0));
        copyRangeImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT_OBJECT, 1, 0, 2));
        copyRangeImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("b")));
        copyRangeImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 1));
        copyRangeImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT_OBJECT, 1, 0, 2));
        copyRangeImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 0));
        copyRangeImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 1));
        copyRangeImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 3, 0, 1, 2, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/Arrays;", "copyOfRange",
                        Arrays.asList("[Ljava/lang/Object;", "I", "I"),
                        "[Ljava/lang/Object;"))));
        copyRangeImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        copyRangeImpl.addInstruction(new BuilderInstruction21c(Opcode.CHECK_CAST, 0,
                builder.internTypeReference("[Ljava/lang/String;")));
        copyRangeImpl.addInstruction(new BuilderInstruction23x(Opcode.AGET_OBJECT, 0, 0, 1));
        copyRangeImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod copyDemo = builder.internMethod(HELLO, "copyDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), copyRangeImpl);
        demos.put("copyDemo", copyDemo);

        // loaderDemo(): classes report the singleton loader.
        MutableMethodImplementation loaderImpl = new MutableMethodImplementation(1);
        loaderImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_CLASS, 0,
                builder.internTypeReference("Ljava/lang/Object;")));
        loaderImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Class;", "getClassLoader",
                        Collections.<String>emptyList(), "Ljava/lang/ClassLoader;"))));
        loaderImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        loaderImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod loaderDemo = builder.internMethod(HELLO, "loaderDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), loaderImpl);
        demos.put("loaderDemo", loaderDemo);

        // loadClassDemo(): loading resolves through the single universe.
        MutableMethodImplementation loadClassImpl = new MutableMethodImplementation(2);
        loadClassImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_CLASS, 0,
                builder.internTypeReference("Ljava/lang/Object;")));
        loadClassImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Class;", "getClassLoader",
                        Collections.<String>emptyList(), "Ljava/lang/ClassLoader;"))));
        loadClassImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        loadClassImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("test.Counter")));
        loadClassImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/ClassLoader;", "loadClass",
                        Collections.singletonList("Ljava/lang/String;"),
                        "Ljava/lang/Class;"))));
        loadClassImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        loadClassImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Class;", "getName",
                        Collections.<String>emptyList(), "Ljava/lang/String;"))));
        loadClassImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        loadClassImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod loadClassDemo = builder.internMethod(HELLO, "loadClassDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), loadClassImpl);
        demos.put("loadClassDemo", loadClassDemo);

        // serviceDemo(): service loading finds no providers.
        MutableMethodImplementation serviceImpl = new MutableMethodImplementation(1);
        serviceImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_CLASS, 0,
                builder.internTypeReference("Ljava/lang/Object;")));
        serviceImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/ServiceLoader;", "load",
                        Collections.singletonList("Ljava/lang/Class;"),
                        "Ljava/util/ServiceLoader;"))));
        serviceImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        serviceImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/ServiceLoader;", "iterator",
                        Collections.<String>emptyList(), "Ljava/util/Iterator;"))));
        serviceImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        serviceImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_INTERFACE, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/Iterator;", "hasNext",
                        Collections.<String>emptyList(), "Z"))));
        serviceImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        serviceImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod serviceDemo = builder.internMethod(HELLO, "serviceDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Z",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), serviceImpl);
        demos.put("serviceDemo", serviceDemo);

        // sdkIntDemo(): Build.VERSION.SDK_INT reports the modeled API.
        MutableMethodImplementation sdkIntImpl = new MutableMethodImplementation(2);
        sdkIntImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_CLASS, 0,
                builder.internTypeReference("Landroid/os/Build$VERSION;")));
        sdkIntImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("SDK_INT")));
        sdkIntImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Class;", "getField",
                        Collections.singletonList("Ljava/lang/String;"),
                        "Ljava/lang/reflect/Field;"))));
        sdkIntImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        sdkIntImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 0));
        sdkIntImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/reflect/Field;", "getInt",
                        Collections.singletonList("Ljava/lang/Object;"), "I"))));
        sdkIntImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        sdkIntImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod sdkIntDemo = builder.internMethod(HELLO, "sdkIntDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), sdkIntImpl);
        demos.put("sdkIntDemo", sdkIntDemo);

        // suppressedDemo(): model throwables record suppressed per receiver.
        // (The host-receiver branch is defensive — host throwables only
        // surface as caught values — and stays untested, like the host
        // branches of the neighboring Throwable builtins.)
        MutableMethodImplementation suppressedImpl = new MutableMethodImplementation(1);
        suppressedImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Ltest/Boom;")));
        suppressedImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        suppressedImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference("Ltest/Boom;",
                        "<init>", Collections.<String>emptyList(), "V"))));
        suppressedImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Throwable;", "addSuppressed",
                        Collections.singletonList("Ljava/lang/Throwable;"), "V"))));
        suppressedImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Throwable;", "getSuppressed",
                        Collections.<String>emptyList(), "[Ljava/lang/Throwable;"))));
        suppressedImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        suppressedImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod suppressedDemo = builder.internMethod(HELLO, "suppressedDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), suppressedImpl);
        demos.put("suppressedDemo", suppressedDemo);

        // zeroCheckDemo(): a boxed zero is an object (if-eqz skips it).
        // Insert-after pattern again: the forward branch needs its target.
        MutableMethodImplementation zeroCheckImpl = new MutableMethodImplementation(1);
        zeroCheckImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 0, 0));
        zeroCheckImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Integer;", "valueOf",
                        Collections.singletonList("I"), "Ljava/lang/Integer;"))));
        zeroCheckImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        zeroCheckImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 0, 7));
        zeroCheckImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        zeroCheckImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 0, 99));
        zeroCheckImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        Label zeroFail = zeroCheckImpl.newLabelForIndex(5);
        zeroCheckImpl.addInstruction(3,
                new BuilderInstruction21t(Opcode.IF_EQZ, 0, zeroFail));
        BuilderMethod zeroCheckDemo = builder.internMethod(HELLO, "zeroCheckDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), zeroCheckImpl);
        demos.put("zeroCheckDemo", zeroCheckDemo);

        // nullCheckDemo(): zero-bits still branch as null (the null idiom).
        MutableMethodImplementation nullCheckImpl = new MutableMethodImplementation(1);
        nullCheckImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 0, 0));
        nullCheckImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 0, 99));
        nullCheckImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        nullCheckImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 0, 7));
        nullCheckImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        Label nullTake = nullCheckImpl.newLabelForIndex(3);
        nullCheckImpl.addInstruction(1,
                new BuilderInstruction21t(Opcode.IF_EQZ, 0, nullTake));
        BuilderMethod nullCheckDemo = builder.internMethod(HELLO, "nullCheckDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), nullCheckImpl);
        demos.put("nullCheckDemo", nullCheckDemo);

        // frameIdDemo(): the content frame carries android.R.id.content.
        MutableMethodImplementation frameIdImpl = new MutableMethodImplementation(2);
        frameIdImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        frameIdImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        frameIdImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        frameIdImpl.addInstruction(new BuilderInstruction31i(Opcode.CONST, 1, 0x1020002));
        frameIdImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "findViewById",
                        Collections.singletonList("I"), "Landroid/view/View;"))));
        frameIdImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        frameIdImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "getId",
                        Collections.<String>emptyList(), "I"))));
        frameIdImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        frameIdImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod frameIdDemo = builder.internMethod(HELLO, "frameIdDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), frameIdImpl);
        demos.put("frameIdDemo", frameIdDemo);

        // frameCbDemo(): unregistering an idle frame callback is silent.
        MutableMethodImplementation frameCbImpl = new MutableMethodImplementation(2);
        frameCbImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 0, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/Choreographer;", "getInstance",
                        Collections.<String>emptyList(),
                        "Landroid/view/Choreographer;"))));
        frameCbImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        frameCbImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 0));
        frameCbImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/Choreographer;", "removeFrameCallback",
                        Collections.singletonList(
                                "Landroid/view/Choreographer$FrameCallback;"),
                        "V"))));
        frameCbImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod frameCbDemo = builder.internMethod(HELLO, "frameCbDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), frameCbImpl);
        demos.put("frameCbDemo", frameCbDemo);

        // postCbDemo(): posting a frame callback records it silently.
        MutableMethodImplementation postCbImpl = new MutableMethodImplementation(2);
        postCbImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 0, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/Choreographer;", "getInstance",
                        Collections.<String>emptyList(),
                        "Landroid/view/Choreographer;"))));
        postCbImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        postCbImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 0));
        postCbImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/Choreographer;", "postFrameCallback",
                        Collections.singletonList(
                                "Landroid/view/Choreographer$FrameCallback;"),
                        "V"))));
        postCbImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod postCbDemo = builder.internMethod(HELLO, "postCbDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), postCbImpl);
        demos.put("postCbDemo", postCbDemo);

        // plusCollDemo(): host addAll over a model array keeps elements.
        MutableMethodImplementation plusCollImpl = new MutableMethodImplementation(4);
        plusCollImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Ljava/util/ArrayList;")));
        plusCollImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        plusCollImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/ArrayList;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        plusCollImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 2));
        plusCollImpl.addInstruction(new BuilderInstruction22c(Opcode.NEW_ARRAY, 2, 1,
                builder.internTypeReference("[Ljava/lang/String;")));
        plusCollImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("a")));
        plusCollImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 3, 0));
        plusCollImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT_OBJECT, 1, 2, 3));
        plusCollImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("b")));
        plusCollImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 3, 1));
        plusCollImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT_OBJECT, 1, 2, 3));
        plusCollImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 2, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/ArrayList;", "addAll",
                        Collections.singletonList("Ljava/util/Collection;"), "Z"))));
        plusCollImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 1));
        plusCollImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/ArrayList;", "size",
                        Collections.<String>emptyList(), "I"))));
        plusCollImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 1));
        plusCollImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 1));
        BuilderMethod plusCollDemo = builder.internMethod(HELLO, "plusCollDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), plusCollImpl);
        demos.put("plusCollDemo", plusCollDemo);

        // childAddDemo(): addView records the child for getChildAt.
        MutableMethodImplementation childAddImpl = new MutableMethodImplementation(4);
        childAddImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/ViewGroup;")));
        childAddImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        childAddImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Landroid/app/Activity;")));
        childAddImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        childAddImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 1, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        childAddImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 0));
        childAddImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 4, 0, 1, 2, 2,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/ViewGroup;", "<init>",
                        Arrays.asList("Landroid/content/Context;",
                                "Landroid/util/AttributeSet;", "I"),
                        "V"))));
        childAddImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Landroid/view/View;")));
        childAddImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        childAddImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 1, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        childAddImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/ViewGroup;", "addView",
                        Collections.singletonList("Landroid/view/View;"), "V"))));
        childAddImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 0));
        childAddImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 2, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/ViewGroup;", "getChildAt",
                        Collections.singletonList("I"), "Landroid/view/View;"))));
        childAddImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        childAddImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod childAddDemo = builder.internMethod(HELLO, "childAddDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), childAddImpl);
        demos.put("childAddDemo", childAddDemo);

        // attachPropDemo(): group.onAttachedToWindow marks group and child:
        // returns childAttached * 10 + groupAttached = 11.
        MutableMethodImplementation attachPropImpl = new MutableMethodImplementation(3);
        attachPropImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/ViewGroup;")));
        attachPropImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        attachPropImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Landroid/app/Activity;")));
        attachPropImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        attachPropImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 1, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        attachPropImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 0));
        attachPropImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 4, 0, 1, 2,
                2, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/ViewGroup;", "<init>",
                        Arrays.asList("Landroid/content/Context;",
                                "Landroid/util/AttributeSet;", "I"),
                        "V"))));
        attachPropImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Landroid/view/View;")));
        attachPropImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        attachPropImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 1, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        attachPropImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/ViewGroup;", "addView",
                        Collections.singletonList("Landroid/view/View;"), "V"))));
        attachPropImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/ViewGroup;", "onAttachedToWindow",
                        Collections.<String>emptyList(), "V"))));
        attachPropImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 1, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "isAttachedToWindow",
                        Collections.<String>emptyList(), "Z"))));
        attachPropImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        attachPropImpl.addInstruction(new BuilderInstruction22b(Opcode.MUL_INT_LIT8, 2, 2, 10));
        attachPropImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/ViewGroup;", "isAttachedToWindow",
                        Collections.<String>emptyList(), "Z"))));
        attachPropImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 1));
        attachPropImpl.addInstruction(new BuilderInstruction23x(Opcode.ADD_INT, 2, 2, 1));
        attachPropImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 2));
        BuilderMethod attachPropDemo = builder.internMethod(HELLO, "attachPropDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), attachPropImpl);
        demos.put("attachPropDemo", attachPropDemo);

        // childRemoveDemo(): removeAllViews detaches and empties.
        MutableMethodImplementation childRemoveImpl = new MutableMethodImplementation(4);
        childRemoveImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/ViewGroup;")));
        childRemoveImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        childRemoveImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Landroid/app/Activity;")));
        childRemoveImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        childRemoveImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 1, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        childRemoveImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 0));
        childRemoveImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 4, 0, 1, 2,
                2, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/ViewGroup;", "<init>",
                        Arrays.asList("Landroid/content/Context;",
                                "Landroid/util/AttributeSet;", "I"),
                        "V"))));
        childRemoveImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Landroid/view/View;")));
        childRemoveImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        childRemoveImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 1, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        childRemoveImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/ViewGroup;", "addView",
                        Collections.singletonList("Landroid/view/View;"), "V"))));
        childRemoveImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/ViewGroup;", "removeAllViews",
                        Collections.<String>emptyList(), "V"))));
        childRemoveImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/ViewGroup;", "getChildCount",
                        Collections.<String>emptyList(), "I"))));
        childRemoveImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        childRemoveImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod childRemoveDemo = builder.internMethod(HELLO, "childRemoveDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), childRemoveImpl);
        demos.put("childRemoveDemo", childRemoveDemo);

        // ctxDemo(): ViewGroup(Context).getContext() keeps the context.
        MutableMethodImplementation ctxImpl = new MutableMethodImplementation(2);
        ctxImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/ViewGroup;")));
        ctxImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        ctxImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Landroid/app/Activity;")));
        ctxImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        ctxImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 1, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        ctxImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/ViewGroup;", "<init>",
                        Collections.singletonList("Landroid/content/Context;"), "V"))));
        ctxImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "getContext",
                        Collections.<String>emptyList(), "Landroid/content/Context;"))));
        ctxImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        ctxImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod ctxDemo = builder.internMethod(HELLO, "ctxDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), ctxImpl);
        demos.put("ctxDemo", ctxDemo);

        // resDemo(): contexts serve Resources placeholders.
        MutableMethodImplementation resImpl = new MutableMethodImplementation(1);
        resImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/app/Activity;")));
        resImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        resImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        resImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/Context;", "getResources",
                        Collections.<String>emptyList(),
                        "Landroid/content/res/Resources;"))));
        resImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        resImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod resDemo = builder.internMethod(HELLO, "resDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), resImpl);
        demos.put("resDemo", resDemo);

        // sysServiceDemo(): getSystemService("accessibility") resolves.
        MutableMethodImplementation sysServiceImpl = new MutableMethodImplementation(2);
        sysServiceImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/app/Activity;")));
        sysServiceImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        sysServiceImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        sysServiceImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("accessibility")));
        sysServiceImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/Context;", "getSystemService",
                        Collections.singletonList("Ljava/lang/String;"),
                        "Ljava/lang/Object;"))));
        sysServiceImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        sysServiceImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod sysServiceDemo = builder.internMethod(HELLO, "sysServiceDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), sysServiceImpl);
        demos.put("sysServiceDemo", sysServiceDemo);

        // clipboardDemo(): getSystemService("clipboard") resolves.
        MutableMethodImplementation clipboardImpl = new MutableMethodImplementation(2);
        clipboardImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/app/Activity;")));
        clipboardImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        clipboardImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        clipboardImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("clipboard")));
        clipboardImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/Context;", "getSystemService",
                        Collections.singletonList("Ljava/lang/String;"),
                        "Ljava/lang/Object;"))));
        clipboardImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        clipboardImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod clipboardDemo = builder.internMethod(HELLO, "clipboardDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), clipboardImpl);
        demos.put("clipboardDemo", clipboardDemo);

        // viewConfigDemo(): ViewConfiguration.get(context) resolves.
        MutableMethodImplementation viewConfigImpl = new MutableMethodImplementation(2);
        viewConfigImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/app/Activity;")));
        viewConfigImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        viewConfigImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        viewConfigImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/ViewConfiguration;", "get",
                        Collections.singletonList("Landroid/content/Context;"),
                        "Landroid/view/ViewConfiguration;"))));
        viewConfigImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        viewConfigImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod viewConfigDemo = builder.internMethod(HELLO, "viewConfigDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), viewConfigImpl);
        demos.put("viewConfigDemo", viewConfigDemo);

        // appCtxDemo(): getApplicationContext() resolves stably.
        MutableMethodImplementation appCtxImpl = new MutableMethodImplementation(1);
        appCtxImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/app/Activity;")));
        appCtxImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        appCtxImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        appCtxImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/Context;", "getApplicationContext",
                        Collections.<String>emptyList(), "Landroid/content/Context;"))));
        appCtxImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        appCtxImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod appCtxDemo = builder.internMethod(HELLO, "appCtxDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), appCtxImpl);
        demos.put("appCtxDemo", appCtxDemo);
        return demos;
    }

    private static Map<String, BuilderMethod> writeTextDemos(DexBuilder builder, BuilderMethodReference objectInit) {
        Map<String, BuilderMethod> demos = new HashMap<>();
        // a11yDemo(): no enabled accessibility services headless (empty list).
        MutableMethodImplementation a11yImpl = new MutableMethodImplementation(2);
        a11yImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/app/Activity;")));
        a11yImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        a11yImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        a11yImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("accessibility")));
        a11yImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/Context;", "getSystemService",
                        Collections.singletonList("Ljava/lang/String;"),
                        "Ljava/lang/Object;"))));
        a11yImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        a11yImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, -1));
        a11yImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/accessibility/AccessibilityManager;",
                        "getEnabledAccessibilityServiceList",
                        Collections.singletonList("I"), "Ljava/util/List;"))));
        a11yImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        a11yImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_INTERFACE, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/List;", "isEmpty",
                        Collections.<String>emptyList(), "Z"))));
        a11yImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        a11yImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod a11yDemo = builder.internMethod(HELLO, "a11yDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Z",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), a11yImpl);
        demos.put("a11yDemo", a11yDemo);

        // a11yEnabledDemo(): accessibility manager is disabled headless.
        MutableMethodImplementation a11yEnabledImpl = new MutableMethodImplementation(2);
        a11yEnabledImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/app/Activity;")));
        a11yEnabledImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        a11yEnabledImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        a11yEnabledImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("accessibility")));
        a11yEnabledImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/Context;", "getSystemService",
                        Collections.singletonList("Ljava/lang/String;"),
                        "Ljava/lang/Object;"))));
        a11yEnabledImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        a11yEnabledImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/accessibility/AccessibilityManager;", "isEnabled",
                        Collections.<String>emptyList(), "Z"))));
        a11yEnabledImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        a11yEnabledImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod a11yEnabledDemo = builder.internMethod(HELLO, "a11yEnabledDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Z",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), a11yEnabledImpl);
        demos.put("a11yEnabledDemo", a11yEnabledDemo);

        // autofillDemo(): getSystemService(AutofillManager.class) resolves.
        MutableMethodImplementation autofillImpl = new MutableMethodImplementation(2);
        autofillImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/app/Activity;")));
        autofillImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        autofillImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        autofillImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_CLASS, 1,
                builder.internTypeReference("Landroid/view/autofill/AutofillManager;")));
        autofillImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/Context;", "getSystemService",
                        Collections.singletonList("Ljava/lang/Class;"),
                        "Ljava/lang/Object;"))));
        autofillImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        autofillImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod autofillDemo = builder.internMethod(HELLO, "autofillDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), autofillImpl);
        demos.put("autofillDemo", autofillDemo);

        // autofillCbDemo(): registerCallback is a silent no-op.
        MutableMethodImplementation autofillCbImpl = new MutableMethodImplementation(3);
        autofillCbImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/app/Activity;")));
        autofillCbImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        autofillCbImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        autofillCbImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_CLASS, 1,
                builder.internTypeReference("Landroid/view/autofill/AutofillManager;")));
        autofillCbImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/Context;", "getSystemService",
                        Collections.singletonList("Ljava/lang/Class;"),
                        "Ljava/lang/Object;"))));
        autofillCbImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        autofillCbImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 2, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/autofill/AutofillManager;", "registerCallback",
                        Collections.singletonList(
                                "Landroid/view/autofill/AutofillManager$AutofillCallback;"),
                        "V"))));
        autofillCbImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod autofillCbDemo = builder.internMethod(HELLO, "autofillCbDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), autofillCbImpl);
        demos.put("autofillCbDemo", autofillCbDemo);

        // importantDemo(): setImportantForAutofill/ContentCapture are no-ops.
        MutableMethodImplementation importantImpl = new MutableMethodImplementation(2);
        importantImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        importantImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        importantImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        importantImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 1));
        importantImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "setImportantForAutofill",
                        Collections.singletonList("I"), "V"))));
        importantImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "setImportantForContentCapture",
                        Collections.singletonList("I"), "V"))));
        importantImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod importantDemo = builder.internMethod(HELLO, "importantDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), importantImpl);
        demos.put("importantDemo", importantDemo);

        // captureDemo(): no content-capture session headless.
        MutableMethodImplementation captureImpl = new MutableMethodImplementation(1);
        captureImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        captureImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        captureImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        captureImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "getContentCaptureSession",
                        Collections.<String>emptyList(),
                        "Landroid/view/contentcapture/ContentCaptureSession;"))));
        captureImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        captureImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod captureDemo = builder.internMethod(HELLO, "captureDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), captureImpl);
        demos.put("captureDemo", captureDemo);

        // metricsDemo(): DisplayMetrics carries baseline mdpi density.
        MutableMethodImplementation metricsImpl = new MutableMethodImplementation(1);
        metricsImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/app/Activity;")));
        metricsImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        metricsImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        metricsImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/Context;", "getResources",
                        Collections.<String>emptyList(),
                        "Landroid/content/res/Resources;"))));
        metricsImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        metricsImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/res/Resources;", "getDisplayMetrics",
                        Collections.<String>emptyList(),
                        "Landroid/util/DisplayMetrics;"))));
        metricsImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        metricsImpl.addInstruction(new BuilderInstruction22c(Opcode.IGET, 0, 0,
                builder.internFieldReference(new ImmutableFieldReference(
                        "Landroid/util/DisplayMetrics;", "density", "F"))));
        metricsImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod metricsDemo = builder.internMethod(HELLO, "metricsDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), metricsImpl);
        demos.put("metricsDemo", metricsDemo);

        // configDemo(): Configuration carries the default font scale.
        MutableMethodImplementation configImpl = new MutableMethodImplementation(1);
        configImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/app/Activity;")));
        configImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        configImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        configImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/Context;", "getResources",
                        Collections.<String>emptyList(),
                        "Landroid/content/res/Resources;"))));
        configImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        configImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/res/Resources;", "getConfiguration",
                        Collections.<String>emptyList(),
                        "Landroid/content/res/Configuration;"))));
        configImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        configImpl.addInstruction(new BuilderInstruction22c(Opcode.IGET, 0, 0,
                builder.internFieldReference(new ImmutableFieldReference(
                        "Landroid/content/res/Configuration;", "fontScale", "F"))));
        configImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod configDemo = builder.internMethod(HELLO, "configDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), configImpl);
        demos.put("configDemo", configDemo);

        // layoutDirDemo(): default configuration lays out left-to-right.
        MutableMethodImplementation layoutDirImpl = new MutableMethodImplementation(1);
        layoutDirImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/app/Activity;")));
        layoutDirImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        layoutDirImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        layoutDirImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/Context;", "getResources",
                        Collections.<String>emptyList(),
                        "Landroid/content/res/Resources;"))));
        layoutDirImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        layoutDirImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/res/Resources;", "getConfiguration",
                        Collections.<String>emptyList(),
                        "Landroid/content/res/Configuration;"))));
        layoutDirImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        layoutDirImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/res/Configuration;", "getLayoutDirection",
                        Collections.<String>emptyList(), "I"))));
        layoutDirImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        layoutDirImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod layoutDirDemo = builder.internMethod(HELLO, "layoutDirDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), layoutDirImpl);
        demos.put("layoutDirDemo", layoutDirDemo);

        // configCopyDemo(): copying a Configuration preserves its fields;
        // setTo overwrites another instance with them.
        MutableMethodImplementation configCopyImpl = new MutableMethodImplementation(3);
        configCopyImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/app/Activity;")));
        configCopyImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        configCopyImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        configCopyImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/Context;", "getResources",
                        Collections.<String>emptyList(),
                        "Landroid/content/res/Resources;"))));
        configCopyImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        configCopyImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/res/Resources;", "getConfiguration",
                        Collections.<String>emptyList(),
                        "Landroid/content/res/Configuration;"))));
        configCopyImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        configCopyImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Landroid/content/res/Configuration;")));
        configCopyImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        configCopyImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 1, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/res/Configuration;", "<init>",
                        Collections.singletonList("Landroid/content/res/Configuration;"),
                        "V"))));
        configCopyImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 2,
                builder.internTypeReference("Landroid/content/res/Configuration;")));
        configCopyImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 2));
        configCopyImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 2, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/res/Configuration;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        configCopyImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 2, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/res/Configuration;", "setTo",
                        Collections.singletonList("Landroid/content/res/Configuration;"),
                        "V"))));
        configCopyImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 2));
        BuilderMethod configCopyDemo = builder.internMethod(HELLO, "configCopyDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), configCopyImpl);
        demos.put("configCopyDemo", configCopyDemo);

        // touchModeDemo(): no touch input headless, so never in touch mode.
        MutableMethodImplementation touchModeImpl = new MutableMethodImplementation(1);
        touchModeImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        touchModeImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        touchModeImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        touchModeImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "isInTouchMode",
                        Collections.<String>emptyList(), "Z"))));
        touchModeImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        touchModeImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod touchModeDemo = builder.internMethod(HELLO, "touchModeDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Z",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), touchModeImpl);
        demos.put("touchModeDemo", touchModeDemo);

        // willNotDrawDemo(): setWillNotDraw is a silent no-op.
        MutableMethodImplementation willNotDrawImpl = new MutableMethodImplementation(2);
        willNotDrawImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        willNotDrawImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        willNotDrawImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        willNotDrawImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 0));
        willNotDrawImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "setWillNotDraw",
                        Collections.singletonList("Z"), "V"))));
        willNotDrawImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod willNotDrawDemo = builder.internMethod(HELLO, "willNotDrawDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), willNotDrawImpl);
        demos.put("willNotDrawDemo", willNotDrawDemo);

        // focusableDemo(): focus setters are silent no-ops.
        MutableMethodImplementation focusableImpl = new MutableMethodImplementation(2);
        focusableImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        focusableImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        focusableImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        focusableImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 1));
        focusableImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "setFocusable",
                        Collections.singletonList("Z"), "V"))));
        focusableImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "setFocusable",
                        Collections.singletonList("I"), "V"))));
        focusableImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "setDefaultFocusHighlightEnabled",
                        Collections.singletonList("Z"), "V"))));
        focusableImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "setFocusableInTouchMode",
                        Collections.singletonList("Z"), "V"))));
        focusableImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod focusableDemo = builder.internMethod(HELLO, "focusableDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), focusableImpl);
        demos.put("focusableDemo", focusableDemo);

        // a11yImportantDemo(): set is observed by get, default would be AUTO.
        MutableMethodImplementation a11yImportantImpl = new MutableMethodImplementation(2);
        a11yImportantImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        a11yImportantImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        a11yImportantImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        a11yImportantImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 1));
        a11yImportantImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "setImportantForAccessibility",
                        Collections.singletonList("I"), "V"))));
        a11yImportantImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "getImportantForAccessibility",
                        Collections.<String>emptyList(), "I"))));
        a11yImportantImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        a11yImportantImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod a11yImportantDemo = builder.internMethod(HELLO, "a11yImportantDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), a11yImportantImpl);
        demos.put("a11yImportantDemo", a11yImportantDemo);

        // a11yDelegateDemo(): setAccessibilityDelegate is a silent no-op.
        MutableMethodImplementation a11yDelegateImpl = new MutableMethodImplementation(2);
        a11yDelegateImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        a11yDelegateImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        a11yDelegateImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        a11yDelegateImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Landroid/view/View$AccessibilityDelegate;")));
        a11yDelegateImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        a11yDelegateImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 1, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View$AccessibilityDelegate;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        a11yDelegateImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "setAccessibilityDelegate",
                        Collections.singletonList("Landroid/view/View$AccessibilityDelegate;"),
                        "V"))));
        a11yDelegateImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod a11yDelegateDemo = builder.internMethod(HELLO, "a11yDelegateDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), a11yDelegateImpl);
        demos.put("a11yDelegateDemo", a11yDelegateDemo);

        // dragDemo(): setOnDragListener is a silent no-op.
        MutableMethodImplementation dragImpl = new MutableMethodImplementation(2);
        dragImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        dragImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        dragImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        dragImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "setOnDragListener",
                        Collections.singletonList("Landroid/view/View$OnDragListener;"),
                        "V"))));
        dragImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod dragDemo = builder.internMethod(HELLO, "dragDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), dragImpl);
        demos.put("dragDemo", dragDemo);

        // layoutDemo(): fresh views have no layout requested (0); after
        // requestLayout they do (1): returns first * 10 + second = 1.
        // requestApplyInsets dispatches silently in between.
        MutableMethodImplementation layoutImpl = new MutableMethodImplementation(3);
        layoutImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        layoutImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        layoutImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        layoutImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "isLayoutRequested",
                        Collections.<String>emptyList(), "Z"))));
        layoutImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 1));
        layoutImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "requestLayout",
                        Collections.<String>emptyList(), "V"))));
        layoutImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "requestApplyInsets",
                        Collections.<String>emptyList(), "V"))));
        layoutImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "isLayoutRequested",
                        Collections.<String>emptyList(), "Z"))));
        layoutImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        layoutImpl.addInstruction(new BuilderInstruction22b(Opcode.MUL_INT_LIT8, 1, 1, 10));
        layoutImpl.addInstruction(new BuilderInstruction23x(Opcode.ADD_INT, 1, 1, 2));
        layoutImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 1));
        BuilderMethod layoutDemo = builder.internMethod(HELLO, "layoutDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), layoutImpl);

        // measuredDemo(): measured dimensions store and read back.
        MutableMethodImplementation measuredImpl = new MutableMethodImplementation(3);
        measuredImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        measuredImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        measuredImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        measuredImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 1, 480));
        measuredImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 2, 960));
        measuredImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 3, 0, 1, 2,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "setMeasuredDimension",
                        Arrays.asList("I", "I"), "V"))));
        measuredImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "getMeasuredWidth",
                        Collections.<String>emptyList(), "I"))));
        measuredImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 1));
        measuredImpl.addInstruction(new BuilderInstruction22b(Opcode.MUL_INT_LIT8, 1, 1, 100));
        measuredImpl.addInstruction(new BuilderInstruction22b(Opcode.MUL_INT_LIT8, 1, 1, 10));
        measuredImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "getMeasuredHeight",
                        Collections.<String>emptyList(), "I"))));
        measuredImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        measuredImpl.addInstruction(new BuilderInstruction23x(Opcode.ADD_INT, 1, 1, 2));
        measuredImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 1));
        BuilderMethod measuredDemo = builder.internMethod(HELLO, "measuredDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), measuredImpl);
        demos.put("measuredDemo", measuredDemo);
        demos.put("layoutDemo", layoutDemo);

        // insetsAnimDemo(): animation callbacks install silently, never fire.
        MutableMethodImplementation insetsAnimImpl = new MutableMethodImplementation(2);
        insetsAnimImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/WindowInsetsAnimation$Callback;")));
        insetsAnimImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        insetsAnimImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 0));
        insetsAnimImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/WindowInsetsAnimation$Callback;", "<init>",
                        Collections.singletonList("I"), "V"))));
        insetsAnimImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Landroid/view/View;")));
        insetsAnimImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        insetsAnimImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 1, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        insetsAnimImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 1, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "setWindowInsetsAnimationCallback",
                        Collections.singletonList(
                                "Landroid/view/WindowInsetsAnimation$Callback;"),
                        "V"))));
        insetsAnimImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod insetsAnimDemo = builder.internMethod(HELLO, "insetsAnimDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), insetsAnimImpl);
        demos.put("insetsAnimDemo", insetsAnimDemo);

        // specDemo(): spec packing round-trips (480 EXACTLY).
        MutableMethodImplementation specImpl = new MutableMethodImplementation(3);
        specImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 0, 480));
        specImpl.addInstruction(new BuilderInstruction31i(Opcode.CONST, 1, 1 << 30));
        specImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 2, 0, 1, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View$MeasureSpec;", "makeMeasureSpec",
                        Arrays.asList("I", "I"), "I"))));
        specImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        specImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View$MeasureSpec;", "getSize",
                        Collections.singletonList("I"), "I"))));
        specImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        specImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View$MeasureSpec;", "getMode",
                        Collections.singletonList("I"), "I"))));
        specImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 1));
        specImpl.addInstruction(new BuilderInstruction23x(Opcode.ADD_INT, 2, 2, 1));
        specImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 2));
        BuilderMethod specDemo = builder.internMethod(HELLO, "specDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), specImpl);
        demos.put("specDemo", specDemo);

        // localeDemo(): the default list holds one US locale.
        MutableMethodImplementation localeImpl = new MutableMethodImplementation(3);
        localeImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 0, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/os/LocaleList;", "getDefault",
                        Collections.<String>emptyList(), "Landroid/os/LocaleList;"))));
        localeImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        localeImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/os/LocaleList;", "size",
                        Collections.<String>emptyList(), "I"))));
        localeImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 1));
        localeImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 0));
        localeImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 2, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/os/LocaleList;", "get",
                        Collections.singletonList("I"), "Ljava/util/Locale;"))));
        localeImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        localeImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/Locale;", "getLanguage",
                        Collections.<String>emptyList(), "Ljava/lang/String;"))));
        localeImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        localeImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/String;", "length",
                        Collections.<String>emptyList(), "I"))));
        localeImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        localeImpl.addInstruction(new BuilderInstruction22b(Opcode.MUL_INT_LIT8, 1, 1, 10));
        localeImpl.addInstruction(new BuilderInstruction23x(Opcode.ADD_INT, 1, 1, 2));
        localeImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 1));
        BuilderMethod localeDemo = builder.internMethod(HELLO, "localeDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), localeImpl);
        demos.put("localeDemo", localeDemo);

        // forceDarkDemo(): setForceDarkAllowed is a silent no-op.
        MutableMethodImplementation forceDarkImpl = new MutableMethodImplementation(2);
        forceDarkImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        forceDarkImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        forceDarkImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        forceDarkImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 0));
        forceDarkImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "setForceDarkAllowed",
                        Collections.singletonList("Z"), "V"))));
        forceDarkImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod forceDarkDemo = builder.internMethod(HELLO, "forceDarkDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), forceDarkImpl);
        demos.put("forceDarkDemo", forceDarkDemo);

        // translationDemo(): setViewTranslationCallback is a silent no-op.
        MutableMethodImplementation translationImpl = new MutableMethodImplementation(2);
        translationImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        translationImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        translationImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        translationImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1,
                0, 0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "setViewTranslationCallback",
                        Collections.singletonList(
                                "Landroid/view/translation/ViewTranslationCallback;"),
                        "V"))));
        translationImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod translationDemo = builder.internMethod(HELLO, "translationDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), translationImpl);
        demos.put("translationDemo", translationDemo);

        // windowFocusDemo(): headless runs foreground, so focus is held.
        MutableMethodImplementation windowFocusImpl = new MutableMethodImplementation(1);
        windowFocusImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        windowFocusImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        windowFocusImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        windowFocusImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "hasWindowFocus",
                        Collections.<String>emptyList(), "Z"))));
        windowFocusImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        windowFocusImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod windowFocusDemo = builder.internMethod(HELLO, "windowFocusDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Z",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), windowFocusImpl);

        // hwDemo(): software rendering only headless.
        MutableMethodImplementation hwImpl = new MutableMethodImplementation(1);
        hwImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        hwImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        hwImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        hwImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "isHardwareAccelerated",
                        Collections.<String>emptyList(), "Z"))));
        hwImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        hwImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod hwDemo = builder.internMethod(HELLO, "hwDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Z",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), hwImpl);
        demos.put("hwDemo", hwDemo);
        demos.put("windowFocusDemo", windowFocusDemo);

        // focusCheckDemo(): window focus held, view/subtree focus never is.
        MutableMethodImplementation focusCheckImpl = new MutableMethodImplementation(3);
        focusCheckImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/view/View;")));
        focusCheckImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        focusCheckImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        focusCheckImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "hasWindowFocus",
                        Collections.<String>emptyList(), "Z"))));
        focusCheckImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 1));
        focusCheckImpl.addInstruction(new BuilderInstruction22b(Opcode.MUL_INT_LIT8, 1, 1, 100));
        focusCheckImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "isFocused",
                        Collections.<String>emptyList(), "Z"))));
        focusCheckImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        focusCheckImpl.addInstruction(new BuilderInstruction22b(Opcode.MUL_INT_LIT8, 2, 2, 10));
        focusCheckImpl.addInstruction(new BuilderInstruction23x(Opcode.ADD_INT, 1, 1, 2));
        focusCheckImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/view/View;", "hasFocus",
                        Collections.<String>emptyList(), "Z"))));
        focusCheckImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        focusCheckImpl.addInstruction(new BuilderInstruction23x(Opcode.ADD_INT, 1, 1, 2));
        focusCheckImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 1));
        BuilderMethod focusCheckDemo = builder.internMethod(HELLO, "focusCheckDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), focusCheckImpl);
        demos.put("focusCheckDemo", focusCheckDemo);

        // stackDemo(): synthesized thread stack names dex frames (length ≥ 2:
        // the Intrinsics hop plus callers).
        MutableMethodImplementation stackImpl = new MutableMethodImplementation(1);
        stackImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 0, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Thread;", "currentThread",
                        Collections.<String>emptyList(), "Ljava/lang/Thread;"))));
        stackImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        stackImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Thread;", "getStackTrace",
                        Collections.<String>emptyList(),
                        "[Ljava/lang/StackTraceElement;"))));
        stackImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        stackImpl.addInstruction(new BuilderInstruction12x(Opcode.ARRAY_LENGTH, 0, 0));
        stackImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod stackDemo = builder.internMethod(HELLO, "stackDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), stackImpl);
        demos.put("stackDemo", stackDemo);

        // traceDemo(): systrace sections are silent no-ops, tracing off.
        MutableMethodImplementation traceImpl = new MutableMethodImplementation(2);
        traceImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 0,
                builder.internStringReference("section")));
        traceImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/os/Trace;", "beginSection",
                        Collections.singletonList("Ljava/lang/String;"), "V"))));
        traceImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 0, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/os/Trace;", "endSection",
                        Collections.<String>emptyList(), "V"))));
        traceImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 0, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/os/Trace;", "isEnabled",
                        Collections.<String>emptyList(), "Z"))));
        traceImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        traceImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod traceDemo = builder.internMethod(HELLO, "traceDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Z",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), traceImpl);
        demos.put("traceDemo", traceDemo);

        // retainedDemo(): nothing retained across config changes headless.
        MutableMethodImplementation retainedImpl = new MutableMethodImplementation(1);
        retainedImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroidx/activity/ComponentActivity;")));
        retainedImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        retainedImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroidx/activity/ComponentActivity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        retainedImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroidx/activity/ComponentActivity;",
                        "getLastNonConfigurationInstance",
                        Collections.<String>emptyList(), "Ljava/lang/Object;"))));
        retainedImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        retainedImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod retainedDemo = builder.internMethod(HELLO, "retainedDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), retainedImpl);
        demos.put("retainedDemo", retainedDemo);

        // bundleDemo(): Bundle string round-trip over a capacity init.
        MutableMethodImplementation bundleImpl = new MutableMethodImplementation(3);
        bundleImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/os/Bundle;")));
        bundleImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        bundleImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 7));
        bundleImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/os/Bundle;", "<init>",
                        Collections.singletonList("I"), "V"))));
        bundleImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("k")));
        bundleImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 2,
                builder.internStringReference("v")));
        bundleImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 3, 0, 1, 2, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/os/Bundle;", "putString",
                        Arrays.asList("Ljava/lang/String;", "Ljava/lang/String;"), "V"))));
        bundleImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/os/Bundle;", "getString",
                        Collections.singletonList("Ljava/lang/String;"),
                        "Ljava/lang/String;"))));
        bundleImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        bundleImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod bundleDemo = builder.internMethod(HELLO, "bundleDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), bundleImpl);
        demos.put("bundleDemo", bundleDemo);

        // bundleIntDemo(): int/boolean/contains round-trip: 7 * 100 + 10 + 1.
        MutableMethodImplementation bundleIntImpl = new MutableMethodImplementation(4);
        bundleIntImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/os/Bundle;")));
        bundleIntImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        bundleIntImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/os/Bundle;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        bundleIntImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("n")));
        bundleIntImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 7));
        bundleIntImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 3, 0, 1, 2,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/os/Bundle;", "putInt",
                        Arrays.asList("Ljava/lang/String;", "I"), "V"))));
        bundleIntImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("b")));
        bundleIntImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 1));
        bundleIntImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 3, 0, 1, 2,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/os/Bundle;", "putBoolean",
                        Arrays.asList("Ljava/lang/String;", "Z"), "V"))));
        bundleIntImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("n")));
        bundleIntImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/os/Bundle;", "getInt",
                        Collections.singletonList("Ljava/lang/String;"), "I"))));
        bundleIntImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 3));
        bundleIntImpl.addInstruction(new BuilderInstruction22b(Opcode.MUL_INT_LIT8, 3, 3, 100));
        bundleIntImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("b")));
        bundleIntImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/os/Bundle;", "getBoolean",
                        Collections.singletonList("Ljava/lang/String;"), "Z"))));
        bundleIntImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        bundleIntImpl.addInstruction(new BuilderInstruction22b(Opcode.MUL_INT_LIT8, 2, 2, 10));
        bundleIntImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("n")));
        bundleIntImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/os/Bundle;", "containsKey",
                        Collections.singletonList("Ljava/lang/String;"), "Z"))));
        bundleIntImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 1));
        bundleIntImpl.addInstruction(new BuilderInstruction23x(Opcode.ADD_INT, 3, 3, 2));
        bundleIntImpl.addInstruction(new BuilderInstruction23x(Opcode.ADD_INT, 3, 3, 1));
        bundleIntImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 3));
        BuilderMethod bundleIntDemo = builder.internMethod(HELLO, "bundleIntDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), bundleIntImpl);
        demos.put("bundleIntDemo", bundleIntDemo);

        // applicationDemo(): getApplication resolves stably per activity.
        MutableMethodImplementation applicationImpl = new MutableMethodImplementation(1);
        applicationImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroidx/activity/ComponentActivity;")));
        applicationImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        applicationImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroidx/activity/ComponentActivity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        applicationImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0,
                0, 0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroidx/activity/ComponentActivity;", "getApplication",
                        Collections.<String>emptyList(), "Landroid/app/Application;"))));
        applicationImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        applicationImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod applicationDemo = builder.internMethod(HELLO, "applicationDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), applicationImpl);
        demos.put("applicationDemo", applicationDemo);

        // intentDemo(): getIntent resolves stably per activity.
        MutableMethodImplementation intentImpl = new MutableMethodImplementation(1);
        intentImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroidx/activity/ComponentActivity;")));
        intentImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        intentImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroidx/activity/ComponentActivity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        intentImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroidx/activity/ComponentActivity;", "getIntent",
                        Collections.<String>emptyList(), "Landroid/content/Intent;"))));
        intentImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        intentImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod intentDemo = builder.internMethod(HELLO, "intentDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), intentImpl);
        demos.put("intentDemo", intentDemo);

        // intentExtrasDemo(): empty Intents carry no extras.
        MutableMethodImplementation intentExtrasImpl = new MutableMethodImplementation(1);
        intentExtrasImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroidx/activity/ComponentActivity;")));
        intentExtrasImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        intentExtrasImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0,
                0, 0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroidx/activity/ComponentActivity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        intentExtrasImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0,
                0, 0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroidx/activity/ComponentActivity;", "getIntent",
                        Collections.<String>emptyList(), "Landroid/content/Intent;"))));
        intentExtrasImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        intentExtrasImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0,
                0, 0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/Intent;", "getExtras",
                        Collections.<String>emptyList(), "Landroid/os/Bundle;"))));
        intentExtrasImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        intentExtrasImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod intentExtrasDemo = builder.internMethod(HELLO, "intentExtrasDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), intentExtrasImpl);
        demos.put("intentExtrasDemo", intentExtrasDemo);

        // iterableDemo(): a host list iterated as bare Iterable is empty.
        MutableMethodImplementation iterableImpl = new MutableMethodImplementation(1);
        iterableImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 0, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/Collections;", "emptyList",
                        Collections.<String>emptyList(), "Ljava/util/List;"))));
        iterableImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        iterableImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_INTERFACE, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Iterable;", "iterator",
                        Collections.<String>emptyList(), "Ljava/util/Iterator;"))));
        iterableImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        iterableImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_INTERFACE, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/Iterator;", "hasNext",
                        Collections.<String>emptyList(), "Z"))));
        iterableImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        iterableImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod iterableDemo = builder.internMethod(HELLO, "iterableDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Z",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), iterableImpl);
        demos.put("iterableDemo", iterableDemo);

        // entryDemo(): a host map entry exposes its key.
        MutableMethodImplementation entryImpl = new MutableMethodImplementation(3);
        entryImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Ljava/util/LinkedHashMap;")));
        entryImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        entryImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/LinkedHashMap;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        entryImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("k")));
        entryImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 2,
                builder.internStringReference("v")));
        entryImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_INTERFACE, 3, 0, 1, 2,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/Map;", "put",
                        Arrays.asList("Ljava/lang/Object;", "Ljava/lang/Object;"),
                        "Ljava/lang/Object;"))));
        entryImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_INTERFACE, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/Map;", "entrySet",
                        Collections.<String>emptyList(), "Ljava/util/Set;"))));
        entryImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        entryImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_INTERFACE, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/Set;", "iterator",
                        Collections.<String>emptyList(), "Ljava/util/Iterator;"))));
        entryImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        entryImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_INTERFACE, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/Iterator;", "next",
                        Collections.<String>emptyList(), "Ljava/lang/Object;"))));
        entryImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        entryImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_INTERFACE, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/Map$Entry;", "getKey",
                        Collections.<String>emptyList(), "Ljava/lang/Object;"))));
        entryImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        entryImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod entryDemo = builder.internMethod(HELLO, "entryDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), entryImpl);
        demos.put("entryDemo", entryDemo);

        // classDemo(): int is primitive, Object is not: 1 * 10 + 0.
        MutableMethodImplementation classPrimImpl = new MutableMethodImplementation(2);
        classPrimImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_CLASS, 0,
                builder.internTypeReference("I")));
        classPrimImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Class;", "isPrimitive",
                        Collections.<String>emptyList(), "Z"))));
        classPrimImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        classPrimImpl.addInstruction(new BuilderInstruction22b(Opcode.MUL_INT_LIT8, 0, 0, 10));
        classPrimImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_CLASS, 1,
                builder.internTypeReference("Ljava/lang/Object;")));
        classPrimImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 1, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Class;", "isPrimitive",
                        Collections.<String>emptyList(), "Z"))));
        classPrimImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 1));
        classPrimImpl.addInstruction(new BuilderInstruction23x(Opcode.ADD_INT, 0, 0, 1));
        classPrimImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod classPrimDemo = builder.internMethod(HELLO, "classPrimDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), classPrimImpl);
        demos.put("classPrimDemo", classPrimDemo);

        // resolverDemo(): getContentResolver resolves stably.
        MutableMethodImplementation resolverImpl = new MutableMethodImplementation(1);
        resolverImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/app/Activity;")));
        resolverImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        resolverImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        resolverImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/Context;", "getContentResolver",
                        Collections.<String>emptyList(),
                        "Landroid/content/ContentResolver;"))));
        resolverImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        resolverImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod resolverDemo = builder.internMethod(HELLO, "resolverDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), resolverImpl);
        demos.put("resolverDemo", resolverDemo);

        // themeDemo(): getTheme resolves stably per context.
        MutableMethodImplementation themeImpl = new MutableMethodImplementation(1);
        themeImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/app/Activity;")));
        themeImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        themeImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        themeImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/Context;", "getTheme",
                        Collections.<String>emptyList(),
                        "Landroid/content/res/Resources$Theme;"))));
        themeImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        themeImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod themeDemo = builder.internMethod(HELLO, "themeDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), themeImpl);
        demos.put("themeDemo", themeDemo);

        // compCbDemo(): component callback (un)registration is a no-op.
        MutableMethodImplementation compCbImpl = new MutableMethodImplementation(2);
        compCbImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/app/Activity;")));
        compCbImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        compCbImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        compCbImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/Context;", "registerComponentCallbacks",
                        Collections.singletonList("Landroid/content/ComponentCallbacks;"),
                        "V"))));
        compCbImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/Context;", "unregisterComponentCallbacks",
                        Collections.singletonList("Landroid/content/ComponentCallbacks;"),
                        "V"))));
        compCbImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod compCbDemo = builder.internMethod(HELLO, "compCbDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), compCbImpl);
        demos.put("compCbDemo", compCbDemo);

        // resColorDemo(): unread resource colors read 0, both overloads.
        MutableMethodImplementation resColorImpl = new MutableMethodImplementation(4);
        resColorImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/app/Activity;")));
        resColorImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        resColorImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        resColorImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/Context;", "getResources",
                        Collections.<String>emptyList(),
                        "Landroid/content/res/Resources;"))));
        resColorImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        resColorImpl.addInstruction(new BuilderInstruction31i(Opcode.CONST, 1, 0x7f010203));
        resColorImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/res/Resources;", "getColor",
                        Collections.singletonList("I"), "I"))));
        resColorImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        resColorImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 3, 0, 1, 3,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/res/Resources;", "getColor",
                        Arrays.asList("I", "Landroid/content/res/Resources$Theme;"), "I"))));
        resColorImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        resColorImpl.addInstruction(new BuilderInstruction23x(Opcode.ADD_INT, 2, 2, 0));
        resColorImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 2));
        BuilderMethod resColorDemo = builder.internMethod(HELLO, "resColorDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), resColorImpl);
        demos.put("resColorDemo", resColorDemo);

        // settingsUriDemo(): Settings.Global.getUriFor builds a Uri.
        MutableMethodImplementation settingsUriImpl = new MutableMethodImplementation(1);
        settingsUriImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 0,
                builder.internStringReference("animator_duration_scale")));
        settingsUriImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/provider/Settings$Global;", "getUriFor",
                        Collections.singletonList("Ljava/lang/String;"),
                        "Landroid/net/Uri;"))));
        settingsUriImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        settingsUriImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod settingsUriDemo = builder.internMethod(HELLO, "settingsUriDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), settingsUriImpl);
        demos.put("settingsUriDemo", settingsUriDemo);

        // contentObserverDemo(): observer construction ignores the handler.
        MutableMethodImplementation contentObserverImpl = new MutableMethodImplementation(2);
        contentObserverImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/database/ContentObserver;")));
        contentObserverImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT,
                0));
        contentObserverImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 0,
                1, 0, 0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/database/ContentObserver;", "<init>",
                        Collections.singletonList("Landroid/os/Handler;"), "V"))));
        contentObserverImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod contentObserverDemo = builder.internMethod(HELLO, "contentObserverDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), contentObserverImpl);
        demos.put("contentObserverDemo", contentObserverDemo);

        // resolverObsDemo(): observer register/unregister is a silent no-op.
        MutableMethodImplementation resolverObsImpl = new MutableMethodImplementation(4);
        resolverObsImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/app/Activity;")));
        resolverObsImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        resolverObsImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        resolverObsImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0,
                0, 0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/Context;", "getContentResolver",
                        Collections.<String>emptyList(),
                        "Landroid/content/ContentResolver;"))));
        resolverObsImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        resolverObsImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("animator_duration_scale")));
        resolverObsImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 1, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/provider/Settings$Global;", "getUriFor",
                        Collections.singletonList("Ljava/lang/String;"),
                        "Landroid/net/Uri;"))));
        resolverObsImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        resolverObsImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 2,
                builder.internTypeReference("Landroid/database/ContentObserver;")));
        resolverObsImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 2));
        resolverObsImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 3, 0));
        resolverObsImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 2, 3, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/database/ContentObserver;", "<init>",
                        Collections.singletonList("Landroid/os/Handler;"), "V"))));
        resolverObsImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 4, 0, 1,
                3, 2, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/ContentResolver;", "registerContentObserver",
                        Arrays.asList("Landroid/net/Uri;", "Z",
                                "Landroid/database/ContentObserver;"),
                        "V"))));
        resolverObsImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 2, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/ContentResolver;", "unregisterContentObserver",
                        Collections.singletonList("Landroid/database/ContentObserver;"),
                        "V"))));
        resolverObsImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod resolverObsDemo = builder.internMethod(HELLO, "resolverObsDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), resolverObsImpl);
        demos.put("resolverObsDemo", resolverObsDemo);

        // settingsFloatDemo(): unread settings fall back to the default.
        MutableMethodImplementation settingsFloatImpl = new MutableMethodImplementation(3);
        settingsFloatImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Landroid/app/Activity;")));
        settingsFloatImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        settingsFloatImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0,
                0, 0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/app/Activity;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        settingsFloatImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0,
                0, 0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/content/Context;", "getContentResolver",
                        Collections.<String>emptyList(),
                        "Landroid/content/ContentResolver;"))));
        settingsFloatImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        settingsFloatImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 1,
                builder.internStringReference("animator_duration_scale")));
        settingsFloatImpl.addInstruction(new BuilderInstruction21ih(Opcode.CONST_HIGH16, 2,
                0x40F00000));
        settingsFloatImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 3, 0, 1,
                2, 0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Landroid/provider/Settings$Global;", "getFloat",
                        Arrays.asList("Landroid/content/ContentResolver;",
                                "Ljava/lang/String;", "F"),
                        "F"))));
        settingsFloatImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        settingsFloatImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod settingsFloatDemo = builder.internMethod(HELLO, "settingsFloatDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "F",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), settingsFloatImpl);
        demos.put("settingsFloatDemo", settingsFloatDemo);

        // getClassDemo(): Counter.getClass().getName() is "test.Counter".
        MutableMethodImplementation getClassImpl = new MutableMethodImplementation(2);
        getClassImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference(COUNTER)));
        getClassImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        getClassImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(COUNTER,
                        "<init>", Collections.<String>emptyList(), "V"))));
        getClassImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Object;", "getClass",
                        Collections.<String>emptyList(), "Ljava/lang/Class;"))));
        getClassImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        getClassImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 1, 0, 0,
                0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Class;", "getName",
                        Collections.<String>emptyList(), "Ljava/lang/String;"))));
        getClassImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        getClassImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod getClassDemo = builder.internMethod(HELLO, "getClassDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/String;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), getClassImpl);
        demos.put("getClassDemo", getClassDemo);

        // getClassHostDemo(): "hi".getClass().getName() is "java.lang.String".
        MutableMethodImplementation getClassHostImpl = new MutableMethodImplementation(2);
        getClassHostImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 0,
                builder.internStringReference("hi")));
        getClassHostImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0,
                0, 0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Object;", "getClass",
                        Collections.<String>emptyList(), "Ljava/lang/Class;"))));
        getClassHostImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        getClassHostImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 1, 0,
                0, 0, 0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Class;", "getName",
                        Collections.<String>emptyList(), "Ljava/lang/String;"))));
        getClassHostImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        getClassHostImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod getClassHostDemo = builder.internMethod(HELLO, "getClassHostDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/String;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), getClassHostImpl);
        demos.put("getClassHostDemo", getClassHostDemo);

        // subDemo(): SubSet (dex subclass of host TreeSet) constructs with
        // a dex comparator, holds 2, and contains(7) is true: returns 1 + 2.
        MutableMethodImplementation subImpl = new MutableMethodImplementation(4);
        subImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Ltest/Cmp;")));
        subImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        subImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 1, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ltest/Cmp;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        subImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Ltest/SubSet;")));
        subImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        subImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 0, 1, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ltest/SubSet;", "<init>",
                        Collections.singletonList("Ljava/util/Comparator;"), "V"))));
        subImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 7));
        subImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 2, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Integer;", "valueOf",
                        Collections.singletonList("I"), "Ljava/lang/Integer;"))));
        subImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 2));
        subImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 2, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ltest/SubSet;", "add",
                        Collections.singletonList("Ljava/lang/Object;"), "Z"))));
        subImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 2, 8));
        subImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 2, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Integer;", "valueOf",
                        Collections.singletonList("I"), "Ljava/lang/Integer;"))));
        subImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 2));
        subImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 2, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ltest/SubSet;", "add",
                        Collections.singletonList("Ljava/lang/Object;"), "Z"))));
        subImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 7));
        subImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 2, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Integer;", "valueOf",
                        Collections.singletonList("I"), "Ljava/lang/Integer;"))));
        subImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 2));
        subImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 2, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ltest/SubSet;", "contains",
                        Collections.singletonList("Ljava/lang/Object;"), "Z"))));
        subImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 3));
        subImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ltest/SubSet;", "size",
                        Collections.<String>emptyList(), "I"))));
        subImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        subImpl.addInstruction(new BuilderInstruction23x(Opcode.ADD_INT, 3, 3, 2));
        subImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 3));
        BuilderMethod subDemo = builder.internMethod(HELLO, "subDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), subImpl);
        demos.put("subDemo", subDemo);

        // hostFillDemo(): host Arrays.fill mutates through the bridge.
        MutableMethodImplementation hostFillImpl = new MutableMethodImplementation(4);
        hostFillImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 3, 2));
        hostFillImpl.addInstruction(new BuilderInstruction22c(Opcode.NEW_ARRAY, 0, 3,
                builder.internTypeReference("[I")));
        hostFillImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 0));
        hostFillImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 2));
        hostFillImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 3, 42));
        hostFillImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 4, 0, 1, 2, 3,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/Arrays;", "fill",
                        Arrays.asList("[I", "I", "I", "I"), "V"))));
        hostFillImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 0));
        hostFillImpl.addInstruction(new BuilderInstruction23x(Opcode.AGET, 0, 0, 1));
        hostFillImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod hostFillDemo = builder.internMethod(HELLO, "hostFillDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), hostFillImpl);
        demos.put("hostFillDemo", hostFillDemo);

        // treeDemo(): host TreeSet built over a dex comparator holds 2.
        MutableMethodImplementation treeImpl = new MutableMethodImplementation(3);
        treeImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Ltest/Cmp;")));
        treeImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        treeImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 1, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ltest/Cmp;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        treeImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 0,
                builder.internTypeReference("Ljava/util/TreeSet;")));
        treeImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        treeImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 2, 0, 1, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/TreeSet;", "<init>",
                        Collections.singletonList("Ljava/util/Comparator;"), "V"))));
        treeImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 7));
        treeImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 1, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Integer;", "valueOf",
                        Collections.singletonList("I"), "Ljava/lang/Integer;"))));
        treeImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        treeImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/TreeSet;", "add",
                        Collections.singletonList("Ljava/lang/Object;"), "Z"))));
        treeImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 1));
        treeImpl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 1, 8));
        treeImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 1, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Integer;", "valueOf",
                        Collections.singletonList("I"), "Ljava/lang/Integer;"))));
        treeImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        treeImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/TreeSet;", "add",
                        Collections.singletonList("Ljava/lang/Object;"), "Z"))));
        treeImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 1));
        treeImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 0, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/TreeSet;", "size",
                        Collections.<String>emptyList(), "I"))));
        treeImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 0));
        treeImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod treeDemo = builder.internMethod(HELLO, "treeDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), treeImpl);
        demos.put("treeDemo", treeDemo);

        // ctorDemo(): forName + getDeclaredConstructor + newInstance on Counter.
        MutableMethodImplementation ctorImpl = new MutableMethodImplementation(3);
        ctorImpl.addInstruction(new BuilderInstruction21c(Opcode.CONST_STRING, 0,
                builder.internStringReference("test.Counter")));
        ctorImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 0));
        ctorImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 0));
        ctorImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 3, 0, 1, 2, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Class;", "forName",
                        Arrays.asList("Ljava/lang/String;", "Z",
                                "Ljava/lang/ClassLoader;"),
                        "Ljava/lang/Class;"))));
        ctorImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        ctorImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 0));
        ctorImpl.addInstruction(new BuilderInstruction22c(Opcode.NEW_ARRAY, 1, 1,
                builder.internTypeReference("[Ljava/lang/Class;")));
        ctorImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Class;", "getDeclaredConstructor",
                        Collections.singletonList("[Ljava/lang/Class;"),
                        "Ljava/lang/reflect/Constructor;"))));
        ctorImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        ctorImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 0));
        ctorImpl.addInstruction(new BuilderInstruction22c(Opcode.NEW_ARRAY, 1, 1,
                builder.internTypeReference("[Ljava/lang/Object;")));
        ctorImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 2, 0, 1, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/reflect/Constructor;", "newInstance",
                        Collections.singletonList("[Ljava/lang/Object;"),
                        "Ljava/lang/Object;"))));
        ctorImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0));
        ctorImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN_OBJECT, 0));
        BuilderMethod ctorDemo = builder.internMethod(HELLO, "ctorDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "Ljava/lang/Object;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), ctorImpl);
        demos.put("ctorDemo", ctorDemo);

        // sortDemo(): sort [3,1,2] over [0,2) ascending, return a[0]*10+a[1].
        MutableMethodImplementation sortImpl = new MutableMethodImplementation(4);
        sortImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 3, 3));
        sortImpl.addInstruction(new BuilderInstruction22c(Opcode.NEW_ARRAY, 0, 3,
                builder.internTypeReference("[Ljava/lang/Object;")));
        sortImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 3));
        sortImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 1, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Integer;", "valueOf",
                        Collections.singletonList("I"), "Ljava/lang/Integer;"))));
        sortImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        sortImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 0));
        sortImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT_OBJECT, 1, 0, 2));
        sortImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 1));
        sortImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 1, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Integer;", "valueOf",
                        Collections.singletonList("I"), "Ljava/lang/Integer;"))));
        sortImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        sortImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 1));
        sortImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT_OBJECT, 1, 0, 2));
        sortImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, 2));
        sortImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 1, 1, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Integer;", "valueOf",
                        Collections.singletonList("I"), "Ljava/lang/Integer;"))));
        sortImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        sortImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 2));
        sortImpl.addInstruction(new BuilderInstruction23x(Opcode.APUT_OBJECT, 1, 0, 2));
        sortImpl.addInstruction(new BuilderInstruction21c(Opcode.NEW_INSTANCE, 1,
                builder.internTypeReference("Ltest/Cmp;")));
        sortImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 1));
        sortImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 1, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ltest/Cmp;", "<init>",
                        Collections.<String>emptyList(), "V"))));
        sortImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 0));
        sortImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 3, 2));
        sortImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 4, 0, 2, 3, 1, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/util/Arrays;", "sort",
                        Arrays.asList("[Ljava/lang/Object;", "I", "I",
                                "Ljava/util/Comparator;"),
                        "V"))));
        sortImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 0));
        sortImpl.addInstruction(new BuilderInstruction23x(Opcode.AGET_OBJECT, 1, 0, 2));
        sortImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 1, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Integer;", "intValue",
                        Collections.<String>emptyList(), "I"))));
        sortImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 1));
        sortImpl.addInstruction(new BuilderInstruction22b(Opcode.MUL_INT_LIT8, 1, 1, 10));
        sortImpl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 2, 1));
        sortImpl.addInstruction(new BuilderInstruction23x(Opcode.AGET_OBJECT, 2, 0, 2));
        sortImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_VIRTUAL, 1, 2, 0, 0, 0, 0,
                builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Integer;", "intValue",
                        Collections.<String>emptyList(), "I"))));
        sortImpl.addInstruction(new BuilderInstruction11x(Opcode.MOVE_RESULT, 2));
        sortImpl.addInstruction(new BuilderInstruction23x(Opcode.ADD_INT, 1, 1, 2));
        sortImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 1));
        BuilderMethod sortDemo = builder.internMethod(HELLO, "sortDemo",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), sortImpl);
        demos.put("sortDemo", sortDemo);
        return demos;
    }


    private static BuilderMethod trivialInit(DexBuilder builder, BuilderMethodReference objectInit,
            String type, String superclass) {
        MutableMethodImplementation impl = new MutableMethodImplementation(1);
        impl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_DIRECT, 1, 0, 0, 0, 0, 0,
                objectInit));
        impl.addInstruction(new BuilderInstruction10x(Opcode.RETURN_VOID));
        return builder.internMethod(type, "<init>",
                Collections.<ImmutableMethodParameter>emptyList(), "V",
                AccessFlags.PUBLIC.getValue() | AccessFlags.CONSTRUCTOR.getValue(),
                Collections.emptySet(), Collections.emptySet(), impl);
    }

    private static void putElement(MutableMethodImplementation impl, DexBuilder builder,
            int index, int value) {
        impl.addInstruction(new BuilderInstruction11n(Opcode.CONST_4, 1, index));
        impl.addInstruction(new BuilderInstruction21s(Opcode.CONST_16, 2, value));
        impl.addInstruction(new BuilderInstruction23x(Opcode.APUT, 2, 0, 1));
    }
}
