package android.view;

    public abstract class ActionMode {

    public static final int DEFAULT_HIDE_DURATION = -1;
    public static final int TYPE_FLOATING = 1;
    public static final int TYPE_PRIMARY = 0;

    public ActionMode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;-><init>()V");
    }

    public abstract void finish();

    public abstract android.view.View getCustomView();

    public abstract android.view.Menu getMenu();

    public abstract android.view.MenuInflater getMenuInflater();

    public abstract java.lang.CharSequence getSubtitle();

    public java.lang.Object getTag() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;->getTag()Ljava/lang/Object;");
    }

    public abstract java.lang.CharSequence getTitle();

    public boolean getTitleOptionalHint() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;->getTitleOptionalHint()Z");
    }

    public int getType() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;->getType()I");
    }

    public void hide(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;->hide(J)V");
    }

    public abstract void invalidate();

    public void invalidateContentRect() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;->invalidateContentRect()V");
    }

    public boolean isTitleOptional() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;->isTitleOptional()Z");
    }

    public void onWindowFocusChanged(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;->onWindowFocusChanged(Z)V");
    }

    public abstract void setCustomView(android.view.View arg0);

    public abstract void setSubtitle(int arg0);

    public abstract void setSubtitle(java.lang.CharSequence arg0);

    public void setTag(java.lang.Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;->setTag(Ljava/lang/Object;)V");
    }

    public abstract void setTitle(int arg0);

    public abstract void setTitle(java.lang.CharSequence arg0);

    public void setTitleOptionalHint(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;->setTitleOptionalHint(Z)V");
    }

    public void setType(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;->setType(I)V");
    }
        public static interface Callback {

        public abstract boolean onActionItemClicked(android.view.ActionMode arg0, android.view.MenuItem arg1);

        public abstract boolean onCreateActionMode(android.view.ActionMode arg0, android.view.Menu arg1);

        public abstract void onDestroyActionMode(android.view.ActionMode arg0);

        public abstract boolean onPrepareActionMode(android.view.ActionMode arg0, android.view.Menu arg1);
        }
    }
