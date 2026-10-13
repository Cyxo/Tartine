package tartine.dex;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;

import org.jf.dexlib2.AccessFlags;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.builder.Label;
import org.jf.dexlib2.builder.MutableMethodImplementation;
import org.jf.dexlib2.builder.Label;
import org.jf.dexlib2.builder.instruction.BuilderInstruction10x;
import org.jf.dexlib2.builder.instruction.BuilderInstruction11n;
import org.jf.dexlib2.builder.instruction.BuilderInstruction11x;
import org.jf.dexlib2.builder.instruction.BuilderInstruction12x;
import org.jf.dexlib2.builder.instruction.BuilderInstruction21c;
import org.jf.dexlib2.builder.instruction.BuilderInstruction21s;
import org.jf.dexlib2.builder.instruction.BuilderInstruction21s;
import org.jf.dexlib2.builder.instruction.BuilderInstruction22b;
import org.jf.dexlib2.builder.instruction.BuilderInstruction22c;
import org.jf.dexlib2.builder.instruction.BuilderInstruction23x;
import org.jf.dexlib2.builder.instruction.BuilderInstruction35c;
import org.jf.dexlib2.immutable.ImmutableMethodParameter;
import org.jf.dexlib2.immutable.reference.ImmutableFieldReference;
import org.jf.dexlib2.immutable.reference.ImmutableMethodReference;
import org.jf.dexlib2.writer.builder.BuilderFieldReference;
import org.jf.dexlib2.writer.builder.BuilderMethod;
import org.jf.dexlib2.writer.builder.BuilderMethodReference;
import org.jf.dexlib2.writer.builder.DexBuilder;
import org.jf.dexlib2.writer.io.FileDataStore;

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
                Collections.<org.jf.dexlib2.writer.builder.BuilderField>emptyList(),
                Collections.singletonList(counterInit));
        builder.internClassDef(BASE, AccessFlags.PUBLIC.getValue(), "Ljava/lang/Object;",
                Collections.<String>emptyList(), "Base.java",
                Collections.emptySet(),
                Collections.<org.jf.dexlib2.writer.builder.BuilderField>emptyList(),
                Collections.singletonList(baseInit));
        builder.internClassDef(SUB, AccessFlags.PUBLIC.getValue(), BASE,
                Collections.<String>emptyList(), "Sub.java",
                Collections.emptySet(),
                Collections.<org.jf.dexlib2.writer.builder.BuilderField>emptyList(),
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
                Collections.<org.jf.dexlib2.writer.builder.BuilderField>emptyList(),
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
                Collections.<org.jf.dexlib2.writer.builder.BuilderField>emptyList(),
                Collections.singletonList(orphanInit));

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
        MutableMethodImplementation bootImpl = new MutableMethodImplementation(1);
        bootImpl.addInstruction(new BuilderInstruction21c(Opcode.SGET, 0, counterStatic));
        bootImpl.addInstruction(new BuilderInstruction22b(Opcode.ADD_INT_LIT8, 0, 0, 1));
        bootImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod boot = builder.internMethod(HELLO, "boot",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), bootImpl);

        // nullCast(): check-cast null to Counter (passes).
        MutableMethodImplementation nullCastImpl = new MutableMethodImplementation(1);
        nullCastImpl.addInstruction(new BuilderInstruction21c(Opcode.CHECK_CAST, 0,
                builder.internTypeReference(COUNTER)));
        nullCastImpl.addInstruction(new BuilderInstruction10x(Opcode.RETURN_VOID));
        BuilderMethod nullCast = builder.internMethod(HELLO, "nullCast",
                Collections.<ImmutableMethodParameter>emptyList(), "V",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), nullCastImpl);

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
        MutableMethodImplementation sdkImpl = new MutableMethodImplementation(1);
        sdkImpl.addInstruction(new BuilderInstruction21c(Opcode.SGET, 0,
                builder.internFieldReference(new ImmutableFieldReference(
                        "Landroid/os/Build$VERSION;", "SDK_INT", "I"))));
        sdkImpl.addInstruction(new BuilderInstruction11x(Opcode.RETURN, 0));
        BuilderMethod sdk = builder.internMethod(HELLO, "sdk",
                Collections.<ImmutableMethodParameter>emptyList(), "I",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), sdkImpl);

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

        // Kotlin-shaped helpers (LazyKt has NO methods: tests the dex-miss path).
        builder.internClassDef("Lkotlin/Lazy;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.INTERFACE.getValue()
                        | AccessFlags.ABSTRACT.getValue(),
                "Ljava/lang/Object;", Collections.<String>emptyList(), "Lazy.kt",
                Collections.emptySet(),
                Collections.<org.jf.dexlib2.writer.builder.BuilderField>emptyList(),
                Collections.<org.jf.dexlib2.writer.builder.BuilderMethod>emptyList());
        builder.internClassDef("Lkotlin/LazyKt;",
                AccessFlags.PUBLIC.getValue(), "Ljava/lang/Object;",
                Collections.<String>emptyList(), "LazyKt.kt",
                Collections.emptySet(),
                Collections.<org.jf.dexlib2.writer.builder.BuilderField>emptyList(),
                Collections.<org.jf.dexlib2.writer.builder.BuilderMethod>emptyList());
        builder.internClassDef("Lkotlin/jvm/functions/Function0;",
                AccessFlags.PUBLIC.getValue() | AccessFlags.INTERFACE.getValue()
                        | AccessFlags.ABSTRACT.getValue(),
                "Ljava/lang/Object;", Collections.<String>emptyList(), "Function0.kt",
                Collections.emptySet(),
                Collections.<org.jf.dexlib2.writer.builder.BuilderField>emptyList(),
                Collections.<org.jf.dexlib2.writer.builder.BuilderMethod>emptyList());
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
                Collections.<org.jf.dexlib2.writer.builder.BuilderField>emptyList(),
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
                Collections.<org.jf.dexlib2.writer.builder.BuilderField>emptyList(),
                Arrays.asList(jobInit, jobRun));

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
                Collections.<org.jf.dexlib2.writer.builder.BuilderField>emptyList(),
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
                Collections.<org.jf.dexlib2.writer.builder.BuilderField>emptyList(),
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
                Collections.<org.jf.dexlib2.writer.builder.BuilderField>emptyList(),
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

        // blockedExec(): Runtime.getRuntime stays denied.
        MutableMethodImplementation blockedImpl = new MutableMethodImplementation(1);
        blockedImpl.addInstruction(new BuilderInstruction35c(Opcode.INVOKE_STATIC, 0, 0, 0, 0, 0,
                0, builder.internMethodReference(new ImmutableMethodReference(
                        "Ljava/lang/Runtime;", "getRuntime",
                        Collections.<String>emptyList(), "Ljava/lang/Runtime;"))));
        blockedImpl.addInstruction(new BuilderInstruction10x(Opcode.RETURN_VOID));
        BuilderMethod blockedExec = builder.internMethod(HELLO, "blockedExec",
                Collections.<ImmutableMethodParameter>emptyList(), "V",
                AccessFlags.PUBLIC.getValue() | AccessFlags.STATIC.getValue(),
                Collections.emptySet(), Collections.emptySet(), blockedImpl);

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

        builder.internClassDef(HELLO,
                AccessFlags.PUBLIC.getValue(), "Ljava/lang/Object;",
                Collections.<String>emptyList(), "Hello.java",
                Collections.emptySet(),
                Collections.<org.jf.dexlib2.writer.builder.BuilderField>emptyList(),
                Arrays.asList(init, id, twice, greet, make, sum, len, safely, clinit, boot,
                        nullCast, castOk, castFail, cmpTiny, sb, throwCatch, intBox, arrayCopy,
                        appendableCast, badCast, stringOps, mathMax, missing, vis, sdk, fwCast,
                        fwGhost, fwInit, activityStub, nops, covarOk, covarPrim, constClass,
                        clazzIsInstance, updater, sysClock, longArith, floatArith, doubleArith,
                        lazyDemo, lazyIsInstance, executorDemo, postDemo, handlerDemo,
                        threadDemo, mapDemo, listDemo, setDemo, iterDemo, nullish, nullRecv,
                        boolStr, charStr, weakGet, weakClear, colorCmp, colorName,
                        colorValueOf, mapDefaults, mapPutAbsent, repeatDemo, emptyDemo,
                        strictSqrt, linkedMapDemo, blockedExec, txChain, argbDemo,
                        parseDemo, activityWindow, viewVis));
        builder.writeTo(new FileDataStore(dexFile.toFile()));
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
