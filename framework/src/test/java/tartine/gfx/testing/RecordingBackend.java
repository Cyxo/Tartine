package tartine.gfx.testing;

import java.util.ArrayList;
import java.util.List;

import tartine.gfx.GfxBackend;
import tartine.gfx.GfxCanvas;
import tartine.gfx.GfxClipMode;
import tartine.gfx.GfxFont;
import tartine.gfx.GfxFontMetrics;
import tartine.gfx.GfxImage;
import tartine.gfx.GfxPaint;
import tartine.gfx.GfxPicture;
import tartine.gfx.GfxPictureRecorder;
import tartine.gfx.GfxRect;
import tartine.gfx.GfxSurface;
import tartine.gfx.GfxTypeface;

/**
 * Deterministic fake backend for framework tests: no natives, fixed font
 * metrics, a readable call log, and close tracking. The clip model is a
 * single axis-aligned rectangle, which is enough for the framework's
 * INTERSECT/DIFFERENCE contract tests; a DIFFERENCE that only partially
 * covers the clip is recorded but leaves the clip unchanged (an
 * over-approximation, not a pixel-accurate Skia clip).
 */
public final class RecordingBackend implements GfxBackend {

    /** Fixed metrics every {@link FakeFont} reports. */
    public static final GfxFontMetrics METRICS =
            new GfxFontMetrics(-17.0f, -15.0f, 4.0f, 5.0f, 0.0f);

    /** Fixed advance per character reported by {@link FakeFont}. */
    public static final float GLYPH_WIDTH = 6.0f;

    /** Every backend call, in order. */
    public final List<String> calls = new ArrayList<>();

    /** The typeface this backend hands out (a fresh instance per lookup). */
    public FakeTypeface typeface = new FakeTypeface();

    /** Every font this backend created, in order. */
    public final List<FakeFont> fonts = new ArrayList<>();

    /** Most recently created objects, for assertions. */
    public FakeSurface lastSurface;
    public FakePictureRecorder lastRecorder;
    public FakeImage lastImage;
    public FakeFont lastFont;

    @Override
    public GfxSurface createSurface(int width, int height) {
        calls.add("createSurface(" + width + "x" + height + ")");
        lastSurface = new FakeSurface(width, height, calls);
        return lastSurface;
    }

    @Override
    public GfxPictureRecorder newPictureRecorder() {
        calls.add("newPictureRecorder");
        lastRecorder = new FakePictureRecorder(calls);
        return lastRecorder;
    }

    @Override
    public GfxTypeface defaultTypeface(String[] preferredFamilies) {
        calls.add("defaultTypeface("
                + (preferredFamilies == null ? "null"
                        : String.join(",", preferredFamilies))
                + ")");
        typeface = new FakeTypeface();
        return typeface;
    }

    @Override
    public GfxFont newFont(GfxTypeface typeface, float size) {
        calls.add("newFont(" + size + ")");
        FakeFont font = new FakeFont(size, calls);
        fonts.add(font);
        lastFont = font;
        return font;
    }

    @Override
    public GfxImage decodeImage(byte[] encoded) {
        calls.add("decodeImage(" + (encoded == null ? -1 : encoded.length) + ")");
        lastImage = new FakeImage(2, 2, calls);
        return lastImage;
    }

    /** Clears the shared call log (keeps the backend installed). */
    public void clearCalls() {
        calls.clear();
    }

    /** Opaque typeface marker. */
    public static final class FakeTypeface implements GfxTypeface {
        @Override
        public String toString() {
            return "FakeTypeface";
        }
    }

    /** A surface whose canvas records every draw. */
    public static final class FakeSurface implements GfxSurface {
        private final int width;
        private final int height;
        private final FakeCanvas canvas;
        private final List<String> calls;
        private boolean closed;

        FakeSurface(int width, int height, List<String> calls) {
            this.width = width;
            this.height = height;
            this.calls = calls;
            this.canvas = new FakeCanvas(width, height, calls);
        }

        @Override
        public GfxCanvas getCanvas() {
            return canvas;
        }

        @Override
        public int getWidth() {
            return width;
        }

        @Override
        public int getHeight() {
            return height;
        }

        @Override
        public int[] readPixels() {
            int[] pixels = new int[width * height];
            for (int i = 0; i < pixels.length; i++) {
                pixels[i] = 0xFF000000 | i;
            }
            return pixels;
        }

        @Override
        public void close() {
            closed = true;
            calls.add("surface.close");
        }

        public boolean isClosed() {
            return closed;
        }
    }

    /** Canvas that logs operations and models a rectangular clip. */
    public static final class FakeCanvas implements GfxCanvas {
        private final int width;
        private final int height;
        private final List<String> calls;
        private final List<String> ops = new ArrayList<>();
        private GfxRect clip;
        private int saveDepth;

        FakeCanvas(int width, int height, List<String> calls) {
            this.width = width;
            this.height = height;
            this.calls = calls;
            this.clip = new GfxRect(0.0f, 0.0f, width, height);
        }

        @Override
        public int getWidth() {
            return width;
        }

        @Override
        public int getHeight() {
            return height;
        }

        @Override
        public void save() {
            saveDepth++;
            record("save");
        }

        @Override
        public void restore() {
            if (saveDepth > 0) {
                saveDepth--;
            }
            record("restore");
        }

        @Override
        public void translate(float dx, float dy) {
            record("translate(" + dx + "," + dy + ")");
        }

        @Override
        public void rotate(float degrees) {
            record("rotate(" + degrees + ")");
        }

        @Override
        public void scale(float sx, float sy) {
            record("scale(" + sx + "," + sy + ")");
        }

        @Override
        public void skew(float sx, float sy) {
            record("skew(" + sx + "," + sy + ")");
        }

