package android.hardware.lights;

    public class Light implements android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;
    public static final int LIGHT_CAPABILITY_BRIGHTNESS = 1;
    public static final int LIGHT_CAPABILITY_COLOR_RGB = 2;
    public static final int LIGHT_CAPABILITY_RGB = 0;
    public static final int LIGHT_TYPE_INPUT = 10001;
    public static final int LIGHT_TYPE_KEYBOARD_BACKLIGHT = 10003;
    public static final int LIGHT_TYPE_MICROPHONE = 8;
    public static final int LIGHT_TYPE_PLAYER_ID = 10002;

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/lights/Light;->describeContents()I");
    }

    public boolean equals(java.lang.Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/lights/Light;->equals(Ljava/lang/Object;)Z");
    }

    public int getId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/lights/Light;->getId()I");
    }

    public java.lang.String getName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/lights/Light;->getName()Ljava/lang/String;");
    }

    public int getOrdinal() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/lights/Light;->getOrdinal()I");
    }

    public int getType() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/lights/Light;->getType()I");
    }

    public boolean hasBrightnessControl() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/lights/Light;->hasBrightnessControl()Z");
    }

    public boolean hasRgbControl() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/lights/Light;->hasRgbControl()Z");
    }

    public int hashCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/lights/Light;->hashCode()I");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/lights/Light;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/lights/Light;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
