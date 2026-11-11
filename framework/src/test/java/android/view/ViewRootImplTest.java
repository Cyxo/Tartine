package android.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import android.os.Looper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Window-root pins: the attach/detach handshake, focus fan-out and the input
 * entry points that the desktop host drives each frame (ADR-0014).
 */
class ViewRootImplTest {

    /**
     * The window root owns a main-thread handler, as on a device. The test
     * JVM starts without the guest looper {@code Main} prepares, so prepare
     * the process main looper once for this class.
     */
    @BeforeAll
    static void prepareMainLooper() {
        if (Looper.getMainLooper() == null) {
            Looper.prepareMainLooper();
        }
    }

    /** Counts the tree-side callbacks a real attach must fire. */
    private static final class RecordingView extends View {
        int attached;
        int detached;
        int focusChanges;
        boolean lastFocus;
        int downs;
        int keys;

        RecordingView() {
            super(null);
        }

        @Override
        protected void onAttachedToWindow() {
            attached++;
        }

        @Override
        protected void onDetachedFromWindow() {
            detached++;
        }

        @Override
        public void onWindowFocusChanged(boolean hasFocus) {
            focusChanges++;
            lastFocus = hasFocus;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                downs++;
                return true;
            }
            return true;
        }

        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            keys++;
            return true;
        }

        @Override
        protected void onMeasure(int widthMeasureSpec,
                int heightMeasureSpec) {
            setMeasuredDimension(
                    MeasureSpec.getSize(widthMeasureSpec),
                    MeasureSpec.getSize(heightMeasureSpec));
        }
    }

    @Test
    void setViewAttachesTheWholeTreeAndDieDetachesIt() {
        ViewRootImpl root = new ViewRootImpl(null);
        DecorView decor = new DecorView(null);
        RecordingView child = new RecordingView();
        decor.addView(child);

        root.setView(decor);

        assertEquals(1, decor.getWindowAttachCount());
        assertEquals(1, child.getWindowAttachCount());
        assertSame(root, decor.getParent());
        assertTrue(child.isAttachedToWindow());
        assertTrue(root.isLayoutRequested());

        root.die(false);

        assertEquals(1, child.detached);
        assertNull(decor.getParent());
        assertFalse(decor.isAttachedToWindow());
        assertFalse(child.isAttachedToWindow());
    }

    @Test
    void windowFocusReachesTheTreeAndTheObserver() {
        ViewRootImpl root = new ViewRootImpl(null);
        DecorView decor = new DecorView(null);
        RecordingView child = new RecordingView();
        decor.addView(child);
        root.setView(decor);
        boolean[] observerFocus = new boolean[1];
        root.getViewTreeObserver().addOnWindowFocusChangeListener(
                hasFocus -> observerFocus[0] = hasFocus);

        root.windowFocusChanged(true);

        assertEquals(1, child.focusChanges);
        assertTrue(child.lastFocus);
        assertTrue(observerFocus[0]);
        assertTrue(root.hasWindowFocus());
    }

    @Test
    void touchAndKeyEnterThroughTheWindowRoot() {
        ViewRootImpl root = new ViewRootImpl(null);
        DecorView decor = new DecorView(null);
        RecordingView child = new RecordingView();
        decor.addView(child);
        root.setView(decor);
        decor.measure(View.MeasureSpec.makeMeasureSpec(100,
                View.MeasureSpec.MODE_EXACTLY),
                View.MeasureSpec.makeMeasureSpec(100,
                View.MeasureSpec.MODE_EXACTLY));
        decor.layout(0, 0, 100, 100);

        MotionEvent.PointerProperties properties =
                new MotionEvent.PointerProperties();
        properties.id = 0;
        MotionEvent.PointerCoords coords = new MotionEvent.PointerCoords();
        coords.x = 10;
        coords.y = 10;
        long now = 1_000L;
        MotionEvent down = MotionEvent.obtain(now, now,
                MotionEvent.ACTION_DOWN, 1,
                new MotionEvent.PointerProperties[] {properties},
                new MotionEvent.PointerCoords[] {coords}, 0, 0, 1.0f, 1.0f,
                0, 0, InputDevice.SOURCE_TOUCHSCREEN, 0);

        assertTrue(root.dispatchTouchEvent(down));
        assertEquals(1, child.downs);
        down.recycle();

        // Key events follow focus on device: make the child focusable, send
        // it a focus request, then dispatch through the window root.
        child.setFocusable(true);
        assertTrue(child.requestFocus());
        assertSame(child, decor.getFocusedChild());

        assertTrue(root.dispatchKeyEvent(new KeyEvent(now, now,
                KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_A, 0, 0, 0, 30, 0,
                InputDevice.SOURCE_KEYBOARD)));
        assertEquals(1, child.keys);
    }
}
