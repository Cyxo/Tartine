package android.content;

    public class IntentSender implements android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentSender;->describeContents()I");
    }

    public boolean equals(java.lang.Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentSender;->equals(Ljava/lang/Object;)Z");
    }

    public java.lang.String getCreatorPackage() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentSender;->getCreatorPackage()Ljava/lang/String;");
    }

    public int getCreatorUid() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentSender;->getCreatorUid()I");
    }

    public android.os.UserHandle getCreatorUserHandle() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentSender;->getCreatorUserHandle()Landroid/os/UserHandle;");
    }

    public java.lang.String getTargetPackage() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentSender;->getTargetPackage()Ljava/lang/String;");
    }

    public int hashCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentSender;->hashCode()I");
    }

    public static android.content.IntentSender readIntentSenderOrNullFromParcel(android.os.Parcel arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentSender;->readIntentSenderOrNullFromParcel(Landroid/os/Parcel;)Landroid/content/IntentSender;");
    }

    public void sendIntent(android.content.Context arg0, int arg1, android.content.Intent arg2, android.content.IntentSender.OnFinished arg3, android.os.Handler arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentSender;->sendIntent(Landroid/content/Context;ILandroid/content/Intent;Landroid/content/IntentSender$OnFinished;Landroid/os/Handler;)V");
    }

    public void sendIntent(android.content.Context arg0, int arg1, android.content.Intent arg2, android.content.IntentSender.OnFinished arg3, android.os.Handler arg4, java.lang.String arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentSender;->sendIntent(Landroid/content/Context;ILandroid/content/Intent;Landroid/content/IntentSender$OnFinished;Landroid/os/Handler;Ljava/lang/String;)V");
    }

    public void sendIntent(android.content.Context arg0, int arg1, android.content.Intent arg2, java.lang.String arg3, android.os.Bundle arg4, java.util.concurrent.Executor arg5, android.content.IntentSender.OnFinished arg6) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentSender;->sendIntent(Landroid/content/Context;ILandroid/content/Intent;Ljava/lang/String;Landroid/os/Bundle;Ljava/util/concurrent/Executor;Landroid/content/IntentSender$OnFinished;)V");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentSender;->toString()Ljava/lang/String;");
    }

    public static void writeIntentSenderOrNullToParcel(android.content.IntentSender arg0, android.os.Parcel arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentSender;->writeIntentSenderOrNullToParcel(Landroid/content/IntentSender;Landroid/os/Parcel;)V");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentSender;->writeToParcel(Landroid/os/Parcel;I)V");
    }
        public static interface OnFinished {

        public abstract void onSendFinished(android.content.IntentSender arg0, android.content.Intent arg1, int arg2, java.lang.String arg3, android.os.Bundle arg4);
        }
    }
