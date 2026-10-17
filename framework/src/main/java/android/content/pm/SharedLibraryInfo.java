package android.content.pm;

    public class SharedLibraryInfo implements android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;
    public static final int TYPE_BUILTIN = 0;
    public static final int TYPE_DYNAMIC = 1;
    public static final int TYPE_SDK_PACKAGE = 3;
    public static final int TYPE_STATIC = 2;
    public static final int VERSION_UNDEFINED = -1;

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/SharedLibraryInfo;->describeContents()I");
    }

    public java.util.List<java.lang.String> getCertDigests() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/SharedLibraryInfo;->getCertDigests()Ljava/util/List;");
    }

    public android.content.pm.VersionedPackage getDeclaringPackage() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/SharedLibraryInfo;->getDeclaringPackage()Landroid/content/pm/VersionedPackage;");
    }

    public java.util.List<android.content.pm.VersionedPackage> getDependentPackages() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/SharedLibraryInfo;->getDependentPackages()Ljava/util/List;");
    }

    public long getLongVersion() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/SharedLibraryInfo;->getLongVersion()J");
    }

    public java.lang.String getName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/SharedLibraryInfo;->getName()Ljava/lang/String;");
    }

    public int getType() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/SharedLibraryInfo;->getType()I");
    }

    public int getVersion() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/SharedLibraryInfo;->getVersion()I");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/SharedLibraryInfo;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/SharedLibraryInfo;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
