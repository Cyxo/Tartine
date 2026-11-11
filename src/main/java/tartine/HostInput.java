package tartine;

import android.os.SystemClock;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewRootImpl;
import tartine.graphics.HostDisplay;

/**
 * Maps desktop window events to Android input dispatch. The GLFW adapter
 * publishes {@link HostDisplay.Event}s in desktop space (GLFW key codes,
 * window coordinates); this translator owns the per-window input state
 * (cursor, button mask, down time) and hands real {@link MotionEvent} /
 * {@link KeyEvent} instances to the framework {@link ViewRootImpl}, which
 * runs the ordinary view-tree dispatch chain.
 */
final class HostInput {

    private static final int GLFW_MOD_SHIFT = 1;
    private static final int GLFW_MOD_CONTROL = 2;
    private static final int GLFW_MOD_ALT = 4;
    private static final int GLFW_MOD_SUPER = 8;

    private final ViewRootImpl root;
    private final float contentScale;

    private float cursorX;
    private float cursorY;
    private long downTime;
    private int buttonState;
    private boolean hovering;

    HostInput(ViewRootImpl root, float contentScale) {
        this.root = root;
        this.contentScale = contentScale > 0 ? contentScale : 1.0f;
    }

    /** Translates one host event. Runs on the guest main-looper thread. */
    void handle(HostDisplay.Event event) {
        if (event instanceof HostDisplay.FocusChanged focus) {
            hovering = false;
            if (!focus.focused()) {
                buttonState = 0;
            }
            root.windowFocusChanged(focus.focused());
            return;
        }
        if (event instanceof HostDisplay.CursorMoved moved) {
            cursorX = (float) (moved.x() * contentScale);
            cursorY = (float) (moved.y() * contentScale);
            if (buttonState != 0) {
                dispatchTouch(MotionEvent.ACTION_MOVE);
            } else {
                dispatchHover();
            }
            return;
        }
        if (event instanceof HostDisplay.Button button) {
            int androidButton = androidButton(button.button());
            if (androidButton == 0) {
                return;
            }
            if (button.action() == HostDisplay.Action.PRESS) {
                downTime = SystemClock.uptimeMillis();
                buttonState |= androidButton;
                dispatchTouch(MotionEvent.ACTION_DOWN);
            } else if (button.action() == HostDisplay.Action.RELEASE) {
                // AOSP reports the released button in the event's button
                // state, not in the action, and the state excludes it by the
                // time ACTION_UP is dispatched.
                buttonState &= ~androidButton;
                dispatchTouch(MotionEvent.ACTION_UP);
                if (buttonState == 0) {
                    downTime = 0;
                }
            }
            return;
        }
        if (event instanceof HostDisplay.Scrolled scrolled) {
            dispatchScroll((float) scrolled.scrollX(),
                    (float) scrolled.scrollY());
            return;
        }
        if (event instanceof HostDisplay.Key key) {
            dispatchKey(key);
            return;
        }
        if (event instanceof HostDisplay.Text text) {
            // Raw typed text is a fallback channel; text editors on device
            // receive commits through InputConnection.
            long now = SystemClock.uptimeMillis();
            root.dispatchKeyEvent(new KeyEvent(now,
                    new String(Character.toChars(text.codepoint())), 0, 0));
        }
    }

    private void dispatchHover() {
        int action = hovering
                ? MotionEvent.ACTION_HOVER_MOVE
                : MotionEvent.ACTION_HOVER_ENTER;
        hovering = true;
        root.dispatchHoverEvent(motionEvent(action, 0, 0));
    }

    private void dispatchTouch(int action) {
        root.dispatchTouchEvent(motionEvent(action, buttonState, 0));
    }

    private void dispatchScroll(float scrollX, float scrollY) {
        MotionEvent.PointerProperties properties = pointerProperties();
        MotionEvent.PointerCoords coords = pointerCoords();
        coords.setAxisValue(MotionEvent.AXIS_HSCROLL, scrollX);
        coords.setAxisValue(MotionEvent.AXIS_VSCROLL, scrollY);
        long now = SystemClock.uptimeMillis();
        MotionEvent event = MotionEvent.obtain(now, now,
                MotionEvent.ACTION_SCROLL, 1,
                new MotionEvent.PointerProperties[] {properties},
                new MotionEvent.PointerCoords[] {coords}, 0, 0, 1.0f, 1.0f,
                0, 0, InputDevice.SOURCE_MOUSE, 0);
        root.dispatchGenericMotionEvent(event);
        event.recycle();
    }

