package android.media.session;

import android.app.PendingIntent;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaMetadata;
import android.media.Rating;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.view.KeyEvent;
import java.util.List;

    public class MediaController {

    public MediaController(Context arg0, MediaSession.Token arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController;-><init>(Landroid/content/Context;Landroid/media/session/MediaSession$Token;)V");
    }

    public void adjustVolume(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController;->adjustVolume(II)V");
    }

    public boolean dispatchMediaButtonEvent(KeyEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController;->dispatchMediaButtonEvent(Landroid/view/KeyEvent;)Z");
    }

    public Bundle getExtras() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController;->getExtras()Landroid/os/Bundle;");
    }

    public long getFlags() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController;->getFlags()J");
    }

    public MediaMetadata getMetadata() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController;->getMetadata()Landroid/media/MediaMetadata;");
    }

    public String getPackageName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController;->getPackageName()Ljava/lang/String;");
    }

    public MediaController.PlaybackInfo getPlaybackInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController;->getPlaybackInfo()Landroid/media/session/MediaController$PlaybackInfo;");
    }

    public PlaybackState getPlaybackState() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController;->getPlaybackState()Landroid/media/session/PlaybackState;");
    }

    public List<MediaSession.QueueItem> getQueue() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController;->getQueue()Ljava/util/List;");
    }

    public CharSequence getQueueTitle() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController;->getQueueTitle()Ljava/lang/CharSequence;");
    }

    public int getRatingType() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController;->getRatingType()I");
    }

    public PendingIntent getSessionActivity() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController;->getSessionActivity()Landroid/app/PendingIntent;");
    }

    public Bundle getSessionInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController;->getSessionInfo()Landroid/os/Bundle;");
    }

    public MediaSession.Token getSessionToken() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController;->getSessionToken()Landroid/media/session/MediaSession$Token;");
    }

    public String getTag() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController;->getTag()Ljava/lang/String;");
    }

    public MediaController.TransportControls getTransportControls() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController;->getTransportControls()Landroid/media/session/MediaController$TransportControls;");
    }

    public void registerCallback(MediaController.Callback arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController;->registerCallback(Landroid/media/session/MediaController$Callback;)V");
    }

    public void registerCallback(MediaController.Callback arg0, Handler arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController;->registerCallback(Landroid/media/session/MediaController$Callback;Landroid/os/Handler;)V");
    }

    public void sendCommand(String arg0, Bundle arg1, ResultReceiver arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController;->sendCommand(Ljava/lang/String;Landroid/os/Bundle;Landroid/os/ResultReceiver;)V");
    }

    public void setVolumeTo(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController;->setVolumeTo(II)V");
    }

    public void unregisterCallback(MediaController.Callback arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController;->unregisterCallback(Landroid/media/session/MediaController$Callback;)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public MediaController() {
        }
        public static class Callback {

        public Callback() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$Callback;-><init>()V");
        }

        public void onAudioInfoChanged(MediaController.PlaybackInfo arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$Callback;->onAudioInfoChanged(Landroid/media/session/MediaController$PlaybackInfo;)V");
        }

        public void onExtrasChanged(Bundle arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$Callback;->onExtrasChanged(Landroid/os/Bundle;)V");
        }

        public void onMetadataChanged(MediaMetadata arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$Callback;->onMetadataChanged(Landroid/media/MediaMetadata;)V");
        }

        public void onPlaybackStateChanged(PlaybackState arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$Callback;->onPlaybackStateChanged(Landroid/media/session/PlaybackState;)V");
        }

        public void onQueueChanged(List<MediaSession.QueueItem> arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$Callback;->onQueueChanged(Ljava/util/List;)V");
        }

        public void onQueueTitleChanged(CharSequence arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$Callback;->onQueueTitleChanged(Ljava/lang/CharSequence;)V");
        }

        public void onSessionDestroyed() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$Callback;->onSessionDestroyed()V");
        }

        public void onSessionEvent(String arg0, Bundle arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$Callback;->onSessionEvent(Ljava/lang/String;Landroid/os/Bundle;)V");
        }
        }
        public static class PlaybackInfo implements Parcelable {

        public static Parcelable.Creator CREATOR;
        public static final int PLAYBACK_TYPE_LOCAL = 1;
        public static final int PLAYBACK_TYPE_REMOTE = 2;

        public int describeContents() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$PlaybackInfo;->describeContents()I");
        }

        public AudioAttributes getAudioAttributes() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$PlaybackInfo;->getAudioAttributes()Landroid/media/AudioAttributes;");
        }

        public int getCurrentVolume() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$PlaybackInfo;->getCurrentVolume()I");
        }

        public int getMaxVolume() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$PlaybackInfo;->getMaxVolume()I");
        }

        public int getPlaybackType() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$PlaybackInfo;->getPlaybackType()I");
        }

        public int getVolumeControl() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$PlaybackInfo;->getVolumeControl()I");
        }

        public String getVolumeControlId() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$PlaybackInfo;->getVolumeControlId()Ljava/lang/String;");
        }

        public String toString() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$PlaybackInfo;->toString()Ljava/lang/String;");
        }

        public void writeToParcel(Parcel arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$PlaybackInfo;->writeToParcel(Landroid/os/Parcel;I)V");
        }
        }
        public static class TransportControls {

        public void fastForward() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$TransportControls;->fastForward()V");
        }

        public void pause() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$TransportControls;->pause()V");
        }

        public void play() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$TransportControls;->play()V");
        }

        public void playFromMediaId(String arg0, Bundle arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$TransportControls;->playFromMediaId(Ljava/lang/String;Landroid/os/Bundle;)V");
        }

        public void playFromSearch(String arg0, Bundle arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$TransportControls;->playFromSearch(Ljava/lang/String;Landroid/os/Bundle;)V");
        }

        public void playFromUri(Uri arg0, Bundle arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$TransportControls;->playFromUri(Landroid/net/Uri;Landroid/os/Bundle;)V");
        }

        public void prepare() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$TransportControls;->prepare()V");
        }

        public void prepareFromMediaId(String arg0, Bundle arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$TransportControls;->prepareFromMediaId(Ljava/lang/String;Landroid/os/Bundle;)V");
        }

        public void prepareFromSearch(String arg0, Bundle arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$TransportControls;->prepareFromSearch(Ljava/lang/String;Landroid/os/Bundle;)V");
        }

        public void prepareFromUri(Uri arg0, Bundle arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$TransportControls;->prepareFromUri(Landroid/net/Uri;Landroid/os/Bundle;)V");
        }

        public void rewind() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$TransportControls;->rewind()V");
        }

        public void seekTo(long arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$TransportControls;->seekTo(J)V");
        }

        public void sendCustomAction(PlaybackState.CustomAction arg0, Bundle arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$TransportControls;->sendCustomAction(Landroid/media/session/PlaybackState$CustomAction;Landroid/os/Bundle;)V");
        }

        public void sendCustomAction(String arg0, Bundle arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$TransportControls;->sendCustomAction(Ljava/lang/String;Landroid/os/Bundle;)V");
        }

        public void setPlaybackSpeed(float arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$TransportControls;->setPlaybackSpeed(F)V");
        }

        public void setRating(Rating arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$TransportControls;->setRating(Landroid/media/Rating;)V");
        }

        public void skipToNext() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$TransportControls;->skipToNext()V");
        }

        public void skipToPrevious() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$TransportControls;->skipToPrevious()V");
        }

        public void skipToQueueItem(long arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$TransportControls;->skipToQueueItem(J)V");
        }

        public void stop() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaController$TransportControls;->stop()V");
        }

            /** Scratch mirror constructor (SkeletonGen). */
            public TransportControls() {
            }
        }
    }
