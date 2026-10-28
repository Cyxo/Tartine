package android.net;

    public class Uri implements java.lang.Comparable<android.net.Uri>, android.os.Parcelable {
    private String uriString;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    public static android.os.Parcelable.Creator CREATOR;
    public static android.net.Uri EMPTY;

    public android.net.Uri.Builder buildUpon() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->buildUpon()Landroid/net/Uri$Builder;");
    }

    public int compareTo(android.net.Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->compareTo(Landroid/net/Uri;)I");
    }

    public static java.lang.String decode(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->decode(Ljava/lang/String;)Ljava/lang/String;");
    }

    public static java.lang.String encode(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->encode(Ljava/lang/String;)Ljava/lang/String;");
    }

    public static java.lang.String encode(java.lang.String arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->encode(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
    }

    public boolean equals(java.lang.Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->equals(Ljava/lang/Object;)Z");
    }

    public static android.net.Uri fromFile(java.io.File arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->fromFile(Ljava/io/File;)Landroid/net/Uri;");
    }

    public static android.net.Uri fromParts(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->fromParts(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri;");
    }

    public java.lang.String getAuthority() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getAuthority()Ljava/lang/String;");
    }

    public boolean getBooleanQueryParameter(java.lang.String arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getBooleanQueryParameter(Ljava/lang/String;Z)Z");
    }

    public java.lang.String getEncodedAuthority() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getEncodedAuthority()Ljava/lang/String;");
    }

    public java.lang.String getEncodedFragment() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getEncodedFragment()Ljava/lang/String;");
    }

    public java.lang.String getEncodedPath() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getEncodedPath()Ljava/lang/String;");
    }

    public java.lang.String getEncodedQuery() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getEncodedQuery()Ljava/lang/String;");
    }

    public java.lang.String getEncodedSchemeSpecificPart() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getEncodedSchemeSpecificPart()Ljava/lang/String;");
    }

    public java.lang.String getEncodedUserInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getEncodedUserInfo()Ljava/lang/String;");
    }

    public java.lang.String getFragment() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getFragment()Ljava/lang/String;");
    }

    public java.lang.String getHost() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getHost()Ljava/lang/String;");
    }

    public java.lang.String getLastPathSegment() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getLastPathSegment()Ljava/lang/String;");
    }

    public java.lang.String getPath() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getPath()Ljava/lang/String;");
    }

    public java.util.List<java.lang.String> getPathSegments() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getPathSegments()Ljava/util/List;");
    }

    public int getPort() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getPort()I");
    }

    public java.lang.String getQuery() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getQuery()Ljava/lang/String;");
    }

    public java.lang.String getQueryParameter(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getQueryParameter(Ljava/lang/String;)Ljava/lang/String;");
    }

    public java.util.Set<java.lang.String> getQueryParameterNames() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getQueryParameterNames()Ljava/util/Set;");
    }

    public java.util.List<java.lang.String> getQueryParameters(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getQueryParameters(Ljava/lang/String;)Ljava/util/List;");
    }

    public java.lang.String getScheme() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getScheme()Ljava/lang/String;");
    }

    public java.lang.String getSchemeSpecificPart() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getSchemeSpecificPart()Ljava/lang/String;");
    }

    public java.lang.String getUserInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getUserInfo()Ljava/lang/String;");
    }

    public int hashCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->hashCode()I");
    }

    public boolean isAbsolute() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->isAbsolute()Z");
    }

    public boolean isHierarchical() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->isHierarchical()Z");
    }

    public boolean isOpaque() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->isOpaque()Z");
    }

    public boolean isRelative() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->isRelative()Z");
    }

    public android.net.Uri normalizeScheme() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->normalizeScheme()Landroid/net/Uri;");
    }

    public static android.net.Uri parse(java.lang.String arg0) {
        android.net.Uri u = new android.net.Uri();
        u.uriString = arg0;
        return u;
    }

    public java.lang.String toString() {
        return (uriString == null) ? "" : uriString;
    }

    public static android.net.Uri withAppendedPath(android.net.Uri arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->withAppendedPath(Landroid/net/Uri;Ljava/lang/String;)Landroid/net/Uri;");
    }

    public static void writeToParcel(android.os.Parcel arg0, android.net.Uri arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->writeToParcel(Landroid/os/Parcel;Landroid/net/Uri;)V");
    }
        public static class Builder {

        public Builder() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;-><init>()V");
        }

        public android.net.Uri.Builder appendEncodedPath(java.lang.String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->appendEncodedPath(Ljava/lang/String;)Landroid/net/Uri$Builder;");
        }

        public android.net.Uri.Builder appendPath(java.lang.String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->appendPath(Ljava/lang/String;)Landroid/net/Uri$Builder;");
        }

        public android.net.Uri.Builder appendQueryParameter(java.lang.String arg0, java.lang.String arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->appendQueryParameter(Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri$Builder;");
        }

        public android.net.Uri.Builder authority(java.lang.String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->authority(Ljava/lang/String;)Landroid/net/Uri$Builder;");
        }

        public android.net.Uri build() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->build()Landroid/net/Uri;");
        }

        public android.net.Uri.Builder clearQuery() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->clearQuery()Landroid/net/Uri$Builder;");
        }

        public android.net.Uri.Builder encodedAuthority(java.lang.String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->encodedAuthority(Ljava/lang/String;)Landroid/net/Uri$Builder;");
        }

        public android.net.Uri.Builder encodedFragment(java.lang.String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->encodedFragment(Ljava/lang/String;)Landroid/net/Uri$Builder;");
        }

        public android.net.Uri.Builder encodedOpaquePart(java.lang.String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->encodedOpaquePart(Ljava/lang/String;)Landroid/net/Uri$Builder;");
        }

        public android.net.Uri.Builder encodedPath(java.lang.String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->encodedPath(Ljava/lang/String;)Landroid/net/Uri$Builder;");
        }

        public android.net.Uri.Builder encodedQuery(java.lang.String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->encodedQuery(Ljava/lang/String;)Landroid/net/Uri$Builder;");
        }

        public android.net.Uri.Builder fragment(java.lang.String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->fragment(Ljava/lang/String;)Landroid/net/Uri$Builder;");
        }

        public android.net.Uri.Builder opaquePart(java.lang.String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->opaquePart(Ljava/lang/String;)Landroid/net/Uri$Builder;");
        }

        public android.net.Uri.Builder path(java.lang.String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->path(Ljava/lang/String;)Landroid/net/Uri$Builder;");
        }

        public android.net.Uri.Builder query(java.lang.String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->query(Ljava/lang/String;)Landroid/net/Uri$Builder;");
        }

        public android.net.Uri.Builder scheme(java.lang.String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->scheme(Ljava/lang/String;)Landroid/net/Uri$Builder;");
        }

        public java.lang.String toString() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->toString()Ljava/lang/String;");
        }
        }
    }
