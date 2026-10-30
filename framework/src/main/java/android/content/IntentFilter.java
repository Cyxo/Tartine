package android.content;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PatternMatcher;
import android.util.Printer;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

    public class IntentFilter implements Parcelable {

    public static Parcelable.Creator CREATOR;
    public static final int MATCH_ADJUSTMENT_MASK = 65535;
    public static final int MATCH_ADJUSTMENT_NORMAL = 32768;
    public static final int MATCH_CATEGORY_EMPTY = 1048576;
    public static final int MATCH_CATEGORY_HOST = 3145728;
    public static final int MATCH_CATEGORY_MASK = 268369920;
    public static final int MATCH_CATEGORY_PATH = 5242880;
    public static final int MATCH_CATEGORY_PORT = 4194304;
    public static final int MATCH_CATEGORY_SCHEME = 2097152;
    public static final int MATCH_CATEGORY_SCHEME_SPECIFIC_PART = 5767168;
    public static final int MATCH_CATEGORY_TYPE = 6291456;
    public static final int NO_MATCH_ACTION = -3;
    public static final int NO_MATCH_CATEGORY = -4;
    public static final int NO_MATCH_DATA = -2;
    public static final int NO_MATCH_TYPE = -1;
    public static final int SYSTEM_HIGH_PRIORITY = 1000;
    public static final int SYSTEM_LOW_PRIORITY = -1000;

    public IntentFilter() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;-><init>()V");
    }

    public IntentFilter(IntentFilter arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;-><init>(Landroid/content/IntentFilter;)V");
    }

    public IntentFilter(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;-><init>(Ljava/lang/String;)V");
    }

    public IntentFilter(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;-><init>(Ljava/lang/String;Ljava/lang/String;)V");
    }

    public final Iterator<String> actionsIterator() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->actionsIterator()Ljava/util/Iterator;");
    }

    public final void addAction(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->addAction(Ljava/lang/String;)V");
    }

    public final void addCategory(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->addCategory(Ljava/lang/String;)V");
    }

    public final void addDataAuthority(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->addDataAuthority(Ljava/lang/String;Ljava/lang/String;)V");
    }

    public final void addDataPath(String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->addDataPath(Ljava/lang/String;I)V");
    }

    public final void addDataScheme(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->addDataScheme(Ljava/lang/String;)V");
    }

    public final void addDataSchemeSpecificPart(String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->addDataSchemeSpecificPart(Ljava/lang/String;I)V");
    }

    public final void addDataType(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->addDataType(Ljava/lang/String;)V");
    }

    public final void addUriRelativeFilterGroup(UriRelativeFilterGroup arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->addUriRelativeFilterGroup(Landroid/content/UriRelativeFilterGroup;)V");
    }

    public Predicate<Intent> asPredicate() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->asPredicate()Ljava/util/function/Predicate;");
    }

    public Predicate<Intent> asPredicateWithTypeResolution(ContentResolver arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->asPredicateWithTypeResolution(Landroid/content/ContentResolver;)Ljava/util/function/Predicate;");
    }

    public final Iterator<IntentFilter.AuthorityEntry> authoritiesIterator() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->authoritiesIterator()Ljava/util/Iterator;");
    }

    public final Iterator<String> categoriesIterator() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->categoriesIterator()Ljava/util/Iterator;");
    }

    public final void clearUriRelativeFilterGroups() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->clearUriRelativeFilterGroups()V");
    }

    public final int countActions() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->countActions()I");
    }

    public final int countCategories() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->countCategories()I");
    }

    public final int countDataAuthorities() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->countDataAuthorities()I");
    }

    public final int countDataPaths() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->countDataPaths()I");
    }

    public final int countDataSchemeSpecificParts() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->countDataSchemeSpecificParts()I");
    }

    public final int countDataSchemes() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->countDataSchemes()I");
    }

    public final int countDataTypes() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->countDataTypes()I");
    }

    public final int countUriRelativeFilterGroups() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->countUriRelativeFilterGroups()I");
    }

    public static IntentFilter create(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->create(Ljava/lang/String;Ljava/lang/String;)Landroid/content/IntentFilter;");
    }

    public final int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->describeContents()I");
    }

    public void dump(Printer arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->dump(Landroid/util/Printer;Ljava/lang/String;)V");
    }

    public final String getAction(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->getAction(I)Ljava/lang/String;");
    }

    public final String getCategory(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->getCategory(I)Ljava/lang/String;");
    }

    public final IntentFilter.AuthorityEntry getDataAuthority(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->getDataAuthority(I)Landroid/content/IntentFilter$AuthorityEntry;");
    }

    public final PatternMatcher getDataPath(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->getDataPath(I)Landroid/os/PatternMatcher;");
    }

    public final String getDataScheme(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->getDataScheme(I)Ljava/lang/String;");
    }

    public final PatternMatcher getDataSchemeSpecificPart(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->getDataSchemeSpecificPart(I)Landroid/os/PatternMatcher;");
    }

    public final String getDataType(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->getDataType(I)Ljava/lang/String;");
    }

    public final int getPriority() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->getPriority()I");
    }

    public final UriRelativeFilterGroup getUriRelativeFilterGroup(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->getUriRelativeFilterGroup(I)Landroid/content/UriRelativeFilterGroup;");
    }

    public final boolean hasAction(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->hasAction(Ljava/lang/String;)Z");
    }

    public final boolean hasCategory(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->hasCategory(Ljava/lang/String;)Z");
    }

    public final boolean hasDataAuthority(Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->hasDataAuthority(Landroid/net/Uri;)Z");
    }

    public final boolean hasDataPath(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->hasDataPath(Ljava/lang/String;)Z");
    }

    public final boolean hasDataScheme(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->hasDataScheme(Ljava/lang/String;)Z");
    }

    public final boolean hasDataSchemeSpecificPart(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->hasDataSchemeSpecificPart(Ljava/lang/String;)Z");
    }

    public final boolean hasDataType(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->hasDataType(Ljava/lang/String;)Z");
    }

    public final int match(ContentResolver arg0, Intent arg1, boolean arg2, String arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->match(Landroid/content/ContentResolver;Landroid/content/Intent;ZLjava/lang/String;)I");
    }

    public final int match(String arg0, String arg1, String arg2, Uri arg3, Set<String> arg4, String arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->match(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Landroid/net/Uri;Ljava/util/Set;Ljava/lang/String;)I");
    }

    public final boolean matchAction(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->matchAction(Ljava/lang/String;)Z");
    }

    public final String matchCategories(Set<String> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->matchCategories(Ljava/util/Set;)Ljava/lang/String;");
    }

    public final int matchData(String arg0, String arg1, Uri arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->matchData(Ljava/lang/String;Ljava/lang/String;Landroid/net/Uri;)I");
    }

    public final int matchDataAuthority(Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->matchDataAuthority(Landroid/net/Uri;)I");
    }

    public final Iterator<PatternMatcher> pathsIterator() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->pathsIterator()Ljava/util/Iterator;");
    }

    public void readFromXml(XmlPullParser arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->readFromXml(Lorg/xmlpull/v1/XmlPullParser;)V");
    }

    public final Iterator<PatternMatcher> schemeSpecificPartsIterator() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->schemeSpecificPartsIterator()Ljava/util/Iterator;");
    }

    public final Iterator<String> schemesIterator() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->schemesIterator()Ljava/util/Iterator;");
    }

    public final void setPriority(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->setPriority(I)V");
    }

    public final Iterator<String> typesIterator() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->typesIterator()Ljava/util/Iterator;");
    }

    public final void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->writeToParcel(Landroid/os/Parcel;I)V");
    }

    public void writeToXml(XmlSerializer arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->writeToXml(Lorg/xmlpull/v1/XmlSerializer;)V");
    }
        public static class AuthorityEntry {

        public AuthorityEntry(String arg0, String arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter$AuthorityEntry;-><init>(Ljava/lang/String;Ljava/lang/String;)V");
        }

        public boolean equals(Object arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter$AuthorityEntry;->equals(Ljava/lang/Object;)Z");
        }

        public String getHost() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter$AuthorityEntry;->getHost()Ljava/lang/String;");
        }

        public int getPort() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter$AuthorityEntry;->getPort()I");
        }

        public int match(Uri arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter$AuthorityEntry;->match(Landroid/net/Uri;)I");
        }

            /** Scratch mirror constructor (SkeletonGen). */
            public AuthorityEntry() {
            }
        }
    }
