package android.content.pm;

    public class ConfigurationInfo implements android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;
    public static final int GL_ES_VERSION_UNDEFINED = 0;
    public static final int INPUT_FEATURE_FIVE_WAY_NAV = 2;
    public static final int INPUT_FEATURE_HARD_KEYBOARD = 1;
    public int reqGlEsVersion;
    public int reqInputFeatures;
    public int reqKeyboardType;
    public int reqNavigation;
    public int reqTouchScreen;

    public ConfigurationInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ConfigurationInfo;-><init>()V");
    }

    public ConfigurationInfo(android.content.pm.ConfigurationInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ConfigurationInfo;-><init>(Landroid/content/pm/ConfigurationInfo;)V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ConfigurationInfo;->describeContents()I");
    }

    public java.lang.String getGlEsVersion() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ConfigurationInfo;->getGlEsVersion()Ljava/lang/String;");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ConfigurationInfo;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ConfigurationInfo;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
