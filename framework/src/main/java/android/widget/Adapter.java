package android.widget;

    public interface Adapter {

    public static final int IGNORE_ITEM_VIEW_TYPE = -1;
    public static final int NO_SELECTION = -2147483648;

    public default java.lang.CharSequence[] getAutofillOptions() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/widget/Adapter;->getAutofillOptions()[Ljava/lang/CharSequence;");
    }

    public abstract int getCount();

    public abstract java.lang.Object getItem(int arg0);

    public abstract long getItemId(int arg0);

    public abstract int getItemViewType(int arg0);

    public abstract android.view.View getView(int arg0, android.view.View arg1, android.view.ViewGroup arg2);

    public abstract int getViewTypeCount();

    public abstract boolean hasStableIds();

    public abstract boolean isEmpty();

    public abstract void registerDataSetObserver(android.database.DataSetObserver arg0);

    public abstract void unregisterDataSetObserver(android.database.DataSetObserver arg0);
    }
