package android.view;

    public class ActionProvider {

    public ActionProvider(android.content.Context arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionProvider;-><init>(Landroid/content/Context;)V");
    }

    public boolean hasSubMenu() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionProvider;->hasSubMenu()Z");
    }

    public boolean isVisible() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionProvider;->isVisible()Z");
    }

    public android.view.View onCreateActionView() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionProvider;->onCreateActionView()Landroid/view/View;");
    }

    public android.view.View onCreateActionView(android.view.MenuItem arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionProvider;->onCreateActionView(Landroid/view/MenuItem;)Landroid/view/View;");
    }

    public boolean onPerformDefaultAction() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionProvider;->onPerformDefaultAction()Z");
    }

    public void onPrepareSubMenu(android.view.SubMenu arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionProvider;->onPrepareSubMenu(Landroid/view/SubMenu;)V");
    }

    public boolean overridesItemVisibility() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionProvider;->overridesItemVisibility()Z");
    }

    public void refreshVisibility() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionProvider;->refreshVisibility()V");
    }

    public void setVisibilityListener(android.view.ActionProvider.VisibilityListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/ActionProvider;->setVisibilityListener(Landroid/view/ActionProvider$VisibilityListener;)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public ActionProvider() {
        }
        public static interface VisibilityListener {

        public abstract void onActionProviderVisibilityChanged(boolean arg0);
        }
    }
