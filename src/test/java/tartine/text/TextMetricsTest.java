package tartine.text;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Paint;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Framework text metrics: AOSP line algebra (Layout.getLineBaseline is
 * getLineTop(line+1) - getLineDescent(line), includePad swaps ascent/descent
 * for top/bottom) and the live device configuration Compose derives its
 * Density from.
 */
class TextMetricsTest {

    /** TextPaint with fixed metrics so the assertions do not depend on the
     *  host's installed fonts. */
    private static final class FixedPaint extends TextPaint {
        @Override
        public float getTextSize() {
            return 16.0f;
        }

        @Override
        public Paint.FontMetrics getFontMetrics() {
            Paint.FontMetrics fm = new Paint.FontMetrics();
            fm.ascent = -15.0f;
            fm.descent = 4.0f;
            fm.top = -17.0f;
            fm.bottom = 5.0f;
            fm.leading = 0.0f;
            return fm;
        }

        @Override
        public float measureText(String text) {
            return text.length() * 6.0f;
        }
    }

    private static StaticLayout build(boolean includePad, float spacingAdd) {
        return StaticLayout.Builder
                .obtain("hi", 0, 2, new FixedPaint(), 100)
                .setIncludePad(includePad)
                .setLineSpacing(spacingAdd, 1.0f)
                .build();
    }

    @Test
    void noPaddingLineMetricsFollowAosp() {
        StaticLayout layout = build(false, 0.0f);
        assertEquals(1, layout.getLineCount());
        assertEquals(100, layout.getWidth());
        assertEquals(19, layout.getHeight());          // descent - ascent
        assertEquals(15, layout.getLineBaseline(0));   // -ascent
        assertEquals(4, layout.getLineDescent(0));
        assertEquals(-15, layout.getLineAscent(0));
        assertEquals(0, layout.getLineTop(0));
        assertEquals(19, layout.getLineTop(1));
        assertEquals(19, layout.getLineBottom(0));
    }

    @Test
    void includePadUsesTopAndBottom() {
        StaticLayout layout = build(true, 0.0f);
        assertEquals(22, layout.getHeight());          // bottom - top
        assertEquals(17, layout.getLineBaseline(0));   // -top
        assertEquals(5, layout.getLineDescent(0));
        assertEquals(-17, layout.getLineAscent(0));
    }

    @Test
    void lineSpacingFoldsIntoHeightNotBaseline() {
        StaticLayout layout = build(false, 6.0f);
        assertEquals(25, layout.getHeight());          // 19 + 6
        assertEquals(15, layout.getLineBaseline(0));   // baseline stays -ascent
        assertEquals(10, layout.getLineDescent(0));    // descent + extra
        assertEquals(6, layout.getLineExtra(0));
        // AOSP Layout.java:4368/4395: the spacing accessors read the stored
        // multiplier/addition (getSpacingAdd is an alias of getLineSpacingAmount).
        assertEquals(6.0f, layout.getSpacingAdd());
        assertEquals(6.0f, layout.getLineSpacingAmount());
        assertEquals(1.0f, layout.getSpacingMultiplier());
        assertEquals(1.0f, layout.getLineSpacingMultiplier());
    }

    @Test
    void emptyTextHasNoLines() {
        StaticLayout layout = StaticLayout.Builder
                .obtain("", 0, 0, new FixedPaint(), 100)
                .build();
        assertEquals(0, layout.getLineCount());
        assertEquals(0, layout.getHeight());
    }

    @Test
    void resourcesServeLiveDeviceConfiguration() {
        Resources resources = new Resources();
        Configuration config = resources.getConfiguration();
        // Compose's Density(context) reads exactly these two values; a zero
        // fontScale collapsed every sp text size to 0 px.
        assertEquals(1.0f, config.fontScale);
        assertEquals(160, config.densityDpi);
        assertEquals(480, config.screenWidthDp);
        assertEquals(960, config.screenHeightDp);
        assertEquals(480, config.smallestScreenWidthDp);
        assertEquals(Configuration.ORIENTATION_PORTRAIT, config.orientation);
        assertEquals(Configuration.UI_MODE_TYPE_NORMAL
                | Configuration.UI_MODE_NIGHT_NO, config.uiMode);

        DisplayMetrics metrics = resources.getDisplayMetrics();
        assertEquals(480, metrics.widthPixels);
        assertEquals(960, metrics.heightPixels);
        assertEquals(1.0f, metrics.density);
        assertEquals(metrics.density * config.fontScale, metrics.scaledDensity);
    }

    @Test
    void configurationSetToDefaultsMatchesAosp() {
        Configuration config = new Configuration();
        // AOSP's no-arg ctor runs unset(), which forces fontScale back to 0.
        assertEquals(0.0f, config.fontScale);
        config.setToDefaults();
        assertEquals(1.0f, config.fontScale);
        assertEquals(Configuration.DENSITY_DPI_UNDEFINED, config.densityDpi);
        assertEquals(Configuration.FONT_WEIGHT_ADJUSTMENT_UNDEFINED,
                config.fontWeightAdjustment);
    }
}
