package android.hardware;

    public class LutProperties {

    public static final int ONE_DIMENSION = 1;
    public static final int SAMPLING_KEY_CIE_Y = 2;
    public static final int SAMPLING_KEY_MAX_RGB = 1;
    public static final int SAMPLING_KEY_RGB = 0;
    public static final int THREE_DIMENSION = 3;

    public int getDimension() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/LutProperties;->getDimension()I");
    }

    public int[] getSamplingKeys() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/LutProperties;->getSamplingKeys()[I");
    }

    public int getSize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/LutProperties;->getSize()I");
    }
    }
