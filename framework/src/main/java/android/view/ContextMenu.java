package android.view;

import android.graphics.drawable.Drawable;

    public interface ContextMenu extends Menu {

    public abstract void clearHeader();

    public abstract ContextMenu setHeaderIcon(Drawable arg0);

    public abstract ContextMenu setHeaderIcon(int arg0);

    public abstract ContextMenu setHeaderTitle(int arg0);

    public abstract ContextMenu setHeaderTitle(CharSequence arg0);

    public abstract ContextMenu setHeaderView(View arg0);
        public static interface ContextMenuInfo {
        }
    }
