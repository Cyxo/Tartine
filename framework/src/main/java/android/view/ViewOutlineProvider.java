package android.view;

    public class ViewOutlineProvider {

    // Ported from aosp-mirror/platform_frameworks_base
    // core/java/android/view/ViewOutlineProvider.java (BACKGROUND/BOUNDS/
    // PADDED_BOUNDS): the class carries an implicit no-arg ctor on device;
    // the three constants are anonymous subclasses. Bodies delegate to
    // APIs whose module ports may themselves still gap (Drawable#getOutline,
    // View#getBackground, Outline#setRect, padding getters) - that is the
    // gap-driven loop naming the next hole, as anywhere else.

    public ViewOutlineProvider() {
    }

    /**
     * Default outline provider for Views, which queries the Outline from
     * the View's background, or generates a 0 alpha, rectangular Outline
     * the size of the View if a background isn't present.
     */
    public static final android.view.ViewOutlineProvider BACKGROUND =
            new android.view.ViewOutlineProvider() {

        @Override
        public void getOutline(android.view.View arg0, android.graphics.Outline arg1) {
            android.graphics.drawable.Drawable background = arg0.getBackground();
            if (background != null) {
                background.getOutline(arg1);
            } else {
                arg1.setRect(0, 0, arg0.getWidth(), arg0.getHeight());
                arg1.setAlpha(0.0f);
            }
        }
    };

    /**
     * Maintains the outline of the View to match its rectangular bounds,
     * at <code>1.0f</code> alpha.
     */
    public static final android.view.ViewOutlineProvider BOUNDS =
            new android.view.ViewOutlineProvider() {

        @Override
        public void getOutline(android.view.View arg0, android.graphics.Outline arg1) {
            arg1.setRect(0, 0, arg0.getWidth(), arg0.getHeight());
        }
    };

    /**
     * Maintains the outline of the View to match its rectangular padded
     * bounds, at <code>1.0f</code> alpha.
     */
    public static final android.view.ViewOutlineProvider PADDED_BOUNDS =
            new android.view.ViewOutlineProvider() {

        @Override
        public void getOutline(android.view.View arg0, android.graphics.Outline arg1) {
            arg1.setRect(arg0.getPaddingLeft(),
                    arg0.getPaddingTop(),
                    arg0.getWidth() - arg0.getPaddingRight(),
                    arg0.getHeight() - arg0.getPaddingBottom());
        }
    };

    public void getOutline(android.view.View arg0, android.graphics.Outline arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewOutlineProvider;->getOutline(Landroid/view/View;Landroid/graphics/Outline;)V");
    }
    }
