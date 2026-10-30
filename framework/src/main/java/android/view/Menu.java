package android.view;

import android.content.ComponentName;
import android.content.Intent;

    public interface Menu {

    public static final int CATEGORY_ALTERNATIVE = 262144;
    public static final int CATEGORY_CONTAINER = 65536;
    public static final int CATEGORY_SECONDARY = 196608;
    public static final int CATEGORY_SYSTEM = 131072;
    public static final int FIRST = 1;
    public static final int FLAG_ALWAYS_PERFORM_CLOSE = 2;
    public static final int FLAG_APPEND_TO_GROUP = 1;
    public static final int FLAG_PERFORM_NO_CLOSE = 1;
    public static final int NONE = 0;
    public static final int SUPPORTED_MODIFIERS_MASK = 69647;

    public abstract MenuItem add(int arg0);

    public abstract MenuItem add(int arg0, int arg1, int arg2, int arg3);

    public abstract MenuItem add(int arg0, int arg1, int arg2, CharSequence arg3);

    public abstract MenuItem add(CharSequence arg0);

    public abstract int addIntentOptions(int arg0, int arg1, int arg2, ComponentName arg3, Intent[] arg4, Intent arg5, int arg6, MenuItem[] arg7);

    public abstract SubMenu addSubMenu(int arg0);

    public abstract SubMenu addSubMenu(int arg0, int arg1, int arg2, int arg3);

    public abstract SubMenu addSubMenu(int arg0, int arg1, int arg2, CharSequence arg3);

    public abstract SubMenu addSubMenu(CharSequence arg0);

    public abstract void clear();

    public abstract void close();

    public abstract MenuItem findItem(int arg0);

    public abstract MenuItem getItem(int arg0);

    public abstract boolean hasVisibleItems();

    public abstract boolean isShortcutKey(int arg0, KeyEvent arg1);

    public abstract boolean performIdentifierAction(int arg0, int arg1);

    public abstract boolean performShortcut(int arg0, KeyEvent arg1, int arg2);

    public abstract void removeGroup(int arg0);

    public abstract void removeItem(int arg0);

    public abstract void setGroupCheckable(int arg0, boolean arg1, boolean arg2);

    public default void setGroupDividerEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/Menu;->setGroupDividerEnabled(Z)V");
    }

    public abstract void setGroupEnabled(int arg0, boolean arg1);

    public abstract void setGroupVisible(int arg0, boolean arg1);

    public abstract void setQwertyMode(boolean arg0);

    public abstract int size();
    }
