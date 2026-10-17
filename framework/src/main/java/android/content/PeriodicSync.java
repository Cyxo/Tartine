package android.content;

    public class PeriodicSync implements android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;
    public android.accounts.Account account;
    public java.lang.String authority;
    public android.os.Bundle extras;
    public long period;

    public PeriodicSync(android.accounts.Account arg0, java.lang.String arg1, android.os.Bundle arg2, long arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/PeriodicSync;-><init>(Landroid/accounts/Account;Ljava/lang/String;Landroid/os/Bundle;J)V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/PeriodicSync;->describeContents()I");
    }

    public boolean equals(java.lang.Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/PeriodicSync;->equals(Ljava/lang/Object;)Z");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/PeriodicSync;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/PeriodicSync;->writeToParcel(Landroid/os/Parcel;I)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public PeriodicSync() {
        }
    }
