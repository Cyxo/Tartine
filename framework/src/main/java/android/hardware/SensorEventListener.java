package android.hardware;

    public interface SensorEventListener {

    public abstract void onAccuracyChanged(Sensor arg0, int arg1);

    public abstract void onSensorChanged(SensorEvent arg0);
    }
