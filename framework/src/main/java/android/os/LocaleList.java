package android.os;

public final class LocaleList implements android.os.Parcelable {
    private final java.util.Locale[] mLocaleList;

    public LocaleList(java.util.Locale... locales) {
        mLocaleList = (locales == null || locales.length == 0)
                ? new java.util.Locale[] { java.util.Locale.getDefault() }
                : locales;
    }

    public static android.os.LocaleList getDefault() {
        return new android.os.LocaleList(new java.util.Locale[] { java.util.Locale.getDefault() });
    }

    public static android.os.LocaleList getEmptyLocaleList() {
        return new android.os.LocaleList(new java.util.Locale[0]);
    }

    public int size() { return mLocaleList.length; }

    public java.util.Locale get(int index) { return mLocaleList[index]; }

    public boolean isEmpty() { return mLocaleList.length == 0; }

    public int indexOf(java.util.Locale locale) {
        for (int i = 0; i < mLocaleList.length; i++) {
            if (mLocaleList[i].equals(locale)) return i;
        }
        return -1;
    }

    public java.lang.String toLanguageTags() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mLocaleList.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(mLocaleList[i].toLanguageTag());
        }
        return sb.toString();
    }

    public java.lang.String toString() { return toLanguageTags(); }

    public int hashCode() {
        int result = 1;
        for (java.util.Locale l : mLocaleList) result = 31 * result + l.hashCode();
        return result;
    }

    public boolean equals(java.lang.Object other) {
        if (other == this) return true;
        if (!(other instanceof android.os.LocaleList)) return false;
        android.os.LocaleList rhs = (android.os.LocaleList) other;
        if (mLocaleList.length != rhs.mLocaleList.length) return false;
        for (int i = 0; i < mLocaleList.length; i++) {
            if (!mLocaleList[i].equals(rhs.mLocaleList[i])) return false;
        }
        return true;
    }

    public int describeContents() { return 0; }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(toLanguageTags());
    }

    public static android.os.LocaleList forLanguageTags(java.lang.String list) {
        if (list == null || list.isEmpty()) return getEmptyLocaleList();
        String[] tags = list.split(",", -1);
        java.util.Locale[] locales = new java.util.Locale[tags.length];
        for (int i = 0; i < tags.length; i++) locales[i] = java.util.Locale.forLanguageTag(tags[i]);
        return new android.os.LocaleList(locales);
    }

    public static final android.os.Parcelable.Creator<android.os.LocaleList> CREATOR =
            new android.os.Parcelable.Creator<android.os.LocaleList>() {
                public android.os.LocaleList createFromParcel(android.os.Parcel in) {
                    return forLanguageTags(in.readString());
                }
                public android.os.LocaleList[] newArray(int size) {
                    return new android.os.LocaleList[size];
                }
            };

    public java.util.Locale getFirstMatch(java.lang.String[] supportedLocales) {
        if (supportedLocales == null || supportedLocales.length == 0 || isEmpty()) return null;
        for (java.util.Locale loc : mLocaleList) {
            for (String tag : supportedLocales) {
                if (tag != null && loc.toLanguageTag().equals(tag)) return loc;
            }
        }
        return null;
    }
}
