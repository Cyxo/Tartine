package org.xmlpull.v1;

import java.io.OutputStream;
import java.io.Writer;

    public interface XmlSerializer {

    public abstract XmlSerializer attribute(String arg0, String arg1, String arg2);

    public abstract void cdsect(String arg0);

    public abstract void comment(String arg0);

    public abstract void docdecl(String arg0);

    public abstract void endDocument();

    public abstract XmlSerializer endTag(String arg0, String arg1);

    public abstract void entityRef(String arg0);

    public abstract void flush();

    public abstract int getDepth();

    public abstract boolean getFeature(String arg0);

    public abstract String getName();

    public abstract String getNamespace();

    public abstract String getPrefix(String arg0, boolean arg1);

    public abstract Object getProperty(String arg0);

    public abstract void ignorableWhitespace(String arg0);

    public abstract void processingInstruction(String arg0);

    public abstract void setFeature(String arg0, boolean arg1);

    public abstract void setOutput(OutputStream arg0, String arg1);

    public abstract void setOutput(Writer arg0);

    public abstract void setPrefix(String arg0, String arg1);

    public abstract void setProperty(String arg0, Object arg1);

    public abstract void startDocument(String arg0, Boolean arg1);

    public abstract XmlSerializer startTag(String arg0, String arg1);

    public abstract XmlSerializer text(char[] arg0, int arg1, int arg2);

    public abstract XmlSerializer text(String arg0);
    }
