package android.hardware.lights;

    public abstract class LightsManager {

    public abstract android.hardware.lights.LightState getLightState(android.hardware.lights.Light arg0);

    public abstract java.util.List<android.hardware.lights.Light> getLights();

    public abstract android.hardware.lights.LightsManager.LightsSession openSession();
        public static abstract class LightsSession implements java.lang.AutoCloseable {

        public abstract void close();

        public abstract void requestLights(android.hardware.lights.LightsRequest arg0);
        }
    }
