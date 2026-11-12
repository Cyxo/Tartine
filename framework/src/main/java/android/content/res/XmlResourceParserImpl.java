package android.content.res;

import android.util.AttributeSet;
import java.util.ArrayDeque;
import java.util.Deque;
import tartine.fw.XmlNode;

/**
 * Tartine: a pull parser over a compiled-XML document the host already
 * decoded ({@link XmlNode}). Mirrors the {@code XmlBlock.Parser} contract the
 * inflater and {@code Resources.obtainAttributes} rely on: a depth-first
 * walk emitting {@code START_TAG}/{@code END_TAG} events, and an
 * {@link AttributeSet} view of the current element's attributes including
 * their {@code android} resource ids.
 *
 * <p>Layout XML carries no text or namespace-declaration chunks, so those
 * events are never reported; callers that need text (string resources) use
 * {@code Resources.getXml} and feed a real pull parser instead.
 */
final class XmlResourceParserImpl implements XmlResourceParser {

    private static final class Frame {
        private final XmlNode node;
        private int nextChild;

        private Frame(XmlNode node) {
            this.node = node;
        }
    }

    private final Deque<Frame> stack = new ArrayDeque<>();
    private XmlNode current;
    private int eventType = START_DOCUMENT;
    private int depth = -1;

    XmlResourceParserImpl(XmlNode root) {
        if (root != null) {
            stack.push(new Frame(root));
        }
    }

    @Override
    public int next() {
        switch (eventType) {
            case START_DOCUMENT:
                return startRoot();
            case START_TAG:
                return descendOrEnd();
            case END_TAG:
                return popOrEnd();
            default:
                return END_DOCUMENT;
        }
    }

    private int startRoot() {
        Frame frame = stack.peek();
        if (frame == null) {
            eventType = END_DOCUMENT;
            depth = -1;
            return END_DOCUMENT;
        }
        current = frame.node;
        depth = 0;
        eventType = START_TAG;
        return START_TAG;
    }

    private int descendOrEnd() {
        Frame frame = stack.peek();
        if (frame == null) {
            eventType = END_DOCUMENT;
            return END_DOCUMENT;
        }
        if (frame.nextChild < frame.node.children.size()) {
            XmlNode child = frame.node.children.get(frame.nextChild++);
            stack.push(new Frame(child));
            current = child;
            depth++;
            eventType = START_TAG;
            return START_TAG;
        }
        eventType = END_TAG;
        return END_TAG;
    }

    private int popOrEnd() {
        stack.pop();
        Frame frame = stack.peek();
        if (frame == null) {
            current = null;
            depth = -1;
            eventType = END_DOCUMENT;
            return END_DOCUMENT;
        }
        if (frame.nextChild < frame.node.children.size()) {
            XmlNode child = frame.node.children.get(frame.nextChild++);
            stack.push(new Frame(child));
            current = child;
            depth--;
            eventType = START_TAG;
            return START_TAG;
        }
        current = frame.node;
        depth--;
        eventType = END_TAG;
        return END_TAG;
    }

    @Override
    public int nextToken() {
        return next();
    }

    @Override
    public int nextTag() {
        int event = next();
        while (event != START_TAG && event != END_TAG && event != END_DOCUMENT) {
            event = next();
        }
        return event;
    }

    @Override
    public String nextText() {
        // Layout documents hold no character data; AOSP returns the empty
        // string once past a tag with no text.
        if (eventType == START_TAG) {
            next();
            if (eventType == END_TAG) {
                next();
            }
        }
        return "";
    }

    @Override
    public void require(int type, String namespace, String name) {
        if (eventType != type || !equalsOrNull(namespace, getNamespace())
                || !equalsOrNull(name, getName())) {
            throw new IllegalStateException("required " + type + " " + namespace
                    + " " + name + " but was " + eventType + " " + getNamespace()
                    + " " + getName());
        }
    }

