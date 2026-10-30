package android.content;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

    public class UriPermission implements Parcelable {

    public static Parcelable.Creator CREATOR;
    public static final long INVALID_TIME = -9223372036854775808L;

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/UriPermission;->describeContents()I");
    }

    public long getPersistedTime() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/UriPermission;->getPersistedTime()J");
    }

    public Uri getUri() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/UriPermission;->getUri()Landroid/net/Uri;");
    }

    public boolean isReadPermission() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/UriPermission;->isReadPermission()Z");
    }

    public boolean isWritePermission() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/UriPermission;->isWritePermission()Z");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/UriPermission;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/UriPermission;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
