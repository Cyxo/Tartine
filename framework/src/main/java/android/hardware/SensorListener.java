package android.hardware;

    public interface SensorListener {

    public abstract void onAccuracyChanged(int arg0, int arg1);

    public abstract void onSensorChanged(int arg0, float[] arg1);
    }
