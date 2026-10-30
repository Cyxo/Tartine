package android.media;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

    public class MediaDescription implements Parcelable {

    public static final long BT_FOLDER_TYPE_ALBUMS = 2L;
    public static final long BT_FOLDER_TYPE_ARTISTS = 3L;
    public static final long BT_FOLDER_TYPE_GENRES = 4L;
    public static final long BT_FOLDER_TYPE_MIXED = 0L;
    public static final long BT_FOLDER_TYPE_PLAYLISTS = 5L;
    public static final long BT_FOLDER_TYPE_TITLES = 1L;
    public static final long BT_FOLDER_TYPE_YEARS = 6L;
    public static Parcelable.Creator CREATOR;
    public static final String EXTRA_BT_FOLDER_TYPE = "android.media.extra.BT_FOLDER_TYPE";

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/MediaDescription;->describeContents()I");
    }

    public boolean equals(Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/MediaDescription;->equals(Ljava/lang/Object;)Z");
    }

    public CharSequence getDescription() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/MediaDescription;->getDescription()Ljava/lang/CharSequence;");
    }

    public Bundle getExtras() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/MediaDescription;->getExtras()Landroid/os/Bundle;");
    }

    public Bitmap getIconBitmap() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/MediaDescription;->getIconBitmap()Landroid/graphics/Bitmap;");
    }

    public Uri getIconUri() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/MediaDescription;->getIconUri()Landroid/net/Uri;");
    }

    public String getMediaId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/MediaDescription;->getMediaId()Ljava/lang/String;");
    }

    public Uri getMediaUri() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/MediaDescription;->getMediaUri()Landroid/net/Uri;");
    }

    public CharSequence getSubtitle() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/MediaDescription;->getSubtitle()Ljava/lang/CharSequence;");
    }

    public CharSequence getTitle() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/MediaDescription;->getTitle()Ljava/lang/CharSequence;");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/MediaDescription;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/MediaDescription;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
