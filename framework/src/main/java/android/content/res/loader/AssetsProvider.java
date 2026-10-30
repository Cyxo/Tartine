package android.content.res.loader;

import android.content.res.AssetFileDescriptor;

    public interface AssetsProvider {

    public default AssetFileDescriptor loadAssetFd(String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/loader/AssetsProvider;->loadAssetFd(Ljava/lang/String;I)Landroid/content/res/AssetFileDescriptor;");
    }
    }
