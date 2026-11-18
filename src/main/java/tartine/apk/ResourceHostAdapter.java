package tartine.apk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import tartine.fw.AppResourceHost;
import tartine.fw.ResValue;
import tartine.fw.SystemColors;
import tartine.fw.XmlNode;

/**
 * Host half of {@link AppResourceHost}: resolves the installed APK's
 * {@code resources.arsc} values and compiled XML for the framework.
 *
 * <p>Wraps a parsed {@link ResourceTable} plus the {@link ApkReader} the
 * table came from, so a layout resource id resolves to the entry file the
 * table points at and the framework can inflate it. Reference
 * ({@code TYPE_REFERENCE}) and attribute-indirection ({@code TYPE_ATTRIBUTE})
 * links are followed here, bounded so a malformed table cannot loop.
 */
public final class ResourceHostAdapter implements AppResourceHost {

    private static final int TYPE_STRING = 0x03;
    private static final int TYPE_REFERENCE = 0x01;
    private static final int TYPE_ATTRIBUTE = 0x02;
    private static final int MAX_DEPTH = 20;

    private final ResourceTable table;
    private final ApkReader apk;
    private final ResourceTable.DeviceConfig device;

    /**
     * Binds a parsed table to the APK it came from.
     *
     * @param table the app's parsed {@code resources.arsc}
     * @param apk the APK entries the table's file references point into
     * @param device the device configuration to resolve against
     */
    public ResourceHostAdapter(ResourceTable table, ApkReader apk,
            ResourceTable.DeviceConfig device) {
        this.table = table;
        this.apk = apk;
        this.device = device;
    }

    @Override
    public ResValue value(int resId) {
        return resolve(resId, 0);
    }

    private ResValue resolve(int resId, int depth) {
        if (resId <= 0 || depth > MAX_DEPTH) {
            return null;
        }
        ResourceTable.ConfValue conf = table.value(resId & 0xFFFFFFFFL, device);
        if (conf == null) {
            return systemColor(resId);
        }
        int type = conf.dataType();
        int data = conf.data();
        if (type == TYPE_REFERENCE) {
            return resolve(data, depth + 1);
        }
        String text = type == TYPE_STRING ? table.string(data) : null;
        return new ResValue(type, data, text, resId);
    }

    /**
     * The {@code android} package ({@code 0x01}) is not part of the installed
     * APK's table. The generated {@link SystemColors} baseline answers its
     * flat color ids, which framework themes (and androidx theme readers)
     * reference directly; every other android-package id stays absent so it
     * surfaces as the framework's named gap instead of a silent zero.
     */
    private static ResValue systemColor(int resId) {
        if ((resId >>> 24) != 0x01) {
            return null;
        }
        Integer argb = SystemColors.get(resId);
        return argb == null ? null
                : new ResValue(BinaryXml.TYPE_COLOR_ARGB8, argb, null, resId);
    }

