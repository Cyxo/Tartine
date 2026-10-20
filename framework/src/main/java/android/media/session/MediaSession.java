package android.media.session;

    public class MediaSession {

    public static final int FLAG_HANDLES_MEDIA_BUTTONS = 1;
    public static final int FLAG_HANDLES_TRANSPORT_CONTROLS = 2;

    public MediaSession(android.content.Context arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;-><init>(Landroid/content/Context;Ljava/lang/String;)V");
    }

    public MediaSession(android.content.Context arg0, java.lang.String arg1, android.os.Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;-><init>(Landroid/content/Context;Ljava/lang/String;Landroid/os/Bundle;)V");
    }

    public android.media.session.MediaController getController() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->getController()Landroid/media/session/MediaController;");
    }

    public android.media.session.MediaSessionManager.RemoteUserInfo getCurrentControllerInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->getCurrentControllerInfo()Landroid/media/session/MediaSessionManager$RemoteUserInfo;");
    }

    public android.media.session.MediaSession.Token getSessionToken() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->getSessionToken()Landroid/media/session/MediaSession$Token;");
    }

    public boolean isActive() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->isActive()Z");
    }

    public void release() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->release()V");
    }

    public void sendSessionEvent(java.lang.String arg0, android.os.Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->sendSessionEvent(Ljava/lang/String;Landroid/os/Bundle;)V");
    }

    public void setActive(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->setActive(Z)V");
    }

    public void setCallback(android.media.session.MediaSession.Callback arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->setCallback(Landroid/media/session/MediaSession$Callback;)V");
    }

    public void setCallback(android.media.session.MediaSession.Callback arg0, android.os.Handler arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->setCallback(Landroid/media/session/MediaSession$Callback;Landroid/os/Handler;)V");
    }

    public void setExtras(android.os.Bundle arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->setExtras(Landroid/os/Bundle;)V");
    }

    public void setFlags(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->setFlags(I)V");
    }

    public void setMediaButtonBroadcastReceiver(android.content.ComponentName arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->setMediaButtonBroadcastReceiver(Landroid/content/ComponentName;)V");
    }

    public void setMediaButtonReceiver(android.app.PendingIntent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->setMediaButtonReceiver(Landroid/app/PendingIntent;)V");
    }

    public void setMetadata(android.media.MediaMetadata arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->setMetadata(Landroid/media/MediaMetadata;)V");
    }

    public void setPlaybackState(android.media.session.PlaybackState arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->setPlaybackState(Landroid/media/session/PlaybackState;)V");
    }

    public void setPlaybackToLocal(android.media.AudioAttributes arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->setPlaybackToLocal(Landroid/media/AudioAttributes;)V");
    }

    public void setPlaybackToRemote(android.media.VolumeProvider arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->setPlaybackToRemote(Landroid/media/VolumeProvider;)V");
    }

    public void setQueue(java.util.List<android.media.session.MediaSession.QueueItem> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->setQueue(Ljava/util/List;)V");
    }

    public void setQueueTitle(java.lang.CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->setQueueTitle(Ljava/lang/CharSequence;)V");
    }

    public void setRatingType(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->setRatingType(I)V");
    }

    public void setSessionActivity(android.app.PendingIntent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession;->setSessionActivity(Landroid/app/PendingIntent;)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public MediaSession() {
        }
        public static class Callback {

        public Callback() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;-><init>()V");
        }

        public void onCommand(java.lang.String arg0, android.os.Bundle arg1, android.os.ResultReceiver arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onCommand(Ljava/lang/String;Landroid/os/Bundle;Landroid/os/ResultReceiver;)V");
        }

        public void onCustomAction(java.lang.String arg0, android.os.Bundle arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onCustomAction(Ljava/lang/String;Landroid/os/Bundle;)V");
        }

        public void onFastForward() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onFastForward()V");
        }

        public boolean onMediaButtonEvent(android.content.Intent arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onMediaButtonEvent(Landroid/content/Intent;)Z");
        }

        public void onPause() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onPause()V");
        }

        public void onPlay() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onPlay()V");
        }

        public void onPlayFromMediaId(java.lang.String arg0, android.os.Bundle arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onPlayFromMediaId(Ljava/lang/String;Landroid/os/Bundle;)V");
        }

        public void onPlayFromSearch(java.lang.String arg0, android.os.Bundle arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onPlayFromSearch(Ljava/lang/String;Landroid/os/Bundle;)V");
        }

        public void onPlayFromUri(android.net.Uri arg0, android.os.Bundle arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onPlayFromUri(Landroid/net/Uri;Landroid/os/Bundle;)V");
        }

        public void onPrepare() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onPrepare()V");
        }

        public void onPrepareFromMediaId(java.lang.String arg0, android.os.Bundle arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onPrepareFromMediaId(Ljava/lang/String;Landroid/os/Bundle;)V");
        }

        public void onPrepareFromSearch(java.lang.String arg0, android.os.Bundle arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Callback;->onPrepareFromSearch(Ljava/lang/String;Landroid/os/Bundle;)V");
        }

        public void onPrepareFromUri(android.net.Uri arg0, android.os.Bundle arg1) {
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

        public void onSetRating(android.media.Rating arg0) {
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
        public static class QueueItem implements android.os.Parcelable {

        public static android.os.Parcelable.Creator CREATOR;
        public static final int UNKNOWN_ID = -1;

        public QueueItem(android.media.MediaDescription arg0, long arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$QueueItem;-><init>(Landroid/media/MediaDescription;J)V");
        }

        public int describeContents() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$QueueItem;->describeContents()I");
        }

        public boolean equals(java.lang.Object arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$QueueItem;->equals(Ljava/lang/Object;)Z");
        }

        public android.media.MediaDescription getDescription() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$QueueItem;->getDescription()Landroid/media/MediaDescription;");
        }

        public long getQueueId() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$QueueItem;->getQueueId()J");
        }

        public java.lang.String toString() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$QueueItem;->toString()Ljava/lang/String;");
        }

        public void writeToParcel(android.os.Parcel arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$QueueItem;->writeToParcel(Landroid/os/Parcel;I)V");
        }

            /** Scratch mirror constructor (SkeletonGen). */
            public QueueItem() {
            }
        }
        public static class Token implements android.os.Parcelable {

        public static android.os.Parcelable.Creator CREATOR;

        public int describeContents() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Token;->describeContents()I");
        }

        public boolean equals(java.lang.Object arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Token;->equals(Ljava/lang/Object;)Z");
        }

        public int hashCode() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Token;->hashCode()I");
        }

        public void writeToParcel(android.os.Parcel arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSession$Token;->writeToParcel(Landroid/os/Parcel;I)V");
        }
        }
    }
