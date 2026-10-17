package android.graphics;

    public class BitmapFactory {

    public BitmapFactory() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/BitmapFactory;-><init>()V");
    }

    public static android.graphics.Bitmap decodeByteArray(byte[] arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/BitmapFactory;->decodeByteArray([BII)Landroid/graphics/Bitmap;");
    }

    public static android.graphics.Bitmap decodeByteArray(byte[] arg0, int arg1, int arg2, android.graphics.BitmapFactory.Options arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/BitmapFactory;->decodeByteArray([BIILandroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;");
    }

    public static android.graphics.Bitmap decodeFile(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/BitmapFactory;->decodeFile(Ljava/lang/String;)Landroid/graphics/Bitmap;");
    }

    public static android.graphics.Bitmap decodeFile(java.lang.String arg0, android.graphics.BitmapFactory.Options arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/BitmapFactory;->decodeFile(Ljava/lang/String;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;");
    }

    public static android.graphics.Bitmap decodeFileDescriptor(java.io.FileDescriptor arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/BitmapFactory;->decodeFileDescriptor(Ljava/io/FileDescriptor;)Landroid/graphics/Bitmap;");
    }

    public static android.graphics.Bitmap decodeFileDescriptor(java.io.FileDescriptor arg0, android.graphics.Rect arg1, android.graphics.BitmapFactory.Options arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/BitmapFactory;->decodeFileDescriptor(Ljava/io/FileDescriptor;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;");
    }

    public static android.graphics.Bitmap decodeResource(android.content.res.Resources arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/BitmapFactory;->decodeResource(Landroid/content/res/Resources;I)Landroid/graphics/Bitmap;");
    }

    public static android.graphics.Bitmap decodeResource(android.content.res.Resources arg0, int arg1, android.graphics.BitmapFactory.Options arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/BitmapFactory;->decodeResource(Landroid/content/res/Resources;ILandroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;");
    }

    public static android.graphics.Bitmap decodeResourceStream(android.content.res.Resources arg0, android.util.TypedValue arg1, java.io.InputStream arg2, android.graphics.Rect arg3, android.graphics.BitmapFactory.Options arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/BitmapFactory;->decodeResourceStream(Landroid/content/res/Resources;Landroid/util/TypedValue;Ljava/io/InputStream;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;");
    }

    public static android.graphics.Bitmap decodeStream(java.io.InputStream arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/BitmapFactory;->decodeStream(Ljava/io/InputStream;)Landroid/graphics/Bitmap;");
    }

    public static android.graphics.Bitmap decodeStream(java.io.InputStream arg0, android.graphics.Rect arg1, android.graphics.BitmapFactory.Options arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/BitmapFactory;->decodeStream(Ljava/io/InputStream;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;");
    }
        public static class Options {

        public android.graphics.Bitmap inBitmap;
        public int inDensity;
        public boolean inDither;
        public boolean inInputShareable;
        public boolean inJustDecodeBounds;
        public boolean inMutable;
        public boolean inPreferQualityOverSpeed;
        public android.graphics.ColorSpace inPreferredColorSpace;
        public android.graphics.Bitmap.Config inPreferredConfig;
        public boolean inPremultiplied;
        public boolean inPurgeable;
        public int inSampleSize;
        public boolean inScaled;
        public int inScreenDensity;
        public int inTargetDensity;
        public byte[] inTempStorage;
        public boolean mCancel;
        public android.graphics.ColorSpace outColorSpace;
        public android.graphics.Bitmap.Config outConfig;
        public int outHeight;
        public java.lang.String outMimeType;
        public int outWidth;

        public Options() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/BitmapFactory$Options;-><init>()V");
        }

        public void requestCancelDecode() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/BitmapFactory$Options;->requestCancelDecode()V");
        }
        }
    }
