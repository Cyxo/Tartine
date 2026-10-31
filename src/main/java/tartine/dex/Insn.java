package tartine.dex;

import com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.Instruction;
import com.android.tools.smali.dexlib2.iface.instruction.NarrowLiteralInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.OffsetInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.RegisterRangeInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.SwitchElement;
import com.android.tools.smali.dexlib2.iface.instruction.SwitchPayload;
import com.android.tools.smali.dexlib2.iface.instruction.ThreeRegisterInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.WideLiteralInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.formats.ArrayPayload;
import com.android.tools.smali.dexlib2.iface.reference.FieldReference;
import com.android.tools.smali.dexlib2.iface.reference.MethodReference;
import com.android.tools.smali.dexlib2.iface.reference.Reference;
import com.android.tools.smali.dexlib2.iface.reference.StringReference;
import com.android.tools.smali.dexlib2.iface.reference.TypeReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * One Dalvik instruction in our own model, decoupled from dexlib2 types.
 *
 * <p>References are typed at decode time ({@link MethodRef}, {@link FieldRef},
 * {@link TypeRef}, {@link StringRef}) so the translator never re-parses
 * reference text. {@link #text()} keeps the historical smali-like rendering
 * for dumps and gap messages.
 */
public final class Insn {
    /** A decoded instruction reference. */
    public sealed interface Ref {
        /** Smali-like rendering, stable across dumps. */
        String text();
    }

    /** {@code Lowner;->name(params)ret}. */
    public record MethodRef(String owner, String name, List<String> parameterTypes,
            String returnType) implements Ref {
        public MethodRef {
            parameterTypes = List.copyOf(parameterTypes);
        }

        /** JVM descriptor, e.g. {@code (ILjava/lang/String;)V}. */
        public String descriptor() {
            return "(" + String.join("", parameterTypes) + ")" + returnType;
        }

        @Override
        public String text() {
            return owner + "->" + name + descriptor();
        }
    }

    /** {@code Lowner;->name:type}. */
    public record FieldRef(String owner, String name, String type) implements Ref {
        @Override
        public String text() {
            return owner + "->" + name + ":" + type;
        }
    }

    /** A type descriptor, e.g. {@code Ljava/lang/String;} or {@code [I}. */
    public record TypeRef(String descriptor) implements Ref {
        @Override
        public String text() {
            return descriptor;
        }
    }

    /** A {@code const-string} payload. */
    public record StringRef(String value) implements Ref {
        @Override
        public String text() {
            return value;
        }
    }

    /**
     * Reference kinds the translator has no typed view of yet (method
     * prototypes, call sites, method handles). Kept verbatim so dumps stay
     * stable and translation can surface a named gap.
     */
    public record RawRef(String value) implements Ref {
        @Override
        public String text() {
            return value;
        }
    }

    /** Offset in 16-bit code units from the start of the method. */
    public final int codeOffset;
    /** Dasherized opcode enum name, e.g. {@code add-int-2addr}. */
    public final String opcode;
    public final List<Integer> registers;
    public final boolean hasLiteral;
    public final long literal;
    /** Typed reference (method/field/string/type), or null when none. */
    public final Ref reference;
    /** Relative branch target in code units, or null when N/A. */
    public final Integer branchOffset;
    /** Array-payload element width in bytes, or 0 when N/A. */
    public final int elementWidth;
    /** Array-payload elements as raw longs, or null when N/A. */
    public final List<Long> payloadData;
    /** Switch-payload keys, or null when N/A. */
    public final List<Integer> payloadKeys;
    /** Switch-payload targets, relative to the switch, or null when N/A. */
    public final List<Integer> payloadTargets;
    /**
     * Index of the payload instruction in the method's instruction list, or
     * -1 when this instruction carries no payload reference.
     */
    public final int payloadIndex;

    Insn(int codeOffset, String opcode, List<Integer> registers, boolean hasLiteral, long literal,
            Ref reference, Integer branchOffset, int elementWidth,
            List<Long> payloadData, List<Integer> payloadKeys, List<Integer> payloadTargets,
            int payloadIndex) {
        this.codeOffset = codeOffset;
        this.opcode = opcode;
        this.registers = Collections.unmodifiableList(new ArrayList<>(registers));
        this.hasLiteral = hasLiteral;
        this.literal = literal;
        this.reference = reference;
        this.branchOffset = branchOffset;
        this.elementWidth = elementWidth;
        this.payloadData = payloadData == null ? null
                : Collections.unmodifiableList(new ArrayList<>(payloadData));
        this.payloadKeys = payloadKeys == null ? null
                : Collections.unmodifiableList(new ArrayList<>(payloadKeys));
        this.payloadTargets = payloadTargets == null ? null
                : Collections.unmodifiableList(new ArrayList<>(payloadTargets));
        this.payloadIndex = payloadIndex;
    }

    /** Copy carrying the resolved payload index (payloads decode after the list). */
    Insn withPayloadIndex(int index) {
        return new Insn(codeOffset, opcode, registers, hasLiteral, literal, reference,
                branchOffset, elementWidth, payloadData, payloadKeys, payloadTargets, index);
    }

    static Insn from(Instruction instruction, int offset) {
        List<Integer> registers = new ArrayList<>();
        if (instruction instanceof FiveRegisterInstruction) {
            FiveRegisterInstruction five = (FiveRegisterInstruction) instruction;
            int count = five.getRegisterCount();
            if (count > 0) {
                registers.add(five.getRegisterC());
            }
            if (count > 1) {
                registers.add(five.getRegisterD());
            }
            if (count > 2) {
                registers.add(five.getRegisterE());
            }
            if (count > 3) {
                registers.add(five.getRegisterF());
            }
            if (count > 4) {
                registers.add(five.getRegisterG());
            }
        } else if (instruction instanceof RegisterRangeInstruction) {
            RegisterRangeInstruction range = (RegisterRangeInstruction) instruction;
            for (int i = 0; i < range.getRegisterCount(); i++) {
                registers.add(range.getStartRegister() + i);
            }
        } else if (instruction instanceof ThreeRegisterInstruction) {
            ThreeRegisterInstruction three = (ThreeRegisterInstruction) instruction;
            registers.add(three.getRegisterA());
            registers.add(three.getRegisterB());
            registers.add(three.getRegisterC());
        } else if (instruction instanceof TwoRegisterInstruction) {
            TwoRegisterInstruction two = (TwoRegisterInstruction) instruction;
            registers.add(two.getRegisterA());
            registers.add(two.getRegisterB());
        } else if (instruction instanceof OneRegisterInstruction) {
            registers.add(((OneRegisterInstruction) instruction).getRegisterA());
        }

        boolean hasLiteral = false;
        long literal = 0;
        if (instruction instanceof NarrowLiteralInstruction) {
            hasLiteral = true;
            literal = ((NarrowLiteralInstruction) instruction).getNarrowLiteral();
        } else if (instruction instanceof WideLiteralInstruction) {
            hasLiteral = true;
            literal = ((WideLiteralInstruction) instruction).getWideLiteral();
        }

        Ref reference = null;
        if (instruction instanceof ReferenceInstruction) {
            reference = decode(((ReferenceInstruction) instruction).getReference());
        }

        Integer branchOffset = null;
        if (instruction instanceof OffsetInstruction) {
            branchOffset = ((OffsetInstruction) instruction).getCodeOffset();
        }

        int elementWidth = 0;
        List<Long> payloadData = null;
        if (instruction instanceof ArrayPayload) {
            ArrayPayload payload = (ArrayPayload) instruction;
            elementWidth = payload.getElementWidth();
            payloadData = new ArrayList<>();
            for (Number element : payload.getArrayElements()) {
                payloadData.add(element.longValue());
            }
        }
        List<Integer> payloadKeys = null;
        List<Integer> payloadTargets = null;
        if (instruction instanceof SwitchPayload) {
            payloadKeys = new ArrayList<>();
            payloadTargets = new ArrayList<>();
            for (SwitchElement element
                    : ((SwitchPayload) instruction).getSwitchElements()) {
                payloadKeys.add(element.getKey());
                payloadTargets.add(element.getOffset());
            }
        }

        String opcode = instruction.getOpcode().name()
                .toLowerCase(Locale.ROOT).replace('_', '-');
        return new Insn(offset, opcode, registers, hasLiteral, literal,
                reference, branchOffset, elementWidth, payloadData,
                payloadKeys, payloadTargets, -1);
    }

    private static Ref decode(Reference reference) {
        if (reference instanceof MethodReference method) {
            List<String> params = new ArrayList<>();
            for (CharSequence type : method.getParameterTypes()) {
                params.add(type.toString());
            }
            return new MethodRef(method.getDefiningClass(), method.getName(), params,
                    method.getReturnType());
        }
        if (reference instanceof FieldReference field) {
            return new FieldRef(field.getDefiningClass(), field.getName(), field.getType());
        }
        if (reference instanceof TypeReference type) {
            return new TypeRef(type.getType());
        }
        if (reference instanceof StringReference string) {
            return new StringRef(string.getString());
        }
        return new RawRef(String.valueOf(reference));
    }
}
