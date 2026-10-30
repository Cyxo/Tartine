package android.content.res;

import android.util.AttributeSet;
import org.xmlpull.v1.XmlPullParser;

    public interface XmlResourceParser extends AttributeSet, AutoCloseable, XmlPullParser {

    public abstract void close();

    public abstract String getAttributeNamespace(int arg0);
    }
