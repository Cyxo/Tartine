package tartine.tx;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import org.objectweb.asm.Type;
import tartine.dex.DexClass;
import tartine.dex.DexMethod;
import tartine.dex.Insn;

/**
 * Translation-time reference resolution: guest dex classes first, then host
 * reflection. The translator asks this resolver where a field really lives,
 * whether an owner is an interface, and whether a reference resolves at all;
 * {@link #problems(List)} is the same lookup replayed over a whole dex set so
 * unresolved references can be reported before any class is translated.
 */
public final class ReferenceResolver {
    /** One unresolved reference, with the reason the lookup failed. */
    public record Problem(String kind, String reference, String reason) {
        @Override
        public String toString() {
            return kind + " " + reference + ": " + reason;
        }
    }

    private final Function<String, DexClass> dexLookup;
    private final Map<String, List<DexClass.DexField>> synthsByOwner;
    private final Set<String> guestInterfaces;
    private final ClassLoader hostLoader;

    private final Map<String, Boolean> interfaceMemo = new HashMap<>();
    private final Map<String, Optional<Class<?>>> hostMemo = new HashMap<>();
    private final Map<String, Optional<String>> fieldOwnerMemo = new HashMap<>();
    private final Map<String, Boolean> methodMemo = new HashMap<>();
    private final Map<String, Boolean> typeMemo = new HashMap<>();

    public ReferenceResolver(Function<String, DexClass> dexLookup,
            Map<String, List<DexClass.DexField>> synthsByOwner,
            Set<String> guestInterfaces, ClassLoader hostLoader) {
        this.dexLookup = dexLookup;
        this.synthsByOwner = synthsByOwner == null ? Map.of() : synthsByOwner;
        this.guestInterfaces = guestInterfaces == null ? Set.of() : guestInterfaces;
        this.hostLoader = hostLoader != null ? hostLoader
                : ReferenceResolver.class.getClassLoader();
    }

    /** Guest dex class for a descriptor, or null when not dex-owned. */
    public DexClass guestClass(String descriptor) {
        return dexLookup == null ? null : dexLookup.apply(descriptor);
    }

    /** Whether {@code owner} is an interface (guest flags, then host). */
    public boolean isInterface(String owner) {
        if (owner == null || owner.startsWith("[")) {
            return false;
        }
        Boolean cached = interfaceMemo.get(owner);
        if (cached != null) {
            return cached;
        }
        boolean result;
        DexClass guest = guestClass(owner);
        if (guest != null) {
            result = (guest.accessFlags & TxTranslator.ACC_INTERFACE) != 0;
        } else if (guestInterfaces.contains(owner)) {
            result = true;
        } else {
            Class<?> host = hostClass(owner);
            result = host != null && host.isInterface();
        }
        interfaceMemo.put(owner, result);
        return result;
    }

    /**
     * Declaring class of a field reference. DEX refs carry the compiler's
     * owner, which may be a subclass of the class that actually declares the
     * field; emitting on the subclass would create a shadow field. Walks the
     * guest/host lineage to the declaration, then falls back to a synthesized
     * field on the ref owner (see {@link TxTranslator#fieldSynths}).
     */
    public String fieldDeclaring(Insn.FieldRef ref) {
        String found = findFieldOwner(ref);
        return found != null ? found : ref.owner();
    }

    /** Whether a field reference resolves (declared or synthesized). */
    public boolean hasField(Insn.FieldRef ref) {
        return findFieldOwner(ref) != null;
    }

    /** Whether a method reference resolves in the guest/host hierarchy. */
    public boolean hasMethod(Insn.MethodRef ref) {
        Boolean cached = methodMemo.get(ref.text());
        if (cached != null) {
            return cached;
        }
        boolean found = ref.owner().startsWith("[")
                ? arrayMethod(ref)
                : hasMethod(ref.owner(), ref, new HashSet<>());
        methodMemo.put(ref.text(), found);
        return found;
    }

    /** Whether a type descriptor names a guest class, host class or array. */
    public boolean hasType(String descriptor) {
        if (descriptor == null || descriptor.isEmpty()) {
            return false;
        }
        Boolean cached = typeMemo.get(descriptor);
        if (cached != null) {
            return cached;
        }
        boolean found;
        char first = descriptor.charAt(0);
        if (descriptor.length() == 1) {
            found = "VZBSCIJFD".indexOf(first) >= 0;
        } else if (first == '[') {
            found = hasType(descriptor.substring(1));
        } else if (first == 'L' && descriptor.endsWith(";")) {
            found = guestClass(descriptor) != null || hostClass(descriptor) != null;
        } else {
            found = false;
        }
        typeMemo.put(descriptor, found);
        return found;
    }

