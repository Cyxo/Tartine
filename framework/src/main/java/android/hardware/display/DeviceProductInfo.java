package android.hardware.display;

import android.os.Parcel;
import android.os.Parcelable;

    public class DeviceProductInfo implements Parcelable {

    public static final int CONNECTION_TO_SINK_BUILT_IN = 1;
    public static final int CONNECTION_TO_SINK_DIRECT = 2;
    public static final int CONNECTION_TO_SINK_TRANSITIVE = 3;
    public static final int CONNECTION_TO_SINK_UNKNOWN = 0;
    public static Parcelable.Creator CREATOR;

    public DeviceProductInfo(String arg0, String arg1, String arg2, int arg3, int arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/display/DeviceProductInfo;-><init>(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;II)V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/display/DeviceProductInfo;->describeContents()I");
    }

    public boolean equals(Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/display/DeviceProductInfo;->equals(Ljava/lang/Object;)Z");
    }

    public int getConnectionToSinkType() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/display/DeviceProductInfo;->getConnectionToSinkType()I");
    }

    public int getManufactureWeek() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/display/DeviceProductInfo;->getManufactureWeek()I");
    }

    public int getManufactureYear() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/display/DeviceProductInfo;->getManufactureYear()I");
    }

    public String getManufacturerPnpId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/display/DeviceProductInfo;->getManufacturerPnpId()Ljava/lang/String;");
    }

    public int getModelYear() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/display/DeviceProductInfo;->getModelYear()I");
    }

    public String getName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/display/DeviceProductInfo;->getName()Ljava/lang/String;");
    }

    public String getProductId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/display/DeviceProductInfo;->getProductId()Ljava/lang/String;");
    }

    public int hashCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/display/DeviceProductInfo;->hashCode()I");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/display/DeviceProductInfo;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/display/DeviceProductInfo;->writeToParcel(Landroid/os/Parcel;I)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public DeviceProductInfo() {
        }
    }
