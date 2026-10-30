package android.os;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

/**
 * AOSP parity for the immutable locale list. Source:
 * aosp-mirror/platform_frameworks_base
 * core/java/android/os/LocaleList.java (LocaleList(Locale...), get,
 * toLanguageTags/toString, getEmptyLocaleList, forLanguageTags,
 * getFirstMatch). The full ICU likely-script matching in
 * matchesLanguageAndScript is not ported yet; getFirstMatch matches exact
 * language tags and falls back to the first locale, as AOSP does when no
 * supported locale matches.
 */
public final class LocaleList implements Parcelable {

    private static final Locale[] EMPTY_LOCALES =
            new Locale[0];
    private static final LocaleList EMPTY_LOCALE_LIST =
            new LocaleList(EMPTY_LOCALES);

    private final Locale[] mLocaleList;
    private final String mStringRepresentation;

    /**
     * AOSP LocaleList(Locale...): an empty array produces an empty list,
     * null entries throw NPE, duplicates are dropped, and the stored
     * locales are clones. The comma-separated tag string is computed once
     * at construction time.
     */
    public LocaleList(Locale... locales) {
        if (locales == null) {
            throw new NullPointerException("list is null");
        }
        if (locales.length == 0) {
            mLocaleList = EMPTY_LOCALES;
            mStringRepresentation = "";
            return;
        }
        ArrayList<Locale> list = new ArrayList<>();
        HashSet<Locale> seen = new HashSet<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < locales.length; i++) {
            Locale locale = locales[i];
            if (locale == null) {
                throw new NullPointerException("list[" + i + "] is null");
            }
            if (seen.contains(locale)) {
                continue;
            }
            Locale clone = (Locale) locale.clone();
            list.add(clone);
            sb.append(clone.toLanguageTag());
            if (i < locales.length - 1) {
                sb.append(',');
            }
            seen.add(clone);
        }
        mLocaleList = list.toArray(new Locale[0]);
        mStringRepresentation = sb.toString();
    }

    public static LocaleList getDefault() {
        return new LocaleList(Locale.getDefault());
    }

    public static LocaleList getEmptyLocaleList() {
        return EMPTY_LOCALE_LIST;
    }

    public int size() { return mLocaleList.length; }

    /** AOSP LocaleList.get: out-of-range indexes answer null. */
    public Locale get(int index) {
        return (0 <= index && index < mLocaleList.length)
                ? mLocaleList[index] : null;
    }

    public boolean isEmpty() { return mLocaleList.length == 0; }

    public int indexOf(Locale locale) {
        for (int i = 0; i < mLocaleList.length; i++) {
            if (mLocaleList[i].equals(locale)) return i;
        }
        return -1;
    }

    public String toLanguageTags() {
        return mStringRepresentation;
    }

    /** AOSP LocaleList.toString: "[en-US,fr-FR]" (not the raw tag list). */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < mLocaleList.length; i++) {
            sb.append(mLocaleList[i]);
            if (i < mLocaleList.length - 1) {
                sb.append(',');
            }
        }
        sb.append(']');
        return sb.toString();
    }

    public int hashCode() {
        int result = 1;
        for (Locale l : mLocaleList) result = 31 * result + l.hashCode();
        return result;
    }

    public boolean equals(Object other) {
        if (other == this) return true;
        if (!(other instanceof LocaleList)) return false;
        LocaleList rhs = (LocaleList) other;
        if (mLocaleList.length != rhs.mLocaleList.length) return false;
        for (int i = 0; i < mLocaleList.length; i++) {
            if (!mLocaleList[i].equals(rhs.mLocaleList[i])) return false;
        }
        return true;
    }

    public int describeContents() { return 0; }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(toLanguageTags());
    }

    public static LocaleList forLanguageTags(String list) {
        if (list == null || list.isEmpty()) return getEmptyLocaleList();
        String[] tags = list.split(",");
        Locale[] locales = new Locale[tags.length];
        for (int i = 0; i < tags.length; i++) locales[i] = Locale.forLanguageTag(tags[i]);
        return new LocaleList(locales);
    }

    public static final Parcelable.Creator<LocaleList> CREATOR =
            new Parcelable.Creator<LocaleList>() {
                public LocaleList createFromParcel(Parcel in) {
                    return forLanguageTags(in.readString());
                }
                public LocaleList[] newArray(int size) {
                    return new LocaleList[size];
                }
            };

    /**
     * AOSP getFirstMatch: an empty list answers null; otherwise the first
     * exact tag match wins, with the first locale as the no-match fallback.
     */
    public Locale getFirstMatch(String[] supportedLocales) {
        if (isEmpty()) return null;
        if (supportedLocales != null) {
            for (Locale loc : mLocaleList) {
                for (String tag : supportedLocales) {
                    if (tag != null && loc.toLanguageTag().equals(tag)) return loc;
                }
            }
        }
        return mLocaleList[0];
    }
}
