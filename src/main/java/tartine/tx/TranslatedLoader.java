package tartine.tx;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import tartine.dex.Dex;
import tartine.dex.DexClass;
import tartine.dex.DexSet;
import tartine.guest.SandboxDenied;

/**
 * Classloader over translated DEX classes: guest types resolve by
 * translating the DEX class into JVM bytecode on first use. Host types
 * resolve through the parent only when {@link SandboxPolicy} allows them;
 * everything else fails with a named sandbox denial instead of reaching the
 * host classpath.
 *
 * <p>Guest classes always win over host classes, so a dex-defined type can
 * never be shadowed by the host; the inverse is refused: reserved names
 * ({@code java.*}, {@code javax.*}, {@code android.*}, {@code tartine.*} and
 * friends) can never be defined by the guest dex set either.
 */
public final class TranslatedLoader extends ClassLoader {
    private final DexSet dex;
    private final Map<String, List<DexClass.DexField>> fieldSynths;
    /** DEX interfaces, for invoke-virtual-on-interface-owner rewriting. */
    private final Set<String> guestInterfaces;
    /** All dex classes, in dex order (reference census source). */
    private final List<DexClass> allClasses;
    private final ReferenceResolver resolver;

    public TranslatedLoader(ClassLoader parent, DexSet dex) {
        super(parent);
        this.dex = dex;
        List<DexClass> all = new ArrayList<>();
        Set<String> ifaces = new HashSet<>();
        for (Dex d : dex.dexes()) {
            all.addAll(d.classes());
            for (DexClass c : d.classes()) {
                if ((c.accessFlags & TxTranslator.ACC_INTERFACE) != 0) {
                    ifaces.add(c.type);
                }
            }
        }
        this.allClasses = List.copyOf(all);
        this.guestInterfaces = ifaces;
        this.fieldSynths = TxTranslator.fieldSynths(all);
        this.resolver = new ReferenceResolver(d -> dex.findClass(d),
                fieldSynths, guestInterfaces, parent);
    }

    /**
     * Whether the dex set defines {@code internalName}; also the predicate
     * the translation pass uses to tell guest classes from host classes.
     */
    public boolean isGuest(String internalName) {
        return dex.findClass("L" + internalName + ";") != null;
    }

