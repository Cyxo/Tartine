package android.view;

    public interface WindowInsetsAnimationControlListener {

    public abstract void onCancelled(WindowInsetsAnimationController arg0);

    public abstract void onFinished(WindowInsetsAnimationController arg0);

    public abstract void onReady(WindowInsetsAnimationController arg0, int arg1);
    }
