package android.graphics;

import android.graphics.fonts.Font;
import android.graphics.text.MeasuredText;
import tartine.gfx.GfxBackend;
import tartine.gfx.GfxCanvas;
import tartine.gfx.GfxPicture;
import tartine.gfx.GfxPictureRecorder;

    public class RecordingCanvas extends Canvas {

    /*
     * Recording variant obtained through RenderNode#beginRecording (AOSP
     * graphics/java/android/graphics/RecordingCanvas.java:26-35: "A Canvas
     * implementation that records view system drawing operations for deferred
     * rendering... valid until the matching endRecording"). The seam's
     * GfxPictureRecorder is the direct analog: beginRecording hands out a
     * GfxCanvas that records; finishRecordingAsPicture yields the emitted
     * picture for drawRenderNode replay.
     *
     * The LOCAL SDK jar redeclares most draw ops as final stub bodies (the
     * BaseRecordingCanvas surface is hidden); per AGENTS the skeletons carry
     * exact SDK signatures, so those overrides stay, but their bodies
     * delegate to the base Canvas port (AOSP's BaseRecordingCanvas native
     * records collapse to our seam recording backing).
     */

    int mWidth;
    int mHeight;

    RecordingCanvas(int width, int height) {
        // Not an SDK ctor (AOSP ctor is private, RecordingCanvas.java:92-95);
        // guests reach this class only through RenderNode#beginRecording.
        GfxPictureRecorder pair = GfxBackend.get().newPictureRecorder();
        GfxCanvas gfx = pair.beginRecording(width, height);
        super.tartineRecord(gfx, pair);
        mWidth = width;
        mHeight = height;
    }

    /** AOSP RecordingCanvas.java:84-86 (finishRecording): seal the picture. */
    void finishRecording(RenderNode node) {
        GfxPicture picture = this.recorder.finishRecordingAsPicture();
        this.recorder.close();
        this.recorder = null;
        node.tartineSetPicture(picture);
    }

    public void disableZ() {
        // AOSP RecordingCanvas.java:151-154 nips to nEnableZ(false); the base
        // port no-ops until Z ordering lands (see Canvas#disableZ).
        super.disableZ();
    }

    public void enableZ() {
        // AOSP RecordingCanvas.java:146-149; see Canvas#enableZ.
        super.enableZ();
    }

    public final void drawARGB(int arg0, int arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawARGB(IIII)V");
    }

    public final void drawArc(RectF arg0, float arg1, float arg2, boolean arg3, Paint arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawArc(Landroid/graphics/RectF;FFZLandroid/graphics/Paint;)V");
    }

    public final void drawArc(float arg0, float arg1, float arg2, float arg3, float arg4, float arg5, boolean arg6, Paint arg7) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawArc(FFFFFFZLandroid/graphics/Paint;)V");
    }

    public final void drawBitmap(Bitmap arg0, Matrix arg1, Paint arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawBitmap(Landroid/graphics/Bitmap;Landroid/graphics/Matrix;Landroid/graphics/Paint;)V");
    }

    public final void drawBitmap(Bitmap arg0, Rect arg1, Rect arg2, Paint arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawBitmap(Landroid/graphics/Bitmap;Landroid/graphics/Rect;Landroid/graphics/Rect;Landroid/graphics/Paint;)V");
    }

    public final void drawBitmap(Bitmap arg0, Rect arg1, RectF arg2, Paint arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawBitmap(Landroid/graphics/Bitmap;Landroid/graphics/Rect;Landroid/graphics/RectF;Landroid/graphics/Paint;)V");
    }

    public final void drawBitmap(Bitmap arg0, float arg1, float arg2, Paint arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawBitmap(Landroid/graphics/Bitmap;FFLandroid/graphics/Paint;)V");
    }

    public final void drawBitmap(int[] arg0, int arg1, int arg2, float arg3, float arg4, int arg5, int arg6, boolean arg7, Paint arg8) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawBitmap([IIIFFIIZLandroid/graphics/Paint;)V");
    }

    public final void drawBitmap(int[] arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, boolean arg7, Paint arg8) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawBitmap([IIIIIIIZLandroid/graphics/Paint;)V");
    }

    public final void drawBitmapMesh(Bitmap arg0, int arg1, int arg2, float[] arg3, int arg4, int[] arg5, int arg6, Paint arg7) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawBitmapMesh(Landroid/graphics/Bitmap;II[FI[IILandroid/graphics/Paint;)V");
    }

    public final void drawCircle(float arg0, float arg1, float arg2, Paint arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawCircle(FFFLandroid/graphics/Paint;)V");
    }

    public final void drawColor(int arg0) {
        // Delegate to the base port (AOSP: RecordingCanvas inherits the
        // BaseRecordingCanvas drawColor which records the SkCanvas op).
        super.drawColor(arg0);
    }

    public final void drawColor(int arg0, BlendMode arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawColor(ILandroid/graphics/BlendMode;)V");
    }

    public final void drawColor(int arg0, PorterDuff.Mode arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawColor(ILandroid/graphics/PorterDuff$Mode;)V");
    }

    public final void drawColor(long arg0, BlendMode arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawColor(JLandroid/graphics/BlendMode;)V");
    }

    public final void drawDoubleRoundRect(RectF arg0, float arg1, float arg2, RectF arg3, float arg4, float arg5, Paint arg6) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawDoubleRoundRect(Landroid/graphics/RectF;FFLandroid/graphics/RectF;FFLandroid/graphics/Paint;)V");
    }

    public final void drawDoubleRoundRect(RectF arg0, float[] arg1, RectF arg2, float[] arg3, Paint arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawDoubleRoundRect(Landroid/graphics/RectF;[FLandroid/graphics/RectF;[FLandroid/graphics/Paint;)V");
    }

    public void drawGlyphs(int[] arg0, int arg1, float[] arg2, int arg3, int arg4, Font arg5, Paint arg6) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawGlyphs([II[FIILandroid/graphics/fonts/Font;Landroid/graphics/Paint;)V");
    }

    public final void drawLine(float arg0, float arg1, float arg2, float arg3, Paint arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawLine(FFFFLandroid/graphics/Paint;)V");
    }

    public final void drawLines(float[] arg0, Paint arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawLines([FLandroid/graphics/Paint;)V");
    }

    public final void drawLines(float[] arg0, int arg1, int arg2, Paint arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawLines([FIILandroid/graphics/Paint;)V");
    }

    public final void drawMesh(Mesh arg0, BlendMode arg1, Paint arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawMesh(Landroid/graphics/Mesh;Landroid/graphics/BlendMode;Landroid/graphics/Paint;)V");
    }

    public final void drawOval(RectF arg0, Paint arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawOval(Landroid/graphics/RectF;Landroid/graphics/Paint;)V");
    }

    public final void drawOval(float arg0, float arg1, float arg2, float arg3, Paint arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawOval(FFFFLandroid/graphics/Paint;)V");
    }

    public final void drawPaint(Paint arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawPaint(Landroid/graphics/Paint;)V");
    }

    public final void drawPatch(NinePatch arg0, Rect arg1, Paint arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawPatch(Landroid/graphics/NinePatch;Landroid/graphics/Rect;Landroid/graphics/Paint;)V");
    }

    public final void drawPatch(NinePatch arg0, RectF arg1, Paint arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawPatch(Landroid/graphics/NinePatch;Landroid/graphics/RectF;Landroid/graphics/Paint;)V");
    }

    public final void drawPath(Path arg0, Paint arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawPath(Landroid/graphics/Path;Landroid/graphics/Paint;)V");
    }

    public final void drawPicture(Picture arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawPicture(Landroid/graphics/Picture;)V");
    }

    public final void drawPicture(Picture arg0, Rect arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawPicture(Landroid/graphics/Picture;Landroid/graphics/Rect;)V");
    }

    public final void drawPicture(Picture arg0, RectF arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawPicture(Landroid/graphics/Picture;Landroid/graphics/RectF;)V");
    }

    public final void drawPoint(float arg0, float arg1, Paint arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawPoint(FFLandroid/graphics/Paint;)V");
    }

    public final void drawPoints(float[] arg0, Paint arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawPoints([FLandroid/graphics/Paint;)V");
    }

    public final void drawPoints(float[] arg0, int arg1, int arg2, Paint arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawPoints([FIILandroid/graphics/Paint;)V");
    }

    public final void drawPosText(char[] arg0, int arg1, int arg2, float[] arg3, Paint arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawPosText([CII[FLandroid/graphics/Paint;)V");
    }

    public final void drawPosText(String arg0, float[] arg1, Paint arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawPosText(Ljava/lang/String;[FLandroid/graphics/Paint;)V");
    }

    public final void drawRGB(int arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawRGB(III)V");
    }

    public final void drawRect(Rect arg0, Paint arg1) {
        // Delegate to the base port.
        super.drawRect(arg0, arg1);
    }

    public final void drawRect(RectF arg0, Paint arg1) {
        // Delegate to the base port.
        super.drawRect(arg0, arg1);
    }

    public final void drawRect(float arg0, float arg1, float arg2, float arg3, Paint arg4) {
        // Delegate to the base port.
        super.drawRect(arg0, arg1, arg2, arg3, arg4);
    }

    public void drawRegion(Region arg0, Paint arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawRegion(Landroid/graphics/Region;Landroid/graphics/Paint;)V");
    }

    public void drawRenderNode(RenderNode arg0) {
        // AOSP RecordingCanvas.java:172-180 (nDrawRenderNode). Base port
        // replays the node's recorded picture.
        super.drawRenderNode(arg0);
    }

    public final void drawRoundRect(RectF arg0, float arg1, float arg2, Paint arg3) {
        // Delegate to the base port.
        super.drawRoundRect(arg0, arg1, arg2, arg3);
    }

    public final void drawRoundRect(float arg0, float arg1, float arg2, float arg3, float arg4, float arg5, Paint arg6) {
        // Delegate to the base port.
        super.drawRoundRect(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
    }

    public final void drawText(char[] arg0, int arg1, int arg2, float arg3, float arg4, Paint arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawText([CIIFFLandroid/graphics/Paint;)V");
    }

    public final void drawText(CharSequence arg0, int arg1, int arg2, float arg3, float arg4, Paint arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawText(Ljava/lang/CharSequence;IIFFLandroid/graphics/Paint;)V");
    }

    public final void drawText(String arg0, float arg1, float arg2, Paint arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawText(Ljava/lang/String;FFLandroid/graphics/Paint;)V");
    }

    public final void drawText(String arg0, int arg1, int arg2, float arg3, float arg4, Paint arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawText(Ljava/lang/String;IIFFLandroid/graphics/Paint;)V");
    }

    public final void drawTextOnPath(char[] arg0, int arg1, int arg2, Path arg3, float arg4, float arg5, Paint arg6) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawTextOnPath([CIILandroid/graphics/Path;FFLandroid/graphics/Paint;)V");
    }

    public final void drawTextOnPath(String arg0, Path arg1, float arg2, float arg3, Paint arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawTextOnPath(Ljava/lang/String;Landroid/graphics/Path;FFLandroid/graphics/Paint;)V");
    }

    public void drawTextRun(MeasuredText arg0, int arg1, int arg2, int arg3, int arg4, float arg5, float arg6, boolean arg7, Paint arg8) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawTextRun(Landroid/graphics/text/MeasuredText;IIIIFFZLandroid/graphics/Paint;)V");
    }

    public final void drawTextRun(char[] arg0, int arg1, int arg2, int arg3, int arg4, float arg5, float arg6, boolean arg7, Paint arg8) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawTextRun([CIIIIFFZLandroid/graphics/Paint;)V");
    }

    public final void drawTextRun(CharSequence arg0, int arg1, int arg2, int arg3, int arg4, float arg5, float arg6, boolean arg7, Paint arg8) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawTextRun(Ljava/lang/CharSequence;IIIIFFZLandroid/graphics/Paint;)V");
    }

    public final void drawVertices(Canvas.VertexMode arg0, int arg1, float[] arg2, int arg3, float[] arg4, int arg5, int[] arg6, int arg7, short[] arg8, int arg9, int arg10, Paint arg11) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->drawVertices(Landroid/graphics/Canvas$VertexMode;I[FI[FI[II[SIILandroid/graphics/Paint;)V");
    }

    public int getHeight() {
        // AOSP RecordingCanvas.java:123-125 (mHeight).
        return mHeight;
    }

    public int getMaximumBitmapHeight() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->getMaximumBitmapHeight()I");
    }

    public int getMaximumBitmapWidth() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/graphics/RecordingCanvas;->getMaximumBitmapWidth()I");
    }

    public int getWidth() {
        // AOSP RecordingCanvas.java:128-130 (mWidth).
        return mWidth;
    }

    public boolean isHardwareAccelerated() {
        // AOSP RecordingCanvas.java:107-110: recording canvases are the
        // hardware-accelerated path.
        return true;
    }

    public boolean isOpaque() {
        // AOSP RecordingCanvas.java:117-120.
        return false;
    }

    public void setBitmap(Bitmap arg0) {
        // AOSP RecordingCanvas.java:112-115.
        throw new UnsupportedOperationException();
    }

    public void setDensity(int arg0) {
        // AOSP RecordingCanvas.java:102-105: drop silently.
    }
    }
