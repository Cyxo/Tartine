package android.icu.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

    public class ULocale implements Comparable<ULocale>, Serializable {

    public static ULocale CANADA;
    public static ULocale CANADA_FRENCH;
    public static ULocale CHINA;
    public static ULocale CHINESE;
    public static ULocale ENGLISH;
    public static ULocale FRANCE;
    public static ULocale FRENCH;
    public static ULocale GERMAN;
    public static ULocale GERMANY;
    public static ULocale ITALIAN;
    public static ULocale ITALY;
    public static ULocale JAPAN;
    public static ULocale JAPANESE;
    public static ULocale KOREA;
    public static ULocale KOREAN;
    public static ULocale PRC;
    public static final char PRIVATE_USE_EXTENSION = 120;
    public static ULocale ROOT;
    public static ULocale SIMPLIFIED_CHINESE;
    public static ULocale TAIWAN;
    public static ULocale TRADITIONAL_CHINESE;
    public static ULocale UK;
    public static final char UNICODE_LOCALE_EXTENSION = 117;
    public static ULocale US;

    public ULocale(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;-><init>(Ljava/lang/String;)V");
    }

    public ULocale(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;-><init>(Ljava/lang/String;Ljava/lang/String;)V");
    }

    public ULocale(String arg0, String arg1, String arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;-><init>(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
    }

    public static ULocale acceptLanguage(ULocale[] arg0, ULocale[] arg1, boolean[] arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->acceptLanguage([Landroid/icu/util/ULocale;[Landroid/icu/util/ULocale;[Z)Landroid/icu/util/ULocale;");
    }

    public static ULocale acceptLanguage(ULocale[] arg0, boolean[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->acceptLanguage([Landroid/icu/util/ULocale;[Z)Landroid/icu/util/ULocale;");
    }

    public static ULocale acceptLanguage(String arg0, ULocale[] arg1, boolean[] arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->acceptLanguage(Ljava/lang/String;[Landroid/icu/util/ULocale;[Z)Landroid/icu/util/ULocale;");
    }

    public static ULocale acceptLanguage(String arg0, boolean[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->acceptLanguage(Ljava/lang/String;[Z)Landroid/icu/util/ULocale;");
    }

    public static ULocale addLikelySubtags(ULocale arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->addLikelySubtags(Landroid/icu/util/ULocale;)Landroid/icu/util/ULocale;");
    }

    public static String canonicalize(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->canonicalize(Ljava/lang/String;)Ljava/lang/String;");
    }

    public Object clone() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->clone()Ljava/lang/Object;");
    }

    public int compareTo(ULocale arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->compareTo(Landroid/icu/util/ULocale;)I");
    }

    public static ULocale createCanonical(ULocale arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->createCanonical(Landroid/icu/util/ULocale;)Landroid/icu/util/ULocale;");
    }

    public static ULocale createCanonical(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->createCanonical(Ljava/lang/String;)Landroid/icu/util/ULocale;");
    }

    public boolean equals(Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->equals(Ljava/lang/Object;)Z");
    }

    public static ULocale forLanguageTag(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->forLanguageTag(Ljava/lang/String;)Landroid/icu/util/ULocale;");
    }

    public static ULocale forLocale(Locale arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->forLocale(Ljava/util/Locale;)Landroid/icu/util/ULocale;");
    }

    public static ULocale[] getAvailableLocales() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getAvailableLocales()[Landroid/icu/util/ULocale;");
    }

    public static Collection<ULocale> getAvailableLocalesByType(ULocale.AvailableType arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getAvailableLocalesByType(Landroid/icu/util/ULocale$AvailableType;)Ljava/util/Collection;");
    }

    public String getBaseName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getBaseName()Ljava/lang/String;");
    }

    public static String getBaseName(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getBaseName(Ljava/lang/String;)Ljava/lang/String;");
    }

    public String getCharacterOrientation() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getCharacterOrientation()Ljava/lang/String;");
    }

    public String getCountry() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getCountry()Ljava/lang/String;");
    }

    public static String getCountry(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getCountry(Ljava/lang/String;)Ljava/lang/String;");
    }

    public static ULocale getDefault() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDefault()Landroid/icu/util/ULocale;");
    }

    public static ULocale getDefault(ULocale.Category arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDefault(Landroid/icu/util/ULocale$Category;)Landroid/icu/util/ULocale;");
    }

    public String getDisplayCountry() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayCountry()Ljava/lang/String;");
    }

    public String getDisplayCountry(ULocale arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayCountry(Landroid/icu/util/ULocale;)Ljava/lang/String;");
    }

    public static String getDisplayCountry(String arg0, ULocale arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayCountry(Ljava/lang/String;Landroid/icu/util/ULocale;)Ljava/lang/String;");
    }

    public static String getDisplayCountry(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayCountry(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
    }

    public static String getDisplayKeyword(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayKeyword(Ljava/lang/String;)Ljava/lang/String;");
    }

    public static String getDisplayKeyword(String arg0, ULocale arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayKeyword(Ljava/lang/String;Landroid/icu/util/ULocale;)Ljava/lang/String;");
    }

    public static String getDisplayKeyword(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayKeyword(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
    }

    public String getDisplayKeywordValue(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayKeywordValue(Ljava/lang/String;)Ljava/lang/String;");
    }

    public String getDisplayKeywordValue(String arg0, ULocale arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayKeywordValue(Ljava/lang/String;Landroid/icu/util/ULocale;)Ljava/lang/String;");
    }

    public static String getDisplayKeywordValue(String arg0, String arg1, ULocale arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayKeywordValue(Ljava/lang/String;Ljava/lang/String;Landroid/icu/util/ULocale;)Ljava/lang/String;");
    }

    public static String getDisplayKeywordValue(String arg0, String arg1, String arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayKeywordValue(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
    }

    public String getDisplayLanguage() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayLanguage()Ljava/lang/String;");
    }

    public String getDisplayLanguage(ULocale arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayLanguage(Landroid/icu/util/ULocale;)Ljava/lang/String;");
    }

    public static String getDisplayLanguage(String arg0, ULocale arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayLanguage(Ljava/lang/String;Landroid/icu/util/ULocale;)Ljava/lang/String;");
    }

    public static String getDisplayLanguage(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayLanguage(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
    }

    public String getDisplayLanguageWithDialect() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayLanguageWithDialect()Ljava/lang/String;");
    }

    public String getDisplayLanguageWithDialect(ULocale arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayLanguageWithDialect(Landroid/icu/util/ULocale;)Ljava/lang/String;");
    }

    public static String getDisplayLanguageWithDialect(String arg0, ULocale arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayLanguageWithDialect(Ljava/lang/String;Landroid/icu/util/ULocale;)Ljava/lang/String;");
    }

    public static String getDisplayLanguageWithDialect(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayLanguageWithDialect(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
    }

    public String getDisplayName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayName()Ljava/lang/String;");
    }

    public String getDisplayName(ULocale arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayName(Landroid/icu/util/ULocale;)Ljava/lang/String;");
    }

    public static String getDisplayName(String arg0, ULocale arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayName(Ljava/lang/String;Landroid/icu/util/ULocale;)Ljava/lang/String;");
    }

    public static String getDisplayName(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayName(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
    }

    public String getDisplayNameWithDialect() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayNameWithDialect()Ljava/lang/String;");
    }

    public String getDisplayNameWithDialect(ULocale arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayNameWithDialect(Landroid/icu/util/ULocale;)Ljava/lang/String;");
    }

    public static String getDisplayNameWithDialect(String arg0, ULocale arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayNameWithDialect(Ljava/lang/String;Landroid/icu/util/ULocale;)Ljava/lang/String;");
    }

    public static String getDisplayNameWithDialect(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayNameWithDialect(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
    }

    public String getDisplayScript() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayScript()Ljava/lang/String;");
    }

    public String getDisplayScript(ULocale arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayScript(Landroid/icu/util/ULocale;)Ljava/lang/String;");
    }

    public static String getDisplayScript(String arg0, ULocale arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayScript(Ljava/lang/String;Landroid/icu/util/ULocale;)Ljava/lang/String;");
    }

    public static String getDisplayScript(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayScript(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
    }

    public String getDisplayVariant() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayVariant()Ljava/lang/String;");
    }

    public String getDisplayVariant(ULocale arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayVariant(Landroid/icu/util/ULocale;)Ljava/lang/String;");
    }

    public static String getDisplayVariant(String arg0, ULocale arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayVariant(Ljava/lang/String;Landroid/icu/util/ULocale;)Ljava/lang/String;");
    }

    public static String getDisplayVariant(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getDisplayVariant(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
    }

    public String getExtension(char arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getExtension(C)Ljava/lang/String;");
    }

    public Set<Character> getExtensionKeys() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getExtensionKeys()Ljava/util/Set;");
    }

    public ULocale getFallback() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getFallback()Landroid/icu/util/ULocale;");
    }

    public static String getFallback(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getFallback(Ljava/lang/String;)Ljava/lang/String;");
    }

    public String getISO3Country() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getISO3Country()Ljava/lang/String;");
    }

    public static String getISO3Country(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getISO3Country(Ljava/lang/String;)Ljava/lang/String;");
    }

    public String getISO3Language() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getISO3Language()Ljava/lang/String;");
    }

    public static String getISO3Language(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getISO3Language(Ljava/lang/String;)Ljava/lang/String;");
    }

    public static String[] getISOCountries() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getISOCountries()[Ljava/lang/String;");
    }

    public static String[] getISOLanguages() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getISOLanguages()[Ljava/lang/String;");
    }

    public String getKeywordValue(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getKeywordValue(Ljava/lang/String;)Ljava/lang/String;");
    }

    public static String getKeywordValue(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getKeywordValue(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
    }

    public Iterator<String> getKeywords() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getKeywords()Ljava/util/Iterator;");
    }

    public static Iterator<String> getKeywords(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getKeywords(Ljava/lang/String;)Ljava/util/Iterator;");
    }

    public String getLanguage() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getLanguage()Ljava/lang/String;");
    }

    public static String getLanguage(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getLanguage(Ljava/lang/String;)Ljava/lang/String;");
    }

    public String getLineOrientation() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getLineOrientation()Ljava/lang/String;");
    }

    public String getName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getName()Ljava/lang/String;");
    }

    public static String getName(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getName(Ljava/lang/String;)Ljava/lang/String;");
    }

    public String getScript() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getScript()Ljava/lang/String;");
    }

    public static String getScript(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getScript(Ljava/lang/String;)Ljava/lang/String;");
    }

    public Set<String> getUnicodeLocaleAttributes() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getUnicodeLocaleAttributes()Ljava/util/Set;");
    }

    public Set<String> getUnicodeLocaleKeys() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getUnicodeLocaleKeys()Ljava/util/Set;");
    }

    public String getUnicodeLocaleType(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getUnicodeLocaleType(Ljava/lang/String;)Ljava/lang/String;");
    }

    public String getVariant() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getVariant()Ljava/lang/String;");
    }

    public static String getVariant(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->getVariant(Ljava/lang/String;)Ljava/lang/String;");
    }

    public int hashCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->hashCode()I");
    }

    public boolean isRightToLeft() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->isRightToLeft()Z");
    }

    public static ULocale minimizeSubtags(ULocale arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->minimizeSubtags(Landroid/icu/util/ULocale;)Landroid/icu/util/ULocale;");
    }

    public ULocale setKeywordValue(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->setKeywordValue(Ljava/lang/String;Ljava/lang/String;)Landroid/icu/util/ULocale;");
    }

    public static String setKeywordValue(String arg0, String arg1, String arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->setKeywordValue(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
    }

    public String toLanguageTag() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->toLanguageTag()Ljava/lang/String;");
    }

    public static String toLegacyKey(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->toLegacyKey(Ljava/lang/String;)Ljava/lang/String;");
    }

    public static String toLegacyType(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->toLegacyType(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
    }

    public Locale toLocale() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->toLocale()Ljava/util/Locale;");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->toString()Ljava/lang/String;");
    }

    public static String toUnicodeLocaleKey(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->toUnicodeLocaleKey(Ljava/lang/String;)Ljava/lang/String;");
    }

    public static String toUnicodeLocaleType(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/icu/util/ULocale;->toUnicodeLocaleType(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public ULocale() {
        }
        public static enum AvailableType {
            DEFAULT, ONLY_LEGACY_ALIASES, WITH_LEGACY_ALIASES;
        }
        public static enum Category {
            DISPLAY, FORMAT;
        }
    }
