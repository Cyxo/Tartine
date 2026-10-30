package android.view;

import android.hardware.BatteryState;
import android.hardware.SensorManager;
import android.hardware.lights.LightsManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Vibrator;
import android.os.VibratorManager;
import java.util.List;

    public class InputDevice implements Parcelable {

    public static Parcelable.Creator CREATOR;
    public static final int KEYBOARD_TYPE_ALPHABETIC = 2;
    public static final int KEYBOARD_TYPE_NONE = 0;
    public static final int KEYBOARD_TYPE_NON_ALPHABETIC = 1;
    public static final int MOTION_RANGE_ORIENTATION = 8;
    public static final int MOTION_RANGE_PRESSURE = 2;
    public static final int MOTION_RANGE_SIZE = 3;
    public static final int MOTION_RANGE_TOOL_MAJOR = 6;
    public static final int MOTION_RANGE_TOOL_MINOR = 7;
    public static final int MOTION_RANGE_TOUCH_MAJOR = 4;
    public static final int MOTION_RANGE_TOUCH_MINOR = 5;
    public static final int MOTION_RANGE_X = 0;
    public static final int MOTION_RANGE_Y = 1;
    public static final int SOURCE_ANY = -256;
    public static final int SOURCE_BLUETOOTH_STYLUS = 49154;
    public static final int SOURCE_CLASS_BUTTON = 1;
    public static final int SOURCE_CLASS_JOYSTICK = 16;
    public static final int SOURCE_CLASS_MASK = 255;
    public static final int SOURCE_CLASS_NONE = 0;
    public static final int SOURCE_CLASS_POINTER = 2;
    public static final int SOURCE_CLASS_POSITION = 8;
    public static final int SOURCE_CLASS_TRACKBALL = 4;
    public static final int SOURCE_DPAD = 513;
    public static final int SOURCE_GAMEPAD = 1025;
    public static final int SOURCE_HDMI = 33554433;
    public static final int SOURCE_JOYSTICK = 16777232;
    public static final int SOURCE_KEYBOARD = 257;
    public static final int SOURCE_MOUSE = 8194;
    public static final int SOURCE_MOUSE_RELATIVE = 131076;
    public static final int SOURCE_ROTARY_ENCODER = 4194304;
    public static final int SOURCE_SENSOR = 67108864;
    public static final int SOURCE_STYLUS = 16386;
    public static final int SOURCE_TOUCHPAD = 1048584;
    public static final int SOURCE_TOUCHSCREEN = 4098;
    public static final int SOURCE_TOUCH_NAVIGATION = 2097152;
    public static final int SOURCE_TRACKBALL = 65540;
    public static final int SOURCE_UNKNOWN = 0;

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice;->describeContents()I");
    }

    public BatteryState getBatteryState() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice;->getBatteryState()Landroid/hardware/BatteryState;");
    }

    public int getControllerNumber() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice;->getControllerNumber()I");
    }

    public String getDescriptor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice;->getDescriptor()Ljava/lang/String;");
    }

    public static InputDevice getDevice(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice;->getDevice(I)Landroid/view/InputDevice;");
    }

    public static int[] getDeviceIds() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice;->getDeviceIds()[I");
    }

    public int getId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice;->getId()I");
    }

    public KeyCharacterMap getKeyCharacterMap() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice;->getKeyCharacterMap()Landroid/view/KeyCharacterMap;");
    }

    public int getKeyCodeForKeyLocation(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice;->getKeyCodeForKeyLocation(I)I");
    }

    public int getKeyboardType() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice;->getKeyboardType()I");
    }

    public LightsManager getLightsManager() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice;->getLightsManager()Landroid/hardware/lights/LightsManager;");
    }

    public InputDevice.MotionRange getMotionRange(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice;->getMotionRange(I)Landroid/view/InputDevice$MotionRange;");
    }

    public InputDevice.MotionRange getMotionRange(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice;->getMotionRange(II)Landroid/view/InputDevice$MotionRange;");
    }

    public List<InputDevice.MotionRange> getMotionRanges() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice;->getMotionRanges()Ljava/util/List;");
    }

    public String getName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice;->getName()Ljava/lang/String;");
    }

    public int getProductId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice;->getProductId()I");
    }

    public SensorManager getSensorManager() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice;->getSensorManager()Landroid/hardware/SensorManager;");
    }

    public int getSources() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice;->getSources()I");
    }

    public int getVendorId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice;->getVendorId()I");
    }

    public Vibrator getVibrator() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice;->getVibrator()Landroid/os/Vibrator;");
    }

    public VibratorManager getVibratorManager() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice;->getVibratorManager()Landroid/os/VibratorManager;");
    }

    public boolean[] hasKeys(int[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice;->hasKeys([I)[Z");
    }

    public boolean hasMicrophone() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice;->hasMicrophone()Z");
    }

    public boolean isEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice;->isEnabled()Z");
    }

    public boolean isExternal() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice;->isExternal()Z");
    }

    public boolean isVirtual() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice;->isVirtual()Z");
    }

    public boolean supportsSource(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice;->supportsSource(I)Z");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice;->writeToParcel(Landroid/os/Parcel;I)V");
    }
        public static class MotionRange {

        public int getAxis() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice$MotionRange;->getAxis()I");
        }

        public float getFlat() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice$MotionRange;->getFlat()F");
        }

        public float getFuzz() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice$MotionRange;->getFuzz()F");
        }

        public float getMax() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice$MotionRange;->getMax()F");
        }

        public float getMin() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice$MotionRange;->getMin()F");
        }

        public float getRange() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice$MotionRange;->getRange()F");
        }

        public float getResolution() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice$MotionRange;->getResolution()F");
        }

        public int getSource() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice$MotionRange;->getSource()I");
        }

        public boolean isFromSource(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/InputDevice$MotionRange;->isFromSource(I)Z");
        }
        }
    }
