package android.content;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

    public class ContentProviderResult implements Parcelable {

    public static Parcelable.Creator CREATOR;
    public Integer count;
    public Throwable exception;
    public Bundle extras;
    public Uri uri;

    public ContentProviderResult(Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderResult;-><init>(Landroid/net/Uri;)V");
    }

    public ContentProviderResult(Bundle arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderResult;-><init>(Landroid/os/Bundle;)V");
    }

    public ContentProviderResult(Parcel arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderResult;-><init>(Landroid/os/Parcel;)V");
    }

    public ContentProviderResult(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderResult;-><init>(I)V");
    }

    public ContentProviderResult(Throwable arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderResult;-><init>(Ljava/lang/Throwable;)V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderResult;->describeContents()I");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderResult;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/ContentProviderResult;->writeToParcel(Landroid/os/Parcel;I)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public ContentProviderResult() {
        }
    }
