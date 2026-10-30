package android.content.res.loader;

import android.content.Context;
import android.content.om.OverlayInfo;
import android.os.ParcelFileDescriptor;
import java.io.Closeable;

    public class ResourcesProvider implements AutoCloseable, Closeable {

    public void close() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/loader/ResourcesProvider;->close()V");
    }

    public static ResourcesProvider empty(AssetsProvider arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/loader/ResourcesProvider;->empty(Landroid/content/res/loader/AssetsProvider;)Landroid/content/res/loader/ResourcesProvider;");
    }

    protected void finalize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/loader/ResourcesProvider;->finalize()V");
    }

    public static ResourcesProvider loadFromApk(ParcelFileDescriptor arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/loader/ResourcesProvider;->loadFromApk(Landroid/os/ParcelFileDescriptor;)Landroid/content/res/loader/ResourcesProvider;");
    }

    public static ResourcesProvider loadFromApk(ParcelFileDescriptor arg0, AssetsProvider arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/loader/ResourcesProvider;->loadFromApk(Landroid/os/ParcelFileDescriptor;Landroid/content/res/loader/AssetsProvider;)Landroid/content/res/loader/ResourcesProvider;");
    }

    public static ResourcesProvider loadFromDirectory(String arg0, AssetsProvider arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/loader/ResourcesProvider;->loadFromDirectory(Ljava/lang/String;Landroid/content/res/loader/AssetsProvider;)Landroid/content/res/loader/ResourcesProvider;");
    }

    public static ResourcesProvider loadFromSplit(Context arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/loader/ResourcesProvider;->loadFromSplit(Landroid/content/Context;Ljava/lang/String;)Landroid/content/res/loader/ResourcesProvider;");
    }

    public static ResourcesProvider loadFromTable(ParcelFileDescriptor arg0, AssetsProvider arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/loader/ResourcesProvider;->loadFromTable(Landroid/os/ParcelFileDescriptor;Landroid/content/res/loader/AssetsProvider;)Landroid/content/res/loader/ResourcesProvider;");
    }

    public static ResourcesProvider loadOverlay(OverlayInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/loader/ResourcesProvider;->loadOverlay(Landroid/content/om/OverlayInfo;)Landroid/content/res/loader/ResourcesProvider;");
    }
    }
