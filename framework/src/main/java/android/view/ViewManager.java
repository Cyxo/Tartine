package android.view;

    public interface ViewManager {

    public abstract void addView(View arg0, ViewGroup.LayoutParams arg1);

    public abstract void removeView(View arg0);

    public abstract void updateViewLayout(View arg0, ViewGroup.LayoutParams arg1);
    }