    @Override
    public ResValue attribute(int attrId, int[] styles) {
        if (styles == null) {
            return null;
        }
        for (int i = styles.length - 1; i >= 0; i--) {
            ResValue found = attributeIn(styles[i], attrId, 0);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    /** Search one style (and its parent chain) for an attribute. */
    private ResValue attributeIn(int styleId, int attrId, int depth) {
        if (styleId <= 0 || depth > MAX_DEPTH) {
            return null;
        }
        ResourceTable.Bag bag = table.bag(styleId & 0xFFFFFFFFL, device);
        if (bag == null) {
            return null;
        }
        for (ResourceTable.MapValue map : bag.values()) {
            if (map.key() != attrId) {
                continue;
            }
            return materialize(map.dataType(), map.data(), map.string(), styleId);
        }
        long parent = bag.parent();
        return parent == 0 ? null : attributeIn((int) parent, attrId, depth + 1);
    }

    /** Turn a bag map value into a {@link ResValue}, following references. */
    private ResValue materialize(int type, int data, String text, int sourceId) {
        if (type == TYPE_REFERENCE) {
            // A reference to a style/array bag has no simple value to
            // resolve: hand the reference back, naming the bag as its
            // source, so TypedArray.getResourceId can answer the style id
            // (Materials' text-appearance readers inflate it by id).
            if (table.bag(data & 0xFFFFFFFFL, device) != null) {
                return new ResValue(type, data, null, data);
            }
            return resolve(data, 0);
        }
        if (type == TYPE_ATTRIBUTE) {
            // ?attr indirection: the framework resolves it against the live
            // theme; hand back the raw attribute reference.
            return new ResValue(type, data, null, 0);
        }
        // Inline value (a color/dimension/boolean literal). AOSP leaves
        // TypedValue.resourceId at 0 for inline values; only a reference
        // names a resource, and resolve() already reports that target id.
        String resolved = type == TYPE_STRING && text == null ? table.string(data) : text;
        return new ResValue(type, data, resolved, 0);
    }

    @Override
    public List<ResValue> arrayValues(int resId) {
        if (resId <= 0) {
            return null;
        }
        ResourceTable.Bag bag = table.bag(resId & 0xFFFFFFFFL, device);
        if (bag == null) {
            return null;
        }
        List<ResourceTable.MapValue> maps = new ArrayList<>(bag.values());
        // Array items are keyed by their index; plurals by their quantity
        // constant. Sorting by key restores document order for arrays and
        // groups plurals predictably.
        maps.sort((left, right) -> Integer.compare(left.key(), right.key()));
        List<ResValue> values = new ArrayList<>(maps.size());
        for (ResourceTable.MapValue map : maps) {
            values.add(materialize(map.dataType(), map.data(), map.string(), resId));
        }
        return values;
    }

    @Override
    public XmlNode xml(int resId) {
        ResourceTable.ConfValue conf = table.value(resId & 0xFFFFFFFFL, device);
        if (conf == null || conf.dataType() != TYPE_STRING) {
            return null;
        }
        String path = table.string(conf.data());
        if (path == null) {
            return null;
        }
        try {
            byte[] bytes = apk.readEntry(path);
            return bytes == null ? null : convert(BinaryXml.parse(bytes));
        } catch (IOException e) {
            return null;
        }
    }

    private static XmlNode convert(BinaryXml.Element element) {
        List<XmlNode.Attribute> attributes =
                new ArrayList<>(element.attributes.size());
        for (BinaryXml.Attribute attr : element.attributes) {
            attributes.add(new XmlNode.Attribute(attr.namespaceUri, attr.name,
                    attr.resourceId, attr.dataType, attr.data, attr.value));
        }
        List<XmlNode> children = new ArrayList<>(element.children.size());
        for (BinaryXml.Element child : element.children) {
            children.add(convert(child));
        }
        return new XmlNode(element.namespaceUri, element.name, attributes, children);
    }

    @Override
    public byte[] asset(String path) {
        if (path == null) {
            return null;
        }
        try {
            return apk.readEntry(path);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public String[] assetList(String dir) {
        if (dir == null) {
            return null;
        }
        String prefix = dir.isEmpty() || dir.endsWith("/") ? dir : dir + "/";
        TreeSet<String> names = new TreeSet<>();
        for (String entry : apk.entries()) {
            if (!entry.startsWith(prefix)) {
                continue;
            }
            String rest = entry.substring(prefix.length());
            int slash = rest.indexOf('/');
            if (slash >= 0) {
                rest = rest.substring(0, slash);
            }
            if (!rest.isEmpty()) {
                names.add(rest);
            }
        }
        return names.isEmpty() ? null : names.toArray(new String[0]);
    }

    @Override
    public String resourceName(int resId, int part) {
        switch (part) {
            case 0:
                return table.typeName(resId & 0xFFFFFFFFL) == null
                        ? null : table.packageName();
            case 1:
                return table.typeName(resId & 0xFFFFFFFFL);
            case 2:
                return table.entryName(resId & 0xFFFFFFFFL);
            default:
                return null;
        }
    }

    @Override
    public int identifier(String name, String type, String pkg) {
        if (name == null || type == null) {
            return 0;
        }
        if (pkg != null && !pkg.isEmpty() && !pkg.equals(table.packageName())) {
            return 0;
        }
        return (int) table.idFor(type, name);
    }
}
