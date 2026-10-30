package android.media.session;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaDescription;
import android.media.MediaMetadata;
import android.media.Rating;
import android.media.VolumeProvider;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.ResultReceiver;
import java.util.List;

    public class MediaSession {

    public static final int FLAG_HANDLES_MEDIA_BUTTONS = 1;
    public static final int FLAG_HANDLES_TRANSPORT_CONTROLS = 2;

    public MediaSession(Context arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;-><init>(Landroid/content/Context;Ljava/lang/String;)V");
    }

    public MediaSession(Context arg0, String arg1, Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;-><init>(Landroid/content/Context;Ljava/lang/String;Landroid/os/Bundle;)V");
    }

    public MediaController getController() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->getController()Landroid/media/session/MediaController;");
    }

    public MediaSessionManager.RemoteUserInfo getCurrentControllerInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->getCurrentControllerInfo()Landroid/media/session/MediaSessionManager$RemoteUserInfo;");
    }

    public MediaSession.Token getSessionToken() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->getSessionToken()Landroid/media/session/MediaSession$Token;");
    }

    public boolean isActive() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->isActive()Z");
    }

    public void release() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->release()V");
    }

    public void sendSessionEvent(String arg0, Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->sendSessionEvent(Ljava/lang/String;Landroid/os/Bundle;)V");
    }

    public void setActive(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->setActive(Z)V");
    }

    public void setCallback(MediaSession.Callback arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->setCallback(Landroid/media/session/MediaSession$Callback;)V");
    }

    public void setCallback(MediaSession.Callback arg0, Handler arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->setCallback(Landroid/media/session/MediaSession$Callback;Landroid/os/Handler;)V");
    }

    public void setExtras(Bundle arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->setExtras(Landroid/os/Bundle;)V");
    }

    public void setFlags(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->setFlags(I)V");
    }

    public void setMediaButtonBroadcastReceiver(ComponentName arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->setMediaButtonBroadcastReceiver(Landroid/content/ComponentName;)V");
    }

    public void setMediaButtonReceiver(PendingIntent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->setMediaButtonReceiver(Landroid/app/PendingIntent;)V");
    }

    public void setMetadata(MediaMetadata arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->setMetadata(Landroid/media/MediaMetadata;)V");
    }

    public void setPlaybackState(PlaybackState arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->setPlaybackState(Landroid/media/session/PlaybackState;)V");
    }

    public void setPlaybackToLocal(AudioAttributes arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->setPlaybackToLocal(Landroid/media/AudioAttributes;)V");
    }

    public void setPlaybackToRemote(VolumeProvider arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->setPlaybackToRemote(Landroid/media/VolumeProvider;)V");
    }

    public void setQueue(List<MediaSession.QueueItem> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->setQueue(Ljava/util/List;)V");
    }

    public void setQueueTitle(CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->setQueueTitle(Ljava/lang/CharSequence;)V");
    }

    public void setRatingType(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->setRatingType(I)V");
    }

    public void setSessionActivity(PendingIntent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->setSessionActivity(Landroid/app/PendingIntent;)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public MediaSession() {
        }
        public static class Callback {

        public Callback() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;-><init>()V");
        }

        public void onCommand(String arg0, Bundle arg1, ResultReceiver arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onCommand(Ljava/lang/String;Landroid/os/Bundle;Landroid/os/ResultReceiver;)V");
        }

        public void onCustomAction(String arg0, Bundle arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onCustomAction(Ljava/lang/String;Landroid/os/Bundle;)V");
        }

        public void onFastForward() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onFastForward()V");
        }

        public boolean onMediaButtonEvent(Intent arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onMediaButtonEvent(Landroid/content/Intent;)Z");
        }

        public void onPause() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onPause()V");
        }

        public void onPlay() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onPlay()V");
        }

        public void onPlayFromMediaId(String arg0, Bundle arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onPlayFromMediaId(Ljava/lang/String;Landroid/os/Bundle;)V");
        }

        public void onPlayFromSearch(String arg0, Bundle arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onPlayFromSearch(Ljava/lang/String;Landroid/os/Bundle;)V");
        }

        public void onPlayFromUri(Uri arg0, Bundle arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onPlayFromUri(Landroid/net/Uri;Landroid/os/Bundle;)V");
        }

        public void onPrepare() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onPrepare()V");
        }

        public void onPrepareFromMediaId(String arg0, Bundle arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onPrepareFromMediaId(Ljava/lang/String;Landroid/os/Bundle;)V");
        }

        public void onPrepareFromSearch(String arg0, Bundle arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onPrepareFromSearch(Ljava/lang/String;Landroid/os/Bundle;)V");
        }

        public void onPrepareFromUri(Uri arg0, Bundle arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onPrepareFromUri(Landroid/net/Uri;Landroid/os/Bundle;)V");
        }

        public void onRewind() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onRewind()V");
        }

        public void onSeekTo(long arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onSeekTo(J)V");
        }

        public void onSetPlaybackSpeed(float arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onSetPlaybackSpeed(F)V");
        }

        public void onSetRating(Rating arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onSetRating(Landroid/media/Rating;)V");
        }

        public void onSkipToNext() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onSkipToNext()V");
        }

        public void onSkipToPrevious() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onSkipToPrevious()V");
        }

        public void onSkipToQueueItem(long arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onSkipToQueueItem(J)V");
        }

        public void onStop() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onStop()V");
        }
        }
        public static class QueueItem implements Parcelable {

        public static Parcelable.Creator CREATOR;
        public static final int UNKNOWN_ID = -1;

        public QueueItem(MediaDescription arg0, long arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$QueueItem;-><init>(Landroid/media/MediaDescription;J)V");
        }

        public int describeContents() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$QueueItem;->describeContents()I");
        }

        public boolean equals(Object arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$QueueItem;->equals(Ljava/lang/Object;)Z");
        }

        public MediaDescription getDescription() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$QueueItem;->getDescription()Landroid/media/MediaDescription;");
        }

        public long getQueueId() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$QueueItem;->getQueueId()J");
        }

        public String toString() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$QueueItem;->toString()Ljava/lang/String;");
        }

        public void writeToParcel(Parcel arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$QueueItem;->writeToParcel(Landroid/os/Parcel;I)V");
        }

            /** Scratch mirror constructor (SkeletonGen). */
            public QueueItem() {
            }
        }
        public static class Token implements Parcelable {

        public static Parcelable.Creator CREATOR;

        public int describeContents() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Token;->describeContents()I");
        }

        public boolean equals(Object arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Token;->equals(Ljava/lang/Object;)Z");
        }

        public int hashCode() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Token;->hashCode()I");
        }

        public void writeToParcel(Parcel arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Token;->writeToParcel(Landroid/os/Parcel;I)V");
        }
        }
    }