    private static boolean equalsOrNull(String expected, String actual) {
        return expected == null || expected.equals(actual);
    }

    @Override
    public int getEventType() {
        return eventType;
    }

    @Override
    public int getDepth() {
        return depth;
    }

    @Override
    public String getName() {
        return current == null ? null : current.name;
    }

    @Override
    public String getNamespace() {
        return current == null ? null : current.namespace;
    }

    @Override
    public String getPrefix() {
        return null; // compiled documents carry URIs, not prefixes
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public char[] getTextCharacters(int[] holderForStartAndLength) {
        holderForStartAndLength[0] = 0;
        holderForStartAndLength[1] = 0;
        return new char[0];
    }

    @Override
    public boolean isEmptyElementTag() {
        return current != null && current.children.isEmpty();
    }

    @Override
    public boolean isWhitespace() {
        return false;
    }

    @Override
    public int getAttributeCount() {
        return current == null ? 0 : current.attributes.size();
    }

    @Override
    public String getAttributeNamespace(int index) {
        return attribute(index).namespace;
    }

    @Override
    public String getAttributeName(int index) {
        return attribute(index).name;
    }

    @Override
    public String getAttributePrefix(int index) {
        return null;
    }

    @Override
    public String getAttributeType(int index) {
        return "CDATA";
    }

    @Override
    public boolean isAttributeDefault(int index) {
        return false;
    }

    @Override
    public String getAttributeValue(int index) {
        XmlNode.Attribute attr = attribute(index);
        if (attr.string != null) {
            return attr.string;
        }
        return String.valueOf(attr.data);
    }

    @Override
    public String getAttributeValue(String namespace, String name) {
        if (current == null) {
            return null;
        }
        for (XmlNode.Attribute attr : current.attributes) {
            if (attr.name.equals(name) && equalsOrNull(namespace, attr.namespace)) {
                return attr.string != null ? attr.string : String.valueOf(attr.data);
            }
        }
        return null;
    }

    private XmlNode.Attribute attribute(int index) {
        if (current == null || index < 0 || index >= current.attributes.size()) {
            throw new IndexOutOfBoundsException("attribute " + index);
        }
        return current.attributes.get(index);
    }

    // ---- AttributeSet typed accessors -----------------------------------

    @Override
    public int getAttributeNameResource(int index) {
        return attribute(index).resourceId;
    }

    @Override
    public int getAttributeResourceValue(int index, int defaultValue) {
        XmlNode.Attribute attr = attribute(index);
        return attr.type == 0x01 ? attr.data : defaultValue;
    }

    @Override
    public int getAttributeResourceValue(String namespace, String name, int defaultValue) {
        int index = findAttribute(namespace, name);
        return index < 0 ? defaultValue : getAttributeResourceValue(index, defaultValue);
    }

    @Override
    public int getAttributeIntValue(int index, int defaultValue) {
        XmlNode.Attribute attr = attribute(index);
        return attr.type >= 0x10 && attr.type <= 0x1F ? attr.data : defaultValue;
    }

    @Override
    public int getAttributeIntValue(String namespace, String name, int defaultValue) {
        int index = findAttribute(namespace, name);
        return index < 0 ? defaultValue : getAttributeIntValue(index, defaultValue);
    }

    @Override
    public int getAttributeUnsignedIntValue(int index, int defaultValue) {
        return getAttributeIntValue(index, defaultValue);
    }

    @Override
    public int getAttributeUnsignedIntValue(String namespace, String name, int defaultValue) {
        int index = findAttribute(namespace, name);
        return index < 0 ? defaultValue : getAttributeIntValue(index, defaultValue);
    }

    @Override
    public boolean getAttributeBooleanValue(int index, boolean defaultValue) {
        XmlNode.Attribute attr = attribute(index);
        return attr.type == 0x12 ? attr.data != 0 : defaultValue;
    }

    @Override
    public boolean getAttributeBooleanValue(String namespace, String name, boolean defaultValue) {
        int index = findAttribute(namespace, name);
        return index < 0 ? defaultValue : getAttributeBooleanValue(index, defaultValue);
    }

    @Override
    public float getAttributeFloatValue(int index, float defaultValue) {
        XmlNode.Attribute attr = attribute(index);
        return attr.type == 0x04 ? Float.intBitsToFloat(attr.data) : defaultValue;
    }

    @Override
    public float getAttributeFloatValue(String namespace, String name, float defaultValue) {
        int index = findAttribute(namespace, name);
        return index < 0 ? defaultValue : getAttributeFloatValue(index, defaultValue);
    }

    @Override
    public int getAttributeListValue(int index, String[] options, int defaultValue) {
        return defaultValue;
    }

    @Override
    public int getAttributeListValue(String namespace, String name,
            String[] options, int defaultValue) {
        return defaultValue;
    }

    @Override
    public String getClassAttribute() {
        return null;
    }

    @Override
    public String getIdAttribute() {
        return null;
    }

    @Override
    public int getIdAttributeResourceValue(int defaultValue) {
        return defaultValue;
    }

    @Override
    public int getStyleAttribute() {
        if (current == null) {
            return 0;
        }
        for (XmlNode.Attribute attr : current.attributes) {
            if ("style".equals(attr.name)) {
                return attr.data;
            }
        }
        return 0;
    }

    @Override
    public String getPositionDescription() {
        return "XmlResourceParser{" + (current == null ? "end" : current.name) + "}";
    }

    /**
     * The raw value of the current element's attribute with {@code attrId},
     * or {@code null} when the element does not declare it. Used by
     * {@code Theme.obtainStyledAttributes} so element-local attributes beat
     * the theme.
     */
    tartine.fw.ResValue valueForAttribute(int attrId) {
        if (current == null || attrId == 0) {
            return null;
        }
        for (XmlNode.Attribute attr : current.attributes) {
            if (attr.resourceId == attrId) {
                return new tartine.fw.ResValue(attr.type, attr.data, attr.string,
                        attr.resourceId);
            }
        }
        return null;
    }

    private int findAttribute(String namespace, String name) {        if (current == null) {
            return -1;
        }
        for (int i = 0; i < current.attributes.size(); i++) {
            XmlNode.Attribute attr = current.attributes.get(i);
            if (attr.name.equals(name) && equalsOrNull(namespace, attr.namespace)) {
                return i;
            }
        }
        return -1;
    }

    // ---- namespace / feature no-ops -------------------------------------

    @Override
    public String getNamespace(String prefix) {
        return null;
    }

    @Override
    public int getNamespaceCount(int depth) {
        return 0;
    }

    @Override
    public String getNamespacePrefix(int position) {
        return null;
    }

    @Override
    public String getNamespaceUri(int position) {
        return null;
    }

    @Override
    public int getLineNumber() {
        return 0;
    }

    @Override
    public int getColumnNumber() {
        return 0;
    }

    @Override
    public String getInputEncoding() {
        return "UTF-8";
    }

    @Override
    public boolean getFeature(String name) {
        return false;
    }

    @Override
    public void setFeature(String name, boolean state) {
        // compiled documents do not support runtime features
    }

    @Override
    public Object getProperty(String name) {
        return null;
    }

    @Override
    public void setProperty(String name, Object value) {
        // compiled documents expose no properties
    }

    @Override
    public void setInput(java.io.InputStream input, String encoding) {
        throw new UnsupportedOperationException(
                "Tartine: compiled XML parsers do not expose setInput");
    }

    @Override
    public void setInput(java.io.Reader reader) {
        throw new UnsupportedOperationException(
                "Tartine: compiled XML parsers do not expose setInput");
    }

    @Override
    public void defineEntityReplacementText(String entity, String replacement) {
        // compiled documents hold no entities
    }

    @Override
    public void close() {
        stack.clear();
        current = null;
        eventType = END_DOCUMENT;
    }
}
