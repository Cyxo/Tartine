package android.hardware.lights;

    public class LightsManager {

    public android.hardware.lights.LightState getLightState(android.hardware.lights.Light arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/lights/LightsManager;->getLightState(Landroid/hardware/lights/Light;)Landroid/hardware/lights/LightState;");
    }

    public java.util.List<android.hardware.lights.Light> getLights() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/lights/LightsManager;->getLights()Ljava/util/List;");
    }

    public android.hardware.lights.LightsManager.LightsSession openSession() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/lights/LightsManager;->openSession()Landroid/hardware/lights/LightsManager$LightsSession;");
    }
        public static class LightsSession implements java.lang.AutoCloseable {

        public void close() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/lights/LightsManager$LightsSession;->close()V");
        }

        public void requestLights(android.hardware.lights.LightsRequest arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/lights/LightsManager$LightsSession;->requestLights(Landroid/hardware/lights/LightsRequest;)V");
        }
        }
    }
