package android.view;

    public interface WindowManager extends android.view.ViewManager {

    public static final int COMPAT_SMALL_COVER_SCREEN_OPT_IN = 1;
    public static final java.lang.String PROPERTY_ACTIVITY_EMBEDDING_ALLOW_SYSTEM_OVERRIDE = "android.window.PROPERTY_ACTIVITY_EMBEDDING_ALLOW_SYSTEM_OVERRIDE";
    public static final java.lang.String PROPERTY_ACTIVITY_EMBEDDING_SPLITS_ENABLED = "android.window.PROPERTY_ACTIVITY_EMBEDDING_SPLITS_ENABLED";
    public static final java.lang.String PROPERTY_CAMERA_COMPAT_ALLOW_FORCE_ROTATION = "android.window.PROPERTY_CAMERA_COMPAT_ALLOW_FORCE_ROTATION";
    public static final java.lang.String PROPERTY_CAMERA_COMPAT_ALLOW_REFRESH = "android.window.PROPERTY_CAMERA_COMPAT_ALLOW_REFRESH";
    public static final java.lang.String PROPERTY_CAMERA_COMPAT_ENABLE_REFRESH_VIA_PAUSE = "android.window.PROPERTY_CAMERA_COMPAT_ENABLE_REFRESH_VIA_PAUSE";
    public static final java.lang.String PROPERTY_COMPAT_ALLOW_DISPLAY_ORIENTATION_OVERRIDE = "android.window.PROPERTY_COMPAT_ALLOW_DISPLAY_ORIENTATION_OVERRIDE";
    public static final java.lang.String PROPERTY_COMPAT_ALLOW_IGNORING_ORIENTATION_REQUEST_WHEN_LOOP_DETECTED = "android.window.PROPERTY_COMPAT_ALLOW_IGNORING_ORIENTATION_REQUEST_WHEN_LOOP_DETECTED";
    public static final java.lang.String PROPERTY_COMPAT_ALLOW_MIN_ASPECT_RATIO_OVERRIDE = "android.window.PROPERTY_COMPAT_ALLOW_MIN_ASPECT_RATIO_OVERRIDE";
    public static final java.lang.String PROPERTY_COMPAT_ALLOW_ORIENTATION_OVERRIDE = "android.window.PROPERTY_COMPAT_ALLOW_ORIENTATION_OVERRIDE";
    public static final java.lang.String PROPERTY_COMPAT_ALLOW_RESIZEABLE_ACTIVITY_OVERRIDES = "android.window.PROPERTY_COMPAT_ALLOW_RESIZEABLE_ACTIVITY_OVERRIDES";
    public static final java.lang.String PROPERTY_COMPAT_ALLOW_SANDBOXING_VIEW_BOUNDS_APIS = "android.window.PROPERTY_COMPAT_ALLOW_SANDBOXING_VIEW_BOUNDS_APIS";
    public static final java.lang.String PROPERTY_COMPAT_ALLOW_SMALL_COVER_SCREEN = "android.window.PROPERTY_COMPAT_ALLOW_SMALL_COVER_SCREEN";
    public static final java.lang.String PROPERTY_COMPAT_ALLOW_USER_ASPECT_RATIO_FULLSCREEN_OVERRIDE = "android.window.PROPERTY_COMPAT_ALLOW_USER_ASPECT_RATIO_FULLSCREEN_OVERRIDE";
    public static final java.lang.String PROPERTY_COMPAT_ALLOW_USER_ASPECT_RATIO_OVERRIDE = "android.window.PROPERTY_COMPAT_ALLOW_USER_ASPECT_RATIO_OVERRIDE";
    public static final java.lang.String PROPERTY_COMPAT_ENABLE_FAKE_FOCUS = "android.window.PROPERTY_COMPAT_ENABLE_FAKE_FOCUS";
    public static final java.lang.String PROPERTY_COMPAT_IGNORE_REQUESTED_ORIENTATION = "android.window.PROPERTY_COMPAT_IGNORE_REQUESTED_ORIENTATION";
    public static final java.lang.String PROPERTY_SUPPORTS_MULTI_INSTANCE_SYSTEM_UI = "android.window.PROPERTY_SUPPORTS_MULTI_INSTANCE_SYSTEM_UI";
    public static final int SCREEN_RECORDING_STATE_NOT_VISIBLE = 0;
    public static final int SCREEN_RECORDING_STATE_VISIBLE = 1;

    public default void addCrossWindowBlurEnabledListener(java.util.concurrent.Executor arg0, java.util.function.Consumer<java.lang.Boolean> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager;->addCrossWindowBlurEnabledListener(Ljava/util/concurrent/Executor;Ljava/util/function/Consumer;)V");
    }

    public default void addCrossWindowBlurEnabledListener(java.util.function.Consumer<java.lang.Boolean> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager;->addCrossWindowBlurEnabledListener(Ljava/util/function/Consumer;)V");
    }

    public default void addProposedRotationListener(java.util.concurrent.Executor arg0, java.util.function.IntConsumer arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager;->addProposedRotationListener(Ljava/util/concurrent/Executor;Ljava/util/function/IntConsumer;)V");
    }

    public default int addScreenRecordingCallback(java.util.concurrent.Executor arg0, java.util.function.Consumer<java.lang.Integer> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager;->addScreenRecordingCallback(Ljava/util/concurrent/Executor;Ljava/util/function/Consumer;)I");
    }

    public default android.view.WindowMetrics getCurrentWindowMetrics() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager;->getCurrentWindowMetrics()Landroid/view/WindowMetrics;");
    }

    public abstract android.view.Display getDefaultDisplay();

    public default android.view.WindowMetrics getMaximumWindowMetrics() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager;->getMaximumWindowMetrics()Landroid/view/WindowMetrics;");
    }

    public default boolean isCrossWindowBlurEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager;->isCrossWindowBlurEnabled()Z");
    }

    public default android.window.InputTransferToken registerBatchedSurfaceControlInputReceiver(android.window.InputTransferToken arg0, android.view.SurfaceControl arg1, android.view.Choreographer arg2, android.view.SurfaceControlInputReceiver arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager;->registerBatchedSurfaceControlInputReceiver(Landroid/window/InputTransferToken;Landroid/view/SurfaceControl;Landroid/view/Choreographer;Landroid/view/SurfaceControlInputReceiver;)Landroid/window/InputTransferToken;");
    }

    public default void registerTrustedPresentationListener(android.os.IBinder arg0, android.window.TrustedPresentationThresholds arg1, java.util.concurrent.Executor arg2, java.util.function.Consumer<java.lang.Boolean> arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager;->registerTrustedPresentationListener(Landroid/os/IBinder;Landroid/window/TrustedPresentationThresholds;Ljava/util/concurrent/Executor;Ljava/util/function/Consumer;)V");
    }

    public default android.window.InputTransferToken registerUnbatchedSurfaceControlInputReceiver(android.window.InputTransferToken arg0, android.view.SurfaceControl arg1, android.os.Looper arg2, android.view.SurfaceControlInputReceiver arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager;->registerUnbatchedSurfaceControlInputReceiver(Landroid/window/InputTransferToken;Landroid/view/SurfaceControl;Landroid/os/Looper;Landroid/view/SurfaceControlInputReceiver;)Landroid/window/InputTransferToken;");
    }

    public default void removeCrossWindowBlurEnabledListener(java.util.function.Consumer<java.lang.Boolean> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager;->removeCrossWindowBlurEnabledListener(Ljava/util/function/Consumer;)V");
    }

    public default void removeProposedRotationListener(java.util.function.IntConsumer arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager;->removeProposedRotationListener(Ljava/util/function/IntConsumer;)V");
    }

    public default void removeScreenRecordingCallback(java.util.function.Consumer<java.lang.Integer> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager;->removeScreenRecordingCallback(Ljava/util/function/Consumer;)V");
    }

    public abstract void removeViewImmediate(android.view.View arg0);

    public default boolean transferTouchGesture(android.window.InputTransferToken arg0, android.window.InputTransferToken arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager;->transferTouchGesture(Landroid/window/InputTransferToken;Landroid/window/InputTransferToken;)Z");
    }

    public default void unregisterSurfaceControlInputReceiver(android.view.SurfaceControl arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager;->unregisterSurfaceControlInputReceiver(Landroid/view/SurfaceControl;)V");
    }

    public default void unregisterTrustedPresentationListener(java.util.function.Consumer<java.lang.Boolean> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager;->unregisterTrustedPresentationListener(Ljava/util/function/Consumer;)V");
    }
        public static class LayoutParams extends android.view.ViewGroup.LayoutParams implements android.os.Parcelable {

        public static final int ALPHA_CHANGED = 128;
        public static final int ANIMATION_CHANGED = 16;
        public static final float BRIGHTNESS_OVERRIDE_FULL = 1.0f;
        public static final float BRIGHTNESS_OVERRIDE_NONE = -1.0f;
        public static final float BRIGHTNESS_OVERRIDE_OFF = 0.0f;
        public static android.os.Parcelable.Creator CREATOR;
        public static final int DIM_AMOUNT_CHANGED = 32;
        public static final int DISPLAY_FLAG_DISABLE_HDR_CONVERSION = 1;
        public static final int FIRST_APPLICATION_WINDOW = 1;
        public static final int FIRST_SUB_WINDOW = 1000;
        public static final int FIRST_SYSTEM_WINDOW = 2000;
        public static final int FLAGS_CHANGED = 4;
        public static final int FLAG_ALLOW_LOCK_WHILE_SCREEN_ON = 1;
        public static final int FLAG_ALT_FOCUSABLE_IM = 131072;
        public static final int FLAG_BLUR_BEHIND = 4;
        public static final int FLAG_DIM_BEHIND = 2;
        public static final int FLAG_DISMISS_KEYGUARD = 4194304;
        public static final int FLAG_DITHER = 4096;
        public static final int FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS = -2147483648;
        public static final int FLAG_FORCE_NOT_FULLSCREEN = 2048;
        public static final int FLAG_FULLSCREEN = 1024;
        public static final int FLAG_HARDWARE_ACCELERATED = 16777216;
        public static final int FLAG_IGNORE_CHEEK_PRESSES = 32768;
        public static final int FLAG_KEEP_SCREEN_ON = 128;
        public static final int FLAG_LAYOUT_ATTACHED_IN_DECOR = 1073741824;
        public static final int FLAG_LAYOUT_INSET_DECOR = 65536;
        public static final int FLAG_LAYOUT_IN_OVERSCAN = 33554432;
        public static final int FLAG_LAYOUT_IN_SCREEN = 256;
        public static final int FLAG_LAYOUT_NO_LIMITS = 512;
        public static final int FLAG_LOCAL_FOCUS_MODE = 268435456;
        public static final int FLAG_NOT_FOCUSABLE = 8;
        public static final int FLAG_NOT_TOUCHABLE = 16;
        public static final int FLAG_NOT_TOUCH_MODAL = 32;
        public static final int FLAG_SCALED = 16384;
        public static final int FLAG_SECURE = 8192;
        public static final int FLAG_SHOW_WALLPAPER = 1048576;
        public static final int FLAG_SHOW_WHEN_LOCKED = 524288;
        public static final int FLAG_SPLIT_TOUCH = 8388608;
        public static final int FLAG_TOUCHABLE_WHEN_WAKING = 64;
        public static final int FLAG_TRANSLUCENT_NAVIGATION = 134217728;
        public static final int FLAG_TRANSLUCENT_STATUS = 67108864;
        public static final int FLAG_TURN_SCREEN_ON = 2097152;
        public static final int FLAG_WATCH_OUTSIDE_TOUCH = 262144;
        public static final int FORMAT_CHANGED = 8;
        public static final int LAST_APPLICATION_WINDOW = 99;
        public static final int LAST_SUB_WINDOW = 1999;
        public static final int LAST_SYSTEM_WINDOW = 2999;
        public static final int LAYOUT_CHANGED = 1;
        public static final int LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS = 3;
        public static final int LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT = 0;
        public static final int LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER = 2;
        public static final int LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES = 1;
        public static final int MEMORY_TYPE_CHANGED = 256;
        public static final int MEMORY_TYPE_GPU = 2;
        public static final int MEMORY_TYPE_HARDWARE = 1;
        public static final int MEMORY_TYPE_NORMAL = 0;
        public static final int MEMORY_TYPE_PUSH_BUFFERS = 3;
        public static final int ROTATION_ANIMATION_CHANGED = 4096;
        public static final int ROTATION_ANIMATION_CROSSFADE = 1;
        public static final int ROTATION_ANIMATION_JUMPCUT = 2;
        public static final int ROTATION_ANIMATION_ROTATE = 0;
        public static final int ROTATION_ANIMATION_SEAMLESS = 3;
        public static final int SCREEN_BRIGHTNESS_CHANGED = 2048;
        public static final int SCREEN_ORIENTATION_CHANGED = 1024;
        public static final int SOFT_INPUT_ADJUST_NOTHING = 48;
        public static final int SOFT_INPUT_ADJUST_PAN = 32;
        public static final int SOFT_INPUT_ADJUST_RESIZE = 16;
        public static final int SOFT_INPUT_ADJUST_UNSPECIFIED = 0;
        public static final int SOFT_INPUT_IS_FORWARD_NAVIGATION = 256;
        public static final int SOFT_INPUT_MASK_ADJUST = 240;
        public static final int SOFT_INPUT_MASK_STATE = 15;
        public static final int SOFT_INPUT_MODE_CHANGED = 512;
        public static final int SOFT_INPUT_STATE_ALWAYS_HIDDEN = 3;
        public static final int SOFT_INPUT_STATE_ALWAYS_VISIBLE = 5;
        public static final int SOFT_INPUT_STATE_HIDDEN = 2;
        public static final int SOFT_INPUT_STATE_UNCHANGED = 1;
        public static final int SOFT_INPUT_STATE_UNSPECIFIED = 0;
        public static final int SOFT_INPUT_STATE_VISIBLE = 4;
        public static final int TITLE_CHANGED = 64;
        public static final int TYPE_ACCESSIBILITY_OVERLAY = 2032;
        public static final int TYPE_APPLICATION = 2;
        public static final int TYPE_APPLICATION_ATTACHED_DIALOG = 1003;
        public static final int TYPE_APPLICATION_MEDIA = 1001;
        public static final int TYPE_APPLICATION_OVERLAY = 2038;
        public static final int TYPE_APPLICATION_PANEL = 1000;
        public static final int TYPE_APPLICATION_STARTING = 3;
        public static final int TYPE_APPLICATION_SUB_PANEL = 1002;
        public static final int TYPE_BASE_APPLICATION = 1;
        public static final int TYPE_CHANGED = 2;
        public static final int TYPE_DRAWN_APPLICATION = 4;
        public static final int TYPE_INPUT_METHOD = 2011;
        public static final int TYPE_INPUT_METHOD_DIALOG = 2012;
        public static final int TYPE_KEYGUARD_DIALOG = 2009;
        public static final int TYPE_PHONE = 2002;
        public static final int TYPE_PRIORITY_PHONE = 2007;
        public static final int TYPE_PRIVATE_PRESENTATION = 2030;
        public static final int TYPE_SEARCH_BAR = 2001;
        public static final int TYPE_STATUS_BAR = 2000;
        public static final int TYPE_SYSTEM_ALERT = 2003;
        public static final int TYPE_SYSTEM_DIALOG = 2008;
        public static final int TYPE_SYSTEM_ERROR = 2010;
        public static final int TYPE_SYSTEM_OVERLAY = 2006;
        public static final int TYPE_TOAST = 2005;
        public static final int TYPE_WALLPAPER = 2013;
        public float alpha;
        public float buttonBrightness;
        public float dimAmount;
        public int flags;
        public int format;
        public int gravity;
        public float horizontalMargin;
        public float horizontalWeight;
        public int layoutInDisplayCutoutMode;
        public int memoryType;
        public java.lang.String packageName;
        public boolean preferMinimalPostProcessing;
        public int preferredDisplayModeId;
        public float preferredRefreshRate;
        public int rotationAnimation;
        public float screenBrightness;
        public int screenOrientation;
        public int softInputMode;
        public int systemUiVisibility;
        public android.os.IBinder token;
        public int type;
        public float verticalMargin;
        public float verticalWeight;
        public int windowAnimations;
        public int x;
        public int y;

        public LayoutParams() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;-><init>()V");
        }

        public LayoutParams(android.os.Parcel arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;-><init>(Landroid/os/Parcel;)V");
        }

        public LayoutParams(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;-><init>(I)V");
        }

        public LayoutParams(int arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;-><init>(II)V");
        }

        public LayoutParams(int arg0, int arg1, int arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;-><init>(III)V");
        }

        public LayoutParams(int arg0, int arg1, int arg2, int arg3, int arg4) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;-><init>(IIIII)V");
        }

        public LayoutParams(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;-><init>(IIIIIII)V");
        }

        public boolean areWallpaperTouchEventsEnabled() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;->areWallpaperTouchEventsEnabled()Z");
        }

        public boolean canPlayMoveAnimation() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;->canPlayMoveAnimation()Z");
        }

        public final int copyFrom(android.view.WindowManager.LayoutParams arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;->copyFrom(Landroid/view/WindowManager$LayoutParams;)I");
        }

        public java.lang.String debug(java.lang.String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;->debug(Ljava/lang/String;)Ljava/lang/String;");
        }

        public int describeContents() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;->describeContents()I");
        }

        public int getBlurBehindRadius() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;->getBlurBehindRadius()I");
        }

        public int getColorMode() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;->getColorMode()I");
        }

        public float getDesiredHdrHeadroom() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;->getDesiredHdrHeadroom()F");
        }

        public int getFitInsetsSides() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;->getFitInsetsSides()I");
        }

        public int getFitInsetsTypes() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;->getFitInsetsTypes()I");
        }

        public boolean getFrameRateBoostOnTouchEnabled() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;->getFrameRateBoostOnTouchEnabled()Z");
        }

        public final java.lang.CharSequence getTitle() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;->getTitle()Ljava/lang/CharSequence;");
        }

        public boolean isFitInsetsIgnoringVisibility() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;->isFitInsetsIgnoringVisibility()Z");
        }

        public boolean isFrameRatePowerSavingsBalanced() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;->isFrameRatePowerSavingsBalanced()Z");
        }

        public boolean isHdrConversionEnabled() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;->isHdrConversionEnabled()Z");
        }

        public static boolean mayUseInputMethod(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;->mayUseInputMethod(I)Z");
        }

        public void setBlurBehindRadius(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;->setBlurBehindRadius(I)V");
        }

        public void setCanPlayMoveAnimation(boolean arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;->setCanPlayMoveAnimation(Z)V");
        }

        public void setColorMode(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;->setColorMode(I)V");
        }

        public void setDesiredHdrHeadroom(float arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;->setDesiredHdrHeadroom(F)V");
        }

        public void setFitInsetsIgnoringVisibility(boolean arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;->setFitInsetsIgnoringVisibility(Z)V");
        }

        public void setFitInsetsSides(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;->setFitInsetsSides(I)V");
        }

        public void setFitInsetsTypes(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;->setFitInsetsTypes(I)V");
        }

        public void setFrameRateBoostOnTouchEnabled(boolean arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;->setFrameRateBoostOnTouchEnabled(Z)V");
        }

        public void setFrameRatePowerSavingsBalanced(boolean arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;->setFrameRatePowerSavingsBalanced(Z)V");
        }

        public void setHdrConversionEnabled(boolean arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;->setHdrConversionEnabled(Z)V");
        }

        public final void setTitle(java.lang.CharSequence arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;->setTitle(Ljava/lang/CharSequence;)V");
        }

        public void setWallpaperTouchEventsEnabled(boolean arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;->setWallpaperTouchEventsEnabled(Z)V");
        }

        public java.lang.String toString() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;->toString()Ljava/lang/String;");
        }

        public void writeToParcel(android.os.Parcel arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/WindowManager$LayoutParams;->writeToParcel(Landroid/os/Parcel;I)V");
        }
        }
    }
