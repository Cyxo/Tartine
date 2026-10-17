package android.text.style;

    public abstract class ClickableSpan extends android.text.style.CharacterStyle implements android.text.style.UpdateAppearance {

    public ClickableSpan() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/style/ClickableSpan;-><init>()V");
    }

    public abstract void onClick(android.view.View arg0);

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/style/ClickableSpan;->toString()Ljava/lang/String;");
    }

    public void updateDrawState(android.text.TextPaint arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/text/style/ClickableSpan;->updateDrawState(Landroid/text/TextPaint;)V");
    }
    }
