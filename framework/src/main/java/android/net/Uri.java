package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.File;
import java.util.List;
import java.util.Set;

    public class Uri implements Comparable<Uri>, Parcelable {
    private String uriString;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    public static Parcelable.Creator CREATOR;
    public static Uri EMPTY;

    public Uri.Builder buildUpon() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->buildUpon()Landroid/net/Uri$Builder;");
    }

    public int compareTo(Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->compareTo(Landroid/net/Uri;)I");
    }

    public static String decode(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->decode(Ljava/lang/String;)Ljava/lang/String;");
    }

    public static String encode(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->encode(Ljava/lang/String;)Ljava/lang/String;");
    }

    public static String encode(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->encode(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
    }

    public boolean equals(Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->equals(Ljava/lang/Object;)Z");
    }

    public static Uri fromFile(File arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->fromFile(Ljava/io/File;)Landroid/net/Uri;");
    }

    public static Uri fromParts(String arg0, String arg1, String arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->fromParts(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri;");
    }

    public String getAuthority() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getAuthority()Ljava/lang/String;");
    }

    public boolean getBooleanQueryParameter(String arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getBooleanQueryParameter(Ljava/lang/String;Z)Z");
    }

    public String getEncodedAuthority() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getEncodedAuthority()Ljava/lang/String;");
    }

    public String getEncodedFragment() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getEncodedFragment()Ljava/lang/String;");
    }

    public String getEncodedPath() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getEncodedPath()Ljava/lang/String;");
    }

    public String getEncodedQuery() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getEncodedQuery()Ljava/lang/String;");
    }

    public String getEncodedSchemeSpecificPart() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getEncodedSchemeSpecificPart()Ljava/lang/String;");
    }

    public String getEncodedUserInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getEncodedUserInfo()Ljava/lang/String;");
    }

    public String getFragment() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getFragment()Ljava/lang/String;");
    }

    public String getHost() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getHost()Ljava/lang/String;");
    }

    public String getLastPathSegment() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getLastPathSegment()Ljava/lang/String;");
    }

    public String getPath() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getPath()Ljava/lang/String;");
    }

    public List<String> getPathSegments() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getPathSegments()Ljava/util/List;");
    }

    public int getPort() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getPort()I");
    }

    public String getQuery() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getQuery()Ljava/lang/String;");
    }

    public String getQueryParameter(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getQueryParameter(Ljava/lang/String;)Ljava/lang/String;");
    }

    public Set<String> getQueryParameterNames() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getQueryParameterNames()Ljava/util/Set;");
    }

    public List<String> getQueryParameters(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getQueryParameters(Ljava/lang/String;)Ljava/util/List;");
    }

    public String getScheme() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getScheme()Ljava/lang/String;");
    }

    public String getSchemeSpecificPart() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->getSchemeSpecificPart()Ljava/lang/String;");
    }

    public String getUserInfo() {
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

    public Uri normalizeScheme() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->normalizeScheme()Landroid/net/Uri;");
    }

    public static Uri parse(String arg0) {
        Uri u = new Uri();
        u.uriString = arg0;
        return u;
    }

    public String toString() {
        return (uriString == null) ? "" : uriString;
    }

    public static Uri withAppendedPath(Uri arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->withAppendedPath(Landroid/net/Uri;Ljava/lang/String;)Landroid/net/Uri;");
    }

    public static void writeToParcel(Parcel arg0, Uri arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri;->writeToParcel(Landroid/os/Parcel;Landroid/net/Uri;)V");
    }
        public static class Builder {

        public Builder() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;-><init>()V");
        }

        public Uri.Builder appendEncodedPath(String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->appendEncodedPath(Ljava/lang/String;)Landroid/net/Uri$Builder;");
        }

        public Uri.Builder appendPath(String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->appendPath(Ljava/lang/String;)Landroid/net/Uri$Builder;");
        }

        public Uri.Builder appendQueryParameter(String arg0, String arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->appendQueryParameter(Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri$Builder;");
        }

        public Uri.Builder authority(String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->authority(Ljava/lang/String;)Landroid/net/Uri$Builder;");
        }

        public Uri build() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->build()Landroid/net/Uri;");
        }

        public Uri.Builder clearQuery() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->clearQuery()Landroid/net/Uri$Builder;");
        }

        public Uri.Builder encodedAuthority(String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->encodedAuthority(Ljava/lang/String;)Landroid/net/Uri$Builder;");
        }

        public Uri.Builder encodedFragment(String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->encodedFragment(Ljava/lang/String;)Landroid/net/Uri$Builder;");
        }

        public Uri.Builder encodedOpaquePart(String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->encodedOpaquePart(Ljava/lang/String;)Landroid/net/Uri$Builder;");
        }

        public Uri.Builder encodedPath(String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->encodedPath(Ljava/lang/String;)Landroid/net/Uri$Builder;");
        }

        public Uri.Builder encodedQuery(String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->encodedQuery(Ljava/lang/String;)Landroid/net/Uri$Builder;");
        }

        public Uri.Builder fragment(String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->fragment(Ljava/lang/String;)Landroid/net/Uri$Builder;");
        }

        public Uri.Builder opaquePart(String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->opaquePart(Ljava/lang/String;)Landroid/net/Uri$Builder;");
        }

        public Uri.Builder path(String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->path(Ljava/lang/String;)Landroid/net/Uri$Builder;");
        }

        public Uri.Builder query(String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->query(Ljava/lang/String;)Landroid/net/Uri$Builder;");
        }

        public Uri.Builder scheme(String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->scheme(Ljava/lang/String;)Landroid/net/Uri$Builder;");
        }

        public String toString() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/net/Uri$Builder;->toString()Ljava/lang/String;");
        }
        }
    }
