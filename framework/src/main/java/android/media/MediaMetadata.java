package android.media;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Set;

    public class MediaMetadata implements Parcelable {

    public static Parcelable.Creator CREATOR;
    public static final String METADATA_KEY_ALBUM = "android.media.metadata.ALBUM";
    public static final String METADATA_KEY_ALBUM_ART = "android.media.metadata.ALBUM_ART";
    public static final String METADATA_KEY_ALBUM_ARTIST = "android.media.metadata.ALBUM_ARTIST";
    public static final String METADATA_KEY_ALBUM_ART_URI = "android.media.metadata.ALBUM_ART_URI";
    public static final String METADATA_KEY_ART = "android.media.metadata.ART";
    public static final String METADATA_KEY_ARTIST = "android.media.metadata.ARTIST";
    public static final String METADATA_KEY_ART_URI = "android.media.metadata.ART_URI";
    public static final String METADATA_KEY_AUTHOR = "android.media.metadata.AUTHOR";
    public static final String METADATA_KEY_BT_FOLDER_TYPE = "android.media.metadata.BT_FOLDER_TYPE";
    public static final String METADATA_KEY_COMPILATION = "android.media.metadata.COMPILATION";
    public static final String METADATA_KEY_COMPOSER = "android.media.metadata.COMPOSER";
    public static final String METADATA_KEY_DATE = "android.media.metadata.DATE";
    public static final String METADATA_KEY_DISC_NUMBER = "android.media.metadata.DISC_NUMBER";
    public static final String METADATA_KEY_DISPLAY_DESCRIPTION = "android.media.metadata.DISPLAY_DESCRIPTION";
    public static final String METADATA_KEY_DISPLAY_ICON = "android.media.metadata.DISPLAY_ICON";
    public static final String METADATA_KEY_DISPLAY_ICON_URI = "android.media.metadata.DISPLAY_ICON_URI";
    public static final String METADATA_KEY_DISPLAY_SUBTITLE = "android.media.metadata.DISPLAY_SUBTITLE";
    public static final String METADATA_KEY_DISPLAY_TITLE = "android.media.metadata.DISPLAY_TITLE";
    public static final String METADATA_KEY_DURATION = "android.media.metadata.DURATION";
    public static final String METADATA_KEY_GENRE = "android.media.metadata.GENRE";
    public static final String METADATA_KEY_MEDIA_ID = "android.media.metadata.MEDIA_ID";
    public static final String METADATA_KEY_MEDIA_URI = "android.media.metadata.MEDIA_URI";
    public static final String METADATA_KEY_NUM_TRACKS = "android.media.metadata.NUM_TRACKS";
    public static final String METADATA_KEY_RATING = "android.media.metadata.RATING";
    public static final String METADATA_KEY_TITLE = "android.media.metadata.TITLE";
    public static final String METADATA_KEY_TRACK_NUMBER = "android.media.metadata.TRACK_NUMBER";
    public static final String METADATA_KEY_USER_RATING = "android.media.metadata.USER_RATING";
    public static final String METADATA_KEY_WRITER = "android.media.metadata.WRITER";
    public static final String METADATA_KEY_YEAR = "android.media.metadata.YEAR";

    public boolean containsKey(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/MediaMetadata;->containsKey(Ljava/lang/String;)Z");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/MediaMetadata;->describeContents()I");
    }

    public boolean equals(Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/MediaMetadata;->equals(Ljava/lang/Object;)Z");
    }

    public Bitmap getBitmap(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/MediaMetadata;->getBitmap(Ljava/lang/String;)Landroid/graphics/Bitmap;");
    }

    public int getBitmapDimensionLimit() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/MediaMetadata;->getBitmapDimensionLimit()I");
    }

    public MediaDescription getDescription() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/MediaMetadata;->getDescription()Landroid/media/MediaDescription;");
    }

    public long getLong(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/MediaMetadata;->getLong(Ljava/lang/String;)J");
    }

    public Rating getRating(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/MediaMetadata;->getRating(Ljava/lang/String;)Landroid/media/Rating;");
    }

    public String getString(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/MediaMetadata;->getString(Ljava/lang/String;)Ljava/lang/String;");
    }

    public CharSequence getText(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/MediaMetadata;->getText(Ljava/lang/String;)Ljava/lang/CharSequence;");
    }

    public int hashCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/MediaMetadata;->hashCode()I");
    }

    public Set<String> keySet() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/MediaMetadata;->keySet()Ljava/util/Set;");
    }

    public int size() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/MediaMetadata;->size()I");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/MediaMetadata;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
