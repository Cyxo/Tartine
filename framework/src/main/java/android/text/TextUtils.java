package android.text;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Printer;
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
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->concat([Ljava/lang/CharSequence;)Ljava/lang/CharSequence;");
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

    public static boolean equals(CharSequence arg0, CharSequence arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->equals(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Z");
    }

    public static CharSequence expandTemplate(CharSequence arg0, CharSequence[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->expandTemplate(Ljava/lang/CharSequence;[Ljava/lang/CharSequence;)Ljava/lang/CharSequence;");
    }

    public static int getCapsMode(CharSequence arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->getCapsMode(Ljava/lang/CharSequence;II)I");
    }

    public static void getChars(CharSequence arg0, int arg1, int arg2, char[] arg3, int arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->getChars(Ljava/lang/CharSequence;II[CI)V");
    }

    public static int getLayoutDirectionFromLocale(Locale arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->getLayoutDirectionFromLocale(Ljava/util/Locale;)I");
    }

    public static int getOffsetAfter(CharSequence arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->getOffsetAfter(Ljava/lang/CharSequence;I)I");
    }

    public static int getOffsetBefore(CharSequence arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->getOffsetBefore(Ljava/lang/CharSequence;I)I");
    }

    public static CharSequence getReverse(CharSequence arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->getReverse(Ljava/lang/CharSequence;II)Ljava/lang/CharSequence;");
    }

    public static int getTrimmedLength(CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->getTrimmedLength(Ljava/lang/CharSequence;)I");
    }

    public static String htmlEncode(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->htmlEncode(Ljava/lang/String;)Ljava/lang/String;");
    }

    public static int indexOf(CharSequence arg0, char arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;C)I");
    }

    public static int indexOf(CharSequence arg0, char arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;CI)I");
    }

    public static int indexOf(CharSequence arg0, char arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;CII)I");
    }

    public static int indexOf(CharSequence arg0, CharSequence arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)I");
    }

    public static int indexOf(CharSequence arg0, CharSequence arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;Ljava/lang/CharSequence;I)I");
    }

    public static int indexOf(CharSequence arg0, CharSequence arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;Ljava/lang/CharSequence;II)I");
    }

    public static boolean isDigitsOnly(CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->isDigitsOnly(Ljava/lang/CharSequence;)Z");
    }

    public static boolean isEmpty(CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z");
    }

    public static boolean isGraphic(char arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->isGraphic(C)Z");
    }

    public static boolean isGraphic(CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->isGraphic(Ljava/lang/CharSequence;)Z");
    }

    public static String join(CharSequence arg0, Iterable arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->join(Ljava/lang/CharSequence;Ljava/lang/Iterable;)Ljava/lang/String;");
    }

    public static String join(CharSequence arg0, Object[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->join(Ljava/lang/CharSequence;[Ljava/lang/Object;)Ljava/lang/String;");
    }

    public static int lastIndexOf(CharSequence arg0, char arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->lastIndexOf(Ljava/lang/CharSequence;C)I");
    }

    public static int lastIndexOf(CharSequence arg0, char arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->lastIndexOf(Ljava/lang/CharSequence;CI)I");
    }

    public static int lastIndexOf(CharSequence arg0, char arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->lastIndexOf(Ljava/lang/CharSequence;CII)I");
    }

    public static CharSequence listEllipsize(Context arg0, List<CharSequence> arg1, String arg2, TextPaint arg3, float arg4, int arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->listEllipsize(Landroid/content/Context;Ljava/util/List;Ljava/lang/String;Landroid/text/TextPaint;FI)Ljava/lang/CharSequence;");
    }

    public static CharSequence makeSafeForPresentation(String arg0, int arg1, float arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->makeSafeForPresentation(Ljava/lang/String;IFI)Ljava/lang/CharSequence;");
    }

    public static boolean regionMatches(CharSequence arg0, int arg1, CharSequence arg2, int arg3, int arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->regionMatches(Ljava/lang/CharSequence;ILjava/lang/CharSequence;II)Z");
    }

    public static CharSequence replace(CharSequence arg0, String[] arg1, CharSequence[] arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->replace(Ljava/lang/CharSequence;[Ljava/lang/String;[Ljava/lang/CharSequence;)Ljava/lang/CharSequence;");
    }

    public static String[] split(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->split(Ljava/lang/String;Ljava/lang/String;)[Ljava/lang/String;");
    }

    public static String[] split(String arg0, Pattern arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->split(Ljava/lang/String;Ljava/util/regex/Pattern;)[Ljava/lang/String;");
    }

    public static CharSequence stringOrSpannedString(CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->stringOrSpannedString(Ljava/lang/CharSequence;)Ljava/lang/CharSequence;");
    }

    public static String substring(CharSequence arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/TextUtils;->substring(Ljava/lang/CharSequence;II)Ljava/lang/String;");
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
