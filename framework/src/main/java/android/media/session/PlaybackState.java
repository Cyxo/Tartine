package android.media.session;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

    public class PlaybackState implements Parcelable {

    public static final long ACTION_FAST_FORWARD = 64L;
    public static final long ACTION_PAUSE = 2L;
    public static final long ACTION_PLAY = 4L;
    public static final long ACTION_PLAY_FROM_MEDIA_ID = 1024L;
    public static final long ACTION_PLAY_FROM_SEARCH = 2048L;
    public static final long ACTION_PLAY_FROM_URI = 8192L;
    public static final long ACTION_PLAY_PAUSE = 512L;
    public static final long ACTION_PREPARE = 16384L;
    public static final long ACTION_PREPARE_FROM_MEDIA_ID = 32768L;
    public static final long ACTION_PREPARE_FROM_SEARCH = 65536L;
    public static final long ACTION_PREPARE_FROM_URI = 131072L;
    public static final long ACTION_REWIND = 8L;
    public static final long ACTION_SEEK_TO = 256L;
    public static final long ACTION_SET_PLAYBACK_SPEED = 4194304L;
    public static final long ACTION_SET_RATING = 128L;
    public static final long ACTION_SKIP_TO_NEXT = 32L;
    public static final long ACTION_SKIP_TO_PREVIOUS = 16L;
    public static final long ACTION_SKIP_TO_QUEUE_ITEM = 4096L;
    public static final long ACTION_STOP = 1L;
    public static Parcelable.Creator CREATOR;
    public static final long PLAYBACK_POSITION_UNKNOWN = -1L;
    public static final int STATE_BUFFERING = 6;
    public static final int STATE_CONNECTING = 8;
    public static final int STATE_ERROR = 7;
    public static final int STATE_FAST_FORWARDING = 4;
    public static final int STATE_NONE = 0;
    public static final int STATE_PAUSED = 2;
    public static final int STATE_PLAYING = 3;
    public static final int STATE_REWINDING = 5;
    public static final int STATE_SKIPPING_TO_NEXT = 10;
    public static final int STATE_SKIPPING_TO_PREVIOUS = 9;
    public static final int STATE_SKIPPING_TO_QUEUE_ITEM = 11;
    public static final int STATE_STOPPED = 1;

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/PlaybackState;->describeContents()I");
    }

    public long getActions() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/PlaybackState;->getActions()J");
    }

    public long getActiveQueueItemId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/PlaybackState;->getActiveQueueItemId()J");
    }

    public long getBufferedPosition() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/PlaybackState;->getBufferedPosition()J");
    }

    public List<PlaybackState.CustomAction> getCustomActions() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/PlaybackState;->getCustomActions()Ljava/util/List;");
    }

    public CharSequence getErrorMessage() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/PlaybackState;->getErrorMessage()Ljava/lang/CharSequence;");
    }

    public Bundle getExtras() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/PlaybackState;->getExtras()Landroid/os/Bundle;");
    }

    public long getLastPositionUpdateTime() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/PlaybackState;->getLastPositionUpdateTime()J");
    }

    public float getPlaybackSpeed() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/PlaybackState;->getPlaybackSpeed()F");
    }

    public long getPosition() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/PlaybackState;->getPosition()J");
    }

    public int getState() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/PlaybackState;->getState()I");
    }

    public boolean isActive() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/PlaybackState;->isActive()Z");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/PlaybackState;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/PlaybackState;->writeToParcel(Landroid/os/Parcel;I)V");
    }
        public static class CustomAction implements Parcelable {

        public static Parcelable.Creator CREATOR;

        public int describeContents() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/PlaybackState$CustomAction;->describeContents()I");
        }

        public String getAction() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/PlaybackState$CustomAction;->getAction()Ljava/lang/String;");
        }

        public Bundle getExtras() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/PlaybackState$CustomAction;->getExtras()Landroid/os/Bundle;");
        }

        public int getIcon() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/PlaybackState$CustomAction;->getIcon()I");
        }

        public CharSequence getName() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/PlaybackState$CustomAction;->getName()Ljava/lang/CharSequence;");
        }

        public String toString() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/PlaybackState$CustomAction;->toString()Ljava/lang/String;");
        }

        public void writeToParcel(Parcel arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/PlaybackState$CustomAction;->writeToParcel(Landroid/os/Parcel;I)V");
        }
        }
    }
