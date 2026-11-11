package android.view.animation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import android.os.SystemClock;
import org.junit.jupiter.api.Test;

/** Pins the animation clock the guest hover/ripple paths reach. */
class AnimationUtilsTest {

    @Test
    void currentAnimationTimeMillisTracksTheUptimeClock() {
        long before = SystemClock.uptimeMillis();
        long now = AnimationUtils.currentAnimationTimeMillis();
        long after = SystemClock.uptimeMillis();

        assertTrue(now >= before && now <= after,
                "animation time should sit between the surrounding uptimes");
    }
}