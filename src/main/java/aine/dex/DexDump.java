package aine.dex;

/** Stable text rendering of our DEX model, one instruction per line.
 * Opcodes are lower-cased enum names with dashes ({@code add-int-2addr});
 * methods appear in dex (sorted) order. */
public final class DexDump {
    private DexDump() {
    }

    public static String dump(DexClass cls) {
        StringBuilder out = new StringBuilder();
        out.append("class ").append(cls.type).append('\n');
        for (DexMethod method : cls.methods) {
            out.append("method ").append(method.signature())
                    .append(" regs=").append(method.registerCount).append('\n');
            for (Insn insn : method.instructions) {
                out.append("  ").append(insn.codeOffset).append(": ").append(insn.opcode);
                for (int i = 0; i < insn.registers.size(); i++) {
                    out.append(i == 0 ? " " : ", ");
                    out.append('v').append(insn.registers.get(i));
                }
                if (insn.hasLiteral) {
                    out.append(", #").append(insn.literal);
                }
                if (insn.reference != null) {
                    out.append(", ").append(insn.reference);
                }
                if (insn.branchOffset != null) {
                    out.append(", +").append(insn.branchOffset);
                }
                out.append('\n');
            }
        }
        return out.toString();
    }
}
