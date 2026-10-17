package android.view.inputmethod;

    public interface InputConnection {

    public static final int CURSOR_UPDATE_FILTER_CHARACTER_BOUNDS = 8;
    public static final int CURSOR_UPDATE_FILTER_EDITOR_BOUNDS = 4;
    public static final int CURSOR_UPDATE_FILTER_INSERTION_MARKER = 16;
    public static final int CURSOR_UPDATE_FILTER_TEXT_APPEARANCE = 64;
    public static final int CURSOR_UPDATE_FILTER_VISIBLE_LINE_BOUNDS = 32;
    public static final int CURSOR_UPDATE_IMMEDIATE = 1;
    public static final int CURSOR_UPDATE_MONITOR = 2;
    public static final int GET_EXTRACTED_TEXT_MONITOR = 1;
    public static final int GET_TEXT_WITH_STYLES = 1;
    public static final int HANDWRITING_GESTURE_RESULT_CANCELLED = 4;
    public static final int HANDWRITING_GESTURE_RESULT_FAILED = 3;
    public static final int HANDWRITING_GESTURE_RESULT_FALLBACK = 5;
    public static final int HANDWRITING_GESTURE_RESULT_SUCCESS = 1;
    public static final int HANDWRITING_GESTURE_RESULT_UNKNOWN = 0;
    public static final int HANDWRITING_GESTURE_RESULT_UNSUPPORTED = 2;
    public static final int INPUT_CONTENT_GRANT_READ_URI_PERMISSION = 1;

    public abstract boolean beginBatchEdit();

    public abstract boolean clearMetaKeyStates(int arg0);

    public abstract void closeConnection();

    public abstract boolean commitCompletion(android.view.inputmethod.CompletionInfo arg0);

    public abstract boolean commitContent(android.view.inputmethod.InputContentInfo arg0, int arg1, android.os.Bundle arg2);

    public abstract boolean commitCorrection(android.view.inputmethod.CorrectionInfo arg0);

    public abstract boolean commitText(java.lang.CharSequence arg0, int arg1);

    public default boolean commitText(java.lang.CharSequence arg0, int arg1, android.view.inputmethod.TextAttribute arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputConnection;->commitText(Ljava/lang/CharSequence;ILandroid/view/inputmethod/TextAttribute;)Z");
    }

    public abstract boolean deleteSurroundingText(int arg0, int arg1);

    public abstract boolean deleteSurroundingTextInCodePoints(int arg0, int arg1);

    public abstract boolean endBatchEdit();

    public abstract boolean finishComposingText();

    public abstract int getCursorCapsMode(int arg0);

    public abstract android.view.inputmethod.ExtractedText getExtractedText(android.view.inputmethod.ExtractedTextRequest arg0, int arg1);

    public abstract android.os.Handler getHandler();

    public abstract java.lang.CharSequence getSelectedText(int arg0);

    public default android.view.inputmethod.SurroundingText getSurroundingText(int arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputConnection;->getSurroundingText(III)Landroid/view/inputmethod/SurroundingText;");
    }

    public abstract java.lang.CharSequence getTextAfterCursor(int arg0, int arg1);

    public abstract java.lang.CharSequence getTextBeforeCursor(int arg0, int arg1);

    public abstract boolean performContextMenuAction(int arg0);

    public abstract boolean performEditorAction(int arg0);

    public default void performHandwritingGesture(android.view.inputmethod.HandwritingGesture arg0, java.util.concurrent.Executor arg1, java.util.function.IntConsumer arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputConnection;->performHandwritingGesture(Landroid/view/inputmethod/HandwritingGesture;Ljava/util/concurrent/Executor;Ljava/util/function/IntConsumer;)V");
    }

    public abstract boolean performPrivateCommand(java.lang.String arg0, android.os.Bundle arg1);

    public default boolean performSpellCheck() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputConnection;->performSpellCheck()Z");
    }

    public default boolean previewHandwritingGesture(android.view.inputmethod.PreviewableHandwritingGesture arg0, android.os.CancellationSignal arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputConnection;->previewHandwritingGesture(Landroid/view/inputmethod/PreviewableHandwritingGesture;Landroid/os/CancellationSignal;)Z");
    }

    public default boolean replaceText(int arg0, int arg1, java.lang.CharSequence arg2, int arg3, android.view.inputmethod.TextAttribute arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputConnection;->replaceText(IILjava/lang/CharSequence;ILandroid/view/inputmethod/TextAttribute;)Z");
    }

    public abstract boolean reportFullscreenMode(boolean arg0);

    public abstract boolean requestCursorUpdates(int arg0);

    public default boolean requestCursorUpdates(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputConnection;->requestCursorUpdates(II)Z");
    }

    public default void requestTextBoundsInfo(android.graphics.RectF arg0, java.util.concurrent.Executor arg1, java.util.function.Consumer<android.view.inputmethod.TextBoundsInfoResult> arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputConnection;->requestTextBoundsInfo(Landroid/graphics/RectF;Ljava/util/concurrent/Executor;Ljava/util/function/Consumer;)V");
    }

    public abstract boolean sendKeyEvent(android.view.KeyEvent arg0);

    public abstract boolean setComposingRegion(int arg0, int arg1);

    public default boolean setComposingRegion(int arg0, int arg1, android.view.inputmethod.TextAttribute arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputConnection;->setComposingRegion(IILandroid/view/inputmethod/TextAttribute;)Z");
    }

    public abstract boolean setComposingText(java.lang.CharSequence arg0, int arg1);

    public default boolean setComposingText(java.lang.CharSequence arg0, int arg1, android.view.inputmethod.TextAttribute arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputConnection;->setComposingText(Ljava/lang/CharSequence;ILandroid/view/inputmethod/TextAttribute;)Z");
    }

    public default boolean setImeConsumesInput(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputConnection;->setImeConsumesInput(Z)Z");
    }

    public abstract boolean setSelection(int arg0, int arg1);

    public default android.view.inputmethod.TextSnapshot takeSnapshot() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputConnection;->takeSnapshot()Landroid/view/inputmethod/TextSnapshot;");
    }
    }
