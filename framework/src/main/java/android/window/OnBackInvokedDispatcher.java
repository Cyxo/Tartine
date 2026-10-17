package android.window;

    public interface OnBackInvokedDispatcher {

    public static final int PRIORITY_DEFAULT = 0;
    public static final int PRIORITY_OVERLAY = 1000000;
    public static final int PRIORITY_SYSTEM_NAVIGATION_OBSERVER = -2;

    public abstract void registerOnBackInvokedCallback(int arg0, android.window.OnBackInvokedCallback arg1);

    public abstract void unregisterOnBackInvokedCallback(android.window.OnBackInvokedCallback arg0);
    }
