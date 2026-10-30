package android.content.pm;

import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Printer;

    public class PackageItemInfo {

    public int banner;
    public int icon;
    public boolean isArchived;
    public int labelRes;
    public int logo;
    public Bundle metaData;
    public String name;
    public CharSequence nonLocalizedLabel;
    public String packageName;

    public PackageItemInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageItemInfo;-><init>()V");
    }

    public PackageItemInfo(PackageItemInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageItemInfo;-><init>(Landroid/content/pm/PackageItemInfo;)V");
    }

    protected PackageItemInfo(Parcel arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageItemInfo;-><init>(Landroid/os/Parcel;)V");
    }

    protected void dumpBack(Printer arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageItemInfo;->dumpBack(Landroid/util/Printer;Ljava/lang/String;)V");
    }

    protected void dumpFront(Printer arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageItemInfo;->dumpFront(Landroid/util/Printer;Ljava/lang/String;)V");
    }

    public Drawable loadBanner(PackageManager arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageItemInfo;->loadBanner(Landroid/content/pm/PackageManager;)Landroid/graphics/drawable/Drawable;");
    }

    public Drawable loadIcon(PackageManager arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageItemInfo;->loadIcon(Landroid/content/pm/PackageManager;)Landroid/graphics/drawable/Drawable;");
    }

    public CharSequence loadLabel(PackageManager arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageItemInfo;->loadLabel(Landroid/content/pm/PackageManager;)Ljava/lang/CharSequence;");
    }

    public Drawable loadLogo(PackageManager arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageItemInfo;->loadLogo(Landroid/content/pm/PackageManager;)Landroid/graphics/drawable/Drawable;");
    }

    public Drawable loadUnbadgedIcon(PackageManager arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageItemInfo;->loadUnbadgedIcon(Landroid/content/pm/PackageManager;)Landroid/graphics/drawable/Drawable;");
    }

    public XmlResourceParser loadXmlMetaData(PackageManager arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageItemInfo;->loadXmlMetaData(Landroid/content/pm/PackageManager;Ljava/lang/String;)Landroid/content/res/XmlResourceParser;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageItemInfo;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
