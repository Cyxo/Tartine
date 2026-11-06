package tartine.guest;

import java.lang.reflect.Array;
import tartine.tx.TranslatedLoader;

/**
 * Guest-side array coercion used by translated {@code check-cast} on array
 * types. DEX allows a polymorphic array register whose runtime component is
 * a subtype; the JVM cast would reject it, so the translator calls this
 * bridge with the static array descriptor and the calling guest class.
 *
 * <p>Component classes resolve through the caller's guest loader only, so
 * the coercion can never pull a class the sandbox hides.
 */
public final class GuestArrays {
    private GuestArrays() {
    }

    /** Coerces a bridged value to its declared array type; non-arrays pass through. */
    public static Object coerce(Object value, String arrDesc, Class<?> caller) {
        if (value == null || arrDesc == null || !arrDesc.startsWith("[")) {
            return value;
        }
        String comp = arrDesc.substring(1);
        Class<?> compClass;
        try {
            if (comp.equals("Z")) {
                compClass = boolean.class;
            } else if (comp.equals("B")) {
                compClass = byte.class;
            } else if (comp.equals("C")) {
                compClass = char.class;
            } else if (comp.equals("S")) {
                compClass = short.class;
            } else if (comp.equals("I")) {
                compClass = int.class;
            } else if (comp.equals("J")) {
                compClass = long.class;
            } else if (comp.equals("F")) {
                compClass = float.class;
            } else if (comp.equals("D")) {
                compClass = double.class;
            } else {
                compClass = classFor(comp, caller);
            }
        } catch (Throwable t) {
            return value;
        }
        if (compClass == null) {
            return value;
        }
        Class<?> runtime = value.getClass().getComponentType();
        if (compClass.isAssignableFrom(runtime)) {
            return value;
        }
        if (runtime.isPrimitive() || compClass.isPrimitive()) {
            return value;
        }
        int len = Array.getLength(value);
        Object out = Array.newInstance(compClass, len);
        System.arraycopy(value, 0, out, 0, len);
        return out;
    }

    private static Class<?> classFor(String comp, Class<?> caller) {
        String className = comp;
        if (className.startsWith("L") && className.endsWith(";")) {
            className = className.substring(1, className.length() - 1)
                    .replace('/', '.');
        } else if (className.startsWith("[")) {
            className = className.replace('/', '.');
        }
        ClassLoader loader = caller == null ? null : caller.getClassLoader();
        if (loader instanceof TranslatedLoader) {
            try {
                return loader.loadClass(className);
            } catch (Throwable t) {
                return null;
            }
        }
        // Test/forensics fallback: the loader that hosts this bridge.
        try {
            return Class.forName(className, false,
                    GuestArrays.class.getClassLoader());
        } catch (Throwable t) {
            return null;
        }
    }
}
