package android.content;

    public abstract class BroadcastReceiver {

    public BroadcastReceiver() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/BroadcastReceiver;-><init>()V");
    }

    public final void abortBroadcast() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/BroadcastReceiver;->abortBroadcast()V");
    }

    public final void clearAbortBroadcast() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/BroadcastReceiver;->clearAbortBroadcast()V");
    }

    public final boolean getAbortBroadcast() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/BroadcastReceiver;->getAbortBroadcast()Z");
    }

    public final boolean getDebugUnregister() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/BroadcastReceiver;->getDebugUnregister()Z");
    }

    public final int getResultCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/BroadcastReceiver;->getResultCode()I");
    }

    public final java.lang.String getResultData() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/BroadcastReceiver;->getResultData()Ljava/lang/String;");
    }

    public final android.os.Bundle getResultExtras(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/BroadcastReceiver;->getResultExtras(Z)Landroid/os/Bundle;");
    }

    public java.lang.String getSentFromPackage() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/BroadcastReceiver;->getSentFromPackage()Ljava/lang/String;");
    }

    public int getSentFromUid() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/BroadcastReceiver;->getSentFromUid()I");
    }

    public final android.content.BroadcastReceiver.PendingResult goAsync() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/BroadcastReceiver;->goAsync()Landroid/content/BroadcastReceiver$PendingResult;");
    }

    public final boolean isInitialStickyBroadcast() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/BroadcastReceiver;->isInitialStickyBroadcast()Z");
    }

    public final boolean isOrderedBroadcast() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/BroadcastReceiver;->isOrderedBroadcast()Z");
    }

    public abstract void onReceive(android.content.Context arg0, android.content.Intent arg1);

    public android.os.IBinder peekService(android.content.Context arg0, android.content.Intent arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/BroadcastReceiver;->peekService(Landroid/content/Context;Landroid/content/Intent;)Landroid/os/IBinder;");
    }

    public final void setDebugUnregister(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/BroadcastReceiver;->setDebugUnregister(Z)V");
    }

    public final void setOrderedHint(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/BroadcastReceiver;->setOrderedHint(Z)V");
    }

    public final void setResult(int arg0, java.lang.String arg1, android.os.Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/BroadcastReceiver;->setResult(ILjava/lang/String;Landroid/os/Bundle;)V");
    }

    public final void setResultCode(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/BroadcastReceiver;->setResultCode(I)V");
    }

    public final void setResultData(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/BroadcastReceiver;->setResultData(Ljava/lang/String;)V");
    }

    public final void setResultExtras(android.os.Bundle arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/BroadcastReceiver;->setResultExtras(Landroid/os/Bundle;)V");
    }
        public static class PendingResult {

        public final void abortBroadcast() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/BroadcastReceiver$PendingResult;->abortBroadcast()V");
        }

        public final void clearAbortBroadcast() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/BroadcastReceiver$PendingResult;->clearAbortBroadcast()V");
        }

        public final void finish() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/BroadcastReceiver$PendingResult;->finish()V");
        }

        public final boolean getAbortBroadcast() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/BroadcastReceiver$PendingResult;->getAbortBroadcast()Z");
        }

        public final int getResultCode() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/BroadcastReceiver$PendingResult;->getResultCode()I");
        }

        public final java.lang.String getResultData() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/BroadcastReceiver$PendingResult;->getResultData()Ljava/lang/String;");
        }

        public final android.os.Bundle getResultExtras(boolean arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/BroadcastReceiver$PendingResult;->getResultExtras(Z)Landroid/os/Bundle;");
        }

        public final void setResult(int arg0, java.lang.String arg1, android.os.Bundle arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/BroadcastReceiver$PendingResult;->setResult(ILjava/lang/String;Landroid/os/Bundle;)V");
        }

        public final void setResultCode(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/BroadcastReceiver$PendingResult;->setResultCode(I)V");
        }

        public final void setResultData(java.lang.String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/BroadcastReceiver$PendingResult;->setResultData(Ljava/lang/String;)V");
        }

        public final void setResultExtras(android.os.Bundle arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/BroadcastReceiver$PendingResult;->setResultExtras(Landroid/os/Bundle;)V");
        }
        }
    }
