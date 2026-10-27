package android.view.inputmethod;

/**
 * Cursor anchor info carrier (android.jar stub re-embodied as shell Java).
 * Compose's text editing builds one per connection to report the cursor
 * selection; the desktop reports a collapsed anchor at position 0.
 */
public class CursorAnchorInfo {

    private int mSelectionStart;
    private int mSelectionEnd;
    private int mCursorPosition;

    private CursorAnchorInfo() {
    }

    int getSelectionStart() {
        return mSelectionStart;
    }

    int getSelectionEnd() {
        return mSelectionEnd;
    }

    int getCursorPosition() {
        return mCursorPosition;
    }

    /**
     * Builder for {@link CursorAnchorInfo}; shell build() returns an anchor
     * at position 0 with empty selection — no text input surface exists.
     */
    public static final class Builder {

        private final CursorAnchorInfo mInfo = new CursorAnchorInfo();

        /**
         * AOSP frameworks-base core/java/android/view/inputmethod/
         * CursorAnchorInfo.java#Builder#setCursorPosition(int).
         */
        public Builder setCursorPosition(int position) {
            mInfo.mCursorPosition = position;
            return this;
        }

        /**
         * AOSP .../CursorAnchorInfo.java#Builder#setSelection(int, int).
         */
        public Builder setSelection(int start, int end) {
            mInfo.mSelectionStart = start;
            mInfo.mSelectionEnd = end;
            return this;
        }

        /**
         * AOSP .../CursorAnchorInfo.java#Builder#build: returns the built
         * anchor; shell reports a collapsed anchor (no input surface).
         */
        public CursorAnchorInfo build() {
            return mInfo;
        }
    }
}
