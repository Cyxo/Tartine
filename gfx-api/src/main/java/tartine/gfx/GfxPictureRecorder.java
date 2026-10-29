package tartine.gfx;

/**
 * Records a display list. One recorder produces one picture; call
 * {@link #finishRecordingAsPicture()} once, then close the recorder.
 */
public interface GfxPictureRecorder extends AutoCloseable {

    /** Starts recording into a canvas with the given bounds. */
    GfxCanvas beginRecording(float width, float height);

    /** Seals the display list and returns the immutable picture. */
    GfxPicture finishRecordingAsPicture();

    @Override
    void close();
}
