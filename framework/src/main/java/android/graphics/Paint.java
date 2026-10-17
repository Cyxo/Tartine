package android.graphics;

    public class Paint {

    public static final int ANTI_ALIAS_FLAG = 1;
    public static final int CURSOR_AFTER = 0;
    public static final int CURSOR_AT = 4;
    public static final int CURSOR_AT_OR_AFTER = 1;
    public static final int CURSOR_AT_OR_BEFORE = 3;
    public static final int CURSOR_BEFORE = 2;
    public static final int DEV_KERN_TEXT_FLAG = 256;
    public static final int DITHER_FLAG = 4;
    public static final int EMBEDDED_BITMAP_TEXT_FLAG = 1024;
    public static final int END_HYPHEN_EDIT_INSERT_ARMENIAN_HYPHEN = 3;
    public static final int END_HYPHEN_EDIT_INSERT_HYPHEN = 2;
    public static final int END_HYPHEN_EDIT_INSERT_MAQAF = 4;
    public static final int END_HYPHEN_EDIT_INSERT_UCAS_HYPHEN = 5;
    public static final int END_HYPHEN_EDIT_INSERT_ZWJ_AND_HYPHEN = 6;
    public static final int END_HYPHEN_EDIT_NO_EDIT = 0;
    public static final int END_HYPHEN_EDIT_REPLACE_WITH_HYPHEN = 1;
    public static final int FAKE_BOLD_TEXT_FLAG = 32;
    public static final int FILTER_BITMAP_FLAG = 2;
    public static final int HINTING_OFF = 0;
    public static final int HINTING_ON = 1;
    public static final int LINEAR_TEXT_FLAG = 64;
    public static final int START_HYPHEN_EDIT_INSERT_HYPHEN = 1;
    public static final int START_HYPHEN_EDIT_INSERT_ZWJ = 2;
    public static final int START_HYPHEN_EDIT_NO_EDIT = 0;
    public static final int STRIKE_THRU_TEXT_FLAG = 16;
    public static final int SUBPIXEL_TEXT_FLAG = 128;
    public static final int TEXT_RUN_FLAG_LEFT_EDGE = 8192;
    public static final int TEXT_RUN_FLAG_RIGHT_EDGE = 16384;
    public static final int UNDERLINE_TEXT_FLAG = 8;
    public static final int VERTICAL_TEXT_FLAG = 4096;

    public Paint() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;-><init>()V");
    }

    public Paint(android.graphics.Paint arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;-><init>(Landroid/graphics/Paint;)V");
    }

    public Paint(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;-><init>(I)V");
    }

    public float ascent() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->ascent()F");
    }

    public int breakText(char[] arg0, int arg1, int arg2, float arg3, float[] arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->breakText([CIIF[F)I");
    }

    public int breakText(java.lang.CharSequence arg0, int arg1, int arg2, boolean arg3, float arg4, float[] arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->breakText(Ljava/lang/CharSequence;IIZF[F)I");
    }

    public int breakText(java.lang.String arg0, boolean arg1, float arg2, float[] arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->breakText(Ljava/lang/String;ZF[F)I");
    }

    public void clearShadowLayer() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->clearShadowLayer()V");
    }

    public float descent() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->descent()F");
    }

    public boolean equalsForTextMeasurement(android.graphics.Paint arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->equalsForTextMeasurement(Landroid/graphics/Paint;)Z");
    }

    public int getAlpha() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getAlpha()I");
    }

    public android.graphics.BlendMode getBlendMode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getBlendMode()Landroid/graphics/BlendMode;");
    }

    public int getColor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getColor()I");
    }

    public android.graphics.ColorFilter getColorFilter() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getColorFilter()Landroid/graphics/ColorFilter;");
    }

    public long getColorLong() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getColorLong()J");
    }

    public int getEndHyphenEdit() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getEndHyphenEdit()I");
    }

    public boolean getFillPath(android.graphics.Path arg0, android.graphics.Path arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getFillPath(Landroid/graphics/Path;Landroid/graphics/Path;)Z");
    }

    public int getFlags() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getFlags()I");
    }

    public java.lang.String getFontFeatureSettings() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getFontFeatureSettings()Ljava/lang/String;");
    }

    public android.graphics.Paint.FontMetrics getFontMetrics() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getFontMetrics()Landroid/graphics/Paint$FontMetrics;");
    }

    public float getFontMetrics(android.graphics.Paint.FontMetrics arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getFontMetrics(Landroid/graphics/Paint$FontMetrics;)F");
    }

    public void getFontMetricsForLocale(android.graphics.Paint.FontMetrics arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getFontMetricsForLocale(Landroid/graphics/Paint$FontMetrics;)V");
    }

    public android.graphics.Paint.FontMetricsInt getFontMetricsInt() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getFontMetricsInt()Landroid/graphics/Paint$FontMetricsInt;");
    }

    public int getFontMetricsInt(android.graphics.Paint.FontMetricsInt arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getFontMetricsInt(Landroid/graphics/Paint$FontMetricsInt;)I");
    }

    public void getFontMetricsInt(char[] arg0, int arg1, int arg2, int arg3, int arg4, boolean arg5, android.graphics.Paint.FontMetricsInt arg6) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getFontMetricsInt([CIIIIZLandroid/graphics/Paint$FontMetricsInt;)V");
    }

    public void getFontMetricsInt(java.lang.CharSequence arg0, int arg1, int arg2, int arg3, int arg4, boolean arg5, android.graphics.Paint.FontMetricsInt arg6) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getFontMetricsInt(Ljava/lang/CharSequence;IIIIZLandroid/graphics/Paint$FontMetricsInt;)V");
    }

    public void getFontMetricsIntForLocale(android.graphics.Paint.FontMetricsInt arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getFontMetricsIntForLocale(Landroid/graphics/Paint$FontMetricsInt;)V");
    }

    public float getFontSpacing() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getFontSpacing()F");
    }

    public java.lang.String getFontVariationSettings() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getFontVariationSettings()Ljava/lang/String;");
    }

    public int getHinting() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getHinting()I");
    }

    public float getLetterSpacing() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getLetterSpacing()F");
    }

    public android.graphics.MaskFilter getMaskFilter() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getMaskFilter()Landroid/graphics/MaskFilter;");
    }

    public int getOffsetForAdvance(char[] arg0, int arg1, int arg2, int arg3, int arg4, boolean arg5, float arg6) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getOffsetForAdvance([CIIIIZF)I");
    }

    public int getOffsetForAdvance(java.lang.CharSequence arg0, int arg1, int arg2, int arg3, int arg4, boolean arg5, float arg6) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getOffsetForAdvance(Ljava/lang/CharSequence;IIIIZF)I");
    }

    public android.graphics.PathEffect getPathEffect() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getPathEffect()Landroid/graphics/PathEffect;");
    }

    public float getRunAdvance(char[] arg0, int arg1, int arg2, int arg3, int arg4, boolean arg5, int arg6) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getRunAdvance([CIIIIZI)F");
    }

    public float getRunAdvance(java.lang.CharSequence arg0, int arg1, int arg2, int arg3, int arg4, boolean arg5, int arg6) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getRunAdvance(Ljava/lang/CharSequence;IIIIZI)F");
    }

    public float getRunCharacterAdvance(char[] arg0, int arg1, int arg2, int arg3, int arg4, boolean arg5, int arg6, float[] arg7, int arg8) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getRunCharacterAdvance([CIIIIZI[FI)F");
    }

    public float getRunCharacterAdvance(java.lang.CharSequence arg0, int arg1, int arg2, int arg3, int arg4, boolean arg5, int arg6, float[] arg7, int arg8) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getRunCharacterAdvance(Ljava/lang/CharSequence;IIIIZI[FI)F");
    }

    public android.graphics.Shader getShader() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getShader()Landroid/graphics/Shader;");
    }

    public int getShadowLayerColor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getShadowLayerColor()I");
    }

    public long getShadowLayerColorLong() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getShadowLayerColorLong()J");
    }

    public float getShadowLayerDx() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getShadowLayerDx()F");
    }

    public float getShadowLayerDy() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getShadowLayerDy()F");
    }

    public float getShadowLayerRadius() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getShadowLayerRadius()F");
    }

    public int getStartHyphenEdit() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getStartHyphenEdit()I");
    }

    public float getStrikeThruPosition() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getStrikeThruPosition()F");
    }

    public float getStrikeThruThickness() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getStrikeThruThickness()F");
    }

    public android.graphics.Paint.Cap getStrokeCap() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getStrokeCap()Landroid/graphics/Paint$Cap;");
    }

    public android.graphics.Paint.Join getStrokeJoin() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getStrokeJoin()Landroid/graphics/Paint$Join;");
    }

    public float getStrokeMiter() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getStrokeMiter()F");
    }

    public float getStrokeWidth() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getStrokeWidth()F");
    }

    public android.graphics.Paint.Style getStyle() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getStyle()Landroid/graphics/Paint$Style;");
    }

    public android.graphics.Paint.Align getTextAlign() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getTextAlign()Landroid/graphics/Paint$Align;");
    }

    public void getTextBounds(char[] arg0, int arg1, int arg2, android.graphics.Rect arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getTextBounds([CIILandroid/graphics/Rect;)V");
    }

    public void getTextBounds(java.lang.CharSequence arg0, int arg1, int arg2, android.graphics.Rect arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getTextBounds(Ljava/lang/CharSequence;IILandroid/graphics/Rect;)V");
    }

    public void getTextBounds(java.lang.String arg0, int arg1, int arg2, android.graphics.Rect arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getTextBounds(Ljava/lang/String;IILandroid/graphics/Rect;)V");
    }

    public java.util.Locale getTextLocale() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getTextLocale()Ljava/util/Locale;");
    }

    public android.os.LocaleList getTextLocales() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getTextLocales()Landroid/os/LocaleList;");
    }

    public void getTextPath(char[] arg0, int arg1, int arg2, float arg3, float arg4, android.graphics.Path arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getTextPath([CIIFFLandroid/graphics/Path;)V");
    }

    public void getTextPath(java.lang.String arg0, int arg1, int arg2, float arg3, float arg4, android.graphics.Path arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getTextPath(Ljava/lang/String;IIFFLandroid/graphics/Path;)V");
    }

    public float getTextRunAdvances(char[] arg0, int arg1, int arg2, int arg3, int arg4, boolean arg5, float[] arg6, int arg7) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getTextRunAdvances([CIIIIZ[FI)F");
    }

    public int getTextRunCursor(char[] arg0, int arg1, int arg2, boolean arg3, int arg4, int arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getTextRunCursor([CIIZII)I");
    }

    public int getTextRunCursor(java.lang.CharSequence arg0, int arg1, int arg2, boolean arg3, int arg4, int arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getTextRunCursor(Ljava/lang/CharSequence;IIZII)I");
    }

    public float getTextScaleX() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getTextScaleX()F");
    }

    public float getTextSize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getTextSize()F");
    }

    public float getTextSkewX() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getTextSkewX()F");
    }

    public int getTextWidths(char[] arg0, int arg1, int arg2, float[] arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getTextWidths([CII[F)I");
    }

    public int getTextWidths(java.lang.CharSequence arg0, int arg1, int arg2, float[] arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getTextWidths(Ljava/lang/CharSequence;II[F)I");
    }

    public int getTextWidths(java.lang.String arg0, float[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getTextWidths(Ljava/lang/String;[F)I");
    }

    public int getTextWidths(java.lang.String arg0, int arg1, int arg2, float[] arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getTextWidths(Ljava/lang/String;II[F)I");
    }

    public android.graphics.Typeface getTypeface() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getTypeface()Landroid/graphics/Typeface;");
    }

    public float getUnderlinePosition() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getUnderlinePosition()F");
    }

    public float getUnderlineThickness() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getUnderlineThickness()F");
    }

    public float getWordSpacing() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getWordSpacing()F");
    }

    public android.graphics.Xfermode getXfermode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getXfermode()Landroid/graphics/Xfermode;");
    }

    public boolean hasGlyph(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->hasGlyph(Ljava/lang/String;)Z");
    }

    public final boolean isAntiAlias() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->isAntiAlias()Z");
    }

    public final boolean isDither() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->isDither()Z");
    }

    public boolean isElegantTextHeight() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->isElegantTextHeight()Z");
    }

    public final boolean isFakeBoldText() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->isFakeBoldText()Z");
    }

    public final boolean isFilterBitmap() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->isFilterBitmap()Z");
    }

    public final boolean isLinearText() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->isLinearText()Z");
    }

    public final boolean isStrikeThruText() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->isStrikeThruText()Z");
    }

    public final boolean isSubpixelText() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->isSubpixelText()Z");
    }

    public final boolean isUnderlineText() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->isUnderlineText()Z");
    }

    public float measureText(char[] arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->measureText([CII)F");
    }

    public float measureText(java.lang.CharSequence arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->measureText(Ljava/lang/CharSequence;II)F");
    }

    public float measureText(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->measureText(Ljava/lang/String;)F");
    }

    public float measureText(java.lang.String arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->measureText(Ljava/lang/String;II)F");
    }

    public void reset() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->reset()V");
    }

    public void set(android.graphics.Paint arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->set(Landroid/graphics/Paint;)V");
    }

    public void setARGB(int arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setARGB(IIII)V");
    }

    public void setAlpha(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setAlpha(I)V");
    }

    public void setAntiAlias(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setAntiAlias(Z)V");
    }

    public void setBlendMode(android.graphics.BlendMode arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setBlendMode(Landroid/graphics/BlendMode;)V");
    }

    public void setColor(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setColor(I)V");
    }

    public void setColor(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setColor(J)V");
    }

    public android.graphics.ColorFilter setColorFilter(android.graphics.ColorFilter arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setColorFilter(Landroid/graphics/ColorFilter;)Landroid/graphics/ColorFilter;");
    }

    public void setDither(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setDither(Z)V");
    }

    public void setElegantTextHeight(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setElegantTextHeight(Z)V");
    }

    public void setEndHyphenEdit(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setEndHyphenEdit(I)V");
    }

    public void setFakeBoldText(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setFakeBoldText(Z)V");
    }

    public void setFilterBitmap(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setFilterBitmap(Z)V");
    }

    public void setFlags(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setFlags(I)V");
    }

    public void setFontFeatureSettings(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setFontFeatureSettings(Ljava/lang/String;)V");
    }

    public boolean setFontVariationSettings(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setFontVariationSettings(Ljava/lang/String;)Z");
    }

    public void setHinting(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setHinting(I)V");
    }

    public void setLetterSpacing(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setLetterSpacing(F)V");
    }

    public void setLinearText(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setLinearText(Z)V");
    }

    public android.graphics.MaskFilter setMaskFilter(android.graphics.MaskFilter arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setMaskFilter(Landroid/graphics/MaskFilter;)Landroid/graphics/MaskFilter;");
    }

    public android.graphics.PathEffect setPathEffect(android.graphics.PathEffect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setPathEffect(Landroid/graphics/PathEffect;)Landroid/graphics/PathEffect;");
    }

    public android.graphics.Shader setShader(android.graphics.Shader arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setShader(Landroid/graphics/Shader;)Landroid/graphics/Shader;");
    }

    public void setShadowLayer(float arg0, float arg1, float arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setShadowLayer(FFFI)V");
    }

    public void setShadowLayer(float arg0, float arg1, float arg2, long arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setShadowLayer(FFFJ)V");
    }

    public void setStartHyphenEdit(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setStartHyphenEdit(I)V");
    }

    public void setStrikeThruText(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setStrikeThruText(Z)V");
    }

    public void setStrokeCap(android.graphics.Paint.Cap arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setStrokeCap(Landroid/graphics/Paint$Cap;)V");
    }

    public void setStrokeJoin(android.graphics.Paint.Join arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setStrokeJoin(Landroid/graphics/Paint$Join;)V");
    }

    public void setStrokeMiter(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setStrokeMiter(F)V");
    }

    public void setStrokeWidth(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setStrokeWidth(F)V");
    }

    public void setStyle(android.graphics.Paint.Style arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setStyle(Landroid/graphics/Paint$Style;)V");
    }

    public void setSubpixelText(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setSubpixelText(Z)V");
    }

    public void setTextAlign(android.graphics.Paint.Align arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setTextAlign(Landroid/graphics/Paint$Align;)V");
    }

    public void setTextLocale(java.util.Locale arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setTextLocale(Ljava/util/Locale;)V");
    }

    public void setTextLocales(android.os.LocaleList arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setTextLocales(Landroid/os/LocaleList;)V");
    }

    public void setTextScaleX(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setTextScaleX(F)V");
    }

    public void setTextSize(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setTextSize(F)V");
    }

    public void setTextSkewX(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setTextSkewX(F)V");
    }

    public android.graphics.Typeface setTypeface(android.graphics.Typeface arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setTypeface(Landroid/graphics/Typeface;)Landroid/graphics/Typeface;");
    }

    public void setUnderlineText(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setUnderlineText(Z)V");
    }

    public void setWordSpacing(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setWordSpacing(F)V");
    }

    public android.graphics.Xfermode setXfermode(android.graphics.Xfermode arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setXfermode(Landroid/graphics/Xfermode;)Landroid/graphics/Xfermode;");
    }
        public static enum Align {
            CENTER, LEFT, RIGHT;
        }
        public static enum Cap {
            BUTT, ROUND, SQUARE;
        }
        public static class FontMetrics {

        public float ascent;
        public float bottom;
        public float descent;
        public float leading;
        public float top;

        public FontMetrics() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint$FontMetrics;-><init>()V");
        }

        public boolean equals(java.lang.Object arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint$FontMetrics;->equals(Ljava/lang/Object;)Z");
        }

        public int hashCode() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint$FontMetrics;->hashCode()I");
        }

        public java.lang.String toString() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint$FontMetrics;->toString()Ljava/lang/String;");
        }
        }
        public static class FontMetricsInt {

        public int ascent;
        public int bottom;
        public int descent;
        public int leading;
        public int top;

        public FontMetricsInt() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint$FontMetricsInt;-><init>()V");
        }

        public boolean equals(java.lang.Object arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint$FontMetricsInt;->equals(Ljava/lang/Object;)Z");
        }

        public int hashCode() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint$FontMetricsInt;->hashCode()I");
        }

        public void set(android.graphics.Paint.FontMetrics arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint$FontMetricsInt;->set(Landroid/graphics/Paint$FontMetrics;)V");
        }

        public void set(android.graphics.Paint.FontMetricsInt arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint$FontMetricsInt;->set(Landroid/graphics/Paint$FontMetricsInt;)V");
        }

        public java.lang.String toString() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint$FontMetricsInt;->toString()Ljava/lang/String;");
        }
        }
        public static enum Join {
            BEVEL, MITER, ROUND;
        }
        public static enum Style {
            FILL, FILL_AND_STROKE, STROKE;
        }
    }
