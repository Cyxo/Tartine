package android.view;

    public interface MenuItem {

    public static final int SHOW_AS_ACTION_ALWAYS = 2;
    public static final int SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW = 8;
    public static final int SHOW_AS_ACTION_IF_ROOM = 1;
    public static final int SHOW_AS_ACTION_NEVER = 0;
    public static final int SHOW_AS_ACTION_WITH_TEXT = 4;

    public abstract boolean collapseActionView();

    public abstract boolean expandActionView();

    public abstract android.view.ActionProvider getActionProvider();

    public abstract android.view.View getActionView();

    public default int getAlphabeticModifiers() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MenuItem;->getAlphabeticModifiers()I");
    }

    public abstract char getAlphabeticShortcut();

    public default java.lang.CharSequence getContentDescription() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MenuItem;->getContentDescription()Ljava/lang/CharSequence;");
    }

    public abstract int getGroupId();

    public abstract android.graphics.drawable.Drawable getIcon();

    public default android.graphics.BlendMode getIconTintBlendMode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MenuItem;->getIconTintBlendMode()Landroid/graphics/BlendMode;");
    }

    public default android.content.res.ColorStateList getIconTintList() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MenuItem;->getIconTintList()Landroid/content/res/ColorStateList;");
    }

    public default android.graphics.PorterDuff.Mode getIconTintMode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MenuItem;->getIconTintMode()Landroid/graphics/PorterDuff$Mode;");
    }

    public abstract android.content.Intent getIntent();

    public abstract int getItemId();

    public abstract android.view.ContextMenu.ContextMenuInfo getMenuInfo();

    public default int getNumericModifiers() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MenuItem;->getNumericModifiers()I");
    }

    public abstract char getNumericShortcut();

    public abstract int getOrder();

    public abstract android.view.SubMenu getSubMenu();

    public abstract java.lang.CharSequence getTitle();

    public abstract java.lang.CharSequence getTitleCondensed();

    public default java.lang.CharSequence getTooltipText() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MenuItem;->getTooltipText()Ljava/lang/CharSequence;");
    }

    public abstract boolean hasSubMenu();

    public abstract boolean isActionViewExpanded();

    public abstract boolean isCheckable();

    public abstract boolean isChecked();

    public abstract boolean isEnabled();

    public abstract boolean isVisible();

    public abstract android.view.MenuItem setActionProvider(android.view.ActionProvider arg0);

    public abstract android.view.MenuItem setActionView(android.view.View arg0);

    public abstract android.view.MenuItem setActionView(int arg0);

    public abstract android.view.MenuItem setAlphabeticShortcut(char arg0);

    public default android.view.MenuItem setAlphabeticShortcut(char arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MenuItem;->setAlphabeticShortcut(CI)Landroid/view/MenuItem;");
    }

    public abstract android.view.MenuItem setCheckable(boolean arg0);

    public abstract android.view.MenuItem setChecked(boolean arg0);

    public default android.view.MenuItem setContentDescription(java.lang.CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MenuItem;->setContentDescription(Ljava/lang/CharSequence;)Landroid/view/MenuItem;");
    }

    public abstract android.view.MenuItem setEnabled(boolean arg0);

    public abstract android.view.MenuItem setIcon(android.graphics.drawable.Drawable arg0);

    public abstract android.view.MenuItem setIcon(int arg0);

    public default android.view.MenuItem setIconTintBlendMode(android.graphics.BlendMode arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MenuItem;->setIconTintBlendMode(Landroid/graphics/BlendMode;)Landroid/view/MenuItem;");
    }

    public default android.view.MenuItem setIconTintList(android.content.res.ColorStateList arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MenuItem;->setIconTintList(Landroid/content/res/ColorStateList;)Landroid/view/MenuItem;");
    }

    public default android.view.MenuItem setIconTintMode(android.graphics.PorterDuff.Mode arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MenuItem;->setIconTintMode(Landroid/graphics/PorterDuff$Mode;)Landroid/view/MenuItem;");
    }

    public abstract android.view.MenuItem setIntent(android.content.Intent arg0);

    public abstract android.view.MenuItem setNumericShortcut(char arg0);

    public default android.view.MenuItem setNumericShortcut(char arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MenuItem;->setNumericShortcut(CI)Landroid/view/MenuItem;");
    }

    public abstract android.view.MenuItem setOnActionExpandListener(android.view.MenuItem.OnActionExpandListener arg0);

    public abstract android.view.MenuItem setOnMenuItemClickListener(android.view.MenuItem.OnMenuItemClickListener arg0);

    public abstract android.view.MenuItem setShortcut(char arg0, char arg1);

    public default android.view.MenuItem setShortcut(char arg0, char arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MenuItem;->setShortcut(CCII)Landroid/view/MenuItem;");
    }

    public abstract void setShowAsAction(int arg0);

    public abstract android.view.MenuItem setShowAsActionFlags(int arg0);

    public abstract android.view.MenuItem setTitle(int arg0);

    public abstract android.view.MenuItem setTitle(java.lang.CharSequence arg0);

    public abstract android.view.MenuItem setTitleCondensed(java.lang.CharSequence arg0);

    public default android.view.MenuItem setTooltipText(java.lang.CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/MenuItem;->setTooltipText(Ljava/lang/CharSequence;)Landroid/view/MenuItem;");
    }

    public abstract android.view.MenuItem setVisible(boolean arg0);
        public static interface OnActionExpandListener {

        public abstract boolean onMenuItemActionCollapse(android.view.MenuItem arg0);

        public abstract boolean onMenuItemActionExpand(android.view.MenuItem arg0);
        }
        public static interface OnMenuItemClickListener {

        public abstract boolean onMenuItemClick(android.view.MenuItem arg0);
        }
    }
