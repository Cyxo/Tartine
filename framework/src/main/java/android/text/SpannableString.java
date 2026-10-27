package android.text;

import java.util.ArrayList;
import java.util.List;

public class SpannableString implements CharSequence, Spannable {
    private final String text;

    public SpannableString(String source) { this.text = (source == null ? "" : source); }

    public SpannableString(CharSequence source) { this.text = (source == null ? "" : source.toString()); }

    public static SpannableString valueOf(CharSequence source) { return new SpannableString(source); }

    public int length() { return text.length(); }
    public char charAt(int index) { return text.charAt(index); }
    public CharSequence subSequence(int start, int end) { return text.subSequence(start, end); }
    public String toString() { return text; }

    private final List<Object> spans = new ArrayList<>();

    public void setSpan(Object what, int start, int end, int flags) { spans.add(what); }
    public void removeSpan(Object what) { spans.remove(what); }
    public int getSpanStart(Object tag) { return -1; }
    public int getSpanEnd(Object tag) { return -1; }
    public int getSpanFlags(Object tag) { return 0; }
    public int getSpanType(Object tag) { return 0; }
    public Object[] getSpans(int start, int end, Class type) { return new Object[0]; }
    public int nextSpanTransition(int start, int limit, Class type) { return -1; }
}
