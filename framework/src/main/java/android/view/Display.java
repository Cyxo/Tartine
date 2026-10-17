package android.view;

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

    public long getAppVsyncOffsetNanos() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getAppVsyncOffsetNanos()J");
    }

    public void getCurrentSizeRange(android.graphics.Point arg0, android.graphics.Point arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getCurrentSizeRange(Landroid/graphics/Point;Landroid/graphics/Point;)V");
    }

    public android.view.DisplayCutout getCutout() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getCutout()Landroid/view/DisplayCutout;");
    }

    public android.hardware.display.DeviceProductInfo getDeviceProductInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getDeviceProductInfo()Landroid/hardware/display/DeviceProductInfo;");
    }

    public int getDisplayId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getDisplayId()I");
    }

    public int getFlags() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getFlags()I");
    }

    public android.view.Display.HdrCapabilities getHdrCapabilities() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getHdrCapabilities()Landroid/view/Display$HdrCapabilities;");
    }

    public float getHdrSdrRatio() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getHdrSdrRatio()F");
    }

    public int getHeight() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getHeight()I");
    }

    public float getHighestHdrSdrRatio() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getHighestHdrSdrRatio()F");
    }

    public void getMetrics(android.util.DisplayMetrics arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getMetrics(Landroid/util/DisplayMetrics;)V");
    }

    public android.view.Display.Mode getMode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getMode()Landroid/view/Display$Mode;");
    }

    public java.lang.String getName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getName()Ljava/lang/String;");
    }

    public int getOrientation() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getOrientation()I");
    }

    public android.hardware.OverlayProperties getOverlaySupport() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getOverlaySupport()Landroid/hardware/OverlayProperties;");
    }

    public int getPixelFormat() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getPixelFormat()I");
    }

    public android.graphics.ColorSpace getPreferredWideGamutColorSpace() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getPreferredWideGamutColorSpace()Landroid/graphics/ColorSpace;");
    }

    public long getPresentationDeadlineNanos() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getPresentationDeadlineNanos()J");
    }

    public void getRealMetrics(android.util.DisplayMetrics arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getRealMetrics(Landroid/util/DisplayMetrics;)V");
    }

    public void getRealSize(android.graphics.Point arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getRealSize(Landroid/graphics/Point;)V");
    }

    public void getRectSize(android.graphics.Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getRectSize(Landroid/graphics/Rect;)V");
    }

    public float getRefreshRate() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getRefreshRate()F");
    }

    public int getRotation() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getRotation()I");
    }

    public android.view.RoundedCorner getRoundedCorner(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getRoundedCorner(I)Landroid/view/RoundedCorner;");
    }

    public android.view.DisplayShape getShape() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getShape()Landroid/view/DisplayShape;");
    }

    public void getSize(android.graphics.Point arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getSize(Landroid/graphics/Point;)V");
    }

    public int getState() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getState()I");
    }

    public float getSuggestedFrameRate(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getSuggestedFrameRate(I)F");
    }

    public android.view.Display.Mode[] getSupportedModes() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getSupportedModes()[Landroid/view/Display$Mode;");
    }

    public float[] getSupportedRefreshRates() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getSupportedRefreshRates()[F");
    }

    public int getWidth() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->getWidth()I");
    }

    public boolean hasArrSupport() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->hasArrSupport()Z");
    }

    public boolean isHdr() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->isHdr()Z");
    }

    public boolean isHdrSdrRatioAvailable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->isHdrSdrRatioAvailable()Z");
    }

    public boolean isMinimalPostProcessingSupported() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->isMinimalPostProcessingSupported()Z");
    }

    public boolean isValid() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->isValid()Z");
    }

    public boolean isWideColorGamut() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->isWideColorGamut()Z");
    }

    public void registerHdrSdrRatioChangedListener(java.util.concurrent.Executor arg0, java.util.function.Consumer<android.view.Display> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->registerHdrSdrRatioChangedListener(Ljava/util/concurrent/Executor;Ljava/util/function/Consumer;)V");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->toString()Ljava/lang/String;");
    }

    public void unregisterHdrSdrRatioChangedListener(java.util.function.Consumer<android.view.Display> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display;->unregisterHdrSdrRatioChangedListener(Ljava/util/function/Consumer;)V");
    }
        public static class HdrCapabilities implements android.os.Parcelable {

        public static android.os.Parcelable.Creator CREATOR;
        public static final int HDR_TYPE_DOLBY_VISION = 1;
        public static final int HDR_TYPE_HDR10 = 2;
        public static final int HDR_TYPE_HDR10_PLUS = 4;
        public static final int HDR_TYPE_HLG = 3;
        public static final int HDR_TYPE_INVALID = -1;
        public static final float INVALID_LUMINANCE = -1.0f;

        public int describeContents() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display$HdrCapabilities;->describeContents()I");
        }

        public boolean equals(java.lang.Object arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display$HdrCapabilities;->equals(Ljava/lang/Object;)Z");
        }

        public float getDesiredMaxAverageLuminance() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display$HdrCapabilities;->getDesiredMaxAverageLuminance()F");
        }

        public float getDesiredMaxLuminance() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display$HdrCapabilities;->getDesiredMaxLuminance()F");
        }

        public float getDesiredMinLuminance() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display$HdrCapabilities;->getDesiredMinLuminance()F");
        }

        public int[] getSupportedHdrTypes() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display$HdrCapabilities;->getSupportedHdrTypes()[I");
        }

        public int hashCode() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display$HdrCapabilities;->hashCode()I");
        }

        public java.lang.String toString() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display$HdrCapabilities;->toString()Ljava/lang/String;");
        }

        public void writeToParcel(android.os.Parcel arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display$HdrCapabilities;->writeToParcel(Landroid/os/Parcel;I)V");
        }
        }
        public static class Mode implements android.os.Parcelable {

        public static android.os.Parcelable.Creator CREATOR;

        public int describeContents() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display$Mode;->describeContents()I");
        }

        public boolean equals(java.lang.Object arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display$Mode;->equals(Ljava/lang/Object;)Z");
        }

        public float[] getAlternativeRefreshRates() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display$Mode;->getAlternativeRefreshRates()[F");
        }

        public int getModeId() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display$Mode;->getModeId()I");
        }

        public int getPhysicalHeight() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display$Mode;->getPhysicalHeight()I");
        }

        public int getPhysicalWidth() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display$Mode;->getPhysicalWidth()I");
        }

        public float getRefreshRate() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display$Mode;->getRefreshRate()F");
        }

        public int[] getSupportedHdrTypes() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display$Mode;->getSupportedHdrTypes()[I");
        }

        public int hashCode() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display$Mode;->hashCode()I");
        }

        public java.lang.String toString() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display$Mode;->toString()Ljava/lang/String;");
        }

        public void writeToParcel(android.os.Parcel arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Display$Mode;->writeToParcel(Landroid/os/Parcel;I)V");
        }
        }
    }
