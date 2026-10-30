package android.media;

import android.os.Parcel;
import android.os.Parcelable;

    public class Rating implements Parcelable {

    public static Parcelable.Creator CREATOR;
    public static final int RATING_3_STARS = 3;
    public static final int RATING_4_STARS = 4;
    public static final int RATING_5_STARS = 5;
    public static final int RATING_HEART = 1;
    public static final int RATING_NONE = 0;
    public static final int RATING_PERCENTAGE = 6;
    public static final int RATING_THUMB_UP_DOWN = 2;

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/Rating;->describeContents()I");
    }

    public float getPercentRating() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/Rating;->getPercentRating()F");
    }

    public int getRatingStyle() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/Rating;->getRatingStyle()I");
    }

    public float getStarRating() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/Rating;->getStarRating()F");
    }

    public boolean hasHeart() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/Rating;->hasHeart()Z");
    }

    public boolean isRated() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/Rating;->isRated()Z");
    }

    public boolean isThumbUp() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/Rating;->isThumbUp()Z");
    }

    public static Rating newHeartRating(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/Rating;->newHeartRating(Z)Landroid/media/Rating;");
    }

    public static Rating newPercentageRating(float arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/Rating;->newPercentageRating(F)Landroid/media/Rating;");
    }

    public static Rating newStarRating(int arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/Rating;->newStarRating(IF)Landroid/media/Rating;");
    }

    public static Rating newThumbRating(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/Rating;->newThumbRating(Z)Landroid/media/Rating;");
    }

    public static Rating newUnratedRating(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/Rating;->newUnratedRating(I)Landroid/media/Rating;");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/Rating;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/Rating;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
