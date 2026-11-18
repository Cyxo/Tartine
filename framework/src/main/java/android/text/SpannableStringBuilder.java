package android.text;

import java.util.ArrayList;
import java.util.List;

/**
 * AOSP {@code android.text.SpannableStringBuilder}, reduced to a mutable
 * character buffer with the {@link Editable} replacement surface. Spans are
 * accepted and dropped; the shell has no span-aware layout yet.
 */
public class SpannableStringBuilder implements CharSequence, Editable {

    private final StringBuilder mText;
    private final List<Object> mSpans = new ArrayList<>();

    public SpannableStringBuilder() {
        mText = new StringBuilder();
    }

    public SpannableStringBuilder(CharSequence text) {
        mText = new StringBuilder(text == null ? "" : text.toString());
    }

    public SpannableStringBuilder(CharSequence text, int start, int end) {
        CharSequence source = text == null ? "" : text;
        mText = new StringBuilder(source.subSequence(
                Math.max(0, start), Math.min(source.length(), end)).toString());
    }

    @Override
    public int length() {
        return mText.length();
    }

    @Override
    public char charAt(int index) {
        return mText.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return mText.subSequence(start, end);
    }

    @Override
    public String toString() {
        return mText.toString();
    }

    @Override
    public Editable replace(int st, int en, CharSequence source, int start,
            int end) {
        String slice = source == null ? ""
                : source.subSequence(start, end).toString();
        return replace(st, en, slice);
    }

    @Override
    public Editable replace(int st, int en, CharSequence text) {
        int from = Math.max(0, Math.min(st, mText.length()));
        int to = Math.max(from, Math.min(en, mText.length()));
        mText.replace(from, to, text == null ? "" : text.toString());
        return this;
    }

    @Override
    public Editable insert(int where, CharSequence text, int start, int end) {
        return replace(where, where,
                text == null ? "" : text.subSequence(start, end));
    }

    @Override
    public Editable insert(int where, CharSequence text) {
        return replace(where, where, text);
    }

    @Override
    public Editable delete(int st, int en) {
        return replace(st, en, "");
    }

    @Override
    public Editable append(CharSequence text) {
        mText.append(text == null ? "" : text);
        return this;
    }

    @Override
    public Editable append(CharSequence text, int start, int end) {
        CharSequence source = text == null ? "" : text;
        mText.append(source.subSequence(start, end));
        return this;
    }

    @Override
    public Editable append(char text) {
        mText.append(text);
        return this;
    }

    @Override
    public void clear() {
        mText.setLength(0);
    }

    @Override
    public void clearSpans() {
        mSpans.clear();
    }

    @Override
    public void setSpan(Object what, int start, int end, int flags) {
        mSpans.add(what);
    }

    @Override
    public void removeSpan(Object what) {
        mSpans.remove(what);
    }

    @Override
    public int getSpanStart(Object tag) {
        return -1;
    }

    @Override
    public int getSpanEnd(Object tag) {
        return -1;
    }

    @Override
    public int getSpanFlags(Object tag) {
        return 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] getSpans(int start, int end, Class<T> type) {
        return (T[]) java.lang.reflect.Array.newInstance(type, 0);
    }

    @Override
    public int nextSpanTransition(int start, int limit, Class type) {
        return limit;
    }
}
