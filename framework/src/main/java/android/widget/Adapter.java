package android.widget;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

    public interface Adapter {

    public static final int IGNORE_ITEM_VIEW_TYPE = -1;
    public static final int NO_SELECTION = -2147483648;

    public default CharSequence[] getAutofillOptions() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/widget/Adapter;->getAutofillOptions()[Ljava/lang/CharSequence;");
    }

    public abstract int getCount();

    public abstract Object getItem(int arg0);

    public abstract long getItemId(int arg0);

    public abstract int getItemViewType(int arg0);

    public abstract View getView(int arg0, View arg1, ViewGroup arg2);

    public abstract int getViewTypeCount();

    public abstract boolean hasStableIds();

    public abstract boolean isEmpty();

    public abstract void registerDataSetObserver(DataSetObserver arg0);

    public abstract void unregisterDataSetObserver(DataSetObserver arg0);
    }
