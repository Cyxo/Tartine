package aine.dex;

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

        builder.internClassDef(HELLO,
                AccessFlags.PUBLIC.getValue(), "Ljava/lang/Object;",
                Collections.<String>emptyList(), "Hello.java",
                Collections.emptySet(),
                Collections.<org.jf.dexlib2.writer.builder.BuilderField>emptyList(),
                Arrays.asList(init, id, twice, greet, make, sum, len, safely, clinit, boot,
                        nullCast, castOk, castFail, cmpTiny));
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
