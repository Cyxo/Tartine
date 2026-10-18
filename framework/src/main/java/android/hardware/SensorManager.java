package android.hardware;

    public class SensorManager {

    public static final int AXIS_MINUS_X = 129;
    public static final int AXIS_MINUS_Y = 130;
    public static final int AXIS_MINUS_Z = 131;
    public static final int AXIS_X = 1;
    public static final int AXIS_Y = 2;
    public static final int AXIS_Z = 3;
    public static final int DATA_X = 0;
    public static final int DATA_Y = 1;
    public static final int DATA_Z = 2;
    public static final float GRAVITY_DEATH_STAR_I = 3.5303614E-7f;
    public static final float GRAVITY_EARTH = 9.80665f;
    public static final float GRAVITY_JUPITER = 23.12f;
    public static final float GRAVITY_MARS = 3.71f;
    public static final float GRAVITY_MERCURY = 3.7f;
    public static final float GRAVITY_MOON = 1.6f;
    public static final float GRAVITY_NEPTUNE = 11.0f;
    public static final float GRAVITY_PLUTO = 0.6f;
    public static final float GRAVITY_SATURN = 8.96f;
    public static final float GRAVITY_SUN = 275.0f;
    public static final float GRAVITY_THE_ISLAND = 4.815162f;
    public static final float GRAVITY_URANUS = 8.69f;
    public static final float GRAVITY_VENUS = 8.87f;
    public static final float LIGHT_CLOUDY = 100.0f;
    public static final float LIGHT_FULLMOON = 0.25f;
    public static final float LIGHT_NO_MOON = 0.001f;
    public static final float LIGHT_OVERCAST = 10000.0f;
    public static final float LIGHT_SHADE = 20000.0f;
    public static final float LIGHT_SUNLIGHT = 110000.0f;
    public static final float LIGHT_SUNLIGHT_MAX = 120000.0f;
    public static final float LIGHT_SUNRISE = 400.0f;
    public static final float MAGNETIC_FIELD_EARTH_MAX = 60.0f;
    public static final float MAGNETIC_FIELD_EARTH_MIN = 30.0f;
    public static final float PRESSURE_STANDARD_ATMOSPHERE = 1013.25f;
    public static final int RAW_DATA_INDEX = 3;
    public static final int RAW_DATA_X = 3;
    public static final int RAW_DATA_Y = 4;
    public static final int RAW_DATA_Z = 5;
    public static final int SENSOR_ACCELEROMETER = 2;
    public static final int SENSOR_ALL = 127;
    public static final int SENSOR_DELAY_FASTEST = 0;
    public static final int SENSOR_DELAY_GAME = 1;
    public static final int SENSOR_DELAY_NORMAL = 3;
    public static final int SENSOR_DELAY_UI = 2;
    public static final int SENSOR_LIGHT = 16;
    public static final int SENSOR_MAGNETIC_FIELD = 8;
    public static final int SENSOR_MAX = 64;
    public static final int SENSOR_MIN = 1;
    public static final int SENSOR_ORIENTATION = 1;
    public static final int SENSOR_ORIENTATION_RAW = 128;
    public static final int SENSOR_PROXIMITY = 32;
    public static final int SENSOR_STATUS_ACCURACY_HIGH = 3;
    public static final int SENSOR_STATUS_ACCURACY_LOW = 1;
    public static final int SENSOR_STATUS_ACCURACY_MEDIUM = 2;
    public static final int SENSOR_STATUS_NO_CONTACT = -1;
    public static final int SENSOR_STATUS_UNRELIABLE = 0;
    public static final int SENSOR_TEMPERATURE = 4;
    public static final int SENSOR_TRICORDER = 64;
    public static final float STANDARD_GRAVITY = 9.80665f;

    public boolean cancelTriggerSensor(android.hardware.TriggerEventListener arg0, android.hardware.Sensor arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->cancelTriggerSensor(Landroid/hardware/TriggerEventListener;Landroid/hardware/Sensor;)Z");
    }

    public android.hardware.SensorDirectChannel createDirectChannel(android.hardware.HardwareBuffer arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->createDirectChannel(Landroid/hardware/HardwareBuffer;)Landroid/hardware/SensorDirectChannel;");
    }

    public android.hardware.SensorDirectChannel createDirectChannel(android.os.MemoryFile arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->createDirectChannel(Landroid/os/MemoryFile;)Landroid/hardware/SensorDirectChannel;");
    }

    public boolean flush(android.hardware.SensorEventListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->flush(Landroid/hardware/SensorEventListener;)Z");
    }

    public static float getAltitude(float arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->getAltitude(FF)F");
    }

    public static void getAngleChange(float[] arg0, float[] arg1, float[] arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->getAngleChange([F[F[F)V");
    }

    public android.hardware.Sensor getDefaultSensor(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->getDefaultSensor(I)Landroid/hardware/Sensor;");
    }

    public android.hardware.Sensor getDefaultSensor(int arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->getDefaultSensor(IZ)Landroid/hardware/Sensor;");
    }

    public java.util.List<android.hardware.Sensor> getDynamicSensorList(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->getDynamicSensorList(I)Ljava/util/List;");
    }

    public static float getInclination(float[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->getInclination([F)F");
    }

    public static float[] getOrientation(float[] arg0, float[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->getOrientation([F[F)[F");
    }

    public static void getQuaternionFromVector(float[] arg0, float[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->getQuaternionFromVector([F[F)V");
    }

    public static boolean getRotationMatrix(float[] arg0, float[] arg1, float[] arg2, float[] arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->getRotationMatrix([F[F[F[F)Z");
    }

    public static void getRotationMatrixFromVector(float[] arg0, float[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->getRotationMatrixFromVector([F[F)V");
    }

    public java.util.List<android.hardware.Sensor> getSensorList(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->getSensorList(I)Ljava/util/List;");
    }

    public int getSensors() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->getSensors()I");
    }

    public boolean isDynamicSensorDiscoverySupported() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->isDynamicSensorDiscoverySupported()Z");
    }

    public void registerDynamicSensorCallback(android.hardware.SensorManager.DynamicSensorCallback arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->registerDynamicSensorCallback(Landroid/hardware/SensorManager$DynamicSensorCallback;)V");
    }

    public void registerDynamicSensorCallback(android.hardware.SensorManager.DynamicSensorCallback arg0, android.os.Handler arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->registerDynamicSensorCallback(Landroid/hardware/SensorManager$DynamicSensorCallback;Landroid/os/Handler;)V");
    }

    public boolean registerListener(android.hardware.SensorEventListener arg0, android.hardware.Sensor arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->registerListener(Landroid/hardware/SensorEventListener;Landroid/hardware/Sensor;I)Z");
    }

    public boolean registerListener(android.hardware.SensorEventListener arg0, android.hardware.Sensor arg1, int arg2, android.os.Handler arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->registerListener(Landroid/hardware/SensorEventListener;Landroid/hardware/Sensor;ILandroid/os/Handler;)Z");
    }

    public boolean registerListener(android.hardware.SensorEventListener arg0, android.hardware.Sensor arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->registerListener(Landroid/hardware/SensorEventListener;Landroid/hardware/Sensor;II)Z");
    }

    public boolean registerListener(android.hardware.SensorEventListener arg0, android.hardware.Sensor arg1, int arg2, int arg3, android.os.Handler arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->registerListener(Landroid/hardware/SensorEventListener;Landroid/hardware/Sensor;IILandroid/os/Handler;)Z");
    }

    public boolean registerListener(android.hardware.SensorListener arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->registerListener(Landroid/hardware/SensorListener;I)Z");
    }

    public boolean registerListener(android.hardware.SensorListener arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->registerListener(Landroid/hardware/SensorListener;II)Z");
    }

    public static boolean remapCoordinateSystem(float[] arg0, int arg1, int arg2, float[] arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->remapCoordinateSystem([FII[F)Z");
    }

    public boolean requestTriggerSensor(android.hardware.TriggerEventListener arg0, android.hardware.Sensor arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->requestTriggerSensor(Landroid/hardware/TriggerEventListener;Landroid/hardware/Sensor;)Z");
    }

    public void unregisterDynamicSensorCallback(android.hardware.SensorManager.DynamicSensorCallback arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->unregisterDynamicSensorCallback(Landroid/hardware/SensorManager$DynamicSensorCallback;)V");
    }

    public void unregisterListener(android.hardware.SensorEventListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->unregisterListener(Landroid/hardware/SensorEventListener;)V");
    }

    public void unregisterListener(android.hardware.SensorEventListener arg0, android.hardware.Sensor arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->unregisterListener(Landroid/hardware/SensorEventListener;Landroid/hardware/Sensor;)V");
    }

    public void unregisterListener(android.hardware.SensorListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->unregisterListener(Landroid/hardware/SensorListener;)V");
    }

    public void unregisterListener(android.hardware.SensorListener arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager;->unregisterListener(Landroid/hardware/SensorListener;I)V");
    }
        public static class DynamicSensorCallback {

        public DynamicSensorCallback() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager$DynamicSensorCallback;-><init>()V");
        }

        public void onDynamicSensorConnected(android.hardware.Sensor arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager$DynamicSensorCallback;->onDynamicSensorConnected(Landroid/hardware/Sensor;)V");
        }

        public void onDynamicSensorDisconnected(android.hardware.Sensor arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/SensorManager$DynamicSensorCallback;->onDynamicSensorDisconnected(Landroid/hardware/Sensor;)V");
        }
        }
    }
