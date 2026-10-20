package android.content;

    public interface DialogInterface {

    public static final int BUTTON1 = -1;
    public static final int BUTTON2 = -2;
    public static final int BUTTON3 = -3;
    public static final int BUTTON_NEGATIVE = -2;
    public static final int BUTTON_NEUTRAL = -3;
    public static final int BUTTON_POSITIVE = -1;

    public abstract void cancel();

    public abstract void dismiss();
        public static interface OnCancelListener {

        public abstract void onCancel(android.content.DialogInterface arg0);
        }
        public static interface OnDismissListener {

        public abstract void onDismiss(android.content.DialogInterface arg0);
        }
        public static interface OnKeyListener {

        public abstract boolean onKey(android.content.DialogInterface arg0, int arg1, android.view.KeyEvent arg2);
        }
        public static interface OnShowListener {

        public abstract void onShow(android.content.DialogInterface arg0);
        }
    }
