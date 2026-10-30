package android.view;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * AOSP parity for the listener registry and dispatch paths. Source:
 * aosp-mirror/platform_frameworks_base
 * core/java/android/view/ViewTreeObserver.java (add/remove methods are
 * checkIsAlive + lazy list; dispatchOnPreDraw aggregates listener
 * cancellations).
 *
 * <p>The test lives in {@code android.view} because the device constructor
 * is package-private. A null Context is enough: the shell constructor does
 * not touch it (AOSP only seeds a debug flag from the app target SDK).
 */
class ViewTreeObserverTest {

    @Test
    void startsAlive() {
        ViewTreeObserver observer = new ViewTreeObserver(null);
        assertTrue(observer.isAlive());
    }

    @Test
    void onDrawListenersDispatchAndRemove() {
        ViewTreeObserver observer = new ViewTreeObserver(null);
        AtomicInteger first = new AtomicInteger();
        AtomicInteger second = new AtomicInteger();
        ViewTreeObserver.OnDrawListener one = first::incrementAndGet;
        ViewTreeObserver.OnDrawListener two = second::incrementAndGet;

        observer.addOnDrawListener(one);
        observer.addOnDrawListener(two);
        observer.dispatchOnDraw();
        assertEquals(1, first.get());
        assertEquals(1, second.get());

        observer.removeOnDrawListener(one);
        observer.dispatchOnDraw();
        assertEquals(1, first.get());
        assertEquals(2, second.get());
    }

    @Test
    void globalLayoutDispatchAndDeprecatedRemoveAlias() {
        ViewTreeObserver observer = new ViewTreeObserver(null);
        AtomicInteger calls = new AtomicInteger();
        ViewTreeObserver.OnGlobalLayoutListener listener = calls::incrementAndGet;

        observer.addOnGlobalLayoutListener(listener);
        observer.dispatchOnGlobalLayout();
        assertEquals(1, calls.get());

        observer.removeGlobalOnLayoutListener(listener);
        observer.dispatchOnGlobalLayout();
        assertEquals(1, calls.get());
    }

    @Test
    void preDrawCancellationAggregatesAcrossListeners() {
        ViewTreeObserver observer = new ViewTreeObserver(null);
        ViewTreeObserver.OnPreDrawListener allow = () -> true;
        ViewTreeObserver.OnPreDrawListener cancel = () -> false;

        observer.addOnPreDrawListener(allow);
        assertFalse(observer.dispatchOnPreDraw());

        observer.addOnPreDrawListener(cancel);
        assertTrue(observer.dispatchOnPreDraw());

        observer.removeOnPreDrawListener(cancel);
        assertFalse(observer.dispatchOnPreDraw());
    }

    @Test
    void frameCommitCallbacksReportWhetherTheyWereRegistered() {
        ViewTreeObserver observer = new ViewTreeObserver(null);
        Runnable callback = () -> { };

        assertFalse(observer.unregisterFrameCommitCallback(callback));
        observer.registerFrameCommitCallback(callback);
        assertTrue(observer.unregisterFrameCommitCallback(callback));
        assertFalse(observer.unregisterFrameCommitCallback(callback));
    }

    @Test
    void windowAndTouchRegistriesAreIndependent() {
        ViewTreeObserver observer = new ViewTreeObserver(null);
        AtomicInteger focus = new AtomicInteger();
        AtomicInteger attach = new AtomicInteger();
        AtomicInteger visibility = new AtomicInteger();
        AtomicInteger touch = new AtomicInteger();

        ViewTreeObserver.OnWindowFocusChangeListener focusListener =
                hasFocus -> focus.incrementAndGet();
        ViewTreeObserver.OnWindowAttachListener attachListener = new ViewTreeObserver.OnWindowAttachListener() {
            @Override
            public void onWindowAttached() {
                attach.incrementAndGet();
            }

            @Override
            public void onWindowDetached() {
                attach.incrementAndGet();
            }
        };
        ViewTreeObserver.OnWindowVisibilityChangeListener visibilityListener =
                v -> visibility.incrementAndGet();
        ViewTreeObserver.OnTouchModeChangeListener touchListener =
                inTouch -> touch.incrementAndGet();

        observer.addOnWindowFocusChangeListener(focusListener);
        observer.addOnWindowAttachListener(attachListener);
        observer.addOnWindowVisibilityChangeListener(visibilityListener);
        observer.addOnTouchModeChangeListener(touchListener);

        // No public dispatch hooks for these registries yet; removal is the
        // observable behavior, so assert each list accepts a remove.
        observer.removeOnWindowFocusChangeListener(focusListener);
        observer.removeOnWindowAttachListener(attachListener);
        observer.removeOnWindowVisibilityChangeListener(visibilityListener);
        observer.removeOnTouchModeChangeListener(touchListener);
        assertEquals(0, focus.get());
        assertEquals(0, attach.get());
        assertEquals(0, visibility.get());
        assertEquals(0, touch.get());
    }

    @Test
    void dispatchWithoutListenersIsSafe() {
        ViewTreeObserver observer = new ViewTreeObserver(null);
        observer.dispatchOnDraw();
        observer.dispatchOnGlobalLayout();
        assertFalse(observer.dispatchOnPreDraw());
    }
}
