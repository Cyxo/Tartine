package android.view;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BlendMode;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

    public interface MenuItem {

    public static final int SHOW_AS_ACTION_ALWAYS = 2;
    public static final int SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW = 8;
    public static final int SHOW_AS_ACTION_IF_ROOM = 1;
    public static final int SHOW_AS_ACTION_NEVER = 0;
    public static final int SHOW_AS_ACTION_WITH_TEXT = 4;

    public abstract boolean collapseActionView();

    public abstract boolean expandActionView();

    public abstract ActionProvider getActionProvider();

    public abstract View getActionView();

    public default int getAlphabeticModifiers() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MenuItem;->getAlphabeticModifiers()I");
    }

    public abstract char getAlphabeticShortcut();

    public default CharSequence getContentDescription() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MenuItem;->getContentDescription()Ljava/lang/CharSequence;");
    }

    public abstract int getGroupId();

    public abstract Drawable getIcon();

    public default BlendMode getIconTintBlendMode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MenuItem;->getIconTintBlendMode()Landroid/graphics/BlendMode;");
    }

    public default ColorStateList getIconTintList() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MenuItem;->getIconTintList()Landroid/content/res/ColorStateList;");
    }

    public default PorterDuff.Mode getIconTintMode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MenuItem;->getIconTintMode()Landroid/graphics/PorterDuff$Mode;");
    }

    public abstract Intent getIntent();

    public abstract int getItemId();

    public abstract ContextMenu.ContextMenuInfo getMenuInfo();

    public default int getNumericModifiers() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MenuItem;->getNumericModifiers()I");
    }

    public abstract char getNumericShortcut();

    public abstract int getOrder();

    public abstract SubMenu getSubMenu();

    public abstract CharSequence getTitle();

    public abstract CharSequence getTitleCondensed();

    public default CharSequence getTooltipText() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MenuItem;->getTooltipText()Ljava/lang/CharSequence;");
    }

    public abstract boolean hasSubMenu();

    public abstract boolean isActionViewExpanded();

    public abstract boolean isCheckable();

    public abstract boolean isChecked();

    public abstract boolean isEnabled();

    public abstract boolean isVisible();

    public abstract MenuItem setActionProvider(ActionProvider arg0);

    public abstract MenuItem setActionView(View arg0);

    public abstract MenuItem setActionView(int arg0);

    public abstract MenuItem setAlphabeticShortcut(char arg0);

    public default MenuItem setAlphabeticShortcut(char arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MenuItem;->setAlphabeticShortcut(CI)Landroid/view/MenuItem;");
    }

    public abstract MenuItem setCheckable(boolean arg0);

    public abstract MenuItem setChecked(boolean arg0);

    public default MenuItem setContentDescription(CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MenuItem;->setContentDescription(Ljava/lang/CharSequence;)Landroid/view/MenuItem;");
    }

    public abstract MenuItem setEnabled(boolean arg0);

    public abstract MenuItem setIcon(Drawable arg0);

    public abstract MenuItem setIcon(int arg0);

    public default MenuItem setIconTintBlendMode(BlendMode arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MenuItem;->setIconTintBlendMode(Landroid/graphics/BlendMode;)Landroid/view/MenuItem;");
    }

    public default MenuItem setIconTintList(ColorStateList arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MenuItem;->setIconTintList(Landroid/content/res/ColorStateList;)Landroid/view/MenuItem;");
    }

    public default MenuItem setIconTintMode(PorterDuff.Mode arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MenuItem;->setIconTintMode(Landroid/graphics/PorterDuff$Mode;)Landroid/view/MenuItem;");
    }

    public abstract MenuItem setIntent(Intent arg0);

    public abstract MenuItem setNumericShortcut(char arg0);

    public default MenuItem setNumericShortcut(char arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MenuItem;->setNumericShortcut(CI)Landroid/view/MenuItem;");
    }

    public abstract MenuItem setOnActionExpandListener(MenuItem.OnActionExpandListener arg0);

    public abstract MenuItem setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener arg0);

    public abstract MenuItem setShortcut(char arg0, char arg1);

    public default MenuItem setShortcut(char arg0, char arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MenuItem;->setShortcut(CCII)Landroid/view/MenuItem;");
    }

    public abstract void setShowAsAction(int arg0);

    public abstract MenuItem setShowAsActionFlags(int arg0);

    public abstract MenuItem setTitle(int arg0);

    public abstract MenuItem setTitle(CharSequence arg0);

    public abstract MenuItem setTitleCondensed(CharSequence arg0);

    public default MenuItem setTooltipText(CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MenuItem;->setTooltipText(Ljava/lang/CharSequence;)Landroid/view/MenuItem;");
    }

    public abstract MenuItem setVisible(boolean arg0);
        public static interface OnActionExpandListener {

        public abstract boolean onMenuItemActionCollapse(MenuItem arg0);

        public abstract boolean onMenuItemActionExpand(MenuItem arg0);
        }
        public static interface OnMenuItemClickListener {

        public abstract boolean onMenuItemClick(MenuItem arg0);
        }
    }
