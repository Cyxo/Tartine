package org.xmlpull.v1;

import java.io.InputStream;
import java.io.Reader;

    public interface XmlPullParser {

    public static final int CDSECT = 5;
    public static final int COMMENT = 9;
    public static final int DOCDECL = 10;
    public static final int END_DOCUMENT = 1;
    public static final int END_TAG = 3;
    public static final int ENTITY_REF = 6;
    public static final String FEATURE_PROCESS_DOCDECL = "http://xmlpull.org/v1/doc/features.html#process-docdecl";
    public static final String FEATURE_PROCESS_NAMESPACES = "http://xmlpull.org/v1/doc/features.html#process-namespaces";
    public static final String FEATURE_REPORT_NAMESPACE_ATTRIBUTES = "http://xmlpull.org/v1/doc/features.html#report-namespace-prefixes";
    public static final String FEATURE_VALIDATION = "http://xmlpull.org/v1/doc/features.html#validation";
    public static final int IGNORABLE_WHITESPACE = 7;
    public static final String NO_NAMESPACE = "";
    public static final int PROCESSING_INSTRUCTION = 8;
    public static final int START_DOCUMENT = 0;
    public static final int START_TAG = 2;
    public static final int TEXT = 4;

    public abstract void defineEntityReplacementText(String arg0, String arg1);

    public abstract int getAttributeCount();

    public abstract String getAttributeName(int arg0);

    public abstract String getAttributeNamespace(int arg0);

    public abstract String getAttributePrefix(int arg0);

    public abstract String getAttributeType(int arg0);

    public abstract String getAttributeValue(int arg0);

    public abstract String getAttributeValue(String arg0, String arg1);

    public abstract int getColumnNumber();

    public abstract int getDepth();

    public abstract int getEventType();

    public abstract boolean getFeature(String arg0);

    public abstract String getInputEncoding();

    public abstract int getLineNumber();

    public abstract String getName();

    public abstract String getNamespace();

    public abstract String getNamespace(String arg0);

    public abstract int getNamespaceCount(int arg0);

    public abstract String getNamespacePrefix(int arg0);

    public abstract String getNamespaceUri(int arg0);

    public abstract String getPositionDescription();

    public abstract String getPrefix();

    public abstract Object getProperty(String arg0);

    public abstract String getText();

    public abstract char[] getTextCharacters(int[] arg0);

    public abstract boolean isAttributeDefault(int arg0);

    public abstract boolean isEmptyElementTag();

    public abstract boolean isWhitespace();

    public abstract int next();

    public abstract int nextTag();

    public abstract String nextText();

    public abstract int nextToken();

    public abstract void require(int arg0, String arg1, String arg2);

    public abstract void setFeature(String arg0, boolean arg1);

    public abstract void setInput(InputStream arg0, String arg1);

    public abstract void setInput(Reader arg0);

    public abstract void setProperty(String arg0, Object arg1);
    }
