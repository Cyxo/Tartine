package android.graphics;

import android.content.res.Resources;
import android.util.TypedValue;
import java.io.FileDescriptor;
import java.io.InputStream;

    public class BitmapFactory {

    public BitmapFactory() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/BitmapFactory;-><init>()V");
    }

    public static Bitmap decodeByteArray(byte[] arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/BitmapFactory;->decodeByteArray([BII)Landroid/graphics/Bitmap;");
    }

    public static Bitmap decodeByteArray(byte[] arg0, int arg1, int arg2, BitmapFactory.Options arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/BitmapFactory;->decodeByteArray([BIILandroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;");
    }

    public static Bitmap decodeFile(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/BitmapFactory;->decodeFile(Ljava/lang/String;)Landroid/graphics/Bitmap;");
    }

    public static Bitmap decodeFile(String arg0, BitmapFactory.Options arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/BitmapFactory;->decodeFile(Ljava/lang/String;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;");
    }

    public static Bitmap decodeFileDescriptor(FileDescriptor arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/BitmapFactory;->decodeFileDescriptor(Ljava/io/FileDescriptor;)Landroid/graphics/Bitmap;");
    }

    public static Bitmap decodeFileDescriptor(FileDescriptor arg0, Rect arg1, BitmapFactory.Options arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/BitmapFactory;->decodeFileDescriptor(Ljava/io/FileDescriptor;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;");
    }

    public static Bitmap decodeResource(Resources arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/BitmapFactory;->decodeResource(Landroid/content/res/Resources;I)Landroid/graphics/Bitmap;");
    }

    public static Bitmap decodeResource(Resources arg0, int arg1, BitmapFactory.Options arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/BitmapFactory;->decodeResource(Landroid/content/res/Resources;ILandroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;");
    }

    public static Bitmap decodeResourceStream(Resources arg0, TypedValue arg1, InputStream arg2, Rect arg3, BitmapFactory.Options arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/BitmapFactory;->decodeResourceStream(Landroid/content/res/Resources;Landroid/util/TypedValue;Ljava/io/InputStream;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;");
    }

    public static Bitmap decodeStream(InputStream arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/BitmapFactory;->decodeStream(Ljava/io/InputStream;)Landroid/graphics/Bitmap;");
    }

    public static Bitmap decodeStream(InputStream arg0, Rect arg1, BitmapFactory.Options arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/BitmapFactory;->decodeStream(Ljava/io/InputStream;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;");
    }
        public static class Options {

        public Bitmap inBitmap;
        public int inDensity;
        public boolean inDither;
        public boolean inInputShareable;
        public boolean inJustDecodeBounds;
        public boolean inMutable;
        public boolean inPreferQualityOverSpeed;
        public ColorSpace inPreferredColorSpace;
        public Bitmap.Config inPreferredConfig;
        public boolean inPremultiplied;
        public boolean inPurgeable;
        public int inSampleSize;
        public boolean inScaled;
        public int inScreenDensity;
        public int inTargetDensity;
        public byte[] inTempStorage;
        public boolean mCancel;
        public ColorSpace outColorSpace;
        public Bitmap.Config outConfig;
        public int outHeight;
        public String outMimeType;
        public int outWidth;

        public Options() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/BitmapFactory$Options;-><init>()V");
        }

        public void requestCancelDecode() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/BitmapFactory$Options;->requestCancelDecode()V");
        }
        }
    }