    /**
     * Guest-first class resolution. Arrays are built from their component;
     * dex classes go through translation unless they squat on a reserved
     * name; host classes go through the sandbox allowlist.
     */
    @Override
    protected Class<?> loadClass(String name, boolean resolve)
            throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            Class<?> loaded = findLoadedClass(name);
            if (loaded == null) {
                if (name.startsWith("[")) {
                    loaded = loadArray(name);
                } else {
                    String desc = "L" + name.replace('.', '/') + ";";
                    if (dex.findClass(desc) != null) {
                        loaded = findClass(name);
                    } else {
                        loaded = loadHost(name);
                    }
                }
            }
            if (resolve && !loaded.isArray()) {
                resolveClass(loaded);
            }
            return loaded;
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (SandboxPolicy.isReservedGuestName(name)) {
            throw new ClassNotFoundException("Tartine: sandbox denied:"
                    + " guest-defined " + name + " is not available");
        }
        String desc = "L" + name.replace('.', '/') + ";";
        DexClass cls = dex.findClass(desc);
        if (cls == null) {
            throw new ClassNotFoundException("Tartine: no class " + name);
        }
        Class<?> loaded = findLoadedClass(name);
        if (loaded == null) {
            String denial = SandboxPolicy.guestMetadataDenial(cls, this::isGuest);
            if (denial != null) {
                throw new ClassNotFoundException(
                        "Tartine: sandbox denied: " + denial);
            }
            // defineClass is reentrant here: defining a guest resolves its
            // superclass (also a guest) through this very method, nested on the
            // same thread. So translate happens lock-free and pure (it only
            // reads the dex model), and the definition itself is serialized:
            // ConcurrentHashMap.computeIfAbsent would abort that recursion with
            // "Recursive update".
            final byte[] bytes;
            try {
                bytes = TxTranslator.translate(cls, fieldSynths,
                        guestInterfaces, d -> dex.findClass(d), getParent(),
                        resolver);
            } catch (SandboxDenied e) {
                throw new ClassNotFoundException(
                        "Tartine: sandbox denied: " + e.getMessage(), e);
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

    /** Builds an array class from its descriptor-style JVM name. */
    private Class<?> loadArray(String name) throws ClassNotFoundException {
        String component = name.substring(1);
        Class<?> componentClass;
        if (component.startsWith("[")) {
            componentClass = loadClass(component);
        } else if (component.startsWith("L") && component.endsWith(";")) {
            componentClass = loadClass(
                    component.substring(1, component.length() - 1)
                            .replace('/', '.'));
        } else {
            componentClass = switch (component.charAt(0)) {
                case 'Z' -> boolean.class;
                case 'B' -> byte.class;
                case 'C' -> char.class;
                case 'S' -> short.class;
                case 'I' -> int.class;
                case 'J' -> long.class;
                case 'F' -> float.class;
                case 'D' -> double.class;
                default -> throw new ClassNotFoundException(
                        "Tartine: bad array name " + name);
            };
        }
        return java.lang.reflect.Array.newInstance(componentClass, 0).getClass();
    }

    /** Host loading, gated by the sandbox allowlist. */
    private Class<?> loadHost(String name) throws ClassNotFoundException {
        String denial = SandboxPolicy.classDenial(name.replace('.', '/'),
                guestName -> false);
        if (denial != null) {
            throw new ClassNotFoundException(
                    "Tartine: sandbox denied: " + denial);
        }
        ClassLoader parent = getParent();
        try {
            return parent == null
                    ? Class.forName(name, false, null)
                    : parent.loadClass(name);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Tartine: no class " + name, e);
        }
    }

    /**
     * Resource lookup is part of the denied host surface: the loader
     * answers "nothing" rather than exposing host classpath resources.
     * Guest code cannot reach these without a loader reference, which the
     * sandbox denies at the call site.
     */
    @Override
    public URL getResource(String name) {
        return null;
    }

    @Override
    public Enumeration<URL> getResources(String name) {
        return Collections.emptyEnumeration();
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        return null;
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
    public Function<String, DexClass> dexLookup() {
        return d -> dex.findClass(d);
    }

    /** Shared translation-time resolver (probe harness seam). */
    public ReferenceResolver resolver() {
        return resolver;
    }

    /**
     * Unresolved references across the whole dex set, sorted and
     * deduplicated. Translation itself stays per-method and lazy; this is
     * the up-front census behind the {@code refs} command.
     */
    public List<ReferenceResolver.Problem> referenceProblems() {
        return resolver.problems(allClasses);
    }

    /** Translates eagerly, creating an IOException on the first gap. */
    public byte[] translateClass(String name) throws IOException {
        DexClass cls = dex.findClass("L" + name.replace('.', '/') + ";");
        if (cls == null) {
            throw new IOException("no class " + name + " index");
        }
        if (SandboxPolicy.isReservedGuestName(name)) {
            throw new IOException("sandbox denied: guest-defined " + name);
        }
        String denial = SandboxPolicy.guestMetadataDenial(cls, this::isGuest);
        if (denial != null) {
            throw new IOException("sandbox denied: " + denial);
        }
        try {
            return TxTranslator.translate(cls, fieldSynths, guestInterfaces,
                    d -> dex.findClass(d), getParent(), resolver);
        } catch (TxTranslator.TxGap e) {
            throw new IOException("translate " + cls.type + ": " + e.getMessage(), e);
        } catch (SandboxDenied e) {
            throw new IOException("translate " + cls.type + ": sandbox denied: "
                    + e.getMessage(), e);
        }
    }
}
