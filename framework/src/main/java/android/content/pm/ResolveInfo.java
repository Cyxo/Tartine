package android.content.pm;

    public class ResolveInfo implements android.os.Parcelable {

    public static android.os.Parcelable.Creator CREATOR;
    public android.content.pm.ActivityInfo activityInfo;
    public android.content.IntentFilter filter;
    public int icon;
    public boolean isDefault;
    public boolean isInstantAppAvailable;
    public int labelRes;
    public int match;
    public java.lang.CharSequence nonLocalizedLabel;
    public int preferredOrder;
    public int priority;
    public android.content.pm.ProviderInfo providerInfo;
    public java.lang.String resolvePackageName;
    public android.content.pm.ServiceInfo serviceInfo;
    public int specificIndex;

    public ResolveInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ResolveInfo;-><init>()V");
    }

    public ResolveInfo(android.content.pm.ResolveInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ResolveInfo;-><init>(Landroid/content/pm/ResolveInfo;)V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ResolveInfo;->describeContents()I");
    }

    public void dump(android.util.Printer arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ResolveInfo;->dump(Landroid/util/Printer;Ljava/lang/String;)V");
    }

    public final int getIconResource() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ResolveInfo;->getIconResource()I");
    }

    public boolean isCrossProfileIntentForwarderActivity() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ResolveInfo;->isCrossProfileIntentForwarderActivity()Z");
    }

    public android.graphics.drawable.Drawable loadIcon(android.content.pm.PackageManager arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ResolveInfo;->loadIcon(Landroid/content/pm/PackageManager;)Landroid/graphics/drawable/Drawable;");
    }

    public java.lang.CharSequence loadLabel(android.content.pm.PackageManager arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ResolveInfo;->loadLabel(Landroid/content/pm/PackageManager;)Ljava/lang/CharSequence;");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ResolveInfo;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ResolveInfo;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
