package android.text;

import android.graphics.Paint;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * AOSP parity for TextPaint's extra paint state. Source:
 * aosp-mirror/platform_frameworks_base
 * core/java/android/text/TextPaint.java (field defaults, set(TextPaint),
 * setUnderlineText, custom underline thickness).
 */
class TextPaintTest {

    @Test
    void fieldDefaultsMatchAosp() {
        TextPaint paint = new TextPaint();
        assertEquals(1.0f, paint.density);
        assertEquals(0, paint.baselineShift);
        assertEquals(0, paint.bgColor);
        assertEquals(0, paint.linkColor);
        assertEquals(0, paint.underlineColor);
        assertEquals(0.0f, paint.underlineThickness);
        assertNull(paint.drawableState);
        // Paint defaults: black, anti-aliased, FILL, text size 0.
        assertEquals(0xFF000000, paint.getColor());
        assertEquals(Paint.Style.FILL, paint.getStyle());
        assertEquals(0.0f, paint.getTextSize());
    }

    @Test
    void setUnderlineTextStoresTheCustomUnderline() {
        TextPaint paint = new TextPaint();
        paint.setUnderlineText(0xFFFF0000, 2.5f);
        assertEquals(0xFFFF0000, paint.underlineColor);
        assertEquals(2.5f, paint.underlineThickness);
        // AOSP: a non-zero custom color makes the thickness accessor answer
        // the stored value instead of the Paint text metric.
        assertEquals(2.5f, paint.getUnderlineThickness());
    }

    @Test
    void setCopiesPaintAndTextPaintFields() {
        TextPaint source = new TextPaint();
        source.setColor(0xFF123456);
        source.setTextSize(18.0f);
        source.setFlags(Paint.ANTI_ALIAS_FLAG | Paint.UNDERLINE_TEXT_FLAG);
        source.baselineShift = 3;
        source.bgColor = 0xFF00FF00;
        source.linkColor = 0xFF0000FF;
        source.density = 2.0f;
        source.drawableState = new int[] { 1, 2 };
        source.underlineColor = 0xFFFF00FF;
        source.underlineThickness = 1.5f;

        TextPaint target = new TextPaint();
        target.set(source);
        assertEquals(source.getColor(), target.getColor());
        assertEquals(source.getTextSize(), target.getTextSize());
        assertEquals(source.getFlags(), target.getFlags());
        assertEquals(source.baselineShift, target.baselineShift);
        assertEquals(source.bgColor, target.bgColor);
        assertEquals(source.linkColor, target.linkColor);
        assertEquals(source.density, target.density);
        assertSame(source.drawableState, target.drawableState);
        assertEquals(source.underlineColor, target.underlineColor);
        assertEquals(source.underlineThickness, target.underlineThickness);

        // Mutating the source afterwards must not change the copy.
        source.bgColor = 0;
        assertEquals(0xFF00FF00, target.bgColor);
    }

    @Test
    void constructorsCopyThePaintState() {
        TextPaint source = new TextPaint();
        source.setColor(0xFFABCDEF);
        source.setTextSize(12.0f);

        TextPaint copy = new TextPaint(source);
        assertEquals(0xFFABCDEF, copy.getColor());
        assertEquals(12.0f, copy.getTextSize());

        TextPaint flagged = new TextPaint(Paint.UNDERLINE_TEXT_FLAG);
        assertEquals(Paint.UNDERLINE_TEXT_FLAG,
                flagged.getFlags() & Paint.UNDERLINE_TEXT_FLAG);
        assertEquals(1.0f, flagged.density);
    }
}
