package android.media;

    public class AudioAttributes implements android.os.Parcelable {

    public static final int ALLOW_CAPTURE_BY_ALL = 1;
    public static final int ALLOW_CAPTURE_BY_NONE = 3;
    public static final int ALLOW_CAPTURE_BY_SYSTEM = 2;
    public static final int CONTENT_TYPE_MOVIE = 3;
    public static final int CONTENT_TYPE_MUSIC = 2;
    public static final int CONTENT_TYPE_SONIFICATION = 4;
    public static final int CONTENT_TYPE_SPEECH = 1;
    public static final int CONTENT_TYPE_UNKNOWN = 0;
    public static android.os.Parcelable.Creator CREATOR;
    public static final int FLAG_AUDIBILITY_ENFORCED = 1;
    public static final int FLAG_HW_AV_SYNC = 16;
    public static final int FLAG_LOW_LATENCY = 256;
    public static final int SPATIALIZATION_BEHAVIOR_AUTO = 0;
    public static final int SPATIALIZATION_BEHAVIOR_NEVER = 1;
    public static final int USAGE_ALARM = 4;
    public static final int USAGE_ASSISTANCE_ACCESSIBILITY = 11;
    public static final int USAGE_ASSISTANCE_NAVIGATION_GUIDANCE = 12;
    public static final int USAGE_ASSISTANCE_SONIFICATION = 13;
    public static final int USAGE_ASSISTANT = 16;
    public static final int USAGE_GAME = 14;
    public static final int USAGE_MEDIA = 1;
    public static final int USAGE_NOTIFICATION = 5;
    public static final int USAGE_NOTIFICATION_COMMUNICATION_DELAYED = 9;
    public static final int USAGE_NOTIFICATION_COMMUNICATION_INSTANT = 8;
    public static final int USAGE_NOTIFICATION_COMMUNICATION_REQUEST = 7;
    public static final int USAGE_NOTIFICATION_EVENT = 10;
    public static final int USAGE_NOTIFICATION_RINGTONE = 6;
    public static final int USAGE_UNKNOWN = 0;
    public static final int USAGE_VOICE_COMMUNICATION = 2;
    public static final int USAGE_VOICE_COMMUNICATION_SIGNALLING = 3;

    public boolean areHapticChannelsMuted() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/AudioAttributes;->areHapticChannelsMuted()Z");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/AudioAttributes;->describeContents()I");
    }

    public boolean equals(java.lang.Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/AudioAttributes;->equals(Ljava/lang/Object;)Z");
    }

    public int getAllowedCapturePolicy() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/AudioAttributes;->getAllowedCapturePolicy()I");
    }

    public int getContentType() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/AudioAttributes;->getContentType()I");
    }

    public int getFlags() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/AudioAttributes;->getFlags()I");
    }

    public int getSpatializationBehavior() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/AudioAttributes;->getSpatializationBehavior()I");
    }

    public int getUsage() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/AudioAttributes;->getUsage()I");
    }

    public int getVolumeControlStream() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/AudioAttributes;->getVolumeControlStream()I");
    }

    public int hashCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/AudioAttributes;->hashCode()I");
    }

    public boolean isContentSpatialized() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/AudioAttributes;->isContentSpatialized()Z");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/AudioAttributes;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/AudioAttributes;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
