package aine.dex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.Method;

/** One class in our own model. */
public final class DexClass {
    /** Type descriptor, e.g. {@code Ltest/Hello;}. */
    public final String type;
    public final List<DexMethod> methods;

    DexClass(String type, List<DexMethod> methods) {
        this.type = type;
        this.methods = Collections.unmodifiableList(new ArrayList<>(methods));
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
            methods.add(DexMethod.from(method));
        }
        return new DexClass(classDef.getType(), methods);
    }
}
