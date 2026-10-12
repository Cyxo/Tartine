package aine.dex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.MethodParameter;
import org.jf.dexlib2.iface.instruction.Instruction;

/** One method in our own model. */
public final class DexMethod {
    public final String name;
    public final List<String> parameterTypes;
    public final String returnType;
    public final int accessFlags;
    public final boolean hasImplementation;
    public final int registerCount;
    public final List<Insn> instructions;

    DexMethod(String name, List<String> parameterTypes, String returnType, int accessFlags,
            boolean hasImplementation, int registerCount, List<Insn> instructions) {
        this.name = name;
        this.parameterTypes = Collections.unmodifiableList(new ArrayList<>(parameterTypes));
        this.returnType = returnType;
        this.accessFlags = accessFlags;
        this.hasImplementation = hasImplementation;
        this.registerCount = registerCount;
        this.instructions = Collections.unmodifiableList(new ArrayList<>(instructions));
    }

    /** Smali-like signature, e.g. {@code add(II)I}. */
    public String signature() {
        StringBuilder out = new StringBuilder(name).append('(');
        for (String param : parameterTypes) {
            out.append(param);
        }
        return out.append(')').append(returnType).toString();
    }

    static DexMethod from(Method method) {
        List<String> params = new ArrayList<>();
        for (MethodParameter param : method.getParameters()) {
            params.add(param.getType());
        }
        MethodImplementation impl = method.getImplementation();
        List<Insn> instructions = new ArrayList<>();
        int registerCount = 0;
        if (impl != null) {
            registerCount = impl.getRegisterCount();
            int offset = 0;
            for (Instruction instruction : impl.getInstructions()) {
                instructions.add(Insn.from(instruction, offset));
                offset += instruction.getCodeUnits();
            }
        }
        return new DexMethod(method.getName(), params, method.getReturnType(),
                method.getAccessFlags(), impl != null, registerCount, instructions);
    }
}