    /**
     * All unresolved references in {@code classes}, deduplicated and sorted.
     * This is a census, not a gate: translation still decides per method
     * whether a reference becomes a named gap.
     */
    public List<Problem> problems(List<DexClass> classes) {
        Map<String, Problem> out = new TreeMap<>();
        for (DexClass cls : classes) {
            checkHierarchy(cls, out);
            checkMembers(cls, out);
            for (DexMethod method : cls.methods) {
                for (Insn in : method.instructions) {
                    checkReference(in, out);
                }
            }
        }
        return new ArrayList<>(out.values());
    }

    private void checkHierarchy(DexClass cls, Map<String, Problem> out) {
        if (cls.superclass != null && !cls.superclass.isEmpty()
                && !hasType(cls.superclass)) {
            add(out, new Problem("class", cls.superclass,
                    "superclass of " + cls.type + " not found"));
        }
        for (String itf : cls.interfaces) {
            if (!hasType(itf)) {
                add(out, new Problem("class", itf,
                        "interface of " + cls.type + " not found"));
            }
        }
    }

    private void checkMembers(DexClass cls, Map<String, Problem> out) {
        for (DexClass.DexField field : cls.fields) {
            if (!hasType(field.type)) {
                add(out, new Problem("type", field.type,
                        "field type of " + cls.type + "->" + field.name));
            }
        }
        for (DexMethod method : cls.methods) {
            for (String param : method.parameterTypes) {
                if (!hasType(param)) {
                    add(out, new Problem("type", param,
                            "parameter of " + cls.type + "->" + method.signature()));
                }
            }
            if (!hasType(method.returnType)) {
                add(out, new Problem("type", method.returnType,
                        "return type of " + cls.type + "->" + method.signature()));
            }
            for (DexMethod.Try tryBlock : method.tries) {
                for (DexMethod.Handler handler : tryBlock.handlers) {
                    if (handler.type != null && !hasType(handler.type)) {
                        add(out, new Problem("type", handler.type,
                                "catch type of " + cls.type + "->" + method.signature()));
                    }
                }
            }
        }
    }

    private void checkReference(Insn in, Map<String, Problem> out) {
        Insn.Ref ref = in.reference;
        if (ref == null || ref instanceof Insn.StringRef) {
            return;
        }
        if (ref instanceof Insn.MethodRef method) {
            if (!hasMethod(method)) {
                add(out, new Problem("method", method.text(), reason(method.owner())));
            }
        } else if (ref instanceof Insn.FieldRef field) {
            if (!hasField(field)) {
                add(out, new Problem("field", field.text(), reason(field.owner())));
            }
        } else if (ref instanceof Insn.TypeRef type) {
            if (!hasType(type.descriptor())) {
                add(out, new Problem("type", type.text(), "type not found"));
            }
        } else {
            add(out, new Problem("ref", ref.text(), "unsupported reference kind"));
        }
    }

    private void add(Map<String, Problem> out, Problem problem) {
        out.putIfAbsent(problem.kind() + " " + problem.reference(), problem);
    }

    private String reason(String owner) {
        if (owner.startsWith("[")) {
            return "member not found on array";
        }
        return hasType(owner) ? "member not found" : "owner class not found";
    }

    private String findFieldOwner(Insn.FieldRef ref) {
        Optional<String> cached = fieldOwnerMemo.get(ref.text());
        if (cached != null) {
            return cached.orElse(null);
        }
        String found = findField(ref.owner(), ref, new HashSet<>());
        if (found == null) {
            List<DexClass.DexField> synths = synthsByOwner.get(ref.owner());
            if (synths != null) {
                for (DexClass.DexField field : synths) {
                    if (field.name.equals(ref.name()) && field.type.equals(ref.type())) {
                        found = ref.owner();
                        break;
                    }
                }
            }
        }
        fieldOwnerMemo.put(ref.text(), Optional.ofNullable(found));
        return found;
    }

