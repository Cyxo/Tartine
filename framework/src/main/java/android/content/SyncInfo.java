package android.content;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;

    public class SyncInfo implements Parcelable {

    public Account account;
    public String authority;
    public long startTime;

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/SyncInfo;->describeContents()I");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/SyncInfo;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
