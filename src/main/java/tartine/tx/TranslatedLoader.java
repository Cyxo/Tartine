package tartine.tx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import tartine.dex.DexClass;

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
    private final Map<String, Class<?>> cache = new ConcurrentHashMap<>();

    public TranslatedLoader(ClassLoader parent, DexSet dex) {
        super(parent);
        this.dex = dex;
        List<DexClass> all = new ArrayList<>();
        Set<String> ifaces = new java.util.HashSet<>();
        for (tartine.dex.Dex d : dex.dexes()) {
            all.addAll(d.classes());
            for (DexClass c : d.classes()) {
                if ((c.accessFlags & 0x400) != 0) { // ACC_INTERFACE (dex)
                    ifaces.add(c.type);
                }
            }
        }
        this.guestInterfaces = ifaces;
        this.fieldSynths = TxTranslator.fieldSynths(all);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String desc = "L" + name.replace('.', '/') + ";";
        DexClass cls = dex.findClass(desc);
        if (cls == null) {
            throw new ClassNotFoundException(name);
        }
        return cache.computeIfAbsent(name, n -> {
            try {
                byte[] bytes = TxTranslator.translate(cls, fieldSynths,
                        guestInterfaces);
                return defineClass(n, bytes, 0, bytes.length);
            } catch (RuntimeException e) {
                throw new IllegalStateException("translate " + cls.type + " failed", e);
            }
        });
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
