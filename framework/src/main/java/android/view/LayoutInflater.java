package android.view;

import android.content.Context;
import android.util.AttributeSet;
import org.xmlpull.v1.XmlPullParser;

    public class LayoutInflater {

    protected LayoutInflater(Context arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;-><init>(Landroid/content/Context;)V");
    }

    protected LayoutInflater(LayoutInflater arg0, Context arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;-><init>(Landroid/view/LayoutInflater;Landroid/content/Context;)V");
    }

    public LayoutInflater cloneInContext(Context arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->cloneInContext(Landroid/content/Context;)Landroid/view/LayoutInflater;");
    }

    public final View createView(Context arg0, String arg1, String arg2, AttributeSet arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->createView(Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;Landroid/util/AttributeSet;)Landroid/view/View;");
    }

    public final View createView(String arg0, String arg1, AttributeSet arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->createView(Ljava/lang/String;Ljava/lang/String;Landroid/util/AttributeSet;)Landroid/view/View;");
    }

    public static LayoutInflater from(Context arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->from(Landroid/content/Context;)Landroid/view/LayoutInflater;");
    }

    public Context getContext() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->getContext()Landroid/content/Context;");
    }

    public final LayoutInflater.Factory getFactory() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->getFactory()Landroid/view/LayoutInflater$Factory;");
    }

    public final LayoutInflater.Factory2 getFactory2() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->getFactory2()Landroid/view/LayoutInflater$Factory2;");
    }

    public LayoutInflater.Filter getFilter() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->getFilter()Landroid/view/LayoutInflater$Filter;");
    }

    public View inflate(int arg0, ViewGroup arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->inflate(ILandroid/view/ViewGroup;)Landroid/view/View;");
    }

    public View inflate(int arg0, ViewGroup arg1, boolean arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->inflate(ILandroid/view/ViewGroup;Z)Landroid/view/View;");
    }

    public View inflate(XmlPullParser arg0, ViewGroup arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->inflate(Lorg/xmlpull/v1/XmlPullParser;Landroid/view/ViewGroup;)Landroid/view/View;");
    }

    public View inflate(XmlPullParser arg0, ViewGroup arg1, boolean arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->inflate(Lorg/xmlpull/v1/XmlPullParser;Landroid/view/ViewGroup;Z)Landroid/view/View;");
    }

    public View onCreateView(Context arg0, View arg1, String arg2, AttributeSet arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->onCreateView(Landroid/content/Context;Landroid/view/View;Ljava/lang/String;Landroid/util/AttributeSet;)Landroid/view/View;");
    }

    protected View onCreateView(View arg0, String arg1, AttributeSet arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->onCreateView(Landroid/view/View;Ljava/lang/String;Landroid/util/AttributeSet;)Landroid/view/View;");
    }

    protected View onCreateView(String arg0, AttributeSet arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->onCreateView(Ljava/lang/String;Landroid/util/AttributeSet;)Landroid/view/View;");
    }

    public void setFactory(LayoutInflater.Factory arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->setFactory(Landroid/view/LayoutInflater$Factory;)V");
    }

    public void setFactory2(LayoutInflater.Factory2 arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->setFactory2(Landroid/view/LayoutInflater$Factory2;)V");
    }

    public void setFilter(LayoutInflater.Filter arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/LayoutInflater;->setFilter(Landroid/view/LayoutInflater$Filter;)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public LayoutInflater() {
        }
        public static interface Factory2 extends LayoutInflater.Factory {

        public abstract View onCreateView(View arg0, String arg1, Context arg2, AttributeSet arg3);
        }
        public static interface Factory {

        public abstract View onCreateView(String arg0, Context arg1, AttributeSet arg2);
        }
        public static interface Filter {

        public abstract boolean onLoadClass(Class arg0);
        }
    }
