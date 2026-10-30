package android.app;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.UserHandle;

    public class PendingIntent implements Parcelable {

    public static Parcelable.Creator CREATOR;
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

    public boolean equals(Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->equals(Ljava/lang/Object;)Z");
    }

    public static PendingIntent getActivities(Context arg0, int arg1, Intent[] arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->getActivities(Landroid/content/Context;I[Landroid/content/Intent;I)Landroid/app/PendingIntent;");
    }

    public static PendingIntent getActivities(Context arg0, int arg1, Intent[] arg2, int arg3, Bundle arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->getActivities(Landroid/content/Context;I[Landroid/content/Intent;ILandroid/os/Bundle;)Landroid/app/PendingIntent;");
    }

    public static PendingIntent getActivity(Context arg0, int arg1, Intent arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->getActivity(Landroid/content/Context;ILandroid/content/Intent;I)Landroid/app/PendingIntent;");
    }

    public static PendingIntent getActivity(Context arg0, int arg1, Intent arg2, int arg3, Bundle arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->getActivity(Landroid/content/Context;ILandroid/content/Intent;ILandroid/os/Bundle;)Landroid/app/PendingIntent;");
    }

    public static PendingIntent getBroadcast(Context arg0, int arg1, Intent arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->getBroadcast(Landroid/content/Context;ILandroid/content/Intent;I)Landroid/app/PendingIntent;");
    }

    public String getCreatorPackage() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->getCreatorPackage()Ljava/lang/String;");
    }

    public int getCreatorUid() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->getCreatorUid()I");
    }

    public UserHandle getCreatorUserHandle() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->getCreatorUserHandle()Landroid/os/UserHandle;");
    }

    public static PendingIntent getForegroundService(Context arg0, int arg1, Intent arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->getForegroundService(Landroid/content/Context;ILandroid/content/Intent;I)Landroid/app/PendingIntent;");
    }

    public IntentSender getIntentSender() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->getIntentSender()Landroid/content/IntentSender;");
    }

    public static PendingIntent getService(Context arg0, int arg1, Intent arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->getService(Landroid/content/Context;ILandroid/content/Intent;I)Landroid/app/PendingIntent;");
    }

    public String getTargetPackage() {
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

    public static PendingIntent readPendingIntentOrNullFromParcel(Parcel arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->readPendingIntentOrNullFromParcel(Landroid/os/Parcel;)Landroid/app/PendingIntent;");
    }

    public void send() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->send()V");
    }

    public void send(Context arg0, int arg1, Intent arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->send(Landroid/content/Context;ILandroid/content/Intent;)V");
    }

    public void send(Context arg0, int arg1, Intent arg2, PendingIntent.OnFinished arg3, Handler arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->send(Landroid/content/Context;ILandroid/content/Intent;Landroid/app/PendingIntent$OnFinished;Landroid/os/Handler;)V");
    }

    public void send(Context arg0, int arg1, Intent arg2, PendingIntent.OnFinished arg3, Handler arg4, String arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->send(Landroid/content/Context;ILandroid/content/Intent;Landroid/app/PendingIntent$OnFinished;Landroid/os/Handler;Ljava/lang/String;)V");
    }

    public void send(Context arg0, int arg1, Intent arg2, PendingIntent.OnFinished arg3, Handler arg4, String arg5, Bundle arg6) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->send(Landroid/content/Context;ILandroid/content/Intent;Landroid/app/PendingIntent$OnFinished;Landroid/os/Handler;Ljava/lang/String;Landroid/os/Bundle;)V");
    }

    public void send(Bundle arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->send(Landroid/os/Bundle;)V");
    }

    public void send(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->send(I)V");
    }

    public void send(int arg0, PendingIntent.OnFinished arg1, Handler arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->send(ILandroid/app/PendingIntent$OnFinished;Landroid/os/Handler;)V");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->toString()Ljava/lang/String;");
    }

    public static void writePendingIntentOrNullToParcel(PendingIntent arg0, Parcel arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->writePendingIntentOrNullToParcel(Landroid/app/PendingIntent;Landroid/os/Parcel;)V");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/app/PendingIntent;->writeToParcel(Landroid/os/Parcel;I)V");
    }
        public static interface OnFinished {

        public abstract void onSendFinished(PendingIntent arg0, Intent arg1, int arg2, String arg3, Bundle arg4);
        }
    }
