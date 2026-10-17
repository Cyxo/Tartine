package android.hardware;

    public interface SensorEventListener {

    public abstract void onAccuracyChanged(android.hardware.Sensor arg0, int arg1);

    public abstract void onSensorChanged(android.hardware.SensorEvent arg0);
    }
