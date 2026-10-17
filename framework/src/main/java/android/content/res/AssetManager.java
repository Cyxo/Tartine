package android.content.res;

    public class AssetManager implements java.lang.AutoCloseable {

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

    public java.lang.String[] getLocales() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetManager;->getLocales()[Ljava/lang/String;");
    }

    public java.lang.String[] list(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetManager;->list(Ljava/lang/String;)[Ljava/lang/String;");
    }

    public java.io.InputStream open(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetManager;->open(Ljava/lang/String;)Ljava/io/InputStream;");
    }

    public java.io.InputStream open(java.lang.String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetManager;->open(Ljava/lang/String;I)Ljava/io/InputStream;");
    }

    public android.content.res.AssetFileDescriptor openFd(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetManager;->openFd(Ljava/lang/String;)Landroid/content/res/AssetFileDescriptor;");
    }

    public android.content.res.AssetFileDescriptor openNonAssetFd(int arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetManager;->openNonAssetFd(ILjava/lang/String;)Landroid/content/res/AssetFileDescriptor;");
    }

    public android.content.res.AssetFileDescriptor openNonAssetFd(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetManager;->openNonAssetFd(Ljava/lang/String;)Landroid/content/res/AssetFileDescriptor;");
    }

    public android.content.res.XmlResourceParser openXmlResourceParser(int arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetManager;->openXmlResourceParser(ILjava/lang/String;)Landroid/content/res/XmlResourceParser;");
    }

    public android.content.res.XmlResourceParser openXmlResourceParser(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/res/AssetManager;->openXmlResourceParser(Ljava/lang/String;)Landroid/content/res/XmlResourceParser;");
    }
    }
