package android.content;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.UserHandle;
import android.util.AndroidException;
import java.util.concurrent.Executor;

    public class IntentSender implements Parcelable {

    public static Parcelable.Creator CREATOR;

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentSender;->describeContents()I");
    }

    public boolean equals(Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentSender;->equals(Ljava/lang/Object;)Z");
    }

    public String getCreatorPackage() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentSender;->getCreatorPackage()Ljava/lang/String;");
    }

    public int getCreatorUid() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentSender;->getCreatorUid()I");
    }

    public UserHandle getCreatorUserHandle() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentSender;->getCreatorUserHandle()Landroid/os/UserHandle;");
    }

    public String getTargetPackage() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentSender;->getTargetPackage()Ljava/lang/String;");
    }

    public int hashCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentSender;->hashCode()I");
    }

    public static IntentSender readIntentSenderOrNullFromParcel(Parcel arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentSender;->readIntentSenderOrNullFromParcel(Landroid/os/Parcel;)Landroid/content/IntentSender;");
    }

    public void sendIntent(Context arg0, int arg1, Intent arg2, IntentSender.OnFinished arg3, Handler arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentSender;->sendIntent(Landroid/content/Context;ILandroid/content/Intent;Landroid/content/IntentSender$OnFinished;Landroid/os/Handler;)V");
    }

    public void sendIntent(Context arg0, int arg1, Intent arg2, IntentSender.OnFinished arg3, Handler arg4, String arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentSender;->sendIntent(Landroid/content/Context;ILandroid/content/Intent;Landroid/content/IntentSender$OnFinished;Landroid/os/Handler;Ljava/lang/String;)V");
    }

    public void sendIntent(Context arg0, int arg1, Intent arg2, String arg3, Bundle arg4, Executor arg5, IntentSender.OnFinished arg6) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentSender;->sendIntent(Landroid/content/Context;ILandroid/content/Intent;Ljava/lang/String;Landroid/os/Bundle;Ljava/util/concurrent/Executor;Landroid/content/IntentSender$OnFinished;)V");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentSender;->toString()Ljava/lang/String;");
    }

    public static void writeIntentSenderOrNullToParcel(IntentSender arg0, Parcel arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentSender;->writeIntentSenderOrNullToParcel(Landroid/content/IntentSender;Landroid/os/Parcel;)V");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/IntentSender;->writeToParcel(Landroid/os/Parcel;I)V");
    }
        public static interface OnFinished {

        public abstract void onSendFinished(IntentSender arg0, Intent arg1, int arg2, String arg3, Bundle arg4);
        }

    /**
     * AOSP aosp-frameworks-base/core/java/android/content/IntentSender.java,
     * class SendIntentException: plain marker exception, three constructors,
     * thrown from IntentSender-send failures ("problems encountered while
     * sending through the IntentSender").
     */
    public static class SendIntentException extends AndroidException {

        public SendIntentException() {
        }

        public SendIntentException(String name) {
            super(name);
        }

        public SendIntentException(Exception cause) {
            super(cause);
        }
    }
}
