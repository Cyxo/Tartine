package android.content;

import android.accounts.Account;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

    public class PeriodicSync implements Parcelable {

    public static Parcelable.Creator CREATOR;
    public Account account;
    public String authority;
    public Bundle extras;
    public long period;

    public PeriodicSync(Account arg0, String arg1, Bundle arg2, long arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/PeriodicSync;-><init>(Landroid/accounts/Account;Ljava/lang/String;Landroid/os/Bundle;J)V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/PeriodicSync;->describeContents()I");
    }

    public boolean equals(Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/PeriodicSync;->equals(Ljava/lang/Object;)Z");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/PeriodicSync;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/PeriodicSync;->writeToParcel(Landroid/os/Parcel;I)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public PeriodicSync() {
        }
    }
