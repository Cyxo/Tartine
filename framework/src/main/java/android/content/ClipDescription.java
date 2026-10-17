package android.content;

    public class ClipDescription implements android.os.Parcelable {

    public static final int CLASSIFICATION_COMPLETE = 3;
    public static final int CLASSIFICATION_NOT_COMPLETE = 1;
    public static final int CLASSIFICATION_NOT_PERFORMED = 2;
    public static android.os.Parcelable.Creator CREATOR;
    public static final java.lang.String EXTRA_IS_REMOTE_DEVICE = "android.content.extra.IS_REMOTE_DEVICE";
    public static final java.lang.String EXTRA_IS_SENSITIVE = "android.content.extra.IS_SENSITIVE";
    public static final java.lang.String MIMETYPE_TEXT_HTML = "text/html";
    public static final java.lang.String MIMETYPE_TEXT_INTENT = "text/vnd.android.intent";
    public static final java.lang.String MIMETYPE_TEXT_PLAIN = "text/plain";
    public static final java.lang.String MIMETYPE_TEXT_URILIST = "text/uri-list";
    public static final java.lang.String MIMETYPE_UNKNOWN = "application/octet-stream";

    public ClipDescription(android.content.ClipDescription arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;-><init>(Landroid/content/ClipDescription;)V");
    }

    public ClipDescription(java.lang.CharSequence arg0, java.lang.String[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;-><init>(Ljava/lang/CharSequence;[Ljava/lang/String;)V");
    }

    public static boolean compareMimeTypes(java.lang.String arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;->compareMimeTypes(Ljava/lang/String;Ljava/lang/String;)Z");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;->describeContents()I");
    }

    public java.lang.String[] filterMimeTypes(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;->filterMimeTypes(Ljava/lang/String;)[Ljava/lang/String;");
    }

    public int getClassificationStatus() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;->getClassificationStatus()I");
    }

    public float getConfidenceScore(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;->getConfidenceScore(Ljava/lang/String;)F");
    }

    public android.os.PersistableBundle getExtras() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;->getExtras()Landroid/os/PersistableBundle;");
    }

    public java.lang.CharSequence getLabel() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;->getLabel()Ljava/lang/CharSequence;");
    }

    public java.lang.String getMimeType(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;->getMimeType(I)Ljava/lang/String;");
    }

    public int getMimeTypeCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;->getMimeTypeCount()I");
    }

    public long getTimestamp() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;->getTimestamp()J");
    }

    public boolean hasMimeType(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;->hasMimeType(Ljava/lang/String;)Z");
    }

    public boolean isStyledText() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;->isStyledText()Z");
    }

    public void setExtras(android.os.PersistableBundle arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;->setExtras(Landroid/os/PersistableBundle;)V");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ClipDescription;->writeToParcel(Landroid/os/Parcel;I)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public ClipDescription() {
        }
    }
