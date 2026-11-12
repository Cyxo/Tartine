package android.app;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Tartine: a dialog window shell, ported minimally from AOSP
 * {@code android.app.Dialog}. A device dialog owns a {@code PhoneWindow}; the
 * shell pairs it with the same flattened {@link Window} an activity uses, so
 * AppCompat/ComponentDialog subclasses that build their content through
 * {@code getWindow()} see a decor and a content container.
 *
 * <p>There is no host dialog chrome yet: {@link #show()} runs the lifecycle
 * callbacks and marks the dialog showing, and {@link #dismiss()} runs the
 * cancel/dismiss callbacks, but nothing is presented in a separate window.
 */
public class Dialog implements DialogInterface {

    private final Context mContext;
    private final Window mWindow;

    private boolean mCreated;
    private boolean mShowing;
    private boolean mCancelable = true;
    private boolean mCanceled;
    private CharSequence mTitle;

    private DialogInterface.OnCancelListener mOnCancelListener;
    private DialogInterface.OnDismissListener mOnDismissListener;
    private DialogInterface.OnShowListener mOnShowListener;

    /** AOSP Dialog(Context): a theme-less dialog. */
    public Dialog(Context context) {
        this(context, 0);
    }

    /**
     * AOSP Dialog(Context, int): the theme id applies to the dialog's own
     * themed context; the shell keeps the plain context (theme overlays land
     * with the dialog chrome layer) but validates like AOSP.
     */
    public Dialog(Context context, int themeResId) {
        this(context, themeResId, true);
    }

    /**
     * AOSP Dialog(Context, int, boolean): {@code createContextThemeWrapper}
     * wraps the context; the shell stores the caller's context and builds
     * the window from it.
     */
    Dialog(Context context, int themeResId, boolean createContextThemeWrapper) {
        mContext = context;
        mWindow = new Window(context);
    }

    /** AOSP Dialog(Context, boolean, OnCancelListener). */
    protected Dialog(Context context, boolean cancelable,
            DialogInterface.OnCancelListener cancelListener) {
        this(context, 0, true);
        mCancelable = cancelable;
        mOnCancelListener = cancelListener;
    }

    public Context getContext() {
        return mContext;
    }

    public Window getWindow() {
        return mWindow;
    }

    public View getCurrentFocus() {
        return mWindow.getDecorView();
    }

    public Bundle onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putString("android:dialog:title", mTitle == null ? null : mTitle.toString());
        return bundle;
    }

    protected void onCreate(Bundle savedInstanceState) {
        // no content of its own; subclasses inflate their layout
    }

    protected void onStart() {
    }

    protected void onStop() {
    }

    /** AOSP Dialog.show(): create, start and mark showing. */
    public void show() {
        if (!mCreated) {
            onCreate(null);
            mCreated = true;
        }
        onStart();
        mShowing = true;
        if (mOnShowListener != null) {
            mOnShowListener.onShow(this);
        }
    }

    public void hide() {
        if (mShowing) {
            onStop();
            mShowing = false;
        }
    }

    /** AOSP Dialog.dismiss(): run callbacks, then stop the dialog. */
    public void dismiss() {
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss(this);
        }
        hide();
        mCreated = false;
    }

    /** AOSP Dialog.dismissDialog is package-private; kept for API parity. */
    void dismissDialog() {
        dismiss();
    }

    public void setCancelable(boolean flag) {
        mCancelable = flag;
    }

    public void setCanceledOnTouchOutside(boolean cancel) {
        if (cancel && !mCancelable) {
            mCancelable = true;
        }
    }

    public boolean isShowing() {
        return mShowing;
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener listener) {
        mOnCancelListener = listener;
        mCanceled = false;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        mOnDismissListener = listener;
    }

    public void setOnShowListener(DialogInterface.OnShowListener listener) {
        mOnShowListener = listener;
    }

    /** AOSP Dialog.cancel(): only a cancelable dialog may be canceled. */
    public void cancel() {
        if (!mCanceled && mOnCancelListener != null) {
            mCanceled = true;
            mOnCancelListener.onCancel(this);
        }
        if (mCanceled) {
            dismiss();
        }
    }

    protected void onCancel() {
    }

    /** AOSP Dialog.onBackPressed: cancel when cancelable, else nothing. */
    public void onBackPressed() {
        if (mCancelable) {
            cancel();
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return event != null && event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_UP && dispatchBack();
    }

    private boolean dispatchBack() {
        onBackPressed();
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }

    public void setTitle(CharSequence title) {
        mTitle = title;
    }

    public void setTitle(int titleId) {
        setTitle(mContext.getText(titleId));
    }

    public CharSequence getTitle() {
        return mTitle;
    }

    public void setContentView(int layoutResID) {
        setContentView(LayoutInflater.from(mContext).inflate(layoutResID, null));
    }

    public void setContentView(View view) {
        mWindow.setContentView(view);
    }

    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mWindow.setContentView(view, params);
    }

    public void addContentView(View view, ViewGroup.LayoutParams params) {
        View decor = mWindow.getDecorView();
        if (decor instanceof ViewGroup) {
            ((ViewGroup) decor).addView(view, params);
        }
    }

    public View findViewById(int id) {
        return mWindow.getDecorView().findViewById(id);
    }

    /** AOSP Dialog.closeOptionsMenu / openOptionsMenu: no menu layer yet. */
    public void closeOptionsMenu() {
    }

    public void openOptionsMenu() {
    }

    public void setVolumeControlStream(int streamType) {
    }

    public int getVolumeControlStream() {
        return 0;
    }

    /** AOSP Dialog.setCancelMessage/setDismissMessage: handler plumbing only. */
    public void setCancelMessage(android.os.Message msg) {
    }

    public void setDismissMessage(android.os.Message msg) {
    }

    /** AOSP Dialog.getWindow().getAttributes() convenience for subclasses. */
    public WindowManager.LayoutParams getWindowAttributes() {
        return mWindow.getAttributes();
    }

    /** AOSP Dialog.OnDismissListener (Dialog-scoped, distinct from the interface one). */
    public interface OnDismissListener {
        void onDismiss(DialogInterface dialog);
    }

    /** AOSP Dialog.OnShowListener (Dialog-scoped). */
    public interface OnShowListener {
        void onShow(DialogInterface dialog);
    }

    /** AOSP Dialog.OnCancelListener (Dialog-scoped). */
    public interface OnCancelListener {
        void onCancel(DialogInterface dialog);
    }
}
