package tartine.tx;

import java.lang.reflect.Array;

public final class ArrayCoerce {
    private ArrayCoerce() {
    }

    private static ClassLoader loaderOf() {
        try {
            ClassLoader c = Thread.currentThread().getContextClassLoader();
            if (c != null) return c;
        } catch (Throwable ignore) {}
        return ArrayCoerce.class.getClassLoader();
    }

    public static Class<?> classFor(String desc) {
        String className = desc;
        if (className.startsWith("L") && className.endsWith(";"))
            className = className.substring(1, className.length() - 1).replace('/', '.');
        else if (className.startsWith("[")) className = className.replace('/', '.');
        try {
            ClassLoader loader = loaderOf();
            for (ClassLoader cursor = loader; cursor != null; cursor = cursor.getParent()) {
                try { return cursor.loadClass(className); } catch (Throwable ignore) {}
            }
            return Class.forName(className);
        } catch (Throwable t) {
            return null;
        }
    }

    public static Object coerce(Object value, String arrDesc) {
        if (value == null || arrDesc == null || !arrDesc.startsWith("[")) return value;
        String comp = arrDesc.substring(1);
        Class<?> compClass;
        try {
            if (comp.equals("Z")) compClass = boolean.class;
            else if (comp.equals("B")) compClass = byte.class;
            else if (comp.equals("C")) compClass = char.class;
            else if (comp.equals("S")) compClass = short.class;
            else if (comp.equals("I")) compClass = int.class;
            else if (comp.equals("J")) compClass = long.class;
            else if (comp.equals("F")) compClass = float.class;
            else if (comp.equals("D")) compClass = double.class;
            else compClass = classFor(comp);
        } catch (Throwable t) {
            return value;
        }
        if (compClass == null) return value;
        Class<?> runtime = value.getClass().getComponentType();
        if (compClass.isAssignableFrom(runtime)) return value;
        if (runtime.isPrimitive() || compClass.isPrimitive()) return value;
        int len = Array.getLength(value);
        Object out = Array.newInstance(compClass, len);
        System.arraycopy(value, 0, out, 0, len);
        return out;
    }
}
