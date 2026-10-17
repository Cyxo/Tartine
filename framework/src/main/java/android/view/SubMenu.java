package android.view;

    public interface SubMenu extends android.view.Menu {

    public abstract void clearHeader();

    public abstract android.view.MenuItem getItem();

    public abstract android.view.SubMenu setHeaderIcon(android.graphics.drawable.Drawable arg0);

    public abstract android.view.SubMenu setHeaderIcon(int arg0);

    public abstract android.view.SubMenu setHeaderTitle(int arg0);

    public abstract android.view.SubMenu setHeaderTitle(java.lang.CharSequence arg0);

    public abstract android.view.SubMenu setHeaderView(android.view.View arg0);

    public abstract android.view.SubMenu setIcon(android.graphics.drawable.Drawable arg0);

    public abstract android.view.SubMenu setIcon(int arg0);
    }
