package android.net;

    public abstract class Uri implements java.lang.Comparable<android.net.Uri>, android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;
    public static android.net.Uri EMPTY;

    public abstract android.net.Uri.Builder buildUpon();

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

    public abstract java.lang.String getAuthority();

    public boolean getBooleanQueryParameter(java.lang.String arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getBooleanQueryParameter(Ljava/lang/String;Z)Z");
    }

    public abstract java.lang.String getEncodedAuthority();

    public abstract java.lang.String getEncodedFragment();

    public abstract java.lang.String getEncodedPath();

    public abstract java.lang.String getEncodedQuery();

    public abstract java.lang.String getEncodedSchemeSpecificPart();

    public abstract java.lang.String getEncodedUserInfo();

    public abstract java.lang.String getFragment();

    public abstract java.lang.String getHost();

    public abstract java.lang.String getLastPathSegment();

    public abstract java.lang.String getPath();

    public abstract java.util.List<java.lang.String> getPathSegments();

    public abstract int getPort();

    public abstract java.lang.String getQuery();

    public java.lang.String getQueryParameter(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getQueryParameter(Ljava/lang/String;)Ljava/lang/String;");
    }

    public java.util.Set<java.lang.String> getQueryParameterNames() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getQueryParameterNames()Ljava/util/Set;");
    }

    public java.util.List<java.lang.String> getQueryParameters(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getQueryParameters(Ljava/lang/String;)Ljava/util/List;");
    }

    public abstract java.lang.String getScheme();

    public abstract java.lang.String getSchemeSpecificPart();

    public abstract java.lang.String getUserInfo();

    public int hashCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->hashCode()I");
    }

    public boolean isAbsolute() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->isAbsolute()Z");
    }

    public abstract boolean isHierarchical();

    public boolean isOpaque() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->isOpaque()Z");
    }

    public abstract boolean isRelative();

    public android.net.Uri normalizeScheme() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->normalizeScheme()Landroid/net/Uri;");
    }

    public static android.net.Uri parse(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->parse(Ljava/lang/String;)Landroid/net/Uri;");
    }

    public abstract java.lang.String toString();

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
