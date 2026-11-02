package tartine.graphics;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * One-way event handoff from the display thread to the guest. Events that
 * arrive before a listener is registered are buffered and replayed, so a
 * close that races guest startup is not lost.
 */
final class HostEventChannel {

    private final Object lock = new Object();
    private final Deque<HostDisplay.Event> pending = new ArrayDeque<>();
    private HostDisplay.EventListener listener;

    void setListener(HostDisplay.EventListener listener) {
        if (listener == null) {
            throw new NullPointerException("listener");
        }
        synchronized (lock) {
            this.listener = listener;
            while (!pending.isEmpty()) {
                listener.onEvent(pending.poll());
            }
        }
    }

    void publish(HostDisplay.Event event) {
        if (event == null) {
            throw new NullPointerException("event");
        }
        synchronized (lock) {
            if (listener == null) {
                pending.add(event);
            } else {
                listener.onEvent(event);
            }
        }
    }
}
