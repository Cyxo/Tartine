package tartine.vm;

import java.util.HashMap;
import java.util.Map;

import tartine.dex.DexClass;

/**
 * A heap instance of a DEX class. Fields are keyed by
 * {@code definingClass.name}; absent fields read as null (integer reads
 * normalize null to 0 at the operation site).
 */
public final class VmObject {
    public final DexClass type;
    private final Map<String, Object> fields = new HashMap<>();

    public VmObject(DexClass type) {
        this.type = type;
    }

    public Object getField(String key) {
        return fields.get(key);
    }

    public void putField(String key, Object value) {
        fields.put(key, value);
    }
}
