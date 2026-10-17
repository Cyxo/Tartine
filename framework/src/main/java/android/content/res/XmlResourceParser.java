package android.content.res;

    public interface XmlResourceParser extends android.util.AttributeSet, java.lang.AutoCloseable, org.xmlpull.v1.XmlPullParser {

    public abstract void close();

    public abstract java.lang.String getAttributeNamespace(int arg0);
    }
