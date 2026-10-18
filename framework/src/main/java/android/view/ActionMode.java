package android.view;

    public class ActionMode {

    public static final int DEFAULT_HIDE_DURATION = -1;
    public static final int TYPE_FLOATING = 1;
    public static final int TYPE_PRIMARY = 0;

    public ActionMode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;-><init>()V");
    }

    public void finish() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;->finish()V");
    }

    public android.view.View getCustomView() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;->getCustomView()Landroid/view/View;");
    }

    public android.view.Menu getMenu() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;->getMenu()Landroid/view/Menu;");
    }

    public android.view.MenuInflater getMenuInflater() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;->getMenuInflater()Landroid/view/MenuInflater;");
    }

    public java.lang.CharSequence getSubtitle() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;->getSubtitle()Ljava/lang/CharSequence;");
    }

    public java.lang.Object getTag() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;->getTag()Ljava/lang/Object;");
    }

    public java.lang.CharSequence getTitle() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;->getTitle()Ljava/lang/CharSequence;");
    }

    public boolean getTitleOptionalHint() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;->getTitleOptionalHint()Z");
    }

    public int getType() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;->getType()I");
    }

    public void hide(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;->hide(J)V");
    }

    public void invalidate() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;->invalidate()V");
    }

    public void invalidateContentRect() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;->invalidateContentRect()V");
    }

    public boolean isTitleOptional() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;->isTitleOptional()Z");
    }

    public void onWindowFocusChanged(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;->onWindowFocusChanged(Z)V");
    }

    public void setCustomView(android.view.View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;->setCustomView(Landroid/view/View;)V");
    }

    public void setSubtitle(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;->setSubtitle(I)V");
    }

    public void setSubtitle(java.lang.CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;->setSubtitle(Ljava/lang/CharSequence;)V");
    }

    public void setTag(java.lang.Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;->setTag(Ljava/lang/Object;)V");
    }

    public void setTitle(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;->setTitle(I)V");
    }

    public void setTitle(java.lang.CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionMode;->setTitle(Ljava/lang/CharSequence;)V");
    }

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
