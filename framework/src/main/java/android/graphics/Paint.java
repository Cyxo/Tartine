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

    /*
     * Skia-backed paint state. Deviation: AOSP Paint.java:707/1208-1211 keeps
     * a native paint plus a ColorLong (mColor = Color.pack(...)); here plain
     * host fields carry the same Java contracts, sRGB-only. ColorLong decode
     * and ColorSpace branches land when a wall needs them.
     */
    private int mColor = 0xFF000000; // black, per AOSP ctor (:707 mColor = Color.pack(Color.BLACK))
    private int mFlags = 0;
    private float mStrokeWidth = 0.0f;
    private Paint.Style mStyle = null; // null = FILL (native default)
    // Text size staging state (AOSP Paint.java:1942/1951: native paint
    // holds it; plain host field carries the same contract here).
    private float mTextSize = 0.0f;
    // AOSP Paint.java:356-357: HIDDEN_DEFAULT_PAINT_FLAGS (all set on new/reset).
    private static final int HOST_HIDDEN_DEFAULT_PAINT_FLAGS =
            DEV_KERN_TEXT_FLAG | EMBEDDED_BITMAP_TEXT_FLAG | FILTER_BITMAP_FLAG;

    public Paint() {
        // AOSP Paint.java:680-682: Paint() == Paint(ANTI_ALIAS_FLAG)
        // ("On devices running Build.VERSION_CODES#S and above, ANTI_ALIAS_FLAG
        // is set by this constructor").
        this(ANTI_ALIAS_FLAG);
    }

    public Paint(android.graphics.Paint arg0) {
        // AOSP Paint.java:718-722 (copy constructor; :765 set →
        // setClassVariablesFrom :777-801). Deviation: copies the state we
        // carry (flags/color/style/width/text size).
        mFlags = arg0.getFlags();
        mColor = arg0.mColor;
        mStyle = arg0.mStyle;
        mStrokeWidth = arg0.mStrokeWidth;
        mTextSize = arg0.mTextSize;
    }

    public Paint(int arg0) {
        // AOSP Paint.java:697-709 (flags ctor): setFlags(flags | HIDDEN),
        // color = packed BLACK. Deviation: text-locale/hinting bits wait.
        setFlags(arg0 | HOST_HIDDEN_DEFAULT_PAINT_FLAGS);
        mColor = 0xFF000000;
    }

    public float ascent() {
        // AOSP Paint.java:2296 (nAscent native there; negative y-up value).
        // Staging reads the skija metrics of the staging font directly.
        io.github.humbleui.skija.Font font = tartineSkiaFont();
        return font.getMetrics().getAscent();
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
        // AOSP Paint.java:1239-1241: alpha of the color, [0..255].
        return (mColor >>> 24) & 0xFF;
    }

    public android.graphics.BlendMode getBlendMode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->getBlendMode()Landroid/graphics/BlendMode;");
    }

    public int getColor() {
        // AOSP Paint.java:1182-1184: sRGB ARGB view of the packed color.
        return mColor;
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
        // AOSP Paint.java:880-882.
        return mFlags;
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
        // AOSP Paint.java: getStrokeWidth returns the mStrokeWidth state.
        return mStrokeWidth;
    }

    public android.graphics.Paint.Style getStyle() {
        // AOSP Paint.java:1158-1160. null state = native default FILL.
        return mStyle == null ? Paint.Style.FILL : mStyle;
    }

    /** Host-only Skia view of this paint (see Canvas.tartineCreate docs). */
    io.github.humbleui.skija.Paint tartineSkia() {
        io.github.humbleui.skija.Paint out = new io.github.humbleui.skija.Paint();
        out.setColor(mColor);
        out.setAntiAlias((mFlags & ANTI_ALIAS_FLAG) != 0);
        switch (mStyle == null ? Paint.Style.FILL : mStyle) {
            case STROKE: out.setMode(io.github.humbleui.skija.PaintMode.STROKE); break;
            case FILL_AND_STROKE: out.setMode(
                    io.github.humbleui.skija.PaintMode.STROKE_AND_FILL); break;
            default: out.setMode(io.github.humbleui.skija.PaintMode.FILL);
        }
        if (mStrokeWidth > 0.0f) {
            out.setStrokeWidth(mStrokeWidth);
        }
        return out;
    }

    /**
     * Host-only Skia font for text draws (staging): the default host
     * typeface at the paint's text size with subpixel antialiasing. The
     * Typeface state model (paint.mTypeface) is still a placeholder until
     * a Typeface module port lands; until then every text draw uses a
     * desktop system face (Roboto first, mirroring the Android default).
     *
     * <p>Skija 0.143.18 note (standalone probe, 2026-10-19): {@code new
     * Font()} binds a typeface with no glyph coverage and
     * {@code FontMgr.getDefault().matchFamilyStyle(null, ...)} answers
     * {@code null}, so the Skia-default-family conventions render
     * zero-extent output. The host FontMgr is real ({@code
     * getFamiliesCount() > 0}); match against it explicitly instead.
     */
    private static final String[] TARTINE_DEFAULT_FAMILIES = {
            "Roboto", "Segoe UI", "Noto Sans", "DejaVu Sans", "Liberation Sans",
            "Helvetica Neue"
    };

    /** Preferred families from host config ({@code font.families}, README
     * Configuration); null falls back to the built-in list. Set once at
     * startup by {@code tartine.config.HostConfig}; cosmetic-only surface,
     * not guest framework behavior. */
    private static volatile String[] tartinePreferredFamilies;

    /** Host config seam: preferred desktop font families, tried in order
     * when a Paint has no explicit typeface; {@code null} restores the
     * built-in list and re-resolves the cached typeface. */
    public static void tartineSetPreferredFamilies(String[] families) {
        tartinePreferredFamilies =
                families == null || families.length == 0 ? null : families;
        tartineDefaultTypeface = null;
    }

    /** Current preferred list (host config introspection/tests). */
    public static String[] tartinePreferredFamilies() {
        return tartinePreferredFamilies != null
                ? tartinePreferredFamilies : TARTINE_DEFAULT_FAMILIES;
    }

    private static io.github.humbleui.skija.Typeface tartineDefaultTypeface;

    private static io.github.humbleui.skija.Typeface tartineDefaultTypeface() {
        io.github.humbleui.skija.Typeface found = tartineDefaultTypeface;
        if (found == null) {
            io.github.humbleui.skija.FontMgr mgr = io.github.humbleui.skija.FontMgr.getDefault();
            for (String family : tartinePreferredFamilies()) {
                found = mgr.matchFamilyStyle(family, io.github.humbleui.skija.FontStyle.NORMAL);
                if (found != null) {
                    break;
                }
            }
            if (found == null) {
                // A text draw needs some real face; no-desktop-font hosts
                // surface as the named gap instead of silent zero-extent
                // output.
                throw new UnsupportedOperationException(
                        "Tartine: not implemented: host typeface lookup"
                                + " (none of "
                                + java.util.Arrays.toString(tartinePreferredFamilies())
                                + " present)");
            }
            tartineDefaultTypeface = found;
        }
        return found;
    }

    io.github.humbleui.skija.Font tartineSkiaFont() {
        float size = mTextSize > 0.0f ? mTextSize : 14.0f;
        io.github.humbleui.skija.Font font = new io.github.humbleui.skija.Font(
                tartineDefaultTypeface(), size);
        font.setSubpixel(true);
        font.setEdging(io.github.humbleui.skija.FontEdging.SUBPIXEL_ANTI_ALIAS);
        return font;
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
        // AOSP Paint.java:1942 (nGetTextSize native there; plain field here).
        return mTextSize;
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
        // AOSP Paint.java:725-739: flags back to HIDDEN|ANTI_ALIAS, color
        // black, filters cleared. Deviation: text-locale/hinting untouched;
        // we only carry color/flags/style/width.
        setFlags(HOST_HIDDEN_DEFAULT_PAINT_FLAGS | ANTI_ALIAS_FLAG);
        mColor = 0xFF000000;
        mStyle = null;
        mStrokeWidth = 0.0f;
    }

    public void set(android.graphics.Paint arg0) {
        // AOSP Paint.java:765-771 set(Paint src): skip self-copy, then
        // setClassVariablesFrom (:777-801; same state the copy ctor above
        // carries, including text size staging state).
        if (arg0 != this) {
            mFlags = arg0.getFlags();
            mColor = arg0.mColor;
            mStyle = arg0.mStyle;
            mStrokeWidth = arg0.mStrokeWidth;
            mTextSize = arg0.mTextSize;
        }
    }

    public void setARGB(int arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setARGB(IIII)V");
    }

    public void setAlpha(int arg0) {
        // AOSP Paint.java:1250-1259: set the alpha channel, keep r,g,b.
        int rgb = mColor & 0x00FFFFFF;
        mColor = rgb | ((arg0 & 0xFF) << 24);
    }

    public void setAntiAlias(boolean arg0) {
        // AOSP Paint.java:929-946: helper setting or clearing the flag bit.
        if (arg0) {
            setFlags(getFlags() | ANTI_ALIAS_FLAG);
        } else {
            setFlags(getFlags() & ~ANTI_ALIAS_FLAG);
        }
    }

    public void setBlendMode(android.graphics.BlendMode arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Paint;->setBlendMode(Landroid/graphics/BlendMode;)V");
    }

    public void setColor(int arg0) {
        // AOSP Paint.java:1208-1211.
        mColor = arg0;
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
        // AOSP Paint.java:889-891.
        mFlags = arg0;
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
        // AOSP Paint.java:1294-1296 (mStrokeWidth state, "> 0" = strokeWidth).
        mStrokeWidth = arg0;
    }

    public void setStyle(android.graphics.Paint.Style arg0) {
        // AOSP Paint.java:1169-1171.
        mStyle = arg0;
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
        // AOSP Paint.java:1951 (nSetTextSize native there; plain field).
        mTextSize = arg0;
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
