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
        /**
         * DEX static-field initial value (dex {@code static_values_off},
         * via dexlib2 Field#getInitialValue), pre-boxed to its Java value
         * (Integer/Long/String/...); null when the dex carries none. Java
         * folds it into the class's ConstantValue attribute for static
         * final primitives/strings so {@code R.id.*} ints resolve without
         * <clinit> bytecode (DEX applies these before <clinit> too).
         */
        public final Object initialValue;

        public DexField(String name, String type, int accessFlags, Object initialValue) {
            this.name = name;
            this.type = type;
            this.accessFlags = accessFlags;
            this.initialValue = initialValue;
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
                    field.getAccessFlags(), javaInitialValue(field.getInitialValue())));
        }
        return new DexClass(classDef.getType(), classDef.getAccessFlags(),
                classDef.getSuperclass(), interfaces, methods, fields);
    }

    /**
     * Boxes a dexlib2 EncodedValue into its Java constant representative
     * for the classfile ConstantValue attribute. Arrays/enums/type/method
     * refs cannot be ConstantValues — they answer null (the loader seeds
     * nothing; the guest's <clinit> keeps owning any runtime setup for
     * those, exactly as today's no-value path behaves).
     */
    private static Object javaInitialValue(
            com.android.tools.smali.dexlib2.iface.value.EncodedValue ev) {
        if (ev instanceof com.android.tools.smali.dexlib2.iface.value.IntEncodedValue v) {
            return v.getValue();
        }
        if (ev instanceof com.android.tools.smali.dexlib2.iface.value.LongEncodedValue v) {
            return v.getValue();
        }
        if (ev instanceof com.android.tools.smali.dexlib2.iface.value.ShortEncodedValue v) {
            return v.getValue();
        }
        if (ev instanceof com.android.tools.smali.dexlib2.iface.value.ByteEncodedValue v) {
            return v.getValue();
        }
        if (ev instanceof com.android.tools.smali.dexlib2.iface.value.CharEncodedValue v) {
            return v.getValue();
        }
        if (ev instanceof com.android.tools.smali.dexlib2.iface.value.FloatEncodedValue v) {
            return v.getValue();
        }
        if (ev instanceof
                com.android.tools.smali.dexlib2.iface.value.DoubleEncodedValue v) {
            return v.getValue();
        }
        if (ev instanceof
                com.android.tools.smali.dexlib2.iface.value.BooleanEncodedValue v) {
            return v.getValue();
        }
        if (ev instanceof
                com.android.tools.smali.dexlib2.iface.value.StringEncodedValue v) {
            return v.getValue();
        }
        return null;
    }
}
