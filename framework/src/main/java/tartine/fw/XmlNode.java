package tartine.fw;

import java.util.List;

/**
 * Host seam document model: one parsed compiled-XML element (a layout, an
 * {@code <xml>} resource, ...) with its attributes and children in document
 * order.
 *
 * <p>The host parses the APK's AXML with {@code tartine.apk.BinaryXml} and
 * hands the framework this tree; the framework never sees raw AXML bytes.
 * An attribute carries the namespace it was declared in, the local name, the
 * {@code android} resource id aapt2 assigned it (0 for non-resource
 * attributes), and the raw typed value, so an {@code AttributeSet} can answer
 * both name-based and resource-id-based lookups.
 */
public final class XmlNode {

    /** One decoded attribute. */
    public static final class Attribute {
        /** Declaration namespace URI, or {@code null}. */
        public final String namespace;
        /** Local name, e.g. {@code layout_width}. */
        public final String name;
        /** {@code android}-assigned resource id, or 0. */
        public final int resourceId;
        /** {@code TypedValue} data-type tag. */
        public final int type;
        /** Raw {@code Res_value} data word. */
        public final int data;
        /** Resolved text for string values, else {@code null}. */
        public final String string;

        public Attribute(String namespace, String name, int resourceId,
                int type, int data, String string) {
            this.namespace = namespace;
            this.name = name;
            this.resourceId = resourceId;
            this.type = type;
            this.data = data;
            this.string = string;
        }
    }

    /** Declaration namespace URI, or {@code null} for no namespace. */
    public final String namespace;
    /** Local element name, e.g. {@code TextView}. */
    public final String name;
    public final List<Attribute> attributes;
    public final List<XmlNode> children;

    public XmlNode(String namespace, String name,
            List<Attribute> attributes, List<XmlNode> children) {
        this.namespace = namespace;
        this.name = name;
        this.attributes = attributes;
        this.children = children;
    }
}
