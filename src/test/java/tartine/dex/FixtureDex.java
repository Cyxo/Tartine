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

        builder.internClassDef(HELLO,
                AccessFlags.PUBLIC.getValue(), "Ljava/lang/Object;",
                Collections.<String>emptyList(), "Hello.java",
                Collections.emptySet(),
                Collections.<org.jf.dexlib2.writer.builder.BuilderField>emptyList(),
                Arrays.asList(init, id, twice, greet, make, sum, len, safely, clinit, boot,
                        nullCast, castOk, castFail, cmpTiny, sb, throwCatch, intBox, arrayCopy,
                        appendableCast, badCast, stringOps, mathMax, missing, vis, sdk, fwCast,
                        fwGhost));
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
