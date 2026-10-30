package android.content.res;

import java.io.InputStream;

    public class AssetManager implements AutoCloseable {

    public static final int ACCESS_BUFFER = 3;
    public static final int ACCESS_RANDOM = 1;
    public static final int ACCESS_STREAMING = 2;
    public static final int ACCESS_UNKNOWN = 0;

    public void close() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetManager;->close()V");
    }

    protected void finalize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetManager;->finalize()V");
    }

    public String[] getLocales() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetManager;->getLocales()[Ljava/lang/String;");
    }

    public String[] list(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetManager;->list(Ljava/lang/String;)[Ljava/lang/String;");
    }

    public InputStream open(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetManager;->open(Ljava/lang/String;)Ljava/io/InputStream;");
    }

    public InputStream open(String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetManager;->open(Ljava/lang/String;I)Ljava/io/InputStream;");
    }

    public AssetFileDescriptor openFd(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetManager;->openFd(Ljava/lang/String;)Landroid/content/res/AssetFileDescriptor;");
    }

    public AssetFileDescriptor openNonAssetFd(int arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetManager;->openNonAssetFd(ILjava/lang/String;)Landroid/content/res/AssetFileDescriptor;");
    }

    public AssetFileDescriptor openNonAssetFd(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetManager;->openNonAssetFd(Ljava/lang/String;)Landroid/content/res/AssetFileDescriptor;");
    }

    public XmlResourceParser openXmlResourceParser(int arg0, String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetManager;->openXmlResourceParser(ILjava/lang/String;)Landroid/content/res/XmlResourceParser;");
    }

    public XmlResourceParser openXmlResourceParser(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetManager;->openXmlResourceParser(Ljava/lang/String;)Landroid/content/res/XmlResourceParser;");
    }
    }
