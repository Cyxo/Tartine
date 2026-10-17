package android.content;

    public class IntentFilter implements android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;
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

    public IntentFilter(android.content.IntentFilter arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;-><init>(Landroid/content/IntentFilter;)V");
    }

    public IntentFilter(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;-><init>(Ljava/lang/String;)V");
    }

    public IntentFilter(java.lang.String arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;-><init>(Ljava/lang/String;Ljava/lang/String;)V");
    }

    public final java.util.Iterator<java.lang.String> actionsIterator() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->actionsIterator()Ljava/util/Iterator;");
    }

    public final void addAction(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->addAction(Ljava/lang/String;)V");
    }

    public final void addCategory(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->addCategory(Ljava/lang/String;)V");
    }

    public final void addDataAuthority(java.lang.String arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->addDataAuthority(Ljava/lang/String;Ljava/lang/String;)V");
    }

    public final void addDataPath(java.lang.String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->addDataPath(Ljava/lang/String;I)V");
    }

    public final void addDataScheme(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->addDataScheme(Ljava/lang/String;)V");
    }

    public final void addDataSchemeSpecificPart(java.lang.String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->addDataSchemeSpecificPart(Ljava/lang/String;I)V");
    }

    public final void addDataType(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->addDataType(Ljava/lang/String;)V");
    }

    public final void addUriRelativeFilterGroup(android.content.UriRelativeFilterGroup arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->addUriRelativeFilterGroup(Landroid/content/UriRelativeFilterGroup;)V");
    }

    public java.util.function.Predicate<android.content.Intent> asPredicate() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->asPredicate()Ljava/util/function/Predicate;");
    }

    public java.util.function.Predicate<android.content.Intent> asPredicateWithTypeResolution(android.content.ContentResolver arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->asPredicateWithTypeResolution(Landroid/content/ContentResolver;)Ljava/util/function/Predicate;");
    }

    public final java.util.Iterator<android.content.IntentFilter.AuthorityEntry> authoritiesIterator() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->authoritiesIterator()Ljava/util/Iterator;");
    }

    public final java.util.Iterator<java.lang.String> categoriesIterator() {
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

    public static android.content.IntentFilter create(java.lang.String arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->create(Ljava/lang/String;Ljava/lang/String;)Landroid/content/IntentFilter;");
    }

    public final int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->describeContents()I");
    }

    public void dump(android.util.Printer arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->dump(Landroid/util/Printer;Ljava/lang/String;)V");
    }

    public final java.lang.String getAction(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->getAction(I)Ljava/lang/String;");
    }

    public final java.lang.String getCategory(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->getCategory(I)Ljava/lang/String;");
    }

    public final android.content.IntentFilter.AuthorityEntry getDataAuthority(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->getDataAuthority(I)Landroid/content/IntentFilter$AuthorityEntry;");
    }

    public final android.os.PatternMatcher getDataPath(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->getDataPath(I)Landroid/os/PatternMatcher;");
    }

    public final java.lang.String getDataScheme(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->getDataScheme(I)Ljava/lang/String;");
    }

    public final android.os.PatternMatcher getDataSchemeSpecificPart(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->getDataSchemeSpecificPart(I)Landroid/os/PatternMatcher;");
    }

    public final java.lang.String getDataType(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->getDataType(I)Ljava/lang/String;");
    }

    public final int getPriority() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->getPriority()I");
    }

    public final android.content.UriRelativeFilterGroup getUriRelativeFilterGroup(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->getUriRelativeFilterGroup(I)Landroid/content/UriRelativeFilterGroup;");
    }

    public final boolean hasAction(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->hasAction(Ljava/lang/String;)Z");
    }

    public final boolean hasCategory(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->hasCategory(Ljava/lang/String;)Z");
    }

    public final boolean hasDataAuthority(android.net.Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->hasDataAuthority(Landroid/net/Uri;)Z");
    }

    public final boolean hasDataPath(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->hasDataPath(Ljava/lang/String;)Z");
    }

    public final boolean hasDataScheme(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->hasDataScheme(Ljava/lang/String;)Z");
    }

    public final boolean hasDataSchemeSpecificPart(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->hasDataSchemeSpecificPart(Ljava/lang/String;)Z");
    }

    public final boolean hasDataType(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->hasDataType(Ljava/lang/String;)Z");
    }

    public final int match(android.content.ContentResolver arg0, android.content.Intent arg1, boolean arg2, java.lang.String arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->match(Landroid/content/ContentResolver;Landroid/content/Intent;ZLjava/lang/String;)I");
    }

    public final int match(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, android.net.Uri arg3, java.util.Set<java.lang.String> arg4, java.lang.String arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->match(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Landroid/net/Uri;Ljava/util/Set;Ljava/lang/String;)I");
    }

    public final boolean matchAction(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->matchAction(Ljava/lang/String;)Z");
    }

    public final java.lang.String matchCategories(java.util.Set<java.lang.String> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->matchCategories(Ljava/util/Set;)Ljava/lang/String;");
    }

    public final int matchData(java.lang.String arg0, java.lang.String arg1, android.net.Uri arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->matchData(Ljava/lang/String;Ljava/lang/String;Landroid/net/Uri;)I");
    }

    public final int matchDataAuthority(android.net.Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->matchDataAuthority(Landroid/net/Uri;)I");
    }

    public final java.util.Iterator<android.os.PatternMatcher> pathsIterator() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->pathsIterator()Ljava/util/Iterator;");
    }

    public void readFromXml(org.xmlpull.v1.XmlPullParser arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->readFromXml(Lorg/xmlpull/v1/XmlPullParser;)V");
    }

    public final java.util.Iterator<android.os.PatternMatcher> schemeSpecificPartsIterator() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->schemeSpecificPartsIterator()Ljava/util/Iterator;");
    }

    public final java.util.Iterator<java.lang.String> schemesIterator() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->schemesIterator()Ljava/util/Iterator;");
    }

    public final void setPriority(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->setPriority(I)V");
    }

    public final java.util.Iterator<java.lang.String> typesIterator() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->typesIterator()Ljava/util/Iterator;");
    }

    public final void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->writeToParcel(Landroid/os/Parcel;I)V");
    }

    public void writeToXml(org.xmlpull.v1.XmlSerializer arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter;->writeToXml(Lorg/xmlpull/v1/XmlSerializer;)V");
    }
        public static class AuthorityEntry {

        public AuthorityEntry(java.lang.String arg0, java.lang.String arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter$AuthorityEntry;-><init>(Ljava/lang/String;Ljava/lang/String;)V");
        }

        public boolean equals(java.lang.Object arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter$AuthorityEntry;->equals(Ljava/lang/Object;)Z");
        }

        public java.lang.String getHost() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter$AuthorityEntry;->getHost()Ljava/lang/String;");
        }

        public int getPort() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter$AuthorityEntry;->getPort()I");
        }

        public int match(android.net.Uri arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentFilter$AuthorityEntry;->match(Landroid/net/Uri;)I");
        }

            /** Scratch mirror constructor (SkeletonGen). */
            public AuthorityEntry() {
            }
        }
    }
