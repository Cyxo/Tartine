package android.app.jank;

    public class AppJankStats {

    public static final String WIDGET_CATEGORY_ANIMATION = "animation";
    public static final String WIDGET_CATEGORY_KEYBOARD = "keyboard";
    public static final String WIDGET_CATEGORY_MEDIA = "media";
    public static final String WIDGET_CATEGORY_NAVIGATION = "navigation";
    public static final String WIDGET_CATEGORY_OTHER = "other";
    public static final String WIDGET_CATEGORY_SCROLL = "scroll";
    public static final String WIDGET_CATEGORY_UNSPECIFIED = "unspecified";
    public static final String WIDGET_STATE_ANIMATING = "animating";
    public static final String WIDGET_STATE_DRAGGING = "dragging";
    public static final String WIDGET_STATE_FLINGING = "flinging";
    public static final String WIDGET_STATE_NONE = "none";
    public static final String WIDGET_STATE_PLAYBACK = "playback";
    public static final String WIDGET_STATE_PREDICTIVE_BACK = "predictive_back";
    public static final String WIDGET_STATE_SCROLLING = "scrolling";
    public static final String WIDGET_STATE_SWIPING = "swiping";
    public static final String WIDGET_STATE_TAPPING = "tapping";
    public static final String WIDGET_STATE_UNSPECIFIED = "unspecified";
    public static final String WIDGET_STATE_ZOOMING = "zooming";

    public AppJankStats(int arg0, String arg1, String arg2, String arg3, String arg4, long arg5, long arg6, RelativeFrameTimeHistogram arg7) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/jank/AppJankStats;-><init>(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JJLandroid/app/jank/RelativeFrameTimeHistogram;)V");
    }

    public long getJankyFrameCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/jank/AppJankStats;->getJankyFrameCount()J");
    }

    public String getNavigationComponent() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/jank/AppJankStats;->getNavigationComponent()Ljava/lang/String;");
    }

    public RelativeFrameTimeHistogram getRelativeFrameTimeHistogram() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/jank/AppJankStats;->getRelativeFrameTimeHistogram()Landroid/app/jank/RelativeFrameTimeHistogram;");
    }

    public long getTotalFrameCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/jank/AppJankStats;->getTotalFrameCount()J");
    }

    public int getUid() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/jank/AppJankStats;->getUid()I");
    }

    public String getWidgetCategory() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/jank/AppJankStats;->getWidgetCategory()Ljava/lang/String;");
    }

    public String getWidgetId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/jank/AppJankStats;->getWidgetId()Ljava/lang/String;");
    }

    public String getWidgetState() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/jank/AppJankStats;->getWidgetState()Ljava/lang/String;");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public AppJankStats() {
        }
    }
