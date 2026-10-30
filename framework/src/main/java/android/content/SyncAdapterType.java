package android.content;

import android.os.Parcel;
import android.os.Parcelable;

    public class SyncAdapterType implements Parcelable {

    public static Parcelable.Creator CREATOR;
    public String accountType;
    public String authority;
    public boolean isKey;

    public SyncAdapterType(Parcel arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/SyncAdapterType;-><init>(Landroid/os/Parcel;)V");
    }

    public SyncAdapterType(String arg0, String arg1, boolean arg2, boolean arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/SyncAdapterType;-><init>(Ljava/lang/String;Ljava/lang/String;ZZ)V");
    }

    public boolean allowParallelSyncs() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/SyncAdapterType;->allowParallelSyncs()Z");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/SyncAdapterType;->describeContents()I");
    }

    public boolean equals(Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/SyncAdapterType;->equals(Ljava/lang/Object;)Z");
    }

    public String getSettingsActivity() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/SyncAdapterType;->getSettingsActivity()Ljava/lang/String;");
    }

    public int hashCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/SyncAdapterType;->hashCode()I");
    }

    public boolean isAlwaysSyncable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/SyncAdapterType;->isAlwaysSyncable()Z");
    }

    public boolean isUserVisible() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/SyncAdapterType;->isUserVisible()Z");
    }

    public static SyncAdapterType newKey(String arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/SyncAdapterType;->newKey(Ljava/lang/String;Ljava/lang/String;)Landroid/content/SyncAdapterType;");
    }

    public boolean supportsUploading() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/SyncAdapterType;->supportsUploading()Z");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/SyncAdapterType;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/SyncAdapterType;->writeToParcel(Landroid/os/Parcel;I)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public SyncAdapterType() {
        }
    }
