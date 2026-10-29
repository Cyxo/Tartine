package tartine.fw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Framework type universe: hierarchy, member signatures, and true constants,
 * parsed from a platform {@code android.jar} by {@link FwIndexLoader}.
 *
 * <p>Dev-time only (the {@code fw-tools} module): {@code SkeletonGen} drives
 * skeleton emission from it. The runtime never reads an {@code android.jar}
 * and has no dependency on this model.
 */
public final class FwIndex {
    public static final class FwMethod {
        public final String name;
        public final List<String> params;
        public final String ret;
        /** Classfile access flags (ACC_* bits); 0 for hand-authored entries. */
        public final int access;
        /** Method-level generic Signature string, or null (erasure only). */
        public final String signature;

        public FwMethod(String name, List<String> params, String ret) {
            this(name, params, ret, 0, null);
        }

        public FwMethod(String name, List<String> params, String ret, int access) {
            this(name, params, ret, access, null);
        }

        public FwMethod(String name, List<String> params, String ret, int access,
                String signature) {
            this.name = name;
            this.params = Collections.unmodifiableList(new ArrayList<>(params));
            this.ret = ret;
            this.access = access;
            this.signature = signature;
        }
    }

    public static final class FwField {
        public final String name;
        public final String type;
        /** Classfile access flags (ACC_* bits); 0 for hand-authored entries. */
        public final int access;

        public FwField(String name, String type) {
            this(name, type, 0);
        }

        public FwField(String name, String type, int access) {
            this.name = name;
            this.type = type;
            this.access = access;
        }
    }

    public static final class FwClass {
        public final String descriptor;
        /** Superclass descriptor, or null (only {@code Ljava/lang/Object;}). */
        public final String superclass;
        public final List<String> interfaces;
        public final List<FwMethod> methods;
        public final List<FwField> fields;
        /** True (ConstantValue) static finals by field name. */
        public final Map<String, Object> constants;
        /** Classfile access flags (ACC_* bits); 0 for hand-authored entries. */
        public final int access;
        /** Class-level generic Signature string, or null (hand-authored). */
        public final String signature;

        public FwClass(String descriptor, String superclass, List<String> interfaces,
                List<FwMethod> methods, List<FwField> fields, Map<String, Object> constants) {
            this(descriptor, superclass, interfaces, methods, fields, constants, 0, null);
        }

        public FwClass(String descriptor, String superclass, List<String> interfaces,
                List<FwMethod> methods, List<FwField> fields, Map<String, Object> constants,
                int access) {
            this(descriptor, superclass, interfaces, methods, fields, constants, access, null);
        }

        public FwClass(String descriptor, String superclass, List<String> interfaces,
                List<FwMethod> methods, List<FwField> fields, Map<String, Object> constants,
                int access, String signature) {
            this.descriptor = descriptor;
            this.superclass = superclass;
            this.interfaces = Collections.unmodifiableList(new ArrayList<>(interfaces));
            this.methods = Collections.unmodifiableList(new ArrayList<>(methods));
            this.fields = Collections.unmodifiableList(new ArrayList<>(fields));
            this.constants = Collections.unmodifiableMap(new HashMap<>(constants));
            this.access = access;
            this.signature = signature;
        }

        public boolean hasMethod(String name, List<String> params, String ret) {
            for (FwMethod method : methods) {
                if (method.name.equals(name) && method.params.equals(params)
                        && method.ret.equals(ret)) {
                    return true;
                }
            }
            return false;
        }

        public boolean hasField(String name) {
            for (FwField field : fields) {
                if (field.name.equals(name)) {
                    return true;
                }
            }
            return false;
        }
    }

    private final Map<String, FwClass> classes = new HashMap<>();

    public FwIndex() {
    }

    public void put(FwClass cls) {
        classes.put(cls.descriptor, cls);
    }

    public FwClass find(String descriptor) {
        return classes.get(descriptor);
    }

    public int classCount() {
        return classes.size();
    }
}
