package android.view;

    public class ContentInfo implements android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;
    public static final int FLAG_CONVERT_TO_PLAIN_TEXT = 1;
    public static final int SOURCE_APP = 0;
    public static final int SOURCE_AUTOFILL = 4;
    public static final int SOURCE_CLIPBOARD = 1;
    public static final int SOURCE_DRAG_AND_DROP = 3;
    public static final int SOURCE_INPUT_METHOD = 2;
    public static final int SOURCE_PROCESS_TEXT = 5;

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ContentInfo;->describeContents()I");
    }

    public android.content.ClipData getClip() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ContentInfo;->getClip()Landroid/content/ClipData;");
    }

    public android.os.Bundle getExtras() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ContentInfo;->getExtras()Landroid/os/Bundle;");
    }

    public int getFlags() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ContentInfo;->getFlags()I");
    }

    public android.net.Uri getLinkUri() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ContentInfo;->getLinkUri()Landroid/net/Uri;");
    }

    public int getSource() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ContentInfo;->getSource()I");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ContentInfo;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ContentInfo;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
