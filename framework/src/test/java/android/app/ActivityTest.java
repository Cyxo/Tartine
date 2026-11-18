package android.app;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import android.view.MenuInflater;
import org.junit.jupiter.api.Test;

/**
 * Activity-level seams the androidx appcompat layer drives during startup:
 * the options-menu inflater is lazily created and cached per activity.
 */
class ActivityTest {

    @Test
    void menuInflaterIsLazilyCreatedAndCached() {
        Activity activity = new Activity();

        MenuInflater first = activity.getMenuInflater();
        assertNotNull(first);

        assertSame(first, activity.getMenuInflater());
    }
}
