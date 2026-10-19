package android.graphics;

    public class Canvas {

    /*
     * Skia-backed raster canvas. AOSP backs Canvas with Skia natively
     * (graphics/java/android/graphics/Canvas.java; the same Skia the Skija
     * bindings expose), so each ported operation maps directly to the
     * corresponding SkCanvas call via Skija. Ops whose port is not yet needed
     * by a probe wall keep their generated stub bodies.
     *
     * Deviations (each noted at its site):
     * - mNativeCanvasWrapper/n* natives become the Skija backing field(s).
     * - the save-count is tracked locally so rootless canvases behave like
     *   AOSP's empty raster canvas (state changes apply even with no pixels).
     * - NativeAllocationRegistry finalizer bookkeeping is dropped: Skija
     *   objects are garbage collected on the JVM side.
     */

    // AOSP Canvas.java:70 (mBitmap), :73 (mDrawFilter).
    // may be null
    private Bitmap mBitmap;
    // optional field set by the caller
    private DrawFilter mDrawFilter;

    // Skia backing. null = rootless canvas (AOSP Canvas() = "empty raster
    // canvas" per :90-104, draw state still applies, pixels are nowhere).
    io.github.humbleui.skija.Canvas skija;

    // Recorder/recorded picture for the recording variant obtained through
    // RenderNode#beginRecording (AOSP RenderNode.java:485-526 stores the
    // RecordingCanvas and its recorder on the node side; here the recording
    // state rides on the canvas).
    io.github.humbleui.skija.PictureRecorder recorder;

    // Local save-count. Tracks the same save() increments the native wrapper
    // would; keeps semantics identical whether or not a Skija target is
    // attached yet (deviation note at the ctor).
    int saveStack;

    /**
     * Construct an empty raster canvas. Use setBitmap() to specify a bitmap to
     * draw into.  The initial target density is {@link Bitmap#DENSITY_NONE};
     * this will typically be replaced when a target bitmap is set for the
     * canvas.
     */
    public Canvas() {
        // AOSP Canvas.java:95-104: empty raster canvas via nInitRaster(0).
        // Here the Skija backing stays null until a target is bound; the
        // save-stack still tracks state (see field doc).
        skija = null;
    }

    // Dev binding: surface-backed canvas for the host renderer
    // (tartine.graphics.SkiaPresenter calls this reflectively; guests never
    // see it and dex surface carries SDK signatures only).
    static Canvas tartineCreate(io.github.humbleui.skija.Surface surface) {
        Canvas canvas = new Canvas();
        canvas.skija = surface.getCanvas();
        return canvas;
    }

    public Canvas(android.graphics.Bitmap arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;-><init>(Landroid/graphics/Bitmap;)V");
    }

    public boolean clipOutPath(android.graphics.Path arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->clipOutPath(Landroid/graphics/Path;)Z");
    }

    public boolean clipOutRect(android.graphics.Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->clipOutRect(Landroid/graphics/Rect;)Z");
    }

    public boolean clipOutRect(android.graphics.RectF arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->clipOutRect(Landroid/graphics/RectF;)Z");
    }

    public boolean clipOutRect(float arg0, float arg1, float arg2, float arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->clipOutRect(FFFF)Z");
    }

    public boolean clipOutRect(int arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->clipOutRect(IIII)Z");
    }

    public void clipOutShader(android.graphics.Shader arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->clipOutShader(Landroid/graphics/Shader;)V");
    }

    public boolean clipPath(android.graphics.Path arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->clipPath(Landroid/graphics/Path;)Z");
    }

    public boolean clipPath(android.graphics.Path arg0, android.graphics.Region.Op arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->clipPath(Landroid/graphics/Path;Landroid/graphics/Region$Op;)Z");
    }

    public boolean clipRect(android.graphics.Rect arg0) {
        // AOSP Canvas.java clipRect(Rect) intersects the current clip with the
        // bounds; SkCanvas clips intersect by default (ClipOp.INTERSECT).
        boolean clipped = clipRect((float) arg0.left, (float) arg0.top,
                (float) arg0.right, (float) arg0.bottom);
        return clipped;
    }

    public boolean clipRect(android.graphics.Rect arg0, android.graphics.Region.Op arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->clipRect(Landroid/graphics/Rect;Landroid/graphics/Region$Op;)Z");
    }

    public boolean clipRect(android.graphics.RectF arg0) {
        // AOSP Canvas.java: clipRect(RectF) = intersect with the current clip.
        return clipRect(arg0.left, arg0.top, arg0.right, arg0.bottom);
    }

    public boolean clipRect(android.graphics.RectF arg0, android.graphics.Region.Op arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->clipRect(Landroid/graphics/RectF;Landroid/graphics/Region$Op;)Z");
    }

    public boolean clipRect(float arg0, float arg1, float arg2, float arg3) {
        // AOSP Canvas.java: clipRect(FFFF) intersects with the current clip;
        // returns whether the canvas is non-empty after clipping (AOSP's
        // nClipRect answer). Skija clipRect itself returns void/fluent, so the
        // non-empty check reads the device clip bounds afterwards.
        return clipRectLtrb(arg0, arg1, arg2, arg3,
                io.github.humbleui.skija.ClipMode.INTERSECT);
    }

    /** Shared clip body for the clipRect ports (AOSP nClipRect semantics:
     *  clip with the op, then report whether the device clip is non-empty —
     *  Skija clipRect is void/fluent, so the check reads device clip bounds). */
    private boolean clipRectLtrb(float arg0, float arg1, float arg2, float arg3,
            io.github.humbleui.skija.ClipMode mode) {
        if (skija != null) {
            skija.clipRect(
                    io.github.humbleui.types.Rect.makeLTRB(arg0, arg1, arg2, arg3),
                    mode);
            io.github.humbleui.types.IRect bounds = skija.getDeviceClipBounds();
            return bounds != null && bounds.getLeft() < bounds.getRight()
                    && bounds.getTop() < bounds.getBottom();
        }
        // Rootless: AOSP's empty raster still applies the clip state for
        // later draws; the non-empty answer is conservatively true.
        return true;
    }

    public boolean clipRect(float arg0, float arg1, float arg2, float arg3, android.graphics.Region.Op arg4) {
        // AOSP Canvas.java:891-901 (clipRect(RectF,Op) body):
        // checkValidClipOp, then nClipRect with the op. As of P only
        // INTERSECT and DIFFERENCE reach the native leg — checkValidClipOp
        // (Canvas.java:847-854) throws for the rest, mirrored here.
        if (arg4 != android.graphics.Region.Op.INTERSECT
                && arg4 != android.graphics.Region.Op.DIFFERENCE) {
            throw new IllegalArgumentException(
                    "Invalid Region.Op - only INTERSECT and DIFFERENCE are allowed");
        }
        io.github.humbleui.skija.ClipMode mode = arg4 == android.graphics.Region.Op.DIFFERENCE
                ? io.github.humbleui.skija.ClipMode.DIFFERENCE
                : io.github.humbleui.skija.ClipMode.INTERSECT;
        return clipRectLtrb(arg0, arg1, arg2, arg3, mode);
    }

    public boolean clipRect(int arg0, int arg1, int arg2, int arg3) {
        // AOSP Canvas.java clipRect(IIII) truncates to floats and intersects.
        return clipRect((float) arg0, (float) arg1, (float) arg2, (float) arg3);
    }

    public void clipShader(android.graphics.Shader arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->clipShader(Landroid/graphics/Shader;)V");
    }

    public void concat(android.graphics.Matrix arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->concat(Landroid/graphics/Matrix;)V");
    }

    public void concat(android.graphics.Matrix44 arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->concat(Landroid/graphics/Matrix44;)V");
    }

    public void disableZ() {
        // AOSP Canvas.java:243-249 + RenderNode Z-reordering hook; recording
        // variant handled in its own class. No-op without reorder support yet.
    }

    public void drawARGB(int arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawARGB(IIII)V");
    }

    public void drawArc(android.graphics.RectF arg0, float arg1, float arg2, boolean arg3, android.graphics.Paint arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawArc(Landroid/graphics/RectF;FFZLandroid/graphics/Paint;)V");
    }

    public void drawArc(float arg0, float arg1, float arg2, float arg3, float arg4, float arg5, boolean arg6, android.graphics.Paint arg7) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawArc(FFFFFFZLandroid/graphics/Paint;)V");
    }

    public void drawBitmap(android.graphics.Bitmap arg0, android.graphics.Matrix arg1, android.graphics.Paint arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawBitmap(Landroid/graphics/Bitmap;Landroid/graphics/Matrix;Landroid/graphics/Paint;)V");
    }

    public void drawBitmap(android.graphics.Bitmap arg0, android.graphics.Rect arg1, android.graphics.Rect arg2, android.graphics.Paint arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawBitmap(Landroid/graphics/Bitmap;Landroid/graphics/Rect;Landroid/graphics/Rect;Landroid/graphics/Paint;)V");
    }

    public void drawBitmap(android.graphics.Bitmap arg0, android.graphics.Rect arg1, android.graphics.RectF arg2, android.graphics.Paint arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawBitmap(Landroid/graphics/Bitmap;Landroid/graphics/Rect;Landroid/graphics/RectF;Landroid/graphics/Paint;)V");
    }

    public void drawBitmap(android.graphics.Bitmap arg0, float arg1, float arg2, android.graphics.Paint arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawBitmap(Landroid/graphics/Bitmap;FFLandroid/graphics/Paint;)V");
    }

    public void drawBitmap(int[] arg0, int arg1, int arg2, float arg3, float arg4, int arg5, int arg6, boolean arg7, android.graphics.Paint arg8) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawBitmap([IIIFFIIZLandroid/graphics/Paint;)V");
    }

    public void drawBitmap(int[] arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, boolean arg7, android.graphics.Paint arg8) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawBitmap([IIIIIIIZLandroid/graphics/Paint;)V");
    }

    public void drawBitmapMesh(android.graphics.Bitmap arg0, int arg1, int arg2, float[] arg3, int arg4, int[] arg5, int arg6, android.graphics.Paint arg7) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawBitmapMesh(Landroid/graphics/Bitmap;II[FI[IILandroid/graphics/Paint;)V");
    }

    public void drawCircle(float arg0, float arg1, float arg2, android.graphics.Paint arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawCircle(FFFLandroid/graphics/Paint;)V");
    }

    public void drawColor(int arg0) {
        // AOSP Canvas.java drawColor(int): SRC_OVER fill of the canvas.
        if (skija != null) {
            skija.drawColor(arg0);
        }
    }

    public void drawColor(int arg0, android.graphics.BlendMode arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawColor(ILandroid/graphics/BlendMode;)V");
    }

    public void drawColor(int arg0, android.graphics.PorterDuff.Mode arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawColor(ILandroid/graphics/PorterDuff$Mode;)V");
    }

    public void drawColor(long arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawColor(J)V");
    }

    public void drawColor(long arg0, android.graphics.BlendMode arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawColor(JLandroid/graphics/BlendMode;)V");
    }

    public void drawDoubleRoundRect(android.graphics.RectF arg0, float arg1, float arg2, android.graphics.RectF arg3, float arg4, float arg5, android.graphics.Paint arg6) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawDoubleRoundRect(Landroid/graphics/RectF;FFLandroid/graphics/RectF;FFLandroid/graphics/Paint;)V");
    }

    public void drawDoubleRoundRect(android.graphics.RectF arg0, float[] arg1, android.graphics.RectF arg2, float[] arg3, android.graphics.Paint arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawDoubleRoundRect(Landroid/graphics/RectF;[FLandroid/graphics/RectF;[FLandroid/graphics/Paint;)V");
    }

    public void drawGlyphs(int[] arg0, int arg1, float[] arg2, int arg3, int arg4, android.graphics.fonts.Font arg5, android.graphics.Paint arg6) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawGlyphs([II[FIILandroid/graphics/fonts/Font;Landroid/graphics/Paint;)V");
    }

    public void drawLine(float arg0, float arg1, float arg2, float arg3, android.graphics.Paint arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawLine(FFFFLandroid/graphics/Paint;)V");
    }

    public void drawLines(float[] arg0, android.graphics.Paint arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawLines([FLandroid/graphics/Paint;)V");
    }

    public void drawLines(float[] arg0, int arg1, int arg2, android.graphics.Paint arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawLines([FIILandroid/graphics/Paint;)V");
    }

    public void drawMesh(android.graphics.Mesh arg0, android.graphics.BlendMode arg1, android.graphics.Paint arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawMesh(Landroid/graphics/Mesh;Landroid/graphics/BlendMode;Landroid/graphics/Paint;)V");
    }

    public void drawOval(android.graphics.RectF arg0, android.graphics.Paint arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawOval(Landroid/graphics/RectF;Landroid/graphics/Paint;)V");
    }

    public void drawOval(float arg0, float arg1, float arg2, float arg3, android.graphics.Paint arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawOval(FFFFLandroid/graphics/Paint;)V");
    }

    public void drawPaint(android.graphics.Paint arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawPaint(Landroid/graphics/Paint;)V");
    }

    public void drawPatch(android.graphics.NinePatch arg0, android.graphics.Rect arg1, android.graphics.Paint arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawPatch(Landroid/graphics/NinePatch;Landroid/graphics/Rect;Landroid/graphics/Paint;)V");
    }

    public void drawPatch(android.graphics.NinePatch arg0, android.graphics.RectF arg1, android.graphics.Paint arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawPatch(Landroid/graphics/NinePatch;Landroid/graphics/RectF;Landroid/graphics/Paint;)V");
    }

    public void drawPath(android.graphics.Path arg0, android.graphics.Paint arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawPath(Landroid/graphics/Path;Landroid/graphics/Paint;)V");
    }

    public void drawPicture(android.graphics.Picture arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawPicture(Landroid/graphics/Picture;)V");
    }

    public void drawPicture(android.graphics.Picture arg0, android.graphics.Rect arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawPicture(Landroid/graphics/Picture;Landroid/graphics/Rect;)V");
    }

    public void drawPicture(android.graphics.Picture arg0, android.graphics.RectF arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawPicture(Landroid/graphics/Picture;Landroid/graphics/RectF;)V");
    }

    public void drawPoint(float arg0, float arg1, android.graphics.Paint arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawPoint(FFLandroid/graphics/Paint;)V");
    }

    public void drawPoints(float[] arg0, android.graphics.Paint arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawPoints([FLandroid/graphics/Paint;)V");
    }

    public void drawPoints(float[] arg0, int arg1, int arg2, android.graphics.Paint arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawPoints([FIILandroid/graphics/Paint;)V");
    }

    public void drawPosText(char[] arg0, int arg1, int arg2, float[] arg3, android.graphics.Paint arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawPosText([CII[FLandroid/graphics/Paint;)V");
    }

    public void drawPosText(java.lang.String arg0, float[] arg1, android.graphics.Paint arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawPosText(Ljava/lang/String;[FLandroid/graphics/Paint;)V");
    }

    public void drawRGB(int arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawRGB(III)V");
    }

    public void drawRect(android.graphics.Rect arg0, android.graphics.Paint arg1) {
        // AOSP Canvas.drawRect(Rect, Paint): left/top/right/bottom to floats.
        drawRect((float) arg0.left, (float) arg0.top,
                (float) arg0.right, (float) arg0.bottom, arg1);
    }

    public void drawRect(android.graphics.RectF arg0, android.graphics.Paint arg1) {
        drawRect(arg0.left, arg0.top, arg0.right, arg0.bottom, arg1);
    }

    public void drawRect(float arg0, float arg1, float arg2, float arg3, android.graphics.Paint arg4) {
        // AOSP Canvas.drawRect(FFFF, Paint) per Canvas.java drawing section;
        // direct SkCanvas::drawRect equivalent.
        if (skija != null) {
            skija.drawRect(
                    io.github.humbleui.types.Rect.makeLTRB(arg0, arg1, arg2, arg3),
                    paintOf(arg4));
        }
    }

    public void drawRegion(android.graphics.Region arg0, android.graphics.Paint arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawRegion(Landroid/graphics/Region;Landroid/graphics/Paint;)V");
    }

    public void drawRenderNode(android.graphics.RenderNode arg0) {
        // AOSP RecordingCanvas.java:172-180 (drawRenderNode -> nDrawRenderNode):
        // draws the recorded display list of the node onto this canvas.
        // Rootless recording-production variant also lands here once nodes
        // supply a picture (deviation note).
        if (skija != null && arg0 != null) {
            io.github.humbleui.skija.Picture picture = arg0.tartinePicture();
            if (picture != null) {
                skija.drawPicture(picture);
            }
        }
    }

    public void drawRoundRect(android.graphics.RectF arg0, float arg1, float arg2, android.graphics.Paint arg3) {
        // AOSP Canvas.drawRoundRect(RectF, rx, ry, Paint).
        drawRoundRect(arg0.left, arg0.top, arg0.right, arg0.bottom, arg1, arg2, arg3);
    }

    public void drawRoundRect(float arg0, float arg1, float arg2, float arg3, float arg4, float arg5, android.graphics.Paint arg6) {
        // AOSP Canvas.drawRoundRect(FFFFrr, Paint); direct Skia RRect fill.
        if (skija != null) {
            io.github.humbleui.types.RRect rrect = io.github.humbleui.types.RRect
                    .makeLTRB(arg0, arg1, arg2, arg3, arg4, arg5);
            skija.drawRRect(rrect, paintOf(arg6));
        }
    }

    public void drawText(char[] arg0, int arg1, int arg2, float arg3, float arg4, android.graphics.Paint arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawText([CIIFFLandroid/graphics/Paint;)V");
    }

    public void drawText(java.lang.CharSequence arg0, int arg1, int arg2, float arg3, float arg4, android.graphics.Paint arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawText(Ljava/lang/CharSequence;IIFFLandroid/graphics/Paint;)V");
    }

    public void drawText(java.lang.String arg0, float arg1, float arg2, android.graphics.Paint arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawText(Ljava/lang/String;FFLandroid/graphics/Paint;)V");
    }

    public void drawText(java.lang.String arg0, int arg1, int arg2, float arg3, float arg4, android.graphics.Paint arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawText(Ljava/lang/String;IIFFLandroid/graphics/Paint;)V");
    }

    public void drawTextOnPath(char[] arg0, int arg1, int arg2, android.graphics.Path arg3, float arg4, float arg5, android.graphics.Paint arg6) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawTextOnPath([CIILandroid/graphics/Path;FFLandroid/graphics/Paint;)V");
    }

    public void drawTextOnPath(java.lang.String arg0, android.graphics.Path arg1, float arg2, float arg3, android.graphics.Paint arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawTextOnPath(Ljava/lang/String;Landroid/graphics/Path;FFLandroid/graphics/Paint;)V");
    }

    public void drawTextRun(android.graphics.text.MeasuredText arg0, int arg1, int arg2, int arg3, int arg4, float arg5, float arg6, boolean arg7, android.graphics.Paint arg8) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawTextRun(Landroid/graphics/text/MeasuredText;IIIIFFZLandroid/graphics/Paint;)V");
    }

    public void drawTextRun(char[] arg0, int arg1, int arg2, int arg3, int arg4, float arg5, float arg6, boolean arg7, android.graphics.Paint arg8) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawTextRun([CIIIIFFZLandroid/graphics/Paint;)V");
    }

    public void drawTextRun(java.lang.CharSequence arg0, int arg1, int arg2, int arg3, int arg4, float arg5, float arg6, boolean arg7, android.graphics.Paint arg8) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawTextRun(Ljava/lang/CharSequence;IIIIFFZLandroid/graphics/Paint;)V");
    }

    public void drawVertices(android.graphics.Canvas.VertexMode arg0, int arg1, float[] arg2, int arg3, float[] arg4, int arg5, int[] arg6, int arg7, short[] arg8, int arg9, int arg10, android.graphics.Paint arg11) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->drawVertices(Landroid/graphics/Canvas$VertexMode;I[FI[FI[II[SIILandroid/graphics/Paint;)V");
    }

    public void enableZ() {
        // AOSP Canvas docs: Z reordering, no-op until RenderNode Z ordering
        // support lands; see disableZ().
    }

    public final android.graphics.Rect getClipBounds() {
        // AOSP Canvas.getClipBounds() allocates and fills the bound rect.
        Rect bounds = new Rect();
        if (skija != null) {
            io.github.humbleui.types.IRect clip = skija.getDeviceClipBounds();
            if (clip == null || clip.getLeft() >= clip.getRight()
                    || clip.getTop() >= clip.getBottom()) {
                return bounds;
            }
            bounds.left = clip.getLeft();
            bounds.top = clip.getTop();
            bounds.right = clip.getRight();
            bounds.bottom = clip.getBottom();
        }
        return bounds;
    }

    public boolean getClipBounds(android.graphics.Rect arg0) {
        // AOSP Canvas.getClipBounds(Rect): sets the receiving rect or clears it.
        if (arg0 == null) {
            throw new NullPointerException("bounds must not be null");
        }
        Rect partial = getClipBounds();
        if (partial.isEmpty() && !isClipBoundsEmpty()) {
            // AOSP marks set-empty on failure; keep parity conservatively.
            arg0.set(partial);
            return false;
        }
        arg0.set(partial);
        return !partial.isEmpty();
    }

    // Rootless canvas has no reachable clip; the bound answer is the whole
    // (implicit) viewport, which AABBs to nothing drawable. Treated as
    // non-empty to keep callers' logic identical to the attached case.
    private boolean isClipBoundsEmpty() {
        return false;
    }

    public int getDensity() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->getDensity()I");
    }

    public android.graphics.DrawFilter getDrawFilter() {
        // AOSP Canvas.java:73: optional DrawFilter, unset by default.
        return mDrawFilter;
    }

    public int getHeight() {
        // AOSP Canvas.getHeight returns the raster's height; rootless is 0.
        if (skija != null) {
            return (int) skija.getSurface().getWidth();
        }
        return 0;
    }

    public final android.graphics.Matrix getMatrix() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->getMatrix()Landroid/graphics/Matrix;");
    }

    public void getMatrix(android.graphics.Matrix arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->getMatrix(Landroid/graphics/Matrix;)V");
    }

    public int getMaximumBitmapHeight() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->getMaximumBitmapHeight()I");
    }

    public int getMaximumBitmapWidth() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->getMaximumBitmapWidth()I");
    }

    public int getSaveCount() {
        // AOSP Canvas.getSaveCount(): the number of save() calls recorded.
        return saveStack;
    }

    public int getWidth() {
        // AOSP Canvas.getWidth returns the raster width; rootless is 0.
        if (skija != null) {
            return (int) skija.getSurface().getWidth();
        }
        return 0;
    }

    public boolean isHardwareAccelerated() {
        // AOSP Canvas.java:151-153: plain Canvas is never hardware accelerated;
        // RecordingCanvas overrides to true (see that class).
        return false;
    }

    public boolean isOpaque() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->isOpaque()Z");
    }

    public boolean quickReject(android.graphics.Path arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->quickReject(Landroid/graphics/Path;)Z");
    }

    public boolean quickReject(android.graphics.Path arg0, android.graphics.Canvas.EdgeType arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->quickReject(Landroid/graphics/Path;Landroid/graphics/Canvas$EdgeType;)Z");
    }

    public boolean quickReject(android.graphics.RectF arg0) {
        // AOSP Canvas.quickReject(RectF) per Canvas.java.
        return quickReject(arg0.left, arg0.top, arg0.right, arg0.bottom);
    }

    public boolean quickReject(android.graphics.RectF arg0, android.graphics.Canvas.EdgeType arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->quickReject(Landroid/graphics/RectF;Landroid/graphics/Canvas$EdgeType;)Z");
    }

    public boolean quickReject(float arg0, float arg1, float arg2, float arg3) {
        // AOSP Canvas.quickReject(FFFF): SkCanvas quickReject semantics —
        // true when the rect cannot intersect the canvas' clip.
        if (skija != null) {
            return skija.quickReject(
                    io.github.humbleui.types.Rect.makeLTRB(arg0, arg1, arg2, arg3));
        }
        return false;
    }

    public boolean quickReject(float arg0, float arg1, float arg2, float arg3, android.graphics.Canvas.EdgeType arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->quickReject(FFFFLandroid/graphics/Canvas$EdgeType;)Z");
    }

    public void restore() {
        // AOSP Canvas.restore(): pop one save level off the stack.
        if (saveStack > 0 && skija != null) {
            skija.restore();
        }
        saveStack = java.lang.Math.max(0, saveStack - 1);
    }

    public void restoreToCount(int arg0) {
        // AOSP Canvas.restoreToCount(int): pop down to the given level.
        while (saveStack > arg0) {
            restore();
        }
        while (saveStack < arg0) {
            // AOSP: counts below the initial state are treated as the base
            // state; clamp to it rather than growing saves.
            break;
        }
    }

    public void rotate(float arg0) {
        // AOSP Canvas.rotate(degrees): post-multiplied rotation.
        if (skija != null) {
            skija.rotate(arg0);
        }
    }

    public final void rotate(float arg0, float arg1, float arg2) {
        // AOSP Canvas.rotate(degrees, px, py): translate/rotate/translate back,
        // the canonical SDK implementation delegates like this.
        translate(arg1, arg2);
        rotate(arg0);
        translate(-arg1, -arg2);
    }

    public int save() {
        // AOSP Canvas.save(): push a save level, return the new count.
        if (skija != null) {
            skija.save();
        }
        return ++saveStack;
    }

    public int saveLayer(android.graphics.RectF arg0, android.graphics.Paint arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->saveLayer(Landroid/graphics/RectF;Landroid/graphics/Paint;)I");
    }

    public int saveLayer(android.graphics.RectF arg0, android.graphics.Paint arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->saveLayer(Landroid/graphics/RectF;Landroid/graphics/Paint;I)I");
    }

    public int saveLayer(float arg0, float arg1, float arg2, float arg3, android.graphics.Paint arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->saveLayer(FFFFLandroid/graphics/Paint;)I");
    }

    public int saveLayer(float arg0, float arg1, float arg2, float arg3, android.graphics.Paint arg4, int arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->saveLayer(FFFFLandroid/graphics/Paint;I)I");
    }

    public int saveLayerAlpha(android.graphics.RectF arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->saveLayerAlpha(Landroid/graphics/RectF;I)I");
    }

    public int saveLayerAlpha(android.graphics.RectF arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->saveLayerAlpha(Landroid/graphics/RectF;II)I");
    }

    public int saveLayerAlpha(float arg0, float arg1, float arg2, float arg3, int arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->saveLayerAlpha(FFFFI)I");
    }

    public int saveLayerAlpha(float arg0, float arg1, float arg2, float arg3, int arg4, int arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->saveLayerAlpha(FFFFII)I");
    }

    public void scale(float arg0, float arg1) {
        // AOSP Canvas.scale(sx, sy): post-multiplied scale.
        if (skija != null) {
            skija.scale(arg0, arg1);
        }
    }

    public final void scale(float arg0, float arg1, float arg2, float arg3) {
        // AOSP Canvas.scale(sx, sy, px, py): scale around the pivot point.
        translate(arg2, arg3);
        scale(arg0, arg1);
        translate(-arg2, -arg3);
    }

    public void setBitmap(android.graphics.Bitmap arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->setBitmap(Landroid/graphics/Bitmap;)V");
    }

    public void setDensity(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->setDensity(I)V");
    }

    public void setDrawFilter(android.graphics.DrawFilter arg0) {
        // AOSP Canvas.java:73-74: stores the DrawFilter (filters apply during
        // draw; none applied yet — honest gap when a filter arrives).
        mDrawFilter = arg0;
    }

    public void setMatrix(android.graphics.Matrix arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/Canvas;->setMatrix(Landroid/graphics/Matrix;)V");
    }

    public void skew(float arg0, float arg1) {
        // AOSP Canvas.skew(sx, sy): post-multiplied skew.
        if (skija != null) {
            skija.skew(arg0, arg1);
        }
    }

    public void translate(float arg0, float arg1) {
        // AOSP Canvas.translate(dx, dy): post-multiplied translate.
        if (skija != null) {
            skija.translate(arg0, arg1);
        }
    }

    /**
     * Converts a guest-press Paint to the backing Skija paint. Paint is
     * bridged to the module mirror before this method runs, so arg0 is a
     * real Paint here. Null paints drop to a default Skija paint (AOSP ops
     * deref; null is never passed in practice).
     */
    io.github.humbleui.skija.Paint paintOf(Paint paint) {
        if (paint == null) {
            return new io.github.humbleui.skija.Paint();
        }
        return paint.tartineSkia();
    }

    void tartineAttach(io.github.humbleui.skija.Canvas backing) {
        // Host renderer binding seam (see tartineCreate).
        this.skija = backing;
    }

    void tartineRecord(io.github.humbleui.skija.Canvas backing,
            io.github.humbleui.skija.PictureRecorder recorder) {
        this.skija = backing;
        this.recorder = recorder;
    }

    // Dev-time readback hook (probe verification of recorded pixels).
    io.github.humbleui.skija.Picture tartineEndPicture() {
        if (recorder == null) {
            return null;
        }
        io.github.humbleui.skija.Picture out = recorder.finishRecordingAsPicture();
        recorder = null;
        return out;
    }

    public static enum EdgeType {
        AA, BW;
    }
    public static enum VertexMode {
        TRIANGLES, TRIANGLE_FAN, TRIANGLE_STRIP;
    }
}
