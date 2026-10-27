package tartine.tx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tartine.dex.DexClass;
import tartine.dex.DexSet;

/**
 * Classloader over translated DEX classes: guest types resolve by
 * translating the DEX class into JVM bytecode on first use; everything else
 * (java.*, framework android.*, host deps) resolves through the parent.
 */
public final class TranslatedLoader extends ClassLoader {
    private final DexSet dex;
    private final Map<String, List<DexClass.DexField>> fieldSynths;
    /** DEX interfaces, for invoke-virtual-on-interface-owner rewriting. */
    private final Set<String> guestInterfaces;

    public TranslatedLoader(ClassLoader parent, DexSet dex) {
        super(parent);
        this.dex = dex;
        List<DexClass> all = new ArrayList<>();
        Set<String> ifaces = new java.util.HashSet<>();
        for (tartine.dex.Dex d : dex.dexes()) {
            all.addAll(d.classes());
            for (DexClass c : d.classes()) {
                if ((c.accessFlags & TxTranslator.ACC_INTERFACE) != 0) {
                    ifaces.add(c.type);
                }
            }
        }
        this.guestInterfaces = ifaces;
        this.fieldSynths = TxTranslator.fieldSynths(all);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (name.contains("StateFlowImpl")) {
            try { return getParent().loadClass(name); } catch (ClassNotFoundException ignored) {}
        }
        String desc = "L" + name.replace('.', '/') + ";";
        DexClass cls = dex.findClass(desc);
        if (cls == null) {
            throw new ClassNotFoundException(name);
        }
        // defineClass is reentrant here: defining a guest resolves its
        // superclass (also a guest) through this very method, nested on the
        // same thread. So translate happens lock-free and pure (it only
        // reads the dex model), and the definition itself is serialized:
        // ConcurrentHashMap.computeIfAbsent would abort that recursion with
        // "Recursive update".
        Class<?> loaded = findLoadedClass(name);
        if (loaded == null) {
            final byte[] bytes;
            try {
                bytes = TxTranslator.translate(cls, fieldSynths,
                        guestInterfaces, d -> dex.findClass(d), getParent());
            } catch (RuntimeException e) {
                throw new IllegalStateException(
                        "translate " + cls.type + " failed", e);
            }
            synchronized (this) {
                loaded = findLoadedClass(name); // another thread may have won
                if (loaded == null) {
                    loaded = defineClass(name, bytes, 0, bytes.length);
                }
            }
        }
        return loaded;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        try {
            return super.loadClass(name, false);
        } catch (RuntimeException e) {
            // TxGap wrapped in ISE would shadow real errors: unwrap.
            throw e;
        }
    }

    @Override
    public String toString() {
        return "TranslatedLoader(" + dex.classCount() + " classes)";
    }

    /** Guest dex-owned interface descriptors (probe harness seam). */
    public Set<String> guestInterfaces() {
        return guestInterfaces;
    }

    /** Undeclared referenced fields (probe harness seam). */
    public Map<String, List<DexClass.DexField>> fieldSynths() {
        return fieldSynths;
    }

    /** Dex model lookup by descriptor (probe harness seam). */
    public java.util.function.Function<String, DexClass> dexLookup() {
        return d -> dex.findClass(d);
    }

    /** Translates eagerly, creating an ISE on the first TxGap. */
    public byte[] translateClass(String name) throws IOException {
        DexClass cls = dex.findClass("L" + name.replace('.', '/') + ";");
        if (cls == null) {
            throw new IOException("no class " + name + " index");
        }
        try {
            return TxTranslator.translate(cls);
        } catch (TxTranslator.TxGap e) {
            throw new IOException("translate " + cls.type + ": " + e.getMessage(), e);
        }
    }
}
