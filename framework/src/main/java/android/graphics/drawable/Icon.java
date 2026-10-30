package android.graphics.drawable;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BlendMode;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;

    public class Icon implements Parcelable {

    public static Parcelable.Creator CREATOR;
    public static final int TYPE_ADAPTIVE_BITMAP = 5;
    public static final int TYPE_BITMAP = 1;
    public static final int TYPE_DATA = 3;
    public static final int TYPE_RESOURCE = 2;
    public static final int TYPE_URI = 4;
    public static final int TYPE_URI_ADAPTIVE_BITMAP = 6;

    public static Icon createWithAdaptiveBitmap(Bitmap arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->createWithAdaptiveBitmap(Landroid/graphics/Bitmap;)Landroid/graphics/drawable/Icon;");
    }

    public static Icon createWithAdaptiveBitmapContentUri(Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->createWithAdaptiveBitmapContentUri(Landroid/net/Uri;)Landroid/graphics/drawable/Icon;");
    }

    public static Icon createWithAdaptiveBitmapContentUri(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->createWithAdaptiveBitmapContentUri(Ljava/lang/String;)Landroid/graphics/drawable/Icon;");
    }

    public static Icon createWithBitmap(Bitmap arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->createWithBitmap(Landroid/graphics/Bitmap;)Landroid/graphics/drawable/Icon;");
    }

    public static Icon createWithContentUri(Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->createWithContentUri(Landroid/net/Uri;)Landroid/graphics/drawable/Icon;");
    }

    public static Icon createWithContentUri(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->createWithContentUri(Ljava/lang/String;)Landroid/graphics/drawable/Icon;");
    }

    public static Icon createWithData(byte[] arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->createWithData([BII)Landroid/graphics/drawable/Icon;");
    }

    public static Icon createWithFilePath(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->createWithFilePath(Ljava/lang/String;)Landroid/graphics/drawable/Icon;");
    }

    public static Icon createWithResource(Context arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->createWithResource(Landroid/content/Context;I)Landroid/graphics/drawable/Icon;");
    }

    public static Icon createWithResource(String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->createWithResource(Ljava/lang/String;I)Landroid/graphics/drawable/Icon;");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->describeContents()I");
    }

    public int getResId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->getResId()I");
    }

    public String getResPackage() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->getResPackage()Ljava/lang/String;");
    }

    public int getType() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->getType()I");
    }

    public Uri getUri() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->getUri()Landroid/net/Uri;");
    }

    public Drawable loadDrawable(Context arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->loadDrawable(Landroid/content/Context;)Landroid/graphics/drawable/Drawable;");
    }

    public void loadDrawableAsync(Context arg0, Icon.OnDrawableLoadedListener arg1, Handler arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->loadDrawableAsync(Landroid/content/Context;Landroid/graphics/drawable/Icon$OnDrawableLoadedListener;Landroid/os/Handler;)V");
    }

    public void loadDrawableAsync(Context arg0, Message arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->loadDrawableAsync(Landroid/content/Context;Landroid/os/Message;)V");
    }

    public Icon setTint(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->setTint(I)Landroid/graphics/drawable/Icon;");
    }

    public Icon setTintBlendMode(BlendMode arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->setTintBlendMode(Landroid/graphics/BlendMode;)Landroid/graphics/drawable/Icon;");
    }

    public Icon setTintList(ColorStateList arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->setTintList(Landroid/content/res/ColorStateList;)Landroid/graphics/drawable/Icon;");
    }

    public Icon setTintMode(PorterDuff.Mode arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->setTintMode(Landroid/graphics/PorterDuff$Mode;)Landroid/graphics/drawable/Icon;");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->writeToParcel(Landroid/os/Parcel;I)V");
    }
        public static interface OnDrawableLoadedListener {

        public abstract void onDrawableLoaded(Drawable arg0);
        }
    }
