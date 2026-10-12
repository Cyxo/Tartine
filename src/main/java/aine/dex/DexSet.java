package aine.dex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * All DEX files of one APK ({@code classes.dex}, {@code classes2.dex}, ...),
 * searched in order. Multidex classes resolve transparently.
 */
public final class DexSet {
    private final List<Dex> dexes;

    public DexSet(List<Dex> dexes) {
        this.dexes = Collections.unmodifiableList(new ArrayList<>(dexes));
    }

    public static DexSet of(Dex... dexes) {
        return new DexSet(Arrays.asList(dexes));
    }

    public List<Dex> dexes() {
        return dexes;
    }

    /** First class with the given descriptor across all files, or null. */
    public DexClass findClass(String type) {
        for (Dex dex : dexes) {
            DexClass cls = dex.findClass(type);
            if (cls != null) {
                return cls;
            }
        }
        return null;
    }

    public int classCount() {
        int count = 0;
        for (Dex dex : dexes) {
            count += dex.classes().size();
        }
        return count;
    }

    public int methodCount() {
        int count = 0;
        for (Dex dex : dexes) {
            for (DexClass cls : dex.classes()) {
                count += cls.methods.size();
            }
        }
        return count;
    }
}
