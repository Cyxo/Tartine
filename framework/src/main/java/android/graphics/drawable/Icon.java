package android.graphics.drawable;

    public class Icon implements android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;
    public static final int TYPE_ADAPTIVE_BITMAP = 5;
    public static final int TYPE_BITMAP = 1;
    public static final int TYPE_DATA = 3;
    public static final int TYPE_RESOURCE = 2;
    public static final int TYPE_URI = 4;
    public static final int TYPE_URI_ADAPTIVE_BITMAP = 6;

    public static android.graphics.drawable.Icon createWithAdaptiveBitmap(android.graphics.Bitmap arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->createWithAdaptiveBitmap(Landroid/graphics/Bitmap;)Landroid/graphics/drawable/Icon;");
    }

    public static android.graphics.drawable.Icon createWithAdaptiveBitmapContentUri(android.net.Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->createWithAdaptiveBitmapContentUri(Landroid/net/Uri;)Landroid/graphics/drawable/Icon;");
    }

    public static android.graphics.drawable.Icon createWithAdaptiveBitmapContentUri(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->createWithAdaptiveBitmapContentUri(Ljava/lang/String;)Landroid/graphics/drawable/Icon;");
    }

    public static android.graphics.drawable.Icon createWithBitmap(android.graphics.Bitmap arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->createWithBitmap(Landroid/graphics/Bitmap;)Landroid/graphics/drawable/Icon;");
    }

    public static android.graphics.drawable.Icon createWithContentUri(android.net.Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->createWithContentUri(Landroid/net/Uri;)Landroid/graphics/drawable/Icon;");
    }

    public static android.graphics.drawable.Icon createWithContentUri(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->createWithContentUri(Ljava/lang/String;)Landroid/graphics/drawable/Icon;");
    }

    public static android.graphics.drawable.Icon createWithData(byte[] arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->createWithData([BII)Landroid/graphics/drawable/Icon;");
    }

    public static android.graphics.drawable.Icon createWithFilePath(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->createWithFilePath(Ljava/lang/String;)Landroid/graphics/drawable/Icon;");
    }

    public static android.graphics.drawable.Icon createWithResource(android.content.Context arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->createWithResource(Landroid/content/Context;I)Landroid/graphics/drawable/Icon;");
    }

    public static android.graphics.drawable.Icon createWithResource(java.lang.String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->createWithResource(Ljava/lang/String;I)Landroid/graphics/drawable/Icon;");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->describeContents()I");
    }

    public int getResId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->getResId()I");
    }

    public java.lang.String getResPackage() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->getResPackage()Ljava/lang/String;");
    }

    public int getType() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->getType()I");
    }

    public android.net.Uri getUri() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->getUri()Landroid/net/Uri;");
    }

    public android.graphics.drawable.Drawable loadDrawable(android.content.Context arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->loadDrawable(Landroid/content/Context;)Landroid/graphics/drawable/Drawable;");
    }

    public void loadDrawableAsync(android.content.Context arg0, android.graphics.drawable.Icon.OnDrawableLoadedListener arg1, android.os.Handler arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->loadDrawableAsync(Landroid/content/Context;Landroid/graphics/drawable/Icon$OnDrawableLoadedListener;Landroid/os/Handler;)V");
    }

    public void loadDrawableAsync(android.content.Context arg0, android.os.Message arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->loadDrawableAsync(Landroid/content/Context;Landroid/os/Message;)V");
    }

    public android.graphics.drawable.Icon setTint(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->setTint(I)Landroid/graphics/drawable/Icon;");
    }

    public android.graphics.drawable.Icon setTintBlendMode(android.graphics.BlendMode arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->setTintBlendMode(Landroid/graphics/BlendMode;)Landroid/graphics/drawable/Icon;");
    }

    public android.graphics.drawable.Icon setTintList(android.content.res.ColorStateList arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->setTintList(Landroid/content/res/ColorStateList;)Landroid/graphics/drawable/Icon;");
    }

    public android.graphics.drawable.Icon setTintMode(android.graphics.PorterDuff.Mode arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->setTintMode(Landroid/graphics/PorterDuff$Mode;)Landroid/graphics/drawable/Icon;");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/drawable/Icon;->writeToParcel(Landroid/os/Parcel;I)V");
    }
        public static interface OnDrawableLoadedListener {

        public abstract void onDrawableLoaded(android.graphics.drawable.Drawable arg0);
        }
    }
