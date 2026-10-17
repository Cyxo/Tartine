package android.view;

    public abstract class ViewOutlineProvider {

    public static android.view.ViewOutlineProvider BACKGROUND;
    public static android.view.ViewOutlineProvider BOUNDS;
    public static android.view.ViewOutlineProvider PADDED_BOUNDS;

    public ViewOutlineProvider() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ViewOutlineProvider;-><init>()V");
    }

    public abstract void getOutline(android.view.View arg0, android.graphics.Outline arg1);
    }
