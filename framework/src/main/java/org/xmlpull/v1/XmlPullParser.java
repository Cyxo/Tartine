package org.xmlpull.v1;

    public interface XmlPullParser {

    public static final int CDSECT = 5;
    public static final int COMMENT = 9;
    public static final int DOCDECL = 10;
    public static final int END_DOCUMENT = 1;
    public static final int END_TAG = 3;
    public static final int ENTITY_REF = 6;
    public static final java.lang.String FEATURE_PROCESS_DOCDECL = "http://xmlpull.org/v1/doc/features.html#process-docdecl";
    public static final java.lang.String FEATURE_PROCESS_NAMESPACES = "http://xmlpull.org/v1/doc/features.html#process-namespaces";
    public static final java.lang.String FEATURE_REPORT_NAMESPACE_ATTRIBUTES = "http://xmlpull.org/v1/doc/features.html#report-namespace-prefixes";
    public static final java.lang.String FEATURE_VALIDATION = "http://xmlpull.org/v1/doc/features.html#validation";
    public static final int IGNORABLE_WHITESPACE = 7;
    public static final java.lang.String NO_NAMESPACE = "";
    public static final int PROCESSING_INSTRUCTION = 8;
    public static final int START_DOCUMENT = 0;
    public static final int START_TAG = 2;
    public static final int TEXT = 4;

    public abstract void defineEntityReplacementText(java.lang.String arg0, java.lang.String arg1);

    public abstract int getAttributeCount();

    public abstract java.lang.String getAttributeName(int arg0);

    public abstract java.lang.String getAttributeNamespace(int arg0);

    public abstract java.lang.String getAttributePrefix(int arg0);

    public abstract java.lang.String getAttributeType(int arg0);

    public abstract java.lang.String getAttributeValue(int arg0);

    public abstract java.lang.String getAttributeValue(java.lang.String arg0, java.lang.String arg1);

    public abstract int getColumnNumber();

    public abstract int getDepth();

    public abstract int getEventType();

    public abstract boolean getFeature(java.lang.String arg0);

    public abstract java.lang.String getInputEncoding();

    public abstract int getLineNumber();

    public abstract java.lang.String getName();

    public abstract java.lang.String getNamespace();

    public abstract java.lang.String getNamespace(java.lang.String arg0);

    public abstract int getNamespaceCount(int arg0);

    public abstract java.lang.String getNamespacePrefix(int arg0);

    public abstract java.lang.String getNamespaceUri(int arg0);

    public abstract java.lang.String getPositionDescription();

    public abstract java.lang.String getPrefix();

    public abstract java.lang.Object getProperty(java.lang.String arg0);

    public abstract java.lang.String getText();

    public abstract char[] getTextCharacters(int[] arg0);

    public abstract boolean isAttributeDefault(int arg0);

    public abstract boolean isEmptyElementTag();

    public abstract boolean isWhitespace();

    public abstract int next();

    public abstract int nextTag();

    public abstract java.lang.String nextText();

    public abstract int nextToken();

    public abstract void require(int arg0, java.lang.String arg1, java.lang.String arg2);

    public abstract void setFeature(java.lang.String arg0, boolean arg1);

    public abstract void setInput(java.io.InputStream arg0, java.lang.String arg1);

    public abstract void setInput(java.io.Reader arg0);

    public abstract void setProperty(java.lang.String arg0, java.lang.Object arg1);
    }
