package tartine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewRootImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tartine.graphics.HostDisplay;

/** Pins the host-side hover session edges translated by {@link HostInput}. */
class HostInputTest {

    /** Attaching a tree needs the process main looper, as on a device. */
    @BeforeAll
    static void prepareMainLooper() {
        if (Looper.getMainLooper() == null) {
            Looper.prepareMainLooper();
        }
    }

    @Test
    void cursorLeaveEndsTheHoverSessionOnce() {
        HoverView view = new HoverView();
        HostInput input = new HostInput(rootFor(view), 1.0f);

        input.handle(new HostDisplay.CursorMoved(10, 20));
        input.handle(new HostDisplay.CursorEntered(false));
        input.handle(new HostDisplay.CursorEntered(false));

        assertEquals(List.of(MotionEvent.ACTION_HOVER_ENTER,
                MotionEvent.ACTION_HOVER_EXIT), view.actions);
    }

    @Test
    void secondEntryStartsNewHoverSession() {
        HoverView view = new HoverView();
        HostInput input = new HostInput(rootFor(view), 1.0f);

        input.handle(new HostDisplay.CursorMoved(10, 20));
        input.handle(new HostDisplay.CursorEntered(false));
        input.handle(new HostDisplay.CursorEntered(true));
        input.handle(new HostDisplay.CursorMoved(11, 21));

        assertEquals(List.of(MotionEvent.ACTION_HOVER_ENTER,
                MotionEvent.ACTION_HOVER_EXIT,
                MotionEvent.ACTION_HOVER_ENTER), view.actions);
    }

    @Test
    void losingWindowFocusEndsTheHoverSession() {
        HoverView view = new HoverView();
        HostInput input = new HostInput(rootFor(view), 1.0f);

        input.handle(new HostDisplay.CursorMoved(10, 20));
        input.handle(new HostDisplay.FocusChanged(false));

        assertEquals(List.of(MotionEvent.ACTION_HOVER_ENTER,
                MotionEvent.ACTION_HOVER_EXIT), view.actions);
    }

    @Test
    void leaveDuringButtonPressKeepsTheTouchStreamOnly() {
        HoverView view = new HoverView();
        HostInput input = new HostInput(rootFor(view), 1.0f);

        input.handle(new HostDisplay.CursorMoved(10, 20));
        input.handle(new HostDisplay.Button(0, HostDisplay.Action.PRESS, 0));
        input.handle(new HostDisplay.CursorEntered(false));
        input.handle(new HostDisplay.Button(0, HostDisplay.Action.RELEASE, 0));
        input.handle(new HostDisplay.CursorEntered(true));
        input.handle(new HostDisplay.CursorMoved(11, 21));

        assertEquals(List.of(MotionEvent.ACTION_HOVER_ENTER,
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP,
                MotionEvent.ACTION_HOVER_ENTER), view.actions);
    }

    private static ViewRootImpl rootFor(View view) {
        ViewRootImpl root = new ViewRootImpl(null);
        root.setView(view);
        return root;
    }

    /** Records the actions that reach the view through the dispatch chain. */
    private static final class HoverView extends View {
        final List<Integer> actions = new ArrayList<>();

        HoverView() {
            super(null);
        }

        @Override
        public boolean onHoverEvent(MotionEvent event) {
            actions.add(event.getAction());
            return true;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            actions.add(event.getAction());
            return true;
        }
    }
}
