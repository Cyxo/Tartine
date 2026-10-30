package android.content.pm;

import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Printer;

    public class ResolveInfo implements Parcelable {

    public static Parcelable.Creator CREATOR;
    public ActivityInfo activityInfo;
    public IntentFilter filter;
    public int icon;
    public boolean isDefault;
    public boolean isInstantAppAvailable;
    public int labelRes;
    public int match;
    public CharSequence nonLocalizedLabel;
    public int preferredOrder;
    public int priority;
    public ProviderInfo providerInfo;
    public String resolvePackageName;
    public ServiceInfo serviceInfo;
    public int specificIndex;

    public ResolveInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ResolveInfo;-><init>()V");
    }

    public ResolveInfo(ResolveInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ResolveInfo;-><init>(Landroid/content/pm/ResolveInfo;)V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ResolveInfo;->describeContents()I");
    }

    public void dump(Printer arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ResolveInfo;->dump(Landroid/util/Printer;Ljava/lang/String;)V");
    }

    public final int getIconResource() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ResolveInfo;->getIconResource()I");
    }

    public boolean isCrossProfileIntentForwarderActivity() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ResolveInfo;->isCrossProfileIntentForwarderActivity()Z");
    }

    public Drawable loadIcon(PackageManager arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ResolveInfo;->loadIcon(Landroid/content/pm/PackageManager;)Landroid/graphics/drawable/Drawable;");
    }

    public CharSequence loadLabel(PackageManager arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ResolveInfo;->loadLabel(Landroid/content/pm/PackageManager;)Ljava/lang/CharSequence;");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ResolveInfo;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ResolveInfo;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
