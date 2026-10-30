package android.os;

import java.io.InputStream;
import java.io.OutputStream;

    public class PersistableBundle extends BaseBundle implements Cloneable, Parcelable {

    public static Parcelable.Creator CREATOR;
    public static PersistableBundle EMPTY;

    public PersistableBundle() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/PersistableBundle;-><init>()V");
    }

    public PersistableBundle(PersistableBundle arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/PersistableBundle;-><init>(Landroid/os/PersistableBundle;)V");
    }

    public PersistableBundle(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/PersistableBundle;-><init>(I)V");
    }

    public Object clone() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/PersistableBundle;->clone()Ljava/lang/Object;");
    }

    public PersistableBundle deepCopy() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/PersistableBundle;->deepCopy()Landroid/os/PersistableBundle;");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/PersistableBundle;->describeContents()I");
    }

    public PersistableBundle getPersistableBundle(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/PersistableBundle;->getPersistableBundle(Ljava/lang/String;)Landroid/os/PersistableBundle;");
    }

    public void putPersistableBundle(String arg0, PersistableBundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/PersistableBundle;->putPersistableBundle(Ljava/lang/String;Landroid/os/PersistableBundle;)V");
    }

    public static PersistableBundle readFromStream(InputStream arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/PersistableBundle;->readFromStream(Ljava/io/InputStream;)Landroid/os/PersistableBundle;");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/PersistableBundle;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/PersistableBundle;->writeToParcel(Landroid/os/Parcel;I)V");
    }

    public void writeToStream(OutputStream arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/PersistableBundle;->writeToStream(Ljava/io/OutputStream;)V");
    }
    }
