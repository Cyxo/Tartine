package android.app;

    public class PendingIntent implements android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;
    public static final int FLAG_ALLOW_UNSAFE_IMPLICIT_INTENT = 16777216;
    public static final int FLAG_CANCEL_CURRENT = 268435456;
    public static final int FLAG_IMMUTABLE = 67108864;
    public static final int FLAG_MUTABLE = 33554432;
    public static final int FLAG_NO_CREATE = 536870912;
    public static final int FLAG_ONE_SHOT = 1073741824;
    public static final int FLAG_UPDATE_CURRENT = 134217728;

    public void cancel() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->cancel()V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->describeContents()I");
    }

    public boolean equals(java.lang.Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->equals(Ljava/lang/Object;)Z");
    }

    public static android.app.PendingIntent getActivities(android.content.Context arg0, int arg1, android.content.Intent[] arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->getActivities(Landroid/content/Context;I[Landroid/content/Intent;I)Landroid/app/PendingIntent;");
    }

    public static android.app.PendingIntent getActivities(android.content.Context arg0, int arg1, android.content.Intent[] arg2, int arg3, android.os.Bundle arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->getActivities(Landroid/content/Context;I[Landroid/content/Intent;ILandroid/os/Bundle;)Landroid/app/PendingIntent;");
    }

    public static android.app.PendingIntent getActivity(android.content.Context arg0, int arg1, android.content.Intent arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->getActivity(Landroid/content/Context;ILandroid/content/Intent;I)Landroid/app/PendingIntent;");
    }

    public static android.app.PendingIntent getActivity(android.content.Context arg0, int arg1, android.content.Intent arg2, int arg3, android.os.Bundle arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->getActivity(Landroid/content/Context;ILandroid/content/Intent;ILandroid/os/Bundle;)Landroid/app/PendingIntent;");
    }

    public static android.app.PendingIntent getBroadcast(android.content.Context arg0, int arg1, android.content.Intent arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->getBroadcast(Landroid/content/Context;ILandroid/content/Intent;I)Landroid/app/PendingIntent;");
    }

    public java.lang.String getCreatorPackage() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->getCreatorPackage()Ljava/lang/String;");
    }

    public int getCreatorUid() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->getCreatorUid()I");
    }

    public android.os.UserHandle getCreatorUserHandle() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->getCreatorUserHandle()Landroid/os/UserHandle;");
    }

    public static android.app.PendingIntent getForegroundService(android.content.Context arg0, int arg1, android.content.Intent arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->getForegroundService(Landroid/content/Context;ILandroid/content/Intent;I)Landroid/app/PendingIntent;");
    }

    public android.content.IntentSender getIntentSender() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->getIntentSender()Landroid/content/IntentSender;");
    }

    public static android.app.PendingIntent getService(android.content.Context arg0, int arg1, android.content.Intent arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->getService(Landroid/content/Context;ILandroid/content/Intent;I)Landroid/app/PendingIntent;");
    }

    public java.lang.String getTargetPackage() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->getTargetPackage()Ljava/lang/String;");
    }

    public int hashCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->hashCode()I");
    }

    public boolean isActivity() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->isActivity()Z");
    }

    public boolean isBroadcast() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->isBroadcast()Z");
    }

    public boolean isForegroundService() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->isForegroundService()Z");
    }

    public boolean isImmutable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->isImmutable()Z");
    }

    public boolean isService() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->isService()Z");
    }

    public static android.app.PendingIntent readPendingIntentOrNullFromParcel(android.os.Parcel arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->readPendingIntentOrNullFromParcel(Landroid/os/Parcel;)Landroid/app/PendingIntent;");
    }

    public void send() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->send()V");
    }

    public void send(android.content.Context arg0, int arg1, android.content.Intent arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->send(Landroid/content/Context;ILandroid/content/Intent;)V");
    }

    public void send(android.content.Context arg0, int arg1, android.content.Intent arg2, android.app.PendingIntent.OnFinished arg3, android.os.Handler arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->send(Landroid/content/Context;ILandroid/content/Intent;Landroid/app/PendingIntent$OnFinished;Landroid/os/Handler;)V");
    }

    public void send(android.content.Context arg0, int arg1, android.content.Intent arg2, android.app.PendingIntent.OnFinished arg3, android.os.Handler arg4, java.lang.String arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->send(Landroid/content/Context;ILandroid/content/Intent;Landroid/app/PendingIntent$OnFinished;Landroid/os/Handler;Ljava/lang/String;)V");
    }

    public void send(android.content.Context arg0, int arg1, android.content.Intent arg2, android.app.PendingIntent.OnFinished arg3, android.os.Handler arg4, java.lang.String arg5, android.os.Bundle arg6) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->send(Landroid/content/Context;ILandroid/content/Intent;Landroid/app/PendingIntent$OnFinished;Landroid/os/Handler;Ljava/lang/String;Landroid/os/Bundle;)V");
    }

    public void send(android.os.Bundle arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->send(Landroid/os/Bundle;)V");
    }

    public void send(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->send(I)V");
    }

    public void send(int arg0, android.app.PendingIntent.OnFinished arg1, android.os.Handler arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->send(ILandroid/app/PendingIntent$OnFinished;Landroid/os/Handler;)V");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->toString()Ljava/lang/String;");
    }

    public static void writePendingIntentOrNullToParcel(android.app.PendingIntent arg0, android.os.Parcel arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->writePendingIntentOrNullToParcel(Landroid/app/PendingIntent;Landroid/os/Parcel;)V");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->writeToParcel(Landroid/os/Parcel;I)V");
    }
        public static interface OnFinished {

        public abstract void onSendFinished(android.app.PendingIntent arg0, android.content.Intent arg1, int arg2, java.lang.String arg3, android.os.Bundle arg4);
        }
    }
