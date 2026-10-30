package android.hardware.lights;

import java.util.List;

    public class LightsManager {

    public LightState getLightState(Light arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/lights/LightsManager;->getLightState(Landroid/hardware/lights/Light;)Landroid/hardware/lights/LightState;");
    }

    public List<Light> getLights() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/lights/LightsManager;->getLights()Ljava/util/List;");
    }

    public LightsManager.LightsSession openSession() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/lights/LightsManager;->openSession()Landroid/hardware/lights/LightsManager$LightsSession;");
    }
        public static class LightsSession implements AutoCloseable {

        public void close() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/lights/LightsManager$LightsSession;->close()V");
        }

        public void requestLights(LightsRequest arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/hardware/lights/LightsManager$LightsSession;->requestLights(Landroid/hardware/lights/LightsRequest;)V");
        }
        }
    }
