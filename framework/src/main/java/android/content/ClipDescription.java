package android.content;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;

    public class ClipDescription implements Parcelable {

    public static final int CLASSIFICATION_COMPLETE = 3;
    public static final int CLASSIFICATION_NOT_COMPLETE = 1;
    public static final int CLASSIFICATION_NOT_PERFORMED = 2;
    public static Parcelable.Creator CREATOR;
    public static final String EXTRA_IS_REMOTE_DEVICE = "android.content.extra.IS_REMOTE_DEVICE";
    public static final String EXTRA_IS_SENSITIVE = "android.content.extra.IS_SENSITIVE";
    public static final String MIMETYPE_TEXT_HTML = "text/html";
    public static final String MIMETYPE_TEXT_INTENT = "text/vnd.android.intent";
    public static final String MIMETYPE_TEXT_PLAIN = "text/plain";
    public static final String MIMETYPE_TEXT_URILIST = "text/uri-list";
    public static final String MIMETYPE_UNKNOWN = "application/octet-stream";

    public ClipDescription(ClipDescription arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;-><init>(Landroid/content/ClipDescription;)V");
    }

    public ClipDescription(CharSequence arg0, String[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;-><init>(Ljava/lang/CharSequence;[Ljava/lang/String;)V");
    }

    public static boolean compareMimeTypes(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;->compareMimeTypes(Ljava/lang/String;Ljava/lang/String;)Z");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;->describeContents()I");
    }

    public String[] filterMimeTypes(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;->filterMimeTypes(Ljava/lang/String;)[Ljava/lang/String;");
    }

    public int getClassificationStatus() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;->getClassificationStatus()I");
    }

    public float getConfidenceScore(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;->getConfidenceScore(Ljava/lang/String;)F");
    }

    public PersistableBundle getExtras() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;->getExtras()Landroid/os/PersistableBundle;");
    }

    public CharSequence getLabel() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;->getLabel()Ljava/lang/CharSequence;");
    }

    public String getMimeType(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;->getMimeType(I)Ljava/lang/String;");
    }

    public int getMimeTypeCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;->getMimeTypeCount()I");
    }

    public long getTimestamp() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;->getTimestamp()J");
    }

    public boolean hasMimeType(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;->hasMimeType(Ljava/lang/String;)Z");
    }

    public boolean isStyledText() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;->isStyledText()Z");
    }

    public void setExtras(PersistableBundle arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;->setExtras(Landroid/os/PersistableBundle;)V");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;->writeToParcel(Landroid/os/Parcel;I)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public ClipDescription() {
        }
    }
