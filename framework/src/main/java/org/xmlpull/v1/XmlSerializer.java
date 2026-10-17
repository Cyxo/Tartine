package org.xmlpull.v1;

    public interface XmlSerializer {

    public abstract org.xmlpull.v1.XmlSerializer attribute(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2);

    public abstract void cdsect(java.lang.String arg0);

    public abstract void comment(java.lang.String arg0);

    public abstract void docdecl(java.lang.String arg0);

    public abstract void endDocument();

    public abstract org.xmlpull.v1.XmlSerializer endTag(java.lang.String arg0, java.lang.String arg1);

    public abstract void entityRef(java.lang.String arg0);

    public abstract void flush();

    public abstract int getDepth();

    public abstract boolean getFeature(java.lang.String arg0);

    public abstract java.lang.String getName();

    public abstract java.lang.String getNamespace();

    public abstract java.lang.String getPrefix(java.lang.String arg0, boolean arg1);

    public abstract java.lang.Object getProperty(java.lang.String arg0);

    public abstract void ignorableWhitespace(java.lang.String arg0);

    public abstract void processingInstruction(java.lang.String arg0);

    public abstract void setFeature(java.lang.String arg0, boolean arg1);

    public abstract void setOutput(java.io.OutputStream arg0, java.lang.String arg1);

    public abstract void setOutput(java.io.Writer arg0);

    public abstract void setPrefix(java.lang.String arg0, java.lang.String arg1);

    public abstract void setProperty(java.lang.String arg0, java.lang.Object arg1);

    public abstract void startDocument(java.lang.String arg0, java.lang.Boolean arg1);

    public abstract org.xmlpull.v1.XmlSerializer startTag(java.lang.String arg0, java.lang.String arg1);

    public abstract org.xmlpull.v1.XmlSerializer text(char[] arg0, int arg1, int arg2);

    public abstract org.xmlpull.v1.XmlSerializer text(java.lang.String arg0);
    }
