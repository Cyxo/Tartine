package tartine.dex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.android.tools.smali.dexlib2.AccessFlags;
import com.android.tools.smali.dexlib2.iface.ClassDef;
import com.android.tools.smali.dexlib2.iface.Field;
import com.android.tools.smali.dexlib2.iface.Method;

/** One class in our own model. */
public final class DexClass {
    /** One field: instance state lives here; statics live in interpreter statics. */
    public static final class DexField {
        public final String name;
        public final String type;
        public final int accessFlags;

        public DexField(String name, String type, int accessFlags) {
            this.name = name;
            this.type = type;
            this.accessFlags = accessFlags;
        }
    }

    /** Type descriptor, e.g. {@code Ltest/Hello;}. */
    public final String type;
    /** Class-level access flags from the dex {@code class_def_item}. */
    public final int accessFlags;
    /** Superclass descriptor, or null (only {@code Ljava/lang/Object;}). */
    public final String superclass;
    public final List<String> interfaces;
    public final List<DexMethod> methods;
    public final List<DexField> fields;

    public DexClass(String type, int accessFlags, String superclass,
            List<String> interfaces, List<DexMethod> methods, List<DexField> fields) {
        this.type = type;
        this.accessFlags = accessFlags;
        this.superclass = superclass;
        this.interfaces = Collections.unmodifiableList(new ArrayList<>(interfaces));
        this.methods = Collections.unmodifiableList(new ArrayList<>(methods));
        this.fields = Collections.unmodifiableList(new ArrayList<>(fields));
    }

    /** True when any field is per-instance state (not static). */
    public boolean hasInstanceFields() {
        for (DexField field : fields) {
            if ((field.accessFlags & AccessFlags.STATIC.getValue()) == 0) {
                return true;
            }
        }
        return false;
    }

    /** First method with the given name, or null. */
    public DexMethod method(String name) {
        for (DexMethod method : methods) {
            if (method.name.equals(name)) {
                return method;
            }
        }
        return null;
    }

    static DexClass from(ClassDef classDef) {
        List<DexMethod> methods = new ArrayList<>();
        for (Method method : classDef.getMethods()) {
            methods.add(DexMethod.from(classDef, method));
        }
        List<String> interfaces = new ArrayList<>(classDef.getInterfaces());
        List<DexField> fields = new ArrayList<>();
        for (Field field : classDef.getFields()) {
            fields.add(new DexField(field.getName(), field.getType(),
                    field.getAccessFlags()));
        }
        return new DexClass(classDef.getType(), classDef.getAccessFlags(),
                classDef.getSuperclass(), interfaces, methods, fields);
    }
}
