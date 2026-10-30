package android.content.pm;

import android.os.Parcel;
import android.util.Printer;

    public class ComponentInfo extends PackageItemInfo {

    public ApplicationInfo applicationInfo;
    public String[] attributionTags;
    public int descriptionRes;
    public boolean directBootAware;
    public boolean enabled;
    public boolean exported;
    public String processName;
    public String splitName;

    public ComponentInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ComponentInfo;-><init>()V");
    }

    public ComponentInfo(ComponentInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ComponentInfo;-><init>(Landroid/content/pm/ComponentInfo;)V");
    }

    protected ComponentInfo(Parcel arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ComponentInfo;-><init>(Landroid/os/Parcel;)V");
    }

    protected void dumpBack(Printer arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ComponentInfo;->dumpBack(Landroid/util/Printer;Ljava/lang/String;)V");
    }

    protected void dumpFront(Printer arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ComponentInfo;->dumpFront(Landroid/util/Printer;Ljava/lang/String;)V");
    }

    public final int getBannerResource() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ComponentInfo;->getBannerResource()I");
    }

    public final int getIconResource() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ComponentInfo;->getIconResource()I");
    }

    public final int getLogoResource() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ComponentInfo;->getLogoResource()I");
    }

    public boolean isEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ComponentInfo;->isEnabled()Z");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/ComponentInfo;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