    private void dispatchKey(HostDisplay.Key key) {
        int keyCode = keyCode(key.key());
        long now = SystemClock.uptimeMillis();
        if (key.action() == HostDisplay.Action.PRESS) {
            downTime = now;
        } else if (downTime == 0) {
            downTime = now;
        }
        int action = key.action() == HostDisplay.Action.RELEASE
                ? KeyEvent.ACTION_UP
                : KeyEvent.ACTION_DOWN;
        int repeat = key.action() == HostDisplay.Action.REPEAT ? 1 : 0;
        KeyEvent event = new KeyEvent(downTime, now, action, keyCode, repeat,
                metaState(key.mods()), 0, key.scancode(), 0,
                InputDevice.SOURCE_KEYBOARD);
        root.dispatchKeyEvent(event);
        if (action == KeyEvent.ACTION_UP && downTime != 0
                && !isModifierKey(keyCode)) {
            downTime = 0;
        }
    }

    /**
     * Builds the single-pointer mouse event. The mouse buttons ride in the
     * button-state field ({@code buttons}), never in the action: the action's
     * upper byte is the pointer index for ACTION_POINTER_DOWN/UP, so
     * encoding a button there makes {@code getActionIndex()} report a second
     * pointer that does not exist.
     */
    private MotionEvent motionEvent(int action, int buttons, int metaState) {
        MotionEvent.PointerProperties properties = pointerProperties();
        MotionEvent.PointerCoords coords = pointerCoords();
        coords.pressure = action == MotionEvent.ACTION_UP ? 0f : 1f;
        long now = SystemClock.uptimeMillis();
        if (action == MotionEvent.ACTION_DOWN) {
            downTime = now;
        }
        return MotionEvent.obtain(
                downTime != 0 ? downTime : now, now, action, 1,
                new MotionEvent.PointerProperties[] {properties},
                new MotionEvent.PointerCoords[] {coords}, metaState, buttons,
                1.0f, 1.0f, 0, 0, InputDevice.SOURCE_MOUSE, 0);
    }

    private MotionEvent.PointerProperties pointerProperties() {
        MotionEvent.PointerProperties properties =
                new MotionEvent.PointerProperties();
        properties.id = 0;
        properties.toolType = MotionEvent.TOOL_TYPE_MOUSE;
        return properties;
    }

    private MotionEvent.PointerCoords pointerCoords() {
        MotionEvent.PointerCoords coords = new MotionEvent.PointerCoords();
        coords.x = cursorX;
        coords.y = cursorY;
        coords.pressure = 1.0f;
        coords.size = 1.0f;
        return coords;
    }

    private static int androidButton(int glfwButton) {
        switch (glfwButton) {
            case 0:
                return MotionEvent.BUTTON_PRIMARY;
            case 1:
                return MotionEvent.BUTTON_SECONDARY;
            case 2:
                return MotionEvent.BUTTON_TERTIARY;
            default:
                return 0;
        }
    }

    private static int metaState(int glfwMods) {
        int metaState = 0;
        if ((glfwMods & GLFW_MOD_SHIFT) != 0) {
            metaState |= KeyEvent.META_SHIFT_ON;
        }
        if ((glfwMods & GLFW_MOD_CONTROL) != 0) {
            metaState |= KeyEvent.META_CTRL_ON;
        }
        if ((glfwMods & GLFW_MOD_ALT) != 0) {
            metaState |= KeyEvent.META_ALT_ON;
        }
        if ((glfwMods & GLFW_MOD_SUPER) != 0) {
            metaState |= KeyEvent.META_META_ON;
        }
        return metaState;
    }

    private static boolean isModifierKey(int keyCode) {
        return KeyEvent.isModifierKey(keyCode);
    }

