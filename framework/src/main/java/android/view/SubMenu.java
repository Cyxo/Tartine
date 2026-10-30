package android.view;

import android.graphics.drawable.Drawable;

    public interface SubMenu extends Menu {

    public abstract void clearHeader();

    public abstract MenuItem getItem();

    public abstract SubMenu setHeaderIcon(Drawable arg0);

    public abstract SubMenu setHeaderIcon(int arg0);

    public abstract SubMenu setHeaderTitle(int arg0);

    public abstract SubMenu setHeaderTitle(CharSequence arg0);

    public abstract SubMenu setHeaderView(View arg0);

    public abstract SubMenu setIcon(Drawable arg0);

    public abstract SubMenu setIcon(int arg0);
    }
