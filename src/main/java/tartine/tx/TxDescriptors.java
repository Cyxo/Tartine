package tartine.tx;

import tartine.dex.Descriptors;

/** DEX/JVM descriptor helpers shared by the translator. */
final class TxDescriptors {
    private TxDescriptors() {
    }

    /**
     * Class-file internal name of a DEX descriptor: {@code Ljava/lang/String;}
     * becomes {@code java/lang/String}; array descriptors pass through.
     */
    static String internal(String desc) {
        if (desc.startsWith("[")) {
            return desc;
        }
        return desc.substring(1, desc.length() - 1);
    }

    /** Whether a descriptor is a wide-typed value (J or D). */
    static boolean isWide(String desc) {
        return Descriptors.isWide(desc);
    }

    /**
     * Element type of an array descriptor, or {@code null} when not an array.
     * Mirrors the DEX element kind for {@code <X>anewarray/newarray} emission.
     */
    static String arrayElement(String arrayDesc) {
        if (!arrayDesc.startsWith("[")) {
            throw new IllegalArgumentException("not an array: " + arrayDesc);
        }
        return arrayDesc.substring(1);
    }

    /**
     * ASM newarray opcode base type for primitive element descriptors, or -1
     * when the element is a reference (use anewarray instead).
     */
    static int newarrayBase(String elementDesc) {
        switch (elementDesc) {
            case "I": return org.objectweb.asm.Opcodes.T_INT;
            case "J": return org.objectweb.asm.Opcodes.T_LONG;
            case "F": return org.objectweb.asm.Opcodes.T_FLOAT;
            case "D": return org.objectweb.asm.Opcodes.T_DOUBLE;
            case "Z": return org.objectweb.asm.Opcodes.T_BOOLEAN;
            case "B": return org.objectweb.asm.Opcodes.T_BYTE;
            case "C": return org.objectweb.asm.Opcodes.T_CHAR;
            case "S": return org.objectweb.asm.Opcodes.T_SHORT;
            default: return -1;
        }
    }
}
