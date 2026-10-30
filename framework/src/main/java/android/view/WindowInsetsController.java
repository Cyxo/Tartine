package android.view;

import android.os.CancellationSignal;
import android.view.animation.Interpolator;

    public interface WindowInsetsController {

    public static final int APPEARANCE_LIGHT_CAPTION_BARS = 256;
    public static final int APPEARANCE_LIGHT_NAVIGATION_BARS = 16;
    public static final int APPEARANCE_LIGHT_STATUS_BARS = 8;
    public static final int APPEARANCE_TRANSPARENT_CAPTION_BAR_BACKGROUND = 128;
    public static final int BEHAVIOR_DEFAULT = 1;
    public static final int BEHAVIOR_SHOW_BARS_BY_SWIPE = 1;
    public static final int BEHAVIOR_SHOW_BARS_BY_TOUCH = 0;
    public static final int BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE = 2;

    public abstract void addOnControllableInsetsChangedListener(WindowInsetsController.OnControllableInsetsChangedListener arg0);

    public abstract void controlWindowInsetsAnimation(int arg0, long arg1, Interpolator arg2, CancellationSignal arg3, WindowInsetsAnimationControlListener arg4);

    public abstract int getSystemBarsAppearance();

    public abstract int getSystemBarsBehavior();

    public abstract void hide(int arg0);

    public abstract void removeOnControllableInsetsChangedListener(WindowInsetsController.OnControllableInsetsChangedListener arg0);

    public abstract void setSystemBarsAppearance(int arg0, int arg1);

    public abstract void setSystemBarsBehavior(int arg0);

    public abstract void show(int arg0);
        public static interface OnControllableInsetsChangedListener {

        public abstract void onControllableInsetsChanged(WindowInsetsController arg0, int arg1);
        }
    }
