package tartine.fw;

import android.util.TypedValue;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Hand-built {@link AppResourceHost} for framework tests: values, styled
 * attributes, documents and bags are declared directly instead of being
 * parsed out of an APK, so the framework's resource and inflation layers can
 * be exercised without a fixture archive.
 *
 * <p>A lookup for an id that was never declared answers {@code null},
 * matching the seam's "no such resource" contract. Bags are copied in and out
 * so a test cannot mutate the host through the list it handed over.
 */
public final class FakeAppResourceHost implements AppResourceHost {

    /** Package/type/entry triple for one resource id. */
    private record Name(String pkg, String type, String entry) {
    }

    private final Map<Integer, ResValue> values = new LinkedHashMap<>();
    private final Map<Integer, Map<Integer, ResValue>> styleAttributes =
            new LinkedHashMap<>();
    private final Map<Integer, XmlNode> documents = new LinkedHashMap<>();
    private final Map<Integer, List<ResValue>> arrays = new LinkedHashMap<>();
    private final Map<Integer, Name> names = new LinkedHashMap<>();
    private final Map<String, Integer> identifiers = new LinkedHashMap<>();

    /** A value that carries no resolved text (int, color, dimension, ...). */
    public static ResValue typed(int type, int data) {
        return new ResValue(type, data, null, 0);
    }

    /** A {@code TYPE_STRING} value carrying its resolved text. */
    public static ResValue text(String text) {
        return new ResValue(TypedValue.TYPE_STRING, 0, text, 0);
    }

    /** A {@code TYPE_REFERENCE} value pointing at another resource. */
    public static ResValue reference(int resId) {
        return new ResValue(TypedValue.TYPE_REFERENCE, resId, null, 0);
    }

    /** Declares the value {@link #value(int)} answers for an id. */
    public void putValue(int resId, ResValue value) {
        values.put(resId, value);
    }

    /** Declares one attribute definition on a style. */
    public void putStyleAttribute(int styleId, int attrId, ResValue value) {
        styleAttributes.computeIfAbsent(styleId, key -> new LinkedHashMap<>())
                .put(attrId, value);
    }

    /** Declares the document {@link #xml(int)} answers for an id. */
    public void putDocument(int resId, XmlNode node) {
        documents.put(resId, node);
    }

    /** Declares the bag contents {@link #arrayValues(int)} answers. */
    public void putArray(int resId, List<ResValue> entries) {
        arrays.put(resId, List.copyOf(entries));
    }

    /** Declares the name triplet {@link #resourceName(int, int)} answers. */
    public void putName(int resId, String pkg, String type, String entry) {
        names.put(resId, new Name(pkg, type, entry));
    }

    /** Declares the id {@link #identifier} answers for a name triplet. */
    public void putIdentifier(String name, String type, String pkg, int resId) {
        identifiers.put(pkg + ":" + type + "/" + name, resId);
    }

    @Override
    public ResValue value(int resId) {
        return values.get(resId);
    }

    @Override
    public ResValue attribute(int attrId, int[] styles) {
        // AOSP Theme semantics: the ordered style list is applied in turn, so
        // the last style that defines the attribute wins.
        ResValue found = null;
        for (int styleId : styles) {
            Map<Integer, ResValue> definitions = styleAttributes.get(styleId);
            if (definitions != null && definitions.containsKey(attrId)) {
                found = definitions.get(attrId);
            }
        }
        return found;
    }

    @Override
    public XmlNode xml(int resId) {
        return documents.get(resId);
    }

    @Override
    public List<ResValue> arrayValues(int resId) {
        List<ResValue> entries = arrays.get(resId);
        return entries == null ? null : List.copyOf(entries);
    }

    @Override
    public byte[] asset(String path) {
        return null;
    }

    @Override
    public String[] assetList(String dir) {
        return null;
    }

    @Override
    public String resourceName(int resId, int part) {
        Name name = names.get(resId);
        if (name == null) {
            return null;
        }
        switch (part) {
            case 0:
                return name.pkg();
            case 1:
                return name.type();
            case 2:
                return name.entry();
            default:
                return null;
        }
    }

    @Override
    public int identifier(String name, String type, String pkg) {
        Integer resId = identifiers.get(pkg + ":" + type + "/" + name);
        return resId == null ? 0 : resId;
    }
}
