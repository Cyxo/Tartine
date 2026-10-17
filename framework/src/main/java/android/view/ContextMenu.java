package android.view;

    public interface ContextMenu extends android.view.Menu {

    public abstract void clearHeader();

    public abstract android.view.ContextMenu setHeaderIcon(android.graphics.drawable.Drawable arg0);

    public abstract android.view.ContextMenu setHeaderIcon(int arg0);

    public abstract android.view.ContextMenu setHeaderTitle(int arg0);

    public abstract android.view.ContextMenu setHeaderTitle(java.lang.CharSequence arg0);

    public abstract android.view.ContextMenu setHeaderView(android.view.View arg0);
        public static interface ContextMenuInfo {
        }
    }
