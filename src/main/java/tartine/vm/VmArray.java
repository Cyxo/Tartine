package tartine.vm;

/**
 * A DEX array. Elements default per Dalvik zeroing rules (0 for
 * numeric, null for references). Narrow values are stored normalized
 * to {@link Integer}, matching the interpreter value model.
 */
public final class VmArray {
    public final String elementType;
    private final Object[] values;

    public VmArray(String elementType, int length) {
        this.elementType = elementType;
        this.values = new Object[length];
        Object def = defaultFor(elementType);
        for (int i = 0; i < length; i++) {
            values[i] = def;
        }
    }

    public int length() {
        return values.length;
    }

    public Object get(int index) {
        return values[index];
    }

    public void set(int index, Object value) {
        values[index] = value;
    }

    private static Object defaultFor(String type) {
        switch (type) {
            case "Z":
            case "B":
            case "S":
            case "C":
            case "I":
                return 0;
            case "J":
                return 0L;
            case "F":
                return 0.0f;
            case "D":
                return 0.0d;
            default:
                return null;
        }
    }
}
