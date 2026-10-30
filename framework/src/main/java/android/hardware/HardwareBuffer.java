package android.hardware;

import android.os.Parcel;
import android.os.Parcelable;

    public class HardwareBuffer implements AutoCloseable, Parcelable {

    public static final int BLOB = 33;
    public static Parcelable.Creator CREATOR;
    public static final int DS_24UI8 = 50;
    public static final int DS_FP32UI8 = 52;
    public static final int D_16 = 48;
    public static final int D_24 = 49;
    public static final int D_FP32 = 51;
    public static final int RGBA_10101010 = 59;
    public static final int RGBA_1010102 = 43;
    public static final int RGBA_8888 = 1;
    public static final int RGBA_FP16 = 22;
    public static final int RGBX_8888 = 2;
    public static final int RGB_565 = 4;
    public static final int RGB_888 = 3;
    public static final int RG_1616 = 58;
    public static final int R_16 = 57;
    public static final int R_8 = 56;
    public static final int S_UI8 = 53;
    public static final long USAGE_COMPOSER_OVERLAY = 2048L;
    public static final long USAGE_CPU_READ_OFTEN = 3L;
    public static final long USAGE_CPU_READ_RARELY = 2L;
    public static final long USAGE_CPU_WRITE_OFTEN = 48L;
    public static final long USAGE_CPU_WRITE_RARELY = 32L;
    public static final long USAGE_FRONT_BUFFER = 4294967296L;
    public static final long USAGE_GPU_COLOR_OUTPUT = 512L;
    public static final long USAGE_GPU_CUBE_MAP = 33554432L;
    public static final long USAGE_GPU_DATA_BUFFER = 16777216L;
    public static final long USAGE_GPU_MIPMAP_COMPLETE = 67108864L;
    public static final long USAGE_GPU_SAMPLED_IMAGE = 256L;
    public static final long USAGE_PROTECTED_CONTENT = 16384L;
    public static final long USAGE_SENSOR_DIRECT_DATA = 8388608L;
    public static final long USAGE_VIDEO_ENCODE = 65536L;
    public static final int YCBCR_420_888 = 35;
    public static final int YCBCR_P010 = 54;
    public static final int YCBCR_P210 = 60;

    public void close() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/HardwareBuffer;->close()V");
    }

    public static HardwareBuffer create(int arg0, int arg1, int arg2, int arg3, long arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/HardwareBuffer;->create(IIIIJ)Landroid/hardware/HardwareBuffer;");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/HardwareBuffer;->describeContents()I");
    }

    protected void finalize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/HardwareBuffer;->finalize()V");
    }

    public int getFormat() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/HardwareBuffer;->getFormat()I");
    }

    public int getHeight() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/HardwareBuffer;->getHeight()I");
    }

    public long getId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/HardwareBuffer;->getId()J");
    }

    public int getLayers() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/HardwareBuffer;->getLayers()I");
    }

    public long getUsage() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/HardwareBuffer;->getUsage()J");
    }

    public int getWidth() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/HardwareBuffer;->getWidth()I");
    }

    public boolean isClosed() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/HardwareBuffer;->isClosed()Z");
    }

    public static boolean isSupported(int arg0, int arg1, int arg2, int arg3, long arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/HardwareBuffer;->isSupported(IIIIJ)Z");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/HardwareBuffer;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
