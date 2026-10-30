package android.graphics;

import android.hardware.HardwareBuffer;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import java.io.OutputStream;
import java.nio.Buffer;

    public class Bitmap implements Parcelable {

    public static Parcelable.Creator CREATOR;
    public static final int DENSITY_NONE = 0;

    public Bitmap asShared() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->asShared()Landroid/graphics/Bitmap;");
    }

    public boolean compress(Bitmap.CompressFormat arg0, int arg1, OutputStream arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->compress(Landroid/graphics/Bitmap$CompressFormat;ILjava/io/OutputStream;)Z");
    }

    public Bitmap copy(Bitmap.Config arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->copy(Landroid/graphics/Bitmap$Config;Z)Landroid/graphics/Bitmap;");
    }

    public void copyPixelsFromBuffer(Buffer arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->copyPixelsFromBuffer(Ljava/nio/Buffer;)V");
    }

    public void copyPixelsToBuffer(Buffer arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->copyPixelsToBuffer(Ljava/nio/Buffer;)V");
    }

    public static Bitmap createBitmap(Bitmap arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->createBitmap(Landroid/graphics/Bitmap;)Landroid/graphics/Bitmap;");
    }

    public static Bitmap createBitmap(Bitmap arg0, int arg1, int arg2, int arg3, int arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->createBitmap(Landroid/graphics/Bitmap;IIII)Landroid/graphics/Bitmap;");
    }

    public static Bitmap createBitmap(Bitmap arg0, int arg1, int arg2, int arg3, int arg4, Matrix arg5, boolean arg6) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->createBitmap(Landroid/graphics/Bitmap;IIIILandroid/graphics/Matrix;Z)Landroid/graphics/Bitmap;");
    }

    public static Bitmap createBitmap(Picture arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->createBitmap(Landroid/graphics/Picture;)Landroid/graphics/Bitmap;");
    }

    public static Bitmap createBitmap(Picture arg0, int arg1, int arg2, Bitmap.Config arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->createBitmap(Landroid/graphics/Picture;IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;");
    }

    public static Bitmap createBitmap(DisplayMetrics arg0, int arg1, int arg2, Bitmap.Config arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->createBitmap(Landroid/util/DisplayMetrics;IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;");
    }

    public static Bitmap createBitmap(DisplayMetrics arg0, int arg1, int arg2, Bitmap.Config arg3, boolean arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->createBitmap(Landroid/util/DisplayMetrics;IILandroid/graphics/Bitmap$Config;Z)Landroid/graphics/Bitmap;");
    }

    public static Bitmap createBitmap(DisplayMetrics arg0, int arg1, int arg2, Bitmap.Config arg3, boolean arg4, ColorSpace arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->createBitmap(Landroid/util/DisplayMetrics;IILandroid/graphics/Bitmap$Config;ZLandroid/graphics/ColorSpace;)Landroid/graphics/Bitmap;");
    }

    public static Bitmap createBitmap(DisplayMetrics arg0, int[] arg1, int arg2, int arg3, Bitmap.Config arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->createBitmap(Landroid/util/DisplayMetrics;[IIILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;");
    }

    public static Bitmap createBitmap(DisplayMetrics arg0, int[] arg1, int arg2, int arg3, int arg4, int arg5, Bitmap.Config arg6) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->createBitmap(Landroid/util/DisplayMetrics;[IIIIILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;");
    }

    public static Bitmap createBitmap(int arg0, int arg1, Bitmap.Config arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->createBitmap(IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;");
    }

    public static Bitmap createBitmap(int arg0, int arg1, Bitmap.Config arg2, boolean arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->createBitmap(IILandroid/graphics/Bitmap$Config;Z)Landroid/graphics/Bitmap;");
    }

    public static Bitmap createBitmap(int arg0, int arg1, Bitmap.Config arg2, boolean arg3, ColorSpace arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->createBitmap(IILandroid/graphics/Bitmap$Config;ZLandroid/graphics/ColorSpace;)Landroid/graphics/Bitmap;");
    }

    public static Bitmap createBitmap(int[] arg0, int arg1, int arg2, Bitmap.Config arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->createBitmap([IIILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;");
    }

    public static Bitmap createBitmap(int[] arg0, int arg1, int arg2, int arg3, int arg4, Bitmap.Config arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->createBitmap([IIIIILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;");
    }

    public static Bitmap createScaledBitmap(Bitmap arg0, int arg1, int arg2, boolean arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->createScaledBitmap(Landroid/graphics/Bitmap;IIZ)Landroid/graphics/Bitmap;");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->describeContents()I");
    }

    public void eraseColor(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->eraseColor(I)V");
    }

    public void eraseColor(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->eraseColor(J)V");
    }

    public Bitmap extractAlpha() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->extractAlpha()Landroid/graphics/Bitmap;");
    }

    public Bitmap extractAlpha(Paint arg0, int[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->extractAlpha(Landroid/graphics/Paint;[I)Landroid/graphics/Bitmap;");
    }

    public int getAllocationByteCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->getAllocationByteCount()I");
    }

    public int getByteCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->getByteCount()I");
    }

    public Color getColor(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->getColor(II)Landroid/graphics/Color;");
    }

    public ColorSpace getColorSpace() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->getColorSpace()Landroid/graphics/ColorSpace;");
    }

    public Bitmap.Config getConfig() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->getConfig()Landroid/graphics/Bitmap$Config;");
    }

    public int getDensity() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->getDensity()I");
    }

    public Gainmap getGainmap() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->getGainmap()Landroid/graphics/Gainmap;");
    }

    public int getGenerationId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->getGenerationId()I");
    }

    public HardwareBuffer getHardwareBuffer() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->getHardwareBuffer()Landroid/hardware/HardwareBuffer;");
    }

    public int getHeight() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->getHeight()I");
    }

    public byte[] getNinePatchChunk() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->getNinePatchChunk()[B");
    }

    public int getPixel(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->getPixel(II)I");
    }

    public void getPixels(int[] arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->getPixels([IIIIIII)V");
    }

    public int getRowBytes() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->getRowBytes()I");
    }

    public int getScaledHeight(Canvas arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->getScaledHeight(Landroid/graphics/Canvas;)I");
    }

    public int getScaledHeight(DisplayMetrics arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->getScaledHeight(Landroid/util/DisplayMetrics;)I");
    }

    public int getScaledHeight(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->getScaledHeight(I)I");
    }

    public int getScaledWidth(Canvas arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->getScaledWidth(Landroid/graphics/Canvas;)I");
    }

    public int getScaledWidth(DisplayMetrics arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->getScaledWidth(Landroid/util/DisplayMetrics;)I");
    }

    public int getScaledWidth(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->getScaledWidth(I)I");
    }

    public int getWidth() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->getWidth()I");
    }

    public boolean hasAlpha() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->hasAlpha()Z");
    }

    public boolean hasGainmap() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->hasGainmap()Z");
    }

    public boolean hasMipMap() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->hasMipMap()Z");
    }

    public boolean isMutable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->isMutable()Z");
    }

    public boolean isPremultiplied() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->isPremultiplied()Z");
    }

    public boolean isRecycled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->isRecycled()Z");
    }

    public void prepareToDraw() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->prepareToDraw()V");
    }

    public void reconfigure(int arg0, int arg1, Bitmap.Config arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->reconfigure(IILandroid/graphics/Bitmap$Config;)V");
    }

    public void recycle() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->recycle()V");
    }

    public boolean sameAs(Bitmap arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->sameAs(Landroid/graphics/Bitmap;)Z");
    }

    public void setColorSpace(ColorSpace arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->setColorSpace(Landroid/graphics/ColorSpace;)V");
    }

    public void setConfig(Bitmap.Config arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->setConfig(Landroid/graphics/Bitmap$Config;)V");
    }

    public void setDensity(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->setDensity(I)V");
    }

    public void setGainmap(Gainmap arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->setGainmap(Landroid/graphics/Gainmap;)V");
    }

    public void setHasAlpha(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->setHasAlpha(Z)V");
    }

    public void setHasMipMap(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->setHasMipMap(Z)V");
    }

    public void setHeight(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->setHeight(I)V");
    }

    public void setPixel(int arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->setPixel(III)V");
    }

    public void setPixels(int[] arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->setPixels([IIIIIII)V");
    }

    public void setPremultiplied(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->setPremultiplied(Z)V");
    }

    public void setWidth(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->setWidth(I)V");
    }

    public static Bitmap wrapHardwareBuffer(HardwareBuffer arg0, ColorSpace arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->wrapHardwareBuffer(Landroid/hardware/HardwareBuffer;Landroid/graphics/ColorSpace;)Landroid/graphics/Bitmap;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Bitmap;->writeToParcel(Landroid/os/Parcel;I)V");
    }
        public static enum CompressFormat {
            JPEG, PNG, WEBP, WEBP_LOSSLESS, WEBP_LOSSY;
        }
        public static enum Config {
            ALPHA_8, ARGB_4444, ARGB_8888, HARDWARE, RGBA_1010102, RGBA_F16, RGB_565;
        }
    }
