package android.accounts;

import android.os.Parcel;
import android.os.Parcelable;

    public class Account implements Parcelable {

    public static Parcelable.Creator CREATOR;
    public String name;
    public String type;

    public Account(Parcel arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/accounts/Account;-><init>(Landroid/os/Parcel;)V");
    }

    public Account(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/accounts/Account;-><init>(Ljava/lang/String;Ljava/lang/String;)V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/accounts/Account;->describeContents()I");
    }

    public boolean equals(Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/accounts/Account;->equals(Ljava/lang/Object;)Z");
    }

    public int hashCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/accounts/Account;->hashCode()I");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/accounts/Account;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/accounts/Account;->writeToParcel(Landroid/os/Parcel;I)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public Account() {
        }
    }
