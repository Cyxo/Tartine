package android.view;

    public class LayoutInflater {

    protected LayoutInflater(android.content.Context arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;-><init>(Landroid/content/Context;)V");
    }

    protected LayoutInflater(android.view.LayoutInflater arg0, android.content.Context arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;-><init>(Landroid/view/LayoutInflater;Landroid/content/Context;)V");
    }

    public android.view.LayoutInflater cloneInContext(android.content.Context arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->cloneInContext(Landroid/content/Context;)Landroid/view/LayoutInflater;");
    }

    public final android.view.View createView(android.content.Context arg0, java.lang.String arg1, java.lang.String arg2, android.util.AttributeSet arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->createView(Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;Landroid/util/AttributeSet;)Landroid/view/View;");
    }

    public final android.view.View createView(java.lang.String arg0, java.lang.String arg1, android.util.AttributeSet arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->createView(Ljava/lang/String;Ljava/lang/String;Landroid/util/AttributeSet;)Landroid/view/View;");
    }

    public static android.view.LayoutInflater from(android.content.Context arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->from(Landroid/content/Context;)Landroid/view/LayoutInflater;");
    }

    public android.content.Context getContext() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->getContext()Landroid/content/Context;");
    }

    public final android.view.LayoutInflater.Factory getFactory() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->getFactory()Landroid/view/LayoutInflater$Factory;");
    }

    public final android.view.LayoutInflater.Factory2 getFactory2() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->getFactory2()Landroid/view/LayoutInflater$Factory2;");
    }

    public android.view.LayoutInflater.Filter getFilter() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->getFilter()Landroid/view/LayoutInflater$Filter;");
    }

    public android.view.View inflate(int arg0, android.view.ViewGroup arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->inflate(ILandroid/view/ViewGroup;)Landroid/view/View;");
    }

    public android.view.View inflate(int arg0, android.view.ViewGroup arg1, boolean arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->inflate(ILandroid/view/ViewGroup;Z)Landroid/view/View;");
    }

    public android.view.View inflate(org.xmlpull.v1.XmlPullParser arg0, android.view.ViewGroup arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->inflate(Lorg/xmlpull/v1/XmlPullParser;Landroid/view/ViewGroup;)Landroid/view/View;");
    }

    public android.view.View inflate(org.xmlpull.v1.XmlPullParser arg0, android.view.ViewGroup arg1, boolean arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->inflate(Lorg/xmlpull/v1/XmlPullParser;Landroid/view/ViewGroup;Z)Landroid/view/View;");
    }

    public android.view.View onCreateView(android.content.Context arg0, android.view.View arg1, java.lang.String arg2, android.util.AttributeSet arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->onCreateView(Landroid/content/Context;Landroid/view/View;Ljava/lang/String;Landroid/util/AttributeSet;)Landroid/view/View;");
    }

    protected android.view.View onCreateView(android.view.View arg0, java.lang.String arg1, android.util.AttributeSet arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->onCreateView(Landroid/view/View;Ljava/lang/String;Landroid/util/AttributeSet;)Landroid/view/View;");
    }

    protected android.view.View onCreateView(java.lang.String arg0, android.util.AttributeSet arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->onCreateView(Ljava/lang/String;Landroid/util/AttributeSet;)Landroid/view/View;");
    }

    public void setFactory(android.view.LayoutInflater.Factory arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->setFactory(Landroid/view/LayoutInflater$Factory;)V");
    }

    public void setFactory2(android.view.LayoutInflater.Factory2 arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->setFactory2(Landroid/view/LayoutInflater$Factory2;)V");
    }

    public void setFilter(android.view.LayoutInflater.Filter arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->setFilter(Landroid/view/LayoutInflater$Filter;)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public LayoutInflater() {
        }
        public static interface Factory2 extends android.view.LayoutInflater.Factory {

        public abstract android.view.View onCreateView(android.view.View arg0, java.lang.String arg1, android.content.Context arg2, android.util.AttributeSet arg3);
        }
        public static interface Factory {

        public abstract android.view.View onCreateView(java.lang.String arg0, android.content.Context arg1, android.util.AttributeSet arg2);
        }
        public static interface Filter {

        public abstract boolean onLoadClass(java.lang.Class arg0);
        }
    }