        @Override
        public boolean clipRect(float left, float top, float right, float bottom,
                GfxClipMode mode) {
            GfxRect rect = new GfxRect(left, top, right, bottom);
            if (mode == GfxClipMode.INTERSECT) {
                clip = clip.intersect(rect);
            } else if (rect.contains(clip)) {
                clip = GfxRect.empty();
            }
            boolean nonEmpty = !clip.isEmpty();
            record("clipRect(" + left + "," + top + "," + right + "," + bottom
                    + "," + mode + ")=" + nonEmpty);
            return nonEmpty;
        }

        @Override
        public GfxRect getDeviceClipBounds() {
            calls.add("getDeviceClipBounds");
            return clip;
        }

        @Override
        public boolean quickReject(float left, float top, float right,
                float bottom) {
            GfxRect rect = new GfxRect(left, top, right, bottom);
            boolean rejected = clip.intersect(rect).isEmpty();
            record("quickReject(" + left + "," + top + "," + right + ","
                    + bottom + ")=" + rejected);
            return rejected;
        }

        @Override
        public void drawColor(int color) {
            record("drawColor(0x" + Integer.toHexString(color) + ")");
        }

        @Override
        public void drawRect(float left, float top, float right, float bottom,
                GfxPaint paint) {
            record("drawRect(" + left + "," + top + "," + right + "," + bottom
                    + "," + paint + ")");
        }

        @Override
        public void drawRoundRect(float left, float top, float right,
                float bottom, float rx, float ry, GfxPaint paint) {
            record("drawRoundRect(" + left + "," + top + "," + right + ","
                    + bottom + "," + rx + "," + ry + "," + paint + ")");
        }

        @Override
        public void drawText(String text, float x, float y, GfxFont font,
                GfxPaint paint) {
            record("drawText(" + text + "," + x + "," + y + "," + font + ","
                    + paint + ")");
        }

        @Override
        public void drawPicture(GfxPicture picture) {
            record("drawPicture(" + picture + ")");
        }

        private void record(String op) {
            calls.add(op);
            ops.add(op);
        }

        public GfxRect getClip() {
            return clip;
        }

        public int getSaveDepth() {
            return saveDepth;
        }

        /** Operations recorded on this canvas, in order. */
        public List<String> ops() {
            return List.copyOf(ops);
        }
    }

    /** Font with fixed metrics and per-character advance. */
    public static final class FakeFont implements GfxFont {
        private final float size;
        private final List<String> calls;
        private boolean closed;

        FakeFont(float size, List<String> calls) {
            this.size = size;
            this.calls = calls;
        }

        @Override
        public GfxFontMetrics getMetrics() {
            calls.add("font.metrics");
            return METRICS;
        }

        @Override
        public float measureTextWidth(String text) {
            calls.add("font.width(" + text + ")");
            return GLYPH_WIDTH * text.length();
        }

        @Override
        public GfxRect measureText(String text) {
            calls.add("font.measure(" + text + ")");
            return new GfxRect(0.0f, METRICS.ascent,
                    GLYPH_WIDTH * text.length(), METRICS.descent);
        }

        @Override
        public void close() {
            closed = true;
            calls.add("font.close");
        }

        public boolean isClosed() {
            return closed;
        }

        public float getSize() {
            return size;
        }

        @Override
        public String toString() {
            return "FakeFont[size=" + size + "]";
        }
    }

    /** Immutable recorded display list. */
    public static final class FakePicture implements GfxPicture {
        private final float width;
        private final float height;
        private final List<String> ops;

        FakePicture(float width, float height, List<String> ops) {
            this.width = width;
            this.height = height;
            this.ops = List.copyOf(ops);
        }

        public List<String> ops() {
            return ops;
        }

        @Override
        public String toString() {
            return "FakePicture[" + width + "x" + height + "]";
        }
    }

    /** Recorder that seals the recorded canvas operations into a picture. */
    public static final class FakePictureRecorder implements GfxPictureRecorder {
        private final List<String> calls;
        private FakeCanvas canvas;
        private FakePicture picture;
        private boolean closed;

        FakePictureRecorder(List<String> calls) {
            this.calls = calls;
        }

        @Override
        public GfxCanvas beginRecording(float width, float height) {
            calls.add("beginRecording(" + width + "x" + height + ")");
            canvas = new FakeCanvas((int) width, (int) height, calls);
            return canvas;
        }

        @Override
        public GfxPicture finishRecordingAsPicture() {
            if (canvas == null) {
                throw new IllegalStateException("not recording");
            }
            calls.add("finishRecordingAsPicture");
            picture = new FakePicture(canvas.getWidth(), canvas.getHeight(),
                    canvas.ops());
            return picture;
        }

        @Override
        public void close() {
            closed = true;
            calls.add("recorder.close");
        }

        public FakeCanvas canvas() {
            return canvas;
        }

        public FakePicture picture() {
            return picture;
        }

        public boolean isClosed() {
            return closed;
        }
    }

    /** Decoded image with a deterministic pixel pattern. */
    public static final class FakeImage implements GfxImage {
        private final int width;
        private final int height;
        private final List<String> calls;
        private boolean closed;

        FakeImage(int width, int height, List<String> calls) {
            this.width = width;
            this.height = height;
            this.calls = calls;
        }

        @Override
        public int getWidth() {
            return width;
        }

        @Override
        public int getHeight() {
            return height;
        }

        @Override
        public int[] readPixels() {
            int[] pixels = new int[width * height];
            for (int i = 0; i < pixels.length; i++) {
                pixels[i] = 0xFF000000 | i;
            }
            return pixels;
        }

        @Override
        public void close() {
            closed = true;
            calls.add("image.close");
        }

        public boolean isClosed() {
            return closed;
        }
    }
}
