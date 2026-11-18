package org.xmlpull.v1;

/**
 * AOSP/XPP {@code org.xmlpull.v1.XmlPullParserException}: the checked parser
 * failure with the parser position attached. Guests wrap it in resource
 * inflation errors, so the position fields are part of the message shape.
 */
public class XmlPullParserException extends Exception {

    /** Underlying cause, or null. */
    protected Throwable detail;
    /** 1-based line, or -1 when unknown. */
    protected int row = -1;
    /** 1-based column, or -1 when unknown. */
    protected int column = -1;

    public XmlPullParserException() {
    }

    public XmlPullParserException(String s) {
        super(s);
    }

    /**
     * XPP's position-aware constructor: appends the parser position and the
     * chained cause to the message and records both.
     */
    public XmlPullParserException(String msg, XmlPullParser parser,
            Throwable chain) {
        super((msg == null ? "" : msg + " ")
                + (parser == null ? ""
                        : "(position:" + parser.getPositionDescription() + ") ")
                + (chain == null ? "" : "caused by: " + chain));
        if (parser != null) {
            this.row = parser.getLineNumber();
            this.column = parser.getColumnNumber();
        }
        this.detail = chain;
    }

    public Throwable getDetail() {
        return detail;
    }

    public int getLineNumber() {
        return row;
    }

    public int getColumnNumber() {
        return column;
    }
}