    private String findField(String type, Insn.FieldRef ref, Set<String> seen) {
        if (type == null || type.isEmpty() || type.startsWith("[")
                || !seen.add(type)) {
            return null;
        }
        DexClass guest = guestClass(type);
        if (guest != null) {
            for (DexClass.DexField field : guest.fields) {
                if (field.name.equals(ref.name()) && field.type.equals(ref.type())) {
                    return type;
                }
            }
            String found = findField(guest.superclass, ref, seen);
            if (found != null) {
                return found;
            }
            for (String itf : guest.interfaces) {
                found = findField(itf, ref, seen);
                if (found != null) {
                    return found;
                }
            }
            return null;
        }
        Class<?> host = hostClass(type);
        if (host == null) {
            return null;
        }
        if (declaresField(host, ref)) {
            return type;
        }
        if (host.getSuperclass() != null) {
            String found = findField(descriptorOf(host.getSuperclass()), ref, seen);
            if (found != null) {
                return found;
            }
        }
        for (Class<?> itf : host.getInterfaces()) {
            String found = findField(descriptorOf(itf), ref, seen);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private static boolean declaresField(Class<?> host, Insn.FieldRef ref) {
        for (Field field : host.getDeclaredFields()) {
            if (field.getName().equals(ref.name())
                    && Type.getDescriptor(field.getType()).equals(ref.type())) {
                return true;
            }
        }
        return false;
    }

    private boolean hasMethod(String type, Insn.MethodRef ref, Set<String> seen) {
        if (type == null || type.isEmpty() || !seen.add(type)) {
            return false;
        }
        DexClass guest = guestClass(type);
        if (guest != null) {
            for (DexMethod method : guest.methods) {
                if (method.name.equals(ref.name())
                        && method.parameterTypes.equals(ref.parameterTypes())
                        && method.returnType.equals(ref.returnType())) {
                    return true;
                }
            }
            if (hasMethod(guest.superclass, ref, seen)) {
                return true;
            }
            for (String itf : guest.interfaces) {
                if (hasMethod(itf, ref, seen)) {
                    return true;
                }
            }
            return false;
        }
        Class<?> host = hostClass(type);
        if (host == null) {
            return false;
        }
        if (signaturePolymorphic(host, ref)) {
            return true;
        }
        if (ref.name().equals("<init>")) {
            for (Constructor<?> ctor : host.getDeclaredConstructors()) {
                if (Type.getConstructorDescriptor(ctor).equals(ref.descriptor())) {
                    return true;
                }
            }
        } else {
            for (Method method : host.getDeclaredMethods()) {
                if (method.getName().equals(ref.name())
                        && Type.getMethodDescriptor(method).equals(ref.descriptor())) {
                    return true;
                }
            }
        }
        if (host.getSuperclass() != null
                && hasMethod(descriptorOf(host.getSuperclass()), ref, seen)) {
            return true;
        }
        for (Class<?> itf : host.getInterfaces()) {
            if (hasMethod(descriptorOf(itf), ref, seen)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Arrays inherit Object's methods and carry {@code clone()}; the JVM
     * resolves them on the array type itself.
     */
    private static boolean arrayMethod(Insn.MethodRef ref) {
        if (ref.name().equals("clone") && ref.parameterTypes().isEmpty()) {
            return true;
        }
        for (Method method : Object.class.getDeclaredMethods()) {
            if (method.getName().equals(ref.name())
                    && Type.getMethodDescriptor(method).equals(ref.descriptor())) {
                return true;
            }
        }
        return false;
    }

    /** MethodHandle.invoke* and VarHandle access modes take any descriptor. */
    private static boolean signaturePolymorphic(Class<?> host, Insn.MethodRef ref) {
        String name = host.getName();
        if (name.equals("java.lang.invoke.MethodHandle")) {
            return ref.name().equals("invoke") || ref.name().equals("invokeExact");
        }
        return name.equals("java.lang.invoke.VarHandle");
    }

    private Class<?> hostClass(String descriptor) {
        if (descriptor == null || descriptor.length() < 3
                || descriptor.charAt(0) != 'L'
                || descriptor.charAt(descriptor.length() - 1) != ';') {
            return null;
        }
        Optional<Class<?>> cached = hostMemo.get(descriptor);
        if (cached != null) {
            return cached.orElse(null);
        }
        Class<?> found = null;
        try {
            found = Class.forName(TxDescriptors.internal(descriptor).replace('/', '.'),
                    false, hostLoader);
        } catch (ClassNotFoundException | LinkageError | SecurityException e) {
            found = null;
        }
        hostMemo.put(descriptor, Optional.ofNullable(found));
        return found;
    }

    private static String descriptorOf(Class<?> type) {
        return Type.getDescriptor(type);
    }
}
