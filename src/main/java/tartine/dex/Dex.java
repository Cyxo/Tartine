package tartine.dex;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.android.tools.smali.dexlib2.DexFileFactory;
import com.android.tools.smali.dexlib2.Opcodes;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.iface.ClassDef;
import com.android.tools.smali.dexlib2.iface.DexFile;

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
        return fromIface(DexFileFactory.loadDexFile(file, Opcodes.getDefault()));
    }

    public static Dex load(byte[] bytes) throws IOException {
        try {
            return fromIface(new DexBackedDexFile(Opcodes.getDefault(), bytes));
        } catch (RuntimeException e) {
            throw new IOException("not a dex file", e);
        }
    }

    private static Dex fromIface(DexFile dexFile) {
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
