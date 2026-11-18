package android.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

/**
 * AOSP {@code android.util.Xml}, ported wall-by-wall. AndroidX's
 * color-state-list inflater reaches {@link #asAttributeSet}; the parser and
 * serializer factories need a KXml port and stay named gaps until a guest
 * wall needs them.
 */
public class Xml {

    /**
     * AOSP Xml.java:66: the relaxed parser feature name. Kept as the same
     * string constant so guests that compare it see the platform value.
     */
    public static final String FEATURE_RELAXED =
            "http://xmlpull.org/v1/doc/features.html#relaxed";

    Xml() {
        // AOSP Xml is a static-only holder with a package-private ctor.
    }

    /**
     * AOSP {@code Xml.asAttributeSet}: a parser that already implements
     * {@link AttributeSet} (the compiled-XML parser does) is returned as-is;
     * a plain pull parser would be wrapped in {@code XmlPullAttributes}.
     */
    public static AttributeSet asAttributeSet(XmlPullParser parser) {
        if (parser instanceof AttributeSet) {
            return (AttributeSet) parser;
        }
        throw new UnsupportedOperationException(
                "Tartine: not implemented: XmlPullAttributes wrapper");
    }

    /**
     * AOSP {@code Xml.findEncodingByName}: matches the encoding names the
     * enum declares (case-insensitive).
     *
     * @param name the encoding name to look up
     * @return the matching encoding
     * @throws UnsupportedEncodingException when the name is unknown
     */
    public static Encoding findEncodingByName(String name)
            throws UnsupportedEncodingException {
        if (name != null) {
            for (Encoding encoding : Encoding.values()) {
                if (encoding.name().equalsIgnoreCase(name)) {
                    return encoding;
                }
            }
        }
        throw new UnsupportedEncodingException(name);
    }

    public static XmlPullParser newPullParser() {
        // AOSP returns KXmlParser; the shell has the pull-parser interface
        // only, so a raw parser factory waits on a KXml port.
        throw new UnsupportedOperationException(
                "Tartine: not implemented: Xml.newPullParser");
    }

    public static XmlSerializer newSerializer() {
        throw new UnsupportedOperationException(
                "Tartine: not implemented: Xml.newSerializer");
    }

    public static void parse(InputStream arg0, Xml.Encoding arg1,
            ContentHandler arg2) throws IOException, SAXException {
        throw new UnsupportedOperationException(
                "Tartine: not implemented: Xml.parse(InputStream,Encoding,ContentHandler)");
    }

    public static void parse(Reader arg0, ContentHandler arg1)
            throws IOException, SAXException {
        throw new UnsupportedOperationException(
                "Tartine: not implemented: Xml.parse(Reader,ContentHandler)");
    }

    public static void parse(String arg0, ContentHandler arg1)
            throws SAXException {
        throw new UnsupportedOperationException(
                "Tartine: not implemented: Xml.parse(String,ContentHandler)");
    }

    /** AOSP Xml.Encoding: the encodings {@code Xml.parse} accepts. */
    public enum Encoding {
        ISO_8859_1, US_ASCII, UTF_16, UTF_8
    }
}
