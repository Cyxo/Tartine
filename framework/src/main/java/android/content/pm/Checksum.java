package android.content.pm;

    public class Checksum implements android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;
    public static final int TYPE_PARTIAL_MERKLE_ROOT_1M_SHA256 = 32;
    public static final int TYPE_PARTIAL_MERKLE_ROOT_1M_SHA512 = 64;
    public static final int TYPE_WHOLE_MD5 = 2;
    public static final int TYPE_WHOLE_MERKLE_ROOT_4K_SHA256 = 1;
    public static final int TYPE_WHOLE_SHA1 = 4;
    public static final int TYPE_WHOLE_SHA256 = 8;
    public static final int TYPE_WHOLE_SHA512 = 16;

    public Checksum(int arg0, byte[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/Checksum;-><init>(I[B)V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/Checksum;->describeContents()I");
    }

    public int getType() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/Checksum;->getType()I");
    }

    public byte[] getValue() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/Checksum;->getValue()[B");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/Checksum;->writeToParcel(Landroid/os/Parcel;I)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public Checksum() {
        }
    }
