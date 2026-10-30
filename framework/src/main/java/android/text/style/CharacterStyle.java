package android.text.style;

import android.text.TextPaint;

public class CharacterStyle {
    public CharacterStyle() {
    }

    public CharacterStyle getUnderlying() {
        return this;
    }

    public void updateDrawState(TextPaint arg0) {
    }

    public static CharacterStyle wrap(CharacterStyle arg0) {
        return arg0;
    }
}
