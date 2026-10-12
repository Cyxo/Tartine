package tartine.dex;

import java.util.ArrayList;
import java.util.List;

/** Dalvik/JVM type-descriptor parsing shared by the frontend and the VM. */
public final class Descriptors {
    private Descriptors() {
    }

    /** Splits concatenated descriptors, e.g. {@code "ILjava/lang/String;[I"}. */
    public static List<String> splitTypes(String descriptors) {
        List<String> types = new ArrayList<>();
        int i = 0;
        while (i < descriptors.length()) {
            char c = descriptors.charAt(i);
            if (c == 'L') {
                int end = descriptors.indexOf(';', i);
                if (end < 0) {
                    throw new IllegalArgumentException("bad type: " + descriptors);
                }
                types.add(descriptors.substring(i, end + 1));
                i = end + 1;
            } else if (c == '[') {
                int dims = 0;
                while (descriptors.charAt(i) == '[') {
                    dims++;
                    i++;
                }
                if (descriptors.charAt(i) == 'L') {
                    int end = descriptors.indexOf(';', i);
                    types.add(descriptors.substring(i - dims, end + 1));
                    i = end + 1;
                } else {
                    types.add(descriptors.substring(i - dims, i + 1));
                    i++;
                }
            } else {
                types.add(String.valueOf(c));
                i++;
            }
        }
        return types;
    }

    /** Parameter types of a method descriptor like {@code "(II)I"}. */
    public static List<String> paramTypes(String methodDescriptor) {
        int open = methodDescriptor.indexOf('(');
        int close = methodDescriptor.indexOf(')', open);
        if (open < 0 || close < 0) {
            throw new IllegalArgumentException("bad descriptor: " + methodDescriptor);
        }
        return splitTypes(methodDescriptor.substring(open + 1, close));
    }

    /** Return type of a method descriptor like {@code "(II)I"}. */
    public static String returnType(String methodDescriptor) {
        int close = methodDescriptor.indexOf(')');
        if (close < 0) {
            throw new IllegalArgumentException("bad descriptor: " + methodDescriptor);
        }
        return methodDescriptor.substring(close + 1);
    }

    public static boolean isWide(String type) {
        return type.equals("J") || type.equals("D");
    }
}
