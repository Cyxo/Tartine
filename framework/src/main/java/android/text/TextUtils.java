package android.text;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Printer;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class TextUtils {

    public static final int CAP_MODE_CHARACTERS = 4096;
    public static final int CAP_MODE_SENTENCES = 16384;
    public static final int CAP_MODE_WORDS = 8192;
    public static Parcelable.Creator CHAR_SEQUENCE_CREATOR;
    public static final int SAFE_STRING_FLAG_FIRST_LINE = 4;
    public static final int SAFE_STRING_FLAG_SINGLE_LINE = 2;
    public static final int SAFE_STRING_FLAG_TRIM = 1;

    public static CharSequence commaEllipsize(CharSequence arg0, TextPaint arg1, float arg2, String arg3, String arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->commaEllipsize(Ljava/lang/CharSequence;Landroid/text/TextPaint;FLjava/lang/String;Ljava/lang/String;)Ljava/lang/CharSequence;");
    }

    public static CharSequence concat(CharSequence[] arg0) {
        if (arg0 == null || arg0.length == 0) {
            return null;
        }
        if (arg0.length == 1) {
            return arg0[0];
        }
        StringBuilder builder = new StringBuilder();
        for (CharSequence piece : arg0) {
            if (piece != null) {
                builder.append(piece);
            }
        }
        return builder.toString();
    }

    public static void copySpansFrom(Spanned arg0, int arg1, int arg2, Class arg3, Spannable arg4, int arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->copySpansFrom(Landroid/text/Spanned;IILjava/lang/Class;Landroid/text/Spannable;I)V");
    }

    public static void dumpSpans(CharSequence arg0, Printer arg1, String arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->dumpSpans(Ljava/lang/CharSequence;Landroid/util/Printer;Ljava/lang/String;)V");
    }

    public static CharSequence ellipsize(CharSequence arg0, TextPaint arg1, float arg2, TextUtils.TruncateAt arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->ellipsize(Ljava/lang/CharSequence;Landroid/text/TextPaint;FLandroid/text/TextUtils$TruncateAt;)Ljava/lang/CharSequence;");
    }

    public static CharSequence ellipsize(CharSequence arg0, TextPaint arg1, float arg2, TextUtils.TruncateAt arg3, boolean arg4, TextUtils.EllipsizeCallback arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->ellipsize(Ljava/lang/CharSequence;Landroid/text/TextPaint;FLandroid/text/TextUtils$TruncateAt;ZLandroid/text/TextUtils$EllipsizeCallback;)Ljava/lang/CharSequence;");
    }

    /** AOSP {@code TextUtils.equals}: identity, then content equality. */
    public static boolean equals(CharSequence arg0, CharSequence arg1) {
        if (arg0 == arg1) {
            return true;
        }
        if (arg0 == null || arg1 == null || arg0.length() != arg1.length()) {
            return false;
        }
        if (arg0 instanceof String && arg1 instanceof String) {
            return arg0.equals(arg1);
        }
        for (int i = 0; i < arg0.length(); i++) {
            if (arg0.charAt(i) != arg1.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public static CharSequence expandTemplate(CharSequence arg0, CharSequence[] arg1) {
        if (arg0 == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (i < arg0.length()) {
            char c = arg0.charAt(i);
            if (c != '^') {
                builder.append(c);
                i++;
                continue;
            }
            i++;
            if (i >= arg0.length()) {
                break;
            }
            char next = arg0.charAt(i);
            if (next == '^') {
                builder.append('^');
            } else if (next >= '1' && next <= '9'
                    && arg1 != null && next - '1' < arg1.length) {
                CharSequence value = arg1[next - '1'];
                if (value != null) {
                    builder.append(value);
                }
            }
            i++;
        }
        return builder;
    }

    public static int getCapsMode(CharSequence arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->getCapsMode(Ljava/lang/CharSequence;II)I");
    }

    /** AOSP {@code TextUtils.getChars}: copies a slice into {@code dest}. */
    public static void getChars(CharSequence arg0, int arg1, int arg2, char[] arg3,
            int arg4) {
        if (arg0 instanceof String) {
            ((String) arg0).getChars(arg1, arg2, arg3, arg4);
            return;
        }
        for (int i = arg1; i < arg2; i++) {
            arg3[arg4++] = arg0.charAt(i);
        }
    }

    /**
     * AOSP {@code TextUtils.getLayoutDirectionFromLocale}: script dominates,
     * otherwise the RTL language set answers RTL (LTR for everything else).
     */
    public static int getLayoutDirectionFromLocale(Locale arg0) {
        if (arg0 == null) {
            return 0;
        }
        String script = arg0.getScript();
        if (script != null && ("Arab".equals(script) || "Hebr".equals(script)
                || "Thaa".equals(script) || "Nkoo".equals(script)
                || "Syrc".equals(script) || "Adlm".equals(script))) {
            return 1;
        }
        String language = arg0.getLanguage();
        if (language == null) {
            return 0;
        }
        switch (language) {
            case "ar": case "arc": case "ckb": case "dv": case "fa":
            case "he": case "iw": case "ji": case "ku": case "mzn":
            case "nqo": case "prs": case "ps": case "rhg": case "sd":
            case "syr": case "ug": case "ur": case "yi":
                return 1;
            default:
                return 0;
        }
    }

    /** AOSP {@code TextUtils.getOffsetAfter}: advances past a surrogate pair. */
    public static int getOffsetAfter(CharSequence arg0, int arg1) {
        int len = arg0.length();
        if (arg1 >= len - 1) {
            return len;
        }
        if (Character.isHighSurrogate(arg0.charAt(arg1))
                && Character.isLowSurrogate(arg0.charAt(arg1 + 1))) {
            return arg1 + 2;
        }
        return arg1 + 1;
    }

    /** AOSP {@code TextUtils.getOffsetBefore}: retreats over a surrogate pair. */
    public static int getOffsetBefore(CharSequence arg0, int arg1) {
        if (arg1 <= 0) {
            return 0;
        }
        if (arg1 == 1) {
            return 0;
        }
        if (Character.isLowSurrogate(arg0.charAt(arg1 - 1))
                && Character.isHighSurrogate(arg0.charAt(arg1 - 2))) {
            return arg1 - 2;
        }
        return arg1 - 1;
    }

    public static CharSequence getReverse(CharSequence arg0, int arg1, int arg2) {
        StringBuilder builder = new StringBuilder(arg2 - arg1);
        for (int i = arg2 - 1; i >= arg1; i--) {
            builder.append(arg0.charAt(i));
        }
        return builder.toString();
    }

    /**
     * AOSP {@code TextUtils.getTrimmedLength}: the index of the last
     * non-space character, as used by the text layout code.
     */
    public static int getTrimmedLength(CharSequence arg0) {
        int len = arg0.length();
        int start = 0;
        while (start < len && arg0.charAt(start) <= ' ') {
            start++;
        }
        int end = len;
        while (end > start && arg0.charAt(end - 1) <= ' ') {
            end--;
        }
        return end;
    }

    /** AOSP {@code TextUtils.htmlEncode}: escapes the HTML metacharacters. */
    public static String htmlEncode(String arg0) {
        if (arg0 == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < arg0.length(); i++) {
            char c = arg0.charAt(i);
            switch (c) {
                case '<':
                    builder.append("&lt;");
                    break;
                case '>':
                    builder.append("&gt;");
                    break;
                case '&':
                    builder.append("&amp;");
                    break;
                case '\'':
                    builder.append("&#39;");
                    break;
                case '"':
                    builder.append("&quot;");
                    break;
                default:
                    builder.append(c);
                    break;
            }
        }
        return builder.toString();
    }

    /** AOSP {@code TextUtils.indexOf}: first occurrence of a character. */
    public static int indexOf(CharSequence arg0, char arg1) {
        return indexOf(arg0, arg1, 0, arg0.length());
    }

    /** AOSP {@code TextUtils.indexOf}: first occurrence from {@code start}. */
    public static int indexOf(CharSequence arg0, char arg1, int arg2) {
        return indexOf(arg0, arg1, arg2, arg0.length());
    }

    /** AOSP {@code TextUtils.indexOf}: first occurrence in a range. */
    public static int indexOf(CharSequence arg0, char arg1, int arg2, int arg3) {
        for (int i = arg2; i < arg3; i++) {
            if (arg0.charAt(i) == arg1) {
                return i;
            }
        }
        return -1;
    }

    /** AOSP {@code TextUtils.indexOf}: first occurrence of a sequence. */
    public static int indexOf(CharSequence arg0, CharSequence arg1) {
        return indexOf(arg0, arg1, 0, arg0.length());
    }

    /** AOSP {@code TextUtils.indexOf}: first occurrence from {@code start}. */
    public static int indexOf(CharSequence arg0, CharSequence arg1, int arg2) {
        return indexOf(arg0, arg1, arg2, arg0.length());
    }

    /** AOSP {@code TextUtils.indexOf}: first occurrence in a range. */
    public static int indexOf(CharSequence arg0, CharSequence arg1, int arg2,
            int arg3) {
        int needleLength = arg1.length();
        if (needleLength == 0) {
            return arg2;
        }
        for (int i = arg2; i <= arg3 - needleLength; i++) {
            boolean match = true;
            for (int j = 0; j < needleLength; j++) {
                if (arg0.charAt(i + j) != arg1.charAt(j)) {
                    match = false;
                    break;
                }
            }
            if (match) {
                return i;
            }
        }
        return -1;
    }

    /** AOSP {@code TextUtils.isDigitsOnly}: every character is ASCII 0-9. */
    public static boolean isDigitsOnly(CharSequence arg0) {
        int len = arg0.length();
        for (int i = 0; i < len; i++) {
            char c = arg0.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    /** AOSP {@code TextUtils.isEmpty}: null or zero length. */
    public static boolean isEmpty(CharSequence arg0) {
        return arg0 == null || arg0.length() == 0;
    }

    /** AOSP {@code TextUtils.isGraphic(char)}: a visible character. */
    public static boolean isGraphic(char arg0) {
        return !Character.isISOControl(arg0) && arg0 != '\ufffe'
                && arg0 != '\uffff' && arg0 != '\u200e' && arg0 != '\u200f';
    }

    /** AOSP {@code TextUtils.isGraphic(CharSequence)}: every char visible. */
    public static boolean isGraphic(CharSequence arg0) {
        int len = arg0.length();
        for (int i = 0; i < len; i++) {
            if (!isGraphic(arg0.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /** AOSP {@code TextUtils.join}: delimiter between iterable elements. */
    public static String join(CharSequence arg0, Iterable arg1) {
        Iterator<?> iterator = ((Iterable<?>) arg1).iterator();
        if (!iterator.hasNext()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(iterator.next());
        while (iterator.hasNext()) {
            builder.append(arg0);
            builder.append(iterator.next());
        }
        return builder.toString();
    }

    /** AOSP {@code TextUtils.join}: delimiter between array elements. */
    public static String join(CharSequence arg0, Object[] arg1) {
        if (arg1.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(arg1[0]);
        for (int i = 1; i < arg1.length; i++) {
            builder.append(arg0);
            builder.append(arg1[i]);
        }
        return builder.toString();
    }

    /** AOSP {@code TextUtils.lastIndexOf}: last occurrence of a character. */
    public static int lastIndexOf(CharSequence arg0, char arg1) {
        return lastIndexOf(arg0, arg1, arg0.length() - 1);
    }

    /** AOSP {@code TextUtils.lastIndexOf}: last occurrence at or before. */
    public static int lastIndexOf(CharSequence arg0, char arg1, int arg2) {
        return lastIndexOf(arg0, arg1, 0, arg2);
    }

    /** AOSP {@code TextUtils.lastIndexOf}: last occurrence in a range. */
    public static int lastIndexOf(CharSequence arg0, char arg1, int arg2,
            int arg3) {
        for (int i = Math.min(arg3, arg0.length() - 1); i >= arg2; i--) {
            if (arg0.charAt(i) == arg1) {
                return i;
            }
        }
        return -1;
    }

    public static CharSequence listEllipsize(Context arg0, List<CharSequence> arg1, String arg2, TextPaint arg3, float arg4, int arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->listEllipsize(Landroid/content/Context;Ljava/util/List;Ljava/lang/String;Landroid/text/TextPaint;FI)Ljava/lang/CharSequence;");
    }

    public static CharSequence makeSafeForPresentation(String arg0, int arg1, float arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->makeSafeForPresentation(Ljava/lang/String;IFI)Ljava/lang/CharSequence;");
    }

    /** AOSP {@code TextUtils.regionMatches}: compares two ranges. */
    public static boolean regionMatches(CharSequence arg0, int arg1,
            CharSequence arg2, int arg3, int arg4) {
        if (arg1 < 0 || arg3 < 0 || arg1 + arg4 > arg0.length()
                || arg3 + arg4 > arg2.length()) {
            return false;
        }
        for (int i = 0; i < arg4; i++) {
            if (arg0.charAt(arg1 + i) != arg2.charAt(arg3 + i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * AOSP {@code TextUtils.replace}: replaces the first matching source
     * (in declaration order) at each position; a null destination deletes.
     */
    public static CharSequence replace(CharSequence arg0, String[] arg1,
            CharSequence[] arg2) {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (i < arg0.length()) {
            int match = -1;
            for (int s = 0; s < arg1.length; s++) {
                String source = arg1[s];
                if (source != null && source.length() > 0
                        && regionMatches(arg0, i, source, 0,
                                source.length())) {
                    match = s;
                    break;
                }
            }
            if (match < 0) {
                builder.append(arg0.charAt(i));
                i++;
            } else {
                CharSequence destination = arg2[match];
                if (destination != null) {
                    builder.append(destination);
                }
                i += arg1[match].length();
            }
        }
        return builder.toString();
    }

    /** AOSP {@code TextUtils.split}: literal-expression split with no limit. */
    public static String[] split(String arg0, String arg1) {
        if (isEmpty(arg1)) {
            return null;
        }
        return arg0.split(arg1, -1);
    }

    /** AOSP {@code TextUtils.split}: pattern split with no limit. */
    public static String[] split(String arg0, Pattern arg1) {
        return arg1.split(arg0, -1);
    }

    /** AOSP {@code TextUtils.stringOrSpannedString}: plain or spanned. */
    public static CharSequence stringOrSpannedString(CharSequence arg0) {
        if (arg0 == null || arg0 instanceof Spanned) {
            return arg0;
        }
        return arg0.toString();
    }

    /** AOSP {@code TextUtils.substring}: a string copy of a slice. */
    public static String substring(CharSequence arg0, int arg1, int arg2) {
        if (arg0 instanceof String) {
            return ((String) arg0).substring(arg1, arg2);
        }
        return arg0.subSequence(arg1, arg2).toString();
    }

    public static void writeToParcel(CharSequence arg0, Parcel arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->writeToParcel(Ljava/lang/CharSequence;Landroid/os/Parcel;I)V");
    }

    public static interface EllipsizeCallback {

        public abstract void ellipsized(int arg0, int arg1);
    }

    public static enum TruncateAt {
        END, MARQUEE, MIDDLE, START;
    }
}
