package android.view;

import android.graphics.ColorSpace;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.OverlayProperties;
import android.hardware.display.DeviceProductInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import tartine.fw.DisplayConfig;

    /**
     * Tartine: desktop twin of AOSP {@code android.view.Display}. The device
     * class proxies a logical display through DisplayManagerGlobal; the shell
     * has exactly one window-backed display, so its metrics come straight
     * from {@link DisplayConfig} (the same seam {@code Resources} uses for
     * {@code DisplayMetrics}). A resize updates the viewport live.
     */
    public class Display {

    public static final int DEFAULT_DISPLAY = 0;
    public static final int FLAG_PRESENTATION = 8;
    public static final int FLAG_PRIVATE = 4;
    public static final int FLAG_ROUND = 16;
    public static final int FLAG_SECURE = 2;
    public static final int FLAG_SUPPORTS_PROTECTED_BUFFERS = 1;
    public static final int FRAME_RATE_CATEGORY_HIGH = 1;
    public static final int FRAME_RATE_CATEGORY_NORMAL = 0;
    public static final int INVALID_DISPLAY = -1;
    public static final int STATE_DOZE = 3;
    public static final int STATE_DOZE_SUSPEND = 4;
    public static final int STATE_OFF = 1;
    public static final int STATE_ON = 2;
    public static final int STATE_ON_SUSPEND = 6;
    public static final int STATE_UNKNOWN = 0;
    public static final int STATE_VR = 5;

    /** Built by {@link ViewRootImpl}; the guest never constructs a display. */
    Display() {
    }

    public long getAppVsyncOffsetNanos() {
        // The desktop loop paces at DisplayConfig.refreshHz(); unlike
        // SurfaceFlinger there is no phase offset to report.
        return 0;
    }

    public void getCurrentSizeRange(Point arg0, Point arg1) {
        arg0.set(DisplayConfig.widthPixels(), DisplayConfig.heightPixels());
        arg1.set(DisplayConfig.widthPixels(), DisplayConfig.heightPixels());
    }

    /** No cutout: the desktop window is a plain rectangle. */
    public DisplayCutout getCutout() {
        return null;
    }

    public DeviceProductInfo getDeviceProductInfo() {
        return null;
    }

    public int getDisplayId() {
        return DEFAULT_DISPLAY;
    }

    public int getFlags() {
        return 0;
    }

    public Display.HdrCapabilities getHdrCapabilities() {
        return new Display.HdrCapabilities();
    }

    public float getHdrSdrRatio() {
        return 1.0f;
    }

    public int getHeight() {
        return DisplayConfig.heightPixels();
    }

    public float getHighestHdrSdrRatio() {
        return 1.0f;
    }

    public void getMetrics(DisplayMetrics arg0) {
        arg0.widthPixels = DisplayConfig.widthPixels();
        arg0.heightPixels = DisplayConfig.heightPixels();
        arg0.density = DisplayConfig.density();
        arg0.densityDpi = DisplayConfig.densityDpi();
        arg0.scaledDensity = DisplayConfig.density();
        arg0.xdpi = DisplayConfig.densityDpi();
        arg0.ydpi = DisplayConfig.densityDpi();
    }

    public Display.Mode getMode() {
        return new Display.Mode(0, DisplayConfig.widthPixels(),
                DisplayConfig.heightPixels(), DisplayConfig.refreshHz());
    }

    public String getName() {
        return "Tartine Display";
    }

    public int getOrientation() {
        // AOSP Display.getOrientation is deprecated in favour of
        // Display.getRotation; the shell is always portrait.
        return android.content.res.Configuration.ORIENTATION_PORTRAIT;
    }

    public OverlayProperties getOverlaySupport() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getOverlaySupport()Landroid/hardware/OverlayProperties;");
    }

    public int getPixelFormat() {
        return PixelFormat.RGBA_8888;
    }

    public ColorSpace getPreferredWideGamutColorSpace() {
        return null;
    }

    public long getPresentationDeadlineNanos() {
        // Present at VSync: the deadline is the frame period itself.
        return (long) (1_000_000_000.0 / DisplayConfig.refreshHz());
    }

    public void getRealMetrics(DisplayMetrics arg0) {
        getMetrics(arg0);
    }

    public void getRealSize(Point arg0) {
        arg0.set(DisplayConfig.widthPixels(), DisplayConfig.heightPixels());
    }

    public void getRectSize(Rect arg0) {
        arg0.set(0, 0, DisplayConfig.widthPixels(),
                DisplayConfig.heightPixels());
    }

    public float getRefreshRate() {
        return DisplayConfig.refreshHz();
    }

    public int getRotation() {
        return Surface.ROTATION_0;
    }

    /** No rounded corners on a desktop window. */
    public RoundedCorner getRoundedCorner(int arg0) {
        return null;
    }

    public DisplayShape getShape() {
        return null;
    }

    public void getSize(Point arg0) {
        arg0.set(DisplayConfig.widthPixels(), DisplayConfig.heightPixels());
    }

    public int getState() {
        return STATE_ON;
    }

    public float getSuggestedFrameRate(int arg0) {
        return DisplayConfig.refreshHz();
    }

    public Display.Mode[] getSupportedModes() {
        return new Display.Mode[] {getMode()};
    }

    public float[] getSupportedRefreshRates() {
        return new float[] {DisplayConfig.refreshHz()};
    }

    public int getWidth() {
        return DisplayConfig.widthPixels();
    }

    public boolean hasArrSupport() {
        return false;
    }

    public boolean isHdr() {
        return false;
    }

    public boolean isHdrSdrRatioAvailable() {
        return false;
    }

    public boolean isMinimalPostProcessingSupported() {
        return false;
    }

    public boolean isValid() {
        return true;
    }

    public boolean isWideColorGamut() {
        return false;
    }

    public void registerHdrSdrRatioChangedListener(Executor arg0,
            Consumer<Display> arg1) {
        // No HDR surface, so the ratio never changes.
    }

    public String toString() {
        return "Display " + getDisplayId() + ": " + getWidth() + "x"
                + getHeight() + " @ " + getRefreshRate() + " Hz";
    }

    public void unregisterHdrSdrRatioChangedListener(Consumer<Display> arg0) {
    }
        public static class HdrCapabilities implements Parcelable {

        public static Parcelable.Creator CREATOR;
        public static final int HDR_TYPE_DOLBY_VISION = 1;
        public static final int HDR_TYPE_HDR10 = 2;
        public static final int HDR_TYPE_HDR10_PLUS = 4;
        public static final int HDR_TYPE_HLG = 3;
        public static final int HDR_TYPE_INVALID = -1;
        public static final float INVALID_LUMINANCE = -1.0f;

        public int describeContents() {
            return 0;
        }

        public boolean equals(Object arg0) {
            return arg0 instanceof HdrCapabilities;
        }

        public float getDesiredMaxAverageLuminance() {
            return INVALID_LUMINANCE;
        }

        public float getDesiredMaxLuminance() {
            return INVALID_LUMINANCE;
        }

        public float getDesiredMinLuminance() {
            return INVALID_LUMINANCE;
        }

        /** No HDR types: the desktop presents SDR only. */
        public int[] getSupportedHdrTypes() {
            return new int[0];
        }

        public int hashCode() {
            return 0;
        }

        public String toString() {
            return "HdrCapabilities{}";
        }

        public void writeToParcel(Parcel arg0, int arg1) {
        }
        }
        public static class Mode implements Parcelable {

        public static Parcelable.Creator CREATOR;

        private final int mModeId;
        private final int mWidth;
        private final int mHeight;
        private final float mRefreshRate;

        /** AOSP Display.Mode(long modeId, ...): shell ids are ints. */
        Mode(int modeId, int width, int height, float refreshRate) {
            mModeId = modeId;
            mWidth = width;
            mHeight = height;
            mRefreshRate = refreshRate;
        }

        public int describeContents() {
            return 0;
        }

        public boolean equals(Object arg0) {
            if (!(arg0 instanceof Mode other)) {
                return false;
            }
            return mModeId == other.mModeId && mWidth == other.mWidth
                    && mHeight == other.mHeight
                    && mRefreshRate == other.mRefreshRate;
        }

        public float[] getAlternativeRefreshRates() {
            return new float[0];
        }

        public int getModeId() {
            return mModeId;
        }

        public int getPhysicalHeight() {
            return mHeight;
        }

        public int getPhysicalWidth() {
            return mWidth;
        }

        public float getRefreshRate() {
            return mRefreshRate;
        }

        public int[] getSupportedHdrTypes() {
            return new int[0];
        }

        public int hashCode() {
            int result = mModeId;
            result = 31 * result + mWidth;
            result = 31 * result + mHeight;
            result = 31 * result + Float.floatToIntBits(mRefreshRate);
            return result;
        }

        public String toString() {
            return "Display.Mode{modeId=" + mModeId + ", width=" + mWidth
                    + ", height=" + mHeight + ", refreshRate="
                    + mRefreshRate + "}";
        }

        public void writeToParcel(Parcel arg0, int arg1) {
        }
        }
    }
