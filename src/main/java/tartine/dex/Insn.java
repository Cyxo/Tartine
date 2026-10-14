package tartine.dex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jf.dexlib2.iface.instruction.FiveRegisterInstruction;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.NarrowLiteralInstruction;
import org.jf.dexlib2.iface.instruction.OffsetInstruction;
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction;
import org.jf.dexlib2.iface.instruction.ReferenceInstruction;
import org.jf.dexlib2.iface.instruction.RegisterRangeInstruction;
import org.jf.dexlib2.iface.instruction.SwitchElement;
import org.jf.dexlib2.iface.instruction.SwitchPayload;
import org.jf.dexlib2.iface.instruction.ThreeRegisterInstruction;
import org.jf.dexlib2.iface.instruction.TwoRegisterInstruction;
import org.jf.dexlib2.iface.instruction.WideLiteralInstruction;
import org.jf.dexlib2.iface.instruction.formats.ArrayPayload;

/**
 * One Dalvik instruction in our own model, decoupled from dexlib2 types.
 *
 * <p>v1 covers structure (registers, literals, references, branch targets).
 * Payload bodies (switch/fill-array data) and debug info land with the VM
 * step; such instructions still appear with their opcode and offset.
 */
public final class Insn {
    /** Offset in 16-bit code units from the start of the method. */
    public final int codeOffset;
    /** Dasherized opcode enum name, e.g. {@code add-int-2addr}. */
    public final String opcode;
    public final List<Integer> registers;
    public final boolean hasLiteral;
    public final long literal;
    /** Reference text (method/field/string/type), or null when none. */
    public final String reference;
    /** Reference kind, or -1 when none. */
    public final int referenceType;
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

    Insn(int codeOffset, String opcode, List<Integer> registers, boolean hasLiteral, long literal,
            String reference, int referenceType, Integer branchOffset, int elementWidth,
            List<Long> payloadData, List<Integer> payloadKeys, List<Integer> payloadTargets) {
        this.codeOffset = codeOffset;
        this.opcode = opcode;
        this.registers = Collections.unmodifiableList(new ArrayList<>(registers));
        this.hasLiteral = hasLiteral;
        this.literal = literal;
        this.reference = reference;
        this.referenceType = referenceType;
        this.branchOffset = branchOffset;
        this.elementWidth = elementWidth;
        this.payloadData = payloadData == null ? null
                : Collections.unmodifiableList(new ArrayList<>(payloadData));
        this.payloadKeys = payloadKeys == null ? null
                : Collections.unmodifiableList(new ArrayList<>(payloadKeys));
        this.payloadTargets = payloadTargets == null ? null
                : Collections.unmodifiableList(new ArrayList<>(payloadTargets));
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

        String reference = null;
        int referenceType = -1;
        if (instruction instanceof ReferenceInstruction) {
            ReferenceInstruction ref = (ReferenceInstruction) instruction;
            reference = String.valueOf(ref.getReference());
            referenceType = ref.getReferenceType();
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
                .toLowerCase(java.util.Locale.ROOT).replace('_', '-');
        return new Insn(offset, opcode, registers, hasLiteral, literal,
                reference, referenceType, branchOffset, elementWidth, payloadData,
                payloadKeys, payloadTargets);
    }
}
