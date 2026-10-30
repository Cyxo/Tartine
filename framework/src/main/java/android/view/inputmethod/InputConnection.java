package android.view.inputmethod;

import android.graphics.RectF;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.view.KeyEvent;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

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

    public abstract boolean commitCompletion(CompletionInfo arg0);

    public abstract boolean commitContent(InputContentInfo arg0, int arg1, Bundle arg2);

    public abstract boolean commitCorrection(CorrectionInfo arg0);

    public abstract boolean commitText(CharSequence arg0, int arg1);

    public default boolean commitText(CharSequence arg0, int arg1, TextAttribute arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputConnection;->commitText(Ljava/lang/CharSequence;ILandroid/view/inputmethod/TextAttribute;)Z");
    }

    public abstract boolean deleteSurroundingText(int arg0, int arg1);

    public abstract boolean deleteSurroundingTextInCodePoints(int arg0, int arg1);

    public abstract boolean endBatchEdit();

    public abstract boolean finishComposingText();

    public abstract int getCursorCapsMode(int arg0);

    public abstract ExtractedText getExtractedText(ExtractedTextRequest arg0, int arg1);

    public abstract Handler getHandler();

    public abstract CharSequence getSelectedText(int arg0);

    public default SurroundingText getSurroundingText(int arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputConnection;->getSurroundingText(III)Landroid/view/inputmethod/SurroundingText;");
    }

    public abstract CharSequence getTextAfterCursor(int arg0, int arg1);

    public abstract CharSequence getTextBeforeCursor(int arg0, int arg1);

    public abstract boolean performContextMenuAction(int arg0);

    public abstract boolean performEditorAction(int arg0);

    public default void performHandwritingGesture(HandwritingGesture arg0, Executor arg1, IntConsumer arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputConnection;->performHandwritingGesture(Landroid/view/inputmethod/HandwritingGesture;Ljava/util/concurrent/Executor;Ljava/util/function/IntConsumer;)V");
    }

    public abstract boolean performPrivateCommand(String arg0, Bundle arg1);

    public default boolean performSpellCheck() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputConnection;->performSpellCheck()Z");
    }

    public default boolean previewHandwritingGesture(PreviewableHandwritingGesture arg0, CancellationSignal arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputConnection;->previewHandwritingGesture(Landroid/view/inputmethod/PreviewableHandwritingGesture;Landroid/os/CancellationSignal;)Z");
    }

    public default boolean replaceText(int arg0, int arg1, CharSequence arg2, int arg3, TextAttribute arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputConnection;->replaceText(IILjava/lang/CharSequence;ILandroid/view/inputmethod/TextAttribute;)Z");
    }

    public abstract boolean reportFullscreenMode(boolean arg0);

    public abstract boolean requestCursorUpdates(int arg0);

    public default boolean requestCursorUpdates(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputConnection;->requestCursorUpdates(II)Z");
    }

    public default void requestTextBoundsInfo(RectF arg0, Executor arg1, Consumer<TextBoundsInfoResult> arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputConnection;->requestTextBoundsInfo(Landroid/graphics/RectF;Ljava/util/concurrent/Executor;Ljava/util/function/Consumer;)V");
    }

    public abstract boolean sendKeyEvent(KeyEvent arg0);

    public abstract boolean setComposingRegion(int arg0, int arg1);

    public default boolean setComposingRegion(int arg0, int arg1, TextAttribute arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputConnection;->setComposingRegion(IILandroid/view/inputmethod/TextAttribute;)Z");
    }

    public abstract boolean setComposingText(CharSequence arg0, int arg1);

    public default boolean setComposingText(CharSequence arg0, int arg1, TextAttribute arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputConnection;->setComposingText(Ljava/lang/CharSequence;ILandroid/view/inputmethod/TextAttribute;)Z");
    }

    public default boolean setImeConsumesInput(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputConnection;->setImeConsumesInput(Z)Z");
    }

    public abstract boolean setSelection(int arg0, int arg1);

    public default TextSnapshot takeSnapshot() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/inputmethod/InputConnection;->takeSnapshot()Landroid/view/inputmethod/TextSnapshot;");
    }
    }
