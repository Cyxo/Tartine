package tartine.dex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.android.tools.smali.dexlib2.iface.ExceptionHandler;
import com.android.tools.smali.dexlib2.iface.ClassDef;
import com.android.tools.smali.dexlib2.iface.Method;
import com.android.tools.smali.dexlib2.iface.MethodImplementation;
import com.android.tools.smali.dexlib2.iface.MethodParameter;
import com.android.tools.smali.dexlib2.iface.TryBlock;
import com.android.tools.smali.dexlib2.iface.instruction.Instruction;

/** One method in our own model. */
public final class DexMethod {
    /** One catch handler: exception type, or null for catch-all. */
    public static final class Handler {
        public final String type;
        /** Handler address in code units. */
        public final int address;

        public Handler(String type, int address) {
            this.type = type;
            this.address = address;
        }
    }

    /** Protected range: start inclusive, end exclusive, in code units. */
    public static final class Try {
        public final int start;
        public final int end;
        public final List<Handler> handlers;

        public Try(int start, int end, List<Handler> handlers) {
            this.start = start;
            this.end = end;
            this.handlers = Collections.unmodifiableList(new ArrayList<>(handlers));
        }
    }

    public final String name;
    public final List<String> parameterTypes;
    public final String returnType;
    public final int accessFlags;
    /** Owning class descriptor, or null for synthetic methods. */
    public final String owner;
    public final boolean hasImplementation;
    public final int registerCount;
    public final List<Insn> instructions;
    public final List<Try> tries;

    public DexMethod(String name, List<String> parameterTypes, String returnType, int accessFlags,
            String owner, boolean hasImplementation, int registerCount, List<Insn> instructions,
            List<Try> tries) {
        this.name = name;
        this.parameterTypes = Collections.unmodifiableList(new ArrayList<>(parameterTypes));
        this.returnType = returnType;
        this.accessFlags = accessFlags;
        this.owner = owner;
        this.hasImplementation = hasImplementation;
        this.registerCount = registerCount;
        this.instructions = Collections.unmodifiableList(new ArrayList<>(instructions));
        this.tries = Collections.unmodifiableList(new ArrayList<>(tries));
    }

    /** Smali-like signature, e.g. {@code add(II)I}. */
    public String signature() {
        StringBuilder out = new StringBuilder(name).append('(');
        for (String param : parameterTypes) {
            out.append(param);
        }
        return out.append(')').append(returnType).toString();
    }

    static DexMethod from(ClassDef classDef, Method method) {
        List<String> params = new ArrayList<>();
        for (MethodParameter param : method.getParameters()) {
            params.add(param.getType());
        }
        MethodImplementation impl = method.getImplementation();
        List<Insn> instructions = new ArrayList<>();
        List<Try> tries = new ArrayList<>();
        int registerCount = 0;
        if (impl != null) {
            registerCount = impl.getRegisterCount();
            int offset = 0;
            for (Instruction instruction : impl.getInstructions()) {
                instructions.add(Insn.from(instruction, offset));
                offset += instruction.getCodeUnits();
            }
            for (TryBlock<?> tryBlock
                    : (List<TryBlock<?>>) (List<?>) impl.getTryBlocks()) {
                List<Handler> handlers = new ArrayList<>();
                for (ExceptionHandler handler
                        : (List<ExceptionHandler>) (List<?>)
                            tryBlock.getExceptionHandlers()) {
                    handlers.add(new Handler(handler.getExceptionType(),
                            handler.getHandlerCodeAddress()));
                }
                tries.add(new Try(tryBlock.getStartCodeAddress(),
                        tryBlock.getStartCodeAddress() + tryBlock.getCodeUnitCount(),
                        handlers));
            }
        }
        return new DexMethod(method.getName(), params, method.getReturnType(),
                method.getAccessFlags(), classDef.getType(), impl != null, registerCount,
                instructions, tries);
    }
}
