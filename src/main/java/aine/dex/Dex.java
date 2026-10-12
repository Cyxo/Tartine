package aine.dex;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jf.dexlib2.DexFileFactory;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.DexFile;

/**
 * Loads a {@code classes.dex} file into our own model via dexlib2.
 * Entry point for everything downstream (dump, interpreter, translator).
 */
public final class Dex {
    private final List<DexClass> classes;

    Dex(List<DexClass> classes) {
        this.classes = classes;
    }

    public static Dex load(File file) throws IOException {
        DexFile dexFile = DexFileFactory.loadDexFile(file, Opcodes.getDefault());
        List<DexClass> classes = new ArrayList<>();
        for (ClassDef classDef : dexFile.getClasses()) {
            classes.add(DexClass.from(classDef));
        }
        return new Dex(classes);
    }

    public List<DexClass> classes() {
        return classes;
    }

    /** Class by type descriptor, or null. */
    public DexClass findClass(String type) {
        for (DexClass cls : classes) {
            if (cls.type.equals(type)) {
                return cls;
            }
        }
        return null;
    }
}
