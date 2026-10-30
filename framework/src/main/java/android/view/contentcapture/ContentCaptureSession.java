package android.view.contentcapture;

import android.graphics.Insets;
import android.view.View;
import android.view.ViewStructure;
import android.view.autofill.AutofillId;
import java.util.List;

    public class ContentCaptureSession implements AutoCloseable {

    public void close() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/contentcapture/ContentCaptureSession;->close()V");
    }

    public final ContentCaptureSession createContentCaptureSession(ContentCaptureContext arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/contentcapture/ContentCaptureSession;->createContentCaptureSession(Landroid/view/contentcapture/ContentCaptureContext;)Landroid/view/contentcapture/ContentCaptureSession;");
    }

    public final void destroy() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/contentcapture/ContentCaptureSession;->destroy()V");
    }

    public void flush() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/contentcapture/ContentCaptureSession;->flush()V");
    }

    public final ContentCaptureContext getContentCaptureContext() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/contentcapture/ContentCaptureSession;->getContentCaptureContext()Landroid/view/contentcapture/ContentCaptureContext;");
    }

    public final ContentCaptureSessionId getContentCaptureSessionId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/contentcapture/ContentCaptureSession;->getContentCaptureSessionId()Landroid/view/contentcapture/ContentCaptureSessionId;");
    }

    public AutofillId newAutofillId(AutofillId arg0, long arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/contentcapture/ContentCaptureSession;->newAutofillId(Landroid/view/autofill/AutofillId;J)Landroid/view/autofill/AutofillId;");
    }

    public final ViewStructure newViewStructure(View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/contentcapture/ContentCaptureSession;->newViewStructure(Landroid/view/View;)Landroid/view/ViewStructure;");
    }

    public final ViewStructure newVirtualViewStructure(AutofillId arg0, long arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/contentcapture/ContentCaptureSession;->newVirtualViewStructure(Landroid/view/autofill/AutofillId;J)Landroid/view/ViewStructure;");
    }

    public final void notifySessionPaused() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/contentcapture/ContentCaptureSession;->notifySessionPaused()V");
    }

    public final void notifySessionResumed() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/contentcapture/ContentCaptureSession;->notifySessionResumed()V");
    }

    public final void notifyViewAppeared(ViewStructure arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/contentcapture/ContentCaptureSession;->notifyViewAppeared(Landroid/view/ViewStructure;)V");
    }

    public final void notifyViewDisappeared(AutofillId arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/contentcapture/ContentCaptureSession;->notifyViewDisappeared(Landroid/view/autofill/AutofillId;)V");
    }

    public final void notifyViewInsetsChanged(Insets arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/contentcapture/ContentCaptureSession;->notifyViewInsetsChanged(Landroid/graphics/Insets;)V");
    }

    public final void notifyViewTextChanged(AutofillId arg0, CharSequence arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/contentcapture/ContentCaptureSession;->notifyViewTextChanged(Landroid/view/autofill/AutofillId;Ljava/lang/CharSequence;)V");
    }

    public final void notifyViewsAppeared(List<ViewStructure> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/contentcapture/ContentCaptureSession;->notifyViewsAppeared(Ljava/util/List;)V");
    }

    public final void notifyViewsDisappeared(AutofillId arg0, long[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/contentcapture/ContentCaptureSession;->notifyViewsDisappeared(Landroid/view/autofill/AutofillId;[J)V");
    }

    public final void setContentCaptureContext(ContentCaptureContext arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/contentcapture/ContentCaptureSession;->setContentCaptureContext(Landroid/view/contentcapture/ContentCaptureContext;)V");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/contentcapture/ContentCaptureSession;->toString()Ljava/lang/String;");
    }
    }