    /**
     * GLFW key code to Android key code. Letters and digits map arithmetically
     * (GLFW's contiguous ASCII blocks match the KEYCODE_A/KEYCODE_0 order);
     * the rest is the AOSP key-layout table.
     */
    // GLFW key codes are the desktop contract here, so the switch is long.
    @SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.ExcessiveMethodLength"})
    static int keyCode(int glfwKey) {
        if (glfwKey >= 65 && glfwKey <= 90) {
            return KeyEvent.KEYCODE_A + (glfwKey - 65);
        }
        if (glfwKey >= 48 && glfwKey <= 57) {
            return KeyEvent.KEYCODE_0 + (glfwKey - 48);
        }
        if (glfwKey >= 290 && glfwKey <= 314) {
            return KeyEvent.KEYCODE_F1 + (glfwKey - 290);
        }
        if (glfwKey >= 320 && glfwKey <= 329) {
            return KeyEvent.KEYCODE_NUMPAD_0 + (glfwKey - 320);
        }
        switch (glfwKey) {
            case 32:
                return KeyEvent.KEYCODE_SPACE;
            case 39:
                return KeyEvent.KEYCODE_APOSTROPHE;
            case 44:
                return KeyEvent.KEYCODE_COMMA;
            case 45:
                return KeyEvent.KEYCODE_MINUS;
            case 46:
                return KeyEvent.KEYCODE_PERIOD;
            case 47:
                return KeyEvent.KEYCODE_SLASH;
            case 59:
                return KeyEvent.KEYCODE_SEMICOLON;
            case 61:
                return KeyEvent.KEYCODE_EQUALS;
            case 91:
                return KeyEvent.KEYCODE_LEFT_BRACKET;
            case 92:
                return KeyEvent.KEYCODE_BACKSLASH;
            case 93:
                return KeyEvent.KEYCODE_RIGHT_BRACKET;
            case 96:
                return KeyEvent.KEYCODE_GRAVE;
            case 256:
                return KeyEvent.KEYCODE_ESCAPE;
            case 257:
                return KeyEvent.KEYCODE_ENTER;
            case 258:
                return KeyEvent.KEYCODE_TAB;
            case 259:
                return KeyEvent.KEYCODE_DEL;
            case 260:
                return KeyEvent.KEYCODE_INSERT;
            case 261:
                return KeyEvent.KEYCODE_FORWARD_DEL;
            case 262:
                return KeyEvent.KEYCODE_DPAD_RIGHT;
            case 263:
                return KeyEvent.KEYCODE_DPAD_LEFT;
            case 264:
                return KeyEvent.KEYCODE_DPAD_DOWN;
            case 265:
                return KeyEvent.KEYCODE_DPAD_UP;
            case 266:
                return KeyEvent.KEYCODE_PAGE_UP;
            case 267:
                return KeyEvent.KEYCODE_PAGE_DOWN;
            case 268:
                return KeyEvent.KEYCODE_MOVE_HOME;
            case 269:
                return KeyEvent.KEYCODE_MOVE_END;
            case 280:
                return KeyEvent.KEYCODE_CAPS_LOCK;
            case 281:
                return KeyEvent.KEYCODE_SCROLL_LOCK;
            case 282:
                return KeyEvent.KEYCODE_NUM_LOCK;
            case 283:
                return KeyEvent.KEYCODE_SYSRQ;
            case 284:
                return KeyEvent.KEYCODE_BREAK;
            case 330:
                return KeyEvent.KEYCODE_NUMPAD_DOT;
            case 331:
                return KeyEvent.KEYCODE_NUMPAD_DIVIDE;
            case 332:
                return KeyEvent.KEYCODE_NUMPAD_MULTIPLY;
            case 333:
                return KeyEvent.KEYCODE_NUMPAD_SUBTRACT;
            case 334:
                return KeyEvent.KEYCODE_NUMPAD_ADD;
            case 335:
                return KeyEvent.KEYCODE_NUMPAD_ENTER;
            case 336:
                return KeyEvent.KEYCODE_NUMPAD_EQUALS;
            case 340:
                return KeyEvent.KEYCODE_SHIFT_LEFT;
            case 341:
                return KeyEvent.KEYCODE_CTRL_LEFT;
            case 342:
                return KeyEvent.KEYCODE_ALT_LEFT;
            case 343:
                return KeyEvent.KEYCODE_META_LEFT;
            case 344:
                return KeyEvent.KEYCODE_SHIFT_RIGHT;
            case 345:
                return KeyEvent.KEYCODE_CTRL_RIGHT;
            case 346:
                return KeyEvent.KEYCODE_ALT_RIGHT;
            case 347:
                return KeyEvent.KEYCODE_META_RIGHT;
            case 348:
                return KeyEvent.KEYCODE_MENU;
            default:
                return KeyEvent.KEYCODE_UNKNOWN;
        }
    }
}
