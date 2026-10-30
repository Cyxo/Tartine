package android.view.inputmethod;

import android.content.ClipDescription;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

    public class InputContentInfo implements Parcelable {

    public static Parcelable.Creator CREATOR;

    public InputContentInfo(Uri arg0, ClipDescription arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputContentInfo;-><init>(Landroid/net/Uri;Landroid/content/ClipDescription;)V");
    }

    public InputContentInfo(Uri arg0, ClipDescription arg1, Uri arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputContentInfo;-><init>(Landroid/net/Uri;Landroid/content/ClipDescription;Landroid/net/Uri;)V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputContentInfo;->describeContents()I");
    }

    public Uri getContentUri() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputContentInfo;->getContentUri()Landroid/net/Uri;");
    }

    public ClipDescription getDescription() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputContentInfo;->getDescription()Landroid/content/ClipDescription;");
    }

    public Uri getLinkUri() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputContentInfo;->getLinkUri()Landroid/net/Uri;");
    }

    public void releasePermission() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputContentInfo;->releasePermission()V");
    }

    public void requestPermission() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputContentInfo;->requestPermission()V");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputContentInfo;->writeToParcel(Landroid/os/Parcel;I)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public InputContentInfo() {
        }
    }
